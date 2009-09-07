package uk.ac.ebi.intenz.domain.constants;

/**
 * Enzyme entry status.
 * @author rafalcan
 *
 */
public enum Status {

	APPROVED("OK"),
	SUGGESTED("SU"),
	PROPOSED("PR"),
	PRELIMINARY("PM");
	
	private String code;
	
	private Status(String code){ this.code = code; }
	
	public String getCode(){ return code; }
	
}
