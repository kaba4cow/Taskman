package kaba4cow.taskman.notifications;

import java.awt.EventQueue;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.utils.ApplicationUtils;

public final class NotificationTray {

	private static NotificationTray instance;

	private final SystemTray tray;
	private final TrayIcon icon;

	private NotificationTray() throws Exception {
		if (!SystemTray.isSupported())
			throw new Exception();
		tray = SystemTray.getSystemTray();
		icon = new TrayIcon(ApplicationUtils.getApplicationIconImage(), Application.getTitle());
		icon.setImageAutoSize(true);
		icon.setPopupMenu(new TrayPopupMenu());
		icon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Application.showWindow();
			}
		});
		tray.add(icon);
	}

	public static void init() {
		try {
			instance = new NotificationTray();
		} catch (Exception e) {
			instance = null;
		}
	}

	public static void remove() {
		if (instance != null)
			instance.tray.remove(instance.icon);
	}

	public static void showNotification(String message) {
		if (instance != null)
			EventQueue.invokeLater(() -> instance.icon.displayMessage(//
					Application.getTitle() + " Reminder", //
					message, //
					MessageType.INFO));
	}

	private class TrayPopupMenu extends PopupMenu {

		private static final long serialVersionUID = 1L;

		public TrayPopupMenu() {
			super();

			MenuItem openItem = new MenuItem("Open");
			openItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					Application.showWindow();
				}
			});
			add(openItem);

			MenuItem exitItem = new MenuItem("Exit");
			exitItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					Application.closeProgram();
				}
			});
			add(exitItem);
		}

	}

}
