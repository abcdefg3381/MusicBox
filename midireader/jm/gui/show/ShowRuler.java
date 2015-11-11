/* --------------------
 * A jMusic tool which displays a score as a
 * Common Practice Notation in a window.
 * @author Andrew Brown 
 * @version 1.0,Sun Feb 25 18:43
 * ---------------------
 */
package jm.gui.show;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/*

 <This Java Class is part of the jMusic API>

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

//--------------
//third class!!
//--------------
public class ShowRuler extends Canvas implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6130746429257738833L;
	private Font font = new Font("Helvetica", Font.PLAIN, 10);
	private int height = 15;
	private ShowPanel sp;
	// attributes
	private int startX;
	private int timeSig = 2;

	public ShowRuler(ShowPanel sp) {
		super();
		this.sp = sp;
		this.setSize((int) (sp.score.getEndTime() * sp.beatWidth), height);
		this.setBackground(Color.lightGray);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setCursor(new Cursor(13));
	}

	/**
	 * Report the height of this ruler panel.
	 */
	@Override
	public int getHeight() {
		return height;
	}

	// Mouse Listener stubs
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// System.out.println("Dragged");
		double beatWidth = sp.beatWidth;
		beatWidth += ((double) e.getX() - (double) startX) / 5.0;
		if (beatWidth < 1.0) {
			beatWidth = 1.0;
		}
		if (beatWidth > 256.0) {
			beatWidth = 256.0;
		}
		// System.out.println("beatWidth = "+beatWidth);
		sp.beatWidth = beatWidth;
		startX = e.getX();
		// sp.update();
		this.repaint();
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
		sp.update();
	}

	@Override
	public void paint(Graphics g) {
		double beatWidth = sp.beatWidth;
		g.setFont(font);
		for (int i = 0; i < (sp.score.getEndTime()); i++) {
			int xLoc = (int) Math.round(i * beatWidth);
			if (i % timeSig == 0) {
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
