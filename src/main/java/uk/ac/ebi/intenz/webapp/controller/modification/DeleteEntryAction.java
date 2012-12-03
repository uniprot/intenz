package uk.ac.ebi.intenz.webapp.controller.modification;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
import uk.ac.ebi.intenz.mapper.CommonProceduresMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EventPackageMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.exceptions.DeregisterException;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;
import uk.ac.ebi.intenz.webapp.utilities.UnitOfWork;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class DeleteEntryAction extends CurationAction {
  private static final Logger LOGGER =
	  Logger.getLogger(DeleteEntryAction.class.getName());
  private final static String ERROR_FWD = "error";
  private final static String SEARCH_BY_EC_ACTION_FWD = "searchEc";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    // Do standard checks in the parent's execute method.
    ActionForward forward = super.execute(mapping, form, request, response);
    if (forward != null) return forward;

    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
//    EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
    Long enzymeId = new Long(enzymeDTO.getId());
    Connection con = (Connection) request.getSession().getAttribute("connection");
    EntryLockSingleton els = (EntryLockSingleton) request.getSession().getServletContext().getAttribute("entryLock");
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    ActionMessages errors = new ActionMessages();
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");
    try {
      // Check if a clone already exists.
      if (enzymeEntryMapper.cloneExists(enzymeId, con)) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.clone.exists", enzymeDTO.getEc()));
        saveErrors(request, errors);
        els.releaseLock(enzymeId.toString()); // release lock
        request.setAttribute("title", "Error - IntEnz Curator Application");
        return mapping.findForward(ERROR_FWD);
      }

      // Set the standard remark in the audit tables.
      AuditPackageMapper auditPackageMapper = new AuditPackageMapper();
      auditPackageMapper.setRemark(AuditPackageMapper.STANDARD_REMARK, con);

      // Create clone.
      CommonProceduresMapper commonProceduresMapper = new CommonProceduresMapper();
      Long newEnzymeId = commonProceduresMapper.createClone(enzymeId, con);
      LOGGER.info("EC " + enzymeDTO.getEc() + " (ID: " + enzymeId + ") cloned. New enzyme ID: " + newEnzymeId);
      enzymeDTO.setId(newEnzymeId.toString());

      // Set clone's history line.
      enzymeDTO.setHistoryLine(getNewHistoryLine(enzymeDTO.getHistoryLine()));

      // Always set the status to 'suggested'.
      enzymeDTO.setStatusCode(Status.SUGGESTED.getCode());
      enzymeDTO.setStatusText(Status.SUGGESTED.toString());

      // Commit
      LOGGER.info("Committing form data.");
      unitOfWork.commit(enzymeDTO, con);
      LOGGER.info("Data subimtted");

      // Store event.
      EventPackageMapper eventPackageMapper = new EventPackageMapper();
      eventPackageMapper.insertFutureDeletionEvent(enzymeId, newEnzymeId, enzymeDTO.getLatestHistoryEventNote(), con);

      con.commit();

      LOGGER.info("Delete event stored.");
    } catch (SQLException e){
        con.rollback();
        throw e;
    } catch (DeregisterException e) {
      LOGGER.error(e.getMessage());
      // Create standard error message (see 'struts_config.xml').
      throw e;
    } finally { // release lock
      els.releaseLock(enzymeId.toString());
      LOGGER.info("Lock of EC " + enzymeDTO.getEc() + " (ID: " + enzymeId + " released.");
    }

    // Forward to 'searchEc' Action to show both entries.
    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(enzymeDTO.getEc());
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_BY_EC_ACTION_FWD);
  }

  /**
   * Gets the new history line by appending 'deleted ...'.
   *
   * @param oldHistory The enzyme's old history.
   * @return the new history line.
   */
  private String getNewHistoryLine(String oldHistory) {
    assert oldHistory != null : "Parameter 'oldHistory' must not be null.";

    GregorianCalendar gc = new GregorianCalendar();
    int year = gc.get(GregorianCalendar.YEAR);
    StringBuffer historyLine = new StringBuffer();
    historyLine.append(oldHistory);
    historyLine.append(", deleted ");
    historyLine.append(year);
    return historyLine.toString();
  }
}
