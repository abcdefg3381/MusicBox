/*
 * Tempo.java
 *
 * Created on 2 March 2004, 13:52
 */

package jm.music.data;

/**
 * 
 * @author Rene Wooller
 */
public class Tempo {
	// do andante etc.
	public static double ANDANTE = 140;

	public static double DEFAULT_HIGH = 1000000;
	public static double DEFAULT_LOW = 0.0000001;
	public static double DEFAULT_TEMPO = 60;

	private double highestTempo = DEFAULT_HIGH;

	private double lowestTempo = DEFAULT_LOW;

	private double value = this.DEFAULT_TEMPO;

	/** Creates a new instance of Tempo */
	public Tempo() {
	}

	public Tempo(double initTempo) {
		value = initTempo;
	}

	private double checkBelowZero(double d) {
		if (d < 0) {
			System.out.println("lowestTempo must be positive number!");
			System.out.println("setting it to 0.001");
			d = 0.001;
		}
		return d;
	}

	// public void setTempo(int newTempo) {
	// value = setInBounds(newTempo);
	// }

	public double getHighestTempo() {
		return this.highestTempo;
	}

	public double getLowestTempo() {
		return this.lowestTempo;
	}

	public double getPerMinute() {
		return value;
	}

	public double getPerSecond() {
		return 60.0 / this.value;
	}

	public void setHighestTempo(double d) {
		d = checkBelowZero(d);
		highestTempo = d;
	}

	private double setInBounds(double newTempo) {
		if (newTempo <= lowestTempo) {
			newTempo = lowestTempo;
		} else if (newTempo >= highestTempo) {
			newTempo = highestTempo;
		}
		return newTempo;
	}

	public void setLowestTempo(double d) {
		d = checkBelowZero(d);
		this.lowestTempo = d;
	}

	public void setTempo(double newTempo) {
		value = setInBounds(newTempo);
	}
}
