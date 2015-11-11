/**
 * 
 */
package musicbox.logic.form;

import java.util.ArrayList;
import java.util.List;

import maggie.network.entity.Node;
import musicbox.db.entity.Composition;
import musicbox.db.entity.Event;
import musicbox.db.entity.Motif;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.entity.Note;

/**
 * @author LIU Xiaofan
 * 
 */
public class MotiveFinder {
	List<Node> nodeList = new ArrayList<Node>();
	List<Node> partialNodeList = new ArrayList<Node>();

	/**
	 * @param noteList
	 * @return
	 */
	public List<? extends Node> findMotiveFromComposition(List<Note> noteList) {
		System.out.println("find from composition...");
		partialNodeList.clear();
		Motif m1, m2;
		List<Motif> motifList = new ArrayList<Motif>();
		// 1. find all sets of motives
		int min = 2;
		int max = noteList.size() / 2;
		// for each length
		for (int length = min; length < max; length++) {
			// find the set of motives of this length
			for (int i = 0; i < noteList.size() - length * 2; i++) {
				// set up initial motif
				m1 = new Motif(noteList.subList(i, i + length));
				boolean exist = false;
				// find motif in motif list
				for (Motif motif : motifList) {
					if (motif.equals(m1) && motif.positions.contains(i)) {
						exist = true;
						break;
					}
				}
				if (exist) {
					// if in motif list, skip
					continue;
				} else {
					// if not in motif list, add
					m1.positions.add(i);
				}

				// then compare with the others
				for (int j = i + length; j < noteList.size() - length;) {
					m2 = new Motif(noteList.subList(j, j + length));
					// if found, update m1 positions, update j
					if (m2.equals(m1)) {
						m1.positions.add(j);
						j += length;
					} else {
						j++;
					}
				}
				// if m1 occur more than once, its a motive
				if (m1.positions.size() > 1) {
					motifList.add(m1);
				}
			}
		}
		// 2. preserve longer ones
		for (int i = motifList.size() - 1; i >= 0; i--) {
			// m1 the longer one
			m1 = motifList.get(i);
			if (m1.positions.size() == 0) {
				continue;
			}
			for (int j = i - 1; j >= 0; j--) {
				// m2 the shorter one
				m2 = motifList.get(j);
				// remove overlapping shorter ones' positions
				m1.exclude(m2);
				// if m2 only one position, remove
				if (m2.positions.size() <= 1) {
					m2.positions.clear();
				}
			}
		}
		// remove motif with empty positions
		for (int i = 0; i < motifList.size();) {
			if (motifList.get(i).positions.size() == 0) {
				motifList.remove(i);
			} else {
				i++;
			}
		}
		// 3. replace note with motif
		boolean mot;
		for (int i = 0; i < noteList.size();) {
			mot = false;
			for (Motif motif : motifList) {
				for (Integer pos : motif.positions) {
					if (i == pos) {
						mot = true;
						nodeList.add(motif);
						i += motif.getLength();
						break;
					}
				}
				if (mot) {
					break;
				}
			}
			if (!mot) {
				nodeList.add(noteList.get(i));
				i++;
			}
		}
		// 4. print motif and node
		// for (Motif motif : motifList) {
		// System.out.println("length: " + motif.getLength() + ", frequency: " +
		// motif.positions.size() + ", content: "
		// + motif.getName());
		// }
		// for (Node node : nodeList) {
		// System.out.println(node.getName() + " ");
		// }
		return nodeList;
	}

	/**
	 * @param network
	 * @return
	 */
	public List<? extends Node> findMotiveFromNetwork(MusicalNetwork network) {
		nodeList.clear();
		List<Note> tmpNoteList = new ArrayList<Note>();
		// for each composition
		for (Composition c : network.getCompositions()) {
			tmpNoteList.clear();
			for (Event e : network.getEventList()) {
				if (e.getComposition().equals(c)) {
					tmpNoteList.add(e.getNote());
				}
			}
			nodeList.addAll(findMotiveFromComposition(tmpNoteList));
		}
		return nodeList;
	}

}
