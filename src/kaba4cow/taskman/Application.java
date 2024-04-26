package kaba4cow.taskman;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;

import javax.swing.JFrame;

import kaba4cow.taskman.notifications.NotificationManager;
import kaba4cow.taskman.notifications.NotificationTray;
import kaba4cow.taskman.repositories.note.NoteRepository;
import kaba4cow.taskman.repositories.task.TaskRepository;
import kaba4cow.taskman.repositories.todo.TodoRepository;
import kaba4cow.taskman.ui.ApplicationWindow;
import kaba4cow.taskman.ui.SplashScreen;
import kaba4cow.taskman.utils.ApplicationUtils;

public class Application {

	private static final String applicationTitle = "Taskman";

	private static final int listeningPort = 57010;

	private static final ApplicationSettings settings = new ApplicationSettings();
	private static final ApplicationCalendar calendar = new ApplicationCalendar();
	private static final ApplicationClock clock = new ApplicationClock();
	private static final TaskRepository taskRepository = new TaskRepository();
	private static final NoteRepository noteRepository = new NoteRepository();
	private static final TodoRepository todoRepository = new TodoRepository();

	private static ApplicationWindow applicationWindow;

	public static void showWindow() {
		applicationWindow.setVisible(true);
		applicationWindow.setExtendedState(JFrame.NORMAL);
		applicationWindow.toFront();
		applicationWindow.requestFocus();
	}

	public static void hideWindow() {
		applicationWindow.setVisible(false);
	}

	public static boolean isOnScreen() {
		return applicationWindow.isFocused();
	}

	public static void closeProgram() {
		NotificationTray.remove();
		System.exit(0);
	}

	public static String getTitle() {
		return applicationTitle;
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
		return applicationWindow;
	}

	private static boolean canRunApplication() {
		try {
			RandomAccessFile file = ApplicationUtils.getLockFile();
			FileLock lock = file.getChannel().tryLock();
			if (lock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						try {
							lock.release();
							file.close();
						} catch (Exception e) {
						}
					}
				});
				return true;
			}
		} catch (Exception e) {
		}
		try {
			new Socket("localhost", listeningPort).close();
		} catch (IOException e) {
		}
		return false;
	}

	private static void startServer() {
		ServerSocket server;
		try {
			server = new ServerSocket(listeningPort);
			new Thread("Server") {
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
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) throws Exception {
		if (canRunApplication()) {
			SplashScreen splashScreen = new SplashScreen();
			applicationWindow = new ApplicationWindow();
			NotificationTray.init();
			NotificationManager.init();
			splashScreen.dispose();
			showWindow();
			startServer();
		} else
			System.exit(0);
	}

}
