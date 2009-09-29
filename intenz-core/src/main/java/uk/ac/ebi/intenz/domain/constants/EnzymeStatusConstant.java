package uk.ac.ebi.intenz.domain.constants;


/**
 * This class represents an enumeration of all database source types.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/04/16 15:01:04 $
 * @deprecated use enumeration {@link Status} instead.
 */
public class EnzymeStatusConstant {
  private final String statusCode;

  private final String text;

  public static final EnzymeStatusConstant APPROVED = new EnzymeStatusConstant("OK", "approved");
  public static final EnzymeStatusConstant SUGGESTED = new EnzymeStatusConstant("SU", "suggested");
  public static final EnzymeStatusConstant PROPOSED = new EnzymeStatusConstant("PR", "proposed");

  /**
   * Returns the corresponding instance of the given status code.
   * <p/>
   * If the status code does not match any code an exception is thrown.
   *
   * @param statusCode The status code.
   * @return the class constant corresponding to the given code.
   * @throws NullPointerException     if <code>statusCode</code> is <code>null</code>.
   * @throws IllegalArgumentException if the code is invalid.
   */
  public static EnzymeStatusConstant valueOf(String statusCode) {
    if (statusCode == null) throw new NullPointerException("Parameter 'statusCode' must not be null.");
    if (statusCode.equals(APPROVED.getCode())) return APPROVED;
    if (statusCode.equals(SUGGESTED.getCode())) return SUGGESTED;
    if (statusCode.equals(PROPOSED.getCode())) return PROPOSED;
	throw new IllegalArgumentException("Parameter 'statusCode' is invalid.");
  }

  /**
   * Object cannot be created outside this class.
   *
   * @param statusCode The status code.
   */
  private EnzymeStatusConstant(String statusCode, String text) {
    this.statusCode = statusCode;
    this.text = text;
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeStatusConstant)) return false;

    final EnzymeStatusConstant enzymeStatus = (EnzymeStatusConstant) o;

    if (statusCode != null ? !statusCode.equals(enzymeStatus.statusCode) : enzymeStatus.statusCode != null) return false;
    if (text != null ? !text.equals(enzymeStatus.text) : enzymeStatus.text != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (statusCode != null ? statusCode.hashCode() : 0);
    result = 29 * result + (text != null ? text.hashCode() : 0);
    return result;
  }

  /**
   * Returns the status code's text.
   *
   * @return the status code's text.
   */
  public String toString() {
    return text;
  }

  /**
   * Returns the status code.
   *
   * @return the status code.
   */
  public String getCode() {
    return statusCode;
  }
}


