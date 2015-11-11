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
public class PlayButton extends PlayerButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9159317263113119123L;

	public PlayButton(PlayingDevice p) {
		super(p, "img/play_up.png", "img/play_over.png", "img/play_down.png");

	}

	@Override
	public void doAction() {
		p.play();
	}
}
