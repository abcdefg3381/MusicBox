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

import jm.JMC;
import jm.music.data.*;
import jm.util.*;
import jm.audio.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.*;
import jmqt.*;

/**
 * This jMusic utility is designed to be extended by user classes. It will
 * provide a simple graphical interface that speeds up the cycle of
 * composing-auditioning-recomposing by minimising the need for recompiling
 * simple changes. It is especially useful for novice Java programmers.
 *
 * To use the HelperGUI class write a standard jMusic class that extends 
 * this class. It shopuld have a main() method and a constructor. 
 * Make a super() call in the constructor. Overwrite the
 * compose() method [which returns a Score object] and include the compositional
 * logic in that method.
 *
 * To render a score as an audio file, an Instrument array needs to be declared 
 * and assigned to the Instrument array varianble 'insts' - which is already 
 * declared in the HelperGUI class. As in this
 * example code fragment:
 *            Instrument sine = new SineInst(44100);
 *            Instrument[] instArray = {sine};
 *            insts = instArray;
 * 
 * There are five variables each with a slider that can be accessed to change
 * values in the composition. Each variable, named 'variableA' to 'variableE',
 * returns an integer values between 0 and 127.
 *
 * @author Andrew Brown
 */

// To make this class use swing composents replace the GUI attributes (Button etc.)
// with ones starting with the letter J (JButton etc.) and uncomment the
// javax.swing import statement above and use the commented 
// getContentPane().add(controls) statement below.

public class QTHelperGUI extends Frame implements JMC, ActionListener, AdjustmentListener{
	//-------------------
	// Attributes
	//-------------------
	protected Score score = new Score();
	private Button composeBtn, playBtn, stopBtn, showBtn, printBtn, saveBtn, renderBtn;
	private TextField midiName, audioName;
        private Scrollbar sliderA, sliderB, sliderC, sliderD, sliderE;
        private Label labelA, labelB, labelC, labelD, labelE;
	private QTUtil qtu = new QTUtil();
	protected Instrument[] insts;
        protected int variableA, variableB, variableC, variableD, variableE;
	
	/*
        // for testing only
        public static void main(String[] args) {
		new HelperGUI();
	}*/
        
