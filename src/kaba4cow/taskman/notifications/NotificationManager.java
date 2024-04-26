package kaba4cow.taskman.notifications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.Timer;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.utils.DateUtils;

public final class NotificationManager {

	private NotificationManager() {
	}

	public static void init() {
		Timer timer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!Application.getSettings().isNotificationsEnabled())
					return;
				LocalDateTime currentDateTime = LocalDateTime.now();
				if (currentDateTime.getMinute() % 10 != 0)
					return;
				update(currentDateTime);
			}
		});
		timer.setInitialDelay(0);
		timer.start();
	}

	private static void update(LocalDateTime currentDateTime) {
		List<Task> allTasks = Application.getTaskRepository().getAll();
		StringBuilder notification = new StringBuilder();
		for (Task task : allTasks) {
			if (!task.isRemindEnabled())
				continue;
			LocalDate taskDate = task.getNextDate();
			long remindMinutes = task.getRemindTime().getMinute() + 60 * task.getRemindTime().getHour();
			List<LocalTime> taskTimes = task.getTimes();
			for (LocalTime taskTime : taskTimes) {
				LocalDateTime taskDateTime = LocalDateTime.of(taskDate, taskTime);
				int leftMinutes = (int) ChronoUnit.MINUTES.between(currentDateTime, taskDateTime)
						+ 60 * (int) ChronoUnit.HOURS.between(currentDateTime, taskDateTime);
				if (leftMinutes >= 0 && leftMinutes <= remindMinutes) {
					LocalTime leftTime = LocalTime.of(leftMinutes / 60, leftMinutes % 60);
					notification.append(String.format("%s left until \"%s\"\n", DateUtils.formatTime(leftTime),
							task.getDescription()));
					break;
				}
			}
		}
		if (notification.length() > 0)
			NotificationTray.showNotification(notification.toString());
	}

}
