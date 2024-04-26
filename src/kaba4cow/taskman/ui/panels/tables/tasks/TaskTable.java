package kaba4cow.taskman.ui.panels.tables.tasks;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.ApplicationCalendar.ApplicationCalendarListener;
import kaba4cow.taskman.ApplicationSettings;
import kaba4cow.taskman.ApplicationSettings.ApplicationSettingsListener;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.dialogs.edit.TaskEditDialog;
import kaba4cow.taskman.ui.panels.tables.UserTable;
import kaba4cow.taskman.ui.panels.tables.UserTablePopupMenu;
import kaba4cow.taskman.ui.panels.tables.renderers.CenteredRenderer;
import kaba4cow.taskman.ui.panels.tables.renderers.ColoredRenderer;
import kaba4cow.taskman.utils.DateUtils;

public class TaskTable extends JTable implements UserTable<TaskTableObject>, ApplicationSettingsListener,
		ApplicationCalendarListener, RepositoryListener<Task> {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = { "", "Time", "Description" };
	private static final int[] COLUMN_WIDTHS = { 20, 80, 300 };

	private final List<TaskTableObject> taskList;

	private final TaskTableFilter tableFilter;

	public TaskTable(TaskTableFilter filter) {
		super();
		Application.getSettings().addListener(this);
		Application.getCalendar().addListener(this);
		Application.getTaskRepository().addListener(this);
		taskList = new ArrayList<>();
		tableFilter = filter;
		setModel(new TaskTableModel());
		setCellSelectionEnabled(false);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		CenteredRenderer centeredRenderer = new CenteredRenderer();
		ColoredRenderer coloredRenderer = new ColoredRenderer();
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			if (i == 0)
				getColumnModel().getColumn(i).setCellRenderer(coloredRenderer);
			else if (i == 1)
				getColumnModel().getColumn(i).setCellRenderer(centeredRenderer);
			getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTHS[i]);
		}
		addMouseListener(new UserTablePopupMenu(this));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_DELETE)
					deleteSelectedItem();
			}
		});
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		Point point = event.getPoint();
		int column = columnAtPoint(point);
		if (column != 0)
			return null;
		int row = rowAtPoint(point);
		if (row >= 0 && row < getRowCount())
			return taskList.get(row).task.getType().getName();
		return null;
	}

	@Override
	public void onSettingsUpdated(ApplicationSettings settings) {
		repaint();
	}

	@Override
	public void onDateChanged(LocalDate newDate) {
		updateTable();
	}

	@Override
	public void onRepositoryUpdated(Repository<Task> repository) {
		updateTable();
	}

	@Override
	public void updateTable() {
		taskList.clear();
		List<Task> tasks = Application.getTaskRepository().getTasksAtDate(Application.getCalendar().getDate());
		for (Task task : tasks) {
			List<LocalTime> taskTimes = task.getTimes();
			for (LocalTime time : taskTimes) {
				TaskTableObject taskObject = new TaskTableObject(task, time);
				if (tableFilter.filter(taskObject))
					taskList.add(taskObject);
			}
		}
		tableFilter.sort(taskList);
		TaskTableModel tableModel = (TaskTableModel) getModel();
		tableModel.fireTableDataChanged();
	}

	@Override
	public void addNewItem() {
		Task task = Application.getTaskRepository().createTask(Application.getCalendar().getDate());
		new TaskEditDialog(task, task.getTimes().get(0), true);
	}

	@Override
	public void editSelectedItem() {
		TaskTableObject task = getSelectedItem();
		if (task != null)
			new TaskEditDialog(task.task, task.time, false);
	}

	@Override
	public void deleteSelectedItem() {
		TaskTableObject task = getSelectedItem();
		if (task != null) {
			String message = String.format("Delete task \"%s\" at %s?", task.task.getDescription(),
					DateUtils.formatTime(task.time));
			if (JOptionPane.showConfirmDialog(TaskTable.this, message, "Delete Task",
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				Application.getTaskRepository().deleteElement(task.task, task.time);
		}
	}

	@Override
	public TaskTableObject getSelectedItem() {
		return getSelectedRow() == -1 ? null : taskList.get(getSelectedRow());
	}

	public static int getPreferredWidth() {
		int total = COLUMN_WIDTHS[0];
		for (int i = 1; i < COLUMN_WIDTHS.length; i++)
			total += COLUMN_WIDTHS[i];
		return total;
	}

	private class TaskTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		public TaskTableModel() {
			super();
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		@Override
		public int getRowCount() {
			return taskList.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			TaskTableObject task = taskList.get(rowIndex);
			switch (columnIndex) {
			default:
			case 0:
				return Application.getSettings().getTaskColor(task.task.getType());
			case 1:
				return DateUtils.formatTime(task.time);
			case 2:
				return task.task.getDescription();
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}

}