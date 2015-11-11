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

package org.jfugue;

/**
 * This class is used to build a Pattern (i.e., a MusicString) given a MIDI
 * Sequence.
 * 
 * @author David Koelle
 * @version 3.0
 */
public final class MusicStringRenderer implements ParserListener {
	private Pattern pattern;

	public MusicStringRenderer() {
		pattern = new Pattern();
	}

	@Override
	public void channelPressureEvent(ChannelPressure channelPressure) {
		pattern.add(channelPressure.getMusicString());
	}

	@Override
	public void controllerEvent(Controller controller) {
		pattern.add(controller.getMusicString());
	}

	public Pattern getPattern() {
		return this.pattern;
	}

	@Override
	public void instrumentEvent(Instrument instrument) {
		pattern.add(instrument.getMusicString());
	}

	@Override
	public void keySignatureEvent(KeySignature keySig) {
		pattern.add(keySig.getMusicString());
	}

	@Override
	public void layerEvent(Layer layer) {
		pattern.add(layer.getMusicString());
	}

	@Override
	public void measureEvent(Measure measure) {
		pattern.add(measure.getMusicString());
	}

	@Override
	public void noteEvent(Note note) {
		// Don't use add(note.getMusicString(), because that will incorrectly
		// add a space between sequential or parallel notes.
		pattern.addElement(note);
	}

	@Override
	public void parallelNoteEvent(Note note) {
		// We won't get these events from a MIDI parser
	}

	@Override
	public void pitchBendEvent(PitchBend pitchBend) {
		pattern.add(pitchBend.getMusicString());
	}

	@Override
	public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure) {
		pattern.add(polyphonicPressure.getMusicString());
	}

	@Override
	public void sequentialNoteEvent(Note note) {
		// We won't get these events from a MIDI parser
	}

	@Override
	public void tempoEvent(Tempo tempo) {
		pattern.add(tempo.getMusicString());
	}

	@Override
	public void timeEvent(Time time) {
		pattern.add(time.getMusicString());
	}

	@Override
	public void voiceEvent(Voice voice) {
		pattern.add(voice.getMusicString());
	}
}
