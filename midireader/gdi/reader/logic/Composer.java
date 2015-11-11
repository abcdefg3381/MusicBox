package gdi.reader.logic;

import gdi.reader.MIDIReader;
import gdi.reader.db.entity.AbstractMusicNote;
import gdi.reader.db.entity.Edge;
import gdi.reader.db.handler.AbstractMusicNoteHandler;
import gdi.reader.db.handler.EdgeHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Composer {
	class EdgeList {
		private int amn;

		private List<Edge> edgeList;

		public EdgeList() {
			amn = 0;
			edgeList = null;
		}

		public int getAmn() {
			return amn;
		}

		public List<Edge> getEdgeList() {
			return edgeList;
		}

		public void setAmn(int amn) {
			this.amn = amn;
		}

		public void setEdgeList(List<Edge> edgeList) {
			this.edgeList = edgeList;
		}

	}

	private EdgeHandler edgeHandler;

	private List<EdgeList> edgeLists = new ArrayList<EdgeList>();

	private Random generator = new Random();

	private AbstractMusicNoteHandler noteHandler;

	private List<AbstractMusicNote> noteList = new ArrayList<AbstractMusicNote>();

	private int VELOCITY = 64;

	public Composer() {
	}

	public void compose() {

		try {
			edgeHandler = (EdgeHandler) MIDIReader.getInstance().getInitialContext().lookup("EdgeHandlerBean/local");

			noteHandler =
					(AbstractMusicNoteHandler) MIDIReader.getInstance().getInitialContext()
							.lookup("AbstractMusicNoteHandlerBean/local");

			int n = noteHandler.getAbstractMusicNoteNumber();
			// 1. new amn
			int oldID = generator.nextInt(n);
			AbstractMusicNote newNote = null;
			for (int i = 0; i < 100; i++) {
				// 2. lookup/retrieve
				// 3. new note
				newNote = generateNextAMN(oldID);
				if (newNote == null) {
					oldID = generator.nextInt(n);
				} else {
					noteList.add(newNote);
					System.out.println(noteList.size());
					oldID = newNote.getId();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// then write temp.mid
		writeNotesToTempFile();
	}

	private MidiEvent createNoteEvent(int nCommand, int nKey, int nVelocity, long lTick) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(nCommand, 0, // always on channel 1
					nKey, nVelocity);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
		MidiEvent event = new MidiEvent(message, lTick);
		return event;
	}

	private MidiEvent createNoteOffEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_OFF, nKey, 0, lTick);
	}

	private MidiEvent createNoteOnEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_ON, nKey, VELOCITY, lTick);
	}

	private AbstractMusicNote generateNextAMN(int oldID) {
		// 1. see if oldid in local list
		for (EdgeList edgeList : edgeLists) {
			// 1.1 if yes , generate and return
			if (edgeList.getAmn() == oldID) {
				return randomNote(edgeList);
			}
		}
		// 1.2 if no , retrieve , generate and return
		EdgeList retrievedList = new EdgeList();
		retrievedList.setAmn(oldID);
		List<Edge> edgeList = edgeHandler.getEdgeWithPreAMN(oldID);
		retrievedList.setEdgeList(edgeList);
		edgeLists.add(retrievedList);
		return randomNote(retrievedList);
	}

	private AbstractMusicNote randomNote(EdgeList edgeList) {
		int sum = 0;
		for (Edge edge : edgeList.getEdgeList()) {
			sum += edge.getWeight();
		}
		if (sum == 0) {
			return null;
		}
		int nextID = generator.nextInt(sum);
		for (Edge edge : edgeList.getEdgeList()) {
			nextID -= edge.getWeight();
			if (nextID <= 0) {
				return edge.getSucAMN();
			}
		}
		return null;
	}

	private void writeNotesToTempFile() {

		File outputFile = new File("temp.mid");
		Sequence sequence = null;
		try {
			sequence = new Sequence(Sequence.PPQ, 16);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		/*
		 * Track objects cannot be created by invoking their constructor
		 * directly. Instead, the Sequence object does the job. So we obtain the
		 * Track there. This links the Track to the Sequence automatically.
		 */
		Track track = sequence.createTrack();

		int tick = 0;
		// track.add(createNoteOffEvent(67, 8));
		// track.add(createNoteOffEvent(72, 8));

		for (AbstractMusicNote amn : noteList) {
			track.add(createNoteOnEvent(amn.getPitch(), tick));
			tick += amn.getLength() * 16;
			track.add(createNoteOffEvent(amn.getPitch(), tick));
		}

		/*
		 * Now we just save the Sequence to the file we specified. The '0'
		 * (second parameter) means saving as SMF type 0. Since we have only one
		 * Track, this is actually the only option (type 1 is for multiple
		 * tracks).
		 */
		try {
			MidiSystem.write(sequence, 0, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
