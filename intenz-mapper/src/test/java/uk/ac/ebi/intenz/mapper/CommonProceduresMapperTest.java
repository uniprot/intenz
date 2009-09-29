package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;

public class CommonProceduresMapperTest extends TestCase {

	private CommonProceduresMapper cpm;
	private EnzymeEntryMapper eem;
	private EnzymeNameMapper enm;
	private EnzymeReactionMapper erm;
	private EnzymeCommentMapper ecm;
	private EnzymeLinkMapper elm;
	private EnzymeReferenceMapper erefm;
	private Long fakeId, cloneId;
	private EnzymeCommissionNumber fakeEc;
	private Status fakeStatus;
	private EnzymeSourceConstant fakeSource;
	private EnzymeViewConstant fakeView;
	private Long fakePubId;
	private Connection con;

	private final String NOTE = "Delete me";
	private final String HISTORY = "Created 2B deleted";
	private final String SYS_NAME = "nil-ase";
	private final String COM_NAME = "nothing-ase";
	private final String REACTION = "nil = nil";
	private final String COMMENT = "nothing to declare.";
	private final String AUTHOR = "Fulano, X.";
	private final String TITLE = "Nothing to say";
	private final String YEAR = "1666";
	private final String PUB_NAME = "Journal of Emptyness";
	private final String FIRST_PAGE = "1";
	private final String LAST_PAGE = "666";
	private final String VOLUME = "1";
	private final String PUBMED = "";
	private final String MEDLINE = "";

    @Override
	protected void setUp() throws Exception {
		super.setUp();
		cpm = new CommonProceduresMapper();
		eem = new EnzymeEntryMapper();
		enm = new EnzymeNameMapper();
		erm = new EnzymeReactionMapper();
		ecm = new EnzymeCommentMapper();
		elm = new EnzymeLinkMapper();
		erefm = new EnzymeReferenceMapper();
		fakeId = new Long(-1L);
		fakeEc = EnzymeCommissionNumber.valueOf(1,1,1,9999);
		fakeStatus = Status.APPROVED;
		fakeSource = EnzymeSourceConstant.INTENZ;
		fakeView = EnzymeViewConstant.INTENZ;
		fakePubId = new Long(-666);
		con = OracleDatabaseInstance.getInstance("intenz-db-dev")
            .getConnection();

		eem.insert(fakeId, fakeEc, fakeStatus, fakeSource, NOTE, HISTORY, true, con);
		enm.insert(EnzymeName.getSystematicNameInstance(SYS_NAME, fakeSource, fakeView),
				fakeId, fakeStatus, 1, con);
		enm.insert(EnzymeName.getCommonNameInstance(COM_NAME, fakeSource, fakeView),
				fakeId, fakeStatus, 1, con);
		EnzymaticReactions reactions = new EnzymaticReactions();
		// Abstract reaction, without ID:
		reactions.add(new Reaction(REACTION, Database.valueOf(fakeSource.toString())), fakeView.toString());
		erm.insert(reactions, fakeId, con);
		List<EnzymeComment> comments = new ArrayList<EnzymeComment>();
		comments.add(new EnzymeComment(COMMENT, fakeSource, fakeView));
		ecm.insert(comments, fakeId, fakeStatus, con);
		elm.insertLink(EnzymeLink.UMBBD, fakeId, fakeStatus, con);
		erefm.insert(new Journal(fakePubId, AUTHOR, TITLE, YEAR, PUB_NAME, FIRST_PAGE, LAST_PAGE, VOLUME, PUBMED, MEDLINE, fakeView, fakeSource),
				fakeId, fakeStatus, con);
		con.commit();
	}

    @Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// Clone:
		enm.deleteNames(cloneId, EnzymeNameTypeConstant.SYSTEMATIC_NAME, con);
		enm.deleteNames(cloneId, EnzymeNameTypeConstant.COMMON_NAME, con);
		erm.deleteAll(cloneId, con);
		ecm.deleteAll(cloneId, con);
		elm.deleteAll(cloneId, con);
		erefm.deleteAll(cloneId, con);
		// Original:
		enm.deleteNames(fakeId, EnzymeNameTypeConstant.SYSTEMATIC_NAME, con);
		enm.deleteNames(fakeId, EnzymeNameTypeConstant.COMMON_NAME, con);
		erm.deleteAll(fakeId, con);
		ecm.deleteAll(fakeId, con);
		elm.deleteAll(fakeId, con);
		erefm.deleteAll(fakeId, con);

		erefm.deletePublication(fakePubId, con);

		// Delete the test enzymes (original and clone)
		PreparedStatement stm = con.prepareStatement("DELETE FROM enzymes WHERE enzyme_id = ?");
		stm.setLong(1, cloneId.longValue());
		stm.execute();
		stm.setLong(1, fakeId.longValue());
		stm.execute();
		stm.close();

		con.commit();
		con.close();
	}

	public void testCreateClone() throws SQLException, DomainException {
		cloneId = cpm.createClone(fakeId, con);
		con.commit();
		// Names:
		List<EnzymeName> cloneNames = enm.find(cloneId, con);
		assertNotNull(cloneNames);
		assertEquals(2, cloneNames.size());
		List<EnzymeName> cloneCommonNames = enm.findCommonNames(cloneId, con);
		assertNotNull(cloneCommonNames);
		assertEquals(1, cloneCommonNames.size());
		assertEquals(COM_NAME, cloneCommonNames.get(0).getName());
		EnzymeName cloneSysName = enm.findSystematicName(cloneId, con);
		assertNotNull(cloneSysName);
		assertEquals(SYS_NAME, cloneSysName.getName());
		// Reaction:
		EnzymaticReactions cloneReactions = erm.find(cloneId, con);
		assertNotNull(cloneReactions);
		assertEquals(1, cloneReactions.size());
		assertEquals(REACTION, cloneReactions.getReaction(0).getTextualRepresentation());
		// Comment:
		List<EnzymeComment> cloneComments = ecm.find(cloneId, con);
		assertNotNull(cloneComments);
		assertEquals(1, cloneComments.size());
		assertEquals(COMMENT, cloneComments.get(0).getCommentText());
		// Link:
		List<EnzymeLink> cloneLinks = elm.find(cloneId, con);
		assertNotNull(cloneLinks);
		assertEquals(1, cloneLinks.size());
		assertEquals(XrefDatabaseConstant.UMBBD, cloneLinks.get(0).getXrefDatabaseConstant());
		// Refs:
		List<Reference> cloneRefs = erefm.find(cloneId, con);
		assertNotNull(cloneRefs);
		assertEquals(1, cloneRefs.size());
		assertEquals(AUTHOR, cloneRefs.get(0).getAuthors());

	}

}
