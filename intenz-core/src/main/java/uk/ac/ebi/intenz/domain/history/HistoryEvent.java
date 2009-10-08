package uk.ac.ebi.intenz.domain.history;

import uk.ac.ebi.intenz.domain.DomainObject;
import uk.ac.ebi.intenz.domain.constants.EventConstant;

import java.util.Date;

/**
 * The history event is a structural copy of the corresponding database table, that is attributes of this class
 * correspond to the table's columns.
 * <p/>
 * History events can be deletions, transfers, etc. of enzymes.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class HistoryEvent extends DomainObject implements Comparable<HistoryEvent> {
  protected Long groupId;

  protected Long eventId;

  protected HistoryNode beforeNode;

  protected HistoryNode afterNode;

  protected EventConstant eventClass;

  protected Date date;

  protected String note;

  /**
   * Nothing special.
   */
  public HistoryEvent() {
    super();
    this.groupId = new Long(0);
    this.eventId = new Long(0);
    this.beforeNode = null;
    this.afterNode = null;
    this.eventClass = EventConstant.CREATION;
    this.date = null;
    this.note = "";
  }

  /**
   * Makes instances of this class comparable.
   *
   * @param historyEvent The object to be compared to this instance.
   * @return a pos. integer if this instance is greater than, a neg. integer if it is less than or 0 if it equals o.
   */
  public int compareTo(HistoryEvent historyEvent) {
    if (this.getDate().before(historyEvent.getDate())) return -1;
    if (this.getDate().after(historyEvent.getDate())) return 1;

    return 0;
  }

  /**
   * Checks if two <code>HistoryEvent</code> objects are equal.
   *
   * @param o The other <code>HistoryEvent</code> to compare with this one.
   * @return <code>true</code>, if the two objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HistoryEvent)) return false;
    if (!super.equals(o)) return false;

    final HistoryEvent event = (HistoryEvent) o;

    if (afterNode != null ? !afterNode.equals(event.afterNode) : event.afterNode != null) return false;
    if (beforeNode != null ? !beforeNode.equals(event.beforeNode) : event.beforeNode != null) return false;
    if (date != null ? !date.equals(event.date) : event.date != null) return false;
    if (eventClass != null ? !eventClass.equals(event.eventClass) : event.eventClass != null) return false;
    if (eventId != null ? !eventId.equals(event.eventId) : event.eventId != null) return false;
    if (groupId != null ? !groupId.equals(event.groupId) : event.groupId != null) return false;
    if (note != null ? !note.equals(event.note) : event.note != null) return false;

    return true;
  }

  /**
   * Calculates a unique hash code.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + (groupId != null ? groupId.hashCode() : 0);
    result = 29 * result + (eventId != null ? eventId.hashCode() : 0);
    result = 29 * result + (beforeNode != null ? beforeNode.hashCode() : 0);
    result = 29 * result + (afterNode != null ? afterNode.hashCode() : 0);
    result = 29 * result + (eventClass != null ? eventClass.hashCode() : 0);
    result = 29 * result + (date != null ? date.hashCode() : 0);
    result = 29 * result + (note != null ? note.hashCode() : 0);
    return result;
  }

  /**
   * Returns <code>true</code> if the given node is the successor of this node.
   *
   * @param node The node to be compared to this node's relatives.
   * @return <code>true</code> if the given node is the successor of this node.
   */
  public boolean isSuccessor(HistoryNode node) {
    if (node == null) return afterNode == null;
    return afterNode.equals(node);
  }


  // ------------------- GETTER & SETTER ------------------------

  /**
   * Returns the relative node of the given node.
   *
   * @param node The node of which the relative is demanded.
   * @return The relative node or <code>null</code> if no relative exists.
   * @throws NullPointerException if the given node is <code>null</code>.
   */
  public HistoryNode getRelative(HistoryNode node) {
    if (node == null) throw new NullPointerException();

    if (beforeNode == null || afterNode == null) return null;

    if (beforeNode.getEnzymeEntry().getId().equals(node.getEnzymeEntry().getId()) ) return afterNode;
    return beforeNode;
  }

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    if (groupId == null) throw new NullPointerException("Parameter 'groupId' must not be null.");
    this.groupId = groupId;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    if (eventId == null) throw new NullPointerException("Parameter 'eventId' must not be null.");
    this.eventId = eventId;
  }

  public HistoryNode getBeforeNode() {
    return beforeNode;
  }

  public void setBeforeNode(HistoryNode beforeNode) {
    this.beforeNode = beforeNode;
  }

  public HistoryNode getAfterNode() {
    return afterNode;
  }

  public void setAfterNode(HistoryNode afterNode) {
    this.afterNode = afterNode;
  }

  public EventConstant getEventClass() {
    return eventClass;
  }

  public void setEventClass(EventConstant eventClass) {
    if (eventClass == null) throw new NullPointerException("Parameter 'eventClass' must not be null.");
    this.eventClass = eventClass;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    if (date == null) throw new NullPointerException("Parameter 'date' must not be null.");
    this.date = date;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    if (note == null) throw new NullPointerException("Parameter 'note' must not be null.");
    this.note = note;
  }
}
