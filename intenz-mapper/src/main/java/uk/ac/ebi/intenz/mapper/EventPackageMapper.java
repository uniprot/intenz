package uk.ac.ebi.intenz.mapper;

import java.sql.*;
import java.util.GregorianCalendar;

/**
 * Provides methods for event package access.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 * @deprecated use {@link HistoryEventMapper} instead.
 */
public class EventPackageMapper {

  public EventPackageMapper() {
  }

  private String callInsertFutureCreationStatement() {
    return "{CALL event.p_insert_future_creation(?, ?)}";
  }

  private String callUpdateFutureCreationStatement() {
    return "{CALL event.p_update_future_creation(?, ?, ?)}";
  }

  private String callInsertFutureTransferStatement() {
    return "{CALL event.p_insert_future_transfer(?, ?, ?, ?, ?)}";
  }

  private String callUpdateFutureTransferStatement() {
    return "{CALL event.p_update_future_transfer(?, ?, ?, ?, ?)}";
  }

  private String callInsertFutureDeletionStatement() {
    return "{CALL event.p_insert_future_deletion(?, ?, ?, ?, ?)}";
  }

  private String callUpdateFutureDeletionStatement() {
    return "{CALL event.p_update_future_deletion(?, ?, ?, ?)}";
  }

  private String callInsertFutureModificationStatement() {
    return "{CALL event.p_insert_future_modification(?, ?, ?)}";
  }

  private String callUpdateFutureModificationStatement() {
    return "{CALL event.p_update_future_modification(?, ?, ?)}";
  }

  /**
   * Inserts a new enzyme creation event into the <code>FUTURE_EVENTS</code> table.
   *
   * @param enzymeId The enzyme ID.
   * @param con      The connection.
   * @throws SQLException
   */
  public void insertFutureCreationEvent(Long enzymeId, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callInsertFutureCreationStatement());
      cStmt.setLong(1, enzymeId.longValue());
      cStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Updates a new enzyme creation event in the <code>FUTURE_EVENTS</code> table.
   *
   * @param groupId   The enzyme ID of the original enzyme.
   * @param eventId   The enzyme ID of the cloned enzyme.
   * @param newStatus ...
   * @param con       ...
   * @throws SQLException
   */
  public void updateFutureCreationEvent(int groupId, int eventId, String newStatus, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callUpdateFutureCreationStatement());
      cStmt.setInt(1, groupId);
      cStmt.setInt(2, eventId);
      cStmt.setString(3, newStatus);
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Inserts a new enzyme creation event into the <code>FUTURE_EVENTS</code> table.
   *
   * @param beforeId The enzyme ID of the original enzyme.
   * @param afterId  The enzyme ID of the cloned enzyme.
   * @param con      The connection.
   * @throws SQLException
   */
  public void insertFutureModificationEvent(Long beforeId, Long afterId, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callInsertFutureModificationStatement());
      cStmt.setLong(1, beforeId.longValue());
      cStmt.setLong(2, afterId.longValue());
      cStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Updates a new enzyme creation event in the <code>FUTURE_EVENTS</code> table.
   *
   * @param groupId   The event group ID.
   * @param eventId   The event ID.
   * @param newStatus ...
   * @param con       ...
   * @throws SQLException
   */
  public void updateFutureModificationEvent(int groupId, int eventId, String newStatus, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callUpdateFutureModificationStatement());
      cStmt.setInt(1, groupId);
      cStmt.setInt(2, eventId);
      cStmt.setString(3, newStatus);
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Inserts a new transfer event into the <code>FUTURE_EVENTS</code> table.
   *
   * @param beforeId The enzyme ID of the original enzyme.
   * @param afterId  The enzyme ID of the cloned enzyme.
   * @param comment  History comment added to this transfer event.
   * @param con      The connection.
   * @throws SQLException
   */
  public void insertFutureTransferEvent(Long beforeId, Long afterId, String comment, Connection con) throws SQLException {
    CallableStatement cStmt = null;
    GregorianCalendar gc = new GregorianCalendar();
    Date date = new Date(gc.getTimeInMillis());

    try {
      cStmt = con.prepareCall(callInsertFutureTransferStatement());
      cStmt.setLong(1, beforeId.longValue());
      cStmt.setLong(2, afterId.longValue());
      cStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      cStmt.setString(4, comment);
      cStmt.setString(5, "SU");
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Updates a transferred enzyme (target and origin) event in the <code>FUTURE_EVENTS</code> table.
   *
   * @param groupId           The enzyme ID of the original enzyme.
   * @param eventId           The enzyme ID of the cloned enzyme.
   * @param newHistoryComment ...
   * @param newStatus         ...
   * @param con               ...
   * @throws SQLException
   */
  public void updateFutureTransferEvent(int groupId, int eventId, String newHistoryComment, String newStatus, String historyLineOfDeletedEntry, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callUpdateFutureTransferStatement());
      cStmt.setInt(1, groupId);
      cStmt.setInt(2, eventId);
      cStmt.setString(3, newStatus);
      cStmt.setString(4, newHistoryComment);
      cStmt.setString(5, historyLineOfDeletedEntry);
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Inserts a new deletion event into the <code>FUTURE_EVENTS</code> table.
   *
   * @param beforeId The enzyme ID of the original enzyme.
   * @param afterId  The enzyme ID of the cloned enzyme.
   * @param comment  History comment added to this deletion event.
   * @param con      The connection.
   * @throws SQLException
   */
  public void insertFutureDeletionEvent(Long beforeId, Long afterId, String comment, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callInsertFutureDeletionStatement());
      cStmt.setLong(1, beforeId.longValue());
      cStmt.setLong(2, afterId.longValue());
      cStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
      cStmt.setString(4, comment);
      cStmt.setString(5, "SU");
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }

  /**
   * Updates a future deletion event.
   *
   * @param groupId   ...
   * @param eventId   ...
   * @param comment   ...
   * @param newStatus The new status of the enzyme.
   * @param con       The connection.
   * @throws SQLException
   */
  public void updateFutureDeletionEvent(int groupId, int eventId, String comment, String newStatus, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callUpdateFutureDeletionStatement());
      cStmt.setInt(1, groupId);
      cStmt.setInt(2, eventId);
      cStmt.setString(3, newStatus);
      cStmt.setString(4, comment);
      cStmt.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      cStmt.close();
    }
  }
}
