package eg.player.Temkine;

import javax.sound.midi.Sequencer;

/*
 * Created on Oct 21, 2005
 */

/**
 * 
 * @author Mikhail Temkine
 * 
 *         This is a simple class to animate the PositSlider object
 * 
 */
public class Checker extends Thread {
	long length = 1;
	boolean running = false;
	Sequencer seq;
	PositSlider slid;

	public Checker(Sequencer seq, PositSlider slid) {
		this.seq = seq;
		this.slid = slid;
	}

	@Override
	public void run() {
		while (true) {
			boolean running2 = seq.isRunning();
			if (running2 && !running) {
				slid.runClock();
			} else if (!running2 && running) // just stopped
			{
				slid.setPosition((double) seq.getTickPosition() / length);
				slid.haltClock();
			}
			running = running2;
			if (running) {
				slid.setPosition((double) seq.getTickPosition() / length);
			}
			try {
				sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}

	public void setSong(long length) {
		this.length = length;
		slid.setPosition(0);
	}
}