package eg.reader.Castleman;

/*
 * MidiC.java
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
 * Converts a MIDI file output by CMidi back into C code.
 */
public class MidiC {
	/**
	 * Main routine
	 * 
	 * @param args
	 *            Command-line arguments. Should be the name of the input MIDI
	 *            file and the output C file.
	 * @exception IOException
	 *                if there's a problem reading or writing
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Syntax:");
			System.out.println("\tjava -cp cmidi.jar MidiC infile.mid outfile.c");
			System.out.println("\t\"-\" means use stdin/out");
			System.exit(1);
		}
		InputStream ins = (args[0].equals("-") ? System.in : new FileInputStream(args[0]));
		OutputStream outs = (args[1].equals("-") ? (OutputStream) (System.out) : new FileOutputStream(args[1]));
		MidiC mc = new MidiC(ins, outs);
		mc.doConversion();
		ins.close();
		outs.close();
	}

	protected InputStream in;

	protected OutputStream out;

	/** pulses per quarter note */
	protected int ppqn;

	/**
	 * Constructs a new MidiC
	 * 
	 * @param in
	 *            The source MIDI file
	 * @param out
	 *            Where the C code will be written to.
	 */
	public MidiC(InputStream in, OutputStream out) {
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
		readHeader();

		ReadTrack mtrk;
		do {
			mtrk = new ReadTrack(in);
		} while (!mtrk.chunkType.equals("MTrk"));

		ReadTrack.Event evt;
		while ((evt = mtrk.readEvent()) != null) {
			if ((evt.status == (byte) (0x80)) || // note off, track 0
					((evt.status == (byte) (0x90)) && (evt.data[1] == 0))) {
				if (evt.deltaTime >= (ppqn / 2)) {
					out.write('\n');
				} else if (evt.deltaTime >= (ppqn / 4)) {
					out.write(' ');
				}
				out.write(evt.data[0]);
			}
		}
		out.write('\n');
	}

	private void readHeader() throws IOException {
		ReadTrack head = new ReadTrack(in);
		if ((!head.chunkType.equals("MThd")) || (head.data.length != 6)) {
			throw new RuntimeException("input is not a MIDI file");
		}
		if (head.data[1] != (byte) (0x01)) {
			throw new RuntimeException("I can only handle format-1 MIDI files");
		}
		if (head.data[4] < 0) {
			throw new RuntimeException("I don't yet know how to handle SMTPE-timecoded MIDI files");
		}

		ppqn = (head.data[4] << 8) | (head.data[5]);

		// there's a stupid track which we can ignore here.
		// also ignore any non-MTrk chunk (there shouldn't be any, but you never
		// know)
		ReadTrack stupid;
		do {
			stupid = new ReadTrack(in);
		} while (!stupid.chunkType.equals("MTrk"));
	}
}
