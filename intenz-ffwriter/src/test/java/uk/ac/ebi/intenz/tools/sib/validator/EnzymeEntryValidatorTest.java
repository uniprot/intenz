package uk.ac.ebi.intenz.tools.sib.validator;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import uk.ac.ebi.intenz.tools.sib.exceptions.EnzymeEntryValidationException;

/**
 * EnzymeEntryValidator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>04/07/2005</pre>
 */
public class EnzymeEntryValidatorTest extends TestCase {
  public EnzymeEntryValidatorTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

  public static Test suite() {
    return new TestSuite(EnzymeEntryValidatorTest.class);
  }

  public void testValidate_ID_LINE() throws Exception {
    String entry = "ID   1.1.1.-\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The ID line does not contain a valid EC number.", e.getMessage());
    }

    entry = "ID  1.1.1.1\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "ID   1.1.1.1\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_DE_LINE() throws Exception {
    String entry = "DE   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DE line does not contain valid content.", e.getMessage());
    }

    entry = "DE   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DE line does not contain valid content.", e.getMessage());
    }

    entry = "DE  Alcohol dehydrogenase.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "DE   Alcohol dehydrogenase.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_AN_LINE() throws Exception {
    String entry = "AN   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The AN line does not contain valid content.", e.getMessage());
    }

    entry = "AN   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The AN line does not contain valid content.", e.getMessage());
    }

    entry = "AN  Aldehyde reductase.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "AN   Aldehyde reductase.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_CA_LINE() throws Exception {
    String entry = "CA   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The CA line does not contain valid content.", e.getMessage());
    }

    entry = "CA   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The CA line does not contain valid content.", e.getMessage());
    }

    entry = "CA  An alcohol + NAD(+) = an aldehyde or ketone + NADH.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "CA   An alcohol + NAD(+) = an aldehyde or ketone + NADH.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Multiple CA lines.
    // Invalid format.
    entry = "CA   Leucocyanidin + 2-oxoglutarate + O(2) = cis- and trans-\n" +
            "CA  dihydroquercetins + succinate + CO(2).\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "CA   Leucocyanidin + 2-oxoglutarate + O(2) = cis- and trans-\n" +
            "CA\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "CA\n" +
            "CA   Leucocyanidin + 2-oxoglutarate + O(2) = cis- and trans-\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "CA   Leucocyanidin + 2-oxoglutarate + O(2) = cis- and trans-\n" +
            "CA   dihydroquercetins + succinate + CO(2).\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_CF_LINE() throws Exception {
    String entry = "CF   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The CF line does not contain valid content.", e.getMessage());
    }

    entry = "CF   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The CF line does not contain valid content.", e.getMessage());
    }

    entry = "CF  Iron(2+); Ascorbate.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "CF   Iron(2+); Ascorbate.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_CC_LINE() throws Exception {
    String entry = "CC   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The comment does not comply to the ENZYME comment format.", e.getMessage());
    }

    entry = "CC   -!- Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The comment does not comply to the ENZYME comment format.", e.getMessage());
    }

    entry = "CC  -!- Requires CO(2) for activity.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "CC   -!- Requires CO(2) for activity.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Multiple CC lines.
    // Invalid format.
    entry = "CC   -!- The enzyme is involved in the pathway by which many flowering\n" +
            "CC      plants make anthocyanin (glycosylated anthocyandin) flower\n" +
            "CC       pigments.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The comment does not comply to the ENZYME comment format.", e.getMessage());
    }

    // Format ok (one sentence).
    entry = "CC   -!- The enzyme is involved in the pathway by which many flowering\n" +
            "CC       plants make anthocyanin (glycosylated anthocyandin) flower\n" +
            "CC       pigments.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Format ok (mutliple sentences).
    entry = "CC   -!- The enzyme is involved in the pathway by which many flowering\n" +
            "CC       plants make anthocyanin (glycosylated anthocyandin) flower\n" +
            "CC       pigments.\n" +
            "CC   -!- The intermediates are transformed into cis- and trans-\n" +
            "CC       dihydroquercetin, which the enzyme can also oxidize to quercetin.\n" +
            "CC   -!- Acidification of the products gives anthocyanidin, which,\n" +
            "CC       however, may not be a natural precursor of the anthocyanins.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_DI_LINE() throws Exception {
    String entry = "DI   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DI line does not contain valid content.", e.getMessage());
    }

    entry = "DI   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DI line does not contain valid content.", e.getMessage());
    }

    entry = "DI  Sorbitol dehydrogenase deficiency; MIM:182500.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok (single line).
    entry = "DI   Sorbitol dehydrogenase deficiency; MIM:182500.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Multiple CA lines.
    // Invalid format.
    entry = "DI   Exertional myoglobinuria; MIM:150000.\n" +
            "DI  Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "DI   Exertional myoglobinuria; MIM:150000.\n" +
            "DI.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "DI\n" +
            "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "DI   Exertional myoglobinuria; MIM:150000.\n" +
            "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_PR_LINE() throws Exception {
    String entry = "PR   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The PR line does not contain valid content.", e.getMessage());
    }

    entry = "PR   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The PR line does not contain valid content.", e.getMessage());
    }

    entry = "PR  PROSITE; PDOC00060;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok (single line).
    entry = "PR   PROSITE; PDOC00060;\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Multiple CA lines.
    // Invalid format.
    entry = "PR   PROSITE; PDOC00059;\n" +
            "PR  PROSITE; PDOC00060;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "PR   PROSITE; PDOC00059;\n" +
            "PR.\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "PR\n" +
            "PR   PROSITE; PDOC00059;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "PR   PROSITE; PDOC00058;\n" +
            "PR   PROSITE; PDOC00059;\n" +
            "PR   PROSITE; PDOC00060;\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testValidate_DR_LINE() throws Exception {
    String entry = "DR   .\n";

    // Invalid format.
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DR line does not contain valid content.", e.getMessage());
    }

    entry = "DR   Text w/o a period\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DR line does not contain valid content.", e.getMessage());
    }

    entry = "DR  P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "DR   P80222,ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("The DR line does not contain valid content.", e.getMessage());
    }

    // Format ok (single line).
    entry = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    assertTrue(EnzymeEntryValidator.validate(entry));

    // Multiple CA lines.
    // Invalid format.
    entry = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
            "DR  P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
            "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
            "DR\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    entry = "DR\n" +
            "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n";
    try {
      EnzymeEntryValidator.validate(entry);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals("No valid line type found.", e.getMessage());
    }

    // Format ok.
    entry = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
            "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
            "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n";
    assertTrue(EnzymeEntryValidator.validate(entry));
  }

  public void testEntry() throws Exception {
    // valid entry
    String entry = "ID   1.1.1.1\n" +
                   "DE   Alcohol dehydrogenase.\n" +
                   "AN   Aldehyde reductase.\n" +
                   "CA   An alcohol + NAD(+) = an aldehyde or ketone + NADH.\n" +
                   "CF   Zinc or Iron.\n" +
                   "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
                   "CC   -!- The animal, but not the yeast, enzyme acts also on cyclic secondary\n" +
                   "CC       alcohols.\n" +
                   "PR   PROSITE; PDOC00058;\n" +
                   "PR   PROSITE; PDOC00059;\n" +
                   "PR   PROSITE; PDOC00060;\n" +
                   "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
                   "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
                   "DR   P33010, TERPD_PSESP;\n" +
                   "//";

    assertTrue(EnzymeEntryValidator.validate(entry));
  }

}
