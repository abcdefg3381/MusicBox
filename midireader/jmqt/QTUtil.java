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

package jmqt;

import java.awt.*;
import java.awt.event.*;

import jm.JMC;
import jm.music.data.*;

import quicktime.*;
import quicktime.app.*;
import quicktime.app.time.*;
import quicktime.io.*;
import quicktime.qd.*;
import quicktime.sound.*;
import quicktime.std.movies.*;
import quicktime.std.movies.media.*;
import quicktime.std.music.*;
import quicktime.std.*;
import quicktime.std.qtcomponents.*;
//new for OS X
//import com.apple.mrj.macos.carbon.CarbonLock;

import quicktime.util.*;

/**
 * A class which plays a jMusic score via Apple's QuickTime.
 * Requires the QTJava classes from Apple Computer inc.
 * Based on example code and help from Bill Stewart (thanks Bill).
 * @author Andrew Brown
 */

public class QTUtil implements JMC, StdQTConstants, SoundConstants {	
	private TunePlayer tunePlayer1;
	private MusicData tune1;
	private boolean usingTuneOne = true;
	private Score score;
	// set up the number of channels required
	private NoteChannel[] nc1 =  new NoteChannel[16];
	private double speed = 1.0;
	private ToneDescription td;
        private double tempoMultiplier = 600.0;
        
        //-------------
        //constructor
        //-------------
        
        public QTUtil() { 
            try {
                //CarbonLock.acquire(); // new for OS X
                QTSession.open();
                tunePlayer1 = new TunePlayer();
                // create note channels for all 16 MIDI channels
                setupNoteChannels();
                tunePlayer1.setNoteChannels(nc1);
                // create a tone desc for single note playing
                td = new ToneDescription(1);
            } catch (QTException qte) { 
                qte.printStackTrace(); 
            } finally {
                //CarbonLock.release(); // new for OS X
            }
        }
        
        public QTUtil(Score s) { 
            try {
                QTSession.open();
                tunePlayer1 = new TunePlayer();
                // create note channels for all 16 MIDI channels
                this.setupNoteChannels();
                tunePlayer1.setNoteChannels(nc1);
                this.setScore(s);
                // create a tone desc for single note playing
                td = new ToneDescription(1);
            } catch (QTException qte) { qte.printStackTrace(); }
        }
        
	//-------------------
	// Auxillary methods
	//-------------------
    
        public void setScore(Score score) {
                this.score = score;
                try {
                        usingTuneOne = !usingTuneOne;
                        setUp();
                } catch (QTException qte) { qte.printStackTrace(); }
                
                }
                
                public Score getScore() {
                return score;
        }
        
        private void setUp() throws QTException {
                //erase any empty parts or phrases
                if(this.score == null)
                        System.err.println("jMusic QTtil error: The score is null. Please initialise before using.");
                this.score.clean();
                // check there is data to convert
                if (totalNotesInScore() < 1) {
                        System.err.println(new Exception("jMusic EXCEPTION: QTUtil Error: The Score is empty!"));
                                (new Exception()).printStackTrace();
                }
                // set speed from tempo
                this.speed = score.getTempo() / 60.0;
                // setup QT stuff
                setGMInstruments();
                // Set the number of events (notes and rests) in the score
                // Add one for an end-of-sequence marker
                int totalNotes = totalNotesInScore();
                // get notes from score into a format ready for QT
                double[][] noteOrder = new double[totalNotes][4];
                noteOrder = sortNotes(score, totalNotes);
                // calcuate the size of the sequence
                int qt_sequence_length = ((totalNotes * 2 + 1) * 4); // * 4 for 8 bit values
                // this is the tune itself
                tune1 = convertFromScore(score, noteOrder, qt_sequence_length);
        }    	
    	
    
        private void setupNoteChannels() throws QTException {
                //System.out.println("QTU set up note channels");
                int theKit = 16385; //409;//"electric"
                //a channel with 4 voice polyphony
                for(int i=0; i<16; i++) {
                        if (i != 9) {
                                nc1[i] = new NoteChannel(1, 6);
                                //System.out.println("set up cannel "+ i);
                        } else {
                                nc1[9] = new NoteChannel(theKit, 8);
                                //System.out.println("set up cannel "+ i);
                        }
                }
        }
        
        
        private void setGMInstruments() throws StdQTException {
                //System.out.println("Score size is " + score.size());
                int scoreSize = score.size();
                for(int i=0; i < scoreSize; i++) {
                        Part currentPart = score.getPart(i);
                        if ( currentPart.getInstrument() != 250) { // skip pg?
                                if (currentPart.getChannel() != 9) { // drum channel?
                                        nc1[ currentPart.getChannel()].setInstrumentNumber(currentPart.getInstrument()+1);
                                } else {
                                        nc1[9].setInstrumentNumber(currentPart.getInstrument() + 16385);
                                }
                        }
                }
        }
	
	
        private int totalNotesInScore() {
                int totalNotes = 0;
                final int scoreSize = score.size();
                for(int i=0; i< scoreSize; i++) {
                        Part currentPart = score.getPart(i);
                        final int partSize = currentPart.size();
                        for(int j=0; j<partSize ; j++) {
                                totalNotes += currentPart.getPhrase(j).size();
                        }
                }
                return totalNotes;
        }
	
