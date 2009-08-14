package uk.ac.ebi.intenz.domain.history;

import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;

/**
 * A future event adds some information to a history event as it has a status and a timeout associated.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class FutureEvent extends HistoryEvent {

  private EnzymeStatusConstant status;

  private Timeout timeout;

  /**
   * Nothing special here.
   */
  public FutureEvent() {
    super();
    status = EnzymeStatusConstant.SUGGESTED;
    timeout = new Timeout();
  }

  /**
   * Checks if two <code>FutureEvent</code> objects are equal.
   *
   * @param o The other <code>HistoryEvent</code> to compare with this one.
   * @return <code>true</code>, if the two objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FutureEvent)) return false;
    if (!super.equals(o)) return false;

    final FutureEvent event = (FutureEvent) o;

    if (status != null ? !status.equals(event.status) : event.status != null) return false;
    if (timeout != null ? !timeout.equals(event.timeout) : event.timeout != null) return false;

    return true;
  }

  /**
   * Calculates a unique hash code.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + (status != null ? status.hashCode() : 0);
    result = 29 * result + (timeout != null ? timeout.hashCode() : 0);
    return result;
  }

  


  // --------------------- GETTER -----------------------------

  public EnzymeStatusConstant getStatus() {
    return status;
  }

  public void setStatus(EnzymeStatusConstant status) {
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    this.status = status;
  }

  public Timeout getTimeout() {
    return timeout;
  }

  public void setTimeout(Timeout timeout) {
    if (timeout == null) throw new NullPointerException("Parameter 'timeout' must not be null.");
    this.timeout = timeout;
  }

}
