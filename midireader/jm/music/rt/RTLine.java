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
You should have received a copy of the GNU General Public Licens
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package jm.music.rt;

import jm.audio.AOException;
import jm.audio.AudioChainListener;
import jm.audio.Instrument;
import jm.audio.RTMixer;
import jm.constants.Pitches;
import jm.music.data.Note;

/**
 * This class frovides a framework for real time audio playback. It renders a
 * Part using the provided instrument(s). If there are more than one instrument
 * it is assumed they are simply multiple instances of the one instrument. These
 * are used to play notes that overlap in the score or as a result of effects
 * such as delays or reverb. There is an assumption that all instruments have
 * the same sample rate number of channels.
 * 
 * @author Andrew Sorensen
 * @version 1.0,Sun Feb 25 18:43:31 2001
 */

public abstract class RTLine implements AudioChainListener {
	/** if clear is true the threads wait call will be skipped */
	protected boolean clear = false;
	// --------------------------------------
	// Attributes
	// --------------------------------------
	/** The instrument associated with this real time line */
	protected Instrument[] inst;
	/** This value is equal to (sampleRate * channels) */
	private double size;
	/** tempo value */
	private double tempo = 60.0;
	/** test position ???? */
	private double testpos;

	// --------------------------------------
	// Constructors
	// --------------------------------------
	/**
	 * The RTLine constructor is primarily responsible for initialising its
	 * associated instrument and the instruments audio chain.
	 */
	public RTLine(Instrument[] inst) {
		this.inst = inst;
		for (Instrument element : inst) {
			element.addRTLine(this);
		}
		// check that all inst sample rates are the same
		int sr = inst[0].getSampleRate();
		int ch = inst[0].getChannels();
		for (Instrument element : inst) {
			if (element.getSampleRate() != sr) {
				System.err.println("jMusic RTLine error: All instruments" + " must have the same sample rate.");
				System.exit(0);
			}
			if (element.getChannels() != ch) {
				System.err.println("jMusic RTLine error: All instruments" + " must have the same number of channels.");
				System.exit(0);
			}
		}
		this.size = sr * ch;
	}

	/**
	 * controlChange is called by RTLine's instrument everytime it completes
	 * filling its current buffer. The regularity at which controlChange is
	 * called is what determines the control rate. The control rate is used
	 * therefore to set the buffer sizes of the instrument sample buffers.
	 * 
	 * @param buffer
	 *            a buffer of samples passed from an instrument
	 * @param returned
	 *            the number of samples in the buffer
	 * @param finished
	 *            this boolean indicates whether the instrument has finished
	 *            processing its current note.
	 */
	@Override
	public synchronized void controlChange(float[] buffer, int returned, boolean finished) {
		// do nothing here unless overriden
	}

	/**
	 * External action is called by RTMixer whenever an external event is sent
	 * to trigger a real time audio event. The event will commonly be triggered
	 * by a GUI widget such as a button or slider.
	 * 
	 * @param obj
	 *            and undetermined Object which will need to be cast locally to
	 *            whatever type is expected.
	 * @param actionNumber
	 *            is an serial index value for the source of the event.
	 */
	public void externalAction(Object obj, int actionNumber) {
		// Is there anything we want done by default ??
	}

	/**
	 * Return the current number of channels, based on the channels of the
	 * instruments. All instruments in real time audio must have the same number
	 * of channels.
	 */
	public int getChannels() {
		return inst[0].getChannels();
	}

	// --------------------------------------
	// Public Methods
	// --------------------------------------
	/**
	 * Returns the instrument associated with this RTLine
	 * 
	 * @return Instrument the instrument associated with this RTLine
	 */
	public Instrument[] getInstrument() {
		return inst;
	}

	/**
	 * Override this method to set the next method to be called.
	 */
	public abstract Note getNote();

	/** Return the number of lines (note polyphony for this RTLine */
	public int getNumLines() {
		return inst.length;
	}

	/**
	 * Return the current samplerate, based on the sample rate of the
	 * instruments. All instrument sample rates in real time audio must be the
	 * same.
	 */
	public int getSampleRate() {
		return inst[0].getSampleRate();
	}

	/**
	 * This method is called from Instrument and in return calls that
	 * instrument's renderNote method. When this method returns, the instruments
	 * iterateChain method is called and the note is processed. This method is
	 * responsible for either fetching a "playable" note from the getNote()
	 * method or else for inserting a rest of an appropriate amount of time.
	 */
	public void instNote(Instrument inst, long samplesProcessed) {
		Note note = null;
		double scorePos = (samplesProcessed) / size;
		double temp = 60.0 / this.tempo;
		if (scorePos > (testpos - 0.001) && scorePos < (testpos + 0.001)) {
			note = getNote().copy();
			note.setRhythmValue(note.getRhythmValue() * temp);
			note.setDuration(note.getDuration() * temp);
			testpos += note.getRhythmValue();
		} else {
			note = new Note(Pitches.REST, (testpos - scorePos));
			note.setRhythmValue(note.getRhythmValue() * temp);
			note.setDuration(note.getRhythmValue());
		}
		inst.renderNote(note, scorePos);
	}

	/*
	 * Halt the playback.
	 */
	public void pause() {
		for (Instrument element : inst) {
			element.pause();
		}
	}

	/**
	 * Specify the buffer size for each instrument. Called by RTMixer.
	 */
	public void setBufferSize(int buf) {
		for (Instrument element : inst) {
			element.setBufSize(buf);
		}
	}

	/** sets the tempo of this RTLine */
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	/**
	 * start this RTLines instrument call for the setting of the first note
	 */
	public void start(double scorePosition, RTMixer rta) {
		for (Instrument element : inst) {
			try {
				if (element.getInitialised() == false) {
					element.createChain();
					element.setInitialised(true);
				}
				// inst[i].setBufSize(bufferSize);
				element.addAudioChainListener(rta);
			} catch (AOException aoe) {
				aoe.printStackTrace();
			}
		}
		for (Instrument element : inst) {
			element.start();
		}
	}

	/*
	 * Continue the playback.
	 */
	public void unPause() {
		for (Instrument element : inst) {
			element.unPause();
		}
	}
}
