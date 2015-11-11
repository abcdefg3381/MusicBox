/**
 * 
 */
package musicbox.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import maggie.network.gui.AboutDialog;
import musicbox.MainProgram;

/**
 * @author LIU Xiaofan
 * 
 */
public class Menus extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1872752905506926241L;
	private AboutDialog aboutDialog = null;
	private JMenuItem aboutMenuItem = null;
	private JMenuItem connectMenuItem = null;
	private JMenu dbMenu = null;
	private JMenu helpMenu = null;
	private JMenu importMenu = null;
	private JMenuItem importMenuItem = null;

	/**
	 * This method initializes
	 * 
	 */
	public Menus() {
		initialize();
	}

	/**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private AboutDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new AboutDialog(MainProgram.getInstance().getGUI());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getAboutDialog().setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

	/**
	 * This method initializes connectMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getConnectMenuItem() {
		if (connectMenuItem == null) {
			connectMenuItem = new JMenuItem();
			connectMenuItem.setText("Reconnect DB");
			connectMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic().reconnect();
				}
			});
		}
		return connectMenuItem;
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
			dbMenu.add(getConnectMenuItem());
		}
		return dbMenu;
	}

	/**
	 * This method initializes helpMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	@Override
	public JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes importMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getImportMenu() {
		if (importMenu == null) {
			importMenu = new JMenu();
			importMenu.setText("Import");
			importMenu.add(getImportMenuItem());
		}
		return importMenu;
	}

	/**
	 * This method initializes importMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getImportMenuItem() {
		if (importMenuItem == null) {
			importMenuItem = new JMenuItem();
			importMenuItem.setText("Import MIDI");
			importMenuItem.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new FileImporter(MainProgram.getInstance().getGUI());
				}
			});
		}
		return importMenuItem;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		add(getDbMenu());
		add(getImportMenu());
		add(getHelpMenu());
	}

}
