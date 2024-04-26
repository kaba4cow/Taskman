package kaba4cow.taskman;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationCalendar {

	private LocalDate date;

	private final List<ApplicationCalendarListener> listeners;

	public ApplicationCalendar() {
		date = LocalDate.now();
		listeners = new ArrayList<>();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
		fireDateChanged();
	}

	private void fireDateChanged() {
		for (ApplicationCalendarListener listener : listeners)
			listener.onDateChanged(date);
	}

	public void addListener(ApplicationCalendarListener listener) {
		listeners.add(listener);
	}

	public static interface ApplicationCalendarListener {

		public void onDateChanged(LocalDate newDate);

	}

}
