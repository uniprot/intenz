package uk.ac.ebi.intenz.domain.exceptions;


/**
 * If an error regarding an enzyme reaction occurs this exception should be thrown.
 * <p/>
 * Currently this <code>Exception</code> is thrown when:
 * <ul>
 * <li>a client attempts to store an empty list of reactions</li>
 * </ul>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class EnzymeReactionException extends DomainException {

	private static final long serialVersionUID = -5288612034876625163L;

	public EnzymeReactionException(){}

	public EnzymeReactionException(String message) {
		super(message);
	}
}
