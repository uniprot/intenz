package uk.ac.ebi.intenz.domain.constants;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;

/**
 * Enzyme entry status.
 * @author rafalcan
 *
 */
public enum Status {

	/**
	 * This preliminary status should only be applied to enzymes with
	 * {@link Type#PRELIMINARY preliminary} EC type and vice-versa.
	 */
	PRELIMINARY("PM"),
	SUGGESTED("SU"),
	PROPOSED("PR"),
	APPROVED("OK");
	
	private String code;
	
	private Status(String code){ this.code = code; }
	
	public String getCode(){ return code; }
	
	public static Status fromCode(String code){
		for (Status status : Status.values()){
			if (status.code.equals(code)) return status;
		}
		return null;
	}
	
}
