package kaba4cow.taskman.repositories.note;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.utils.ApplicationUtils;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class NoteRepository extends Repository<Note> {

	public NoteRepository() {
		super();
	}

	public Note createNote() {
		return createElement(new Note());
	}

	public Set<String> getAllTags() {
		Set<String> tags = new HashSet<>();
		List<Note> all = getAll();
		for (Note note : all)
			tags.add(note.getTag());
		return tags;
	}

	@Override
	protected Note createNewElement(DataReader reader) throws IOException {
		return new Note(reader);
	}

	@Override
	protected DataReader getDataReader() throws IOException {
		return ApplicationUtils.getDataReader("notes");
	}

	@Override
	protected DataWriter getDataWriter() throws IOException {
		return ApplicationUtils.getDataWriter("notes");
	}

}
