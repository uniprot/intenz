package uk.ac.ebi.intenz.domain.exceptions;


/**
 * If an error regarding an enzyme reference occurs this exception should be thrown.
 * <p/>
 * Currently this <code>Exception</code> is thrown when:
 * <ul>
 * <li>a client attempts to store an empty list of references</li>
 * </ul>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class EnzymeReferenceException extends DomainException {

	private static final long serialVersionUID = 1347812603822224978L;

	public EnzymeReferenceException(){}

	public EnzymeReferenceException(String message) {
		super(message);
	}
}
