package eg.player.Temkine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Created on Oct 21, 2005
 */

public class ControlButton extends JPanel implements MouseListener {
	public static final Color backgroundColor = new Color(200, 255, 200);
	/**
	 * 
	 */
	private static final long serialVersionUID = 5951917430375062091L;

	/**
	 * This one is fully for testing purposes. Ignore!
	 */
	public static void main(String[] args) {
		JFrame f = new JFrame("test");
		f.getContentPane().add(new ControlButton("img/play_up.png", "img/play_over.png", "img/play_down.png"));
		f.setSize(100, 100);
		f.setVisible(true);
	}

	boolean mouseOver = false, press = false;

	Image up, over, down;

	public ControlButton(String fileUp, String fileOver, String fileDown) {
		MediaTracker mt = new MediaTracker(this);
		up = loadImage(fileUp);
		over = loadImage(fileOver);
		down = loadImage(fileDown);
		mt.addImage(up, 0);
		mt.addImage(over, 1);
		mt.addImage(down, 2);
		for (int i = 0; i < 3; i++) {
			try {
				mt.waitForID(i);
			} catch (InterruptedException e) {
			}
		}
		addMouseListener(this);
		setPreferredSize(new Dimension(up.getWidth(this), up.getHeight(this)));
	}

	/**
	 * Override this method to tell this button what to do if it is pressed.
	 */
	public void doAction() {
	}

	private Image loadImage(String filename) {
		return Toolkit.getDefaultToolkit().createImage(filename);
	}

	// ################################################
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseOver = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseOver = false;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		press = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		press = false;
		repaint();
		if (mouseOver) {
			doAction();
		}
	}

	// ################################################
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(backgroundColor);
		g.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		if (press && mouseOver) {
			g.drawImage(down, 0, 0, this);
		} else if (mouseOver) {
			g.drawImage(over, 0, 0, this);
		} else {
			g.drawImage(up, 0, 0, this);
		}
	}
}
