package kaba4cow.taskman.ui.dialogs.edit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.todo.Todo;
import kaba4cow.taskman.utils.UiUtils;

public class TodoEditDialog extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private boolean option = false;

	private final JTextField descriptionTextField;
	private final JTextField tagTextField;
	private final JComboBox<String> tagComboBox;
	private final JComboBox<String> priorityComboBox;
	private final JComboBox<String> statusComboBox;

	private final JButton saveButton;
	private final JButton cancelButton;

	public TodoEditDialog(Todo todo, boolean createTodo) {
		super();
		if (createTodo)
			setTitle("Add New TODO");
		else
			setTitle("Edit TODO - " + todo.getDescription());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		JPanel descriptionPanel = UiUtils.createTitledPanel("Description", 1, 1);
		{
			descriptionTextField = new JTextField(todo.getDescription(), 30);
			descriptionPanel.add(descriptionTextField);
		}
		add(descriptionPanel, gbc);

		JPanel tagPanel = UiUtils.createTitledPanel("Tag", 3, 1);
		{
			tagTextField = new JTextField(todo.getTag());
			tagPanel.add(tagTextField);

			tagPanel.add(new JLabel("All Tags:"));
			tagComboBox = new JComboBox<>();
			tagComboBox.removeAllItems();
			Set<String> tagSet = Application.getTodoRepository().getAllTags();
			for (String tag : tagSet)
				tagComboBox.addItem(tag);
			tagComboBox.addItemListener(this);
			tagComboBox.addActionListener(this);
			tagPanel.add(tagComboBox);
		}
		add(tagPanel, gbc);

		JPanel priorityPanel = UiUtils.createTitledPanel("Priority", 1, 1);
		{
			priorityComboBox = new JComboBox<>();
			for (Todo.Priority priority : Todo.Priority.values())
				priorityComboBox.addItem(priority.getName());
			priorityComboBox.setSelectedIndex(todo.getPriority().ordinal());
			priorityPanel.add(priorityComboBox);
		}
		add(priorityPanel, gbc);

		JPanel statusPanel = UiUtils.createTitledPanel("Status", 1, 1);
		{
			statusComboBox = new JComboBox<>();
			for (Todo.Status status : Todo.Status.values())
				statusComboBox.addItem(status.getName());
			statusComboBox.setSelectedIndex(todo.getStatus().ordinal());
			statusPanel.add(statusComboBox);
		}
		add(statusPanel, gbc);

		JPanel buttonsPanel = UiUtils.createTitledPanel("", 1, 2, 4);
		{
			saveButton = new JButton("Save");
			saveButton.addActionListener(this);
			buttonsPanel.add(saveButton);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);
			buttonsPanel.add(cancelButton);
		}
		add(buttonsPanel, gbc);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		if (option) {
			todo.setDescription(descriptionTextField.getText());
			todo.setTag(tagTextField.getText());
			todo.setPriority(Todo.Priority.get(priorityComboBox.getSelectedIndex()));
			todo.setStatus(Todo.Status.get(statusComboBox.getSelectedIndex()));
			Application.getTodoRepository().updateElement(todo);
		} else if (createTodo)
			Application.getTodoRepository().deleteElement(todo);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveButton) {
			option = true;
			dispose();
		} else if (event.getSource() == cancelButton) {
			option = false;
			dispose();
		} else if (event.getSource() == tagComboBox)
			tagTextField.setText((String) tagComboBox.getSelectedItem());
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == tagComboBox)
			tagTextField.setText((String) tagComboBox.getSelectedItem());
	}

}
