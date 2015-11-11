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

package jm.gui.helper;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

import jm.JMC;
import jm.audio.Instrument;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

/**
 * This jMusic utility is designed to be extended by user classes. It will
 * provide a simple graphical interface that speeds up the cycle of
 * composing-auditioning-recomposing by minimising the need for recompiling
 * simple changes. It is especially useful for novice Java programmers.
 * 
 * To use the HelperGUI class write a standard jMusic class that extends this
 * class. It shopuld have a main() method and a constructor. Make a super() call
 * in the constructor. Overwrite the compose() method [which returns a Score
 * object] and include the compositional logic in that method.
 * 
 * To render a score as an audio file, an Instrument array needs to be declared
 * and assigned to the Instrument array varianble 'insts' - which is already
 * declared in the HelperGUI class. As in this example code fragment: Instrument
 * sine = new SineInst(44100); Instrument[] instArray = {sine}; insts =
 * instArray;
 * 
 * There are five variables each with a slider that can be accessed to change
 * values in the composition. Each variable, named 'variableA' to 'variableE',
 * returns an integer values between 0 and 127.
 * 
 * @author Andrew Brown
 */

// To make this class use swing composents replace the GUI attributes (Button
// etc.)
// with ones starting with the letter J (JButton etc.) add a
// javax.swing import statement and use the commented
// getContentPane().add(controls) statement below.

