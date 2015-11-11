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
package jm.midi;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import jm.JMC;
import jm.midi.event.CChange;
import jm.midi.event.EndTrack;
import jm.midi.event.Event;
import jm.midi.event.KeySig;
import jm.midi.event.NoteOn;
import jm.midi.event.PChange;
import jm.midi.event.TempoEvent;
import jm.midi.event.TimeSig;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

/**
 * A MIDI parser
 * 
 * @author Andrew Sorensen
 */

public final class MidiParser implements JMC {

	private static double tickRemainder = 0.0;

	private static void noteOn(int phrIndex, Note[] curNote, SMF smf, int i, double[] currentLength, double startTime,
			Vector phrVct, short midiChannel, short pitch, int dynamic, Vector evtList) {

		phrIndex = -1;
		// work out what phrase is ready to accept a note
		for (int p = 0; p < phrVct.size(); p++) {
			// Warning 0.02 should really be fixed
			if (currentLength[p] <= (startTime + 0.08)) {
				phrIndex = p;
				break;
			}
		}
		// need to create new phrase for a new voice?
		if (phrIndex == -1) {
			phrIndex = phrVct.size();
			phrVct.addElement(new Phrase(startTime));
			currentLength[phrIndex] = startTime;
		}
		// Do we need to add a rest ?
		if ((startTime > currentLength[phrIndex]) && (curNote[phrIndex] != null)) {
			double newTime = startTime - currentLength[phrIndex];
			// perform a level of quantisation first
			if (newTime < 0.25) {
				double length = curNote[phrIndex].getRhythmValue();
				curNote[phrIndex].setRhythmValue(length + newTime);
			} else {
				Note restNote = new Note(REST, newTime, 0);
				restNote.setPan(midiChannel);
				restNote.setDuration(newTime);
				restNote.setOffset(0.0);
				((Phrase) phrVct.elementAt(phrIndex)).addNote(restNote);
			}
			currentLength[phrIndex] += newTime;
		}
		// get end time
		double time = MidiUtil.getEndEvt(pitch, evtList, i) / smf.getPPQN();
		// create the new note
		Note tempNote = new Note(pitch, time, dynamic);
		tempNote.setDuration(time);
		curNote[phrIndex] = tempNote;
		((Phrase) phrVct.elementAt(phrIndex)).addNote(curNote[phrIndex]);
		currentLength[phrIndex] += curNote[phrIndex].getRhythmValue();
	}

	private static void resetTicker() {
		tickRemainder = 0.0;
	}

	// ------------------------------------------------------------------
	// Converts a score into a SMF
	// ------------------------------------------------------------------
	// MODIFIED 6/12/2003 Ron Legere to avoid use of magic note values for
	// Program CHanges
	// Etc.

