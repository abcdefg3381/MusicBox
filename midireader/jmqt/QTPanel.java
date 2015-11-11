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
import jm.music.data.*;

/**
 * A class which plays a jMusic score via Apple's QuickTime.
 * It requires the QTJava classes.
 * @author Andrew Brown
 */

//send class
public class QTPanel extends Panel implements ActionListener, AdjustmentListener {	
    private QTUtil qtu;
    private Button b, b2;
    private Scrollbar tempoScroll;
    //private Label tempoReadout;
    private Label tempoTitle;
    private Score s;
    
    //-------------
    //constructors
    //-------------
	public QTPanel(QTUtil qtu, Score score) { 
	    this.qtu = qtu;
	    this.s = score;
	    this.setLayout(new BorderLayout());
	    //title
	    Label text = new Label("QT Controls");
	    text.setAlignment(1);
	    this.add(text, "North");
	    // button panel
	    Panel bp = new Panel();
	    //create a button to start the tune playing
	    b = new Button ("Play");
	    b.addActionListener(this); //(new ActionListener () {
	    bp.add(b);
	    
	    //create a button to stop the tune playing
	    b2 = new Button ("Stop");
	    b2.addActionListener(this);
	    bp.add (b2);
	    
	    this.add(bp, "Center");
	    // tempo panel
	    Panel tp = new Panel();
	    tp.setLayout(new BorderLayout());
	    tempoTitle = new Label("Tempo: "+(int)(60*qtu.getSpeed())+ " bpm");
	    tempoTitle.setAlignment(1);
	    tp.add(tempoTitle, "North");
	    // tempo slider
	    tempoScroll = new Scrollbar(0, (int)(60*qtu.getSpeed()), 1, 20, 301);
	    tempoScroll.addAdjustmentListener(this);
	    tp.add(tempoScroll, "Center");
	    // tempo readout
	    //tempoReadout = new Label("120");
	    //tempoReadout.setAlignment(1);
	    //tp.add( tempoReadout, "South");
	    this.add(tp, "South");
    }
        
	//-------------------
	// Auxillary methods
	//-------------------
    public void setQTUtil(QTUtil qtu) {
        this.qtu = qtu;
    }
	
	//--------------------------------------
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();
      
        if (obj == b) {
                qtu.stopPlayback();
                qtu.playback(s);
        }
        if (obj == b2) {
            qtu.stopPlayback();
        }
    }
    
    //--------------------------------------
    public void adjustmentValueChanged(AdjustmentEvent e) {
        tempoTitle.setText( "Tempo: " + Integer.toString(tempoScroll.getValue())+ " bpm");
        qtu.setSpeed( (double)tempoScroll.getValue()/60.0);
        s.setTempo( tempoScroll.getValue());
    }

}