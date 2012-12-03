package uk.ac.ebi.intenz.webapp.utilities;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

/**
 * Compares two objects' size. If any of them is not a {@link Collection}
 * or {@link Map}, its size is considered as 0.
 * @author rafalcan
 *
 */
public class SizeComparator implements Comparator<Object> {

	public int compare(Object o1, Object o2) {
		Class<?> class1 = o1.getClass();
		Class<?> class2 = o2.getClass();
		if (!class1.equals(class2))
			throw new IllegalArgumentException();
		int size1 = 0, size2 = 0;
		if ((o1 instanceof Collection)){
			size1 = ((Collection<?>) o1).size();
		} else if (o1 instanceof Map){
			size1 = ((Map<?, ?>) o1).size();
		}
		if ((o2 instanceof Collection)){
			size2 = ((Collection<?>) o2).size();
		} else if (o2 instanceof Map){
			size2 = ((Map<?, ?>) o2).size();
		}
		return size1 - size2;
	}

}
