package kaba4cow.taskman.ui.panels.tables;

import java.util.Comparator;

public interface TableSorter<E> extends Comparator<E> {

	default public int compare(E element1, E element2, boolean reverse) {
		int result = compare(element1, element2);
		if (!reverse && result != 0) {
			if (result == -1)
				result = 1;
			else
				result = -1;
		}
		return result;
	}

}
