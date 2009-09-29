package uk.ac.ebi.intenz.tools.sib.comparator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ebi.intenz.tools.sib.exceptions.EnzymeEntryValidationException;

/**
 * FlatFileComparator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>04/06/2005</pre>
 */
public class FlatFileComparatorTest extends TestCase {
  public FlatFileComparatorTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

  public static Test suite() {
    return new TestSuite(FlatFileComparatorTest.class);
  }

  public void testCompareEntries_STANDARD() throws Exception {
    String entryA = "";
    String entryB = "";
    String expectedDifferences = "";

    // Test null values.
    try {
      FlatFileComparator.compareEntries(null, entryB);
      fail("NullPointerException expected.");
    } catch (NullPointerException e) {
      assertEquals(e.getMessage(), "Parameter 'entryA' must not be null.");
    }

    try {
      FlatFileComparator.compareEntries(entryA, null);
      fail("NullPointerException expected.");
    } catch (NullPointerException e) {
      assertEquals(e.getMessage(), "Parameter 'entryB' must not be null.");
    }

    // Test empty entries.
    try {
      assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
    	assertTrue(true);
    }

    // Test IllegalArgumentExcpetion.
    entryA = "invalid ENZYME entry format";
    try {
      FlatFileComparator.compareEntries(entryA, entryB);
      fail("EnzymeEntryValidationException expected.");
    } catch (EnzymeEntryValidationException e) {
      assertEquals(e.getMessage(), "Error in entryA: No valid line type found.");
    }
  }

