package eg.player.Temkine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JPanel;

public class TrackControls extends JPanel implements MouseListener, MouseMotionListener, MIDIPlayerListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5816572061301653190L;
	int curBox = -1;
	MIDIPlayer p;
	Sequencer seq;
	LinkedList<Integer> toggles;
	int tracks = 3;
	final int trackSpace = 25, instSpace = 0;

	public TrackControls(MIDIPlayer p) {
		super();
		this.p = p;
		this.seq = p.p.getSequencer();

		p.addPlayerListener(this);

		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(new Dimension(200, 400));
		setPreferredSize(new Dimension(200, 400));

		toggles = new LinkedList<Integer>();
	}

	// ---------------------------------------------
	private int getCurBox(int mx, int my) {
		// x-coordinate
		int x = mx - (93 + instSpace);
		int bbx = -1;
		if (x < 0) {
			return -1;
		} else if (x < 40) {
			bbx = 0;
		} else {
			x -= 50;
			if (x > 40 && x <= 0) {
				return -1;
			}
			bbx = tracks;
			x -= 44;
			if (x > 0) {
				return -1;
			}
		}
		// y-coordinate
		int y = (my - 5) % trackSpace;
		int boxN = (my - 5) / trackSpace;
		if (y >= 0 && y < 20 && boxN < tracks) {
			bbx += boxN;
		} else {
			bbx = -1;
		}
		return bbx;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMoved(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int bbx = getCurBox(arg0.getX(), arg0.getY());
		if (bbx != curBox) {
			curBox = bbx;
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		curBox = getCurBox(arg0.getX(), arg0.getY());
		if (curBox != -1) {
			if (toggles.contains(curBox)) {
				toggles.remove(toggles.indexOf(curBox));
				if (curBox / tracks == 0) {
					seq.setTrackMute(curBox % tracks, false);
				} else {
					seq.setTrackSolo(curBox % tracks, false);
				}
			} else {
				toggles.add(curBox);
				if (curBox / tracks == 0) {
					seq.setTrackMute(curBox % tracks, true);
				} else {
					seq.setTrackSolo(curBox % tracks, true);
				}
			}
			// seq.setTrackMute(curBox%tracks, true);
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(Color.white);
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		for (Integer integer : toggles) {
			int cur = integer;
			int bx = cur / tracks;
			int by = cur % tracks;
			if (bx == 0) {
				g.setColor(Color.red);
				g.fill(new Rectangle2D.Double(93 + instSpace, trackSpace * by + 5, 40, 20));
			} else {
				g.setColor(Color.yellow);
				g.fill(new Rectangle2D.Double(143 + instSpace, trackSpace * by + 5, 40, 20));
			}
		}

		g.setColor(Color.black);
		for (int i = 0; i < tracks; i++) {
			g.drawString("Track #" + (i + 1), 10, 20 + trackSpace * i);
			g.drawString("Mute", 100 + instSpace, 20 + trackSpace * i);
			g.drawString("Solo", 150 + instSpace, 20 + trackSpace * i);
			g.draw(new Rectangle2D.Double(93 + instSpace, trackSpace * i + 5, 40, 20));
			g.draw(new Rectangle2D.Double(143 + instSpace, trackSpace * i + 5, 40, 20));
		}
		g.setColor(Color.blue);
		if (curBox >= 0) {
			int bx = curBox / tracks;
			int by = curBox % tracks;
			if (bx == 0) {
				g.draw(new Rectangle2D.Double(91 + instSpace, trackSpace * by + 3, 44, 24));
			} else {
				g.draw(new Rectangle2D.Double(141 + instSpace, trackSpace * by + 3, 44, 24));
			}
		}
	}

	@Override
	public void songChanged(MIDIPlayer p) {
		Sequence s = p.getCurrentSequence();
		tracks = s.getTracks().length;
		curBox = -1;

		setSize(new Dimension(200, trackSpace * tracks + 5));
		setPreferredSize(new Dimension(200, trackSpace * tracks + 5));
		repaint();
	}

}
