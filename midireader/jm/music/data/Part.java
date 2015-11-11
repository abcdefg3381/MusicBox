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

package jm.music.data;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import jm.JMC;

/**
 * The Part class is representative of a single instrumental part. An Part is
 * made up of a number of Phrase objects, and Parts in turn are contained by
 * Score objects which form the highest level in the jMusic data structure.
 * Parts are added to Score objects like this...
 * 
 * <pre>
 * Score score = new Score(&quot;Concerto for Solo Clarinet&quot;);
 * Part inst = new Part(&quot;Clarinet&quot;);
 * score.addPart(inst);
 * </pre>
 * 
 * @see Score
 * @see Phrase
 * @author Andrew Sorensen
 * @version 1.0,Sun Feb 25 18:43:32 2001
 */
public class Part implements Cloneable, Serializable, JMC {
	// ----------------------------------------------
	// Defaults
	// ----------------------------------------------

	public static final int DEFAULT_CHANNEL = 0;

	public static final int DEFAULT_DENOMINATOR = JMC.NO_DENOMINATOR;

	public static final int DEFAULT_INSTRUMENT = 0;

	public static final int DEFAULT_KEY_QUALITY = JMC.NO_KEY_QUALITY;

	public static final int DEFAULT_KEY_SIGNATURE = JMC.NO_KEY_SIGNATURE;

	public static final int DEFAULT_NUMERATOR = JMC.NO_NUMERATOR;

	public static final double DEFAULT_PAN = Note.DEFAULT_PAN;

	public static final double DEFAULT_TEMPO = Phrase.DEFAULT_TEMPO;

	public static final String DEFAULT_TITLE = "Untitled Part";

	public static final int DEFAULT_VOLUME = Phrase.DEFAULT_VOLUME;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4750957932470960967L;

	/** The channel on which this part is to be played */
	private int channel;
	/** the bottom number of the time signature */
	private int denominator;
	/** Optional instrument number/ MIDI program change to apply to this Part */
	private int instrument;
	/** 0 = major, 1 = minor, others modes not specified */
	private int keyQuality;
	/**
	 * the number of accidents this part -1 is One Flat, 1 is One Sharp
	 */
	private int keySignature;
	/** A reference to the Score containing this part */
	private Score myScore = null;
	/** the top number of the time signature */

	// Possible Alternative:
	// Consider making a TimeSignature class, containing the following
	// two fields.

	private int numerator;
	/** The stereo (quad etc.) postion of all notes in this part. */
	private double pan = DEFAULT_PAN;
	// ----------------------------------------------
	// Attributes
	// ----------------------------------------------
	/** A container holding phrase objects */
	private Vector phraseList;
	/** break points */
	private double[] points = null;

	// Possible Alternative:
	// Consider using the jm.music.data.KeySignature class and modifying
	// it to support the keyQuality field.

	/** the speed for this part */
	private double tempo;
	private long[] time = null;
	private int timeIndex = 0;
	/** The title of this Part */
	private String title;
	/** the loudness for this part */
	private int volume;

	// ----------------------------------------------
	// Constructors
	// ----------------------------------------------
	/**
	 * Creates an empty Part
	 */
	public Part() {
		this(DEFAULT_TITLE);
	}

	/**
	 * Constructs a Part containing the specified <CODE>cphrase</CODE>.
	 * 
	 * @param cphrase
	 *            CPhrase to be contained in the Score
	 */
	public Part(CPhrase cphrase) {
		this();
		addCPhrase(cphrase);
	}

	/**
	 * Creates an empty Part
	 * 
	 * @param int instrument the instrument number for this Part
	 */
	public Part(int instrument) {
		this("", instrument);
	}

