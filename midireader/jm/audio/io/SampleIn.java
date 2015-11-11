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

package jm.audio.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import jm.audio.AOException;
import jm.audio.AudioObject;
import jm.audio.Instrument;

/**
 * Audio file reading audio object for jMusic. The class utilises the JavaSound
 * file reading classes. It can be used as the primary audio object in a chain
 * to generate audio sample data for playabck and or later processing.
 * 
 * @author Andrew Sorensen and Andrew Brown (at the same time ;)
 */

public class SampleIn extends AudioObject implements jm.JMC {
	/** Endianess */
	private boolean bigEndian;
	/** Should we be cacheing the files audio data */
	private boolean cache;
	/** Duration of the sound file in samples */
	private long duration;
	private File file;
	private AudioFileFormat fileFormat;
	/* The file format name, e.g. WAV, AIFF, AU */
	private String fileType;
	private AudioFormat format;
	/** Input Stream */
	private InputStream is;
	/** Loop this audio file how many times ? */
	private int loop;
	/** number of times to loop */
	private int loopCount;
	/**
	 * The sample locations between which to repeat and the current position in
	 * the stream. This number is independent of channels - i.e. 44100 = 1 sec
	 * audio regarless of channels
	 */
	private int loopStart, loopEnd, streamPosition;
	/**
	 * The numnber of bits that represent one sample, e.g. 1 = 8 bits, 2 = 16
	 * bits etc.
	 */
	private int sampleSize;
	/** Play a whole file, or only note length? */
	private boolean wholeFile = false;

	/**
	 * Read in the data from the specified file as input to an audio ren dering
	 * process.
	 * 
	 * @param fileName
	 *            - The name of the file to be used.
	 * @param fileName
	 *            - The name of the file to be used.
	 */
	public SampleIn(Instrument inst, String fileName) {
		this(inst, fileName, false); // should be false - hack!!!
	}

	/**
	 * Read in the data from the specified file as input to an audio ren dering
	 * process.
	 * 
	 * @param inst
	 *            - The instrument for which this audio object is a part.
	 * @param fileName
	 *            - The name of the file to be used.
	 * @param cache
	 *            - A flag to say weather or not to hold sample data in memory.
	 */
	public SampleIn(Instrument inst, String fileName, boolean cache) {
		this(inst, fileName, cache, false);
	}

	/**
	 * Read in the data from the specified file as input to an audio ren dering
	 * process.
	 * 
	 * @param inst
	 *            - The instrument for which this audio object is a part.
	 * @param fileName
	 *            - The name of the file to be used.
	 * @param cache
	 *            - A flag to say weather or not to hold sample data in memory.
	 * @param wholeFile
	 *            - A flag to indicate weather the file should be played all the
	 *            way through, regardless of the note duration.
	 */
	public SampleIn(Instrument inst, String fileName, boolean cache, boolean wholeFile) {
		this(inst, fileName, cache, wholeFile, 0);
	}

	/**
	 * Read in the data from the specified file as input to an audio ren dering
	 * process.
	 * 
	 * @param inst
	 *            - The instrument for which this audio object is a part.
	 * @param fileName
	 *            - The name of the file to be used.
	 * @param cache
	 *            - A flag to say weather or not to hold sample data in memory.
	 * @param wholeFile
	 *            - A flag to indicate weather the file should be played all the
	 *            way through, regardless of the note duration.
	 * @param loop
	 *            - The number of time to reapeatedly playback the file (0 is no
	 *            loop, -1 is infinite).
	 */
	public SampleIn(Instrument inst, String fileName, boolean cache, boolean wholeFile, int loop) {
		this(inst, fileName, cache, wholeFile, 0, 0, 0);
	}

