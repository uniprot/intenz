package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.DataComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.domain.history.HistoryNode;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import uk.ac.ebi.intenz.webapp.dtos.CofactorDTO;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.DataCommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;
import uk.ac.ebi.intenz.webapp.helper.EnzymeReactionHelper;
import uk.ac.ebi.intenz.webapp.utilities.AutoGrowingList;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.intenz.webapp.utilities.UnitOfWork;
import uk.ac.ebi.rhea.domain.SimpleReaction;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.10 $ $Date: 2008/06/09 10:28:39 $
 */
public class PopulateIntEnzEnzymeDTOAction extends Action {
  private static final XCharsASCIITranslator TRANSLATOR = XCharsASCIITranslator.getInstance();

  private static final Logger LOGGER = Logger.getLogger(PopulateIntEnzEnzymeDTOAction.class);
  private static final String INTENZ_ENTRY_JSP_FWD = "intenz_entry";
  private static final String DELETED_INTENZ_ENTRY_JSP_FWD = "deleted_intenz_entry";
  private static final String TRANSFERRED_INTENZ_ENTRY_JSP_FWD = "transferred_intenz_entry";

    @Override
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateIntEnzEnzymeDTOAction");
     String xcharsView = getXcharsView(request);
     // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    final EnzymeEntry enzymeEntry = (EnzymeEntry) request.getAttribute("result");
    populateForm(enzymeEntry, form, encoding, null, false, xcharsView);
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");

    // Check if an entry had to be reloaded and forward to the page where this entry has been displayed before
    // it expired.
    boolean reload = false;
    if (request.getParameter("reload") != null) reload = request.getParameter("reload").equals("") ? false : true;
    if (reload) {
      if (request.getAttribute("forwardPath") != null) {
        // Register form (instance of EnzymeDTO).
        unitOfWork.register((EnzymeDTO) form);
        return new ActionForward((String) request.getAttribute("forwardPath"));
      }
    }

