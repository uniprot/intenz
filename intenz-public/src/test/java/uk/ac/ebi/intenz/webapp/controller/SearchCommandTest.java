package uk.ac.ebi.intenz.webapp.controller;

import junit.framework.TestCase;

/**
 * This class ...
 * 
 * @author Michael Darsow
 * @version 0.0 12-Jul-2004
 */
public class SearchCommandTest extends TestCase {
  SearchCommand searchCommand;
  public SearchCommandTest(String name) {
    super(name);
    searchCommand = new SearchCommand();
  }

    @Override
  public void setUp() throws Exception {
    super.setUp();
  }

    @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

  public void testDecodeQuery() throws Exception {
//    assertEquals("Some text for testing &#163; it", searchCommand.decodeQuery(new StringBuffer("q=Some text for testing %C2%A3 it")));
//    assertEquals("Some text &#241; for testing &#163; it", searchCommand.decodeQuery(new StringBuffer("q=Some text %C3%B1 for testing %C2%A3 it")));
//    assertEquals("Some text for testing&#163;abc", searchCommand.decodeQuery(new StringBuffer("q=Some text for testing%C2%A3abc")));
//    assertEquals("Some text&#241;abc testing&#163;abc", searchCommand.decodeQuery(new StringBuffer("q=Some text%C3%B1abc testing%C2%A3abc")));
  }
}