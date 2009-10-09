package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.DatabaseInstance;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.webapp.IntEnzConnectionPool;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
import uk.ac.ebi.xchars.SpecialCharacters;

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
public class IntEnzHandlerServlet extends HttpServlet {

   Logger LOGGER = Logger.getLogger(IntEnzHandlerServlet.class);

 // private IntEnzConnectionPool intEnzCacheManager;

  /**
   * Initialises the connection pooling and initialises the SpecialCharacters instance.
   * <p/>
   * The SpecialCharacters instance is stored in application scope after it has been initialised. However, every
   * session will have its own (cloned) instance of this class, to prevent the application from parsing the XML files
   * again and again.
   *
   * @param servletConfig ...
   * @throws ServletException ...
   */
  public void init(ServletConfig servletConfig)throws ServletException {
    super.init(servletConfig);

    // Load application properties.
    try {
      InputStream in = IntEnzHandlerServlet.class.getClassLoader().getResourceAsStream("application.properties");
      PropertyResourceBundle applicationProperties = new PropertyResourceBundle(in);
      this.getServletContext().setAttribute("application_properties", applicationProperties);
      in.close();
      Properties intenzProps = new Properties();
      intenzProps.load(getClass().getClassLoader().getResourceAsStream("intenz-release.properties"));
      this.getServletContext().setAttribute("intenz_properties", intenzProps);
    } catch (IOException e) {
       LOGGER.error(e);
      throw new ServletException("Error during initialisation.", e);
    }
    
    try {
        Properties spotlightsMap = new Properties();
		spotlightsMap.load(getClass().getClassLoader().getResourceAsStream("spotlights.properties"));
		SortedMap<EnzymeCommissionNumber, String> sortedSpotlights =
			new TreeMap<EnzymeCommissionNumber, String>();
		for (Object ec : spotlightsMap.keySet()){
			sortedSpotlights.put(EnzymeCommissionNumber.valueOf(ec.toString()),
								 (String) spotlightsMap.get(ec));
		}
		this.getServletContext().setAttribute("spotlightsMap", sortedSpotlights);

		this.getServletContext().setAttribute("views", EnumSet.of(View.INTENZ, View.SIB));
	} catch (Exception e) {
       LOGGER.error(e);
       throw new ServletException("Error during initialisation.", e);
	}

    // Init caches.
    try {
        String dbConfig = this.getServletConfig().getServletContext()
            .getInitParameter("intenz.db.config");
        DatabaseInstance odbi = OracleDatabaseInstance.getInstance(dbConfig);
        String[] dbInitValues = new String[4];
        dbInitValues[0] = odbi.getUrl();
        dbInitValues[1] = odbi.getDriver();
        dbInitValues[2] = odbi.getUser();
        dbInitValues[3] = odbi.getPassword();

      IntEnzConnectionPool intEnzCacheManager = IntEnzConnectionPool.getInstance(dbInitValues);
       getServletContext().setAttribute("intEnzCacheManager", intEnzCacheManager);
    } catch (Exception e) {
      IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), "servlet initialisation");
      LOGGER.error(e);
    }

    // Caches
    Map entriesEc = new HashMap(4500); // Currently there are ~4500 entries which will minimise rehash calls when the amount will increase.
    getServletContext().setAttribute("entries_ec", entriesEc);
    Map entriesId = new HashMap(4500); // Stored by using the id.
    getServletContext().setAttribute("entries_id", entriesId);
    Map proposedEntries = new HashMap(100); // Stored by using the id.
    getServletContext().setAttribute("proposedEntries", proposedEntries);
    getServletContext().setAttribute("proposedList", null);
    Map subSubclasses = new HashMap(250);
    getServletContext().setAttribute("subsubclasses", subSubclasses);
    Map subclasses = new HashMap(65);
    getServletContext().setAttribute("subclasses", subclasses);
    Map classes = new HashMap(6);
    getServletContext().setAttribute("classes", classes);

    // Set special characters instance and save it in application scope.
    this.getServletContext().setAttribute("characters", SpecialCharacters.getInstance(null));
  }

  /**
   * This front handler sets the SpecialCharacters instance for each session and invokes the requested command.
   * <p/>
   * The SpecialCharacters instance is created only once per session and allows to provide a browser-dependent
   * representation of special characters, if it is demanded. See {@link ChangeCharacterRepresentationCommand#process() this command}
   * for more information.
   * <p/>
   * The following commands are currently available:
   * <p/>
   * <ul>
   * <li>{@link BrowseResultCommand BrowseResultCommand} - allows navigating through search results.</li>
   * <li>{@link ChangeCharacterRepresentationCommand ChangeCharacterRepresentationCommand} - allows to switch between special character
   * representations.</li>
   * <li>{@link SearchCommand SearchCommand} - Provides free text search functionality.</li>
   * <li>{@link SearchECCommand SearchECCommand} - Searches for enzymes using an EC.</li>
   * <li>{@link SearchIDCommand SearchIDCommand} - Searches for enzymes using the ID.</li>
   * <li>{@link SearchProposedCommand SearchProposedCommand} - Finds all proposed enzymes.</li>
   * <li>{@link UnknownDatabaseCommand UnknownDatabaseCommand} - Is used when the URL contains an unknown command.</li>
   * </ul>
   *
   * @param request  ...
   * @param response ...
   * @throws ServletException ...
   * @throws IOException      ...
   */
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     Command command = null;
     try {
      command = getCommand(request);

      if (command instanceof DatabaseCommand) {
        DatabaseCommand databaseCommand = (DatabaseCommand) command;
        databaseCommand.init(getServletContext(), request, response, ((IntEnzConnectionPool)getServletContext().getAttribute("intEnzCacheManager")).getConnection());
      } else {
        NonDatabaseCommand nonDatabaseCommand = (NonDatabaseCommand) command;
        nonDatabaseCommand.init(getServletContext(), request, response);
      }

      command.process();
    } catch (SQLException e) {
       LOGGER.error(e);
      IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(),
                                (String) request.getSession().getAttribute("user"));
      request.setAttribute("message", "The following database error occured:\n" + e.getMessage());
      request.getRequestDispatcher("/error.jsp").forward(request, response);
    } finally {
       if ( command!= null && command instanceof DatabaseCommand ){
          DatabaseCommand databaseCommand = (DatabaseCommand) command;
          databaseCommand.closeConnection();
       }
    }
  }


  /**
   * Closes all physical database connections.
   */
  public void destroy() {
     LOGGER.debug("Cleaning up connections.");
    ((IntEnzConnectionPool)getServletContext().getAttribute("intEnzCacheManager")).cleanUp();
  }

  // ----------------  PRIVATE METHODS ------------------

  private Command getCommand(HttpServletRequest request) {
    Command command = null;
    try {
      command = (Command) getCommandClass(request).newInstance();
    } catch (Exception e) {
      return new UnknownDatabaseCommand();
    }

    return command;
  }

  private Class getCommandClass(HttpServletRequest request) {
    Class result;

    try {
      if (request.getParameter("cmd") == null || request.getParameter("cmd").equals(""))
        throw new ClassNotFoundException();

      final String commandClassName = "uk.ac.ebi.intenz.webapp.controller." + request.getParameter("cmd") + "Command";
      result = Class.forName(commandClassName);
    } catch (ClassNotFoundException e) {
      result = UnknownDatabaseCommand.class;
    }

    return result;
  }

}
