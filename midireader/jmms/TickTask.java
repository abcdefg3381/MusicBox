/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>

Copyright (C) 2000 Andrew Sorensen & Andrew Brown

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or any
later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

 */

//
//  TickTask.java
//  
//
//  Created by Rene Wooller on Tue Dec 17 2002.
//  Copyright (c) 2002 __MyCompanyName__. All rights reserved.
//
package jmms;

import grame.midishare.Midi;
import grame.midishare.MidiAppl;
import grame.midishare.MidiTask;
import jm.music.data.Tempo;

/**
 * this is an extension of the MidiTask and is used to play the events from a
 * Sequencer
 */
public class TickTask extends MidiTask {

	private long c = System.currentTimeMillis();
	private MPlayer mp;
	private PlayTask pt;
	private double RES = 0.25; // the resolution of the cycle
	double res24 = 1 / 24;
	private boolean stop = false;
	private Tempo tempo;
	private PlayTask timingTask;

	/**
	 * @param int ev the event that if to be played
	 */
	public TickTask(MPlayer mp) {
		this.mp = mp;
		this.tempo = mp.getTempo();
	}

	/**
	 * call this to execute the current Midishare event
	 * 
	 * @param MidiAppl
	 *            appl the MidiAppl that is to be used to send the event
	 * @param int date the actual date that it should be executed
	 */

	@Override
	public void Execute(MidiAppl appl, int date) {
		// trigger make changes in tickscore
		if (!stop) {
			for (int i = 0; i < 6; i++) {
				this.timingTask = new PlayTask(Midi.NewEv(Midi.typeClock));
				appl.ScheduleTask(timingTask, date + inMillis(i * res24));
			}
			appl.ScheduleTask(this, date + inMillis(RES));
		} else {
			// put in stop (and start message) here
		}

		int firstEv = 0;
		Sequence seq = mp.nextSequence();

		firstEv = seq.nextEvent();

		if (firstEv != 0) { // if seq not empty
			pt = new PlayTask(firstEv);
			pt.setSequence(seq);
			appl.ScheduleTask(pt, date + inMillis(RES));
		}
	}

	private int inMillis(double toConvert) {
		double toRet = ((toConvert * 60000 / tempo.getPerMinute()));
		return (int) (toRet + 0.5); // round this mutha off!
	}

	public void setResolution(double newRes) {
		this.RES = newRes;
	}

	public void stop() {
		this.stop = true;
	}
}
