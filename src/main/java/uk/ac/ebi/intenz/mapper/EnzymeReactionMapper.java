package uk.ac.ebi.intenz.mapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;

/**
 * Maps reaction information to the corresponding database table.
 * <p/>
 *
 * @author Michael Darsow,
 * 		Rafael Alcántara
 * @version $Revision: 1.6 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeReactionMapper {

	private static final Logger LOGGER =
		Logger.getLogger(EnzymeReactionMapper.class.getName());
	
	protected RheaDbReader rheaReader;
	
  public EnzymeReactionMapper(){
	  try {
		rheaReader = new RheaDbReader(new RheaCompoundDbReader(null));
	} catch (IOException e) {
		throw new RuntimeException(e);
	}
  }

  private static final String SEL_COLS_FROM_TBLS =
	  "SELECT rm.reaction_id, rm.web_view, rm.order_in, ir.equation, ir.source, ir.qualifiers, ir.status" +
	  " FROM reactions_map rm, intenz_reactions ir";

  private static final String FIND_STM =
	  SEL_COLS_FROM_TBLS +
	  " WHERE rm.enzyme_id = ? AND rm.reaction_id = ir.reaction_id" +
	  " ORDER BY rm.order_in";

  private static final String FIND_SIB_STM =
	  SEL_COLS_FROM_TBLS +
	" WHERE rm.enzyme_id = ? AND rm.reaction_id = ir.reaction_id" +
	" AND (rm.web_view  LIKE '%SIB%' OR rm.web_view = 'INTENZ')" +
	" FOR UPDATE ORDER BY rm.order_in";

  private static final String INSERT_STM =
	  "INSERT INTO reactions_map (reaction_id, enzyme_id, web_view, order_in)" +
	  " VALUES (?, ?, ?, ?)";

  private static final String DELETE_ALL_STM =
	  "DELETE reactions_map WHERE enzyme_id = ?";

  /* Abstract reactions in old REACTIONS table */
  
	private static final String SEL_ABSTRACT_COLS_FROM_TBLS =
		"SELECT equation, web_view, order_in, source, NULL reaction_id, NULL qualifiers, status FROM reactions";

	private static final String FIND_ABSTRACT_STM =
		SEL_ABSTRACT_COLS_FROM_TBLS +
		" WHERE enzyme_id = ? ORDER BY order_in";

	private static final String FIND_ABSTRACT_SIB_STM =
		SEL_ABSTRACT_COLS_FROM_TBLS +
		" WHERE enzyme_id = ? AND (web_view LIKE '%SIB%' OR web_view = 'INTENZ') ORDER BY order_in";

	private static final String INSERT_ABSTRACT_STM =
		"INSERT INTO reactions (enzyme_id, equation, order_in, status, source, web_view)" +
		" VALUES (?, ?, ?, ?, ?, ?)";

  private static final String DELETE_ALL_ABSTRACT_STM =
	  "DELETE reactions WHERE enzyme_id = ?";

  /**
   * Tries to find reaction information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>EnzymaticReactions</code> object containing
   * 	<code>Reaction</code>instances or <code>null</code> if nothing has been found.
   * @throws SQLException in case of a generic database problem.
   * @throws MapperException in case of problem retrieving Rhea reactions.
   */
  public EnzymaticReactions find(Long enzymeId, Connection con)
  throws SQLException, MapperException {
        if (enzymeId == null) {
            throw new NullPointerException("Parameter 'enzymeId' must not be null.");
        }
        if (con == null) {
            throw new NullPointerException("Parameter 'con' must not be null.");
        }

        EnzymaticReactions result = find(enzymeId, con, FIND_STM);
    	EnzymaticReactions abstractReactions =
    		find(enzymeId, con, FIND_ABSTRACT_STM);
        if (result == null) { // no Rhea reactions
            result = abstractReactions;
        } else {
        	if (abstractReactions != null) result.add(abstractReactions);
        }
        return result;
    }

  /**
   * Exports all reactions which are displayed in the ENZYME view.
   *
   * Affected table rows will be locked.
   *
   * @param enzymeId The enzyme ID used to retreive the related reactions.
   * @param con The database connection.
   * @return an <code>EnzymaticReactions</code> containing reactions
   * 	or <code>null</code> if no reaction could be found.
   * @throws SQLException if a generic database error occured.
   * @throws MapperException in case of problem retrieving Rhea reactions.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public EnzymaticReactions exportSibReactions(Long enzymeId, Connection con)
  throws SQLException, MapperException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    EnzymaticReactions result = find(enzymeId, con, FIND_SIB_STM);
    if (result == null)
    	result = find(enzymeId, con, FIND_ABSTRACT_SIB_STM);
    else
        result.add(find(enzymeId, con, FIND_ABSTRACT_SIB_STM));
    return result;
  }
  
  private EnzymaticReactions find(Long enzymeId, Connection con, String findQuery)
  throws SQLException, MapperException {
	  EnzymaticReactions result = null;
	  PreparedStatement findStatement = null;
	  ResultSet rs = null;
	  try {
		  findStatement = con.prepareStatement(findQuery);
		  findStatement.setLong(1, enzymeId.longValue());
		  rs = findStatement.executeQuery();
		  while (rs.next()) {
			  if (result == null) result = new EnzymaticReactions();
              result.add(loadReaction(rs), rs.getString("web_view"));
		  }
	  } finally {
		  if (rs != null) rs.close();
		  if (findStatement != null) findStatement.close();
	  }
	  return result;
  }

  /**
   * Stores the given list of reactions into the database.
   *
   * @param reactions The vector of reactions.
   * @param enzymeId  The enzyme ID.
   * @param con       ...
   * @throws SQLException
   */
  public void insert(EnzymaticReactions reactions, Long enzymeId, Connection con)
  throws SQLException {
    if (reactions == null) throw new NullPointerException("Parameter 'reactions' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    for (int i = 0; i < reactions.size(); i++) {
		Reaction reaction = reactions.getReaction(i);
		EnzymeViewConstant view = reactions.getReactionView(i);
    	if (reaction.getId().equals(Reaction.NO_ID_ASSIGNED)){
    		insertReaction(enzymeId, reaction, view, i+1, con);
    	} else {
    		insertMapping(enzymeId, reaction.getId(), view, i+1, con);
    	}
    }
  }
  
  /**
   * Inserts abstract reactions into the old REACTIONS table.
   * @param enzymeId
   * @param reaction
   * @param view
   * @param orderIn
   * @param con
   * @throws SQLException
   */
  private void insertReaction(Long enzymeId, Reaction reaction, EnzymeViewConstant view,
		  int orderIn, Connection con) throws SQLException {
	  PreparedStatement stm = null;
	  try {
		  stm = con.prepareStatement(INSERT_ABSTRACT_STM);
		  stm.setLong(1, enzymeId);
		  stm.setString(2, reaction.getTextualRepresentation());
		  stm.setInt(3, orderIn);
		  stm.setString(4, reaction.getStatus().toString()); // NO status
		  stm.setString(5, reaction.getSource().toString());
		  stm.setString(6, view.toString());
		  stm.execute();
	  } finally {
		  if (stm != null) stm.close();
	  }
  }
  
  /**
   * Inserts a mapping reaction-enzyme into the database.
   * @param enzymeId
   * @param reactionId
   * @param view
   * @param orderIn
   * @param con
 * @throws SQLException
   */
  public void insertMapping(Long enzymeId, Long reactionId, EnzymeViewConstant view,
		  int orderIn, Connection con) throws SQLException{
	  PreparedStatement stm = null;
	  try {
		  stm = con.prepareStatement(INSERT_STM);
		  stm.setLong(1, reactionId);
		  stm.setLong(2, enzymeId);
		  stm.setString(3, view.toString());
		  stm.setInt(4, orderIn);
		  stm.execute();
		  LOGGER.info("[MAPPED] enzyme_id=" + enzymeId + " to reaction_id=" + reactionId);
	  } finally {
		  if (stm != null) stm.close();
	  }
  }

  /**
   * Replace the reactions in the database related to one enzyme
   * with those passed as parameter.
   *
   * @param enzymeId  The enzyme ID.
   * @param reactions The vector of reactions.
   * @param con
   * @throws SQLException
   */
  public void update(Long enzymeId, EnzymaticReactions reactions, Connection con)
  throws SQLException {
    deleteAll(enzymeId, con);
    insert(reactions, enzymeId, con);
  }

  /**
   * Deletes all reactions mappings and abstract reactions related to one enzyme instance.
   *
   * @param enzymeId Enzyme ID of the enzyme instance.
   * @param con      ...
   * @throws SQLException
   */
  void deleteAll(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement stm = null;
    try {
      stm = con.prepareStatement(DELETE_ALL_STM);
      stm.setLong(1, enzymeId.longValue());
      stm.execute();
      stm.close();
      stm = con.prepareStatement(DELETE_ALL_ABSTRACT_STM);
      stm.setLong(1, enzymeId.longValue());
      stm.execute();
    } finally {
      if (stm != null) stm.close();
    }
  }

  /**
   * Creates an empty Reaction object, with just an equation, source and status.
   * The reaction ID will be <code>null</code> for old plain text reactions, not
   * <code>null</code> in case of error while reading data from Rhea.
   * @param reactionId
   * @param equation
   * @param source
   * @param status
   * @return
   * @throws java.sql.SQLException
   */
    private Reaction loadEmtpyReaction(Long reactionId, String equation,
            String source, String status)
    throws SQLException {
        Reaction reaction = new Reaction(reactionId, equation, Database.valueOf(source));
        reaction.setStatus(Status.valueOf(status));
        return reaction;
    }

  /**
   * Creates the <code>Reaction</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return a {@link Reaction}
   * @throws SQLException in case of a generic database problem.
   * @throws MapperException in case of problem retrieving the Rhea reaction.
   */
  private Reaction loadReaction(ResultSet rs) throws SQLException, MapperException {
    assert rs != null : "Parameter 'rs' must not be null.";

    String equation = rs.getString("equation");
    String source = rs.getString("source");
    Long reactionId = rs.getLong("reaction_id");
    String status = rs.getString("status");
	// Convert enzyme status codes to Rhea-ction status codes:
	if (status.equals("SU") || status.equals("PR")) status = "NO";
    Reaction reaction;
    if (reactionId.equals(0L)){
        reaction = loadEmtpyReaction(reactionId, equation, source, status);
    } else { // get the whole reaction
        try {
            rheaReader.setConnection(rs.getStatement().getConnection());
            reaction = rheaReader.findByReactionId(reactionId);
        } catch (Exception ex) {
            LOGGER.error("Unable to retrieve reaction from Rhea", ex);
            reaction = loadEmtpyReaction(reactionId, equation, source, status);
        } finally {
        	rheaReader.close();
        }
    }
    return reaction;
  }

}
