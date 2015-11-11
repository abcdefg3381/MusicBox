package eg.player.Temkine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

/*
 * Created on Oct 25, 2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Mikhail Temkine
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class LoopMenu extends JPopupMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9080948588016564816L;

	private class LoopRadioButton extends JRadioButtonMenuItem implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8536790215431667483L;
		MIDIPlayer p;
		SongLooper sl;

		public LoopRadioButton(String name, SongLooper sl, MIDIPlayer p) {
			super(name);
			this.p = p;
			this.sl = sl;
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			p.setSongLooper(sl);
		}
	}

	ButtonGroup bg;
	int selIndex;

	public LoopMenu(MIDIPlayer p, int selIndex) {
		this.selIndex = selIndex;
		bg = new ButtonGroup();
		SongLooper random = new SongLooper.RandomLooper(p);
		p.setSongLooper(random);
		addRadioButton("Normal", new SongLooper.NormalLooper(p), p);
		addRadioButton("Repeat", new SongLooper.RepeatLooper(p), p);
		addRadioButton("Play in order", new SongLooper.IncrementalLooper(p), p);
		addRadioButton("Random", random, p);

	}

	public void addRadioButton(String name, SongLooper sl, MIDIPlayer p) {
		LoopRadioButton b = new LoopRadioButton(name, sl, p);
		add(b);
		bg.add(b);
		if (bg.getButtonCount() == selIndex) {
			b.setSelected(true);
		}
	}
}
