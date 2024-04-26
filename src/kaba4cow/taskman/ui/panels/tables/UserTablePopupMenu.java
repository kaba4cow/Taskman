package kaba4cow.taskman.ui.panels.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class UserTablePopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public UserTablePopupMenu(UserTable table) {
		super();
		JMenuItem editItem = new JMenuItem("Edit");
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				table.editSelectedItem();
			}
		});
		add(editItem);
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				table.deleteSelectedItem();
			}
		});
		add(deleteItem);
	}

}