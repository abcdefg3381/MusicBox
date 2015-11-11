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

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.util.Vector;
import java.net.URL; 
import jm.JMC;
import jm.music.data.*;
import jm.gui.cpn.*;
import jmqt.*;

/**
* Uses QuickTime to playback the music
* Requires Quicktime for Java to be installed!!
* @author Andrew Brown
*/

public abstract class QTTrebleStave extends TrebleStave implements JMC{

    protected QTUtil qtu;
    
    
    // constructor
    public QTTrebleStave() { 
        this(new Phrase());
    }
    
    public QTTrebleStave(Phrase phr) {
        super();
        QTTrebleStaveHandler qttsah = new QTTrebleStaveHandler(this);
    }
    
    
    public int getStaveDelta() {
        return staveDelta;
    }

    public void playPhrase() {
        if(phrase.size() == 0) return;
        if(qtu != null) qtu.stopPlayback();
        Score s = new Score();
        Part p = new Part();
        p.addPhrase(phrase);
        s.addPart(p);
        qtu = new QTUtil(s);
        qtu.playback(s);
    }
    /**
    * Stops QuickTime playback of the music
    * Requires Quicktime for Java to be installed!!
    */
    public void stopPhrase() {
        if(qtu != null) qtu.stopPlayback();
    }
    
    /**
    * Play the current note
    */
    public void playCurrentNote(int noteNumb) {
        if(qtu == null) qtu = new QTUtil();
        qtu.playOneNote(phrase.getNote(noteNumb), 0);
    }

}