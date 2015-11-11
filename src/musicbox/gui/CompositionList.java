/**
 * 
 */
package musicbox.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import musicbox.MainProgram;

/**
 * @author LIU Xiaofan
 * 
 */
public class CompositionList extends JList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3532477312826679678L;
	private JMenuItem formNet = null;
	private NetworkPanel panel;
	private JMenuItem playNow = null;
	private JPopupMenu popup = null;
	private JMenuItem visual = null;

	/**
	 * 
	 */
	public CompositionList(NetworkPanel panel) {
		this.panel = panel;
		initialize();
	}

	private void formNetwork() {
		MainProgram
				.getInstance()
				.getLogic()
				.fetchNetworkOrigin(getSelectedValues(), (Integer) panel.getParameters()[0],
						(Integer) panel.getParameters()[1], (Boolean) panel.getParameters()[2]);
	}

	private void formPopUp(Object selectedValue) {
		popup = new JPopupMenu();
		formNet = new JMenuItem("Form network");
		formNet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				formNetwork();
			}
		});
		visual = new JMenuItem("Visualize");
		visual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainProgram.getInstance().getLogic().vizMIDI(getSelectedValue());
			}
		});
		playNow = new JMenuItem("Play now");
		playNow.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				MainProgram.getInstance().getLogic().playMIDI(getSelectedValue());
			}
		});
		popup.add(formNet);
		popup.add(visual);
		popup.add(playNow);
	}

	/**
	 * 
	 */
	private void initialize() {
		this.setToolTipText("double click to form network");
		addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					formNetwork();
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					setSelectedIndex(locationToIndex(e.getPoint()));
					formPopUp(getSelectedValue());
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
	}

}
