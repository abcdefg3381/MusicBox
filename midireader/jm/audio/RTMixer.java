/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>

Copyright (C) 2000 Andrew Sorensen & Andrew Brown

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or any
later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 */
package jm.audio;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import jm.music.rt.RTLine;

/**
 * RTMixer is responsible for convolving the audio signals being pulled from n
 * number of RTLines. RTMixer uses a single JMF Java Sound SourceDataLine object
 * for writing the newly convolved signal to the audio device. Buffers of audio
 * sample data are passed to the SourceDataLine at a rate set by the Control
 * Rate value. The contol rate sets the size of the audio buffers used by
 * RTMixer, SourceDataLine and Instrument. Information about sample rate and
 * channels is retrieved from the first audio object of the first instrument of
 * the first RTLine. Therefore, there is an assumption that all instruments have
 * the same sample rate and number of channels.
 * 
 * @author Andrew Sorensen
 * @version 1.0,Sun Feb 25 18:42:43 2001
 */
public class RTMixer implements AudioChainListener {
	/** bos is used to convert sampleArray into a byte stream */
	private ByteArrayOutputStream bos;
	/** buffer size */
	private int bufferSize;
	/** The number of channels to be used when setting up SourceDataLine */
	protected int channels;
	/**
	 * The control rate is used to set how often in instrument returns a full
	 * buffer. This is acheived by changing the size of the instrument buffers
	 */
	protected double controlRate = 0.05;
	/** count shows how many RTLines have passed RTMixer their full buffers */
	private int count = 0;
	/**
	 * A Timer which keeps track of how many samples have been written since
	 * this object started
	 */
	public long currentTime = 0;
	/** dline is the JFM java sound object which we write sampleArray to */
	private SourceDataLine dline;
	/** dos is used to help convert sampleArray into bos */
	private DataOutputStream dos;
	/** RTLines associated with this RTMixer object */
	private RTLine[] rtlines;
	/** sampleArray contains the convolution of all RTLines buffers */
	private float[] sampleArray;
	/** The sampleRate to be used when establishing the JMF SourceDataLine */
	protected int sampleRate;
	/** How far into the score we are in terms of beats */
	private double scorePosition = 0.0;
	// ------------------------------------------
	// Attributes
	// ------------------------------------------
	/** The number of lines in the RTLine array */
	private int totLines = 0;

	// -------------------------------------
	// Constructors
	// -------------------------------------
	/**
	 * The RTMixer constructor sets a number of attributes and opens a JMF java
	 * sound sourceDataLine.
	 * 
	 * @param inst
	 *            the Instruments to be processed by this object.
	 */
	public RTMixer(RTLine[] rtlines) {
		this.rtlines = rtlines;
		this.sampleRate = rtlines[0].getSampleRate();
		this.channels = rtlines[0].getChannels();
		this.bufferSize = (int) (sampleRate * channels * controlRate);
		while (this.bufferSize % 4 != 0) {
			this.controlRate += 0.001;
			this.bufferSize = (int) (sampleRate * channels * controlRate);
		}
		// feed info down to lines and instruments
		for (int i = 0; i < rtlines.length; i++) {
			this.totLines += this.rtlines[i].getNumLines();
			this.rtlines[i].setBufferSize(this.bufferSize);
			// check for matching values
			if (rtlines[i].getSampleRate() != this.sampleRate) {
				System.err.println("jMusic RTMixer error: All instruments must have the same sample rate.");
				System.exit(0);
			}
			if (rtlines[i].getChannels() != this.channels) {
				System.err.println("jMusic RTMixer error: All instruments must have the same number of channels.");
				System.exit(0);
			}
		}

		initJMFSound(bufferSize);
		bos = new ByteArrayOutputStream();
		dos = new DataOutputStream(bos);
	}

	// -------------------------------------
	// Abstract Methods
	// -------------------------------------

