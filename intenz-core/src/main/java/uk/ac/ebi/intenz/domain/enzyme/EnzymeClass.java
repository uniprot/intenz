package uk.ac.ebi.intenz.domain.enzyme;

import java.util.List;

/**
 * This class represents an enzyme class of the Enzyme Nomenclature.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeClass implements Comparable<EnzymeClass> {

  private EnzymeCommissionNumber ec;

  private String name;

  private String description;

  private List<EnzymeSubclass> subclasses;

  /**
   * Returns an <code>EnzymeClass</code> instance.
   *
   * @param ec          The class' EC.
   * @param name        The name of the class.
   * @param description The description of the class.
   * @param subclasses  The list of subclasses (see {@link EnzymeSubclass}).
   */
  private EnzymeClass(EnzymeCommissionNumber ec, String name, String description, List<EnzymeSubclass> subclasses) {
    this.ec = ec;
    this.name = name;
    this.description = description;
    this.subclasses = subclasses;
  }

  /**
   * Returns an <code>EnzymeClass</code> instance.
   *
   * @param ec          The class' EC.
   * @param name        The name of the class.
   * @param description The description of the class.
   * @param subclasses  The list of subclasses (see {@link EnzymeSubclass}).
   * @throws NullPointerException     if any parameter is <code>null</code>.
   * @throws IllegalArgumentException if <code>name</code> or the list of subclasses is empty.
   */
  public static EnzymeClass valueOf(EnzymeCommissionNumber ec, String name, String description,
		  List<EnzymeSubclass> subclasses) {
    if (ec == null || name == null || description == null || subclasses == null)
      throw new NullPointerException("No parameter must be 'null'.");
    if (name.equals("")) throw new IllegalArgumentException("A class must have a name.");
    if (subclasses.size() == 0) throw new IllegalArgumentException("The list of subclasses must not be empty.");
    return new EnzymeClass(ec, name, description, subclasses);
  }

  /**
   * Makes instances of this class comparable.
   *
   * @param enzymeClass The object to be compared to this instance.
   * @return a pos. integer if this instance is greater than, a neg. integer if it is less than or 0 if it equals o.
   */
  public int compareTo(EnzymeClass enzymeClass) {
    return ec.compareTo(enzymeClass.getEc());
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeClass)) return false;

    final EnzymeClass enzymeClass = (EnzymeClass) o;

    if (description != null ? !description.equals(enzymeClass.description) : enzymeClass.description != null) return false;
    if (ec != null ? !ec.equals(enzymeClass.ec) : enzymeClass.ec != null) return false;
    if (name != null ? !name.equals(enzymeClass.name) : enzymeClass.name != null) return false;
    if (subclasses != null ? !subclasses.equals(enzymeClass.subclasses) : enzymeClass.subclasses != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (ec != null ? ec.hashCode() : 0);
    result = 29 * result + (name != null ? name.hashCode() : 0);
    result = 29 * result + (description != null ? description.hashCode() : 0);
    result = 29 * result + (subclasses != null ? subclasses.hashCode() : 0);
    return result;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */



  // ----------------  GETTER  ------------------

  public EnzymeCommissionNumber getEc() {
    return ec;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<EnzymeSubclass> getSubclasses() {
    return subclasses;
  }

}
