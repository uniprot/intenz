package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.history.FutureEvent;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.history.HistoryNode;
import uk.ac.ebi.intenz.domain.history.Timeout;

/**
 * Maps future event information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeFutureMapper extends EnzymeHistoryMapper {

  private static final String COLUMNS = "f.group_id, f.event_id, f.before_id, f.after_id, f.event_year, " +
          "f.event_note, f.event_class, f.status, f.timeout_id, " +
          "t.enzyme_id, t.start_date, t.due_date";

  public EnzymeFutureMapper() {
    super();
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
            " FROM future_events f, timeouts t" +
            " WHERE (before_id = ? OR after_id = ?) AND f.timeout_id = t.timeout_id";
  }

    @Override
  public HistoryGraph find(EnzymeEntry enzymeEntry, Connection con) throws SQLException, DomainException {
    if (enzymeEntry == null) throw new NullPointerException();
    HistoryNode currentNode = findNode(enzymeEntry, true, con);
    if (currentNode == null)
      return null;

    return new HistoryGraph(currentNode);
  }

  /**
   * Checks whether an entry in this table exists containing the given enzyme_id.
   *
   * @param enzymeId Enzyme ID of the entry.
   * @param con      The logical connection.
   * @return a <code>HistoryEvent</code>instance or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public boolean futureEventExists(Long enzymeId, Connection con) throws SQLException {
    PreparedStatement findStatement = null;
    ResultSet rs = null;
    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setLong(2, enzymeId.longValue());
      rs = findStatement.executeQuery();
      if (rs.next()) {
        return true;
      }
    } finally {
    	if (rs != null) rs.close();
      findStatement.close();
    }

    return false;
  }


  // ---------------- PRIVATE METHODS ----------------------

  private HistoryNode findNode(EnzymeEntry currentEntry, boolean isRoot, Connection con) throws SQLException, DomainException {
    HistoryNode historyNode = new HistoryNode();
    historyNode.setEnzymeEntry(currentEntry);
    historyNode.setRoot(isRoot);

// Keep history line loaded before. Ugly code but avoids another DB query.
    historyNode.setHistoryLine(currentEntry.getHistory().getRootNode().getHistoryLine());

    if (visitedNodes.containsKey(currentEntry.getId())) {
      return (HistoryNode) visitedNodes.get(currentEntry.getId()); // Stops recursion.
    }

    visitedNodes.put(currentEntry.getId(), historyNode);

    // Find event(s)
    List futureEvents = findFutureEvents(historyNode, con);
    historyNode.setEdges(futureEvents);

    return historyNode;
  }

  private List findFutureEvents(HistoryNode currentNode, Connection con) throws SQLException, DomainException {
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
   *
   * @param rs The result set object.
   * @return an <code>EnzymeLink</code> instance.
   * @throws SQLException
   */
  private FutureEvent doLoad(ResultSet rs, HistoryNode currentNode, Connection con) throws SQLException, DomainException {
    long groupId = 0;
    long eventId = 0;
    int beforeId = 0;
    int afterId = 0;
    int enzymeId = 0;
    Date eventYear = new Date();
    Date startDate = null;
    Date dueDate = null;
    String eventNote = "";
    String eventClass = "";
    String status = "";
    int timeoutId = 0;

    if (rs.getInt(1) > 0) groupId = rs.getLong(1);
    if (rs.getInt(2) > 0) eventId = rs.getLong(2);
    if (rs.getInt(3) > 0) beforeId = rs.getInt(3);
    if (rs.getInt(4) > 0) afterId = rs.getInt(4);
    if (rs.getDate(5) != null) eventYear = rs.getDate(5);
    if (rs.getString(6) != null) eventNote = rs.getString(6);
    if (rs.getString(7) != null) eventClass = rs.getString(7);
    if (rs.getString(8) != null) status = rs.getString(8);
    if (rs.getInt(9) > 0) timeoutId = rs.getInt(9);
    if (rs.getInt(10) > 0) enzymeId = rs.getInt(10);
    if (rs.getDate(11) != null) startDate = rs.getDate(11);
    if (rs.getDate(12) != null) dueDate = rs.getDate(12);

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

    Timeout timeout = new Timeout();
    timeout.setEnzymeId(enzymeId);
    timeout.setStartDate(startDate);
    timeout.setDueDate(dueDate);
    timeout.setTimeoutId(timeoutId);

    FutureEvent futureEvent = new FutureEvent();
    futureEvent.setGroupId(new Long(groupId));
    futureEvent.setEventId(new Long(eventId));
    futureEvent.setBeforeNode(beforeNode);
    futureEvent.setAfterNode(afterNode);
    futureEvent.setDate(eventYear);
    futureEvent.setNote(eventNote);
    futureEvent.setEventClass(EventConstant.valueOf(eventClass));
    futureEvent.setStatus(Status.fromCode(status));
    futureEvent.setTimeout(timeout);

    return futureEvent;
  }

}
