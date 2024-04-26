package kaba4cow.taskman.ui.panels.tables.todos;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import kaba4cow.taskman.ApplicationSettings;
import kaba4cow.taskman.ApplicationSettings.ApplicationSettingsListener;
import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.todo.Todo;
import kaba4cow.taskman.ui.dialogs.edit.TodoEditDialog;
import kaba4cow.taskman.ui.panels.tables.UserTable;
import kaba4cow.taskman.ui.panels.tables.UserTablePopupMenu;
import kaba4cow.taskman.ui.panels.tables.renderers.CenteredRenderer;

public class TodoTable extends JTable implements UserTable, ApplicationSettingsListener, RepositoryListener<Todo> {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = { "Status", "Priority", "Tag", "Description" };
	private static final int[] COLUMN_WIDTHS = { 60, 50, 70, 220 };

	private final List<Todo> todoList;

	private final UserTablePopupMenu popupMenu;

	private final TodoTableFilter tableFilter;

	public TodoTable(TodoTableFilter filter) {
		super();
		Application.getSettings().addListener(this);
		Application.getTodoRepository().addListener(this);
		todoList = new ArrayList<>();
		popupMenu = new UserTablePopupMenu(this);
		tableFilter = filter;
		setModel(new TodoTableModel());
		setCellSelectionEnabled(false);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		CenteredRenderer alignedRenderer = new CenteredRenderer();
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			if (i != 3)
				getColumnModel().getColumn(i).setCellRenderer(alignedRenderer);
			getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTHS[i]);
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3 && getSelectedTodo() != null)
					popupMenu.show(TodoTable.this, event.getX(), event.getY());
			}

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() % 2 == 0)
					editSelectedItem();
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_DELETE)
					deleteSelectedItem();
			}
		});
	}

	@Override
	public void onSettingsUpdated(ApplicationSettings settings) {
		repaint();
	}

	@Override
	public void onRepositoryUpdated(Repository<Todo> repository) {
		updateTable();
	}

	@Override
	public void updateTable() {
		todoList.clear();
		List<Todo> todos = Application.getTodoRepository().getAll();
		for (Todo todo : todos)
			if (tableFilter.filter(todo))
				todoList.add(todo);
		tableFilter.sort(todoList);
		TodoTableModel tableModel = (TodoTableModel) getModel();
		tableModel.fireTableDataChanged();
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		Point point = event.getPoint();
		int column = columnAtPoint(point);
		if (column != 2)
			return null;
		int row = rowAtPoint(point);
		if (row >= 0 && row < getRowCount())
			return todoList.get(row).getDescription();
		return null;
	}

	@Override
	public void editSelectedItem() {
		Todo todo = getSelectedTodo();
		if (todo != null)
			new TodoEditDialog(todo, false);
	}

	@Override
	public void deleteSelectedItem() {
		Todo todo = getSelectedTodo();
		if (todo != null) {
			String message = String.format("Delete TODO \"%s\"?", todo.getDescription());
			if (JOptionPane.showConfirmDialog(TodoTable.this, "Delete TODO?", message,
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				Application.getTodoRepository().deleteElement(todo);
		}
	}

	private Todo getSelectedTodo() {
		return getSelectedRow() == -1 ? null : todoList.get(getSelectedRow());
	}

	public static int getPreferredWidth() {
		int total = COLUMN_WIDTHS[0];
		for (int i = 1; i < COLUMN_WIDTHS.length; i++)
			total += COLUMN_WIDTHS[i];
		return total;
	}

	private class TodoTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		public TodoTableModel() {
			super();
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		@Override
		public int getRowCount() {
			return todoList.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Todo todo = todoList.get(rowIndex);
			switch (columnIndex) {
			default:
			case 0:
				return todo.getStatus().getName();
			case 1:
				return todo.getPriority().getName();
			case 2:
				return todo.getTag();
			case 3:
				return todo.getDescription();
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}

}