/**
 * 
 */
package musicbox.logic.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import maggie.common.algorithm.Interval;
import maggie.common.algorithm.SetAlgorithm;
import musicbox.db.entity.Event;
import musicbox.db.entity.MusicalNetwork;

/**
 * @author LIU Xiaofan
 * 
 */
public class SkylineProcessor {

	private List<Event> melody = new ArrayList<Event>();
	private MusicalNetwork network;

	/**
	 * @param network
	 */
	public SkylineProcessor(MusicalNetwork network) {
		this.network = network;
	}

	public void findSkyline() {

		// sort by pitch
		Collections.sort(network.getEventList(), new Comparator<Event>() {
			@Override
			public int compare(Event e1, Event e2) {
				return e1.getNote().getPitch() > e2.getNote().getPitch() ? 0 : 1;
			}
		});

		// calc top and add event to melody line
		melody.clear();
		for (Event e : network.getEventList()) {
			// first construct melody time line
			// then compare with event
			if (top(e) < 0.5) {
				melody.add(e);
			}
		}

		// sort melody by time
		Collections.sort(melody, new Comparator<Event>() {
			@Override
			public int compare(Event e1, Event e2) {
				return (int) (e1.getStartTick() - e2.getStartTick());
			}
		});

		network.setSkyLine(melody);
	}

	public void replaceWithSkyline() {

		// find sky line
		findSkyline();

		// replace network with melody
		network.setEventList(network.getSkyLine());
	}

	private double top(Event e) {
		Interval eIn = new Interval((int) e.getStartTick(), (int) e.getEndTick());
		List<Interval> intervals = new ArrayList<Interval>();
		for (Event me : melody) {
			if (me.getComposition().equals(e.getComposition())) {
				intervals.add(new Interval((int) me.getStartTick(), (int) me.getEndTick()));
			}
		}

		int intersectDu = 0;
		Interval[] intarray = intervals.toArray(new Interval[intervals.size()]);
		for (Interval interval : SetAlgorithm.intersect(intarray, eIn)) {
			intersectDu += interval.getLength();
		}
		return (double) intersectDu / (double) eIn.getLength();
	}
}
