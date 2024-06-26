package kaba4cow.taskman.ui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.ui.dialogs.edit.NoteEditDialog;
import kaba4cow.taskman.ui.dialogs.edit.TaskEditDialog;
import kaba4cow.taskman.ui.dialogs.edit.TodoEditDialog;

public class EditMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	public EditMenu() {
		super("Edit");

		JMenuItem addTaskItem = new JMenuItem("Add New Task");
		addTaskItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Task task = Application.getTaskRepository().createTask(Application.getCalendar().getDate());
				new TaskEditDialog(task, task.getTimes().get(0), true);
			}
		});
		add(addTaskItem);

		JMenuItem addNoteItem = new JMenuItem("Add New Note");
		addNoteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new NoteEditDialog(Application.getNoteRepository().createNote(), true);
			}
		});
		add(addNoteItem);

		JMenuItem addTodoItem = new JMenuItem("Add New TODO");
		addTodoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new TodoEditDialog(Application.getTodoRepository().createTodo(), true);
			}
		});
		add(addTodoItem);
	}

}
