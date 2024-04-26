package kaba4cow.taskman.ui.panels.tables.notes;

import java.time.LocalDate;

import kaba4cow.taskman.repositories.note.Note;
import kaba4cow.taskman.ui.panels.tables.TableFilter;
import kaba4cow.taskman.ui.panels.tables.TableSorter;

public class NoteTableFilter extends TableFilter<Note> {

	private String description;
	private boolean caseSensitive;

	private String tag;
	private LocalDate startDate;
	private LocalDate endDate;

	public NoteTableFilter() {
		super();
		setSortingTarget(SortingTarget.DATE);
		setSortingReversed(false);
		description = "";
		caseSensitive = false;
		tag = null;
		startDate = null;
		endDate = null;
	}

	public boolean filter(Note note) {
		if (!description.trim().isEmpty()) {
			String noteDescription = caseSensitive ? note.getDescription() : note.getDescription().toLowerCase();
			String filterDescription = caseSensitive ? description : description.toLowerCase();
			if (!noteDescription.contains(filterDescription))
				return false;
		}
		if (tag != null && !note.getTag().equals(tag))
			return false;
		if (startDate != null && note.getDate().isBefore(startDate))
			return false;
		if (endDate != null && note.getDate().isAfter(endDate))
			return false;
		return true;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public static enum SortingTarget implements TableSorter<Note> {

		DATE("Date") {
			@Override
			public int compare(Note note1, Note note2) {
				return note1.getDate().compareTo(note2.getDate());
			}
		}, //
		DESCRIPTION("Description") {
			@Override
			public int compare(Note note1, Note note2) {
				return note1.getDescription().compareTo(note2.getDescription());
			}
		}, //
		TAG("Tag") {
			@Override
			public int compare(Note note1, Note note2) {
				return note1.getTag().compareTo(note2.getTag());
			}
		};

		private final String name;

		private SortingTarget(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
