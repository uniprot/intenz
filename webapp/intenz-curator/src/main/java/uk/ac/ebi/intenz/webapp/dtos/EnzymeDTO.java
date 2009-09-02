package uk.ac.ebi.intenz.webapp.dtos;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.validator.ValidatorForm;

import uk.ac.ebi.biobabel.validator.DbIdentifierValidator;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.webapp.utilities.AutoGrowingList;

/**
 * This ActionForm stores all enzyme properties in a <code>Map</code> since the number of properties needs to
 * be dynamically adjustable.
 * <p/>
 * The enzyme properties will be stored as follows:
 * <p/>
 * <table border="1" style="border-collapse:collapse;" cellpadding="5">
 * <tr>
 * <td align="center" style="border-width:1px;"><b><code>KEY</code></b></td>
 * <td align="center" style="border-width:1px;"><b><code>VALUE TYPE</code></b></td>
 * <td align="center" style="border-width:1px;"><b><code>Description</code></b></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>ec</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>The enzyme's EC number.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>commonNames</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{name, source}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeName}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>reactions</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{textualRepresentation, source}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.Reaction}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>systematicName</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>The enzyme's systematic name.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>synonyms</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{name, qualifier, source, orderIn}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeName}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>cofactors</code></td>
 * <td align="center" style="border-width:1px;"><code>String[]</code></td>
 * <td style="border-width:1px;"><code>String[]</code> of cofactors (Strings containing the 'cofactorValue').<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.Cofactor}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>links</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{source, specificUrl, accession, name}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeLink}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>comments</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{commentText, source}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeComment}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>references</code></td>
 * <td align="center" style="border-width:1px;"><code>String[][]</code></td>
 * <td style="border-width:1px;"><code>String[][]</code> containing [index]{pubId, authors, title, year,
 * pubName, firstPage, lastPage, edition, editor, volume, publisher, publisherPlace, pubMedId, medlineId, patentNumber}.<br/>
 * See also {@link uk.ac.ebi.intenz.domain.reference}.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>note</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>A note which might have been added by a curator.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>historyLine</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>The enzyme's history line.</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>status</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>The enzyme's status (code).</code></td>
 * </tr>
 * <tr>
 * <td align="center" style="border-width:1px;"><code>source</code></td>
 * <td align="center" style="border-width:1px;"><code>String</code></td>
 * <td style="border-width:1px;"><code>The enzyme's source (code).</code></td>
 * </tr>
 * </table>
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/03/12 12:29:16 $
 */
public class EnzymeDTO extends ValidatorForm {
  // ------------------------------ FIELDS ------------------------------

  private Integer uowId;

  private boolean isActive;
  private String id;
  private String ec;
  private String transferredEc;
  private String transferredToEc;
  private EnzymeNameDTO systematicName;
  private String note;
  private String historyLine;
  private String latestHistoryEventNote;
  private String latestHistoryEventClass;
  private String latestHistoryEventGroupId;
  private String latestHistoryEventId;
  private String latestHistoryBeforeId;
  private String latestHistoryAfterId;
  private String statusCode;
  private String statusText;
  private String source;

  private String className;
  private String classEc;
  private String subclassName;
  private String subclassEc;
  private String subSubclassName;
  private String subSubclassEc;

  private List commonNames;
  private List<ReactionDTO> reactionDtos;
  private List synonyms;
  private List<CofactorDTO> cofactors;
  private List links;
  private List uniProtLinks;
  private List comments;
  private List<ReferenceDTO> references;

   private String xcharsView;

  // --------------------------- CONSTRUCTORS ---------------------------

  public String getXcharsView () {
      return xcharsView;
   }

