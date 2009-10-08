package uk.ac.ebi.intenz.tools.sib.writer;

import uk.ac.ebi.interfaces.sptr.SPTRException;

/**
 * This exception is thrown whenever an error occurs during the creation of the ENZYME flat file.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class EnzymeFlatFileWriteException extends SPTRException {
  /**
   * Stores the (lower-level) exception this exception should wrap.
   *
   * @param e the exception to be wrapped.
   */
  public EnzymeFlatFileWriteException(Exception e) {
    super(e);
  }

  /**
   * Stores the exception message and the (lower-level) exception this exception should wrap.
   *
   * @param message the exception message.
   * @param e the exception to be wrapped.
   */
  public EnzymeFlatFileWriteException(String message, Exception e) {
    super(message, e);
  }

  /**
   * Stores the exception message.
   *
   * @param message the exception message.
   */
  public EnzymeFlatFileWriteException(String message) {
    super(message);
  }
}
