package kaba4cow.taskman.ui.panels.tables.notes;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class NoteTablePanel extends JSplitPane {

	private static final long serialVersionUID = 1L;

	public NoteTablePanel() {
		super();
		setResizeWeight(1d);
		NoteTableFilter filter = new NoteTableFilter();
		NoteTable table = new NoteTable(filter);
		setRightComponent(new NoteTableFilterPanel(table, filter));
		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setViewportView(table);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(NoteTable.getPreferredWidth(), 0));
		setLeftComponent(scrollPanel);
	}

}
