package kaba4cow.taskman;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.utils.ApplicationUtils;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class ApplicationSettings {

	private final Map<Task.Type, Color> taskColors;
	private boolean notifications;

	private final List<ApplicationSettingsListener> listeners;

	public ApplicationSettings() {
		taskColors = new HashMap<>();
		try {
			DataReader reader = ApplicationUtils.getDataReader("settings");
			notifications = reader.readBoolean();
			int taskColorAmount = reader.readByte();
			for (int i = 0; i < taskColorAmount; i++) {
				String taskTypeValue = reader.readString();
				try {
					Task.Type taskType = Task.Type.valueOf(taskTypeValue);
					Color taskColor = reader.readColor();
					taskColors.put(taskType, taskColor);
				} catch (NullPointerException e) {
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			notifications = true;
		}
		listeners = new ArrayList<>();
	}

	private void save(DataWriter writer) throws IOException {
		writer.writeBoolean(notifications);
		writer.writeByte(Task.Type.values().length);
		for (Task.Type type : Task.Type.values()) {
			writer.writeString(type.toString());
			writer.writeColor(getTaskColor(type));
		}
	}

	public boolean isNotificationsEnabled() {
		return notifications;
	}

	public void setNotificationsEnabled(boolean enabled) {
		this.notifications = enabled;
	}

	public Color getTaskColor(Task.Type type) {
		if (!taskColors.containsKey(type))
			setTaskColor(type, type.getColor());
		return taskColors.get(type);
	}

	public void setTaskColor(Task.Type type, Color color) {
		taskColors.put(type, color);
	}

	public void updateSettings() {
		for (ApplicationSettingsListener listener : listeners)
			listener.onSettingsUpdated(this);
		try {
			DataWriter writer = ApplicationUtils.getDataWriter("settings");
			save(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addListener(ApplicationSettingsListener listener) {
		listeners.add(listener);
	}

	public static interface ApplicationSettingsListener {

		public void onSettingsUpdated(ApplicationSettings settings);

	}

}
