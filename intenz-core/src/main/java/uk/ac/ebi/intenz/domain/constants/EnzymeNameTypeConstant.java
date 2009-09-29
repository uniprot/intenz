package uk.ac.ebi.intenz.domain.constants;

/**
 * This class represents an enumeration of all database source types.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:06 $
 */
public class EnzymeNameTypeConstant {
  private final String nameTypeCode;

  public static final EnzymeNameTypeConstant UNDEF = new EnzymeNameTypeConstant("NON");
  public static final EnzymeNameTypeConstant COMMON_NAME = new EnzymeNameTypeConstant("COM");
  public static final EnzymeNameTypeConstant SYSTEMATIC_NAME = new EnzymeNameTypeConstant("SYS");
  public static final EnzymeNameTypeConstant OTHER_NAME = new EnzymeNameTypeConstant("OTH");

  /**
   * Returns an <code>EnzymeNameTypeConstant</code> instance of the given type string.
   *
   * @param type The type string defining this instance.
   * @return The <code>EnzymeNameTypeConstant</code> instance.
   * @throws NullPointerException     if <code>type</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>type</code> is unknown.
   */
  public static EnzymeNameTypeConstant valueOf(String type) {
    if (type == null) throw new NullPointerException("Parameter 'type' must not be null.");

    if (type.equals(UNDEF.toString())) return UNDEF;
    if (type.equals(COMMON_NAME.toString())) return COMMON_NAME;
    if (type.equals(SYSTEMATIC_NAME.toString())) return SYSTEMATIC_NAME;
    if (type.equals(OTHER_NAME.toString())) return OTHER_NAME;
	throw new IllegalArgumentException();
  }

  /**
   * Object cannot be created outside this class.
   *
   * @param nameTypeCode The line type code.
   */
  private EnzymeNameTypeConstant(String nameTypeCode) {
    this.nameTypeCode = nameTypeCode;
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeNameTypeConstant)) return false;

    final EnzymeNameTypeConstant enzymeSource = (EnzymeNameTypeConstant) o;

    if (!nameTypeCode.equals(enzymeSource.nameTypeCode)) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    return nameTypeCode.hashCode();
  }

  /**
   * Returns the database code.
   *
   * @return the database code.
   */
  public String toString() {
    return nameTypeCode;
  }
}


