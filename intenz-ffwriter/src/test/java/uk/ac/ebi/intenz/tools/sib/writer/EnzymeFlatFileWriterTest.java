package uk.ac.ebi.intenz.tools.sib.writer;

import java.sql.Connection;

import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

public class EnzymeFlatFileWriterTest extends TestCase {

    Connection con;
    EnzymeEntryMapper entryMapper;

	protected void setUp() throws Exception {
		con = OracleDatabaseInstance.getInstance("intenz-db-dev").getConnection();
        entryMapper = new EnzymeEntryMapper();
	}

	protected void tearDown() throws Exception {
		if (con != null) con.close();
	}

	public void testTransferredNotDeleted() throws UnsupportedOperationException, SPTRException {
		EnzymeEntryImpl entry = new EnzymeEntryImpl();
		entry.setEC("1.1.1.249");
		entry.setTransferredOrDeleted(true);
		String ffEntry = EnzymeFlatFileWriter.export(entry);
		assertEquals("ID   1.1.1.249\nDE   Transferred entry: 2.5.1.46.\n//\n", ffEntry);
	}

    public void testTroublesomeEcs() throws Exception{
        EnzymeEntry entry = entryMapper.findById(3828L, con);
        EnzymeEntryImpl sibEntry = SibEntryHelper.getSibEnzymeEntry(entry,
                SpecialCharacters.getInstance(null), EncodingType.SWISSPROT_CODE);
		String ffEntry = EnzymeFlatFileWriter.export(sibEntry);

		entry = entryMapper.findById(18543L, con);
        sibEntry = SibEntryHelper.getSibEnzymeEntry(entry,
                SpecialCharacters.getInstance(null), EncodingType.SWISSPROT_CODE);
		ffEntry = EnzymeFlatFileWriter.export(sibEntry);
    }
 
    public void testLineWrapping() throws Exception{
    }
}
