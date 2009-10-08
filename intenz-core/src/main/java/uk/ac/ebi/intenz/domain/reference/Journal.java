package uk.ac.ebi.intenz.domain.reference;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

/**
 * This class stores additional information about a journal.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:10 $
 */
public class Journal extends Reference {

  private String pubName;

  private String firstPage;

  private String lastPage;

  private String volume;

  private String pubMedId;

  private String medlineId;

  /**
   * Returns a Journal instance.
   *
   * @param id        The pubId.
   * @param authors   The string containing all author names.
   * @param title     The title of the publication.
   * @param year      The year of the publication.
   * @param pubName   The journal's name.
   * @param firstPage The first page of the article.
   * @param lastPage  The last page of the article.
   * @param volume    The journal's volume.
   * @param pubMedId  The PubMed ID.
   * @param medlineId The Medline ID
   * @throws NullPointerException if any parameter is <code>null</code>.
   */
  public Journal(Long id, String authors, String title, String year, String pubName, String firstPage,
                 String lastPage, String volume, String pubMedId, String medlineId, EnzymeViewConstant view,
                 EnzymeSourceConstant source) {
    super(id, authors, title, year, view, source);
    if (pubName == null || firstPage == null || lastPage == null || volume == null || pubMedId == null ||
            medlineId == null)
      throw new NullPointerException("No parameter must be 'null'");
    this.pubName = pubName;
    this.firstPage = firstPage;
    this.lastPage = lastPage;
    this.volume = volume;
    this.pubMedId = pubMedId;
    this.medlineId = medlineId;
  }

  /**
   * Returns a string representation of this book.
   *
   * @return the string representation.
   */
  public String toString() {
    StringBuffer referenceString = new StringBuffer();

    referenceString.append(authors);
    referenceString.append(" ");
    referenceString.append(title);
    referenceString.append(" ");
    if (!pubName.equals("")) {
      referenceString.append("<i>");
      referenceString.append(pubName);
      referenceString.append("</i> ");
    }
    if (!volume.equals("")) {
      referenceString.append(volume);
      referenceString.append(" ");
    }
    if (!year.equals("")) {
      referenceString.append("(");
      referenceString.append(year);
      referenceString.append(") ");
    }
    if (!firstPage.equals("")) {
      referenceString.append(firstPage);
      if (!lastPage.equals("")) {
        referenceString.append("-");
        referenceString.append(lastPage);
        referenceString.append(". ");
      } else {
        referenceString.append(" only. ");
      }
    }
    // Medline is not accessible anymore.
//    if (!medlineId.equals("")) {
//      referenceString.append("[Medline UI: <a target=\"_blank\" href=\"http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-id+IntEnz+[med2pub-id:");
//      referenceString.append(medlineId);
//      referenceString.append("]%3Emedline+-view+MedlineRef\">");
//      referenceString.append(medlineId);
//      referenceString.append("</a>]");
//    }
    if (!pubMedId.equals("")) {
      referenceString.append("[PMID: <a target=\"_blank\" href=\"http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-id+IntEnz+[medline-PMID:");
      referenceString.append(pubMedId);
      referenceString.append("]+-e\">");
      referenceString.append(pubMedId);
      referenceString.append("</a>]");
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
    if (!(o instanceof Journal)) return false;
    if (!super.equals(o)) return false;

    final Journal journal = (Journal) o;

    if (firstPage != null ? !firstPage.equals(journal.firstPage) : journal.firstPage != null) return false;
    if (pubName != null ? !pubName.equals(journal.pubName) : journal.pubName != null) return false;
    if (lastPage != null ? !lastPage.equals(journal.lastPage) : journal.lastPage != null) return false;
    if (medlineId != null ? !medlineId.equals(journal.medlineId) : journal.medlineId != null) return false;
    if (pubMedId != null ? !pubMedId.equals(journal.pubMedId) : journal.pubMedId != null) return false;
    if (volume != null ? !volume.equals(journal.volume) : journal.volume != null) return false;

    return true;
  }

  /**
   * Generates a hash code for this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + (pubName != null ? pubName.hashCode() : 0);
    result = 29 * result + (firstPage != null ? firstPage.hashCode() : 0);
    result = 29 * result + (lastPage != null ? lastPage.hashCode() : 0);
    result = 29 * result + (volume != null ? volume.hashCode() : 0);
    result = 29 * result + (pubMedId != null ? pubMedId.hashCode() : 0);
    result = 29 * result + (medlineId != null ? medlineId.hashCode() : 0);
    return result;
  }


  // --------------------  GETTER  -----------------------

  public String getPubName() {
    return pubName;
  }

  public String getFirstPage() {
    return firstPage;
  }

  public String getLastPage() {
    return lastPage;
  }

  public String getVolume() {
    return volume;
  }

  public String getPubMedId() {
    return pubMedId;
  }

  public String getMedlineId() {
    return medlineId;
  }

  // TODO: Put this method's content somewhere else.
  public String toXML(int number, boolean intenzTextXML) {
    StringBuffer xmlStringBuffer = new StringBuffer();

    xmlStringBuffer.append(super.toXML(number, intenzTextXML));
    if (intenzTextXML) {
      xmlStringBuffer.append("<pubName>");
      xmlStringBuffer.append(pubName);
      xmlStringBuffer.append("</pubName>");
      xmlStringBuffer.append("<patent_no>");
      xmlStringBuffer.append("</patent_no>");
      xmlStringBuffer.append("<first_page>");
      xmlStringBuffer.append(firstPage);
      xmlStringBuffer.append("</first_page>");
      xmlStringBuffer.append("<last_page>");
      xmlStringBuffer.append(lastPage);
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
      xmlStringBuffer.append(pubMedId);
      xmlStringBuffer.append("</pub_med>");
      xmlStringBuffer.append("<medline>");
      xmlStringBuffer.append(medlineId);
      xmlStringBuffer.append("</medline>");
    } else {
      xmlStringBuffer.append("      <pubName>");
      xmlStringBuffer.append(pubName);
      xmlStringBuffer.append("</pubName>\n");
      xmlStringBuffer.append("      <patent_no>");
      xmlStringBuffer.append("</patent_no>\n");
      xmlStringBuffer.append("      <first_page>");
      xmlStringBuffer.append(firstPage);
      xmlStringBuffer.append("</first_page>\n");
      xmlStringBuffer.append("      <last_page>");
      xmlStringBuffer.append(lastPage);
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
      xmlStringBuffer.append(pubMedId);
      xmlStringBuffer.append("</pub_med>\n");
      xmlStringBuffer.append("      <medline>");
      xmlStringBuffer.append(medlineId);
      xmlStringBuffer.append("</medline>\n");
    }
    return xmlStringBuffer.toString();
  }

}
