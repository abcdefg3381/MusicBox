package eg.reader.Castleman;

/*
 * ReadTrack.java
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

import java.io.IOException;
import java.io.InputStream;

/**
 * Reads a chunk from a {@link InputStream} and aids in parsing it if it's a
 * track.
 */
final class ReadTrack {
	/** Little data structure representing a MIDI event */
	public static class Event {
		/** actual data for the event. */
		public byte[] data;
		/** Time since previous event */
		public int deltaTime;
		/** Status byte (indicates type of event) */
		public byte status;

		/** creates a new <code>Event</code> */
		public Event(int deltaTime, byte status, byte[] data) {
			this.deltaTime = deltaTime;
			this.status = status;
			this.data = data;
		}
	}

	protected String chunkType;
	protected byte[] data;
	private int inPos;
	private byte lastStatus;

	/**
	 * Reads a chunk from the stream and makes it ready for parsing.
	 * 
	 * @throws IOException
	 *             if there's a problem reading the data.
	 */
	public ReadTrack(InputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b);
		chunkType = new String(b);

		in.read(b);
		int chunkLen = (((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | ((b[3] & 0xFF)));
		System.err.println(chunkType + " " + chunkLen + " (" + b[0] + "," + b[1] + "," + b[2] + "," + b[3] + ")");
		data = new byte[chunkLen];
		in.read(data);
		inPos = 0;
	}

	/**
	 * Constructs a reader for the specified chunk type and data
	 */
	public ReadTrack(String chunkType, byte[] data) {
		this.data = (data.clone());
		this.chunkType = chunkType;
		inPos = 0;
	}

	/**
	 * Reads from the buffer
	 * 
	 * @param buf
	 *            The buffer to fill
	 */
	public void read(byte[] buf) {
		read(buf, 0, buf.length);
	}

	/**
	 * Reads from the buffer
	 * 
	 * @param buf
	 *            A buffer to store data into
	 * @param start
	 *            Where in the buffer to start storing data
	 * @param len
	 *            How many bytes to store
	 */
	public void read(byte[] buf, int start, int len) {
		System.arraycopy(data, inPos, buf, start, len);
		inPos += len;
	}

	/**
	 * Reads a byte from the buffer
	 */
	public byte readByte() {
		return data[inPos++];
	}

	/**
	 * Reads a MIDI event
	 * 
	 * @return an {@link Event} or <code>null</code> if the track is done
	 */
	public Event readEvent() {
		if (this.inPos >= this.data.length) {
			return null;
		}
		int deltaTime = readVarLen();
		byte status = readByte();
		if ((status & (byte) (0x80)) == 0) {
			System.out.println(status + " " + (status & (byte) (0x80)) + " " + lastStatus);
			System.out.println((status & (byte) (0x80)) == 0);
			unReadByte();
			status = lastStatus;
		} else {
			lastStatus = status;
		}
		byte[] data = null;
		if (status == (byte) (0xFF)) { // midi file special stuff
			byte type = readByte();
			int len = readVarLen();
			data = new byte[1 + len];
			data[0] = type;
			read(data, 1, len);
		} else {
			int len;
			switch (status & (byte) (0xF0)) { // get 'status nibble'
			case (byte) (0x80): // note off
			case (byte) (0x90): // note on
			case (byte) (0xA0): // after touch
			case (byte) (0xB0): // controller
			case (byte) (0xE0): // pitch wheel
				len = 2;
				break;
			case (byte) (0xC0): // program change
			case (byte) (0xD0): // channel pressure
				len = 1;
				break;
			case (byte) (0xF0):
				switch (status & (byte) (0x0F)) {
				// case 0xFF is special and handled above
				case (byte) (0x00): // System exclusive
				case (byte) (0x07): // system exclusive continuation
					len = readVarLen();
					break;
				case (byte) (0x01): // MTC quarter frame
				case (byte) (0x03): // Song select
					len = 1;
					break;
				case (byte) (0x02): // Song position pointer
					len = 2;
					break;
				case (byte) (0x06): // Tune request
				case (byte) (0x08): // MIDI clock
				case (byte) (0x09): // Tick
				case (byte) (0x0A): // start
				case (byte) (0x0B): // continue
				case (byte) (0x0C): // stop
				case (byte) (0x0E): // active sense
					len = 0;
					break;
				default:
					throw new RuntimeException("Invalid MIDI byte or broken parser?");
				}
				break;
			default:
				throw new RuntimeException("I'm confused");
			}
			data = new byte[len];
			read(data);
		}
		return new Event(deltaTime, status, data);
	}

	/**
	 * Reads a variable length value
	 * 
	 * @see TrackChunk#addVarLen(int)
	 */
	public int readVarLen() {
		int value;
		byte c;

		if (((value = readByte()) & 0x80) != 0) {
			value &= 0x7F;
			do {
				value = (value << 7) + ((c = readByte()) & 0x7F);
			} while ((c & (byte) (0x80)) != 0);
		}

		return value;
	}

	@Override
	public String toString() {
		return "ReadTrack " + chunkType + ", " + data.length + " bytes";
	}

	/**
	 * Pushes back the previously read byte
	 */
	public void unReadByte() {
		inPos--;
	}
}
