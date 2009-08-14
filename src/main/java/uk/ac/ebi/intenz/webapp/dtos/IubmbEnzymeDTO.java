package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class IubmbEnzymeDTO extends ActionForm {

  private String id;
  private String ec;

  private String latestHistoryEventClass;
  private String latestHistoryEventNote;

  private EnzymeNameDTO systematicName;
  private String historyLine;
  private List commonNames;
  private List reactions;
  private List synonyms;
  private List links;
  private List comments;
  private List references;
  private String statusCode;

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();


    return errors;
  }


  // ------------------------------- GETTER & SETTER --------------------------------

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEc() {
    return ec;
  }

  public void setEc(String ec) {
    this.ec = ec;
  }

  public String getLatestHistoryEventClass() {
    return latestHistoryEventClass;
  }

  public void setLatestHistoryEventClass(String latestHistoryEventClass) {
    this.latestHistoryEventClass = latestHistoryEventClass;
  }

  public String getLatestHistoryEventNote() {
    return latestHistoryEventNote;
  }

  public void setLatestHistoryEventNote(String latestHistoryEventNote) {
    this.latestHistoryEventNote = latestHistoryEventNote;
  }

  public EnzymeNameDTO getSystematicName() {
    return systematicName;
  }

  public void setSystematicName(EnzymeNameDTO systematicName) {
    this.systematicName = systematicName;
  }

  public String getHistoryLine() {
    return historyLine;
  }

  public void setHistoryLine(String historyLine) {
    this.historyLine = historyLine;
  }

  public List getCommonNames() {
    return commonNames;
  }

  public void setCommonNames(List commonNames) {
    this.commonNames = commonNames;
  }

  public List getReactions() {
    return reactions;
  }

  public void setReactions(List reactions) {
    this.reactions = reactions;
  }

  public List getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List synonyms) {
    this.synonyms = synonyms;
  }

  public List getLinks() {
    return links;
  }

  public void setLinks(List links) {
    this.links = links;
  }

  public List getComments() {
    return comments;
  }

  public void setComments(List comments) {
    this.comments = comments;
  }

  public List getReferences() {
    return references;
  }

  public void setReferences(List references) {
    this.references = references;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

}
