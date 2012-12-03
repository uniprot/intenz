package uk.ac.ebi.intenz.webapp.utilities;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * This class makes it possible to bind objects of this class to the session, which will be notified
 * if they are binded or unbinded from a session.
 * <p/>
 * The purpose of this class is to close connections properly, when the session is invalidated or timed out.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class EntryLockListener implements HttpSessionBindingListener {

  private EntryLockSingleton els;
  private String user;

  /**
   * Store reference to the EntryLockSingleton object.
   *
   * @param els The EntryLockSingleton object.
   */
  public EntryLockListener(EntryLockSingleton els) {
    this.els = els;
    this.user = "";
  }

  /**
   * Stores the user of this session.
   *
   * @param event HttpSessionBindingEvent object.
   */
  public void valueBound(HttpSessionBindingEvent event) {
    user = (String) event.getSession().getAttribute("user");
  }

  /**
   * Invalidate all locks of the user.
   *
   * @param event HttpSessionBindingEvent object.
   */
  public void valueUnbound(HttpSessionBindingEvent event) {
    els.invalidateLocks(user);
  }
}