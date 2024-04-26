package kaba4cow.taskman.ui.panels.tables;

public interface UserTable<E> {

	public void updateTable();

	public void addNewItem();

	public E getSelectedItem();

	public void editSelectedItem();

	public void deleteSelectedItem();

}
