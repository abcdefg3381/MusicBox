package eg.player.Temkine;

import java.io.File;

/*
 * Created on Oct 22, 2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Mikhail Temkine
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SequenceItem {
	File file;

	/**
	 * @param s
	 * @param file
	 */
	public SequenceItem(File file) {
		this.file = file;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SequenceItem)) {
			return false;
		}
		return ((SequenceItem) o).getFile().equals(file);
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return file.getName();
	}
}
