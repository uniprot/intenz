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
	
	public Status fromCode(String code){
		for (Status status : Status.values()){
			if (status.code.equals(code)) return status;
		}
		return null;
	}
	
}
