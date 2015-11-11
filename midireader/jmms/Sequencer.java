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

//
//  Sequencer.java
//  
//
//  Created by Rene Wooller on Tue Dec 17 2002.
//  Copyright (c) 2002 LEMu. All rights reserved.
//
package jmms;

import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiException;

import java.util.Enumeration;

import jm.midi.MidiInputListener;
//import jm.midi.event.*;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.data.Tempo;

//import parts.Parameter;
/**
 * this class lets you play a jmusic score using midishare
 */
public class Sequencer extends MidiAppl {

	public static int[] msrefnums = new int[10];

	public static int refnumCount = 0;

	/**
	 * sonds MIDI controller messages
	 * 
	 * @param channel
	 * @param type
	 * @param value
	 * @param refNumIndex
	 */
	public static void sendControllerData(int channel, int type, int value, int refNumIndex) {
		// System.out.println("type" + type + "value" + value);
		int event = Midi.NewEv(Midi.typeCtrlChange);
		if (event != 0) { // if the allocation was succesfull

			Midi.SetChan(event, channel); // set the Midi channel
			Midi.SetField(event, 0, type);
			Midi.SetField(event, 1, value);

			Midi.SetDate(event, 0);
		}
		Midi.SendIm(msrefnums[refNumIndex], event);
	}

	public static void sendPrgChng(int instrument, int channel, int refnumIndex) {
		// System.out.println("Sending insturment = " + instrument);
		Integer bank = null;
		int prgChng = instrument;
		// Integer lsb = null;
		int event = Midi.NewEv(Midi.typeProgChange); // ask for a new note event
		if (event != 0) { // if the allocation was succesfull

			Midi.SetChan(event, channel); // set the Midi channel
			// Midi.SetPort(event,destinationPort); // set the destination port
			// check to see if lsb is being used
			if (instrument > 999999) {
				prgChng = instrument % 1000;
				int lsb = instrument / 1000;
				lsb = lsb % 1000;
				int msb = instrument / 1000000;
				// System.out.println("msb: " + msb + "  lsb: " + lsb +
				// "  prgChng: " + prgChng);
				sendControllerData(channel, 0, msb, msrefnums[refnumIndex]);
				sendControllerData(channel, 32, lsb, msrefnums[refnumIndex]);

			} else if (instrument > 99) {
				bank = new Integer((100 + (instrument / 100)));
				// System.out.println(" bank = " + bank.intValue());
				prgChng = instrument % 100;
			}
			Midi.SetField(event, 0, prgChng);

			Midi.SetDate(event, 0);
		}
		if (bank != null) {
			// / System.out.println(" bank not null " + bank.intValue() +
			// " instrument = " + instrument);
			int bevent = Midi.NewEv(Midi.typeProgChange);
			Midi.SetChan(bevent, channel);
			Midi.SetField(bevent, 0, bank.intValue());
			Midi.SetDate(bevent, 0);
			Midi.SendIm(msrefnums[refnumIndex], bevent);
			Midi.SetDate(event, 2000);
		}
		// System.out.println(" sending immediately " + instrument + "  " +
		// channel);
		Midi.SendIm(msrefnums[refnumIndex], event);
	}

	public static void sendVol(int channel, int value, int refNumIndex) {
		int event = Midi.NewEv(Midi.typeCtrlChange); // ask for a new note event
		if (event != 0) { // if the allocation was succesfull

			Midi.SetChan(event, channel); // set the Midi channel
			// Midi.SetPort(event,destinationPort); // set the destination port
			Midi.SetField(event, 0, 7);
			Midi.SetField(event, 1, value);
			Midi.SetDate(event, 0);
		}
		Midi.SendIm(msrefnums[refNumIndex], event);
	}

	private boolean bankStylePrgChg = true;

	int clockAt = 0;

	int count = 0;

	private int[] instruments = new int[20];

	// int timeFromLast;
	long lastClock = 0;
	// midiTempo represents the incoming MIDI clock tempo
	double midiTempo = 120.0;
	int recieveTimeDif = 0;
	Sequence sequence;

	//

	// public boolean recievingClock = false;

	Sequence sequence1 = new Sequence();

	Sequence sequence2 = new Sequence();

	private Tempo tempo;

	private boolean tempoFromScore = false;

	long thisClock = 0;

	int timeDif = 0;

