package kaba4cow.taskman.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.Repository;
import kaba4cow.taskman.repositories.RepositoryListener;
import kaba4cow.taskman.repositories.note.Note;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.repositories.todo.Todo;
import kaba4cow.taskman.ui.menus.EditMenu;
import kaba4cow.taskman.ui.menus.FileMenu;
import kaba4cow.taskman.ui.menus.HelpMenu;
import kaba4cow.taskman.ui.panels.ClockPanel;
import kaba4cow.taskman.ui.panels.calendar.CalendarListener;
import kaba4cow.taskman.ui.panels.calendar.CalendarPanel;
import kaba4cow.taskman.ui.panels.tables.notes.NoteTablePanel;
import kaba4cow.taskman.ui.panels.tables.tasks.TaskTablePanel;
import kaba4cow.taskman.ui.panels.tables.todos.TodoTablePanel;
import kaba4cow.taskman.utils.ApplicationUtils;

public class ApplicationWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public ApplicationWindow() {
		super();
		setTitle(Application.getTitle());
		setLayout(new BorderLayout(1, 1));

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new FileMenu());
		menuBar.add(new EditMenu());
		menuBar.add(new HelpMenu());
		setJMenuBar(menuBar);

		JPanel navigationPanel = new JPanel();
		navigationPanel.setLayout(new GridLayout(2, 1));
		{
			CalendarPanel calendarPanel = new CalendarPanel();
			calendarPanel.addListener(new CalendarListener() {
				@Override
				public void onDateSelected(LocalDate date) {
					Application.getCalendar().setDate(date);
				}
			});
			navigationPanel.add(calendarPanel);
			navigationPanel.add(new ClockPanel());
		}
		add(navigationPanel, BorderLayout.WEST);

		JTabbedPane tablesPanel = new JTabbedPane();
		{
			tablesPanel.add("Tasks", new TaskTablePanel());
			tablesPanel.add("Notes", new NoteTablePanel());
			tablesPanel.add("TODOs", new TodoTablePanel());
		}
		add(tablesPanel, BorderLayout.EAST);

		JLabel expandLabel = new JLabel("<");
		expandLabel.setHorizontalAlignment(JLabel.CENTER);
		expandLabel.setBorder(BorderFactory.createLineBorder(SystemColor.textInactiveText));
		expandLabel.setPreferredSize(new Dimension(10, 0));
		expandLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1) {
					tablesPanel.setVisible(!tablesPanel.isVisible());
					expandLabel.setText(tablesPanel.isVisible() ? "<" : ">");
					SwingUtilities.getWindowAncestor(expandLabel).pack();
				}
			}
		});
		add(expandLabel, BorderLayout.CENTER);

		pack();
		setFocusable(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setIconImage(ApplicationUtils.getApplicationIconImage());

		Application.getCalendar().setDate(LocalDate.now());
		Application.getTaskRepository().loadRepository();
		Application.getNoteRepository().loadRepository();
		Application.getTodoRepository().loadRepository();

		Application.getTaskRepository().addListener(new RepositoryListener<>() {
			@Override
			public void onRepositoryUpdated(Repository<Task> repository) {
				tablesPanel.setSelectedIndex(0);
			}
		});
		Application.getNoteRepository().addListener(new RepositoryListener<>() {
			@Override
			public void onRepositoryUpdated(Repository<Note> repository) {
				tablesPanel.setSelectedIndex(1);
			}
		});
		Application.getTodoRepository().addListener(new RepositoryListener<>() {
			@Override
			public void onRepositoryUpdated(Repository<Todo> repository) {
				tablesPanel.setSelectedIndex(2);
			}
		});
	}

}
