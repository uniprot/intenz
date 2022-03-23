package uk.ac.ebi.intenz.tools.importer;

import static org.junit.Assert.assertFalse;

import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;

public class KrakenLinkImporterTest {

	private static Logger LOGGER = Logger.getLogger(KrakenLinkImporter.class);
	
	private KrakenLinkImporter importer;
	@Before
	public void setUp() throws Exception {
		importer = new KrakenLinkImporter();
		
	}

	@Test
	public void testGetKrakenLinks() {
		SortedSet<EnzymeLink> links = importer.getUniProtLinks("1.2.3.4");
		assertFalse(links.isEmpty());
		for (EnzymeLink link : links) {
			LOGGER.info(link.getName() + " - " + link.getAccession());
		}
	}

}
