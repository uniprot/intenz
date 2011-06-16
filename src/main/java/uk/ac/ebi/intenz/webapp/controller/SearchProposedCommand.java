package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
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

	public static final Logger LOGGER = Logger
			.getLogger(SearchProposedCommand.class);

	private final EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();

	/**
	 * TBD
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void process() throws ServletException, IOException {
		String id = request.getParameter("id");

		if (id == null) {
			List<?> proposedEntries = findProposedList();
			if (proposedEntries.size() == 0) {
				request.setAttribute("message",
						"There are no proposed entries stored in the database at the moment.");
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
					request.setAttribute("message",
							"The requested proposed entry does not exist.");
					forward("/error.jsp"); // No result found.
				}
			}
		}
	}

	// ------------------- PRIVATE METHODS ------------------------

	/**
	 * Returns the list of all proposed entries.
	 * <p/>
	 * The method returns <code>null</code> if an error occured or no entries
	 * have been found.
	 * 
	 * @return the list of proposed entries or <code>null</code> if an error
	 *         occured.
	 */
	private List<EnzymeEntry> findProposedList() {
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		List<EnzymeEntry> proposedList = (List<EnzymeEntry>) application
				.getAttribute("proposedList");

		if (proposedList == null) {
			try {
				proposedList = enzymeEntryMapper.findProposedList(con);
				if (proposedList != null) {
					application.setAttribute("proposedList", proposedList);
				}
			} catch (SQLException e) {
				LOGGER.error("Finding proposed list", e);
				IntEnzMessenger.sendError(this.getClass().toString(), e
						.getMessage(), (String) request.getSession()
						.getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			} catch (DomainException e) {
				LOGGER.error("Finding proposed list", e);
				PropertyResourceBundle intenzMessages = (PropertyResourceBundle) request
						.getSession().getServletContext()
						.getAttribute("intenz.messages");
				IntEnzMessenger.sendError(this.getClass().toString(),
						intenzMessages.getString(e.getMessageKey()),
						(String) request.getSession().getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			}
		}

		return proposedList;
	}

	/**
	 * Returns the proposed entry requested by the given ID.
	 * 
	 * @param id
	 *            The enzyme ID of the proposed entry.
	 * @return The proposed entry or <code>null</code> if an error occured.
	 */
	private EnzymeEntry findProposedEntry(Long id) {
		EnzymeEntry enzymeEntry = null;
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<Long, EnzymeEntry> proposedEntries = (Map<Long, EnzymeEntry>) application
				.getAttribute("proposedEntries");

		// Check if object already stored in the cache.
		if (proposedEntries.containsKey(id)) {
			enzymeEntry = (EnzymeEntry) proposedEntries.get(id);
		} else {
			try {
				enzymeEntry = enzymeEntryMapper.findById(id, con);
				if (enzymeEntry != null) {
					proposedEntries.put(id, enzymeEntry);
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
				LOGGER.error("Finding proposed entry", e);
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
