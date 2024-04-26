package kaba4cow.taskman.ui.panels.calendar;

import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.task.Task;
import kaba4cow.taskman.utils.DateUtils;

public class DayLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private final Set<Task.Type> markers = new HashSet<>();

	private LocalDate dayDate;

	public DayLabel(CalendarPanel calendarPanel) {
		super();
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1)
					calendarPanel.selectDate(dayDate);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth() / 4;
		int height = getHeight() / Task.Type.values().length;
		int y = 1;
		for (Task.Type type : markers) {
			g.setColor(Application.getSettings().getTaskColor(type));
			g.fillRect(1, y, width, height);
			g.setColor(SystemColor.textText);
			g.drawRect(1, y, width - 1, height - 1);
			y += height;
		}
	}

	public void selectDate(LocalDate selectedDate, LocalDate currentDate) {
		dayDate = currentDate;
		boolean isCurrentDay = dayDate.equals(LocalDate.now());
		boolean isSelectedMonth = dayDate.getMonthValue() == selectedDate.getMonthValue();
		if (dayDate.equals(selectedDate))
			select(isCurrentDay, isSelectedMonth);
		else
			deselect(isCurrentDay, isSelectedMonth);
		markers.clear();
		List<Task> tasks = Application.getTaskRepository().getTasksAtDate(dayDate);
		for (Task task : tasks)
			markers.add(task.getType());
		setText(Integer.toString(dayDate.getDayOfMonth()));
		repaint();
		setToolTipText(createToolTipText(Application.getTaskRepository().getTotalTaskTimesAtDate(dayDate)));
	}

	private String createToolTipText(int tasks) {
		if (tasks == 0)
			return null;
		else
			return String.format("%d task%s on %s", tasks, tasks == 1 ? "" : "s", DateUtils.formatDate(dayDate));
	}

	private void select(boolean isCurrentDay, boolean isSelectedMonth) {
		if (isCurrentDay || isSelectedMonth) {
			setOpaque(true);
			setBorder(BorderFactory.createDashedBorder(SystemColor.textHighlightText, 1f, 1f));
			setBackground(SystemColor.activeCaption);
			setForeground(SystemColor.textHighlightText);
		} else {
			setOpaque(false);
			setBorder(BorderFactory.createLineBorder(SystemColor.activeCaptionBorder, 1));
			setForeground(SystemColor.textText);
		}
	}

	private void deselect(boolean isCurrentDay, boolean isSelectedMonth) {
		if (isCurrentDay) {
			setOpaque(false);
			setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption, 1));
			setForeground(SystemColor.textText);
		} else {
			setOpaque(false);
			setBorder(null);
			setForeground(isSelectedMonth ? SystemColor.textText : SystemColor.textInactiveText);
		}
	}

}
