package uk.ac.ebi.intenz.webapp.dtos;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzValidations;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.XMLErrorReport;
import uk.ac.ebi.xchars.utilities.XCharsValidator;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class ReferenceDTO extends ActionForm {

  private String pubId;
  private String type;
  private String authors;
  private String title;
  private String year;
  private String pubName;
  private String firstPage;
  private String lastPage;
  private String edition;
  private String editor;
  private String volume;
  private String publisher;
  private String publisherPlace;
  private String pubMedId;
  private String medlineId;
  private String view;
  private String source;
  private String sourceDisplay;
  private String viewDisplayString;
  private String viewDisplayImage;

  private String xmlAuthors;
  private String xmlTitle;
  private String xmlPubName;
  private String xmlEditor;
  private String xmlPublisher;

  public ReferenceDTO() {
    pubId = "";
    type = "J";
    authors = "";
    title = "";
    year = "";
    pubName = "";
    firstPage = "";
    lastPage = "";
    edition = "";
    editor = "";
    volume = "";
    publisher = "";
    publisherPlace = "";
    pubMedId = "";
    medlineId = "";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "\"<img src=\\\"images/blue_bullet.gif\\\"/><img src=\\\"images/green_bullet.gif\\\"/><img src=\\\"images/red_bullet.gif\\\"/>\"";
    xmlAuthors = "";
    xmlTitle = "";
    xmlPubName = "";
    xmlEditor = "";
    xmlPublisher = "";
  }

  public ReferenceDTO(ReferenceDTO referenceDTO) {
    setPubId(referenceDTO.getPubId());
    setType(referenceDTO.getType());
    setAuthors(referenceDTO.getAuthors());
    setTitle(referenceDTO.getTitle());
    setYear(referenceDTO.getYear());
    setPubName(referenceDTO.getPubName());
    setFirstPage(referenceDTO.getFirstPage());
    setLastPage(referenceDTO.getLastPage());
    setEdition(referenceDTO.getEdition());
    setEditor(referenceDTO.getEditor());
    setVolume(referenceDTO.getVolume());
    setPublisher(referenceDTO.getPublisher());
    setPublisherPlace(referenceDTO.getPublisherPlace());
    setPubMedId(referenceDTO.getPubMedId());
    setMedlineId(referenceDTO.getMedlineId());
    setView(referenceDTO.getView());
    setSource(referenceDTO.getSource());
    setSourceDisplay(referenceDTO.getSourceDisplay());
    setViewDisplayString(referenceDTO.getViewDisplayString());
    setViewDisplayImage(referenceDTO.getViewDisplayImage());
    setXmlAuthors(referenceDTO.getXmlAuthors());
    setXmlTitle(referenceDTO.getXmlTitle());
    setXmlPubName(referenceDTO.getXmlPubName());
    setXmlEditor(referenceDTO.getXmlEditor());
    setXmlPublisher(referenceDTO.getXmlPublisher());
  }

    @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    updateAuthors(encoding);
    updateTitle(encoding);
    updateEditor(encoding);
    updatePubName(encoding);
    updatePublisher(encoding);
    updateView();

    // Name:
    if (StringUtil.isNullOrEmpty(xmlPubName)){
    	errors.add("reference", new ActionMessage("validator.reference.name.required"));
    }

    // Authors:
    XMLErrorReport xmlErrors = XCharsValidator.validate(xmlAuthors);
    if (xmlErrors != null)
    	errors.add("reference",
    			new ActionMessage("errors.form.xchars", "authors", xmlAuthors, xmlErrors.getErrorMessage()));
    if (!IntEnzValidations.hasNoUnicode(xmlAuthors))
    	errors.add("reference", new ActionMessage("errors.form.unicode", "authors", xmlAuthors));

    // Editor:
    xmlErrors = XCharsValidator.validate(xmlEditor);
    if (xmlErrors != null)
    	errors.add("reference",
    			new ActionMessage("errors.form.xchars", "editor", xmlEditor, xmlErrors.getErrorMessage()));
    if (!IntEnzValidations.hasNoUnicode(xmlEditor))
    	errors.add("reference", new ActionMessage("errors.form.unicode", "editor", xmlEditor));

    // Title:
    if (!StringUtil.isNullOrEmpty(xmlTitle)){
        xmlErrors = XCharsValidator.validate(xmlTitle);
        if (xmlErrors != null)
        	errors.add("reference",
        			new ActionMessage("errors.form.xchars", "title", xmlTitle, xmlErrors.getErrorMessage()));
        if (!IntEnzValidations.hasNoUnicode(xmlTitle))
        	errors.add("reference", new ActionMessage("errors.form.unicode", "title", xmlTitle));
    }

	if (!StringUtil.isNullOrEmpty(edition) && !StringUtil.isInteger(edition)){
      ActionMessage message = new ActionMessage("errors.form.reference.edition");
      errors.add("reference", message);
	}
    
    if (StringUtil.isNullOrEmpty(year)){
      ActionMessage message = new ActionMessage("errors.form.reference.year.missing");
      errors.add("reference", message);
    }
    return errors;
  }

    @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    pubId = "";
    type = "J";
    authors = "";
    title = "";
    year = "";
    pubName = "";
    firstPage = "";
    lastPage = "";
    edition = "";
    editor = "";
    volume = "";
    publisher = "";
    publisherPlace = "";
    pubMedId = "";
    medlineId = "";
    view = "";
    source = "";
    sourceDisplay = "";
    viewDisplayString = "";
    viewDisplayImage = "";
    xmlAuthors = "";
    xmlTitle = "";
    xmlPubName = "";
    xmlEditor = "";
    xmlPublisher = "";
  }

  /**
   * A reference is regarded as an empty reference if the fields below do not contain a value.
   *
   * @return <code>true</code> if at least one of the checked fields does contain a value.
   */
  public boolean isEmpty() {
    return (xmlAuthors == null || xmlAuthors.equals("")) && (xmlTitle == null || xmlTitle.equals("")) &&
           (year == null || year.equals("")) && (xmlPubName == null || xmlPubName.equals("")) &&
           (firstPage == null || firstPage.equals("")) && (lastPage == null || lastPage.equals("")) &&
           (edition == null || edition.equals("")) && (xmlEditor == null || xmlEditor.equals("")) &&
           (volume == null || volume.equals("")) && (xmlPublisher == null || xmlPublisher.equals("")) &&
           (publisherPlace == null || publisherPlace.equals("")) && (pubMedId == null || pubMedId.equals(""));
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReferenceDTO)) return false;

    final ReferenceDTO referenceDTO = (ReferenceDTO) o;

    if (edition != null ? !edition.equals(referenceDTO.edition) : referenceDTO.edition != null) return false;
    if (firstPage != null ? !firstPage.equals(referenceDTO.firstPage) : referenceDTO.firstPage != null) return false;
    if (lastPage != null ? !lastPage.equals(referenceDTO.lastPage) : referenceDTO.lastPage != null) return false;
    if (pubMedId != null ? !pubMedId.equals(referenceDTO.pubMedId) : referenceDTO.pubMedId != null) return false;
    if (publisherPlace != null ? !publisherPlace.equals(referenceDTO.publisherPlace) : referenceDTO.publisherPlace !=
                                                                                       null)
      return false;
    if (type != null ? !type.equals(referenceDTO.type) : referenceDTO.type != null) return false;
    if (volume != null ? !volume.equals(referenceDTO.volume) : referenceDTO.volume != null) return false;
    if (xmlAuthors != null ? !xmlAuthors.equals(referenceDTO.xmlAuthors) : referenceDTO.xmlAuthors != null) return false;
    if (xmlEditor != null ? !xmlEditor.equals(referenceDTO.xmlEditor) : referenceDTO.xmlEditor != null) return false;
    if (xmlPubName != null ? !xmlPubName.equals(referenceDTO.xmlPubName) : referenceDTO.xmlPubName != null) return false;
    if (xmlPublisher != null ? !xmlPublisher.equals(referenceDTO.xmlPublisher) : referenceDTO.xmlPublisher != null) return false;
    if (xmlTitle != null ? !xmlTitle.equals(referenceDTO.xmlTitle) : referenceDTO.xmlTitle != null) return false;
    if (year != null ? !year.equals(referenceDTO.year) : referenceDTO.year != null) return false;

    return true;
  }

    @Override
  public int hashCode() {
    int result;
    result = (type != null ? type.hashCode() : 0);
    result = 29 * result + (year != null ? year.hashCode() : 0);
    result = 29 * result + (firstPage != null ? firstPage.hashCode() : 0);
    result = 29 * result + (lastPage != null ? lastPage.hashCode() : 0);
    result = 29 * result + (edition != null ? edition.hashCode() : 0);
    result = 29 * result + (volume != null ? volume.hashCode() : 0);
    result = 29 * result + (publisherPlace != null ? publisherPlace.hashCode() : 0);
    result = 29 * result + (pubMedId != null ? pubMedId.hashCode() : 0);
    result = 29 * result + (xmlAuthors != null ? xmlAuthors.hashCode() : 0);
    result = 29 * result + (xmlTitle != null ? xmlTitle.hashCode() : 0);
    result = 29 * result + (xmlPubName != null ? xmlPubName.hashCode() : 0);
    result = 29 * result + (xmlEditor != null ? xmlEditor.hashCode() : 0);
    result = 29 * result + (xmlPublisher != null ? xmlPublisher.hashCode() : 0);
    return result;
  }

  private void updateAuthors(SpecialCharacters encoding) {
    if (xmlAuthors != null && !xmlAuthors.equals("")) {
      authors = encoding.xml2Display(xmlAuthors = xmlAuthors.trim());
    }
  }

  private void updateTitle(SpecialCharacters encoding) {
    if (xmlTitle != null && !xmlTitle.equals("")) {
      title = encoding.xml2Display(xmlTitle = xmlTitle.trim());
    }
  }

  private void updatePubName(SpecialCharacters encoding) {
    if (xmlPubName != null && !xmlPubName.equals("")) {
      pubName = encoding.xml2Display(xmlPubName = xmlPubName.trim());
    }
  }

  private void updateEditor(SpecialCharacters encoding) {
    if (xmlEditor != null && !xmlEditor.equals("")) {
      editor = encoding.xml2Display(xmlEditor = xmlEditor.trim());
    }
  }

  private void updatePublisher(SpecialCharacters encoding) {
    if (xmlPublisher != null && !xmlPublisher.equals("")) {
      publisher = encoding.xml2Display(xmlPublisher = xmlPublisher.trim());
    }
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

  public String getPubId() {
    return pubId;
  }

  public void setPubId(String pubId) {
    this.pubId = pubId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getPubName() {
    return pubName;
  }

  public void setPubName(String pubName) {
    this.pubName = pubName;
  }

  public String getFirstPage() {
    return firstPage;
  }

  public void setFirstPage(String firstPage) {
    this.firstPage = firstPage;
  }

  public String getLastPage() {
    return lastPage;
  }

  public void setLastPage(String lastPage) {
    this.lastPage = lastPage;
  }

  public String getEdition() {
    return edition;
  }

  public void setEdition(String edition) {
    this.edition = edition;
  }

  public String getEditor() {
    return editor;
  }

  public void setEditor(String editor) {
    this.editor = editor;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getPublisherPlace() {
    return publisherPlace;
  }

  public void setPublisherPlace(String publisherPlace) {
    this.publisherPlace = publisherPlace;
  }

  public String getPubMedId() {
    return pubMedId;
  }

  public void setPubMedId(String pubMedId) {
    this.pubMedId = pubMedId;
  }

  public String getMedlineId() {
    return medlineId;
  }

  public void setMedlineId(String medlineId) {
    this.medlineId = medlineId;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
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

  public String getXmlAuthors() {
    return xmlAuthors;
  }

  public void setXmlAuthors(String xmlAuthors) {
    this.xmlAuthors = xmlAuthors;
  }

  public String getXmlTitle() {
    return xmlTitle;
  }

  public void setXmlTitle(String xmlTitle) {
    this.xmlTitle = xmlTitle;
  }

  public String getXmlPubName() {
    return xmlPubName;
  }

  public void setXmlPubName(String xmlPubName) {
    this.xmlPubName = xmlPubName;
  }

  public String getXmlEditor() {
    return xmlEditor;
  }

  public void setXmlEditor(String xmlEditor) {
    this.xmlEditor = xmlEditor;
  }

  public String getXmlPublisher() {
    return xmlPublisher;
  }

  public void setXmlPublisher(String xmlPublisher) {
    this.xmlPublisher = xmlPublisher;
  }
}
