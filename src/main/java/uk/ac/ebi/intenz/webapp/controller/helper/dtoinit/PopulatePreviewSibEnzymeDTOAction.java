package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.intenz.tools.sib.helper.FFWriterHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeXrefFactory;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriteException;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.dtos.CofactorDTO;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.SibEnzymeDTO;
import uk.ac.ebi.intenz.webapp.helper.EnzymeNamesHelper;
import uk.ac.ebi.intenz.webapp.helper.HyperlinkHelper;
import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2008/03/31 11:46:19 $
 */
public class PopulatePreviewSibEnzymeDTOAction extends Action {
   private static final XCharsASCIITranslator TRANSLATOR = XCharsASCIITranslator.getInstance();

   private final static String SIB_ENTRY_PREVIEW_JSP_FWD = "sib_entry_preview";

   private static final Logger LOGGER = Logger.getLogger(PopulatePreviewSibEnzymeDTOAction.class);

    @Override
   public ActionForward execute (ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
      LOGGER.debug("PopulatePreviewSibEnzymeDTOAction");

      // Get special characters instance (set in the servlet handler's process method).
      SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
      EncodingType encodingType = EncodingType.SWISSPROT_CODE;
      populateForm((EnzymeDTO) request.getSession().getAttribute("enzymeDTO"), form, encoding, encodingType, true);
      request.setAttribute("title",
            "Preview entry " + ((EnzymeDTO) request.getSession().getAttribute("enzymeDTO")).getEc() + " - IntEnz Curator Application");
      return mapping.findForward(SIB_ENTRY_PREVIEW_JSP_FWD);
   }

   private void populateForm (EnzymeDTO enzymeDTO, ActionForm form, SpecialCharacters encoding,
                              EncodingType encodingType, boolean translate) throws SPTRException {
      // Get SPTREnzymeEntry.
      EnzymeEntryImpl sibEnzymeEntry = getSibEnzymeEntry(enzymeDTO, encoding, encodingType, translate);
      SibEnzymeDTO sibEnzymeDTO = (SibEnzymeDTO) form;
      sibEnzymeDTO.setId("" + enzymeDTO.getId());
      sibEnzymeDTO.setEc(enzymeDTO.getEc());
	  sibEnzymeDTO.setEntry(HyperlinkHelper.linkEcNumbers(
		  FFWriterHelper.createXrefHyperlinks(EnzymeFlatFileWriter.export(sibEnzymeEntry))));
      sibEnzymeDTO.setStatusCode(enzymeDTO.getStatusCode());
   }


