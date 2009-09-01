package uk.ac.ebi.intenz.tools.sib.writer;

import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import junit.framework.TestCase;

public class EnzymeFlatFileWriterTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTransferredNotDeleted() throws UnsupportedOperationException, SPTRException {
		EnzymeEntryImpl entry = new EnzymeEntryImpl();
		entry.setEC("1.1.1.249");
		entry.setTransferredOrDeleted(true);
		String ffEntry = EnzymeFlatFileWriter.export(entry);
		assertEquals("ID   1.1.1.249\nDE   Transferred entry: 2.5.1.46.\n//\n", ffEntry);
	}

}
