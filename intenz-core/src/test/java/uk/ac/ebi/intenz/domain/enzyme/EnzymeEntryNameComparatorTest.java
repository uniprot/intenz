package uk.ac.ebi.intenz.domain.enzyme;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeNameException;
import junit.framework.TestCase;

public class EnzymeEntryNameComparatorTest extends TestCase {

	private EnzymeEntry e1, e2;
	private EnzymeEntryNameComparator comparator = new EnzymeEntryNameComparator();

	@Override
	protected void setUp() throws Exception {
		e1 = new EnzymeEntry();
		e1.setEc(EnzymeCommissionNumber.valueOf("1.2.3.4"));
		e1.setCommonNames(getCommonNames("oxalate oxidase"));
		
		e2 = new EnzymeEntry();
		e2.setEc(EnzymeCommissionNumber.valueOf("2.3.4.5"));
		e2.setCommonNames(getCommonNames("this one does not exist yet"));
	}

	private List<EnzymeName> getCommonNames(String name) {
		List<EnzymeName> names2 = new ArrayList<EnzymeName>();
		EnzymeName name2 = EnzymeName.valueOf(name,
				EnzymeNameTypeConstant.COMMON_NAME, null,
				EnzymeSourceConstant.INTENZ,
				EnzymeViewConstant.INTENZ);
		names2.add(name2);
		return names2;
	}

	public void testCompare() throws EnzymeNameException {
		// oxalate < this:
		assertTrue(comparator.compare(e1, e2) < 0);
		e2.setCommonNames(getCommonNames("another name"));
		// now oxalate > another :
		assertFalse(comparator.compare(e1, e2) < 0);
		e2.setCommonNames(getCommonNames("<smallsub>another</smallsub> name"));
		// XChars is converted to ASCII for comparison:
		assertFalse(comparator.compare(e1, e2) < 0);
		e1.setCommonNames(getCommonNames("<smallsub>oxalate</smallsub> oxidase"));
		// ASCII versions are still the same:
		assertFalse(comparator.compare(e1, e2) < 0);
		
	}

}
