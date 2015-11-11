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
package jm.music.data;

import java.io.Serializable;

import jm.constants.Frequencies;

/**
 * The Note class is representative of notes in standard western music notation.
 * It contains data relavent to music note information such as time, duration
 * and pitch. Notes get contained in Phrase objects. Like in tradtional music
 * notation (CPN) notes get played one after the other in the order in which
 * they are added to the Phrase. <br>
 * !IMPORTANT: notes with a pitch of the minimum integer are rests, those
 * between 0 <> 127 are exactly the same as normal sounding notes numbered like
 * the MIDI specification. Notes with a pitch specified as a double value, e.g.,
 * 440.0, are frequency values for the note pitch. In general, note pitch refers
 * to the chromatic key number (as per MIDI) as an int and the note frequency
 * refer to the value in hertz as a double value. <br>
 * Notes can be added to a phrase like this...
 * 
 * <pre>
 * Phrase phrase = new Phrase(0.0);
 * Note note = new Note(C4, CROTCHET, MF);
 * phrase.addNote(note);
 * </pre>
 * 
 * The note also has the option to create notes with reasonable default values.
 * This allows a user who is not interested in any performance details to work
 * with notes solely using pitch value and rythmValue like this....
 * 
 * <pre>
 * Note note = new Note(C4, CROTCHET);
 * </pre>
 * 
 * The final option for building a note also includes the dynamic parameter for
 * minimal performace information.
 * 
 * <pre>
 * Note note = new Note(C4, CROTCHET, F);
 * </pre>
 * 
 * <B>Comments about the offset parameter:</B> The intention of offset was to
 * allow for 'feel' in score playback. For example, a snare drum might be played
 * slightly ahead or behind the beat, or a sound with a slow attack might need
 * to be triggered early in order to sound in time with other parts.
 * 
 * With this in mind, offset should have no influence on rhythmValue or duration
 * calculations within the jMusic data structure, only when translated to
 * another format (eg MIDI). In your example (offset 0.1, duration 0.8, and rv
 * 1.0) the rendering (MIDI or other that does not support the offset concept)
 * should consider the start time of the note 0.1 bpm later than 'normal' but
 * the duration of the note will be 0.8 bpm from that offset start time. The
 * next note should start 1.0 bpm after the 'normal' startTime (ignoring
 * offset). This allows for offset to be used in cases where performance of
 * music varies from beat to beat, eg., the downbeat is anticipated slightly. In
 * extreme cases this could cause overlapping notes in MIDI translation which
 * need to be 'handled' but we consider this a weakness of the MIDI data
 * specification rather than a problem for jMusic : )
 * 
 * In short, think of offset as "A displacement from the normal start time of a
 * note that allows performance interpretation without distorting the score for
 * compositional or analysis purposes." ---------------
 * 
 * @see Phrase
 * @author Andrew Sorensen
 * @version 1.0,Sun Feb 25 18:43:31 2001
 */

public class Note implements Cloneable, Serializable {
	// ----------------------------------------------
	// Defaults
	// -----------------------------------------------

	/** Specified envelope break point value indexes */
	public static final int AMP_ENV = 0;

	/**
	 * Default articulation. This field is meant to replace
	 * DEFAULT_DURATION_MULTIPLIER.
	 */
	public static final double DEFAULT_ARTICULATION = 0.9;

	/** default duration multiplier */
	// should be deprecated in favour of the more musically meaning term
	// DEFAULT_ARTICULATION
	public static final double DEFAULT_DURATION_MULTIPLIER = 0.9;

	/** default dynamic */
	public static final int DEFAULT_DYNAMIC = 85;

	/** default offset value */
	public static final double DEFAULT_OFFSET = 0.0;

	/** default pan value */
	public static final double DEFAULT_PAN = 0.5;

	/** default pitch value */
	public static final int DEFAULT_PITCH = 60;

	/** default rhythmValue */
	public static final double DEFAULT_RHYTHM_VALUE = 1.0;

	/** default duration */
	public static final double DEFAULT_DURATION = DEFAULT_RHYTHM_VALUE * DEFAULT_DURATION_MULTIPLIER;

	/** The number of seconds into a sample to begin reading data */
	public static final double DEFAULT_SAMPLE_START_TIME = 0.0;

	// ----------------------------------------------
	// Constants
	// -----------------------------------------------

