/*
 *
 * <This Java Class is part of the jMusic API version 1.5, March 2004.>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 *
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

// GPL code for jMusic CPN.   
// Written by Al Christians (achrist@easystreet.com).
// Copyright  2002, Trillium Resources Corporation, Oregon's
// leading provider of unvarnished software.
package jm.gui.cpn;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;

import jm.music.data.Note;
import jm.music.data.Phrase;

public class PhraseViewer extends Dialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 746023899503558901L;
	private DecimalFormat decimalFormat = new DecimalFormat("#####.######");
	private Phrase phrase;
	private ScrollPane scrollPane = new ScrollPane();
	private Stave stave;

	private TextArea textArea = new TextArea(20, 120);

	public PhraseViewer(Frame parentFrame) {
		super(parentFrame, "Phrase Detail Display", true);
		setSize(500, 400);
		placeControls();
		addWindowListener(this);
		setVisible(false);
		pack();
	}

	private void getNoteText(Note n) {
		textArea.append("Pitch " + n.getPitch());
		textArea.append("   Start " + decimalFormat.format(n.getSampleStartTime()));
		textArea.append("   Rhythm " + decimalFormat.format(n.getRhythmValue()));
		textArea.append("   Dur " + decimalFormat.format(n.getDuration()));
		textArea.append("   Offset " + decimalFormat.format(n.getOffset()));
		textArea.append("   Vol " + n.getDynamic());
		textArea.append("\n");
	}

	private void getPhraseText() {
		getStaveText();
		textArea.append("Phrase has " + phrase.size() + " notes.\n");

		textArea.append("Tempo " + decimalFormat.format(phrase.getTempo()));
		textArea.append("    Numerator " + phrase.getNumerator());
		textArea.append("    Denominator " + phrase.getDenominator());
		textArea.append("\n");

		for (int i = 0; i < phrase.size(); ++i) {
			getNoteText(phrase.getNote(i));
		}
	}

	private void getStaveText() {
		textArea.append("Stave " + stave.getTitle() + "   Metre " + decimalFormat.format(stave.getMetre()) + "\n");
	}

	private void placeControls() {
		scrollPane.add(textArea);
		setLayout(new BorderLayout());
		add("Center", scrollPane);
	}

	public void showPhrase(Stave theStave, Phrase thePhrase, int locX, int locY) {
		stave = theStave;
		phrase = thePhrase;
		getPhraseText();
		setLocation(locX, locY);
		show();
	}

	/**
	 * Invoked when a window is activated.
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * Invoked when a window has been closed.
	 */
	@Override
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * Invoked when a window is in the process of being closed. The close
	 * operation can be overridden at this point.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		if (e.getSource() == this) {
			dispose();
			// System.exit(0);
		}
	}

	/**
	 * Invoked when a window is de-activated.
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Invoked when a window is de-iconified.
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * Invoked when a window is iconified.
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
