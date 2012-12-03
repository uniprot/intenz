package uk.ac.ebi.intenz.webapp.helper;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;

import java.util.List;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class ReferenceHelper {

//  private static final Logger LOGGER = Logger.getLogger(ReferenceHelper.class);

  public static String renderReferences(List references) {
    StringBuffer html = new StringBuffer();
    html.append("<table valign=\"top\" width=\"100%\">");
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO reference = (ReferenceDTO) references.get(iii);
      if (reference.getType().equals("J")) html.append(getJournalHTMLTableRow(reference, iii+1));
      if (reference.getType().equals("B")) html.append(getBookHTMLTableRow(reference, iii+1));
      if (reference.getType().equals("P")) html.append(getPatentHTMLTableRow(reference, iii+1));
    }
    html.append("</table>");
    return html.toString();
  }

  private static String getJournalHTMLTableRow(ReferenceDTO journal, int orderIn) {
    StringBuffer referenceString = new StringBuffer();

    referenceString.append("<tr><td align=\"right\" valign=\"top\">");
    referenceString.append(orderIn);
    referenceString.append(".</td><td>");
    referenceString.append(journal.getAuthors());
    referenceString.append(" ");
    referenceString.append(journal.getTitle());
    referenceString.append(" ");
    if (!journal.getPubName().equals("")) {
      referenceString.append("<i>");
      referenceString.append(journal.getPubName());
      referenceString.append("</i> ");
    }
    if (!journal.getVolume().equals("")) {
      referenceString.append(journal.getVolume());
      referenceString.append(" ");
    }
    if (!journal.getYear().equals("")) {
      referenceString.append("(");
      referenceString.append(journal.getYear());
      referenceString.append(") ");
    }
    if (!journal.getFirstPage().equals("")) {
      referenceString.append(journal.getFirstPage());
      if (!journal.getLastPage().equals("")) {
        referenceString.append("-");
        referenceString.append(journal.getLastPage());
        referenceString.append(". ");
      } else {
        referenceString.append(" only. ");
      }
    }
    if (!journal.getPubMedId().equals("")) {
      referenceString.append("[PMID: <a target=\"_blank\" href=\"http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-id+IntEnz+[medline-PMID:");
      referenceString.append(journal.getPubMedId());
      referenceString.append("]+-e\">");
      referenceString.append(journal.getPubMedId());
      referenceString.append("</a>]");
    }
    referenceString.append("</td></tr>");

    return referenceString.toString();
  }

  private static String getBookHTMLTableRow(ReferenceDTO book, int orderIn) {
    StringBuffer referenceString = new StringBuffer();

    referenceString.append("<tr><td align=\"right\" valign=\"top\">");
    referenceString.append(orderIn);
    referenceString.append(".</td><td>");
    referenceString.append(book.getAuthors());
    referenceString.append(" ");
    referenceString.append(book.getTitle());
    referenceString.append(" ");
    if (!book.getEditor().equals("")) {
      referenceString.append("In: ");
      referenceString.append(book.getEditor());
      referenceString.append(", ");
    }
    if (!book.getPubName().equals("")) {
      referenceString.append("<i>");
      referenceString.append(book.getPubName());
      referenceString.append("</i>, ");
    }
    if (!book.getEdition().equals("")) {
      referenceString.append(book.getEdition());
      referenceString.append(", ");
    }
    if (!book.getVolume().equals("")) {
      referenceString.append("vol. ");
      referenceString.append(book.getVolume());
      referenceString.append(", ");
    }
    if (!book.getPublisher().equals("")) {
      referenceString.append(book.getPublisher());
      referenceString.append(", ");
    }
    if (!book.getPublisherPlace().equals("")) {
      referenceString.append(book.getPublisherPlace());
      referenceString.append(", ");
    }
    if (!book.getYear().equals("")) {
      if (book.getFirstPage().equals("") && book.getLastPage().equals(""))
        referenceString.append(book.getYear());
      else {
        referenceString.append(book.getYear());
        referenceString.append(", ");
      }
    }
    if (!book.getFirstPage().equals("")) {
      if (!book.getLastPage().equals("")) {
        referenceString.append("pp. ");
      } else {
        referenceString.append("p. ");
      }
      referenceString.append(book.getFirstPage());
      if (!book.getLastPage().equals("")) {
        referenceString.append("-");
        referenceString.append(book.getLastPage());
      }
      referenceString.append(".");
    }
    referenceString.append("</td></tr>");

    return referenceString.toString();
  }

  private static String getPatentHTMLTableRow(ReferenceDTO patent, int orderIn) {
    StringBuffer referenceString = new StringBuffer();

    referenceString.append("<tr><td align=\"right\" valign=\"top\">");
    referenceString.append(orderIn);
    referenceString.append(".</td><td>");
    referenceString.append(patent.getAuthors());
    referenceString.append(" ");
    referenceString.append(patent.getTitle());
    if (!patent.getPubMedId().equals("")) {
      referenceString.append("<i>");
      referenceString.append(patent.getPubMedId());
      referenceString.append("</i>,");
    }
    if (!patent.getYear().equals("")) {
      referenceString.append(patent.getYear());
    }
    referenceString.append("</td></tr>");

    return referenceString.toString();
  }
}
