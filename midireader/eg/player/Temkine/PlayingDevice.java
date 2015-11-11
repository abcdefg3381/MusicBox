package eg.player.Temkine;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/*
 * Created on Oct 22, 2005
 */

/**
 * @author Mikhail Temkine
 * 
 *         This is the class which is responsible for handling the play-content
 * 
 */
public class PlayingDevice {
	public static Sequence loadSequence(File f) throws IOException {
		Sequence sequence = null;
		try {
			sequence = MidiSystem.getSequence(f);
		} catch (InvalidMidiDataException ex) {
			return null;
		}
		return sequence;
	}

	// **************************************************
	public static void main(String[] args) {
		new PlayingDevice();

	}

	Checker checker;

	Sequencer seq;

	Sequence tape = null;

	public PlayingDevice() {
		openMidiResources();
	}

	public void close() {
		seq.stop();
		seq.close();
	}

	public Sequence getSequence() {
		return seq.getSequence();
	}

	public Sequencer getSequencer() {
		return seq;
	}

	public void openMidiResources() {
		try {
			seq = MidiSystem.getSequencer();
			seq.open();
		} catch (Exception e) {
			return;
		}
	}

	public void pause() {
		seq.stop();
	}

	public void play() {
		if (seq.getSequence() != null) {
			seq.start();
		}
	}

	public void setSequence(Sequence tape) {
		boolean playing = seq.isRunning();
		stop();
		this.tape = tape;
		try {
			seq.setSequence(tape);
		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
		}
		// if(playing)
		play();
	}

	public void stop() {
		seq.stop();
		seq.setTickPosition(0);
	}
}
