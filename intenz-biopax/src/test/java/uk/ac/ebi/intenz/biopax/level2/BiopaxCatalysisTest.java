package uk.ac.ebi.intenz.biopax.level2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.biopax.paxtools.model.Model;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;

/**
 *
 * @author rafalcan
 */
public class BiopaxCatalysisTest {

    private EnzymeEntryMapper mapper;
    private Connection con;
    private Model model;

    public BiopaxCatalysisTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IOException {
        mapper = new EnzymeEntryMapper();
        con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
        model = Biopax.createModel();
    }

    @After
    public void tearDown() throws SQLException {
        if (con != null) con.close();
    }

    /**
     * Just a visual test (check the output).
     */
    @Test
    public void testWrite() throws Exception {
        // Old plain text reaction (no public rheaction yet), lots of xrefs:
        new BiopaxCatalysis(mapper.findByEc(1, 1, 1, 1, null, con), model);
        // Abstract reaction:
        new BiopaxCatalysis(mapper.findByEc(3, 2, 1, 97, null, con), model);
        // One rheaction:
        new BiopaxCatalysis(mapper.findByEc(1, 2, 3, 4, null, con), model);
        // One complex rheaction:
        new BiopaxCatalysis(mapper.findByEc(1, 14, 13, 39, null, con), model);
        // Several rheactions:
        new BiopaxCatalysis(mapper.findByEc(1, 3, 99, 17, null, con), model);
        Biopax.write(model, System.out);
    }

}
