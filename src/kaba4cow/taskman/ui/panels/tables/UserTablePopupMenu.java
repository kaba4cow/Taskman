package kaba4cow.taskman.ui.panels.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class UserTablePopupMenu extends JPopupMenu implements MouseListener {

	private static final long serialVersionUID = 1L;

	private final UserTable<?> table;

	private final JMenuItem addItem;
	private final JMenuItem editItem;
	private final JMenuItem deleteItem;

	public UserTablePopupMenu(UserTable<?> table) {
		super();
		this.table = table;
		addItem = new JMenuItem("Add");
		addItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				table.addNewItem();
			}
		});
		add(addItem);
		addSeparator();
		editItem = new JMenuItem("Edit");
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				table.editSelectedItem();
			}
		});
		add(editItem);
		deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				table.deleteSelectedItem();
			}
		});
		add(deleteItem);
	}

	public void show(int x, int y) {
		editItem.setEnabled(table.getSelectedItem() != null);
		deleteItem.setEnabled(table.getSelectedItem() != null);
		show((JTable) table, x, y);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3)
			show(event.getX(), event.getY());
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() % 2 == 0)
			table.editSelectedItem();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}