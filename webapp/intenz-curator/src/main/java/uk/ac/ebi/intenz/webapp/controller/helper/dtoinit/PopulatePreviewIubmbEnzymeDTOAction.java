package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.IubmbEnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/03/12 12:29:16 $
 */
public class PopulatePreviewIubmbEnzymeDTOAction extends Action {
  private static final Logger LOGGER =
	  Logger.getLogger(PopulatePreviewIubmbEnzymeDTOAction.class.getName());
  private static final String IUBMB_ENTRY_PREVIEW_JSP_FWD = "iubmb_entry_preview";

    @Override
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateIubmbEnzymeDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    populateForm((EnzymeDTO) request.getSession().getAttribute("enzymeDTO"), form, encoding, null);
    request.setAttribute("title", "Preview entry " + ((EnzymeDTO) request.getSession().getAttribute("enzymeDTO")).getEc() + " - IntEnz Curator Application");
    return mapping.findForward(IUBMB_ENTRY_PREVIEW_JSP_FWD);
  }

  private void populateForm(EnzymeDTO enzymeDTO, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType) {
    LOGGER.debug("Populating IubmbEnzymeDTO ...");
    IubmbEnzymeDTO iubmbEnzymeDTO = (IubmbEnzymeDTO) form;
    iubmbEnzymeDTO.setId("" + enzymeDTO.getId());
    iubmbEnzymeDTO.setEc(enzymeDTO.getEc());
    iubmbEnzymeDTO.setCommonNames(getNamesList(enzymeDTO.getCommonNames(), encoding, encodingType));
    iubmbEnzymeDTO.setReactions(getReactionsList(enzymeDTO.getReactionDtos(), encoding, encodingType));
    iubmbEnzymeDTO.setSystematicName(getSystematicNameDTO(enzymeDTO.getSystematicName(), encoding, encodingType));
    iubmbEnzymeDTO.setSynonyms(getNamesList(enzymeDTO.getSynonyms(), encoding, encodingType));
    iubmbEnzymeDTO.setLinks(getLinksList(enzymeDTO.getLinks(), enzymeDTO.getEc(), encoding, encodingType));
    iubmbEnzymeDTO.setComments(getCommentsList(enzymeDTO.getComments(), encoding, encodingType));
    iubmbEnzymeDTO.setReferences(getReferencesList(enzymeDTO.getReferences(), encoding, encodingType));
    iubmbEnzymeDTO.setHistoryLine(enzymeDTO.getHistoryLine());
    iubmbEnzymeDTO.setStatusCode(enzymeDTO.getStatusCode());
    LOGGER.debug("... IubmbEnzymeDTO populated.");
  }

  private List getNamesList(List names, SpecialCharacters encoding, EncodingType encodingType) {
    assert names != null : "Parameter 'names' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List namesList = new ArrayList();
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeNameDTO intEnzNameDTO = (EnzymeNameDTO) names.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(intEnzNameDTO.getView().toString())) {
        EnzymeNameDTO enzymeNameDTO = new EnzymeNameDTO();
        enzymeNameDTO.setName(encoding.xml2Display(intEnzNameDTO.getName(), encodingType));
        enzymeNameDTO.setSource(intEnzNameDTO.getSource());
        enzymeNameDTO.setView(intEnzNameDTO.getView());
        enzymeNameDTO.setQualifier(intEnzNameDTO.getQualifier());
        enzymeNameDTO.setType(intEnzNameDTO.getType());
        namesList.add(enzymeNameDTO);
      }
    }

    return namesList;
  }

  private List getReactionsList(List reactions, SpecialCharacters encoding, EncodingType encodingType) {
    assert reactions != null : "Parameter 'reactions' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List reactionsList = new ArrayList();
    for (int iii = 0; iii < reactions.size(); iii++) {
      ReactionDTO intEnzReactionDTO = (ReactionDTO) reactions.get(iii);
 	 if (intEnzReactionDTO.getId() > Reaction.NO_ID_ASSIGNED)
		 continue; // Ignore Rhea-ctions
      if (EnzymeViewConstant.isInIUBMBView(intEnzReactionDTO.getView())) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setXmlTextualRepresentation(intEnzReactionDTO.getTextualRepresentation());
        reactionDTO.setTextualRepresentation(IntEnzUtilities.linkMarkedEC(
            encoding.xml2Display(intEnzReactionDTO.getTextualRepresentation(), encodingType), false));
        reactionDTO.setSource(intEnzReactionDTO.getSource());
        reactionDTO.setView(intEnzReactionDTO.getView());
        reactionsList.add(reactionDTO);
      }
    }

    return reactionsList;
  }

  private EnzymeNameDTO getSystematicNameDTO(EnzymeNameDTO intEnzSystematicNameDTO, SpecialCharacters encoding,
                                             EncodingType encodingType) {
    EnzymeNameDTO systematicNameDTO = new EnzymeNameDTO();
    systematicNameDTO.setName(encoding.xml2Display(intEnzSystematicNameDTO.getName(), encodingType));
    systematicNameDTO.setSource(intEnzSystematicNameDTO.getSource());
    systematicNameDTO.setView(intEnzSystematicNameDTO.getView());
    systematicNameDTO.setType(intEnzSystematicNameDTO.getType());
    return systematicNameDTO;
  }

  private List getLinksList(List links, String ec, SpecialCharacters encoding, EncodingType encodingType) {
    assert links != null : "Parameter 'links' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List linksList = new ArrayList();
    for (Iterator it = links.iterator(); it.hasNext();) {
      EnzymeLinkDTO intEnzEnzymeLinkDTO = (EnzymeLinkDTO) it.next();
      if (EnzymeViewConstant.isInIUBMBView(intEnzEnzymeLinkDTO.getView().toString())) {
        EnzymeLinkDTO enzymeLinkDTO = new EnzymeLinkDTO();
        enzymeLinkDTO.setDatabaseName(intEnzEnzymeLinkDTO.getDatabaseName());
        enzymeLinkDTO.setSource(intEnzEnzymeLinkDTO.getSource());
        enzymeLinkDTO.setUrl(intEnzEnzymeLinkDTO.getUrl());
        enzymeLinkDTO.setName(encoding.xml2Display(intEnzEnzymeLinkDTO.getName(), encodingType));
        enzymeLinkDTO.setAccession(intEnzEnzymeLinkDTO.getAccession());
        enzymeLinkDTO.setView(intEnzEnzymeLinkDTO.getView());
        enzymeLinkDTO.setDataComment(intEnzEnzymeLinkDTO.getDataComment());
        linksList.add(enzymeLinkDTO);
      }
    }

    return linksList;
  }

  private List getCommentsList(List comments, SpecialCharacters encoding, EncodingType encodingType) {
    assert comments != null : "Parameter 'comments' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List commentsList = new ArrayList();
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO intEnzCommentDTO = (CommentDTO) comments.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(intEnzCommentDTO.getView().toString())) {
        CommentDTO commentDTO = new CommentDTO();
        // Some comments might contain legacy links to Gerry Moss's web pages. These have to be corrected.
        commentDTO.setComment(IntEnzUtilities.linkMarkedEC(encoding.xml2Display(intEnzCommentDTO.getComment(), encodingType), true));
        commentDTO.setSource(intEnzCommentDTO.getSource());
        commentDTO.setView(intEnzCommentDTO.getView());
        commentsList.add(commentDTO);
      }
    }

    return commentsList;
  }

  private List getReferencesList(List references, SpecialCharacters encoding, EncodingType encodingType) {
    assert references != null : "Parameter 'references' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List referencesList = new ArrayList();
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO intEnzReferenceDTO = (ReferenceDTO) references.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(intEnzReferenceDTO.getView())) {
        ReferenceDTO referenceDTO = new ReferenceDTO();
        referenceDTO.setPubId("" + intEnzReferenceDTO.getPubId());
        referenceDTO.setAuthors(encoding.xml2Display(intEnzReferenceDTO.getAuthors(), encodingType));
        referenceDTO.setTitle(encoding.xml2Display(intEnzReferenceDTO.getTitle(), encodingType));
        referenceDTO.setYear(intEnzReferenceDTO.getYear());
        referenceDTO.setView(intEnzReferenceDTO.getView());
        referenceDTO.setSource(intEnzReferenceDTO.getSource());

        if (intEnzReferenceDTO.getType().equals("J")) {
          referenceDTO.setType("J");
          referenceDTO.setPubName(encoding.xml2Display(intEnzReferenceDTO.getPubName(), encodingType));
          referenceDTO.setFirstPage(intEnzReferenceDTO.getFirstPage());
          referenceDTO.setLastPage(intEnzReferenceDTO.getLastPage());
          referenceDTO.setVolume(intEnzReferenceDTO.getVolume());
          referenceDTO.setPubMedId(intEnzReferenceDTO.getPubMedId());
          referenceDTO.setMedlineId(intEnzReferenceDTO.getMedlineId());
        }
        if (intEnzReferenceDTO.getType().equals("B")) {
          referenceDTO.setType("B");
          referenceDTO.setPubName(encoding.xml2Display(intEnzReferenceDTO.getPubName(), encodingType));
          referenceDTO.setFirstPage(intEnzReferenceDTO.getFirstPage());
          referenceDTO.setLastPage(intEnzReferenceDTO.getLastPage());
          referenceDTO.setEdition(encoding.xml2Display(intEnzReferenceDTO.getEdition(), encodingType));
          referenceDTO.setEditor(encoding.xml2Display(intEnzReferenceDTO.getEditor(), encodingType));
          referenceDTO.setVolume(intEnzReferenceDTO.getVolume());
          referenceDTO.setPublisher(encoding.xml2Display(intEnzReferenceDTO.getPublisher(), encodingType));
          referenceDTO.setPublisherPlace(encoding.xml2Display(intEnzReferenceDTO.getPublisherPlace(), encodingType));
        }
        if (intEnzReferenceDTO.getType().equals("P")) {
          referenceDTO.setType("P");
          referenceDTO.setPubMedId(intEnzReferenceDTO.getPubMedId());
        }
        referencesList.add(referenceDTO);
      }
    }

    return referencesList;
  }

}