	/**
	 * this is the method that plays the score that you give it instantaneously
	 * It ignores the tempo of the score. You need to set that using the
	 * setTempo(double tempo) method
	 * 
	 * @param Score
	 *            s the score that is to be played
	 * 
	 *            public void play(int date) { playSeq(seq, date); }
	 */
	boolean useSeq1 = true;

	/*
	 * this opens up the midishare session and connects all the ports (it goes
	 * to port 0)
	 */
	public Sequencer() {
		super();

		try {
			this.Open("seq");
		} catch (MidiException e) {
			System.out.println(e);
		}

		msrefnums[refnumCount++] = this.refnum;

		Midi.Connect(this.refnum, 0, 1);
		Midi.Connect(0, this.refnum, 1);
	}

	public void close() {
		Midi.Close(this.refnum);
	}

	public int getMidiClockAt() {
		return clockAt;
	}

	public double getMidiTempo() {
		return midiTempo;
	}

	public Sequence getMSSeq(Score s) {
		if (s == null) {
			System.out.println("score is null");
			return null;
		}
		if (tempoFromScore) {
			tempo.setTempo(s.getTempo());
		}

		// System.out.println("score = " + s.toString());

		if (useSeq1) {
			sequence = sequence1;
		} else {
			sequence = sequence2;
		}

		sequence.reset();

		// if (seq != 0) Midi.FreeSeq(seq); // free the previous Midishare
		// sequence
		// seq = Midi.NewSeq(); // allocates a new one

		Enumeration enum1 = s.getPartList().elements();
		while (enum1.hasMoreElements()) {
			Part p = (Part) enum1.nextElement();
			partIntoSeq(p, sequence);
		}
		// sequence.print();
		useSeq1 = !useSeq1;
		return sequence;
	}

	private int inMillis(double toConvert, double tempo) {
		double toRet = ((toConvert * 60000 / tempo));
		return (int) (toRet + 0.5); // round this mutha off!
	}

	public boolean isRecievingClock() {
		recieveTimeDif = (int) (Midi.GetTime() - thisClock);
		return recieveTimeDif < 2000; // if it hasn't had any signal for two
										// seconds than it's not recieving
	}

	private int makeMSNote(int pitch, int dynamic, int duration, int date, int channel) {
		int event = Midi.NewEv(Midi.typeNote); // ask for a new note event
		if (event != 0) { // if the allocation was succesfull
			p("setting parameters");
			Midi.SetChan(event, channel); // set the Midi channel
			// Midi.SetPort(event,destinationPort); // set the destination port
			Midi.SetField(event, 0, pitch); // set the pitch field
			Midi.SetField(event, 1, dynamic); // set the velocity field
			// System.out.println(" setting Duration " + duration);
			Midi.SetField(event, 2, duration); // set the duration field
			// System.out.println(count + " getting Duration " +
			// Midi.GetField(event, 2));
			Midi.SetDate(event, date); // will try and make date relative to the
										// short sequence
		}
		return event;
	}

	private void p(String toPrint) {

		// System.out.println(toPrint);
	}

	// panic doesn't seem to work with Reason... Does it work with other synths?
	public void panic() {
		int ev;
		// each channel
		for (int i = 0; i < 16; i++) {
			ev = Midi.NewEv(Midi.typeCtrlChange); // Midi.AllNoteOff);
			Midi.SetChan(ev, i);
			Midi.SetData0(ev, 123); // 123 should be "AllNotesOff message"
			Midi.SendIm(refnum, ev);
			// each pitch
			for (int j = 0; i < 128; i++) {
				// send a note off message
				int ev2 = Midi.NewEv(Midi.typeKeyOff);
				Midi.SetChan(ev2, i);
				Midi.SetData0(ev2, j);
				Midi.SendIm(refnum, ev2);
				// Midi.FreeEv(ev);
				// System.out.println(" sending immediately ");
			}

			int ev3 = Midi.NewEv(Midi.typeCtrlChange); // Midi.AllNoteOff);
			Midi.SetChan(ev3, i);
			Midi.SetData0(ev3, 122); // 123 should be "AllNotesOff message"
			Midi.SendIm(refnum, ev3);
		}
	}

