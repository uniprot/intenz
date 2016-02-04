package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;

/**
 * Maps enzyme Sub-subclass information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeSubSubclassMapper {

  private static final String COLUMNS = "s1.ec1, s1.ec2, s1.ec3, s1.name, s2.name, s3.name, s1.description";

  private static final String SELECT_ALL = "SELECT " + COLUMNS
      + " FROM subsubclasses s1, subclasses s2, classes s3";
  
  public EnzymeSubSubclassMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM subsubclasses s1, subclasses s2, classes s3 WHERE s3.ec1 = ? AND s2.ec1 = ? AND s2.ec2 = ? AND s1.ec1 = ? AND s1.ec2 = ? AND s1.ec3 = ?" +
           " ORDER BY s1.ec1, s1.ec2, s1.ec3";
  }

  private String findListStatement() {
    return "SELECT " + COLUMNS +
           " FROM subsubclasses s1, subclasses s2, classes s3 WHERE s3.ec1 = ? AND s2.ec1 = ? AND s2.ec2 = ? AND s1.ec1 = ? AND s1.ec2 = ?" +
           " ORDER BY s1.ec1, s1.ec2, s1.ec3";
  }

  private String findSubSubclassOnly() {
    return "SELECT ec1 FROM subsubclasses WHERE ec1 = ? AND ec2 = ? AND ec3 = ?";
  }

  private String insertStatement() {
    return "INSERT INTO subsubclasses (ec1, ec2, ec3, name, description, active ) VALUES (?, ?, ?, ?, ?, ?)";
  }

  /**
   * Tries to find Sub-subclass information about an enzyme.
   *
   * @param ec1 Number of class to search for.
   * @param ec2 Number of subclass to search for.
   * @param ec3 Number of sub-subclass to search for.
   * @param con The logical connection.
   * @return an <code>EnzymeClass</code> instance or <code>null</code> if nothing has been found.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   * @throws SQLException         if a database error occurs.
   * @throws DomainException      if any error related to domain information occurs.
   */
  public EnzymeSubSubclass find(int ec1, int ec2, int ec3, Connection con)
  throws SQLException, DomainException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    EnzymeSubSubclass result = null;
    List<EnzymeEntry> entries;

    // Get entries.
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    entries = enzymeEntryMapper.findAllSubSubclassEntriesByEc(ec1, ec2, ec3, con);
    if(entries == null) entries = new ArrayList<EnzymeEntry>();

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setInt(1, ec1);
      findStatement.setInt(2, ec1);
      findStatement.setInt(3, ec2);
      findStatement.setInt(4, ec1);
      findStatement.setInt(5, ec2);
      findStatement.setInt(6, ec3);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs, entries);
      }
    } finally {
    	enzymeEntryMapper.close();
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return result;
  }

  /**
   * Tries to find all Sub-subclasses requested.
   *
   * @param ec1 Number/Wildcard of class to search for.
   * @param ec2 Number/Wildcard of subclass to search for.
   * @param con The logical connection.
   * @return a <code>Vector</code> of <code>SubSubClass</code> instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeSubSubclass> findList(String ec1, String ec2, Connection con)
  throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeSubSubclass> result = new ArrayList<EnzymeSubSubclass>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findListStatement());
      findListStatement.setString(1, ec1);
      findListStatement.setString(2, ec1);
      findListStatement.setString(3, ec2);
      findListStatement.setString(4, ec1);
      findListStatement.setString(5, ec2);
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        result.add(doLoad(rs, null));
      }
    } finally {
    	if (rs != null) rs.close();
      if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }
  
  public List<EnzymeSubSubclass> findAll(Connection con) throws SQLException, DomainException{
      List<EnzymeSubSubclass> result = new ArrayList<EnzymeSubSubclass>();
      Statement stm = null;
      ResultSet rs = null;
      try {
          stm = con.createStatement();
          rs = stm.executeQuery(SELECT_ALL);
          while (rs.next()){
              result.add(doLoad(rs, null));
          }
      } finally {
      	if (rs != null) rs.close();
          if (stm != null) stm.close();
      }
      return result;
  }

  /**
   * Creates a new row in the <code>SUBSUBCLASSES</code> table.
   *
   * @param ec EC number of this sub-subclass.
   * @param name The name of the sub-subclass.
   * @param description The description of the sub-subclass.
   * @param con The database connection.
   * @throws SQLException if a database error occurs.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public synchronized void insertSubSubclass(EnzymeCommissionNumber ec, String name, String description, Connection con) throws SQLException {
    if (ec == null) throw new NullPointerException("Parameter 'ec' must not be null.");
    if (name == null) throw new NullPointerException("Parameter 'name' must not be null.");
    if (description == null) throw new NullPointerException("Parameter 'description' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertStatement = null;
    try {
      insertStatement = con.prepareStatement(insertStatement());
      doInsert(ec, name, description, insertStatement);
      insertStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (insertStatement != null) insertStatement.close();
    }
  }

  /**
   * Checks if the given sub-subclass numbers are valid.
   *
   * @param ec1 The class number.
   * @param ec2 The subclass number.
   * @param ec3 The sub-subclass number.
   * @param con The connection.
   * @return <code>true</code> if the class exists.
   * @throws SQLException
   */
  public boolean subSubclassExists(String ec1, String ec2, String ec3, Connection con) throws SQLException {
    PreparedStatement findStatement = null;
    ResultSet rs = null;

    try {
      findStatement = con.prepareStatement(findSubSubclassOnly());
      findStatement.setString(1, ec1);
      findStatement.setString(2, ec2);
      findStatement.setString(3, ec3);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        return true;
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return false;
  }

  
  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>EnzymeSubSubclass</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeSubSubclass</code> instance.
   * @throws SQLException
   */
  private EnzymeSubSubclass doLoad(ResultSet rs, List<EnzymeEntry> entries)
  throws SQLException, DomainException {
    int ec1 = 0;
    int ec2 = 0;
    int ec3 = 0;
    String name = "";
    String subclassName = "";
    String className = "";
    String description = "";

    if (rs.getInt(1) > 0) ec1 = rs.getInt(1);
    if (rs.getInt(2) > 0) ec2 = rs.getInt(2);
    if (rs.getInt(3) > 0) ec3 = rs.getInt(3);
    if (rs.getString(4) != null) name = rs.getString(4);
    if (rs.getString(5) != null) subclassName = rs.getString(5);
    if (rs.getString(6) != null) className = rs.getString(6);
    if (rs.getString(7) != null) description = rs.getString(7);

    return new EnzymeSubSubclass(EnzymeCommissionNumber.valueOf(ec1, ec2, ec3),
                                 className, subclassName, name, description, entries);
  }

  private void doInsert(EnzymeCommissionNumber ec, String name, String description, PreparedStatement insertStatement) throws SQLException {
    assert ec != null : "Parameter 'ec' must not be null.";
    assert name != null : "Parameter 'name' must not be null.";
    assert description != null : "Parameter 'description' must not be null.";
    assert insertStatement != null : "Parameter 'insertStatement' must not be null.";

    insertStatement.setInt(1, ec.getEc1());
    insertStatement.setInt(2, ec.getEc2());
    insertStatement.setInt(3, ec.getEc3());
    insertStatement.setString(4, name);
    insertStatement.setString(5, description);
    insertStatement.setString(6, "Y");
  }
}
