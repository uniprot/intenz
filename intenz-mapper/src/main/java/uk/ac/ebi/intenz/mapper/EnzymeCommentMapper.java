package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;

/**
 * Maps link information to the corresponding database tables.
 * <p/>
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeCommentMapper {

  private static final String COLUMNS = "enzyme_id, comment_text, order_in, status, source, web_view";

  private static final Logger LOGGER = Logger.getLogger(EnzymeCofactorMapper.class);

  public EnzymeCommentMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM comments" +
           " WHERE enzyme_id = ? ORDER BY order_in";
  }

  private String exportSibCommentsStatement() {
    return "SELECT " + COLUMNS +
           " FROM comments" +
           " WHERE enzyme_id = ? AND (web_view = ? OR web_view = ? OR web_view = ? OR web_view = ?)" +
           " FOR UPDATE" +
           " ORDER BY order_in";
  }

  private String insertStatement() {
    return "INSERT INTO comments (enzyme_id, comment_text, order_in, status, source, web_view) VALUES (?, ?, ?, ?, ?, ?)";
  }

  private String updateStatement() {
    return "UPDATE comments SET comment_text = ?, order_in = ?, status = ?, source = ?, web_view = ? WHERE enzyme_id = ?";
  }

  private String deleteAllStatement() {
    return "DELETE comments WHERE enzyme_id = ?";
  }

  /**
   * Tries to find comment information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>String</code> representing the comment.
   * @throws SQLException
   */
  public List<EnzymeComment> find(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeComment> result = new ArrayList<EnzymeComment>();
    boolean noResult = true;

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        noResult = false;
        EnzymeComment enzymeComment = doLoad(rs);
        result.add(enzymeComment);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (noResult) {
      LOGGER.info("No comment information found for the enzyme with ID " + enzymeId);
      return null;
    }
    return result;
  }

  /**
   * Exports all comments which are displayed in the ENZYME view.
   *
   * Affected table rows will be locked.
   *
   * @param enzymeId The enzyme ID used to retreive the related comments.
   * @param con The database connection.
   * @return an {@link java.util.ArrayList} of comments or <code>null</code> if no comment could be found.
   * @throws SQLException if a database error occured.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public List<EnzymeComment> exportSibComments(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeComment> result = new ArrayList<EnzymeComment>();
    boolean noResult = true;

    try {
      findStatement = con.prepareStatement(exportSibCommentsStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeViewConstant.INTENZ.toString());
      findStatement.setString(3, EnzymeViewConstant.IUBMB_SIB.toString());
      findStatement.setString(4, EnzymeViewConstant.SIB.toString());
      findStatement.setString(5, EnzymeViewConstant.SIB_INTENZ.toString());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        noResult = false;
        EnzymeComment enzymeComment = doLoad(rs);
        result.add(enzymeComment);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  public void reload(List<EnzymeComment> comments, Long enzymeId, EnzymeStatusConstant status, Connection con)
          throws SQLException {
    deleteAll(enzymeId, con);
    insert(comments, enzymeId, status, con);
  }

  /**
   * Deletes all comments related to an enzyme instance.
   *
   * @param enzymeId Enzyme ID of the enzyme instance.
   * @param con      A database connection.
   * @throws SQLException if a database error occurs.
   * @throws NullPointerException if any of the parameter is <code>null</code>.
   */
  public void deleteAll(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteAllStatement = null;

    try {
      deleteAllStatement = con.prepareStatement(deleteAllStatement());
      deleteAllStatement.setLong(1, enzymeId.longValue());
      deleteAllStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteAllStatement != null) deleteAllStatement.close();
    }
  }

  /**
   * Stores the given comment into the database.
   *
   * @param comments The enzyme's comment.
   * @param enzymeId The enzyme ID.
   * @param status   ...
   * @param con      ...
   * @throws SQLException
   */
  public void insert(List<EnzymeComment> comments, Long enzymeId, EnzymeStatusConstant status, Connection con)
          throws SQLException {
    if (comments == null) throw new NullPointerException("Parameter 'comments' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertStatement = null;
    try {
      insertStatement = con.prepareStatement(insertStatement());
      for (int iii = 0; iii < comments.size(); iii++) {
        EnzymeComment enzymeComment = comments.get(iii);
        doInsert(enzymeComment, enzymeId, (iii+1), status, insertStatement);
        insertStatement.execute();
//        con.commit();
      }
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (insertStatement != null) insertStatement.close();
    }
  }

  public void update(EnzymeComment comment, Long enzymeId, EnzymeStatusConstant status, int orderIn, Connection con) throws SQLException {
    if (comment == null) throw new NullPointerException("Parameter 'comment' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (orderIn < 1) throw new IllegalArgumentException("Parameter 'orderIn' must not be > 0.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement updateStatement = null;
    try {
      updateStatement = con.prepareStatement(updateStatement());
      doUpdate(comment, enzymeId, status, orderIn, updateStatement);
      updateStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (updateStatement != null) updateStatement.close();
    }
  }

  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>EnzymeComment</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeComment</code> instance.
   * @throws SQLException
   */
  private EnzymeComment doLoad(ResultSet rs) throws SQLException {
    assert rs != null : "Parameter 'rs' must not be null.";

    String comment = "";
    String source = "";
    String webView = "";
    if (rs.getString("comment_text") != null) comment = rs.getString("comment_text");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("web_view") != null) webView = rs.getString("web_view");

    EnzymeComment enzymeComment = new EnzymeComment(comment, EnzymeSourceConstant.valueOf(source),
                                                    EnzymeViewConstant.valueOf(webView));

    return enzymeComment;
  }

  /**
   * Sets the parameters of the prepared statement.
   *
   * @param comment         ...
   * @param enzymeId        ...
   * @param status          ...
   * @param insertStatement ...
   * @throws SQLException
   */
  private void doInsert(EnzymeComment comment, Long enzymeId, int orderIn, EnzymeStatusConstant status,
                        PreparedStatement insertStatement)
          throws SQLException {
    assert comment != null : "Parameter 'comment' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert insertStatement != null : "Parameter 'insertStatement' must not be null.";

    insertStatement.setLong(1, enzymeId.longValue());
    insertStatement.setString(2, comment.getCommentText());
    insertStatement.setInt(3, orderIn);
    insertStatement.setString(4, status.getCode());
    insertStatement.setString(5, comment.getSource().toString());
    insertStatement.setString(6, comment.getView().toString());
  }

  private void doUpdate(EnzymeComment comment, Long enzymeId, EnzymeStatusConstant status, int orderIn,
                        PreparedStatement updateStatement)
          throws SQLException {
    assert comment != null : "Parameter 'comment' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert orderIn > 0 : "Parameter 'orderIn' must not be > 0.";
    assert updateStatement != null : "Parameter 'insertStatement' must not be null.";

    updateStatement.setString(1, comment.getCommentText());
    updateStatement.setInt(2, orderIn);
    updateStatement.setString(3, status.getCode());
    updateStatement.setString(4, comment.getSource().toString());
    updateStatement.setString(5, comment.getView().toString());
    updateStatement.setLong(6, enzymeId.longValue());
  }
}
