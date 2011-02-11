package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;

/**
 * Maps enzyme names information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeNameMapper {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeNameMapper.class.getName());

  private static final String COLUMNS = "enzyme_id, name, name_class, warning, status, source, note, order_in, web_view";

  public EnzymeNameMapper() {
  }

  private String exportSibNamesStatement() {
    return "SELECT " + COLUMNS +
           " FROM names WHERE enzyme_id = ?" +
           " AND (web_view = ? OR web_view = ? OR web_view = ? OR web_view = ?)" +
           " FOR UPDATE ORDER BY UPPER(name)";
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM names" +
           " WHERE enzyme_id = ?" +
           " ORDER BY order_in";
  }

  private String findByClassStatement() {
    return "SELECT " + COLUMNS +
           " FROM names" +
           " WHERE enzyme_id = ? AND name_class = ?" +
           " ORDER BY order_in";
  }

  private String insertStatement() {
    return "INSERT INTO names (enzyme_id, name, name_class, warning, status, source, note, order_in, web_view) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  }

  private String updateNameStatement() {
    return "UPDATE names SET name = ?, warning = ?, status = ?, source = ?, web_view = ? " +
           "WHERE enzyme_id = ? AND name_class = ?";
  }

  private String deleteNamesStatement() {
    return "DELETE names WHERE enzyme_id = ? AND name_class = ?";
  }

  /**
   * Tries to find name information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>Vector</code> containing <code>EnzymeName</code>instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeName> find(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeName> result = new ArrayList<EnzymeName>();
    boolean noResult = true;

    try {
      // Core information.
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        EnzymeName name = doLoad(rs);
        if (name != null) {
          noResult = false;
          result.add(name);
        }
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Exports all names which are displayed in the ENZYME view.
   *
   * Affected table rows will be locked.
   *
   * @param enzymeId The enzyme ID used to retreive the related names.
   * @param con The database connection.
   * @return an {@link java.util.ArrayList} of names or <code>null</code> if no name could be found.
   * @throws SQLException if a database error occured.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public List<EnzymeName> exportSibNames(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    List<EnzymeName> result = new ArrayList<EnzymeName>();
    boolean noResult = true;

    try {
      // Core information.
      findStatement = con.prepareStatement(exportSibNamesStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeViewConstant.INTENZ.toString());
      findStatement.setString(3, EnzymeViewConstant.IUBMB_SIB.toString());
      findStatement.setString(4, EnzymeViewConstant.SIB.toString());
      findStatement.setString(5, EnzymeViewConstant.SIB_INTENZ.toString());
      ResultSet rs = findStatement.executeQuery();
      while (rs.next()) {
        EnzymeName name = doLoad(rs);
        if (name != null) {
          noResult = false;
          result.add(name);
        }
      }
    } finally {
      if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Finds only the common name of an entry.
   * <p/>
   * This method will be used to load a list of ghost entries.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return the name or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeName> findCommonNames(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    boolean noResult = true;
    List<EnzymeName> commonNames = new ArrayList<EnzymeName>();
    EnzymeName result = null;

    try {
      // Core information.
      findStatement = con.prepareStatement(findByClassStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeNameTypeConstant.COMMON_NAME.toString());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        result = doLoad(rs);
        if (result != null) {
          noResult = false;
          commonNames.add(result);
        }
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return commonNames;
  }

  /**
   * Finds only the systematic name of an entry.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return the name or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public EnzymeName findSystematicName(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    EnzymeName result = null;
    try {
      // Core information.
      findStatement = con.prepareStatement(findByClassStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString());
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return result;
  }

  public void update(EnzymeName name, Long enzymeId, Status status, int orderIn, Connection con)
          throws SQLException {
    if (name == null) throw new NullPointerException("Parameter 'name' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (orderIn < 1) throw new IllegalArgumentException("Parameter 'orderIn' must not be < 1.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement updateNameStatement = null;
    try {
      updateNameStatement = con.prepareStatement(updateNameStatement());
      doUpdate(name, enzymeId, status, orderIn, updateNameStatement);
      updateNameStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (updateNameStatement != null) updateNameStatement.close();
    }
  }

  public void insertNames(List<EnzymeName> names, Long enzymeId, Status status, Connection con)
          throws SQLException {
    if (names == null) throw new NullPointerException("Parameter 'names' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    for (int iii = 0; iii < names.size(); iii++) {
      insert(names.get(iii), enzymeId, status, (iii+1), con);
    }
  }

  public void insert(EnzymeName enzymeName, Long enzymeId, Status status, int orderIn, Connection con)
          throws SQLException {
    if (enzymeName == null) throw new NullPointerException("Parameter 'enzymeName' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertStatement = null;
    try {
      insertStatement = con.prepareStatement(insertStatement());
      doInsert(enzymeName, enzymeId, status, orderIn, insertStatement);
      insertStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (insertStatement != null) insertStatement.close();
    }
  }

  public void reload(List<EnzymeName> names, Long enzymeId, EnzymeNameTypeConstant type, Status status,
                     Connection con)
          throws SQLException {
    deleteNames(enzymeId, type, con);
    insertNames(names, enzymeId, status, con);
  }

  public void deleteNames(Long enzymeId, EnzymeNameTypeConstant nameType, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteNamesStatement = null;
    try {
      deleteNamesStatement = con.prepareStatement(deleteNamesStatement());
      deleteNamesStatement.setLong(1, enzymeId.longValue());
      deleteNamesStatement.setString(2, nameType.toString());
      deleteNamesStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteNamesStatement != null) deleteNamesStatement.close();
    }
  }

  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>EnzymeName</code> object from the given result set.
   * <p/>
   * This method calls all related mappers for names, references, etc.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeName</code> instance.
   * @throws SQLException
   */
  private EnzymeName doLoad(ResultSet rs) throws SQLException {
    assert rs != null : "Parameter 'rs' must not be null.";

    String name = "";
    String nameClass = "";
    String warning = "";
    String source = "";
    String webView = "";

    if (rs.getString("name") != null) name = rs.getString("name");
    if (rs.getString("name_class") != null) nameClass = rs.getString("name_class");
    if (rs.getString("warning") != null) warning = rs.getString("warning");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("web_view") != null) webView = rs.getString("web_view");

    return EnzymeName.valueOf(name, EnzymeNameTypeConstant.valueOf(nameClass.toUpperCase()),
                              EnzymeNameQualifierConstant.valueOf(warning.toUpperCase()),
                              EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(webView));
  }

  /**
   * Sets the parameters of the insert statement
   *
   * @param name            ...
   * @param enzymeId        ...
   * @param status          ...
   * @param insertStatement ...
   * @throws SQLException
   */
  private void doInsert(EnzymeName name, Long enzymeId, Status status, int orderIn,
                        PreparedStatement insertStatement) throws SQLException {
    assert name != null : "Parameter 'name' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert orderIn > 0 : "Parameter 'orderIn' must not be < 1.";
    assert insertStatement != null : "Parameter 'insertStatement' must not be null.";

    insertStatement.setLong(1, enzymeId.longValue());
    insertStatement.setString(2, name.getName());
    insertStatement.setString(3, name.getType().toString());
    if (name.getQualifier() == EnzymeNameQualifierConstant.UNDEF)
      insertStatement.setNull(4, Types.VARCHAR);
    else
      insertStatement.setString(4, name.getQualifier().toString());
    insertStatement.setString(5, status.getCode());
    insertStatement.setString(6, name.getSource().toString());
    insertStatement.setNull(7, Types.VARCHAR);
    insertStatement.setInt(8, orderIn);
    insertStatement.setString(9, name.getView().toString());
  }

  private void doUpdate(EnzymeName name, Long enzymeId, Status status, int orderIn,
                        PreparedStatement updateStatement) throws SQLException {
    assert name != null : "Parameter 'name' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert orderIn > 0 : "Parameter 'orderIn' must not be < 1.";
    assert updateStatement != null : "Parameter 'updateStatement' must not be null.";

    updateStatement.setString(1, name.getName());
    if (name.getQualifier() == EnzymeNameQualifierConstant.UNDEF)
      updateStatement.setNull(2, Types.VARCHAR);
    else
      updateStatement.setString(2, name.getQualifier().toString());
    updateStatement.setString(3, status.getCode());
    updateStatement.setString(4, name.getSource().toString());
    updateStatement.setString(5, name.getView().toString());
    updateStatement.setLong(6, enzymeId.longValue());
    updateStatement.setString(7, name.getType().toString());
  }
}
