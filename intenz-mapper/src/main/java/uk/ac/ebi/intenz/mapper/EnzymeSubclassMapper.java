package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;

/**
 * Maps enzyme subclass information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeSubclassMapper {

  private static final String COLUMNS = "s.ec1, s.ec2, s.name, c.name, s.description";

  private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM subclasses s, classes c";
  
  public EnzymeSubclassMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS + " FROM subclasses s, classes c WHERE c.ec1 = ? AND s.ec1 = ? AND s.ec2 = ? ORDER BY s.ec1, s.ec2";
  }

  private String findListStatement() {
    return "SELECT " + COLUMNS + " FROM subclasses s, classes c WHERE c.ec1 = ? AND s.ec1 = ? ORDER BY s.ec1, s.ec2";
  }

  private String findSubclassOnlyStatement() {
    return "SELECT ec1 FROM subclasses WHERE ec1 = ? AND ec2 = ?";
  }

  /**
   * Tries to find Subclass information about an enzyme.
   *
   * @param ec1 Number of class to search for.
   * @param ec2 Number of subclass to search for.
   * @param con The logical connection.
   * @return an <code>EnzymeClass</code> instance or <code>null</code> if nothing has been found.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   * @throws SQLException         if a database error occurs.
   * @throws DomainException      if any error related to domain information occurs.
   */
  public EnzymeSubclass find(String ec1, String ec2, Connection con) throws SQLException, DomainException {
    if (ec1 == null) throw new NullPointerException("Parameter 'ec1' must not be null.");
    if (ec2 == null) throw new NullPointerException("Parameter 'ec2' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    EnzymeSubclass result = null;
    List<EnzymeSubSubclass> subSubclasses;

    // Get sub-subclasses.
    EnzymeSubSubclassMapper subSubclassMapper = new EnzymeSubSubclassMapper();
    subSubclasses = subSubclassMapper.findList(ec1, ec2, con);

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setString(1, ec1);
      findStatement.setString(2, ec1);
      findStatement.setString(3, ec2);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs, subSubclasses);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return result;
  }

  /**
   * Tries to find all Subclasses requested.
   *
   * @param ec1 Number/Wildcard of class to search for.
   * @param con The logical connection.
   * @return a <code>Vector</code> of <code>EnzymeClass</code> instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeSubclass> findList(String ec1, Connection con) throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeSubclass> result = new ArrayList<EnzymeSubclass>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findListStatement());
      findListStatement.setString(1, ec1);
      findListStatement.setString(2, ec1);
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
  
  /**
   * 
   * @param con
   * @return A <code>List</code> of <code>EnzymeSubclass</code>es (without
   *    subsubclass information).
 * @throws SQLException 
 * @throws DomainException 
   */
  public List<EnzymeSubclass> findAll(Connection con) throws SQLException, DomainException{
      List<EnzymeSubclass> result = new ArrayList<EnzymeSubclass>();
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
   * Checks if the given subclass numbers are valid.
   *
   * @param ec1 The class number.
   * @param ec2 The subclass number.
   * @param con The connection.
   * @return <code>true</code> if the class exists.
   * @throws SQLException
   */
  public boolean subclassExists(String ec1, String ec2, Connection con) throws SQLException {
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    try {
      findStatement = con.prepareStatement(findSubclassOnlyStatement());
      findStatement.setString(1, ec1);
      findStatement.setString(2, ec2);
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
   * Creates the <code>EnzymeSubclass</code> object.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeSubclass</code> instance.
   * @throws SQLException
   */
  private EnzymeSubclass doLoad(ResultSet rs, List<EnzymeSubSubclass> subSubclasses)
  throws SQLException, DomainException {
    int ec1 = 0;
    int ec2 = 0;
    String className = "";
    String name = "";
    String description = "";

    if (rs.getInt(1) > 0) ec1 = rs.getInt(1);
    if (rs.getInt(2) > 0) ec2 = rs.getInt(2);
    if (rs.getString(3) != null) name = rs.getString(3);
    if (rs.getString(4) != null) className = rs.getString(4);
    if (rs.getString(5) != null) description = rs.getString(5);

    return new EnzymeSubclass(EnzymeCommissionNumber.valueOf(ec1, ec2), className, name, description, subSubclasses);
  }
}
