package uk.ac.ebi.intenz.domain.enzyme;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

public class EnzymeEntryEcComparatorTest extends TestCase {

	private EnzymeEntry e1, e2;
	private EnzymeEntryEcComparator comparator = new EnzymeEntryEcComparator();
	
	@Override
	protected void setUp() throws Exception {
		e1 = new EnzymeEntry();
		e1.setEc(EnzymeCommissionNumber.valueOf("1.2.3.4"));
		
		e2 = new EnzymeEntry();
		e2.setEc(EnzymeCommissionNumber.valueOf("2.3.4.5"));
	}

	public void testCompare() throws Exception {
		assertTrue(comparator.compare(e1, e2) < 0);
		e1.setEc(EnzymeCommissionNumber.valueOf("3.4.5.6"));
		assertFalse(comparator.compare(e1, e2) < 0);
	}

}
