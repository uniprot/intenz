package uk.ac.ebi.intenz.webapp;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class makes it possible to bind objects of this class to the session, which will be notified
 * if they are binded or unbinded from a session.
 * <p/>
 * The purpose of this class is to close connections properly, when the session is invalidated or timed out.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:13 $
 */
public class ConnectionBindingListener implements HttpSessionBindingListener {
  private Connection con;

  public ConnectionBindingListener(Connection con) {
    this.con = con;
  }

  public void valueBound(HttpSessionBindingEvent event) {
  }

  public void valueUnbound(HttpSessionBindingEvent event) {
    try {
      con.close();
    } catch (SQLException e) {
      System.err.println("Connection cannot be closed, because of:\n" + e.toString());
    }
  }
}
