/**
 * 
 */
package musicbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import musicbox.MainProgram;

/**
 * @author LIU Xiaofan
 * 
 */
public class ComposePanel extends JPanel {
	private static Integer[] lengthPerComp = { 6800, 50, 100, 200, 400 };
	private static String[] major = { "Major", "Minor" };
	private static String[] scales = { "C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B" };
	/**
	 * 
	 */
	private static final long serialVersionUID = -5564868987480176153L;
	private static String[] weight = { "By MyEdge Weight", "By Node Degree", "By Node Weight", "Purely Random" };
	private JPanel blankPanel = null;
	private JButton button;
	private JPanel buttonPanel;
	private JButton composeButton;
	private JTabbedPane composeTabbedPane = null;
	private JPanel formNetworkPanel;
	private JLabel label_1;
	private JLabel label_2;
	private JComboBox lengthPerCompComboBox = null;
	private JLabel lengthPerCompLabel = null;
	private JComboBox majorComboBox = null;
	private JLabel majorLabel = null;
	private JButton motifComposeButton = null;
	private ButtonGroup motifComposeButtonGroup = null;
	private JPanel motifPanel = null;
	private JLabel numberLabel = null;
	private JTextField numberTextField = null;
	private JButton playButton = null;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JRadioButton radioButton_2;
	private JRadioButton radioButton_3;
	private JRadioButton radioButton_4;
	private JRadioButton radioButton_5;
	private JButton rdmWlkComposeButton = null;
	private JPanel rdmwlkPanel = null;
	private JButton saveButton = null;
	private JComboBox scaleComboBox = null;
	private JLabel scaleLabel = null;
	private JButton stopButton = null;
	private JComboBox weightComboBox = null;
	private JLabel weightLabel = null;
	private JRadioButton woRdmWlkRadioButton = null;
	private JRadioButton wRdmWlkRadioButton = null;

	/**
	 * 
	 */
	public ComposePanel() {
		initialize();
	}