public class HelperGUI extends Frame implements JMC, ActionListener, AdjustmentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7572243468532058194L;
	private Label commentLabA, commentLabB, commentLabC, commentLabD, commentLabE;
	private Button composeBtn, playBtn, stopBtn, showBtn, sketchBtn, histogramBtn, printBtn, saveBtn, renderBtn, notateBtn,
			audioViewBtn, audioPlayBtn, audioStopBtn, xmlOpenBtn, xmlSaveBtn;
	protected Instrument[] insts;
	private Label labelA, labelB, labelC, labelD, labelE;
	private TextField midiName, audioName;
	private MidiSynth ms = new MidiSynth();
	// Is there are open JavaSound synth?
	private boolean playing = false;
	// -------------------
	// Attributes
	// -------------------
	protected Score score = new Score();
	private Scrollbar sliderA, sliderB, sliderC, sliderD, sliderE;
	protected int variableA, variableB, variableC, variableD, variableE;

	/*
	 * // for testing only public static void main(String[] args) { new
	 * HelperGUI(); }
	 */

	// -------------------
	// Constructor
	// -------------------
	public HelperGUI() {
		super("jMusic Helper GUI");
		// Panel bg = this.getContentPane();
		this.setLayout(new BorderLayout());
		Panel controls = new Panel();
		controls.setLayout(new GridLayout(13, 1));
		// this.getContentPane().add(controls);
		this.add(controls, "North");
		Panel sliders = new Panel();
		sliders.setLayout(new GridLayout(6, 1));
		this.add(sliders, "Center");

		Panel composeBtnPanel = new Panel();
		Panel viewBtnPanel1 = new Panel();
		Panel viewBtnPanel2 = new Panel();
		Panel midiSaveBtnPanel = new Panel();
		Panel midiPlayBtnPanel = new Panel();
		Panel audioViewBtnPanel = new Panel();
		Panel audioSaveBtnPanel = new Panel();
		Panel xmlBtnPanel = new Panel();
		// Panel audioPlayBtnPanel = new Panel();
		Label create = new Label("Create and View");
		create.setAlignment(Label.CENTER);
		controls.add(create);
		controls.add(composeBtnPanel);
		controls.add(viewBtnPanel1);
		controls.add(viewBtnPanel2);
		Label midi = new Label("MIDI Options");
		midi.setAlignment(Label.CENTER);
		controls.add(midi);
		controls.add(midiPlayBtnPanel);
		controls.add(midiSaveBtnPanel);
		Label audio = new Label("Audio Options");
		audio.setAlignment(Label.CENTER);
		controls.add(audio);
		controls.add(audioSaveBtnPanel);
		controls.add(audioViewBtnPanel);
		// controls.add(audioPlayBtnPanel);
		Label xmlHeader = new Label("XML Options");
		xmlHeader.setAlignment(Label.CENTER);
		controls.add(xmlHeader);
		controls.add(xmlBtnPanel);

		Label variables = new Label("Control compositional parameters");
		variables.setAlignment(Label.CENTER);
		controls.add(variables);

		// butons
		composeBtn = new Button("Compose");
		composeBtn.addActionListener(this);
		composeBtnPanel.add(composeBtn);

		playBtn = new Button("Play.midi()");
		playBtn.addActionListener(this);
		playBtn.setEnabled(false);
		midiPlayBtnPanel.add(playBtn);

		stopBtn = new Button("Stop MIDI");
		stopBtn.addActionListener(this);
		stopBtn.setEnabled(false);
		midiPlayBtnPanel.add(stopBtn);

		showBtn = new Button("View.show()");
		showBtn.addActionListener(this);
		showBtn.setEnabled(false);
		viewBtnPanel1.add(showBtn);

		notateBtn = new Button("View.notate()");
		notateBtn.addActionListener(this);
		notateBtn.setEnabled(false);
		viewBtnPanel1.add(notateBtn);

		printBtn = new Button("View.print()");
		printBtn.addActionListener(this);
		printBtn.setEnabled(false);
		viewBtnPanel1.add(printBtn);

		sketchBtn = new Button("View.sketch()");
		sketchBtn.addActionListener(this);
		sketchBtn.setEnabled(false);
		viewBtnPanel2.add(sketchBtn);

		histogramBtn = new Button("View.histogram()");
		histogramBtn.addActionListener(this);
		histogramBtn.setEnabled(false);
		viewBtnPanel2.add(histogramBtn);

		saveBtn = new Button("Write.midi()");
		saveBtn.addActionListener(this);
		saveBtn.setEnabled(false);
		midiSaveBtnPanel.add(saveBtn);

		midiName = new TextField("FileName.mid", 20);
		midiSaveBtnPanel.add(midiName);

		audioViewBtn = new Button("View.au()");
		audioViewBtn.addActionListener(this);
		audioViewBtn.setEnabled(false);
		audioViewBtnPanel.add(audioViewBtn);

		renderBtn = new Button("Write.au()");
		renderBtn.addActionListener(this);
		renderBtn.setEnabled(false);
		audioSaveBtnPanel.add(renderBtn);

		audioName = new TextField("FileName.au", 20);
		audioSaveBtnPanel.add(audioName);

		audioPlayBtn = new Button("Play.au()");
		audioPlayBtn.addActionListener(this);
		audioPlayBtn.setEnabled(false);
		audioViewBtnPanel.add(audioPlayBtn);
		// audioPlayBtnPanel.add(audioPlayBtn);

		xmlOpenBtn = new Button("Read.xml()");
		xmlOpenBtn.addActionListener(this);
		xmlOpenBtn.setEnabled(true);
		xmlBtnPanel.add(xmlOpenBtn);

		xmlSaveBtn = new Button("Write.xml()");
		xmlSaveBtn.addActionListener(this);
		xmlSaveBtn.setEnabled(false);
		xmlBtnPanel.add(xmlSaveBtn);

		// not yet implemented!
		/*
		 * audioStopBtn = new Button("Stop Audio");
		 * audioStopBtn.addActionListener(this); audioStopBtn.setEnabled(false);
		 * audioPlayBtnPanel.add(audioStopBtn);
		 */

		// sliders
		Panel sliderAPanel = new Panel(new GridLayout(1, 3));
		labelA = new Label(" variableA = 0");
		sliderAPanel.add(labelA);
		sliderA = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127 + 15));
		sliderA.addAdjustmentListener(this);
		sliderAPanel.add(sliderA);
		commentLabA = new Label(" No Comment ");
		sliderAPanel.add(commentLabA);
		sliders.add(sliderAPanel);

		Panel sliderBPanel = new Panel(new GridLayout(1, 3));
		labelB = new Label(" variableB = 0");
		sliderBPanel.add(labelB);
		sliderB = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127 + 15));
		sliderB.addAdjustmentListener(this);
		sliderBPanel.add(sliderB);
		commentLabB = new Label(" No Comment ");
		sliderBPanel.add(commentLabB);
		sliders.add(sliderBPanel);

		Panel sliderCPanel = new Panel(new GridLayout(1, 3));
		labelC = new Label(" variableC = 0");
		sliderCPanel.add(labelC);
		sliderC = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127 + 15));
		sliderC.addAdjustmentListener(this);
		sliderCPanel.add(sliderC);
		commentLabC = new Label(" No Comment ");
		sliderCPanel.add(commentLabC);
		sliders.add(sliderCPanel);

		Panel sliderDPanel = new Panel(new GridLayout(1, 3));
		labelD = new Label(" variableD = 0");
		sliderDPanel.add(labelD);
		sliderD = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127 + 15));
		sliderD.addAdjustmentListener(this);
		sliderDPanel.add(sliderD);
		commentLabD = new Label(" No Comment ");
		sliderDPanel.add(commentLabD);
		sliders.add(sliderDPanel);

		Panel sliderEPanel = new Panel(new GridLayout(1, 3));
		labelE = new Label(" variableE = 0");
		sliderEPanel.add(labelE);
		sliderE = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127 + 15));
		sliderE.addAdjustmentListener(this);
		sliderEPanel.add(sliderE);
		commentLabE = new Label(" No Comment ");
		sliderEPanel.add(commentLabE);
		sliders.add(sliderEPanel);

		// filler
		Label filler = new Label(" ");
		sliders.add(filler);
		// this.pack();
		this.setBounds(0, 0, 350, 600);
		this.setVisible(true);

		composeBtn.requestFocus();
	}

	// -------------------
	// methods
	// -------------------

	/**
	 * Handle the button clicks in the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == composeBtn) {
			composeScore();
		}
		if (e.getSource() == playBtn) {
			playScore();
		}
		if (e.getSource() == stopBtn) {
			stopScore();
		}
		if (e.getSource() == showBtn) {
			showScore();
		}
		if (e.getSource() == notateBtn) {
			notateScore();
		}
		if (e.getSource() == printBtn) {
			printScore();
		}
		if (e.getSource() == sketchBtn) {
			sketchScore();
		}
		if (e.getSource() == histogramBtn) {
			histogramScore();
		}
		if (e.getSource() == saveBtn) {
			saveScore();
		}
		if (e.getSource() == renderBtn) {
			renderScore();
		}
		if (e.getSource() == audioViewBtn) {
			viewAudio();
		}
		if (e.getSource() == audioPlayBtn) {
			playAudio();
		}
		if (e.getSource() == audioStopBtn) {
			stopAudio();
		}
		if (e.getSource() == xmlOpenBtn) {
			xmlOpen();
		}
		if (e.getSource() == xmlSaveBtn) {
			xmlSave();
		}
	}

	@Override
	public void adjustmentValueChanged(java.awt.event.AdjustmentEvent ae) {
		if (ae.getSource() == sliderA) {
			labelA.setText(" variableA = " + sliderA.getValue());
			variableA = new Integer(sliderA.getValue()).intValue();
		}
		if (ae.getSource() == sliderB) {
			labelB.setText(" variableB = " + sliderB.getValue());
			variableB = new Integer(sliderB.getValue()).intValue();
		}
		if (ae.getSource() == sliderC) {
			labelC.setText(" variableC = " + sliderC.getValue());
			variableC = new Integer(sliderC.getValue()).intValue();
		}
		if (ae.getSource() == sliderD) {
			labelD.setText(" variableD = " + sliderD.getValue());
			variableD = new Integer(sliderD.getValue()).intValue();
		}
		if (ae.getSource() == sliderE) {
			labelE.setText(" variableE = " + sliderE.getValue());
			variableE = new Integer(sliderE.getValue()).intValue();
		}
	}

	/**
	 * This method should be overridden by classes that extend the HelperGUI
	 * class.
	 */
	protected Score compose() {
		// Simple example composition
		Phrase phrase = new Phrase();
		Score s = new Score(new Part(phrase));
		// for(int i = 0; i < 8; i++) {
		Note n = new Note(48 + (int) (Math.random() * variableA), 0.5 + variableB * 0.25);
		phrase.addNote(n);
		// }

		// Instrument[] tempInsts = {new SineInst(44100)};
		// insts = tempInsts;
		return s;
	}

	private void composeScore() {
		// get composed score
		score = compose();
		makeBtnsVisible();
	}

	/** Display a score in the histogram viewer */
	private void histogramScore() {
		View.histogram(score, 0, this.getSize().width + 15, 0);
	}

	private void makeBtnsVisible() {
		// show availible buttons
		playBtn.setEnabled(true);
		stopBtn.setEnabled(true);
		showBtn.setEnabled(true);
		notateBtn.setEnabled(true);
		printBtn.setEnabled(true);
		sketchBtn.setEnabled(true);
		histogramBtn.setEnabled(true);
		saveBtn.setEnabled(true);
		xmlSaveBtn.setEnabled(true);
		// show audio buttons if audio used
		if (insts != null) {
			renderBtn.setEnabled(true);
		}
	}

	/** Display a score with the notation viewer */
	private void notateScore() {
		View.notate(score, this.getSize().width + 15, 0);
	}

	/** Playback the audio file using jMusic real time audio */
	private void playAudio() {
		Play.au(audioName.getText().trim(), false);
	}

	/** Start MIDI playback */
	private void playScore() {
		if (playing) {
			ms.stop();
		}
		try {
			ms.play(score);
			playing = true;
		} catch (Exception e) {
			System.err.println("JavaSound MIDI Playback Error:" + e);
			return;
		}
	}

	/** print score data to the CLI */
	private void printScore() {
		View.print(score);
	}

	/** Save the score as an audio file */
	private void renderScore() {
		String fileName = audioName.getText().trim();
		if (fileName != null) {
			Write.au(score, fileName, insts);
		} else {
			Write.au(score, "RenderedFile.au", insts);
			audioName.setText("RenderedFile.au");
		}
		audioViewBtn.setEnabled(true);
		audioPlayBtn.setEnabled(true);
		// audioStopBtn.setEnabled(true);
	}

	/** Save score as a standard MIDI file */
	private void saveScore() {
		String fileName = midiName.getText().trim();
		if (fileName != null) {
			Write.midi(score, fileName);
		} else {
			Write.midi(score);
		}
	}

	/**
	 * Specify the value for vaiable A
	 */
	public void setVariableA(int value) {
		setVariableA(value, "No Comment");
	}

	/**
	 * Specify the value and comment for vaiable A
	 */
	public void setVariableA(int value, String comment) {
		if (value >= 0 && value <= 127) {
			sliderA.setValue(value);
			labelA.setText(" variableA = " + value + "  ");
			variableA = value;
		}
		if (comment.length() > 18) {
			commentLabA.setText(" " + comment.substring(0, 16) + "...");
		} else {
			commentLabA.setText(" " + comment + " ");
		}
	}

	/**
	 * Specify the value for vaiable B
	 */
	public void setVariableB(int value) {
		setVariableB(value, "No Comment");
	}

	/**
	 * Specify the value and comment for vaiable B
	 */
	public void setVariableB(int value, String comment) {
		if (value >= 0 && value <= 127) {
			sliderB.setValue(value);
			labelB.setText(" variableB = " + value + "  ");
			variableB = value;
		}
		if (comment.length() > 18) {
			commentLabB.setText(" " + comment.substring(0, 16) + "...");
		} else {
			commentLabB.setText(" " + comment + " ");
		}
	}

	/**
	 * Specify the value for vaiable C
	 */
	public void setVariableC(int value) {
		setVariableC(value, "No Comment");
	}

	/**
	 * Specify the value and string for vaiable C
	 */
	public void setVariableC(int value, String comment) {
		if (value >= 0 && value <= 127) {
			sliderC.setValue(value);
			labelC.setText(" variableC = " + value + "  ");
			variableC = value;
		}
		if (comment.length() > 18) {
			commentLabC.setText(" " + comment.substring(0, 16) + "...");
		} else {
			commentLabC.setText(" " + comment + " ");
		}
	}

	/**
	 * Specify the value for vaiable D
	 */
	public void setVariableD(int value) {
		setVariableD(value, "No Comment");
	}

	/**
	 * Specify the value and string for vaiable D
	 */
	public void setVariableD(int value, String comment) {
		if (value >= 0 && value <= 127) {
			sliderD.setValue(value);
			labelD.setText(" variableD = " + value + "  ");
			variableD = value;
		}
		if (comment.length() > 18) {
			commentLabD.setText(" " + comment.substring(0, 16) + "...");
		} else {
			commentLabD.setText(" " + comment + " ");
		}
	}

	/**
	 * Specify the value for vaiable E
	 */
	public void setVariableE(int value) {
		setVariableE(value, "No Comment");
	}

	/**
	 * Specify the value and string for vaiable E
	 */
	public void setVariableE(int value, String comment) {
		if (value >= 0 && value <= 127) {
			sliderE.setValue(value);
			labelE.setText(" variableE = " + value + "  ");
			variableE = value;
		}
		if (comment.length() > 18) {
			commentLabE.setText(" " + comment.substring(0, 16) + "...");
		} else {
			commentLabE.setText(" " + comment + " ");
		}
	}

	/** Display a score with the show score viewer */
	private void showScore() {
		View.show(score, this.getSize().width + 15, 0);
	}

	/** Display a score with the sketch score viewer */
	private void sketchScore() {
		View.sketch(score, this.getSize().width + 15, 0);
	}

	/** Stop playback of the audio file */
	private void stopAudio() {
		// stop playback if possible here!
	}

	/** halt MIDI playback */
	private void stopScore() {
		if (playing) {
			ms.stop();
			playing = false;
		}
	}

	/** Show the saved audio file in the wave viewer */
	private void viewAudio() {
		View.au(audioName.getText().trim(), this.getSize().width + 5, 0);
	}

	/*
	 * Import a jMusic XML score to be the current score.
	 */
	private void xmlOpen() {
		Read.xml(score);
		makeBtnsVisible();
	}

	/*
	 * Save the current scare as a jMusic XML file.
	 */
	private void xmlSave() {
		FileDialog fd = new FileDialog(new Frame(), "Save as a jMusic XML file...", FileDialog.SAVE);
		fd.show();
		if (fd.getFile() != null) {
			jm.util.Write.xml(score, fd.getDirectory() + fd.getFile());
		}
	}
}
