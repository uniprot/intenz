package uk.ac.ebi.intenz.domain;

/**
 * This class is the base class of most of the domain classes.
 * <p/>
 * It only stores the ID.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:13 $
 */
public class DomainObject {

  /**
   * IDs can be long, therefore this data type. Furthermore the ID is frequently used as an object
   * key (e.g. within a <code>HashMap</code>) and thus should be of type <code>Long</code> instead of <code>long</code>.
   */
  protected Long id;

  /**
   * The ID is initially set to 0.
   */
  protected DomainObject() {
    id = new Long(0);
  }

  /**
   * Initialises the ID.
   *
   * @param id the new ID.
   * @throws NullPointerException     if id is <code>null</code>.
   * @throws IllegalArgumentException if id is < 0.
   */
  protected DomainObject(Long id) {
    if (id == null) throw new NullPointerException();
    if (id.longValue() < 0) throw new IllegalArgumentException("Negative IDs are not allowed.");
    this.id = id;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DomainObject)) return false;

    final DomainObject domainObject = (DomainObject) o;

    if (id != null ? !id.equals(domainObject.id) : domainObject.id != null) return false;

    return true;
  }

  public int hashCode() {
    return (id != null ? id.hashCode() : 0);
  }

  // --------------------  GETTER -----------------------

  public Long getId() {
    return id;
  }

}
