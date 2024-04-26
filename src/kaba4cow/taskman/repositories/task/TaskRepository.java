package kaba4cow.taskman.repositories.task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.utils.ApplicationUtils;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class TaskRepository extends Repository<Task> {

	public TaskRepository() {
		super();
	}

	public Task createTask(Task.Type type, LocalDate date) {
		Task task = new Task();
		task.setDescription("New " + type.getName());
		task.addTime(LocalTime.of(12, 0));
		task.setDate(date);
		switch (type) {
		case DAILY:
			task.setIntervals(0, 0, 0, 1);
			break;
		case EVENT:
			task.setIntervals(0, 0, 0, 0);
			break;
		case REPEATING:
			task.setIntervals(0, 0, 1, 0);
			break;
		}
		createElement(task);
		return task;
	}

	public List<Task> getTasksAtDate(LocalDate date) {
		List<Task> list = new ArrayList<>();
		List<Task> all = getAll();
		for (Task task : all)
			if (task.isAtDate(date))
				list.add(task);
		return list;
	}

	public int getTotalTaskTimesAtDate(LocalDate date) {
		int total = 0;
		List<Task> all = getAll();
		for (Task task : all)
			if (task.isAtDate(date))
				total += task.getTotalTimes();
		return total;
	}

	public void deleteElement(Task task, LocalTime time) {
		if (task.removeTime(time))
			deleteElement(task);
		else
			updateElement(task);
	}

	@Override
	protected Task createNewElement(DataReader reader) throws IOException {
		return new Task(reader);
	}

	@Override
	protected DataReader getDataReader() throws IOException {
		return ApplicationUtils.getDataReader("tasks");
	}

	@Override
	protected DataWriter getDataWriter() throws IOException {
		return ApplicationUtils.getDataWriter("tasks");
	}

}
