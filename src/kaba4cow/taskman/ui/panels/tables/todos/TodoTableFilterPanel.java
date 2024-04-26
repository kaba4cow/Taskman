package kaba4cow.taskman.ui.panels.tables.todos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.todo.Todo;
import kaba4cow.taskman.ui.panels.tables.todos.TodoTableFilter.SortingTarget;
import kaba4cow.taskman.utils.UiUtils;

public class TodoTableFilterPanel extends JPanel implements ItemListener, DocumentListener, RepositoryListener<Todo> {

	private static final long serialVersionUID = 1L;

	private final JTextField descriptionTextField;
	private final JCheckBox caseSensitiveCheckBox;
	private final JCheckBox tagCheckBox;
	private final JComboBox<String> tagComboBox;
	private final JCheckBox priorityCheckBox;
	private final JComboBox<String> priorityComboBox;
	private final JCheckBox statusCheckBox;
	private final JComboBox<String> statusComboBox;
	private final JComboBox<String> sortingComboBox;
	private final JCheckBox sortingReversedCheckBox;

	private final TodoTable table;
	private final TodoTableFilter filter;

	public TodoTableFilterPanel(TodoTable table, TodoTableFilter filter) {
		super();
		this.table = table;
		this.filter = filter;
		setLayout(new GridBagLayout());
		Application.getTodoRepository().addListener(this);
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		JPanel descriptionPanel = UiUtils.createTitledPanel("Find", 2, 1);
		{
			descriptionTextField = new JTextField("");
			descriptionTextField.getDocument().addDocumentListener(this);
			descriptionPanel.add(descriptionTextField);

			caseSensitiveCheckBox = new JCheckBox("Case Sensitive");
			caseSensitiveCheckBox.setHorizontalTextPosition(JCheckBox.LEADING);
			caseSensitiveCheckBox.setSelected(false);
			caseSensitiveCheckBox.addItemListener(this);
			descriptionPanel.add(caseSensitiveCheckBox);
		}
		add(descriptionPanel, gbc);

		JPanel tagPanel = UiUtils.createTitledPanel("Tag Filter", 1, 1);
		{
			tagCheckBox = new JCheckBox("Enabled");
			tagCheckBox.setSelected(false);
			tagCheckBox.addItemListener(this);
			tagPanel.add(tagCheckBox);

			tagComboBox = new JComboBox<>();
			tagComboBox.setEnabled(false);
			tagComboBox.addItemListener(this);
			fillTagComboBox();
			tagPanel.add(tagComboBox);
		}
		add(tagPanel, gbc);

		JPanel statusPanel = UiUtils.createTitledPanel("Status Filter", 1, 2);
		{
			statusCheckBox = new JCheckBox("Enable");
			statusCheckBox.setHorizontalTextPosition(JCheckBox.LEADING);
			statusCheckBox.setSelected(false);
			statusCheckBox.addItemListener(this);
			statusPanel.add(statusCheckBox);

			statusComboBox = new JComboBox<>();
			for (Todo.Status status : Todo.Status.values())
				statusComboBox.addItem(status.getName());
			statusComboBox.addItemListener(this);
			statusComboBox.setEnabled(false);
			statusPanel.add(statusComboBox);
		}
		add(statusPanel, gbc);

		JPanel priorityPanel = UiUtils.createTitledPanel("Priority Filter", 1, 2);
		{
			priorityCheckBox = new JCheckBox("Enable");
			priorityCheckBox.setHorizontalTextPosition(JCheckBox.LEADING);
			priorityCheckBox.setSelected(false);
			priorityCheckBox.addItemListener(this);
			priorityPanel.add(priorityCheckBox);

			priorityComboBox = new JComboBox<>();
			for (Todo.Priority priority : Todo.Priority.values())
				priorityComboBox.addItem(priority.getName());
			priorityComboBox.addItemListener(this);
			priorityComboBox.setEnabled(false);
			priorityPanel.add(priorityComboBox);
		}
		add(priorityPanel, gbc);

		JPanel sortingPanel = UiUtils.createTitledPanel("Sorting", 2, 2);
		{
			sortingPanel.add(new JLabel("Sort By"));
			sortingComboBox = new JComboBox<>();
			for (SortingTarget sorting : SortingTarget.values())
				sortingComboBox.addItem(sorting.getName());
			sortingComboBox.addItemListener(this);
			sortingPanel.add(sortingComboBox);

			sortingReversedCheckBox = new JCheckBox("Reversed");
			sortingReversedCheckBox.setSelected(false);
			sortingReversedCheckBox.addItemListener(this);
			sortingPanel.add(sortingReversedCheckBox);
		}
		add(sortingPanel, gbc);
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == caseSensitiveCheckBox)
			filter.setCaseSensitive(caseSensitiveCheckBox.isSelected());

		else if (event.getSource() == tagCheckBox) {
			tagComboBox.setEnabled(tagCheckBox.isSelected());
			filter.setTag(tagCheckBox.isSelected() ? (String) tagComboBox.getSelectedItem() : null);
		} else if (event.getSource() == tagComboBox)
			filter.setTag((String) tagComboBox.getSelectedItem());

		else if (event.getSource() == statusCheckBox) {
			statusComboBox.setEnabled(statusCheckBox.isSelected());
			filter.setStatus(statusCheckBox.isSelected() ? Todo.Status.get(statusComboBox.getSelectedIndex()) : null);
		} else if (event.getSource() == statusComboBox)
			filter.setStatus(Todo.Status.get(statusComboBox.getSelectedIndex()));

		else if (event.getSource() == priorityCheckBox) {
			priorityComboBox.setEnabled(priorityCheckBox.isSelected());
			filter.setPriority(
					priorityCheckBox.isSelected() ? Todo.Priority.get(priorityComboBox.getSelectedIndex()) : null);
		} else if (event.getSource() == priorityComboBox)
			filter.setPriority(Todo.Priority.get(priorityComboBox.getSelectedIndex()));

		else if (event.getSource() == sortingComboBox)
			filter.setSortingTarget(SortingTarget.values()[sortingComboBox.getSelectedIndex()]);
		else if (event.getSource() == sortingReversedCheckBox)
			filter.setSortingReversed(sortingReversedCheckBox.isSelected());
		table.updateTable();
	}

	@Override
	public void insertUpdate(DocumentEvent event) {
		filter.setDescription(descriptionTextField.getText());
		table.updateTable();
	}

	@Override
	public void removeUpdate(DocumentEvent event) {
		filter.setDescription(descriptionTextField.getText());
		table.updateTable();
	}

	@Override
	public void changedUpdate(DocumentEvent event) {
		filter.setDescription(descriptionTextField.getText());
		table.updateTable();
	}

	@Override
	public void onRepositoryUpdated(Repository<Todo> repository) {
		fillTagComboBox();
	}

	private void fillTagComboBox() {
		tagComboBox.removeAllItems();
		Set<String> tagSet = Application.getTodoRepository().getAllTags();
		for (String tag : tagSet)
			tagComboBox.addItem(tag);
		filter.setTag(null);
		table.updateTable();
	}

}
