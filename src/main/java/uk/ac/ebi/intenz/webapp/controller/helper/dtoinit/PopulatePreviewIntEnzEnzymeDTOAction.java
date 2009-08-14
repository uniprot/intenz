package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeXrefFactory;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriteException;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.dtos.*;
import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class PopulatePreviewIntEnzEnzymeDTOAction extends Action {
  private final static String INTENZ_ENTRY_PREVIEW_JSP_FWD = "intenz_entry_preview";

  private static final Logger LOGGER = Logger.getLogger(PopulatePreviewIntEnzEnzymeDTOAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulatePreviewIntEnzEnzymeDTOAction");
    request.setAttribute("title", "Preview entry " + ((EnzymeDTO) request.getSession().getAttribute("enzymeDTO")).getEc() + " - IntEnz Curator Application");
    return mapping.findForward(INTENZ_ENTRY_PREVIEW_JSP_FWD);
  }

}
