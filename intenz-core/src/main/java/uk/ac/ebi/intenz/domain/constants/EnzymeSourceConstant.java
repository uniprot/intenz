package uk.ac.ebi.intenz.domain.constants;



/**
 * This class represents an enumeration of all database source types.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:06 $
 */
public class EnzymeSourceConstant {
  private final String sourceCode;

  public static final EnzymeSourceConstant UNDEF = new EnzymeSourceConstant("UNDEF");
  public static final EnzymeSourceConstant IUBMB = new EnzymeSourceConstant("IUBMB");
  public static final EnzymeSourceConstant SIB = new EnzymeSourceConstant("SIB");
  public static final EnzymeSourceConstant BRENDA = new EnzymeSourceConstant("BRENDA");
  public static final EnzymeSourceConstant INTENZ = new EnzymeSourceConstant("INTENZ");

  /**
   * Object cannot be created outside this class.
   *
   * @param sourceCode The line type code.
   */
  private EnzymeSourceConstant(String sourceCode) {
    this.sourceCode = sourceCode;
  }

  /**
   * Returns the corresponding instance of the given source code.
   * <p/>
   * If the source code does not match any code an exception is thrown.
   *
   * @param sourceCode The source code.
   * @return the class constant corresponding to the given code.
   * @throws NullPointerException     if <code>sourceCode</code> is <code>null</code>.
   * @throws IllegalArgumentException if the code is invalid.
   */
  public static EnzymeSourceConstant valueOf(String sourceCode) {
    if (sourceCode == null) throw new NullPointerException("Parameter 'sourceCode' must not be null.");
    if (sourceCode.equals(IUBMB.toString())) return IUBMB;
    if (sourceCode.equals(SIB.toString())) return SIB;
    if (sourceCode.equals(BRENDA.toString())) return BRENDA;
    if (sourceCode.equals(INTENZ.toString())) return INTENZ;
	throw new IllegalArgumentException("The given source code is not supported.");
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeSourceConstant)) return false;

    final EnzymeSourceConstant enzymeSource = (EnzymeSourceConstant) o;

    if (!sourceCode.equals(enzymeSource.sourceCode)) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    return sourceCode.hashCode();
  }

  /**
   * Returns the database code.
   *
   * @return the database code.
   */
  public String toString() {
    return sourceCode;
  }

  /**
   * @deprecated
   * @param source
   * @return
   */
  public static String toDisplayString(String source) {
    EnzymeSourceConstant sourceConstant = EnzymeSourceConstant.valueOf(source);
    if(sourceConstant == UNDEF) return "undefined";
    if(sourceConstant == IUBMB) return "NC-IUBMB";
    if(sourceConstant == SIB) return "ENZYME";
    if(sourceConstant == INTENZ) return "IntEnz";
    return "BRENDA";
  }

  /**
   * @deprecated
   * @return
   */
  public String toDisplayString() {
    if(this == UNDEF) return "undefined";
    if(this == IUBMB) return "NC-IUBMB";
    if(this == SIB) return "ENZYME";
    if(this == INTENZ) return "IntEnz";
    return "BRENDA";
  }
}


