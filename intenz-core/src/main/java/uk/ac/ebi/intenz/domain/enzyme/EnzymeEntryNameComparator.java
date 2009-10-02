package uk.ac.ebi.intenz.domain.enzyme;

import java.util.Comparator;

import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * Compares enzyme entries according to their common name
 * (SwissProt - ASCII - encoding).
 * @author rafalcan
 */
public class EnzymeEntryNameComparator implements Comparator<EnzymeEntry> {

	public int compare(EnzymeEntry o1, EnzymeEntry o2) {
		String name1 = SpecialCharacters.getInstance(null).xml2Display(
				o1.getCommonName().getName(), EncodingType.SWISSPROT_CODE);
		String name2 = SpecialCharacters.getInstance(null).xml2Display(
				o2.getCommonName().getName(), EncodingType.SWISSPROT_CODE);
		return name1.compareTo(name2);
	}

}
