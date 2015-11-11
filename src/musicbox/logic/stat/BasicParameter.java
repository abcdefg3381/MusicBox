/**
 * 
 */
package musicbox.logic.stat;

/**
 * @author LIU Xiaofan
 * 
 */
public class BasicParameter {

	private float meanDegree;

	private float meanDistance;
	private int noe;
	private int non;

	public BasicParameter(int non, int noe, float meanDegree, float meanDistance) {
		super();
		this.noe = noe;
		this.non = non;
		this.meanDegree = meanDegree;
		this.meanDistance = meanDistance;
	}

	public float getMeanDegree() {
		return meanDegree;
	}

	public float getMeanDistance() {
		return meanDistance;
	}

	public int getNoe() {
		return noe;
	}

	public int getNon() {
		return non;
	}

}