	//-------------------
	// Constructor
	//-------------------
	public QTHelperGUI() {
		super("jMusic Helper Interface");
		//Panel bg = this.getContentPane();
                this.setLayout(new BorderLayout());
		Panel controls = new Panel();
		controls.setLayout(new GridLayout(7, 1));
		//this.getContentPane().add(controls);
		this.add(controls, "North");
                Panel sliders = new Panel();
                sliders.setLayout(new GridLayout(6, 1));
                this.add(sliders, "Center");
                
		Panel topControls = new Panel();
		Panel middleControls = new Panel();
		Panel middle2Controls = new Panel();
		Panel bottomControls = new Panel();
		Label create = new Label("Create and Review");
		create.setAlignment(Label.CENTER);
		controls.add(create);
		controls.add(topControls);
		//Label display = new Label("Display the score");
		//display.setAlignment(Label.CENTER);
		//controls.add(display);
		controls.add(middleControls);
		Label midi = new Label("Save as...");
		midi.setAlignment(Label.CENTER);
		controls.add(midi);
		controls.add(middle2Controls);
		//Label audio = new Label("Save as an audio file");
		//audio.setAlignment(Label.CENTER);
		//controls.add(audio);
		controls.add(bottomControls);
		Label variables = new Label("Control compositional parameters");
		variables.setAlignment(Label.CENTER);
		controls.add(variables);
		
		// butons
		composeBtn = new Button("Compose");
		composeBtn.addActionListener(this);
		topControls.add(composeBtn);
		
		playBtn = new Button("QT Play");
		playBtn.addActionListener(this);
		playBtn.setEnabled(false);
		topControls.add(playBtn);
		
		stopBtn = new Button("QT Stop");
		stopBtn.addActionListener(this);
		stopBtn.setEnabled(false);
		topControls.add(stopBtn);
		
		showBtn = new Button("View.show()");
		showBtn.addActionListener(this);
		showBtn.setEnabled(false);
		middleControls.add(showBtn);
		
		printBtn = new Button("View.print()");
		printBtn.addActionListener(this);
		printBtn.setEnabled(false);
		middleControls.add(printBtn);
		
		saveBtn = new Button("Write.midi()");
		saveBtn.addActionListener(this);
		saveBtn.setEnabled(false);
		middle2Controls.add(saveBtn);
		
		midiName = new TextField("FileName.mid", 20);
		middle2Controls.add(midiName);
		
		renderBtn = new Button("Write.au()");
		renderBtn.addActionListener(this);
		renderBtn.setEnabled(false);
		bottomControls.add(renderBtn);
		
		audioName = new TextField("FileName.au", 20);
		bottomControls.add(audioName);
		//sliders
                Panel sliderAPanel = new Panel(new BorderLayout());
                labelA = new Label(" variableA = 0     ");
                sliderAPanel.add(labelA, "West");
                sliderA = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127+ 15));
                sliderA.addAdjustmentListener(this);
                sliderAPanel.add(sliderA, "Center");
                sliders.add(sliderAPanel);
                
                Panel sliderBPanel = new Panel(new BorderLayout());
                labelB = new Label(" variableB = 0     ");
                sliderBPanel.add(labelB, "West");
                sliderB = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127+ 15));
                sliderB.addAdjustmentListener(this);
                sliderBPanel.add(sliderB, "Center");
                sliders.add(sliderBPanel);
                
                Panel sliderCPanel = new Panel(new BorderLayout());
                labelC = new Label(" variableC = 0     ");
                sliderCPanel.add(labelC, "West");
                sliderC = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127+ 15));
                sliderC.addAdjustmentListener(this);
                sliderCPanel.add(sliderC, "Center");
                sliders.add(sliderCPanel);
                
                Panel sliderDPanel = new Panel(new BorderLayout());
                labelD = new Label(" variableD = 0     ");
                sliderDPanel.add(labelD, "West");
                sliderD = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127+ 15));
                sliderD.addAdjustmentListener(this);
                sliderDPanel.add(sliderD, "Center");
                sliders.add(sliderDPanel);
                
                Panel sliderEPanel = new Panel(new BorderLayout());
                labelE = new Label(" variableE = 0     ");
                sliderEPanel.add(labelE, "West");
                sliderE = new Scrollbar(Scrollbar.HORIZONTAL, 0, 15, 0, (127+ 15));
                sliderE.addAdjustmentListener(this);
                sliderEPanel.add(sliderE, "Center");
                sliders.add(sliderEPanel);
                
                // filler
                Label filler = new Label(" ");
                sliders.add(filler);
                
		this.pack();
		this.setVisible(true);
	}
	
	//-------------------
	// methods
	//-------------------

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == composeBtn) composeScore();
		if (e.getSource() == playBtn) playScore();
		if (e.getSource() == stopBtn) stopScore();
		if (e.getSource() == showBtn) showScore();
		if (e.getSource() == printBtn) printScore();
		if (e.getSource() == saveBtn) saveScore();
		if (e.getSource() == renderBtn) renderScore();
	}
	
	private void composeScore() {
		playBtn.setEnabled(true);
		stopBtn.setEnabled(true);
		showBtn.setEnabled(true);
		printBtn.setEnabled(true);
		saveBtn.setEnabled(true);
		
		renderBtn.setEnabled(true);
		// get composed score
		score = compose();		
	}
	
	/**
	* The compose() method should be overridden by classes that extend this class.
	*/
	protected Score compose() {
   		// Simple example composition
                Phrase phrase = new Phrase();
		Score s = new Score(new Part(phrase));
		//for(int i = 0; i < 8; i++) {
			Note n = new Note (48 + (int)(Math.random() * variableA), 0.5 + variableB * 0.25);
			phrase.addNote(n);
		//}
		
		//Instrument[] tempInsts = {new SineInst(22000)};
		//insts = tempInsts;
		return s;
	}
	
	private void playScore() {
		qtu.stopPlayback();
		qtu.playback(score);
	}
	
	private void stopScore() {
		qtu.stopPlayback();
	}
	
	private void showScore() {
		View.show(score, this.getSize().width + 15, 0);
	}
	
	private void printScore() {
		View.print(score);
	}
	
	private void saveScore() {
		String fileName = midiName.getText().trim();
		if (fileName != null){
			Write.midi(score, fileName);
		} else Write.midi(score);
	}
	
	private void renderScore() {
		String fileName = audioName.getText().trim();
		if (fileName != null){
			Write.au(score, fileName, insts);
		} else Write.au(score, "RenderedFile.au", insts);
	}

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
}
