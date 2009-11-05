package uk.ac.ebi.intenz.webapp.controller;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * This is the abstract base class of all commands.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public abstract class Command {
  /**
   * This is unique to each command and has to be implemented by the inherting subclass.
   *
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  abstract public void process() throws ServletException, IOException;
}
