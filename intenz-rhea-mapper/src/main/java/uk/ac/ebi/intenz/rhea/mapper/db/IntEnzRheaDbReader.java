package uk.ac.ebi.intenz.rhea.mapper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.sql.ARRAY;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.rhea.domain.Coefficient;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Direction;
import uk.ac.ebi.rhea.domain.Merging;
import uk.ac.ebi.rhea.domain.Qualifier;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.ReactionException;
import uk.ac.ebi.rhea.domain.ReactionParticipant;
import uk.ac.ebi.rhea.domain.ReactionParticipant.Location;
import uk.ac.ebi.rhea.domain.Side;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.rhea.domain.XRef;
import uk.ac.ebi.rhea.domain.XRef.Availability;
import uk.ac.ebi.rhea.mapper.IRheaCompoundReader;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.SearchOptions;
import uk.ac.ebi.rhea.mapper.SearchSwitch;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;

/**
 * created to access to rhea from intenz after db split - see UES-12 for
 * motivations, UES-14 for specific sql queries
 *
 */
public class IntEnzRheaDbReader  {

    /**
     * Compound field used for a search.
     */
    private enum Field {

        NAME, ACCESSION
    }

    private final Logger LOGGER = Logger.getLogger(IntEnzRheaDbReader.class);

    protected Connection con;
    protected SQLLoader sqlLoader;
    protected IntEnzRheaCompoundDbReader compoundReader;



    /**
     * @param compoundReader the object who retrieves information about reaction
     * participants from the database.
     * @throws IOException
     */
    public IntEnzRheaDbReader(IntEnzRheaCompoundDbReader compoundReader) throws IOException {
        this.compoundReader = compoundReader;
        this.con = compoundReader.getConnection();
     
   
        this.sqlLoader = compoundReader.getSqlLoader();
      
    }

   
    public IntEnzRheaCompoundDbReader getCompoundReader() {
        return compoundReader;
    }

    /**
     * Setter for database connection. It also sets the same connection for the
     * underlying {@link IRheaCompoundReader}.<br>
     * This method should be used with pooled connections, and only when the
     * previous one and its prepared statements have been properly closed
     * (returned).
     *
     * @param con
     * @throws SQLException
     */
    public void setConnection(Connection con) throws SQLException {
        compoundReader.setConnection(con);
        this.con = con;
        sqlLoader.setConnection(con);
    }

