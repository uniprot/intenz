package uk.ac.ebi.intenz.webapp.dtos;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import uk.ac.ebi.biobabel.validator.SyntaxValidator;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzValidations;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.XMLErrorReport;
import uk.ac.ebi.xchars.utilities.XCharsValidator;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeNameDTO extends ActionForm {

  private String name;
  private String xmlName;
  private String type;
  private String qualifier;
  private String qualifierDisplay;
  private String orderIn;
  private String source;
  private String view;
  private String sourceDisplay;
  private String viewDisplayString;
  private String viewDisplayImage;

  public EnzymeNameDTO() {
    name = "";
    xmlName = "";
    type = "";
    qualifier = "NON";
    qualifierDisplay = "";
    orderIn = "0";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "\"<img src=\\\"images/blue_bullet.gif\\\"/><img src=\\\"images/green_bullet.gif\\\"/><img src=\\\"images/red_bullet.gif\\\"/>\"";
  }

  public EnzymeNameDTO(EnzymeNameDTO enzymeNameDTO) {
    setName(enzymeNameDTO.getName());
    setXmlName(enzymeNameDTO.getXmlName());
    setType(enzymeNameDTO.getType());
    setQualifier(enzymeNameDTO.getQualifier());
    setQualifierDisplay(enzymeNameDTO.getQualifierDisplay());
    setOrderIn(enzymeNameDTO.getOrderIn());
    setSource(enzymeNameDTO.getSource());
    setView(enzymeNameDTO.getView());
    setSourceDisplay(enzymeNameDTO.getSourceDisplay());
    setViewDisplayString(enzymeNameDTO.getViewDisplayString());
    setViewDisplayImage(enzymeNameDTO.getViewDisplayImage());
  }

  /**
   * Sets convenience fields which cannot be set through the forms.
   * and validates the XML markup.
   *
   * @param mapping The Action mapping.
   * @param request The common request object.
   * @return errors, if any (otherwise <code>null</code>).
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    // Set name value according to XML name value.
    if (xmlName != null && !xmlName.equals("")) {
      SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
      name = encoding.xml2Display(xmlName.trim());
    }
    if (!SyntaxValidator.getInstance().validate(xmlName, SyntaxValidator.BRACKETS))
    	errors.add("names", new ActionMessage("errors.form.brackets", "name", xmlName));
    XMLErrorReport xmlErrors = XCharsValidator.validate(xmlName);
    if (xmlErrors != null)
    	errors.add("names",
    			new ActionMessage("errors.form.xchars", "name", xmlName, xmlErrors.getErrorMessage()));
    if (!IntEnzValidations.hasNoUnicode(xmlName))
    	errors.add("names", new ActionMessage("errors.form.unicode", "name", xmlName));
    return errors.isEmpty() ? null : errors;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeNameDTO)) return false;

    final EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) o;

    if (source != null ? !source.equals(enzymeNameDTO.source) : enzymeNameDTO.source != null) return false;
    if (type != null ? !type.equals(enzymeNameDTO.type) : enzymeNameDTO.type != null) return false;
    if (xmlName != null ? !xmlName.equals(enzymeNameDTO.xmlName) : enzymeNameDTO.xmlName != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (xmlName != null ? xmlName.hashCode() : 0);
    result = 29 * result + (type != null ? type.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    return result;
  }


  // ------------------------------- GETTER & SETTER --------------------------------

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name.trim();
  }

  public String getXmlName() {
    return xmlName;
  }

  public void setXmlName(String xmlName) {
    this.xmlName = xmlName.trim();
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getQualifier() {
    return qualifier;
  }

  public void setQualifier(String qualifier) {
    this.qualifier = qualifier;
    if (qualifier != null && !qualifier.equals("")) {
      EnzymeNameQualifierConstant qualifierConstant = EnzymeNameQualifierConstant.valueOf(qualifier);
      if (qualifierConstant == EnzymeNameQualifierConstant.UNDEF) setQualifierDisplay("");
      setQualifierDisplay(qualifierConstant.toHTML(xmlName));
    }
  }

  public String getQualifierDisplay() {
    return qualifierDisplay;
  }

  public void setQualifierDisplay(String qualifierDisplay) {
    this.qualifierDisplay = qualifierDisplay;
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
    if (source != null && !source.equals("")) {
      sourceDisplay = EnzymeSourceConstant.toDisplayString(source);
    }
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
    if (view != null && !view.equals("")) updateView();
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

  /**
   * Updates <code>viewDisplayImage</code> and <code>viewDisplayString</code> using the given <code>view</code> value.
   */
  private void updateView() {
    viewDisplayImage = EnzymeViewConstant.toDisplayImage(view);
    viewDisplayString = EnzymeViewConstant.toDisplayString(view);
  }
}
