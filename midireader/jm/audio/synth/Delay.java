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

package jm.audio.synth;

import java.util.Vector;

import jm.audio.AOException;
import jm.audio.AudioObject;

/**
 * The <bl>Delay</bl> Audio Object is a simple delay line. It calculates the
 * delay time in samples (so the time will depend on the current sample rate).
 * It only passes on the delayed data so an add Audio Object needs to be used in
 * the chain to mix the delayed signal with the original if that is required for
 * an echo effect.
 * 
 * @author Andrew Brown
 * @version 1.0,Sun Feb 25 18:42:45 2001
 */

public final class Delay extends AudioObject {
	/**
	 * The number of samples processed so far
	 */
	int sampleCounter = 0;
	/**
	 * The number of samples long the delay line is
	 */
	int sampleDelay = 0;
	// ----------------------------------------------
	// Attributes
	// ----------------------------------------------
	/**
	 * A place to keep the previous sample values
	 */
	Vector storedSamples = new Vector();

	// ----------------------------------------------
	// Constructors
	// ----------------------------------------------
	/**
	 * The Delay constructor takes the object previous to it in the chain, and
	 * the delay length in samples.
	 * 
	 * @param sampleDelay
	 *            the lenght of the delay in samples
	 * @param ao
	 *            the single AudioObject taken as input.
	 */
	public Delay(AudioObject ao, int sampleDelay) {
		super(ao, "[Delay]");
		this.sampleDelay = sampleDelay;
	}

	// ----------------------------------------------
	// Public Methods
	// ----------------------------------------------

	// ----------------------------------------------
	// Protected Methods
	// ----------------------------------------------
	/**
	 * The nextWork method for <bl>Delay<bl> will store the sample and send out
	 * the delayed one instead.
	 */
	@Override
	public int work(float[] buffer) throws AOException {
		int returned = this.previous[0].nextWork(buffer);
		if (sampleCounter >= sampleDelay) {
			for (int i = 0; i < returned; i++) {
				// keep the current sample from the chain
				storedSamples.addElement(new Float(buffer[i]));
				// replace it with a stored sample
				Float tempFloat = (Float) storedSamples.elementAt(storedSamples.size() - 1);
				buffer[i] = tempFloat.floatValue();
				// remove the used stored sample
				storedSamples.removeElementAt(storedSamples.size() - 1);
			}

		} else { // early on pad with silence
			for (int i = 0; i < returned; i++) {
				// keep the current sample from the chain
				storedSamples.addElement(new Float(buffer[i]));
				// send out a zero value
				buffer[i] = (float) 0.0;
			}
		}

		sampleCounter++;
		return returned;
	}
}
