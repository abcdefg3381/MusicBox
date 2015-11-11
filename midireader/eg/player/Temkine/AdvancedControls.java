package eg.player.Temkine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.border.EtchedBorder;

public class AdvancedControls extends JWindow {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7557930637170484740L;
	MIDIPlayer p;

	public AdvancedControls(MIDIPlayer p) {
		super(p);
		this.p = p;

		JPanel mainP = new JPanel(new BorderLayout());
		mainP.setBackground(ControlButton.backgroundColor);
		mainP.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue),
						BorderFactory.createLineBorder(Color.blue)),
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue),
						BorderFactory.createEtchedBorder(EtchedBorder.RAISED))));

		JPanel bottom = new JPanel(new FlowLayout());
		JLabel ll = new JLabel("Tempo: 120");
		bottom.add(new TempoSlider(p, ll));
		bottom.add(ll);

		JScrollPane scrollTracks = new JScrollPane(new TrackControls(p));
		scrollTracks.getVerticalScrollBar().setUnitIncrement(10);
		mainP.add(scrollTracks, BorderLayout.CENTER);
		mainP.add(bottom, BorderLayout.SOUTH);

		add(mainP);
		setSize(600, 200);
	}
}
