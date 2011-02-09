package uk.ac.ebi.intenz.mapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;

/**
 * Maps reaction information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.10 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeCofactorMapper {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeCofactorMapper.class.getName());
  
  protected RheaCompoundDbReader rheaCompoundReader;

  public EnzymeCofactorMapper(){
	  try {
		rheaCompoundReader = new RheaCompoundDbReader(null);
	} catch (IOException e) {
		throw new RuntimeException(e);
	}
  }

  private static final String COLUMNS =
	  "enzyme_id, cofactor_text, order_in, source, status, web_view, compound_id, operator, op_grp";

  private static final String FIND_STM =
	  "SELECT " + COLUMNS + " FROM cofactors WHERE enzyme_id = ? AND compound_id IS NOT NULL" +
	  " ORDER BY order_in, op_grp ASC";

  private static final String FIND_SIB_COFACTORS_STM =
	  "SELECT " + COLUMNS + " FROM cofactors" +
      " WHERE enzyme_id = ? AND compound_id IS NOT NULL AND (web_view = 'INTENZ' OR web_view LIKE '%SIB%')" +
      " ORDER BY order_in, op_grp ASC";
  
  private static final String FIND_ALL =
	  "SELECT cf.compound_id, cf.enzyme_id, f_quad2string(e.ec1, e.ec2, e.ec3, e.ec4) ec" +
	  " FROM cofactors cf, enzymes e WHERE cf.compound_id IS NOT NULL and cf.enzyme_id = e.enzyme_id" +
	  " ORDER BY compound_id";

  private static final String INSERT_STM =
	  "INSERT INTO cofactors (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String DELETE_ALL_STM =
	  "DELETE cofactors WHERE enzyme_id = ?";
  
  private static final String UPDATE_STM =
	  "UPDATE cofactors SET compound_id = ?, cofactor_text = ? WHERE compound_id = ?";

  /**
   * Tries to find cofactor information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>Set</code> containing <code>Cofactor</code> instances,
   * 	or <code>OperatorSet</code> instances filled with <code>Cofactor</code> objects,
   * 	or <code>null</code> if nothing has been found.
   * @throws SQLException in case of a generic database problem.
 * @throws MapperException in case of problem retrieving the cofactors.
   */
  public Set<Object> find(Long enzymeId, Connection con)
  throws SQLException, MapperException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    Set<Object> result = null;

    try {
      // Core information.
      findStatement = con.prepareStatement(FIND_STM);
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      result = getCofactors(rs);
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (result == null) {
      LOGGER.info("No cofactor information found for the enzyme with ID " + enzymeId);
    }
    return result;
  }

  /**
   * Exports all cofactors which are displayed in the ENZYME view.
   *
   * Affected table rows will be locked.
   *
   * @param enzymeId The enzyme ID used to retreive the related cofactors.
   * @param con The database connection.
   * @return an {@link java.util.ArrayList} of cofactors or <code>null</code> if no cofactor could be found.
   * @throws SQLException in case of a generic database problem.
   * @throws MapperException in case of problem retrieving the cofactors.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public Set<Object> exportSibCofactors(Long enzymeId, Connection con)
  throws SQLException, MapperException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    Set<Object> result = new HashSet<Object>();

    try {
      // Core information.
      findStatement = con.prepareStatement(FIND_SIB_COFACTORS_STM);
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      result = getCofactors(rs);
    } finally {
    	if (rs != null) rs.close();
    	if (findStatement != null) findStatement.close();
    }

    return result;
  }
  
  /**
   * Retrieves a list of compounds acting as cofactors in the database, along with
   * the identifiers for the enzymes they appear in.
   * @param con a database connection
   * @return a map of compounds (cofactors) to pairs enzyme ID/EC number.
   * @throws SQLException in case of a generic database problem.
   * @throws MapperException in case of problem retrieving compounds.
   * @throws MapperException
   */
  public Map<Compound, Map<Long, String>> findAll(Connection con)
  throws SQLException, MapperException {
	  Map<Compound, Map<Long, String>> result = new Hashtable<Compound, Map<Long, String>>();
	  Statement stm = null;
	  ResultSet rs = null;
	  try {
		  rheaCompoundReader.setConnection(con);
		  stm = con.createStatement();
		  rs = stm.executeQuery(FIND_ALL);
		  Long lastCompoundId = null;
		  Map<Long, String> lastMap = null;
		  while (rs.next()){
			  Long compoundId = rs.getLong("compound_id");
			  Long enzymeId = rs.getLong("enzyme_id");
			  String ec = rs.getString("ec");
			  if (!compoundId.equals(lastCompoundId)){
				  // new in the result
				  Compound compound = rheaCompoundReader.find(compoundId);
				  lastMap = new HashMap<Long, String>();
				  result.put(compound, lastMap);
			  }
			  lastMap.put(enzymeId, ec);
			  lastCompoundId = compoundId;
		  }
	  } finally {
		  if (rs != null) rs.close();
		  if (stm != null) stm.close();
		  rheaCompoundReader.close();
	  }
	  return result;
  }

  /**
   * Stores the given list of cofactors into the database.
   *
   * @param cofactors The vector of cofactors.
   * @param enzymeId  The enzyme ID.
   * @param status
   * @param con       ...
   * @throws SQLException
   */
  public void insert(Collection<Object> cofactors, Long enzymeId, Status status, Connection con)
  throws SQLException {
	  if (cofactors == null) throw new NullPointerException("Parameter 'cofactors' must not be null.");
	  if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
	  if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
	  if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

	  PreparedStatement insertStatement = null;

	  try {
		  insertStatement = con.prepareStatement(INSERT_STM);
		  int iii = 1;
		  for (Object o : cofactors) {
			  if (o instanceof Cofactor){
				  Cofactor cofactor = (Cofactor) o;
				  doInsert(cofactor, enzymeId, (iii), status, null, null, insertStatement);
				  insertStatement.execute();
			  } else if (o instanceof OperatorSet){
				  OperatorSet os = (OperatorSet) o;
				  doInsert(os, enzymeId, (iii), status, "0", 0, insertStatement);
			  }
			  iii++;
		  }
	  } finally {
		  insertStatement.close();
	  }
  }

  /**
   * Reloads the given list of cofactors into the database.
   *
   * @param cofactors The vector of cofactors.
   * @param enzymeId  The enzyme ID.
   * @param status
   * @param con       ...
   * @throws SQLException
   */
  public void reload(Collection<Object> cofactors, Long enzymeId, Status status, Connection con)
  throws SQLException {
    if (cofactors == null) throw new NullPointerException("Parameter 'cofactors' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    deleteAll(enzymeId, con);
    insert(cofactors, enzymeId, status, con);
  }
  
  /**
   * Updates the ChEBI mapping of a cofactor, and the name if needed too.
   * @param oldCf the old cofactor
   * @param newCf the new cofactor
   * @param con
   * @throws SQLException
   */
  public void update(Compound oldCf, Compound newCf, Connection con)
  throws SQLException{
	  PreparedStatement stm = null;
	  try {
		  stm = con.prepareStatement(UPDATE_STM);
		  stm.setLong(1, newCf.getId());
		  stm.setString(2, newCf.getName());
		  stm.setLong(3, oldCf.getId());
		  stm.execute();
	  } finally {
		  if (stm != null) stm.close();
	  }
  }

  /**
   * Deletes all cofactors related to one enzyme instance.
   *
   * @param enzymeId Enzyme ID of the enzyme instance.
   * @param con      ...
   * @throws SQLException
   */
  public void deleteAll(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteAllStatement = null;

    try {
      deleteAllStatement = con.prepareStatement(DELETE_ALL_STM);
      deleteAllStatement.setLong(1, enzymeId.longValue());
      deleteAllStatement.execute();
    } finally {
      deleteAllStatement.close();
    }
  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>Cofactor</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return a <code>Cofactor</code> instance.
   * @throws SQLException if a generic database error occurs.
   * @throws MapperException in case of error loading compounds.
   * @throws MapperException
   */
  private Cofactor doLoad(ResultSet rs) throws SQLException, MapperException {
    assert rs != null;

    String cofactorString = "";
    String source = "";
    String view = "";
    Long compoundId;

    if (rs.getString("cofactor_text") != null) cofactorString = rs.getString("cofactor_text");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("web_view") != null) view = rs.getString("web_view");
    compoundId = rs.getLong("compound_id");

    Compound compound = null;
	if (compoundId == Compound.NO_ID_ASSIGNED){
		compound = Compound.valueOf(compoundId, cofactorString, null, 0, null, null, null, null);
	} else {
        try {
    	    rheaCompoundReader.setConnection(rs.getStatement().getConnection());
    	    compound = rheaCompoundReader.find(compoundId);
        } finally {
        	rheaCompoundReader.close();
        }
	}
    return Cofactor.valueOf(compound, EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(view));
  }

  @SuppressWarnings("unchecked")
  private Set<Object> getCofactors(ResultSet rs)
  throws SQLException, MapperException {
	  Set result = null;
	  int currentOrderIn = 0;
	  OperatorSet.Builder osBuilder = null;
      while (rs.next()) {
    	  if (result == null) result = Collections.synchronizedSet(new HashSet());
    	  int orderIn = rs.getInt("order_in");
    	  if (orderIn > currentOrderIn){
    		  if (osBuilder != null && !osBuilder.isEmpty()){
	    		  result.add(osBuilder.getOperatorSet());
	    		  osBuilder.clear();
    		  }
    		  currentOrderIn = orderIn;
    	  }
    	  Cofactor cofactor = doLoad(rs);
    	  if (rs.getString("op_grp") == null){
    		  result.add(cofactor);
    	  } else {
    		  if (osBuilder == null) osBuilder = new OperatorSet.Builder();
    		  osBuilder.add(rs.getString("op_grp"), rs.getString("operator"), cofactor);
    	  }
      }
	  if (osBuilder != null && !osBuilder.isEmpty()) result.add(osBuilder.getOperatorSet());
	  return result;
  }

  private int doInsert(OperatorSet cofactors, Long enzymeId, int orderIn, Status status,
  		String operatorGroup, int currentGroup, PreparedStatement insertStatement)
  throws SQLException{
  	for (Object o : cofactors) {
  		if (o instanceof Cofactor){
  			Cofactor cofactor = (Cofactor) o;
  			doInsert(cofactor, enzymeId, orderIn, status, cofactors.getOperator(),
  					operatorGroup, insertStatement);
  			insertStatement.execute();
  		} else {
  			currentGroup++;
  			OperatorSet os = (OperatorSet) o;
  			currentGroup = doInsert(os, enzymeId, orderIn, status,
  					operatorGroup+String.valueOf(currentGroup), currentGroup, insertStatement);
  		}
  	}
  	return currentGroup;
  }

  /**
   * Sets the parameters of the insert statement.
   *
   * @param cofactor        ...
   * @param enzymeId        ...
   * @param orderIn         ...
   * @param insertStatement ...
   * @throws java.sql.SQLException
   */
  private void doInsert(Cofactor cofactor, Long enzymeId, int orderIn, Status status,
                        String operator, String operatorGroup, PreparedStatement insertStatement)
          throws SQLException {
    assert cofactor != null : "Parameter 'cofactor' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert orderIn > 0 : "Parameter 'orderIn' must be > 0.";
    assert status != null : "Parameter 'status' must not be null.";
    assert insertStatement != null : "Parameter 'insertStatement' must not be null.";
    assert !(operator == null ^ operatorGroup == null) : "No operator w/o operator group and viceversa";

    insertStatement.setLong(1, enzymeId.longValue());
    insertStatement.setString(2, cofactor.getCompound().getName());
    insertStatement.setInt(3, orderIn);
    insertStatement.setString(4, cofactor.getSource().toString());
    insertStatement.setString(5, status.getCode());
    insertStatement.setString(6, cofactor.getView().toString());
    if (cofactor.getCompound().getId() == Compound.NO_ID_ASSIGNED){
    	insertStatement.setNull(7, Types.NUMERIC);
    } else {
        insertStatement.setLong(7, cofactor.getCompound().getId());
    }
    if (operatorGroup == null){
    	insertStatement.setNull(8, Types.VARCHAR);
    	insertStatement.setNull(9, Types.VARCHAR);
    } else {
    	insertStatement.setString(8, operator);
    	insertStatement.setString(9, operatorGroup);
    }
  }
}
