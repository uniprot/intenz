package uk.ac.ebi.intenz.tools.sib.translator.rules;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * TagRules Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>02/23/2005</pre>
 */
public class TagRulesTest extends TestCase {
  public TagRulesTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testGetInstance() throws Exception {
    //TODO: Test goes here...
  }

  public void testApplyRules() throws Exception {
//    String expected = "N-acetylhexosamine 1-dehydrogenase.";
//    String input = "<element>N</element>-acetylhexosamine 1-dehydrogenase.";
//    assertEquals(expected, TagRules.getInstance().applyRules(input));
//
//    expected = "The enzyme from Escherichia coli requires Mg2+ or Mn2+. ATP or UTP can replace CTP, but both are less effective. GTP and TTP are not substrates. Forms part of an alternative nonmevalonate pathway for terpenoid biosynthesis (for diagram, click here)";
//    input = "The enzyme from Escherichia coli requires Mg<smallsup>2+</smallsup> or Mn<smallsup>2+</smallsup>. ATP or UTP can replace CTP, but both are less effective. GTP and TTP are not substrates. Forms part of an alternative nonmevalonate pathway for terpenoid biosynthesis (for diagram, <a href=\"http://www.chem.qmul.ac.uk/iubmb/enzyme/reaction/terp/nonMVA.html\">click here</a>)";
//    assertEquals(expected, TagRules.getInstance().applyRules(input));
  }

  public void testReverseRules() throws Exception {
    //TODO: Test goes here...
  }

  public static Test suite() {
    return new TestSuite(TagRulesTest.class);
  }
}
