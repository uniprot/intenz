/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;

/**
 *
 * @author rafalcan
 */
public class EnzymeHistoryMapperTest extends TestCase {

    Connection con;
    EnzymeEntryMapper entryMapper;
    EnzymeHistoryMapper historyMapper;

    public EnzymeHistoryMapperTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
        entryMapper = new EnzymeEntryMapper();
        historyMapper = new EnzymeHistoryMapper();
    }

    @Override
    protected void tearDown() throws Exception {
        if (con != null) con.close();
    }

    /**
     * Test of find method, of class EnzymeHistoryMapper.
     * @throws Exception
     */
    public void testFind() throws Exception {
        EnzymeEntry enzymeEntry = entryMapper.findById(1000L, con);
        HistoryGraph graph = historyMapper.find(enzymeEntry, con);
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("1961-10-01");
        assertEquals(expected, graph.getEdges().first().getDate());
    }

}