	public static final int FILTER_ENV = 2;
	/** Pitch type flag - indicates that pitch should be a frequency in hertz. */
	public static final boolean FREQUENCY = true;
	/** The largest value for a note's duration. */
	public final static double MAX_DURATION = Double.MAX_VALUE;
	/** The largest value for a dynamic. */
	public final static int MAX_DYNAMIC = 127;
	/** The largest value for a pitch. */
	public final static double MAX_MIDI_PITCH = 127.0;
	/** The largest pan value for a note. */
	public final static double MAX_PAN = Double.MAX_VALUE;
	/** The largest value for a pitch. */
	public final static int MAX_PITCH = 127;
	/** The largest value for a rhythValue. */
	public final static double MAX_RHYTHM_VALUE = Double.MAX_VALUE;
	/** Pitch type flag - indicates that pitch should be MIDI note number. */
	public static final boolean MIDI_PITCH = false;
	/** The smallest value for a note's duration. */
	public final static double MIN_DURATION = 0.0;
	/** The smallest value for a dynamic. */
	public final static int MIN_DYNAMIC = 0;
	/** The smallest value for a frequency. */
	public final static double MIN_FREQUENCY = 0.00000000000000001;
	/** The smallest pan value for a note. */
	public final static double MIN_PAN = 0.0;
	/** The smallest value for a pitch. */
	public final static int MIN_PITCH = 0;
	/** The smallest value for a rhythmValue. */
	public final static double MIN_RHYTHM_VALUE = 0.0;
	public static final int PAN_ENV = 3;
	public static final int PITCH_ENV = 1;
	/** The pitch value which indicates a rest. */
	public static final int REST = Integer.MIN_VALUE;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8376546571036759093L;

	/**
	 * Convert a frequency into a MIDI note pitch. Assumes A440 and equal
	 * tempered intonation. Adapted from C code written by Andrew Botros.
	 * 
	 * @param freq
	 *            The frequency value to convert.
	 * @return int The MIDI pitch number closest to the input frequency.
	 */
	public static int freqToMidiPitch(double freq) {
		/* input frequency must be between A0 and A9 */
		if ((freq < 26.73) || (freq > 14496.0)) {
			System.err.println("freqToMidiPitch error: " + "Frequency " + freq + " is not within the MIDI note range.");
			return -1;
		}
		// A semitone higher than a given frequency
		// is 2^(1/12) times the frequency.
		double r = Math.pow(2, 1.0 / 12.0);
		// A cent higher than a given frequency
		// is 2^(1/1200) times the frequency
		double cent = Math.pow(2, 1.0 / 1200.0);
		int r_index = 0;
		int cent_index = 0;
		int side;
		/*
		 * search for input ratio against A4 to the nearest cent in range -49 to
		 * +50 cents around closest note
		 */
		double referenceFreq = 440.0;
		if (freq >= referenceFreq) {
			while (freq > r * referenceFreq) {
				referenceFreq = r * referenceFreq;
				r_index++;
			}
			while (freq > cent * referenceFreq) {
				referenceFreq = cent * referenceFreq;
				cent_index++;
			}
			if ((cent * referenceFreq - freq) < (freq - referenceFreq)) {
				cent_index++;
			}
			if (cent_index > 50) {
				r_index++;
				cent_index = 100 - cent_index;
			}
		} else {
			while (freq < referenceFreq / r) {
				referenceFreq = referenceFreq / r;
				r_index--;
			}
			while (freq < referenceFreq / cent) {
				referenceFreq = referenceFreq / cent;
				cent_index++;
			}
			if ((freq - referenceFreq / cent) < (referenceFreq - freq)) {
				cent_index++;
			}
			if (cent_index >= 50) {
				r_index--;
				cent_index = 100 - cent_index;
			}
		}

		return 69 + r_index;
	}

	/**
	 * Calculate the frequency in hertz of a MIDI note pitch. Assumes an A440.0
	 * reference and equal tempered intonation. Written by Andrew Brown based on
	 * C code by Andrew Botros.
	 * 
	 * @param midiPitch
	 *            The note pitch value to convert.
	 * @return double The frequency equivalent in cycles per second.
	 */
	public static double midiPitchToFreq(int midiPitch) {
		// range OK
		if (midiPitch < 0 || midiPitch > 127) {
			System.err
					.println("jMusic Note.midiPitchToFreq error:" + "midiPitch of " + midiPitch + " is out side valid range.");
			return -1.0;
		}
		// A semitone higher than a given frequency
		// is 2^(1/12) times the frequency.
		double r = Math.pow(2, 1.0 / 12.0);
		int pitchOffset = midiPitch - 69;
		double freq = 440.0;
		if (midiPitch > 69) {
			for (int i = 69; i < midiPitch; i++) {
				freq = freq * r;
			}
		} else {
			for (int i = 69; i > midiPitch; i--) {
				freq = freq / r;
			}
		}
		// rounding to get more reasonable values
		freq = Math.round(freq * 1000.0) / 1000.0;

		return freq;
	}

