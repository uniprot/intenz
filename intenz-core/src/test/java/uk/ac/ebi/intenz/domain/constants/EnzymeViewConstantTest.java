package uk.ac.ebi.intenz.domain.constants;

import junit.framework.TestCase;
import uk.ac.ebi.intenz.domain.enzyme.Viewable;

/**
 *
 * @author rafalcan
 */
public class EnzymeViewConstantTest extends TestCase {
    
    public EnzymeViewConstantTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of isInView method, of class EnzymeViewConstant.
     */
    public void testIsInView() {
        Object o1 = new Viewable() {
            public EnzymeViewConstant getView() {
                return EnzymeViewConstant.SIB;
            }
        };
        Object o2 = new Viewable() {
            public EnzymeViewConstant getView() {
                return EnzymeViewConstant.SIB_INTENZ;
            }
        };
        Object o3 = new Viewable() {
            public EnzymeViewConstant getView() {
                return EnzymeViewConstant.INTENZ;
            }
        };
        assertTrue(EnzymeViewConstant.isInView(EnzymeViewConstant.SIB, o1));
        assertTrue(EnzymeViewConstant.isInView(EnzymeViewConstant.SIB, o2));
        assertTrue(EnzymeViewConstant.isInView(EnzymeViewConstant.SIB, o3));
    }

}