  public void testCompareEntries_ID_LINE_DIFFERENCES() throws Exception {
    String entryA = "ID   1.1.1.1\n";
    String entryB = "ID   1.1.1.1\n";
    String expectedDifferences = "";

    // Test equal ID lines.
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test ID line difference.
    entryB = "ID   1.1.1.2\n";
    expectedDifferences = "MISSING ENZYME ENTRY: 1.1.1.1 does not exist in IntEnz";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));
  }

  public void testCompareEntries_DE_LINE_DIFFERENCES() throws Exception {
    // Test equal DE lines.
    String entryA = "DE   Alcohol dehydrogenase.\n";
    String entryB = "DE   Alcohol dehydrogenase.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test DE line differences.
    entryA = "DE   Alcohol dehydrogenase.\n";
    entryB = "DE   Something else.\n";
    expectedDifferences = "DE line is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_AN_LINE_DIFFERENCES() throws Exception {
    // Test equal (single) AN lines.
    String entryA = "AN   Aldehyde reductase.\n";
    String entryB = "AN   Aldehyde reductase.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (single) AN line differences.
    entryA = "AN   Aldehyde reductase.\n";
    entryB = "AN   Something else.\n";
    expectedDifferences = "AN line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) AN lines.
    entryA = "AN   Aldehyde reductase I.\n" +
             "AN   Xenobiotic ketone reductase.\n" +
             "AN   NADPH-dependent carbonyl reductase.\n" +
             "AN   Prostaglandin 9-ketoreductase.\n";
    entryB = "AN   Aldehyde reductase I.\n" +
             "AN   Xenobiotic ketone reductase.\n" +
             "AN   NADPH-dependent carbonyl reductase.\n" +
             "AN   Prostaglandin 9-ketoreductase.\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) AN line differences.
    entryA = "AN   Aldehyde reductase I.\n" +
             "AN   Xenobiotic ketone reductase.\n" +
             "AN   NADPH-dependent carbonyl reductase.\n" +
             "AN   Prostaglandin 9-ketoreductase.\n";
    entryB = "AN   Aldehyde reductase I.\n" +
             "AN   Xenobiotic ketone reductase.\n" +
             "AN   SOMETHING ELSE.\n" +
             "AN   Prostaglandin 9-ketoreductase.\n";
    expectedDifferences = "AN line 3 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_CA_LINE_DIFFERENCES() throws Exception {
    // Test equal (single) CA lines.
    String entryA = "CA   R-CHOH-R' + NADP(+) = R-CO-R' + NADPH.\n";
    String entryB = "CA   R-CHOH-R' + NADP(+) = R-CO-R' + NADPH.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (single) CA line differences.
    entryA = "CA   R-CHOH-R' + NADP(+) = R-CO-R' + NADPH.\n";
    entryB = "CA   Something else.\n";
    expectedDifferences = "CA line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) CA lines.
    entryA = "CA   Propane-1,2-diol 1-phosphate + NAD(+) = hydroxyacetone phosphate +\n" +
             "CA   NADH.\n";
    entryB = "CA   Propane-1,2-diol 1-phosphate + NAD(+) = hydroxyacetone phosphate +\n" +
             "CA   NADH.\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) CA line differences.
    entryA = "CA   Propane-1,2-diol 1-phosphate + NAD(+) = hydroxyacetone phosphate +\n" +
             "CA   NADH.\n";
    entryB = "CA   Propane-1,2-diol 1-phosphate + NAD(+) = hydroxyacetone phosphate +\n" +
             "CA   SOMETHING ELSE.\n";
    // Line 1 - not 2 - because of line reconstruction:
    expectedDifferences = "CA line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_CF_LINE_DIFFERENCES() throws Exception {
    // Test equal CF lines.
    String entryA = "CF   Zinc or Iron.\n";
    String entryB = "CF   Zinc or Iron.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test CF line differences.
    entryA = "CF   Zinc or Iron.\n";
    entryB = "CF   Something else.\n";
    expectedDifferences = "CF line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) CF lines.
    entryA = "CF   Zinc or Iron.\n" +
             "CF   Potassium.\n";
    entryB = "CF   Zinc or Iron.\n" +
             "CF   Potassium.\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) CF line differences.
    entryA = "CF   Zinc or Iron.\n" +
             "CF   Potassium.\n";
    entryB = "CF   Zinc or Iron.\n" +
             "CF   SOMETHING ELSE.\n";
    expectedDifferences = "CF line 2 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_CC_LINE_DIFFERENCES() throws Exception {
    // Test equal CC lines.
    String entryA = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n";
    String entryB = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test CC line differences.
    entryA = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n";
    entryB = "CC   -!- Something else.\n";
    expectedDifferences = "CC line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) CC lines.
    entryA = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
             "CC   -!- The animal, but not the yeast, enzyme acts also on cyclic secondary\n" +
             "CC       alcohols.\n";
    entryB = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
             "CC   -!- The animal, but not the yeast, enzyme acts also on cyclic secondary\n" +
             "CC       alcohols.\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) CC line differences.
    entryA = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
             "CC   -!- The animal, but not the yeast, enzyme acts also on cyclic secondary\n" +
             "CC       alcohols.\n";
    entryB = "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
             "CC   -!- SOMETHING ELSE.\n";
    expectedDifferences = "CC line 2 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_PR_LINE_DIFFERENCES() throws Exception {
    // Test equal PR lines.
    String entryA = "PR   PROSITE; PDOC00058;\n";
    String entryB = "PR   PROSITE; PDOC00058;\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test PR line differences.
    entryA = "PR   PROSITE; PDOC00058;\n";
    entryB = "PR   PROSITE; PDOC11158;\n";
    expectedDifferences = "PR line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) PR lines.
    entryA = "PR   PROSITE; PDOC00058;\n" +
             "PR   PROSITE; PDOC00059;\n" +
             "PR   PROSITE; PDOC00060;\n";
    entryB = "PR   PROSITE; PDOC00058;\n" +
             "PR   PROSITE; PDOC00059;\n" +
             "PR   PROSITE; PDOC00060;\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) PR line differences.
    entryA = "PR   PROSITE; PDOC00058;\n" +
             "PR   PROSITE; PDOC00059;\n" +
             "PR   PROSITE; PDOC00060;\n";
    entryB = "PR   PROSITE; PDOC00058;\n" +
             "PR   PROSITE; PDOC00059;\n" +
             "PR   PROSITE; PDOC11160;\n";
    expectedDifferences = "PR line 3 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_DI_LINE_DIFFERENCES() throws Exception {
    // Test equal PR lines.
    String entryA = "DI   Sorbitol dehydrogenase deficiency; MIM:182500.\n";
    String entryB = "DI   Sorbitol dehydrogenase deficiency; MIM:182500.\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test PR line differences.
    entryA = "DI   Sorbitol dehydrogenase deficiency; MIM:182500.\n";
    entryB = "DI   Sorbitol dehydrogenase deficiency; MIM:999999.\n";
    expectedDifferences = "DI line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) PR lines.
    entryA = "DI   Exertional myoglobinuria; MIM:150000.\n" +
             "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    entryB = "DI   Exertional myoglobinuria; MIM:150000.\n" +
             "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) PR line differences.
    entryA = "DI   Exertional myoglobinuria; MIM:150000.\n" +
             "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n";
    entryB = "DI   Exertional myoglobinuria; MIM:150000.\n" +
             "DI   Lactate dehydrogenase B chain deficiency; MIM:999999.\n";
    expectedDifferences = "DI line 2 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_DR_LINE_DIFFERENCES() throws Exception {
    // Test equal DR lines.
    String entryA = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    String entryB = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    String expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test DR line differences.
    entryA = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n";
    entryB = "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n";
    expectedDifferences = "DR line 1 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));

    // Test equal (multiple) DR lines.
    entryA = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
             "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
             "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n" +
             "DR   P23236, ADH1_DROHY ;  P48586, ADH1_DROMN ;  P09370, ADH1_DROMO ;\n" +
             "DR   P22246, ADH1_DROMT ;  P07161, ADH1_DROMU ;  P12854, ADH1_DRONA ;\n" +
             "DR   P08843, ADH1_EMENI ;  P05336, ADH1_HORVU ;  P20369, ADH1_KLULA ;\n" +
             "DR   Q07288, ADH1_KLUMA ;  P00333, ADH1_MAIZE ;  P80512, ADH1_NAJNA ;\n" +
             "DR   Q9P6C8, ADH1_NEUCR ;  P20306, ADH1_ORYSA ;  P12886, ADH1_PEA   ;\n";
    entryB = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
             "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
             "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n" +
             "DR   P23236, ADH1_DROHY ;  P48586, ADH1_DROMN ;  P09370, ADH1_DROMO ;\n" +
             "DR   P22246, ADH1_DROMT ;  P07161, ADH1_DROMU ;  P12854, ADH1_DRONA ;\n" +
             "DR   P08843, ADH1_EMENI ;  P05336, ADH1_HORVU ;  P20369, ADH1_KLULA ;\n" +
             "DR   Q07288, ADH1_KLUMA ;  P00333, ADH1_MAIZE ;  P80512, ADH1_NAJNA ;\n" +
             "DR   Q9P6C8, ADH1_NEUCR ;  P20306, ADH1_ORYSA ;  P12886, ADH1_PEA   ;\n";
    expectedDifferences = "";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));

    // Test (multiple) DR line differences.
    entryA = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
             "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
             "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n" +
             "DR   P23236, ADH1_DROHY ;  P48586, ADH1_DROMN ;  P09370, ADH1_DROMO ;\n" +
             "DR   P22246, ADH1_DROMT ;  P07161, ADH1_DROMU ;  P12854, ADH1_DRONA ;\n" +
             "DR   P08843, ADH1_EMENI ;  P05336, ADH1_HORVU ;  P20369, ADH1_KLULA ;\n" +
             "DR   Q07288, ADH1_KLUMA ;  P00333, ADH1_MAIZE ;  P80512, ADH1_NAJNA ;\n" +
             "DR   Q9P6C8, ADH1_NEUCR ;  P20306, ADH1_ORYSA ;  P12886, ADH1_PEA   ;\n";
    entryB = "DR   P80222, ADH1_ALLMI ;  P49645, ADH1_APTAU ;  P06525, ADH1_ARATH ;\n" +
             "DR   P41747, ADH1_ASPFL ;  P12311, ADH1_BACST ;  Q17334, ADH1_CAEEL ;\n" +
             "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n" +
             "DR   P23236, ADH1_DROHY ;  P48586, ADH1_DROMN ;  P09370, ADH1_DROMO ;\n" +
             "DR   P22246, ADH1_DROMT ;  P07161, ADH1_DROMU ;  P12854, ADH1_DRONA ;\n" +
             "DR   P08843, ADH1_EMENI ;  P05336, ADH1_HORVU ;  P20369, ADH1_KLULA ;\n" +
             "DR   Q03505, ADH1_RABIT ;  P22797, ADH1_RANPE ;  P14673, ADH1_SOLTU ;\n" +
             "DR   Q9P6C8, ADH1_NEUCR ;  P20306, ADH1_ORYSA ;  P12886, ADH1_PEA   ;\n";
    expectedDifferences = "DR line 7 is different for entry:  (EC of enzyme.dat)\n";
    assertTrue(FlatFileComparator.compareEntries(entryA, entryB).startsWith(expectedDifferences));
  }

  public void testCompareEntries_EQUAL_ENTRIES() throws Exception {
    String entryA = "ID   1.1.1.8\n" +
                    "DE   Glycerol-3-phosphate dehydrogenase (NAD+).\n" +
                    "AN   Glycerophosphate dehydrogenase (NAD).\n" +
                    "AN   NAD-dependent glycerol phosphate dehydrogenase.\n" +
                    "AN   NAD-dependent glycerol-3-phosphate dehydrogenase.\n" +
                    "CA   Sn-glycerol 3-phosphate + NAD(+) = glycerone phosphate + NADH.\n" +
                    "CC   -!- Also acts on 1,2-propanediol phosphate and glycerone sulfate (but\n" +
                    "CC       with a much lower affinity).\n" +
                    "PR   PROSITE; PDOC00740;\n" +
                    "DR   Q6J5J3, GPD1_SACBA ;  Q9UVF4, GPD1_YARLI ;  Q00055, GPD1_YEAST ;\n" +
                    "DR   Q9HGY2, GPD1_ZYGRO ;  P41911, GPD2_YEAST ;  Q9HGY1, GPD2_ZYGRO ;\n" +
                    "DR   P34517, GPDA_CAEEL ;  P52425, GPDA_CUPLA ;  Q27556, GPDA_DROAE ;\n" +
                    "DR   Q27567, GPDA_DROEZ ;  O97463, GPDA_DROKA ;  P13706, GPDA_DROME ;\n" +
                    "DR   Q27928, GPDA_DROPS ;  P07735, GPDA_DROVI ;  O57656, GPDA_FUGRU ;\n" +
                    "DR   P21695, GPDA_HUMAN ;  P13707, GPDA_MOUSE ;  P08507, GPDA_RABIT ;\n" +
                    "DR   O35077, GPDA_RAT   ;  P21696, GPDA_SCHPO ;  P90593, GPDA_TRYBB ;\n" +
                    "DR   Q26756, GPDA_TRYBR ;  Q09845, GPDB_SCHPO ;\n" +
                    "//\n";
    String entryB = "ID   1.1.1.8\n" +
                    "DE   Glycerol-3-phosphate dehydrogenase (NAD+).\n" +
                    "AN   Glycerophosphate dehydrogenase (NAD).\n" +
                    "AN   NAD-dependent glycerol phosphate dehydrogenase.\n" +
                    "AN   NAD-dependent glycerol-3-phosphate dehydrogenase.\n" +
                    "CA   Sn-glycerol 3-phosphate + NAD(+) = glycerone phosphate + NADH.\n" +
                    "CC   -!- Also acts on 1,2-propanediol phosphate and glycerone sulfate (but\n" +
                    "CC       with a much lower affinity).\n" +
                    "PR   PROSITE; PDOC00740;\n" +
                    "DR   Q6J5J3, GPD1_SACBA ;  Q9UVF4, GPD1_YARLI ;  Q00055, GPD1_YEAST ;\n" +
                    "DR   Q9HGY2, GPD1_ZYGRO ;  P41911, GPD2_YEAST ;  Q9HGY1, GPD2_ZYGRO ;\n" +
                    "DR   P34517, GPDA_CAEEL ;  P52425, GPDA_CUPLA ;  Q27556, GPDA_DROAE ;\n" +
                    "DR   Q27567, GPDA_DROEZ ;  O97463, GPDA_DROKA ;  P13706, GPDA_DROME ;\n" +
                    "DR   Q27928, GPDA_DROPS ;  P07735, GPDA_DROVI ;  O57656, GPDA_FUGRU ;\n" +
                    "DR   P21695, GPDA_HUMAN ;  P13707, GPDA_MOUSE ;  P08507, GPDA_RABIT ;\n" +
                    "DR   O35077, GPDA_RAT   ;  P21696, GPDA_SCHPO ;  P90593, GPDA_TRYBB ;\n" +
                    "DR   Q26756, GPDA_TRYBR ;  Q09845, GPDB_SCHPO ;\n" +
                    "//\n";
    String expectedDifferences = "";

    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));
  }

  public void testCompareEntries_DIFFERENT_ENTRIES() throws Exception {
    String entryA = "ID   1.1.1.8\n" +
                    "DE   Glycerol-3-phosphate dehydrogenase (NAD+).\n" +
                    "AN   Glycerophosphate dehydrogenase (NAD).\n" +
                    "AN   NAD-dependent glycerol phosphate dehydrogenase.\n" +
                    "AN   NAD-dependent glycerol-3-phosphate dehydrogenase.\n" +
                    "CA   Sn-glycerol 3-phosphate + NAD(+) = glycerone phosphate + NADH.\n" +
                    "CC   -!- Also acts on 1,2-propanediol phosphate and glycerone sulfate (but\n" +
                    "CC       with a much lower affinity).\n" +
                    "PR   PROSITE; PDOC00740;\n" +
                    "DR   Q6J5J3, GPD1_SACBA ;  Q9UVF4, GPD1_YARLI ;  Q00055, GPD1_YEAST ;\n" +
                    "DR   Q9HGY2, GPD1_ZYGRO ;  P41911, GPD2_YEAST ;  Q9HGY1, GPD2_ZYGRO ;\n" +
                    "DR   P34517, GPDA_CAEEL ;  P52425, GPDA_CUPLA ;  Q27556, GPDA_DROAE ;\n" +
                    "DR   Q27567, GPDA_DROEZ ;  O97463, GPDA_DROKA ;  P13706, GPDA_DROME ;\n" +
                    "DR   Q27928, GPDA_DROPS ;  P07735, GPDA_DROVI ;  O57656, GPDA_FUGRU ;\n" +
                    "DR   P21695, GPDA_HUMAN ;  P13707, GPDA_MOUSE ;  P08507, GPDA_RABIT ;\n" +
                    "DR   O35077, GPDA_RAT   ;  P21696, GPDA_SCHPO ;  P90593, GPDA_TRYBB ;\n" +
                    "DR   Q26756, GPDA_TRYBR ;  Q09845, GPDB_SCHPO ;\n" +
                    "//\n";
    String entryB = "ID   1.1.1.1\n" +
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
                    "DR   P43067, ADH1_CANAL ;  P48814, ADH1_CERCA ;  P23991, ADH1_CHICK ;\n" +
                    "DR   P23236, ADH1_DROHY ;  P48586, ADH1_DROMN ;  P09370, ADH1_DROMO ;\n" +
                    "DR   P22246, ADH1_DROMT ;  P07161, ADH1_DROMU ;  P12854, ADH1_DRONA ;\n" +
                    "DR   P08843, ADH1_EMENI ;  P05336, ADH1_HORVU ;  P20369, ADH1_KLULA ;\n" +
                    "DR   Q07288, ADH1_KLUMA ;  P00333, ADH1_MAIZE ;  P80512, ADH1_NAJNA ;\n" +
                    "DR   Q9P6C8, ADH1_NEUCR ;  P20306, ADH1_ORYSA ;  P12886, ADH1_PEA   ;\n" +
                    "DR   P14219, ADH1_PENAM ;  P25141, ADH1_PETHY ;  O00097, ADH1_PICST ;\n" +
                    "DR   Q03505, ADH1_RABIT ;  P22797, ADH1_RANPE ;  P14673, ADH1_SOLTU ;\n" +
                    "DR   P80338, ADH1_STRCA ;  P13603, ADH1_TRIRP ;  P00330, ADH1_YEAST ;\n" +
                    "DR   Q07264, ADH1_ZEALU ;  P20368, ADH1_ZYMMO ;  P42327, ADH2_BACST ;\n" +
                    "DR   O45687, ADH2_CAEEL ;  O94038, ADH2_CANAL ;  P48815, ADH2_CERCA ;\n" +
                    "DR   P27581, ADH2_DROAR ;  P25720, ADH2_DROBU ;  P23237, ADH2_DROHY ;\n" +
                    "DR   P48587, ADH2_DROMN ;  P09369, ADH2_DROMO ;  P07160, ADH2_DROMU ;\n" +
                    "DR   P24267, ADH2_DROWH ;  P37686, ADH2_ECOLI ;  P54202, ADH2_EMENI ;\n" +
                    "DR   Q24803, ADH2_ENTHI ;  P10847, ADH2_HORVU ;  P49383, ADH2_KLULA ;\n" +
                    "DR   Q9P4C2, ADH2_KLUMA ;  P28032, ADH2_LYCES ;  P04707, ADH2_MAIZE ;\n" +
                    "DR   P18332, ADH2_ORYSA ;  P41681, ADH2_PERMA ;  O13309, ADH2_PICST ;\n" +
                    "DR   P14674, ADH2_SOLTU ;  P80468, ADH2_STRCA ;  P00331, ADH2_YEAST ;\n" +
                    "DR   P06758, ADH2_ZYMMO ;  P42328, ADH3_BACST ;  P19631, ADH3_COTJA ;\n" +
                    "DR   P25437, ADH3_ECOLI ;  P07754, ADH3_EMENI ;  P44557, ADH3_HAEIN ;\n" +
                    "DR   P10848, ADH3_HORVU ;  P49384, ADH3_KLULA ;  P39450, ADH3_PASPI ;\n" +
                    "DR   P14675, ADH3_SOLTU ;  P73138, ADH3_SYNY3 ;  P07246, ADH3_YEAST ;\n" +
                    "DR   P08319, ADH4_HUMAN ;  P49385, ADH4_KLULA ;  Q9QYY9, ADH4_MOUSE ;\n" +
                    "DR   Q64563, ADH4_RAT   ;  Q09669, ADH4_SCHPO ;  P10127, ADH4_YEAST ;\n" +
                    "DR   Q6XQ67, ADH5_SACPS ;  P38113, ADH5_YEAST ;  P28332, ADH6_HUMAN ;\n" +
                    "DR   P40394, ADH7_HUMAN ;  Q64437, ADH7_MOUSE ;  P41682, ADH7_RAT   ;\n" +
                    "DR   Q64413, ADHA_GEOBU ;  Q64415, ADHA_GEOKN ;  P07327, ADHA_HUMAN ;\n" +
                    "DR   P00329, ADHA_MOUSE ;  P41680, ADHA_PERMA ;  P06757, ADHA_RAT   ;\n" +
                    "DR   O31186, ADHA_RHIME ;  P25405, ADHA_UROHA ;  P00325, ADHB_HUMAN ;\n" +
                    "DR   Q7U1B9, ADHB_MYCBO ;  P71818, ADHB_MYCTU ;  Q5R1W2, ADHB_PANTR ;\n" +
                    "DR   P25406, ADHB_UROHA ;  P33744, ADHE_CLOAB ;  P17547, ADHE_ECOLI ;\n" +
                    "DR   P00327, ADHE_HORSE ;  P00326, ADHG_HUMAN ;  P81600, ADHH_GADMO ;\n" +
                    "DR   P72324, ADHI_RHOSH ;  P81601, ADHL_GADMO ;  P39451, ADHP_ECOLI ;\n" +
                    "DR   O46649, ADHP_RABIT ;  O46650, ADHQ_RABIT ;  P00328, ADHS_HORSE ;\n" +
                    "DR   Q96533, ADHX_ARATH ;  Q17335, ADHX_CAEEL ;  P46415, ADHX_DROME ;\n" +
                    "DR   P19854, ADHX_HORSE ;  P11766, ADHX_HUMAN ;  P93629, ADHX_MAIZE ;\n" +
                    "DR   P28474, ADHX_MOUSE ;  P80360, ADHX_MYXGL ;  P81431, ADHX_OCTVU ;\n" +
                    "DR   P93436, ADHX_ORYSA ;  P80572, ADHX_PEA   ;  O19053, ADHX_RABIT ;\n" +
                    "DR   P12711, ADHX_RAT   ;  P79896, ADHX_SPAAU ;  P80467, ADHX_UROHA ;\n" +
                    "DR   P14940, ADH_ALCEU  ;  P30350, ADH_ANAPL  ;  Q9NAR7, ADH_BACOL  ;\n" +
                    "DR   Q00669, ADH_DROAD  ;  P21518, ADH_DROAF  ;  P25139, ADH_DROAM  ;\n" +
                    "DR   P48584, ADH_DROBO  ;  Q00670, ADH_DROCR  ;  P22245, ADH_DRODI  ;\n" +
                    "DR   Q9NG42, ADH_DROEQ  ;  P28483, ADH_DROER  ;  P48585, ADH_DROFL  ;\n" +
                    "DR   P51551, ADH_DROGR  ;  Q09009, ADH_DROGU  ;  P51549, ADH_DROHA  ;\n" +
                    "DR   P21898, ADH_DROHE  ;  Q07588, ADH_DROIM  ;  Q9NG40, ADH_DROIN  ;\n" +
                    "DR   Q27404, ADH_DROLA  ;  P10807, ADH_DROLE  ;  P07162, ADH_DROMA  ;\n" +
                    "DR   Q09010, ADH_DROMD  ;  P00334, ADH_DROME  ;  Q00671, ADH_DROMM  ;\n" +
                    "DR   P25721, ADH_DROMY  ;  Q00672, ADH_DRONI  ;  P07159, ADH_DROOR  ;\n" +
                    "DR   P84328, ADH_DROPB  ;  P37473, ADH_DROPE  ;  P23361, ADH_DROPI  ;\n" +
                    "DR   P23277, ADH_DROPL  ;  Q6LCE4, ADH_DROPS  ;  Q9U8S9, ADH_DROPU  ;\n" +
                    "DR   Q9GN94, ADH_DROSE  ;  Q24641, ADH_DROSI  ;  P23278, ADH_DROSL  ;\n" +
                    "DR   Q03384, ADH_DROSU  ;  P28484, ADH_DROTE  ;  P51550, ADH_DROTS  ;\n" +
                    "DR   Q05114, ADH_DROWI  ;  P26719, ADH_DROYA  ;  P17648, ADH_FRAAN  ;\n" +
                    "DR   P26325, ADH_GADCA  ;  P28469, ADH_MACMU  ;  P48977, ADH_MALDO  ;\n" +
                    "DR   P81786, ADH_MORSE  ;  P14139, ADH_PAPHA  ;  P25988, ADH_SCAAL  ;\n" +
                    "DR   P00332, ADH_SCHPO  ;  P39462, ADH_SULSO  ;  P50381, ADH_SULSR  ;\n" +
                    "DR   P51552, ADH_ZAPTU  ;  P32771, FADH_YEAST ;  P71017, GBSB_BACSU ;\n" +
                    "DR   P33010, TERPD_PSESP;\n" +
                    "//\n";
    String expectedDifferences = "ID line is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "DE line is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "AN line 1 is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "CA line 1 is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "CC line 1 is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "PR line 1 is different for entry: 1.1.1.8 (EC of entryA)\n" +
                                 "DR line 1 is different for entry: 1.1.1.8 (EC of entryA)\n";
    expectedDifferences = "MISSING INTENZ ENTRY: 1.1.1.1 does not exist in ENZYME";
    assertEquals(expectedDifferences, FlatFileComparator.compareEntries(entryA, entryB));
  }

}
