package uk.ac.ebi.intenz.tools.sib.writer;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import uk.ac.ebi.intenz.db.util.NewDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

public class EnzymeFlatFileWriterTest {

    Connection con;
    EnzymeEntryMapper entryMapper;

    @Before
	public void setUp() throws Exception {
	//        only used when IDE cannot get System environment variables
//        String userHome = System.getProperty("user.home");
//        System.setProperty(
//                "oracle.net.tns_admin",
//                userHome + "/tns_admin");	
            
            con = NewDatabaseInstance.getInstance("intenz-db-dev").getConnection();
        entryMapper = new EnzymeEntryMapper();
	}

    @After
	public void tearDown() throws Exception {
		if (con != null) con.close();
	}

    @Test
	public void testTransferredNotDeleted() throws UnsupportedOperationException, SPTRException {
		EnzymeEntryImpl entry = new EnzymeEntryImpl();
		entry.setEC("1.1.1.249");
		entry.setTransferredOrDeleted(true);
		String ffEntry = EnzymeFlatFileWriter.export(entry);
		assertEquals("ID   1.1.1.249\nDE   Transferred entry: 2.5.1.46.\n//\n", ffEntry);
	}

    @Test
    public void testTroublesomeEcs() throws Exception{
        EnzymeEntry entry = entryMapper.findById(3828L, con);
        EnzymeEntryImpl sibEntry = SibEntryHelper.getSibEnzymeEntry(entry,
                SpecialCharacters.getInstance(null), EncodingType.SWISSPROT_CODE);
		String ffEntry = EnzymeFlatFileWriter.export(sibEntry);
		System.out.println(ffEntry);
//		entry = entryMapper.findById(18543L, con);
//        sibEntry = SibEntryHelper.getSibEnzymeEntry(entry,
//                SpecialCharacters.getInstance(null), EncodingType.SWISSPROT_CODE);
//		ffEntry = EnzymeFlatFileWriter.export(sibEntry);
		
		entry = entryMapper.findByEc(3, 4, 22, 55, Status.APPROVED, con);
        sibEntry = SibEntryHelper.getSibEnzymeEntry(entry,
                SpecialCharacters.getInstance(null), EncodingType.SWISSPROT_CODE);
		ffEntry = EnzymeFlatFileWriter.export(sibEntry);
	System.out.println(ffEntry);
    }
    @Test
    public void testLineWrapping() throws Exception{
    }
}