	private void partIntoSeq(Part p, Sequence seq) {
		try {
			if (p == null) {
				new NullPointerException();
			}
		} catch (NullPointerException e) {
			e.toString();
			return;
		}
		if (instruments[p.getChannel()] != p.getInstrument()) {
			// send program change only if the sequencer is operating in non
			// realTime.
			// if it is real time it is to many program changes
			// System.out.println(" sending ");
			// if(!realTime) {
			if (bankStylePrgChg) {
				Sequencer.sendPrgChng(p.getInstrument(), p.getChannel(), refnumCount - 1);

			} else {
				sendPrgChng(p.getInstrument(), p.getChannel(), refnumCount);
			}
			// update instrument value
			instruments[p.getChannel()] = p.getInstrument();
			// }
		}

		Enumeration enum1 = p.getPhraseList().elements();
		while (enum1.hasMoreElements()) {
			Phrase phr = (Phrase) enum1.nextElement();
			phraseIntoSeq(phr, p.getChannel(), seq);
		}
	}

	private void phraseIntoSeq(Phrase phr, int channel, Sequence seq) {
		try {
			if (phr == null) {
				new NullPointerException();
			}
		} catch (NullPointerException e) {
			e.toString();
			return;
		}

		int dateOfEvent = 0;
		int ev = 0;

		Enumeration enum1 = phr.getNoteList().elements();
		double counter = 0.0;
		double startTime = phr.getStartTime();
		// System.out.println("trempo = " + tempo);
		while (enum1.hasMoreElements()) {
			dateOfEvent = inMillis((counter + startTime), tempo.getPerMinute());
			// System.out.println(" doe " + dateOfEvent);
			Note n = (Note) enum1.nextElement();
			double idur = n.getDuration();
			// System.out.println(" idur " + idur);

			int milDur = inMillis(idur, tempo.getPerMinute());
			// System.out.println(" millDur  " + milDur);
			// duration conversion working properly.

			ev =
					makeMSNote(n.getPitch(), n.getDynamic(), inMillis(n.getDuration(), tempo.getPerMinute()), dateOfEvent,
							channel);

			// System.out.println("date = " + Midi.GetDate(ev));
			// System.out.println("ev length " + Midi.GetData2(ev));
			seq.addMidishareEvent(ev);
			counter += n.getRhythmValue();
		}
	}

	@Override
	public void ReceiveAlarm(int event) {
		// need to discuss this one to work out how events are going to be
		// passed.
		// remember also to sort out the setMidiListener(MidiListener mil)
		// method.
		switch (Midi.GetType(event)) {

		case Midi.typeCtrlChange: {
			// note controllerType = Midi.GetData0(event), controllerValue =
			// Midi.GetData1(event);
			// mp.receiveController(Midi.GetChan(event), Midi.GetData0(event),
			// Midi.GetData1(event)));
			break;
		}
		case Midi.typeKeyOn: {
		}
		case Midi.typeKeyOff: {
		}
		case Midi.typeClock: {
			// need to fix this up a lot!
			lastClock = thisClock;
			thisClock = Midi.GetTime();
			timeDif = (int) (thisClock - lastClock);
			timeDif = timeDif * 6;
			if (timeDif != 0) {
				midiTempo = (15000 / timeDif);
			}
			clockAt++;
		}
		default:
			Midi.FreeEv(event); // otherwise dispose the event
			break;
		}
	}

	public void sendPrgChng(int instrument, int channel) {
		// System.out.println(" sending program change ");
		// System.out.println("insturment = " + instrument);
		int event = Midi.NewEv(Midi.typeProgChange); // ask for a new note event
		if (event != 0) { // if the allocation was succesfull

			Midi.SetChan(event, channel); // set the Midi channel
			// Midi.SetPort(event,destinationPort); // set the destination port
			Midi.SetField(event, 0, instrument);

			Midi.SetDate(event, 0);
		}
		Midi.SendIm(refnum, event);
	}

	public void sendVol(int channel, int value) {
		int event = Midi.NewEv(Midi.typeCtrlChange); // ask for a new note event
		if (event != 0) { // if the allocation was succesfull

			Midi.SetChan(event, channel); // set the Midi channel
			// Midi.SetPort(event,destinationPort); // set the destination port
			Midi.SetField(event, 0, 7);
			Midi.SetField(event, 1, value);
			Midi.SetDate(event, 0);
		}
		Midi.SendIm(refnum, event);
		// Midi.FreeEv(event);
	}

	public void setMidiInput(MidiInputListener mil) {
	}

	public void setTempo(Tempo newTempo) {
		this.tempo = newTempo;
	}

	/**
	 * toggles the mode of wether to get the tempo from the score, or to rely
	 * soley on it being set. true means that the tempo will come from the score
	 * 
	 */
	public void setTempoFromScore(boolean b) {
		tempoFromScore = b;
	}
}
