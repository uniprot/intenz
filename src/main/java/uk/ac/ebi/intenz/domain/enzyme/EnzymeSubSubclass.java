package uk.ac.ebi.intenz.domain.enzyme;

import java.util.List;

/**
 * This class represents an enzyme sub-subclass of the Enzyme Nomenclature.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeSubSubclass {
  private EnzymeCommissionNumber ec;

  private String className;

  private String subclassName;

  private String name;

  private String description;

  private List<EnzymeEntry> entries;

  /**
   * Returns an <code>EnzymeSubSubclass</code> instance.
   *
   * @param ec           The class' EC.
   * @param className    The name of the class.
   * @param subclassName The name of the subclass.
   * @param name         The name of the subclass.
   * @param description  The description of the class.
   * @param entries      The list of entries (see {@link EnzymeEntry}).
   * @throws NullPointerException     if any parameters are <code>null</code> apart from <code>name</code> and <code>description</code>.
   * @throws IllegalArgumentException if the list of entries is empty.
   */
  public EnzymeSubSubclass(EnzymeCommissionNumber ec, String className, String subclassName, String name,
                           String description, List<EnzymeEntry> entries) {
    if (ec == null || className == null || subclassName == null)
      throw new NullPointerException("No parameter must be 'null'.");
    this.ec = ec;
    this.className = className;
    this.subclassName = subclassName;
    this.name = name != null ? name : "";
    this.description = description != null ? description : "";
    this.entries = entries;
  }

  /**
   * Makes instances of this class comparable.
   *
   * @param o The object to be compared to this instance.
   * @return a pos. integer if this instance is greater than, a neg. integer if it is less than or 0 if it equals o.
   * @throws NullPointerException if <code>o</code> is <code>null</code>.
   */
  public int compareTo(Object o) {
    if (o == null) throw new NullPointerException("Parameter 'o' must not be null.");
    EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) o;
    return ec.compareTo(enzymeSubSubclass.getEc());
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeSubSubclass)) return false;

    final EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) o;

    if (className != null ? !className.equals(enzymeSubSubclass.className) : enzymeSubSubclass.className != null) return false;
    if (description != null ? !description.equals(enzymeSubSubclass.description) : enzymeSubSubclass.description != null) return false;
    if (ec != null ? !ec.equals(enzymeSubSubclass.ec) : enzymeSubSubclass.ec != null) return false;
    if (entries != null ? !entries.equals(enzymeSubSubclass.entries) : enzymeSubSubclass.entries != null) return false;
    if (name != null ? !name.equals(enzymeSubSubclass.name) : enzymeSubSubclass.name != null) return false;
    if (subclassName != null ? !subclassName.equals(enzymeSubSubclass.subclassName) : enzymeSubSubclass.subclassName != null) return false;

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
    result = 29 * result + (subclassName != null ? subclassName.hashCode() : 0);
    result = 29 * result + (name != null ? name.hashCode() : 0);
    result = 29 * result + (description != null ? description.hashCode() : 0);
    result = 29 * result + (entries != null ? entries.hashCode() : 0);
    return result;
  }


  // ----------------  GETTER ------------------

  public EnzymeCommissionNumber getEc() {
    return ec;
  }

  public String getClassName() {
    return className;
  }

  public String getSubclassName() {
    return subclassName;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<EnzymeEntry> getEntries() {
    return entries;
  }
}
