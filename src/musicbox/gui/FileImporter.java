package musicbox.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import maggie.network.gui.GuiUtils;
import musicbox.MainProgram;

/**
 * @author LIU Xiaofan
 * 
 */
public class FileImporter extends JDialog implements PropertyChangeListener {

	/**
	 * FileImportWorker is a SwingWorker which import the files and refresh
	 * progress on a progress bar.
	 * 
	 * @author LIU Xiaofan
	 * 
	 */
	class FileImportWorker extends SwingWorker<Void, Void> {

		private File[] files;

		public FileImportWorker(File[] files) {
			super();
			this.files = files;
		}

		@Override
		public Void doInBackground() {
			setProgress(0);

			while (getProgress() < 100) {

				int i = 1;
				int total = files.length;

				long time = System.currentTimeMillis();
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
					MainProgram.getInstance().getLogic().importFile(file);
					setProgress(i++ * 100 / total);
				}

				System.out.println("time eclapsed: " + (System.currentTimeMillis() - time) + " ms");
				setProgress(100);
			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8442137002640539563L;
	private JPanel baseContentPane = null;
	private JButton browseButton = null;
	private JTextField browseTextField = null;
	private FileImportWorker fileImportWorker;
	private File[] files;
	private JPanel importPanel = null; // @jve:decl-index=0:visual-constraint="44,46"
	private JLabel lblMidiFile;
	private JLabel lblProgress;
	private JProgressBar progressBar = null;
	private JPanel progressPanel = null;

	private JScrollPane progressScrollPane = null;
	private JTextArea progressTextArea = null;
	private JButton startButton = null;

	/**
	 * @param gui
	 */
	public FileImporter(MusicBoxClientGUI gui) {
		super(gui);
		setTitle("Import MIDI");
		initialize();
	}

	/**
	 * This method initializes baseContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBaseContentPane() {
		if (baseContentPane == null) {
			baseContentPane = new JPanel();
			baseContentPane.setLayout(new BorderLayout());
			baseContentPane.add(getImportPanel(), BorderLayout.NORTH);
		}
		return baseContentPane;
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
					getBrowseTextField().setText(setImportFile() + " file(s) selected");
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
	 * This method initializes importPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getImportPanel() {
		if (importPanel == null) {
			importPanel = new JPanel();
			importPanel.setLayout(new GridBagLayout());
			importPanel.setPreferredSize(new Dimension(280, 400));
			importPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagConstraints gbc_lblMidiFile = new GridBagConstraints();
			gbc_lblMidiFile.insets = new Insets(0, 0, 5, 5);
			gbc_lblMidiFile.anchor = GridBagConstraints.WEST;
			gbc_lblMidiFile.gridx = 0;
			gbc_lblMidiFile.gridy = 0;
			importPanel.add(getLblMidiFile(), gbc_lblMidiFile);
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.insets = new Insets(0, 0, 5, 5);
			gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints41.gridy = 0;
			gridBagConstraints41.ipadx = 0;
			gridBagConstraints41.ipady = 0;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.gridx = 1;
			importPanel.add(getBrowseTextField(), gridBagConstraints41);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.gridx = 2;
			importPanel.add(getBrowseButton(), gridBagConstraints21);
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.NONE;
			gridBagConstraints51.gridx = 3;
			gridBagConstraints51.gridy = 0;
			gridBagConstraints51.ipadx = 0;
			gridBagConstraints51.insets = new Insets(5, 5, 5, 0);
			importPanel.add(getStartButton(), gridBagConstraints51);
			GridBagConstraints gbc_lblProgress = new GridBagConstraints();
			gbc_lblProgress.anchor = GridBagConstraints.WEST;
			gbc_lblProgress.insets = new Insets(0, 0, 5, 5);
			gbc_lblProgress.gridx = 0;
			gbc_lblProgress.gridy = 1;
			importPanel.add(getLblProgress(), gbc_lblProgress);
			GridBagConstraints gbc_progressBar = new GridBagConstraints();
			gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
			gbc_progressBar.gridwidth = 3;
			gbc_progressBar.insets = new Insets(0, 0, 5, 0);
			gbc_progressBar.gridx = 1;
			gbc_progressBar.gridy = 1;
			importPanel.add(getProgressBar(), gbc_progressBar);
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints71.fill = GridBagConstraints.BOTH;
			gridBagConstraints71.gridwidth = 4;
			gridBagConstraints71.gridx = 0;
			gridBagConstraints71.gridy = 2;
			gridBagConstraints71.weighty = 4.0;
			importPanel.add(getProgressPanel(), gridBagConstraints71);
		}
		return importPanel;
	}

	private JLabel getLblMidiFile() {
		if (lblMidiFile == null) {
			lblMidiFile = new JLabel("MIDI File: ");
		}
		return lblMidiFile;
	}

	private JLabel getLblProgress() {
		if (lblProgress == null) {
			lblProgress = new JLabel("Progress: ");
		}
		return lblProgress;
	}

	/**
	 * This method initializes progressBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar(0, 200);
			progressBar.setStringPainted(true);
			progressBar.setValue(0);
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
			BorderLayout borderLayout31 = new BorderLayout();
			borderLayout31.setHgap(5);
			borderLayout31.setVgap(5);
			progressPanel = new JPanel();
			progressPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Details: ",
					TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
			progressPanel.setLayout(borderLayout31);
			progressPanel.add(getProgressScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return progressPanel;
	}

	/**
	 * This method initializes progressScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getProgressScrollPane() {
		if (progressScrollPane == null) {
			progressScrollPane = new JScrollPane();
			progressScrollPane.setViewportView(getProgressTextArea());
		}
		return progressScrollPane;
	}

	/**
	 * This method initializes progressTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getProgressTextArea() {
		if (progressTextArea == null) {
			progressTextArea = new JTextArea();
			progressTextArea.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			progressTextArea.setEditable(false);
		}
		return progressTextArea;
	}

	/**
	 * This method initializes startButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setEnabled(false);
			startButton.setText("Import");
			startButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					startButton.setEnabled(false);
					importFile(new FileImportWorker(files));
				}

			});
		}
		return startButton;
	}

	private void importFile(FileImportWorker fileImportWorker) {
		this.fileImportWorker = fileImportWorker;
		fileImportWorker.addPropertyChangeListener(this);
		fileImportWorker.execute();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setPreferredSize(new Dimension(400, 434));
		this.setContentPane(getBaseContentPane());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.pack();
		this.setModal(true);
		GuiUtils.centerWindow(this);
		this.setVisible(true);
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			getProgressBar().setValue(progress);
			getProgressTextArea().append(String.format("%d%% completed\n", fileImportWorker.getProgress()));
		}
		if (evt.getNewValue().toString() == "DONE") {
			setCursor(null); // turn off the wait cursor
			getProgressTextArea().append("Done!\n");
		}
	}

	public int setImportFile() {
		JFileChooser fc = new JFileChooser("./midi files/");
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".mid") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return ".mid file";
			}
		});
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new File("."));
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			files = fc.getSelectedFiles();
			return files.length;
		} else {
			return 0;
		}
	}
}