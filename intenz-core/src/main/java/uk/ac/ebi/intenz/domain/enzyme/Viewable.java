package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

/**
 * Interface for objects with a web view.
 * @author rafalcan
 *
 */
public interface Viewable {

	public EnzymeViewConstant getView();
	
}
