package musicbox.logic.util.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

/**
 * create MIDI event according to numbers.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MIDIUtils {

	private static int VELOCITY = 64;

	public static MidiEvent createNoteEvent(int nCommand, int nKey, int nVelocity, long lTick) {
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

	public static MidiEvent createNoteOffEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_OFF, nKey, 0, lTick);
	}

	public static MidiEvent createNoteOnEvent(int nKey, long lTick) {
		return createNoteEvent(ShortMessage.NOTE_ON, nKey, VELOCITY, lTick);
	}

	public static MidiEvent createTimeSignature(int n, int d) {
		MetaMessage mm = new MetaMessage();
		byte[] data = { 0x04, 0x02, 0x18, 0x08 };
		try {
			mm.setMessage(0x58, data, 4);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(mm, 0);
	}

	public static MidiEvent setInstrument(int i) {
		ShortMessage sm = new ShortMessage();
		try {
			switch (i) {
			case 0:
				sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 1, 0);
			case 1:
				sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 40, 0);
			default:
				sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0, 0);
			}
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return new MidiEvent(sm, 0);
	}

}