	/**
	 * An array of break point envelope values to be used by this note - see
	 * Note constants for specified index allocations
	 */
	private double[][] breakPoints = new double[64][];
	/** A notes duration of time in beats */
	private double duration;
	/** Dynamic value ranging from 0-127 (0 = off; 1 = quiet; 127=loud) */
	private int dynamic;
	/** The phrase that this note has been added to. */
	private Phrase myPhrase = null;
	/**
	 * A notes offset time. How far in front or behind of the rhythmic value
	 * should we place this note
	 */
	private double offset;
	/** 0.0 (full left) 0.5 (center) 1.0 (full right) */
	private double pan;
	// ----------------------------------------------
	// Attributes
	// ----------------------------------------------
	/** Pitch/frequency value of the note */
	private double pitch;
	/**
	 * The type of value that pitch is representing - frequency (true) or MIDI
	 * note (false)
	 */
	private boolean pitchType;

	/**
	 * The length of the note. Constant values representing standard note
	 * lengths are defined in JMC
	 */
	private double rhythmValue;

	/**
	 * The time into a sample for playback to begin in milliseconds - for audio
	 * only
	 */
	private double sampleStartTime;

	/**
	 * Default constructor assigns null values to all note attributes
	 */
	public Note() {
		this(DEFAULT_PITCH, DEFAULT_RHYTHM_VALUE);
		this.pitch = DEFAULT_PITCH;
		this.pitchType = MIDI_PITCH;
		this.rhythmValue = DEFAULT_RHYTHM_VALUE;
		this.dynamic = DEFAULT_DYNAMIC;
		this.pan = DEFAULT_PAN; // centre pan
		this.duration = DEFAULT_DURATION;
		this.offset = DEFAULT_OFFSET;
	}

	/**
	 * Assigns frequency and rhythmic values to the note object upon creation
	 * 
	 * @param frequency
	 *            Pitch in hertz, any double value (A4 = 400)
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 */
	public Note(double frequency, double rhythmValue) {
		this(frequency, rhythmValue, DEFAULT_DYNAMIC);
	}

	/**
	 * Assigns frequency and rhythmic values to the note object upon creation
	 * 
	 * @param frequency
	 *            Pitch in hertz, any double value (e.g., A4 = 440.0)
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 * @param dynamic
	 *            range is 0-127 (0 = off; 127 = loud): Constant values
	 *            representing some basic dynamic types can be found in JMC
	 */
	public Note(double frequency, double rhythmValue, int dynamic) {
		this(frequency, rhythmValue, dynamic, DEFAULT_PAN);
	}

	/**
	 * Assigns frequency and rhythmic values to the note object upon creation
	 * 
	 * @param frequency
	 *            Pitch in hertz, any double value (e.g., A4 = 440.0)
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 * @param dynamic
	 *            range is 0-127 (0 = off; 127 = loud): Constant values
	 *            representing some basic dynamic types can be found in JMC
	 * @param pan
	 *            Specifies the balance between output channels; usually between
	 *            0 - left, and 1 - right.
	 */
	public Note(double frequency, double rhythmValue, int dynamic, double pan) {
		if (frequency > MIN_FREQUENCY) {
			this.pitch = frequency;
		} else {
			System.err.println("jMusic Note constructor error: Frequency is " + frequency + ", it must be greater than "
					+ MIN_FREQUENCY + " hertz.");
			System.exit(1);
		}
		this.pitchType = FREQUENCY;
		this.rhythmValue = rhythmValue;
		this.dynamic = (dynamic < MIN_DYNAMIC) ? MIN_DYNAMIC : ((dynamic > MAX_DYNAMIC) ? MAX_DYNAMIC : dynamic);
		this.pan = pan;
		this.duration = rhythmValue * DEFAULT_DURATION_MULTIPLIER;
		this.offset = DEFAULT_OFFSET;
		// this.sampleStartTime = DEFAULT_SAMPLE_START_TIME;
	}

