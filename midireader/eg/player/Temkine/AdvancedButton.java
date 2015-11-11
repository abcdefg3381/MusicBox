package eg.player.Temkine;

public class AdvancedButton extends ControlToggleButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1465926472599072862L;
	MIDIPlayer p;

	public AdvancedButton(MIDIPlayer p) {
		super("img/options2_up.png", "img/options2_over.png", "img/options2_down.png", "img/options2_downOver.png");
		this.p = p;
	}

	@Override
	public void doAction(boolean enabled) {
		p.slideAdvanced(enabled);
	}

}
