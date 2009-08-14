package uk.ac.ebi.intenz.tools.release;

import uk.ac.ebi.biobabel.util.db.DatabaseInstance;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;

import java.io.IOException;
import java.sql.*;

/**
 * This class is used to populate the table <code>id2ec</code>.
 *
 * This table is a convenience table used to speed up the searching of enzymes by EC within the IntEnz web applications.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/06/06 09:41:17 $
 */
public class ID2EC {

    private static final String LIST_COLUMNS = "enzyme_id, ec1, ec2, ec3, ec4, status, source";

    private static String findAllStatement() {
        return "SELECT " + LIST_COLUMNS +
                " FROM enzyme.enzymes" +
                " WHERE ( status = ? OR status = ? )" +
                " AND enzyme_id NOT IN" +
                " ( SELECT before_id FROM enzyme.history_events WHERE event_class = ? )" +
                " ORDER BY ec1, ec2, ec3, ec4";
    }

    private static String DELETE_ALL = "DELETE FROM id2ec";

    private static String insertIDAndECStatement() {
        return "INSERT INTO id2ec (enzyme_id, ec, status, source) VALUES ( ?, ?, ?, ? )";
    }

    public static void main(String[] args) {

        if (args.length == 0){
            System.err.println("ID2EC needs one parameter");
            System.exit(1);
        }

        String instanceName = args[0];
        DatabaseInstance instance = null;
        try {
            instance = OracleDatabaseInstance.getInstance(instanceName);
        } catch (IOException e) {
            System.err.println("Missing database configuration for " + instanceName);
            e.printStackTrace();
            System.exit(2);
        }

        if (instance == null){
            System.err.println("Missing database parameter(s)");
            System.exit(3);
        }

        Connection con = null;
        System.out.println("In ID2EC method....");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@" + instance.getHost() + ":" + instance.getPort() + ":" + instanceName;
            con = DriverManager.getConnection(url, instance.getUser(), instance.getPassword());
            con.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println("Could not open connection to " + instanceName);
            e.printStackTrace();
            try {
                if (con != null) con.close();
            } catch (SQLException e1){
                e1.printStackTrace();
            }
            System.exit(4);
        }

        Statement deleteAllStatement = null;
        try {
        	System.out.print("Deleting id2ec table...");
            deleteAllStatement = con.createStatement();
            deleteAllStatement.execute(DELETE_ALL);
            System.out.println(" Deleted!");
        } catch (SQLException e) {
            System.err.println("Could not clear table id2ec on " + instanceName);
            e.printStackTrace();
            try {
                if (deleteAllStatement != null) deleteAllStatement.close();
                if (con != null) con.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            System.exit(5);
        }

        PreparedStatement getAllPRAndOKEnzymes = null, insertIntoID2EC = null;
        ResultSet rs = null;

        System.out.println("Start populating database ....");
        try {
            insertIntoID2EC = con.prepareStatement(insertIDAndECStatement());

            // Enzymes
            getAllPRAndOKEnzymes = con.prepareStatement(findAllStatement());
            getAllPRAndOKEnzymes.setString(1, "OK");
            getAllPRAndOKEnzymes.setString(2, "PR");
            getAllPRAndOKEnzymes.setString(3, "MOD");
            rs = getAllPRAndOKEnzymes.executeQuery();

            while (rs.next()) {
                String id = rs.getString(1);
                insertIntoID2EC.setString(1, id);
                insertIntoID2EC.setString(2, rs.getString("ec1") + "." + rs.getString("ec2") + "." + rs.getString("ec3") + "." + rs.getString("ec4"));
                insertIntoID2EC.setString(3, rs.getString("status"));
                insertIntoID2EC.setString(4, rs.getString("source"));
                insertIntoID2EC.executeUpdate();
                insertIntoID2EC.clearParameters();
            }

            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (getAllPRAndOKEnzymes != null) getAllPRAndOKEnzymes.close();
                if (insertIntoID2EC != null) insertIntoID2EC.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("... population successfully ended.");
    }

}
