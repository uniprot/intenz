package uk.ac.ebi.intenz.tools.sib.helper;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeXrefFactory;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriteException;
import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This class was used to write the SIB entry.
 * Now use SibEntryHelper from the curator application.
 *
 * @deprecated Use the new class uk.ac.ebi.intenz.webapp.helper.SibEntryHelper
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class EnzymeHelper {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeHelper.class.getName());

   /**
    * @deprecated Use uk.ac.ebi.intenz.webapp.helper.SibEntryHelper
    * @param enzymeEntry
    * @param encoding
    * @param encodingType
    * @param translate
    * @return
    * @throws SPTRException
    */
  public static EnzymeEntryImpl getSibEnzymeEntry(EnzymeEntry enzymeEntry,
                                                  SpecialCharacters encoding, EncodingType encodingType,
                                                  boolean translate) throws SPTRException {
    final XCharsASCIITranslator translator = XCharsASCIITranslator.getInstance();

    EnzymeEntryImpl sibEnzymeEntry = new EnzymeEntryImpl();
    try {
      // Deleted or transferred entries are treated differently.
      if (enzymeEntry.getHistory().isDeletedRootNode() || enzymeEntry.getHistory().isTransferredRootNode())
        return getDeletedTransferredSibEntry(enzymeEntry);

      // ID
      sibEnzymeEntry.setEC(enzymeEntry.getEc().toString());

      // DEs
      List commonNames = enzymeEntry.getCommonNames();
      boolean hasSibCommonName = false;
      for (int iii = 0; iii < commonNames.size(); iii++) {
        EnzymeName commonName = (EnzymeName) commonNames.get(iii);
        if (EnzymeViewConstant.isInSIBView(commonName.getView().toString())) {
          hasSibCommonName = true;
          if (translate)
            sibEnzymeEntry.setCommonName(encoding.xml2Display(translator.toASCII(commonName.getName().trim(), false, true), encodingType));
          else
            sibEnzymeEntry.setCommonName(encoding.xml2Display(commonName.getName().toString().trim(), encodingType));
          break;
        }
      }
      if(!hasSibCommonName) {
        LOGGER.error("EC "+enzymeEntry.getEc().toString()+" does not have a SIB common name.");
        sibEnzymeEntry.setCommonName("--error--");
      }

      // ANs
      List synonyms = enzymeEntry.getSynonyms();
      for (int iii = 0; iii < synonyms.size(); iii++) {
        EnzymeName synonym = (EnzymeName) synonyms.get(iii);
        if (EnzymeViewConstant.isInSIBView(synonym.getView().toString())) {
          if (translate)
            sibEnzymeEntry.addSynonym(encoding.xml2Display(translator.toASCII(synonym.getName().trim(), false, false), encodingType));
          else
            sibEnzymeEntry.addSynonym(encoding.xml2Display(synonym.getName().trim(), encodingType));
        }
      }

       // CAs
      EnzymaticReactions reactions = enzymeEntry.getEnzymaticReactions();
      for (int iii = 0; iii < reactions.size(); iii++) {
        Reaction reaction = reactions.getReaction(iii);
        if (EnzymeViewConstant.isInSIBView(reactions.getReactionView(iii).toString())) {
          if (translate)
            sibEnzymeEntry.addReaction(encoding.xml2Display(translator.toASCII(reaction.getTextualRepresentation().trim(), true, false), encodingType));
          else
            sibEnzymeEntry.addReaction(encoding.xml2Display(reaction.getTextualRepresentation().trim(), encodingType));
        }
      }

    // CFs
    Set<Object> cofactors = enzymeEntry.getCofactors();
    StringBuffer cofactorString = new StringBuffer();
    for (Iterator it = cofactors.iterator(); it.hasNext();) {
    	Object cofactor = it.next();
        if (EnzymeViewConstant.isInView(EnzymeViewConstant.SIB, cofactor)) {
        	String s = (cofactor instanceof OperatorSet)?
    				((OperatorSet) cofactor).toString(true, true) :
					cofactor.toString();
            cofactorString.append(encoding.xml2Display(translator.toASCII(s, false, false), encodingType));
            if (cofactorString.length() > 0 && it.hasNext())
                cofactorString.append(";");
            cofactorString.append(" ");
        }
    }

      // CCs
      List comments = enzymeEntry.getComments();
      StringBuffer commentString = new StringBuffer();
      for (int iii = 0; iii < comments.size(); iii++) {
        EnzymeComment comment = (EnzymeComment) comments.get(iii);
        if (EnzymeViewConstant.isInSIBView(comment.getView().toString())) {
          commentString.append(encoding.xml2Display(translator.toASCII(comment.getCommentText().trim(), false, false), encodingType));
          commentString.append(" ");
        }
      }
      sibEnzymeEntry.setComment(commentString.toString().trim());

      // MIM/PROSITE/UNIPROT xrefs.
      Set xrefs = enzymeEntry.getLinks();
      for (Iterator it = xrefs.iterator(); it.hasNext();) {
        EnzymeLink enzymeLink = (EnzymeLink) it.next();
        if (enzymeLink.getXrefDatabaseConstant() == XrefDatabaseConstant.MIM &&
            EnzymeViewConstant.isInSIBView(enzymeLink.getView().toString())) {
          SPTRCrossReference DICrossReference = getDICrossReference(enzymeLink);
          sibEnzymeEntry.addCrossReference(DICrossReference);
        }
        if (enzymeLink.getXrefDatabaseConstant() == XrefDatabaseConstant.PROSITE &&
            EnzymeViewConstant.isInSIBView(enzymeLink.getView().toString())) {
          SPTRCrossReference PRCrossReference = getPRCrossReference(enzymeLink);
          sibEnzymeEntry.addCrossReference(PRCrossReference);
        }
        if (enzymeLink.getXrefDatabaseConstant() == XrefDatabaseConstant.SWISSPROT &&
            EnzymeViewConstant.isInSIBView(enzymeLink.getView().toString())) {
          SPTRCrossReference DRCrossReference = getDRCrossReference(enzymeLink);
          sibEnzymeEntry.addCrossReference(DRCrossReference);
        }
      }
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }

    return sibEnzymeEntry;
  }

  

   private static EnzymeEntryImpl getDeletedTransferredSibEntry(EnzymeEntry enzymeEntry) throws SPTRException {
    assert enzymeEntry != null : "Parameter 'enzymeEntry' must not be null.";
    EnzymeEntryImpl sibEnzymeEntry = new EnzymeEntryImpl();
    sibEnzymeEntry.setTransferredOrDeleted(true);
    sibEnzymeEntry.setEC(enzymeEntry.getEc().toString());
    if (enzymeEntry.getHistory().isDeletedRootNode()) {
      sibEnzymeEntry.setCommonName("Deleted entry");
    } else {
      sibEnzymeEntry.setCommonName("Transferred entry: " +
                                   enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getAfterNode().getEnzymeEntry().getEc().toString());
    }
    return sibEnzymeEntry;
  }

  private static SPTRCrossReference getDICrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.MIM);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession());
      enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLink.getName());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }

  private static SPTRCrossReference getPRCrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.PROSITE);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }

  private static SPTRCrossReference getDRCrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.SWISSPROT);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession());
      enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLink.getName());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }

}
