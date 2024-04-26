package kaba4cow.taskman.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public abstract class Repository<E extends RepositoryElement> {

	private final List<E> data;
	private final List<RepositoryListener<E>> listeners;

	protected Repository() {
		data = new ArrayList<>();
		listeners = new ArrayList<>();
	}

	public List<E> getAll() {
		synchronized (data) {
			return new ArrayList<>(data);
		}
	}

	protected void addAll(List<E> list) {
		data.addAll(list);
		onRepositoryUpdated();
	}

	protected E createElement(E element) {
		if (element != null) {
			data.add(element);
			onRepositoryUpdated();
			return element;
		}
		return null;
	}

	public void updateElement(E element) {
		if (element != null)
			onRepositoryUpdated();
	}

	public void deleteElement(E element) {
		if (element != null) {
			data.remove(element);
			onRepositoryUpdated();
		}
	}

	private void onRepositoryUpdated() {
		for (RepositoryListener<E> listener : listeners)
			listener.onRepositoryUpdated(this);
		writeData();
	}

	public void addListener(RepositoryListener<E> listener) {
		listeners.add(listener);
	}

	public void removeListener(Object listener) {
		listeners.remove(listener);
	}

	protected abstract E createNewElement(DataReader reader) throws IOException;

	public void loadRepository() {
		try {
			DataReader reader = getDataReader();
			int amount = reader.readInt();
			List<E> list = new ArrayList<>();
			for (int i = 0; i < amount; i++)
				list.add(createNewElement(reader));
			reader.close();
			addAll(list);
		} catch (IOException e) {
		}
	}

	protected abstract DataReader getDataReader() throws IOException;

	private void writeData() {
		try {
			DataWriter writer = getDataWriter();
			writer.writeInt(data.size());
			for (E element : data)
				element.saveData(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract DataWriter getDataWriter() throws IOException;

}
