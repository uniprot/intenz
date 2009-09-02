package uk.ac.ebi.intenz.webapp.controller;

import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;

/**
 * This command handles enzyme ID requests.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class SearchIDCommand extends DatabaseCommand {

  /**
   * TBD
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    String id = request.getParameter("id");

    if (id != null) {
      EnzymeEntry enzymeEntry = findEnzymeEntry(new Long(id));
      if (enzymeEntry != null) {
        request.setAttribute("result", enzymeEntry);
        forward("/entry.jsp");
      } else {
        if (request.getAttribute("message") != null) {
          forward("/error.jsp");
        } else {
          request.setAttribute("message", "The enzyme with the given ID: <b>" + id + "</b> does not exist.");
          forward("/notFound.jsp"); // No result found.
        }
      }
    } else {
      try {
        con.close();
      } catch (SQLException e) {
        request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                this.databaseErrorMessage);
        forward("/error.jsp");
        return;
      }
      request.setAttribute("message", "The <b>id</b> parameter has to be set to be able to search for an enzyme using its ID.");
      forward("/error.jsp");
      return;
    }
  }

  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Calls the according <code>find()</code> method and handles empty results and SQL exceptions.
   *
   * @param id ID of the enzyme to search for.
   * @return An <code>EnzymeEntry</code> instance or <code>null</code>.
   */
  protected EnzymeEntry findEnzymeEntry(Long id) {
    EnzymeEntry enzymeEntry = null;

    ServletContext application = request.getSession().getServletContext();
    Map entries = (HashMap) application.getAttribute("entries_id");

    // Check if object already stored in the cache.
    if (entries.containsKey(id)) {
      enzymeEntry = (EnzymeEntry) entries.get(id);
      try {
        con.close(); // The connection is not needed.
      } catch (SQLException e) {
        IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
        request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                this.databaseErrorMessage);
        return null;
      }
    } else {
      EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
      try {
        enzymeEntry = enzymeEntryMapper.findById(id, con);
        if (enzymeEntry == null) {
          try {
            con.close(); // The connection is not needed anymore.
          } catch (SQLException e) {
            IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
            request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                    this.databaseErrorMessage);
            return null;
          }
          //  return enzymeEntry = null.
        } else {
          entries.put(id, enzymeEntry);
        }
      } catch (SQLException e) {
        IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
        request.setAttribute("message", e.getMessage());
        return null;
      } catch (DomainException e) {
        PropertyResourceBundle applicationProperties = (PropertyResourceBundle) request.getSession().getServletContext().getAttribute("application_properties");
        IntEnzMessenger.sendError(this.getClass().toString(), applicationProperties.getString(e.getMessageKey()), (String) request.getSession().getAttribute("user"));
        request.setAttribute("message", e.getMessage());
        return null;
      } finally {
        try {
          con.close();
        } catch (SQLException e) {
          IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
          request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                  this.databaseErrorMessage);
          return null;
        }
      }
    }

    return enzymeEntry;
  }

}
