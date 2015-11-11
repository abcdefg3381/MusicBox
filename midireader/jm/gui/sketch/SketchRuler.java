/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>:40  2001

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

package jm.gui.sketch;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A jMusic tool which disketchScorelays a score as a simple 'piano roll'
 * disketchScorelay in a window. The tool disketchScorelays a jMusic class as a
 * simple piano roll view. To use it write: new ViewScore(scoreName); Where
 * scoreName is the jMusic Score object. Alternately: new ViewScore(scoreName,
 * xpos, ypos); Where xpos and ypos are intergers sketchScoreecifying the
 * topleft position of the window. This is useful if you want to use ViewScore
 * in conjunction with some other GUI interface which is already positioned in
 * the top left corner of the screen.
 * 
 * @author Andrew Brown and Andrew Troedson
 * @version 1.0,Sun Feb 25 18:43
 */
// --------------
// Ruler class!!
// --------------
public class SketchRuler extends Canvas implements MouseListener, MouseMotionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5960233820870121631L;
	private int barNumbCount = 2;
	private Font font = new Font("Helvetica", Font.PLAIN, 10);
	private int height = 15;
	private SketchScore sketchScore;
	// attributes
	private int startX;

	public SketchRuler(SketchScore sketchScore) {
		super();
		this.sketchScore = sketchScore;
		this.setSize((int) (SketchScore.score.getEndTime() * sketchScore.beatWidth), height);
		this.setBackground(Color.lightGray);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		this.setCursor(new Cursor(13));
	}

	/**
	 * Report on the set height of this panel
	 */
	@Override
	public int getHeight() {
		return height;
	}

	// key listener stubs
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\b') {
			repaint();
		}
	}

	// Mouse Listener stubs
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// System.out.println("Dragged");
		double beatWidth = sketchScore.beatWidth;
		beatWidth += ((double) e.getX() - (double) startX) / 5.0;
		if (beatWidth < 1.0) {
			beatWidth = 1.0;
		}
		if (beatWidth > 256.0) {
			beatWidth = 256.0;
		}
		// System.out.println("beatWidth = "+beatWidth);
		sketchScore.beatWidth = beatWidth;
		startX = e.getX();
		sketchScore.update();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	// mouseMotionListener stubs
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	// get the position of inital mouse click
	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Pressed");
		this.setCursor(new Cursor(10));
		startX = e.getX();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.setCursor(new Cursor(13));
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		double beatWidth = sketchScore.beatWidth;
		g.setFont(font);
		for (int i = 0; i < (sketchScore.getSketchScoreArea().getNewWidth()); i++) {
			int xLoc = (int) Math.round(i * beatWidth);
			if (i % barNumbCount == 0) {
				g.drawLine(xLoc, 0, xLoc, height);
				if (beatWidth > 15) {
					g.drawString("" + i, xLoc + 2, height - 2);
				}
			} else {
				g.drawLine(xLoc, height / 2, xLoc, height);
			}
		}
	}
}
