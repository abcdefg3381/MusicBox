/*
 * <This Java Class is part of the jMusic API version 1.5, March 2004.>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

// GPL code for jMusic CPN.   
// Written by Al Christians (achrist@easystreet.com).
// Copyright  2002, Trillium Resources Corporation, Oregon's
// leading provider of unvarnished software.

package jm.gui.cpn;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.sound.midi.MidiUnavailableException;

import jm.constants.Durations;
import jm.constants.Pitches;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

// Use this class to enter notes for CPN by letter
// Sharps and flats are automatic according to the key
// signature.  

// Add a # to move the note up a half step
// beyond its place according to the key signature.

// Add a - to move the note down a half step
// beyond its place according to the key signature.

// Notes are A thru G for the notes, R for a rest  

// Rhythm value and octave are based on the preceding note.

// Notes are placed to be within 3 notes above or below the
// previous note.  An optional octave number after the note
// will override this default.

// Rhythm is the same as the previous note. Otherwise add
// the following after the note letter:

// W - Whole
// H - Half
// Q - Quarter
// N - Eighth
// X - Sixteenth
// T - Triplet
// & - After a note ties it to the previous note.  These
//     must be the same pitch.  Rhythm values are
//     added.  The next note will almost certainly require
//     an explicit rhythm value, because it will otherwise
//     default to the sum of the two tied notes.  

// Octave number (after the note letter) can be 1 to 9.
// Alternatively put > to move the note up 1 octave fron
// its default position or < to move it down 1 octave

// A period . adds 50% to the rhythm value of the note.
// Put the note letter first, then any of the rhythm or octave
// info that is needed.  Spaces are optional. Use for readability.

public class LetterNotesEditor extends Dialog

implements ActionListener, WindowListener {

	private static List inputList = new List(8);

	/**
	 * 
	 */
	private static final long serialVersionUID = -525881470566412920L;

	private static String crunchLine(String s) {
		StringBuffer b = new StringBuffer(s);
		for (int i = 0; i < b.length(); ++i) {
			if (b.charAt(i) < ' ') {
				b.setCharAt(i, ' ');
			}
		}
		for (int i = 0; i < b.length() - 1;) {
			if (b.charAt(i) == ' ') {
				if (b.charAt(i + 1) == ' ') {
					b.deleteCharAt(i);
				} else {
					i += 2;
				}
			} else {
				++i;
			}
		}
		return b.toString();
	}

	// Get the note letter for a pitch
	private static char getNoteLetter(int pitch, int keySignature) {

		int notePos = pitch % 12;
		switch (notePos) {
		case 0:
			return 'C';
		case 2:
			return 'D';
		case 4:
			return 'E';
		case 5:
			return 'F';
		case 7:
			return 'G';
		case 9:
			return 'A';
		case 11:
			return 'B';
		default:
			if (keySignature >= 0) {
				return getNoteLetter(pitch - 1, keySignature);
			} else {
				return getNoteLetter(pitch + 1, keySignature);
			}
		}
	}

	// See if the pitch is a big jump up
	static boolean pitchIsHigh(int pitchValue, int currentPitch, char letterValue, char currentLetter) {
		String notesString = "ABCDEFGABCDEFG";
		if (pitchValue > currentPitch + 8) {
			return true;
		} else if (pitchValue < currentPitch + 3) {
			return false;
		}
		int i, j;
		i = notesString.indexOf(currentLetter);
		j = notesString.indexOf(letterValue, i);
		return j > i + 3;
	}

	// See if the pitch is a big jump down
	static boolean pitchIsLow(int pitchValue, int currentPitch, char letterValue, char currentLetter) {
		String notesString = "ABCDEFGABCDEFG";
		if (pitchValue < currentPitch - 8) {
			return true;
		} else if (pitchValue > currentPitch - 3) {
			return false;
		}
		int i, j;
		i = notesString.indexOf(letterValue);
		j = notesString.indexOf(currentLetter, i);
		return j > i + 3;
	}

	private Note currentNote;
	private char currentNoteLetter = 'A';
	private int currentPitch = 69;

	private Label inputLabel;

	private TextArea notesInput;

	private Button okButton, playButton, cancelButton, copyButton;

	private Phrase phrase;

	private Stave stave;

	public LetterNotesEditor(Frame parentFrame) {
		super(parentFrame, "Set Music Parameters", true);
		initializeData();
		initializeButtons();
		initializeLabels();
		setSize(500, 300);
		placeControls();
		addWindowListener(this);
		setVisible(false);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			addNotes();
			dispose();
		} else if (e.getSource() == playButton) {
			playNotes();
		} else if (e.getSource() == cancelButton) {
			dispose();
		} else if (e.getSource() == copyButton) {
			if (inputList.getSelectedItem().length() > 0) {
				notesInput.setText(inputList.getSelectedItem());
			}
		}
	}

	// Add a note to the current phrase
	private void addNote(int pitchValue, char charValue) {
		Note newNote;
		int pv = pitchValue;
		if (currentNote != null) {
			newNote = currentNote.copy();
		} else {
			newNote = new Note();
		}
		if (charValue != 'R') {
			if (pv > currentPitch) {
				while (pitchIsHigh(pv, currentPitch, charValue, currentNoteLetter)) {
					pv -= 12;
				}
			} else {
				while (pitchIsLow(pv, currentPitch, charValue, currentNoteLetter)) {
					pv += 12;
				}
			}
		}
		if (noteIsSharp(charValue)) {
			++pv;
		}
		if (noteIsFlat(charValue)) {
			--pv;
		}
		newNote.setPitch(pv);
		phrase.add(newNote);
		currentNote = newNote;
		currentNoteLetter = charValue;
		if (currentNoteLetter != 'R') {
			currentPitch = pv;
		}
	}

	// Add the notes from the input edit control to the phrase.
	private void addNotes() {
		initializeUpdate();
		String s = notesInput.getText();
		for (int i = 0; i < s.length(); ++i) {
			processChar(Character.toUpperCase(s.charAt(i)));
		}
		inputList.add(crunchLine(s), 0);
		while (inputList.getItemCount() > 100) {
			inputList.remove(100);
		}
	}

	private void addOctave() {
		currentNote.setPitch(currentNote.getPitch() + 12);
		currentPitch = currentNote.getPitch();
	}

	private void adjustNoteByFactor(double x) {
		currentNote.setRhythmValue(x * currentNote.getRhythmValue());
		currentNote.setDuration(x * currentNote.getDuration());
	}

	private void dotNote() {
		adjustNoteByFactor(1.5);
	}

	private void flattenNote() {
		currentNote.setPitch(currentNote.getPitch() - 1);
		currentPitch = currentNote.getPitch();
	}

	// Show the screen and process input
	public void getNotes(Stave theStave) {
		phrase = theStave.getPhrase();
		stave = theStave;
		setLocation(200, 50);
		show();
	}

	private void initializeButtons() {
		okButton = new Button("Add Notes");
		playButton = new Button("Play Notes");
		cancelButton = new Button("Cancel");
		copyButton = new Button("Copy");
	}

	// Input area has multiple lines. You can cut and paste in
	// an entire song if you want to. Save it in a text file
	// with some other editor if you want to.
	private void initializeData() {
		notesInput = new TextArea("", 10, 100, TextArea.SCROLLBARS_BOTH);
	}

	private void initializeLabels() {
		inputLabel = new Label("Enter Note Names, R for Rest");
	}

	// Set up the initial default rhythm and octave values
	private void initializeUpdate() {
		if (phrase.size() > 0) {
			currentNote = phrase.getNote(phrase.size() - 1);
			if (currentNote.getPitch() >= 0) {
				currentPitch = currentNote.getPitch();
				currentNoteLetter = getNoteLetter(currentPitch, stave.getKeySignature());
			}
		} else {
			currentNote = null;
		}
	}

	private void makeEighthNote() {
		adjustNoteByFactor(Durations.EIGHTH_NOTE / currentNote.getRhythmValue());
	}

	private void makeHalfNote() {
		adjustNoteByFactor(Durations.HALF_NOTE / currentNote.getRhythmValue());
	}

	private void makeQuarterNote() {
		adjustNoteByFactor(Durations.QUARTER_NOTE / currentNote.getRhythmValue());
	}

	private void makeSixteenthNote() {
		adjustNoteByFactor(Durations.SIXTEENTH_NOTE / currentNote.getRhythmValue());
	}

	private void makeTriplet() {
		adjustNoteByFactor(Durations.QUAVER_TRIPLET / currentNote.getRhythmValue());
	}

	private void makeWholeNote() {
		adjustNoteByFactor(Durations.WHOLE_NOTE / currentNote.getRhythmValue());
	}

	private void moveToInterval(int from, int thru) {
		while (currentNote.getPitch() > thru) {
			currentNote.setPitch(currentNote.getPitch() - 12);
		}
		while (currentNote.getPitch() < from) {
			currentNote.setPitch(currentNote.getPitch() + 12);
		}
		if (currentNote.getPitch() == from) {
			if (currentNoteLetter == 'B') {
				currentNote.setPitch(currentNote.getPitch() + 12);
			}
		}
		if (currentNote.getPitch() == thru) {
			if (currentNoteLetter == 'C') {
				currentNote.setPitch(currentNote.getPitch() - 12);
			}
		}
		currentPitch = currentNote.getPitch();
	}

	private boolean noteIsFlat(char charValue) {
		boolean answer;
		String flatNotes;
		flatNotes = "BEADGCF";
		int numFlats = -stave.getKeySignature();
		if (charValue == 'R') {
			answer = false;
		} else if (numFlats > 0) {
			answer = flatNotes.indexOf(charValue) < numFlats;
		} else {
			answer = false;
		}
		return answer;
	}

	private boolean noteIsSharp(char charValue) {
		boolean answer;
		String sharpNotes;
		sharpNotes = "FCGDAEB";
		int numSharps = stave.getKeySignature();
		if (charValue == 'R') {
			answer = false;
		} else if (numSharps > 0) {
			answer = sharpNotes.indexOf(charValue) < numSharps;
		} else {
			answer = false;
		}
		return answer;
	}

	private void placeControls() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(layout);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.gridwidth = 5;
		c.gridheight = 1;

		c.gridx = 0;
		c.gridy = 0;
		layout.setConstraints(inputLabel, c);
		add(inputLabel);

		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 10;
		layout.setConstraints(notesInput, c);
		add(notesInput);

		c.gridx = 2;
		c.gridy = 12;
		c.gridheight = 1;
		c.gridwidth = 1;
		layout.setConstraints(okButton, c);
		add(okButton);
		okButton.addActionListener(this);

		c.gridx = 3;
		c.gridy = 12;
		c.gridwidth = 1;
		layout.setConstraints(playButton, c);
		add(playButton);
		playButton.addActionListener(this);

		c.gridx = 4;
		c.gridy = 12;
		c.gridwidth = 1;
		layout.setConstraints(cancelButton, c);
		add(cancelButton);
		cancelButton.addActionListener(this);

		c.gridx = 1;
		c.gridy = 15;
		c.gridwidth = 5;
		c.gridheight = 5;
		layout.setConstraints(inputList, c);
		add(inputList);

		c.gridx = 1;
		c.gridy = 20;
		c.gridwidth = 5;
		c.gridheight = 1;
		layout.setConstraints(copyButton, c);
		add(copyButton);
		copyButton.addActionListener(this);

	}

	// Play the notes in the input edit control to hear if
	// you want to add them to the phrase
	private void playNotes() {
		Phrase savePhrase = phrase.copy();
		initializeUpdate();
		String s = notesInput.getText();
		for (int i = 0; i < s.length(); ++i) {
			processChar(Character.toUpperCase(s.charAt(i)));
		}
		for (int i = 0; i < savePhrase.size(); ++i) {
			phrase.removeNote(0);
		}
		try {
			JmMidiPlayer midiPlayer = new JmMidiPlayer();
			Score sc = new Score();
			Part p = new Part();
			sc.addPart(p);
			p.addPhrase(phrase);
			Write.midi(sc, midiPlayer);
			midiPlayer.play();
			midiPlayer.close();
		} catch (MidiUnavailableException ex) {
			System.out.println("Midi Not Available");
		}
		phrase.empty();
		for (int i = 0; i < savePhrase.size(); ++i) {
			phrase.addNote(savePhrase.getNote(i));
		}
	}

	// Add the note for a letter to the phrase
	private void processChar(char c) {
		switch (c) {
		case 'A':
			addNote(Pitches.a4, c);
			break;
		case 'B':
			addNote(Pitches.b4, c);
			break;
		case 'C':
			addNote(Pitches.c4, c);
			break;
		case 'D':
			addNote(Pitches.d4, c);
			break;
		case 'E':
			addNote(Pitches.e4, c);
			break;
		case 'F':
			addNote(Pitches.f4, c);
			break;
		case 'G':
			addNote(Pitches.g4, c);
			break;
		case 'R':
			addNote(Pitches.REST, c);
			break;
		case '#':
		case '+':
			sharpenNote();
			break;
		case '-':
			flattenNote();
			break;
		case '>':
			addOctave();
			break;
		case '<':
			subtractOctave();
			break;

		case '1':
			moveToInterval(Pitches.cf1, Pitches.bs1);
			break;
		case '2':
			moveToInterval(Pitches.cf2, Pitches.bs2);
			break;
		case '3':
			moveToInterval(Pitches.cf3, Pitches.bs3);
			break;
		case '4':
			moveToInterval(Pitches.cf4, Pitches.bs4);
			break;
		case '5':
			moveToInterval(Pitches.cf5, Pitches.bs5);
			break;
		case '6':
			moveToInterval(Pitches.cf6, Pitches.bs6);
			break;
		case '7':
			moveToInterval(Pitches.cf7, Pitches.bs7);
			break;
		case '8':
			moveToInterval(Pitches.cf8, Pitches.bs8);
			break;
		case '9':
			moveToInterval(Pitches.cf9, Pitches.g9);
			break;

		case '.':
			dotNote();
			break;
		case 'H':
			makeHalfNote();
			break;
		case 'W':
			makeWholeNote();
			break;
		case 'Q':
			makeQuarterNote();
			break;
		case 'N':
			makeEighthNote();
			break;
		case 'T':
			makeTriplet();
			break;
		case 'X':
			makeSixteenthNote();
			break;
		case '&':
			tieNotes();
			break;

		default: {
		}
			break;
		}
	}

	private void sharpenNote() {
		currentNote.setPitch(currentNote.getPitch() + 1);
		currentPitch = currentNote.getPitch();
	}

	private void subtractOctave() {
		currentNote.setPitch(currentNote.getPitch() - 12);
		currentPitch = currentNote.getPitch();
	}

	private void tieNotes() {
		if (phrase.size() > 1) {
			phrase.removeLastNote();
			Note lastNote;
			lastNote = phrase.getNote(phrase.size() - 1);
			lastNote.setDuration(lastNote.getDuration() + currentNote.getDuration());
			lastNote.setRhythmValue(lastNote.getRhythmValue() + currentNote.getRhythmValue());
			currentNote = lastNote;
		}
	}

	/**
	 * Invoked when a window is activated.
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (e.getSource() == this) {
			dispose();
		}
	}

	/**
	 * Invoked when a window is de-activated.
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Invoked when a window is de-iconified.
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * Invoked when a window is iconified.
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
