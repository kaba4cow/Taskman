package kaba4cow.taskman.ui.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.dialogs.selection.ColorSelectionDialog;
import kaba4cow.taskman.utils.UiUtils;

public class SettingsDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private boolean option = false;

	private final JCheckBox notificationsCheckBox;
	private final JButton[] taskColorButtons = new JButton[Task.Type.values().length];

	private final JButton saveButton;
	private final JButton cancelButton;

	public SettingsDialog() {
		super();
		setTitle("Settings");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		JPanel notificationsPanel = UiUtils.createTitledPanel("Notifications", 0, 1);
		{
			notificationsCheckBox = new JCheckBox("Enabled");
			notificationsCheckBox.setSelected(Application.getSettings().isNotificationsEnabled());
			notificationsCheckBox.setHorizontalTextPosition(JCheckBox.LEADING);
			notificationsPanel.add(notificationsCheckBox);
		}
		add(notificationsPanel, gbc);

		JPanel taskColorPanel = UiUtils.createTitledPanel("Task Colors", 0, 2);
		for (int i = 0; i < taskColorButtons.length; i++) {
			Task.Type type = Task.Type.values()[i];
			taskColorPanel.add(new JLabel(type.getName()));
			taskColorButtons[i] = new JButton();
			taskColorButtons[i].setBackground(Application.getSettings().getTaskColor(type));
			taskColorButtons[i].setPreferredSize(new Dimension(0, 20));
			taskColorButtons[i].addActionListener(this);
			taskColorPanel.add(taskColorButtons[i]);
			add(taskColorPanel);
		}
		add(taskColorPanel, gbc);

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
			Application.getSettings().setNotificationsEnabled(notificationsCheckBox.isSelected());
			for (int i = 0; i < taskColorButtons.length; i++) {
				Task.Type type = Task.Type.values()[i];
				Application.getSettings().setTaskColor(type, taskColorButtons[i].getBackground());
			}
			Application.getSettings().updateSettings();
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		for (int i = 0; i < taskColorButtons.length; i++)
			if (event.getSource() == taskColorButtons[i]) {
				Color newColor = new ColorSelectionDialog(Task.Type.values()[i].getName(),
						taskColorButtons[i].getBackground()).getSelectedColor();
				taskColorButtons[i].setBackground(newColor);
				return;
			}
		if (event.getSource() == saveButton) {
			option = true;
			dispose();
		} else if (event.getSource() == cancelButton) {
			option = false;
			dispose();
		}
	}

}
