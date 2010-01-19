package uk.ac.ebi.intenz.tools.sib.sptr_enzyme;

import uk.ac.ebi.interfaces.sptr.SPTRException;

/** 
 * This exception is thrown when cross references are created with wrong values
 * (for example database names).
 *
 * @author Michael Darsow
 * @version preliminary - $Revision: 1.2 $ $Date: 2008/01/28 11:43:22 $
 */
public class EnzymeCrossReferenceException extends SPTRException {

  public EnzymeCrossReferenceException(String message) {
    super(message);
  }

  public EnzymeCrossReferenceException(Exception originalException) {
    super(originalException);
  }

  public EnzymeCrossReferenceException(String message, Exception originalException) {
    super(message, originalException);
  }

}
