package kaba4cow.taskman.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

	private static final String[] suffixes = { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	private static final String[] dayNames = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private static final String[] monthNames = { "January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December" };

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter titleFormatter = DateTimeFormatter.ofPattern("yyyy - HH:mm:ss");

	private static final int numberOfDays = 7;
	private static final int numberOfWeeks = 6;

	private DateUtils() {
	}

	public static LocalDate parseDate(String date) {
		return LocalDate.parse(date, dateFormatter);
	}

	public static String getDayName(int index) {
		return dayNames[index];
	}

	public static String getMonthName(int index) {
		return monthNames[index];
	}

	public static String formatTitleTime(LocalDateTime time) {
		String suffix;
		int dayOfMonth = time.getDayOfMonth();
		switch (dayOfMonth) {
		case 11:
		case 12:
		case 13:
			suffix = "th";
			break;
		default:
			suffix = suffixes[dayOfMonth % 10];
		}
		return String.format("%s %d%s %s", getMonthName(time.getMonthValue() - 1), dayOfMonth, suffix,
				time.format(titleFormatter));
	}

	public static String formatMonthYearDate(LocalDate date) {
		return String.format("%s %d", getMonthName(date.getMonthValue() - 1), date.getYear());
	}

	public static String formatDate(LocalDate date) {
		return date.format(dateFormatter);
	}

	public static String formatTime(LocalTime time) {
		return time.format(timeFormatter);
	}

	public static int getNumberOfDays() {
		return numberOfDays;
	}

	public static int getNumberOfWeeks() {
		return numberOfWeeks;
	}

}
