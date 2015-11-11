package gdi.reader.db.handler;

import gdi.reader.db.entity.Edge;

import java.util.ArrayList;
import java.util.List;

public interface EdgeHandler {
	public ArrayList<Edge> getAllEdges();

	public int getEdgeNumber();

	public List<Edge> getEdgeWithPreAMN(int oldID);

	public void saveEdge(Edge edge);
}