	/**
	 * Creates an empty Part
	 * 
	 * @param int instrument The instrument number for this Part
	 * @param int channel The channel number for the part - only required for
	 *        MIDI writing
	 */
	public Part(int instrument, int channel) {
		this("", instrument, channel);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE>.
	 * 
	 * @param phrase
	 *            Phrase to be contained in the Part
	 */
	public Part(Phrase phrase) {
		this();
		phrase.setMyPart(this);
		addPhrase(phrase);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE>.
	 * 
	 * @param phrase
	 *            Phrase to be contained in the Part
	 * @param title
	 *            String describing the title of the Part
	 */
	public Part(Phrase phrase, String title) {
		this(title);
		addPhrase(phrase);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE> and with the timbre of the specified
	 * <CODE>instrument</CODE>.
	 * 
	 * @param phrase
	 *            Phrase to be contained in the Part
	 * @param title
	 *            String describing the title of the Part
	 * @param instrument
	 *            integer describing the MIDI instrument number
	 */
	public Part(Phrase phrase, String title, int instrument) {
		this(title, instrument);
		addPhrase(phrase);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE>, with the timbre of the specified
	 * <CODE>instrument</CODE> and using the specified MIDI channel.
	 * 
	 * @param phrase
	 *            Phrase to be contained in the Part
	 * @param title
	 *            String describing the title of the Part
	 * @param instrument
	 *            integer describing the MIDI instrument number
	 * @param channel
	 *            integer describing the MIDI channel
	 */
	public Part(Phrase phrase, String title, int instrument, int channel) {
		this(title, instrument, channel);
		addPhrase(phrase);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrases</CODE>.
	 * 
	 * @param phrases
	 *            array of Phrases to be contained in the Score
	 */
	public Part(Phrase[] phrases) {
		this();
		addPhraseList(phrases);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE>.
	 * 
	 * @param phrases
	 *            array of Phrases to be contained in the Score
	 * @param title
	 *            String describing the title of the Part
	 */
	public Part(Phrase[] phrases, String title) {
		this(title);
		addPhraseList(phrases);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE> and with the timbre of the specified
	 * <CODE>instrument</CODE>.
	 * 
	 * @param phrases
	 *            array of Phrases to be contained in the Score
	 * @param title
	 *            String describing the title of the Part
	 * @param instrument
	 *            integer describing the MIDI instrument number
	 */
	public Part(Phrase[] phrases, String title, int instrument) {
		this(title, instrument);
		addPhraseList(phrases);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE> with the
	 * specified <CODE>title</CODE>, with the timbre of the specified
	 * <CODE>instrument</CODE> and using the specified MIDI channel.
	 * 
	 * @param phrases
	 *            array of Phrases to be contained in the Score
	 * @param title
	 *            String describing the title of the Part
	 * @param instrument
	 *            integer describing the MIDI instrument number
	 * @param channel
	 *            integer describing the MIDI channel
	 */
	public Part(Phrase[] phrases, String title, int instrument, int channel) {
		this(title, instrument, channel);
		addPhraseList(phrases);
	}

	/**
	 * Creates an empty Part
	 * 
	 * @param String
	 *            title the title of the Part
	 */
	public Part(String title) {
		this(title, DEFAULT_INSTRUMENT);
	}

	/**
	 * Creates an empty Part
	 * 
	 * @param String
	 *            title the title of the Part
	 * @param int instrument
	 */
	public Part(String title, int instrument) {
		this(title, instrument, DEFAULT_CHANNEL);
	}

	/**
	 * Creates an empty Part
	 * 
	 * @param String
	 *            title the title of the Part
	 * @param int instrument
	 * @param int channel
	 */
	public Part(String title, int instrument, int channel) {
		this.title = title;
		this.phraseList = new Vector();

		if (this.channel > 16) {
			System.err.println(new Exception("jMusic Warning: A MIDI Channel " + "cannot be greater than 16. "
					+ "There can be any number of Audio channels."));
			(new Exception()).printStackTrace();
			// System.exit(1);
		}
		this.channel = channel;

		if (instrument < NO_INSTRUMENT) {
			System.err.println(new Exception("jMusic EXCEPTION: instrument " + "value must be greater than 0"));
			(new Exception()).printStackTrace();
			System.exit(1); // crash ungracefully
		}
		this.instrument = instrument;
		this.tempo = DEFAULT_TEMPO;
		this.volume = DEFAULT_VOLUME;
		this.keySignature = DEFAULT_KEY_SIGNATURE;
		this.keyQuality = DEFAULT_KEY_QUALITY;
		this.numerator = DEFAULT_NUMERATOR;
		this.denominator = DEFAULT_DENOMINATOR;
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE>.
	 * 
	 * @param String
	 *            Title of the Part
	 * @param int The instrument number
	 * @param int The channel number
	 * @param phrase
	 *            Phrase to be contained in the Part
	 */
	public Part(String title, int instrument, int channel, Phrase phrase) {
		this(title, instrument, channel);
		phrase.setMyPart(this);
		addPhrase(phrase);
	}

	/**
	 * Constructs a Part containing the specified <CODE>phrase</CODE>.
	 * 
	 * @param String
	 *            Title of the Part
	 * @param int The instrument number
	 * @param phrase
	 *            Phrase to be contained in the Part
	 */
	public Part(String title, int instrument, Phrase phrase) {
		this(title, instrument, DEFAULT_CHANNEL, phrase);
	}

	// ----------------------------------------------
	// Data Methods
	// ----------------------------------------------
	/**
	 * Add a phrase to this Part
	 * 
	 * @param Phrase
	 *            phrase - add a phrase to this Part
	 */
	public void add(Phrase phrase) {
		this.addPhrase(phrase);
	}

	/**
	 * Add a chord phrase to the part as part of this process we need to remove
	 * the CPhrase's phrase list and add them to the Part's normal phrase list.
	 * CPhrases with a 'true' append flag are adeed to the end of the part.
	 * 
	 * @param CPhrase
	 */
	public void addCPhrase(CPhrase cphrase) {
		if (cphrase.getAppend()) {
			cphrase.setStartTime(this.getEndTime());
		}
		Enumeration enum1 = cphrase.getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phr = (Phrase) enum1.nextElement();
			// phr.setStartTime(phr.getStartTime() + cphrase.getStartTime());
			this.addPhrase(phr);
		}
	}

	/**
	 * Add a note directly to a part, this method automatically encapsulates the
	 * note within a phrase.
	 * 
	 * @param Note
	 *            the note to be added.
	 * @param startTime
	 *            the beat position where the note (phrase) will be placed
	 */
	public void addNote(Note n, double startTime) {
		Phrase phrase = new Phrase("Generated by Part.addNote()", startTime);
		phrase.addNote(n);
		this.addPhrase(phrase);
	}

	/**
	 * Add a phrase to this Part Phrases with a 'true' append flag are added to
	 * the end of the part.
	 * 
	 * @param Phrase
	 *            phrase - add a phrase to this Part
	 */
	public void addPhrase(Phrase phrase) {
		phrase.setMyPart(this);
		if (phrase.getAppend()) {
			phrase.setStartTime(this.getEndTime());
		}
		this.phraseList.addElement(phrase);
	}

	/**
	 * Adds multiple phrases to the part from an array of phrases
	 * 
	 * @param phraseArray
	 */
	public void addPhraseList(Phrase[] phraseArray) {
		for (Phrase element : phraseArray) {
			if (element.getAppend()) {
				Phrase newPhrase = element.copy();
				newPhrase.setStartTime(this.getEndTime());
				this.addPhrase(newPhrase);
			} else {
				this.addPhrase(element);
			}
		}
	}

	/**
	 * Add a copy of a phrase to the end of this Part
	 * 
	 * @param Phrase
	 *            the phrase to add
	 */
	public void appendPhrase(Phrase phrase) {
		Phrase newPhrase = phrase.copy();
		newPhrase.setStartTime(this.getEndTime());
		this.addPhrase(newPhrase);
	}

	/**
	 * Remove phrases from the score.
	 */
	public void clean() {
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (phrase.getInstrument() == this.instrument) {
				phrase.setInstrument(Phrase.DEFAULT_INSTRUMENT);
			}
			if (phrase.getNoteList().size() == 0) {
				this.removePhrase(phrase);
			}
		}
	}

	/** Make a duplicate of the current part */
	public Part copy() {
		Part i;
		i = new Part();
		Enumeration enum1 = this.phraseList.elements();
		while (enum1.hasMoreElements()) {
			Phrase oldPhrase = (Phrase) enum1.nextElement();
			i.addPhrase(oldPhrase.copy());
		}
		copyAttributes(i);
		return i;
	}

	/**
	 * Returns a copy of the Part between specified loactions
	 * 
	 * @param double start of copy section in beats
	 * @param double end of copy section in beats
	 * @return Part a copy of section of the Part
	 */
	public Part copy(double startLoc, double endLoc) {
		Part cp;
		Vector tempVect = new Vector();
		cp = new Part();
		copyAttributes(cp);
		Enumeration enum1 = this.phraseList.elements();
		while (enum1.hasMoreElements()) {
			Phrase ph = (Phrase) enum1.nextElement();
			if (ph.getStartTime() < endLoc && ph.getEndTime() > startLoc) {
				// if((ph.getStartTime() >= startLoc && ph.getStartTime() <
				// endLoc)
				// || (ph.getEndTime() > startLoc && ph.getEndTime() <= endLoc))
				// {
				tempVect.addElement(ph.copy(startLoc, endLoc));
			}
		}
		cp.setPhraseList(tempVect);
		return cp;
	}

	/**
	 * Returns a copy of the Part between specified loactions
	 * 
	 * @param boolean wether to trim the notes or not
	 * @param boolean wether to truncated the notes duration when trimming them
	 *        or not
	 * @param boolean wether to set the start time of the phrases in relation to
	 *        the start of the <br>
	 *        old part (true) or the new one (false) maybe should be called
	 *        "relative to old"
	 * @param double start of copy section in beats
	 * @param double end of copy section in beats
	 * @return Part a copy of section of the Part
	 */
	public Part copy(double startLoc, double endLoc, boolean trimmed, boolean truncated, boolean relativeStartLoc) {

		Part cp = new Part();
		copyAttributes(cp);
		Vector tempVect = new Vector();
		// copy the core to which it belongs accross
		cp.setMyScore(this.getMyScore());
		Enumeration enum1 = this.phraseList.elements();

		while (enum1.hasMoreElements()) {
			Phrase ph = (Phrase) enum1.nextElement();
			double startTime = ph.getStartTime();
			if (startTime < endLoc && ph.getEndTime() > startLoc) {
				// trimmed, truncated, move start time if first
				// note is past startloc
				Phrase cpy = ph.copy(startLoc, endLoc, trimmed, truncated, false);
				// make the start time relative to the beginning
				// of the new Part
				double newStartTime = 0.0;
				if (newStartTime < 0.0) {
					newStartTime = 0.0;
				}
				if (relativeStartLoc) {
					newStartTime += startLoc;
				}
				cpy.setStartTime(newStartTime);
				// by this stage, the phrase should be as long as
				// is needed to fit the endLoc.
				// it isn't which means that something in
				// phrase.copy isn't working

				tempVect.addElement(cpy);
			}
		}
		cp.setPhraseList(tempVect);
		return cp;
	}

	private void copyAttributes(Part i) {
		// copy the attrubutes
		i.setInstrument(this.getInstrument());
		i.setChannel(this.getChannel());
		i.setTitle(this.getTitle() + " copy");
		i.setTempo(this.tempo);
		i.setVolume(this.volume);
		i.setPoints(this.points);
		i.setTime(this.time);
		i.setTimeIndex(this.timeIndex);
		i.setMyScore(this.getMyScore());
	}

	/**
	 * Generates and returns a new empty phrase and adds it to this part.
	 */
	public Phrase createPhrase() {
		Phrase p = new Phrase();
		this.addPhrase(p);
		return p;
	}

	/**
	 * Empty removes all elements in the vector
	 */
	public void empty() {
		phraseList.removeAllElements();
	}

	/**
	 * Gets the channel for this channel
	 * 
	 * @return short channel
	 */
	public int getChannel() {
		return this.channel;
	}

	/**
	 * Returns the Parts time signature denominator
	 * 
	 * @return int time signature denominator
	 */
	public int getDenominator() {
		return this.denominator;
	}

	/**
	 * Return the Parts endTime
	 * 
	 * @return double the Parts endTime
	 */
	public double getEndTime() {
		double endTime = 0.0;
		Enumeration enum1 = this.phraseList.elements();
		while (enum1.hasMoreElements()) {
			Phrase nextPhr = (Phrase) enum1.nextElement();
			double phraseEnd = nextPhr.getEndTime();
			if (phraseEnd > endTime) {
				endTime = phraseEnd;
			}
		}
		return endTime;
	}

	/**
	 * Return the value of the highest note in the part.
	 */
	public int getHighestPitch() {
		int max = 0;
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (phrase.getHighestPitch() > max) {
				max = phrase.getHighestPitch();
			}
		}
		return max;
	}

	/**
	 * Get Instrument number / MIDI Program Change
	 * 
	 * @return program change
	 */
	public int getInstrument() {
		return this.instrument;
	}

	/**
	 * Returns the Parts key quality 0 is Major, 1 is minor
	 * 
	 * @return int key quality
	 */
	public int getKeyQuality() {
		return this.keyQuality;
	}

	/**
	 * ' Returns the Parts key signature The number of sharps (+) or flats (-)
	 * 
	 * @return int key signature
	 */
	public int getKeySignature() {
		return this.keySignature;
	}

	/**
	 * Return the value of the longest rhythm value in the part.
	 */
	public double getLongestRhythmValue() {
		double max = 0.0;
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (phrase.getLongestRhythmValue() > max) {
				max = phrase.getLongestRhythmValue();
			}
		}
		return max;
	}

	/**
	 * Return the value of the lowest note in the part.
	 */
	public int getLowestPitch() {
		int min = 127;
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (phrase.getLowestPitch() < min) {
				min = phrase.getLowestPitch();
			}
		}
		return min;
	}

	/** get a reference to the score that contains this part */
	public Score getMyScore() {
		return myScore;
	}

	/**
	 * Returns the Parts time signature numerator
	 * 
	 * @return int time signature numerator
	 */
	public int getNumerator() {
		return this.numerator;
	}

	/**
	 * Return the pan position for this part
	 * 
	 * @return double the part's pan setting
	 */
	public double getPan() {
		return this.pan;
	}

	/**
	 * Get an individual phrase object from its number
	 * 
	 * @param int number - the number of the Track to return
	 * @return phrase answer - the phrase to return
	 */
	public Phrase getPhrase(int number) {
		Enumeration enum1 = phraseList.elements();
		int counter = 0;
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (counter == number) {
				return phrase;
			}
			counter++;
		}
		return null;
	}

	/**
	 * Returns the all phrases in this part as a array
	 * 
	 * @return Phrase[] An array containing all Phrase objects in this part
	 */
	public Phrase[] getPhraseArray() {
		Vector vct = this.phraseList;
		Phrase[] phraseArray = new Phrase[vct.size()];
		for (int i = 0; i < phraseArray.length; i++) {
			phraseArray[i] = (Phrase) vct.elementAt(i);
		}
		return phraseArray;
	}

	/**
	 * Returns the entire phrase list
	 * 
	 * @return Vector - A vector containing all this Parts phrase objects
	 */
	public Vector getPhraseList() {
		return this.phraseList;
	}

	public double getPoint() {
		return this.points[timeIndex];
	}

	/**
	 * Return the value of the shortest rhythm value in the part.
	 */
	public double getShortestRhythmValue() {
		double min = 1000.0;
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			if (phrase.getShortestRhythmValue() < min) {
				min = phrase.getShortestRhythmValue();
			}
		}
		return min;
	}

	/**
	 * get the number of phrases in this part
	 * 
	 * @return int length - the number of phrases
	 */
	public int getSize() {
		return (phraseList.size());
	}

	/**
	 * Returns the Part's tempo
	 * 
	 * @return double tempo
	 */
	public double getTempo() {
		return this.tempo;
	}

	/**
	 * Returns a copy of the Part
	 * 
	 * @return Part a copy of the Part
	 */

	public long getTime() {
		return this.time[timeIndex++];
	}

	public int getTimeIndex() {
		return this.timeIndex;
	}

	/**
	 * Returns the Parts title
	 * 
	 * @return String title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the Part's volume
	 * 
	 * @return int volume
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * Get the number of phrases in this part
	 * 
	 * @return int The number of phrases
	 */
	public int length() {
		return size();
	}

	/**
	 * Deletes all the phrases previously added to the part
	 */
	public void removeAllPhrases() {
		this.phraseList.removeAllElements();
	}

	/**
	 * Deletes the last phrase added to the part
	 */
	public void removeLastPhrase() {
		Vector vct = this.phraseList;
		vct.removeElement(vct.lastElement());
	}

	/**
	 * Deletes the specified phrase in the part
	 * 
	 * @param int noteNumb the index of the note to be deleted
	 */
	public void removePhrase(int phraseNumb) {
		Vector vct = this.phraseList;
		try {
			vct.removeElement(vct.elementAt(phraseNumb));
		} catch (RuntimeException re) {
			System.err.println("The Phrase index to be deleted must be within the part.");
		}
	}

	/**
	 * Deletes the first occurence of the specified phrase in the Part.
	 * 
	 * @param phrase
	 *            the Phrase object to be deleted.
	 */
	public void removePhrase(Phrase phrase) {
		this.phraseList.removeElement(phrase);
	}

	/**
	 * Sets the MidiChannel for this part
	 * 
	 * @param short midiChannel
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * Specifies the Part's time signature denominator
	 * 
	 * @param int time signature denominator
	 */
	public void setDenominator(int dem) {
		this.denominator = dem;
	}

	/**
	 * Change the duration value of each note in the Part.
	 */
	public void setDuration(double val) {
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			phrase.setDuration(val);
		}
	}

	/**
	 * Change the dynamic value of each note in the Part.
	 */
	public void setDynamic(int dyn) {
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			phrase.setDynamic(dyn);
		}
	}

