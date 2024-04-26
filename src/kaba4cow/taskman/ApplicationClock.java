package kaba4cow.taskman;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class ApplicationClock implements ActionListener {

	private final List<ApplicationClockListener> listeners;

	private int prevSecond;

	public ApplicationClock() {
		listeners = new ArrayList<>();
		prevSecond = 0;
		Timer timer = new Timer(500, this);
		timer.setInitialDelay(0);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int second = LocalTime.now().getSecond();
		if (second != prevSecond) {
			prevSecond = second;
			fireTimeUpdated();
		}
	}

	private void fireTimeUpdated() {
		for (ApplicationClockListener listener : listeners)
			listener.onTimeUpdated();
	}

	public void addListener(ApplicationClockListener listener) {
		listeners.add(listener);
	}

	public static interface ApplicationClockListener {

		public void onTimeUpdated();

	}

}
