/**
 * 
 */
package musicbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import musicbox.MainProgram;
import musicbox.logic.form.EdgeFormer;
import musicbox.logic.form.NodeFormer;

/**
 * @author LIU Xiaofan
 * 
 */
public class NetworkPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1871289089981519340L;
	private CompositionList compositionList = null;
	private int edgeType = 1;
	private ButtonGroup edgeTypeButtonGroup = null;
	private JRadioButton edgeTypeCoOccur = null;
	private JRadioButton edgeTypeStardEnd = null;
	private JButton formNetworkButton = null;
	private JLabel lblEdgetype;
	private JLabel lblNodeType;
	private JLabel lblSkyline;
	private JPanel listPanel = null;
	private JButton listRefreshButton = null;
	private JScrollPane listScrollPane = null;
	private JPanel mfinderPanel;
	private JButton motifButton = null;
	private JTextField motifTextField = null;
	private JPanel networkTypePanel = null;
	private int nodeType = 4;
	private ButtonGroup nodeTypeButtonGroup;
	private JRadioButton nodeTypeMotif;
	private JRadioButton nodeTypeNormal = null;
	private JRadioButton nodeTypeRhythm = null;
	private JRadioButton nodeTypeTonal = null;
	private JCheckBox skylineCheckBox = null;

	/**
 * 
 */
	public NetworkPanel() {
		initialize();
	}

	/**
	 * This method initializes compositionList
	 * 
	 * @return musicbox.gui.CompositionList
	 */
	private CompositionList getCompositionList() {
		if (compositionList == null) {
			compositionList = new CompositionList(this);
		}
		return compositionList;
	}

	/**
	 * This method initializes edgeTypeRadioButton1
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getEdgeTypeCoOccur() {
		if (edgeTypeCoOccur == null) {
			edgeTypeCoOccur = new JRadioButton();
			edgeTypeCoOccur.setSelected(true);
			edgeTypeCoOccur.setText("Co-Occurrent");
			edgeTypeCoOccur.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					edgeType = EdgeFormer.CO;
				}
			});
		}
		return edgeTypeCoOccur;
	}

	/**
	 * This method initializes edgeTypeRadioButton2
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getEdgeTypeStardEnd() {
		if (edgeTypeStardEnd == null) {
			edgeTypeStardEnd = new JRadioButton();
			edgeTypeStardEnd.setEnabled(false);
			edgeTypeStardEnd.setText("Start-End");
			edgeTypeStardEnd.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					edgeType = EdgeFormer.SE;
				}
			});
		}
		return edgeTypeStardEnd;
	}

	/**
	 * This method initializes formNetworkButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFormNetworkButton() {
		if (formNetworkButton == null) {
			formNetworkButton = new JButton();
			formNetworkButton.setText("Form Network");
			formNetworkButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					if (MainProgram
							.getInstance()
							.getLogic()
							.fetchNetworkOrigin(getCompositionList().getSelectedValues(), edgeType, nodeType,
									getSkylineCheckBox().isSelected())) {
						getMotifTextField().setEnabled(true);
						getMotifButton().setEnabled(true);
					}
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
		}
		return formNetworkButton;
	}

	private JLabel getLblEdgetype() {
		if (lblEdgetype == null) {
			lblEdgetype = new JLabel("Edge Type:");
		}
		return lblEdgetype;
	}

	private JLabel getLblNodeType() {
		if (lblNodeType == null) {
			lblNodeType = new JLabel("Node Type:");
		}
		return lblNodeType;
	}

	private JLabel getLblSkyline() {
		if (lblSkyline == null) {
			lblSkyline = new JLabel("Skyline:");
		}
		return lblSkyline;
	}

	/*
	 * This method initializes listPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getListPanel() {
		if (listPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.ipadx = -16;
			gridBagConstraints.ipady = 52;
			gridBagConstraints.weightx = 4.0D;
			gridBagConstraints.weighty = 4.0D;
			gridBagConstraints.gridheight = 1;
			listPanel = new JPanel();
			listPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
			listPanel.add(getListScrollPane());
		}
		return listPanel;
	}

	/**
	 * This method initializes refreshNetworksButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getListRefreshButton() {
		if (listRefreshButton == null) {
			listRefreshButton = new JButton();
			listRefreshButton.setText("Refresh List");
			listRefreshButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getCompositionList().setListData(MainProgram.getInstance().getLogic().getNetworkList());
				}
			});
		}
		return listRefreshButton;
	}

	/**
	 * This method initializes listScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getListScrollPane() {
		if (listScrollPane == null) {
			listScrollPane = new JScrollPane();
			listScrollPane.setViewportView(getCompositionList());
		}
		return listScrollPane;
	}

	private JPanel getMfinderPanel() {
		if (mfinderPanel == null) {
			mfinderPanel = new JPanel();
			mfinderPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			mfinderPanel.setLayout(new BoxLayout(mfinderPanel, BoxLayout.X_AXIS));
			mfinderPanel.add(getMotifTextField());
			mfinderPanel.add(getMotifButton());
		}
		return mfinderPanel;
	}

	/**
	 * This method initializes motifButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMotifButton() {
		if (motifButton == null) {
			motifButton = new JButton();
			motifButton.setEnabled(false);
			motifButton.setText("Find Motif");
			motifButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					MainProgram.getInstance().getLogic().findMotifs(getMotifTextField().getText());
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
		}
		return motifButton;
	}

	/**
	 * This method initializes motifTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getMotifTextField() {
		if (motifTextField == null) {
			motifTextField = new JTextField();
			motifTextField.setText("enter parameters");
			motifTextField.setEnabled(false);
			motifTextField.setToolTipText("enter parameters for mfinder (leave blank to use default)");
		}
		return motifTextField;
	}

	/**
	 * This method initializes networkTypePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getNetworkTypePanel() {
		if (networkTypePanel == null) {
			networkTypePanel = new JPanel();
			networkTypePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			networkTypePanel.setLayout(new BoxLayout(networkTypePanel, BoxLayout.Y_AXIS));
			networkTypePanel.add(getListRefreshButton());
			networkTypePanel.add(getLblSkyline());
			networkTypePanel.add(getSkylineCheckBox());
			networkTypePanel.add(getLblEdgetype());
			networkTypePanel.add(getEdgeTypeCoOccur());
			networkTypePanel.add(getEdgeTypeStardEnd());
			networkTypePanel.add(getLblNodeType());
			networkTypePanel.add(getNodeTypeNormal());
			networkTypePanel.add(getNodeTypeRhythm());
			networkTypePanel.add(getNodeTypeTonal());
			networkTypePanel.add(getNodeTypeMotif());
			edgeTypeButtonGroup = new ButtonGroup();
			edgeTypeButtonGroup.add(getEdgeTypeCoOccur());
			edgeTypeButtonGroup.add(getEdgeTypeStardEnd());
			nodeTypeButtonGroup = new ButtonGroup();
			nodeTypeButtonGroup.add(getNodeTypeNormal());
			nodeTypeButtonGroup.add(getNodeTypeRhythm());
			nodeTypeButtonGroup.add(getNodeTypeTonal());
			nodeTypeButtonGroup.add(getNodeTypeMotif());
			networkTypePanel.add(getFormNetworkButton());

		}
		return networkTypePanel;
	}

	private JRadioButton getNodeTypeMotif() {
		if (nodeTypeMotif == null) {
			nodeTypeMotif = new JRadioButton("Motif/Note");
			nodeTypeMotif.setSelected(true);
			nodeTypeMotif.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					nodeType = NodeFormer.MOTIF_NOTE;
				}
			});
		}
		return nodeTypeMotif;
	}

	/**
	 * This method initializes formNormalRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNodeTypeNormal() {
		if (nodeTypeNormal == null) {
			nodeTypeNormal = new JRadioButton();
			nodeTypeNormal.setText("Normal");
			nodeTypeNormal.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nodeType = NodeFormer.NORMAL;
				}
			});
		}
		return nodeTypeNormal;
	}

	/**
	 * This method initializes formRhythmRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNodeTypeRhythm() {
		if (nodeTypeRhythm == null) {
			nodeTypeRhythm = new JRadioButton();
			nodeTypeRhythm.setText("Rhythmic");
			nodeTypeRhythm.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nodeType = NodeFormer.RHYTHMIC;
				}
			});
		}
		return nodeTypeRhythm;
	}

	/**
	 * This method initializes formTonalRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getNodeTypeTonal() {
		if (nodeTypeTonal == null) {
			nodeTypeTonal = new JRadioButton();
			nodeTypeTonal.setText("Tonal");
			nodeTypeTonal.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					nodeType = NodeFormer.TONAL;
				}
			});
		}
		return nodeTypeTonal;
	}

	/**
	 * @return
	 */
	public Object[] getParameters() {
		return new Object[] { edgeType, nodeType, getSkylineCheckBox().isSelected() };
	}

	/**
	 * This method initializes skylineCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getSkylineCheckBox() {
		if (skylineCheckBox == null) {
			skylineCheckBox = new JCheckBox();
			skylineCheckBox.setSelected(true);
			skylineCheckBox.setText("Skyline pre-process");
		}
		return skylineCheckBox;
	}

	/**
	 * 
	 */
	private void initialize() {
		setBorder(BorderFactory.createTitledBorder(null, "Form Networks", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		setLayout(new BorderLayout(0, 0));
		add(getMfinderPanel(), BorderLayout.SOUTH);
		add(getNetworkTypePanel(), BorderLayout.EAST);
		add(getListPanel());
	}

	public void setComposition(Object[] compositions) {
		getCompositionList().setListData(compositions);
	}
}
