package kaba4cow.taskman.ui.panels.tables.tasks;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class TaskTablePanel extends JSplitPane {

	private static final long serialVersionUID = 1L;

	public TaskTablePanel() {
		super();
		setResizeWeight(1d);
		TaskTableFilter filter = new TaskTableFilter();
		TaskTable table = new TaskTable(filter);
		setRightComponent(new TaskTableFilterPanel(table, filter));
		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setViewportView(table);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(TaskTable.getPreferredWidth(), 0));
		setLeftComponent(scrollPanel);
	}

}