	/**
	 * Assigns pitch and rhythmic values to the note object upon creation Other
	 * values (e.g. dynamic) are given reasonable defaults
	 * 
	 * @param MIDI
	 *            pitch range is 0-127 (middle c = 60): Constant values
	 *            representing pitch values can be found in JMC
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 */
	public Note(int pitch, double rhythmValue) {
		this(pitch, rhythmValue, DEFAULT_DYNAMIC);
	}

	/**
	 * Assigns pitch and rhythmic values to the note object upon creation Other
	 * values (e.g. dynamic) and given reasonable defaults
	 * 
	 * @param MIDI
	 *            pitch range is 0-127 (middle c = 60): Constant values
	 *            representing pitch values can be found in JMC
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 * @param dynamic
	 *            range is 0-127 (0 = off; 127 = loud): Constant values
	 *            representing some basic dynamic types can be found in JMC
	 */
	public Note(int pitch, double rhythmValue, int dynamic) {
		this(pitch, rhythmValue, dynamic, DEFAULT_PAN);
	}

	/**
	 * Assigns pitch and rhythmic values to the note object upon creation Other
	 * values (e.g. dynamic) and given reasonable defaults
	 * 
	 * @param MIDI
	 *            pitch range is 0-127 (middle c = 60): Constant values
	 *            representing pitch values can be found in JMC
	 * @param rhythmValue
	 *            0.5 = quaver: constant values representing most duration types
	 *            can be found in JMC
	 * @param dynamic
	 *            range is 0-127 (0 = off; 127 = loud): Constant values
	 *            representing some basic dynamic types can be found in JMC
	 * @param pan
	 *            Specifies the balance between output channels; usually between
	 *            0 - left, and 1 - right.
	 */
	public Note(int pitch, double rhythmValue, int dynamic, double pan) {
		if (pitch < MIN_PITCH && pitch > REST + 2) {
			// bit of a hack to cater for casting error
			System.err.println("jMusic Note constructor error: Pitch is" + " " + pitch + ", it must be no less than "
					+ MIN_PITCH + " (REST = " + REST + ")");
			System.exit(1);
		}
		this.pitchType = MIDI_PITCH;
		this.pitch = pitch;
		this.rhythmValue = rhythmValue;
		this.dynamic = (dynamic < MIN_DYNAMIC) ? MIN_DYNAMIC : ((dynamic > MAX_DYNAMIC) ? MAX_DYNAMIC : dynamic);
		this.pan = pan;
		this.duration = rhythmValue * DEFAULT_DURATION_MULTIPLIER;
		this.offset = DEFAULT_OFFSET;
		// this.sampleStartTime = DEFAULT_SAMPLE_START_TIME;
	}

	/**
	 * Returns a copy of this note
	 * 
	 * @return Note
	 */
	public Note copy() {
		Note note;
		if (pitchType == MIDI_PITCH) {
			note = new Note(this.getPitch(), this.rhythmValue, this.dynamic);
		} else {
			note = new Note(this.getFrequency(), this.rhythmValue, this.dynamic);
		}
		note.setPan(this.pan);
		note.setDuration(this.duration);
		note.setOffset(this.offset);
		note.setSampleStartTime(this.sampleStartTime);
		note.setMyPhrase(this.myPhrase);
		for (int i = 0; i < breakPoints.length; i++) {
			if (this.breakPoints[i] != null) {
				note.setBreakPoints(i, this.getBreakPoints(i));
			}
		}
		return note;
	}

	/**
	 * Retrieve the break point envelope values. Some indexes of the breakPoints
	 * array are reserved for specific purposes. See the Note constants for a
	 * list of these.
	 * 
	 * @return double[] The break oint values for the specified index
	 */
	public double[] getBreakPoints(int index) {
		if (index < 0 || index > breakPoints.length) {
			System.err.println("jMusic Note error: BreakPoint index " + index + "is out of range when getting.");
			System.exit(1);
		}
		if (breakPoints[index] == null) {
			System.err.println("jMusic Note error: Breakpoint index " + index + " is empty.");
			System.exit(1);
		}
		return this.breakPoints[index];
	}

	/**
	 * Return note duration. 1.0 = Crotchet (Quater Note), 0.5 = Quaver (Eighth
	 * Note), etc...
	 * 
	 * @return double note's duration
	 */
	public double getDuration() {
		return this.duration;
	}

	/**
	 * Retrieve notes dynamic
	 * 
	 * @return int notes dynamic
	 */
	public int getDynamic() {
		return this.dynamic;
	}

