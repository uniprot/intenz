package uk.ac.ebi.intenz.domain.constants;

/**
 * Events for enzyme entries.
 * @author rafalcan
 *
 */
public enum Event {

	DELETION("DEL", "deleted"),
	TRANSFER("TRA", "transferred"),
	MODIFICATION("MOD", "modified"),
	CREATION("NEW", "created");
	
	/**
	 * Short code for the event.
	 */
	private String code;
	
	/**
	 * The attribute given to an enzyme entry after the event.
	 */
	private String attribute;
	
	private Event(String code, String attribute){
		this.code = code;
		this.attribute = attribute;
	}
	
	public Event fromCode(String code){
		for (Event e : Event.values()){
			if (e.code.equals(code)) return e;
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getAttribute() {
		return attribute;
	}
}
