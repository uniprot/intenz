package uk.ac.ebi.intenz.webapp.utilities;

import java.util.List;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.cdb.webservice.Authors;
import uk.ac.ebi.cdb.webservice.AuthorsList;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.cdb.webservice.JournalInfo;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;

/**
 * This class provides access method(s) to the PubMed database.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class PubMedAccessor {

  /**
   * Creates a {@link Journal Journal} instance by using the PubMed ID of the reference.
   * <p/>
   * The method accesses the PubMed database to obtain an XML version of this journal entry in PubMed.
   *
   */
  public static synchronized void fetchPubMedJournal(
		  List<ReferenceDTO> references, int index)
  throws DomainException {
    ReferenceDTO referenceDTO = references.get(index);
    final String pubMedId = referenceDTO.getPubMedId();
    try {
      Integer.parseInt(pubMedId);
    } catch (NumberFormatException e) {
      throw new DomainException("pubMedFetch[" + index + "]", "errors.form.reference.pubMedFetch.id");
    }
    if (referenceDTO.getPubMedId().equals("")) return;

      Result citation = null;
      JournalInfo issue = null;

      StringBuffer authorsString = new StringBuffer();
      String title = "";
      String year = "";
      String pubName = "";
      String firstPage = "";
      String lastPage = "";
      String volume = "";
      String newPubMedId = "";
      try {
          citation = CitexploreWSClient.getCitation(DataSource.MED, pubMedId);
          if (citation == null) return;

          issue = citation.getJournalInfo();
          AuthorsList authors = citation.getAuthorList();
          for (int i = 0; i < authors.getAuthor().size(); i++){
              Authors author = authors.getAuthor().get(i);
              authorsString.append(author.getLastName());
              authorsString.append(", ");
              authorsString.append(getFormattedInitials(author.getInitials()));
              if (i < authors.getAuthor().size()-1) authorsString.append(", ");
          }

          title = citation.getTitle();

          year = String.valueOf(issue.getYearOfPublication());

          pubName = issue.getJournal().getISOAbbreviation();
          if (StringUtil.isNullOrEmpty(pubName)) pubName = issue.getJournal().getTitle();

          String pages = citation.getPageInfo();
          if (pages.indexOf("-") > -1) {
              firstPage = pages.substring(0, pages.indexOf("-"));
              lastPage = pages.substring(pages.indexOf("-") + 1);
              if (lastPage.length() < firstPage.length()) { // e.g. '633-9'
                  lastPage = calculateLastPage(firstPage, lastPage);
              }
          } else {
              firstPage = pages;
          }

          volume = issue.getVolume();

          newPubMedId = citation.getId();
      } catch (Exception e) {
          e.printStackTrace();
          throw new DomainException("pubMedFetch[" + index + "]",
        		  "errors.form.reference.pubMedFetch.standard");
      } catch (Throwable t){
    	  t.printStackTrace();
    	  throw new DomainException("pubMedFetch[" + index + "]",
    			  "errors.form.reference.pubMedFetch.ws");
      }

      referenceDTO.setType("J");
      referenceDTO.setXmlAuthors(authorsString.toString());
      referenceDTO.setXmlTitle(title);
      referenceDTO.setYear(year);
      referenceDTO.setXmlPubName(pubName);
      referenceDTO.setFirstPage(firstPage);
      referenceDTO.setLastPage(lastPage);
      referenceDTO.setVolume(volume);
      referenceDTO.setPubMedId(newPubMedId);
      referenceDTO.setViewDisplayImage(EnzymeViewConstant.INTENZ.toDisplayImage());
      referenceDTO.setViewDisplayString(EnzymeViewConstant.INTENZ.toDisplayString());
      referenceDTO.setSource(EnzymeSourceConstant.INTENZ.toString());
      referenceDTO.setSourceDisplay(EnzymeSourceConstant.INTENZ.toDisplayString());
  }


  // ---------------------- PRIVATE methods ----------------------------

  private static String getFormattedInitials(String unformattedInitials) {
    StringBuffer initials = new StringBuffer();
    char[] initialArray = unformattedInitials.toCharArray();
    for (int iii = 0; iii < initialArray.length; iii++) {
      char c = initialArray[iii];
      initials.append(c);
      initials.append(". ");
    }

    return initials.toString().trim();
  }

  private static String calculateLastPage(String firstPageString, String lastPageString) {
    char[] firstPageArray = firstPageString.toCharArray();
    StringBuffer lastPage = new StringBuffer();
    for (int iii = 0; iii < firstPageString.length() - lastPageString.length(); iii++) {
      lastPage.append(firstPageArray[iii]);
    }
    lastPage.append(lastPageString);

    return lastPage.toString();
  }

}
