package eg.player.Temkine;

import java.io.File;

/*
 * Created on Oct 21, 2005
 */

public class MIDIFilter extends javax.swing.filechooser.FileFilter {
	public MIDIFilter() {
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname.isDirectory()) {
			return true;
		} else {
			if (pathname.getName().indexOf(".mid") != -1) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public String getDescription() {
		return "MIDI Files (*.mid)";
	}
}