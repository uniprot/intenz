package uk.ac.ebi.intenz.webapp.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.stats.IIntEnzStatistics;
import uk.ac.ebi.intenz.stats.db.IntEnzDbStatistics;
import uk.ac.ebi.intenz.webapp.IntEnzConfig;
import uk.ac.ebi.intenz.webapp.controller.SearchECCommand.EnzymeEntryCacheKey;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
import uk.ac.ebi.xchars.SpecialCharacters;

import static uk.ac.ebi.intenz.webapp.IntEnzConfig.Property.DATA_SOURCE;

/**
 * This servlet acts as a handler of the front controller.
 * <p/>
 * This handler processes all queries that require database access.
 * The URL will be parsed and depending on the parameters according actions will be performed.
 * See {@link #service(HttpServletRequest, HttpServletResponse) service(HttpServletRequest, HttpServletResponse)}
 * for more information about the specific actions.
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2009/04/16 15:02:01 $
 */
public class IntEnzHandlerServlet extends HttpServlet
        implements PropertyChangeListener {

	private static final long serialVersionUID = -7167746042181128767L;
	
	private Logger LOGGER = Logger.getLogger(IntEnzHandlerServlet.class);

    // JNDI context
    private Context envContext;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		initJndiContext();
		loadProperties();
		populateApplication();
		createCaches();
		gatherStatistics();
        try {
            IntEnzConfig.getInstance()
                .addPropertyChangeListener(DATA_SOURCE.toString(), this);
        } catch (IOException e){
            throw new ServletException(e);
        }
    }

	@Override
	public void destroy() {
		IntEnzConfig intenzConfig = null;
		try {
			intenzConfig = IntEnzConfig.getInstance();
			try {
				ObjectName name = new ObjectName(intenzConfig.getIntEnzConfigMbeanName());
				MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				mbs.unregisterMBean(name);
			} catch (MalformedObjectNameException e) {
				LOGGER.error(intenzConfig.getIntEnzConfigMbeanName());
			} catch (MBeanRegistrationException e) {
				LOGGER.error("Unable to unregister IntEnzConfig MBean", e);
			} catch (InstanceNotFoundException e) {
				LOGGER.error("IntEnzConfig MBean not found", e);
			}
		} catch (IOException e) {
			LOGGER.error("Unable to get the IntEnz configuration", e);
		}
		super.destroy();
	}

	/**
	 * Initialises the JNDI context to get database connections from.
	 * @throws ServletException
	 */
	private void initJndiContext() throws ServletException {
		try {
			Context initContext = new InitialContext();
			envContext = (Context) initContext.lookup("java:/comp/env");
		} catch (NamingException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * Loads properties files:
	 * <ul>
	 * 	<li>messages</li>
	 * 	<li>enzyme spotlights</li>
	 * </ul>
	 * @throws ServletException
	 */
	private void loadProperties() throws ServletException {
		InputStream in = null;
		try {
			in = IntEnzHandlerServlet.class.getClassLoader()
					.getResourceAsStream("messages.properties");
			PropertyResourceBundle intenzMessages = new PropertyResourceBundle(in);
			this.getServletContext().setAttribute("intenz.messages", intenzMessages);

			Properties spotlightsMap = new Properties();
			in = getClass().getClassLoader().getResourceAsStream("spotlights.properties");
			spotlightsMap.load(in);
			SortedMap<EnzymeCommissionNumber, String> sortedSpotlights =
					new TreeMap<EnzymeCommissionNumber, String>();
			for (Object ec : spotlightsMap.keySet()) {
				sortedSpotlights.put(
						EnzymeCommissionNumber.valueOf(ec.toString()),
						(String) spotlightsMap.get(ec));
			}
			this.getServletContext().setAttribute("spotlightsMap", sortedSpotlights);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new ServletException("Error during initialisation.", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close input stream", e);
				}
			}
		}
	}

	/**
	 * Populates the application scope with other required objects:
	 * <ul>
	 * 	<li>xchars</li>
	 * 	<li>application configuration (the also JMX-exposed mbean)</li>
	 * 	<li>enzyme views</li>
	 * </ul>
	 */
	private void populateApplication() {
		this.getServletContext().setAttribute("characters",
				SpecialCharacters.getInstance(null));
		try {
			IntEnzConfig intenzConfig = IntEnzConfig.getInstance();
			this.getServletContext().setAttribute("intenzConfig", intenzConfig);
			// Publish as MBean:
			ObjectName name = new ObjectName(intenzConfig.getIntEnzConfigMbeanName());
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			mbs.registerMBean(intenzConfig, name);
		} catch (Exception e) {
			LOGGER.error("Unable to load IntEnz configuration", e);
		}
		
		this.getServletContext().setAttribute("views", EnumSet.of(View.INTENZ, View.SIB));
	}

	/**
	 * Creates caches for requested objects.
	 */
	private void createCaches() {
		Map<EnzymeEntryCacheKey, EnzymeEntry> entriesEc =
			new HashMap<EnzymeEntryCacheKey, EnzymeEntry>(4500);
		getServletContext().setAttribute("entries_ec", entriesEc);
		Map<Long, EnzymeEntry> entriesId =
			new HashMap<Long, EnzymeEntry>(4500); // Stored by using the id.
		getServletContext().setAttribute("entries_id", entriesId);
		Map<Long, EnzymeEntry> proposedEntries =
			new HashMap<Long, EnzymeEntry>(100); // Stored by using the id.
		getServletContext().setAttribute("proposedEntries", proposedEntries);
		getServletContext().setAttribute("proposedList", null);
		Map<EnzymeCommissionNumber, EnzymeSubSubclass> subSubclasses =
			new HashMap<EnzymeCommissionNumber, EnzymeSubSubclass>(250);
		getServletContext().setAttribute("subsubclasses", subSubclasses);
		Map<EnzymeCommissionNumber, EnzymeSubclass> subclasses =
			new HashMap<EnzymeCommissionNumber, EnzymeSubclass>(65);
		getServletContext().setAttribute("subclasses", subclasses);
		Map<EnzymeCommissionNumber, EnzymeClass> classes =
			new HashMap<EnzymeCommissionNumber, EnzymeClass>(6);
		getServletContext().setAttribute("classes", classes);
	}

	/**
	 * Gathers database statistics and puts them in the application scope.
	 * Must be called <b>after</b> {@link #initJndiContext()}.
	 */
	private void gatherStatistics() {
		Connection statsConnection = null;
		try {
			statsConnection = getConnection();
			LOGGER.info("Gathering statistics for IntEnz...");
			IIntEnzStatistics statistics = new IntEnzDbStatistics(statsConnection);
			LOGGER.info("Done: statistics gathered for IntEnz...");
			getServletContext().setAttribute("statistics", statistics);
		} catch (Exception e) {
			LOGGER.error("Unable to get IntEnz statistics", e);
		} finally {
			if (statsConnection != null){
				try {
					statsConnection.close();
				} catch (SQLException e) {
					LOGGER.error("Unable to close connection", e);
				}
			}
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Command command = null;
		try {
			command = getCommand(request);

			if (command instanceof DatabaseCommand) {
				DatabaseCommand databaseCommand = (DatabaseCommand) command;
				databaseCommand.init(getServletContext(), request, response,
						getConnection());
			} else {
				NonDatabaseCommand nonDatabaseCommand = (NonDatabaseCommand) command;
				nonDatabaseCommand.init(getServletContext(), request, response);
			}

			command.process();
		} catch (Exception e) {
			LOGGER.error("While processing request", e);
			IntEnzMessenger.sendError(this.getClass().toString(),
					e.getMessage(),
					(String) request.getSession().getAttribute("user"));
			request.setAttribute("message",
					"The following database error occured:\n" + e.getMessage());
			request.getRequestDispatcher("/error.jsp").forward(request,
					response);
		} finally {
			if (command != null && command instanceof DatabaseCommand) {
				DatabaseCommand databaseCommand = (DatabaseCommand) command;
				databaseCommand.closeConnection();
			}
		}
	}

	private Command getCommand(HttpServletRequest request) {
		Command command = null;
		try {
			command = (Command) getCommandClass(request).newInstance();
		} catch (Exception e) {
			return new UnknownDatabaseCommand();
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Command> getCommandClass(HttpServletRequest request) {
		Class<? extends Command> result;
		try {
			if (request.getParameter("cmd") == null || request.getParameter("cmd").equals("")){
				throw new ClassNotFoundException();
			}
			final String commandClassName = "uk.ac.ebi.intenz.webapp.controller."
					+ request.getParameter("cmd") + "Command";
			result = (Class<Command>) Class.forName(commandClassName);
		} catch (ClassNotFoundException e) {
			result = UnknownDatabaseCommand.class;
		}
		return result;
	}

	private Connection getConnection()
	throws NamingException, SQLException, IOException {
		DataSource ds = (DataSource) envContext.lookup(IntEnzConfig
				.getInstance().getIntEnzDataSource());
		Connection con = ds.getConnection();
        // This will not be needed if configured in the context file
        // for the new dbcp as init SQL:
        // FIXME when moved to LDCs with new tomcat dbcp
        Statement stm = null;
        try {
            stm = con.createStatement();
            stm.execute("ALTER SESSION SET CURRENT_SCHEMA = \"ENZYME\"");
        } catch (SQLException e){
            LOGGER.error("Unable to change database schema", e);
        } finally {
            if (stm != null) stm.close();
        }
        return con;
	}

    public void propertyChange(PropertyChangeEvent evt){
        try {
            if (DATA_SOURCE.toString().equals(evt.getPropertyName())){
                LOGGER.info("IntEnz data source changed");
                gatherStatistics();
            }
        } catch (Exception e){
            LOGGER.error("Unable to set property " + evt.getPropertyName()
                + " to " + evt.getNewValue(), e);
            throw new IllegalArgumentException(e);
        }
    }

}
