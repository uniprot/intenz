package uk.ac.ebi.intenz.webapp.controller;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * All commands of this type do not need a connection to the database.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public abstract class NonDatabaseCommand extends Command {

  protected ServletContext context;

  protected HttpServletRequest request;

  protected HttpServletResponse response;

  /**
   * Initialises the attributes which will be used to extract information for the command execution.
   *
   * @param context The servlet context.
   * @param request The request object.
   * @param response The response object.
   */
  public void init(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
    this.context = context;
    this.request = request;
    this.response = response;
  }

  /**
   * Forwards to the given target.
   *
   * @param target A valid URI.
   * @throws ServletException
   * @throws IOException
   */
  protected void forward(String target) throws ServletException, IOException {
    RequestDispatcher dispatcher = context.getRequestDispatcher(target);
    dispatcher.forward(request, response);
  }
}
