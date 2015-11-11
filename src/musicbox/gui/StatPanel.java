/**
 * 
 */
package musicbox.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import musicbox.MainProgram;
import musicbox.logic.stat.BasicParameter;

/**
 * @author LIU Xiaofan
 * 
 */
public class StatPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -900990322558213509L;
	private JTextField txtMeanDegree;
	private JTextField txtMeanShortestDistance;
	private JTextField txtNumberOfEdges;
	private JTextField txtNumberOfNodes;

	public StatPanel(final ViewerPanel vp) {
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Statistics: ", TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.BLACK));
		setLayout(new GridLayout(3, 4, 0, 0));

		JLabel lblNumberOfNodes = new JLabel("Number of Nodes: ");
		add(lblNumberOfNodes);

		txtNumberOfNodes = new JTextField();
		txtNumberOfNodes.setText("Number of Nodes");
		txtNumberOfNodes.setColumns(10);
		add(txtNumberOfNodes);

		JLabel lblNumberOfEdges = new JLabel("Number of Edges: ");
		add(lblNumberOfEdges);

		txtNumberOfEdges = new JTextField();
		txtNumberOfEdges.setText("Number of Edges");
		add(txtNumberOfEdges);
		txtNumberOfEdges.setColumns(10);

		JLabel lblMeanDegree = new JLabel("Mean Degree: ");
		add(lblMeanDegree);

		txtMeanDegree = new JTextField();
		txtMeanDegree.setText("Mean Degree");
		add(txtMeanDegree);
		txtMeanDegree.setColumns(10);

		JLabel lblMeanShortestDistance = new JLabel("Mean Shortest Distance: ");
		add(lblMeanShortestDistance);

		txtMeanShortestDistance = new JTextField();
		txtMeanShortestDistance.setText("Mean Shortest Distance");
		txtMeanShortestDistance.setColumns(10);
		add(txtMeanShortestDistance);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		add(horizontalStrut_1);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		add(horizontalStrut_2);

		JButton btnFullStat = new JButton("Full Stat.");
		btnFullStat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainProgram.getInstance().getLogic().getStatistics(vp.getNetwork(), vp.getTitle());
			}
		});
		add(btnFullStat);
	}

	public void setParameters(BasicParameter bp) {
		txtNumberOfNodes.setText(String.valueOf(bp.getNon()));
		txtNumberOfEdges.setText(String.valueOf(bp.getNoe()));
		txtMeanDegree.setText(String.valueOf(bp.getMeanDegree()));
		txtMeanShortestDistance.setText(String.valueOf(bp.getMeanDistance()));
	}

}