   public void setXcharsView (String xcharsView) {
      this.xcharsView = xcharsView;
   }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public EnzymeDTO() {
    isActive = false;
    id = "";
    ec = "";
    systematicName = new EnzymeNameDTO();
    systematicName.setName("");
    systematicName.setXmlName("");
    systematicName.setSource(EnzymeSourceConstant.INTENZ.toString());
    systematicName.setSourceDisplay(EnzymeSourceConstant.INTENZ.toDisplayString());
    systematicName.setView(EnzymeViewConstant.IUBMB_INTENZ.toString());
    systematicName.setViewDisplayImage(EnzymeViewConstant.IUBMB_INTENZ.toDisplayImage());
    systematicName.setViewDisplayString(EnzymeViewConstant.IUBMB_INTENZ.toDisplayString());
    systematicName.setType(EnzymeNameTypeConstant.SYSTEMATIC_NAME.toString());
    note = "";
    historyLine = "";
    latestHistoryEventNote = "";
    latestHistoryEventClass = "";
    latestHistoryEventGroupId = "";
    latestHistoryEventId = "";
    latestHistoryBeforeId = "";
    latestHistoryAfterId = "";
    statusCode = "SU";
    statusText = "suggested";
    source = "INTENZ";

    className = "";
    classEc = "";
    subclassName = "";
    subclassEc = "";
    subSubclassName = "";
    subSubclassEc = "";
    xcharsView = "false";

    commonNames = new AutoGrowingList(EnzymeNameDTO.class);
    reactionDtos = new AutoGrowingList(ReactionDTO.class);
    synonyms = new AutoGrowingList(EnzymeNameDTO.class);
    cofactors = new AutoGrowingList(CofactorDTO.class);
    links = new AutoGrowingList(EnzymeLinkDTO.class);
    uniProtLinks = new AutoGrowingList(EnzymeLinkDTO.class);
    comments = new AutoGrowingList(CommentDTO.class);
    references = new AutoGrowingList(ReferenceDTO.class);
  }

  public EnzymeDTO(EnzymeDTO enzymeDTO) {
    setUowId(enzymeDTO.getUowId());
    setId(enzymeDTO.getId());
    setEc(enzymeDTO.getEc());
    setTransferredEc(enzymeDTO.getTransferredEc());
    setSystematicName(new EnzymeNameDTO(enzymeDTO.getSystematicName()));
    setNote(enzymeDTO.getNote());
    setHistoryLine(enzymeDTO.getHistoryLine());
    setLatestHistoryEventNote(enzymeDTO.getLatestHistoryEventNote());
    setLatestHistoryEventClass(enzymeDTO.getLatestHistoryEventClass());
    setLatestHistoryEventGroupId(enzymeDTO.getLatestHistoryEventGroupId());
    setLatestHistoryEventId(enzymeDTO.getLatestHistoryEventId());
    setStatusCode(enzymeDTO.getStatusCode());
    setStatusText(enzymeDTO.getStatusText());
    setSource(enzymeDTO.getSource());
    setClassName(enzymeDTO.getClassName());
    setClassEc(enzymeDTO.getClassEc());
    setSubclassName(enzymeDTO.getSubclassName());
    setSubclassEc(enzymeDTO.getSubclassEc());
    setSubSubclassName(enzymeDTO.getSubSubclassName());
    setSubSubclassEc(enzymeDTO.getSubSubclassEc());
    setXcharsView(enzymeDTO.getXcharsView());

    List commonNames = enzymeDTO.getCommonNames();
    List newCommonNames = new AutoGrowingList(EnzymeNameDTO.class);
    for (int iii = 0; iii < commonNames.size(); iii++) {
      EnzymeNameDTO commonName = (EnzymeNameDTO) commonNames.get(iii);
      newCommonNames.add(new EnzymeNameDTO(commonName));
    }
    setCommonNames(newCommonNames);

    List reactions = enzymeDTO.getReactionDtos();
    List newReactions = new AutoGrowingList(ReactionDTO.class);
    for (int iii = 0; iii < reactions.size(); iii++) {
      ReactionDTO reaction = (ReactionDTO) reactions.get(iii);
      newReactions.add(new ReactionDTO(reaction));
    }
    setReactionDtos(newReactions);

    List synonyms = enzymeDTO.getSynonyms();
    List newSynonyms = new AutoGrowingList(EnzymeNameDTO.class);
    for (int iii = 0; iii < synonyms.size(); iii++) {
      EnzymeNameDTO synonym = (EnzymeNameDTO) synonyms.get(iii);
      newSynonyms.add(new EnzymeNameDTO(synonym));
    }
    setSynonyms(newSynonyms);

    List<CofactorDTO> cofactors = enzymeDTO.getCofactors();
    List<CofactorDTO> newCofactors = new AutoGrowingList(CofactorDTO.class);
    for (int iii = 0; iii < cofactors.size(); iii++) {
      CofactorDTO cofactor = cofactors.get(iii);
      newCofactors.add(new CofactorDTO(cofactor));
    }
    setCofactors(newCofactors);

    List links = enzymeDTO.getLinks();
    List newLinks = new AutoGrowingList(EnzymeLinkDTO.class);
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO link = (EnzymeLinkDTO) links.get(iii);
      newLinks.add(new EnzymeLinkDTO(link));
    }
    setLinks(newLinks);

