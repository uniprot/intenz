package uk.ac.ebi.intenz.tools.sib.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.domain.history.HistoryNode;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
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
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class SibEntryHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeEntryHelper.class);

  public static EnzymeEntryImpl getSibEnzymeEntry(uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry enzymeEntry,
                                                  SpecialCharacters encoding, EncodingType encodingType) throws SPTRException {
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
      for (int iii = 0; iii < commonNames.size(); iii++) {
        EnzymeName commonName = (EnzymeName) commonNames.get(iii);
        if (EnzymeViewConstant.isInSIBView(commonName.getView().toString())) {
          sibEnzymeEntry.setCommonName(encoding.xml2Display(translator.toASCII(commonName.getName().trim(), false, true), encodingType));
          break;
        }
      }

      // ANs
       // need to create a new list so that the reference is not stored in the enzyme entry
      List synonyms = new ArrayList();
      synonyms.addAll(enzymeEntry.getSynonyms());
      // We also had systematic names to our view
      synonyms.add(enzymeEntry.getSystematicName());

      List synonymStringsTranslated = new ArrayList();
      for (int iii = 0; iii < synonyms.size(); iii++) {
        EnzymeName synonym = (EnzymeName) synonyms.get(iii);
        if (EnzymeViewConstant.isInSIBView(synonym.getView().toString())) {
          synonymStringsTranslated.add(encoding.xml2Display(translator.toASCII(synonym.getName().trim(), false, false), encodingType));
        }
      }
       // Do the alphabetical sorting here
      Collections.sort(synonymStringsTranslated, StringUtil.getStringReverseCasingComparator());
      sibEnzymeEntry.setSynonyms((String[]) synonymStringsTranslated.toArray(sibEnzymeEntry.getSynonyms()));

      // CAs
      EnzymaticReactions reactions = enzymeEntry.getEnzymaticReactions();
      for (int iii = 0; iii < reactions.size(); iii++) {
        Reaction reaction = reactions.getReaction(iii);
        // For now, Rhea-ctions won't appear in enzyme.dat:
        if (reaction.getId() > Reaction.NO_ID_ASSIGNED /*&& !reaction.getStatus().equals(Status.OK)*/)
        	// Not accepted Rhea-ction
        	continue;
        if (EnzymeViewConstant.isInSIBView(reactions.getReactionView(iii).toString())) {
        	String rtr = reaction.getTextualRepresentation().trim()
        		.replace(" <?> "," = ").replace(" => "," = ").replace(" <= "," = ").replace(" <=> "," = ");
          sibEnzymeEntry.addReaction(encoding.xml2Display(translator.toASCII(rtr, true, false), encodingType));
        }
      }

      // CFs
      Set<Object> cofactors = enzymeEntry.getCofactors();
      List cofactorNames = new ArrayList();
      StringBuffer cofactorString = new StringBuffer();
      for (Object cofactor : cofactors) {
    	  if (EnzymeViewConstant.isInView(EnzymeViewConstant.SIB, cofactor)){
              String cfText = cofactor.toString()
                      .replaceAll(" OR\\d? ", " or ")
                      .replaceAll(" AND ", " and ")
                      .trim();
              cofactorNames.add(encoding.xml2Display(translator.toASCII(cfText, false, false), encodingType));
    	  }    		  
      }
      Collections.sort(cofactorNames, StringUtil.getStringReverseCasingComparator());
      for (int i = 0; i < cofactorNames.size(); i++){
          cofactorString.append(cofactorNames.get(i));
          if (i < cofactorNames.size()-1) cofactorString.append(";");
          cofactorString.append(" ");
      }
      sibEnzymeEntry.setCofactors(cofactorString.toString().trim());

      // CCs
      List comments = enzymeEntry.getComments();
      StringBuffer commentString = new StringBuffer();
      for (int iii = 0; iii < comments.size(); iii++) {
        EnzymeComment comment = (EnzymeComment) comments.get(iii);
        if (EnzymeViewConstant.isInSIBView(comment.getView().toString())) {
          commentString.append(encoding.xml2Display(translator.toASCII(comment.getCommentText().trim(), false, false ), encodingType));
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

   /**
    * Method returns the name qualifier in the correct format.
    * @param constant
    * @return
    */
   private static String getSynonymQualifier(EnzymeNameQualifierConstant constant){
      StringBuffer buffer = new StringBuffer("");
      if(constant!=null){         
         String constantString = constant.toASCII(constant);
         if( !StringUtil.isNullOrEmpty(constantString) ){
            buffer.append(" ");
            buffer.append(constantString);
         }
      }

      return buffer.toString();
   }

   private static EnzymeEntryImpl getDeletedTransferredSibEntry(uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry enzymeEntry) throws SPTRException {
    assert enzymeEntry != null : "Parameter 'enzymeEntry' must not be null.";
    EnzymeEntryImpl sibEnzymeEntry = new EnzymeEntryImpl();
    sibEnzymeEntry.setTransferredOrDeleted(true);
    sibEnzymeEntry.setEC(enzymeEntry.getEc().toString());
    if (enzymeEntry.getHistory().isDeletedRootNode()) {
      sibEnzymeEntry.setCommonName("Deleted entry");
    } else {
    	HistoryNode rootNode = enzymeEntry.getHistory().getRootNode();
    	HistoryNode lastRootNode = ((HistoryEvent) new TreeSet(rootNode.getEdges()).last()).getAfterNode();
    	HistoryNode lastNode = null;
    	try {
    		lastNode = ((HistoryEvent) new TreeSet(lastRootNode.getEdges()).last()).getAfterNode();
    		if (lastNode != null){
    			lastRootNode = lastNode;
    		} else { // transferred to null?? Let's look closer...
                
    		}
    	} catch (NullPointerException e){}
        String commonName = "Transferred entry: " + lastRootNode.getEnzymeEntry().getEc().toString();
    	sibEnzymeEntry.setCommonName(XCharsASCIITranslator.getInstance().toASCII(commonName, false, true));
//      sibEnzymeEntry.setCommonName("Transferred entry: " +
//                                   enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getAfterNode().getEnzymeEntry().getEc().toString());
    }
    return sibEnzymeEntry;
  }

  private static SPTRCrossReference getDICrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.MIM);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession());
      enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLink.getName().trim());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }

  private static SPTRCrossReference getPRCrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.PROSITE);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession().trim());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }

  private static SPTRCrossReference getDRCrossReference(EnzymeLink enzymeLink) throws SPTRException {
    SPTRCrossReference enzymeXref = null;
    try {
      enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.SWISSPROT);
      enzymeXref.setAccessionNumber(enzymeLink.getAccession().trim());
      enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLink.getName().trim());
    } catch (SPTRException e) {
      throw new EnzymeFlatFileWriteException(e);
    }
    return enzymeXref;
  }
}
