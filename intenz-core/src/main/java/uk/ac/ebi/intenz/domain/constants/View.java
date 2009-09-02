package uk.ac.ebi.intenz.domain.constants;


/**
 * Enumeration for web views of IntEnz data.
 * Made to replace {@link EnzymeViewConstant}, perhaps moving to a webapp-only
 * module?
 * @author rafalcan
 *
 */
public enum View {

	INTENZ("IntEnz"),
	IUBMB("NC-IUBMB"),
	SIB("ENZYME");
	
	private String txt;

	private View(String txt){ this.txt = txt; }

	public String getLabel() {
		return txt;
	}

	
}
