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
 * The <bl>Volume</bl> Audio Object is a simple volume control. Any samples that
 * pass through the <bl>Volume</bl> Object will have their amplitudes adjusted
 * by whatever volume level is set. <bl>Volume</bl> can take one or two Audio
 * Objects for input. When using a single Audio Object as input <bl>Volume</bl>
 * uses a default volume level for all incoming audio samples. When taking two
 * Audio Objects as input the first input is a volume and the second is the
 * sample data.
 * 
 * @author Andrew Sorensen
 * @version 1.0,Sun Feb 25 18:42:51 2001
 */

public final class Volume extends AudioObject {
	double linearVolumeValue = 1.0;

	// ----------------------------------------------
	// Attributes
	// ----------------------------------------------
	/**
	 * The percent to alter volume by e.g. 1.0 makes no alteration to the voluem
	 * 0.5 reduces it by half. 2.0 doubles it etc....
	 */
	float mainVolume = (float) 1.0; // default is to make no alteration
	float volume = (float) 1.0;

	// ----------------------------------------------
	// Constructors
	// ----------------------------------------------
	/**
	 * The standard Volume constructor takes a Single Audio Object as input and
	 * sets a default volume which will adjust all samples passing through this
	 * object.
	 * 
	 * @param ao
	 *            The single AudioObject taken as input.
	 */
	public Volume(AudioObject ao) {
		this(ao, 1.0f);
	}

	/**
	 * The standard Volume constructor takes a Single Audio Object as input and
	 * sets a default volume which will adjust all samples passing through this
	 * object.
	 * 
	 * @param ao
	 *            The single AudioObject taken as input.
	 * @param volume
	 *            The default volume for all samples.
	 */
	public Volume(AudioObject ao, double volume) {
		this(ao, (float) volume);
	}

	/**
	 * The standard Volume constructor takes a Single Audio Object as input and
	 * sets a default volume which will adjust all samples passing through this
	 * object.
	 * 
	 * @param ao
	 *            the single AudioObject taken as input.
	 * @param volume
	 *            the default volume for all samples.
	 */
	public Volume(AudioObject ao, float volume) {
		super(ao, "[Volume]");
		this.mainVolume = volume;
	}

	// ----------------------------------------------
	// Public Methods
	// ----------------------------------------------

	/**
	 */
	@Override
	public void build() {
		this.linearVolumeValue = currentNote.getDynamic() / 127.0;
		this.volume = (float) ((1.0 - (Math.log(128.0 - currentNote.getDynamic())) * 0.2)) * mainVolume;
	}

	/**
	 * Return the current lienar volumelevel (0.0 - 1.0 normally).
	 * 
	 * @return volume The current linear volume volue.
	 */
	public double getVolume() {
		return this.linearVolumeValue;
	}

	/**
	 * Specify a new volume level (0.0 - 1.0 normally).
	 * 
	 * @param vol
	 *            The new volume volue.
	 */
	public void setVolume(double volValue) {
		this.linearVolumeValue = volValue;
		this.volume = (float) Math.min(1.0, (Math.abs(Math.log(1.0 - volValue) * 0.2)));
	}

	// ----------------------------------------------
	// Protected Methods
	// ----------------------------------------------
	/**
	 * The nextWork method for <bl>Volume<bl> will multiply the sample and
	 * default volume. Otherwise the inputs are as follows. <br>
	 * <br>
	 * input 2 is the incoming volume <br>
	 * input 1 is the incoming sample <br>
	 */
	@Override
	public int work(float[] buffer) throws AOException {
		int returned = this.previous[0].nextWork(buffer);
		if (this.inputs == 2) {
			float[] tmp = new float[buffer.length];
			if (returned != this.previous[1].nextWork(tmp)) {
				throw new AOException(this.name, 0);
			}
			for (int i = 0; i < returned; i++) {
				buffer[i] = buffer[i] * tmp[i];
			}
		} else {
			for (int i = 0; i < returned; i++) {
				buffer[i] = buffer[i] * this.volume;
			}
		}
		return returned;
	}
}
