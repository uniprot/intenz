package uk.ac.ebi.intenz.domain.reference;

import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a reference and serves as the super class for all types of references.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:10 $
 */
public class Reference {
  Long pubId;

  String authors;

  String title;

  String year;

  EnzymeViewConstant view;

  EnzymeSourceConstant source;

  /**
   * Object cannot be created outside this class.
   *
   * @param id      The pubId.
   * @param authors The string containing all author names.
   * @param title   The title of the publication.
   * @param year    The year of the publication.
   */
  Reference(Long id, String authors, String title, String year, EnzymeViewConstant enzymeViewConstant, EnzymeSourceConstant source) {
    this.pubId = id;
    if (authors == null || title == null || year == null) throw new NullPointerException("No parameter must be 'null'");
    this.authors = authors;
    this.title = title;
    this.year = year;
    this.view = enzymeViewConstant;
    this.source = source;
  }

  /**
   * Standard <code>equals()</code> method.
   *
   * @param o The object to be compared to this one.
   * @return <code>true</code> if <code>o</code> is equal to this object.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Reference)) return false;

    final Reference reference = (Reference) o;

    if (authors != null ? !authors.equals(reference.authors) : reference.authors != null) return false;
    if (view != null ? !view.equals(reference.view) : reference.view !=
            null) return false;
    if (pubId != null ? !pubId.equals(reference.pubId) : reference.pubId != null) return false;
    if (source != null ? !source.equals(reference.source) : reference.source != null) return false;
    if (title != null ? !title.equals(reference.title) : reference.title != null) return false;
    if (year != null ? !year.equals(reference.year) : reference.year != null) return false;

    return true;
  }

  /**
   * Generates a hash code for this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result;
    result = (pubId != null ? pubId.hashCode() : 0);
    result = 29 * result + (authors != null ? authors.hashCode() : 0);
    result = 29 * result + (title != null ? title.hashCode() : 0);
    result = 29 * result + (year != null ? year.hashCode() : 0);
    result = 29 * result + (view != null ? view.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    return result;
  }

  // --------------------  GETTER -----------------------

  public Long getPubId() {
      return pubId;
    }

  public String getAuthors() {
    return authors;
  }

  public String getTitle() {
    return title;
  }

  public String getYear() {
    return year;
  }

  public EnzymeViewConstant getView() {
    return view;
  }

  public EnzymeSourceConstant getSource() {
    return source;
  }

  // TODO: Put this method's content somewhere else.
  public String toXML(int number, boolean intenzTextXML) {
    StringBuffer xmlStringBuffer = new StringBuffer();
    if (intenzTextXML) {
      SpecialCharacters encoding = SpecialCharacters.getInstance(null);

      xmlStringBuffer.append("<number>");
      xmlStringBuffer.append(number + ".");
      xmlStringBuffer.append("</number>");
      xmlStringBuffer.append("<authors>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(authors)));
      xmlStringBuffer.append("</authors>");
//      if (encoding.isSpecialCharactersElement(authors)) { // Store text only version as well.
      xmlStringBuffer.append("<authors>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(authors, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</authors>");
//      }
      xmlStringBuffer.append("<title>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(title)));
      xmlStringBuffer.append("</title>");
//      if (encoding.isSpecialCharactersElement(title)) { // Store text only version as well.
      xmlStringBuffer.append("<title>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(title, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</title>");
//      }
      xmlStringBuffer.append("<year>");
      xmlStringBuffer.append(year);
      xmlStringBuffer.append("</year>");
    } else {
      xmlStringBuffer.append("      <number>");
      xmlStringBuffer.append(number + ".");
      xmlStringBuffer.append("</number>\n");
      xmlStringBuffer.append("      <authors>");
      xmlStringBuffer.append(encodeTags(removeFormatting(authors)));
      xmlStringBuffer.append("</authors>\n");
      xmlStringBuffer.append("      <title>");
      xmlStringBuffer.append(encodeTags(removeFormatting(title)));
      xmlStringBuffer.append("</title>\n");
      xmlStringBuffer.append("      <year>");
      xmlStringBuffer.append(year);
      xmlStringBuffer.append("</year>\n");
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
