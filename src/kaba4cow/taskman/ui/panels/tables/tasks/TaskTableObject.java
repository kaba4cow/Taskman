package kaba4cow.taskman.ui.panels.tables.tasks;

import java.time.LocalTime;

import kaba4cow.taskman.repositories.task.Task;

public class TaskTableObject {

	public final Task task;
	public final LocalTime time;

	public TaskTableObject(Task task, LocalTime time) {
		this.task = task;
		this.time = time;
	}

}