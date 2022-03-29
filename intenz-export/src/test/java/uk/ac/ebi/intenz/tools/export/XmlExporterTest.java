package uk.ac.ebi.intenz.tools.export;

import java.io.IOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.intenz.db.util.NewDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;

import static org.junit.Assert.fail;

public class XmlExporterTest {

	private XmlExporter exporter;
	private Connection con;
	private EnzymeEntryMapper mapper;
	private final Logger LOGGER = Logger.getLogger(XmlExporterTest.class);
	
	@Before
	public void setUp() throws Exception {
		exporter = new XmlExporter();
		exporter.setReleaseDate("9999-09-09");
		exporter.setReleaseNumber(9999);
		con = NewDatabaseInstance.getInstance("intenz-db-dev")
				.getConnection();
		mapper = new EnzymeEntryMapper();
	}

	@After
	public void tearDown() throws Exception {
		if (con != null) con.close();
	}
	@Ignore
	@Test
	public void testExportEnzymeEntryStringStringOutputStream()
	throws Exception {
		try {
	        EnzymeEntry entry = mapper.findById(1000L, con);
			exporter.export(entry, System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testExportEnzymeEntryNoLinks()
	throws Exception {
		try {
	        EnzymeEntry entry = mapper.findById(17515L, con);
			exporter.export(entry, System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	@Ignore
	@Test
	public void testExportEnzymeEntry() throws Exception{
		try {
	        EnzymeEntry entry = mapper.findById(10396L, con);
			exporter.export(entry, System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		try {
	        EnzymeEntry entry =
	        		mapper.findByEc("1.1.3.41", Status.APPROVED, con);
			exporter.export(entry, System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		try {
	        EnzymeEntry entry =
	        		mapper.findByEc("1.1.1.100", Status.APPROVED, con);
			exporter.export(entry, System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
