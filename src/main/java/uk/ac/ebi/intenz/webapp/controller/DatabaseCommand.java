package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

/**
 * A database command always relies on a connection to the database.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public abstract class DatabaseCommand extends Command {

   Logger LOGGER = Logger.getLogger(DatabaseCommand.class);

  protected final String databaseErrorMessage = "\nIt was not possible to retrieve any information from the database. " +
          "\nIt would be very kind of you, if you could report this error to " +
          "<a href=\"http://www.ebi.ac.uk/support/\">EBI Support</a>.\n" +
          "Sorry for any inconvenience this error might have caused.";

  protected ServletContext context;

  protected HttpServletRequest request;

  protected HttpServletResponse response;

  protected Connection con;

	protected final EnzymeEntryMapper enzymeEntryMapper =
			new EnzymeEntryMapper();

  /**
   * Initialises the attributes which will be used to extract information for the command execution.
   *
   * @param context  The servlet context.
   * @param request  The request object.
   * @param response The response object.
   * @param con      A logical connection.
   */
  public void init(ServletContext context, HttpServletRequest request, HttpServletResponse response, Connection con) {
    this.context = context;
    this.request = request;
    this.response = response;
    this.con = con;
  }

  /**
   * Forwards to the given target.
   *
   * @param target A valid URI.
   * @throws ServletException
   * @throws IOException
   */
  protected void forward(String target) throws ServletException, IOException {
    closeConnection();
    RequestDispatcher dispatcher = context.getRequestDispatcher(target);
    dispatcher.forward(request, response);
  }

  /**
   * Closes the database connection or forwards to an error page if any error occured.
   * 
   * @throws ServletException
   * @throws IOException
   */
  protected void closeConnection() throws ServletException, IOException {
    try {

       if (con != null && !con.isClosed()){
          LOGGER.debug("Closing connection");
         con.close();
       }
    } catch (SQLException e) {
       LOGGER.error("Closing connection", e);
      IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(),
            (String) request.getSession().getAttribute("user"));
      request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
              this.databaseErrorMessage);
      RequestDispatcher dispatcher = context.getRequestDispatcher("/error.jsp");
      dispatcher.forward(request, response);
    }
  }
  
	protected void close() {
		enzymeEntryMapper.close();
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}

}
