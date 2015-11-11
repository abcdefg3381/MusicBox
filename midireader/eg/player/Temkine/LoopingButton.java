package eg.player.Temkine;

/*
 * Created on Oct 22, 2005
 *
 */

/**
 * @author Mikhail Temkine
 * 
 *         Options button button
 */
public class LoopingButton extends ControlButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6284226786947798412L;
	LoopMenu m;
	MIDIPlayer p;

	public LoopingButton(MIDIPlayer p, int selIndex) {
		super("img/options1_up.png", "img/options1_over.png", "img/options1_down.png");
		this.p = p;
		m = new LoopMenu(p, selIndex);
	}

	@Override
	public void doAction() {
		m.show(this, 0, getHeight());
	}
}
