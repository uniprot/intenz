package uk.ac.ebi.intenz.tools.sib.writer;

import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: rafalcan
 * Date: 14-Sep-2005
 * Time: 14:28:27
 * To change this template use File | Settings | File Templates.
 */
public class CC_LineFormatterTest extends TestCase {

    public void testOrderedList() throws Exception {
        CC_LineFormatter formatter = new CC_LineFormatter();

        String ccLine = "In many bacteria, plants and animals, betaine is synthesized in two steps: "
            + "(1) choline to betaine aldehyde and (2) betaine aldehyde to betaine.";
        String enzymeLine = "CC   -!- In many bacteria, plants and animals, betaine is synthesized in two\n"
                          + "CC       steps: (1) choline to betaine aldehyde and (2) betaine aldehyde to\n"
                          + "CC       betaine.\n";
        String formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        ccLine = "The overall reaction takes place in two separate steps: "
            + "(1) Adenosine + O(2) = 5'-dehydroadenosine + H(2)O(2). "
            + "(2) 5'-dehydroadenosine + O(2) = 9-riburonosyladenine + H(2)O(2).";
        enzymeLine = "CC   -!- The overall reaction takes place in two separate steps:\n" +
                     "CC       (1) Adenosine + O(2) = 5'-dehydroadenosine + H(2)O(2).\n" +
                     "CC       (2) 5'-dehydroadenosine + O(2) = 9-riburonosyladenine + H(2)O(2).\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        // EC 1.21.4.2
        ccLine = "The enzyme from <ital>Eubacterium acidaminophilum</ital> consists of subunits A, B and C. " +
                 "Subunit B contains selenocysteine and a pyruvoyl group, and is responsible for glycine binding " +
                 "and ammonia release.";
        enzymeLine = "CC   -!- The enzyme from Eubacterium acidaminophilum consists of subunits A,\n" +
                     "CC       B and C.\n" +
                     "CC   -!- Subunit B contains selenocysteine and a pyruvoyl group, and is\n" +
                     "CC       responsible for glycine binding and ammonia release.\n";
        // We need some tag translation here:
        String translated = XCharsASCIITranslator.getInstance().toASCII(ccLine, false, false);
        formattedLine = formatter.formatLines(translated, LineType.CC);
        assertEquals(enzymeLine, formattedLine);
    }
}
