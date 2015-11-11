package musicbox.gui.viz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import maggie.network.gui.GuiUtils;
import musicbox.MainProgram;
import musicbox.db.entity.Event;
import musicbox.db.entity.MusicalNetwork;
import musicbox.logic.form.SkylineProcessor;
import musicbox.logic.util.midi.MIDIPlayer;
import musicbox.logic.util.midi.MIDIWriter;

/**
 * This class draws a network as MIDI piano roll plays on pop-up plane.<br>
 * This class is also able to skyline transfer one network and show them in red
 * color.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MIDIViewer {

	class MIDIVisualizer extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2942065431267793521L;

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			int preferedWidth = 0;
			int lowestPitch = 108;
			int highestPitch = 0;
			for (Event e : network.getEventList()) {
				if (e.getEndTick() > preferedWidth) {
					preferedWidth = (int) e.getEndTick();
				}
				if (e.getNote().getPitch() < lowestPitch) {
					lowestPitch = e.getNote().getPitch();
				}
				if (e.getNote().getPitch() > highestPitch) {
					highestPitch = e.getNote().getPitch();
				}
			}
			for (Event e : network.getEventList()) {
				g2d.fillRect((int) e.getStartTick() / 20, (highestPitch - e.getNote().getPitch()) * 5,
						(int) (e.getEndTick() - e.getStartTick()) / 20, 5);
			}
			g2d.setColor(Color.red);
			for (Event e : network.getSkyLine()) {
				g2d.fillRect((int) e.getStartTick() / 20, (highestPitch - e.getNote().getPitch()) * 5,
						(int) (e.getEndTick() - e.getStartTick()) / 20, 5);
			}
			this.setPreferredSize(new Dimension(preferedWidth / 20, (highestPitch - lowestPitch) * 5));
		}
	}

	private JPanel buttonPanel = null;

	private JButton closeButton = null;

	private JPanel jContentPane = null;

	private JFrame jFrame = null;

	private JScrollPane jScrollPane = null;

	private MIDIVisualizer mViz = null;

	protected MusicalNetwork network;

	private JButton playButton = null;

	private JButton saveButton = null;

	private JButton skylineButton = null;

	private JButton snapshotButton = null;
	private JButton stopButton;

	public MIDIViewer(MusicalNetwork network) {
		this.network = network;
		initialize();
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getPlayButton(), null);
			buttonPanel.add(getStopButton());
			buttonPanel.add(getSaveButton(), null);
			buttonPanel.add(getSnapshotButton(), null);
			buttonPanel.add(getSkylineButton(), null);
			buttonPanel.add(getCloseButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("CLOSE");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					onClose();
					jFrame.dispose();
				}
			});
		}
		return closeButton;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jFrame.setContentPane(getJContentPane());
			jFrame.pack();
		}
		return jFrame;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(800, 400));
			jScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			jScrollPane.setViewportView(getMIDIVisualizer());
		}
		return jScrollPane;
	}

	private JComponent getMIDIVisualizer() {
		if (mViz == null) {
			mViz = new MIDIVisualizer();
		}
		return mViz;
	}

	/**
	 * This method initializes playButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPlayButton() {
		if (playButton == null) {
			playButton = new JButton();
			playButton.setText("PLAY");
			playButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {

					MIDIPlayer.play(MIDIWriter.writeEventsToSequence(network.getEventList()));
				}
			});
		}
		return playButton;
	}

	/**
	 * This method initializes exportButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("Export");
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic().saveMIDItoFile(network);
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes skylineButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSkylineButton() {
		if (skylineButton == null) {
			skylineButton = new JButton();
			skylineButton.setText("Skyline Transfer");
			skylineButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

					long sc = System.currentTimeMillis();
					SkylineProcessor sp = new SkylineProcessor(network);
					sp.findSkyline();
					sc = System.currentTimeMillis() - sc;
					System.out.println("cost:" + sc + "ms");

					jFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					mViz.repaint();
				}
			});
		}
		return skylineButton;
	}

	/**
	 * This method initializes snapshotButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSnapshotButton() {
		if (snapshotButton == null) {
			snapshotButton = new JButton();
			snapshotButton.setText("Snap Shot");
			snapshotButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					GuiUtils.drawComponentToFile(mViz, new File(network.getName()), "jpg");
				}
			});
		}
		return snapshotButton;
	}

	private JButton getStopButton() {
		if (stopButton == null) {
			stopButton = new JButton("STOP");
			stopButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MIDIPlayer.stop();
				}
			});
		}
		return stopButton;
	}

	private void initialize() {
		GuiUtils.centerWindow(getJFrame());
		getJFrame().setTitle(network.getName());
		getJFrame().setVisible(true);
		getJFrame().addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				onClose();
			}

		});
	}

	private void onClose() {
		MIDIPlayer.stop();
	}
}
