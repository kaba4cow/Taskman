package kaba4cow.taskman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import kaba4cow.taskman.notifications.NotificationManager;
import kaba4cow.taskman.notifications.NotificationTray;
import kaba4cow.taskman.repositories.note.NoteRepository;
import kaba4cow.taskman.repositories.task.TaskRepository;
import kaba4cow.taskman.repositories.todo.TodoRepository;
import kaba4cow.taskman.ui.ApplicationWindow;
import kaba4cow.taskman.ui.SplashScreen;

public class Application {

	private static final String TITLE = "Taskman";

	private static Application instance;

	private static final ApplicationSettings settings = new ApplicationSettings();
	private static final ApplicationCalendar calendar = new ApplicationCalendar();
	private static final ApplicationClock clock = new ApplicationClock();
	private static final TaskRepository taskRepository = new TaskRepository();
	private static final NoteRepository noteRepository = new NoteRepository();
	private static final TodoRepository todoRepository = new TodoRepository();
	private final ApplicationWindow applicationWindow;

	private Application() {
		applicationWindow = new ApplicationWindow();
	}

	public static void showWindow() {
		instance.applicationWindow.setVisible(true);
		instance.applicationWindow.setExtendedState(JFrame.NORMAL);
		instance.applicationWindow.toFront();
		instance.applicationWindow.requestFocus();
	}

	public static void hideWindow() {
		instance.applicationWindow.setVisible(false);
	}

	public static boolean isOnScreen() {
		return instance.applicationWindow.isFocused();
	}

	public static void closeProgram() {
		NotificationTray.remove();
		System.exit(0);
	}

	public static String getTitle() {
		return TITLE;
	}

	public static ApplicationSettings getSettings() {
		return settings;
	}

	public static ApplicationCalendar getCalendar() {
		return calendar;
	}

	public static ApplicationClock getClock() {
		return clock;
	}

	public static TaskRepository getTaskRepository() {
		return taskRepository;
	}

	public static NoteRepository getNoteRepository() {
		return noteRepository;
	}

	public static TodoRepository getTodoRepository() {
		return todoRepository;
	}

	public static ApplicationWindow getApplicationWindow() {
		return instance.applicationWindow;
	}

	private static boolean canRunApplication() {
		int port = 50710;
		try {
			new Socket("localhost", port).close();
			return false;
		} catch (IOException e1) {
			try {
				ServerSocket server = new ServerSocket(port);
				new Thread("Instance Lock") {
					@Override
					public void run() {
						while (true)
							try {
								server.accept().close();
								showWindow();
							} catch (IOException e) {
							}
					}
				}.start();
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						try {
							server.close();
						} catch (IOException e) {
						}
					}
				});
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return true;
		}
	}

	public static void main(String[] args) throws Exception {
		if (canRunApplication()) {
			SplashScreen splashScreen = new SplashScreen();
			instance = new Application();
			NotificationTray.init();
			NotificationManager.init();
			splashScreen.dispose();
			showWindow();
		}
	}

}
