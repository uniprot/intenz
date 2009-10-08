package uk.ac.ebi.intenz.domain.reference;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

/**
 * This class stores additional information about patents.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:10 $
 */
public class Patent extends Reference {

  private String patentNumber;

  /**
   * Returns a <code>Patent</code> instance.
   *
   * @param id           The pubId
   * @param authors      String containing all author names.
   * @param title        The title of the publication.
   * @param year         The year of the publication.
   * @param patentNumber The patent number.
   */
  public Patent(Long id, String authors, String title, String year, String patentNumber, EnzymeViewConstant view,
                 EnzymeSourceConstant source) {
    super(id, authors, title, year, view, source);
    if (patentNumber == null) throw new NullPointerException("Parameter 'patentNumber' must not be null.");
    this.patentNumber = patentNumber;
  }

   /**
   * Returns a string representation of this patent.
   *
   * @return the string representation.
   */
  public String toString() {
    StringBuffer referenceString = new StringBuffer();

    referenceString.append(authors);
    referenceString.append(" ");
    referenceString.append(title);
    if (!patentNumber.equals("")) {
      referenceString.append("<i>");
      referenceString.append(patentNumber);
      referenceString.append("</i>,");
    }
    if (!year.equals("")) {
      referenceString.append(year);
    }

    return referenceString.toString();
  }

  /**
   * Standard <code>equals()</code> method.
   *
   * @param o The object to be compared to this one.
   * @return <code>true</code> if <code>o</code> is equal to this object.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Patent)) return false;
    if (!super.equals(o)) return false;

    final Patent patent = (Patent) o;

    if (patentNumber != null ? !patentNumber.equals(patent.patentNumber) : patent.patentNumber != null) return false;

    return true;
  }

  /**
   * Generates a hash code for this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + (patentNumber != null ? patentNumber.hashCode() : 0);
    return result;
  }


  // --------------------  GETTER -----------------------

  public String getPatentNumber() {
    return patentNumber;
  }

  // TODO: Put method's content somewhere else.
  public String toXML(int number, boolean intenzTextXML) {
    StringBuffer xmlStringBuffer = new StringBuffer();

    xmlStringBuffer.append(super.toXML(number, intenzTextXML));
    if (intenzTextXML) {
      xmlStringBuffer.append("<issue>");
      xmlStringBuffer.append("</issue>");
      xmlStringBuffer.append("<patent_no>");
      xmlStringBuffer.append(patentNumber);
      xmlStringBuffer.append("</patent_no>");
      xmlStringBuffer.append("<first_page>");
      xmlStringBuffer.append("</first_page>");
      xmlStringBuffer.append("<last_page>");
      xmlStringBuffer.append("</last_page>");
      xmlStringBuffer.append("<edition>");
      xmlStringBuffer.append("</edition>");
      xmlStringBuffer.append("<editor>");
      xmlStringBuffer.append("</editor>");
      xmlStringBuffer.append("<volume>");
      xmlStringBuffer.append("</volume>");
      xmlStringBuffer.append("<pub_place>");
      xmlStringBuffer.append("</pub_place>");
      xmlStringBuffer.append("<pub_company>");
      xmlStringBuffer.append("</pub_company>");
      xmlStringBuffer.append("<pub_med>");
      xmlStringBuffer.append("</pub_med>");
      xmlStringBuffer.append("<medline>");
      xmlStringBuffer.append("</medline>");
    } else {
      xmlStringBuffer.append("      <issue>");
      xmlStringBuffer.append("</issue>\n");
      xmlStringBuffer.append("      <patent_no>");
      xmlStringBuffer.append(patentNumber);
      xmlStringBuffer.append("</patent_no>\n");
      xmlStringBuffer.append("      <first_page>");
      xmlStringBuffer.append("</first_page>\n");
      xmlStringBuffer.append("      <last_page>");
      xmlStringBuffer.append("</last_page>\n");
      xmlStringBuffer.append("      <edition>");
      xmlStringBuffer.append("</edition>\n");
      xmlStringBuffer.append("      <editor>");
      xmlStringBuffer.append("</editor>\n");
      xmlStringBuffer.append("      <volume>");
      xmlStringBuffer.append("</volume>\n");
      xmlStringBuffer.append("      <pub_place>");
      xmlStringBuffer.append("</pub_place>\n");
      xmlStringBuffer.append("      <pub_company>");
      xmlStringBuffer.append("</pub_company>\n");
      xmlStringBuffer.append("      <pub_med>");
      xmlStringBuffer.append("</pub_med>\n");
      xmlStringBuffer.append("      <medline>");
      xmlStringBuffer.append("</medline>\n");
    }
    return xmlStringBuffer.toString();
  }
}