    // Register form (instance of EnzymeDTO).
    unitOfWork.register((EnzymeDTO) form);
    if (enzymeEntry.getHistory().isDeletedRootNode()) return mapping.findForward(DELETED_INTENZ_ENTRY_JSP_FWD);
    if (enzymeEntry.getHistory().isTransferredRootNode()) return mapping.findForward(TRANSFERRED_INTENZ_ENTRY_JSP_FWD);
    return mapping.findForward(INTENZ_ENTRY_JSP_FWD);
  }

   private String getXcharsView (HttpServletRequest request) {
      String xcharsViewString = request.getParameter("xcharsView");
      if (xcharsViewString == null) xcharsViewString = "false";
     // if(xcharsViewString != null && xcharsViewString.indexOf("true")!=-1)
     //   xcharsView = true;
      return xcharsViewString;
   }

   private void populateForm(EnzymeEntry enzymeEntry, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType, boolean translate, String xcharsView) {
    LOGGER.debug("Populating EnzymeDTO ...");
    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
    enzymeDTO.setXcharsView(xcharsView);
    enzymeDTO.setActive(enzymeEntry.isActive());
    enzymeDTO.setId(enzymeEntry.getId().toString());
    enzymeDTO.setEc(enzymeEntry.getEc().toString());
    enzymeDTO.setCommonNames(getNamesList(enzymeEntry.getCommonNames(), encoding, encodingType, translate));
    enzymeDTO.setSystematicName(getSystematicNameDTO(enzymeEntry.getSystematicName(), encoding, encodingType));
    enzymeDTO.setSynonyms(getNamesList(enzymeEntry.getSynonyms(), encoding, encodingType, translate));
    enzymeDTO.setReactionDtos(getReactionsList(enzymeEntry.getEnzymaticReactions(), encoding, encodingType, translate));
    enzymeDTO.setCofactors(getCofactorsList(enzymeEntry.getCofactors(), encoding, encodingType, translate));
    enzymeDTO.setLinks(getLinksList(enzymeEntry.getLinks(), false, enzymeEntry.getEc().toString(), encoding, encodingType));
    enzymeDTO.setUniProtLinks(getLinksList(enzymeEntry.getLinks(), true, enzymeEntry.getEc().toString(), encoding, encodingType));
    enzymeDTO.setComments(getCommentsList(enzymeEntry.getComments(), encoding, encodingType));
    enzymeDTO.setReferences(getReferencesList(enzymeEntry.getReferences(), encoding, encodingType));
    enzymeDTO.setNote(encoding.xml2Display(enzymeEntry.getNote(), encodingType));
    enzymeDTO.setHistoryLine(enzymeEntry.getHistory().getRootNode().getHistoryLine());
    final HistoryEvent latestHistoryEventOfRoot = enzymeEntry.getHistory().getLatestHistoryEventOfRoot();
    enzymeDTO.setLatestHistoryEventNote(latestHistoryEventOfRoot.getNote());
    enzymeDTO.setLatestHistoryEventClass(latestHistoryEventOfRoot.getEventClass().toString());
    // Check whether this node has been transferred (set event class to 'TRA') or
    // if it is the new enzyme in a transfer process (set event class NOT to 'TRA' but 'NEW').
//    if (latestHistoryEventOfRoot.getEventClass() == EventConstant.TRANSFER &&
//        !enzymeEntry.getHistory().isTransferredRootNode())
//      enzymeDTO.setLatestHistoryEventClass(EventConstant.CREATION.toString());
    enzymeDTO.setLatestHistoryEventGroupId(enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getGroupId().toString());
    enzymeDTO.setLatestHistoryEventId(enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getEventId().toString());
    if (enzymeEntry.getHistory().isTransferredRootNode()){
        String transferredToEc = enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getAfterNode().getEnzymeEntry().getEc().toString();
        if (!enzymeDTO.getEc().equals(transferredToEc))
            enzymeDTO.setTransferredToEc(transferredToEc);
    } else {
        enzymeDTO.setTransferredToEc(null);
    }
    final HistoryNode beforeNode = enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getBeforeNode();
    Long beforeId = null;
    if (beforeNode != null) beforeId = beforeNode.getEnzymeEntry().getId();
    if (beforeId != null) enzymeDTO.setLatestHistoryBeforeId(beforeId.toString());
    final HistoryNode afterNode = enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getAfterNode();
    Long afterId = null;
    if (afterNode != null) afterId = afterNode.getEnzymeEntry().getId();
    if (afterId != null) enzymeDTO.setLatestHistoryAfterId(afterId.toString());
    
    enzymeDTO.setStatusCode(enzymeEntry.getStatus().getCode());
    enzymeDTO.setStatusText(enzymeEntry.getStatus().toString());
    enzymeDTO.setSource(enzymeEntry.getSource().toString());
    enzymeDTO.setClassEc("" + enzymeEntry.getEc().getEc1());
    enzymeDTO.setClassName(enzymeEntry.getClassName());
    enzymeDTO.setSubclassEc(enzymeEntry.getEc().getEc1() + "." + enzymeEntry.getEc().getEc2());
    enzymeDTO.setSubclassName(enzymeEntry.getSubclassName());
    enzymeDTO.setSubSubclassEc(enzymeEntry.getEc().getEc1() + "." + enzymeEntry.getEc().getEc2() + "." +
                               enzymeEntry.getEc().getEc3());
    enzymeDTO.setSubSubclassName(enzymeEntry.getSubSubclassName());
    LOGGER.debug("... EnzymeDTO populated.");
  }
   
