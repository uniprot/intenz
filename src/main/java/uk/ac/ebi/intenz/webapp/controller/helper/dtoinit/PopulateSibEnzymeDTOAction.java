package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.tools.sib.helper.FFWriterHelper;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.dtos.SibEnzymeDTO;
import uk.ac.ebi.intenz.webapp.helper.HyperlinkHelper;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class PopulateSibEnzymeDTOAction extends Action {
  private static final String SIB_ENTRY_JSP_FWD = "sib_entry";

  private static final Logger LOGGER = Logger.getLogger(PopulateSibEnzymeDTOAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateSibEnzymeDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
    EncodingType encodingType = EncodingType.SWISSPROT_CODE;
    populateForm((EnzymeEntry) request.getAttribute("result"), form, encoding, encodingType);
    return mapping.findForward(SIB_ENTRY_JSP_FWD);
  }

  private void populateForm(EnzymeEntry enzymeEntry, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType) throws SPTRException {
    // Get SPTREnzymeEntry.
     // gets the SPTR interface expected to use in the FFwriter
    EnzymeEntryImpl sibEnzymeEntry = SibEntryHelper.getSibEnzymeEntry(enzymeEntry, encoding, encodingType);
    SibEnzymeDTO sibEnzymeDTO = (SibEnzymeDTO) form;
     // convenience used for links and headings
    sibEnzymeDTO.setId("" + enzymeEntry.getId());
    sibEnzymeDTO.setEc(enzymeEntry.getEc().toString());
     // Entire entry from ID to end of entry createed in FFwriter
	sibEnzymeDTO.setEntry(HyperlinkHelper.linkEcNumbers(
		FFWriterHelper.createXrefHyperlinks(EnzymeFlatFileWriter.export(sibEnzymeEntry))));
     // status code used specifically for suggested or proposed entries in the view.
    sibEnzymeDTO.setStatusCode(enzymeEntry.getStatus().getCode());
  }

}
