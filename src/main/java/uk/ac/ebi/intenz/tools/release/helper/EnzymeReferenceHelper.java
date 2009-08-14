/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.release.helper;

import uk.ac.ebi.intenz.tools.release.helper.EnzymeEntryHelper;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;


/**
 * EnzymeReferenceHelper used to display the XML text version.
 *
 * @author Paula
 * @version $id 06-Jun-2005 10:18:25
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          Paula            06-Jun-2005         Created class<br>
 */
public class EnzymeReferenceHelper {

   /**
    * Used to transform the reference object into the IntEnz text XML used in
    * the search engine.
    * 
    * @param reference
    * @param number
    * @param encoding
    * @return
    */
   public static String referenceToIntEnzTextXML (Reference reference, int number, SpecialCharacters encoding) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(referenceXML(reference, number, encoding));
      if ( reference instanceof Book ) {
         Book book = (Book) reference;
         buffer.append(bookXML(book, encoding));
      } else if ( reference instanceof Journal ) {
         Journal journal = (Journal) reference;
         buffer.append(journalXML(journal, encoding));
      } else if ( reference instanceof Patent ) {
         Patent patent = (Patent) reference;
         buffer.append(patentXML(patent, encoding));
      }
      return buffer.toString();
   }

   /** PRIVATE METHODS **/

   /**
    * Private method to display the XML data.
    * @param reference
    * @param number
    * @param encoding
    * @return
    */
   private static String referenceXML (Reference reference, int number, SpecialCharacters encoding) {
      StringBuffer xmlStringBuffer = new StringBuffer();

      xmlStringBuffer.append("<number>");
      xmlStringBuffer.append(number + ".");
      xmlStringBuffer.append("</number>");
      xmlStringBuffer.append("<authors>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(reference.getAuthors())));
      xmlStringBuffer.append("</authors>");
//      if (encoding.isSpecialCharactersElement(authors)) { // Store text only version as well.
      xmlStringBuffer.append("<authors>");
      xmlStringBuffer.append(EnzymeEntryHelper.
            removeFormatting(encoding.xml2Display(reference.getAuthors(), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</authors>");
//      }
      xmlStringBuffer.append("<title>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(reference.getTitle())));
      xmlStringBuffer.append("</title>");
//      if (encoding.isSpecialCharactersElement(title)) { // Store text only version as well.
      xmlStringBuffer.append("<title>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(reference.getTitle(), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</title>");
//      }
      xmlStringBuffer.append("<year>");
      xmlStringBuffer.append(reference.getYear());
      xmlStringBuffer.append("</year>");

      return xmlStringBuffer.toString();
   }

   private static String bookXML (Book book, SpecialCharacters encoding) {
      StringBuffer xmlStringBuffer = new StringBuffer();
      xmlStringBuffer.append("<pubName>");
      xmlStringBuffer.append(book.getPubName());
      xmlStringBuffer.append("</pubName>");
      xmlStringBuffer.append("<patent_no>");
      xmlStringBuffer.append("</patent_no>");
      xmlStringBuffer.append("<first_page>");
      xmlStringBuffer.append(book.getFirstPage());
      xmlStringBuffer.append("</first_page>");
      xmlStringBuffer.append("<last_page>");
      xmlStringBuffer.append(book.getLastPage());
      xmlStringBuffer.append("</last_page>");
      xmlStringBuffer.append("<edition>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(book.getEdition(false))));
      xmlStringBuffer.append("</edition>");
//      if (encoding.isSpecialCharactersElement(edition)) { // Store text only version as well.
      xmlStringBuffer.append("<edition>");
      xmlStringBuffer.append(EnzymeEntryHelper.
            removeFormatting(encoding.xml2Display(book.getEdition(false), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</edition>");
//      }
      xmlStringBuffer.append("<editor>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(book.getEditor(false))));
      xmlStringBuffer.append("</editor>");
//      if (encoding.isSpecialCharactersElement(editor)) { // Store text only version as well.
      xmlStringBuffer.append("<editor>");
      xmlStringBuffer.append(EnzymeEntryHelper.
            removeFormatting(encoding.xml2Display(book.getEditor(false), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</editor>");
//      }
      xmlStringBuffer.append("<volume>");
      xmlStringBuffer.append(book.getVolume());
      xmlStringBuffer.append("</volume>");
      xmlStringBuffer.append("<pub_place>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(book.getPublisherPlace())));
      xmlStringBuffer.append("</pub_place>");
//      if (encoding.isSpecialCharactersElement(publisherPlace)) { // Store text only version as well.
      xmlStringBuffer.append("<pub_place>");
      xmlStringBuffer.append(EnzymeEntryHelper.
            removeFormatting(encoding.xml2Display(book.getPublisherPlace(), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</pub_place>");
//      }
      xmlStringBuffer.append("<pub_company>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(book.getPublisher())));
      xmlStringBuffer.append("</pub_company>");
//      if (encoding.isSpecialCharactersElement(publisher)) { // Store text only version as well.
      xmlStringBuffer.append("<pub_company>");
      xmlStringBuffer.append(EnzymeEntryHelper.removeFormatting(encoding.xml2Display(book.getPublisher(), EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</pub_company>");
//      }
      xmlStringBuffer.append("<pub_med>");
      xmlStringBuffer.append("</pub_med>");
      xmlStringBuffer.append("<medline>");
      xmlStringBuffer.append("</medline>");

      return xmlStringBuffer.toString();
   }

   private static String journalXML (Journal journal, SpecialCharacters encoding) {
      StringBuffer xmlStringBuffer = new StringBuffer();
      xmlStringBuffer.append("<pubName>");
      xmlStringBuffer.append(journal.getPubName());
      xmlStringBuffer.append("</pubName>");
      xmlStringBuffer.append("<patent_no>");
      xmlStringBuffer.append("</patent_no>");
      xmlStringBuffer.append("<first_page>");
      xmlStringBuffer.append(journal.getFirstPage());
      xmlStringBuffer.append("</first_page>");
      xmlStringBuffer.append("<last_page>");
      xmlStringBuffer.append(journal.getLastPage());
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
      xmlStringBuffer.append(journal.getPubMedId());
      xmlStringBuffer.append("</pub_med>");
      xmlStringBuffer.append("<medline>");
      xmlStringBuffer.append(journal.getMedlineId());
      xmlStringBuffer.append("</medline>");
      return xmlStringBuffer.toString();
   }

   private static String patentXML (Patent patent, SpecialCharacters encoding) {
      StringBuffer xmlStringBuffer = new StringBuffer();
      xmlStringBuffer.append("<issue>");
      xmlStringBuffer.append("</issue>");
      xmlStringBuffer.append("<patent_no>");
      xmlStringBuffer.append(patent.getPatentNumber());
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
      return xmlStringBuffer.toString();
   }

}
