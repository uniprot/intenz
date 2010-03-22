package uk.ac.ebi.intenz.mapper;

import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.history.HistoryNode;

import java.sql.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import oracle.jdbc.OracleResultSet;

import org.apache.log4j.Logger;

/**
 * Maps history event information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeHistoryMapper {

  private Logger LOGGER = Logger.getLogger(EnzymeHistoryMapper.class.getName());

  protected Hashtable visitedNodes;

  private static final String COLUMNS = "group_id, event_id, before_id, after_id, event_year, event_note, event_class";

  public EnzymeHistoryMapper() {
    visitedNodes = new Hashtable();
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM history_events WHERE before_id = ? OR after_id = ?";
  }

  public HistoryGraph find(EnzymeEntry enzymeEntry, Connection con) throws SQLException, DomainException {
    if (enzymeEntry == null) throw new NullPointerException();
    HistoryNode currentNode = findNode(enzymeEntry, true, con);
    if (currentNode == null) return null;
    return new HistoryGraph(currentNode);
  }


  // ------------------- PRIVATE METHODS ------------------------

  private HistoryNode findNode(EnzymeEntry currentEntry, boolean isRoot, Connection con) throws SQLException,
          DomainException {
    HistoryNode historyNode = new HistoryNode();
    historyNode.setEnzymeEntry(currentEntry);
    historyNode.setRoot(isRoot);

    // Keep history line loaded before.
    historyNode.setHistoryLine(currentEntry.getHistory().getRootNode().getHistoryLine());

    if (visitedNodes.containsKey(currentEntry.getId()))
      return (HistoryNode) visitedNodes.get(currentEntry.getId()); // Stops recursion.

    visitedNodes.put(currentEntry.getId(), historyNode);

    // Find event(s)
    List historyEvents = findHistoryEvents(historyNode, con);
    historyNode.setEdges(historyEvents);

    return historyNode;
  }

  private List findHistoryEvents(HistoryNode currentNode, Connection con) throws SQLException, DomainException {
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    Vector result = new Vector();

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, currentNode.getEnzymeEntry().getId().longValue());
      findStatement.setLong(2, currentNode.getEnzymeEntry().getId().longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        result.addElement(doLoad(rs, currentNode, con));
      }
    } finally {
    	if (rs != null) rs.close();
      findStatement.close();
    }

    return result;
  }

  /**
   * Creates the <code>EnzymeLink</code> object from the given result set.
   * Some modifications were made so that the timestamp could be included in the Date
   * object. In java.sql.Date the time is obsoleted to zero and only the date is
   * valid eg. 06-Jun-2005 23:34:00 in java.sql.Date becomes 06-Jun-2005 00:00:00
   *
   * @param rs The result set object.
   * @return an <code>EnzymeLink</code> instance.
   * @throws SQLException
   */
  private HistoryEvent doLoad(ResultSet rs, HistoryNode currentNode, Connection con) throws SQLException,
          DomainException {
    long groupId = 0;
    long eventId = 0;
    int beforeId = 0;
    int afterId = 0;
    java.util.Date eventYear = new Date();
    String eventNote = "";
    String eventClass = "";

    if (rs.getInt("group_id") > 0) groupId = rs.getLong("group_id");
    if (rs.getInt("event_id") > 0) eventId = rs.getInt("event_id");
    if (rs.getInt("before_id") > 0) beforeId = rs.getInt("before_id");
    if (rs.getInt("after_id") > 0) afterId = rs.getInt("after_id");
    if (rs.getDate("event_year") != null){
        // WARNING: remove this cast and you will get into trouble!
        Timestamp ts = ((OracleResultSet) rs).getDATE("event_year").timestampValue();
        eventYear =  new Date(ts.getTime());
    }
    if (rs.getString("event_note") != null) eventNote = rs.getString("event_note");
    if (rs.getString("event_class") != null) eventClass = rs.getString("event_class");

    // Check for relative. Either beforeNode or afterNode is different from current node or one of them is null.
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    HistoryNode beforeNode = null, afterNode = null;
    if (beforeId > 0) {
      if (beforeId != currentNode.getEnzymeEntry().getId().longValue()) {
        EnzymeEntry ghostEntry = enzymeEntryMapper.findGhostById(beforeId, con);
        beforeNode = findNode(ghostEntry, false, con);
        afterNode = currentNode;
      } else {
        beforeNode = currentNode;
      }
    }
    if (afterId > 0) {
      if (afterId != currentNode.getEnzymeEntry().getId().longValue()) {
        EnzymeEntry ghostEntry = enzymeEntryMapper.findGhostById(afterId, con);
        beforeNode = currentNode;
        afterNode = findNode(ghostEntry, false, con);
      } else {
        afterNode = currentNode;
      }
    }

    HistoryEvent historyEvent = new HistoryEvent();
    historyEvent.setGroupId(new Long(groupId));
    historyEvent.setEventId(new Long(eventId));
    historyEvent.setBeforeNode(beforeNode);
    historyEvent.setAfterNode(afterNode);
    historyEvent.setDate(eventYear);
    historyEvent.setNote(eventNote);
    historyEvent.setEventClass(EventConstant.valueOf(eventClass));

    return historyEvent;
  }

}
