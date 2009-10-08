package uk.ac.ebi.intenz.domain.reference;

import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * This class stores additional information about a book.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/04/20 13:44:24 $
 */
public class Book extends Reference {

  private String firstPage;

  private String lastPage;

  private String pubName;

  private String edition;

  private String editor;

  private String volume;

  private String publisher;

  private String publisherPlace;

  /**
   * Returns a <code>Book</code> instance.
   *
   * @param id             The pubId.
   * @param authors        String containing all author names.
   * @param title          The title of the publication.
   * @param year           The year of the publication.
   * @param firstPage      The first page of the publication in the book.
   * @param lastPage       The last page of the publication in the book.
   * @param pubName        The name of the book.
   * @param edition        The edition.
   * @param editor         The editor(s).
   * @param volume         The book's volume.
   * @param publisher      The company that published this book.
   * @param publisherPlace The place of the company.
   * @throws NullPointerException if any paramter is <code>null</code>.
   */
  public Book(Long id, String authors, String title, String year, String firstPage, String lastPage, String pubName,
              String edition, String editor, String volume, String publisher, String publisherPlace, EnzymeViewConstant view,
                 EnzymeSourceConstant source) {
    super(id, authors, title, year, view, source);
    if (firstPage == null || lastPage == null || pubName == null || edition == null ||
            editor == null || volume == null || publisher == null || publisherPlace == null)
      throw new NullPointerException("No parameter must be 'null'");
    this.firstPage = firstPage;
    this.lastPage = lastPage;
    this.pubName = pubName;
    this.edition = edition;
    this.editor = editor;
    this.volume = volume;
    this.publisher = publisher;
    this.publisherPlace = publisherPlace;
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
    if (!editor.equals("")) {
      referenceString.append("In: ");
      referenceString.append(getEditor(true));
      referenceString.append(", ");
    }
    if (!pubName.equals("")) {
      referenceString.append("<i>");
      referenceString.append(pubName);
      referenceString.append("</i>, ");
    }
    if (!edition.equals("")) {
      referenceString.append(getEdition(true));
      referenceString.append(", ");
    }
    if (!volume.equals("")) {
      referenceString.append("vol. ");
      referenceString.append(volume);
      referenceString.append(", ");
    }
    if (!publisher.equals("")) {
      referenceString.append(publisher);
      referenceString.append(", ");
    }
    if (!publisherPlace.equals("")) {
      referenceString.append(publisherPlace);
      referenceString.append(", ");
    }
    if (!year.equals("")) {
      if (firstPage.equals("") && lastPage.equals(""))
        referenceString.append(year);
      else {
        referenceString.append(year);
        referenceString.append(", ");
      }
    }
    if (!firstPage.equals("")) {
      if (!lastPage.equals("")) {
        referenceString.append("pp. ");
      } else {
        referenceString.append("p. ");
      }
      referenceString.append(firstPage);
      if (!lastPage.equals("")) {
        referenceString.append("-");
        referenceString.append(lastPage);
      }
      referenceString.append(".");
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
    if (!(o instanceof Book)) return false;
    if (!super.equals(o)) return false;

    final Book book = (Book) o;

    if (edition != null ? !edition.equals(book.edition) : book.edition != null) return false;
    if (editor != null ? !editor.equals(book.editor) : book.editor != null) return false;
    if (firstPage != null ? !firstPage.equals(book.firstPage) : book.firstPage != null) return false;
    if (pubName != null ? !pubName.equals(book.pubName) : book.pubName != null) return false;
    if (lastPage != null ? !lastPage.equals(book.lastPage) : book.lastPage != null) return false;
    if (publisher != null ? !publisher.equals(book.publisher) : book.publisher != null) return false;
    if (publisherPlace != null ? !publisherPlace.equals(book.publisherPlace) : book.publisherPlace != null) return false;
    if (volume != null ? !volume.equals(book.volume) : book.volume != null) return false;

    return true;
  }

  /**
   * Generates a hash code for this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result = super.hashCode();
    result = 29 * result + (firstPage != null ? firstPage.hashCode() : 0);
    result = 29 * result + (lastPage != null ? lastPage.hashCode() : 0);
    result = 29 * result + (pubName != null ? pubName.hashCode() : 0);
    result = 29 * result + (edition != null ? edition.hashCode() : 0);
    result = 29 * result + (editor != null ? editor.hashCode() : 0);
    result = 29 * result + (volume != null ? volume.hashCode() : 0);
    result = 29 * result + (publisher != null ? publisher.hashCode() : 0);
    result = 29 * result + (publisherPlace != null ? publisherPlace.hashCode() : 0);
    return result;
  }


  // --------------------  GETTER -----------------------

  public String getFirstPage() {
    return firstPage;
  }

  public String getLastPage() {
    return lastPage;
  }

  /**
   * Returns the edition string as it is stored in the database or formatted.
   *
   * @param formatted <code>true</code>, if it should be formatted.
   * @return the (formatted) edition string.
   */
  public String getEdition(boolean formatted) {
    if (formatted) return formatEditionString();
    return edition;
  }

  /**
   *
   * @return the formatted edition string.
   */
  public String getEdition(){
     return formatEditionString();
  }

  /**
   * Returns the editor string as it is stored in the database or formatted.
   *
   * @param formatted <code>true</code>, if it should be formatted.
   * @return the (formatted) editor string.
   */
  public String getEditor(boolean formatted) {
    if (formatted) return formatEditorString();
    return editor;
  }

  /**
   *
   * @return The formatted editor string.
   */
  public String getEditor(){
      return formatEditorString();
  }

  public String getPublisher() {
    return publisher;
  }

  public String getPublisherPlace() {
    return publisherPlace;
  }

  public String getPubName() {
    return pubName;
  }

  public String getVolume() {
    return volume;
  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Formats the editor string according the usual layout (using "(Ed.)" or "(Eds.)").
   *
   * @return the formatted editor string.
   */
  private String formatEditorString() {
    if (!editor.equals("")) {
      int index = 0;
      int commaCount = 0;
      while (editor.indexOf(",", index) > -1) {
        commaCount++;
        index = editor.indexOf(",", index) + 1;
      }

      if (commaCount > 1) {
        return editor + " (Eds.)";

      } else {
        return editor + " (Ed.)";
      }
    }

    return editor;
  }

  /**
   * Formats the edition string according the usual layout (using <code>"1st ed."</code>,
   * <code>"2nd ed."</code> etc.).
   *
   * @return the formatted edition string.
   */
  private String formatEditionString() {
    String formattedEdition = "";
    if (!edition.equals("")) {
      String lastNumber = edition.substring(edition.length() - 1);

      try {
        if (edition.length() > 1) {
          switch (Integer.parseInt(lastNumber)) {
            case 1:
              formattedEdition = edition.substring(0, edition.length() - 2) + "1st ed.";
              break;
            case 2:
              formattedEdition = edition.substring(0, edition.length() - 2) + "2nd ed.";
              break;
            case 3:
              formattedEdition = edition.substring(0, edition.length() - 2) + "3rd ed.";
              break;
            default:
              formattedEdition = edition.substring(0, edition.length()) + "th ed.";
              break;
          }
        } else {
          switch (Integer.parseInt(lastNumber)) {
            case 1:
              formattedEdition = "1st ed.";
              break;
            case 2:
              formattedEdition = "2nd ed.";
              break;
            case 3:
              formattedEdition = "3rd ed.";
              break;
            default:
              formattedEdition += "th ed.";
              break;
          }
        }
      } catch (NumberFormatException e) {
        return edition;
      }
    }

    return formattedEdition;
  }

  // TODO: put method's content somewhere else.
  public String toXML(int number, boolean intenzTextXML) {
    StringBuffer xmlStringBuffer = new StringBuffer();

    xmlStringBuffer.append(super.toXML(number, intenzTextXML));
    if (intenzTextXML) {
      SpecialCharacters encoding = SpecialCharacters.getInstance(null);
//      SpecialCharacters encoding = SpecialCharacters.getInstance("/projects/intenz/prg/webapps/curator/", null);

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
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(edition)));
      xmlStringBuffer.append("</edition>");
//      if (encoding.isSpecialCharactersElement(edition)) { // Store text only version as well.
      xmlStringBuffer.append("<edition>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(edition, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</edition>");
//      }
      xmlStringBuffer.append("<editor>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(editor)));
      xmlStringBuffer.append("</editor>");
//      if (encoding.isSpecialCharactersElement(editor)) { // Store text only version as well.
      xmlStringBuffer.append("<editor>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(editor, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</editor>");
//      }
      xmlStringBuffer.append("<volume>");
      xmlStringBuffer.append(volume);
      xmlStringBuffer.append("</volume>");
      xmlStringBuffer.append("<pub_place>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(publisherPlace)));
      xmlStringBuffer.append("</pub_place>");
//      if (encoding.isSpecialCharactersElement(publisherPlace)) { // Store text only version as well.
      xmlStringBuffer.append("<pub_place>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(publisherPlace, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</pub_place>");
//      }
      xmlStringBuffer.append("<pub_company>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(publisher)));
      xmlStringBuffer.append("</pub_company>");
//      if (encoding.isSpecialCharactersElement(publisher)) { // Store text only version as well.
        xmlStringBuffer.append("<pub_company>");
        xmlStringBuffer.append(removeFormatting(encoding.xml2Display(publisher, EncodingType.SWISSPROT_CODE)));
        xmlStringBuffer.append("</pub_company>");
//      }
      xmlStringBuffer.append("<pub_med>");
      xmlStringBuffer.append("</pub_med>");
      xmlStringBuffer.append("<medline>");
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
      xmlStringBuffer.append(encodeTags(edition));
      xmlStringBuffer.append("</edition>\n");
      xmlStringBuffer.append("      <editor>");
      xmlStringBuffer.append(encodeTags(editor));
      xmlStringBuffer.append("</editor>\n");
      xmlStringBuffer.append("      <volume>");
      xmlStringBuffer.append(volume);
      xmlStringBuffer.append("</volume>\n");
      xmlStringBuffer.append("      <pub_place>");
      xmlStringBuffer.append(encodeTags(publisherPlace));
      xmlStringBuffer.append("</pub_place>\n");
      xmlStringBuffer.append("      <pub_company>");
      xmlStringBuffer.append(encodeTags(publisher));
      xmlStringBuffer.append("</pub_company>\n");
      xmlStringBuffer.append("      <pub_med>");
      xmlStringBuffer.append("</pub_med>\n");
      xmlStringBuffer.append("      <medline>");
      xmlStringBuffer.append("</medline>\n");
    }
    return xmlStringBuffer.toString();
  }

  private String encodeTags(String name) {
    Pattern p = Pattern.compile("<");
    Matcher m = p.matcher(name);

    Pattern p2 = Pattern.compile(">");
    Matcher m2 = p2.matcher(m.replaceAll("&lt;"));

    return m2.replaceAll("&gt;");
  }

  private String removeFormatting(String text) {
    text = text.replaceAll("\\<small\\>", "");
    text = text.replaceAll("\\<\\/small\\>", "");
    text = text.replaceAll("\\<b\\>", "");
    text = text.replaceAll("\\<\\/b\\>", "");
    text = text.replaceAll("\\<i\\>", "");
    text = text.replaceAll("\\<\\/i\\>", "");
    return text;
  }
}