private List getNamesList(List names, SpecialCharacters encoding, EncodingType encodingType, boolean translate) {
    assert names != null : "Parameter 'names' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List namesList = new AutoGrowingList(EnzymeNameDTO.class);
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeName enzymeName = (EnzymeName) names.get(iii);
      EnzymeNameDTO enzymeNameDTO = new EnzymeNameDTO();
      if (translate)
        enzymeNameDTO.setName(encoding.xml2Display(TRANSLATOR.toASCII(enzymeName.getName(), false, true), encodingType));
      else
        enzymeNameDTO.setName(encoding.xml2Display(enzymeName.getName().toString(), encodingType));
      enzymeNameDTO.setXmlName(enzymeName.getName().toString());
      enzymeNameDTO.setOrderIn("" + (iii + 1));
      enzymeNameDTO.setSource(enzymeName.getSource().toString());
      enzymeNameDTO.setSourceDisplay(enzymeName.getSource().toDisplayString());
      enzymeNameDTO.setView(enzymeName.getView().toString());
      enzymeNameDTO.setViewDisplayString(enzymeName.getView().toDisplayString());
      enzymeNameDTO.setViewDisplayImage(enzymeName.getView().toDisplayImage());
      enzymeNameDTO.setQualifier(enzymeName.getQualifier().toString());
      enzymeNameDTO.setQualifierDisplay(enzymeName.getQualifier().toHTML(encoding.xml2Display(enzymeName.getName(), null)));
      enzymeNameDTO.setType(enzymeName.getType().toString());
      namesList.add(enzymeNameDTO);
    }

    return namesList;
  }

  private List getReactionsList(EnzymaticReactions reactions, SpecialCharacters encoding, EncodingType encodingType,
                                boolean translate) {
    assert reactions != null : "Parameter 'reactions' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List reactionsList = new AutoGrowingList(ReactionDTO.class);
    if (reactions != null) for (int iii = 0; iii < reactions.size(); iii++) {
      Reaction reaction = reactions.getReaction(iii);
      ReactionDTO reactionDTO = new ReactionDTO();
      reactionDTO.setTextualRepresentation(IntEnzUtilities.linkMarkedEC(
        EnzymeReactionHelper.textualRepresentationToHTML(reaction, encoding, encodingType), false));
      StringBuilder xmlTextualRepresentation = new StringBuilder(reaction.getTextualRepresentation());
      if (reaction.isComplex()){
          for (SimpleReaction sr : reaction.getChildren()) {
              xmlTextualRepresentation.append('\n')
                      .append(sr.getReaction().getTextualRepresentation());
          }
      }
      reactionDTO.setXmlTextualRepresentation(xmlTextualRepresentation.toString());
      reactionDTO.setOrderIn("" + (iii + 1));
      reactionDTO.setSource(reaction.getSource().toString());
      reactionDTO.setSourceDisplay(reaction.getSource().getName());
      reactionDTO.setView(reactions.getReactionView(iii).toString());
      reactionDTO.setViewDisplayImage(reactions.getReactionView(iii).toDisplayImage());
      reactionDTO.setViewDisplayString(reactions.getReactionView(iii).toDisplayString());
      reactionDTO.setStatus(reaction.getStatus().toString());
      reactionDTO.setId(reaction.getId());
      reactionsList.add(reactionDTO);
    }

    return reactionsList;
  }

  private EnzymeNameDTO getSystematicNameDTO(EnzymeName systematicName, SpecialCharacters encoding,
                                             EncodingType encodingType) {
    EnzymeNameDTO systematicNameDTO = new EnzymeNameDTO();
    if (systematicName == null || systematicName.getName().equals("")) {
      systematicNameDTO.setName("");
      systematicNameDTO.setXmlName("");
      systematicNameDTO.setSource(EnzymeSourceConstant.INTENZ.toString());
      systematicNameDTO.setSourceDisplay(EnzymeSourceConstant.INTENZ.toDisplayString());
      systematicNameDTO.setView(EnzymeViewConstant.INTENZ.toString());
      systematicNameDTO.setViewDisplayImage(EnzymeViewConstant.INTENZ.toDisplayImage());
      systematicNameDTO.setViewDisplayString(EnzymeViewConstant.INTENZ.toDisplayString());
      systematicNameDTO.setType(EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString());
    } else {
      systematicNameDTO.setName(encoding.xml2Display(systematicName.getName().toString(), encodingType));
      systematicNameDTO.setXmlName(systematicName.getName().toString());
      systematicNameDTO.setSource(systematicName.getSource().toString());
      systematicNameDTO.setSourceDisplay(systematicName.getSource().toDisplayString());
      systematicNameDTO.setView(systematicName.getView().toString());
      systematicNameDTO.setViewDisplayImage(systematicName.getView().toDisplayImage());
      systematicNameDTO.setViewDisplayString(systematicName.getView().toDisplayString());
      systematicNameDTO.setType(EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString());
    }
    return systematicNameDTO;
  }

  private List<CofactorDTO> getCofactorsList(Set<Object> cofactors, SpecialCharacters encoding,
		  EncodingType encodingType, boolean translate) {
    assert cofactors != null : "Parameter 'cofactors' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List<CofactorDTO> cofactorsList = new AutoGrowingList(CofactorDTO.class);
    int iii = 1;
    for (Object o : cofactors) {
    	CofactorDTO dto = new CofactorDTO();
    	if (o instanceof Cofactor){
    		Cofactor c = (Cofactor) o;
    		dto.setCompoundId(c.getCompound().getId().toString());
    		dto.setAccession(c.getCompound().getAccession());
    		dto.setXmlCofactorValue(c.getCompound().getName());
    		dto.setSource(c.getSource().toString());
    		dto.setView(c.getView().toString());
    	} else if (o instanceof OperatorSet){
    		OperatorSet os = (OperatorSet) o;
    		try {
				dto.setCompoundId(os.toString("compound.id"));
	    		dto.setAccession(os.toString("compound.accession"));
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
    		dto.setXmlCofactorValue(os.toString());
    		// For source and view, take one of them (they are all the same):
    		Cofactor c = null;
    		OperatorSet loopOs = os;
    		while (c == null){
    			Object obj = loopOs.iterator().next();
    			if (obj instanceof Cofactor) c = (Cofactor) obj;
    			else loopOs = (OperatorSet) obj;
    		}
    		dto.setSource(c.getSource().toString());
    		dto.setView(c.getView().toString());
    	}
    	cofactorsList.add(dto);
    }

    return cofactorsList;
  }
  
  private List getLinksList(SortedSet links, boolean getUniProtLinksOnly, String ec, SpecialCharacters encoding,
                            EncodingType encodingType) {
    assert links != null : "Parameter 'links' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List linksList = new AutoGrowingList(EnzymeLinkDTO.class);
    List uniProtLinksList = new AutoGrowingList(EnzymeLinkDTO.class);
    for (Iterator it = links.iterator(); it.hasNext();) {
      EnzymeLink enzymeLink = (EnzymeLink) it.next();
      if (!getUniProtLinksOnly &&
          enzymeLink.getXrefDatabaseConstant().getDatabaseCode().equals(XrefDatabaseConstant.SWISSPROT.getDatabaseCode()))
        continue;
      EnzymeLinkDTO enzymeLinkDTO = new EnzymeLinkDTO();
      enzymeLinkDTO.setDatabaseName(enzymeLink.getXrefDatabaseConstant().getDisplayName());
      enzymeLinkDTO.setDatabaseCode(enzymeLink.getXrefDatabaseConstant().getDatabaseCode());
      enzymeLinkDTO.setSource(enzymeLink.getSource().toString());
      enzymeLinkDTO.setSourceDisplay(enzymeLink.getSource().toDisplayString());
      enzymeLinkDTO.setUrl(enzymeLink.getFullUrl(ec));
      enzymeLinkDTO.setName(encoding.xml2Display(enzymeLink.getName(), encodingType));
      enzymeLinkDTO.setAccession(enzymeLink.getAccession());
      enzymeLinkDTO.setView(enzymeLink.getView().toString());
      enzymeLinkDTO.setViewDisplayImage(enzymeLink.getView().toDisplayImage());
      enzymeLinkDTO.setViewDisplayString(enzymeLink.getView().toDisplayString());
      enzymeLinkDTO.setDataComment(getDataCommentDTO(enzymeLink.getDataComment(), encoding, encodingType));

      if (getUniProtLinksOnly) {
        if (enzymeLinkDTO.getDatabaseCode().equals(XrefDatabaseConstant.SWISSPROT.getDatabaseCode())) {
          uniProtLinksList.add(enzymeLinkDTO);
        }
        continue;
      }

      if (enzymeLinkDTO.getDatabaseCode().equals(XrefDatabaseConstant.SWISSPROT.getDatabaseCode())) continue;
      linksList.add(enzymeLinkDTO);
    }

    return getUniProtLinksOnly ? uniProtLinksList : linksList;
  }

    private DataCommentDTO getDataCommentDTO(DataComment dataComment, SpecialCharacters encoding, EncodingType encodingType){
        DataCommentDTO dataCommentDTO = new DataCommentDTO();
        if (dataComment != null){
            if (dataComment.getId() != null) dataCommentDTO.setId(dataComment.getId().toString());
            dataCommentDTO.setXmlComment(dataComment.getComment());
            dataCommentDTO.setDisplayComment(encoding.xml2Display(dataComment.getComment(), encodingType));
        }
        return dataCommentDTO;
    }

    private List getCommentsList(List comments, SpecialCharacters encoding, EncodingType encodingType) {
    assert comments != null : "Parameter 'comments' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List commentsList = new AutoGrowingList(CommentDTO.class);
    for (int iii = 0; iii < comments.size(); iii++) {
      EnzymeComment comment = (EnzymeComment) comments.get(iii);
      CommentDTO commentDTO = new CommentDTO();
      // Some comments might contain legacy links to Gerry Moss's web pages. These have to be corrected.
      if (comment.getView().isInSIBView()){
          commentDTO.setComment(encoding.xml2Display(comment.getCommentText(), encodingType));
      } else {
          commentDTO.setComment(IntEnzUtilities.linkMarkedEC(encoding.xml2Display(comment.getCommentText(), encodingType), false));
      }
      commentDTO.setXmlComment(comment.getCommentText());
      commentDTO.setOrderIn("" + (iii + 1));
      commentDTO.setSource(comment.getSource().toString());
      commentDTO.setSourceDisplay(comment.getSource().toDisplayString());
      commentDTO.setView(comment.getView().toString());
      commentDTO.setViewDisplayImage(comment.getView().toDisplayImage());
      commentDTO.setViewDisplayString(comment.getView().toDisplayString());
      commentsList.add(commentDTO);
    }

    return commentsList;
  }

  private List getReferencesList(List references, SpecialCharacters encoding, EncodingType encodingType) {
    assert references != null : "Parameter 'references' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";
    assert encodingType != null : "Parameter 'encodingType' must not be null.";

    List referencesList = new AutoGrowingList(ReferenceDTO.class);
    for (int iii = 0; iii < references.size(); iii++) {
      Reference reference = (Reference) references.get(iii);
      ReferenceDTO referenceDTO = new ReferenceDTO();
      referenceDTO.setPubId("" + reference.getPubId());
      referenceDTO.setAuthors(encoding.xml2Display(reference.getAuthors(), encodingType));
      referenceDTO.setXmlAuthors(reference.getAuthors());
      referenceDTO.setTitle(encoding.xml2Display(reference.getTitle(), encodingType));
      referenceDTO.setXmlTitle(reference.getTitle());
      referenceDTO.setYear(reference.getYear());
      referenceDTO.setView(reference.getView().toString());
      referenceDTO.setViewDisplayImage(reference.getView().toDisplayImage());
      referenceDTO.setViewDisplayString(reference.getView().toDisplayString());
      referenceDTO.setSource(reference.getSource().toString());
      referenceDTO.setSourceDisplay(reference.getSource().toDisplayString());

      if (reference instanceof Journal) {
        Journal journal = (Journal) reference;
        referenceDTO.setType("J");
        referenceDTO.setPubName(encoding.xml2Display(journal.getPubName(), encodingType));
        referenceDTO.setXmlPubName(journal.getPubName());
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
        referenceDTO.setXmlPubName(book.getPubName());
        referenceDTO.setFirstPage(book.getFirstPage());
        referenceDTO.setLastPage(book.getLastPage());
        referenceDTO.setEdition(encoding.xml2Display(book.getEdition(false), encodingType));
        referenceDTO.setEditor(encoding.xml2Display(book.getEditor(false), encodingType));
        referenceDTO.setXmlEditor(book.getEditor(false));
        referenceDTO.setVolume(book.getVolume());
        referenceDTO.setPublisher(encoding.xml2Display(book.getPublisher(), encodingType));
        referenceDTO.setXmlPublisher(book.getPublisher());
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

    return referencesList;
  }

}
