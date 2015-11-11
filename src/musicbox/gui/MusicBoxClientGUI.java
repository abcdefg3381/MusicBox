package musicbox.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import maggie.network.gui.GuiUtils;
import musicbox.MainProgram;

/**
 * GUI of Client application
 * 
 * @author LIU Xiaofan
 * 
 */
public class MusicBoxClientGUI extends JFrame {

	/**
	 * random serial version uid is given
	 */
	private static final long serialVersionUID = -1046660606841733678L;
	private JPanel baseContentPane = null;
	private ComposePanel composePanel = null;
	private JPanel controlPanel;
	private Menus jMenuBar = null;
	private NetworkPanel networkPanel = null;
	private ViewerPanel vizComposedPanel;
	private ViewerPanel vizOriginPanel = null;

	/**
	 * This method initializes
	 * 
	 */
	public MusicBoxClientGUI() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initialize();
	}

	public void fetchData() {
		this.getNetworkPanel().setComposition(MainProgram.getInstance().getLogic().getNetworkList());
	}

	/**
	 * This method initializes baseContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBaseContentPane() {
		if (baseContentPane == null) {
			baseContentPane = new JPanel();
			GridBagLayout gbl_baseContentPane = new GridBagLayout();
			gbl_baseContentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gbl_baseContentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			baseContentPane.setLayout(gbl_baseContentPane);
			GridBagConstraints gbc_controlPanel = new GridBagConstraints();
			gbc_controlPanel.gridheight = 2;
			gbc_controlPanel.anchor = GridBagConstraints.WEST;
			gbc_controlPanel.fill = GridBagConstraints.BOTH;
			gbc_controlPanel.gridx = 2;
			gbc_controlPanel.gridy = 0;
			baseContentPane.add(getControlPanel(), gbc_controlPanel);
			GridBagConstraints gbc_vizOriginPanel = new GridBagConstraints();
			gbc_vizOriginPanel.insets = new Insets(0, 0, 0, 5);
			gbc_vizOriginPanel.gridwidth = 2;
			gbc_vizOriginPanel.gridheight = 2;
			gbc_vizOriginPanel.fill = GridBagConstraints.BOTH;
			gbc_vizOriginPanel.gridx = 0;
			gbc_vizOriginPanel.gridy = 0;
			baseContentPane.add(getVizOriginPanel(), gbc_vizOriginPanel);
			GridBagConstraints gbc_vizComposedPanel = new GridBagConstraints();
			gbc_vizComposedPanel.gridwidth = 2;
			gbc_vizComposedPanel.gridheight = 2;
			gbc_vizComposedPanel.insets = new Insets(0, 0, 0, 5);
			gbc_vizComposedPanel.fill = GridBagConstraints.BOTH;
			gbc_vizComposedPanel.gridx = 4;
			gbc_vizComposedPanel.gridy = 0;
			baseContentPane.add(getVizComposedPanel(), gbc_vizComposedPanel);
		}
		return baseContentPane;
	}

	/**
	 * This method initializes composePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getComposePanel() {
		if (composePanel == null) {
			composePanel = new ComposePanel();
		}
		return composePanel;
	}

	private JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
			controlPanel.add(getNetworkPanel());
			controlPanel.add(getComposePanel());
		}
		return controlPanel;
	}

	/**
	 * This method initializes jMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private JMenuBar getjMenuBar() {
		if (jMenuBar == null) {
			jMenuBar = new Menus();
		}
		return jMenuBar;
	}

	/**
	 * This method initializes resultPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private NetworkPanel getNetworkPanel() {
		if (networkPanel == null) {
			networkPanel = new NetworkPanel();
		}
		return networkPanel;
	}

	public ViewerPanel getVizComposedPanel() {
		if (vizComposedPanel == null) {
			vizComposedPanel = new ViewerPanel("Composed Music: ");
		}
		return vizComposedPanel;
	}

	/**
	 * This method initializes drawPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public ViewerPanel getVizOriginPanel() {
		if (vizOriginPanel == null) {
			vizOriginPanel = new ViewerPanel("Original Music: ");
		}
		return vizOriginPanel;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setContentPane(getBaseContentPane());
		this.setJMenuBar(getjMenuBar());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("musicbox.gif")));
		this.setTitle("MusicBox");
		GuiUtils.maxFrame(this);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				MainProgram.getInstance().exit();
			}
		});
		this.setVisible(true);
	}
}