	/**
	 * Converts jmusic score data into SMF data
	 * 
	 * @param Score
	 *            score - data to change to SMF
	 * @exception Exception
	 */
	public static void scoreToSMF(Score score, SMF smf) {
		if (VERBOSE) {
			System.out.println("Converting to SMF data structure...");
		}

		double scoreTempo = score.getTempo();
		double partTempoMultiplier = 1.0;
		int phraseNumb;
		Phrase phrase1, phrase2;

		// Add a tempo track at the start of top of the list
		// Add time sig to the tempo track
		Track smfT = new Track();
		smfT.addEvent(new TempoEvent(0, score.getTempo()));
		smfT.addEvent(new TimeSig(0, score.getNumerator(), score.getDenominator()));
		smfT.addEvent(new KeySig(0, score.getKeySignature()));
		smfT.addEvent(new EndTrack());
		smf.getTrackList().addElement(smfT);
		// ---------------------------------------------------
		int partCount = 0;
		Enumeration enum1 = score.getPartList().elements();
		while (enum1.hasMoreElements()) {
			Track smfTrack = new Track();
			Part inst = (Part) enum1.nextElement();
			System.out.print("    Part " + partCount + " '" + inst.getTitle() + "' to SMF Track on Ch. " + inst.getChannel()
					+ ": ");
			partCount++;

			// set up tempo difference between score and track - if any
			if (inst.getTempo() != Part.DEFAULT_TEMPO) {
				partTempoMultiplier = scoreTempo / inst.getTempo();
			} else {
				partTempoMultiplier = 1.0;
				// System.out.println("partTempoMultiplier = " +
				// partTempoMultiplier);
			}

			// order phrases based on their startTimes
			phraseNumb = inst.getPhraseList().size();
			for (int i = 0; i < phraseNumb; i++) {
				phrase1 = (Phrase) inst.getPhraseList().elementAt(i);
				for (int j = 0; j < phraseNumb; j++) {
					phrase2 = (Phrase) inst.getPhraseList().elementAt(j);
					if (phrase2.getStartTime() > phrase1.getStartTime()) {
						inst.getPhraseList().setElementAt(phrase2, i);
						inst.getPhraseList().setElementAt(phrase1, j);
						break;
					}
				}
			}
			// break Note objects into NoteStart's and NoteEnd's
			// as well as combining all phrases into one list
			HashMap midiEvents = new HashMap();

			if (inst.getTempo() != Part.DEFAULT_TEMPO) {
				// System.out.println("Adding part tempo");
				midiEvents.put(new Double(0.0), new TempoEvent(inst.getTempo()));
			}
			// if this part has a Program Change value then set it
			if (inst.getInstrument() != NO_INSTRUMENT) {
				// System.out.println("Instrument change no. " +
				// inst.getInstrument());
				midiEvents.put(new Double(0.0), new PChange((short) inst.getInstrument(), (short) inst.getChannel(), 0));
			}

			if (inst.getNumerator() != NO_NUMERATOR) {
				midiEvents.put(new Double(0.0), new TimeSig(inst.getNumerator(), inst.getDenominator()));
			}

			if (inst.getKeySignature() != NO_KEY_SIGNATURE) {
				midiEvents.put(new Double(0.0), new KeySig(inst.getKeySignature(), inst.getKeyQuality()));
			}

			Enumeration partEnum = inst.getPhraseList().elements();
			double max = 0;
			double startTime = 0.0;
			double offsetValue = 0.0;
			int phraseCounter = 0;
			double phraseTempoMultiplier = 1.0;
			while (partEnum.hasMoreElements()) {
				Phrase phrase = (Phrase) partEnum.nextElement();
				Enumeration phraseEnum = phrase.getNoteList().elements();
				startTime = phrase.getStartTime() * partTempoMultiplier;
				if (phrase.getInstrument() != NO_INSTRUMENT) {
					midiEvents.put(new Double(startTime), new PChange((short) phrase.getInstrument(),
							(short) inst.getChannel(), 0));
				}
				if (phrase.getTempo() != Phrase.DEFAULT_TEMPO) {
					phraseTempoMultiplier = (scoreTempo * partTempoMultiplier) / phrase.getTempo();
				} else {
					phraseTempoMultiplier = partTempoMultiplier;
				}

				// //////////////////////////////////////////////
				int noteCounter = 0;
				// System.out.println();
				System.out.print(" Phrase " + phraseCounter++ + ":");
				// set a silly starting value to force and initial pan cc event
				double pan = -1.0;
				resetTicker(); // zero the ppqn error calculator
				while (phraseEnum.hasMoreElements()) {

					Note note = (Note) phraseEnum.nextElement();
					offsetValue = note.getOffset();
					// add a pan control change if required
					if (note.getPan() != pan) {
						pan = note.getPan();
						midiEvents.put(new Double(startTime + offsetValue), new CChange((short) 10, (short) (pan * 127),
								(short) inst.getChannel(), 0));
					}
					// check for frequency rather than MIDI notes
					int pitch = 0;
					if (note.getPitchType() == Note.FREQUENCY) {
						System.err.println("jMusic warning: converting note frequency to the closest MIDI pitch for SMF.");
						// System.exit(1);
						pitch = Note.freqToMidiPitch(note.getFrequency());
					} else {
						pitch = note.getPitch();
					}
					if (pitch != REST) {
						midiEvents.put(new Double(startTime + offsetValue), new NoteOn((short) pitch,
								(short) note.getDynamic(), (short) inst.getChannel(), 0));

						// Add a NoteOn for the END of the note with 0 dynamic,
						// as recommended.
						// create a timing event at the end of the notes
						// duration
						double endTime = startTime + (note.getDuration() * phraseTempoMultiplier);
						// Add the note-off time to the list
						midiEvents.put(new Double(endTime + offsetValue),
								new NoteOn((short) pitch, (short) 0, (short) inst.getChannel(), 0));
					}
					// move the note-on time forward by the rhythmic value
					startTime += tickRounder(note.getRhythmValue() * phraseTempoMultiplier); // time
																								// between
																								// start
																								// times
					System.out.print("."); // completed a note
				}
			}
			/*
			 * //Sort lists so start times are in the right order Enumeration
			 * start = midiNoteEvents.elements(); Enumeration timeing =
			 * timeingList.elements(); Vector sortedStarts = new Vector();
			 * Vector sortedEvents = new Vector();
			 * while(start.hasMoreElements()){ double smallest =
			 * ((Double)start.nextElement()).doubleValue(); Event anevent =
			 * (Event) timeing.nextElement(); int index = 0, count = 0;
			 * while(start.hasMoreElements()){ count++; double d1 =
			 * ((Double)start.nextElement()).doubleValue(); Event event1 =
			 * (Event) timeing.nextElement(); if(smallest == d1){ //if note time
			 * is equal if(zeroVelEventQ(event1)) { index = count; } }
			 * if(smallest > d1){ smallest = d1; index = count; } }
			 * sortedStarts.addElement(midiNoteEvents.elementAt(index));
			 * sortedEvents.addElement(timeingList.elementAt(index));
			 * midiNoteEvents.removeElementAt(index);
			 * timeingList.removeElementAt(index); //reset lists for next run
			 * start = midiNoteEvents.elements(); timeing =
			 * timeingList.elements(); }
			 */

			// Sort the hashmap by starttime (key value)
			Object[] keys = midiEvents.keySet().toArray();
			Arrays.sort(keys);
			// Add times to events, now that things are sorted
			double st = 0.0; // start time
			double sortStart; // start time from list of notes ons and offs.
			int time; // the start time as ppqn value
			resetTicker();
			for (Object key : keys) {
				Event event = (Event) midiEvents.get(key);
				sortStart = ((Double) key).doubleValue();
				time = (int) (((((sortStart - st) * smf.getPPQN())) * partTempoMultiplier) + 0.5);
				st = sortStart;
				event.setTime(time);
				smfTrack.addEvent(event);
			}
			smfTrack.addEvent(new EndTrack());
			// add this track to the SMF
			smf.getTrackList().addElement(smfTrack);
			System.out.println();
		}
	}

