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
import jmqt.QTUtil;
import jm.util.View;
import jm.music.tools.Mod;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import quicktime.*;
import quicktime.app.time.Ticklish;
import quicktime.app.time.Timer;

/**
* Real Time MIDI playback for jMusic using Apple's QuickTime Java API.
* To use, create a score and pass it to the constructor of this class
* then call the startPlayback() method (only once!) to begin looping.
* Use suspendPlayback() and resumePlayback() to interupt the playback 
* once started.
* It is the responsibility of the calling app to
* update the scores as required, and numerous accessor methods are
* provided to adjust elements of the score or cycle process on the fly.
* A swing GUI is provided to adjust the scheduling 
* parameters as required. It is initiated by thecalling the settings() method.
*
* @author Andrew Brown
*/

public final class QTCycle implements JMC, ChangeListener, ActionListener, Ticklish{
	private Thread myThread;
	private QTUtil qtu1; 
	private Object obj;
	private Score score = new Score();
	private double tempo = 60.0; // bpm
	private double nextTempo = 0.0; // bpm, 0.0 == off
	private double scoreLength = CROTCHET;
        private double waitTime;
	private double anticipationAmt = 0.0;
        private int cycleCount = 0;
	// non accessable attributes
	private double time;
        private JSlider tempoSlider, sleepSlider, gapSlider;
	private JLabel tempoNumber, sleepNumber, gapNumber;
	private JButton startButton, stopButton;
	private boolean newScoreFlag = true;
        private Timer timer;

	/**
	* Create an empty object ready to add a score to with setScore().
	 */
	public QTCycle() {
		this(new Score( new Part( new Phrase(new Note(REST, 0.125)))));
	}
		
    /**
     * @param Score the first score object
     */
	public QTCycle(Score score) {
		this.obj = obj;
		this.score = score;
		this.scoreLength = score.getEndTime();
		this.tempo = score.getTempo();
		qtu1 = new QTUtil(this.score);
		// setup quicktime
		try {
			QTSession.open();
			// ms time for loop
			int ms = (int)(1000 * scoreLength * 60.0/tempo);
			// n times every n seconds - (3, 2, this) = two times every three seconds
                        //System.out.println("tempo = " + tempo + " length = " + scoreLength + " ms = " + ms); 
                        timer = new Timer(1000, ms, this);
			//timer.setActive(true);
			timer.setRate (1.0f);
		} catch (QTException e) {
			System.out.println("Timer error");
		}
	}
	
	/**
	* Begin the cycling playback of the scores.
	*/
	public void startPlayback() {
                try {
                        timer.setActive(true);
                } catch (QTException e) {
                        System.out.println("Timer playback error");
                }
	}
	
	/**
	* Shedule score playback and score updating when required.
	*/
	private void playCycle() {
                updateTempo();
                qtu1.setSpeed(tempo/60.0);
                if (newScoreFlag) {
                        qtu1.playback(score);
                        newScoreFlag = false;
                } else  qtu1.replay();
                cycleCount ++;
	}
	
	/**
	* Specifies the length of a score to be played.
	* Changes the interval between score update calls.
	*/
	public void setScoreLength(double newLength) {
	    if (newLength > 0.0) scoreLength = newLength;
            int ms = (int)(1000 * scoreLength * 60.0/tempo);
            try {
                // n times every n seconds - (3, 2, this) = two times every three seconds
                //System.out.println("tempo = " + tempo + " length = " + scoreLength + " ms = " + ms); 
                timer = new Timer(1000, ms, this);
                //timer.setActive(true);
                timer.setRate (1.0f);
            } catch (QTException e) {
                System.out.println("Timer error");
            }
	}
	
	/**
	* Reports the current score length setting.
	*/
	public double getScoreLength() {
	    return scoreLength;
	}
	
	/**
	* Updates the score to be played.
	*/
	public void setScore(Score s) {
	    this.score = s.copy();
	    scoreLength = s.getEndTime();
            tempo = s.getTempo();
	    newScoreFlag = true;
	}
	
	/**
	* Specifies the speed in beats per minute.
	*/
	public void setTempo(double newTempo) {
	    if (newTempo > 0.0) tempo = newTempo;
	}
	
	/**
	* Reports the current speed in beats per minute.
	*/
	public double getTempo() {
	    return tempo;
	}
	
	/**
	* Specifies the speed in beats per minute.
	*/
	public void setNextTempo(double newTempo) {
	    if (newTempo > 0.0) nextTempo = newTempo;
	}
    
