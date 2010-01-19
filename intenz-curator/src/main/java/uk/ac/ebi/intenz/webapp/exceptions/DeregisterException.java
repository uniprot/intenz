package uk.ac.ebi.intenz.webapp.exceptions;

import uk.ac.ebi.intenz.domain.exceptions.DomainException;

/**
 * This exception is the base exception for all domain exception and just acts as a wrapper.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class DeregisterException extends DomainException {

  public DeregisterException() {
	  super("", null);
  }

  public DeregisterException(String message) {
    super(message);
  }

  public DeregisterException(String property, String messageKey) {
    super(property, messageKey);
  }
}
