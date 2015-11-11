package gdi.reader.gui;

import gdi.reader.MIDIReader;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class TrackInfoDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6949428828694674170L;

	private JLabel authorLabel = null;

	private JTextField authorTextField = null;

	private JLabel collectionNameLabel = null;

	private JTextField collectionNameTextField = null;

	private JLabel descLabel = null;

	private JComboBox genreComboBox = null;

	private JLabel genreLabel = null;

	private JPanel jContentPane = null;

	private JButton okButton = null;

	private Component parent;

	/**
	 * This method initializes
	 * 
	 */
	public TrackInfoDialog(JFrame parent) {
		super(parent, true);
		this.parent = parent;
		initialize();
	}

	/**
	 * This method initializes authorTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getAuthorTextField() {
		if (authorTextField == null) {
			authorTextField = new JTextField();
		}
		return authorTextField;
	}

	/**
	 * This method initializes collectionNameTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCollectionNameTextField() {
		if (collectionNameTextField == null) {
			collectionNameTextField = new JTextField();
		}
		return collectionNameTextField;
	}

	/**
	 * This method initializes genreComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getGenreComboBox() {
		if (genreComboBox == null) {
			Vector<String> genreList = new Vector<String>(3);
			genreList.add("Classic");
			genreList.add("Pop");
			genreList.add("Folk");
			genreComboBox = new JComboBox(genreList);
			genreComboBox.setActionCommand(null);
		}
		return genreComboBox;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.gridy = 4;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridwidth = 2;
			gridBagConstraints11.gridy = 0;
			descLabel = new JLabel();
			descLabel.setText("Please enter information for the collection:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 3;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.gridy = 2;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			genreLabel = new JLabel();
			genreLabel.setText("Genre: ");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			collectionNameLabel = new JLabel();
			collectionNameLabel.setText("Collection Name: ");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			authorLabel = new JLabel();
			authorLabel.setText("Author: ");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setPreferredSize(new Dimension(300, 200));
			jContentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			jContentPane.add(authorLabel, gridBagConstraints);
			jContentPane.add(collectionNameLabel, gridBagConstraints1);
			jContentPane.add(genreLabel, gridBagConstraints2);
			jContentPane.add(getAuthorTextField(), gridBagConstraints3);
			jContentPane.add(getCollectionNameTextField(), gridBagConstraints4);
			jContentPane.add(getGenreComboBox(), gridBagConstraints5);
			jContentPane.add(descLabel, gridBagConstraints11);
			jContentPane.add(getOkButton(), gridBagConstraints12);
		}
		return jContentPane;
	}

	/**
	 * This method initializes okButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MIDIReader
							.getInstance()
							.getLogic()
							.importFile(getAuthorTextField().getText(), getCollectionNameTextField().getText(),
									getGenreComboBox().getSelectedIndex());
					dispose();
				}
			});
		}
		return okButton;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(new Dimension(300, 184));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Sequence Information");
		this.setPreferredSize(new Dimension(300, 150));
		this.setContentPane(getJContentPane());
		this.pack();
		this.setLocationRelativeTo(parent);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
