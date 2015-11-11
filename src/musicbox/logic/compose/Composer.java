package musicbox.logic.compose;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maggie.network.entity.Edge;
import maggie.network.entity.Node;
import musicbox.db.entity.Event;
import musicbox.db.entity.Motif;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.entity.Note;
import musicbox.logic.util.midi.MIDIWriter;

/**
 * Compose MIDI using motif or random walk algorithm.
 * 
 * @author LIU Xiaofan
 * 
 */
public class Composer {

	public static final int MOTIF = 3;
	public static final int RANDOMOTIF = 2;
	public static final int RANDOMWALK = 1;

	public static List<Note> compose(MusicalNetwork network, int type) {
		Composer composer = new Composer(network);
		switch (type) {
		case RANDOMWALK:
			System.out.println("RandomWalk");
			composer.composeWithRandomWalk();
			break;
		case RANDOMOTIF:
			System.out.println("MotifAndRandomWalk");
			composer.composeWithMotifAndRandomWalk();
			break;
		case MOTIF:
			System.out.println("Motif");
			composer.composeWithMotif();
			break;
		default:
			break;
		}
		try {
			Desktop.getDesktop().open(new File("report"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return composer.getComposedNoteList();
	}

	@Deprecated
	private List<Event> composedEventList;

	private List<Note> composedNoteList;
	private MusicalNetwork network;

	private Random random;
	private List<Event> tempEventList;

	public Composer(MusicalNetwork currentNetwork) {
		this.network = currentNetwork;
		this.random = new Random();
		this.composedEventList = new ArrayList<Event>();
		this.composedNoteList = new ArrayList<Note>();
		this.tempEventList = new ArrayList<Event>();
	}

	/**
	 * Compose music with composed motifs put one after another, following rules
	 * as listed:<br>
	 * 1. No music notes lasts from one measure to the next one. i.e. every
	 * measure starts with a new note.<br>
	 * 2. The rhythm (combination of regular/irregular/isochronous) of composed
	 * music follows the original one.<br>
	 * 3. In case of violation to the rule, the program retries. The maximum
	 * trial is 1000 times.
	 */
	@Deprecated
	protected void composeWithMotif() {
		int length = 32;
		int bar = 0;
		float unit = 0;
		int barTemp = 0;
		float unitTemp = 0;
		boolean cont;
		int[] rhythm = null;
		for (int j = 0, k = 0; j < length; k++) {

			// initialize
			tempEventList.clear();
			tempEventList.addAll(composedEventList);
			barTemp = bar;
			unitTemp = unit;

			// compose motif
			Motif composite = compositeMotif();

			// dont let one note go across the measure line.
			cont = true;
			int totalBar = 0;
			for (Note note : composite.getMotif()) {
				tempEventList.add(new Event(note, barTemp, unitTemp));
				unitTemp += note.getDuration();
				barTemp += unitTemp / 4;
				unitTemp %= 4;
				totalBar = tempEventList.get(tempEventList.size() - 1).getBar();
				if (barTemp > totalBar && unitTemp > 0) {
					cont = false;
					break;
				}
			}

			// fit the rhythm
			rhythm = new int[totalBar + 1];
			float[] beatMax;
			float[] beatMin;
			// for each bar
			for (int i = 0; i < rhythm.length; i++) {
				beatMax = new float[4];
				beatMin = new float[4];
				for (Event event : tempEventList) {
					if (event.getBar() == i) {
						if (beatMax[(int) event.getUnit()] < event.getNote().getDuration()) {
							beatMax[(int) event.getUnit()] = event.getNote().getDuration();
						}
						if (beatMin[(int) event.getUnit()] == 0
								|| beatMin[(int) event.getUnit()] > event.getNote().getDuration()) {
							beatMin[(int) event.getUnit()] = event.getNote().getDuration();
						}
					}
				}
				// irregular
				if (beatMin[1] > beatMin[0] || beatMax[1] > beatMax[0]) {
					rhythm[i] = 2;
				}
				// isochronous
				else if (beatMin[1] == beatMin[0] && beatMax[1] == beatMax[0]) {
					rhythm[i] = 3;

				} // default regular
				else {
					rhythm[i] = 1;
				}
				// System.out.print(rhythm[i] + ":"
				// + network.getCompositions().get(0).getRhythm()[i]);

				if (rhythm[i] != network.getCompositions().get(0).getRhythm()[i]) {
					cont = false;
				}
			}
			if (cont) {
				// write to composedEventList
				for (Note note : composite.getMotif()) {
					composedEventList.add(new Event(note, bar, unit));
					unit += note.getDuration();
					bar += unit / 4;
					unit %= 4;
				}
				j++;
			}
			System.out.println(j + ":" + k);
			if (k > 1000) {
				composeWithMotif();
				return;
			}
		}
		for (int i : rhythm) {
			System.out.print(i);
		}
		// writeNetworkToFile(0);
		MIDIWriter.writeEventsToFile(composedEventList, "motif");
	}

	/**
	 * Compose music with original composite motifs inserted in random walk,
	 * following rules as listed:<br>
	 * 1. No music note lasts from one measure to the next.<br>
	 * 2. The rhythm (combination of regular/irregular/isochronous) of composed
	 * music follows the original one.<br>
	 * 3. In case of violation to the rule, the program retries. The maximum
	 * trial is 1000 times.
	 */
	@Deprecated
	protected void composeWithMotifAndRandomWalk() {
		for (int j = 0; j < 10; j++) {
			// find composite motif
			Motif composite = originalCompositeMotif();
			for (Note note : composite.getMotif()) {
				composedNoteList.add(note);
			}
			// walk from the motif
			randomWalk(composite);
		}

		MIDIWriter.writeNotesToFile(composedNoteList, "motifrdmwlk");
	}

	protected void composeWithRandomWalk() {
		List<Node> slctdNdLst = new ArrayList<Node>();
		int weight = 0;
		Node lastNode;
		// randomly choose a starter
		Edge rndEdge = randomEdge();
		slctdNdLst.add(rndEdge.getFrom());
		slctdNdLst.add(rndEdge.getTo());
		lastNode = rndEdge.getTo();
		// XXX choose if remove edge
		edgeUsed(rndEdge);
		// compose number of nodes equals to original network
		for (int iteration = 0; iteration < network.getLength(); iteration++) {
			// happens to the end of network, random select
			if (lastNode.getOutStrength() == 0) {
				rndEdge = randomEdge();
				if (rndEdge == null) {
					break;
				}
				slctdNdLst.add(rndEdge.getFrom());
				slctdNdLst.add(rndEdge.getTo());
				lastNode = rndEdge.getTo();
				// XXX choose if remove edge
				edgeUsed(rndEdge);
			} else {
				// normal condition
				weight = random.nextInt((int) lastNode.getOutStrength());
				for (Edge edge : network.getEdgeList()) {
					if (edge.getFrom().equals(lastNode)) {
						weight -= edge.getWeight();
					}
					if (weight < 0) {
						lastNode = edge.getTo();
						slctdNdLst.add(edge.getTo());
						// XXX choose if remove edge
						edgeUsed(edge);
						break;
					}
				}
			}
		}
		// convert nodes into music notes
		for (Node node : slctdNdLst) {
			if (node instanceof Note) {
				composedNoteList.add(((Note) node).clone());
			} else {
				for (Note n : ((Motif) node).getMotif()) {
					composedNoteList.add(n.clone());
				}
			}
		}
		// group same notes
		for (int i = 0; i < composedNoteList.size(); i++) {
			for (int j = i; j < composedNoteList.size(); j++) {
				if (composedNoteList.get(i).equals(composedNoteList.get(j))) {
					composedNoteList.set(j, composedNoteList.get(i));
				}
			}
		}
		// save composed music to file
		MIDIWriter.writeNotesToFile(composedNoteList, "rdmwlk");
	}

	/**
	 * First find rhythmic motif, then tonal motif with same length.<br>
	 * Only the motifs with high frequency (upper 50%) are chosen.
	 * 
	 * @return a composite motif
	 */
	private Motif compositeMotif() {
		// 1. find a rhythm
		int total = 0;
		for (Motif motif : network.getRhythmicMotifList()) {
			total += motif.getFrequency();
		}

		// XXX total/2 because only significant motifs are selected.
		total = random.nextInt(total / 2);
		Motif selectedRhythm = null;
		for (Motif motif : network.getRhythmicMotifList()) {
			total -= motif.getFrequency();
			if (total < 0) {
				selectedRhythm = motif;
				break;
			}
		}
		int length = selectedRhythm.getMotif().length;

		// 2. find a tone
		total = 0;
		for (Motif motif : network.getTonalMotifList()) {
			if (motif.getMotif().length == length) {
				total += motif.getFrequency();
			}
		}
		if (total == 0) {
			return null;
		}

		// XXX total/2 because only significant motifs are selected.
		total = random.nextInt(total / 2);
		Motif selectedTone = null;
		for (Motif motif : network.getTonalMotifList()) {
			if (motif.getMotif().length == length) {
				total -= motif.getFrequency();
				if (total < 0) {
					selectedTone = motif;
					break;
				}
			}
		}

		// 3. combine rhythm and tone
		for (int i = 0; i < length; i++) {
			selectedRhythm.getMotif()[i].setPitch(selectedTone.getMotif()[i].getPitch());
		}
		return selectedRhythm;
	}

	/**
	 * @param rndEdge
	 */
	private void edgeUsed(Edge rndEdge) {
		rndEdge.subWeight();
		rndEdge.getFrom().setOutStrength(rndEdge.getFrom().getOutStrength() - 1);
		rndEdge.getTo().setInStrength(rndEdge.getTo().getInStrength() - 1);
	}

	protected List<Note> getComposedNoteList() {
		return composedNoteList;
	}

	/**
	 * @return A randomly chosen original composite motif.
	 */
	private Motif originalCompositeMotif() {
		int total = 0;
		for (Motif motif : network.getMotifList()) {
			total += motif.getFrequency();
		}
		total = random.nextInt(total);
		for (Motif motif : network.getMotifList()) {
			total -= motif.getFrequency();
			if (total < 0) {
				return motif;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	private Edge randomEdge() {
		int weight = 0;
		for (Edge edge : network.getEdgeList()) {
			weight += edge.getWeight();
		}
		if (weight == 0) {
			return null;
		}
		weight = random.nextInt(weight);
		for (Edge edge : network.getEdgeList()) {
			weight -= edge.getWeight();
			if (weight < 0) {
				return edge;
			}
		}
		return null;
	}

	/**
	 * Follows the last note of a composite motif and walks a certain distance.
	 * 
	 * @param A
	 *            original composite motif
	 */
	private void randomWalk(Motif composite) {
		// find last note
		Node last = composite.getMotif()[composite.getMotif().length - 1];
		// find all possible choices and choose one. repeat the procedure for 5
		// times.
		for (int iteration = 0; iteration < 5; iteration++) {
			int total = 0;
			for (Edge edge : network.getEdgeList()) {
				if (edge.getFrom().equals(last)) {
					total += edge.getWeight();
				}
			}
			total = random.nextInt(total);
			for (Edge edge : network.getEdgeList()) {
				if (edge.getFrom().equals(last)) {
					total -= edge.getWeight();
				}
				if (total < 0) {
					if (edge.getTo() instanceof Note) {
						composedNoteList.add((Note) edge.getTo());
					} else {
						for (Note n : ((Motif) edge.getTo()).getMotif()) {
							composedNoteList.add(n);
						}
					}
					last = edge.getTo();
					break;
				}
			}
		}
	}

}