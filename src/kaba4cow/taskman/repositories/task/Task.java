package kaba4cow.taskman.repositories.task;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaba4cow.taskman.repositories.RepositoryElement;
import kaba4cow.taskman.utils.io.DataReader;
import kaba4cow.taskman.utils.io.DataWriter;

public class Task implements RepositoryElement {

	private String description;
	private LocalDate date;
	private final int[] intervals;
	private final ArrayList<LocalTime> times;
	private boolean remind;
	private LocalTime remindTime;

	public Task() {
		description = "";
		date = null;
		intervals = new int[4];
		times = new ArrayList<>();
		remind = false;
		remindTime = LocalTime.of(0, 30);
	}

	public Task(DataReader reader) throws IOException {
		description = reader.readString();
		date = reader.readDate();
		intervals = new int[4];
		for (int i = 0; i < intervals.length; i++)
			intervals[i] = reader.readByte();
		int timesAmount = reader.readByte();
		times = new ArrayList<>(timesAmount);
		for (int i = 0; i < timesAmount; i++)
			times.add(reader.readTime());
		remind = reader.readBoolean();
		remindTime = reader.readTime();
	}

	@Override
	public void saveData(DataWriter writer) throws IOException {
		writer.writeString(description);
		writer.writeDate(date);
		for (int i = 0; i < intervals.length; i++)
			writer.writeByte(intervals[i]);
		writer.writeByte(times.size());
		for (int i = 0; i < times.size(); i++)
			writer.writeTime(times.get(i));
		writer.writeBoolean(remind);
		writer.writeTime(remindTime);
	}

	public List<LocalTime> getTimes() {
		return new ArrayList<>(times);
	}

	public int getTotalTimes() {
		return times.size();
	}

	public void addTime(LocalTime time) {
		if (!times.contains(time)) {
			times.add(time);
			Collections.sort(times);
		}
	}

	public boolean removeTime(LocalTime time) {
		times.remove(time);
		return times.isEmpty();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate lastDate) {
		this.date = lastDate;
	}

	public int getYearInterval() {
		return intervals[0];
	}

	public int getMonthInterval() {
		return intervals[1];
	}

	public int getWeekInterval() {
		return intervals[2];
	}

	public int getDayInterval() {
		return intervals[3];
	}

	public int getInterval(int index) {
		return intervals[index];
	}

	public void setIntervals(int years, int months, int weeks, int days) {
		intervals[0] = years;
		intervals[1] = months;
		intervals[2] = weeks;
		intervals[3] = days;
	}

	public boolean isRemindEnabled() {
		return remind;
	}

	public void setRemindEnabled(boolean remind) {
		this.remind = remind;
	}

	public LocalTime getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(LocalTime remindTime) {
		this.remindTime = remindTime;
	}

	public Type getType() {
		int interval = 365 * intervals[0] + 30 * intervals[1] + 7 * intervals[2] + intervals[3];
		switch (interval) {
		case 0:
			return Type.EVENT;
		case 1:
			return Type.DAILY;
		default:
			return Type.REPEATING;
		}
	}

	public LocalDate getNextDate() {
		Type type = getType();
		switch (type) {
		case DAILY:
			return LocalDate.now();
		case EVENT:
			return date;
		default:
		case REPEATING:
			Period period = Period.of(getYearInterval(), getMonthInterval(), getWeekInterval() * 7 + getDayInterval());
			LocalDate currentDate = LocalDate.now();
			LocalDate nextDate = date;
			while (nextDate.isBefore(currentDate))
				nextDate = nextDate.plus(period);
			return nextDate;
		}
	}

	public boolean isAtDate(LocalDate date) {
		return getNextDate().equals(date);
	}

	public boolean isAtHour(int hour) {
		for (LocalTime time : times)
			if (time.getHour() == hour)
				return true;
		return false;
	}

	public static enum Type {

		DAILY("Daily Task", Color.green), REPEATING("Repeating Task", Color.yellow), EVENT("Event", Color.red);

		private final String name;
		private final Color color;

		private Type(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public Color getColor() {
			return color;
		}

	}

}
