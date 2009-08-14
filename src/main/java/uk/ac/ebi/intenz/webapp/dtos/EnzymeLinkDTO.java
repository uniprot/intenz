package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeLinkDTO extends ActionForm implements Comparable {
  // ------------------------------ FIELDS ------------------------------

  private String databaseName;
  private String databaseCode;
  private String source;
  private String url;
  private String accession;
  private String name;
  private String sourceDisplay;
  private String view;
  private String viewDisplayString;
  private String viewDisplayImage;
  private DataCommentDTO dataComment;

  // --------------------------- CONSTRUCTORS ---------------------------

  public EnzymeLinkDTO() {
    databaseName = "";
    databaseCode = "";
    url = "";
    accession = "";
    name = "";
    source = "INTENZ";
    view = "INTENZ";
    sourceDisplay = "IntEnz";
    viewDisplayString = "all views";
    viewDisplayImage = "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
    dataComment = new DataCommentDTO();
  }

  public EnzymeLinkDTO(EnzymeLinkDTO enzymeLinkDTO) {
    setDatabaseName(enzymeLinkDTO.getDatabaseName());
    setDatabaseCode(enzymeLinkDTO.getDatabaseCode());
    setSource(enzymeLinkDTO.getSource());
    setUrl(enzymeLinkDTO.getUrl());
    setAccession(enzymeLinkDTO.getAccession());
    setName(enzymeLinkDTO.getName());
    setSourceDisplay(enzymeLinkDTO.getSourceDisplay());
    setView(enzymeLinkDTO.getView());
    setViewDisplayString(enzymeLinkDTO.getViewDisplayString());
    setViewDisplayImage(enzymeLinkDTO.getViewDisplayImage());
    setDataComment(new DataCommentDTO(enzymeLinkDTO.getDataComment()));
  }

  // --------------------- GETTER / SETTER METHODS ---------------------

  public String getAccession() {
    return accession;
  }

  public void setAccession(String accession) {
    this.accession = accession;
  }

  public String getDatabaseCode() {
    return databaseCode;
  }

  public void setDatabaseCode(String databaseCode) {
    this.databaseCode = databaseCode;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * Sets the database name <b>and</b> code.
   *
   * @param databaseName
   */
  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
    this.databaseCode = XrefDatabaseConstant.getDatabaseCodeOf(databaseName);
  }

  public String getName() {
     // if its a prosite link set the correct name.
    if( this.databaseCode.equals(XrefDatabaseConstant.PROSITE.getDatabaseCode()))
      this.name = XrefDatabaseConstant.PROSITE.getDisplayName();
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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

    public DataCommentDTO getDataComment() {
        return dataComment;
    }

    public void setDataComment(DataCommentDTO dataComment) {
        this.dataComment = dataComment;
    }


  // ------------------------ CANONICAL METHODS ------------------------

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeLinkDTO)) return false;

    final EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) o;

    if (accession != null ? !accession.equals(enzymeLinkDTO.accession) : enzymeLinkDTO.accession != null) return false;
    if (databaseName != null ? !databaseName.equals(enzymeLinkDTO.databaseName) : enzymeLinkDTO.databaseName != null) return false;
    if (name != null ? !name.equals(enzymeLinkDTO.name) : enzymeLinkDTO.name != null) return false;
    if (url != null ? !url.equals(enzymeLinkDTO.url) : enzymeLinkDTO.url != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (databaseName != null ? databaseName.hashCode() : 0);
    result = 29 * result + (url != null ? url.hashCode() : 0);
    result = 29 * result + (accession != null ? accession.hashCode() : 0);
    result = 29 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  public String toString() {
    StringBuffer output = new StringBuffer();
    output.append("databaseName: ");
    output.append(databaseName);
    output.append("\n");
    output.append("databaseCode: ");
    output.append(databaseCode);
    output.append("\n");
    output.append("source: ");
    output.append(source);
    output.append("\n");
    output.append("url: ");
    output.append(url);
    output.append("\n");
    output.append("accession: ");
    output.append(accession);
    output.append("\n");
    output.append("name: ");
    output.append(name);
    output.append("\n");
    output.append("sourceDisplay: ");
    output.append(sourceDisplay);
    output.append("\n");
    output.append("view: ");
    output.append(view);
    output.append("\n");
    output.append("viewDisplayString: ");
    output.append(viewDisplayString);
    output.append("\n");
    output.append("viewDisplayImage: ");
    output.append(viewDisplayImage);
    output.append("\n");
    return output.toString();
  }

  // ------------------------ INTERFACE METHODS ------------------------

  // --------------------- Interface Comparable ---------------------

  /**
   * Used for ordering links in a collection.
   * <p/>
   * Static and other links are ordered by their xrefDatabaseConstant name, SwissProt and MIM cross-references by their name
   * and PROSITE links by their accession. Other links are ordered by their specific URL if the xrefDatabaseConstant name is the same.
   *
   * @param o The object to be compared to this.
   * @return neg, 0 or pos. value to indicate the order of these two objects.
   * @throws NullPointerException if <code>o</code> is <code>null</code>.
   */
  public int compareTo(Object o) {
    if (o == null) throw new NullPointerException("Parameter 'o' must not be null.");
    EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) o;

    if (databaseName.equals(enzymeLinkDTO.getDatabaseName())) {
      if (name.equals(enzymeLinkDTO.getName())) {
        if (accession.equals(enzymeLinkDTO.getAccession())) {
          return url.compareTo(enzymeLinkDTO.getUrl());
        }
        return accession.compareTo(enzymeLinkDTO.getAccession());
      }
      return name.compareTo(enzymeLinkDTO.getName());
    }

    return databaseName.compareTo(enzymeLinkDTO.getDatabaseName());
  }

  // -------------------------- OTHER METHODS --------------------------

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    databaseName = "";
    databaseCode = "";
    source = "";
    url = "";
    accession = "";
    name = "";
    sourceDisplay = "";
    view = "";
    viewDisplayString = "";
    viewDisplayImage = "";
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    errors.add(dataComment.validate(mapping, request));
    updateView();
    return errors;
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

  private void validateCASRegistryNumber() {
    if(this.getDatabaseCode().equals(XrefDatabaseConstant.CAS.getDatabaseCode())) {
//      return IntEnzValidations.validateCASRegistryNumberFormat()
    }
  }
}

