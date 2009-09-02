package uk.ac.ebi.intenz.webapp.utilities;

import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * This class is responsible for locking entries in process.
 * <p/>
 * This will be guaranteed by a synchronized Hashtable which stores the enzyme id of the entry
 * and the users name.
 * If a user locks an entry but quits the application without finishing its modifications, the
 * ModificationBindingListener will take care of releasing the lock.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class EntryLockSingleton {

  private Logger LOGGER = Logger.getLogger(EntryLockSingleton.class);

  /**
   * Creates a single statically availble instance of this class, which cannot be redefined.
   */
  private static final EntryLockSingleton instance = new EntryLockSingleton();

  private static Hashtable entriesInProcess;

  /**
   * Initialises the Hashtable for storing enzyme ids of currently modified entries.
   */
  protected EntryLockSingleton() {
    entriesInProcess = new Hashtable();
  }

  /**
   * Returns the EntryLockSingleton instance.
   *
   * @return the one and only EntryLockSingleton instance.
   */
  public static EntryLockSingleton getInstance() {
    return instance;
  }

  /**
   * Sets a lock for an entry in process.
   * <p/>
   * This method is synchronized to ensure, that only one user at the time can set a lock, hence only one
   * user is able to modify one and the same entry.
   *
   * @param enzymeId The enzyme id of the entry to be modified.
   * @param user     The user who is modifying the entry.
   * @return <tt>true</tt>, if no-one else is modifying the entry.
   */
  public synchronized boolean setLock(String enzymeId, String user) {
    if (entriesInProcess.containsKey(enzymeId)) {
      if (entriesInProcess.get(enzymeId).equals(user)) {
        return true;
      } else {
        return false;
      }
    } else
      entriesInProcess.put(enzymeId, user);

    LOGGER.debug("The enzyme (ID: " + enzymeId + ") has been locked by user " + user);
    return true;
  }

  /**
   * Removes a lock from an entry.
   *
   * @param enzymeId The enzyme id of the entry to be released.
   */
  public synchronized void releaseLock(String enzymeId) {
    entriesInProcess.remove(enzymeId);
    LOGGER.debug("The enzyme (ID: " + enzymeId + ") has been released.");
  }

  /**
   * Removes all locks of entries modified by one and the same user.
   * <p/>
   * This (and the EntryLockListener) ensures that even if the user does not finish a modification
   * and closes the browser, that all locked entries will be released.
   *
   * @param user The user name.
   */
  public synchronized void invalidateLocks(String user) {
    LOGGER.debug("All locks are being invalidated.");
    Enumeration keys = entriesInProcess.keys();
    while (keys.hasMoreElements()) {
      String key = (String) keys.nextElement();
      String value = (String) entriesInProcess.get(key);
      if (value.equals(user)) {
        entriesInProcess.remove(key);
      }
    }
  }
}