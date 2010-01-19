package uk.ac.ebi.intenz.webapp.dtos;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import uk.ac.ebi.biobabel.validator.SyntaxValidator;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzValidations;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.XMLErrorReport;
import uk.ac.ebi.xchars.utilities.XCharsValidator;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class CommentDTO extends ActionForm {
  // ------------------------------ FIELDS ------------------------------

  private String comment;
  private String xmlComment;
  private String orderIn;
  private String source;
  private String view;
  private String sourceDisplay;
  private String viewDisplayString;
  private String viewDisplayImage;

  // --------------------------- CONSTRUCTORS ---------------------------

  public CommentDTO() {
    comment = "";
    xmlComment = "";
    orderIn = "0";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "\"<img src=\\\"images/blue_bullet.gif\\\"/><img src=\\\"images/green_bullet.gif\\\"/><img src=\\\"images/red_bullet.gif\\\"/>\"";
  }

  public CommentDTO(CommentDTO commentDTO) {
    setComment(commentDTO.getComment());
    setXmlComment(commentDTO.getXmlComment());
    setOrderIn(commentDTO.getOrderIn());
    setSource(commentDTO.getSource());
    setView(commentDTO.getView());
    setSourceDisplay(commentDTO.getSourceDisplay());
    setViewDisplayString(commentDTO.getViewDisplayString());
    setViewDisplayImage(commentDTO.getViewDisplayImage());
  }

  // --------------------- GETTER / SETTER METHODS ---------------------

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getOrderIn() {
    return orderIn;
  }

  public void setOrderIn(String orderIn) {
    this.orderIn = orderIn;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getSourceDisplay() {
    return sourceDisplay;
  }

  public void setSourceDisplay(String sourceDisplay) {
    this.sourceDisplay = sourceDisplay;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  public String getViewDisplayImage() {
    return viewDisplayImage;
  }

  public void setViewDisplayImage(String viewDisplayImage) {
    this.viewDisplayImage = viewDisplayImage;
  }

  public String getViewDisplayString() {
    return viewDisplayString;
  }

  public void setViewDisplayString(String viewDisplayString) {
    this.viewDisplayString = viewDisplayString;
  }

  public String getXmlComment() {
    return xmlComment;
  }

  public void setXmlComment(String xmlComment) {
    xmlComment = IntEnzUtilities.linkMarkedEC(xmlComment.trim(), false);
    this.xmlComment = xmlComment;
  }

  // ------------------------ CANONICAL METHODS ------------------------

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CommentDTO)) return false;

    final CommentDTO commentDTO = (CommentDTO) o;
    if (source != null ? !source.equals(commentDTO.source) : commentDTO.source != null) return false;
    if (xmlComment != null ? !xmlComment.equals(commentDTO.xmlComment) : commentDTO.xmlComment != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (xmlComment != null ? xmlComment.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    return result;
  }

  // -------------------------- OTHER METHODS --------------------------

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    updateView();
    updateComment(request);
    if (!SyntaxValidator.getInstance().validate(xmlComment, SyntaxValidator.BRACKETS))
    	errors.add("comments", new ActionMessage("errors.form.brackets", "comment", xmlComment));
    XMLErrorReport xmlErrors = XCharsValidator.validate(xmlComment);
    if (xmlErrors != null)
    	errors.add("comments", new ActionMessage("errors.form.xchars", "comment", xmlComment, xmlErrors.getErrorMessage()));
    if (!IntEnzValidations.hasNoUnicode(xmlComment))
    	errors.add("comments", new ActionMessage("errors.form.unicode", "comment", xmlComment));
    return errors.isEmpty() ? null : errors;
  }

  /**
   * Updates <code>viewDisplayImage</code> and <code>viewDisplayString</code> using the given <code>view</code> value.
   */
  private void updateView() {
    if (view != null && !view.equals("")) {
      viewDisplayImage = EnzymeViewConstant.toDisplayImage(view);
      viewDisplayString = EnzymeViewConstant.toDisplayString(view);
    }
  }

  private void updateComment(HttpServletRequest request) {
    if (xmlComment != null && !xmlComment.equals("")) {
      SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
      comment = encoding.xml2Display(xmlComment.trim());
    }
  }
}

