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

/*
 * MPlayer.java
 *
 * Created on 22 March 2004, 15:33
 */

package jmms;

import grame.midishare.Midi;
import jm.music.data.Score;
import jm.music.data.Tempo;

/**
 * 
 * @author Rene Wooller
 */
public abstract class MPlayer {

	private Sequencer seq = new Sequencer();

	private Tempo tempo = new Tempo();

	private TickTask tickTask;

	/** Creates a new instance of MPlay */
	public MPlayer() {
		seq.setTempo(tempo);
	}

	public void close() {
		seq.close();
	}

	public Tempo getTempo() {
		return this.tempo;
	}

	public void go() {
		PlayTask startTask = new PlayTask(Midi.NewEv(Midi.typeStart));
		seq.ScheduleTask(startTask, Midi.GetTime());
		tickTask = new TickTask(this);
		tickTask.Execute(seq, Midi.GetTime());
	}

	public Score nextScore() {
		System.out.println("define the music to play by overwriting the nextScore method");
		return null;
	}

	public Sequence nextSequence() {
		return seq.getMSSeq(this.nextScore());
	}

	protected void setResolution(double newRes) {
		this.tickTask.setResolution(newRes);
	}

	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}

	public void stop() {
		PlayTask startTask = new PlayTask(Midi.NewEv(Midi.typeStop));
		seq.ScheduleTask(startTask, Midi.GetTime());
		tickTask.stop();
		seq.panic();
	}

}
