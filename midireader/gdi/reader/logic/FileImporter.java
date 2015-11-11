package gdi.reader.logic;

import eg.reader.gnu.MidiFileReader;
import eg.reader.ibm.MessageInfo;
import gdi.reader.MIDIReader;
import gdi.reader.db.entity.AbstractMusicNote;
import gdi.reader.db.entity.Edge;
import gdi.reader.db.entity.MusicNote;
import gdi.reader.db.entity.TrackInfo;
import gdi.reader.db.handler.AbstractMusicNoteHandler;
import gdi.reader.db.handler.EdgeHandler;
import gdi.reader.db.handler.MusicNoteHandler;
import gdi.reader.db.handler.TrackInfoHandler;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.SwingWorker;

public class FileImporter extends SwingWorker<Void, Void> {

	private AbstractMusicNoteHandler abstractNoteHandler;

	private String author;

	private String collectionName;

	private EdgeHandler edgeHandler;

	private List<Edge> edgeList = new ArrayList<Edge>();

	private File[] files;

	private int genre;

	private MusicNoteHandler noteHandler;

	private List<MusicNote> noteList = new ArrayList<MusicNote>();

	private List<MusicNote> openList = new ArrayList<MusicNote>();

	private MidiFileReader reader;

	private TrackInfo trackInfo;

	private TrackInfoHandler trackInfoHandler;

	public FileImporter(File[] files, String author, String collectionName, int genre) {
		super();
		this.files = files;
		this.author = author;
		this.collectionName = collectionName;
		this.genre = genre;
	}

	private void addEdgeToList(Edge newEdge) {
		for (Edge edge : edgeList) {
			if (edge.equals(newEdge)) {
				edge.setWeight(edge.getWeight() + 1);
				return;
			}
		}
		edgeList.add(newEdge);
	}

	private void createMusicNotes(TrackInfo track, MidiEvent event, int resolution) {
		MusicNote newMusicNote = null;
		AbstractMusicNote newAMN = null;

		// on note
		if (MessageInfo.isOnNote(event.getMessage())) {
			newMusicNote = new MusicNote();
			newAMN = new AbstractMusicNote();
			newAMN.setPitch(MessageInfo.findPitch(event.getMessage()));
			newMusicNote.setAmn(newAMN);
			newMusicNote.setTrackInfo(track);
			newMusicNote.setSequence(track.getSequence());
			newMusicNote.setChannel(MessageInfo.findChannel(event.getMessage()));
			newMusicNote.setStartTick(event.getTick());
			newMusicNote.setVelo(MessageInfo.findVelo(event.getMessage()));
			noteList.add(newMusicNote);
		}
		// FIXME problem here off note
		else if (MessageInfo.isOffNote(event.getMessage())) {
			for (MusicNote targetNote : noteList) {
				if (targetNote.getTrackInfo().equals(track)
						&& targetNote.getAmn().getPitch() == MessageInfo.findPitch(event.getMessage())
						&& targetNote.getEndTick() == 0) {
					// openList.remove(targetNote);
					targetNote.setEndTick(event.getTick());
					targetNote.getAmn().setLength(targetNote.getStartTick(), targetNote.getEndTick(), resolution);
					targetNote.setAmn(abstractNoteHandler.findOrSaveAbstractMusicNote(targetNote.getAmn()));
					// noteList.add(targetNote);
				}
			}
		}
	}

	@Override
	public Void doInBackground() {
		setProgress(0);

		while (getProgress() < 100) {

			try {
				noteHandler =
						(MusicNoteHandler) MIDIReader.getInstance().getInitialContext().lookup("MusicNoteHandlerBean/local");
				edgeHandler = (EdgeHandler) MIDIReader.getInstance().getInitialContext().lookup("EdgeHandlerBean/local");
				trackInfoHandler =
						(TrackInfoHandler) MIDIReader.getInstance().getInitialContext().lookup("TrackInfoHandlerBean/local");
				abstractNoteHandler =
						(AbstractMusicNoteHandler) MIDIReader.getInstance().getInitialContext()
								.lookup("AbstractMusicNoteHandlerBean/local");

				int i = 1;
				int total = files.length;
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
					readSampleFile(file);
					setProgress(i++ * 100 / total);
				}

				for (Edge edge : edgeList) {
					edgeHandler.saveEdge(edge);
				}
				edgeList.clear();

				setProgress(100);
			} catch (NamingException e1) {
				System.out.println(e1.getStackTrace());
			} catch (InvalidMidiDataException e2) {
				e2.printStackTrace();
			} catch (IOException e3) {
				e3.printStackTrace();
			}

		}
		return null;
	}

	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
		Toolkit.getDefaultToolkit().beep();
	}

	private void formEdges(int sequence, int resolution) throws NamingException {
		MusicNoteHandler noteHandler =
				(MusicNoteHandler) MIDIReader.getInstance().getInitialContext().lookup("MusicNoteHandlerBean/local");

		// get all music notes
		noteList = noteHandler.getAllNotesFromSequence(sequence);

		// iterate the music notes
		for (MusicNote previousNote : noteList) {
			// find the latter note
			for (MusicNote latterNote : noteList) {
				if (previousNote.getEndTick() >= latterNote.getStartTick() - resolution / 16
						&& previousNote.getEndTick() <= latterNote.getStartTick() + resolution / 16) {
					addEdgeToList(new Edge(previousNote.getAmn(), latterNote.getAmn()));
				}
			}

		}
	}

	private void readSampleFile(File sampleFile) throws InvalidMidiDataException, IOException, NamingException {

		reader = new MidiFileReader();
		Sequence sequence = null;
		try {
			sequence = reader.getSequence(sampleFile);
			Track[] tracks = sequence.getTracks();
			if (tracks == null) {
				return;
			}

			for (Track track : tracks) {
				trackInfo =
						trackInfoHandler.saveTrackInfo(new TrackInfo(sequence.hashCode(), track.hashCode(), author,
								collectionName, genre));
				for (int j = 0; j < track.size(); j++) {
					MidiEvent event = track.get(j);
					createMusicNotes(trackInfo, event, sequence.getResolution());
				}
			}
			for (MusicNote note : noteList) {
				noteHandler.saveNote(note);
			}
			noteList.clear();
			formEdges(sequence.hashCode(), sequence.getResolution());
			noteList.clear();
		} catch (Exception e) {
			return;
		}
	}
}
