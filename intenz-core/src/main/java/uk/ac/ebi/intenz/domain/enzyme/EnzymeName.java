package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

/**
 * This class stores a name related to an enzyme.
 * <p/>
 * Currently three different types of names are supported:
 * <p/>
 * <ol>
 * <li>common (recommended) name</li>
 * <li>systematic name</li>
 * <li>synonym</li>
 * </ol>
 * <p/>
 * Synonyms can be qualified and ordered.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeName implements Comparable<EnzymeName>, Viewable {

  public static EnzymeName UNDEF = new EnzymeName("", EnzymeNameTypeConstant.UNDEF, EnzymeNameQualifierConstant.UNDEF,
                                                  EnzymeSourceConstant.UNDEF, EnzymeViewConstant.UNDEF);

  private String name;

  private EnzymeNameTypeConstant type;

  private EnzymeNameQualifierConstant qualifier;

  private EnzymeSourceConstant source;

  private EnzymeViewConstant view;


  /**
   * Object cannot be created outside this class.
   *
   * @param name      A name of an enzyme.
   * @param type      The type of an enzyme's name (see {@link uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant}).
   * @param qualifier A qualifier used for synonyms (see {@link uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant}).
   */
  private EnzymeName(String name, EnzymeNameTypeConstant type, EnzymeNameQualifierConstant qualifier,
                     EnzymeSourceConstant source, EnzymeViewConstant view) {
    this.name = name;
    this.type = type;
    this.qualifier = qualifier;
    this.source = source;
    this.view = view;
  }

  public static EnzymeName valueOf(String name, EnzymeNameTypeConstant type, EnzymeNameQualifierConstant qualifier,
                                   EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (type == EnzymeNameTypeConstant.COMMON_NAME) return getCommonNameInstance(name, source, view);
    if (type == EnzymeNameTypeConstant.SYSTEMATIC_NAME) return getSystematicNameInstance(name, source, view);
    if (type == EnzymeNameTypeConstant.OTHER_NAME) return getSynonymInstance(name, qualifier, source, view);
    throw new IllegalArgumentException("The given type is not supported.");
  }

  /**
   * Returns an <code>EnzymeName</code> instance representing an enzyme's common (recommended) name.
   *
   * @param name The common name.
   * @return The <code>EnzymeName</code> instance representing the common name.
   * @throws NullPointerException     if <code>name</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>name</code> is empty.
   */
  public static EnzymeName getCommonNameInstance(String name, EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (name == null) throw new NullPointerException("Parameter 'name' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (name.equals("")) throw new IllegalArgumentException("Parameter 'name' must not be empty.");

    return new EnzymeName(name, EnzymeNameTypeConstant.COMMON_NAME,
                          EnzymeNameQualifierConstant.UNDEF, source, view);
  }

  /**
   * Returns an <code>EnzymeName</code> instance representing an enzyme's systematic name.
   *
   * @param name The systematic name.
   * @return The <code>EnzymeName</code> instance representing the systematic name.
   * @throws NullPointerException     if <code>name</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>name</code> is empty.
   */
  public static EnzymeName getSystematicNameInstance(String name, EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (name == null) throw new NullPointerException("Parameter 'name' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (name.equals("")) throw new IllegalArgumentException("Parameter 'name' must not be empty.");

    return new EnzymeName(name, EnzymeNameTypeConstant.SYSTEMATIC_NAME,
                          EnzymeNameQualifierConstant.UNDEF, source, view);
  }

  /**
   * Returns an <code>EnzymeName</code> instance representing an enzyme's synonym.
   *
   * @param name      The systematic name.
   * @param qualifier A qualifier used for synonyms (see {@link uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant}).
   * @return The <code>EnzymeName</code> instance representing the synonym.
   * @throws NullPointerException     if <code>name</code> or <code>qualifier</code> are <code>null</code>.
   * @throws IllegalArgumentException if <code>name</code> is empty or <code>orderIn</code> is < 1.
   */
  public static EnzymeName getSynonymInstance(String name, EnzymeNameQualifierConstant qualifier,
                                              EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (name == null) throw new NullPointerException("Parameter 'name' must not be null.");
    if (qualifier == null) throw new NullPointerException("Parameter 'qualifier' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (name.equals("")) throw new IllegalArgumentException("Parameter 'name' must not be empty.");

    return new EnzymeName(name, EnzymeNameTypeConstant.OTHER_NAME, qualifier, source, view);
  }

  /**
   * @param name The specified enzyme name.
   * @return a neg. integer if this instance is smaller than the specified name 0 if they are equal and a pos. integer otherwise.
   */
  public int compareTo(EnzymeName name) {
    int result = name.getSource().toString().compareTo(source.toString());
    if (result == 0) {
      return (name.getName().compareTo(this.name));
    }
    return result;
  }

  /**
   * Standard equals method omitting 'orderIn' attribute, which is irrelevant for this check.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeName)) return false;

    final EnzymeName enzymeName = (EnzymeName) o;

    if (name != null ? !name.equals(enzymeName.name) : enzymeName.name != null) return false;
    if (qualifier != null ? !qualifier.equals(enzymeName.qualifier) : enzymeName.qualifier != null) return false;
    if (source != null ? !source.equals(enzymeName.source) : enzymeName.source != null) return false;
    if (type != null ? !type.equals(enzymeName.type) : enzymeName.type != null) return false;
    if (view != null ? !view.equals(enzymeName.view) : enzymeName.view != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (name != null ? name.hashCode() : 0);
    result = 29 * result + (type != null ? type.hashCode() : 0);
    result = 29 * result + (qualifier != null ? qualifier.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (view != null ? view.hashCode() : 0);
    return result;
  }

  // --------------------  GETTER -----------------------

  /**
   * Returns the name.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the name's type.
   *
   * @return the name's type.
   * @see EnzymeNameTypeConstant
   */
  public EnzymeNameTypeConstant getType() {
    return type;
  }

  /**
   * Returns the name's qualifier.
   *
   * @return the name's qualifier.
   * @see EnzymeNameQualifierConstant
   */
  public EnzymeNameQualifierConstant getQualifier() {
    return qualifier;
  }

  /**
   * Returns the name's source.
   *
   * @return the name's source.
   * @see EnzymeSourceConstant
   */
  public EnzymeSourceConstant getSource() {
    return source;
  }

  /**
   * Returns the name's view code.
   * <p/>
   * The code defines in which view this name will be displayed.
   *
   * @return the name's view code.
   * @see EnzymeViewConstant
   */
  public EnzymeViewConstant getView() {
    return view;
  }

  // ------------------- PRIVATE METHODS ------------------------
}
