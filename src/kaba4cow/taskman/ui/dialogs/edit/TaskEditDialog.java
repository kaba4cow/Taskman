package kaba4cow.taskman.ui.dialogs.edit;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.TimeChooser;
import kaba4cow.taskman.ui.dialogs.selection.DateSelectionDialog;
import kaba4cow.taskman.utils.DateUtils;
import kaba4cow.taskman.utils.UiUtils;

public class TaskEditDialog extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private static final String[] PERIOD_NAMES = { "Years:", "Months:", "Weeks:", "Days:" };

	private boolean option = false;

	private LocalDate selectedDate;

	private final JTextField descriptionTextField;
	private final JButton dateButton;
	private final JCheckBox addTimeCheckBox;
	private final TimeChooser timeChooser;
	private final JCheckBox repeatCheckBox;
	private final JLabel[] intervalLabels = new JLabel[4];
	private final JSpinner[] intervalSpinners = new JSpinner[4];
	private final JCheckBox notifyRemindCheckBox;
	private final TimeChooser notifyRemindTimeChooser;
	private final JButton saveButton;
	private final JButton cancelButton;

	public TaskEditDialog(Task task, LocalTime time, boolean createTask) {
		super();
		if (createTask)
			setTitle("Add New Task");
		else
			setTitle("Edit Task - " + task.getDescription());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		selectedDate = task.getDate();

		JPanel descriptionPanel = UiUtils.createTitledPanel("Description", 1, 1);
		{
			descriptionTextField = new JTextField(task.getDescription(), 30);
			descriptionPanel.add(descriptionTextField);
		}
		add(descriptionPanel, gbc);

		JPanel datePanel = UiUtils.createTitledPanel("Date", 1, 1);
		{
			dateButton = new JButton(DateUtils.formatDate(selectedDate));
			dateButton.addActionListener(this);
			datePanel.add(dateButton);
		}
		add(datePanel, gbc);

		JPanel timePanel = UiUtils.createTitledPanel("Time", 1, 2);
		{
			addTimeCheckBox = new JCheckBox("Set/Add Time");
			addTimeCheckBox.setHorizontalAlignment(JCheckBox.RIGHT);
			timePanel.add(addTimeCheckBox);
			timeChooser = new TimeChooser();
			timeChooser.setTime(time);
			timePanel.add(timeChooser);
		}
		add(timePanel, gbc);

		JPanel repeatPanel = UiUtils.createTitledPanel("Repeat", 0, 1);
		{
			repeatCheckBox = new JCheckBox("Repeat");
			repeatCheckBox.setHorizontalTextPosition(JCheckBox.LEADING);
			repeatCheckBox.setSelected(task.getType() != Task.Type.EVENT);
			repeatCheckBox.addItemListener(this);
			repeatPanel.add(repeatCheckBox);

			for (int i = 0; i < 4; i++) {
				JPanel periodPanel = new JPanel();
				periodPanel.setLayout(new GridLayout(1, 2));
				intervalLabels[i] = new JLabel(PERIOD_NAMES[i]);
				intervalLabels[i].setEnabled(repeatCheckBox.isSelected());
				periodPanel.add(intervalLabels[i], BorderLayout.WEST);
				periodPanel.add(intervalSpinners[i] = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1)));
				intervalSpinners[i].setValue(task.getInterval(i));
				intervalSpinners[i].setEnabled(repeatCheckBox.isSelected());
				repeatPanel.add(periodPanel);
			}
		}
		add(repeatPanel, gbc);

		JPanel notifyPanel = UiUtils.createTitledPanel("Notifications", 0, 2);
		{
			notifyRemindCheckBox = new JCheckBox("Remind");
			notifyRemindCheckBox.setHorizontalAlignment(JCheckBox.RIGHT);
			notifyRemindCheckBox.setSelected(task.isRemindEnabled());
			notifyRemindCheckBox.setEnabled(Application.getSettings().isNotificationsEnabled());
			notifyRemindCheckBox.addItemListener(this);
			notifyPanel.add(notifyRemindCheckBox);

			notifyRemindTimeChooser = new TimeChooser();
			notifyRemindTimeChooser.setTime(task.getRemindTime());
			notifyRemindTimeChooser
					.setEnabled(Application.getSettings().isNotificationsEnabled() && task.isRemindEnabled());
			notifyPanel.add(notifyRemindTimeChooser);
		}
		add(notifyPanel, gbc);

		JPanel buttonsPanel = UiUtils.createTitledPanel("", 0, 2, 4);
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
			task.setDescription(descriptionTextField.getText());
			task.setDate(selectedDate);
			if (!addTimeCheckBox.isSelected())
				task.removeTime(time);
			task.addTime(timeChooser.getTime());
			if (repeatCheckBox.isSelected())
				task.setIntervals((Integer) intervalSpinners[0].getValue(), (Integer) intervalSpinners[1].getValue(),
						(Integer) intervalSpinners[2].getValue(), (Integer) intervalSpinners[3].getValue());
			else
				task.setIntervals(0, 0, 0, 0);
			task.setRemindEnabled(notifyRemindCheckBox.isSelected());
			if (notifyRemindCheckBox.isSelected())
				task.setRemindTime(notifyRemindTimeChooser.getTime());
			Application.getCalendar().setDate(task.getNextDate());
			Application.getTaskRepository().updateElement(task);
		} else if (createTask)
			Application.getTaskRepository().deleteElement(task, time);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == dateButton) {
			selectedDate = new DateSelectionDialog(selectedDate).getSelectedDate();
			dateButton.setText(DateUtils.formatDate(selectedDate));
		} else if (event.getSource() == saveButton) {
			option = true;
			dispose();
		} else if (event.getSource() == cancelButton) {
			option = false;
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == repeatCheckBox)
			for (int i = 0; i < intervalLabels.length; i++) {
				intervalLabels[i].setEnabled(repeatCheckBox.isSelected());
				intervalSpinners[i].setEnabled(repeatCheckBox.isSelected());
			}
		else if (event.getSource() == notifyRemindCheckBox)
			notifyRemindTimeChooser.setEnabled(notifyRemindCheckBox.isSelected());
	}

}
