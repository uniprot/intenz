package uk.ac.ebi.intenz.domain.exceptions;

/**
 * This exception is the base exception for all domain exception and just acts as a wrapper.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class DomainException extends Exception {

	private static final long serialVersionUID = -6121194983313071023L;
	
	String messageKey;
	String property;

  public DomainException() {
    property = "";
  }

  public DomainException(String message) {
    super(message);
    this.property = "";
    this.messageKey = "";
  }

  public DomainException(String property, String messageKey) {
    super("");
    this.property = property;
    this.messageKey = messageKey;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getMessageKey() {
    return messageKey;
  }

  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }
}
