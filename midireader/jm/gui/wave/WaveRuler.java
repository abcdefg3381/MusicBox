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

package jm.gui.wave;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/*
 * A part of the jMusic audio wave file viewing package
 * @author Andrew Brown
 */
public class WaveRuler extends Panel implements MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2849086200878220600L;
	private Font font = new Font("Helvetica", Font.PLAIN, 9);
	private int markerWidth, startX;
	private WaveScrollPanel scrollPanel;
	private int startSecond = 0;
	private double tempRes;

	public WaveRuler() {
		super();
		setBackground(Color.lightGray);
		this.setSize(new Dimension(800, 20));
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	// Mouse Listener stubs
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dist = 5;
		if (e.getX() > startX + dist) {
			if (tempRes < 8.0) {
				tempRes = Math.round(tempRes / 2.0);
			} else {
				tempRes = Math.round(tempRes / 1.1);
			}
			if (tempRes < 1.0) {
				tempRes = 1.0;
			}
			if (tempRes > 2048.0) {
				tempRes = 2048.0; // 8000
			}
			scrollPanel.setResolution((int) Math.round(tempRes));
			startX = e.getX();
			repaint();
		}
		if (e.getX() < startX - dist) {
			if (tempRes < 8.0) {
				tempRes = Math.round(tempRes * 2.0);
			} else {
				tempRes = Math.round(tempRes * 1.1);
			}
			if (tempRes < 1.0) {
				tempRes = 1.0;
			}
			if (tempRes > 2048.0) {
				tempRes = 2048.0; // 8000
			}
			scrollPanel.setResolution((int) Math.round(tempRes));
			startX = e.getX();
			repaint();
		}
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
		tempRes = scrollPanel.getWaveView().getResolution();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.setCursor(new Cursor(13));
		scrollPanel.getWaveView().setResolution((int) tempRes);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.darkGray);
		g.setFont(font);
		int max = this.getSize().width;
		int startLoc = -1 * scrollPanel.getWaveView().getStartPos();
		int secInc = markerWidth;
		int tenthInc = (int) Math.round(markerWidth / 10.0);
		int centInc = (int) Math.round(markerWidth / 100.0);
		int milliInc = (int) Math.round(markerWidth / 1000.0);
		int res = scrollPanel.getWaveView().getResolution();
		startSecond =
				scrollPanel.getWaveView().getStartPos() / scrollPanel.getWaveView().getSampleRate()
						/ scrollPanel.getWaveView().getChannels();
		// 1000ths of seconds
		int counter = 0;
		g.setColor(Color.white);
		if (markerWidth > 20000) {
			for (int j = startLoc / res; j < max; j += secInc) {
				for (int k = 0; k < 10; k++) {
					for (int m = 0; m < 10; m++) {
						for (int i = 0; i < 10; i++) {
							if (counter % 10 != 0) {
								int pos = j + k * tenthInc + m * centInc + i * milliInc;
								g.drawLine(pos, getSize().height / 8 * 7, pos, getSize().height);
								if (markerWidth > 40000) {
									g.drawString("" + (startSecond + counter / 1000.0), pos + 2, getSize().height - 1);
								}
							}
							counter++;
						}
					}
				}
			}
		}

		// 100ths of seconds
		counter = 0;
		g.setColor(Color.magenta);
		if (markerWidth > 1200) {
			for (int j = startLoc / res; j < max; j += secInc) {
				for (int k = 0; k < 10; k++) {
					for (int i = 0; i < 10; i++) {
						if (counter % 10 != 0) {
							int pos = j + k * tenthInc + i * centInc;
							g.drawLine(pos, getSize().height / 4 * 3, pos, getSize().height);
							if (markerWidth > 4800) {
								g.drawString("" + (startSecond + counter / 100.0), pos + 2, getSize().height - 1);
							}
						}
						counter++;
					}
				}
			}
		}

		// 10th of seconds
		counter = 0;
		g.setColor(Color.blue);
		if (markerWidth > 150) {
			for (int j = startLoc / res; j < max; j += secInc) {
				for (int i = 0; i < 10; i++) {
					if (counter % 10 != 0) {
						int pos = j + i * tenthInc;
						g.drawLine(pos, getSize().height / 2, pos, getSize().height);
						if (markerWidth > 300) {
							g.drawString("" + (startSecond + counter / 10.0), pos + 2, getSize().height - 1);
						}
					}
					counter++;
				}
			}
		}

		// seconds
		counter = 0;
		g.setColor(Color.red);
		for (int i = startLoc / res; i < max; i += secInc) {
			g.drawLine(i, 1, i, getSize().height);
			if (markerWidth > 20 && markerWidth <= 300) {
				g.drawString("" + (startSecond + counter), i + 2, getSize().height - 1); // single
			} else if (markerWidth > 300) {
				g.drawString("" + (startSecond + counter / 1.0), i + 2, getSize().height - 1); // with
			}
			// decimal
			// place
			counter++;
		}

	}

	public void setMarkerWidth(int newWidth) {
		if (newWidth > 0) {
			this.markerWidth = newWidth;
			repaint();
		}
	}

	/*
	 * Set up a link with the object tht contains this ruler.
	 */
	public void setWaveScrollPanel(WaveScrollPanel scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

}