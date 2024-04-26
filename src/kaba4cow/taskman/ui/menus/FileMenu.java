package kaba4cow.taskman.ui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.ui.dialogs.SettingsDialog;

public class FileMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	public FileMenu() {
		super("File");

		JMenuItem settingsItem = new JMenuItem("Settings");
		settingsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new SettingsDialog();
			}
		});
		add(settingsItem);
		addSeparator();

		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Application.hideWindow();
			}
		});
		add(closeItem);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Application.closeProgram();
			}
		});
		add(exitItem);
	}

}