	/**
	 * Retrieve the note's pitch as a frequency
	 * 
	 * @return double note's pitch
	 */
	public double getFrequency() {
		double frq = this.pitch;
		if (this.pitchType == MIDI_PITCH && this.pitch != REST) {
			frq = Frequencies.FRQ[(int) this.pitch];
		}
		if (this.pitch == REST) {
			frq = REST;
		}
		return frq;
	}

	/** Return a reference to the phrase containing this note */
	public Phrase getMyPhrase() {
		return this.myPhrase;
	}

	/**
	 * Return note offset. The range is 0 = no change, positive number delay the
	 * note, negative values rush (advance) it
	 * 
	 * @return double note's offset
	 */
	public double getOffset() {
		return this.offset;
	}

	/**
	 * Retrieves note's pan. 0.0 (full left) 0.5 (center) 1.0 (full right)
	 * 
	 * @return notes pan
	 */
	public double getPan() {
		return this.pan;
	}

	/**
	 * Retrieve the note's pitch as an integer. Useful for working with pitch as
	 * MIDI note numbers.
	 * 
	 * @return int note's pitch
	 */
	public int getPitch() {
		if (pitchType == FREQUENCY && this.pitch != REST) {
			System.err.println("jMusic error getting Note pitch: Pitch is a frequency - " + "getPitch() can't be used.");
			System.exit(1);
			// return 0; // to do - calculation to work out MIDI note from freq.
		}
		int val;
		if (this.pitch < REST + 2) {
			val = REST;
		} else {
			val = (int) this.pitch;
		}
		return val;
	}

	// ----------------------------------------------
	// Data Methods
	// ----------------------------------------------
	/**
	 * Retrieve note's pitch type
	 * 
	 * @return boolean notes pitch type
	 */
	public boolean getPitchType() {
		return this.pitchType;
	}

	/**
	 * Retrieve note's rhythmValue
	 * 
	 * @return float notes rhythmValue
	 */
	public double getRhythmValue() {
		return this.rhythmValue;
	}

	/**
	 * Return note sampleStartTime
	 * 
	 * @return int note's sampleStartTime
	 */
	public double getSampleStartTime() {
		return this.sampleStartTime;
	}

