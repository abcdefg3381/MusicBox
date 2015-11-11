/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>

Copyright (C) 2000 Andrew Brown

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
import jm.JMC;


public class QTTrebleStaveActionHandler implements JMC, MouseMotionListener {
    private int clickedPosY, clickedPosX, selectedNote = -1;
    private QTTrebleStave theApp;
    
    // constructor
	QTTrebleStaveActionHandler(QTTrebleStave stave) {
		theApp = stave;
		// allow for negative rv's as a rest flag
	}

    //mouseMotionListener stubs
	public void mouseMoved(MouseEvent e) {}

	public void mousePressed(MouseEvent e) { 
	    // no notes yet?
		Integer lastX;
		if (theApp.notePositions.size() < 2) {
			lastX =  new Integer(theApp.getTotalBeatWidth() );
			
		} else {
			lastX = (Integer)theApp.notePositions.elementAt(theApp.notePositions.size() -2);
			
		}
	    if(e.getX() > (lastX.intValue() + 15) && e.getX() < theApp.getTotalBeatWidth() + 50) { 
	        theApp.playCurrentNote(selectedNote);
	        Phrase phr = theApp.getPhrase();
	        selectedNote = phr.size()-1;
	        //theApp.playCurrentNote(selectedNote);
			clickedPosY = e.getY()  + theApp.getStaveDelta();
			clickedPosX = e.getX();
	    } else {
			// check for a click on a note  - head?
			for(int i=0;i< theApp.notePositions.size(); i += 2) {
			    Integer tempX = (Integer)theApp.notePositions.elementAt(i);
				Integer tempY = (Integer)theApp.notePositions.elementAt(i+1);
				if(e.getX() > tempX.intValue() && e.getX() < tempX.intValue() + 15 && e.getY() + 
						theApp.getStaveDelta() > tempY.intValue() + 22 && e.getY() + 
						theApp.getStaveDelta() < tempY.intValue() + 35) {
					theApp.playCurrentNote(selectedNote);
					//theApp.playCurrentNote(selectedNote);
			        clickedPosY = e.getY()  + theApp.getStaveDelta();
			        clickedPosX = e.getX();
			    }
			}
        }
	    
	    if (selectedNote < 0) {
	        for(int j=0;j< theApp.notePositions.size() - 2; j += 2) {
	            Integer tempX = (Integer)theApp.notePositions.elementAt(j);
				Integer nextTempX = (Integer)theApp.notePositions.elementAt(j+2);
				if(e.getX() > tempX.intValue() + 15 && e.getX() < nextTempX.intValue()) {
	                theApp.playCurrentNote(selectedNote);
	                //theApp.playCurrentNote(selectedNote);
			        clickedPosY = e.getY()  + theApp.getStaveDelta();
			        clickedPosX = e.getX();
	            }
	        }
	    }
	}
	                
	                
	public void mouseDragged(MouseEvent e) {
		
		// move note down
		if(e.getY() + theApp.getStaveDelta() > clickedPosY + 2 && theApp.getPhrase().getNote(selectedNote).getPitch() != REST) { 
		    theApp.playCurrentNote(selectedNote);
		}
		
		// move note up
		if(e.getY() + theApp.getStaveDelta() < clickedPosY - 2  && theApp.getPhrase().getNote(selectedNote).getPitch() != REST) {
		    theApp.playCurrentNote(selectedNote); 
		}
    }
    
    public void mouseReleased(MouseEvent e) { }
}