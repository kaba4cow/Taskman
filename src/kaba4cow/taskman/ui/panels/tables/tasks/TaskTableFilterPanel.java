package kaba4cow.taskman.ui.panels.tables.tasks;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalTime;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.TimeChooser;
import kaba4cow.taskman.ui.TimeChooser.TimeChooserListener;
import kaba4cow.taskman.ui.panels.tables.tasks.TaskTableFilter.SortingTarget;
import kaba4cow.taskman.utils.UiUtils;

public class TaskTableFilterPanel extends JPanel implements ItemListener, DocumentListener, TimeChooserListener {

	private static final long serialVersionUID = 1L;

	private final JTextField descriptionTextField;
	private final JCheckBox caseSensitiveCheckBox;
	private final JCheckBox typeCheckBox;
	private final JComboBox<String> typeComboBox;
	private final JCheckBox startTimeCheckBox;
	private final TimeChooser startTimeChooser;
	private final JCheckBox endTimeCheckBox;
	private final TimeChooser endTimeChooser;
	private final JComboBox<String> sortingComboBox;
	private final JCheckBox sortingReversedCheckBox;

	private final TaskTable table;
	private final TaskTableFilter filter;

	public TaskTableFilterPanel(TaskTable table, TaskTableFilter filter) {
		super();
		this.table = table;
		this.filter = filter;
		setLayout(new GridBagLayout());
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

		JPanel typePanel = UiUtils.createTitledPanel("Type Filter", 1, 1);
		{
			typeCheckBox = new JCheckBox("Enabled");
			typeCheckBox.setSelected(false);
			typeCheckBox.addItemListener(this);
			typePanel.add(typeCheckBox);
			typeComboBox = new JComboBox<>();
			typeComboBox.setEnabled(false);
			for (Task.Type type : Task.Type.values())
				typeComboBox.addItem(type.getName());
			typeComboBox.addItemListener(this);
			typePanel.add(typeComboBox);
		}
		add(typePanel, gbc);

		JPanel timePanel = UiUtils.createTitledPanel("Time Filter", 2, 2);
		{
			startTimeCheckBox = new JCheckBox("From");
			startTimeCheckBox.setSelected(false);
			startTimeCheckBox.addItemListener(this);
			timePanel.add(startTimeCheckBox);
			startTimeChooser = new TimeChooser();
			startTimeChooser.setTime(LocalTime.of(0, 0));
			startTimeChooser.setEnabled(false);
			startTimeChooser.addListener(this);
			timePanel.add(startTimeChooser);

			endTimeCheckBox = new JCheckBox("To");
			endTimeCheckBox.setSelected(false);
			endTimeCheckBox.addItemListener(this);
			timePanel.add(endTimeCheckBox);
			endTimeChooser = new TimeChooser();
			endTimeChooser.setTime(LocalTime.of(23, 0));
			endTimeChooser.setEnabled(false);
			endTimeChooser.addListener(this);
			timePanel.add(endTimeChooser);
		}
		add(timePanel, gbc);

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
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == caseSensitiveCheckBox)
			filter.setCaseSensitive(caseSensitiveCheckBox.isSelected());
		else if (event.getSource() == typeCheckBox) {
			typeComboBox.setEnabled(typeCheckBox.isSelected());
			filter.setType(typeComboBox.isEnabled() ? Task.Type.values()[typeComboBox.getSelectedIndex()] : null);
		} else if (event.getSource() == typeComboBox)
			filter.setType(Task.Type.values()[typeComboBox.getSelectedIndex()]);
		else if (event.getSource() == startTimeCheckBox) {
			startTimeChooser.setEnabled(startTimeCheckBox.isSelected());
			filter.setStartTime(startTimeChooser.isEnabled() ? startTimeChooser.getTime() : null);
		} else if (event.getSource() == endTimeCheckBox) {
			endTimeChooser.setEnabled(endTimeCheckBox.isSelected());
			filter.setEndTime(endTimeChooser.isEnabled() ? endTimeChooser.getTime() : null);
		} else if (event.getSource() == sortingComboBox)
			filter.setSortingTarget(SortingTarget.values()[sortingComboBox.getSelectedIndex()]);
		else if (event.getSource() == sortingReversedCheckBox)
			filter.setSortingReversed(sortingReversedCheckBox.isSelected());
		table.updateTable();
	}

	@Override
	public void onTimeChanged(TimeChooser source) {
		if (source == startTimeChooser)
			filter.setStartTime(startTimeChooser.getTime());
		else if (source == endTimeChooser)
			filter.setEndTime(endTimeChooser.getTime());
		table.updateTable();
	}

}