	private double[][] sortNotes(Score score, int totalNotes) {
	    //System.out.println("QTU sort");
	    // put all notes into an array according to startTimes
	    double[][] noteOrder = new double[totalNotes][4];
	    int index = 0;
	    int scoreSize = score.size();
	    for(int i=0; i < scoreSize; i++) { // for each part
	    	Part currentPart = score.getPart(i);
	    	int partSize = currentPart.size();
	        for(int j=0; j < partSize; j++) { // for each phrase
	        	Phrase currentPhrase = currentPart.getPhrase(j);
	            double st = currentPhrase.getStartTime();
	            for(int k = 0; k < currentPhrase.size(); k++) { // for each note
	                if (k == 0) { // deal with offset on the first note
	                    st += currentPhrase.getNote(k).getOffset();
	                    if (st < 0.0) { // deal with a negative offset on the first note
	                        st = 0.0;
	                        Note tempNote = (Note)currentPhrase.getNote(k);
	                        tempNote.setRhythmValue( tempNote.getRhythmValue() + tempNote.getOffset());
	                    }
	                }
	                // insert the values for each note
	                noteOrder[index][0] = st; // start time
	                //System.out.println("Start time is being set to "+noteOrder[index][0]);
	                noteOrder[index][1] = (double) i; // part in score
	                noteOrder[index][2] = (double) j; // phrase in part
	                noteOrder[index][3] = (double) k; // note in phrase
	                st += currentPhrase.getNote(k).getRhythmValue();
	                if (k > 0) {st = st - currentPhrase.getNote(k - 1).getOffset()
	                                + currentPhrase.getNote(k).getOffset();
	                }
	                if (st < 0.0) st = 0.0;
	                index++;
	            }
	        }
	    }
		
		quickSort(noteOrder, 0, noteOrder.length - 1);
		
		//double time2 = System.currentTimeMillis();
		//System.out.println("Sort time is "+(time2-time1)/1000.0+" seconds");
		return noteOrder;
	}
	
        private void quickSort(double[][] noteOrder, int left, int right) {
                int i, last;
                if(left >= right) return; // already sorted
                        swap(noteOrder, left, (int)(Math.random() * (right - left)) + left);
                        // choose new pivot point
                        last = left;
                        for (i = left+1; i<=right; i++) {
                        if(noteOrder[i][0] <= noteOrder[left][0]) swap(noteOrder, ++last, i);
                }
                swap(noteOrder, left, last); // restore pivot
                quickSort(noteOrder, left, last-1);
                quickSort(noteOrder, last+1, right);
        }
        
