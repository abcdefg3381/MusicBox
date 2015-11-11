/**
 * 
 */
package musicbox.gui.viz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import maggie.network.entity.Node;
import musicbox.db.entity.CoEdge;
import musicbox.db.entity.Note;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

/**
 * @author LIU Xiaofan
 * 
 */
@SuppressWarnings("unchecked")
public class TreeViewer extends VisualizationViewer<Node, CoEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2788693014412517860L;
	Map<CoEdge, Paint> edgePaints = LazyMap.<CoEdge, Paint> decorate(new HashMap<CoEdge, Paint>(), new ConstantTransformer(
			Color.blue));
	private float edgeWeight = 0;
	Map<Node, Paint> vertexPaints = LazyMap.<Node, Paint> decorate(new HashMap<Node, Paint>(), new ConstantTransformer(
			Color.white));

	/**
	 * @param model
	 */
	public TreeViewer(VisualizationModel model) {
		super(model);
		initialize();
	}

	private void initialize() {
		// background color
		setBackground(Color.WHITE);
		// node size
		getRenderContext().setVertexShapeTransformer(new ConstantTransformer(new Ellipse2D.Float(-6, -6, 12, 12)));
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
		getRenderContext().setVertexFillPaintTransformer(MapTransformer.<Node, Paint> getInstance(vertexPaints));
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
		getRenderContext().setVertexLabelTransformer(new Transformer<Node, String>() {
			@Override
			public String transform(Node node) {
				if (node instanceof Note) {
					Note note = (Note) node;
					return note.getPitch() + " " + note.getDuration();
				} else {
					return "";
				}
			}
		});
		// edge shape
		getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, CoEdge>());
		// edge color
		getRenderContext().setEdgeFillPaintTransformer(new Transformer<CoEdge, Paint>() {

			@Override
			public Paint transform(CoEdge edge) {
				if (edge.getWeight() < edgeWeight) {
					return new Color(1, 1, 1, 1);
				}
				int color = (int) (255 - (edge.getWeight() + 1) * 50);
				if (color < 0) {
					color = 0;
				}
				return new Color(color, color, color);

			}
		});
		getRenderContext().setEdgeDrawPaintTransformer(MapTransformer.<CoEdge, Paint> getInstance(edgePaints));

		getRenderContext().setEdgeStrokeTransformer(new Transformer<CoEdge, Stroke>() {
			protected final Stroke THICK = new BasicStroke(2);
			protected final Stroke THIN = new BasicStroke(1);

			@Override
			public Stroke transform(CoEdge e) {
				Paint c = edgePaints.get(e);
				if (c == Color.LIGHT_GRAY) {
					return THIN;
				} else {
					return THICK;
				}
			}
		});
		// arrow color
		getRenderContext().setArrowFillPaintTransformer(new Transformer<CoEdge, Paint>() {
			@Override
			public Color transform(CoEdge edge) {
				if (edge.getWeight() < edgeWeight) {
					return new Color(1, 1, 1, 1);
				} else {
					return Color.GRAY;
				}

			}
		});
		getRenderContext().setArrowDrawPaintTransformer(new Transformer<CoEdge, Paint>() {
			@Override
			public Color transform(CoEdge edge) {
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
