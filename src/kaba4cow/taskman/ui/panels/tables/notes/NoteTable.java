package kaba4cow.taskman.ui.panels.tables.notes;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.note.Note;
import kaba4cow.taskman.ui.dialogs.edit.NoteEditDialog;
import kaba4cow.taskman.ui.panels.tables.UserTable;
import kaba4cow.taskman.ui.panels.tables.UserTablePopupMenu;
import kaba4cow.taskman.ui.panels.tables.renderers.CenteredRenderer;
import kaba4cow.taskman.utils.DateUtils;

public class NoteTable extends JTable
		implements UserTable<Note>, RepositoryListener<Note> {

	private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = { "Date", "Tag", "Description" };
	private static final int[] COLUMN_WIDTHS = { 80, 80, 240 };

	private final List<Note> noteList;

	private final NoteTableFilter tableFilter;

	public NoteTable(NoteTableFilter filter) {
		super();
		Application.getNoteRepository().addListener(this);
		noteList = new ArrayList<>();
		tableFilter = filter;
		setModel(new NoteTableModel());
		setCellSelectionEnabled(false);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
		CenteredRenderer alignedRenderer = new CenteredRenderer();
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			if (i != 2)
				getColumnModel().getColumn(i).setCellRenderer(alignedRenderer);
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
		if (column != 2)
			return null;
		int row = rowAtPoint(point);
		if (row >= 0 && row < getRowCount())
			return noteList.get(row).getDescription();
		return null;
	}

	@Override
	public void onRepositoryUpdated(Repository<Note> repository) {
		updateTable();
	}

	@Override
	public void updateTable() {
		noteList.clear();
		List<Note> notes = Application.getNoteRepository().getAll();
		for (Note note : notes)
			if (tableFilter.filter(note))
				noteList.add(note);
		tableFilter.sort(noteList);
		NoteTableModel tableModel = (NoteTableModel) getModel();
		tableModel.fireTableDataChanged();
	}

	@Override
	public void addNewItem() {
		new NoteEditDialog(Application.getNoteRepository().createNote(), true);
	}

	@Override
	public void editSelectedItem() {
		Note note = getSelectedItem();
		if (note != null)
			new NoteEditDialog(note, false);
	}

	@Override
	public void deleteSelectedItem() {
		Note note = getSelectedItem();
		if (note != null)
			if (JOptionPane.showConfirmDialog(NoteTable.this, "Delete note?", "Delete note",
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
				Application.getNoteRepository().deleteElement(note);
	}

	@Override
	public Note getSelectedItem() {
		return getSelectedRow() == -1 ? null : noteList.get(getSelectedRow());
	}

	public static int getPreferredWidth() {
		int total = COLUMN_WIDTHS[0];
		for (int i = 1; i < COLUMN_WIDTHS.length; i++)
			total += COLUMN_WIDTHS[i];
		return total;
	}

	private class NoteTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		public NoteTableModel() {
			super();
		}

		@Override
		public String getColumnName(int column) {
			return COLUMN_NAMES[column];
		}

		@Override
		public int getRowCount() {
			return noteList.size();
		}

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Note note = noteList.get(rowIndex);
			switch (columnIndex) {
			default:
			case 0:
				return DateUtils.formatDate(note.getDate());
			case 1:
				return note.getTag();
			case 2:
				return note.getDescription();
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

	}

}