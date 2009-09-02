package uk.ac.ebi.intenz.webapp.controller.export;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.dtos.FlatFileExportForm;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This Action exports all approved entries using the SIB flat file format.
 *
 * This flat file will only contain information which is relevant to SIB entries (e.g. no references are included)
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class ExportFlatFileAction extends Action {

  private static final Logger LOGGER = Logger.getLogger(ExportFlatFileAction.class);

  private final static String RESULT_JSP = "result";
  private final static String ERROR_JSP_FWD = "error";

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    FlatFileExportForm exportForm = (FlatFileExportForm) form;
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    Connection con = (Connection) request.getSession().getAttribute("connection");
    ActionMessages errors = new ActionMessages();
    MessageResources messageResources = getResources(request);
    List sibEntries = null;
    try {
      sibEntries = enzymeEntryMapper.exportApprovedSibEntries(con);
    } catch (SQLException e) {
      LOGGER.error(messageResources.getMessage("errors.application.database"), e);
//      errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.standard"));
        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.other", e.getMessage()));
      saveErrors(request, errors);
      return mapping.findForward(ERROR_JSP_FWD);
    }

    if (sibEntries != null) {
      SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
      List translatedSibEntries = null;

      for (int iii = 0; iii < sibEntries.size(); iii++) {
        EnzymeEntryImpl sibEnzymeEntry = SibEntryHelper.getSibEnzymeEntry((EnzymeEntry) sibEntries.get(iii), encoding, EncodingType.SWISSPROT_CODE);
        translatedSibEntries.add(sibEnzymeEntry);
      }

      if(translatedSibEntries != null) {
        EnzymeFlatFileWriter.export(translatedSibEntries, exportForm.getVersionNumber(), new File(messageResources.getMessage("application.export.flatfile")));
      } else {
        LOGGER.error(messageResources.getMessage("errors.application.export.translation"));
        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.export.translation"));
        saveErrors(request, errors);
        return mapping.findForward(ERROR_JSP_FWD);
      }
    } else {
      LOGGER.error(messageResources.getMessage("errors.application.export.noSibEntries"));
        errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.export.noSibEntries"));
        saveErrors(request, errors);
        return mapping.findForward(ERROR_JSP_FWD);
    }

    return mapping.findForward(RESULT_JSP);
  }


  // ------------------- PRIVATE METHODS ------------------------

}
