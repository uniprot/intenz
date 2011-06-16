package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;

import javax.servlet.ServletException;

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
   * Just forwards to a notification page.
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    forward("/unknown.jsp");
  }
}
