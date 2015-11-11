package musicbox.logic.util.midi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import musicbox.db.entity.Composition;
import musicbox.db.entity.Event;
import musicbox.db.entity.MusicalNetwork;
import musicbox.db.entity.Note;
import tools.com.ibm.midi.MessageInfo;
import tools.org.gnu.midi.MidiFileReader;

/**
 * See details @ http://www.developer.ibm.com/devcon/mag.htm.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MIDIReader {

	public static String[] keySignatures =
			{ "Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#" };

	public static void main(String[] args) {
		MIDIReader mdr = new MIDIReader();
		MusicalNetwork network = new MusicalNetwork();

		JFileChooser fc = new JFileChooser("./midi files/");
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".mid") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return ".mid file";
			}
		});
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new File(".."));
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			network = mdr.importFile(file);
		}
		System.out.println(network.getLength());
	}

	/** Converts a a signed byte to unsigned. */
	public static int signedByteToUnsigned(byte b) {
		if (b >= 0) {
			return b;
		}
		return 256 + b;
	}

	private int bar = 0;

	private Composition composition;

	private List<Event> eventList = new ArrayList<Event>();

	private MusicalNetwork importedNetwork = new MusicalNetwork();

	private MidiFileReader reader = new MidiFileReader();

	private long tickThisBar = 0;

	private void createNotes(Composition composition, MidiEvent midiEvent, int resolution) {
		Event event = null;

		// TODO add bar and unit
		int beatNum = composition.getTimeSignature() / 100;
		int beatDur = composition.getTimeSignature() % 100;

		// on note
		if (MessageInfo.isOnNote(midiEvent.getMessage())) {
			event = new Event();
			event.setNote(new Note(MessageInfo.findPitch(midiEvent.getMessage()), 0));
			event.setComposition(composition);
			event.setChannel(MessageInfo.findChannel(midiEvent.getMessage()));
			event.setStartTick(midiEvent.getTick());
			event.setVelo(MessageInfo.findVelo(midiEvent.getMessage()));
			// XXX set bar and unit
			if (event.getStartTick() >= tickThisBar + composition.getResolution() * beatNum) {
				bar++;
				tickThisBar += composition.getResolution() * beatNum;
			}
			event.setUnit((event.getStartTick() - tickThisBar) / (float) resolution);
			event.setBar(bar);
			eventList.add(event);
		} else if (MessageInfo.isOffNote(midiEvent.getMessage())) {
			for (Event targetNote : eventList) {
				if (targetNote.getEndTick() == 0) {
					if (targetNote.getComposition().equals(composition)) {
						if (targetNote.getNote().getPitch() == MessageInfo.findPitch(midiEvent.getMessage())) {
							targetNote.setEndTick(midiEvent.getTick());
							targetNote.getNote().setDuration(targetNote.getStartTick(), targetNote.getEndTick(),
									resolution * beatDur / 4);
						}
					}
				}
			}
		}
	}

	public MusicalNetwork importFile(File midiFile) {
		importedNetwork = new MusicalNetwork();
		eventList.clear();
		bar = 0;
		tickThisBar = 0;

		Sequence sequence = null;
		try {
			sequence = reader.getSequence(midiFile);
			Track[] tracks = sequence.getTracks();
			if (tracks == null) {
				return null;
			}
			// store seq info
			composition = new Composition(sequence.getResolution(), midiFile.getName());
			importedNetwork.getCompositions().add(composition);
			for (Track track : tracks) {
				for (int j = 0; j < track.size(); j++) {
					MidiEvent event = track.get(j);
					if (event.getMessage() instanceof MetaMessage) {
						MetaMessage meta = (MetaMessage) event.getMessage();
						byte[] abData = meta.getData();
						// String strMessage = null;
						switch (meta.getType()) {
						case 0x51:
							// int nTempo = signedByteToUnsigned(abData[0])
							// * 65536 + signedByteToUnsigned(abData[1])
							// * 256 + signedByteToUnsigned(abData[2]);
							// strMessage = "Set Tempo (us/quarter note): "
							// + nTempo;
							break;
						case 0x58:
							// strMessage = "Time Signature: " + abData[0] + "/"
							// + (1 << abData[1])
							// + ", MIDI clocks per metronome tick: "
							// + abData[2] + ", 1/32 per 24 MIDI clocks: "
							// + abData[3];
							int er = abData[0];
							int ed = (1 << abData[1]);
							composition.setTimeSignature(er * 100 + ed);
							break;
						case 0x59:
							// String strGender = (abData[1] == 1) ? "minor"
							// : "major";
							// strMessage = "Key Signature: "
							// + keySignatures[abData[0] + 7] + " "
							// + strGender;
							composition.setKeySignature(abData[0] + 7 + abData[1] * 100);
							break;
						default:
							break;
						}
					} else if (event.getMessage() instanceof MidiMessage) {
						createNotes(composition, event, sequence.getResolution());
					}
				}
			}
			importedNetwork.setEventList(eventList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return importedNetwork;
	}

	public MusicalNetwork importFile(File midiFile, int instrument) {
		importedNetwork = new MusicalNetwork();
		eventList.clear();
		bar = 0;
		tickThisBar = 0;

		Sequence sequence = null;
		try {
			sequence = reader.getSequence(midiFile);
			Track[] tracks = sequence.getTracks();
			if (tracks == null) {
				return null;
			}
			// store seq info
			composition = new Composition(sequence.getResolution(), midiFile.getName());
			importedNetwork.getCompositions().add(composition);
			for (Track track : tracks) {
				int trackInstrument = 0;
				for (int j = 0; j < track.size(); j++) {
					MidiEvent event = track.get(j);
					if (event.getMessage() instanceof MetaMessage) {
						MetaMessage meta = (MetaMessage) event.getMessage();
						byte[] abData = meta.getData();
						switch (meta.getType()) {
						case 0x51:
							// int nTempo = signedByteToUnsigned(abData[0])
							// * 65536 + signedByteToUnsigned(abData[1])
							// * 256 + signedByteToUnsigned(abData[2]);
							// strMessage = "Set Tempo (us/quarter note): "
							// + nTempo;
							break;
						case 0x58:
							int er = abData[0];
							int ed = (1 << abData[1]);
							composition.setTimeSignature(er * 100 + ed);
							break;
						case 0x59:
							composition.setKeySignature(abData[0] + 7 + abData[1] * 100);
							break;
						default:
							break;
						}
					} else if (event.getMessage() instanceof ShortMessage) {
						ShortMessage msg = (ShortMessage) event.getMessage();
						switch (msg.getCommand()) {
						case 0xC0: // program (instrument) change
							trackInstrument = msg.getData1();
							break;
						case 0x80:
						case 0x90:
							if (trackInstrument == (instrument - 1)) {
								createNotes(composition, event, sequence.getResolution());
							}
						default:
							break;
						}
					}
				}
			}
			importedNetwork.setEventList(eventList);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		return importedNetwork;
	}
}
