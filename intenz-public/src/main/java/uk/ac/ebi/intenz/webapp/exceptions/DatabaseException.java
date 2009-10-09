package uk.ac.ebi.intenz.webapp.exceptions;

import java.sql.SQLException;

/**
 * This exception is a wrapper around the SQLException.
 *
 * The purpose of this class is to provide either a user friendly version of a SQL exception
 * or for the programmer the tough details.
 *
 * @author Michael Darsow
 * @version 2.0.0 - 27-February-2004
 */
public class DatabaseException extends Exception {
  private SQLException sqlException;

  /**
   * Initialise this exception with a user friendly message and the <code>SQLException</code> object.
   *
   * @param message The self-defined message.
   * @param sqlException <code>SQLException</code> instance.
   */
  public DatabaseException(String message, SQLException sqlException) {
    super(message);
    this.sqlException = sqlException;
  }

  public DatabaseException(String message) {
    super(message);
    this.sqlException = null;
  }

  /**
   * Uses a standard message.
   *
   * @param sqlException <code>SQLException</code> instance.
   */
  public DatabaseException(SQLException sqlException) {
    super("\nIt was not possible to retrieve any information from the database." +
            "\nPresumably the database server is either too busy or down at the moment" +
            "\nSorry for any inconvenience this error might have caused.\nPlease try again later.");
    this.sqlException = sqlException;
  }

  // --------------------  GETTER & SETTER -----------------------

  /**
   * Return the details about this exception.
   *
   * @return <code>SQLException</code> message.
   */
  public String getSqlExceptionMessage() {
    return sqlException.toString();
  }
}