        static void swap(double[][] noteOrder, final int i, final int j) {
                double temp;
                for(int a=0;a<4;a++){
                        temp = noteOrder[i][a];
                        noteOrder[i][a] = noteOrder[j][a];
                        noteOrder[j][a] = temp;
                }
        }
	        
		    	
	private MusicData convertFromScore(Score score, double[][] noteOrder, int qt_sequence_length) 
                    throws QTException {
		//System.out.println("QTU convert from score");
		MusicData tune = new MusicData (qt_sequence_length * 3);		        
		double currentTime = 0.0;
		// duration in 600ths of a second -- @ 300 the tempo will be 120 bpm
		double deltaTime = 0.0;
		// event index
		int eventIndex = 0;
		// check for not starting at time 0.0
		if ( noteOrder[0][0] > 0.0) {
		    // add a rest to pad it out until the start time
		    tune.setMusicEvent (eventIndex, MusicData.stuffRestEvent(
				(int)(noteOrder[0][0] * tempoMultiplier)));
		    eventIndex++;
		    currentTime = noteOrder[0][0];
		} 
		// iterate through the notes converting them to QT music events
                for(int index=0;index<noteOrder.length - 1 ;index++){ 
	       
		    Note n = score.getPart((int)noteOrder[index][1]).getPhrase(
				(int)noteOrder[index][2]).getNote((int)noteOrder[index][3]);
		   deltaTime = noteOrder[index+1][0]  * tempoMultiplier - currentTime;
		   // store current pan position
		   double lastPan = 0.5;
		   // create events
		   if (n.getPitch() < 0) { // a REST
		       
			   tune.setMusicEvent ( eventIndex, MusicData.stuffRestEvent((int)(deltaTime)));
		        eventIndex++;
		  } else {
		        // each note requires a NoteEvent for its duration and 
		        // a RestEvent for the distance before the next note
		        // Add a pan event (controller 10) for each note if required
			  if (n.getPan() != lastPan) {
				  tune.setMusicEvent (eventIndex, MusicData.stuffControlEvent(
                                                score.getPart((int)noteOrder[index][1]).getChannel() + 1,
                                                10, (int)(n.getPan() * 256) + 256)); // pan
				  eventIndex++;
				  lastPan = n.getPan();
			  }		        
			  
			  tune.setXMusicEvent (eventIndex, MusicData.stuffXNoteEvent(score.getPart(
					(int)noteOrder[index][1]).getChannel()+1,
					n.getPitch() * 256, n.getDynamic(), 
                                        (int)(n.getDuration() * tempoMultiplier)));
			  eventIndex+=2;
			  // set timing between events
		        tune.setMusicEvent (eventIndex, MusicData.stuffRestEvent((int)(deltaTime)));
		        eventIndex++;
		   }
		   // the rhythm value time 
		   currentTime += deltaTime;
		 }
		 // deal with the last note
		   Note nLast = score.getPart((int)noteOrder[(noteOrder.length -1)][1]).
			getPhrase((int)noteOrder[(noteOrder.length -1)][2]).
			getNote((int)noteOrder[(noteOrder.length -1)][3]);
		   if (nLast.getPitch() < 0) { // a REST
		        tune.setMusicEvent ( eventIndex, MusicData.stuffRestEvent(
				(int)(nLast.getRhythmValue()* tempoMultiplier)));
		        eventIndex++;
		   } else {
			   // Add a pan event (controller 10) for each note
			   tune.setMusicEvent (eventIndex, MusicData.stuffControlEvent(
                                        score.getPart((int)noteOrder[noteOrder.length - 1][1]).getChannel() + 1,
                                        10, (int)(nLast.getPan() * 256) + 256)); // pan
			   eventIndex++;
		        // each note requires a NoteEvent for its duration and 
                        // a RestEvent for the distance before the next note
			   tune.setXMusicEvent (eventIndex, MusicData.stuffXNoteEvent(score.getPart(
					(int)noteOrder[(noteOrder.length -1)][1]).getChannel()+1,
					nLast.getPitch() * 256, nLast.getDynamic(), 
                                        (int)(nLast.getDuration() * tempoMultiplier)));
			   eventIndex+=2;
                }
		return tune;
	}
    
	//--------------------------------------
	public void playback(Score s){
		//System.out.println("QTU playback");
		setScore(s);
		replay();
	}
	
