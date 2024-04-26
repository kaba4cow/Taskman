package kaba4cow.taskman.ui.panels.tables.notes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.JButton;
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
import kaba4cow.taskman.repositories.note.Note;
import kaba4cow.taskman.ui.dialogs.selection.DateSelectionDialog;
import kaba4cow.taskman.ui.panels.tables.notes.NoteTableFilter.SortingTarget;
import kaba4cow.taskman.utils.DateUtils;
import kaba4cow.taskman.utils.UiUtils;

public class NoteTableFilterPanel extends JPanel
		implements ActionListener, ItemListener, DocumentListener, RepositoryListener<Note> {

	private static final long serialVersionUID = 1L;

	private final JTextField descriptionTextField;
	private final JCheckBox caseSensitiveCheckBox;
	private final JCheckBox tagCheckBox;
	private final JComboBox<String> tagComboBox;
	private final JCheckBox startDateCheckBox;
	private final JButton startDateButton;
	private final JCheckBox endDateCheckBox;
	private final JButton endDateButton;
	private final JComboBox<String> sortingComboBox;
	private final JCheckBox sortingReversedCheckBox;

	private final NoteTable table;
	private final NoteTableFilter filter;

	private LocalDate selectedStartDate;
	private LocalDate selectedEndDate;

	public NoteTableFilterPanel(NoteTable table, NoteTableFilter filter) {
		super();
		this.table = table;
		this.filter = filter;
		setLayout(new GridBagLayout());
		Application.getNoteRepository().addListener(this);
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		selectedEndDate = LocalDate.now();
		selectedStartDate = selectedEndDate.minusMonths(1);

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

		JPanel datePanel = UiUtils.createTitledPanel("Date Filter", 2, 2);
		{
			startDateCheckBox = new JCheckBox("From");
			startDateCheckBox.setSelected(false);
			startDateCheckBox.addItemListener(this);
			datePanel.add(startDateCheckBox);
			startDateButton = new JButton(DateUtils.formatDate(LocalDate.now().minusMonths(1)));
			startDateButton.setEnabled(false);
			startDateButton.addActionListener(this);
			datePanel.add(startDateButton);

			endDateCheckBox = new JCheckBox("To");
			endDateCheckBox.setSelected(false);
			endDateCheckBox.addItemListener(this);
			datePanel.add(endDateCheckBox);
			endDateButton = new JButton(DateUtils.formatDate(LocalDate.now()));
			endDateButton.setEnabled(false);
			endDateButton.addActionListener(this);
			datePanel.add(endDateButton);
		}
		add(datePanel, gbc);

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

		else if (event.getSource() == startDateCheckBox) {
			startDateButton.setEnabled(startDateCheckBox.isSelected());
			filter.setStartDate(startDateCheckBox.isSelected() ? selectedStartDate : null);
		} else if (event.getSource() == endDateCheckBox) {
			endDateButton.setEnabled(endDateCheckBox.isSelected());
			filter.setEndDate(endDateCheckBox.isSelected() ? selectedEndDate : null);

		} else if (event.getSource() == sortingComboBox)
			filter.setSortingTarget(SortingTarget.values()[sortingComboBox.getSelectedIndex()]);
		else if (event.getSource() == sortingReversedCheckBox)
			filter.setSortingReversed(sortingReversedCheckBox.isSelected());
		table.updateTable();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == startDateButton) {
			selectedStartDate = new DateSelectionDialog(selectedStartDate).getSelectedDate();
			startDateButton.setText(DateUtils.formatDate(selectedStartDate));
			filter.setStartDate(selectedStartDate);
		} else if (event.getSource() == endDateButton) {
			selectedEndDate = new DateSelectionDialog(selectedEndDate).getSelectedDate();
			endDateButton.setText(DateUtils.formatDate(selectedEndDate));
			filter.setEndDate(selectedEndDate);
		}
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
	public void onRepositoryUpdated(Repository<Note> repository) {
		fillTagComboBox();
	}

	private void fillTagComboBox() {
		tagComboBox.removeAllItems();
		Set<String> tagSet = Application.getNoteRepository().getAllTags();
		for (String tag : tagSet)
			tagComboBox.addItem(tag);
		filter.setTag(null);
		table.updateTable();
	}

}
