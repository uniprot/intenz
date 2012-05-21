package uk.ac.ebi.intenz.webapp.utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.mapper.EnzymeCofactorMapper;
import uk.ac.ebi.intenz.mapper.EnzymeCommentMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeLinkMapper;
import uk.ac.ebi.intenz.mapper.EnzymeNameMapper;
import uk.ac.ebi.intenz.mapper.EnzymeReactionMapper;
import uk.ac.ebi.intenz.mapper.EnzymeReferenceMapper;
import uk.ac.ebi.intenz.webapp.dtos.CofactorDTO;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;
import uk.ac.ebi.intenz.webapp.exceptions.CommitException;
import uk.ac.ebi.intenz.webapp.exceptions.DeregisterException;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;

/**
 * This is a simple <i>UnitOfWork</i> implementation to ease database updates.
 * <p/>
 * Before a requested enzyme is loaded into the session a copy of this instance is made by the
 * {@link #register(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO)} method and kept in memory.
 * When the curator finished and pressed the 'submit' button, the enzyme instance modified
 * by the curator will be compared to the copy created in the beginning. Usually only data that changed will then be
 * stored (removed) in (from) the database (links are handled differently).<br/>
 * For data stored in a list the comparison works as follows:
 * <ol>
 * <li>Data that has been removed from the list of the enzyme under development will also be removed from the copy's list.</li>
 * <li>As long as data is available in the relevant list of the copy, data with the same list index will be compared,
 * and in case of a difference the database will be updated.</li>
 * <li>Finally the database will be populated with new data if the list of the enzyme under development still contains
 * items.</li>
 * </ol>
 *
 * @author Michael Darsow
 * @version $Revision: 1.6 $ $Date: 2008/04/23 14:20:13 $
 */
public class UnitOfWork implements HttpSessionBindingListener {

	/**
	 * This class member is used to give registered objects a unique Unit of Work ID.
	 * Both, the original object and the copy will share this ID. This allows to retreive the correct copy in the
	 * {@link UnitOfWork#commit(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO, java.sql.Connection)} phase.
	 */
	public static int UOW_ID = 0;

	/**
	 * The logger instance is currently only used to provide some information during the
	 * {@link UnitOfWork#register(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO)} and
	 * {@link UnitOfWork#commit(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO, java.sql.Connection)} phases.
	 */
	private static final Logger LOGGER = Logger.getLogger(UnitOfWork.class.getName());

	/**
	 * The EnzymeDTO instance the curator is working on.
	 */
	private Map enzymesUnderDevelopment;
	
	EnzymeReactionMapper enzymeReactionMapper;

	EnzymeEntryMapper enzymeEntryMapper;

	/**
	 * Initialises <code>enzymesUnderDevelopment</code>.
	 * @deprecated 
	 */
	public UnitOfWork() {
		this(new EnzymeEntryMapper(), new EnzymeReactionMapper());
	}
	
	public UnitOfWork(EnzymeReactionMapper enzymeReactionMapper){
		this(new EnzymeEntryMapper(enzymeReactionMapper), enzymeReactionMapper);
	}
	
	public UnitOfWork(EnzymeEntryMapper enzymeEntryMapper,
			EnzymeReactionMapper enzymeReactionMapper){
		this.enzymeEntryMapper = enzymeEntryMapper;
		this.enzymeReactionMapper = enzymeReactionMapper;
		enzymesUnderDevelopment = new HashMap();
	}
	
	@Override
	protected void finalize() throws Throwable {
		enzymeEntryMapper.close();
		enzymeReactionMapper.close();
	}

