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
public class StopButton extends PlayerButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7001758702054345623L;

	public StopButton(PlayingDevice p) {
		super(p, "img/stop_up.png", "img/stop_over.png", "img/stop_down.png");

	}

	@Override
	public void doAction() {
		p.stop();
	}
}
