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
import org.jfugue.JFugueElement;
import org.jfugue.KeySignature;
import org.jfugue.Layer;
import org.jfugue.Measure;
import org.jfugue.Note;
import org.jfugue.ParserListenerAdapter;
import org.jfugue.Pattern;
import org.jfugue.PitchBend;
import org.jfugue.PolyphonicPressure;
import org.jfugue.Tempo;
import org.jfugue.Time;
import org.jfugue.Voice;

/**
 * Returns all of the MusicString events that are played in the requested Voice
 * (i.e., Channel)
 * 
 * @author David Koelle
 * @version 3.0
 * 
 */
public class GetPatternForVoiceTool extends ParserListenerAdapter {
	private byte activeVoice = 0;
	private Pattern pattern;
	private byte voice = 0;

	public GetPatternForVoiceTool(int voice) {
		this.voice = (byte) voice;
		reset();
	}

	private void addElementIfActiveVoice(JFugueElement element) {
		if (activeVoice == voice) {
			pattern.add(element.getMusicString());
		}
	}

	/**
	 * Called when the parser encounters a channel pressure event.
	 * 
	 * @param channelPressure
	 *            the event that has been parsed
	 * @see ChannelPressure
	 */
	@Override
	public void channelPressureEvent(ChannelPressure channelPressure) {
		addElementIfActiveVoice(channelPressure);
	}

	/**
	 * Called when the parser encounters a controller event.
	 * 
	 * @param controller
	 *            the event that has been parsed
	 */
	@Override
	public void controllerEvent(Controller controller) {
		addElementIfActiveVoice(controller);
	}

	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * Called when the parser encounters an instrument event.
	 * 
	 * @param instrument
	 *            the event that has been parsed
	 * @see Instrument
	 */
	@Override
	public void instrumentEvent(Instrument instrument) {
		addElementIfActiveVoice(instrument);
	}

	/**
	 * Key Signature changes affect the voice regardless of what voice they
	 * appear to be in
	 */
	@Override
	public void keySignatureEvent(KeySignature keySig) {
		pattern.add(keySig.getMusicString());
	}

	/**
	 * Called when the parser encounters a layer event.
	 * 
	 * @param layer
	 *            the event that has been parsed
	 * @see Layer
	 */
	@Override
	public void layerEvent(Layer layer) {
		addElementIfActiveVoice(layer);
	}

	/**
	 * Called when the parser encounters a measure event.
	 * 
	 * @param measure
	 *            the event that has been parsed
	 * @see Measure
	 */
	@Override
	public void measureEvent(Measure measure) {
		addElementIfActiveVoice(measure);
	}

	/**
	 * Called when the parser encounters an initial note event.
	 * 
	 * @param note
	 *            the event that has been parsed
	 * @see Note
	 */
	@Override
	public void noteEvent(Note note) {
		addElementIfActiveVoice(note);
	}

	/**
	 * Called when the parser encounters a parallel note event.
	 * 
	 * @param note
	 *            the event that has been parsed
	 * @see Note
	 */
	@Override
	public void parallelNoteEvent(Note note) {
		addElementIfActiveVoice(note);
	}

	/**
	 * Called when the parser encounters a pitch bend event.
	 * 
	 * @param pitchBend
	 *            the event that has been parsed
	 * @see PitchBend
	 */
	@Override
	public void pitchBendEvent(PitchBend pitchBend) {
		addElementIfActiveVoice(pitchBend);
	}

	/**
	 * Called when the parser encounters a polyphonic pressure event.
	 * 
	 * @param polyphonicPressure
	 *            the event that has been parsed
	 * @see PolyphonicPressure
	 */
	@Override
	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		addElementIfActiveVoice(polyphonicPressure);
	}

	public void reset() {
		pattern = new Pattern();
		activeVoice = 0;
	}

	/**
	 * Called when the parser encounters a sequential note event.
	 * 
	 * @param note
	 *            the event that has been parsed
	 * @see Note
	 */
	@Override
	public void sequentialNoteEvent(Note note) {
		addElementIfActiveVoice(note);
	}

	/**
	 * Tempo changes affect the voice regardless of what voice they appear to be
	 * in
	 */
	@Override
	public void tempoEvent(Tempo tempo) {
		pattern.add(tempo.getMusicString());
	}

	/**
	 * Called when the parser encounters a time event.
	 * 
	 * @param time
	 *            the event that has been parsed
	 * @see Time
	 */
	@Override
	public void timeEvent(Time time) {
		addElementIfActiveVoice(time);
	}

	@Override
	public void voiceEvent(Voice voice) {
		if (activeVoice != voice.getVoice()) {
			this.activeVoice = voice.getVoice();
			addElementIfActiveVoice(voice);
		}
	}
}
