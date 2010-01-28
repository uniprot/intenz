package uk.ac.ebi.intenz.tools.sib.helper;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * FFWriterHelper Tester.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/07/09 09:26:34 $
 */
public class FFWriterHelperTest extends TestCase {
  public FFWriterHelperTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

  public static Test suite() {
    return new TestSuite(FFWriterHelperTest.class);
  }


  public void testCreateXrefHyperlinks() throws Exception {
    // Test "" as parameter value.
    assertEquals("", FFWriterHelper.createXrefHyperlinks(""));

    // Test null as parameter value.
    try {
      FFWriterHelper.createXrefHyperlinks(null);
      fail("NullPointerException expected.");
    } catch (NullPointerException e) {
      assertEquals(e.getMessage(), "Parameter 'flatFile' must not be null.");
    }

    // Test single PR line.
    assertEquals("PR   PROSITE; <a class=\"pre_anchor\" href=\"http://www.expasy.org/prosite/PDOC00058\" target=\"_blank\">PDOC00058</a>;\n",
                 FFWriterHelper.createXrefHyperlinks("PR   PROSITE; PDOC00058;\n"));

    // Test multiple PR lines.
    assertEquals("PR   PROSITE; <a class=\"pre_anchor\" href=\"http://www.expasy.org/prosite/PDOC00058\" target=\"_blank\">PDOC00058</a>;\n" +
                 "PR   PROSITE; <a class=\"pre_anchor\" href=\"http://www.expasy.org/prosite/PDOC00059\" target=\"_blank\">PDOC00059</a>;\n" +
                 "PR   PROSITE; <a class=\"pre_anchor\" href=\"http://www.expasy.org/prosite/PDOC00060\" target=\"_blank\">PDOC00060</a>;\n",
                 FFWriterHelper.createXrefHyperlinks("PR   PROSITE; PDOC00058;\n" +
                                                     "PR   PROSITE; PDOC00059;\n" +
                                                     "PR   PROSITE; PDOC00060;\n"));

    // Test single DI line.
    assertEquals("DI   Exertional myoglobinuria; <a class=\"pre_anchor\" href=\"http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=150000\" target=\"_blank\">MIM:150000</a>.\n",
                 FFWriterHelper.createXrefHyperlinks("DI   Exertional myoglobinuria; MIM:150000.\n"));

    // Test multiple DI lines.
    assertEquals("DI   Exertional myoglobinuria; <a class=\"pre_anchor\" href=\"http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=150000\" target=\"_blank\">MIM:150000</a>.\n" +
                 "DI   Lactate dehydrogenase B chain deficiency; <a class=\"pre_anchor\" href=\"http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=150100\" target=\"_blank\">MIM:150100</a>.\n",
                 FFWriterHelper.createXrefHyperlinks("DI   Exertional myoglobinuria; MIM:150000.\n" +
                                                     "DI   Lactate dehydrogenase B chain deficiency; MIM:150100.\n"));

    // Test single DR link.
    assertEquals("DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81RW4\" target=\"_blank\">Q81RW4</a>, LDH1_BACAN ;\n",
                 FFWriterHelper.createXrefHyperlinks("DR   Q81RW4, LDH1_BACAN ;\n"));

    // Test single DR line.
    assertEquals("DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81RW4\" target=\"_blank\">Q81RW4</a>, LDH1_BACAN ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/P62047\" target=\"_blank\">P62047</a>, LDH1_BACC1 ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81EP4\" target=\"_blank\">Q81EP4</a>, LDH1_BACCR ;\n",
                 FFWriterHelper.createXrefHyperlinks("DR   Q81RW4, LDH1_BACAN ;  P62047, LDH1_BACC1 ;  Q81EP4, LDH1_BACCR ;\n"));

    // Test single DR line with only one xref.
    assertEquals("DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81RW4\" target=\"_blank\">Q81RW4</a>, LDH1_BACAN ;\n",
                 FFWriterHelper.createXrefHyperlinks("DR   Q81RW4, LDH1_BACAN ;\n"));

    // Test single DR line with only two xrefs.
    assertEquals("DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81RW4\" target=\"_blank\">Q81RW4</a>, LDH1_BACAN ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/P62047\" target=\"_blank\">P62047</a>, LDH1_BACC1 ;\n",
                 FFWriterHelper.createXrefHyperlinks("DR   Q81RW4, LDH1_BACAN ;  P62047, LDH1_BACC1 ;\n"));

    // Test multiple DR lines.
    assertEquals("DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81RW4\" target=\"_blank\">Q81RW4</a>, LDH1_BACAN ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/P62047\" target=\"_blank\">P62047</a>, LDH1_BACC1 ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q81EP4\" target=\"_blank\">Q81EP4</a>, LDH1_BACCR ;\n" +
                 "DR   <a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/P59050\" target=\"_blank\">P59050</a>, LDH1_BIFLO ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q97MD1\" target=\"_blank\">Q97MD1</a>, LDH1_CLOAB ;  " +
                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/Q839C1\" target=\"_blank\">Q839C1</a>, LDH1_ENTFA ;\n",
                 FFWriterHelper.createXrefHyperlinks("DR   Q81RW4, LDH1_BACAN ;  P62047, LDH1_BACC1 ;  Q81EP4, LDH1_BACCR ;\n" +
                                                     "DR   P59050, LDH1_BIFLO ;  Q97MD1, LDH1_CLOAB ;  Q839C1, LDH1_ENTFA ;\n"));
  }