	/**
	 * This method passes on external action requests (i.e. gui based action
	 * events) to each RTLines externalAction method).
	 * 
	 * @param obj
	 *            an unspecified object type (externalAction will cast)
	 * @param actionNumber
	 *            an indentifyer for the originator of the action request (i.e.
	 *            if there are three buttons there would actionNumbers 1,2 and
	 *            3)
	 */
	public void actionLines(Object obj, int actionNumber) {
		for (RTLine rtline : rtlines) {
			rtline.externalAction(obj, actionNumber);
		}
		// I need to develop some action listeners for this
	}

	/**
	 * Begin starts RTMixer.
	 */
	public void begin() {
		this.sampleArray = new float[bufferSize];
		for (RTLine rtline : rtlines) {
			rtline.start(this.scorePosition, this);
		}
	}

	// --------------------------------------
	// Public Methods
	// ---------------------------------------
	/**
	 * The controlChange method is called every time an instrument fills a
	 * sample buffer. This method is responsible for receiving the sample buffer
	 * and convolving it with the data in sampleArray.
	 * 
	 * @param buffer
	 *            a sample array filled by an instrument
	 * @param returned
	 *            the number of samples in the buffer
	 * @param finished
	 *            indicates whether the instruments current note is finished or
	 *            not.
	 */
	@Override
	public synchronized void controlChange(float[] buffer, int returned, boolean finished) {
		for (int i = 0; i < returned; i++) {
			sampleArray[i] += buffer[i];
		}
		if (++count == totLines) {
			this.scorePosition += controlRate;
			for (RTLine rtline : this.rtlines) {
				Instrument[] inst = rtline.getInstrument();
				for (Instrument element : inst) {
					element.release();
				}
			}
			count = 0;
			this.writeOutAudio(sampleArray.length);
		}
	}

	@Override
	public void finalize() {
		try {
			dos.close();
			bos.close();
		} catch (IOException e) {
		}
	}

	/**
	 * This method creates an instance of the JMF SourceDataLine object. This
	 * becomes the sink for all sample data leaving jMusic.
	 * 
	 * @param bufferSize
	 *            the size to set the SourceDataLine's buffer size to.
	 */
	private void initJMFSound(int bufferSize) {
		// Set up jmf audio stuff
		// AudioFormat af = new AudioFormat((float)this.sampleRate,16,
		// this.channels,true,true);
		AudioFormat af =
				new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.sampleRate, 16, this.channels, this.channels * 2,
						this.sampleRate, true);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
		// System.out.println("Setting for audio line: "+info);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println(info);
			System.err.println("jMusic RTMixer error: JMF Line not supported. "
					+ "Real time audio must be 16 bit stereo ... exiting .. so there : (");
			System.exit(1);
		}
		try {
			this.dline = (SourceDataLine) AudioSystem.getLine(info);
			// multiply buffersize by 2 because this is bytes not shorts
			this.dline.open(af, bufferSize * 8); // 2 or 4?
			this.dline.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pauses RTMixer playback.
	 */
	public void pause() {
		for (RTLine rtline : rtlines) {
			rtline.pause();
		}
	}

	/**
	 * Continues the RTMixer playback.
	 */
	public void unPause() {
		for (RTLine rtline : rtlines) {
			rtline.unPause();
		}
	}

	// -----------------------------------------
	// Private Methods
	// -----------------------------------------
	/**
	 * This method writes out the convolved sampleArray to the SourceDataLine
	 * 
	 * @param length
	 *            the number of samples to write
	 */
	private void writeOutAudio(final int length) {
		bos.reset();
		for (int i = 0; i < length; i++) {
			// scale to avoid clipping
			if (this.totLines > 1) {
				this.sampleArray[i] = this.sampleArray[i] / (this.totLines * 0.75f);
			}
			try {
				dos.writeShort((short) (this.sampleArray[i] * 32767));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			this.sampleArray[i] = (float) 0.0;
		}
		int returned = this.dline.write(bos.toByteArray(), 0, bos.size());
		this.currentTime += length;
	}
}
