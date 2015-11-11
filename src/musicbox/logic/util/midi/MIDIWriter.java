package musicbox.logic.util.midi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import musicbox.MainProgram;
import musicbox.db.entity.Event;
import musicbox.db.entity.Note;
import tools.com.ibm.midi.MessageInfo;

/**
 * writes a MIDI file on disc.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MIDIWriter {

	private static Sequence sequence;

	public static void write() {

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
		int returnVal = fc.showOpenDialog(MainProgram.getInstance().getGUI());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = fc.getSelectedFile();
			File temp = new File("temp.mid");
			try {
				InputStream in = new FileInputStream(temp);
				OutputStream out = new FileOutputStream(saveFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeEventsToFile(List<Event> composedEventList, String i) {

		File output = new File("./report/midi/" + i + ".mid");

		/*
		 * Now we just save the Sequence to the file we specified. The '0'
		 * (second parameter) means saving as SMF type 0. Since we have only one
		 * Track, this is actually the only option (type 1 is for multiple
		 * tracks).
		 */
		try {
			MidiSystem.write(writeEventsToSequence(composedEventList), 0, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Sequence writeEventsToSequence(List<Event> eventList) {

		try {
			sequence = new Sequence(Sequence.PPQ, 240);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		/*
		 * Track objects cannot be created by invoking their constructor
		 * directly. Instead, the Sequence object does the job. So we obtain the
		 * Track there. This links the Track to the Sequence automatically.
		 */
		Track track = sequence.createTrack();

		// track add time signature 4/4
		track.add(MIDIUtils.createTimeSignature(4, 4));
		// 1 ==> violin 0==> piano
		track.add(MIDIUtils.setInstrument(1));

		List<MidiEvent> midiEventList = new ArrayList<MidiEvent>();

		for (Event e : eventList) {
			midiEventList.add(MIDIUtils.createNoteOnEvent(e.getNote().getPitch(), e.getStartTick()));
			midiEventList.add(MIDIUtils.createNoteOffEvent(e.getNote().getPitch(), e.getEndTick()));
		}

		Collections.sort(midiEventList, new Comparator<MidiEvent>() {
			@Override
			public int compare(MidiEvent o1, MidiEvent o2) {
				long tick = o1.getTick() - o2.getTick();

				if (tick != 0) {
					return (int) tick;
				} else if (MessageInfo.isOffNote(o1.getMessage())) {
					return -1;
				} else {
					return 1;
				}

			}
		});

		for (MidiEvent midiEvent : midiEventList) {
			track.add(midiEvent);
		}

		return sequence;
	}

	public static void writeNotesToFile(List<Note> composedNoteList, String i) {

		File outputFile = new File("report/" + i + ".mid");
		try {
			sequence = new Sequence(Sequence.PPQ, 240);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		/*
		 * Track objects cannot be created by invoking their constructor
		 * directly. Instead, the Sequence object does the job. So we obtain the
		 * Track there. This links the Track to the Sequence automatically.
		 */
		Track track = sequence.createTrack();

		// track add time signature 4/4
		track.add(MIDIUtils.createTimeSignature(4, 4));
		// 1 ==> violin 0==> piano
		track.add(MIDIUtils.setInstrument(1));

		// make midi event list begin
		int tick = 0;
		List<MidiEvent> eventList = new ArrayList<MidiEvent>();

		for (Note note : composedNoteList) {
			eventList.add(MIDIUtils.createNoteOnEvent(note.getPitch(), tick));
			tick = (int) (tick + note.getDuration() * 240);
			eventList.add(MIDIUtils.createNoteOffEvent(note.getPitch(), tick));
		}
		// make midi event list finish

		// sort list begin
		Collections.sort(eventList, new Comparator<MidiEvent>() {
			@Override
			public int compare(MidiEvent o1, MidiEvent o2) {
				long tick = o1.getTick() - o2.getTick();

				if (tick != 0) {
					return (int) tick;
				} else if (MessageInfo.isOffNote(o1.getMessage())) {
					return -1;
				} else {
					return 1;
				}

			}
		});
		// for (MidiEvent midiEvent : eventList) {
		// if (MessageInfo.isOnNote(midiEvent.getMessage())) {
		// System.out
		// .println(midiEvent.getTick() + " "
		// + MessageInfo.findPitch(midiEvent.getMessage())
		// + " On");
		// } else {
		// System.out.println(midiEvent.getTick() + " "
		// + MessageInfo.findPitch(midiEvent.getMessage())
		// + " Off");
		// }
		// }
		// sort list finish

		for (MidiEvent midiEvent : eventList) {
			track.add(midiEvent);
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
