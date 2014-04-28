/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.intenz.webapp.helper;

import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.XRef.Availability;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 *
 * @author rafalcan
 */
public class EnzymeEntryHelperTest extends TestCase {
    
    public EnzymeEntryHelperTest(String testName) {
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

    public void testCofactorObjectLinks(){
        OperatorSet os = new OperatorSet(Cofactor.Operators.OR_OPTIONAL.getCode());
        os.add(Cofactor.valueOf(Compound.valueOf(Compound.NO_ID_ASSIGNED, 
                999L, "Foo", null, null, "CHEBI:000999", Availability.P),
                EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ));
        os.add(Cofactor.valueOf(Compound.valueOf(Compound.NO_ID_ASSIGNED,
                888L, "Bar", null, null, "CHEBI:000888", Availability.P),
                EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ));
        assertEquals("<a href=\"http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:000999\" target=\"_blank\">Foo</a>"
                + " or <a href=\"http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI:000888\" target=\"_blank\">Bar</a>",
                EnzymeEntryHelper.getCofactorObjectLinks(os, SpecialCharacters.getInstance(null), null, false));
    }
}