   private EnzymeEntryImpl getSibEnzymeEntry (EnzymeDTO enzymeDTO, SpecialCharacters encoding, EncodingType encodingType,
                                              boolean translate) throws SPTRException {
      EnzymeEntryImpl sibEnzymeEntry = new EnzymeEntryImpl();
      try {
         // ID
         sibEnzymeEntry.setEC(enzymeDTO.getEc());

         // DEs
         List commonNames = enzymeDTO.getCommonNames();
         for ( int iii = 0; iii < commonNames.size(); iii++ ) {
            EnzymeNameDTO commonNameDTO = (EnzymeNameDTO) commonNames.get(iii);
            if ( EnzymeViewConstant.isInSIBView(commonNameDTO.getView()) ) {
               if ( translate )
                  sibEnzymeEntry.setCommonName(
                        encoding.xml2Display(TRANSLATOR.toASCII(commonNameDTO.getXmlName(), false, true), encodingType));
               else
                  sibEnzymeEntry.setCommonName(encoding.xml2Display(commonNameDTO.getName(), encodingType));
               break;
            }
         }

         // ANs
         List synonyms = new ArrayList();
         synonyms.addAll(enzymeDTO.getSynonyms());
         // add the systematic name if its the correct view
         synonyms.add(enzymeDTO.getSystematicName());
         List synonymStringsTranslated = new ArrayList();
         for ( int iii = 0; iii < synonyms.size(); iii++ ) {
            EnzymeNameDTO synonymDTO = (EnzymeNameDTO) synonyms.get(iii);
            if ( EnzymeViewConstant.isInSIBView(synonymDTO.getView()) ) {

               if ( translate )
                  synonymStringsTranslated.add(
                        encoding.xml2Display(TRANSLATOR.toASCII(synonymDTO.getXmlName(), false, false), encodingType));
               else
                  synonymStringsTranslated.add(encoding.xml2Display(synonymDTO.getName(), encodingType));
            }
         }
         // Do the alphabetical sorting
         Collections.sort(synonymStringsTranslated, EnzymeNamesHelper.getStringCasingComparator());
         sibEnzymeEntry.setSynonyms((String[]) synonymStringsTranslated.toArray(sibEnzymeEntry.getSynonyms()) );

         // CAs
         List<ReactionDTO> reactions = enzymeDTO.getReactionDtos();
         for ( int iii = 0; iii < reactions.size(); iii++ ) {
        	 ReactionDTO reactionDTO = reactions.get(iii);
        	 if (reactionDTO.getId() > Reaction.NO_ID_ASSIGNED)
        		 continue; // Ignore Rhea-ctions
        	 if ( EnzymeViewConstant.isInSIBView(reactionDTO.getView()) ) {
        		 String rtr = null;
        		 reactionDTO.getXmlTextualRepresentation().trim()
        		 	.replace(" <?> "," = ").replace(" => "," = ").replace(" <= "," = ").replace(" <=> "," = ");
        		 rtr = translate?
        			 TRANSLATOR.toASCII(reactionDTO.getXmlTextualRepresentation(), true, false) :
        			 reactionDTO.getTextualRepresentation();
    			 sibEnzymeEntry.addReaction(encoding.xml2Display(rtr, encodingType));
        	 }
         }

         // CFs
         List cofactors = enzymeDTO.getCofactors();
         StringBuffer cofactorString = new StringBuffer();
         for ( int iii = 0, iiiListSize = cofactors.size(); iii < iiiListSize; iii++ ) {
        	 CofactorDTO cofactorDTO = (CofactorDTO) cofactors.get(iii);
        	 if ( EnzymeViewConstant.isInSIBView(cofactorDTO.getView()) ) {
        		 String ct = translate?
    				 TRANSLATOR.toASCII(cofactorDTO.getXmlCofactorValue().trim(), false, false):
					 cofactorDTO.getCofactorValue().trim();
				 cofactorString.append(encoding.xml2Display(
						 ct.replaceAll(" AND ", " and ").replaceAll(" OR\\d ", " or "),
						 encodingType));
				 if ( iiiListSize > 1 && (iii + 1) != iiiListSize )
					 cofactorString.append(";");
				 cofactorString.append(" ");
        	 }
         }
         sibEnzymeEntry.setCofactors(cofactorString.toString().trim());

         // CCs
         List comments = enzymeDTO.getComments();
         StringBuffer commentString = new StringBuffer();
         for ( int iii = 0; iii < comments.size(); iii++ ) {
            CommentDTO commentDTO = (CommentDTO) comments.get(iii);
            if ( EnzymeViewConstant.isInSIBView(commentDTO.getView()) ) {
               commentString.append(
                     encoding.xml2Display(TRANSLATOR.toASCII(commentDTO.getXmlComment().trim(), false, false), encodingType));
//            commentString.append(encoding.xml2Display(commentDTO.getXmlComment().trim(), encodingType));
               commentString.append(" ");
            }
         }
         sibEnzymeEntry.setComment(commentString.toString().trim());

         // MIM/PROSITE/UNIPROT xrefs.
         List xrefs = enzymeDTO.getAllLinks();
         for ( Iterator it = xrefs.iterator(); it.hasNext(); ) {
            EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) it.next();
            if ( enzymeLinkDTO.getDatabaseName().equals( XrefDatabaseConstant.MIM.getDisplayName() ) &&
                  EnzymeViewConstant.isInSIBView(enzymeLinkDTO.getView().toString()) ) {
               SPTRCrossReference DICrossReference = getDICrossReference(enzymeLinkDTO);
               sibEnzymeEntry.addCrossReference(DICrossReference);
            }
            if ( enzymeLinkDTO.getDatabaseName().equals( XrefDatabaseConstant.PROSITE.getDisplayName() ) &&
                  EnzymeViewConstant.isInSIBView(enzymeLinkDTO.getView().toString()) ) {
               SPTRCrossReference PRCrossReference = getPRCrossReference(enzymeLinkDTO);
               sibEnzymeEntry.addCrossReference(PRCrossReference);
            }
            if ( enzymeLinkDTO.getDatabaseName().equals( XrefDatabaseConstant.SWISSPROT.getDisplayName() ) &&
                  EnzymeViewConstant.isInSIBView(enzymeLinkDTO.getView().toString()) ) {
               SPTRCrossReference DRCrossReference = getDRCrossReference(enzymeLinkDTO);
               sibEnzymeEntry.addCrossReference(DRCrossReference);
            }
         }
      } catch ( SPTRException e ) {
         throw new EnzymeFlatFileWriteException(e);
      }

      return sibEnzymeEntry;
   }

   private SPTRCrossReference getDICrossReference (EnzymeLinkDTO enzymeLinkDTO) throws SPTRException {
      SPTRCrossReference enzymeXref = null;
      try {
         enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.MIM);
         enzymeXref.setAccessionNumber(enzymeLinkDTO.getAccession());
         enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLinkDTO.getName());
      } catch ( SPTRException e ) {
         throw new EnzymeFlatFileWriteException(e);
      }
      return enzymeXref;
   }

   private SPTRCrossReference getPRCrossReference (EnzymeLinkDTO enzymeLinkDTO) throws SPTRException {
      SPTRCrossReference enzymeXref = null;
      try {
         enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.PROSITE);
         enzymeXref.setAccessionNumber(enzymeLinkDTO.getAccession());
      } catch ( SPTRException e ) {
         throw new EnzymeFlatFileWriteException(e);
      }
      return enzymeXref;
   }

   private SPTRCrossReference getDRCrossReference (EnzymeLinkDTO enzymeLinkDTO) throws SPTRException {
      SPTRCrossReference enzymeXref = null;
      try {
         enzymeXref = new EnzymeXrefFactory().newEnzymeCrossReference(EnzymeCrossReference.SWISSPROT);
         enzymeXref.setAccessionNumber(enzymeLinkDTO.getAccession());
         enzymeXref.setPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION, enzymeLinkDTO.getName());
      } catch ( SPTRException e ) {
         throw new EnzymeFlatFileWriteException(e);
      }
      return enzymeXref;
   }

}