	/**
	 * This method initializes blankPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBlankPanel() {
		if (blankPanel == null) {
			blankPanel = new JPanel();
			blankPanel.setLayout(null);
			blankPanel.setPreferredSize(new Dimension(200, 300));
		}
		return blankPanel;
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton();
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO
				}
			});
			button.setEnabled(false);
			button.setText("Form Network");
		}
		return button;
	}

	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			buttonPanel.add(getComposeButton());
			buttonPanel.add(getPlayButton());
			buttonPanel.add(getSaveButton());
			buttonPanel.add(getStopButton());
		}
		return buttonPanel;
	}

	private JButton getComposeButton() {
		if (composeButton == null) {
			composeButton = new JButton("Compose");
			composeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MainProgram.getInstance().getLogic().compose(0, 0, 0, 0, 0);
				}
			});
		}
		return composeButton;
	}

	/**
	 * This method initializes composeTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getComposeTabbedPane() {
		if (composeTabbedPane == null) {
			composeTabbedPane = new JTabbedPane();
			composeTabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			composeTabbedPane.setTabPlacement(SwingConstants.BOTTOM);
			composeTabbedPane.addTab("Motif", null, getMotifPanel(), null);
			composeTabbedPane.addTab("Random Walk", null, getRdmwlkPanel(), null);
		}
		return composeTabbedPane;
	}

	private JPanel getFormNetworkPanel() {
		if (formNetworkPanel == null) {
			formNetworkPanel = new JPanel();
			formNetworkPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			formNetworkPanel.setLayout(new BoxLayout(formNetworkPanel, BoxLayout.Y_AXIS));
			formNetworkPanel.add(getLabel_1());
			formNetworkPanel.add(getRadioButton());
			formNetworkPanel.add(getRadioButton_1());
			formNetworkPanel.add(getLabel_2());
			formNetworkPanel.add(getRadioButton_2());
			formNetworkPanel.add(getRadioButton_3());
			formNetworkPanel.add(getRadioButton_4());
			formNetworkPanel.add(getRadioButton_5());
			formNetworkPanel.add(getButton());
		}
		return formNetworkPanel;
	}

	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Edge Type:");
		}
		return label_1;
	}

	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("Node Type:");
		}
		return label_2;
	}

	/**
	 * This method initializes lengthPerCompComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getLengthPerCompComboBox() {
		if (lengthPerCompComboBox == null) {
			lengthPerCompComboBox = new JComboBox();
			lengthPerCompComboBox.setModel(new DefaultComboBoxModel(lengthPerComp));
		}
		return lengthPerCompComboBox;
	}

	/**
	 * This method initializes majorComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getMajorComboBox() {
		if (majorComboBox == null) {
			majorComboBox = new JComboBox();
			majorComboBox.setModel(new DefaultComboBoxModel(major));
			majorComboBox.setSelectedIndex(1);
		}
		return majorComboBox;
	}

	/**
	 * This method initializes motifComposeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMotifComposeButton() {
		if (motifComposeButton == null) {
			motifComposeButton = new JButton();
			motifComposeButton.setText("Compose");
			motifComposeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic()
							.composeWithMotif(getWRdmWlkRadioButton().isSelected(), getWoRdmWlkRadioButton().isSelected());
				}
			});
		}
		return motifComposeButton;
	}

	/**
	 * This method initializes motifPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMotifPanel() {
		if (motifPanel == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints21.gridy = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints20.gridy = 0;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints10.gridy = 2;
			motifPanel = new JPanel();
			motifPanel.setLayout(new GridBagLayout());
			motifPanel.add(getMotifComposeButton(), gridBagConstraints10);
			motifPanel.add(getWRdmWlkRadioButton(), gridBagConstraints20);
			motifPanel.add(getWoRdmWlkRadioButton(), gridBagConstraints21);
			motifComposeButtonGroup = new ButtonGroup();
			motifComposeButtonGroup.add(getWRdmWlkRadioButton());
			motifComposeButtonGroup.add(getWoRdmWlkRadioButton());
		}
		return motifPanel;
	}

	/**
	 * This method initializes numberTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNumberTextField() {
		if (numberTextField == null) {
			numberTextField = new JTextField();
			numberTextField.setText("1");
		}
		return numberTextField;
	}

	/**
	 * This method initializes playButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPlayButton() {
		if (playButton == null) {
			playButton = new JButton();
			playButton.setText("PLAY");
			playButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic().tempMIDIPlay();
				}
			});
		}
		return playButton;
	}

	private JRadioButton getRadioButton() {
		if (radioButton == null) {
			radioButton = new JRadioButton();
			radioButton.setText("Co-Occurrent");
			radioButton.setSelected(true);
		}
		return radioButton;
	}

	private JRadioButton getRadioButton_1() {
		if (radioButton_1 == null) {
			radioButton_1 = new JRadioButton();
			radioButton_1.setText("Start-End");
			radioButton_1.setEnabled(false);
		}
		return radioButton_1;
	}

	private JRadioButton getRadioButton_2() {
		if (radioButton_2 == null) {
			radioButton_2 = new JRadioButton();
			radioButton_2.setText("Normal");
		}
		return radioButton_2;
	}

	private JRadioButton getRadioButton_3() {
		if (radioButton_3 == null) {
			radioButton_3 = new JRadioButton();
			radioButton_3.setText("Rhythmic");
		}
		return radioButton_3;
	}

	private JRadioButton getRadioButton_4() {
		if (radioButton_4 == null) {
			radioButton_4 = new JRadioButton();
			radioButton_4.setText("Tonal");
		}
		return radioButton_4;
	}

	private JRadioButton getRadioButton_5() {
		if (radioButton_5 == null) {
			radioButton_5 = new JRadioButton("Motif/Note");
			radioButton_5.setSelected(true);
		}
		return radioButton_5;
	}

	/**
	 * This method initializes rdmWlkComposeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRdmWlkComposeButton() {
		if (rdmWlkComposeButton == null) {
			rdmWlkComposeButton = new JButton();
			rdmWlkComposeButton.setText("COMPOSE");
			rdmWlkComposeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram
							.getInstance()
							.getLogic()
							.compose(getWeightComboBox().getSelectedIndex(), getScaleComboBox().getSelectedIndex(),
									getMajorComboBox().getSelectedIndex(), getLengthPerCompComboBox().getSelectedIndex(),
									Integer.parseInt(getNumberTextField().getText()));
				}
			});
		}
		return rdmWlkComposeButton;
	}

	/**
	 * This method initializes rdmwlkPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRdmwlkPanel() {
		if (rdmwlkPanel == null) {
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.insets = new Insets(0, 27, 0, 90);
			gridBagConstraints33.gridy = 11;
			gridBagConstraints33.gridx = 0;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.fill = GridBagConstraints.BOTH;
			gridBagConstraints32.weighty = 1.0D;
			gridBagConstraints32.gridy = 10;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints31.gridy = 9;
			gridBagConstraints31.ipadx = 199;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.gridx = 0;
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.insets = new Insets(0, 27, 0, 82);
			gridBagConstraints30.gridy = 8;
			gridBagConstraints30.gridx = 0;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints29.gridy = 7;
			gridBagConstraints29.ipadx = 179;
			gridBagConstraints29.weightx = 1.0;
			gridBagConstraints29.gridx = 0;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.insets = new Insets(0, 27, 0, 93);
			gridBagConstraints28.gridy = 6;
			gridBagConstraints28.gridx = 0;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints27.gridy = 5;
			gridBagConstraints27.ipadx = 179;
			gridBagConstraints27.weightx = 1.0;
			gridBagConstraints27.gridx = 0;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.insets = new Insets(0, 27, 0, 101);
			gridBagConstraints26.gridy = 4;
			gridBagConstraints26.gridx = 0;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints25.gridy = 3;
			gridBagConstraints25.ipadx = 179;
			gridBagConstraints25.weightx = 1.0;
			gridBagConstraints25.gridx = 0;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new Insets(0, 27, 0, 151);
			gridBagConstraints24.gridy = 2;
			gridBagConstraints24.gridx = 0;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints23.gridy = 1;
			gridBagConstraints23.ipadx = 179;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.gridx = 0;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.insets = new Insets(0, 27, 0, 124);
			gridBagConstraints22.gridy = 0;
			gridBagConstraints22.gridx = 0;
			weightLabel = new JLabel();
			weightLabel.setText("Choose by");
			scaleLabel = new JLabel();
			scaleLabel.setText("Scale");
			majorLabel = new JLabel();
			majorLabel.setText("Major or Minor");
			lengthPerCompLabel = new JLabel();
			lengthPerCompLabel.setText("Notes per Piece");
			numberLabel = new JLabel();
			numberLabel.setText("Number of Pieces");
			rdmwlkPanel = new JPanel();
			rdmwlkPanel.setLayout(new GridBagLayout());
			rdmwlkPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rdmwlkPanel.setPreferredSize(new Dimension(220, 250));
			rdmwlkPanel.add(weightLabel, gridBagConstraints22);
			rdmwlkPanel.add(getWeightComboBox(), gridBagConstraints23);
			rdmwlkPanel.add(scaleLabel, gridBagConstraints24);
			rdmwlkPanel.add(getScaleComboBox(), gridBagConstraints25);
			rdmwlkPanel.add(majorLabel, gridBagConstraints26);
			rdmwlkPanel.add(getMajorComboBox(), gridBagConstraints27);
			rdmwlkPanel.add(lengthPerCompLabel, gridBagConstraints28);
			rdmwlkPanel.add(getLengthPerCompComboBox(), gridBagConstraints29);
			rdmwlkPanel.add(numberLabel, gridBagConstraints30);
			rdmwlkPanel.add(getNumberTextField(), gridBagConstraints31);
			rdmwlkPanel.add(getBlankPanel(), gridBagConstraints32);
			rdmwlkPanel.add(getRdmWlkComposeButton(), gridBagConstraints33);
		}
		return rdmwlkPanel;
	}

	/**
	 * This method initializes saveButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setText("SAVE");
			saveButton.setEnabled(false);
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic().tempMIDISave();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes scaleComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getScaleComboBox() {
		if (scaleComboBox == null) {
			scaleComboBox = new JComboBox();
			scaleComboBox.setModel(new DefaultComboBoxModel(scales));
			scaleComboBox.setSelectedIndex(11);
		}
		return scaleComboBox;
	}

	/**
	 * This method initializes stopButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStopButton() {
		if (stopButton == null) {
			stopButton = new JButton();
			stopButton.setText("STOP");
			stopButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic().tempMIDIStop();
				}
			});
		}
		return stopButton;
	}

	/**
	 * This method initializes weightComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getWeightComboBox() {
		if (weightComboBox == null) {
			weightComboBox = new JComboBox();
			weightComboBox.setModel(new DefaultComboBoxModel(weight));
		}
		return weightComboBox;
	}

	/**
	 * This method initializes woRdmWlkRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getWoRdmWlkRadioButton() {
		if (woRdmWlkRadioButton == null) {
			woRdmWlkRadioButton = new JRadioButton();
			woRdmWlkRadioButton.setText("W/O Random Walk");
			woRdmWlkRadioButton.setSelected(true);
		}
		return woRdmWlkRadioButton;
	}

	/**
	 * This method initializes wRdmWlkRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getWRdmWlkRadioButton() {
		if (wRdmWlkRadioButton == null) {
			wRdmWlkRadioButton = new JRadioButton();
			wRdmWlkRadioButton.setText("With Random Walk");
		}
		return wRdmWlkRadioButton;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridx = 0;
		gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints16.gridwidth = 3;
		gridBagConstraints16.weighty = 1.0D;
		gridBagConstraints16.weightx = 3.0D;
		gridBagConstraints16.gridy = 0;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints15.gridy = 0;
		gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints15.gridheight = 4;
		gridBagConstraints15.gridwidth = 4;
		gridBagConstraints15.weighty = 4.0D;
		gridBagConstraints15.weightx = 4.0D;
		gridBagConstraints15.gridx = 0;
		setBorder(BorderFactory.createTitledBorder(null, "Compose Music", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
		setLayout(new BorderLayout(0, 0));
		add(getFormNetworkPanel(), BorderLayout.EAST);
		add(getComposeTabbedPane());
		add(getButtonPanel(), BorderLayout.SOUTH);
	}
}
