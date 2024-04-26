package kaba4cow.taskman.ui.panels.tables.tasks;

import java.time.LocalTime;

import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.panels.tables.TableFilter;
import kaba4cow.taskman.ui.panels.tables.TableSorter;

public class TaskTableFilter extends TableFilter<TaskTableObject> {

	private String description;
	private boolean caseSensitive;

	private Task.Type type;
	private LocalTime startTime;
	private LocalTime endTime;

	public TaskTableFilter() {
		super();
		setSortingTarget(SortingTarget.TIME);
		setSortingReversed(false);
		description = "";
		caseSensitive = false;
		type = null;
		startTime = null;
		endTime = null;
	}

	public boolean filter(TaskTableObject task) {
		if (!description.trim().isEmpty()) {
			String taskDescription = caseSensitive ? task.task.getDescription()
					: task.task.getDescription().toLowerCase();
			String filterDescription = caseSensitive ? description : description.toLowerCase();
			if (!taskDescription.contains(filterDescription))
				return false;
		}
		if (type != null && task.task.getType() != type)
			return false;
		if (startTime != null && task.time.isBefore(startTime))
			return false;
		if (endTime != null && task.time.isAfter(endTime))
			return false;
		return true;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public void setType(Task.Type type) {
		this.type = type;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public static enum SortingTarget implements TableSorter<TaskTableObject> {

		TIME("Time") {
			@Override
			public int compare(TaskTableObject task1, TaskTableObject task2) {
				return task1.time.compareTo(task2.time);
			}
		}, //
		DESCRIPTION("Description") {
			@Override
			public int compare(TaskTableObject task1, TaskTableObject task2) {
				return task1.task.getDescription().compareTo(task2.task.getDescription());
			}
		}, //
		TYPE("Type") {
			@Override
			public int compare(TaskTableObject task1, TaskTableObject task2) {
				return task1.task.getType().getName().compareTo(task2.task.getType().getName());
			}
		};

		private final String name;

		private SortingTarget(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
