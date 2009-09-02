package uk.ac.ebi.intenz.webapp.exceptions;

/** 
 * This exception is thrown when a EC number is not valid (e.g. used numbers are out of range).
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class InvalidECException extends Exception {
  public InvalidECException() {
  }

  public InvalidECException(String message) {
    super(message);
  }
}
