package musicbox.logic.form;

import maggie.network.entity.Edge;
import musicbox.db.entity.MusicalNetwork;

/**
 * Preprocess network, including
 * <ul>
 * <li>form edges</li>
 * <li>calculate degree</li>
 * <li>skyline transfer</li>
 * </ul>
 * 
 * @author LIU Xiaofan
 * 
 */
public class NetworkFormer {

	private EdgeFormer edgeForm;
	private MusicalNetwork network = new MusicalNetwork();
	private NodeFormer nodeForm;
	private int nodeType;
	long sc;
	private boolean skyline;

	private SkylineProcessor skyProc;

	/**
	 * @param input
	 */
	public NetworkFormer(MusicalNetwork input) {
		network = input;
		edgeForm = new EdgeFormer(network);
	}

	public NetworkFormer(MusicalNetwork input, boolean skyline, int nodeType, int edgeType) {
		network = input;
		skyProc = new SkylineProcessor(network);
		nodeForm = new NodeFormer(network);
		edgeForm = new EdgeFormer(network);
		this.skyline = skyline;
		this.nodeType = nodeType;
	}

	private void calcDegree() {
		for (Edge e : network.getEdgeList()) {
			// if (e.getType()) {
			e.getFrom().setOutStrength(e.getFrom().getOutStrength() + e.getWeight());
			e.getTo().setInStrength(e.getTo().getInStrength() + e.getWeight());
			e.getFrom().addOutDegree();
			e.getTo().addInDegree();
			// }
		}
	}

	private void displayTimeUsed() {
		System.out.println(" finished in " + (System.currentTimeMillis() - sc) + " ms");
	}

	/**
	 * 
	 */
	public void formNetworkComposed() {
		// 4. form edges
		// TODO by far only co-occurrence edge is supported
		System.out.print("forming edges...");
		recordTime();
		edgeForm.formEdge();
		displayTimeUsed();

		// 5. adj matrix formed
		System.out.print("forming adj matrix... " + network.getNodeList().size() + " X " + network.getNodeList().size());
		recordTime();
		float[][] matrix = new float[network.getNodeList().size()][network.getNodeList().size()];
		for (Edge edge : network.getEdgeList()) {
			matrix[network.getNodeList().indexOf(edge.getPair().getFirst())][network.getNodeList().indexOf(
					edge.getPair().getSecond())] = edge.getWeight();
		}
		network.setAdjMatrix(matrix);
		displayTimeUsed();

		// 3. calc degrees
		System.out.print("calculating degrees...");
		recordTime();
		calcDegree();
		displayTimeUsed();
	}

	/**
	 * 
	 */
	public void formNetworkOrigin() {
		// 1. skyline transform
		if (skyline) {
			System.out.print("skyline processing...");
			recordTime();
			skyProc.replaceWithSkyline();
			displayTimeUsed();
		}

		// 2. unify music length
		unifyLength(10000);

		// 3. form note
		System.out.print("forming nodes...");
		recordTime();
		nodeForm.formNode(nodeType);
		displayTimeUsed();

		// 4. form edges
		// TODO by far only co-occurrence edge is supported
		System.out.print("forming edges...");
		recordTime();
		edgeForm.formEdge();
		displayTimeUsed();

		// 5. adj matrix formed
		System.out.print("forming adj matrix... " + network.getNodeList().size() + " X " + network.getNodeList().size());
		recordTime();
		float[][] matrix = new float[network.getNodeList().size()][network.getNodeList().size()];
		for (Edge edge : network.getEdgeList()) {
			matrix[network.getNodeList().indexOf(edge.getPair().getFirst())][network.getNodeList().indexOf(
					edge.getPair().getSecond())] = edge.getWeight();
		}
		network.setAdjMatrix(matrix);
		displayTimeUsed();

		// 3. calc degrees
		System.out.print("calculating degrees...");
		recordTime();
		calcDegree();
		displayTimeUsed();

	}

	private void recordTime() {
		sc = System.currentTimeMillis();
	}

	/**
	 * @param length
	 */
	private void unifyLength(int length) {
		if (network.getLength() > length) {
			network.setEventList(network.getEventList().subList(0, length));
		}
	}
}
