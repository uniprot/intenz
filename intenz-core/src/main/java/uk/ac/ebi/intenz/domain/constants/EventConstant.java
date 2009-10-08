package uk.ac.ebi.intenz.domain.constants;

/**
 * This class provides all event constants.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/04/16 15:01:04 $
 * @deprecated use enumeration {@link Event} instead.
 */
public class EventConstant {
  private final String eventCode;

  private final String eventFullName;

  public static final EventConstant DELETION = new EventConstant("DEL", "deleted");
  public static final EventConstant TRANSFER = new EventConstant("TRA", "transferred");
  public static final EventConstant MODIFICATION = new EventConstant("MOD", "modified");
  public static final EventConstant CREATION = new EventConstant("NEW", "created");

  /**
   * Object cannot be created outside this class.
   *
   * @param eventCode     The event code.
   * @param eventFullName Full name of this code.
   */
  private EventConstant(String eventCode, String eventFullName) {
    this.eventCode = eventCode;
    this.eventFullName = eventFullName;
  }

  /**
   * Returns the corresponding instance of the given event code.
   * <p/>
   * If the event code does not match any code an exception is thrown.
   *
   * @param eventCode The event code.
   * @return the class constant corresponding to the given code.
   * @throws IllegalArgumentException if the code is invalid.
   */
  public static EventConstant valueOf(String eventCode) {
    if (eventCode.equals(DELETION.getCode())) return DELETION;
    if (eventCode.equals(TRANSFER.getCode())) return TRANSFER;
    if (eventCode.equals(MODIFICATION.getCode())) return MODIFICATION;
    if (eventCode.equals(CREATION.getCode())) return CREATION;
	throw new IllegalArgumentException();
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EventConstant)) return false;

    final EventConstant eventConstants = (EventConstant) o;

    if (eventCode != null ? !eventCode.equals(eventConstants.eventCode) : eventConstants.eventCode != null) return false;
    if (eventFullName != null ? !eventFullName.equals(eventConstants.eventFullName) : eventConstants.eventFullName != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (eventCode != null ? eventCode.hashCode() : 0);
    result = 29 * result + (eventFullName != null ? eventFullName.hashCode() : 0);
    return result;
  }

  /**
   * Returns the database code.
   *
   * @return the database code.
   */
  public String toString() {
    return eventFullName;
  }


  // ------------------- GETTER ------------------

  /**
   * Returns the code of this event.
   *
   * @return the event's code.
   */
  public String getCode() {
    return eventCode;
  }
}
