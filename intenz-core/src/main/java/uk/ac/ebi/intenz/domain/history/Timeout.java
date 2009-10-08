package uk.ac.ebi.intenz.domain.history;

import uk.ac.ebi.intenz.domain.DomainObject;

import java.util.Date;

/**
 * This class stores information about the timeout related to a future event.
 * <p/>
 * A timeout defines when an automatic upgrade of a suggested or proposed enzyme will be made w/o asking for permission.
 * This system is not activated at the moment.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class Timeout extends DomainObject {

  private int enzymeId;
  private int timeoutId;
  private Date startDate;
  private Date dueDate;

  public Timeout() {
    enzymeId = 0;
    timeoutId = 0;
    startDate = null;
    dueDate = null;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Timeout)) return false;
    if (!super.equals(o)) return false;

    final Timeout timeout = (Timeout) o;

    if (enzymeId != timeout.enzymeId) return false;
    if (timeoutId != timeout.timeoutId) return false;
    if (dueDate != null ? !dueDate.equals(timeout.dueDate) : timeout.dueDate != null) return false;
    if (startDate != null ? !startDate.equals(timeout.startDate) : timeout.startDate != null) return false;

    return true;
  }

  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + enzymeId;
    result = 29 * result + timeoutId;
    result = 29 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 29 * result + (dueDate != null ? dueDate.hashCode() : 0);
    return result;
  }

  public int getEnzymeId() {
    return enzymeId;
  }

  public void setEnzymeId(int enzymeId) {
    this.enzymeId = enzymeId;
  }

  public int getTimeoutId() {
    return timeoutId;
  }

  public void setTimeoutId(int timeoutId) {
    this.timeoutId = timeoutId;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }
}
