package uk.ac.ebi.intenz.domain.exceptions;


/**
 * If an error regarding an enzyme name occurs this exception should be thrown.
 * <p/>
 * Currently this <code>Exception</code> is thrown when:
 * <ul>
 * <li>a client tries to store more than 2 common names for the same enzyme</li>
 * <li>a client attempts to store an empty list of common names</li>
 * <li>a name has the wrong type associated (see {@link uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant})</li>
 * </ul>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class EnzymeNameException extends DomainException {

	private static final long serialVersionUID = 5676315508371150769L;

	public EnzymeNameException(){}

	public EnzymeNameException(String message) {
		super(message);
	}
}
