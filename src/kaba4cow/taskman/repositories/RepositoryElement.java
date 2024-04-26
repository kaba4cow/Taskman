package kaba4cow.taskman.repositories;

import java.io.IOException;

import kaba4cow.taskman.utils.io.DataWriter;

public interface RepositoryElement {

	public void saveData(DataWriter writer) throws IOException;

}
