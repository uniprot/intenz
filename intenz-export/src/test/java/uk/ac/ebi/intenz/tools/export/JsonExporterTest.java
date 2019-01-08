package uk.ac.ebi.intenz.tools.export;

import java.sql.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;

/**
 * @author rafa
 * @since 1.3.18
 */
public class JsonExporterTest {

    Connection con;
    JsonExporter exporter;

    @Before
    public void setUp() throws Exception {
//        //only used when IDE cannot get System environment variables
//        String userHome = System.getProperty("user.home");           
//        System.setProperty(
//                    "oracle.net.tns_admin",
//                    userHome + "/tns_admin"); 
        con = OracleDatabaseInstance.getInstance("intenz-db-dev")
                .getConnection();
        exporter = new JsonExporter();
    }

    @After
    public void tearDown() throws Exception {
        if (con != null) {
            con.close();
        }
    }

    @Test
    public void testExportEnzyme() throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        try {
            //EnzymeEntry e = mapper.findByEc("1.1.3.41", Status.APPROVED, con);
            EnzymeEntry e = mapper.findByEc("1.1.1.1", Status.APPROVED, con);
            System.out.println("************ Exporting enzyme:");
            exporter.export(e, System.out);
        } finally {
            mapper.close();
        }
    }

    @Test
    public void testExportSubSubclass() throws Exception {
        EnzymeSubSubclassMapper mapper = new EnzymeSubSubclassMapper();
        EnzymeSubSubclass ssc = mapper.find(1, 2, 3, con);
        System.out.println("************ Exporting subsubclass:");
        exporter.export(ssc, System.out, false);
    }

    @Test
    public void testExportSubclass() throws Exception {
        EnzymeSubclassMapper mapper = new EnzymeSubclassMapper();
        EnzymeSubclass sc = mapper.find("1", "2", con);
        System.out.println("************ Exporting subclass:");
        exporter.export(sc, System.out);
    }

    @Test
    public void testExportclass() throws Exception {
        EnzymeClassMapper mapper = new EnzymeClassMapper();
        EnzymeClass c = mapper.find("1", con);
        System.out.println("************ Exporting class:");
        exporter.export(c, System.out);
    }

}
