package uk.ac.ebi.intenz.stats;

import java.util.Date;
import java.util.Map;

import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.stats.db.XrefsStats;

/**
 * IntEnz statistics.
 * @author rafalcan
 * @since 1.1.0
 */
public interface IIntEnzStatistics {

	public int getReleaseNumber();
	
	public Date getReleaseDate();
	
	public int getClasses();
	
	public int getSubclasses();
	
	public int getSubSubclasses();
	
	/**
	 * Retrieves the number of enzymes by status.
	 * @return a nested map of status (String) to active (Boolean) and number
	 * 		of reactions.
	 */
	public Map<String, Map<Boolean, Integer>> getEnzymesByStatus();
	
	public int getSynonyms();
	
	public Map<XrefDatabaseConstant, XrefsStats> getXrefs();
	
	public Map<XrefDatabaseConstant, XrefsStats> getLinks();

}
