package uk.ac.ebi.intenz.webapp.controller;

import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.rhea.mapper.MapperException;

/**
 * This command handles requests about proposed entries.
 * <p/>
 * At the moment only requests for the whole list are processed.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class SearchProposedCommand extends DatabaseCommand {

  /**
   * TBD
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    String id = request.getParameter("id");

    if (id == null) {
      List proposedEntries = findProposedList();
      if (proposedEntries.size() == 0) {
        request.setAttribute("message", "There are no proposed entries stored in the database at the moment.");
      }
      request.setAttribute("proposed", proposedEntries);
      forward("/proposed.jsp");
    } else {
      EnzymeEntry proposedEntry = findProposedEntry(new Long(id));
      if (proposedEntry != null) {
        request.setAttribute("result", proposedEntry);
        forward("/entry.jsp");
      } else {
        if (request.getAttribute("message") != null) {
          forward("/error.jsp");
        } else {
          request.setAttribute("message", "The requested proposed entry does not exist.");
          forward("/error.jsp"); // No result found.
        }
      }
    }
  }

  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Returns the list of all proposed entries.
   * <p/>
   * The method returns <code>null</code> if an error occured or no entries have been found.
   *
   * @return the list of proposed entries or <code>null</code> if an error occured.
   */
  private List findProposedList() {
    ServletContext application = request.getSession().getServletContext();
    List proposedList = (List) application.getAttribute("proposedList");

    if (proposedList != null) {
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
        proposedList = enzymeEntryMapper.findProposedList(con);
        if (proposedList == null) {
          try {
            con.close(); // The connection is not needed anymore.
          } catch (SQLException e) {
            IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
            request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                                            this.databaseErrorMessage);
            return null;
          }
        } else {
          application.setAttribute("proposedList", proposedList);
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

    return proposedList;
  }

  /**
   * Returns the proposed entry requested by the given ID.
   *
   * @param id The enzyme ID of the proposed entry.
   * @return The proposed entry or <code>null</code> if an error occured.
   */
  private EnzymeEntry findProposedEntry(Long id) {
    EnzymeEntry enzymeEntry = null;
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();

    ServletContext application = request.getSession().getServletContext();
    Map proposedEntries = (HashMap) application.getAttribute("proposedEntries");

    // Check if object already stored in the cache.
    if (proposedEntries.containsKey(id)) {
      enzymeEntry = (EnzymeEntry) proposedEntries.get(id);
      try {
        con.close(); // The connection is not needed.
      } catch (SQLException e) {
        IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
        request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
                                        this.databaseErrorMessage);
        return null;
      }
    } else {
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
          proposedEntries.put(id, enzymeEntry);
        }
      } catch (SQLException e) {
        IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
        request.setAttribute("message", e.getMessage());
        return null;
      } catch (MapperException e) {
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
