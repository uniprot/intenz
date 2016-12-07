package uk.ac.ebi.intenz.webapp;

import junit.framework.TestCase;

public class IntEnzUtilitiesTest extends TestCase {

	public void testLinkEcNumber() {
		String[][] ecStrings = {
				{ "An EC number...", "An EC number..." },
				{ "A dot-delimited code: 1.2 blah blah", "A dot-delimited code: 1.2 blah blah" },
				{ "Another dot-delimited code: 1.2.3 blah blah", "Another dot-delimited code: 1.2.3 blah blah" },
				{ "Another dot-delimited code: 1.2.3.4 blah blah", "Another dot-delimited code: <a href=\"query?cmd=SearchEC&ec=1.2.3.4\">1.2.3.4</a> blah blah" },
				{ "Now EC 1.2.3.4 is the test", "Now EC <a href=\"query?cmd=SearchEC&ec=1.2.3.4\">1.2.3.4</a> is the test" },
				{ "But EC 1.2 is a subclass", "But EC <a href=\"query?cmd=SearchEC&ec=1.2\">1.2</a> is a subclass" },
				{ "And EC 1.2.3 is a sub-subclass", "And EC <a href=\"query?cmd=SearchEC&ec=1.2.3\">1.2.3</a> is a sub-subclass" }
		};
		for (int i = 0; i < ecStrings.length; i++) {
			assertEquals(ecStrings[i][1], IntEnzUtilities.linkEcNumber(ecStrings[i][0], false));
		}
	}

	public void testLink2carb(){
		String[][] s = {
				{ "dummy", "dummy" },
				{ "2carb something", "2carb something" },
				{ "a 2-carb-l link", "a 2-carb-l link" },
				{ "a 2-Carb-l link", "a 2-Carb-l link" },
				{ "a 2-Carb-1 link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/01.html\">2-Carb-1</a> link" },
				{ "a 2-Carb-12 link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/12.html\">2-Carb-12</a> link" },
				{ "a 2-Carb-12.a link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/12.html\">2-Carb-12</a>.a link" },
				{ "a 2-Carb-12.5 link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/12.html#125\">2-Carb-12.5</a> link" },
				{ "a 2-Carb-2.5 link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/02.html#025\">2-Carb-2.5</a> link" },
				{ "a 2-Carb-2.567 link", "a <a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/02.html#025\">2-Carb-2.5</a>67 link" }
		};
		for (int i = 0; i < s.length; i++) {
			assertEquals(s[i][1], IntEnzUtilities.link2carb(s[i][0]));
		}
	}

}
