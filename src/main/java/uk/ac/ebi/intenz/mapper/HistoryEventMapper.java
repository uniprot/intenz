package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import uk.ac.ebi.intenz.domain.constants.Event;
import uk.ac.ebi.intenz.domain.constants.Status;

public class HistoryEventMapper {

	private static final String INSERT_EVENT_SQL =
		"INSERT INTO history_events" +
		" (event_id, group_id, event_class, before_id, after_id, event_note, event_year)" +
		" VALUES (s_history_event_id.nextval, s_history_group_id.nextval, ?, ?, ?, ?, ?)";
	
	//TODO
	private static final String SELECT_EVENT_SQL =
		"SELECT group_id, event_id, before_id, after_id, event_year," +
		" event_note, event_class FROM history_events" +
		" WHERE before_id = ? OR after_id = ?";

    public void insertEvent(Event event, Long beforeId, Long afterId,
			String note, Status status, Connection con) throws SQLException{
    	PreparedStatement stm = null;
    	try {
	    	stm = con.prepareStatement(INSERT_EVENT_SQL);
	    	stm.setString(1, event.getCode());
			switch (event) {
			case CREATION:
				stm.setNull(2, Types.NUMERIC);
				stm.setLong(3, afterId);
				break;
			case TRANSFER:
				stm.setLong(2, beforeId);
				stm.setLong(3, afterId);
				break;
			case DELETION:
				stm.setLong(2, beforeId);
				stm.setNull(3, Types.NUMERIC);
				break;
			default:
				throw new IllegalArgumentException("Event not supported: " + event);
			}
			if (note == null){
				stm.setNull(4, Types.VARCHAR);
			} else {
				stm.setString(4, note);
			}
			stm.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			stm.executeUpdate();
    	} finally {
    		if (stm != null) stm.close();
    	}
	}
}
