package kaba4cow.taskman.ui.panels.tables.todos;

import kaba4cow.taskman.repositories.todo.Todo;
import kaba4cow.taskman.ui.panels.tables.TableFilter;
import kaba4cow.taskman.ui.panels.tables.TableSorter;

public class TodoTableFilter extends TableFilter<Todo> {

	private String description;
	private boolean caseSensitive;
	private String tag;
	private Todo.Priority priority;
	private Todo.Status status;

	public TodoTableFilter() {
		super();
		setSortingTarget(SortingTarget.DESCRIPTION);
		setSortingReversed(false);
		description = "";
		caseSensitive = false;
		tag = null;
		priority = null;
		status = null;
	}

	public boolean filter(Todo todo) {
		if (!description.trim().isEmpty()) {
			String todoDescription = caseSensitive ? todo.getDescription() : todo.getDescription().toLowerCase();
			String filterDescription = caseSensitive ? description : description.toLowerCase();
			if (!todoDescription.contains(filterDescription))
				return false;
		}
		if (tag != null && !todo.getTag().equals(tag))
			return false;
		if (priority != null && todo.getPriority() != priority)
			return false;
		if (status != null && todo.getStatus() != status)
			return false;
		return true;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setPriority(Todo.Priority priority) {
		this.priority = priority;
	}

	public void setStatus(Todo.Status status) {
		this.status = status;
	}

	public static enum SortingTarget implements TableSorter<Todo> {

		DESCRIPTION("Description") {
			@Override
			public int compare(Todo todo1, Todo todo2) {
				return todo1.getDescription().compareTo(todo2.getDescription());
			}
		}, //
		TAG("Tag") {
			@Override
			public int compare(Todo todo1, Todo todo2) {
				return todo1.getTag().compareTo(todo2.getTag());
			}
		}, //
		PRIORITY("Priority") {
			@Override
			public int compare(Todo todo1, Todo todo2) {
				return todo1.getPriority().compareTo(todo2.getPriority());
			}
		}, //
		STATUS("Status") {
			@Override
			public int compare(Todo todo1, Todo todo2) {
				return todo1.getStatus().compareTo(todo2.getStatus());
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