    /**
     * getter for the database connection. added for MET-187
     * @return con
     */
    public Connection getConnection() {
        return this.con;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    /**
     * Closes prepared statements, but not the connection. If you want to close
     * the connection or return it to a pool, please call explicitly the method
     * {@link Connection#close()} or {@link #returnPooledConnection()}
     * respectively.
     * @throws uk.ac.ebi.rhea.mapper.MapperException
     */
    public void close() throws MapperException {
        compoundReader.close(); 
//        try {
//            sqlLoader.close();
//        } catch (SQLException ex) {
//            throw new MapperException(ex);
//        }
    }

    /**
     * Closes (returns to the pool) prepared statements and connection. This
     * method should be called explicitly before finalising this object, in case
     * its connection belongs to a pool.
     * <br>
     * The underlying RheaCompoundDbReader also frees its resources, so there is
     * no need to call its {@link RheaCompoundDbReader#returnPooledConnection()}
     * method.
     *
     * @throws MapperException while closing the compound reader.
     * @throws SQLException while setting the compound reader connection to
     * null.
     */
    public void returnPooledConnection() throws MapperException, SQLException {
        compoundReader.close();
        compoundReader.setConnection(null);
        close();
        if (con != null) {
            con.close();
            con = null;
        }
    }

    /**
     * Finds a reaction related to the given one and having the given direction.
     *
     * @param id ID of the UN reaction.
     * @param dir Direction of the searched reaction.
     * @return
     * @throws MapperException
     */
    public Reaction findRelatedReaction(Long id, Direction dir)
            throws MapperException {
        return findByReactionId(findRelatedReactions(id).get(dir));
    }

    /**
     * Finds the related reactions (same participants, different directions),
     * <i>excluding</i> the given one.
     *
     * @param id a reaction id
     * @return An <code>EnumMap</code> of <code>Reaction.Direction</code>s to
     * reaction ids,
     * <i>excluding</i> the passed id.
     * @throws MapperException
     */
    public EnumMap<Direction, Long> findRelatedReactions(Long id)
            throws MapperException {
        ResultSet rs = null;
        try {
            EnumMap<Direction, Long> related = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--related");
            stm.clearParameters();
            stm.setLong(1, id);
            stm.setLong(2, id);
            stm.setLong(3, id);
            stm.setLong(4, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (related == null) {
                    related = new EnumMap<Direction, Long>(Direction.class);
                }
                related.put(Direction.valueOf(rs.getString("direction")),
                        rs.getLong("reaction_id"));
            }
            return related;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    public Set<Reaction> findParentReactions(Long id)
            throws MapperException {
        ResultSet parentsRs = null;
        try {
            Set<Reaction> parents = null;
            PreparedStatement parentsStm = sqlLoader.getPreparedStatement("--parents");
            parentsStm.clearParameters();
            parentsStm.setLong(1, id);
            parentsRs = parentsStm.executeQuery();
            if (parentsRs.next()) {
                do {
                    Long parentId = parentsRs.getLong("parent_id");
                    Reaction parentReaction = getReactionCore(parentId);
                    if (parents == null) {
                        parents = new HashSet<Reaction>();
                    }
                    parents.add(parentReaction);
                    // WARNING: this parent reaction is just a skeleton!
                } while (parentsRs.next());
            }
            return parents;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (parentsRs != null) {
                try {
                    parentsRs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Finds the related reactions (same participants, different directions),
     * <i>including</i> the given one.
     *
     * @param id a reaction id
     * @return An <code>EnumMap</code> of <code>Reaction.Direction</code>s to
     * reaction ids,
     * <i>including</i> the passed id.
     * @throws uk.ac.ebi.rhea.mapper.MapperException
     */
    public EnumMap<Direction, Long> findAllRelatedReactions(Long id)
            throws MapperException {
        ResultSet rs = null;
        try {
            EnumMap<Direction, Long> related = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--related.all");
            stm.clearParameters();
            stm.setLong(1, id);
            stm.setLong(2, id);
            stm.setLong(3, id);
            stm.setLong(4, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (related == null) {
                    related = new EnumMap<Direction, Long>(Direction.class);
                }
                related.put(Direction.valueOf(rs.getString("direction")),
                        rs.getLong("reaction_id"));
            }
            if (related == null) {
                throw new ReactionException("The reaction id doesn't exist");
            }
            return related;
        } catch (SQLException e) {
            throw new MapperException(e);
        } catch (ReactionException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Finds every reaction in which the passed compound is a participant (this
     * includes UN and complex reactions).
     *
     * @param compoundId a compound ID
     * @return a collection of reaction IDs, or null if none is found
     * @throws Exception
     */
    public Set<Long> findParticipatedReactions(Long compoundId)
            throws MapperException {
        ResultSet rs = null;
        try {
            Set<Long> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--compound.participated.reactions");
            stm.clearParameters();
            stm.setLong(1, compoundId);
            stm.setLong(2, compoundId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new HashSet<Long>();
                }
                result.add(rs.getLong("reaction_id"));
            }
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Get reaction IDs for in which a given ChEBI ID is used
     * @param chebiId
     * @return reaction ids
     * @throws uk.ac.ebi.rhea.mapper.MapperException
     */
    public Set<Long> findParticipatedReactionsByChebiId(Long chebiId)
            throws MapperException {

        ResultSet rs = null;
        try {
            Set<Long> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--participated.reactions.by.chebi.id");
            stm.clearParameters();
            stm.setLong(1, chebiId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new HashSet<Long>();
                }
                result.add(rs.getLong("reaction_id"));
            }
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }

    }

    /**
     * Get the number of reactions in which compound ID (RhEA internal ID) is
     * used
     *
     * @param compoundId
     * @return
     * @throws MapperException
     */
    public int findNumOfParticipatedReactions(Long compoundId)
            throws MapperException {
        ResultSet rs = null;
        try {
            PreparedStatement stm = sqlLoader.getPreparedStatement(
                    "--compound.participated.reactions.number");
            stm.setLong(1, compoundId);
            rs = stm.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Find the enzymes which catalyse a given reaction.
     *
     * @param id The reaction ID
     * @return a <code>Map</code> of enzyme IDs (only the active ones) to EC
     * numbers, or <code>null</code> if nothing is found.
     * @throws uk.ac.ebi.rhea.mapper.MapperException
     */
    public Map<Long, String> findRelatedEnzymes(Long id)
            throws MapperException {
        ResultSet rs = null;
        try {
            Map<Long, String> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement(
                    "--enzymes.related");
            stm.clearParameters();
            stm.setLong(1, id);
            stm.setLong(2, id);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new HashMap<Long, String>();
                }
                String ec = rs.getString("ec");
                if (rs.getString("status").equals("PM")) {
                    ec = fixPreliminaryEcNumber(ec);
                }
                result.put(rs.getLong("enzyme_id"), ec);
            }
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Retrieves cross-references to IntEnz for a whole family of reactions.
     *
     * @param familyId a reaction family (UN reaction) ID
     * @return Sets of cross-references to IntEnz from any of the reactions in
     * the family, organised by reaction direction.
     * @throws SQLException
     */
    public Map<Direction, Set<XRef>> findFamilyRelatedEnzymes(Long familyId)
            throws MapperException {
        Map<Direction, Set<XRef>> result = null;
        ResultSet rs = null;
        try {
            PreparedStatement stm
                    = sqlLoader.getPreparedStatement("--family.enzymes.related");
            stm.clearParameters();
            stm.setLong(1, familyId);
            stm.setLong(2, familyId);
            stm.setLong(3, familyId);
            stm.setLong(4, familyId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new HashMap<Direction, Set<XRef>>();
                }
                String ec = rs.getString("ec");
                if (rs.getString("status").equals("PM")) {
                    ec = fixPreliminaryEcNumber(ec);
                }
                Direction dir = Direction.valueOf(rs.getString("direction"));
                if (result.get(dir) == null) {
                    result.put(dir, new HashSet<XRef>());
                }
                result.get(dir).add(new XRef(
                        Database.INTENZ, rs.getString("enzyme_id"), ec));
            }
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
        return result;
    }

    private String fixPreliminaryEcNumber(String ec) {
        // Add 'n' to last digit of EC number:
        int lastDot = ec.lastIndexOf('.');
        String ec123 = ec.substring(0, lastDot);
        String ec4 = ec.substring(lastDot + 1);
        ec = ec123 + ".n" + ec4;
        return ec;
    }

    private Reaction getReactionCore(Long reactionId)
            throws SQLException {
        ResultSet rs = null;
        try {
            Reaction reaction = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.core");
            stm.clearParameters();
            stm.setLong(1, reactionId);
            rs = stm.executeQuery();
            reaction = loadReaction(rs);
            if (reaction != null) {
                reaction.setId(reactionId);
            }
            return reaction;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Retrieves one reaction from the database, including information about
     * other reactions in the same family (IDs, xrefs).
     *
     * @param reactionId
     * @return the complete reaction, or <code>null</code> if not found.
     * @throws MapperException
     */
    public Reaction findByReactionId(Long reactionId) throws MapperException {
        return findByReactionId(reactionId, true);
    }

    /**
     * Retrieves one reaction from the database.
     *
     * @param reactionId
     * @param getFamily get information about the other reactions in the family
     * (IDs, xrefs)?
     * @return the complete reaction, or <code>null</code> if not found.
     * @throws MapperException
     */
    public Reaction findByReactionId(Long reactionId, boolean getFamily)
            throws MapperException {
        ResultSet childrenRs = null;
        try {
            Reaction reaction = getReactionCore(reactionId);
            if (reaction != null) {
                reaction.setId(reactionId);
                reaction.setCitations(getCitations(reactionId));
                reaction.setXrefs(getXrefs(reactionId));
                if (getFamily) {
                    reaction.setFamilyXrefs(getFamilyXrefs(reaction.getFamilyId()));
                }
                // Children or participants:
                PreparedStatement childrenStm = sqlLoader.getPreparedStatement("--children");
                childrenStm.clearParameters();
                childrenStm.setLong(1, reactionId);
                childrenRs = childrenStm.executeQuery();
                List<Long> childrenIds = null;
                List<Integer> coefs = null;
                List<Integer> orderIns = null;
                if (childrenRs.next()) {
                    do {
                        if (childrenIds == null) {
                            childrenIds = new ArrayList<Long>();
                        }
                        if (coefs == null) {
                            coefs = new ArrayList<Integer>();
                        }
                        if (orderIns == null) {
                            orderIns = new ArrayList<Integer>();
                        }
                        childrenIds.add(childrenRs.getLong("child_id"));
                        coefs.add(childrenRs.getInt("coefficient"));
                        orderIns.add(childrenRs.getInt("order_in"));
                    } while (childrenRs.next());
                    // Must build children reactions in other loop because of the statement reuse:
                    for (int i = 0; i < childrenIds.size(); i++) {
                        Reaction childReaction
                                = findByReactionId(childrenIds.get(i), getFamily);
                        if (orderIns.get(i) == 0) {
                            reaction.addCoupled(childReaction, coefs.get(i));
                        } else {
                            reaction.addStep(childReaction, coefs.get(i));
                        }
                    }
                    reaction.setOverallId(findOverall(reaction));
                } else {
                    loadParticipants(reaction);
                    reaction.setDecompositionIds(findDecompositions(reaction));
                }
                if (getFamily) {
                    reaction.setSiblings(findRelatedReactions(reactionId));
                }
            }
            return reaction;
        } catch (SQLException e) {
            throw new MapperException(e);
        } catch (ReactionException e) {
            throw new MapperException(e);
        } finally {
            if (childrenRs != null) {
                try {
                    childrenRs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    //TODO: comment
    public Reaction findByReactionId(String id) throws MapperException {
        Reaction found = null;
        Pattern p = Pattern.compile("^(\\d+)(_(BI|RL|LR))?$");
        Matcher m = p.matcher(id);
        if (!m.find()) {
            throw new IllegalArgumentException("Invalid reaction ID: " + id);
        }
        Long reactionId = Long.valueOf(m.group(1));
        if (m.group(3) == null) {
            found = findByReactionId(reactionId);
        } else {
            ResultSet rs = null;
            try {
                PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.directional.id");
                stm.setLong(1, reactionId);
                stm.setString(2, m.group(3));
                rs = stm.executeQuery();
                if (rs.next()) {
                    found = findByReactionId(rs.getLong("reaction_id"));
                }
            } catch (SQLException ex) {
                throw new MapperException(ex);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException e) {
                        LOGGER.error("Unable to close result set", e);
                    }
                }
            }
        }
        return found;
    }

    public Long findIdenticalByFingerprint(String fp) throws MapperException {
        ResultSet rs = null;
        try {
            Long result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.identical.by.fp");
            stm.clearParameters();
            stm.setString(1, fp);
            rs = stm.executeQuery();
            if (rs.next()) {
                result = Long.valueOf(rs.getLong("reaction_id"));
            }
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    public Collection<Long> findByFingerprint(String fp) throws MapperException {
        ResultSet rs = null;
        try {
            Collection<Long> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.by.fp");
            stm.clearParameters();
            stm.setString(1, fp);
            stm.setString(2, fp + "|%");
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new HashSet<Long>();
                }
                result.add(rs.getLong("reaction_id"));
            }
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    public Set<Reaction> findByEcNumber(String ec, SearchOptions searchOptions)
            throws MapperException {
        String stmKey = "--reaction.by.ec";
		// Constraints applied to the query. Every constraint requiring a
        // parameter should add it immediately to the stmParams list:
        List<String> queryConstraints = new ArrayList<String>();
        List<String> stmParams = new ArrayList<String>();
        stmParams.add(ec);
        try {
            return findReactions(stmKey, queryConstraints, stmParams, searchOptions);
        } catch (SQLException ex) {
            throw new MapperException(ex);
        }
    }

    /**
     * @deprecated use {@link #findByCompoundName(String, SearchOptions)}
     * instead
     */
    public Set<Reaction> findByReactionText(String text,
            SearchOptions searchOptions)
            throws MapperException {
        try {
            return findByCompound(text, Field.NAME, searchOptions);
        } catch (SQLException ex) {
            throw new MapperException(ex);
        }
    }

    public Set<Reaction> findByCompoundName(String name,
            SearchOptions searchOptions) throws MapperException {
        try {
            return findByCompound(name, Field.NAME, searchOptions);
        } catch (SQLException ex) {
            throw new MapperException(ex);
        }
    }

    /**
     * Chooses a query based on the compound and applies any specific constraint
     * to it. Then, it delegates the rest of the job to the
     * {@link #findReactions(String, List, List, SearchOptions)} method.
     *
     * @param text The search term.
     * @param field The type of search term (see {@link Field}).
     * @param searchOptions Options applied to the search.
     * @return a set of reactions matching the search term and options.
     * @throws SQLException
     */
    private Set<Reaction> findByCompound(String text, Field field,
            SearchOptions searchOptions) throws SQLException {
        String stmKey = null;
		// Constraints applied to the query. Every query or constraint
        // requiring a parameter should add it immediately to the stmParams list:
        List<String> queryConstraints = new ArrayList<String>();
        List<String> stmParams = new ArrayList<String>();

        switch (searchOptions.getSimpleSwitch()) {
            case ANY:
                SearchOptions clonedOptions = null;
                try {
                    clonedOptions = searchOptions.clone();
                } catch (CloneNotSupportedException e) {
                    LOGGER.error("Weird...", e);
                }
                clonedOptions.setSimpleSwitch(SearchSwitch.YES);
                Set<Reaction> simple = findByCompound(text, field, clonedOptions);
                clonedOptions.setSimpleSwitch(SearchSwitch.NO);
                Set<Reaction> complex = findByCompound(text, field, clonedOptions);
                return processSearchResult(simple, complex, searchOptions);

            case NO: // searching for complex ones
                switch (field) {
                    case NAME:
                        stmKey = "--complex.by.compound.name";
                        queryConstraints.add(searchOptions.isIgnoreCase()
                                ? "--constraint.compound.name.ignore.case"
                                : "--constraint.compound.name");
                        break;
                    case ACCESSION:
                        stmKey = "--complex.by.compound.accession";
                        break;
                }
                switch (searchOptions.getStepwiseSwitch()) {
                    case YES:
                        queryConstraints.add("--constraint.complex.stepwise");
                        break;
                    case NO:
                        queryConstraints.add("--constraint.complex.coupled");
                        break;
                    default:
                        queryConstraints.add("");
                }
                break;

            case YES: // searching for simple ones
                switch (field) {
                    case NAME:
                        stmKey = "--simple.by.compound.name";
                        queryConstraints.add(searchOptions.isIgnoreCase()
                                ? "--constraint.compound.name.ignore.case"
                                : "--constraint.compound.name");
                        break;
                    case ACCESSION:
                        stmKey = "--simple.by.compound.accession";
                        break;
                }
                break;
        }
        stmParams.add(text);

        return findReactions(stmKey, queryConstraints, stmParams, searchOptions);
    }

    /**
     * Queries the database to find reactions.
     *
     * @param stmKey The key of the prepared statement to use.
     * @param queryConstraints A list of constraints applied to the query.
     * @param stmParams A list of parameters values to substitute in the
     * prepared statement (it usually includes the user search term).
     * @param searchOptions Options applied to the search.
     * @return A set of reactions matching the user query.
     * @throws SQLException
     */
    private Set<Reaction> findReactions(String stmKey,
            List<String> queryConstraints, List<String> stmParams,
            SearchOptions searchOptions)
            throws SQLException {
        applyReactionConstraints(searchOptions, queryConstraints, stmParams);

        PreparedStatement stm
                = sqlLoader.getPreparedStatement(stmKey, queryConstraints.toArray());

        ResultSet rs = null;
        try {
            stm.clearParameters();
            int i = 0;
            for (String stmParam : stmParams) {
                stm.setString(++i, stmParam);
            }
            Set<Reaction> result = null;
            rs = stm.executeQuery();
            result = loadReactions(rs);
            return filterOptions(result, searchOptions);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Anayses the search options in order to add any additional constraints
     * concerning the reaction (direction, status, mergings) to the database
     * query.
     *
     * @param searchOptions The search options.
     * @param queryConstraints A list of constraints to which new ones - derived
     * from the search options - will be added.
     * @param stmParams A list of parameters to use with the prepared statement.
     * Any new parameter needed by a new constraint will be added here.
     */
    private void applyReactionConstraints(SearchOptions searchOptions,
            List<String> queryConstraints, List<String> stmParams) {

        EnumSet<Direction> dirOption = searchOptions.getDirection();
        String directionConstraint = "";
        if (dirOption == null) {
            // No constraint on direction
        } else if (dirOption.size() == 1) {
            directionConstraint = "--constraint.direction";
            stmParams.add(dirOption.iterator().next().name());
        } else if (dirOption.size() == 2
                && EnumSet.of(Direction.LR, Direction.RL).equals(dirOption)) {
            directionConstraint = "--constraint.directional";
        } // other cases ignored
        queryConstraints.add(directionConstraint);

        if (searchOptions.getStatus() != null) {
            queryConstraints.add("--constraint.status");
            stmParams.add(searchOptions.getStatus());
        } else {
            queryConstraints.add("");
        }

        switch (searchOptions.getMergedSwitch()) {
            case YES:
                queryConstraints.add("--constraint.merged");
                break;
            case NO:
                queryConstraints.add("--constraint.not.merged");
                break;
            default:
                queryConstraints.add("");
        }
    }

    public Set<Reaction> findByCompoundAccession(String accession, SearchOptions searchOptions)
            throws MapperException {
        try {
            return findByCompound(accession, Field.ACCESSION, searchOptions);
        } catch (SQLException ex) {
            throw new MapperException(ex);
        }
    }

    public Set<Reaction> findByCompoundId(Long compoundId)
            throws MapperException {
        ResultSet rs = null;
        try {
            Set<Reaction> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.by.compound.id");
            stm.clearParameters();
            stm.setLong(1, compoundId);
            rs = stm.executeQuery();
            result = loadReactions(rs);
            return result;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    /**
     * Retrieves every citation for a given reaction. Currently, citations are
     * shared among reactions in the same family.
     *
     * @param reactionId the reaction ID
     * @return a collection of citations, or <code>null</code> if none found.
     * The collection will be non-null but empty in case there is some problem
     * with CiteXplore web services.
     * @throws SQLException
     */
    private Set<Result> getCitations(Long reactionId)
            throws SQLException {
        ResultSet rs = null;
        try {
            Set<Result> result = null;
            PreparedStatement stm = sqlLoader.getPreparedStatement("--citations");
            stm.clearParameters();
            stm.setLong(1, reactionId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new LinkedHashSet<Result>();
                }
                try {
                    Result cit
                            = CitexploreWSClient.getCitation(DataSource.valueOf(rs.getString("source")), rs.getString("pub_id"));
                    result.add(cit);
                } catch (Throwable t) {
                    LOGGER.error("Unable to retrieve citation from CiteXplore", t);
                    continue;
                }
            }
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private Set<XRef> getXrefs(Long reactionId) throws MapperException {
        Set<XRef> result = null;
        ResultSet rs = null;
        try {
            PreparedStatement stm = sqlLoader.getPreparedStatement("--xrefs");
            stm.clearParameters();
            stm.setLong(1, reactionId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new TreeSet<XRef>();
                }
                XRef xref = new XRef(Database.fromCode(rs.getString("db_code")),
                        rs.getString("db_accession"));
                result.add(xref);
            }
            // EC numbers:
            Map<Long, String> enzymes = findRelatedEnzymes(reactionId);
            if (enzymes != null) {
                if (result == null) {
                    result = new TreeSet<XRef>();
                }
                for (Entry<Long, String> enzyme : enzymes.entrySet()) {
                    XRef xref = new XRef(Database.INTENZ, enzyme.getKey().toString(),
                            enzyme.getValue());
                    result.add(xref);
                }
            }
            // UniProt: 	 
            stm = sqlLoader.getPreparedStatement("--xrefs.uniprot");
            stm.clearParameters();
            stm.setLong(1, reactionId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (result == null) {
                    result = new TreeSet<XRef>();
                }
                XRef xref = new XRef(Database.UNIPROT,
                        rs.getString("database_ac"), rs.getString("name"));
                result.add(xref);
            }
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
        return result;
    }

    private Map<Direction, Set<XRef>> getFamilyXrefs(Long familyId)
            throws SQLException, MapperException {
        Map<Direction, Set<XRef>> familyXrefs = null;
        ResultSet rs = null;
        try {
            PreparedStatement stm = sqlLoader.getPreparedStatement("--family.xrefs");
            stm.clearParameters();
            stm.setLong(1, familyId);
            stm.setLong(2, familyId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (familyXrefs == null) {
                    familyXrefs = new EnumMap<Direction, Set<XRef>>(Direction.class);
                }
                Direction dir = Direction.valueOf(rs.getString("direction"));
                if (familyXrefs.get(dir) == null) {
                    familyXrefs.put(dir, new TreeSet<XRef>());
                }
                XRef xref = new XRef(Database.fromCode(rs.getString("db_code")),
                        rs.getString("db_accession"));
                familyXrefs.get(dir).add(xref);
            }
            // EC numbers:
            Map<Direction, Set<XRef>> enzymes = findFamilyRelatedEnzymes(familyId);
            if (enzymes != null) {
                if (familyXrefs == null) {
                    familyXrefs = new EnumMap<Direction, Set<XRef>>(Direction.class);
                }
                for (Direction dir : enzymes.keySet()) {
                    if (familyXrefs.get(dir) == null) {
                        familyXrefs.put(dir, new TreeSet<XRef>());
                    }
                    familyXrefs.get(dir).addAll(enzymes.get(dir));
                }
            }
            // UniProt: 
            stm = sqlLoader.getPreparedStatement("--family.xrefs.uniprot");
            stm.clearParameters();
            stm.setLong(1, familyId);
            stm.setLong(2, familyId);
            rs = stm.executeQuery();
            while (rs.next()) {
                if (familyXrefs == null) {
                    familyXrefs = new EnumMap<Direction, Set<XRef>>(Direction.class);
                }
                Direction dir = Direction.valueOf(rs.getString("direction"));
                if (familyXrefs.get(dir) == null) {
                    familyXrefs.put(dir, new TreeSet<XRef>());
                }
                XRef xref = new XRef(Database.UNIPROT,
                        rs.getString("database_ac"), rs.getString("name"));
                familyXrefs.get(dir).add(xref);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return familyXrefs;
    }

    private Set<Reaction> loadReactions(ResultSet rs)
            throws SQLException {
        Set<Reaction> result = null;
        while (rs.next()) {
            if (result == null) {
                result = new HashSet<Reaction>();
            }
            Reaction reaction = new Reaction(rs.getLong("reaction_id"), rs.getString("equation"),
                    Database.fromCode(rs.getString("source")));
            Object qualifiers = rs.getObject("qualifiers");
            if (qualifiers != null) {
                Set<Qualifier> rq = getQualifiers(qualifiers);
                reaction.setQualifiers(rq);
            }
            reaction.setStatus(Status.valueOf(rs.getString("status")));
            result.add(reaction);
        }
        return (result == null) ? null : new HashSet<Reaction>(result);
    }

    private Reaction loadReaction(ResultSet rs) throws SQLException {
        Reaction reaction = null;
        if (rs.next()) {
            String equation = rs.getString("equation");
            String status = rs.getString("status");
            String source = rs.getString("source");
            boolean iubmb = rs.getString("iubmb").equals("Y");
            Object qualifiers = rs.getObject("qualifiers");
            String dataComment = rs.getString("data_comment");
            String reactionComment = rs.getString("reaction_comment");
            String direction = rs.getString("direction");
            String un = rs.getString("un_reaction");
            String avail = rs.getString("public_in_chebi");
            reaction = new Reaction(equation, Database.fromCode(source));
            reaction.setIubmb(iubmb);
            reaction.setStatus(Status.valueOf(status));
            reaction.setDirection(Direction.valueOf(direction));
            if (un != null) {
                reaction.setUnId(Long.valueOf(un));
            }
            if (avail != null) {
                reaction.setChebiPublicAvailability(Availability.valueOf(avail));
            }
            if (qualifiers != null) {
                Set<Qualifier> rq = getQualifiers(qualifiers);
                reaction.setQualifiers(rq);
            }
            if (dataComment != null) {
                reaction.setDataComment(dataComment);
            }
            if (reactionComment != null) {
                reaction.setReactionComment(reactionComment);
            }
        }
        return reaction;
    }

    /**
     * Transforms an object retrieved from a result set into a collection of
     * {@link Qualifier} objects.
     *
     * @param o an object from a result set (must be an
     * <code>oracle.sql.ARRAY</code>).
     * @return a set of qualifiers.
     * @throws SQLException
     */
    public static Set<Qualifier> getQualifiers(Object o) throws SQLException {
        if (o == null) {
            return null;
        }
        ARRAY qualifiers = (ARRAY) o;
        Set<Qualifier> rq = EnumSet.noneOf(Qualifier.class);
        String[] qArray = (String[]) qualifiers.getArray();
        for (int i = 0; i < qArray.length; i++) {
            rq.add(Qualifier.valueOf(qArray[i]));
        }
        return rq;
    }

    private void loadParticipants(Reaction reaction)
            throws SQLException, ReactionException, MapperException {
        ResultSet rs = null;
        try {
            PreparedStatement stm = sqlLoader.getPreparedStatement("--reaction.participants");
            stm.clearParameters();
            stm.setLong(1, reaction.getId());
            rs = stm.executeQuery();
            while (rs.next()) {
                String side = rs.getString("side");
                int coef = rs.getInt("coefficient");
                Coefficient.Type coefType
                        = Coefficient.Type.valueOf(rs.getString("coeff_type"));
                String location = rs.getString("location");
                Long compoundId = rs.getLong("compound_id");
                Compound compound = compoundReader.find(compoundId);
                ReactionParticipant rp = ReactionParticipant.valueOf(compound, coef, coefType);
                if (location != null) {
                    rp.setLocation(Location.fromCode(location));
                }
                reaction.addParticipant(rp, Side.fromCode(side), false);
            }
            reaction.update();
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Combines two collections of search results and filters them
     *
     * @param simple search results for simple reactions
     * @param complex search results for complex reactions
     * @param searchOptions options used to filter the results
     * @return the filtered collection of results, or <code>null</code> if none.
     * @see #filterOptions(Set, SearchOptions)
     */
    private Set<Reaction> processSearchResult(Set<Reaction> simple, Set<Reaction> complex,
            SearchOptions searchOptions) {
        Set<Reaction> result;
        if (simple == null) {
            result = complex;
        } else if (complex == null) {
            result = simple;
        } else {
            result = simple;
            result.addAll(complex);
        }
        return filterOptions(result, searchOptions);
    }

    /**
     * Filters a search result by qualifiers and ChEBI public availability.
     *
     * @param original the whole collection of search results
     * @param searchOptions options used to filter the original result
     * @return the filtered collection of results, or <code>null</code> if none.
     */
    private Set<Reaction> filterOptions(Set<Reaction> original, SearchOptions searchOptions) {
        if (original == null) {
            return null;
        }

        // Consider only non-ANY switches:
        EnumMap<Qualifier, SearchSwitch> relevantSwitches
                = new EnumMap<Qualifier, SearchSwitch>(Qualifier.class);
        for (Qualifier qualifier : searchOptions.getQualifiersSwitches().keySet()) {
            SearchSwitch qs = searchOptions.getQualifiersSwitches().get(qualifier);
            if (!qs.equals(SearchSwitch.ANY)) {
                relevantSwitches.put(qualifier, qs);
            }
        }
        Set<Reaction> filtered = null;
        reactionsLoop:
        for (Reaction reaction : original) {
            for (Qualifier qualifier : relevantSwitches.keySet()) {
                SearchSwitch qs = searchOptions.getQualifiersSwitches().get(qualifier);
                boolean isQualified
                        = reaction.getQualifiers() != null && reaction.getQualifiers().contains(qualifier);
                if ((qs.equals(SearchSwitch.YES) && !isQualified) || (qs.equals(SearchSwitch.NO) && isQualified)) {
                    continue reactionsLoop;
                }
            }
            // Participants public in ChEBI?
            switch (searchOptions.getChebiPublicSwitch()) {
                case ANY:
                    break;
                default:
                    try {
                        // Complete version of the reaction, with all the participants and stuff:
                        Reaction completeReaction = findByReactionId(reaction.getId());
                        boolean isPublicInChebi = Availability.P.equals(
                                completeReaction.getChebiPublicAvailability());
                        if (isPublicInChebi != searchOptions.getChebiPublicSwitch().equals(SearchSwitch.YES)) {
                            continue reactionsLoop;
                        }
                    } catch (Exception e) {
                        LOGGER.error("Unable to filter reactions by checked status", e);
                    }
                    break;
            }
            if (filtered == null) {
                filtered = new HashSet<Reaction>();
            }
            filtered.add(reaction);
        }
        return filtered;
    }

    /**
     * Note that returned reactions have public status, but are not necessarily
     * valid.
     */
    public Collection<Long> findAllPublic() throws MapperException {
        ResultSet rs = null;
        try {
            Collection<Long> reactionIds = new HashSet<Long>();
            PreparedStatement stm = sqlLoader.getPreparedStatement("--public.all");
            rs = stm.executeQuery();
            while (rs.next()) {
                reactionIds.add(rs.getLong("reaction_id"));
            }
            return reactionIds;
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

    public List<Merging> findMergings(Long id) throws MapperException {
        List<Merging> mergings = null;
        ResultSet rs = null;
        try {
            PreparedStatement stm = sqlLoader.getPreparedStatement("--mergings");
            stm.setLong(1, id);
            rs = stm.executeQuery();
            if (rs.next()) { // there should be only one, at most
                mergings = new ArrayList<Merging>();
                do {
                    Merging merging = loadMerging(rs);
                    mergings.add(merging);
                    stm.setLong(1, merging.getToId());
                    rs = stm.executeQuery();
                } while (rs.next());
            }
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
        return mergings;
    }

    /**
     * @param rs
     * @param toId
     * @return
     * @throws SQLException
     */
    private Merging loadMerging(ResultSet rs) throws SQLException {
        return new Merging(
                rs.getLong("from_id"),
                rs.getLong("to_id"),
                rs.getDate("merging_when"),
                rs.getString("merging_who"),
                rs.getString("merging_comment"));
    }

    /**
     *
     * @throws MapperException In case of problem querying the database.
     * @throws IllegalArgumentException if the passed reaction is complex (a
     * decomposition).
     * @since 3.4
     */
    public Set<Long> findDecompositions(Reaction reaction) throws MapperException {
        if (reaction.isComplex()) {
            throw new IllegalArgumentException(
                    "Cannot find decompositions of a decomposition");
        }
        Set<Long> decomps = null;
        ResultSet rs = null;
        try {
            PreparedStatement stm
                    = sqlLoader.getPreparedStatement("--decompositions");
            stm.setString(1, reaction.getFingerprint());
            rs = stm.executeQuery();
            while (rs.next()) {
                if (decomps == null) {
                    decomps = new HashSet<Long>();
                }
                decomps.add(rs.getLong("reaction_id"));
            }
        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
        return decomps;
    }

    /**
     * @throws SQLException In case of problem querying the database.
     * @throws IllegalArgumentException if the passed reaction is not complex (a
     * decomposition).
     * @since 3.4
     */
    public Long findOverall(Reaction reaction) throws MapperException {
        if (!reaction.isComplex()) {
            throw new IllegalArgumentException(
                    "Cannot search overall transformation of an elementary reaction");
        }
        return findIdenticalByFingerprint(reaction.getOverallFingerprint());
    }

    /**
     * Find the reactions in which this ChEBI update (by chebiId) is involved
     * and classify these reactions in notNeutral or neutral update. (modified
     * for MET-129)
     *
     */
    public void findModifiedReactions(Long chebiId, Map<Long, String> notNeutralUpdateList, Map<Long, String> neutralUpdateList) throws MapperException {
//	public void findModifiedReactions(Map<Long, String> l1, Map<Long, String> l2) { 
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            stm = sqlLoader.getPreparedStatement("--find.reactions.affected.by.chebi.formula.or.charge.update");
            stm.setLong(1, chebiId);
            stm.setLong(2, chebiId);
            rs = stm.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    Long reactionId = rs.getLong(1);
                    //String chebiId = rs.getLong(2);
                    String status = rs.getString(3);
                    long countLeft = rs.getLong(4);
                    long countRight = rs.getLong(5);
                    // compare right-left counts and put results in two distinct reactions lists: notNeutralUpdateList and neutralUpdateList
                    if (countLeft - countRight == 0) { // this ChEBI compound being updated is present as many times on the right and on the left of the reaction 
                        neutralUpdateList.put(reactionId, status);
                    } else { // this ChEBI compound being update is NOT present as many times on the right and on the left of the reaction
                        notNeutralUpdateList.put(reactionId, status);
                    }
//				if (count == 1) l1.put(reactionId, status);
//				else if (count > 1) l2.put(reactionId, status);

                }
            }

        } catch (SQLException e) {
            throw new MapperException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    LOGGER.error("Closing ResultSet", ex);
                }
            }
        }
    }

}
