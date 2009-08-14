package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeReactionException;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeReferenceException;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.domain.stats.EnzymeStatistics;

/**
 * Maps enzyme entry information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.5 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeEntryMapper {
//  private static final Logger LOGGER = Logger.getLogger(EnzymeEntryMapper.class);

  /**
   * The names are used for the heading of each enzyme, that is to which class, sub- and sub-subclass it belongs to.
   */
  private static final String COLUMNS = "e.enzyme_id, e.ec1, e.ec2, e.ec3, e.ec4, e.history, e.note, e.status, " +
                                        "e.source, e.active, s1.name, s2.name, s3.name";

  private static final String LIST_COLUMNS = "enzyme_id, ec1, ec2, ec3, ec4, history, note, status, source, active";

  public EnzymeEntryMapper() {
  }

  /**
   * Returns the SQL statement used for loading an enzyme by the given EC.
   *
   * @return the SQL statement.
   */
  private String findByEcStatement() {
    return "SELECT " + COLUMNS +
           " FROM enzymes e, classes s1, subclasses s2, subsubclasses s3" +
           " WHERE e.ec1 = ? AND e.ec2 = ? AND e.ec3 = ? AND e.ec4 = ? AND e.status = ? AND s1.ec1 = ? AND" +
           " s2.ec1 = ? AND s2.ec2 = ? AND s3.ec1 = ? AND s3.ec2 = ? AND s3.ec3 = ?";
  }

  /**
   * Returns the SQL statement used for loading all enzymes by the given EC.
   *
   * @return the SQL statement.
   */
  private String findAllSubSubclassEntriesByEcStatement() {
    return "SELECT " + COLUMNS +
           " FROM enzymes e, classes s1, subclasses s2, subsubclasses s3" +
           " WHERE e.ec1 = ? AND e.ec2 = ? AND e.ec3 = ? AND s1.ec1 = ? AND" +
           " s2.ec1 = ? AND s2.ec2 = ? AND s3.ec1 = ? AND s3.ec2 = ? AND s3.ec3 = ? AND e.source = ?" +
           " ORDER BY e.ec1, e.ec2, e.ec3, e.ec4";
  }

  /**
   * Returns the SQL statement used for loading all enzymes by the given EC.
   *
   * @return the SQL statement.
   */
  private String findAllByEcStatement() {
    return "SELECT " + COLUMNS +
           " FROM enzymes e, classes s1, subclasses s2, subsubclasses s3" +
           " WHERE e.ec1 = ? AND e.ec2 = ? AND e.ec3 = ? AND e.ec4 = ? AND s1.ec1 = ? AND" +
           " s2.ec1 = ? AND s2.ec2 = ? AND s3.ec1 = ? AND s3.ec2 = ? AND s3.ec3 = ?";
  }

  /**
   * Returns the SQL statement used for loading an enzyme by the given ID.
   *
   * @return the SQL statement.
   */
  private String findByIdStatement() {
    return "SELECT " + COLUMNS +
           " FROM enzymes e, classes s1, subclasses s2, subsubclasses s3" +
           " WHERE e.enzyme_id = ? AND s1.ec1 = ? AND" +
           " s2.ec1 = ? AND s2.ec2 = ? AND s3.ec1 = ? AND s3.ec2 = ? AND s3.ec3 = ?";
  }

  /**
   * Returns the SQL statement used for loading a list of approved enzymes.
   * <p/>
   * Because of modified enzymes which still have the same EC number (and are both approved) only the most up-to-date
   * version should be loaded. This is done by the substatement.
   *
   * @return the SQL statement.
   */
  private String findListStatement() {
    return "SELECT " + LIST_COLUMNS +
           " FROM enzymes" +
           " WHERE ec1 = ? AND ec2 = ? AND ec3 = ?" +
           " AND status = ?" +
           " AND enzyme_id NOT IN" +
           " ( SELECT before_id FROM history_events WHERE event_class = ? )" +
           " ORDER BY ec1, ec2, ec3, ec4";
  }

  /**
   * Returns the SQL statement used for loading the list of all approved enzymes.
   * <p/>
   * Because of modified enzymes which still have the same EC number (and are both approved) only the most up-to-date
   * version should be loaded. This is done by the substatement.
   *
   * @return the SQL statement.
   */
  private String findAllStatement() {
    return "SELECT " + LIST_COLUMNS +
           " FROM enzymes" +
           " WHERE status = ?" +
           " AND enzyme_id NOT IN" +
           " ( SELECT before_id FROM history_events WHERE event_class = ? )" +
           " ORDER BY ec1, ec2, ec3, ec4";
  }

  /**
   * Returns the SQL statement used for loading the list of proposed enzymes.
   *
   * @return the SQL statement.
   */
  private String findCoreListStatement() {
    return "SELECT " + LIST_COLUMNS +
           " FROM enzymes" +
           " WHERE status = ?" +
           " ORDER BY ec1, ec2, ec3, ec4";
  }

  /**
   * Returns the SQL statement used for loading an EC number by a given enzyme ID.
   *
   * @return the SQL statement.
   */
  private String findEcStatement() {
    return "SELECT ec1, ec2, ec3, ec4" +
           " FROM enzymes" +
           " WHERE enzyme_id = ?";
  }

  private String findIDInMappingTable() {
    return "SELECT enzyme_id" +
           " FROM id2ec" +
           " WHERE ec = ? and status = ?";
  }

  /**
   * Returns the SQL statement used for loading the history line by a given enzyme ID.
   *
   * @return the SQL statement.
   */
  private String findHistoryLineStatement() {
    return "SELECT history" +
           " FROM enzymes" +
           " WHERE enzyme_id = ?";
  }

  /**
   * Returns the SQL statement used for loading the note by a given enzyme ID.
   *
   * @return the SQL statement.
   */
  private String findNoteStatement() {
    return "SELECT note" +
           " FROM enzymes" +
           " WHERE enzyme_id = ?";
  }

  /**
   * Returns the SQL statement to fetch the next available enzyme ID.
   *
   * @return the SQL statement.
   */
  private String findNextEnzymeIdStatement() {
    return "SELECT s_enzyme_id.nextval from DUAL";
  }

  private String countIUBMBEnzymesStatement() {
    return "SELECT status, active" +
           " FROM enzymes" +
           " WHERE source = ?" +
           " AND enzyme_id NOT IN" +
           " ( SELECT before_id FROM history_events WHERE event_class = ? )" +
           " ORDER BY ec1, ec2, ec3, ec4";
  }

  private String countClassesStatement() {
    return "SELECT count(*) FROM classes";
  }

  private String countSubclassesStatement() {
    return "SELECT count(*) FROM subclasses";
  }

  private String countSubSubclassesStatement() {
    return "SELECT count(*) FROM subsubclasses";
  }

  /**
   * Returns the INSERT statement for inserting an enzyme.
   *
   * @return the SQL statement.
   */
  private String insertStatement() {
    return "INSERT INTO enzymes (enzyme_id, ec1, ec2, ec3, ec4, history, note, status, source, active)" +
    		" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  }

  /**
   * Creates the SQL statement to be used to update the core data of an enzyme.
   *
   * @return the SQL statement.
   */
  private String updateStatement() {
    return "UPDATE enzymes SET ec1 = ?, ec2 = ?, ec3 = ?, ec4 = ?, status = ?, source = ?, note = ?," +
    		" history = ? WHERE enzyme_id = ?";
  }

  /**
   * Updates the EC.
   *
   * @return the SQL statement.
   */
  private String updateEcStatement() {
    return "UPDATE enzymes SET ec1 = ?, ec2 = ?, ec3 = ?, ec4 = ? WHERE enzyme_id = ?";
  }

  /**
   * Updates the history line.
   *
   * @return the SQL statement.
   */
  private String updateHistoryLineStatement() {
    return "UPDATE enzymes SET history = ? WHERE enzyme_id = ?";
  }

  /**
   * Updates the note.
   *
   * @return the SQL statement.
   */
  private String updateNoteStatement() {
    return "UPDATE enzymes SET note = ? WHERE enzyme_id = ?";
  }

  /**
   * Updates the status.
   *
   * @return the SQL statement.
   */
  private String updateStatusStatement() {
    return "UPDATE enzymes SET status = ? WHERE enzyme_id = ?";
  }

  /**
   * Tries to find entry information about an enzyme.
   *
   * @param ec1 Number of class to search for.
   * @param ec2 Number of subclass to search for.
   * @param ec3 Number of sub-subclass to search for.
   * @param ec4 Number of entry to search for.
   * @param con The logical connection.
   * @return an <code>EnzymeEntry</code> instance or <code>null</code> if nothing has been found.
   * @throws java.sql.SQLException 
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException
   */
  public EnzymeEntry findByEc(String ec1, String ec2, String ec3, String ec4, Connection con)
  throws SQLException, DomainException {
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    EnzymeEntry result = null;

    try {
      // Core information.
      findStatement = con.prepareStatement(findByEcStatement());
      findStatement.setString(1, ec1);
      findStatement.setString(2, ec2);
      findStatement.setString(3, ec3);
      findStatement.setString(4, ec4);
      findStatement.setString(5, "OK");
      findStatement.setString(6, ec1);
      findStatement.setString(7, ec1);
      findStatement.setString(8, ec2);
      findStatement.setString(9, ec1);
      findStatement.setString(10, ec2);
      findStatement.setString(11, ec3);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs);
        findEnzymeData(result, con);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    return result;
  }

  /**
   * Tries to find entry information about all enzymes specified by the given EC.
   *
   * @param ec1 Number of class to search for.
   * @param ec2 Number of subclass to search for.
   * @param ec3 Number of sub-subclass to search for.
   * @param ec4 Number of entry to search for.
   * @param con The logical connection.
   * @return an <code>EnzymeEntry</code> instance or <code>null</code> if nothing has been found.
   * @throws NullPointerException if parameter <code>con</code> is <code>null</code>.
   * @throws SQLException         if a database error occurs.
   * @throws DomainException      if any error related to domain information occurs.
   */
  public List<EnzymeEntry> findAllByEc(int ec1, int ec2, int ec3, int ec4, Connection con)
  throws SQLException, DomainException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findAllByEcStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();

    try {
      // Core information.
      findAllByEcStatement = con.prepareStatement(findAllByEcStatement());
      findAllByEcStatement.setInt(1, ec1);
      findAllByEcStatement.setInt(2, ec2);
      findAllByEcStatement.setInt(3, ec3);
      findAllByEcStatement.setInt(4, ec4);
      findAllByEcStatement.setInt(5, ec1);
      findAllByEcStatement.setInt(6, ec1);
      findAllByEcStatement.setInt(7, ec2);
      findAllByEcStatement.setInt(8, ec1);
      findAllByEcStatement.setInt(9, ec2);
      findAllByEcStatement.setInt(10, ec3);
      rs = findAllByEcStatement.executeQuery();
      while (rs.next()) {
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        // History information
        HistoryGraph historyGraph = null;
        if (enzymeEntry.getStatus().equals(EnzymeStatusConstant.APPROVED)) {
          EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
          historyGraph = historyEventsMapper.find(enzymeEntry, con);
        } else {
          EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
          historyGraph = futureEventsMapper.find(enzymeEntry, con);
        }
        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);

        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findAllByEcStatement != null) findAllByEcStatement.close();
    }

    if (result.size() == 0) return null;
    return result;
  }

  public List<EnzymeEntry> findAllSubSubclassEntriesByEc(int ec1, int ec2, int ec3, Connection con)
  throws SQLException,
          DomainException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findAllByEcStatement = null;
    ResultSet rs = null;
    Map map = new TreeMap();

    try {
      // Core information.
      findAllByEcStatement = con.prepareStatement(findAllSubSubclassEntriesByEcStatement());
      findAllByEcStatement.setInt(1, ec1);
      findAllByEcStatement.setInt(2, ec2);
      findAllByEcStatement.setInt(3, ec3);
      findAllByEcStatement.setInt(4, ec1);
      findAllByEcStatement.setInt(5, ec1);
      findAllByEcStatement.setInt(6, ec2);
      findAllByEcStatement.setInt(7, ec1);
      findAllByEcStatement.setInt(8, ec2);
      findAllByEcStatement.setInt(9, ec3);
      findAllByEcStatement.setString(10, EnzymeSourceConstant.INTENZ.toString());
      rs = findAllByEcStatement.executeQuery();
      while (rs.next()) {
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        if (map.containsKey(enzymeEntry.getEc())){
        	EnzymeEntry existing = (EnzymeEntry) map.get(enzymeEntry.getEc());
        	// From several entries with the same EC number,
        	// we choose the most recent one (higher id):
        	if (existing.getId().longValue() > enzymeEntry.getId().longValue())
        		continue;
        }
        // History information
        HistoryGraph historyGraph = null;
        if (enzymeEntry.getStatus().equals(EnzymeStatusConstant.APPROVED)) {
          EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
          historyGraph = historyEventsMapper.find(enzymeEntry, con);
        } else {
          EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
          historyGraph = futureEventsMapper.find(enzymeEntry, con);
        }
        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);

        map.put(enzymeEntry.getEc(), enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findAllByEcStatement != null) findAllByEcStatement.close();
    }

    return map.isEmpty()? null : new ArrayList<EnzymeEntry>(map.values());
  }

  /**
   * Tries to find entry information about an enzyme.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return an <code>EnzymeEntry</code> instance or <code>null</code> if nothing has been found.
   * @throws java.sql.SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public EnzymeEntry findById(Long id, Connection con) throws SQLException, DomainException {
    PreparedStatement findStatement = null;
    EnzymeEntry result = null;
    ResultSet rsEc = null, rs = null;
    String ec1 = "", ec2 = "", ec3 = "";

    try {
      // EC number (necessary for the heading)
      findStatement = con.prepareStatement(findEcStatement());
      findStatement.setLong(1, id.longValue());
      rsEc = findStatement.executeQuery();
      if (rsEc.next()) {
        if (rsEc.getString("ec1") != null)
          ec1 = rsEc.getString("ec1");
        else
          return result;
        if (rsEc.getString("ec2") != null)
          ec2 = rsEc.getString("ec2");
        else
          return result;
        if (rsEc.getString("ec3") != null)
          ec3 = rsEc.getString("ec3");
        else
          return result;
      } else
        return result;

      if (findStatement != null) findStatement.close();

      // Core information.
      findStatement = con.prepareStatement(findByIdStatement());
      findStatement.setLong(1, id.longValue());
      findStatement.setString(2, ec1);
      findStatement.setString(3, ec1);
      findStatement.setString(4, ec2);
      findStatement.setString(5, ec1);
      findStatement.setString(6, ec2);
      findStatement.setString(7, ec3);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoad(rs);
        findEnzymeData(result, con);
      }
    } finally {
    	if (rsEc != null) rsEc.close();
    	if (rs != null) rs.close();
    	if (findStatement != null) findStatement.close();
    }

    return result;
  }

  /**
   * Loads a ghost version of the enzyme with the given ID.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return an <code>EnzymeEntry</code> instance or <code>null</code> if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public EnzymeEntry findGhostById(int id, Connection con) throws SQLException, DomainException {
    PreparedStatement findStatement = null;
    ResultSet rsEc = null, rs = null;
    EnzymeEntry result = null;
    String ec1 = "", ec2 = "", ec3 = "";

    try {
      // EC number (necessary for the heading)
      findStatement = con.prepareStatement(findEcStatement());
      findStatement.setInt(1, id);
      rsEc = findStatement.executeQuery();
      if (rsEc.next()) {
        if (rsEc.getString("ec1") != null)
          ec1 = rsEc.getString("ec1");
        else
          return result;
        if (rsEc.getString("ec2") != null)
          ec2 = rsEc.getString("ec2");
        else
          return result;
        if (rsEc.getString("ec3") != null)
          ec3 = rsEc.getString("ec3");
        else
          return result;
      } else
        return result;

      if (findStatement != null) findStatement.close();

      // Core information.
      findStatement = con.prepareStatement(findByIdStatement());
      findStatement.setInt(1, id);
      findStatement.setString(2, ec1);
      findStatement.setString(3, ec1);
      findStatement.setString(4, ec2);
      findStatement.setString(5, ec1);
      findStatement.setString(6, ec2);
      findStatement.setString(7, ec3);
      rs = findStatement.executeQuery();
      if (rs.next()) {
        result = doLoadGhost(rs);
      }
    } finally {
    	if (rsEc != null) rsEc.close();
    	if (rs != null) rs.close();
        if (findStatement != null) findStatement.close();
    }

    return result;
  }

  public Long findIDInMappingTable(String ec, EnzymeStatusConstant status, Connection con)
  throws SQLException {
    if (con == null || ec == null) throw new NullPointerException();
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    try {
      // Get id
      findStatement = con.prepareStatement(findIDInMappingTable());
      findStatement.setString(1, ec);
      findStatement.setString(2, status.getCode());
      rs = findStatement.executeQuery();
      if (rs.next()) {
        return new Long(rs.getLong(1));
      }
      return new Long(-1);
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }
  }

  /**
   * Tries to find all approved entries but loads the entries only with minimum information.
   *
   * @param con The logical connection.
   * @return a <code>List</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> findAll(Connection con) throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findAllStatement());
      findListStatement.setString(1, "OK");
      findListStatement.setString(2, "MOD");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Tries to find all entries requested but loads the entries only with minimum information,
   * if not already existing.
   *
   * @param ec1 Number of class to search for.
   * @param ec2 Number of subclass to search for.
   * @param ec3 Number of sub-subclass to search for.
   * @param con The logical connection.
   * @return a <code>List</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * if nothing has been found.
   * @throws java.sql.SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> findList(String ec1, String ec2, String ec3, Connection con)
  throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findListStatement());
      findListStatement.setString(1, ec1);
      findListStatement.setString(2, ec2);
      findListStatement.setString(3, ec3);
      findListStatement.setString(4, "OK");
      findListStatement.setString(5, "MOD");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        // History information
        HistoryGraph historyGraph = null;
        if (enzymeEntry.getStatus().equals(EnzymeStatusConstant.APPROVED)) {
          EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
          historyGraph = historyEventsMapper.find(enzymeEntry, con);
        } else {
          EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
          historyGraph = futureEventsMapper.find(enzymeEntry, con);
        }
        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
      if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Tries to find all proposed entries but loads the entries only with minimum information.
   *
   * @param con The logical connection.
   * @return an {@link java.util.ArrayList} of {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry}
   * 	instances.
   * @throws SQLException    if a database error occurs.
   * @throws DomainException if a domain related error occurs.
   */
  public List<EnzymeEntry> findProposedList(Connection con) throws SQLException, DomainException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();

    try {
      findListStatement = con.prepareStatement(findCoreListStatement());
      findListStatement.setString(1, "PR");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        // History information
        HistoryGraph historyGraph = null;
        if (enzymeEntry.getStatus().equals(EnzymeStatusConstant.APPROVED)) {
          EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
          historyGraph = historyEventsMapper.find(enzymeEntry, con);
        } else {
          EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
          historyGraph = futureEventsMapper.find(enzymeEntry, con);
        }
        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findListStatement != null) findListStatement.close();
    }

    return result;
  }

  /**
   * Tries to find all suggested entries but loads the entries only with minimum information.
   *
   * @param con The logical connection.
   * @return a <code>Vector</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * 	if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> findSuggestedList(Connection con) throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();

    try {
      findListStatement = con.prepareStatement(findCoreListStatement());
      findListStatement.setString(1, "SU");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        EnzymeEntry enzymeEntry = doLoadGhost(rs);
        enzymeEntry.setGhost(true);

        // History information
//        EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
//        HistoryGraph historyGraph = futureEventsMapper.find(enzymeEntry, con);
//        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);
        HistoryGraph historyGraph = null;
        if (enzymeEntry.getStatus().equals(EnzymeStatusConstant.APPROVED)) {
          EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
          historyGraph = historyEventsMapper.find(enzymeEntry, con);
        } else {
          EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
          historyGraph = futureEventsMapper.find(enzymeEntry, con);
        }
        if (historyGraph != null) enzymeEntry.setHistory(historyGraph);

        // Common name
        EnzymeNameMapper nameMapper = new EnzymeNameMapper();
        List<EnzymeName> commonNames = nameMapper.findCommonNames(enzymeEntry.getId(), con);
        if (commonNames != null) enzymeEntry.setCommonNames(commonNames);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findListStatement != null) findListStatement.close();
    }

    return result;
  }
  
  /**
   * Exports every piece of data for publicly available entries.
   * @param con
   * @return A <code>List</code> of <code>EnzymeEntry</code>s
   * @throws SQLException 
   * @throws DomainException 
   */
  public List exportAllEntries(Connection con) throws SQLException, DomainException{
	  List entries = new ArrayList();
	  PreparedStatement findListStatement = null;
	    ResultSet rs = null;
	  try {
		  findListStatement = con.prepareStatement(findAllStatement());
		  findListStatement.setString(1, "OK");
		  findListStatement.setString(2, "MOD");
		  rs = findListStatement.executeQuery();
		  while (rs.next()){
			  EnzymeEntry entry = doLoadCore(rs);
			  findEnzymeData(entry, con);
			  entries.add(entry);
		  }
	  } finally {
	    	if (rs != null) rs.close();
	        if (findListStatement != null) findListStatement.close();
	  }
	  return entries.isEmpty()? null : entries;
  }

  /**
   * Tries to export all approved entries containing only ENZYME relevant information.
   * <p/>
   * Affected table rows will be locked.
   *
   * @param con The logical connection.
   * @return an <code>ArrayList</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * 	if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> exportApprovedSibEntries(Connection con) throws SQLException, DomainException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findAllStatement());
      findListStatement.setString(1, "OK");
      findListStatement.setString(2, "MOD");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        EnzymeEntry enzymeEntry = doLoadCore(rs);
        exportSIBEnzymeData(enzymeEntry, con);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Tries to find all proposed entries with all information.
   * <p/>
   * This feature is just needed to export a list of all proposed entries.
   *
   * @param con The logical connection.
   * @return a <code>Vector</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * 	if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> findFullProposedList(Connection con)
  throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findCoreListStatement());
      findListStatement.setString(1, "PR");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        EnzymeEntry enzymeEntry = doLoadCore(rs);
        findEnzymeData(enzymeEntry, con);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Tries to find all suggested entries with all information.
   * <p/>
   * This feature is just needed to export a list of all suggested entries.
   *
   * @param con The logical connection.
   * @return a <code>Vector</code> of <code>EnzymeEntry</code> instances or <code>null</code>
   * 	if nothing has been found.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public List<EnzymeEntry> findFullSuggestedList(Connection con)
  throws SQLException, DomainException {
    PreparedStatement findListStatement = null;
    ResultSet rs = null;
    List<EnzymeEntry> result = new ArrayList<EnzymeEntry>();
    boolean noResult = true;

    try {
      findListStatement = con.prepareStatement(findCoreListStatement());
      findListStatement.setString(1, "SU");
      rs = findListStatement.executeQuery();

      while (rs.next()) {
        noResult = false;
        EnzymeEntry enzymeEntry = doLoadCore(rs);
        findEnzymeData(enzymeEntry, con);
        result.add(enzymeEntry);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findListStatement != null) findListStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Returns the EC number of the given enzyme ID.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return The EC, if found, otherwise <code>null</code>.
   * @throws SQLException
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException 
   */
  public EnzymeCommissionNumber findEC(String id, Connection con)
  throws SQLException, DomainException {
    int ec1 = 0, ec2 = 0, ec3 = 0, ec4 = 0;

    PreparedStatement findEcStatement = null;
    ResultSet rs = null;

    try {
      findEcStatement = con.prepareStatement(findEcStatement());
      findEcStatement.setString(1, id);
      rs = findEcStatement.executeQuery();

      while (rs.next()) {
        ec1 = rs.getInt("ec1");
        ec2 = rs.getInt("ec2");
        ec3 = rs.getInt("ec3");
        ec4 = rs.getInt("ec4");
      }
    } finally {
    	if (rs != null) rs.close();
      if (findEcStatement != null) findEcStatement.close();
    }

    return EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4);
  }

  /**
   * Checks whether a given EC number already exists in the database.
   *
   * @param ec  The EC number to be checked.
   * @param con The database connection.
   * @return <code>true</code> if the given EC number exists in the database.
   * @throws SQLException         if a database error occurs.
   * @throws NullPointerException if any of the parameters is <code>null</code>
   */
  public static boolean ecExists(EnzymeCommissionNumber ec, Connection con)
  throws SQLException {
    if (ec == null) throw new NullPointerException("Parameter 'ec' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    try {
      switch (ec.getType()) {
        case EnzymeCommissionNumber.TYPE_CLASS:
          findStatement = con.prepareStatement("SELECT ec1 FROM classes WHERE ec1 = ?");
          findStatement.setInt(1, ec.getEc1());
          break;
        case EnzymeCommissionNumber.TYPE_SUBCLASS:
          findStatement = con.prepareStatement("SELECT ec1 FROM subclasses" +
          		" WHERE ec1 = ? AND ec2 = ?");
          findStatement.setInt(1, ec.getEc1());
          findStatement.setInt(2, ec.getEc2());
          break;
        case EnzymeCommissionNumber.TYPE_SUBSUBCLASS:
          findStatement = con.prepareStatement("SELECT ec1 FROM subsubclasses" +
          		" WHERE ec1 = ? AND ec2 = ? AND ec3 = ?");
          findStatement.setInt(1, ec.getEc1());
          findStatement.setInt(2, ec.getEc2());
          findStatement.setInt(3, ec.getEc3());
          break;
        case EnzymeCommissionNumber.TYPE_ENZYME:
          findStatement = con.prepareStatement("SELECT ec1 FROM enzymes " +
          		"WHERE ec1 = ? AND ec2 = ? AND ec3 = ? AND ec4 = ?");
          findStatement.setInt(1, ec.getEc1());
          findStatement.setInt(2, ec.getEc2());
          findStatement.setInt(3, ec.getEc3());
          findStatement.setInt(4, ec.getEc4());
          break;
      }
      rs = findStatement.executeQuery();
      return rs.next();
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }
  }

  /**
   * Returns the history line of the enzyme with the given ID.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return The history line, if found, otherwise <code>null</code>.
   * @throws SQLException
   */
  public String findHistoryLine(Long id, Connection con) throws SQLException {
    String historyLine = "";
    PreparedStatement findHistoryLineStatement = null;
    ResultSet rs = null;
    try {
      findHistoryLineStatement = con.prepareStatement(findHistoryLineStatement());
      findHistoryLineStatement.setLong(1, id.longValue());
      rs = findHistoryLineStatement.executeQuery();

      while (rs.next()) {
        if (rs.getString("history") != null) historyLine = rs.getString("history");
      }
    } finally {
    	if (rs != null) rs.close();
      if (findHistoryLineStatement != null) findHistoryLineStatement.close();
    }

    return historyLine;
  }

  /**
   * Returns the note of the enzyme with the given ID.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return The note, if found, otherwise <code>null</code>.
   * @throws SQLException
   */
  public String findNote(Long id, Connection con) throws SQLException {
    String note = "";
    PreparedStatement findNoteStatement = null;
    ResultSet rs = null;

    try {
      findNoteStatement = con.prepareStatement(findNoteStatement());
      findNoteStatement.setLong(1, id.longValue());
      rs = findNoteStatement.executeQuery();

      while (rs.next()) {
        if (rs.getString("note") != null) note = rs.getString("note");
      }
    } finally {
    	if (rs != null) rs.close();
        if (findNoteStatement != null) findNoteStatement.close();
    }

    return note;
  }

  /**
   * @param con The logical connection.
   * @return
   * @throws SQLException
   */
  public EnzymeStatistics findStats(Connection con) throws SQLException {
    PreparedStatement countIUBMBEnzymes = null, countClasses = null,
    	countSubclasses = null, countSubSubclasses = null;
    EnzymeStatistics result = new EnzymeStatistics();
    ResultSet rs = null;

    int allEnzymes = 0;
    int approved = 0;
    int proposed = 0;
    int suggested = 0;
    int approvedDeleted = 0;
    int proposedDeleted = 0;
    int suggestedDeleted = 0;

    try {
      countIUBMBEnzymes = con.prepareStatement(countIUBMBEnzymesStatement());
      countIUBMBEnzymes.setString(1, "IUBMB");
      countIUBMBEnzymes.setString(2, "MOD");
      rs = countIUBMBEnzymes.executeQuery();
      while (rs.next()) {
        allEnzymes++;
        if (rs.getString(1).equals("OK")) {
          approved++;
          if (rs.getString(2).equals("N")) approvedDeleted++;
        }
        if (rs.getString(1).equals("PR")) {
          proposed++;
          if (rs.getString(2).equals("N")) proposedDeleted++;
        }
        if (rs.getString(1).equals("SU")) {
          suggested++;
          if (rs.getString(2).equals("N")) suggestedDeleted++;
        }
      }

      result.setAllEnzymes(allEnzymes);
      result.setApprovedEnzymes(approved);
      result.setApprovedDeletedEnzymes(approvedDeleted);
      result.setProposedEnzymes(proposed);
      result.setProposedDeletedEnzymes(proposedDeleted);
      result.setSuggestedEnzymes(suggested);
      result.setSuggestedDeletedEnzymes(suggestedDeleted);

      countClasses = con.prepareStatement(countClassesStatement());
      rs = countClasses.executeQuery();
      if (rs.next()) {
        result.setClasses(rs.getInt(1));
      }

      countSubclasses = con.prepareStatement(countSubclassesStatement());
      rs = countSubclasses.executeQuery();
      if (rs.next()) {
        result.setSubclasses(rs.getInt(1));
      }

      countSubSubclasses = con.prepareStatement(countSubSubclassesStatement());
      rs = countSubSubclasses.executeQuery();
      if (rs.next()) {
        result.setSubsubclasses(rs.getInt(1));
      }

    } finally {
    	if (rs != null) rs.close();
      countIUBMBEnzymes.close();
      countClasses.close();
      countSubclasses.close();
      countSubSubclasses.close();
    }

    return result;
  }


  /**
   * Inserts the core data of a new enzyme entry.
   * <p/>
   * A new enzyme ID must already be available. It can be obtained via
   * {@link EnzymeEntryMapper#findNextEnzymeId(java.sql.Connection)}
   *
   * @param enzymeId 
   * @param ec 
   * @param status 
   * @param source
   * @param isActive
   * @param note 
   * @param historyLine 
   * @param con
   * @throws java.sql.SQLException 
   */
  public void insert(Long enzymeId, EnzymeCommissionNumber ec, EnzymeStatusConstant status,
                     EnzymeSourceConstant source, String note, String historyLine,
                     boolean isActive, Connection con)
          throws SQLException {
    PreparedStatement insertStatement = null;
    try {
      insertStatement = con.prepareStatement(insertStatement());
      insertStatement.setLong(1, enzymeId.longValue());
      insertStatement.setInt(2, ec.getEc1());
      insertStatement.setInt(3, ec.getEc2());
      insertStatement.setInt(4, ec.getEc3());
      insertStatement.setInt(5, ec.getEc4());
      insertStatement.setString(6, historyLine);
      insertStatement.setString(7, note);
      insertStatement.setString(8, status.getCode());
      insertStatement.setString(9, source.toString());
      insertStatement.setString(10, isActive ? "Y" : "N");
      insertStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      insertStatement.close();
    }
  }


  // ----------------- UPDATE PROCEDURES ----------------------

  /**
   * Updates the core data of an enzyme stored in the table <b><code>ENZYMES</code></b>.
   * <p/>
   * <b>NOTE:</b> The <b><code>ACTIVE</code></b> column of this table is cannot be updated
   * using this method since this column
   * is managed by the event package.
   *
   * @param enzymeId    The enzyme's ID.
   * @param ec          The EC number.
   * @param status      The current status of the enzyme.
   * @param source      The source of this enzyme.
   * @param note        A note the curator might have added.
   * @param historyLine The enzyme's history line.
   * @param con         A database connection.
   * @throws java.sql.SQLException 
   */
  public void update(Long enzymeId, EnzymeCommissionNumber ec, EnzymeStatusConstant status,
                     EnzymeSourceConstant source, String note, String historyLine, Connection con)
  throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (ec == null) throw new NullPointerException("Parameter 'ec' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (note == null) throw new NullPointerException("Parameter 'note' must not be null.");
    if (historyLine == null) throw new NullPointerException("Parameter 'historyLine' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement updateStatement = null;
    try {
      updateStatement = con.prepareStatement(updateStatement());
      updateStatement.setInt(1, ec.getEc1());
      updateStatement.setInt(2, ec.getEc2());
      updateStatement.setInt(3, ec.getEc3());
      updateStatement.setInt(4, ec.getEc4());
      updateStatement.setString(5, status.getCode());
      updateStatement.setString(6, source.toString());
      updateStatement.setString(7, note);
      updateStatement.setString(8, historyLine);
      updateStatement.setLong(9, enzymeId.longValue());
      updateStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      updateStatement.close();
    }
  }

  /**
   * Updates the EC of the given enzyme.
   *
   * @param enzymeId The enzyme's ID.
   * @param newEc    The new EC.
   * @param con      The logical connection.
   * @throws SQLException
   */
  public void updateEc(Long enzymeId, EnzymeCommissionNumber newEc, Connection con) throws SQLException {
    PreparedStatement updateEcStatement = null;

    try {
      updateEcStatement = con.prepareStatement(updateEcStatement());
      updateEcStatement.setInt(1, newEc.getEc1());
      updateEcStatement.setInt(2, newEc.getEc2());
      updateEcStatement.setInt(3, newEc.getEc3());
      updateEcStatement.setInt(4, newEc.getEc4());
      updateEcStatement.setLong(5, enzymeId.longValue());
      updateEcStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      updateEcStatement.close();
    }
  }

  /**
   * Updates the notes of the given enzyme.
   *
   * @param enzymeId The enzyme's ID.
   * @param note     The note.
   * @param con      The logical connection.
   * @throws SQLException
   */
  public void updateNote(Long enzymeId, String note, Connection con) throws SQLException {
    PreparedStatement updateNoteStatement = null;

    try {
      updateNoteStatement = con.prepareStatement(updateNoteStatement());
      updateNoteStatement.setString(1, note);
      updateNoteStatement.setLong(2, enzymeId.longValue());
      updateNoteStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      updateNoteStatement.close();
    }
  }

  /**
   * Updates the status of the given enzyme.
   *
   * @param enzymeId The enzyme's ID.
   * @param status   The status.
   * @param con      The logical connection.
   * @throws SQLException
   */
  public void updateStatus(Long enzymeId, EnzymeStatusConstant status, Connection con) throws SQLException {
    PreparedStatement updateStatusStatement = null;

    try {
      updateStatusStatement = con.prepareStatement(updateStatusStatement());
      updateStatusStatement.setString(1, status.getCode());
      updateStatusStatement.setLong(2, enzymeId.longValue());
      updateStatusStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      updateStatusStatement.close();
    }
  }

  /**
   * Updates the history line of the given enzyme.
   *
   * @param enzymeId    The enzyme's ID.
   * @param historyLine The new history line.
   * @param con         The logical connection.
   * @throws SQLException
   */
  public void updateHistoryLine(Long enzymeId, String historyLine, Connection con) throws SQLException {
    PreparedStatement updateHistoryLineStatement = null;

    try {
      updateHistoryLineStatement = con.prepareStatement(updateHistoryLineStatement());
      updateHistoryLineStatement.setString(1, historyLine);
      updateHistoryLineStatement.setLong(2, enzymeId.longValue());
      updateHistoryLineStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      updateHistoryLineStatement.close();
    }
  }

  /**
   * Checks if a clone of an enzyme already exists.
   *
   * @param id  The enzyme ID.
   * @param con The logical connection.
   * @return <code>true</code>, if a clone already exists.
   * @throws SQLException
   */
  public boolean cloneExists(Long id, Connection con) throws SQLException {
    EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
    return futureEventsMapper.futureEventExists(id, con);
  }

  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>EnzymeEntry</code> object from the given result set.
   * <p/>
   * This object includes information about its corresponding class, subclass and sub-subclass.
   *
   * @param rs The result set object.
   * @return a <code>EnzymeEntry</code> instance containing the core data of an enzyme.
   * @throws java.sql.SQLException if a database error occurs.
   */
  private EnzymeEntry doLoad(ResultSet rs) throws SQLException, EcException {
    Long enzymeId = null;
    int ec1 = 0;
    int ec2 = 0;
    int ec3 = 0;
    int ec4 = 0;
    String history = "";
    String note = "";
    String status = "";
    String source = "";
    String active = "";
    String className = "";
    String subclassName = "";
    String subSubclassName = "";

    if (rs.getInt(1) > 0) enzymeId = new Long(rs.getLong(1));
    if (rs.getInt(2) > 0) ec1 = rs.getInt(2);
    if (rs.getInt(3) > 0) ec2 = rs.getInt(3);
    if (rs.getInt(4) > 0) ec3 = rs.getInt(4);
    if (rs.getInt(5) > 0) ec4 = rs.getInt(5);
    if (rs.getString(6) != null) history = rs.getString(6);
    if (rs.getString(7) != null) note = rs.getString(7);
    if (rs.getString(8) != null) status = rs.getString(8);
    if (rs.getString(9) != null) source = rs.getString(9);
    if (rs.getString(10) != null) active = rs.getString(10);
    if (rs.getString(11) != null) className = rs.getString(11);
    if (rs.getString(12) != null) subclassName = rs.getString(12);
    if (rs.getString(13) != null) subSubclassName = rs.getString(13);

    EnzymeEntry enzymeEntry = new EnzymeEntry();
    enzymeEntry.setId(enzymeId);
    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4);
    enzymeEntry.setEc(ec);
    enzymeEntry.getHistory().getRootNode().setHistoryLine(history);
    enzymeEntry.setNote(note);
    enzymeEntry.setStatus(EnzymeStatusConstant.valueOf(status));
    enzymeEntry.setSource(EnzymeSourceConstant.valueOf(source));
    enzymeEntry.setActive(active.charAt(0) == 'Y');
    enzymeEntry.setSubSubclassName(subSubclassName);
    enzymeEntry.setSubclassName(subclassName);
    enzymeEntry.setClassName(className);

    return enzymeEntry;
  }


  /**
   * Creates the <code>EnzymeEntry</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeEntry</code> instance.
   * @throws java.sql.SQLException
   */
  private EnzymeEntry doLoadCore(ResultSet rs) throws SQLException, DomainException {
    Long enzymeId = null;
    int ec1 = 0;
    int ec2 = 0;
    int ec3 = 0;
    int ec4 = 0;
    String history = "";
    String note = "";
    String status = "";
    String source = "";
    String active = "";

    if (rs.getInt(1) > 0) enzymeId = new Long(rs.getLong(1));
    if (rs.getInt(2) > 0) ec1 = rs.getInt(2);
    if (rs.getInt(3) > 0) ec2 = rs.getInt(3);
    if (rs.getInt(4) > 0) ec3 = rs.getInt(4);
    if (rs.getInt(5) > 0) ec4 = rs.getInt(5);
    if (rs.getString(6) != null) history = rs.getString(6);
    if (rs.getString(7) != null) note = rs.getString(7);
    if (rs.getString(8) != null) status = rs.getString(8);
    if (rs.getString(9) != null) source = rs.getString(9);
    if (rs.getString(10) != null) active = rs.getString(10);

    EnzymeEntry enzymeEntry = new EnzymeEntry();
    enzymeEntry.setId(enzymeId);
    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4);
    enzymeEntry.setEc(ec);
    enzymeEntry.getHistory().getRootNode().setHistoryLine(history);
    enzymeEntry.setNote(note);
    enzymeEntry.setStatus(EnzymeStatusConstant.valueOf(status));
    enzymeEntry.setSource(EnzymeSourceConstant.valueOf(source));
    enzymeEntry.setActive(active.charAt(0) == 'Y');

    return enzymeEntry;
  }

  /**
   * Checks the list of loaded names and stores them in an entry object according to its type
   * (common, systematic or other name).
   *
   * @param entry The entry where the names will be stored.
   * @param names The list of names.
   */
  private void doLoadNames(EnzymeEntry entry, List<EnzymeName> names) throws DomainException {
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeName enzymeName = names.get(iii);
      if (enzymeName.getType().equals(EnzymeNameTypeConstant.COMMON_NAME)) {
        entry.addCommonName(enzymeName);
      }
      if (enzymeName.getType().equals(EnzymeNameTypeConstant.SYSTEMATIC_NAME)) {
        entry.setSystematicName(enzymeName);
      }
      if (enzymeName.getType().equals(EnzymeNameTypeConstant.OTHER_NAME)) {
        entry.addSynonym(enzymeName);
      }
    }
  }

  /**
   * Creates the <code>EnzymeEntry</code> ghost (light-weight) object from the given result set.
   *
   * @param rs The result set object.
   * @return an <code>EnzymeEntry</code> instance.
   * @throws java.sql.SQLException
   */
  private EnzymeEntry doLoadGhost(ResultSet rs) throws SQLException, DomainException {
    Long enzymeId = null;
    int ec1 = 0;
    int ec2 = 0;
    int ec3 = 0;
    int ec4 = 0;
    String status = "";
    String source = "";
    String active = "";
    String history = "";

    if (rs.getInt("enzyme_id") > 0) enzymeId = new Long(rs.getLong("enzyme_id"));
    if (rs.getInt("ec1") > 0) ec1 = rs.getInt("ec1");
    if (rs.getInt("ec2") > 0) ec2 = rs.getInt("ec2");
    if (rs.getInt("ec3") > 0) ec3 = rs.getInt("ec3");
    if (rs.getInt("ec4") > 0) ec4 = rs.getInt("ec4");
    if (rs.getString("history") != null) history = rs.getString("history");
    if (rs.getString("status") != null) status = rs.getString("status");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("active") != null) active = rs.getString("active");

    EnzymeEntry enzymeEntry = new EnzymeEntry();
    enzymeEntry.setId(enzymeId);
    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4);
    enzymeEntry.setEc(ec);
    enzymeEntry.getHistory().getRootNode().setHistoryLine(history);
    if (!status.equals("")) enzymeEntry.setStatus(EnzymeStatusConstant.valueOf(status));
    if (!source.equals("")) enzymeEntry.setSource(EnzymeSourceConstant.valueOf(source));
    if (!active.equals("")) enzymeEntry.setActive(active.charAt(0) == 'Y');

    return enzymeEntry;
  }

  /**
   * Loads the enzyme data except for the core information (see <code>findBy</code> methods).
   *
   * @param enzymeEntry The <code>EnzymeEntry</code> instance to be populated.
   * @param con         The logical connection.
   * @throws SQLException if a database error occurs.
   */
  private void findEnzymeData(EnzymeEntry enzymeEntry, Connection con)
  throws SQLException, DomainException,
          EnzymeReactionException, EnzymeReferenceException {
    loadHistoryGraph(enzymeEntry, con);

    // Names
    EnzymeNameMapper nameMapper = new EnzymeNameMapper();
    List<EnzymeName> names = nameMapper.find(enzymeEntry.getId(), con);
    if (names != null) doLoadNames(enzymeEntry, names);

    // Reaction
    EnzymeReactionMapper reactionMapper = new EnzymeReactionMapper();
    EnzymaticReactions reactions = reactionMapper.find(enzymeEntry.getId(), con);
    if (reactions != null) enzymeEntry.setEnzymaticReactions(reactions);

    // Cofactors
    EnzymeCofactorMapper cofactorMapper = new EnzymeCofactorMapper();
    Set<Object> cofactors = cofactorMapper.find(enzymeEntry.getId(), con);
    if (cofactors != null) enzymeEntry.setCofactors(cofactors);

    // Links
    EnzymeLinkMapper linkMapper = new EnzymeLinkMapper();
    List<EnzymeLink> links = linkMapper.find(enzymeEntry.getId(), con);
    if (links != null) {
		enzymeEntry.setLinks(new TreeSet<EnzymeLink>(links));
	}

    // Comments
    EnzymeCommentMapper commentMapper = new EnzymeCommentMapper();
    List<EnzymeComment> comments = commentMapper.find(enzymeEntry.getId(), con);
    if (comments != null) enzymeEntry.setComments(comments);

    // References
    EnzymeReferenceMapper referenceMapper = new EnzymeReferenceMapper();
    List<Reference> references = referenceMapper.find(enzymeEntry.getId(), con);
    if (references != null) enzymeEntry.setReferences(references);
  }

  /**
   * Exports all data available for this ENZYME entry.
   * <p/>
   * Affected table rows will be locked.
   *
   * @param enzymeEntry The instance storing the data to be gathered.
   * @param con         The logical connection.
   * @throws SQLException            if a database error occurs.
   * @throws DomainException         if a domain related error occurs.
   * @throws EnzymeReactionException if no reaction has been found; one reaction is mandatory.
   */
  private void exportSIBEnzymeData(EnzymeEntry enzymeEntry, Connection con)
  throws SQLException, DomainException,
          EnzymeReactionException {
    assert enzymeEntry != null : "Parameter 'enzymeEntry' must not be null.";
    assert con != null : "Parameter 'con' must not be null.";

    loadHistoryGraph(enzymeEntry, con);
    
    // Names
    EnzymeNameMapper nameMapper = new EnzymeNameMapper();
    List<EnzymeName> names = nameMapper.exportSibNames(enzymeEntry.getId(), con);
    if (names != null) doLoadNames(enzymeEntry, names);

    // Reaction
    EnzymeReactionMapper reactionMapper = new EnzymeReactionMapper();
    EnzymaticReactions reactions = reactionMapper.exportSibReactions(enzymeEntry.getId(), con);
    if (reactions != null) enzymeEntry.setEnzymaticReactions(reactions);

    // Cofactors
    EnzymeCofactorMapper cofactorMapper = new EnzymeCofactorMapper();
    Set<Object> cofactors = cofactorMapper.exportSibCofactors(enzymeEntry.getId(), con);
    if (cofactors != null) enzymeEntry.setCofactors(cofactors);

    // Links
    EnzymeLinkMapper linkMapper = new EnzymeLinkMapper();
    List<EnzymeLink> links = linkMapper.exportSibLinks(enzymeEntry.getId(), con);
    if (links != null)
      enzymeEntry.setLinks(new TreeSet<EnzymeLink>(links));

    // Comments
    EnzymeCommentMapper commentMapper = new EnzymeCommentMapper();
    List<EnzymeComment> comments = commentMapper.exportSibComments(enzymeEntry.getId(), con);
    if (comments != null) enzymeEntry.setComments(comments);
  }

  /**
   * Returns the next available enzyme ID.
   *
   * @param con The connection.
   * @return The next enzyme ID.
   * @throws SQLException
   */
  public Long findNextEnzymeId(Connection con) throws SQLException {
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");
    long enzymeId = 0;
    PreparedStatement findNextEnzymeId = null;

    try {
      findNextEnzymeId = con.prepareStatement(findNextEnzymeIdStatement());
      ResultSet rs = findNextEnzymeId.executeQuery();
      if (rs.next()) {
        enzymeId = rs.getLong(1);
      }
    } finally {
      findNextEnzymeId.close();
    }

    return new Long(enzymeId);
  }

	private void loadHistoryGraph(EnzymeEntry enzymeEntry, Connection con)
    throws SQLException, DomainException {
		HistoryGraph historyGraph = null;
	    if (enzymeEntry.getStatus() == EnzymeStatusConstant.APPROVED) {
	      EnzymeHistoryMapper historyEventsMapper = new EnzymeHistoryMapper();
	      historyGraph = historyEventsMapper.find(enzymeEntry, con);
	    } else {
	      EnzymeFutureMapper futureEventsMapper = new EnzymeFutureMapper();
	      historyGraph = futureEventsMapper.find(enzymeEntry, con);
	    }
	    if (historyGraph != null) enzymeEntry.setHistory(historyGraph);
	}

}
