package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.DataComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.DataCommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.IubmbEnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;
import uk.ac.ebi.intenz.webapp.helper.EnzymeReactionHelper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.6 $ $Date: 2008/03/12 12:29:16 $
 */
public class PopulateIubmbEnzymeDTOAction extends Action {
  private static final Logger LOGGER =
	  Logger.getLogger(PopulateIubmbEnzymeDTOAction.class.getName());
  private static final String IUBMB_ENTRY_JSP_FWD = "iubmb_entry";
  private static final String DELETED_TRANSFERRED_IUBMB_ENTRY_JSP_FWD = "iubmb_entry_del_trans";

    @Override
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateIubmbEnzymeDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    EnzymeEntry enzymeEntry = (EnzymeEntry) request.getAttribute("result");
    populateForm(enzymeEntry, form, encoding, null);
    if(enzymeEntry.getHistory().isDeletedRootNode() || enzymeEntry.getHistory().isTransferredRootNode())
      return mapping.findForward(DELETED_TRANSFERRED_IUBMB_ENTRY_JSP_FWD);
    return mapping.findForward(IUBMB_ENTRY_JSP_FWD);
  }

  private void populateForm(EnzymeEntry enzymeEntry, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType) {
    LOGGER.debug("Populating IubmbEnzymeDTO ...");
    IubmbEnzymeDTO iubmbEnzymeDTO = (IubmbEnzymeDTO) form;
    iubmbEnzymeDTO.setId("" + enzymeEntry.getId());
    iubmbEnzymeDTO.setEc(enzymeEntry.getEc().toString());
    iubmbEnzymeDTO.setCommonNames(getNamesList(enzymeEntry.getCommonNames(), encoding, encodingType));
    iubmbEnzymeDTO.setSystematicName(getSystematicNameDTO(enzymeEntry.getSystematicName(), encoding, encodingType));
    iubmbEnzymeDTO.setSynonyms(getNamesList(enzymeEntry.getSynonyms(), encoding, encodingType));
    iubmbEnzymeDTO.setReactions(getReactionsList(enzymeEntry.getEnzymaticReactions(), encoding, encodingType));
    iubmbEnzymeDTO.setLinks(getLinksList(enzymeEntry.getLinks(), enzymeEntry.getEc().toString(), encoding, encodingType));
    iubmbEnzymeDTO.setComments(getCommentsList(enzymeEntry.getComments(), encoding, encodingType));
    iubmbEnzymeDTO.setReferences(getReferencesList(enzymeEntry.getReferences(), encoding, encodingType));
    iubmbEnzymeDTO.setStatusCode(enzymeEntry.getStatus().getCode());
    final HistoryEvent latestHistoryEventOfRoot = enzymeEntry.getHistory().getLatestHistoryEventOfRoot();
    iubmbEnzymeDTO.setLatestHistoryEventNote(IntEnzUtilities.linkMarkedEC(encoding.xml2Display(latestHistoryEventOfRoot.getNote(), encodingType), true));
    iubmbEnzymeDTO.setLatestHistoryEventClass(enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getEventClass().toString());
    // Check whether this node has been transferred (set event class to 'TRA') or
    // if it is the new enzyme in a transfer process (set event class NOT to 'TRA' but 'NEW').
    if (latestHistoryEventOfRoot.getEventClass() == EventConstant.TRANSFER &&
        !enzymeEntry.getHistory().isTransferredRootNode())
      iubmbEnzymeDTO.setLatestHistoryEventClass(EventConstant.CREATION.toString());
    if (enzymeEntry.getHistory() != null && enzymeEntry.getHistory().getRootNode() != null)
      iubmbEnzymeDTO.setHistoryLine(enzymeEntry.getHistory().getRootNode().getHistoryLine());
    LOGGER.debug("... IubmbEnzymeDTO populated.");
  }

  private List getNamesList(List names, SpecialCharacters encoding, EncodingType encodingType) {
    assert names != null : "Parameter 'names' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List namesList = new ArrayList();
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeName enzymeName = (EnzymeName) names.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(enzymeName.getView().toString())) {
        EnzymeNameDTO enzymeNameDTO = new EnzymeNameDTO();
        enzymeNameDTO.setName(encoding.xml2Display(enzymeName.getName().toString(), encodingType));
        enzymeNameDTO.setSource(enzymeName.getSource().toString());
        enzymeNameDTO.setView(enzymeName.getView().toString());
        enzymeNameDTO.setQualifier(enzymeName.getQualifier().toString());
        enzymeNameDTO.setQualifierDisplay(enzymeName.getQualifier().toHTML(encoding.xml2Display(enzymeName.getName(), encodingType)));
        enzymeNameDTO.setType(enzymeName.getType().toString());
        namesList.add(enzymeNameDTO);
      }
    }

    return namesList;
  }

  private List getReactionsList(EnzymaticReactions reactions, SpecialCharacters encoding, EncodingType encodingType) {
    assert reactions != null : "Parameter 'reactions' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List reactionsList = new ArrayList();
    if (reactions != null) for (int iii = 0; iii < reactions.size(); iii++) {
      Reaction reaction = reactions.getReaction(iii);
      if (reaction.getId() > Reaction.NO_ID_ASSIGNED)
    	  continue; // Ignore Rhea-ctions
      if (EnzymeViewConstant.isInIUBMBView(reactions.getReactionView(iii).toString())) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setXmlTextualRepresentation(reaction.getTextualRepresentation());
        reactionDTO.setTextualRepresentation(IntEnzUtilities.linkMarkedEC(
            EnzymeReactionHelper.textualRepresentationToHTML(reaction, encoding, encodingType), false));
        reactionDTO.setSource(reaction.getSource().toString());
        reactionDTO.setView(reactions.getReactionView(iii).toString());
        reactionsList.add(reactionDTO);
      }
    }

    return reactionsList;
  }

  private EnzymeNameDTO getSystematicNameDTO(EnzymeName systematicName, SpecialCharacters encoding,
                                             EncodingType encodingType) {
    EnzymeNameDTO systematicNameDTO = new EnzymeNameDTO();
    if (systematicName.getName().equals("")) {
      systematicNameDTO.setType(EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString());
    } else {
      systematicNameDTO.setName(encoding.xml2Display(systematicName.getName().toString(), encodingType));
      systematicNameDTO.setSource(systematicName.getSource().toString());
      systematicNameDTO.setView(systematicName.getView().toString());
      systematicNameDTO.setType(systematicName.getType().toString());
    }
    return systematicNameDTO;
  }

  private List getLinksList(SortedSet links, String ec, SpecialCharacters encoding, EncodingType encodingType) {
    assert links != null : "Parameter 'links' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List linksList = new ArrayList();
    for (Iterator it = links.iterator(); it.hasNext();) {
      EnzymeLink enzymeLink = (EnzymeLink) it.next();
      if (EnzymeViewConstant.isInIUBMBView(enzymeLink.getView().toString())) {
        EnzymeLinkDTO enzymeLinkDTO = new EnzymeLinkDTO();
        enzymeLinkDTO.setDatabaseName(enzymeLink.getXrefDatabaseConstant().getDisplayName());
        enzymeLinkDTO.setDatabaseCode(enzymeLink.getXrefDatabaseConstant().getDatabaseCode());
        enzymeLinkDTO.setSource(enzymeLink.getSource().toString());
        enzymeLinkDTO.setUrl(enzymeLink.getFullUrl(ec));
        enzymeLinkDTO.setName(encoding.xml2Display(enzymeLink.getName(), encodingType));
        enzymeLinkDTO.setAccession(enzymeLink.getAccession());
        enzymeLinkDTO.setView(enzymeLink.getView().toString());
        if (enzymeLink.getDataComment() != null)
        	enzymeLinkDTO.setDataComment(encoding.xml2Display(enzymeLink.getDataComment()));
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
      EnzymeComment comment = (EnzymeComment) comments.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(comment.getView().toString())) {
        CommentDTO commentDTO = new CommentDTO();
        // Some comments might contain legacy links to Gerry Moss's web pages. These have to be corrected.
        commentDTO.setComment(IntEnzUtilities.linkMarkedEC(encoding.xml2Display(comment.getCommentText(), encodingType), true));
        commentDTO.setSource(comment.getSource().toString());
        commentDTO.setView(comment.getView().toString());
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
      Reference reference = (Reference) references.get(iii);
      if (EnzymeViewConstant.isInIUBMBView(reference.getView().toString())) {
        ReferenceDTO referenceDTO = new ReferenceDTO();
        referenceDTO.setPubId("" + reference.getPubId());
        referenceDTO.setAuthors(encoding.xml2Display(reference.getAuthors(), encodingType));
        referenceDTO.setTitle(encoding.xml2Display(reference.getTitle(), encodingType));
        referenceDTO.setYear(reference.getYear());
        referenceDTO.setView(reference.getView().toString());
        referenceDTO.setSource(reference.getSource().toString());

        if (reference instanceof Journal) {
          Journal journal = (Journal) reference;
          referenceDTO.setType("J");
          referenceDTO.setPubName(encoding.xml2Display(journal.getPubName(), encodingType));
          referenceDTO.setFirstPage(journal.getFirstPage());
          referenceDTO.setLastPage(journal.getLastPage());
          referenceDTO.setVolume(journal.getVolume());
          referenceDTO.setPubMedId(journal.getPubMedId());
          referenceDTO.setMedlineId(journal.getMedlineId());
        }
        if (reference instanceof Book) {
          Book book = (Book) reference;
          referenceDTO.setType("B");
          referenceDTO.setPubName(encoding.xml2Display(book.getPubName(), encodingType));
          referenceDTO.setFirstPage(book.getFirstPage());
          referenceDTO.setLastPage(book.getLastPage());
          referenceDTO.setEdition(encoding.xml2Display(book.getEdition(false), encodingType));
          referenceDTO.setEditor(encoding.xml2Display(book.getEditor(false), encodingType));
          referenceDTO.setVolume(book.getVolume());
          referenceDTO.setPublisher(encoding.xml2Display(book.getPublisher(), encodingType));
          referenceDTO.setPublisherPlace(encoding.xml2Display(book.getPublisherPlace(), encodingType));
        }
        if (reference instanceof Patent) {
          Patent patent = (Patent) reference;
          referenceDTO.setPubName(encoding.xml2Display(patent.getPatentNumber(), encodingType));
          referenceDTO.setXmlPubName(patent.getPatentNumber());
          referenceDTO.setType("P");
          referenceDTO.setPubMedId(patent.getPatentNumber());
        }
        referencesList.add(referenceDTO);
      }
    }

    return referencesList;
  }

}