    List uniProtLinks = enzymeDTO.getUniProtLinks();
    List newUniprotLinks = new AutoGrowingList(EnzymeLinkDTO.class);
    for (int iii = 0; iii < uniProtLinks.size(); iii++) {
      EnzymeLinkDTO link = (EnzymeLinkDTO) uniProtLinks.get(iii);
      newUniprotLinks.add(new EnzymeLinkDTO(link));
    }
    setUniProtLinks(newUniprotLinks);

    List comments = enzymeDTO.getComments();
    List newComments = new AutoGrowingList(CommentDTO.class);
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO comment = (CommentDTO) comments.get(iii);
      newComments.add(new CommentDTO(comment));
    }
    setComments(newComments);

    List<ReferenceDTO> references = enzymeDTO.getReferences();
    List newReferences = new AutoGrowingList(ReferenceDTO.class);
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO reference = (ReferenceDTO) references.get(iii);
      newReferences.add(new ReferenceDTO(reference));
    }
    setReferences(newReferences);
  }

  // --------------------- GETTER / SETTER METHODS ---------------------

  public List getAllLinks() {
    List allLinks = new AutoGrowingList(EnzymeLinkDTO.class);
    allLinks.addAll(links);
    allLinks.addAll(uniProtLinks);
    return allLinks;
  }

  public String getClassEc() {
    return classEc;
  }

  public void setClassEc(String classEc) {
    this.classEc = classEc;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public List<CofactorDTO> getCofactors() {
    return cofactors;
  }

  public void setCofactors(List<CofactorDTO> cofactors) {
    this.cofactors = cofactors;
  }

  public List getComments() {
    return comments;
  }

  public void setComments(List comments) {
    this.comments = comments;
  }

  public List getCommonNames() {
    return commonNames;
  }

  public void setCommonNames(List commonNames) {
    this.commonNames = commonNames;
  }

  public String getEc() {
    return ec;
  }

  public void setEc(String ec) {
    this.ec = ec;
  }

  public String getHistoryLine() {
    return historyLine;
  }

  public void setHistoryLine(String historyLine) {
    this.historyLine = historyLine;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLatestHistoryAfterId() {
    return latestHistoryAfterId;
  }

  public void setLatestHistoryAfterId(String latestHistoryAfterId) {
    this.latestHistoryAfterId = latestHistoryAfterId;
  }

  public String getLatestHistoryBeforeId() {
    return latestHistoryBeforeId;
  }

  public void setLatestHistoryBeforeId(String latestHistoryBeforeId) {
    this.latestHistoryBeforeId = latestHistoryBeforeId;
  }

  public String getLatestHistoryEventClass() {
    return latestHistoryEventClass;
  }

  public void setLatestHistoryEventClass(String latestHistoryEventClass) {
    this.latestHistoryEventClass = latestHistoryEventClass;
  }

  public String getLatestHistoryEventGroupId() {
    return latestHistoryEventGroupId;
  }

  public void setLatestHistoryEventGroupId(String latestHistoryEventGroupId) {
    this.latestHistoryEventGroupId = latestHistoryEventGroupId;
  }

  public String getLatestHistoryEventId() {
    return latestHistoryEventId;
  }

  public void setLatestHistoryEventId(String latestHistoryEventId) {
    this.latestHistoryEventId = latestHistoryEventId;
  }

  public String getLatestHistoryEventNote() {
    return latestHistoryEventNote;
  }

  public void setLatestHistoryEventNote(String latestHistoryEventNote) {
    this.latestHistoryEventNote = latestHistoryEventNote;
  }

  public List getLinks() {
    return links;
  }

  public void setLinks(List links) {
    this.links = links;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public List<ReactionDTO> getReactionDtos() {
    return reactionDtos;
  }

  public void setReactionDtos(List<ReactionDTO> reactions) {
    this.reactionDtos = reactions;
  }

  public List<ReferenceDTO> getReferences() {
    return references;
  }

  public void setReferences(List<ReferenceDTO> references) {
    this.references = references;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public String getSubSubclassEc() {
    return subSubclassEc;
  }

  public void setSubSubclassEc(String subSubclassEc) {
    this.subSubclassEc = subSubclassEc;
  }

  public String getSubSubclassName() {
    return subSubclassName;
  }

  public void setSubSubclassName(String subSubclassName) {
    this.subSubclassName = subSubclassName;
  }

  public String getSubclassEc() {
    return subclassEc;
  }

  public void setSubclassEc(String subclassEc) {
    this.subclassEc = subclassEc;
  }

  public String getSubclassName() {
    return subclassName;
  }

  public void setSubclassName(String subclassName) {
    this.subclassName = subclassName;
  }

  public List getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List synonyms) {
    this.synonyms = synonyms;
  }

  public EnzymeNameDTO getSystematicName() {
    return systematicName;
  }

  public void setSystematicName(EnzymeNameDTO systematicName) {
    this.systematicName = systematicName;
  }

  public String getTransferredEc() {
    return transferredEc;
  }

  public void setTransferredEc(String transferredEc) {
    this.transferredEc = transferredEc;
  }

  public String getTransferredToEc() {
    return transferredToEc;
  }

  public void setTransferredToEc(String transferredToEc) {
    this.transferredToEc = transferredToEc;
  }

  public List getUniProtLinks() {
    return uniProtLinks;
  }

  public void setUniProtLinks(List uniProtLinks) {
    this.uniProtLinks = uniProtLinks;
  }

  // ------------------------------- GETTER & SETTER --------------------------------

  public Integer getUowId() {
    return uowId;
  }

  public void setUowId(Integer uowId) {
    this.uowId = uowId;
  }

  // -------------------------- OTHER METHODS --------------------------

  public Object getCofactor(int index) {
    return cofactors.toArray()[index];
  }

  public CommentDTO getComment(int index) {
    return (CommentDTO) comments.get(index);
  }

  public EnzymeNameDTO getCommonName(int index) {
    return (EnzymeNameDTO) commonNames.get(index);
  }

  public EnzymeLinkDTO getLink(int index) {
    return (EnzymeLinkDTO) links.get(index);
  }

  public ReactionDTO getReactionDto(int index) {
    return reactionDtos.get(index);
  }

  public ReferenceDTO getReference(int index) {
    return references.get(index);
  }

  public EnzymeNameDTO getSynonym(int index) {
    return (EnzymeNameDTO) synonyms.get(index);
  }

  public EnzymeLinkDTO getUniProtLink(int index) {
    return (EnzymeLinkDTO) uniProtLinks.get(index);
  }

  public List getLinks(String dbCode){
      List selectedLinks = new ArrayList();
      for (int i = 0; i < links.size(); i++){
          EnzymeLinkDTO link = (EnzymeLinkDTO) links.get(i);
          if (link.getDatabaseCode().equals(dbCode))
              selectedLinks.add(link);
      }
      return selectedLinks;
  }

  public List getGoLinks(){
      return getLinks(XrefDatabaseConstant.GO.getDatabaseCode());
  }

  public List getCasLinks(){
      return getLinks(XrefDatabaseConstant.CAS.getDatabaseCode());
  }

    @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
//    id = "";
  }

    @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = super.validate(mapping, request); // use Validator framework first
    errors.add(systematicName.validate(mapping, request));

    // Validate common names.
    validateCommonNames(errors, mapping, request);

    // Validate synonyms.
    validateSynonyms(errors, mapping, request);

    // Checks all names for uniqueness.
    validateAllNames(errors, request);

    // Validate reactions.
    validateReactions(errors, mapping, request);

    // Validate cofactors.
//    validateCofactors(errors, mapping, request);

    // Validate comments.
    validateComments(errors, mapping, request);

    // Validate links.
    validateLinks(errors, mapping, request);

    // Validate UniProt links.
    validateUniProtLinks(errors, mapping, request);

    // Validate references.
    validateReferences(errors, mapping, request);

    updateStatus();

    if (errors == null) errors = new ActionErrors();
    if (errors.isEmpty()) return null;

    // Keep token when an error occurs.
    if (request.getParameter(Constants.TOKEN_KEY) != null) request.setAttribute(Constants.TOKEN_KEY, request.getParameter(Constants.TOKEN_KEY));
    return errors;
  }

  /**
   * Validates the list of common names.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>checks the common name independently from the others (using its <code>validate()</code> method)</li>
   * <li>checks whether two common names have the view flag
   * {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant#INTENZ} attached.</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateCommonNames(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyNameItems(commonNames);
    removeDuplicates(commonNames);
    boolean intenzViewUsed = false;
    for (int iii = 0; iii < commonNames.size(); iii++) {
      // Validate common names individually.
      EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) commonNames.get(iii);
      errors.add(enzymeNameDTO.validate(mapping, request));
      // Validate dependencies.
      if (EnzymeViewConstant.isInIntEnzView(enzymeNameDTO.getView()) &&
          ((enzymeNameDTO.getName() != null && !enzymeNameDTO.getName().equals("")) ||
           (enzymeNameDTO.getXmlName() != null && !enzymeNameDTO.getXmlName().equals("")))) {
        if (intenzViewUsed) {
          ActionMessage message = new ActionMessage("errors.form.common_name.invalidView");
          errors.add("commonName", message);
        } else
          intenzViewUsed = true;
      }
    }
  }

  /**
   * Validates the list of synonyms.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>checks the synonym independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateSynonyms(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyNameItems(synonyms);
    removeDuplicates(synonyms);
    for (int iii = 0; iii < synonyms.size(); iii++) {
      EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) synonyms.get(iii);
      errors.add(enzymeNameDTO.validate(mapping, request));
    }
  }

  /**
   * Checks whether the list of all names contains only unique names.
   *
   * @param errors  Stores errors.
   * @param request Request object.
   */
  private void validateAllNames(ActionErrors errors, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    for (int iii = 0; iii < commonNames.size(); iii++) {
      EnzymeNameDTO commonName = (EnzymeNameDTO) commonNames.get(iii);
//      if (systematicName.getXmlName().equals(commonName.getXmlName())) {
//        ActionMessage message = new ActionMessage("errors.form.common_name.duplicate_sys_name", commonName.getName());
//        errors.add("commonName", message);
//        return;
//      }
      for (int jjj = 0; jjj < synonyms.size(); jjj++) {
        EnzymeNameDTO synonym = (EnzymeNameDTO) synonyms.get(jjj);
        if (systematicName.getXmlName().equals(synonym.getXmlName())) {
          ActionMessage message = new ActionMessage("errors.form.synonym.duplicate_sys_name", synonym.getName());
          errors.add("otherNames", message);
          return;
        }
        if (commonName.getXmlName().equals(synonym.getXmlName())) {
          ActionMessage message = new ActionMessage("errors.form.common_name.duplicate_synonym", commonName.getName());
          errors.add("commonName", message);
          return;
        }
      }
    }

  }

  /**
   * Validates the list of reactions.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>check if at least one reaction is present</li>
   * <li>checks the reaction independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateReactions(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyReactionItems();
    removeDuplicates(reactionDtos);
    if (reactionDtos.size() == 0) {
      ActionMessage message = new ActionMessage("errors.form.reaction.notExisting");
      errors.add("reaction", message);
      return;
    }
    for (int iii = 0; iii < reactionDtos.size(); iii++) {
      ReactionDTO reactionDTO = reactionDtos.get(iii);
      errors.add(reactionDTO.validate(mapping, request));
    }
  }

  /**
   * Validates the list of cofactors.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>checks the cofactor independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
//  private void validateCofactors(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
//    assert errors != null : "Parameter 'errors' must not be null.";
//    assert mapping != null : "Parameter 'mapping' must not be null.";
//    assert request != null : "Parameter 'request' must not be null.";
//
//    removeEmptyCofactorItems();
////    removeDuplicates(cofactors);
//    for (int iii = 0; iii < cofactors.size(); iii++) {
//      CofactorDTO cofactorDTO = (CofactorDTO) cofactors.get(iii);
//      errors.add(cofactorDTO.validate(mapping, request));
//    }
//  }

  /**
   * Validates the list of comments.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>checks whether a comment ends with a full stop</li>
   * <li>checks the comment independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateComments(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyCommentItems();
    removeDuplicates(comments);
    checkForPeriod(errors);
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO commentDTO = (CommentDTO) comments.get(iii);
      errors.add(commentDTO.validate(mapping, request));
    }
  }

  /**
   * Validates the list of links.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>updates static link values</li>
   * <li>checks the link independently from the others (using its <code>validate()</code> method)</li>
   * <li>validates CAS Registry number for existance (if available) and for correct format usage</li>
   * <li>validates if the MEROPS URL is working</li>
   * <li>validates if required OMIM link values (accession and name) are present</li>
   * <li>validates if the required PROSITE accession is present</li>
   * <li>validates if the required DIAGRAM name is present and whether the given link is working</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateLinks(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyLinkItems();
    removeLinkDuplicates();
    try {
      updateStaticLinks();
    } catch (DomainException e) {
      ActionMessage message = new ActionMessage(e.getMessageKey(), e.getProperty());
      errors.add("links", message);
    }
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      errors.add(enzymeLinkDTO.validate(mapping, request));
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.CAS.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("")) {
          ActionMessage error = new ActionMessage("errors.form.link.invalidCasValue");
          errors.add("links", error);
        }
//        if (!IntEnzValidations.validateCASRegistryNumberFormat(enzymeLinkDTO)) {
        if (!DbIdentifierValidator.getInstance().validate(enzymeLinkDTO.getAccession(), DbIdentifierValidator.CAS_REGISTRY_NUMBER)){
          ActionMessage error = new ActionMessage("errors.form.link.invalidCasFormat");
          errors.add("links", error);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.MEROPS.getDisplayName())) {
        try {
          URL url = new URL(enzymeLinkDTO.getUrl());
          URLConnection connection = url.openConnection();
          connection.connect();
        } catch (IOException e) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidMeropsUrl");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.MIM.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("") ||
            enzymeLinkDTO.getName() == null || enzymeLinkDTO.getName().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.MimIncomplete");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.PROSITE.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidPrositeValue");
          errors.add("links", message);
        } else {
        	enzymeLinkDTO.setUrl(XrefDatabaseConstant.PROSITE.getUrl() + enzymeLinkDTO.getAccession());
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.DIAGRAM.getDisplayName())) {
        if (enzymeLinkDTO.getName() == null || enzymeLinkDTO.getName().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramName");
          errors.add("links", message);
        }
        try {
          URL url = new URL(enzymeLinkDTO.getUrl());
          Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-proxy.ebi.ac.uk", 8080));
          HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
              ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramUrl");
              errors.add("links", message);        	  
          }
        } catch (IOException e) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramUrl");
          errors.add("links", message);
        }
      }
    }
  }

  /**
   * Validates the list of UniProt links.
   * <p/>
   * NB: UniProt links are generated automatically and do not contain any empty or duplicated values.
   * <p/>
   * <ol>
   * <li>checks the link independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateUniProtLinks(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    for (int iii = 0; iii < uniProtLinks.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) uniProtLinks.get(iii);
      errors.add(enzymeLinkDTO.validate(mapping, request));
    }
  }

  /**
   * Validates the list of references.
   * <p/>
   * <ol>
   * <li>removes empty items</li>
   * <li>removes duplicates</li>
   * <li>checks the reference independently from the others (using its <code>validate()</code> method)</li>
   * </ol>
   *
   * @param errors  Stores errors.
   * @param mapping Action mapping object.
   * @param request Request object.
   */
  private void validateReferences(ActionErrors errors, ActionMapping mapping, HttpServletRequest request) {
    assert errors != null : "Parameter 'errors' must not be null.";
    assert mapping != null : "Parameter 'mapping' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";

    removeEmptyReferenceItems();
    removeDuplicates(references);
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO referenceDTO = (ReferenceDTO) references.get(iii);
      errors.add(referenceDTO.validate(mapping, request));
    }
  }

  private void removeEmptyNameItems(List names) {
    assert names != null : "Parameter 'names' must not be null.";
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) names.get(iii);
      if (enzymeNameDTO.getXmlName() == null || enzymeNameDTO.getXmlName().equals("")) {
        names.remove(iii);
        iii--;
      }
    }
  }

  private void removeEmptyReactionItems() {
    for (int iii = 0; iii < reactionDtos.size(); iii++) {
      ReactionDTO reactionDTO = (ReactionDTO) reactionDtos.get(iii);
      if (reactionDTO.getXmlTextualRepresentation() == null || reactionDTO.getXmlTextualRepresentation().equals("")) {
        reactionDtos.remove(iii);
        iii--;
      }
    }
  }

