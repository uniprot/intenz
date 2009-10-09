package uk.ac.ebi.intenz.webapp.exceptions;

/**
 * If a query is invalid this exception will be thrown.
 *
 * A query is invalid if it makes incorrect use of reserved characters such as '{', '}' or their surrogate '"'.
 *
 * TODO: Extend for other reserved characters as well.
 *
 * @author Michael Darsow
 * @version 2.0.0 - 27-February-2004
 */
public class QueryException extends Exception {
  public QueryException() {
  }

  public QueryException(String message) {
    super(message);
  }
}
