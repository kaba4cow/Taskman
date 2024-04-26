package kaba4cow.taskman.ui.panels.calendar;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.ApplicationCalendar.ApplicationCalendarListener;
import kaba4cow.taskman.ApplicationClock.ApplicationClockListener;
import kaba4cow.taskman.ApplicationSettings;
import kaba4cow.taskman.ApplicationSettings.ApplicationSettingsListener;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.utils.DateUtils;

public class CalendarPanel extends JPanel
		implements ApplicationSettingsListener, ApplicationCalendarListener, RepositoryListener<Task> {

	private static final long serialVersionUID = 1L;

	private final List<CalendarListener> listeners = new ArrayList<>();

	private final JLabel monthLabel;
	private final DayLabel[] dayLabels;

	private LocalDate selectedDate;

	public CalendarPanel() {
		super(new BorderLayout());
		Application.getSettings().addListener(this);
		Application.getCalendar().addListener(this);
		Application.getTaskRepository().addListener(this);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		{
			JButton clockButton = new JButton(DateUtils.formatTitleTime(LocalDateTime.now()));
			clockButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					selectDate(LocalDate.now());
				}
			});
			Application.getClock().addListener(new ApplicationClockListener() {
				@Override
				public void onTimeUpdated() {
					clockButton.setText(DateUtils.formatTitleTime(LocalDateTime.now()));
				}
			});
			titlePanel.add(clockButton, BorderLayout.NORTH);

			monthLabel = new JLabel();
			monthLabel.setHorizontalAlignment(JLabel.CENTER);
			monthLabel.setVerticalAlignment(JLabel.CENTER);
			titlePanel.add(monthLabel, BorderLayout.CENTER);

			JButton prevMonthButton = new JButton("<");
			prevMonthButton.setMargin(new Insets(1, 1, 1, 1));
			prevMonthButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					selectDate(selectedDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
				}
			});
			titlePanel.add(prevMonthButton, BorderLayout.WEST);

			JButton nextMonthButton = new JButton(">");
			nextMonthButton.setMargin(new Insets(1, 1, 1, 1));
			nextMonthButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					selectDate(selectedDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()));
				}
			});
			titlePanel.add(nextMonthButton, BorderLayout.EAST);
		}
		add(titlePanel, BorderLayout.NORTH);

		JPanel daysPanel = new JPanel();
		daysPanel.setLayout(new GridLayout(DateUtils.getNumberOfWeeks() + 1, DateUtils.getNumberOfDays()));
		daysPanel.setBorder(BorderFactory.createLineBorder(SystemColor.textInactiveText));
		{
			for (int i = 0; i < DateUtils.getNumberOfDays(); i++) {
				JLabel weekDayLabel = new JLabel(DateUtils.getDayName(i));
				weekDayLabel.setHorizontalAlignment(JLabel.CENTER);
				weekDayLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColor.textText));
				daysPanel.add(weekDayLabel);
			}
			dayLabels = new DayLabel[DateUtils.getNumberOfWeeks() * DateUtils.getNumberOfDays()];
			for (int i = 0; i < dayLabels.length; i++) {
				dayLabels[i] = new DayLabel(this);
				daysPanel.add(dayLabels[i]);
			}
		}
		add(daysPanel, BorderLayout.CENTER);

		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				switch (event.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					selectDate(selectedDate.minusDays(1));
					return;
				case KeyEvent.VK_RIGHT:
					selectDate(selectedDate.plusDays(1));
					return;
				case KeyEvent.VK_UP:
					selectDate(selectedDate.minusWeeks(1));
					return;
				case KeyEvent.VK_DOWN:
					selectDate(selectedDate.plusWeeks(1));
					return;
				}
			}
		});
	}

	@Override
	public void onRepositoryUpdated(Repository<Task> repository) {
		selectDate(Application.getCalendar().getDate());
	}

	@Override
	public void onDateChanged(LocalDate newDate) {
		if (!newDate.equals(selectedDate))
			selectDate(newDate);
	}

	@Override
	public void onSettingsUpdated(ApplicationSettings settings) {
		repaint();
	}

	public void selectDate(LocalDate date) {
		monthLabel.setText(DateUtils.formatMonthYearDate(date));
		LocalDate start = date//
				.with(TemporalAdjusters.firstDayOfMonth())//
				.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
		LocalDate end = date//
				.with(TemporalAdjusters.lastDayOfMonth())//
				.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		end = end.plusWeeks(DateUtils.getNumberOfWeeks() - ChronoUnit.DAYS.between(start, end) / 7 - 1).plusDays(1);
		Object[] dates = start.datesUntil(end).toArray();
		for (int i = 0; i < dates.length; i++)
			dayLabels[i].selectDate(date, (LocalDate) dates[i]);
		requestFocus();
		boolean update = !date.equals(selectedDate);
		selectedDate = date;
		if (update)
			for (CalendarListener listener : listeners)
				listener.onDateSelected(date);
	}

	public void addListener(CalendarListener listener) {
		listeners.add(listener);
	}

}
