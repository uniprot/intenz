package uk.ac.ebi.intenz.mapper;

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
import uk.ac.ebi.rhea.domain.XRef.Availability;
import uk.ac.ebi.rhea.mapper.MapperException;

/**
 * Maps reaction information to the corresponding database tables.
 * <br>
 * <b>IMPORTANT</b>: after using instances of this class, call the
 * {@link #close()} method, otherwise the underlying Rhea mapper objects will
 * keep their statements open.
 * @author Michael Darsow
 * @version $Revision: 1.10 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeCofactorMapper {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeCofactorMapper.class.getName());

  public EnzymeCofactorMapper(){
  }
  
  public void close(){
  }

  	@Override
	protected void finalize() throws Throwable {
		close();
	}

  private static final String COLUMNS =
	  "enzyme_id, cofactor_text, order_in, source, status, web_view, compound_id, operator, op_grp";

  private static final String FIND_STM =
	  "SELECT " + COLUMNS + " FROM cofactors WHERE enzyme_id = ? AND compound_id IS NOT NULL" +
	  " ORDER BY order_in, op_grp ASC";
  
  private static final String FIND_BY_CHEBI_ID =
          "SELECT cd.compound_id, cd.name FROM compound_data cd"
          + " WHERE cd.accession = ?";

  private static final String FIND_SIB_COFACTORS_STM =
	  "SELECT " + COLUMNS + " FROM cofactors" +
      " WHERE enzyme_id = ? AND compound_id IS NOT NULL AND (web_view = 'INTENZ' OR web_view LIKE '%SIB%')" +
      " ORDER BY order_in, op_grp ASC";
  
  private static final String FIND_ALL =
	  "SELECT cf.compound_id, cf.enzyme_id, f_quad2string(e.ec1, e.ec2, e.ec3, e.ec4) ec" +
	  " FROM cofactors cf, enzymes e WHERE cf.compound_id IS NOT NULL and cf.enzyme_id = e.enzyme_id" +
	  " ORDER BY compound_id";
  
  private static final String LOAD_COMPOUND_STM =
          "SELECT accession, name, formula, charge, published"
          + " FROM compound_data WHERE compound_id = ?";

  private static final String INSERT_STM =
	  "INSERT INTO cofactors (" + COLUMNS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String INSERT_COMPOUND_STM =
          "INSERT INTO compound_data"
          + " (compound_id, name, source, accession) VALUES"
          + " (s_compound_id.nextval, ?, 'CHEBI', ?)";
  
  private static final String DELETE_COMPOUND_STM =
          "DELETE FROM compound_data WHERE compound_id = ?";
  
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
      LOGGER.debug("No cofactor information found for the enzyme with ID "
              + enzymeId);
    }
    return result;
  }
  
  /**
   * Retrieves one cofactor from the IntEnz database by its ChEBI ID.
   * @param chebiId the ChEBI ID of the cofactor.
   * @param con a connection to IntEnz database.
   * @return a Compound, or <code>null</code> if it does not exist in IntEnz.
   * @throws SQLException in case of problem retrieving the cofactor.
   * @since 4.6.0
   */
  public Compound findByChebiId(String chebiId, Connection con)
  throws SQLException{
      Compound compound = null;
      PreparedStatement stm = null;
      ResultSet rs = null;
      try {
          stm = con.prepareStatement(FIND_BY_CHEBI_ID);
          stm.setString(1, chebiId);
          rs = stm.executeQuery();
          if (rs.next()){
              compound = new Compound(rs.getLong("compound_id"),
                      rs.getString("name"), "CHEBI", chebiId);
          }
      } finally {
          if (rs != null) rs.close();
          if (stm != null) stm.close();
      }
      return compound;
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
				  Compound compound = getCompound(con, compoundId);
				  lastMap = new HashMap<Long, String>();
				  result.put(compound, lastMap);
			  }
			  lastMap.put(enzymeId, ec);
			  lastCompoundId = compoundId;
		  }
	  } finally {
		  if (rs != null) rs.close();
		  if (stm != null) stm.close();
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
   * Inserts one compound (cofactor) into the COMPOUND_DATA table.
   * @param compound a compound.
   * @param con a connection to the IntEnz database.
   * @throws SQLException in case of problem inserting the row.
   * @return the internal ID of the compound in the table.
   * @since 4.6.0
   */
  public Long insertCompound(Compound compound, Connection con)
  throws SQLException {
      Long compoundId = null;
      PreparedStatement stm = null;
      ResultSet rs = null;
      try {
          stm = con.prepareStatement(INSERT_COMPOUND_STM,
                  new String[]{ "compound_id" });
          stm.setString(1, compound.getXmlName());
          stm.setString(2, compound.getAccession());
          int result = stm.executeUpdate();
          if (result > 0){
              rs = stm.getGeneratedKeys();
              if (rs != null && rs.next()){
                  compoundId = rs.getLong(1);
                  compound.setId(compoundId);
              }
          }
          LOGGER.info("Inserted new cofactor: " + compound.getXmlName()
                  + " [" + compound.getAccession() + "]");
          return compoundId;
      } finally {
          if (rs != null) rs.close();
          if (stm != null) stm.close();
      }
  }
  
  /**
   * Deletes a cofactor from the COMPOUND_DATA table.
   * @param compoundId the internal ID of the compound.
   * @param con a connection to the IntEnz database.
   * @throws SQLException in case of problem with the database.
   * @since 4.6.0
   */
  public void deleteCompound(Long compoundId, Connection con)
  throws SQLException{
      PreparedStatement stm = null;
      try {
          stm = con.prepareStatement(DELETE_COMPOUND_STM);
          stm.setLong(1, compoundId);
          stm.execute();
          LOGGER.info("Deleted compound with internal ID " + compoundId);
      } finally {
          if (stm != null) stm.close();
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
   */
  private Cofactor doLoad(ResultSet rs) throws SQLException {
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
		compound = Compound.valueOf(compoundId, null, cofactorString, null,
		        null, null, Availability.N);
	} else {
	    Connection connection = rs.getStatement().getConnection();
	    compound = getCompound(connection, compoundId);
	}
    return Cofactor.valueOf(compound, EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(view));
  }

  /**
   * Retrieves a compound (cofactor) from the database.
   * @param connection
   * @param compoundId the internal compound ID.
   * @return a cofactor.
   * @throws SQLException
   */
    public Compound getCompound(Connection connection, Long compoundId)
    throws SQLException {
        Compound compound = null;
        PreparedStatement stm = null;
        ResultSet cofactorRs = null;
        try {
            stm = connection.prepareStatement(LOAD_COMPOUND_STM);
            stm.setLong(1, compoundId);
            cofactorRs = stm.executeQuery();
            if (cofactorRs.next()){
                String accession = cofactorRs.getString("accession");
                String name = cofactorRs.getString("name");
                String formula = cofactorRs.getString("formula");
                String charge = String.valueOf(cofactorRs.getInt("charge"));
                String avail = cofactorRs.getString("published");
                compound = Compound.valueOf(compoundId,
                        Long.valueOf(accession.replace("CHEBI:", "")),
                        name, formula, charge, accession,
                        avail == null? null : Availability.valueOf(avail));
            }
        } finally {
        	if (cofactorRs != null) cofactorRs.close();
        	if (stm != null) stm.close();
        }
        return compound;
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
    insertStatement.setString(2, cofactor.getCompound().getXmlName());
    insertStatement.setInt(3, orderIn);
    insertStatement.setString(4, cofactor.getSource().toString());
    insertStatement.setString(5, status.getCode());
    insertStatement.setString(6, cofactor.getView().toString());
    if (Compound.NO_ID_ASSIGNED.equals(cofactor.getCompound().getId())){
        insertStatement.setLong(7, insertCompound(
                cofactor.getCompound(), insertStatement.getConnection()));
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
