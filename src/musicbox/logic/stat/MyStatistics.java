package musicbox.logic.stat;

import java.io.File;

import maggie.network.metrics.MaggieNetworkStatistics;
import musicbox.db.entity.MusicalNetwork;

/**
 * This class calculates the properties of networks.<br>
 * It also calls mfinder1.2 to calculate motifs.
 * 
 * @author LIU Xiaofan
 * 
 */
public class MyStatistics extends MaggieNetworkStatistics {

	/**
	 * @param network
	 * @return
	 */
	public static BasicParameter getBasicParameters(MusicalNetwork network) {
		MyStatistics ms = new MyStatistics(network);
		ms.initialize();
		BasicParameter bp =
				new BasicParameter(ms.getNetworkN(), ms.getNetworkM(), ms.getMeanDegree(), ms.getMeanDistanceAndDiameter()[0]);
		return bp;
	}

	/**
	 * @param network
	 */
	public MyStatistics(MusicalNetwork network) {
		super(network);
	}

	public MyStatistics(MusicalNetwork network, File file) {
		super(network, file);
	}

	/**
	 * 
	 */
	private void printConnectivityTableWeighted() {
		for (int[] element : adjacencyMatrixWeighted) {
			for (int j = 0; j < element.length; j++) {
				print(element[j] + ",");
			}
			println();
		}
	}

	@Override
	public void printReport() {
		super.printReport();
		println("total number of notes: " + ((MusicalNetwork) network).getLength());
		printConnectivityTableWeighted();
		// finish
		closePrintWriter();
	}
}