	/**
	 * Set instrument number / MIDI Program Change
	 * 
	 * @param int program change
	 */
	public void setInstrument(int instrument) {
		this.instrument = instrument;
	}

	/**
	 * Specifies the Part's key quality 0 is Major, 1 is minor
	 * 
	 * @param int key quality (modality)
	 */
	public void setKeyQuality(int newQual) {
		this.keyQuality = newQual;
	}

	/**
	 * Specifies the Part's key signature The number of sharps (+) or flats (-)
	 * 
	 * @param int key signature
	 */
	public void setKeySignature(int newSig) {
		this.keySignature = newSig;
	}

	/** set a reference to the score containing this part */
	public void setMyScore(Score scr) {
		this.myScore = scr;
	}

	/**
	 * Specifies the Part's time signature numerator
	 * 
	 * @param int time signature numerator
	 */
	public void setNumerator(int num) {
		this.numerator = num;
	}

	/**
	 * Determine the pan position for all notes in this part.
	 * 
	 * @param double the part's pan setting
	 */
	public void setPan(double pan) {
		this.pan = pan;
		Enumeration enum1 = phraseList.elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			phrase.setPan(pan);
		}
	}

	/**
	 * Updates the entire phrase list
	 * 
	 * @param Vector
	 *            containing phrase objects
	 */
	public void setPhraseList(Vector newPhraseList) {
		this.phraseList = newPhraseList;
	}

	/**
	 * Change the Pitch value of each note in the Part.
	 */
	public void setPitch(int val) {
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			phrase.setPitch(val);
		}
	}

	// this section is an incomplete implementation of part envelopes
	// needs updating or deleting - unstable!!!
	public void setPoints(double[] p) {
		points = p;
	}

	// for backwards compatability
	/**
	 * Set instrument number / MIDI Program Change This method is deprecated in
	 * favour of setInstrument!!
	 * 
	 * @param int program change
	 */
	public void setProgChg(int program) {
		this.instrument = program;
	}

	/**
	 * Change the rhythmValue value of each note in the Part.
	 */
	public void setRhythmValue(int val) {
		Enumeration enum1 = getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			phrase.setRhythmValue(val);
		}
	}

	/**
	 * Sets the Part's tempo
	 * 
	 * @param double tempo
	 */
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public void setTime(long[] t) {
		this.time = t;
	}

	public void setTimeIndex(int index) {
		this.timeIndex = index;
	}

	/**
	 * Sets the Parts title
	 * 
	 * @param String
	 *            title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the Part's volume
	 * 
	 * @param int volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * get the number of phrases in this part
	 * 
	 * @return int length - the number of phrases
	 */
	public int size() {
		return (phraseList.size());
	}

	/**
	 * Collects the Parts attributes to a string
	 */
	@Override
	public String toString() {
		String partData = new String("----- jMusic PART: '" + title + "' contains " + this.size() + " phrases.  -----" + '\n');
		partData += "Channel = " + channel + '\n';
		partData += "Instrument = " + instrument + '\n';
		if (this.tempo > 0) {
			partData += "Part Tempo = " + this.tempo + '\n';
		}
		Enumeration enum1 = phraseList.elements();
		while (enum1.hasMoreElements()) {
			Phrase phrase = (Phrase) enum1.nextElement();
			partData = partData + phrase.toString() + '\n';
		}
		return partData;
	}

}