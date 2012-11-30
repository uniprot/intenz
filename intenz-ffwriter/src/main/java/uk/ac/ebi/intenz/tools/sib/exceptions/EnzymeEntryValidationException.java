package uk.ac.ebi.intenz.tools.sib.exceptions;

/**
 * This exception is used to indicate an error in the syntax of an ENZYME entry.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class EnzymeEntryValidationException extends Exception {

  public EnzymeEntryValidationException(String message) {
    super(message);
  }

}
