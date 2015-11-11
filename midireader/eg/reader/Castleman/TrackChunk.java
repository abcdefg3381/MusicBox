package eg.reader.Castleman;

/*
 * TrackChunk.java
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

/**
 * Reperesentation of a MTrk chunk, which encodes a MIDI track
 * 
 * @see CMidi
 */
final class TrackChunk {
	/**
	 * Magic 4 bytes that identify a track chunk
	 * 
	 * @see CMidi#writeChunk(String,byte[])
	 */
	public static final String TRACK_TYPE = "MTrk";

	private int arySize;
	private byte[] bytes;
	private int chunklen;

	private boolean haveNL = false;
	private boolean haveSpace = false;

	/**
	 * Creates a new TrackChunk
	 * <p>
	 * The intial buffer size will be 32
	 * 
	 * @see TrackChunk(int)
	 */
	public TrackChunk() {
		this(32);
	}

	/**
	 * Creates a new TrackChunk
	 * <p>
	 * The TrackChunk is basically a {@link java.util.Vector} for
	 * <code>byte</code>s. It's got a buffer, which will grow automatically as
	 * things are added to it.
	 * 
	 * @param initialSize
	 *            Initial bufffersize.
	 */
	public TrackChunk(int initialSize) {
		chunklen = 0;
		arySize = initialSize;
		bytes = new byte[arySize];
	}

	/**
	 * Adds a single byte to the buffer.
	 * 
	 * @param data
	 *            the byte
	 */
	public void addByte(byte data) {
		if (chunklen + 1 > arySize) {
			int newSize = arySize * 2;
			byte[] newAry = new byte[newSize];
			System.arraycopy(bytes, 0, newAry, 0, chunklen);
			arySize = newSize;
			bytes = newAry;
		}
		bytes[chunklen++] = data;
	}

	/**
	 * Adds bytes to the chunk
	 * 
	 * @param bytes
	 *            the bytes to add
	 */
	public void addBytes(byte[] data) {
		if (chunklen + data.length > arySize) {
			int newSize = Math.max(chunklen + data.length, arySize * 2);
			byte[] newAry = new byte[newSize];
			System.arraycopy(bytes, 0, newAry, 0, chunklen);
			arySize = newSize;
			bytes = newAry;
		}
		System.arraycopy(data, 0, bytes, chunklen, data.length);
		chunklen += data.length;
	}

	/**
	 * Adds a text tag.
	 * 
	 * @param text
	 *            The text to add.
	 */
	public void addText(String text) {
		addByte((byte) (0xFF));
		addByte((byte) (0x03));
		addVarLen(text.length());
		addBytes(text.getBytes());
	}

	/**
	 * Adds a funky varlen.
	 * <p>
	 * This is a datatype used by MIDI files which can hold values from
	 * 0-0x0FFFFFFF and takes between 1 and 4 bytes.
	 * 
	 * @param value
	 *            The varlen to add.
	 */
	public void addVarLen(int value) {
		int buffer = (value & 0x7F);

		while ((value >>= 7) != 0) {
			buffer <<= 8;
			buffer |= ((value & 0x7F) | 0x80);
		}

		while (true) {
			addByte((byte) (buffer & 0xFF));
			if ((buffer & 0x80) != 0) {
				buffer >>= 8;
			} else {
				break;
			}
		}
	}

	/**
	 * Ends the track.
	 */
	public void endTrack() {
		final byte[] data = { (byte) (0x00), (byte) (0xFF), (byte) (0x2f), (byte) (0x00) };
		addBytes(data);
	}

	/**
	 * Gets the bytes in a form suitable for passing to
	 * {@link CMidi#writeChunk(String,byte[])}
	 * 
	 * @return an array of bytes
	 */
	public byte[] getBytes() {
		byte[] ret = new byte[chunklen];
		System.arraycopy(bytes, 0, ret, 0, chunklen);
		return ret;
	}

	private void realWriteData(byte b) {
		addVarLen(0);
		addByte((byte) 0x90); // note on
		addByte(b);
		addByte((byte) (127));
		int timing;
		if (haveNL) {
			timing = 48;
		} else if (haveSpace) {
			timing = 24;
		} else {
			timing = 12;
		}
		addVarLen(timing);
		addByte((byte) 0x80); // note off
		addByte(b);
		addByte((byte) (127));
		haveSpace = haveNL = false;
	}

	/**
	 * Adds a note representing a byte from the input file.
	 * 
	 * @param b
	 *            The character to add.
	 * @trows IllegalArgumentException if <code>b > 127</code>
	 */
	public void writeDataByte(byte b) {
		if (b > 127) {
			throw new IllegalArgumentException("Notes must be between 0 and 127");
		}
		switch (b) {
		case ' ':
		case '\t':
			haveSpace = true;
			break;
		case '\n':
		case '\r':
			haveNL = true;
			break;
		default:
			realWriteData(b);
			break;
		}
	}
}
