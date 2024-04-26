package kaba4cow.taskman.repositories.note;

import java.io.IOException;
import java.time.LocalDate;

import kaba4cow.taskman.repositories.RepositoryElement;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class Note implements RepositoryElement {

	private String description;
	private String tag;
	private final LocalDate date;

	public Note() {
		description = "";
		tag = "";
		date = LocalDate.now();
	}

	public Note(DataReader reader) throws IOException {
		description = reader.readString();
		tag = reader.readString();
		date = reader.readDate();
	}

	@Override
	public void saveData(DataWriter writer) throws IOException {
		writer.writeString(description);
		writer.writeString(tag);
		writer.writeDate(date);
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

	public LocalDate getDate() {
		return date;
	}

}
