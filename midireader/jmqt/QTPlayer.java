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
import jm.JMC;
 /**
 * This class displays a window to control playback of a jMusic score via Apple's QuickTime.
 * It requires the QTJava classes from Apple to be installed.
 * Installing QTJava can be done as part of a 'custom' install of QuickTime.
 * @author Andrew Brown
 */

public class QTPlayer  implements JMC {
    
    // constructor  
    public QTPlayer() {    }
    
    /**
    * Open a visual Player to play the score using Quicktime
    * This method is called most often, as in QT.play(score);
    * @param Score
    */
    public static void display(Score s) {
        display(s, 0, 300);
    }
    /**
    * Open a visual Player to Play the score using Quicktime
    * @param Score
    * @param int - the left-right location of the window
    * @param int - the up-down location of the window
    */
    public static void display(Score s, int xLoc, int yLoc) {
        Frame f = new Frame("jMusic Playback");
        // check if score is empty - if so add a note
        checkScore(s);
        QTUtil qtu = new QTUtil(s);
        QTPanel qtp = new QTPanel(qtu, s);
        f.add(qtp,"North");
        
        f.setLocation(xLoc,yLoc);
        /*addWindowListener (new java.awt.event.WindowAdapter () {
            public void windowClosing (java.awt.event.WindowEvent evt) {
               f.dispose();
            }
        )
        }; */
        f.pack();
        f.show();
    }
    
    /**
    * put a note in any empty score so we don't crash
    * - A hack - woops!
    */
    private static void checkScore(Score s) {
        if (s.size() == 0) { // add a note
            Note n = new Note(REST, 1.0);
            Phrase phr = new Phrase();
            phr.addNote(n);
            Part p = new Part();
            p.addPhrase(phr);
            s.addPart(p);
        }
    }
}