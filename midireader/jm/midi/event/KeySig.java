/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>:49  2001

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

package jm.midi.event;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Andrew Sorensen
 */

public final class KeySig implements Event {
	private final short id;
	private int keyQual;
	private int keySig;
	private int time;

	// ------------------------------------------
	// Constructors
	/** Default Constructor */
	public KeySig() {
		this.id = 022;
		this.time = 0;
		this.keySig = 0;
		this.keyQual = 0;
	}

	public KeySig(int keySig, int keyQual) {
		this.id = 022;
		this.time = 0;
		this.keySig = keySig;
		this.keyQual = keyQual;
	}

	public KeySig(int time, int keySig, int keyQual) {
		this.id = 022;
		this.time = time;
		this.keySig = keySig;
		this.keyQual = keyQual;
	}

	// ------------------------------------------------
	// Copy Object
	@Override
	public Event copy() throws CloneNotSupportedException {
		KeySig event;
		try {
			event = (KeySig) this.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e);
			event = new KeySig();
		}
		return event;
	}

	// ----------------------------------------------
	// Return Id
	@Override
	public short getID() {
		return id;
	}

	// --------------------------------------------
	public int getKeyQuality() {
		return this.keyQual;
	}

	public int getKeySig() {
		return this.keySig;
	}

	// -----------------------------------------------
	// time
	@Override
	public int getTime() {
		return time;
	}

	// ---------------------------------------------------
	// Print
	@Override
	public void print() {
		System.out.println("KeySig(022):             [time = " + this.time + "][keySig = " + this.keySig + "][keyQual = "
				+ this.keyQual + "]");
	}

	// ----------------------------------------------
	// Read the contends of this objec in from disk
	@Override
	public int read(DataInputStream dis) throws IOException {
		return 0;
	}

	public void setKeyQuality(int keyQuality) {
		this.keyQual = keyQual;
	}

	public void setKeySig(int keySig) {
		this.keySig = keySig;
	}

	@Override
	public void setTime(int time) {
		this.time = time;
	}

	// ----------------------------------------------
	// Write the contents of this object out to disk
	// ----------------------------------------------
	@Override
	public int write(DataOutputStream dos) throws IOException {
		int bytes_out = jm.midi.MidiUtil.writeVarLength(this.time, dos);
		dos.writeByte(0xFF);
		dos.writeByte(0x59);
		bytes_out += jm.midi.MidiUtil.writeVarLength(2, dos);
		dos.writeByte((byte) this.keySig);
		dos.writeByte((byte) this.keyQual);
		return bytes_out + 4;
	}
}
