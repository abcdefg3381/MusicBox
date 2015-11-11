package jm.gui.show;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class NoteGraphic extends Component implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7339353426309000253L;

	NoteGraphic() {
		super();
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
		System.out.println("X is: " + me.getX());
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}
}
