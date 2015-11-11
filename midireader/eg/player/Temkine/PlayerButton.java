package eg.player.Temkine;

/*
 * Created on Oct 22, 2005
 *
 */

/**
 * @author Mikhail Temkine a super class of all 3 buttons for this small program
 */
public class PlayerButton extends ControlButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8898146263721572325L;
	PlayingDevice p;

	public PlayerButton(PlayingDevice p, String fileUp, String fileOver, String fileDown) {
		super(fileUp, fileOver, fileDown);
		this.p = p;
	}

}
