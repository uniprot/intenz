package uk.ac.ebi.intenz.domain.enzyme;

import java.util.Comparator;

/**
 * Compares enzyme entries according to their EC number.
 * @author rafalcan
 */
public class EnzymeEntryEcComparator implements Comparator<EnzymeEntry> {

	public int compare(EnzymeEntry o1, EnzymeEntry o2) {
		return o1.getEc().compareTo(o2.getEc());
	}

}
