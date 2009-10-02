package uk.ac.ebi.intenz.domain.enzyme;

import java.util.List;

/**
 * This class represents an enzyme subclass of the Enzyme Nomenclature.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeSubclass implements Comparable<EnzymeSubclass> {
  private EnzymeCommissionNumber ec;

  private String className;

  private String name;

  private String description;

  private List<EnzymeSubSubclass> subSubclasses;

  /**
   * Returns an <code>EnzymeSubclass</code> instance.
   *
   * @param ec            The class' EC.
   * @param className     The name of the class.
   * @param name          The name of the subclass.
   * @param description   The description of the class.
   * @param subSubclasses The list of sub-subclasses (see {@link EnzymeSubSubclass}).
   * @throws NullPointerException     if any of the parameters is <code>null</code>.
   * @throws IllegalArgumentException if the list of sub-subclasses is empty.
   */
  public EnzymeSubclass(EnzymeCommissionNumber ec, String className, String name, String description,
		  List<EnzymeSubSubclass> subSubclasses) {
    if (ec == null || className == null || name == null || description == null)
      throw new NullPointerException("Only parameter 'subSubclasses' can be 'null'.");
    this.ec = ec;
    this.className = className;
    this.name = name;
    this.description = description;
    this.subSubclasses = subSubclasses;
  }

  /**
   * Makes instances of this class comparable.
   *
   * @param enzymeSubclass The object to be compared to this instance.
   * @return a pos. integer if this instance is greater than, a neg. integer if it is less than or 0 if it equals o.
   */
  public int compareTo(EnzymeSubclass enzymeSubclass) {
    return ec.compareTo(enzymeSubclass.getEc());
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeSubclass)) return false;

    final EnzymeSubclass enzymeSubclass = (EnzymeSubclass) o;

    if (className != null ? !className.equals(enzymeSubclass.className) : enzymeSubclass.className != null) return false;
    if (description != null ? !description.equals(enzymeSubclass.description) : enzymeSubclass.description != null) return false;
    if (ec != null ? !ec.equals(enzymeSubclass.ec) : enzymeSubclass.ec != null) return false;
    if (name != null ? !name.equals(enzymeSubclass.name) : enzymeSubclass.name != null) return false;
    if (subSubclasses != null ? !subSubclasses.equals(enzymeSubclass.subSubclasses) : enzymeSubclass.subSubclasses != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (ec != null ? ec.hashCode() : 0);
    result = 29 * result + (className != null ? className.hashCode() : 0);
    result = 29 * result + (name != null ? name.hashCode() : 0);
    result = 29 * result + (description != null ? description.hashCode() : 0);
    result = 29 * result + (subSubclasses != null ? subSubclasses.hashCode() : 0);
    return result;
  }


  // ----------------  GETTER ------------------

  public EnzymeCommissionNumber getEc() {
    return ec;
  }

  public String getClassName() {
    return className;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<EnzymeSubSubclass> getSubSubclasses() {
    return subSubclasses;
  }
}
