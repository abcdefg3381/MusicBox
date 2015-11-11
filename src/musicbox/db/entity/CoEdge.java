package musicbox.db.entity;

import maggie.network.entity.Edge;
import maggie.network.entity.Node;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * An edge has from and to nodes, a weight and a type of co-occurent or
 * concurrent.
 * 
 * @author LIU Xiaofan
 * 
 */
public class CoEdge extends Edge {

	/**
	 * proportion of from note duration
	 */
	private float duration = 1;

	/**
	 * type true for co-occurrence type false for concurrent
	 */
	private boolean type;

	public CoEdge(Node node, Node node2, boolean type) {
		this.pair = new Pair<Node>(node, node2);
		this.weight = 1;
		this.type = type;
		// TODO for the moment
		// duration *= this.getFrom().getDuration();
		duration = ((int) (duration * 100)) / 100;
	}

	public CoEdge(Node from, Node to, boolean type, float duration) {
		this.pair = new Pair<Node>(from, to);
		this.weight = 1;
		this.type = type;
		// TODO for the moment
		// duration *= this.getFrom().getDuration();
		this.duration = ((int) (duration * 100)) / 100;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CoEdge) {
			CoEdge edge = (CoEdge) obj;
			if (edge.type == true) {
				return edge.getFrom().equals(this.getFrom()) && edge.getTo().equals(this.getTo())
						&& edge.getDuration() == this.getDuration();
			} else if (edge.type == false) {
				return (edge.getFrom().equals(this.getFrom()) && edge.getTo().equals(this.getTo()))
						|| (edge.getTo().equals(this.getFrom()) && edge.getFrom().equals(this.getTo()));
			}
		}
		return false;
	}

	public float getDuration() {
		return duration;
	}

	@Override
	public Pair<? extends Node> getPair() {
		return pair;
	}

	/**
	 * @return true for co-occurrence, false for concurrent
	 */
	@Override
	public boolean getType() {
		return type;
	}

	@Override
	public float getWeight() {
		return weight;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}