package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * XCharsASCIITranslator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>10/27/2004</pre>
 */
public class XCharsASCIITranslatorTest extends TestCase {
  private XCharsASCIITranslator translator = XCharsASCIITranslator.getInstance();

  public XCharsASCIITranslatorTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testToXCharsFormat1() throws Exception {
    String expected = "[RNA-polymerase]-subunit kinase";
    String input = "[RNA-polymerase]-subunit kinase";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "ADAMTS-4 endopeptidase";
    input = "ADAMTS-4 endopeptidase";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "<stereo>cis</stereo>-1,2-dihydrobenzene-1,2-diol dehydrogenase.";
    input = "Cis-1,2-dihydrobenzene-1,2-diol dehydrogenase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "<stereo>L</stereo>-xylulose reductase.";
    input = "L-xylulose reductase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "(<stereo>S</stereo>)-carnitine 3-dehydrogenase.";
    input = "(S)-carnitine 3-dehydrogenase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "<element>N</element>-acetylhexosamine 1-dehydrogenase.";
    input = "N-acetylhexosamine 1-dehydrogenase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "<stereo>all-trans</stereo>-retinyl-palmitate hydrolase.";
    input = "All-trans-retinyl-palmitate hydrolase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "UDP-2-acetamido-4-amino-2,4,6-trideoxyglucose transaminase";
    input = "UDP-2-acetamido-4-amino-2,4,6-trideoxyglucose transaminase";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "Precorrin-6Y <element>C</element>(5,15)-methyltransferase (decarboxylating).";
    input = "Precorrin-6Y C(5,15)-methyltransferase (decarboxylating).";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "Aldehyde dehydrogenase (FAD-independent).";
    input = "Aldehyde dehydrogenase (FAD-independent).";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "[Acyl-carrier-protein] <element>S</element>-malonyltransferase.";
    input = "[Acyl-carrier-protein] S-malonyltransferase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "Cycloartenol 24-<element>C</element>-methyltransferase.";
    input = "Cycloartenol 24-C-methyltransferase.";
    assertEquals(expected, translator.toXCharsFormat(input, false));

    expected = "Alpha-macroglobulins are cleaved much more rapidly.";
    input = "<greek>alpha</greek>-macroglobulins are cleaved much more rapidly.";
    assertEquals(expected, translator.toASCII(input, false, false));

	input = "<greek>alpha</greek>II-spectrin, ...";
	expected = "Alpha-II-spectrin, ...";
    assertEquals(expected, translator.toASCII(input, false, false));
  }

//  public void testToASCII() throws Exception {
//////    \\<b\\>m\\<\\/b\\>=(M)\u0020
//    String expected = "(M) ";
//    String input = "<b>m</b>";
//    assertEquals(expected, translator.toASCII(input, true));
//    expected = "3-alpha-hydroxysteroid dehydrogenase (B-specific)";
//    input = "3<greek>alpha</greek>-hydroxysteroid dehydrogenase (B-specific)";
//    assertEquals(expected, translator.toASCII(input, false));
//    expected = "The enzyme from Escherichia coli requires Mg(2+) or Mn(2+). ATP or UTP can replace CTP, but both are less effective. GTP and TTP are not substrates. Forms part of an alternative nonmevalonate pathway for terpenoid biosynthesis (for diagram, click here)";
//    input = "The enzyme from Escherichia coli requires Mg<smallsup>2+</smallsup> or Mn<smallsup>2+</smallsup>. ATP or UTP can replace CTP, but both are less effective. GTP and TTP are not substrates. Forms part of an alternative nonmevalonate pathway for terpenoid biosynthesis (for diagram, <a href=\"http://www.chem.qmul.ac.uk/iubmb/enzyme/reaction/terp/nonMVA.html\">click here</a>)";
//    assertEquals(expected, translator.toASCII(input, false));
//  }

  public static Test suite() {
    return new TestSuite(XCharsASCIITranslatorTest.class);
  }
}
