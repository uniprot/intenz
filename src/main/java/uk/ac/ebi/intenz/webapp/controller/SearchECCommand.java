package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.*;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
import uk.ac.ebi.rhea.mapper.MapperException;

/**
 * This command processes all inquiries about EC numbers.
 * 
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class SearchECCommand extends DatabaseCommand {

	public static final Logger LOGGER = Logger.getLogger(SearchECCommand.class);

	private final EnzymeClassMapper classMapper = new EnzymeClassMapper();
	private final EnzymeSubclassMapper subclassMapper = new EnzymeSubclassMapper();
	private final EnzymeSubSubclassMapper subSubclassMapper = new EnzymeSubSubclassMapper();

	/**
	 * @throws ServletException
	 * @throws IOException
	 * @throws MapperException
	 */
	public void process() throws ServletException, IOException {
		String paramEc = request.getParameter("ec");
		String paramStatus = request.getParameter("status");
		EnzymeCommissionNumber ec = null;
		String forwardPageName = null;

		if (paramEc == null) {
			request.setAttribute("message",
					"The <b>ec</b> parameter has to be set to be able to search for an EC.");
			forward("/error.jsp");
			return;
		}

		// First check if the given EC is valid.
		try {
			ec = EnzymeCommissionNumber.valueOf(paramEc);
			if (ec == null) {
				request.setAttribute("message", "The EC <b>" + paramEc
						+ "</b> you used with this inquiry is not valid.");
				forward("/error.jsp");
				return;
			}
		} catch (EcException e) {
			request.setAttribute("message", "The EC <b>" + paramEc
					+ "</b> you used with this inquiry is not valid.");
			forward("/error.jsp");
			return;
		}

		switch (ec.getType()) {
		case CLASS:
			forwardPageName = setClassData(ec);
			break;
		case SUBCLASS:
			forwardPageName = setSubclassData(ec);
			break;
		case SUBSUBCLASS:
			forwardPageName = setSubSubclassData(ec);
			break;
		case ENZYME:
		case PRELIMINARY:
			forwardPageName = setEnzymeData(ec, getStatus(paramStatus, ec));
			break;
		}
		forward(forwardPageName);
	}

	/**
	 * Adjusts the requested status.
	 * 
	 * @param paramStatus
	 *            the requested status, if any.
	 * @param ec
	 *            the requested EC number.
	 * @return
	 */
	private Status getStatus(String paramStatus, EnzymeCommissionNumber ec) {
		switch (ec.getType()) {
		case PRELIMINARY:
			return Status.PRELIMINARY;
		default:
			return paramStatus == null ? Status.APPROVED : Status
					.fromCode(paramStatus);
		}
	}

	private String setClassData(EnzymeCommissionNumber ec) {
		assert ec != null;

		EnzymeClass enzymeClass = findEnzymeClass(ec);
		if (enzymeClass != null) {
			request.setAttribute("result", enzymeClass);
			return "/class.jsp";
		} else {
			if (request.getAttribute("message") != null) {
				return "/error.jsp";
			} else {
				request.setAttribute("message", "No entry found for EC: <b>"
						+ ec.toString() + "</b>.");
				return "/notFound.jsp"; // No result found.
			}
		}
	}

	private String setSubclassData(EnzymeCommissionNumber ec) {
		assert ec != null;

		EnzymeSubclass enzymeSubclass = findEnzymeSubclass(ec);
		if (enzymeSubclass != null) {
			request.setAttribute("result", enzymeSubclass);
			return "/subclass.jsp";
		} else {
			if (request.getAttribute("message") != null) {
				return "/error.jsp";
			} else {
				request.setAttribute("message", "No entry found for EC: <b>"
						+ ec.toString() + "</b>.");
				return "/notFound.jsp"; // No result found.
			}
		}
	}

	private String setSubSubclassData(EnzymeCommissionNumber ec) {
		assert ec != null;

		EnzymeSubSubclass enzymeSubSubclass = findEnzymeSubSubclass(ec);
		if (enzymeSubSubclass != null) {
			request.setAttribute("result", enzymeSubSubclass);
			return "/subsubclass.jsp";
		} else {
			if (request.getAttribute("message") != null) {
				return "/error.jsp";
			} else {
				request.setAttribute("message", "No entry found for EC: <b>"
						+ ec.toString() + "</b>.");
				return "/notFound.jsp"; // No result found.
			}
		}
	}

	private String setEnzymeData(EnzymeCommissionNumber ec, Status status) {
		assert ec != null && status != null;
		EnzymeEntry enzymeEntry = findEnzymeEntry(ec, status);
		if (enzymeEntry != null) {
			request.setAttribute("result", enzymeEntry);
			return "/entry.jsp";
		} else {
			if (request.getAttribute("message") != null) {
				return "/error.jsp";
			} else {
				request.setAttribute("message", "No entry found for EC: <b>"
						+ ec.toString() + "</b>.");
				return "/notFound.jsp"; // No result found.
			}
		}
	}

	/**
	 * Calls the according <code>find()</code> method and handles empty results
	 * and SQL exceptions.
	 * 
	 * @param ec
	 *            Number of enzyme class to search for.
	 * @return An <code>EnzymeClass</code> instance or <code>null</code>.
	 */
	protected EnzymeClass findEnzymeClass(EnzymeCommissionNumber ec) {
		if (ec == null)
			throw new NullPointerException();

		EnzymeClass enzymeClass = null;
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<EnzymeCommissionNumber, EnzymeClass> classes = (Map<EnzymeCommissionNumber, EnzymeClass>) application
				.getAttribute("classes");

		// Check if object already stored in the cache.
		if (classes.containsKey(ec)) {
			enzymeClass = (EnzymeClass) classes.get(ec);
		} else { // If the object is not stored in the cache load it from the
					// database.
			try {
				enzymeClass = classMapper.find(Integer.toString(ec.getEc1()),
						con);
				if (enzymeClass != null) {
					classes.put(ec, enzymeClass);
					application.setAttribute("classes", classes);
				}
			} catch (SQLException e) {
				LOGGER.error("Finding enzyme class", e);
				IntEnzMessenger.sendError(this.getClass().toString(), e
						.getMessage(), (String) request.getSession()
						.getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			} catch (DomainException e) {
				LOGGER.error("Finding enzyme class", e);
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

		return enzymeClass;
	}

	/**
	 * Calls the according <code>find()</code> method and handles empty results
	 * and SQL exceptions.
	 * 
	 * @param ec
	 *            Number of enzyme class to search for.
	 * @return An <code>EnzymeClass</code> instance or <code>null</code>.
	 * @throws NullPointerException
	 *             if any of the parameters is <code>null</code>.
	 */
	protected EnzymeSubclass findEnzymeSubclass(EnzymeCommissionNumber ec) {
		if (ec == null)
			throw new NullPointerException();

		EnzymeSubclass enzymeSubclass = null;
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<EnzymeCommissionNumber, EnzymeSubclass> subclasses = (Map<EnzymeCommissionNumber, EnzymeSubclass>) application
				.getAttribute("subclasses");

		// Check if object already stored in the cache.
		if (subclasses.containsKey(ec)) {
			enzymeSubclass = (EnzymeSubclass) subclasses.get(ec);
		} else { // If the object is not stored in the cache load it from the
			try {
				enzymeSubclass = subclassMapper.find(
						Integer.toString(ec.getEc1()),
						Integer.toString(ec.getEc2()), con);
				if (enzymeSubclass != null) {
					subclasses.put(ec, enzymeSubclass);
					application.setAttribute("subclasses", subclasses);
				}
			} catch (SQLException e) {
				LOGGER.error("Finding enzyme subclass", e);
				IntEnzMessenger.sendError(this.getClass().toString(), e
						.getMessage(), (String) request.getSession()
						.getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			} catch (DomainException e) {
				LOGGER.error("Finding enzyme subclass", e);
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
		return enzymeSubclass;
	}

	/**
	 * Calls the according <code>find()</code> method and handles empty results
	 * and SQL exceptions.
	 * 
	 * @param ec
	 *            Number of enzyme class to search for.
	 * @return An <code>EnzymeClass</code> instance or <code>null</code>.
	 * @throws NullPointerException
	 *             if any of the parameters is <code>null</code>.
	 */
	protected EnzymeSubSubclass findEnzymeSubSubclass(EnzymeCommissionNumber ec) {
 		if (ec == null)
			throw new NullPointerException();

		EnzymeSubSubclass enzymeSubSubclass = null;
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<EnzymeCommissionNumber, EnzymeSubSubclass> subsubclasses = (Map<EnzymeCommissionNumber, EnzymeSubSubclass>) application
				.getAttribute("subsubclasses");

		// Check if object already stored in the cache.
		if (subsubclasses.containsKey(ec)) {
			enzymeSubSubclass = (EnzymeSubSubclass) subsubclasses.get(ec);
		} else { // If the object is not stored in the cache load it from the
					// database.
			try {
				enzymeSubSubclass = subSubclassMapper.find(ec.getEc1(),
						ec.getEc2(), ec.getEc3(), con);
				if (enzymeSubSubclass != null) {
					subsubclasses.put(ec, enzymeSubSubclass);
					application.setAttribute("subsubclasses", subsubclasses);
				}
			} catch (DomainException e) {
				LOGGER.error("Finding enzyme subSubclass", e);
				PropertyResourceBundle intenzMessages = (PropertyResourceBundle) request
						.getSession().getServletContext()
						.getAttribute("intenz.messages");
				IntEnzMessenger.sendError(this.getClass().toString(),
						intenzMessages.getString(e.getMessageKey()),
						(String) request.getSession().getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			} catch (Exception e) {
				LOGGER.error("Finding enzyme subSubclass", e);
				IntEnzMessenger.sendError(this.getClass().toString(),
						e.getMessage(),
						(String) request.getSession().getAttribute("user"));
				request.setAttribute("message", e.getMessage());
				return null;
			}
		}
		return enzymeSubSubclass;
	}

	/**
	 * Gets the type of the EC number.
	 * <p/>
	 * The following types are possible (in [] the value returned by this
	 * method):
	 * <ul>
	 * <li>Class EC (e.g. EC 1) [1]</li>
	 * <li>Subclass EC (e.g. EC 1.1) [2]</li>
	 * <li>Sub-subclass EC (e.g. EC 1.1.1) [3]</li>
	 * <li>Entry EC (e.g. EC 1.1.1.1) [4]</li>
	 * </ul>
	 * 
	 * @param ec
	 *            The EC string.
	 * @return Integer code for the type.
	 */
	protected int analyseECType(String ec) {
		int count = 0;
		for (StringTokenizer stringTokenizer = new StringTokenizer(ec, "."); stringTokenizer
				.hasMoreTokens();) {
			stringTokenizer.nextToken();
			count++;
		}

		return count;
	}

	// ------------------- PRIVATE METHODS ------------------------

	/**
	 * Calls the according <code>find()</code> method and handles empty results
	 * and SQL exceptions.
	 * 
	 * @param ec
	 *            EC number of enzyme to search for.
	 * @param status
	 *            the enzyme status.
	 * @return An <code>EnzymeEntry</code> instance or <code>null</code>.
	 * @throws NullPointerException
	 *             if any of the parameters is <code>null</code>.
	 */
	protected EnzymeEntry findEnzymeEntry(EnzymeCommissionNumber ec,
			Status status) {
		if (ec == null || status == null)
			throw new NullPointerException();

		EnzymeEntry enzymeEntry = null;
		ServletContext application = request.getSession().getServletContext();
		@SuppressWarnings("unchecked")
		Map<EnzymeEntryCacheKey, EnzymeEntry> entries = (Map<EnzymeEntryCacheKey, EnzymeEntry>) application
				.getAttribute("entries_ec");

		// Check if object already stored in the cache.
		EnzymeEntryCacheKey key = new EnzymeEntryCacheKey(ec, status);
		if (entries.containsKey(key)) {
			enzymeEntry = (EnzymeEntry) entries.get(key);
		} else {
			try {
				Long enzyme_id = enzymeEntryMapper.findIDInMappingTable(
						ec.toString(), status, con);
				if (enzyme_id.longValue() == -1)
					return null;
				enzymeEntry = enzymeEntryMapper.findById(enzyme_id, con);
				if (enzymeEntry != null)
					entries.put(key, enzymeEntry);
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
				LOGGER.error("Finding enzyme entry", e);
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

	protected static class EnzymeEntryCacheKey {
		private EnzymeCommissionNumber ec;
		private Status status;

		public EnzymeEntryCacheKey(EnzymeCommissionNumber ec, Status status) {
			this.ec = ec;
			this.status = status;
		}

		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof SearchECCommand.EnzymeEntryCacheKey))
				return false;

			final SearchECCommand.EnzymeEntryCacheKey enzymeEntryCacheKey = (SearchECCommand.EnzymeEntryCacheKey) o;

			if (ec != null ? !ec.equals(enzymeEntryCacheKey.ec)
					: enzymeEntryCacheKey.ec != null)
				return false;
			if (status != null ? !status.equals(enzymeEntryCacheKey.status)
					: enzymeEntryCacheKey.status != null)
				return false;

			return true;
		}

		public int hashCode() {
			int result;
			result = (ec != null ? ec.hashCode() : 0);
			result = 29 * result + (status != null ? status.hashCode() : 0);
			return result;
		}

		public String toString() {
			StringBuffer output = new StringBuffer();
			output.append("EC: ");
			output.append(ec.toString());
			output.append("\n");
			output.append("Status: ");
			output.append(status.toString());
			output.append("\n");
			return output.toString();
		}
	}
}
