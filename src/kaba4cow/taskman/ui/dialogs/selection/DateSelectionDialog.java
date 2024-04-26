package kaba4cow.taskman.ui.dialogs.selection;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import kaba4cow.taskman.ui.panels.calendar.CalendarListener;
import kaba4cow.taskman.ui.panels.calendar.CalendarPanel;

public class DateSelectionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JButton saveButton;
	private final JButton cancelButton;

	private boolean option;
	private LocalDate selectedDate;

	public DateSelectionDialog(LocalDate initialDate) {
		super();
		setTitle("Select date");
		setLayout(new BorderLayout());

		option = false;
		selectedDate = initialDate;

		CalendarPanel calendarPanel = new CalendarPanel();
		calendarPanel.selectDate(initialDate);
		calendarPanel.addListener(new CalendarListener() {
			@Override
			public void onDateSelected(LocalDate date) {
				selectedDate = date;
			}
		});
		add(calendarPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 4, 4));
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				option = true;
				dispose();
			}
		});
		buttonsPanel.add(saveButton);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				option = false;
				dispose();
			}
		});
		buttonsPanel.add(cancelButton);
		add(buttonsPanel, BorderLayout.SOUTH);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		if (!option)
			selectedDate = initialDate;
	}

	public LocalDate getSelectedDate() {
		return selectedDate;
	}

}