        /**
        * Play the current score again.
        */
	public void replay() {
		//System.out.println("QTU replay");
                try {
			if(tunePlayer1 != null) {
				tunePlayer1.queue (tune1, (float)speed, 0, 0x7FFFFFFF, 0);
			} else {System.out.println("jMusic QTUtil error: Woops! No score to play.");}
                } catch (QTException qte) { qte.printStackTrace(); }
        }
	
        /**
        * Halt the current score playabck.
        */
	public void stopPlayback() {
		try {
			if(tunePlayer1 != null) {
                                tunePlayer1.stop();
			} 
		} catch (QTException qte) { qte.printStackTrace(); }
	}
    
	/**
	* return the current speed, which changes with the score tempo
	*/
	public double getSpeed() {
		return this.speed;
	}
	
	/**
	* return the current speed, which changes with the score tempo.
        * @param speed The tempo to play back with (1.0 = normal).
	*/
	public void setSpeed(double speed) {
	    this.speed = speed;
	}

	// ----------------------------------------
	/**
	*  Play a single note via QuickTime
	* @param note The jMusic note to be played
	* @param channel The MIDI channel on which to play the note
	*/
	public void playOneNote(Note note, int channel) {
	int tempPitch = note.getPitch();
		try {
			NoteChannel tempnc = nc1[channel];
			tempnc.playNote(tempPitch, note.getDynamic()); // note on
			try { 
				Thread.sleep ((long)(note.getDuration() * 1000)); // * 1000 = 60 bpm
			} catch (InterruptedException e) { 
				System.out.println("jMusic QTUtil ERROR: Problem sleeping single note playback thread");
				tempnc.playNote (tempPitch, 0); // note off
			}
			tempnc.playNote ( tempPitch, 0); // note off
		} catch (QTException qte) {
			qte.printStackTrace();
		}
	
	}
    
	/**
	* Send a MIDI control change message immediately
	* Note: this method is at the mercy of QuickTime and may not work when QT is set to
	* the internal QT musical instruments sound synthesizer. It does work when sending to
	* external MIDI devices. QuickTime may also not support external MIDI devices on some 
	* implementations, for example Mac OS X 10.0. In these cases a standard QT exception 
	* will be printed to he command line.
	* @param channel MIDI channel number from 0 - 15
	* @param controllerNumber MIDI controller number from 1 - 127 .e.g, 7 = volume
	* @param value data for the specified controller, between 0 - 127
	*/
	public void sendMIDIControlChange(int channel, int controllerNumber, int value) {
		int[] data = new int[2];
		data[0] = controllerNumber;
		data[1] = value;
		sendMIDIMessage(176, channel, data);
	}
    
	/**
	* Send any MIDI channel message immediately
	* @param staus The MIDI staus value. A number between 127 - 255.
	* @param channel MIDI channel to send meesage on, 0 - 15.
	* @param dataByteArray a list of the data values, usually 1 or 2 
	* values depending on the message status type.
	*/
	public void sendMIDIMessage(int status, int channel, int[] dataByteArray) {
		//System.out.println("getting note channel");
		NoteChannel tempnc = nc1[channel];
		
		byte[] midiMessage = new byte[dataByteArray.length + 1];
		MusicMIDIPacket mmp = new MusicMIDIPacket(midiMessage);
		mmp.setDataByte(0, status + channel); // 8 bits to a byte.. 
		// first bit is status or data
		// next three bits are the message type.  next four bits are channel
		for(int i = 0; i < dataByteArray.length; i++) {
			mmp.setDataByte(i + 1, dataByteArray[i]);
		}
		try {          
			tempnc.sendMIDI(mmp);      
		} catch (QTException qte) {
			qte.printStackTrace();
		}
	}

    
	/**
	* Choose a general MIDI sound for playback of single notes
	*/
	public void setNoteInstrument() {
		try {
			// Have the user choose an instrument and print out the choice
			td.pickInstrument (NoteAllocator.getDefault(), "Choose an Instrument...", 0);
			//System.out.println (td);
		} catch (QTException e) {
			e.printStackTrace();
		}
	}
	
	/*
	* Help Java finish the quickTime session elegantly
	*/
	public void finalize() {
		QTSession.close();
	}
	
}
