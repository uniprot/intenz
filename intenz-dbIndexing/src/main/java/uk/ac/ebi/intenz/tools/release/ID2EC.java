package uk.ac.ebi.intenz.tools.release;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.db.util.NewDatabaseInstance;

/**
 * This class is used to populate the table <code>id2ec</code>.
 *
 * This table is a convenience table used to speed up the searching of enzymes by EC within the IntEnz web applications.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/06/06 09:41:17 $
 */
public class ID2EC {

	private static final Logger LOGGER = Logger.getLogger(ID2EC.class);
	
    private static String findAllStatement() {
        return new StringBuilder("SELECT enzyme_id, ec1, ec2, ec3, ec4, status, source")
        	.append(" FROM enzyme.enzymes WHERE status IN ('OK','PR','PM')")
            .append(" AND enzyme_id NOT IN")
            .append(" (SELECT before_id FROM enzyme.history_events")
            .append(" WHERE event_class = 'MOD')")
            .append(" ORDER BY ec1, ec2, ec3, ec4")
            .toString();
    }

    private static String DELETE_ALL = "DELETE FROM id2ec";

    private static String insertIDAndECStatement() {
        return "INSERT INTO id2ec (enzyme_id, ec, status, source) VALUES ( ?, ?, ?, ? )";
    }

    /**
     * Re-generates the ID2EC table in the database, which maps EC number of
     * public entries to their internal IDs.
     * @param args
     * 		<ol><li>database instance name</li></ol>
     */
    public static void main(String[] args) {

        if (args.length == 0){
            LOGGER.error("ID2EC needs one parameter (DB instance name)");
            System.exit(1);
        }

        String instanceName = args[0];
        NewDatabaseInstance instance = null;
        try {
            instance = NewDatabaseInstance.getInstance(instanceName);
        } catch (IOException e) {
            LOGGER.error("Missing database configuration for " + instanceName, e);
            System.exit(2);
        }

        if (instance == null){
            LOGGER.error("Missing database parameter(s)");
            System.exit(3);
        }

        Connection con = instance.getConnection();
        if (con == null){
            LOGGER.error("Could not open connection to " + instance.getInstance());
            System.exit(4);
        }

        Statement deleteAllStatement = null;
        try {
        	LOGGER.info("Deleting ID2EC table...");
            deleteAllStatement = con.createStatement();
            deleteAllStatement.execute(DELETE_ALL);
            LOGGER.info("... Deleted!");
        } catch (SQLException e) {
            LOGGER.error("Could not clear table ID2EC on " + instanceName, e);
            try {
                if (deleteAllStatement != null) deleteAllStatement.close();
                if (con != null) con.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            System.exit(5);
        }

        PreparedStatement getAllPublicEnzymes = null, insertIntoID2EC = null;
        ResultSet rs = null;

        LOGGER.info("Starting ID2EC index...");
        try {
            insertIntoID2EC = con.prepareStatement(insertIDAndECStatement());

            // Enzymes
            getAllPublicEnzymes = con.prepareStatement(findAllStatement());
            rs = getAllPublicEnzymes.executeQuery();

            while (rs.next()) {
            	final String id = rs.getString(1);
                final String status = rs.getString("status");
                final String ec = new StringBuilder(rs.getString("ec1"))
                	.append('.').append(rs.getString("ec2"))
                	.append('.').append(rs.getString("ec3"))
                	.append('.').append(status.equals("PM")? "n" : "")
                	.append(rs.getString("ec4"))
                	.toString();
                final String source = rs.getString("source");
                insertIntoID2EC.setString(1, id);
				insertIntoID2EC.setString(2, ec);
                insertIntoID2EC.setString(3, status);
                insertIntoID2EC.setString(4, source);
                insertIntoID2EC.executeUpdate();
                insertIntoID2EC.clearParameters();
            }

          
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (getAllPublicEnzymes != null) getAllPublicEnzymes.close();
                if (insertIntoID2EC != null) insertIntoID2EC.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info("Indexing finished!");
    }

}
