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

/*
 * Sequence.java
 *
 * Created on 25 May 2003, 11:01
 */

package jmms;

import grame.midishare.Midi;


/**
 * 
 * @author Rene Wooller
 */
public class Sequence {

	public int counter = 0;
	public int eventAt = 0;

	public int[] midishareEvents = new int[200];

	/** Creates a new instance of Sequence */
	public Sequence() {
	}

	public void addMidishareEvent(int event) {
		// System.out.println(" sequence before ");
		// this.print();

		if (counter < midishareEvents.length) {

			if (counter == 0) {
				midishareEvents[counter++] = event;
				// System.out.println("sequence ");
				// System.out.println(Midi.GetDate(midishareEvents[0]));
				// this.print();
				return;
			}

			// find the insertion point
			int tc = -1; // temp counter
			// int insertionPoint = 0;
			int date = Midi.GetDate(event);
			boolean ipFound = false; // insertion point found
			while (tc + 1 < counter && !ipFound) {
				if (Midi.GetDate(midishareEvents[++tc]) >= date) {
					ipFound = true;
				}
			}

			if (ipFound) {
				// insert the event
				insert(event, tc);
			} else { // ( it got to the end without finding anything bigger than
						// it
				// add it to the end
				midishareEvents[counter] = event;
			}

			counter++;
			/*
			 * System.out.println("sequence "); for(int i=0; i< counter; i++) {
			 * System.out.println("n " + Midi.GetData0(midishareEvents[i]));
			 * System.out.println(Midi.GetDate(midishareEvents[i])); }
			 */
		}
	}

	private void insert(int event, int insertPoint) {
		// System.out.println(" inserting event ");
		// fill an array full of the ones after it
		int[] excess = new int[counter - insertPoint];
		for (int i = insertPoint; i < counter; i++) {
			excess[i - insertPoint] = midishareEvents[i];
		}

		// put it in
		midishareEvents[insertPoint] = event;

		// copy the excess to be after it
		for (int i = 0; i < excess.length; i++) {
			midishareEvents[insertPoint + 1 + i] = excess[i];
		}

	}

	public int nextEvent() {
		if (eventAt < counter) {
			return midishareEvents[eventAt++];
		} else { // nothing left
			return 0;
		}
	}

	public void print() {
		// for(int i=0; i<midishareEvents.length; i++) {
		// System.out.print(midishareEvents[i] + ", ");
		// }

		for (int i = 0; i < counter; i++) {
			System.out.println(Midi.GetDate(midishareEvents[i]) + ", " + Midi.GetField(midishareEvents[i], 2) + ", .....:"
					+ Midi.GetData0(midishareEvents[i]) + ", ");
		}
		System.out.println("counter = " + counter);
	}

	public void reset() {
		for (int i = 0; i < counter; i++) {
			Midi.FreeEv(midishareEvents[i]);
		}
		counter = 0;
		eventAt = 0;
	}
}
