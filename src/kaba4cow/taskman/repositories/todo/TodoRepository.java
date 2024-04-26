package kaba4cow.taskman.repositories.todo;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.utils.ApplicationUtils;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class TodoRepository extends Repository<Todo> {

	public TodoRepository() {
		super();
	}

	public Todo createTodo() {
		Todo todo = new Todo();
		createElement(todo);
		return todo;
	}

	public Set<String> getAllTags() {
		Set<String> tags = new HashSet<>();
		List<Todo> all = getAll();
		for (Todo todo : all)
			tags.add(todo.getTag());
		return tags;
	}

	@Override
	protected Todo createNewElement(DataReader reader) throws IOException {
		return new Todo(reader);
	}

	@Override
	protected DataReader getDataReader() throws IOException {
		return ApplicationUtils.getDataReader("todos");
	}

	@Override
	protected DataWriter getDataWriter() throws IOException {
		return ApplicationUtils.getDataWriter("todos");
	}

}
