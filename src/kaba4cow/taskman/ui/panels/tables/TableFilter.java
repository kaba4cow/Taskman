package kaba4cow.taskman.ui.panels.tables;

import java.util.Collections;
import java.util.List;

public abstract class TableFilter<E> {

	private TableSorter<E> sortingTarget;
	private boolean sortingReversed;

	public TableFilter() {
		sortingTarget = null;
		sortingReversed = false;
	}

	public void sort(List<E> list) {
		Collections.sort(list, sortingTarget);
		if (sortingReversed)
			Collections.reverse(list);
	}

	public void setSortingTarget(TableSorter<E> sortingTarget) {
		this.sortingTarget = sortingTarget;
	}

	public void setSortingReversed(boolean sortingReversed) {
		this.sortingReversed = sortingReversed;
	}

}
