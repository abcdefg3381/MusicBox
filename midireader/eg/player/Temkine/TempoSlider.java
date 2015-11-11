package eg.player.Temkine;

import java.awt.Dimension;

import javax.sound.midi.Sequencer;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TempoSlider extends JSlider implements ChangeListener, MIDIPlayerListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8985365922076782050L;
	int curTempo = 120;
	MIDIPlayer p;
	Sequencer seq;
	PositSlider slid;
	JLabel tempoLabel;

	public TempoSlider(MIDIPlayer p, JLabel tempoLabel) {
		super(10, 240, 120);
		this.p = p;
		this.seq = p.p.getSequencer();
		this.slid = p.slid;
		this.tempoLabel = tempoLabel;
		addChangeListener(this);
		p.addPlayerListener(this);

		setPreferredSize(new Dimension(400, 20));
	}

	@Override
	public void songChanged(MIDIPlayer p) {
		seq.setTempoFactor(1);
		curTempo = (int) seq.getTempoInBPM();
		setValue(curTempo);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// seq.setTempoInBPM(getValue());
		seq.setTempoFactor((float) getValue() / curTempo);
		tempoLabel.setText("Tempo: " + getValue());
		slid.setTempoFactor(getValue() / curTempo);
	}
}