	/**
	* Updates the current speed in beats per minute
	* based on a waiting tempo change.
	* This prevents gaps or overlaps in the playback
	* which can occur from the use of setTempo()
	* which updates immediatly.
	*/
	public void updateTempo() {
	    if (nextTempo != 0.0) {
	        tempo = nextTempo;
	        nextTempo = 0.0;
	    }
	}
    
    /**
    * Choose the balance between computing power
    * allocated to music timing compared to other tasks.
	* There are constants such as Thread.MAX_PRIORITY
    */
    public void setThreadPriority(int newPriority) {
        myThread.setPriority(newPriority);
    }
    
	/**
	* Specify the number ot count cycles from.
	*/
	public void setCycleCount(int newCount) {
	    this.cycleCount = newCount;
	}
	
	/**
	* Report the number of times the cycle method has been run.
	* Useful for counting beats , bars, phrases or 
	* whatever chunk of music was passed to this class.
	*/
	public int getCycleCount() {
	    return this.cycleCount;
	}
	
	/**
	* provide access to the quicktime utility object used by this class
	*/
	public QTUtil getQTUtil() {
		return this.qtu1;
	}
	
	/**
	* If paused, restart playback.
	*/
	public void resumePlayback() {
	    try{
                timer.setActive(true);
            } catch (QTException e) {
                        System.out.println("Timer resume error");
            }
            //myThread.resume();
	}
	
	/**
	* Pause playback.
	*/
	public void suspendPlayback() {
	    try {
                    timer.setActive(false);
                } catch (QTException e) {
                        System.out.println("Timer error");
                }
            /*
            myThread.suspend();
	    qtu1.stopPlayback();
            */
	}
	
	/**
	* Update the sleep time value..
	*/
	public void setAnticipationAmt( double newGap) {
	    this.anticipationAmt = newGap;
	}
	
	
	/**
	* Report the current loop anticipation time.
	*/
	public double getAnticipationAmt() {
	    return this.anticipationAmt;
	}
	
	/**
	* Open a GUI with sliders to adjust the scheduler loading.
	* Used to optimise the timing and efficiecy of the real time
	* QuickTime playback.
	*/
	public void settings() {
		JFrame f = new JFrame("Thread GUI");
		f.setSize(400, 200);
		Box masterBox = new Box(0);
		// tempo
		Box b1 = new Box(1);
		JLabel tempoTitle = new JLabel("Tempo");
		b1.add(tempoTitle);
		tempoSlider = new JSlider(1, 20, 200, 120);
		tempoSlider.addChangeListener(this);
		b1.add(tempoSlider);
		tempoNumber = new JLabel("120");
		b1.add(tempoNumber);
		masterBox.add(b1);
		
                        
		// time in ms to start score early to copmpensate for slow processing
		Box b3 = new Box(1);
		JLabel gapTitle = new JLabel("Anticipation");
		b3.add(gapTitle);
		gapSlider = new JSlider(1, 0, 100, (int)(anticipationAmt));
		gapSlider.addChangeListener(this);
		b3.add(gapSlider);
		gapNumber = new JLabel(Double.toString(anticipationAmt));
		b3.add(gapNumber);
		masterBox.add(b3);
		
		// buttons
		Box b4 = new Box(1);
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		b4.add(startButton);
		stopButton = new JButton("Stop");
		stopButton.addActionListener(this);
		b4.add(stopButton);
		masterBox.add(b4);
		
		f.getContentPane().add(masterBox);
		f.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == tempoSlider) changeTempo();
		if(e.getSource() == gapSlider) changeGap();
	}
	
	private void changeTempo() {
		tempo = (double)(tempoSlider.getValue());
		tempoNumber.setText(Integer.toString(tempoSlider.getValue()));
	}
	
	private void changeGap() {
		anticipationAmt = (double)(gapSlider.getValue());
		gapNumber.setText(Integer.toString(gapSlider.getValue()));
	}
	
	public void actionPerformed(ActionEvent ae) {
	    if(ae.getSource() == startButton) resumePlayback();
	    if(ae.getSource() == stopButton) suspendPlayback();
	}

	// required methods for Ticklish interface
	public boolean tickle(float er, int timer) {
		//System.out.println("Tickle: " + er + " " + timer);
		playCycle();
		return true;
	}

	public void timeChanged(int newTime) {
		//System.out.println("Time changed to " + newTime);
	}
}
