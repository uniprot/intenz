package uk.ac.ebi.intenz.webapp.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.webapp.controller.modification.CurationAction;
import uk.ac.ebi.intenz.webapp.dtos.CofactorDTO;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;
import uk.ac.ebi.intenz.webapp.utilities.PubMedAccessor;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.mapper.IRheaCompoundReader;
import uk.ac.ebi.rhea.mapper.IRheaReader;
import uk.ac.ebi.rhea.updater.ChebiUpdater;
import uk.ac.ebi.rhea.webapp.SessionManager;

/**
 * This Action handles all button events apart from submit buttons.
 * <p/>
 * Before the actual button command is being processed the enzyme ActionForm (<code>EnzymeDTO</code>) is validated.
 * This is done in addition to the form validations performed by the Struts environment to be able to compare a field
 * value with other field values of the form. These validation are done by methods provided by the
 * {@link uk.ac.ebi.intenz.webapp.utilities.IntEnzValidations IntEnzValidations} class, which acts as a central
 * container for validations needed by the IntEnz curator web application.
 * <p/>
 * The button actions which are supported at the moment are:
 * <ul>
 * <li>adding/deleting of values</li>
 * <li>updating UniProt xrefs</li>
 * <li>retreiving information of a publication from PubMed (using a PubMed ID)</li>
 * </ul>
 * <p/>
 * <code>ActionMessages</code> are being created if any errors occur during the execution of this Action. These messages
 * will then be displayed on the form.
 * <p/>
 * Furthermore this Action will check and set tokens to keep the session in a defined state.
 *
 * @author Michael Darsow
 * @version $Revision: 1.8 $ $Date: 2008/05/30 15:27:54 $
 */
public class FormButtonAction extends CurationAction {

	private static final Logger LOGGER =
		Logger.getLogger(FormButtonAction.class.getName());

	private static enum Forward { RHEACTION_SELECTOR, COFACTOR_SELECTOR };

	/**
	 * Executes the command as described in the class comment.
	 *
	 * @param mapping  The ActionMapping as defined in <code>strust-config.xml</code>
	 * @param form     The ActionForm attached to this Action.
	 * @param request  The request object.
	 * @param response The response object.
	 * @return an ActionForward object.
	 * @throws Exception forward exceptions
	 */
    @Override
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		EnzymeDTO enzymeDTO = (EnzymeDTO) form;
		ActionMessages errors = new ActionMessages();
		Forward fwd = null;

