package eg.reader.Castleman;

/*
 * CMidi.java
 * Copyright 2001 Michael Castleman, mlc67@columbia.edu
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Converts a .c file (any text file, really) into a MIDI file.
 * 
 * @author Michael Castleman, <a
 *         href="mailto:mlc67@columbia.edu">mlc67@columbia.edu</a>
 * @version 0.1
 */
public class CMidi {
	/**
	 * Main routine
	 * 
	 * @param args
	 *            Command-line arguments. Should be the name of the input C file
	 *            and the output MIDI file.
	 * @exception IOException
	 *                if there's a problem reading or writing
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Syntax:");
			System.out.println("\tjava -cp cmidi.jar CMidi infile.c outfile.mid");
			System.exit(1);
		}
		FileInputStream ins = new FileInputStream(args[0]);
		FileOutputStream outs = new FileOutputStream(args[1]);
		CMidi cm = new CMidi(ins, outs);
		cm.doConversion();
		ins.close();
		outs.close();
	}

	protected InputStream in;

	protected OutputStream out;

	/**
	 * Constructs a new CMidi
	 * 
	 * @param in
	 *            The source file
	 * @param out
	 *            Where the MIDI will be written to.
	 */
	public CMidi(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	/**
	 * Actually does the conversion.
	 * 
	 * @exception IOException
	 *                If a read or write error occurred.
	 */
	public void doConversion() throws IOException {
		writeHeader();
		TrackChunk tc = new TrackChunk();

		// all controllers off
		tc.addBytes(new byte[] { 0, (byte) (0xb0), (byte) (121), 0 });

		// bank select 0
		tc.addBytes(new byte[] { 0, 0, 0 });

		// program change: acoustic guitar
		tc.addBytes(new byte[] { 0, (byte) (0xc0), 25 });

		// max volume
		tc.addBytes(new byte[] { 0, (byte) (0xb0), (byte) (7), (byte) (0x7f) });

		int inByte;
		while ((inByte = in.read()) != -1) {
			tc.writeDataByte((byte) inByte);
		}

		tc.endTrack();
		writeChunk(TrackChunk.TRACK_TYPE, tc.getBytes());
	}

	/**
	 * Writes a chunk
	 * 
	 * @param type
	 *            Type of chunk. <b>MUST BE 4 CHARS LONG</b>
	 * @param data
	 *            Chunk data.
	 * @exception IllegalArgumentException
	 *                If <code>type.length() != 4</code>
	 */
	protected void writeChunk(String type, byte[] data) throws IllegalArgumentException, IOException {
		if (type.length() != 4) {
			throw new IllegalArgumentException("Bad type");
		}

		out.write(type.getBytes());
		int len = data.length;
		out.write((byte) ((len & 0xFF00000) >> 24));
		out.write((byte) ((len & 0x00FF000) >> 16));
		out.write((byte) ((len & 0x000FF00) >> 8));
		out.write((byte) ((len & 0x00000FF)));
		out.write(data);
	}

	/**
	 * Writes the MIDI header
	 */
	protected void writeHeader() throws IOException {
		// 0x0001 - multiple track file
		// 0x0002 - 2 tracks
		// 0x0060 - 96 pulses per quarter note (ppqn)
		byte[] chunk = { 0x00, 0x01, 0x00, 0x02, 0x00, 0x60 };
		writeChunk("MThd", chunk);

		// stupid track
		TrackChunk tc = new TrackChunk();
		tc.addVarLen(0);
		tc.addByte((byte) 0xff);
		tc.addByte((byte) 0x51);
		tc.addByte((byte) 0x03);
		tc.addByte((byte) 0x07);
		tc.addByte((byte) 0xa1);
		tc.addByte((byte) 0x20);
		tc.addVarLen(0);
		tc.addText("This file was created by CMidi. Run MidiC to get the data back out.");
		tc.endTrack();
		writeChunk(TrackChunk.TRACK_TYPE, tc.getBytes());
	}
}
