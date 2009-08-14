package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;

/**
 * Maps enzyme class information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeClassMapper {

  private static final String COLUMNS = "ec1, name, description";
  
  private static final String SELECT_ALL = "SELECT " + COLUMNS + " FROM classes";

  private static final Logger LOGGER = Logger.getLogger(EnzymeClassMapper.class);

  public EnzymeClassMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS + " FROM classes WHERE ec1 = ?";
  }

  private String findNumberOfClassesStatement() {
    return "SELECT count(*) AS count FROM classes";
  }

  /**
   * Tries to find Class information about an enzyme.
   *
   * @param ec1 Number of class to search for.
   * @param con The logical connection.
   * @return an <code>EnzymeClass</code> instance or <code>null</code> if nothing has been found.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   * @throws SQLException         if a database error occurs.
   * @throws DomainException      if any error related to domain information occurs (e.g. <code>ec1</code> is invalid).
   */
  public EnzymeClass find(String ec1, Connection con) throws SQLException, DomainException {
    if (ec1 == null) throw new NullPointerException("Parameter 'ec1' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    EnzymeClass result = null;
    List<EnzymeSubclass> subclasses;

    // Get subclasses.
    EnzymeSubclassMapper subclassMapper = new EnzymeSubclassMapper();
    subclasses = subclassMapper.findList(ec1, con);

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setString(1, ec1);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs, subclasses);
      } else {
        LOGGER.warn("No class information found for EC " + ec1);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return result;
  }
  
  /**
   * 
   * @param con
   * @return A <code>List</code> of all the <code>EnzymeClass</code>es
   *    (without subclass information)
 * @throws SQLException 
 * @throws EcException 
   */
  public List<EnzymeClass> findAll(Connection con) throws SQLException, DomainException{
      List<EnzymeClass> result = new ArrayList<EnzymeClass>();
      Statement stm = null;
      ResultSet rs = null;
      try {
          stm = con.createStatement();
          rs = stm.executeQuery("SELECT ec1 FROM classes");
          while (rs.next()){
              result.add(find(rs.getString(1), con));
          }
      } finally {
      	if (rs != null) rs.close();
          if (stm != null) stm.close();
      }
      return result;
  }

  /**
   * Finds the number of classes.
   *
   * @param con The logical connection.
   * @return the number of classes.
   * @throws SQLException
   */
  public int findNumberOfClasses(Connection con) throws SQLException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    int number = 0;

    try {
      findStatement = con.prepareStatement(findNumberOfClassesStatement());
      rs = findStatement.executeQuery();
      if (rs.next()) {
        number = Integer.parseInt(rs.getString("count"));
      }
    } finally {
      	if (rs != null) rs.close();
      	if (findStatement != null) findStatement.close();
    }

    return number;
  }

  /**
   * Checks if the given class number is valid.
   *
   * @param ec1 The class number.
   * @param con The connection.
   * @return <code>true</code> if the class exists.
   * @throws SQLException
   */
  public boolean classExists(String ec1, Connection con) throws SQLException {
    if (ec1 == null) throw new NullPointerException("Parameter 'ec1' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setString(1, ec1);
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
   * Creates the <code>EnzymeClass</code> object.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeClass</code> instance.
   * @throws SQLException
   */
  private EnzymeClass doLoad(ResultSet rs, List<EnzymeSubclass> subclasses)
  throws SQLException, EcException {
    assert rs != null;
    
    int ec1 = 0;
    String name = "";
    String description = "";

    if (rs.getInt("ec1") > 0) ec1 = rs.getInt("ec1");
    if (rs.getString("name") != null) name = rs.getString("name");
    if (rs.getString("description") != null) description = rs.getString("description");

    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ec1);
    return EnzymeClass.valueOf(ec, name, description, subclasses);
  }
}
