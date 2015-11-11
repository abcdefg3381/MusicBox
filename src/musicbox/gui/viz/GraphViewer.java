package musicbox.gui.viz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maggie.network.entity.Edge;
import maggie.network.entity.Node;
import musicbox.db.entity.Motif;
import musicbox.db.entity.Note;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * This class draws a network as a node-edge graph using several algorithms.<br>
 * This class uses resources in project JUNG.
 * 
 * @author LIU Xiaofan
 * 
 */
@SuppressWarnings("unchecked")
public class GraphViewer extends VisualizationViewer<Node, Edge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6028467353320336378L;

	Map<Edge, Paint> edgePaints = LazyMap.<Edge, Paint> decorate(new HashMap<Edge, Paint>(),
			new ConstantTransformer(Color.blue));

	private float edgeWeight = 0;
	public final Color[] similarColors = { new Color(216, 134, 134), new Color(135, 137, 211), new Color(134, 206, 189),
			new Color(206, 176, 134), new Color(194, 204, 134), new Color(145, 214, 134), new Color(133, 178, 209),
			new Color(103, 148, 255), new Color(60, 220, 220), new Color(30, 250, 100) };

	Map<Node, Paint> vertexPaints = LazyMap.<Node, Paint> decorate(new HashMap<Node, Paint>(), new ConstantTransformer(
			Color.white));

	/**
	 * @param layout
	 */
	public GraphViewer(Layout<Node, Edge> layout) {
		super(layout);
		initialize();
	}

	public void clusterAndRecolor(AggregateLayout<Node, Edge> layout, int numEdgesToRemove, boolean groupClusters) {
		Color[] colors = similarColors;
		// Now cluster the vertices by removing the top 50 edges with highest
		// betweenness
		// if (numEdgesToRemove == 0) {
		// colorCluster( g.getVertices(), colors[0] );
		// } else {

		Graph<Node, Edge> g = layout.getGraph();
		layout.removeAll();

		EdgeBetweennessClusterer<Node, Edge> clusterer = new EdgeBetweennessClusterer<Node, Edge>(numEdgesToRemove);
		Set<Set<Node>> clusterSet = clusterer.transform(g);
		List<Edge> edges = clusterer.getEdgesRemoved();

		int i = 0;
		// Set the colors of each node so that each cluster's vertices have the
		// same color
		for (Set<Node> vertices : clusterSet) {

			Color c = colors[i % colors.length];

			colorCluster(vertices, c);
			if (groupClusters == true) {
				groupCluster(layout, vertices);
			}
			i++;
		}
		for (Edge e : g.getEdges()) {

			if (edges.contains(e)) {
				edgePaints.put(e, Color.lightGray);
			} else {
				edgePaints.put(e, Color.black);
			}
		}

	}

	private void colorCluster(Set<Node> vertices, Color c) {
		for (Node v : vertices) {
			vertexPaints.put(v, c);
		}
	}

	private void groupCluster(AggregateLayout<Node, Edge> layout, Set<Node> vertices) {
		if (vertices.size() < layout.getGraph().getVertexCount()) {
			System.out.println("group");
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<Node, Edge> subGraph = SparseMultigraph.<Node, Edge> getFactory().create();
			for (Node v : vertices) {
				subGraph.addVertex(v);
			}
			Layout<Node, Edge> subLayout = new CircleLayout<Node, Edge>(subGraph);
			subLayout.setInitializer(getGraphLayout());
			subLayout.setSize(new Dimension(40, 40));

			layout.put(subLayout, center);
		}
	}

	private void initialize() {
		// background color
		setBackground(Color.WHITE);
		// node size
		getRenderContext().setVertexShapeTransformer(new ConstantTransformer(new Ellipse2D.Float(-6, -6, 12, 12)));
		getRenderContext().setVertexShapeTransformer(new Transformer<Node, Shape>() {
			@Override
			public Shape transform(Node node) {
				// TODO check node type
				if (node instanceof Note) {
					return new Ellipse2D.Float(-6, -6, 12, 12);
				} else if (node instanceof Motif) {
					return new Rectangle2D.Float(-6, -6, 12, 12);
				} else {
					return null;
				}
			}
		});
		// node color
		getRenderContext().setVertexFillPaintTransformer(new Transformer<Node, Paint>() {
			@Override
			public Paint transform(Node note) {
				int degree = getGraphLayout().getGraph().degree(note);
				if (degree < 30) {
					degree *= 8;
				}
				if (degree > 255) {
					degree = 255;
				}
				return new Color(255, 255 - degree, 255 - degree);
			}
		});
		// getRenderContext().setVertexFillPaintTransformer(MapTransformer.<Note,
		// Paint> getInstance(vertexPaints));
		getRenderContext().setVertexDrawPaintTransformer(new Transformer<Node, Paint>() {
			@Override
			public Paint transform(Node v) {
				if (getPickedVertexState().isPicked(v)) {
					return Color.cyan;
				} else {
					return Color.BLACK;
				}
			}
		});
		// node label
		// getRenderContext().setVertexLabelTransformer(new Transformer<Node,
		// String>() {
		// @Override
		// public String transform(Node node) {
		// return node.getName();
		// }
		// });
		// edge shape
		getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Edge>());
		// edge color
		getRenderContext().setEdgeFillPaintTransformer(new Transformer<Edge, Paint>() {

			@Override
			public Paint transform(Edge edge) {
				if (edge.getWeight() < edgeWeight) {
					return new Color(1, 1, 1, 1);
				}
				int color = (int) (255 - (edge.getWeight() * 3 + 1));
				if (color < 0) {
					color = 0;
				}
				return new Color(color, color, color);

			}
		});
		// getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.<MyEdge,
		// Paint> getInstance(edgePaints));
		// edge stroke
		// getRenderContext().setEdgeStrokeTransformer(new Transformer<Edge,
		// Stroke>() {
		// @Override
		// public Stroke transform(Edge e) {
		// return new BasicStroke(e.getWeight());
		// }
		// });
		// edge label
		// getRenderContext().setEdgeLabelTransformer(new Transformer<Edge,
		// String>() {
		//
		// @Override
		// public String transform(Edge e) {
		// return String.valueOf(e.getWeight());
		// }
		// });
		// arrow color
		getRenderContext().setArrowFillPaintTransformer(new Transformer<Edge, Paint>() {
			@Override
			public Color transform(Edge edge) {
				if (edge.getWeight() < edgeWeight) {
					return new Color(1, 1, 1, 1);
				} else {
					return Color.GRAY;
				}

			}
		});
		// arrow line
		getRenderContext().setArrowDrawPaintTransformer(new Transformer<Edge, Paint>() {
			@Override
			public Color transform(Edge edge) {
				if (edge.getWeight() < edgeWeight) {
					return new Color(1, 1, 1, 1);
				} else {
					return Color.GRAY;
				}
			}
		});
		// mouse control
		setGraphMouse(new DefaultModalGraphMouse<Integer, Number>());
	}
}