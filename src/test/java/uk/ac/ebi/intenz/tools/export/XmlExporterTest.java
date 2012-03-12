package uk.ac.ebi.intenz.tools.export;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.xml.jaxb.CofactorType;
import uk.ac.ebi.intenz.xml.jaxb.ObjectFactory;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.XRef.Availability;

public class XmlExporterTest {

	private XmlExporter exporter;
	
	@Before
	public void setUp() throws Exception {
		exporter = new XmlExporter();
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
			exporter.export(entry, "9999", "9999-09-09", System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			if (con != null) con.close();
		}
	}

	@Test
	public void testExportEnzymeEntryNoLinks()
	throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        Connection con = null;
		try {
			con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
	        EnzymeEntry entry = mapper.findById(17515L, con);
			exporter.export(entry, "9999", "9999-09-09", System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			if (con != null) con.close();
		}
	}
	
	@Test
	public void testExportEnzymeEntry() throws Exception{
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        Connection con = null;
		try {
			con = OracleDatabaseInstance.getInstance("intenz-db-dev")
					.getConnection();
	        EnzymeEntry entry = mapper.findById(10396L, con);
			exporter.export(entry, "9999", "9999-09-09", System.out);
		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			if (con != null) con.close();
		}
	}

}
