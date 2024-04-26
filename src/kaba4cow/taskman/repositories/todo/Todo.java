package kaba4cow.taskman.repositories.todo;

import java.io.IOException;

import kaba4cow.taskman.repositories.RepositoryElement;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class Todo implements RepositoryElement {

	private String description;
	private String tag;
	private Priority priority;
	private Status status;

	public Todo() {
		description = "";
		tag = "";
		priority = Priority.NORMAL;
		status = Status.NONE;
	}

	public Todo(DataReader reader) throws IOException {
		description = reader.readString();
		tag = reader.readString();
		priority = Priority.get(reader.readByte());
		status = Status.get(reader.readByte());
	}

	@Override
	public void saveData(DataWriter writer) throws IOException {
		writer.writeString(description);
		writer.writeString(tag);
		writer.writeByte(priority.ordinal());
		writer.writeByte(status.ordinal());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static enum Priority {

		HIGH("High"), NORMAL("Normal"), LOW("Low");

		private final String name;

		private Priority(String name) {
			this.name = name;
		}

		public static Priority get(int index) {
			Priority[] values = values();
			if (index >= 0 && index < values.length)
				return values[index];
			return NORMAL;
		}

		public String getName() {
			return name;
		}

	}

	public static enum Status {

		IN_PROGRESS("In Progress"), NONE("None"), FINISHED("Finished");

		private final String name;

		private Status(String name) {
			this.name = name;
		}

		public static Status get(int index) {
			Status[] values = values();
			if (index >= 0 && index < values.length)
				return values[index];
			return NONE;
		}

		public String getName() {
			return name;
		}

	}

}
