/**
 * 
 */
package musicbox.logic.form;

import java.util.ArrayList;
import java.util.List;

import musicbox.db.entity.Event;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.entity.Note;

/**
 * @author LIU Xiaofan
 * 
 */
public class NodeFormer {
	public static final int MOTIF_NOTE = 4;
	public static final int NORMAL = 1;
	public static final int RHYTHMIC = 2;
	public static final int TONAL = 3;
	private MusicalNetwork network;

	/**
	 * @param network
	 */
	public NodeFormer(MusicalNetwork network) {
		this.network = network;
	}

	/**
	 * @param type
	 */
	public void formNode(int type) {

		// form note list according to network type
		List<Note> noteList = new ArrayList<Note>();
		switch (type) {
		case NORMAL:
			// form Normal Network
			for (Event event : network.getEventList()) {
				// TODO not distinct list anymore
				// if (!noteList.contains(event.getNote())) {
				noteList.add(event.getNote());
				// }
			}
			network.setNodeList(noteList);
			break;
		case RHYTHMIC:
			// form Rhythmic Network
			for (Event event : network.getEventList()) {
				event.getNote().setPitch(60);
				event.getNote().setId(0);
			}
			// TODO not distinct list anymore
			// for (Event event : network.getEventList()) {
			// if (!noteList.contains(event.getNote())) {
			// noteList.add(event.getNote());
			// }
			// }
			for (Event event : network.getEventList()) {
				for (Note note : noteList) {
					if (event.getNote().getPitch() == note.getPitch() && event.getNote().getDuration() == note.getDuration()) {
						event.setNote(note);
					}
				}
			}
			network.setNodeList(noteList);
			break;
		case TONAL:
			// form Tonal Network
			for (Event event : network.getEventList()) {
				event.getNote().setDuration(1.0f);
				event.getNote().setId(0);
			}
			// TODO not distinct list anymore
			// for (Event event : network.getEventList()) {
			// if (!noteList.contains(event.getNote())) {
			// noteList.add(event.getNote());
			// }
			// }
			for (Event event : network.getEventList()) {
				for (Note note : noteList) {
					if (event.getNote().getPitch() == note.getPitch() && event.getNote().getDuration() == note.getDuration()) {
						event.setNote(note);
					}
				}
			}
			network.setNodeList(noteList);
			break;
		case MOTIF_NOTE:
			// form motif/note network
			MotiveFinder mf = new MotiveFinder();
			network.setNodeList(mf.findMotiveFromNetwork(network));
			break;
		default:
			break;
		}
	}

}
