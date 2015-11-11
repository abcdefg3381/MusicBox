package eg.reader.ibm;

/***************************************************************************/
/*                                                                         */
/* (c) Copyright IBM Corp. 2001  All rights reserved.                      */
/*                                                                         */
/* This sample program is owned by International Business Machines         */
/* Corporation or one of its subsidiaries ("IBM") and is copyrighted       */
/* and licensed, not sold.                                                 */
/*                                                                         */
/* You may copy, modify, and distribute this sample program in any         */
/* form without payment to IBM, for any purpose including developing,      */
/* using, marketing or distributing programs that include or are           */
/* derivative works of the sample program.                                 */
/*                                                                         */
/* The sample program is provided to you on an "AS IS" basis, without      */
/* warranty of any kind.  IBM HEREBY  EXPRESSLY DISCLAIMS ALL WARRANTIES,  */
/* EITHER EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED   */
/* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.     */
/* Some jurisdictions do not allow for the exclusion or limitation of      */
/* implied warranties, so the above limitations or exclusions may not      */
/* apply to you.  IBM shall not be liable for any damages you suffer as    */
/* a result of using, modifying or distributing the sample program or      */
/* its derivatives.                                                        */
/*                                                                         */
/* Each copy of any portion of this sample program or any derivative       */
/* work,  must include the above copyright notice and disclaimer of        */
/* warranty.                                                               */
/*                                                                         */
/***************************************************************************/

/***************************************************************************/
/*                                                                         */
/* This file accompanies the article "Understanding and using Java MIDI    */
/* audio." This article was published in the Special Edition 2001 issue    */
/* of the IBM DeveloperToolbox Technical Magazine at                       */
/* http://www.developer.ibm.com/devcon/mag.htm.                            */
/*                                                                         */
/***************************************************************************/

//------------------------------------------------------------------------
// File Name:     MidiPlay
// Description:   Play midi or rmf files from the command line.
//------------------------------------------------------------------------

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

/**
 * MidiPlay Play midi or rmf files from the command line.
 * 
 * @author Dan Becker
 */
public class MidiPlay implements MetaEventListener {
	public static int currentPlay;
	public static String[] playList;
	public static Sequencer sequencer;

	/** Returns a buffered input stream from the given file name. */
	public static InputStream getBufferedStream(String fileName) throws FileNotFoundException {
		File midiFile = new File(fileName);
		InputStream sequenceStream = new FileInputStream(midiFile);
		sequenceStream = new BufferedInputStream(sequenceStream);
		return sequenceStream;
	} // getStream

	/** Play given arguments as file names. */
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage: java MidiPlay [fileName]*");
			System.exit(1);
		}
		playList = args;
		System.out.println("MidiPlay playlist:");
		for (int i = 0; i < args.length; i++) {
			System.out.println("   " + playList[i]);
		}

		sequencer = MidiSystem.getSequencer(); // throws
												// MidiUnavailableException
		sequencer.open();
		sequencer.addMetaEventListener(new MidiPlay());

		System.out.println("Playing " + playList[currentPlay]);
		playStream(sequencer, getBufferedStream(playList[currentPlay++]));
	} // main

	/** Sets and starts play of the given midi sequence stream. */
	public static void playStream(Sequencer sequencer, InputStream sequenceStream) throws InvalidMidiDataException, IOException {

		sequencer.setSequence(sequenceStream);
		sequencer.start();
	} // playStream

	// MetaEventListener role
	@Override
	public void meta(MetaMessage event) {
		if (event.getType() == 47) { // end of stream
			sequencer.stop();

			if (currentPlay < playList.length) {
				try {
					System.out.println("Playing " + playList[currentPlay]);
					playStream(sequencer, getBufferedStream(playList[currentPlay++]));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				} // catch
			} else {
				sequencer.close();
				System.exit(1);
			} /* endif */
		}
	} // meta
} // class MidiPlay

