package uk.ac.ebi.intenz.tools.release.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This class provides methods for HTML and XML export of enzyme data.
 * @deprecated
 * @author Michael Darsow
 * @version $Revision: 1.1 $ $Date: 2008/02/21 12:44:56 $
 */
public class EnzymeEntryHelper {

   public static String toXML (EnzymeEntry enzymeEntry, SpecialCharacters encoding, EnzymeViewConstant view,
                               boolean intenzTextXML) {
      if ( enzymeEntry.getHistory().isDeletedRootNode() || enzymeEntry.getHistory().isTransferredRootNode() ) {
         return intenzTextXML ?
        		 deletedEntryToIntEnzTextXML(enzymeEntry, encoding) : "";
      } else {
         return intenzTextXML ?
        		 entryToIntEnzTextXML(enzymeEntry, encoding, view) : "";
      }
   }

   private static String deletedEntryToIntEnzTextXML (EnzymeEntry enzymeEntry, SpecialCharacters encoding) {
      StringBuffer xmlStringBuffer = new StringBuffer();
      xmlStringBuffer.append("<enzyme>");
      xmlStringBuffer.append("<ec>");
      xmlStringBuffer.append(enzymeEntry.getEc().toString());
      xmlStringBuffer.append("</ec>");
      xmlStringBuffer.append("<active>");
//      xmlStringBuffer.append(enzymeEntry.isActive());
      xmlStringBuffer.append("</active>");
      xmlStringBuffer.append("<deleted_text>");
      final HistoryGraph history = enzymeEntry.getHistory();
      if ( history.isDeletedRootNode() )
         xmlStringBuffer.append("Deleted entry: ");
      if ( history.isTransferredRootNode() ) {
         xmlStringBuffer.append("Transferred entry: ");
         if ( !history.getLatestHistoryEventOfAll().getNote().equals("") ) {
            xmlStringBuffer.append(" ");
            xmlStringBuffer.append(removeFormatting(encoding.xml2Display(
                  history.getLatestHistoryEventOfAll().getNote(),
                  EncodingType.SWISSPROT_CODE)));
            xmlStringBuffer.append(
                  removeFormatting(encoding.xml2Display(history.getLatestHistoryEventOfAll().getNote())));
         }
         // Add EC number of new enzyme.
         xmlStringBuffer.append(
               enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getAfterNode().getEnzymeEntry().getEc().toString());
      }
      xmlStringBuffer.append("</deleted_text>");

      // Get IntEnz common name. If no IntEnz common name exists then use NC-IUBMB common name.
      xmlStringBuffer.append("<common_names>");
      String commonName = enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName();
      if ( commonName == null ) commonName = enzymeEntry.getCommonName(EnzymeViewConstant.IUBMB).getName();
      xmlStringBuffer.append("<common_name>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(commonName)));
      xmlStringBuffer.append("</common_name>");
      xmlStringBuffer.append("<common_name>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(commonName, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</common_name>");
      xmlStringBuffer.append("</common_names>");

      // All other fields are empty.
      xmlStringBuffer.append("<description>");
      xmlStringBuffer.append("</description>");
      xmlStringBuffer.append("<reactions>");
      xmlStringBuffer.append("<reaction>");
      xmlStringBuffer.append("</reaction>");
      xmlStringBuffer.append("</reactions>");
      xmlStringBuffer.append("<cofactors>");
      xmlStringBuffer.append("<cofactor>");
      xmlStringBuffer.append("</cofactor>");
      xmlStringBuffer.append("</cofactors>");
      xmlStringBuffer.append("<synonyms>");
      xmlStringBuffer.append("<synonym>");
      xmlStringBuffer.append("</synonym>");
      xmlStringBuffer.append("</synonyms>");
      xmlStringBuffer.append("<syst_name>");
      xmlStringBuffer.append("</syst_name>");
      xmlStringBuffer.append("<comments>");
      xmlStringBuffer.append("</comments>");
      xmlStringBuffer.append("<links>");
      xmlStringBuffer.append("<link>");
      xmlStringBuffer.append("</link>");
      xmlStringBuffer.append("</links>");
      xmlStringBuffer.append("<references>");
      xmlStringBuffer.append("<reference>");
      xmlStringBuffer.append("<number>");
      xmlStringBuffer.append("</number>");
      xmlStringBuffer.append("<authors>");
      xmlStringBuffer.append("</authors>");
      xmlStringBuffer.append("<title>");
      xmlStringBuffer.append("</title>");
      xmlStringBuffer.append("<year>");
      xmlStringBuffer.append("</year>");
      xmlStringBuffer.append("<issue>");
      xmlStringBuffer.append("</issue>");
      xmlStringBuffer.append("<patent_no>");
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
      xmlStringBuffer.append("</reference>");
      xmlStringBuffer.append("</references>");

      xmlStringBuffer.append("<history>");
      xmlStringBuffer.append("[" + history.getRootNode().getHistoryLine() + "]");
      xmlStringBuffer.append("</history>");

      xmlStringBuffer.append("</enzyme>");

      return xmlStringBuffer.toString();
   }

   private static String entryToIntEnzTextXML (EnzymeEntry enzymeEntry, SpecialCharacters encoding,
                                               EnzymeViewConstant view) {

      StringBuffer xmlStringBuffer = new StringBuffer();

      xmlStringBuffer.append("<enzyme>");

      xmlStringBuffer.append("<ec>");
      xmlStringBuffer.append("EC " + enzymeEntry.getEc().toString());
      xmlStringBuffer.append("</ec>");

      xmlStringBuffer.append("<active>");
      xmlStringBuffer.append(enzymeEntry.isActive());
      xmlStringBuffer.append("</active>");

      xmlStringBuffer.append("<deleted_text>");
      xmlStringBuffer.append("</deleted_text>");

      // Get IntEnz common name. If no IntEnz common name exists then use NC-IUBMB common name.
      xmlStringBuffer.append("<common_names>");
      String commonName = enzymeEntry.getCommonName(view).getName();
      if ( commonName == null ) commonName = enzymeEntry.getCommonName(EnzymeViewConstant.IUBMB).getName();
      xmlStringBuffer.append("<common_name>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(commonName)));
      xmlStringBuffer.append("</common_name>");
      xmlStringBuffer.append("<common_name>");
      xmlStringBuffer.append(removeFormatting(encoding.xml2Display(commonName, EncodingType.SWISSPROT_CODE)));
      xmlStringBuffer.append("</common_name>");
      xmlStringBuffer.append("</common_names>");

      xmlStringBuffer.append("<description>");
      xmlStringBuffer.append("</description>");

      xmlStringBuffer.append("<reactions>");
      List reactions = enzymeEntry.getReactions(view);
      if ( reactions != null ) {
         for ( int iii = 0; iii < reactions.size(); iii++ ) {
            Reaction reaction = (Reaction) reactions.get(iii);
			if (reaction.getId() > Reaction.NO_ID_ASSIGNED
					&& !reaction.getStatus().isPublic()){
				continue; // Rhea reaction not public
			}
            xmlStringBuffer.append("<reaction>");
            xmlStringBuffer.append(removeFormatting(
                encoding.xml2Display(reaction.getTextualRepresentation())));
            xmlStringBuffer.append("</reaction>");
            xmlStringBuffer.append("<reaction>");
            xmlStringBuffer.append(removeFormatting(
                encoding.xml2Display(reaction.getTextualRepresentation(), EncodingType.SWISSPROT_CODE)));
            xmlStringBuffer.append("</reaction>");
            if (reaction.getId() > Reaction.NO_ID_ASSIGNED){
                xmlStringBuffer.append("<reaction>");
                xmlStringBuffer.append("RHEA:" + reaction.getId());
                xmlStringBuffer.append("</reaction>");
            }
         }
      }
      xmlStringBuffer.append("</reactions>");

      if ( view == EnzymeViewConstant.INTENZ ) {
         xmlStringBuffer.append("<cofactors>");
         Set<Object> cofactors = enzymeEntry.getCofactors();
         if ( cofactors != null ) {
            for (Object cofactor : cofactors) {
			   String cfText = cofactor.toString().replaceAll(" OR\\d ", " or ").replaceAll(" AND ", " and ");
               xmlStringBuffer.append("<cofactor>");
               xmlStringBuffer.append(removeFormatting(encoding.xml2Display(cfText)));
               xmlStringBuffer.append("</cofactor>");
               xmlStringBuffer.append("<cofactor>");
               xmlStringBuffer.append(
                     removeFormatting(encoding.xml2Display(cfText, EncodingType.SWISSPROT_CODE)));
               xmlStringBuffer.append("</cofactor>");
            }
         }
         xmlStringBuffer.append("</cofactors>");
      }

      xmlStringBuffer.append("<syst_name>");
      if (!enzymeEntry.getSystematicName().getName().equals("-")){
         xmlStringBuffer.append(removeFormatting(encoding.xml2Display(enzymeEntry.getSystematicName().getName())));
      }
      xmlStringBuffer.append("</syst_name>");

      if (!enzymeEntry.getSystematicName().getName().equals("-")){
         xmlStringBuffer.append("<syst_name>");
         xmlStringBuffer.append(removeFormatting(
               encoding.xml2Display(enzymeEntry.getSystematicName().getName(), EncodingType.SWISSPROT_CODE)));
         xmlStringBuffer.append("</syst_name>");
      }

      xmlStringBuffer.append("<synonyms>");
      List synonyms = enzymeEntry.getSynonyms(view);
      if ( synonyms.size() > 0 ) {
         for ( int iii = 0; iii < synonyms.size(); iii++ ) {
            EnzymeName synonym = (EnzymeName) synonyms.get(iii);
            xmlStringBuffer.append("<synonym>");
            xmlStringBuffer.append(removeFormatting(encoding.xml2Display(synonym.getName())));
            if ( synonym.getQualifier().toString().equals("MIS") ) {
               xmlStringBuffer.append(" [misleading]");
            }

            if ( synonym.getQualifier().toString().equals("OBS") ) {
               xmlStringBuffer.append(" [obsolete]");
            }

            if ( synonym.getQualifier().toString().equals("AMB") ) {
               xmlStringBuffer.append(" [ambiguous]");
            }
            xmlStringBuffer.append("</synonym>");

            xmlStringBuffer.append("<synonym>");
            xmlStringBuffer.append(
                  removeFormatting(encoding.xml2Display(synonym.getName(), EncodingType.SWISSPROT_CODE)));
            if ( synonym.getQualifier().toString().equals("MIS") ) {
               xmlStringBuffer.append(" [misleading]");
            }

            if ( synonym.getQualifier().toString().equals("OBS") ) {
               xmlStringBuffer.append(" [obsolete]");
            }

            if ( synonym.getQualifier().toString().equals("AMB") ) {
               xmlStringBuffer.append(" [ambiguous]");
            }
            xmlStringBuffer.append("</synonym>");
         }
      } else {
         xmlStringBuffer.append("<synonym>");
         xmlStringBuffer.append("</synonym>");
      }
      xmlStringBuffer.append("</synonyms>");

      xmlStringBuffer.append("<comments>");
      List comments = enzymeEntry.getComments(view);
      if ( comments != null ) {
         for ( int iii = 0; iii < comments.size(); iii++ ) {
            EnzymeComment comment = (EnzymeComment) comments.get(iii);
            if ( !comment.getCommentText().trim().equals("") ) {
               xmlStringBuffer.append("<comment>");
               xmlStringBuffer.append(removeFormatting(encoding.xml2Display(comment.getCommentText())));
               xmlStringBuffer.append("</comment>");
               xmlStringBuffer.append("<comment>");
               xmlStringBuffer.append(
                     removeFormatting(encoding.xml2Display(comment.getCommentText(), EncodingType.SWISSPROT_CODE)));
               xmlStringBuffer.append("</comment>");
            }
         }
      }
      xmlStringBuffer.append("</comments>");

      xmlStringBuffer.append("<links>");
      SortedSet links = enzymeEntry.getLinks(view);
      if ( links.size() > 0 || links.size() > 0 ) {
         for ( Iterator it = links.iterator(); it.hasNext(); ) {
        	 // remove links from preliminary ECs to external preliminary ECs?
            EnzymeLink link = (EnzymeLink) it.next();
            xmlStringBuffer.append("<link>");
            xmlStringBuffer.append(link.getAccession());
            xmlStringBuffer.append("</link>");
         }
      } else {
         xmlStringBuffer.append("<link>");
         xmlStringBuffer.append("</link>");
      }
      xmlStringBuffer.append("</links>");

      xmlStringBuffer.append("<references>");
      List references = enzymeEntry.getReferences();
      boolean refExists = false;
      for ( int iii = 0; iii < references.size(); iii++ ) {
         refExists = true;
         Reference reference = (Reference) references.get(iii);
         xmlStringBuffer.append("<reference>");
         xmlStringBuffer.append(EnzymeReferenceHelper.referenceToIntEnzTextXML(reference, iii, encoding));
         xmlStringBuffer.append("</reference>");
      }
      if ( !refExists ) {
         xmlStringBuffer.append("<reference>");
         xmlStringBuffer.append("<number>");
         xmlStringBuffer.append("</number>");
         xmlStringBuffer.append("<authors>");
         xmlStringBuffer.append("</authors>");
         xmlStringBuffer.append("<title>");
         xmlStringBuffer.append("</title>");
         xmlStringBuffer.append("<year>");
         xmlStringBuffer.append("</year>");
         xmlStringBuffer.append("<issue>");
         xmlStringBuffer.append("</issue>");
         xmlStringBuffer.append("<patent_no>");
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
         xmlStringBuffer.append("</reference>");
      }
      xmlStringBuffer.append("</references>");

      xmlStringBuffer.append("<history>");
      xmlStringBuffer.append("[" + enzymeEntry.getHistory().getRootNode().getHistoryLine() + "]");
      xmlStringBuffer.append("</history>");

      xmlStringBuffer.append("</enzyme>");

      return xmlStringBuffer.toString();
   }

   protected static String removeFormatting (String text) {
      text = text.replaceAll("\\<\\/?small\\>", "");
      text = text.replaceAll("\\<\\/?sup\\>", "");
      text = text.replaceAll("\\<\\/?sub\\>", "");
      text = text.replaceAll("\\<\\/?b\\>", "");
      text = text.replaceAll("\\<\\/?i\\>", "");
      text = text.replaceAll("\\<\\/?p\\/?\\>", "");
      return text;
   }




}
