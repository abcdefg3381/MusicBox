package gdi.reader.gui;

import gdi.reader.MIDIReader;
import gdi.reader.logic.FileImporter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ReaderGUI extends JFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1230123262612512274L;

	public static void main(String[] args) {
		new ReaderGUI();
	}

	private JCheckBox authorCheckBox = null;

	private JButton browseButton = null;

	private JTextField browseTextField = null;

	private JButton composeButton = null;

	private JPanel composePanel = null;

	private JMenuItem connectDBMenuItem = null;

	private JList constrainList = null;

	private JScrollPane constrainScrollPane = null;

	private JMenu dbMenu = null;

	private FileImporter fileImporter;

	private JCheckBox genreCheckBox = null;

	private JPanel importPanel = null;

	private JPanel jContentPane = null;

	private JCheckBox modeCheckBox = null;

	private JButton playButton = null;

	private JProgressBar progressBar = null;

	private JPanel progressPanel = null;

	private JTextArea progressTextArea = null;

	private JMenuBar readerMenuBar = null;

	private JPanel resultConstrainPanel = null;

	private JPanel resultPanel = null;

	private JButton saveButton = null;

	private JButton startButton = null;

	private JButton statisticButton = null;

	private JButton stopButton = null;

	/**
	 * This method initializes
	 * 
	 */
	public ReaderGUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes authorCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getAuthorCheckBox() {
		if (authorCheckBox == null) {
			authorCheckBox = new JCheckBox();
			authorCheckBox.setText("limit author: ");
			authorCheckBox.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					// TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return authorCheckBox;
	}

	/**
	 * This method initializes browseButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBrowseButton() {
		if (browseButton == null) {
			browseButton = new JButton();
			browseButton.setText("Browse");
			browseButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getBrowseTextField().setText(MIDIReader.getInstance().getLogic().setImportFile() + " file(s) selected");
					getStartButton().setEnabled(true);
				}
			});
		}
		return browseButton;
	}

	/**
	 * This method initializes browseTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getBrowseTextField() {
		if (browseTextField == null) {
			browseTextField = new JTextField();
		}
		return browseTextField;
	}

	/**
	 * This method initializes composeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getComposeButton() {
		if (composeButton == null) {
			composeButton = new JButton();
			composeButton.setText("COMPOSE!");
			composeButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MIDIReader.getInstance().getLogic().composeSamplePiece();
				}
			});
		}
		return composeButton;
	}

	/**
	 * This method initializes composePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getComposePanel() {
		if (composePanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 2;
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 3;
			gridBagConstraints9.gridy = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 0;
			composePanel = new JPanel();
			composePanel.setLayout(new GridBagLayout());
			composePanel.setPreferredSize(new Dimension(0, 50));
			composePanel.setBorder(BorderFactory.createTitledBorder(null, "Compose", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			composePanel.add(getComposeButton(), new GridBagConstraints());
			composePanel.add(getPlayButton(), gridBagConstraints8);
			composePanel.add(getSaveButton(), gridBagConstraints9);
			composePanel.add(getStopButton(), gridBagConstraints10);
		}
		return composePanel;
	}

	/**
	 * This method initializes connectDBMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getConnectDBMenuItem() {
		if (connectDBMenuItem == null) {
			connectDBMenuItem = new JMenuItem();
			connectDBMenuItem.setText("Connect");
			connectDBMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MIDIReader.getInstance().getLogic().startBootstrap();
				}
			});
		}
		return connectDBMenuItem;
	}

	/**
	 * This method initializes constrainList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getConstrainList() {
		if (constrainList == null) {
			constrainList = new JList();
		}
		return constrainList;
	}

	/**
	 * This method initializes constrainScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getConstrainScrollPane() {
		if (constrainScrollPane == null) {
			constrainScrollPane = new JScrollPane();
			constrainScrollPane.setPreferredSize(new Dimension(0, 0));
			constrainScrollPane.setViewportView(getConstrainList());
		}
		return constrainScrollPane;
	}

	/**
	 * This method initializes dbMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getDbMenu() {
		if (dbMenu == null) {
			dbMenu = new JMenu();
			dbMenu.setText("Database");
			dbMenu.add(getConnectDBMenuItem());
		}
		return dbMenu;
	}

	/**
	 * This method initializes genreCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getGenreCheckBox() {
		if (genreCheckBox == null) {
			genreCheckBox = new JCheckBox();
			genreCheckBox.setText("limit genre: ");
			genreCheckBox.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					// TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return genreCheckBox;
	}

	/**
	 * This method initializes importPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getImportPanel() {
		if (importPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridwidth = 4;
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridheight = 4;
			gridBagConstraints7.weighty = 4.0;
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.ipadx = 0;
			gridBagConstraints5.fill = GridBagConstraints.NONE;
			gridBagConstraints5.gridwidth = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.ipadx = 0;
			gridBagConstraints4.ipady = 0;
			gridBagConstraints4.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.gridx = 1;
			importPanel = new JPanel();
			importPanel.setLayout(new GridBagLayout());
			importPanel.setBorder(BorderFactory.createTitledBorder(null, "Import from MIDI file",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12),
					new Color(51, 51, 51)));
			importPanel.add(getBrowseButton(), gridBagConstraints2);
			importPanel.add(getBrowseTextField(), gridBagConstraints4);
			importPanel.add(getStartButton(), gridBagConstraints5);
			importPanel.add(getProgressPanel(), gridBagConstraints7);
		}
		return importPanel;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
			jContentPane.add(getImportPanel(), null);
			jContentPane.add(getResultPanel(), null);
			jContentPane.add(getComposePanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes modeCheckBox
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getModeCheckBox() {
		if (modeCheckBox == null) {
			modeCheckBox = new JCheckBox();
			modeCheckBox.setText("limit mode: ");
			modeCheckBox.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()");
					// TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return modeCheckBox;
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
					MIDIReader.getInstance().getLogic().playTempMIDI();
				}
			});
		}
		return playButton;
	}

	/**
	 * This method initializes progressBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
			progressBar.setStringPainted(true);
		}
		return progressBar;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			BorderLayout borderLayout3 = new BorderLayout();
			borderLayout3.setHgap(5);
			borderLayout3.setVgap(5);
			progressPanel = new JPanel();
			progressPanel.setLayout(borderLayout3);
			progressPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			progressPanel.add(getProgressBar(), BorderLayout.PAGE_START);
			progressPanel.add(getProgressTextArea(), BorderLayout.CENTER);
		}
		return progressPanel;
	}

	/**
	 * This method initializes progressTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getProgressTextArea() {
		if (progressTextArea == null) {
			progressTextArea = new JTextArea();
			progressTextArea.setEditable(false);
			progressTextArea.setRows(8);
			progressTextArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		}
		return progressTextArea;
	}

	/**
	 * This method initializes readerMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getReaderMenuBar() {
		if (readerMenuBar == null) {
			readerMenuBar = new JMenuBar();
			readerMenuBar.add(getDbMenu());
		}
		return readerMenuBar;
	}

	/**
	 * This method initializes resultConstrainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getResultConstrainPanel() {
		if (resultConstrainPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.gridheight = 4;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridheight = 4;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 2;
			resultConstrainPanel = new JPanel();
			resultConstrainPanel.setLayout(new GridBagLayout());
			resultConstrainPanel.add(getAuthorCheckBox(), gridBagConstraints11);
			resultConstrainPanel.add(getGenreCheckBox(), gridBagConstraints);
			resultConstrainPanel.add(getModeCheckBox(), gridBagConstraints1);
			resultConstrainPanel.add(getConstrainScrollPane(), gridBagConstraints6);
		}
		return resultConstrainPanel;
	}

	/**
	 * This method initializes resultPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getResultPanel() {
		if (resultPanel == null) {
			BorderLayout borderLayout1 = new BorderLayout();
			borderLayout1.setHgap(5);
			borderLayout1.setVgap(5);
			resultPanel = new JPanel();
			resultPanel.setLayout(borderLayout1);
			resultPanel.setBorder(BorderFactory.createTitledBorder(null, "View Results", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			resultPanel.add(getStatisticButton(), BorderLayout.SOUTH);
			resultPanel.add(getResultConstrainPanel(), BorderLayout.NORTH);
		}
		return resultPanel;
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
			saveButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MIDIReader.getInstance().getLogic().saveTempMIDI();
				}
			});
		}
		return saveButton;
	}

	/**
	 * This method initializes startButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setText("Start");
			startButton.setEnabled(false);
			startButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					startButton.setEnabled(false);
					TrackInfoDialog tid = new TrackInfoDialog(MIDIReader.getInstance().getGUI());
					tid.setVisible(true);
				}
			});
		}
		return startButton;
	}

	/**
	 * This method initializes statisticButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStatisticButton() {
		if (statisticButton == null) {
			statisticButton = new JButton();
			statisticButton.setText("View Statistics");
			statisticButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					MIDIReader.getInstance().getLogic().getStatistics();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			});
		}
		return statisticButton;
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
					MIDIReader.getInstance().getLogic().stopTempMIDI();
				}
			});
		}
		return stopButton;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(400, 450));
		this.setPreferredSize(new Dimension(400, 450));
		this.setJMenuBar(getReaderMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("MIDI Reader 0.2");
		this.pack();
		GuiUtils.centerWindow(this);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				MIDIReader.getInstance().getLogic().shutDown();
			}
		});
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			getProgressBar().setValue(progress);
			getProgressTextArea().append(String.format("%d%% completed\n", fileImporter.getProgress()));
		}
		if (evt.getNewValue().toString() == "DONE") {
			setCursor(null); // turn off the wait cursor
			getProgressTextArea().append("Done!\n");
		}
	}

	public void setFileImporter(FileImporter fileImporter) {
		this.fileImporter = fileImporter;
		fileImporter.addPropertyChangeListener(this);
		fileImporter.execute();
	}
} // @jve:decl-index=0:visual-constraint="6,32"
