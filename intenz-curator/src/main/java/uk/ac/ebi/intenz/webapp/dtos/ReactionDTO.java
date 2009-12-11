package uk.ac.ebi.intenz.webapp.dtos;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import uk.ac.ebi.biobabel.validator.SyntaxValidator;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzValidations;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2008/06/09 10:28:39 $
 */
public class ReactionDTO extends ActionForm {

	private long id = Reaction.NO_ID_ASSIGNED;
  private String textualRepresentation;
  private String xmlTextualRepresentation;
  private String orderIn;
  private String source;
  private String view;
  private String sourceDisplay;
  private String viewDisplayString;
  private String viewDisplayImage;
  private String status;

  public ReactionDTO() {
    textualRepresentation = "";
    xmlTextualRepresentation = "";
    orderIn = "0";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "\"<img src=\\\"images/blue_bullet.gif\\\"/><img src=\\\"images/green_bullet.gif\\\"/><img src=\\\"images/red_bullet.gif\\\"/>\"";
    status = Status.NO.toString();
  }

  public ReactionDTO(ReactionDTO reactionDTO) {
    setTextualRepresentation(reactionDTO.getTextualRepresentation());
    setXmlTextualRepresentation(reactionDTO.getXmlTextualRepresentation());
    setSource(reactionDTO.getSource());
    setView(reactionDTO.getView());
    setSourceDisplay(reactionDTO.getSourceDisplay());
    setViewDisplayString(reactionDTO.getViewDisplayString());
    setViewDisplayImage(reactionDTO.getViewDisplayImage());
  }

    @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    updateTextualRepresentation(encoding);
    updateSource();
    updateView();
    if (!SyntaxValidator.getInstance().validate(xmlTextualRepresentation, SyntaxValidator.BRACKETS))
    	errors.add("reaction", new ActionMessage("errors.form.brackets", "reaction", xmlTextualRepresentation));
    // Commented out because of <b></b> tags for coefficients:
//    XMLErrorReport xmlErrors = XCharsValidator.validate(xmlTextualRepresentation);
//    if (xmlErrors != null)
//    	errors.add("reaction", new ActionMessage("errors.form.xchars", "reaction", xmlTextualRepresentation, xmlErrors.getErrorMessage()));
    if (!IntEnzValidations.hasNoUnicode(xmlTextualRepresentation))
    	errors.add("reaction", new ActionMessage("errors.form.unicode", "reaction", xmlTextualRepresentation));
    return errors.isEmpty() ? null : errors;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReactionDTO)) return false;

    final ReactionDTO reactionDTO = (ReactionDTO) o;

    //if (source != null ? !source.equals(reactionDTO.source) : reactionDTO.source != null) return false;
    if (xmlTextualRepresentation != null ?
    		!xmlTextualRepresentation.equals(reactionDTO.xmlTextualRepresentation) :
			reactionDTO.xmlTextualRepresentation != null)
    	return false;

    return true;
  }

    @Override
  public int hashCode() {
    int result;
    result = (xmlTextualRepresentation != null ? xmlTextualRepresentation.hashCode() : 0);
    //result = 29 * result + (source != null ? source.hashCode() : 0);
    return result;
  }

  /**
   * Updates <code>name</code> using the encoded <code>xmlName</code> value.
   *
   * This update has to be done to automatically to provide the encoded version of the common name after the curator
   * entered a name which containing <i>XChars</i> tags.
   */
  private void updateTextualRepresentation(SpecialCharacters encoding) {
    if (xmlTextualRepresentation != null && !xmlTextualRepresentation.equals("")) {
      textualRepresentation = encoding.xml2Display(xmlTextualRepresentation.trim());
    }
  }

  /**
   * Updates <code>sourceDisplay</code> using the given <code>source</code> value.
   */
  private void updateSource() {
    if (source != null && !source.equals("")) {
      sourceDisplay = EnzymeSourceConstant.toDisplayString(source);
    }
  }

  /**
   * Updates <code>viewDisplayImage</code> and <code>viewDisplayString</code> using the given <code>view</code> value.
   */
  private void updateView() {
    if(view != null && !view.equals("")) {
      viewDisplayImage = EnzymeViewConstant.toDisplayImage(view);
      viewDisplayString = EnzymeViewConstant.toDisplayString(view);
    }
  }

  // ------------------------------- GETTER & SETTER --------------------------------

  public long getId() {
	return id;
}

public void setId(long id) {
	this.id = id;
}

public String getTextualRepresentation() {
    return textualRepresentation;
  }

  public void setTextualRepresentation(String textualRepresentation) {
    this.textualRepresentation = textualRepresentation
    	.replaceAll("[\r\n]+(.)", " $1")
		.replaceAll("[\r\n]+$", "");
  }

  public String getXmlTextualRepresentation() {
    return xmlTextualRepresentation;
  }

  public void setXmlTextualRepresentation(String xmlTextualRepresentation) {
    this.xmlTextualRepresentation = xmlTextualRepresentation
		.replaceAll("[\r\n]+(.)", " $1")
		.replaceAll("[\r\n]+$", "");
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

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  public String getSourceDisplay() {
    return sourceDisplay;
  }

  public void setSourceDisplay(String sourceDisplay) {
    this.sourceDisplay = sourceDisplay;
  }

  public String getViewDisplayString() {
    return viewDisplayString;
  }

  public void setViewDisplayString(String viewDisplayString) {
    this.viewDisplayString = viewDisplayString;
  }

  public String getViewDisplayImage() {
    return viewDisplayImage;
  }

  public void setViewDisplayImage(String viewDisplayImage) {
    this.viewDisplayImage = viewDisplayImage;
  }

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

}
