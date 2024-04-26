package kaba4cow.taskman.ui.panels;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.SystemColor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import kaba4cow.taskman.ApplicationCalendar.ApplicationCalendarListener;
import kaba4cow.taskman.ApplicationClock.ApplicationClockListener;
import kaba4cow.taskman.ApplicationSettings.ApplicationSettingsListener;
import kaba4cow.taskman.Application;
import kaba4cow.taskman.ApplicationSettings;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.task.Task;

public class ClockPanel extends JPanel implements ApplicationClockListener, ApplicationCalendarListener,
		ApplicationSettingsListener, RepositoryListener<Task> {

	private static final long serialVersionUID = 1L;

	private final Map<LocalTime, Set<Task.Type>> markers;

	public ClockPanel() {
		super();
		Application.getClock().addListener(this);
		Application.getCalendar().addListener(this);
		Application.getSettings().addListener(this);
		Application.getTaskRepository().addListener(this);
		setPreferredSize(new Dimension(160, 160));
		markers = new HashMap<>();
	}

	@Override
	public void onTimeUpdated() {
		updateMarkers();
	}

	@Override
	public void onRepositoryUpdated(Repository<Task> repository) {
		updateMarkers();
	}

	@Override
	public void onSettingsUpdated(ApplicationSettings settings) {
		repaint();
	}

	@Override
	public void onDateChanged(LocalDate newDate) {
		updateMarkers();
	}

	private void updateMarkers() {
		List<Task> tasks = Application.getTaskRepository()
				.getTasksAtDate(Application.getCalendar().getDate());
		markers.clear();
		LocalDateTime currentDateTime = LocalDateTime.of(Application.getCalendar().getDate(), LocalTime.now());
		LocalDateTime nextDateTime = currentDateTime.plusHours(12);
		for (Task task : tasks) {
			List<LocalTime> times = task.getTimes();
			for (LocalTime time : times) {
				LocalDateTime dateTime = LocalDateTime.of(task.getNextDate(), time);
				if (!dateTime.isBefore(currentDateTime) && !dateTime.isAfter(nextDateTime))
					getTimeMarkers(time).add(task.getType());
			}
		}
		repaint();
	}

	private Set<Task.Type> getTimeMarkers(LocalTime time) {
		if (!markers.containsKey(time))
			markers.put(time, new HashSet<>());
		return markers.get(time);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D) g;

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		double hourDistance = 0.9d;
		double markerDistance = 0.9d * hourDistance;
		double hourArrowRadius = 0.5d * hourDistance;
		double minuteArrowRadius = 0.7d * hourDistance;
		double secondArrowRadius = 0.9d * hourDistance;

		double delta12 = 1d / 12d;
		double delta60 = 1d / 60d;

		graphics.setStroke(new BasicStroke(1f));
		int markerRadius = 4;
		for (LocalTime markerTime : markers.keySet()) {
			Set<Task.Type> markerTasks = markers.get(markerTime);
			if (!markerTasks.isEmpty()) {
				double angle = calculateAngle(
						markerTime.getHour() * delta12 + markerTime.getMinute() * delta12 * delta60);
				double dirX = Math.cos(angle);
				double dirY = Math.sin(angle);
				double markerX = centerX + (markerDistance * centerX - markerRadius) * dirX;
				double markerY = centerY + (markerDistance * centerY - markerRadius) * dirY;
				for (Task.Type task : markerTasks) {
					int x = (int) markerX;
					int y = (int) markerY;
					graphics.setColor(Application.getSettings().getTaskColor(task));
					graphics.fillRect(x - markerRadius / 2, y - markerRadius / 2, markerRadius, markerRadius);
					graphics.setColor(SystemColor.textText);
					graphics.drawOval(x - markerRadius / 2, y - markerRadius / 2, markerRadius, markerRadius);
					markerX -= 2d * markerRadius * dirX;
					markerY -= 2d * markerRadius * dirY;
				}
			}
		}

		for (int minute = 0; minute < 60; minute++)
			if (minute % 5 != 0) {
				double angle = calculateAngle(minute * delta60);
				int x = centerX + (int) (hourDistance * centerX * Math.cos(angle));
				int y = centerY + (int) (hourDistance * centerY * Math.sin(angle));
				graphics.setColor(SystemColor.textInactiveText);
				graphics.fillOval(x, y, 3, 3);
			} else {
				int hour = (minute / 5) + 1;
				double angle = calculateAngle(hour * delta12);
				int x = (int) (hourDistance * centerX * Math.cos(angle));
				int y = (int) (hourDistance * centerY * Math.sin(angle));
				graphics.setColor(SystemColor.textText);
				drawCenteredString(graphics, Integer.toString(hour), centerX + x, centerY + y);
			}

		LocalTime currentTime = LocalTime.now();
		double secondValue = currentTime.getSecond() * delta60;
		double minuteValue = (currentTime.getMinute() + secondValue) * delta60;
		double hourValue = (currentTime.getHour() + minuteValue) * delta12;
		graphics.setColor(SystemColor.textText);
		{
			double angle = calculateAngle(secondValue);
			int x = (int) (secondArrowRadius * centerX * Math.cos(angle));
			int y = (int) (secondArrowRadius * centerY * Math.sin(angle));
			graphics.setStroke(new BasicStroke(1f));
			graphics.drawLine(centerX, centerY, centerX + x, centerY + y);
		}
		{
			double angle = calculateAngle(minuteValue);
			int x = (int) (minuteArrowRadius * centerX * Math.cos(angle));
			int y = (int) (minuteArrowRadius * centerY * Math.sin(angle));
			graphics.setStroke(new BasicStroke(1.5f));
			graphics.drawLine(centerX, centerY, centerX + x, centerY + y);
		}
		{
			double angle = calculateAngle(hourValue);
			int x = (int) (hourArrowRadius * centerX * Math.cos(angle));
			int y = (int) (hourArrowRadius * centerY * Math.sin(angle));
			graphics.setStroke(new BasicStroke(2f));
			graphics.drawLine(centerX, centerY, centerX + x, centerY + y);
		}
		graphics.fillOval(centerX - 3, centerY - 3, 6, 6);
	}

	private double calculateAngle(double value) {
		return 2d * Math.PI * value - 0.5d * Math.PI;
	}

	public void drawCenteredString(Graphics g, String text, int textX, int textY) {
		FontMetrics metrics = g.getFontMetrics();
		int x = textX - metrics.stringWidth(text) / 2;
		int y = textY - metrics.getHeight() / 2 + metrics.getAscent();
		g.drawString(text, x, y);
	}

}
