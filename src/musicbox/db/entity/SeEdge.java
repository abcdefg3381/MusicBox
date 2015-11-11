package musicbox.db.entity;

import maggie.network.entity.Edge;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * An edge has from and to nodes, a weight and a type of co-occurent or
 * concurrent.
 * 
 * @author LIU Xiaofan
 * 
 */
public class SeEdge extends Edge {

	/**
	 * type true for co-occurrence type false for concurrent
	 */
	private boolean type;

	private float weight;

	public SeEdge(Note from, Note to, boolean type) {
		this.pair = new Pair<Note>(from, to);
		this.weight = 1;
		this.type = type;
	}

	public SeEdge(Note from, Note to, boolean type, float duration) {
		this.pair = new Pair<Note>(from, to);
		this.weight = 1;
		this.type = type;
	}

	@Override
	public void addWeight() {
		this.weight++;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SeEdge) {
			SeEdge edge = (SeEdge) obj;
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
		return this.getPair().getFirst().getDuration();
	}

	@Override
	public Note getFrom() {
		return (Note) pair.getFirst();
	}

	@Override
	public Pair<Note> getPair() {
		return (Pair<Note>) pair;
	}

	@Override
	public Note getTo() {
		return (Note) pair.getSecond();
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

	public void setType(boolean type) {
		this.type = type;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}