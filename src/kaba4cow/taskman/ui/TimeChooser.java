package kaba4cow.taskman.ui;

import java.awt.GridLayout;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TimeChooser extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final List<TimeChooserListener> listeners;

	private final JSpinner hourSpinner;
	private final JSpinner minuteSpinner;

	public TimeChooser() {
		super();
		setLayout(new GridLayout(1, 2));
		listeners = new ArrayList<>();

		hourSpinner = new JSpinner(new SpinnerNumberModel(12, -1, 24, 1));
		hourSpinner.addChangeListener(this);
		add(hourSpinner);

		minuteSpinner = new JSpinner(new SpinnerNumberModel(0, -10, 60, 10));
		minuteSpinner.addChangeListener(this);
		add(minuteSpinner);
	}

	@Override
	public void setEnabled(boolean enabled) {
		hourSpinner.setEnabled(enabled);
		minuteSpinner.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	public void addListener(TimeChooserListener listener) {
		listeners.add(listener);
	}

	private void fireTimeChanged() {
		for (TimeChooserListener listener : listeners)
			listener.onTimeChanged(this);
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		Integer hour = (Integer) hourSpinner.getValue();
		Integer minute = (Integer) minuteSpinner.getValue();
		if (minute < 0) {
			hour--;
			minute = 50;
		} else if (minute >= 60) {
			hour++;
			minute = 0;
		}
		if (hour < 0)
			hour = 23;
		else if (hour > 23)
			hour = 0;
		minuteSpinner.setValue(minute);
		hourSpinner.setValue(hour);
		fireTimeChanged();
	}

	public void setTime(LocalTime time) {
		minuteSpinner.setValue(time.getMinute() - time.getMinute() % 10);
		hourSpinner.setValue(time.getHour());
	}

	public LocalTime getTime() {
		return LocalTime.of((int) hourSpinner.getValue(), (int) minuteSpinner.getValue());
	}

	public static interface TimeChooserListener {

		public void onTimeChanged(TimeChooser source);

	}

}
