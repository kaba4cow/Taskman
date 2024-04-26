package kaba4cow.taskman.ui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import kaba4cow.taskman.ui.dialogs.AboutDialog;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	public HelpMenu() {
		super("Help");

		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new AboutDialog();
			}
		});
		add(aboutItem);
	}

}
