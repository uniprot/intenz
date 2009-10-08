package uk.ac.ebi.intenz.domain.exceptions;


/**
 * This exception is thrown when a EC number is not valid (e.g. used numbers are out of range) or cannot be assigned
 * because the corresponding class/subclass/sub-subclass does not exist.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class EcException extends DomainException {

	private static final long serialVersionUID = 7460755784911875672L;

	public EcException(){}

	public EcException(String message) {
		super(message);
	}
}