	/**
	 * Creates and stores a copy of the given {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO} instance.
	 *
	 * @param enzymeUnderDevelopment The current enzyme entry instance.
	 */
	public void register(EnzymeDTO enzymeUnderDevelopment) {
		if (enzymeUnderDevelopment == null) throw new NullPointerException("Parameter 'enzymeUnderDevelopment' must not be null.");
		final Integer uowId = new Integer(UOW_ID);
		final EnzymeDTO enzymeCopy = new EnzymeDTO(enzymeUnderDevelopment);
		enzymesUnderDevelopment.put(uowId, enzymeCopy);
		enzymeUnderDevelopment.setUowId(uowId);
		UOW_ID++;
		LOGGER.info("EC " + enzymeUnderDevelopment.getEc() + " has been registered in the UoW.");
	}

	/**
	 * Compares the given enzyme entry to the copy stored in {@link #register(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO)}.
	 * <p/>
	 * Changes will be transferred to the database.
	 * <p/>
	 * Data which is stored in lists will simply be reloaded at the moment.
	 * <p/>
	 * If a new enzyme has been submitted this method will insert the entry into the database.
	 *
	 * @param enzymeUnderDevelopment The enzyme the curator has worked on.
	 * @param con                    A database connection used to perform the changes in the database.
	 * @throws EcException         if an invalid EC number has been used.
	 * @throws SQLException        if a database error occurs.
	 * @throws DomainException     if a domain error occurs.
	 * @throws CommitException if there is some data integrity threat
	 * @throws NumberFormatException 
	 * @throws DeregisterException if an object does not exist in {@link UnitOfWork#enzymesUnderDevelopment}
	 *                             (list of registered objects).
	 */
	public void commit(EnzymeDTO enzymeUnderDevelopment, Connection con)
	throws EcException, SQLException, DomainException, NumberFormatException, CommitException {
		if (enzymeUnderDevelopment == null) throw new NullPointerException("Parameter 'enzymeUnderDevelopment' must not be null.");
		if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

		LOGGER.info("EC " + enzymeUnderDevelopment.getEc() + " is being committed ...");

		// Insert new enzymes.
		if (enzymeUnderDevelopment.getId() == null || enzymeUnderDevelopment.getId().equals("")) {
			insertEnzymeEntry(enzymeUnderDevelopment, con);
			LOGGER.info("... commit completed.");
			return;
		}

		EnzymeDTO copy = (EnzymeDTO) enzymesUnderDevelopment.remove(enzymeUnderDevelopment.getUowId()); // also remove instance from the list of registered objects
		if (copy == null) throw new DeregisterException(ActionMessages.GLOBAL_MESSAGE, "errors.application.uow.deregister");

		compareCoreData(enzymeUnderDevelopment, copy, con);
		reloadNames(enzymeUnderDevelopment.getCommonNames(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()),
				EnzymeNameTypeConstant.COMMON_NAME, con);
		reloadReactions(enzymeUnderDevelopment.getReactionDtos(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		compareSystematicName(enzymeUnderDevelopment, copy, con);
		reloadNames(enzymeUnderDevelopment.getSynonyms(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()),
				EnzymeNameTypeConstant.OTHER_NAME, con);
		reloadCofactors(enzymeUnderDevelopment.getCofactors(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		reloadLinks(enzymeUnderDevelopment, con);
		reloadComments(enzymeUnderDevelopment.getComments(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		reloadReferences(enzymeUnderDevelopment.getReferences(), new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()),
				EnzymeSourceConstant.valueOf(enzymeUnderDevelopment.getSource()), con);
		LOGGER.info("... commit completed.");
	}

	/**
	 * Compares the core data of the two {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO} instances and updates the table
	 * <b><code>ENZYME</code></b> if any difference exists.
	 * <p/>
	 * 'Core' data comprises:
	 * <ul>
	 * <li>EC number</li>
	 * <li>status code</li>
	 * <li>source</li>
	 * <li>note</li>
	 * <li>history line</li>
	 * </ul>
	 *
	 * @param enzymeUnderDevelopment The enzyme the curator has worked on.
	 * @param copy                   The copy created in {@link #register(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO)}.
	 * @param con                    A database connection used to perform the changes in the database.
	 * @throws EcException  if an invalid EC number has been used.
	 * @throws SQLException if a database error occurs.
	 */
	private void compareCoreData(EnzymeDTO enzymeUnderDevelopment, EnzymeDTO copy, Connection con)
	throws EcException, SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert copy != null : "Parameter 'copy' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		boolean update = false;

		if (!enzymeUnderDevelopment.getEc().equals(copy.getEc())) update = true;
		if (!update && !enzymeUnderDevelopment.getStatusCode().equals(copy.getStatusCode())) update = true;
		if (!update && !enzymeUnderDevelopment.getSource().equals(copy.getSource())) update = true;
		if (!update && !enzymeUnderDevelopment.getNote().equals(copy.getNote())) update = true;
		if (!update && !enzymeUnderDevelopment.getHistoryLine().equals(copy.getHistoryLine())) update = true;

		// Update as soon as an element og the core date changed.
		if (update) {
			enzymeEntryMapper.update(new Long(enzymeUnderDevelopment.getId()),
					EnzymeCommissionNumber.valueOf(enzymeUnderDevelopment.getEc()),
					Status.fromCode(enzymeUnderDevelopment.getStatusCode()),
					EnzymeSourceConstant.valueOf(enzymeUnderDevelopment.getSource()),
					enzymeUnderDevelopment.getNote(),
					enzymeUnderDevelopment.getHistoryLine(),
					enzymeUnderDevelopment.isActive(),
					con);
		}
	}

	private void reloadReactions(List<ReactionDTO> reactions, Long enzymeId, Status status, Connection con)
	throws SQLException {
		assert reactions != null : "Parameter 'reactions' must not be null.";
		assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
		assert status != null : "Parameter 'status' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		EnzymaticReactions er = new EnzymaticReactions();
		for (ReactionDTO reactionDTO : reactions) {
			er.add(getReactionObject(reactionDTO), reactionDTO.getView());
		}
		enzymeReactionMapper.update(enzymeId, er, con);
	}

	private void reloadNames(List names, Long enzymeId, Status status, EnzymeNameTypeConstant type,
			Connection con) throws SQLException {
		assert names != null : "Parameter 'names' must not be null.";
		assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
		assert status != null : "Parameter 'status' must not be null.";
		assert type != null : "Parameter 'type' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List enzymeNames = new ArrayList();
		for (int iii = 0; iii < names.size(); iii++) {
			enzymeNames.add(getEnzymeNameObject((EnzymeNameDTO) names.get(iii)));
		}
		//if (enzymeNames.size() > 0) {
		EnzymeNameMapper enzymeNameMapper = new EnzymeNameMapper();
		enzymeNameMapper.reload(enzymeNames, enzymeId, type, status, con);
		// }
	}

	/**
	 * Compares the systematic names of the two enzyme DTO objects and updates the name in the database if it changed.
	 *
	 * @param enzymeUnderDevelopment The enzyme the curator has worked on.
	 * @param copy                   The copy created in {@link #register(uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO)}.
	 * @param con                    A database connection used to perform the changes in the database.
	 * @throws SQLException if a database error occurs.
	 */
	private void compareSystematicName(EnzymeDTO enzymeUnderDevelopment, EnzymeDTO copy, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert copy != null : "Parameter 'copy' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		if (!enzymeUnderDevelopment.getSystematicName().getXmlName().equals(copy.getSystematicName().getXmlName()) ||
				!enzymeUnderDevelopment.getSystematicName().getView().equals(copy.getSystematicName().getView()) ||
				!enzymeUnderDevelopment.getStatusCode().equals(copy.getStatusCode())) {
			EnzymeNameMapper enzymeNameMapper = new EnzymeNameMapper();
			EnzymeName enzymeName = EnzymeName.getSystematicNameInstance(enzymeUnderDevelopment.getSystematicName().getXmlName(),
					EnzymeSourceConstant.valueOf(enzymeUnderDevelopment.getSource()),
					EnzymeViewConstant.valueOf(enzymeUnderDevelopment.getSystematicName().getView()));
			if (copy.getSystematicName().getXmlName().equals("")){
				// There was no systematic name in the database
				enzymeNameMapper.insert(enzymeName, new Long(enzymeUnderDevelopment.getId()),
						Status.fromCode(enzymeUnderDevelopment.getStatusCode()), 1, con);
			} else {
				enzymeNameMapper.update(enzymeName, new Long(enzymeUnderDevelopment.getId()),
						Status.fromCode(enzymeUnderDevelopment.getStatusCode()), 1, con);
			}
		}
	}

	private void reloadCofactors(List<CofactorDTO> cofactors, Long enzymeId, Status status, Connection con)
	throws SQLException, CommitException {
		assert cofactors != null : "Parameter 'names' must not be null.";
		assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
		assert status != null : "Parameter 'status' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		Collection<Object> enzymeCofactors = new ArrayList<Object>();
		for (int iii = 0; iii < cofactors.size(); iii++) {
			enzymeCofactors.add(getCofactorObject(cofactors.get(iii), con));
		}
		if (cofactors.size() != enzymeCofactors.size()){
			// some cofactor lost!
			LOGGER.error("Error while parsing cofactors!");
			throw new CommitException("Error while parsing cofactors!");
		}
		EnzymeCofactorMapper enzymeCofactorMapper = new EnzymeCofactorMapper();
		enzymeCofactorMapper.reload(enzymeCofactors, enzymeId, status, con);

	}

	/**
	 * Stores the given list of links (incl. UniProt links) after deleting all existing links.
	 *
	 * @param enzymeUnderDevelopment The enzyme DTO containing the list of links.
	 * @param con                    A database connection used to perform the changes in the database.
	 * @throws SQLException if a database error occurs.
	 */
	private void reloadLinks(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException, DomainException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List links = getEnzymeLinkObjects(enzymeUnderDevelopment.getLinks(), enzymeUnderDevelopment.getUniProtLinks());
		EnzymeLinkMapper enzymeLinkMapper = new EnzymeLinkMapper();
		enzymeLinkMapper.reloadLinks(links, new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
	}

	private void reloadComments(List comments, Long enzymeId, Status status, Connection con)
	throws SQLException {
		assert comments != null : "Parameter 'names' must not be null.";
		assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
		assert status != null : "Parameter 'status' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List enzymeComments = new ArrayList();
		for (int iii = 0; iii < comments.size(); iii++) {
			enzymeComments.add(getEnzymeCommentObject((CommentDTO) comments.get(iii)));
		}

		EnzymeCommentMapper enzymeCommentMapper = new EnzymeCommentMapper();
		enzymeCommentMapper.reload(enzymeComments, enzymeId, status, con);

	}

	private void reloadReferences(List references, Long enzymeId, Status status,
			EnzymeSourceConstant source, Connection con) throws SQLException {
		assert references != null : "Parameter 'names' must not be null.";
		assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
		assert status != null : "Parameter 'status' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List<Reference> enzymeReferences = new ArrayList();
		for (int iii = 0; iii < references.size(); iii++) {
			enzymeReferences.add(getReferenceObject((ReferenceDTO) references.get(iii)));
		}

		//if (enzymeReferences.size() > 0) {
		EnzymeReferenceMapper enzymeReferenceMapper = new EnzymeReferenceMapper();
		enzymeReferenceMapper.reload(enzymeReferences, enzymeId, status, con);
		//}
	}


	// --------------------- Insert methods ----------------------------

	/**
	 * Inserts the given enzyme information into the database.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry to be loaded into the database.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertEnzymeEntry(EnzymeDTO enzymeUnderDevelopment, Connection con)
	throws SQLException, EcException, DomainException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		insertCoreData(enzymeUnderDevelopment, con);
		insertNames(enzymeUnderDevelopment, con);
		insertReactions(enzymeUnderDevelopment, con);
		insertCofactors(enzymeUnderDevelopment, con);
		insertLinks(enzymeUnderDevelopment, con);
		insertComments(enzymeUnderDevelopment, con);
		insertReferences(enzymeUnderDevelopment, con);
	}

	/**
	 * Inserts the enzyem's core data (see <code>TABLE enzymes</code>) into the database and assigns a new enzyme ID.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 * @throws SQLException if a database error occurs.
	 * @throws EcException  if an invalid EC number has been used.
	 */
	private void insertCoreData(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException, EcException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		Long newEnzymeId = enzymeEntryMapper.findNextEnzymeId(con);
		enzymeUnderDevelopment.setId(newEnzymeId.toString());
		enzymeEntryMapper.insert(newEnzymeId, EnzymeCommissionNumber.valueOf(enzymeUnderDevelopment.getEc()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()),
				EnzymeSourceConstant.valueOf(enzymeUnderDevelopment.getSource()),
				enzymeUnderDevelopment.getNote(), enzymeUnderDevelopment.getHistoryLine(), true, con);
	}

	/**
	 * Inserts the new enzyme's names (common name, synonyms and systematic name).
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertNames(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		EnzymeNameMapper enzymeNameMapper = new EnzymeNameMapper();
		List enzymeNames = new ArrayList();
		List commonNames = enzymeUnderDevelopment.getCommonNames();
		for (int iii = 0; iii < commonNames.size(); iii++) {
			enzymeNames.add(getEnzymeNameObject((EnzymeNameDTO) commonNames.get(iii)));
		}
		List synonyms = enzymeUnderDevelopment.getSynonyms();
		for (int iii = 0; iii < synonyms.size(); iii++) {
			enzymeNames.add(getEnzymeNameObject((EnzymeNameDTO) synonyms.get(iii)));
		}
		if(enzymeUnderDevelopment.getSystematicName() != null &&
				!enzymeUnderDevelopment.getSystematicName().getXmlName().equals(""))
			enzymeNames.add(getEnzymeNameObject(enzymeUnderDevelopment.getSystematicName()));
		enzymeNameMapper.insertNames(enzymeNames, new Long(enzymeUnderDevelopment.getId()),
				Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);

	}

	/**
	 * Inserts the new enzyme's reactions.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertReactions(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		EnzymaticReactions er = new EnzymaticReactions();
		for (ReactionDTO reactionDTO : enzymeUnderDevelopment.getReactionDtos()) {
			er.add(getReactionObject(reactionDTO), reactionDTO.getView());
		}

		if (er.size() > 0) {
			enzymeReactionMapper.insert(er, new Long(enzymeUnderDevelopment.getId()), con);
		}
	}

	/**
	 * Inserts the new enzyme's cofactors.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertCofactors(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List<Object> enzymeCofactors = new ArrayList<Object>();
		List<CofactorDTO> cofactors = enzymeUnderDevelopment.getCofactors();
		for (int iii = 0; iii < cofactors.size(); iii++) {
			enzymeCofactors.add(getCofactorObject(cofactors.get(iii), con));
		}

		if (enzymeCofactors.size() > 0) {
			EnzymeCofactorMapper enzymeCofactorMapper = new EnzymeCofactorMapper();
			enzymeCofactorMapper.insert(enzymeCofactors, new Long(enzymeUnderDevelopment.getId()),
					Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		}
	}

	/**
	 * Inserts the new enzyme's links.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertLinks(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException, DomainException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List enzymeLinks = getEnzymeLinkObjects(enzymeUnderDevelopment.getLinks(), enzymeUnderDevelopment.getUniProtLinks());
		if (enzymeLinks.size() > 0) {
			EnzymeLinkMapper enzymeLinkMapper = new EnzymeLinkMapper();
			enzymeLinkMapper.insert(enzymeLinks, new Long(enzymeUnderDevelopment.getId()),
					Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		}
	}

	/**
	 * Inserts the new enzyme's comments.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertComments(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List enzymeComments = new ArrayList();
		List comments = enzymeUnderDevelopment.getComments();
		for (int iii = 0; iii < comments.size(); iii++) {
			enzymeComments.add(getEnzymeCommentObject((CommentDTO) comments.get(iii)));
		}

		if (enzymeComments.size() > 0) {
			EnzymeCommentMapper enzymeCommentMapper = new EnzymeCommentMapper();
			enzymeCommentMapper.insert(enzymeComments, new Long(enzymeUnderDevelopment.getId()),
					Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		}
	}

	/**
	 * Inserts the new enzyme's references.
	 *
	 * @param enzymeUnderDevelopment The new enzyme entry.
	 * @param con                    A database connection used to perform the changes in the database.
	 */
	private void insertReferences(EnzymeDTO enzymeUnderDevelopment, Connection con) throws SQLException {
		assert enzymeUnderDevelopment != null : "Parameter 'enzymeUnderDevelopment' must not be null.";
		assert con != null : "Parameter 'con' must not be null.";
		List enzymeReferences = new ArrayList();
		List references = enzymeUnderDevelopment.getReferences();
		for (int iii = 0; iii < references.size(); iii++) {
			enzymeReferences.add(getReferenceObject((ReferenceDTO) references.get(iii)));
		}

		if (enzymeReferences.size() > 0) {
			EnzymeReferenceMapper enzymeReferenceMapper = new EnzymeReferenceMapper();
			enzymeReferenceMapper.insert(enzymeReferences, new Long(enzymeUnderDevelopment.getId()),
					Status.fromCode(enzymeUnderDevelopment.getStatusCode()), con);
		}
	}

	// ---------------------- Get domain objects --------------------------------

	/**
	 * Creates a {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeName} object using a
	 * {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO} object stored in the enzyme DTO.
	 *
	 * @param enzymeNameDTO The {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO} used to create a
	 *                      {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeName} object.
	 * @return an instance of {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeName}.
	 */
	public EnzymeName getEnzymeNameObject(EnzymeNameDTO enzymeNameDTO) {
		assert enzymeNameDTO != null : "Parameter 'enzymeNameDTO' must not be null.";
		if (enzymeNameDTO.getType().equals(EnzymeNameTypeConstant.COMMON_NAME.toString()))
			return EnzymeName.getCommonNameInstance(enzymeNameDTO.getXmlName(),
					EnzymeSourceConstant.valueOf(enzymeNameDTO.getSource()),
					EnzymeViewConstant.valueOf(enzymeNameDTO.getView()));
		if (enzymeNameDTO.getType().equals(EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString()))
			return EnzymeName.getSystematicNameInstance(enzymeNameDTO.getXmlName(),
					EnzymeSourceConstant.valueOf(enzymeNameDTO.getSource()),
					EnzymeViewConstant.valueOf(enzymeNameDTO.getView()));
		return EnzymeName.getSynonymInstance(enzymeNameDTO.getXmlName(), EnzymeNameQualifierConstant.valueOf(enzymeNameDTO.getQualifier()),
				EnzymeSourceConstant.valueOf(enzymeNameDTO.getSource()),
				EnzymeViewConstant.valueOf(enzymeNameDTO.getView()));
	}

	/**
	 * Creates a {@link uk.ac.ebi.rhea.domain.Reaction} object using a
	 * {@link uk.ac.ebi.intenz.webapp.dtos.ReactionDTO} object stored in the enzyme DTO.
	 *
	 * @param reactionDTO The {@link uk.ac.ebi.intenz.webapp.dtos.ReactionDTO} used to create a
	 *                    {@link uk.ac.ebi.intenz.domain.enzyme.Reaction} object.
	 * @return an instance of {@link uk.ac.ebi.intenz.domain.enzyme.Reaction}.
	 */
	private Reaction getReactionObject(ReactionDTO reactionDTO) {
		assert reactionDTO != null : "Parameter 'reactionDTO' must not be null.";
		Reaction reaction = new Reaction(reactionDTO.getId(), reactionDTO.getXmlTextualRepresentation(),
				Database.valueOf(reactionDTO.getSource()));
		reaction.setStatus(uk.ac.ebi.rhea.domain.Status.valueOf(reactionDTO.getStatus()));
		return reaction;
	}

	/**
	 * Creates a {@link uk.ac.ebi.intenz.domain.enzyme.Cofactor} object using a
	 * {@link uk.ac.ebi.intenz.webapp.dtos.CofactorDTO} object stored in the enzyme DTO.
	 *
	 * @param cofactorDTO The {@link uk.ac.ebi.intenz.webapp.dtos.CofactorDTO} used to create a
	 *                    {@link uk.ac.ebi.intenz.domain.enzyme.Cofactor} object.
	 * @return an instance of {@link uk.ac.ebi.intenz.domain.enzyme.Cofactor}.
	 */
	private Object getCofactorObject(final CofactorDTO cofactorDTO, final Connection con) {
		assert cofactorDTO != null : "Parameter 'cofactorDTO' must not be null.";
		Object o = null;
		if (cofactorDTO.getCompoundId().indexOf(' ') > -1){ // we have an OperatorSet
			OperatorSet.ObjectBuilder builder = new OperatorSet.ObjectBuilder(){
				public Object parse(String s) throws Exception {
					Long compoundId = Long.valueOf(s);
					Compound compound = new RheaCompoundDbReader(con).find(compoundId);
					return Cofactor.valueOf(compound, EnzymeSourceConstant.valueOf(cofactorDTO.getSource()),
							EnzymeViewConstant.valueOf(cofactorDTO.getView()));
				}
			};
			try {
                String[] operators = new String[Cofactor.Operators.values().length];
                for (int i = 0; i < operators.length; i++){
                    operators[i] = Cofactor.Operators.values()[i].getCode();
                }
				o = OperatorSet.parse(cofactorDTO.getCompoundId(), operators, builder);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		} else {
			Compound compound = Compound.valueOf(Long.valueOf(cofactorDTO.getCompoundId()),
					cofactorDTO.getXmlCofactorValue(), null, 0, null, null, null, null);
			o = Cofactor.valueOf(compound, EnzymeSourceConstant.valueOf(cofactorDTO.getSource()),
					EnzymeViewConstant.valueOf(cofactorDTO.getView()));
		}
		return o;
	}

	/**
	 * Creates {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeLink} objects using
	 * {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO} objects stored in the enzyme DTO.
	 *
	 * @param links        The {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO} objects used to create
	 *                     {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeLink} objects. Contains all links except
	 *                     Uniprot links.
	 * @param uniprotLinks UniProt {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO} objects also used to create
	 *                     {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeLink} objects.
	 * @return List {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeLink} objects.
	 */
	private List getEnzymeLinkObjects(List links, List uniprotLinks) throws DomainException {
		assert links != null : "Parameter 'links' must not be null.";
		assert uniprotLinks != null : "Parameter 'uniprotLinks' must not be null.";
		List enzymeLinks = new ArrayList();
		for (int iii = 0; iii < links.size(); iii++) {
			EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
			EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.valueOf(enzymeLinkDTO.getDatabaseCode()),
					enzymeLinkDTO.getUrl(), enzymeLinkDTO.getAccession(),
					enzymeLinkDTO.getName(),
					EnzymeSourceConstant.valueOf(enzymeLinkDTO.getSource()),
					EnzymeViewConstant.valueOf(enzymeLinkDTO.getView()),
					enzymeLinkDTO.getDataComment());
			enzymeLinks.add(enzymeLink);
		}
		for (int iii = 0; iii < uniprotLinks.size(); iii++) {
			EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) uniprotLinks.get(iii);
			EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.valueOf(enzymeLinkDTO.getDatabaseCode()),
					enzymeLinkDTO.getUrl(), enzymeLinkDTO.getAccession(),
					enzymeLinkDTO.getName(),
					EnzymeSourceConstant.valueOf(enzymeLinkDTO.getSource()),
					EnzymeViewConstant.valueOf(enzymeLinkDTO.getView()),
					enzymeLinkDTO.getDataComment());
			enzymeLinks.add(enzymeLink);
		}
		return enzymeLinks;
	}

	/**
	 * Creates a {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeComment} object using a
	 * {@link uk.ac.ebi.intenz.webapp.dtos.CommentDTO} object stored in the enzyme DTO.
	 *
	 * @param commentDTO The {@link uk.ac.ebi.intenz.webapp.dtos.CommentDTO} object used to create a
	 *                   {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeComment} object.
	 * @return an instance of {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeComment}.
	 */
	private EnzymeComment getEnzymeCommentObject(CommentDTO commentDTO) {
		assert commentDTO != null : "Parameter 'commentDTO' must not be null.";
		return new EnzymeComment(commentDTO.getXmlComment(), EnzymeSourceConstant.valueOf(commentDTO.getSource()),
				EnzymeViewConstant.valueOf(commentDTO.getView()));
	}

	/**
	 * Creates a {@link uk.ac.ebi.intenz.domain.reference.Journal},
	 * {@link uk.ac.ebi.intenz.domain.reference.Book} or
	 * {@link uk.ac.ebi.intenz.domain.reference.Patent} object using a
	 * {@link uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO} object stored in the enzyme DTO.
	 *
	 * @param referenceDTO The {@link uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO} object used to create a
	 *                     {@link uk.ac.ebi.intenz.domain.reference.Reference} object.
	 * @return an instance of {@link uk.ac.ebi.intenz.domain.reference.Journal},
	 *         {@link uk.ac.ebi.intenz.domain.reference.Book} or
	 *         {@link uk.ac.ebi.intenz.domain.reference.Patent}.
	 */
	private Reference getReferenceObject(ReferenceDTO referenceDTO) {
		assert referenceDTO != null : "Parameter 'referenceDTO' must not be null.";
		Long pubId = null;
		if(!referenceDTO.getPubId().equals("")) {
			pubId = new Long(referenceDTO.getPubId());
		}
		if (referenceDTO.getType().equals("J")) {
			return new Journal(pubId, referenceDTO.getXmlAuthors(), referenceDTO.getXmlTitle(),
					referenceDTO.getYear(), referenceDTO.getXmlPubName(), referenceDTO.getFirstPage(),
					referenceDTO.getLastPage(), referenceDTO.getVolume(), referenceDTO.getPubMedId(),
					referenceDTO.getMedlineId(), EnzymeViewConstant.valueOf(referenceDTO.getView()),
					EnzymeSourceConstant.valueOf(referenceDTO.getSource()));
		}
		if (referenceDTO.getType().equals("B")) {
			return new Book(pubId, referenceDTO.getXmlAuthors(), referenceDTO.getXmlTitle(),
					referenceDTO.getYear(), referenceDTO.getFirstPage(), referenceDTO.getLastPage(),
					referenceDTO.getXmlPubName(), referenceDTO.getEdition(), referenceDTO.getXmlEditor(),
					referenceDTO.getVolume(), referenceDTO.getXmlPublisher(), referenceDTO.getPublisherPlace(),
					EnzymeViewConstant.valueOf(referenceDTO.getView()),
					EnzymeSourceConstant.valueOf(referenceDTO.getSource()));
		}
		// N.B.: patent number is currently stored in the field pubMedId (TODO: change this).
		return new Patent(pubId, referenceDTO.getXmlAuthors(), referenceDTO.getXmlTitle(),
				referenceDTO.getYear(), referenceDTO.getPubMedId(),
				EnzymeViewConstant.valueOf(referenceDTO.getView()),
				EnzymeSourceConstant.valueOf(referenceDTO.getSource()));
	}

	public void valueBound(HttpSessionBindingEvent arg0) {
		// no-op
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		enzymeEntryMapper.close();
		try {
			enzymeReactionMapper.close();
		} catch (MapperException e) {
			LOGGER.error("While closing session", e);
		}
	}

}
