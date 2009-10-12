package uk.ac.ebi.intenz.tools.export;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;

public class XmlExporterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExportEnzymeEntryStringStringOutputStream()
	throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        Connection con = null;
		try {
			con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
	        EnzymeEntry entry = mapper.findById(1000L, con);
			new XmlExporter().export(entry, "9999", "9999-09-09", System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			if (con != null) con.close();
		}
	}

}