	/**
	 * Check if the note is a rest or a pitched note.
	 * 
	 * @return boolean True if the note is a rest otherwise false.
	 */
	public boolean isRest() {
		if (this.getPitch() == REST) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the note is within a particular scale There are a number of
	 * scale constants specified in the JMC which can be used with this method,
	 * these include MAJOR_SCALE, MINOR_SCALE, and PENTATONIC_SCALE
	 * 
	 * @param int[] - an array of scale degrees
	 * @return boolean - true means it is in the scale
	 */
	public boolean isScale(int[] scale) {
		for (int element : scale) {
			if (this.pitch % 12 == element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Specify the values for a break point envelope. Some indexes of the
	 * breakPoints array are reserved for specific purposes. See the Note
	 * constants for a list of these.
	 * 
	 * @param index
	 *            The specific breakPoint number to set.
	 * @param points
	 *            The values for this break point array.
	 */
	public void setBreakPoints(int index, double[] points) {
		if (index < 0 || index > breakPoints.length) {
			System.err.println("jMusic Note error: BreakPoint index " + index + " is out of range when setting.");
			System.exit(1);
		}
		this.breakPoints[index] = points;
	}

	/**
	 * Set notes duration. 1.0 = Crotchet (Quater Note), 0.5 = Quaver (Eighth
	 * Note), etc...
	 * 
	 * @param double note's duration
	 */
	public void setDuration(double duration) {
		this.duration = (duration < MIN_DURATION) ? MIN_DURATION : ((duration > MAX_DURATION) ? MAX_DURATION : duration);
	}

	/**
	 * Assign notes dynamic
	 * 
	 * @param int notes dynamic
	 */
	public void setDynamic(int dynamic) {
		this.dynamic = (dynamic < MIN_DYNAMIC) ? MIN_DYNAMIC : ((dynamic > MAX_DYNAMIC) ? MAX_DYNAMIC : dynamic);
	}

	/**
	 * Assign notes pitch as a frequency. If the parameter <CODE>pitch</CODE> is
	 * less than {@link #MIN_FREQUENCY} then the pitch of this note will be set
	 * to MIN_FREQUENCY.
	 * 
	 * @param double note pitch as a frequency in hertz
	 */
	public void setFrequency(double freq) {
		try {
			this.pitch = (pitch < MIN_FREQUENCY) ? MIN_FREQUENCY : freq;
			pitchType = FREQUENCY;
		} catch (RuntimeException re) {
			System.err.println("Error setting note value: " + "You must enter frequency values above " + MIN_FREQUENCY);
			System.exit(1);
		}

	}

	/**
	 * Change both the rhythmValue and duration of a note in the one step.
	 * 
	 * @param newLength
	 *            The new rhythmValue for the note (Duration is a proportion of
	 *            this value)
	 */
	public void setLength(double newLength) {
		this.setRhythmValue(newLength);
		this.setDuration(newLength * DEFAULT_DURATION_MULTIPLIER);
	}

	/** Sets a reference to the phrase that contains this note */
	public void setMyPhrase(Phrase phr) {
		this.myPhrase = phr;
	}

	/**
	 * Set notes offset. The range is 0 = no change, positive number delay the
	 * note, negative values rush (advance) it
	 * 
	 * @param double note's offset
	 */
	public void setOffset(double offset) {
		this.offset = offset;
	}

	/**
	 * Assign notes pan. 0.0 (full left) 0.5 (center) 1.0 (full right).
	 * 
	 * @param double note's pan
	 */
	public void setPan(double pan) {
		this.pan = (pan < MIN_PAN) ? MIN_PAN : ((pan > MAX_PAN) ? MAX_PAN : pan);
	}

	/**
	 * Assign notes pitch. If the parameter <CODE>pitch</CODE> is less than
	 * {@link #MIN_PITCH} then the pitch of this note will be set to MIN_PITCH.
	 * Likewise, if <CODE>pitch</CODE> is greater than {@link #MAX_MIDI_PITCH},
	 * pitch will be set to MAX_MIDI_PITCH.
	 * 
	 * @param int notes pitch
	 */
	public void setPitch(int pitch) {
		if (pitch == REST) {
			this.pitch = REST;
		} else {
			try {
				this.pitch = (pitch < MIN_PITCH) ? MIN_PITCH : ((pitch > MAX_MIDI_PITCH) ? MAX_MIDI_PITCH : pitch);
			} catch (RuntimeException re) {
				System.err.println("Error setting pitch value: " + "You must enter pitch values between " + MIN_PITCH + " and "
						+ MAX_MIDI_PITCH);
			}
		}
		pitchType = MIDI_PITCH;
	}

	/**
	 * Specifies the note's pitch type. There are constants for FREQUENCY and
	 * MIDI_PITCH
	 * 
	 * @param boolean note's pitch type
	 */
	public void setPitchType(boolean newType) {
		this.pitchType = newType;
	}

	/**
	 * Assign notes rhythmValue
	 * 
	 * @param float notes rhythmValue
	 */
	public void setRhythmValue(double rhythmValue) {
		this.rhythmValue =
				(rhythmValue < MIN_RHYTHM_VALUE) ? MIN_RHYTHM_VALUE : ((rhythmValue > MAX_RHYTHM_VALUE) ? MAX_RHYTHM_VALUE
						: rhythmValue);
	}

	/**
	 * Sets the rhythmValue, and optionally change the duration at the same
	 * time.
	 * 
	 * @param factorDuration
	 *            wether or not to change the duration to be a multiple of the
	 *            rhythm value as well
	 */
	public void setRhythmValue(double rv, boolean factorDuration) {
		setRhythmValue(rv);
		if (factorDuration) {
			setDuration(rv * DEFAULT_DURATION_MULTIPLIER);
		}
	}

	/**
	 * Set notes sampleStartTime
	 * 
	 * @param int note's sampleStartTime
	 */
	public void setSampleStartTime(double sampleStartTime) {
		this.sampleStartTime = sampleStartTime;
	}

	// ----------------------------------------------
	// Utility Methods
	// ----------------------------------------------
	/**
	 * Collects a string of the notes attributes
	 */
	@Override
	public String toString() {
		String noteDetails;
		if (pitchType == MIDI_PITCH) {
			noteDetails =
					new String("jMusic NOTE: " + "[Pitch = " + (int) pitch + "][RhythmValue = " + rhythmValue + "][Dynamic = "
							+ dynamic + "][Pan = " + pan + "][Duration = " + duration + "]");
		} else {
			noteDetails =
					new String("Note: " + "[Frequency = " + pitch + "][RhythmValue = " + rhythmValue + "][Dynamic = " + dynamic
							+ "][Pan = " + pan + "][Duration = " + duration + "]");
		}
		return noteDetails;
	}
}
