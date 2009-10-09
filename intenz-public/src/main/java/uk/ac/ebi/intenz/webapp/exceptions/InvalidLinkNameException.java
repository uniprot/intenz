package uk.ac.ebi.intenz.webapp.exceptions;

/** 
 * This exception is thrown when a link was requested but the given name is not supported.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class InvalidLinkNameException extends Exception {
  public InvalidLinkNameException() {
  }

  public InvalidLinkNameException(String message) {
    super(message);
  }
}
