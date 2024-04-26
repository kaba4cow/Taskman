package kaba4cow.taskman.repositories;

public interface RepositoryListener<E extends RepositoryElement> {

	public void onRepositoryUpdated(Repository<E> repository);

}
