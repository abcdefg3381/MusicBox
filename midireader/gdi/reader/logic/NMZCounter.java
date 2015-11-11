package gdi.reader.logic;

import gdi.reader.MIDIReader;
import gdi.reader.db.handler.AbstractMusicNoteHandler;
import gdi.reader.db.handler.EdgeHandler;

import javax.naming.NamingException;

public class NMZCounter {

	public static int[] getStatistics() {

		int[] nmz = new int[2];

		// n: number of nodes
		try {
			AbstractMusicNoteHandler abstractNoteHandler =
					(AbstractMusicNoteHandler) MIDIReader.getInstance().getInitialContext()
							.lookup("AbstractMusicNoteHandlerBean/local");

			nmz[0] = abstractNoteHandler.getAbstractMusicNoteNumber();
		} catch (NamingException e) {
			e.printStackTrace();
		}

		// m: number of links
		try {
			EdgeHandler edgeHandler =
					(EdgeHandler) MIDIReader.getInstance().getInitialContext().lookup("EdgeHandlerBean/local");
			nmz[1] = edgeHandler.getEdgeNumber();
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return nmz;
	}

}
