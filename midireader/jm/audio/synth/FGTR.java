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

import jm.audio.AOException;
import jm.audio.AudioObject;

/**
 * The Fast Grain Transform
 * 
 * @author Timothy Opie
 * @version 1.0, Tuesday November 4, 2003 Last changed: Description: This class
 *          takes a stream of audio data and reduces it to grain information
 *          comprising solely of a start and finish time band, a top and bottom
 *          frequency band and an amplitude.
 */

public final class FGTR extends AudioObject {
	// ----------------------------------------------
	// Attributes
	// ----------------------------------------------

	private float bandwidthTop, bandwidthBottom, highestAmp, frequency, grainDuration;
	// private float grainDuration, bandwidthTop, bandwidthBottom;
	private int bCounter, gCounter, grainsPerBuffer;
	private float[][] FGTArray;

	// ----------------------------------------------
	// Constructors
	// ----------------------------------------------
	public FGTR(AudioObject ao) {
		super(ao, "[FGTR]");

	}

	/**
	 * -------------------------------------------- Beginning
	 * --------------------------------------------
	 */
	@Override
	public int work(float[] buffer) throws AOException {

		// Warning!!!! This code suffers from buffer confusion :(

		for (gCounter = 0; gCounter < grainsPerBuffer; gCounter++) {
			bCounter = (int) FGTArray[gCounter][0];
			bandwidthTop = FGTArray[gCounter][2];
			bandwidthBottom = FGTArray[gCounter][3];
			// improved later!
			highestAmp = FGTArray[gCounter][5];
			frequency = (float) ((bandwidthTop + bandwidthBottom) * 0.5); // to
																			// be
																			// improved
																			// later

			for (int dCounter = 0; dCounter < grainDuration; dCounter++) {
				// This following short equation creates a sine wave and
				// envelopes it at the correct duration
				buffer[bCounter] =
						buffer[bCounter]
								+ (float) ((Math.sin(2 * Math.PI * dCounter
										* ((frequency * buffer.length) / (sampleRate * channels))) * (Math.sin(Math.PI
										* (dCounter / grainDuration)) * highestAmp)));
				// Or there is this method which generates a noise band between
				// the specified top and bottom frequency range
				// and then envelopes it just like the previous equation.

				// buffer[bCounter]=buffer[bCounter]+((bandwidthBottom+Math.random()*(bandwidthTop-bandwidthBottom))*
				// (Math.sin(Math.PI*(dCounter/grainDuration))*highestAmp));

				bCounter++;
			}
		}
		return 0;
	}
}
