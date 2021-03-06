/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *  
 */

package org.jfugue.extras;

import org.jfugue.ChannelPressure;
import org.jfugue.Controller;
import org.jfugue.Instrument;
import org.jfugue.KeySignature;
import org.jfugue.Note;
import org.jfugue.PatternTransformer;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

/**
 * Reverses a given pattern.
 * 
 * @author David Koelle
 * @version 2.0
 */
public class ReversePatternTransformer extends PatternTransformer {
	public static final String INTERVAL = "interval";

	public ReversePatternTransformer() {
		super();
	}

	@Override
	public void channelPressureEvent(ChannelPressure channelPressure) {
		insert(channelPressure.getMusicString(), " ");
	}

	@Override
	public void controllerEvent(Controller controller) {
		insert(controller.getMusicString(), " ");
	}

	public String getDescription() {
		return "Reverses the given pattern";
	}

	/**
	 * ReversePatternTransformer does not require that the user specify any
	 * variables.
	 */
	public String getParameters() {
		return "";
	}

	private void insert(String string, String connector) {
		StringBuilder buddy = new StringBuilder();
		buddy.append(string);
		buddy.append(connector);
		buddy.append(getReturnPattern().getMusicString());
		getReturnPattern().setMusicString(buddy.toString());
	}

	@Override
	public void instrumentEvent(Instrument instrument) {
		insert(instrument.getMusicString(), " ");
	}

	@Override
	public void keySignatureEvent(KeySignature keySig) {
		insert(keySig.getMusicString(), " ");
	}

	@Override
	public void noteEvent(Note note) {
		insert(note.getMusicString(), " ");
	}

	@Override
	public void parallelNoteEvent(Note note) {
		insert(note.getMusicString().substring(1, note.getMusicString().length()), "+");
	}

	@Override
	public void pitchBendEvent(PitchBend pitchBend) {
		insert(pitchBend.getMusicString(), " ");
	}

	@Override
	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		insert(polyphonicPressure.getMusicString(), " ");
	}

	@Override
	public void sequentialNoteEvent(Note note) {
		insert(note.getMusicString().substring(1, note.getMusicString().length()), "_");
	}

	@Override
	public void tempoEvent(Tempo tempo) {
		insert(tempo.getMusicString(), " ");
	}

	@Override
	public void timeEvent(Time time) {
		// nothing to do?
	}

	@Override
	public void voiceEvent(Voice voice) {
		insert(voice.getMusicString(), " ");
	}
}