//  private void removeEmptyCofactorItems() {
//    for (int iii = 0; iii < cofactors.size(); iii++) {
//      CofactorDTO cofactorDTO = (CofactorDTO) cofactors.get(iii);
//      if (cofactorDTO.getXmlCofactorValue() == null || cofactorDTO.getXmlCofactorValue().equals("")) {
//        cofactors.remove(iii);
//        iii--;
//      }
//    }
//  }

  /**
   * All database links which name value is <code>NONE</code> will be removed.
   * <p/>
   * All the others will be kept and checked in the updating Action.
   */
  private void removeEmptyLinkItems() {
    assert links != null : "Parameter 'links' must not be null.";
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.UNDEF.getDisplayName())) {
        links.remove(iii);
        iii--;
      }
    }
  }

  private void removeEmptyCommentItems() {
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO commentDTO = (CommentDTO) comments.get(iii);
      if (commentDTO.getXmlComment() == null || commentDTO.getXmlComment().equals("")) {
        comments.remove(iii);
        iii--;
      }
    }
  }

  private void removeEmptyReferenceItems() {
    assert references != null : "Parameter 'references' must not be null.";
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO referenceDTO = references.get(iii);
      if (referenceDTO.isEmpty()) {
        references.remove(iii);
        iii--;
      }
    }
  }

  private void checkForPeriod(ActionMessages errors) {
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO commentDTO = (CommentDTO) comments.get(iii);
      if (!commentDTO.getXmlComment().endsWith(".")) {
        ActionMessage error = new ActionMessage("errors.form.comment.missing_period", "" + (iii + 1));
        errors.add("comments", error);
        return;
      }
    }
  }

  /**
   * Removes duplicates from the list of links.
   */
  private void removeLinkDuplicates() {
    assert links != null : "Parameter 'links' must not be null.";
    Set databaseNames = new HashSet();
    Set checkedLinks = new HashSet();
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (databaseNames.contains(enzymeLinkDTO.getDatabaseName())) {
        // If the given name is a valid xref name and it is a unique xref then remove it from the list.
        if (XrefDatabaseConstant.isUniqueXrefDatabaseName(enzymeLinkDTO.getDatabaseName())) {
          links.remove(enzymeLinkDTO);
        } else { // Remove xref only if all xref values are the same.
          for (Iterator it = checkedLinks.iterator(); it.hasNext();) {
            EnzymeLinkDTO checkedLink = (EnzymeLinkDTO) it.next();
            if (checkedLink.equals(enzymeLinkDTO)) links.remove(enzymeLinkDTO);
          }
        }
      } else {
        databaseNames.add(enzymeLinkDTO.getDatabaseName());
        checkedLinks.add(enzymeLinkDTO);
      }
    }
  }

  private void removeDuplicates(List list) {
    list.retainAll(SetUniqueList.decorate(list));
  }

  /**
   * If a user added a static link the values such as the correct URL will be set here.
   *
   * @throws DomainException if the database code of this link is unknown.
   */
  private void updateStaticLinks() throws DomainException {
    assert links != null : "Parameter 'links' must not be null.";
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      EnzymeLink staticLink = EnzymeLink.getStaticLink(XrefDatabaseConstant.valueOf(enzymeLinkDTO.getDatabaseCode()));
      if (staticLink != null) {
        enzymeLinkDTO.setDatabaseName(staticLink.getXrefDatabaseConstant().getDisplayName());
        enzymeLinkDTO.setName(staticLink.getName());
        enzymeLinkDTO.setUrl(staticLink.getFullUrl(ec));
      }
    }
  }

  private void updateStatus() {
    if (statusCode != null && !statusCode.equals("")) {
      statusText = EnzymeStatusConstant.valueOf(statusCode).toString();
    }
  }
}

