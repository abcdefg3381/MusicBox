/**
 * 
 */
package musicbox;

import java.io.File;
import java.io.FilenameFilter;

import musicbox.db.entity.MusicalNetwork;
import musicbox.logic.util.midi.MIDIReader;

/**
 * @author EF501b
 * 
 */
public class FastProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FastProgram fp = new FastProgram();
		fp.readFiles();
		fp.formNetwork();
		fp.exportAdjMatrix();
	}

	/**
	 * 
	 */
	private void exportAdjMatrix() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void formNetwork() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void readFiles() {
		File folder = new File("clarinet");
		System.out.println(folder.exists());
		File[] midiFiles = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("mid");
			}
		});
		MIDIReader mr = new MIDIReader();
		for (File file : midiFiles) {
			System.out.println(file);
			MusicalNetwork mn = mr.importFile(file, 72);
			System.out.println(mn.getEventList().size());
		}
	}

}
