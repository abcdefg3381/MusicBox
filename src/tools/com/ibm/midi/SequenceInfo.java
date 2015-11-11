package tools.com.ibm.midi;

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
// File Name:     SequenceInfo
// Description:   Reports the sequence info in a Midi file.
//------------------------------------------------------------------------
import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import tools.org.gnu.midi.MidiFileReader;

/**
 * SequenceInfo - allows you to view sequence information in a Midi file.
 * 
 * @author Dan Becker
 */
public class SequenceInfo {

	/** Returns a string version of the division type. */
	public static String divisionTypeToString(float divisionType) {
		if (divisionType == Sequence.PPQ) {
			return "PPQ";
		} else if (divisionType == Sequence.SMPTE_24) {
			return "SMPTE, 24 frames per second";
		} else if (divisionType == Sequence.SMPTE_25) {
			return "SMPTE, 25 frames per second";
		} else if (divisionType == Sequence.SMPTE_30DROP) {
			return "SMPTE, 29.97 frames per second";
		} else if (divisionType == Sequence.SMPTE_30) {
			return "SMPTE, 30 frames per second";
		}
		return "unknown division type";
	} // divisionTypeToString

	public static void main(String[] args) throws IOException, InvalidMidiDataException {
		if (args.length != 1) {
			System.out.println("SequenceInfo - allows you to view sequence information in a Midi file.");
			System.out.println("   usage: java <MIDI filename>");
			System.exit(1);
		}

		// Convert the argument into a file for the sequencer.
		String fileName = args[0];
		File midiFile = new File(fileName);
		System.out.println("Sequence file name: " + fileName);

		// Using MidiSystem, convert the file to a sequence.
		// Sequence sequence = MidiSystem.getSequence( midiFile );
		MidiFileReader reader = new MidiFileReader();
		Sequence sequence = reader.getSequence(midiFile);
		if (sequence != null) {
			// Print sequence information
			System.out.println("   length: " + sequence.getTickLength() + " ticks");
			System.out.println("   duration: " + sequence.getMicrosecondLength() + " micro seconds");
			System.out.println("   division type: " + divisionTypeToString(sequence.getDivisionType()));
			String resUnits = (sequence.getDivisionType() == Sequence.PPQ) ? " ticks per beat" : " ticks per frame";
			System.out.println("   resolution: " + sequence.getResolution() + resUnits);

			// Print patch information
			Patch[] patches = sequence.getPatchList();
			if (patches != null) {
				for (int i = 0; i < patches.length; i++) {
					Patch patch = patches[i];
					System.out.println("      patch " + i + ": " + " bank " + patch.getBank() + ", program "
							+ patch.getProgram());
				} // for
			} // if

			// Print track information
			Track[] tracks = sequence.getTracks();
			if (tracks != null) {
				for (int i = 0; i < tracks.length; i++) {
					System.out.println("Track " + i + ":");
					Track track = tracks[i];
					for (int j = 0; j < track.size(); j++) {
						MidiEvent event = track.get(j);
						System.out.println("   tick " + event.getTick() + ", " + MessageInfo.toString(event.getMessage()));
					} // for
				} // for
			} // if
		} else {
			System.out.println("   Sequence is null.");
		} // if
	} // main
} // class SequenceInfo

