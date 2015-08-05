package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;

public class EventPackageMapperTest extends TestCase {

	private EventPackageMapper epm;
	private EnzymeEntryMapper eem;
	private Connection con;
	private Statement stm;
	private Long id1, id2;
	private EnzymeCommissionNumber ec1, ec2;
	private Status status;
	private EnzymeSourceConstant source;

	private final String NEW = "NEW";
	private final String MOD = "MOD";
	private final String TRA = "TRA";
	private final String DEL = "DEL";
	private final String SU = "SU";
	private final String PR = "PR";
	private final String OK = "OK";

	protected void setUp() throws Exception {
		super.setUp();
		epm = new EventPackageMapper();
		eem = new EnzymeEntryMapper();
		con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
		stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		id1 = new Long(-1L);
		id2 = new Long(-2L);
		ec1 = EnzymeCommissionNumber.valueOf(1,1,1,9999);
		ec2 = EnzymeCommissionNumber.valueOf(1,1,1,9998);
		status = Status.APPROVED;
		source = EnzymeSourceConstant.INTENZ;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		stm.execute("DELETE FROM future_events WHERE before_id < 0 OR after_id < 0");
		stm.execute("DELETE FROM history_events WHERE before_id < 0 OR after_id < 0");
		stm.execute("DELETE FROM enzymes WHERE enzyme_id < 0");
		con.commit();
		stm.close();
		con.close();
	}

	public void testInsertFutureCreationEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", false, con);
		epm.insertFutureCreationEvent(id1, con);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertTrue(rs.last());
			assertEquals(1, rs.getRow());
			assertEquals(NEW, rs.getString("event_class"));
			assertEquals(SU, rs.getString("status"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testUpdateFutureCreationEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", false, con);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("SELECT event_id, group_id FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			epm.insertFutureCreationEvent(id1, con);
			rs = stm.executeQuery("SELECT event_id, group_id FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			int groupId = rs.getInt("group_id");
			int eventId = rs.getInt("event_id");
			epm.updateFutureCreationEvent(groupId, eventId, PR, con);
			rs = stm.executeQuery("SELECT status FROM enzymes WHERE enzyme_id < 0");
			assertTrue(rs.next());
			assertEquals(PR, rs.getString("status"));
			epm.updateFutureCreationEvent(groupId, eventId, OK, con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			rs = stm.executeQuery("SELECT * FROM history_events WHERE after_id < 0");
			assertTrue(rs.next());
			assertEquals(id1.longValue(), rs.getLong("after_id"));
			assertEquals(NEW, rs.getString("event_class"));
			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id < 0");
			assertTrue(rs.next());
			assertEquals("Y", rs.getString("active"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testInsertFutureModificationEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			epm.insertFutureModificationEvent(id1, id2, con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			assertEquals(MOD, rs.getString("event_class"));
			assertEquals(SU, rs.getString("status"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testUpdateFutureModificationEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			epm.insertFutureModificationEvent(id1, id2, con);
			rs = stm.executeQuery("SELECT event_id, group_id FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			int groupId = rs.getInt("group_id");
			int eventId = rs.getInt("event_id");
			epm.updateFutureModificationEvent(groupId, eventId, PR, con);
			rs = stm.executeQuery("SELECT status FROM enzymes WHERE enzyme_id < 0");
			assertTrue(rs.next());
			assertEquals(PR, rs.getString("status"));
			epm.updateFutureModificationEvent(groupId, eventId, OK, con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			// The new one:
			rs = stm.executeQuery("SELECT * FROM history_events WHERE before_id IS NULL AND after_id = " + id2);
			assertTrue(rs.next());
			assertEquals(NEW, rs.getString("event_class"));
			// The obsolete one:
			rs = stm.executeQuery("SELECT * FROM history_events WHERE before_id = " + id1 + " AND after_id = " + id2);
			assertTrue(rs.next());
			assertEquals(MOD, rs.getString("event_class"));

			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id1);
			assertTrue(rs.next());
			assertEquals("N", rs.getString("active"));
			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id2);
			assertTrue(rs.next());
			assertEquals("Y", rs.getString("active"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testInsertFutureTransferEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			epm.insertFutureTransferEvent(id1, id2, "test transfer", con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			assertEquals(TRA, rs.getString("event_class"));
			assertEquals(SU, rs.getString("status"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testUpdateFutureTransferEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			epm.insertFutureTransferEvent(id1, id2, "test transfer", con);
			rs = stm.executeQuery("SELECT event_id, group_id FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			int groupId = rs.getInt("group_id");
			int eventId = rs.getInt("event_id");
			epm.updateFutureTransferEvent(groupId, eventId, "just more testing", PR, "well deserved deletion", con);
			rs = stm.executeQuery("SELECT status FROM enzymes WHERE enzyme_id < 0");
			assertTrue(rs.next());
			assertEquals(PR, rs.getString("status"));

			epm.updateFutureTransferEvent(groupId, eventId, "just finishing", OK, "deleted :)", con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			// The new one:
			rs = stm.executeQuery("SELECT * FROM history_events WHERE before_id IS NULL AND after_id = " + id2);
			assertTrue(rs.next());
			assertEquals(NEW, rs.getString("event_class"));
			// The obsolete one:
			rs = stm.executeQuery("SELECT * FROM history_events WHERE before_id = " + id1 + " AND after_id = " + id2);
			assertTrue(rs.next());
			assertEquals(TRA, rs.getString("event_class"));

			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id1);
			assertTrue(rs.next());
			assertEquals("N", rs.getString("active"));
			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id2);
			assertTrue(rs.next());
			assertEquals("Y", rs.getString("active"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testInsertFutureDeletionEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());
			epm.insertFutureDeletionEvent(id1, id2, "deleting...", con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			assertEquals(DEL, rs.getString("event_class"));
			assertEquals(SU, rs.getString("status"));
		} finally {
			if (rs != null) rs.close();
		}
	}

	public void testUpdateFutureDeletionEvent() throws SQLException {
		eem.insert(id1, ec1, status, source, "enzyme1", "just testing", true, con);
		eem.insert(id2, ec2, status, source, "enzyme2", "just testing", false, con);
		ResultSet rs = null;
		try {
			epm.insertFutureDeletionEvent(id1, id2, "deleting...", con);
			rs = stm.executeQuery("SELECT event_id, group_id FROM future_events WHERE after_id < 0");
			assertTrue(rs.next());
			int groupId = rs.getInt("group_id");
			int eventId = rs.getInt("event_id");
			epm.updateFutureDeletionEvent(groupId, eventId, "deleting!!", PR, con);
			rs = stm.executeQuery("SELECT status FROM enzymes WHERE enzyme_id < 0");
			assertTrue(rs.next());
			assertEquals(PR, rs.getString("status"));

			epm.updateFutureDeletionEvent(groupId, eventId, "DELETED!!!", OK, con);
			rs = stm.executeQuery("SELECT * FROM future_events WHERE after_id < 0");
			assertFalse(rs.next());

			rs = stm.executeQuery("SELECT * FROM history_events WHERE after_id IS NULL AND before_id = " + id1);
			assertTrue(rs.next());
			assertEquals(DEL, rs.getString("event_class"));

			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id1);
			assertTrue(rs.next());
			assertEquals("N", rs.getString("active"));
			rs = stm.executeQuery("SELECT active FROM enzymes WHERE enzyme_id = " + id2);
			assertFalse(rs.next());
		} finally {
			if (rs != null) rs.close();
		}
	}

}
