package musicbox.logic.util.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.swing.JOptionPane;

/**
 * plays MIDI using {@link javax.sound.midi.Sequencer}
 * 
 * @author LIU Xiaofan
 * 
 */
public class MIDIPlayer {

	private static Sequencer sm_sequencer = null;
	private static Synthesizer sm_synthesizer = null;

	public static void play(Sequence sequence) {
		if (sequence == null) {
			/*
			 * In case of an exception, we dump the exception including the
			 * stack trace to the console. Then, we exit the program.
			 */
			JOptionPane.showMessageDialog(null, "No Music Composed!", "File Not Found", JOptionPane.WARNING_MESSAGE);
			return;
		}

		/*
		 * Now, we need a Sequencer to play the sequence. Here, we simply
		 * request the default sequencer.
		 */
		try {
			sm_sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}
		if (sm_sequencer == null) {
			return;
		}

		/*
		 * There is a bug in the Sun jdk1.3/1.4. It prevents correct termination
		 * of the VM. So we have to exit ourselves. To accomplish this, we
		 * register a Listener to the Sequencer. It is called when there are
		 * "meta" events. Meta event 47 is end of track.
		 * 
		 * Thanks to Espen Riskedal for finding this trick.
		 */
		sm_sequencer.addMetaEventListener(new MetaEventListener() {
			@Override
			public void meta(MetaMessage event) {
				if (event.getType() == 47) {
					sm_sequencer.close();
					if (sm_synthesizer != null) {
						sm_synthesizer.close();
					}
					return;
				}
			}
		});

		/*
		 * The Sequencer is still a dead object. We have to open() it to become
		 * live. This is necessary to allocate some ressources in the native
		 * part.
		 */
		try {
			sm_sequencer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * Next step is to tell the Sequencer which Sequence it has to play. In
		 * this case, we set it as the Sequence object created above.
		 */
		try {
			sm_sequencer.setSequence(sequence);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			return;
		}

		/*
		 * Now, we set up the destinations the Sequence should be played on.
		 * Here, we try to use the default synthesizer. With some Java Sound
		 * implementations (Sun jdk1.3/1.4 and others derived from this
		 * codebase), the default sequencer and the default synthesizer are
		 * combined in one object. We test for this condition, and if it's true,
		 * nothing more has to be done. With other implementations (namely
		 * Tritonus), sequencers and synthesizers are always seperate objects.
		 * In this case, we have to set up a link between the two objects
		 * manually.
		 * 
		 * By the way, you should never rely on sequencers being synthesizers,
		 * too; this is a highly non- portable programming style. You should be
		 * able to rely on the other case working. Alas, it is only partly true
		 * for the Sun jdk1.3/1.4.
		 */
		if (!(sm_sequencer instanceof Synthesizer)) {
			/*
			 * We try to get the default synthesizer, open() it and chain it to
			 * the sequencer with a Transmitter-Receiver pair.
			 */
			try {
				sm_synthesizer = MidiSystem.getSynthesizer();
				sm_synthesizer.open();
				Receiver synthReceiver = sm_synthesizer.getReceiver();
				Transmitter seqTransmitter = sm_sequencer.getTransmitter();
				seqTransmitter.setReceiver(synthReceiver);
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Now, we can start over.
		 */
		sm_sequencer.start();
	}

	public static void stop() {
		if (sm_sequencer != null && sm_sequencer.isRunning()) {
			sm_sequencer.stop();
		}
	}

}
