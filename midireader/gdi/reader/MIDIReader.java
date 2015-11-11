package gdi.reader;

import gdi.reader.db.entity.Edge;
import gdi.reader.gui.ReaderGUI;
import gdi.reader.logic.ReaderLogic;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MIDIReader {

	private static MIDIReader instance;

	public static MIDIReader getInstance() {
		if (instance == null) {
			instance = new MIDIReader();
		}
		return instance;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("MIDI file reader 1.0\nFinal Year Project\nLIU Xiaofan");
		getInstance().getGUI();
		// getInstance().getLogic().startBootstrap();

		new Edge();
	}

	private ReaderGUI readerGUI;

	private ReaderLogic readerLogic;

	public void exit() {
		getInstance().getLogic().stopBootstrap();
		System.exit(0);
	}

	public ReaderGUI getGUI() {
		if (readerGUI == null) {
			readerGUI = new ReaderGUI();
		}
		return readerGUI;
	}

	public InitialContext getInitialContext() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReaderLogic getLogic() {
		if (readerLogic == null) {
			readerLogic = new ReaderLogic();
		}
		return readerLogic;
	}
}
