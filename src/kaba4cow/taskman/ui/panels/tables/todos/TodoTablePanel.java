package kaba4cow.taskman.ui.panels.tables.todos;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class TodoTablePanel extends JSplitPane {

	private static final long serialVersionUID = 1L;

	public TodoTablePanel() {
		super();
		setResizeWeight(1d);
		TodoTableFilter filter = new TodoTableFilter();
		TodoTable table = new TodoTable(filter);
		setRightComponent(new TodoTableFilterPanel(table, filter));
		JScrollPane scrollPanel = new JScrollPane(table);
		scrollPanel.setViewportView(table);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setPreferredSize(new Dimension(TodoTable.getPreferredWidth(), 0));
		setLeftComponent(scrollPanel);
	}

}
