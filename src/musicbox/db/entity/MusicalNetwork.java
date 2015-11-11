package musicbox.db.entity;

import java.util.ArrayList;
import java.util.List;

import maggie.network.entity.Edge;
import maggie.network.entity.Network;
import maggie.network.entity.Node;

/**
 * A network has a name and the lists of:
 * <ul>
 * <li>compositions</li>
 * <li>music events, i.e. notes with start and end tick</li>
 * <li>music notes, i.e. network nodes</li>
 * <li>edges</li>
 * <li>sequence of edges</li>
 * <li>composite motif</li>
 * <li>rhythmic motif</li>
 * <li>tonal motif</li>
 * </ul>
 * 
 * @author LIU Xiaofan
 * 
 */
public class MusicalNetwork extends Network {

	private List<Composition> compositions = new ArrayList<Composition>();
	private List<Edge> edgeSeqList = new ArrayList<Edge>();
	private List<Event> eventList = new ArrayList<Event>();
	private List<Motif> motifList = new ArrayList<Motif>();
	private List<Motif> rhythmicMotifList = new ArrayList<Motif>();
	private List<Event> skyLine = new ArrayList<Event>();
	private List<Motif> tonalMotifList = new ArrayList<Motif>();

	public MusicalNetwork() {
		super();
		edgeList = new ArrayList<Edge>();
		nodeList = new ArrayList<Node>();
	}

	@Override
	public float[][] getAdjMatrix() {
		return this.adjMatrix;
	}

	public List<Composition> getCompositions() {
		return compositions;
	}

	@Override
	public List<Edge> getEdgeList() {
		return (List<Edge>) edgeList;
	}

	public List<Edge> getEdgeSeqList() {
		return edgeSeqList;
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public int getLength() {
		return getEventList().size();
	}

	public List<Motif> getMotifList() {
		return motifList;
	}

	@Override
	public List<? extends Node> getNodeList() {
		return nodeList;
	}

	public List<Motif> getRhythmicMotifList() {
		return rhythmicMotifList;
	}

	public List<Event> getSkyLine() {
		return skyLine;
	}

	public List<Motif> getTonalMotifList() {
		return tonalMotifList;
	}

	@Override
	public void setAdjMatrix(float[][] adjMatrix) {
		this.adjMatrix = adjMatrix;
	}

	public void setCompositions(List<Composition> compositions) {
		this.compositions = compositions;
	}

	@Override
	public void setEdgeList(List<? extends Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public void setEdgeSeqList(List<Edge> edgeSeqList) {
		this.edgeSeqList = edgeSeqList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	public void setMotifList(List<Motif> motifList) {
		this.motifList = motifList;
	}

	@Override
	public void setNodeList(List<? extends Node> nodeList) {
		this.nodeList = nodeList;
	}

	public void setRhythmicMotifList(List<Motif> rhythmicMotifList) {
		this.rhythmicMotifList = rhythmicMotifList;
	}

	public void setSkyLine(List<Event> skyLine) {
		this.skyLine = skyLine;
	}

	public void setTonalMotifList(List<Motif> tonalMotifList) {
		this.tonalMotifList = tonalMotifList;
	}
}