	// -----------------------------------------------------------
	// Converts a SMF into jMusic Score data
	// -----------------------------------------------------------
	/**
	 * Convert a SMF into the jMusic data type
	 */
	public static void SMFToScore(Score score, SMF smf) {
		System.out.println("Convert SMF to JM");
		Enumeration enum1 = smf.getTrackList().elements();
		// Go through tracks
		while (enum1.hasMoreElements()) {
			Part part = new Part();
			Track smfTrack = (Track) enum1.nextElement();
			Vector evtList = smfTrack.getEvtList();
			Vector phrVct = new Vector();
			sortEvents(score, evtList, phrVct, smf, part);
			for (int i = 0; i < phrVct.size(); i++) {
				part.addPhrase((Phrase) phrVct.elementAt(i));
			}
			score.addPart(part);
			score.clean();
		}
	}

	private static void sortEvents(Score score, Vector evtList, Vector phrVct, SMF smf, Part part) {
		double startTime = 0.0;
		double[] currentLength = new double[100];
		Note[] curNote = new Note[100];
		int numOfPhrases = 0;
		double oldTime = 0.0;
		int phrIndex = 0;
		// Go through evts
		for (int i = 0; i < evtList.size(); i++) {
			Event evt = (Event) evtList.elementAt(i);
			startTime += (double) evt.getTime() / (double) smf.getPPQN();
			if (evt.getID() == 007) {
				PChange pchg = (PChange) evt;
				part.setInstrument(pchg.getValue());
				// if this event is a NoteOn event go on
			} else if (evt.getID() == 020) {
				TempoEvent t = (TempoEvent) evt;
				score.setTempo(t.getTempo());
			} else if (evt.getID() == 005) {
				NoteOn noteOn = (NoteOn) evt;
				part.setChannel(noteOn.getMidiChannel());
				short pitch = noteOn.getPitch();
				int dynamic = noteOn.getVelocity();
				short midiChannel = noteOn.getMidiChannel();
				// if you're a true NoteOn
				if (dynamic > 0) {
					noteOn(phrIndex, curNote, smf, i, currentLength, startTime, phrVct, midiChannel, pitch, dynamic, evtList);
				}
			}
		}
	}

	/**
	 * We need to call this any time we calculate unusual time values, to
	 * prevent time creep due to the MIDI tick roundoff error. This method
	 * wriiten by Bob Lee.
	 */
	private static double tickRounder(double timeValue) {
		final double tick = 1. / 480.;
		final double halfTick = 1. / 960.;
		int ticks = (int) (timeValue * 480.);
		double rounded = (ticks) * tick;
		tickRemainder += timeValue - rounded;
		if (tickRemainder > halfTick) {
			rounded += tick;
			tickRemainder -= tick;
		}
		return rounded;
	}

}