	/**
	 * Read in the data from the specified file as input to an audio ren dering
	 * process.
	 * 
	 * @param inst
	 *            - The instrument for which this audio object is a part.
	 * @param fileName
	 *            - The name of the file to be used.
	 * @param cache
	 *            - A flag to say weather or not to hold sample data in memory.
	 * @param wholeFile
	 *            - A flag to indicate weather the file should be played all the
	 *            way through, regardless of the note duration.
	 * @param loop
	 *            - The number of time to reapeatedly playback the file (0 is no
	 *            loop, -1 is infinite).
	 * @param loopStart
	 *            - The sample from which to start looping.
	 * @param loopEnd
	 *            - The sample at which to end the loop.
	 */
	public SampleIn(Instrument inst, String fileName, boolean cache, boolean wholeFile, int loop, int loopStart, int loopEnd) {
		super(inst, 0, "[SampleIn]");
		try {
			this.file = new File(fileName);
			this.cache = cache;
			this.wholeFile = wholeFile;
			this.loop = loop;
			this.loopStart = loopStart;
			this.loopEnd = loopEnd;
			if (this.loop == -1) {
				this.loop = Integer.MAX_VALUE;
			}
			this.fileFormat = AudioSystem.getAudioFileFormat(this.file);
			this.format = this.fileFormat.getFormat();
			bigEndian = this.format.isBigEndian();
			channels = format.getChannels();
			sampleRate = (int) format.getSampleRate();
			this.duration = (long) this.fileFormat.getFrameLength() * this.channels;
			// System.out.println("duration = " + duration + " sampleSize = " +
			// sampleSize);
			// if(wholeFile) this.duration = Long.MAX_VALUE;
			this.sampleSize = (format.getSampleSizeInBits()) / 8;
			fileType = fileFormat.toString();
			this.is = AudioSystem.getAudioInputStream(this.file);
			if (this.cache) {
				byte[] tmp = new byte[(int) this.duration * this.sampleSize];
				this.is.read(tmp);
				this.is.close();
				this.is = new ByteArrayInputStream(tmp);
			}
		} catch (UnsupportedAudioFileException uafe) {
			// ??
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Set up for the next note rendering.
	 */
	@Override
	public void build() {
		// this.finished = false;
		// if(wholeFile)this.finished = false;
		if (!wholeFile) {
			this.duration = (long) (currentNote.getDuration() * this.sampleRate * this.channels);
		}
		this.loopCount = this.loop;
		reset(0);
	}

	@Override
	public void finalize() {
		try {
			this.is.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Provide the bit size of the current audio file.
	 * 
	 * @return The bit depth, 8, 16, 24, or 32.
	 */
	public int getBitResolution() {
		int depth = -1;
		switch (this.sampleSize) {
		case 1:
			depth = 8;
			break;
		case 2:
			depth = 16;
			break;
		case 3:
			depth = 24;
			break;
		case 4:
			depth = 32;
			break;
		}
		return depth;
	}

	/**
	 * Provide the sampleSize value of this file as the number of bytes per
	 * sample. Deprictaed in favour of getSampleSize.
	 * 
	 * @return The number of bytes per sample, 1 = 8 bit, 2 = 16 bit, etc.
	 */
	public int getBits() {
		return this.sampleSize;
	}

	/**
	 * BigEndian conversion
	 */
	private float getFloat(byte[] b) {
		float sample = 0.0f;
		int ret = 0;
		int length = b.length;
		for (int i = 0; i < b.length; i++, length--) {
			ret |= ((b[i] & 0xFF) << ((((bigEndian) ? length : (i + 1)) * 8) - 8));
		}
		switch (sampleSize) {
		case 1:
			if (ret > 0x7F) {
				ret = ~ret + 1;
				ret &= 0x7F;
				ret = ~ret + 1;
			}
			sample = (float) ret / (float) Byte.MAX_VALUE;
			break;
		case 2:
			if (ret > 0x7FFF) {
				ret = ~ret + 1;
				ret &= 0x7FFF;
				ret = ~ret + 1;
			}
			sample = (float) ret / (float) Short.MAX_VALUE;
			break;
		case 3:
			if (ret > 0x7FFFFF) {
				ret = ~ret + 1;
				ret &= 0x7FFFFF;
				ret = ~ret + 1;
			}
			sample = ret / 8388608f;
			break;
		case 4:
			sample = (float) ret / (float) Integer.MAX_VALUE;
			break;
		default:
			System.err.println("Format not accepted");
		}
		return sample;
	}

	/**
	 * Return the length of the sample in Bytes.
	 */
	public int getNumOfBytes() {
		return (int) (this.duration * this.sampleSize);
	}

	/**
	 * Provide the sampleSize value of this file as the number of bytes per
	 * sample.
	 * 
	 * @return The number of bytes per sample, 1 = 8 bit, 2 = 16 bit, etc.
	 */
	public int getSampleSize() {
		return this.sampleSize;
	}

	/**
	 * Returns the number of samples in one track of the file. i.e., the wave
	 * length, or size.
	 */
	public int getWaveSize() {
		return (int) (duration / channels);
	}

	/**
	 * Set the sample read location back to the beginning of the file.
	 */
	public void reset(int spot) {
		this.streamPosition = 0;
		try {
			if (cache) {
				this.is.reset();
			} else {
				this.is = AudioSystem.getAudioInputStream(this.file);
				if (spot > 0) {
					this.is.read(new byte[spot * this.sampleSize * this.channels]);
				}
			}
		} catch (UnsupportedAudioFileException uafe) {
			System.out.println("jMusic SampleIn error: This file format is not supported.");
			System.exit(0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void setLoopEnd(int newPos) {
		this.loopEnd = newPos;
	}

	/**
	 * Specify the starting point for the audio loop. The number of loops will
	 * need to be set to be greater than 0 for the loop to go.
	 */
	public void setLoopStart(int newPos) {
		this.loopStart = newPos;
	}

	/**
	 * Specify weather or not to read the whole file before finishing. If not,
	 * then the note length will determine the amount of data read.
	 * 
	 * @param val
	 *            - The new boolean value.
	 */
	public void setWholeFile(boolean val) {
		this.wholeFile = val;
	}

	/**
	 * Read in samples from the file and pass it down the audio chain. The input
	 * to this method is bogus as it is always the first in the audio chain and
	 * therefore receives no input.
	 * 
	 * @param input
	 *            bogus input here to fit in.
	 */
	@Override
	public int work(float[] buffer) throws AOException {
		// System.out.println("SampleIn is working ...");
		this.finished = false;
		float sample = 0f;
		byte[] tmp = new byte[sampleSize];
		for (int i = 0; i < buffer.length; i++) {
			try {
				if (is.read(tmp) == -1) {
					this.finished = true;
				} else {
					buffer[i] = this.getFloat(tmp);
					if (++this.streamPosition == loopStart && loop > 0) {
						this.is.mark(loopStart);
					} else if (this.streamPosition == loopEnd && loop > 0) {
						if (--loopCount >= 1) {
							this.reset(loopStart);
							this.streamPosition = loopStart;
						}
					}
					if (this.streamPosition >= this.duration) {
						this.finished = true;
					}
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return buffer.length;
	}
}
