package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.PropertyResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
import uk.ac.ebi.rhea.mapper.MapperException;

/**
 * This command handles enzyme ID requests.
 * 
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class SearchIDCommand extends DatabaseCommand {

	public static final Logger LOGGER = Logger.getLogger(SearchIDCommand.class);

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
					request.setAttribute("message",
							"The enzyme with the given ID: <b>" + id
									+ "</b> does not exist.");
					forward("/notFound.jsp"); // No result found.
				}
			}
		} else {
			request.setAttribute(
					"message",
					"The <b>id</b> parameter has to be set to be able to search for an enzyme using its ID.");
			forward("/error.jsp");
			return;
		}
	}

	// ------------------- PRIVATE METHODS ------------------------

	/**
	 * Calls the according <code>find()</code> method and handles empty results
	 * and SQL exceptions.
	 * 
	 * @param id
	 *            ID of the enzyme to search for.
	 * @return An <code>EnzymeEntry</code> instance or <code>null</code>.
	 */
	protected EnzymeEntry findEnzymeEntry(Long id) {
		EnzymeEntry enzymeEntry = null;

		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<Long, EnzymeEntry> entries = (Map<Long, EnzymeEntry>) application
				.getAttribute("entries_id");

		// Check if object already stored in the cache.
		if (entries.containsKey(id)) {
			enzymeEntry = (EnzymeEntry) entries.get(id);
		} else {
			try {
				enzymeEntry = enzymeEntryMapper.findById(id, con);
				if (enzymeEntry != null) {
					entries.put(id, enzymeEntry);
				}
			} catch (SQLException e) {
				IntEnzMessenger.sendError(this.getClass().toString(), e
						.getMessage(), (String) request.getSession()
						.getAttribute("user"));
				request.setAttribute("message", e.getMessage());
			} catch (MapperException e) {
				IntEnzMessenger.sendError(this.getClass().toString(), e
						.getMessage(), (String) request.getSession()
						.getAttribute("user"));
				request.setAttribute("message", e.getMessage());
			} catch (DomainException e) {
				LOGGER.error("Searching by ID", e);
				PropertyResourceBundle intenzMessages = (PropertyResourceBundle) request
						.getSession().getServletContext()
						.getAttribute("intenz.messages");
				IntEnzMessenger.sendError(this.getClass().toString(),
						intenzMessages.getString(e.getMessageKey()),
						(String) request.getSession().getAttribute("user"));
				request.setAttribute("message", e.getMessage());
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					IntEnzMessenger.sendError(this.getClass().toString(), e
							.getMessage(), (String) request.getSession()
							.getAttribute("user"));
					request.setAttribute("message", e.getMessage());
					return null;
				}
			}
		}
		return enzymeEntry;
	}
}
