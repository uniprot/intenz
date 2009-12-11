package uk.ac.ebi.intenz.webapp.helper;

public class HyperlinkHelper {

	public static String linkEcNumbers(String s){
		return s.replaceAll(" (\\d+\\.\\d+\\.\\d+\\.\\d+)([ ,\\.])",
		" <a class=\"pre_anchor\" href=\"searchEc.do?ec=$1&view=INTENZ\">$1</a>$2");
	}
}
