package eg.player.Temkine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

/*
 * Created on Oct 22, 2005
 *
 */

/**
 * @author Mikhail Temkine
 * 
 */
public class BrowseButton extends JButton implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3425195763961323804L;
	public JFileChooser fileChooser;
	javax.swing.filechooser.FileFilter filter;
	MIDIPlayer p;

	public BrowseButton(MIDIPlayer p) {
		super("Browse...");
		this.p = p;
		fileChooser = new JFileChooser("C:/Documents and Settings/Mikhail Temkine/My Documents/My Music/beatles MIDI");
		fileChooser.setFileFilter(new MIDIFilter());
		fileChooser.setMultiSelectionEnabled(true);
		addActionListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int option = fileChooser.showOpenDialog(this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File[] f = fileChooser.getSelectedFiles();
			for (File element : f) {
				p.addSong(element);
			}
		}
	}

}