		try {
			fwd = processButtonCommand(enzymeDTO, request);
		} catch (DomainException e) {
			LOGGER.info("Form error.", e);
			errors.add(e.getProperty(), new ActionMessage(e.getMessageKey()));
			saveErrors(request, errors);
			if(enzymeDTO.getId() != null && !enzymeDTO.getId().equals(""))
				ControlFlowToken.setToken(request, new Long(enzymeDTO.getId()));
			System.out.println(mapping.getInputForward().toString());
			return mapping.getInputForward();
		}
		if (enzymeDTO.getId() != null && !enzymeDTO.getId().equals(""))
			ControlFlowToken.setToken(request, new Long(enzymeDTO.getId()));
		saveErrors(request, errors);
		request.setAttribute("title", "EC "+enzymeDTO.getEc());
		return (fwd == null)? mapping.getInputForward() : mapping.findForward(fwd.toString());
	}


	// ---------------------- PRIVATE methods ----------------------------

	/**
	 * Checks the action which has to be taken and triggers further processing if necessary.
	 *
	 * @param enzymeDTO Needed for further processing.
	 */
	private Forward processButtonCommand(EnzymeDTO enzymeDTO, HttpServletRequest request) throws DomainException {
		assert enzymeDTO != null : "Parameter 'enzymeDTO' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";

		String buttonCommand = request.getParameter("buttonCmd");
		String listType = request.getParameter("listType");

		if (buttonCommand.equals("plus")) {
			if (listType.equals("commonNames")) namesPlus(enzymeDTO.getCommonNames(), EnzymeNameTypeConstant.COMMON_NAME);
			if (listType.equals("synonyms")) namesPlus(enzymeDTO.getSynonyms(), EnzymeNameTypeConstant.OTHER_NAME);
			if (listType.equals("reactions")) reactionsPlus(enzymeDTO.getReactionDtos());
			if (listType.equals("rheaction")){
				Long rheaId = Long.valueOf(request.getParameter("rheaId"));
				IRheaReader rheaReader = SessionManager.getRheaReader(request);
				Reaction reaction;
				try {
					reaction = rheaReader.findByReactionId(rheaId);
					rheactionPlus(enzymeDTO.getReactionDtos(), reaction);
				} catch (Exception e) {
					LOGGER.error("Unable to fetch reaction with ID " + rheaId, e);
					throw new DomainException("reaction", "errors.application.reaction");
				}
			}
			if (listType.equals("cofactors")){
				processCofactors(enzymeDTO, request);
			}
			if (listType.equals("comments")) commentsPlus(enzymeDTO.getComments());
			if (listType.equals("links")) linksPlus(enzymeDTO.getLinks());
			if (listType.equals("references")) referencesPlus(enzymeDTO.getReferences());
		}

		if (buttonCommand.equals("delete")) {
			if (listType.equals("commonNames")) namesDelete(enzymeDTO.getCommonNames(), request);
			if (listType.equals("synonyms")) namesDelete(enzymeDTO.getSynonyms(), request);
			if (listType.equals("reactions")) reactionsDelete(enzymeDTO.getReactionDtos(), request);
			if (listType.equals("cofactors")) cofactorsDelete(enzymeDTO.getCofactors(), request);
			if (listType.equals("comments")) commentsDelete(enzymeDTO.getComments(), request);
			if (listType.equals("links")) linksDelete(enzymeDTO.getLinks(), request);
			if (listType.equals("references")) referencesDelete(enzymeDTO.getReferences(), request);
		}

		if (buttonCommand.equals("pubMedFetch")) {
			int index = Integer.parseInt(request.getParameter("index"));
			PubMedAccessor.fetchPubMedJournal(enzymeDTO.getReferences(), index);
		}
		return null;
	}


	private void processCofactors(EnzymeDTO enzymeDTO, HttpServletRequest request)
	throws DomainException {
		String pCofactorId = request.getParameter("cofactorId");
		// If the cofactor has to be OR-ed to another existing one:
		Integer complexCofactorDtoIndex =
			StringUtil.isNullOrEmpty(request.getParameter("complexCofactorDtoIndex"))?
				null : Integer.valueOf(request.getParameter("complexCofactorDtoIndex"));
		Integer complexCofactorDtoInternalIndex =
			StringUtil.isNullOrEmpty(request.getParameter("complexCofactorDtoInternalIndex"))?
				null : Integer.valueOf(request.getParameter("complexCofactorDtoInternalIndex"));
		String complexCofactorOperator =
			StringUtil.isNullOrEmpty(request.getParameter("complexCofactorOperator"))?
				null : request.getParameter("complexCofactorOperator");
		boolean complexCofactorBrackets =
			StringUtil.isNullOrEmpty(request.getParameter("complexCofactorBrackets"))?
				false : Boolean.parseBoolean(request.getParameter("complexCofactorBrackets"));
		
		IRheaCompoundReader compoundReader =
                SessionManager.getRheaCompoundReader(request);
		Compound compound = null;
		try {
			if (StringUtil.isInteger(pCofactorId)){
				// internal Rhea compound ID:
				compound = compoundReader.find(Long.valueOf(pCofactorId));
			} else if (pCofactorId.toUpperCase().startsWith("CHEBI:")){
				compound = compoundReader.findByAccession(pCofactorId);
				if (compound == null){
					// compound new to Rhea/IntEnz:
		            ChebiUpdater chebiUpdater =
		                    (ChebiUpdater) request.getSession().getAttribute("chebiUpdater");
		            compound = chebiUpdater.importCompound(pCofactorId);
				}
			}
			cofactorsPlus(enzymeDTO.getCofactors(), compound, complexCofactorDtoIndex,
					complexCofactorDtoInternalIndex, complexCofactorOperator, complexCofactorBrackets);
		} catch (DomainException e){
			throw e;
		} catch (Exception e){
			LOGGER.error("Unable to retrieve cofactor with ID " + pCofactorId, e);
			throw new DomainException("cofactor", "errors.application.cofactor");
		}
	}

	/**
	 * Adds a new (empty) name to the given list of names.
	 *
	 * @param names The list of names (common or other names).
	 * @param type
	 */
	private void namesPlus(List names, EnzymeNameTypeConstant type) {
		assert names != null : "Parameter 'names' must not be null.";
		EnzymeNameDTO newName = getNewEnzymeNameDTOInstance(names.size());
		newName.setType(type.toString());
		names.add(newName);
	}

	/**
	 * Deletes a name from the list of names.
	 *
	 * @param names   The list of names.
	 * @param request Stores the list index of the name to be deleted.
	 */
	private void namesDelete(List names, HttpServletRequest request) {
		assert names != null : "Parameter 'names' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= names.size()) return; // if the user deletes a name which is not stored yet
		names.remove(index);
	}

	/**
	 * Adds a reaction to the given list of reactions.
	 *
	 * @param reactions The list of reactions.
	 */
	private void reactionsPlus(List<ReactionDTO> reactions) {
		assert reactions != null : "Parameter 'reactions' must not be null.";
		ReactionDTO newReaction = getNewReactionDTOInstance(reactions.size());
		reactions.add(newReaction);
	}

	private void rheactionPlus(List<ReactionDTO> reactions, Reaction reaction) {
		for (ReactionDTO reactionDTO : reactions) {
			if (reactionDTO.getId() == reaction.getId()) return; // avoid duplicates
		}
		ReactionDTO newReaction = getNewReactionDTOInstance(reactions.size());
		newReaction.setXmlTextualRepresentation(reaction.getTextualRepresentation());
		newReaction.setStatus(reaction.getStatus().toString());
		newReaction.setId(reaction.getId());	  
		reactions.add(newReaction);
	}

	/**
	 * Deletes a reaction from the list of reactions.
	 *
	 * @param reactions The list of reactions.
	 * @param request   Stores the list index of the reaction to be deleted.
	 */
	private void reactionsDelete(List reactions, HttpServletRequest request) {
		assert reactions != null : "Parameter 'reactions' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= reactions.size()) return; // if the user deletes a reaction which is not stored yet
		reactions.remove(Integer.parseInt(request.getParameter("index")));
	}

	/**
	 * Adds a cofactor to the given list of cofactors.
	 * @param cofactors The list of cofactor DTOs.
	 * @param complexCofactorDtoIndex The index of the CofactorDTO to add the new cofactor to.
	 * 		If not <code>null</code>, the CofactorDTO with the given index will be
	 * 		modified by adding a cofactor. Otherwise, a new CofactorDTO will be added to the list.
	 * @param complexCofactorDtoInternalIndex the index of the affected
	 * 		existing cofactor within the cofactorDto
	 * @param complexCofactorOperator operator to be used with the newly added
	 * 		cofactor and the existing one at dtoInternalIndex
	 * @param complexCofactorBrackets create a new group of cofactors enclosed in brackets?
	 */
	private void cofactorsPlus(List<CofactorDTO> cofactorDtos, Compound cofactor,
			Integer complexCofactorDtoIndex, Integer complexCofactorDtoInternalIndex,
			String complexCofactorOperator, boolean complexCofactorBrackets)
    throws DomainException {
		assert cofactorDtos != null : "Parameter 'cofactors' must not be null.";
        // CHECK THAT THE COFACTOR IS NOT ALREADY ASSIGNED TO THIS ENZYME:
        for (CofactorDTO dto : cofactorDtos){
            if (dto.getXmlCofactorValue().contains(cofactor.getName())
                    && dto.getAccession().contains(cofactor.getAccession())
                    && dto.getCompoundId().contains(cofactor.getId().toString())){
                throw new DomainException("cofactor", "errors.form.cofactor.already.present");
            }
        }
        if (complexCofactorDtoIndex == null){
			CofactorDTO newCofactor = getNewCofactorDTOInstance(cofactorDtos.size());
			newCofactor.setAccession(cofactor.getAccession());
			newCofactor.setXmlCofactorValue(cofactor.getName());
			newCofactor.setCompoundId(cofactor.getId().toString());
			cofactorDtos.add(newCofactor);
        } else {
        	CofactorDTO dto = cofactorDtos.get(complexCofactorDtoIndex);
        	if (complexCofactorDtoInternalIndex == null){
	        	dto.setAccession(dto.getAccession()
	        			+ " " + Cofactor.Operators.OR_OPTIONAL.getCode() + " "
	        			+ cofactor.getAccession());
	        	dto.setXmlCofactorValue(dto.getXmlCofactorValue()
	        			+ " " + Cofactor.Operators.OR_OPTIONAL.getCode() + " "
	        			+ cofactor.getName());
	        	dto.setCompoundId(dto.getCompoundId()
	        			+ " " + Cofactor.Operators.OR_OPTIONAL.getCode() + " "
	        			+ cofactor.getId().toString());
        	} else {
        		String[] dtoAccessions = splitComplexCofactorString(dto.getAccession());
        		if (complexCofactorDtoInternalIndex == dtoAccessions.length){
        			dto.setAccession(new StringBuilder(dto.getAccession())
        				.append(' ').append(complexCofactorOperator).append(' ')
        				.append(cofactor.getAccession()).toString());
        			dto.setXmlCofactorValue(new StringBuilder(dto.getXmlCofactorValue())
    					.append(' ').append(complexCofactorOperator).append(' ')
        				.append(cofactor.getName()).toString());
        			dto.setCompoundId(new StringBuilder(dto.getCompoundId())
        				.append(' ').append(complexCofactorOperator).append(' ')
        				.append(cofactor.getId().toString()).toString());
        		} else {
	       			dto.setAccession(replaceSimpleCofactorString(dto.getAccession(),
						dtoAccessions[complexCofactorDtoInternalIndex],
						complexCofactorOperator, cofactor.getAccession(), complexCofactorBrackets));
	        		String[] dtoXmls = splitComplexCofactorString(dto.getXmlCofactorValue());
	       			dto.setXmlCofactorValue(replaceSimpleCofactorString(dto.getXmlCofactorValue(),
						dtoXmls[complexCofactorDtoInternalIndex],
						complexCofactorOperator, cofactor.getName(), complexCofactorBrackets));
	        		String[] dtoCompoundIds = splitComplexCofactorString(dto.getCompoundId());
	       			dto.setCompoundId(replaceSimpleCofactorString(dto.getCompoundId(),
						dtoCompoundIds[complexCofactorDtoInternalIndex],
						complexCofactorOperator, cofactor.getId().toString(), complexCofactorBrackets));
        		}
        	}
        }
	}
	
	private String replaceSimpleCofactorString(String complexCofactor, String simpleCofactor,
			String operator, String newSimpleCofactor, boolean bracket){
		String escapedSimpleCofactorRegexp =
			simpleCofactor.replace("(", "\\(").replace(")", "\\)").replace("+", "\\+");
		return complexCofactor.replaceFirst(
			"(^| |\\()(" + escapedSimpleCofactorRegexp + ")(\\)| |$)",
			"$1"+(bracket? "( ":"")+"$2 "+operator+" "+newSimpleCofactor+(bracket? " )":"")+"$3");
	}

	private String[] splitComplexCofactorString(String s){
		return s.replaceAll("^\\( |(?<= )\\( | \\)(?= )| \\)$","")
			.split(" ("+Cofactor.Operators.OR_OPTIONAL.getCode()+"|"+Cofactor.Operators.AND.getCode()+") ");
	}

	/**
	 * Deletes a cofactor from the given list of cofactors.
	 *
	 * @param cofactors The list of cofactors.
	 * @param request   Stores the list index of the cofactor to be deleted.
	 */
	private void cofactorsDelete(List cofactors, HttpServletRequest request) {
		assert cofactors != null : "Parameter 'cofactors' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= cofactors.size()) return; // if the user deletes a cofactor which is not stored yet
		cofactors.remove(Integer.parseInt(request.getParameter("index")));
	}

	/**
	 * Adds a comment to the given list of comments.
	 *
	 * @param comments The list of comments.
	 */
	private void commentsPlus(List comments) {
		assert comments != null : "Parameter 'comments' must not be null.";
		CommentDTO newComment = getNewCommentDTOInstance(comments.size());
		comments.add(newComment);
	}

	/**
	 * Deletes a comment from the given list of comments.
	 *
	 * @param comments The list of comments.
	 * @param request  Stores the list index of the comment to be deleted.
	 */
	private void commentsDelete(List comments, HttpServletRequest request) {
		assert comments != null : "Parameter 'comments' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= comments.size()) return; // if the user deletes a comment which is not stored yet
		comments.remove(Integer.parseInt(request.getParameter("index")));
	}

	/**
	 * Adds a link to the given list of links.
	 *
	 * @param links The list of links.
	 */
	private void linksPlus(List links) {
		assert links != null : "Parameter 'links' must not be null.";
		EnzymeLinkDTO newLink = getNewEnzymeLinkDTOInstance();
		links.add(newLink);
	}

	/**
	 * Deletes a link from the given list of links.
	 *
	 * @param links   The list of links.
	 * @param request Stores the index of the link to be deleted.
	 */
	private void linksDelete(List links, HttpServletRequest request) {
		assert links != null : "Parameter 'links' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= links.size()) return; // if the user deletes a link which is not stored yet
		links.remove(Integer.parseInt(request.getParameter("index")));
	}

	/**
	 * Adds a reference to the given list of references.
	 *
	 * @param references The list of references.
	 */
	private void referencesPlus(List references) {
		assert references != null : "Parameter 'references' must not be null.";
		ReferenceDTO newReference = getNewReferenceDTOInstance();
		references.add(newReference);
	}

	/**
	 * Deletes a reference from the given list of references.
	 *
	 * @param references The list of references.
	 * @param request    Stores the index of the reference to be deleted.
	 */
	private void referencesDelete(List references, HttpServletRequest request) {
		assert references != null : "Parameter 'references' must not be null.";
		assert request != null : "Parameter 'request' must not be null.";
		final int index = Integer.parseInt(request.getParameter("index"));
		if (index >= references.size()) return; // if the user deletes a reference which is not stored yet
		references.remove(Integer.parseInt(request.getParameter("index")));
	}

	/**
	 * Creates a new instance of {@link uk.ac.ebi.intenz.webapp.dtos.ReactionDTO ReactionDTO} and sets the default
	 * (INTENZ) source and view values.
	 *
	 * @return the new <code>ReactionDTO</code> object.
	 */
	private ReactionDTO getNewReactionDTOInstance(int size) {
		ReactionDTO newReaction = new ReactionDTO();
		newReaction.setSource(EnzymeSourceConstant.INTENZ.toString());
		newReaction.setSourceDisplay(EnzymeSourceConstant.INTENZ.toDisplayString());
		newReaction.setView(EnzymeViewConstant.INTENZ.toString());
		newReaction.setViewDisplayString(EnzymeViewConstant.INTENZ.toDisplayString());
		newReaction.setOrderIn("" + size);
		return newReaction;
	}

	/**
	 * Creates a new instance of {@link uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO EnzymeNameDTO} and sets the default
	 * (INTENZ) source and view values.
	 *
	 * @return the new <code>EnzymeNameDTO</code> object.
	 */
	private EnzymeNameDTO getNewEnzymeNameDTOInstance(int size) {
		EnzymeNameDTO newName = new EnzymeNameDTO();
		newName.setSource(EnzymeSourceConstant.INTENZ.toString());
		newName.setSourceDisplay(EnzymeSourceConstant.INTENZ.toString());
		newName.setView(EnzymeViewConstant.INTENZ.toString());
		newName.setViewDisplayString(EnzymeViewConstant.INTENZ.toString());
		newName.setOrderIn("" + size);
		return newName;
	}

	/**
	 * Creates a new instance of {@link uk.ac.ebi.intenz.webapp.dtos.CofactorDTO CofactorDTO} and sets the default
	 * (INTENZ) source and view values.
	 *
	 * @return the new <code>CofactorDTO</code> object.
	 */
	private CofactorDTO getNewCofactorDTOInstance(int size) {
		CofactorDTO newCofactor = new CofactorDTO();
		newCofactor.setSource(EnzymeSourceConstant.INTENZ.toString());
		newCofactor.setSourceDisplay(EnzymeSourceConstant.INTENZ.toString());
		newCofactor.setView(EnzymeViewConstant.INTENZ.toString());
		newCofactor.setViewDisplayString(EnzymeViewConstant.INTENZ.toString());
		newCofactor.setOrderIn("" + size);
		return newCofactor;
	}

	/**
	 * Creates a new instance of {@link uk.ac.ebi.intenz.webapp.dtos.CommentDTO CommentDTO} and sets the default
	 * (INTENZ) source and view values.
	 *
	 * @return the new <code>CommentDTO</code> object.
	 */
	private CommentDTO getNewCommentDTOInstance(int size) {
		CommentDTO newCommentDTO = new CommentDTO();
		newCommentDTO.setSource(EnzymeSourceConstant.INTENZ.toString());
		newCommentDTO.setSourceDisplay(EnzymeSourceConstant.INTENZ.toString());
		newCommentDTO.setView(EnzymeViewConstant.INTENZ.toString());
		newCommentDTO.setViewDisplayString(EnzymeViewConstant.INTENZ.toString());
		newCommentDTO.setOrderIn("" + size);
		return newCommentDTO;
	}

	/**
	 * @return
	 */
	private EnzymeLinkDTO getNewEnzymeLinkDTOInstance() {
		EnzymeLinkDTO newEnzymeLinkDTO = new EnzymeLinkDTO();
		newEnzymeLinkDTO.setDatabaseName(XrefDatabaseConstant.UNDEF.getDisplayName());
		newEnzymeLinkDTO.setDatabaseCode(XrefDatabaseConstant.UNDEF.getDatabaseCode());
		newEnzymeLinkDTO.setSource(EnzymeSourceConstant.INTENZ.toString());
		newEnzymeLinkDTO.setSourceDisplay(EnzymeSourceConstant.INTENZ.toString());
		newEnzymeLinkDTO.setView(EnzymeViewConstant.INTENZ.toString());
		newEnzymeLinkDTO.setViewDisplayString(EnzymeViewConstant.INTENZ.toString());
		newEnzymeLinkDTO.setUrl("");
		newEnzymeLinkDTO.setName("");
		newEnzymeLinkDTO.setAccession("");
		return newEnzymeLinkDTO;
	}

	private ReferenceDTO getNewReferenceDTOInstance() {
		ReferenceDTO newReferenceDTO = new ReferenceDTO();
		newReferenceDTO.reset(null, null);
		newReferenceDTO.setSource(EnzymeSourceConstant.INTENZ.toString());
		newReferenceDTO.setSourceDisplay(EnzymeSourceConstant.INTENZ.toString());
		newReferenceDTO.setView(EnzymeViewConstant.INTENZ.toString());
		newReferenceDTO.setViewDisplayString(EnzymeViewConstant.INTENZ.toString());
		return newReferenceDTO;
	}
}