  private static final String ENTRY = "ID   1.1.1.1\n" +
                                      "DE   Alcohol dehydrogenase.\n" +
                                      "AN   Aldehyde reductase.\n" +
                                      "CA   An alcohol + NAD+ = an aldehyde or ketone + NADH.\n" +
                                      "CF   Zinc or Iron.\n" +
                                      "CC   -!- Acts on primary or secondary alcohols or hemiacetals.\n" +
                                      "CC   -!- The animal, but not the yeast, enzyme acts also on cyclic secondary\n" +
                                      "CC       alcohols.\n" +
                                      "PR   PROSITE; PDOC00058;\n" +
                                      "PR   PROSITE; PDOC00059;\n" +
                                      "PR   PROSITE; PDOC00060;\n" +
                                      "DR   P80222, ADH1_ALLMI;  P49645, ADH1_APTAU;  P06525, ADH1_ARATH;\n" +
                                      "DR   P41747, ADH1_ASPFL;  P12311, ADH1_BACST;  Q17334, ADH1_CAEEL;\n" +
                                      "DR   P43067, ADH1_CANAL;  P48814, ADH1_CERCA;  P23991, ADH1_CHICK;\n" +
                                      "DR   P23236, ADH1_DROHY;  P48586, ADH1_DROMN;  P09370, ADH1_DROMO;\n" +
                                      "DR   P22246, ADH1_DROMT;  P07161, ADH1_DROMU;  P12854, ADH1_DRONA;\n" +
                                      "DR   P08843, ADH1_EMENI;  P05336, ADH1_HORVU;  P20369, ADH1_KLULA;\n" +
                                      "DR   Q07288, ADH1_KLUMA;  P00333, ADH1_MAIZE;  P80512, ADH1_NAJNA;\n" +
                                      "DR   Q9P6C8, ADH1_NEUCR;  P20306, ADH1_ORYSA;  P12886, ADH1_PEA  ;\n" +
                                      "DR   P14219, ADH1_PENAM;  P25141, ADH1_PETHY;  O00097, ADH1_PICST;\n" +
                                      "DR   Q03505, ADH1_RABIT;  P22797, ADH1_RANPE;  P14673, ADH1_SOLTU;\n" +
                                      "DR   P80338, ADH1_STRCA;  P13603, ADH1_TRIRP;  P00330, ADH1_YEAST;\n" +
                                      "DR   Q07264, ADH1_ZEALU;  P20368, ADH1_ZYMMO;  P42327, ADH2_BACST;\n" +
                                      "DR   O45687, ADH2_CAEEL;  O94038, ADH2_CANAL;  P48815, ADH2_CERCA;\n" +
                                      "DR   P27581, ADH2_DROAR;  P25720, ADH2_DROBU;  P23237, ADH2_DROHY;\n" +
                                      "DR   P48587, ADH2_DROMN;  P09369, ADH2_DROMO;  P07160, ADH2_DROMU;\n" +
                                      "DR   P24267, ADH2_DROWH;  P37686, ADH2_ECOLI;  P54202, ADH2_EMENI;\n" +
                                      "DR   Q24803, ADH2_ENTHI;  P10847, ADH2_HORVU;  P49383, ADH2_KLULA;\n" +
                                      "DR   Q9P4C2, ADH2_KLUMA;  P28032, ADH2_LYCES;  P04707, ADH2_MAIZE;\n" +
                                      "DR   P18332, ADH2_ORYSA;  P41681, ADH2_PERMA;  O13309, ADH2_PICST;\n" +
                                      "DR   P14674, ADH2_SOLTU;  P80468, ADH2_STRCA;  P00331, ADH2_YEAST;\n" +
                                      "DR   P06758, ADH2_ZYMMO;  P42328, ADH3_BACST;  P19631, ADH3_COTJA;\n" +
                                      "DR   P25437, ADH3_ECOLI;  P07754, ADH3_EMENI;  P44557, ADH3_HAEIN;\n" +
                                      "DR   P10848, ADH3_HORVU;  P49384, ADH3_KLULA;  P39450, ADH3_PASPI;\n" +
                                      "DR   P14675, ADH3_SOLTU;  P73138, ADH3_SYNY3;  P07246, ADH3_YEAST;\n" +
                                      "DR   P08319, ADH4_HUMAN;  P49385, ADH4_KLULA;  Q9QYY9, ADH4_MOUSE;\n" +
                                      "DR   Q64563, ADH4_RAT  ;  P10127, ADH4_YEAST;  Q6XQ67, ADH5_SACPS;\n" +
                                      "DR   P38113, ADH5_YEAST;  P28332, ADH6_HUMAN;  P40394, ADH7_HUMAN;\n" +
                                      "DR   Q64437, ADH7_MOUSE;  P41682, ADH7_RAT  ;  Q64413, ADHA_GEOBU;\n" +
                                      "DR   Q64415, ADHA_GEOKN;  P07327, ADHA_HUMAN;  P00329, ADHA_MOUSE;\n" +
                                      "DR   P41680, ADHA_PERMA;  P06757, ADHA_RAT  ;  O31186, ADHA_RHIME;\n" +
                                      "DR   P25405, ADHA_UROHA;  P00325, ADHB_HUMAN;  Q7U1B9, ADHB_MYCBO;\n" +
                                      "DR   P71818, ADHB_MYCTU;  Q5R1W2, ADHB_PANTR;  P25406, ADHB_UROHA;\n" +
                                      "DR   P33744, ADHE_CLOAB;  P17547, ADHE_ECOLI;  P00327, ADHE_HORSE;\n" +
                                      "DR   Q09669, ADHF_SCHPO;  P00326, ADHG_HUMAN;  P81600, ADHH_GADMO;\n" +
                                      "DR   P72324, ADHI_RHOSH;  P81601, ADHL_GADMO;  P39451, ADHP_ECOLI;\n" +
                                      "DR   O46649, ADHP_RABIT;  O46650, ADHQ_RABIT;  P00328, ADHS_HORSE;\n" +
                                      "DR   Q96533, ADHX_ARATH;  Q17335, ADHX_CAEEL;  P46415, ADHX_DROME;\n" +
                                      "DR   P19854, ADHX_HORSE;  P11766, ADHX_HUMAN;  P93629, ADHX_MAIZE;\n" +
                                      "DR   P28474, ADHX_MOUSE;  P80360, ADHX_MYXGL;  P81431, ADHX_OCTVU;\n" +
                                      "DR   P93436, ADHX_ORYSA;  P80572, ADHX_PEA  ;  O19053, ADHX_RABIT;\n" +
                                      "DR   P12711, ADHX_RAT  ;  P79896, ADHX_SPAAU;  P80467, ADHX_UROHA;\n" +
                                      "DR   P14940, ADH_ALCEU ;  P30350, ADH_ANAPL ;  Q9NAR7, ADH_BACOL ;\n" +
                                      "DR   Q00669, ADH_DROAD ;  P21518, ADH_DROAF ;  P25139, ADH_DROAM ;\n" +
                                      "DR   P48584, ADH_DROBO ;  Q00670, ADH_DROCR ;  P22245, ADH_DRODI ;\n" +
                                      "DR   Q9NG42, ADH_DROEQ ;  P28483, ADH_DROER ;  P48585, ADH_DROFL ;\n" +
                                      "DR   P51551, ADH_DROGR ;  Q09009, ADH_DROGU ;  P51549, ADH_DROHA ;\n" +
                                      "DR   P21898, ADH_DROHE ;  Q07588, ADH_DROIM ;  Q9NG40, ADH_DROIN ;\n" +
                                      "DR   Q27404, ADH_DROLA ;  P10807, ADH_DROLE ;  P07162, ADH_DROMA ;\n" +
                                      "DR   Q09010, ADH_DROMD ;  P00334, ADH_DROME ;  Q00671, ADH_DROMM ;\n" +
                                      "DR   P25721, ADH_DROMY ;  Q00672, ADH_DRONI ;  P07159, ADH_DROOR ;\n" +
                                      "DR   P84328, ADH_DROPB ;  P37473, ADH_DROPE ;  P23361, ADH_DROPI ;\n" +
                                      "DR   P23277, ADH_DROPL ;  Q6LCE4, ADH_DROPS ;  Q9U8S9, ADH_DROPU ;\n" +
                                      "DR   Q9GN94, ADH_DROSE ;  Q24641, ADH_DROSI ;  P23278, ADH_DROSL ;\n" +
                                      "DR   Q03384, ADH_DROSU ;  P28484, ADH_DROTE ;  P51550, ADH_DROTS ;\n" +
                                      "DR   Q05114, ADH_DROWI ;  P26719, ADH_DROYA ;  P17648, ADH_FRAAN ;\n" +
                                      "DR   P26325, ADH_GADCA ;  P28469, ADH_MACMU ;  P48977, ADH_MALDO ;\n" +
                                      "DR   P81786, ADH_MORSE ;  P14139, ADH_PAPHA ;  P25988, ADH_SCAAL ;\n" +
                                      "DR   P00332, ADH_SCHPO ;  P39462, ADH_SULSO ;  P50381, ADH_SULSR ;\n" +
                                      "DR   P51552, ADH_ZAPTU ;  P32771, FADH_YEAST;  P71017, GBSB_BACSU;\n" +
                                      "DR   P33010, TERPD_PSESP;\n" +
                                      "//\n";
}