package uk.ac.ebi.intenz.webapp.dtos;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class CofactorDTO extends ActionForm implements Comparable<CofactorDTO>{

  private String cofactorValue;
  private String xmlCofactorValue; // may include several compound names
  private String orderIn;
  private String source;
  private String view;
  private String sourceDisplay;
  private String viewDisplayString;
  private String viewDisplayImage;
  private String accession; // may include several compound ChEBI IDs
  private String compoundId; // may include several compound IDs

public CofactorDTO() {
    cofactorValue = "";
    xmlCofactorValue = "";
    orderIn = "0";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
  }

  public CofactorDTO(CofactorDTO cofactorDTO) {
    setXmlCofactorValue(cofactorDTO.getXmlCofactorValue());
    setOrderIn(cofactorDTO.getOrderIn());
    setSource(cofactorDTO.getSource());
    setView(cofactorDTO.getView());
    setSourceDisplay(cofactorDTO.getSourceDisplay());
    setAccession(cofactorDTO.accession);
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    updateView();
    if (xmlCofactorValue != null && !xmlCofactorValue.equals("")) {
      SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
      cofactorValue = encoding.xml2Display(xmlCofactorValue.trim());
    }
    return errors.isEmpty() ? null : errors;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CofactorDTO)) return false;

    final CofactorDTO cofactorDTO = (CofactorDTO) o;

    if (source != null ? !source.equals(cofactorDTO.source) : cofactorDTO.source != null) return false;
    if (xmlCofactorValue != null ? !xmlCofactorValue.equals(cofactorDTO.xmlCofactorValue) : cofactorDTO.xmlCofactorValue != null)
      return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (xmlCofactorValue != null ? xmlCofactorValue.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (accession != null? accession.hashCode() : 0);
    return result;
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

  // ------------------------------- GETTER & SETTER --------------------------------

  public String getCofactorValue() {
    return cofactorValue;
  }

  public void setCofactorValue(String cofactorValue) {
    this.cofactorValue = cofactorValue;
  }

  public String getXmlCofactorValue() {
    return xmlCofactorValue;
  }

  public void setXmlCofactorValue(String xmlCofactorValue) {
    this.xmlCofactorValue = xmlCofactorValue;
    cofactorValue = SpecialCharacters.getInstance(null)
            .xml2Display(xmlCofactorValue.trim());
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
    updateView();
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

public String getAccession() {
	return accession;
}

public void setAccession(String accession) {
	this.accession = accession;
}

public String getCompoundId() {
	return compoundId;
}

public void setCompoundId(String compoundId) {
	this.compoundId = compoundId;
}


    public int compareTo(CofactorDTO other) {
        return this.cofactorValue.compareTo(other.cofactorValue);
    }
}
