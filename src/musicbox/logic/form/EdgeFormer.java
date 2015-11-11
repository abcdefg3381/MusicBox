/**
 * 
 */
package musicbox.logic.form;

import java.util.ArrayList;
import java.util.List;

import maggie.network.entity.Edge;
import maggie.network.entity.Node;
import musicbox.db.entity.CoEdge;
import musicbox.db.entity.MusicalNetwork;

/**
 * @author LIU Xiaofan
 * 
 */
public class EdgeFormer {
	public static final int CO = 1;
	public static final int SE = 2;
	private MusicalNetwork network;

	/**
	 * @param network
	 */
	public EdgeFormer(MusicalNetwork network) {
		this.network = network;
	}

	private void addEdgeToList(Edge edge) {
		network.getEdgeSeqList().add(edge);
		if (network.getEdgeList().contains(edge)) {
			network.getEdgeList().get(network.getEdgeList().indexOf(edge)).addWeight();
			return;
		}
		network.getEdgeList().add(edge);
	}

	/**
	 * @return
	 */
	public MusicalNetwork formEdge() {

		// TODO for the moment, changed
		// for (Composition comp : network.getCompositions()) {
		// formCoOccurEdge(comp);
		// }
		System.out.println(network.getNodeList().size() + " notes");
		for (int i = 0; i < network.getNodeList().size() - 1; i++) {
			addEdgeToList(new CoEdge(network.getNodeList().get(i), network.getNodeList().get(i + 1), true));
		}

		List<Node> tmpList = new ArrayList<Node>();
		for (Node node : network.getNodeList()) {
			if (!tmpList.contains(node)) {
				tmpList.add(node);
			}
		}
		network.setNodeList(tmpList);

		return network;
	}
}
