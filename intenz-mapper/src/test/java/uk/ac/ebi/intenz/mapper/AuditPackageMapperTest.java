package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.TestCase;
import uk.ac.ebi.intenz.db.util.NewDatabaseInstance;

public class AuditPackageMapperTest extends TestCase {

    private AuditPackageMapper apm;
    Connection con;

    protected void setUp() throws Exception {
        super.setUp();
        apm = new AuditPackageMapper();
        con = NewDatabaseInstance.getInstance("intenz-db-dev")
                .getConnection();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        con.close();
    }

    public void testSetRemark() throws SQLException {
        apm.setRemark("AuditPackageMapperTest", con);
        apm.setRemark(null, con);
    }

}
