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
        
        // EC 2.5.1.98
        ccLine = "The enzyme is responsible for pyruvylation of subterminal" +
        		" glucose in the acidic octasaccharide repeating unit of the" +
        		" exopolysaccharide of Rhizobium leguminosarum (bv. viciae" +
        		" strain VF39) which is necessary to establish nitrogen-fixing" +
        		" symbiosis with Pisum sativum, Vicia faba, and Vicia sativa.";
        enzymeLine =
        		"CC   -!- The enzyme is responsible for pyruvylation of subterminal glucose in\n" +
        		"CC       the acidic octasaccharide repeating unit of the exopolysaccharide of\n" +
				"CC       Rhizobium leguminosarum (bv. viciae strain VF39) which is necessary\n" +
				"CC       to establish nitrogen-fixing symbiosis with Pisum sativum, Vicia\n" +
				"CC       faba, and Vicia sativa.\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        // EC 1.14.15.10
        ccLine = "This line with Rhodococcus sp. NCIMB 9784 should not be split";
        enzymeLine = "CC   -!- This line with Rhodococcus sp. NCIMB 9784 should not be split\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);
        
        ccLine = "foo Pseudomonas sp. HZN6 bar.";
        enzymeLine = "CC   -!- foo Pseudomonas sp. HZN6 bar.\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        ccLine = "foo Cladosporium sp. G-10 bar.";
        enzymeLine = "CC   -!- foo Cladosporium sp. G-10 bar.\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        ccLine = "foo Bradyrhizobium sp. JS329 bar.";
        enzymeLine = "CC   -!- foo Bradyrhizobium sp. JS329 bar.\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);

        ccLine = "foo Pseudomonas sp. WBC-3 bar.";
        enzymeLine = "CC   -!- foo Pseudomonas sp. WBC-3 bar.\n";
        formattedLine = formatter.formatLines(ccLine, LineType.CC);
        assertEquals(enzymeLine, formattedLine);
    }
}
