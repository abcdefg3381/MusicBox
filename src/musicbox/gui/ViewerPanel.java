/**
 * 
 */
package musicbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import maggie.network.entity.Edge;
import maggie.network.entity.Node;
import maggie.network.gui.GuiUtils;
import musicbox.MainProgram;
import musicbox.db.entity.MusicalNetwork;
import musicbox.gui.viz.GraphViewer;
import musicbox.gui.viz.TreeViewer;
import musicbox.logic.stat.MyStatistics;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * @author LIU Xiaofan
 * 
 */
public class ViewerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -518929055944253663L;
	private JButton addButton = null;
	private JPanel buttonPanel = null;
	private JButton clusterButton = null;
	private JLabel edgeTypeLabel = null;
	private Graph<Node, Edge> g;
	private GraphViewer graphViewer;
	private Layout<Node, Edge> layout;
	private MusicalNetwork network;
	private JButton showGraphButton = null;
	private JButton showTreeButton = null;
	private JButton snapshotButton;
	private StatPanel statPanel;
	private JButton subButton = null;
	private String title;
	private TreeViewer treeViewer;
	private VisualizationViewer<?, ?> vv = null;
	private JLabel weightLabel1 = null;
	private JTextField weightTextField = null;
	private float weightThreshold;

	/**
	 * @param title
	 * 
	 */
	public ViewerPanel(String title) {
		this.title = title.split(" ")[0];
		initialize();
	}

	/**
	 * This method initializes addButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("+");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// TODO add weight by 1
					// edgeWeight++;
					// weightTextField.setText(" " + edgeWeight + " ");
					// clusterAndRecolor(layout, edgeWeight, similarColors,
					// false);
					// vv.validate();
					// vv.repaint();
				}
			});
		}
		return addButton;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			weightLabel1 = new JLabel();
			weightLabel1.setText("Weight:");
			edgeTypeLabel = new JLabel();
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getShowGraphButton(), null);
			buttonPanel.add(getVizTreeButton(), null);
			buttonPanel.add(getClusterButton(), null);
			buttonPanel.add(edgeTypeLabel, null);
			buttonPanel.add(getSnapshotButton());
			buttonPanel.add(weightLabel1, null);
			buttonPanel.add(getAddButton(), null);
			buttonPanel.add(getWeightTextField(), null);
			buttonPanel.add(getSubButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes clusterButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getClusterButton() {
		if (clusterButton == null) {
			clusterButton = new JButton();
			clusterButton.setText("Cluster");
			clusterButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					vizCluster();
				}

			});
		}
		return clusterButton;
	}

	synchronized MusicalNetwork getNetwork() {
		return network;
	}

	/**
	 * This method initializes showGraphButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getShowGraphButton() {
		if (showGraphButton == null) {
			showGraphButton = new JButton();
			showGraphButton.setText("Show Graph");
			showGraphButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					vizGraph();
				}
			});
		}
		return showGraphButton;
	}

	private JButton getSnapshotButton() {
		if (snapshotButton == null) {
			snapshotButton = new JButton("Snap Shot");
			snapshotButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String name = JOptionPane.showInputDialog("network name:");
					if (name == null || vv == null) {
						return;
					}
					if (vv instanceof GraphViewer) {
						name = name.concat(".graph");
					} else if (vv instanceof TreeViewer) {
						name = name.concat(".tree");
					}
					GuiUtils.drawComponentToFile(vv, new File("name"), "jpg");
				}
			});
		}
		return snapshotButton;
	}

	/**
	 * This method initializes statPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private StatPanel getStatPanel() {
		if (statPanel == null) {
			statPanel = new StatPanel(this);
		}
		return statPanel;
	}

	/**
	 * This method initializes subButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSubButton() {
		if (subButton == null) {
			subButton = new JButton();
			subButton.setText("-");
		}
		return subButton;
	}

	synchronized String getTitle() {
		return title;
	}

	/**
	 * This method initializes showTreeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getVizTreeButton() {
		if (showTreeButton == null) {
			showTreeButton = new JButton();
			showTreeButton.setText("Show Tree");
			showTreeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					vizTree();
				}
			});
		}
		return showTreeButton;
	}

	/**
	 * This method initializes weightTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getWeightTextField() {
		if (weightTextField == null) {
			weightTextField = new JTextField();
			weightTextField.setText(" " + 0 + " ");
			weightTextField.setHorizontalAlignment(SwingConstants.CENTER);
			weightTextField.setColumns(4);
		}
		return weightTextField;
	}

	/**
	 * @param title
	 * 
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		add(getButtonPanel(), BorderLayout.SOUTH);
		add(getStatPanel(), BorderLayout.NORTH);
		setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		setSize(1000, 1000);
	}

	/**
	 * Generates a graph: in this case, reads it from current network
	 * 
	 * @param currentNetwork
	 * 
	 * @return A sample undirected graph
	 */
	private Graph<Node, Edge> setGraph() {
		// new graph
		Graph<Node, Edge> g = new DirectedSparseGraph<Node, Edge>();
		for (Edge edge : this.network.getEdgeList()) {
			if (edge.getWeight() > weightThreshold) {
				g.addEdge(edge, edge.getPair(), EdgeType.DIRECTED);
			}
		}
		return g;
	}

	public void setNetwork(MusicalNetwork network) {
		this.network = network;
		this.getStatPanel().setParameters(MyStatistics.getBasicParameters(network));
		vizGraph();
	}

	private boolean vizCheckNetwork() {
		if (MainProgram.getInstance().getLogic().getNetworkOrigin() == null) {
			JOptionPane.showMessageDialog(null, "No Network Formed!", "Network Not Found", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean vizClearContent() {
		if (vv != null) {
			remove(vv);
		}
		validate();
		return true;
	}

	private void vizCluster() {
		if (vv instanceof GraphViewer) {
			graphViewer.clusterAndRecolor(new AggregateLayout<Node, Edge>(layout), 10, true);
		}
		revalidate();
		repaint();
	}

	private void vizGraph() {
		if (vizCheckNetwork() && vizClearContent()) {
			g = setGraph();
			layout = new FRLayout<Node, Edge>(g);
			graphViewer = new GraphViewer(layout);
			vv = graphViewer;
			add(vv, BorderLayout.CENTER);
			revalidate();
		}
	}

	@SuppressWarnings("unchecked")
	private void vizTree() {
		if (vizCheckNetwork() && vizClearContent()) {
			g = setGraph();
			MinimumSpanningForest2 prim =
					new MinimumSpanningForest2(g, new DelegateForest(), DelegateTree.getFactory(), new ConstantTransformer(1.0));
			Forest forest = prim.getForest();
			// create two layouts for the one graph, one layout for each model
			layout = new TreeLayout(forest);
			VisualizationModel vm1 = new DefaultVisualizationModel(layout);
			treeViewer = new TreeViewer(vm1);
			vv = treeViewer;
			add(vv, BorderLayout.CENTER);
			revalidate();
		}
	}
}
