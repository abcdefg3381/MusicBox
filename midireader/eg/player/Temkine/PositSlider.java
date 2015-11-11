package eg.player.Temkine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JPanel;

/*
 * Created on Oct 21, 2005
 */

/**
 * 
 * @author Mikhail Temkine
 * 
 *         An animated slider to show positing of the playing file.
 * 
 */
public class PositSlider extends JPanel {
	public static final Color outlineColor = new Color(20, 0, 150);
	/**
	 * 
	 */
	private static final long serialVersionUID = 903409607788186832L;
	double factor;
	boolean isPlaying;
	MIDIPlayer m;
	double posit;
	Sequencer s;
	double time, prevT;

	public PositSlider(Sequencer s, MIDIPlayer m) {
		this.s = s;
		this.m = m;
		time = 0;
		posit = 0;
		factor = 1.0;
		isPlaying = false;
		prevT = System.currentTimeMillis();
		setPreferredSize(new Dimension(200, 20));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				press(e);
			}
		});
	}

	public void haltClock() {
		// halt clocking the time
		isPlaying = false;
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(Color.white);
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		g.setColor(Color.cyan);
		g.fill(new Rectangle2D.Double(0, 0, getWidth() * posit, getHeight()));
		g.setColor(outlineColor);
		g.draw(new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
		g.setColor(Color.black);
		long secs = ((long) time) / 1000;
		int seconds = (int) (secs % 60);
		int minutes = (int) (secs / 60);
		String timeString;
		if (seconds < 10) {
			timeString = minutes + ":0" + seconds;
		} else {
			timeString = minutes + ":" + seconds;
		}
		g.drawString(timeString, getWidth() / 2 - 20, getHeight() - 5);
	}

	public void press(MouseEvent e) {
		double pos = (double) e.getX() / getWidth();
		Sequence sq = s.getSequence();
		if (sq != null && isPlaying) {
			long totalL = sq.getTickLength();
			s.setTickPosition((long) (pos * totalL));
			// s.setTempoInBPM((float)(factor*s.getTempoInBPM()));
			s.setTempoFactor((float) factor);
			time = (long) ((time) * (pos / posit));
			setPosition(pos);
		}
	}

	public void runClock() {
		// resume clocking the time
		prevT = System.currentTimeMillis();
		isPlaying = true;
	}

	/**
	 * Call to update the display (Called by a separate animated thread every 20
	 * milis)
	 */
	public void setPosition(double pos) {
		if (posit != pos) {
			this.posit = pos;
			if (posit == 1.0) {
				stopClock();
				posit = 0;
				m.songEnded();
			}
			if (isPlaying) // update time
			{
				long curT = System.currentTimeMillis();
				time += factor * (curT - prevT);
				prevT = curT;
			}
			repaint();
		}
		if (posit == 0) {
			time = 0;
		}
	}

	public void setTempoFactor(double factor) {
		this.factor = factor;
	}

	public void stopClock() {
		// halt clocking the time and set time to 0
		isPlaying = false;
		time = 0;
	}
}