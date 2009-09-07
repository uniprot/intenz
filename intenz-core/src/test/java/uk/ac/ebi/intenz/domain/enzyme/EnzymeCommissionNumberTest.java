package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import junit.framework.TestCase;

public class EnzymeCommissionNumberTest extends TestCase {

	public void testValueOfInt() throws EcException {
		EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(1);
		assertEquals(Type.CLASS, ec.getType());
	}

	public void testValueOfIntInt() throws EcException {
		EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(1, 2);
		assertEquals(Type.SUBCLASS, ec.getType());
	}

	public void testValueOfIntIntInt() throws EcException {
		EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(1, 2, 3);
		assertEquals(Type.SUBSUBCLASS, ec.getType());
	}

	public void testValueOfIntIntIntInt() throws EcException {
		EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(1, 2, 3, 4);
		assertEquals(Type.ENZYME, ec.getType());
	}

	public void testValueOfIntIntIntIntBoolean() throws EcException {
		EnzymeCommissionNumber ec;
		ec = EnzymeCommissionNumber.valueOf(1, 2, 3, 4, false);
		assertEquals(Type.ENZYME, ec.getType());
		ec = EnzymeCommissionNumber.valueOf(1, 2, 3, 4, true);
		assertEquals(Type.PRELIMINARY, ec.getType());
	}

	public void testValueOfString() throws NumberFormatException, EcException {
		EnzymeCommissionNumber ec;
		ec = EnzymeCommissionNumber.valueOf("1");
		assertEquals(Type.CLASS, ec.getType());
		ec = EnzymeCommissionNumber.valueOf("1.2");
		assertEquals(Type.SUBCLASS, ec.getType());
		ec = EnzymeCommissionNumber.valueOf("1.2.3");
		assertEquals(Type.SUBSUBCLASS, ec.getType());
		ec = EnzymeCommissionNumber.valueOf("1.2.3.4");
		assertEquals(Type.ENZYME, ec.getType());
		ec = EnzymeCommissionNumber.valueOf("1.2.3.n4");
		assertEquals(Type.PRELIMINARY, ec.getType());
		try {
			ec = EnzymeCommissionNumber.valueOf("1.2.3.4n");
			assertTrue(false);
		} catch (Exception e){
			assertFalse(false);
		}
		try {
			ec = EnzymeCommissionNumber.valueOf("1.2.3.-");
			assertTrue(false);
		} catch (Exception e){
			assertFalse(false);
		}
	}

	public void testToString() throws EcException {
		EnzymeCommissionNumber ec;
		ec = EnzymeCommissionNumber.valueOf(1, 2, 3, 4, false);
		assertEquals("1.2.3.4", ec.toString());
		ec = EnzymeCommissionNumber.valueOf(1, 2, 3, 4, true);
		assertEquals("1.2.3.n4", ec.toString());
	}

}
