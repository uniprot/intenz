package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;

/**
 * Created by IntelliJ IDEA.
 * User: rafalcan
 * Date: 10-Oct-2005
 * Time: 11:36:12
 * To change this template use File | Settings | File Templates.
 */
public class EnzymeLinkMapperTest extends TestCase {

    private static Connection con;
    private static EnzymeLinkMapper elm = new EnzymeLinkMapper();

    // NOTE: For these tests to work, the database must contain an entry with this id:
    private static Long id = new Long(16059l);
    
    public void testFind() throws Exception {
        Long id1002 = new Long(1002l);
        List<?> entries = elm.find(id1002, con);
        assertFalse(entries == null || entries.isEmpty());
        entries = elm.findSibXrefs(id1002, con);
        assertFalse(entries == null || entries.isEmpty());
        entries = elm.findXrefs(id1002, con);
        assertFalse(entries == null || entries.isEmpty());
        entries = elm.exportSibLinks(id1002, con);
        assertFalse(entries == null || entries.isEmpty());
    }

    public void testInsert() throws Exception {
        elm.insert(getSampleLinks(), id, Status.SUGGESTED, con);
        List<?> entries = elm.find(id, con);
        assertTrue(entries != null);
        assertTrue(entries.size() == 2);
        entries = elm.findXrefs(id, con);
        assertTrue(entries != null);
        assertTrue(entries.size() == 1);
    }

    /* This test relies on the previous one being successful! */
    public void testDelete() throws Exception {
        elm.deleteAllXref(id, con);
        List<?> entries = elm.find(id, con);
        assertTrue(entries != null);
        assertFalse(entries.isEmpty()); // still some links
        elm.deleteAll(id, con);
        entries = elm.find(id, con);
        assertTrue(entries == null || entries.isEmpty());
    }

    private List<EnzymeLink> getSampleLinks() {
        List<EnzymeLink> sampleLinks = new ArrayList<EnzymeLink>();
        // One non-xref:
        EnzymeLink sl1 = EnzymeLink.valueOf(XrefDatabaseConstant.KEGG, "http://www.google.com", "G666",
            "TestLinkG", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);
        sl1.setDataComment("a google kegg comment 4321");
        // One xref:
        EnzymeLink sl2 = EnzymeLink.valueOf(XrefDatabaseConstant.SWISSPROT, "http://www.yahoo.com", "Y666",
            "TestLinkY", EnzymeSourceConstant.IUBMB, EnzymeViewConstant.INTENZ);
        sl2.setDataComment("a yahoo swissprot comment 9876");
        sampleLinks.add(sl1);
        sampleLinks.add(sl2);
        return sampleLinks;
    }

    protected void setUp() throws Exception {
        super.setUp();
        con = OracleDatabaseInstance.getInstance("intenz-db-dev")
            .getConnection();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (con != null) con.close();
    }

}
