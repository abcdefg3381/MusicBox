/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>:57  2001

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

public final class TimeSig implements Event {
	private int denominator;
	private final short id;
	private int numerator;
	private int time;

	// ------------------------------------------
	// Constructors
	/** Default Constructor */
	public TimeSig() {
		this.id = 021;
		this.time = 0;
		this.numerator = 4;
		this.denominator = 4;
	}

	public TimeSig(int numerator, int denominator) {
		this.id = 021;
		this.time = 0;
		this.denominator = denominator;
		this.numerator = numerator;
	}

	public TimeSig(int time, int numerator, int denominator) {
		this.id = 021;
		this.time = time;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	// ------------------------------------------------
	// Copy Object
	@Override
	public Event copy() throws CloneNotSupportedException {
		TimeSig event;
		try {
			event = (TimeSig) this.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e);
			event = new TimeSig();
		}
		return event;
	}

	// --------------------------------------------
	public int getDenominator() {
		return this.denominator;
	}

	// ----------------------------------------------
	// Return Id
	@Override
	public short getID() {
		return id;
	}

	public int getNumerator() {
		return this.numerator;
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
		System.out.println("TimeSig(021):             [time = " + this.time + "][numerator = " + this.numerator
				+ "][denominator = " + this.denominator + "]");
	}

	// ----------------------------------------------
	// Read the contends of this objec in from disk
	@Override
	public int read(DataInputStream dis) throws IOException {
		return 0;
	}

	public void setDenominator(int denom) {
		this.denominator = denom;
	}

	public void setNumerator(int numer) {
		this.numerator = numer;
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
		dos.writeByte(0x58);
		bytes_out += jm.midi.MidiUtil.writeVarLength(4, dos);
		dos.writeByte((byte) this.numerator);
		int num = this.denominator;
		int cnt = 0;
		while (num % 2 == 0) {
			num = num / 2;
			cnt++;
		}
		dos.writeByte((byte) cnt);
		dos.writeByte(0x18);
		dos.writeByte(0x08);
		return bytes_out + 6;
	}
}
