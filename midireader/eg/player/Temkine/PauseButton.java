package eg.player.Temkine;

/*
 * Created on Oct 22, 2005
 *
 */

/**
 * @author Mikhail Temkine
 * 
 *         "Play" button
 */
public class PauseButton extends PlayerButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1622169028172614108L;

	public PauseButton(PlayingDevice p) {
		super(p, "img/pause_up.png", "img/pause_over.png", "img/pause_down.png");

	}

	@Override
	public void doAction() {
		p.pause();
	}
}
