package uk.ac.ebi.intenz.webapp.controller;

import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class is used if the parsed URL provides a wrong class name
 * parameter.
 *
 * This may happen if the user modifies the URL manually.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class UnknownDatabaseCommand extends DatabaseCommand {
  /**
   * Just closes the logical connection and forwards to a notification page.
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    try {
      con.close();
    } catch (SQLException e) {
      IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
      request.setAttribute("message", "The following database error occured:\n" + e.getMessage()  +
              this.databaseErrorMessage);
      forward("/error.jsp");
      return;
    }
    forward("/unknown.jsp");
  }
}
