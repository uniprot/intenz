/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.intenz.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.intenz.db.util.NewDatabaseInstance;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;

/**
 *
 * @author rafalcan
 */
public class EnzymeHistoryMapperTest {

    Connection con;
    EnzymeEntryMapper entryMapper;
    EnzymeHistoryMapper historyMapper;


    @Before
    public void setUp() throws Exception {
        con = NewDatabaseInstance.getInstance("intenz-db-dev").getConnection();
        entryMapper = new EnzymeEntryMapper();
        historyMapper = new EnzymeHistoryMapper();
    }

    @After
    public void tearDown() throws Exception {
        if (con != null) con.close();
    }

    /**
     * Test of find method, of class EnzymeHistoryMapper.
     * @throws Exception
     */
    @Ignore
    @Test
    public void testFind() throws Exception {
        EnzymeEntry enzymeEntry = entryMapper.findById(1000L, con);
        HistoryGraph graph = historyMapper.find(enzymeEntry, con);
        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("1961-10-01");
        assertEquals(expected, graph.getEdges().first().getDate());
    }

}
