package uk.ac.ebi.intenz.webapp.controller.modification;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
import uk.ac.ebi.intenz.mapper.EventPackageMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;
import uk.ac.ebi.intenz.webapp.utilities.UnitOfWork;
import uk.ac.ebi.intenz.webapp.exceptions.DeregisterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class DeleteEntryUpdateAction extends CurationAction {
  private static final Logger LOGGER =
	  Logger.getLogger(DeleteEntryUpdateAction.class.getName());
  private final static String SEARCH_BY_EC_ACTION_FWD = "searchEc";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    // Do standard checks in the parent's execute method.
    ActionForward forward = super.execute(mapping, form, request, response);
    if (forward != null) return forward;

    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
    Long enzymeId = new Long(enzymeDTO.getId());
    Connection con = (Connection) request.getSession().getAttribute("connection");
    EntryLockSingleton els = (EntryLockSingleton) request.getSession().getServletContext().getAttribute("entryLock");
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");
    try {
      // Set the standard remark in the audit tables.
      AuditPackageMapper auditPackageMapper = new AuditPackageMapper();
      auditPackageMapper.setRemark(AuditPackageMapper.STANDARD_REMARK, con);

      // Commit
      LOGGER.info("Committing form data.");
      unitOfWork.commit(enzymeDTO, con);
      LOGGER.info("Data subimtted");

      // Update event.
      EventPackageMapper eventPackageMapper = new EventPackageMapper();
      eventPackageMapper.updateFutureDeletionEvent(Integer.parseInt(enzymeDTO.getLatestHistoryEventGroupId()),
              Integer.parseInt(enzymeDTO.getLatestHistoryEventId()), enzymeDTO.getLatestHistoryEventNote(),
              enzymeDTO.getStatusCode(), con);
      con.commit();

      LOGGER.info("Delete event updated.");
    } catch (SQLException e) {
        con.rollback();
        throw e;
    } catch (DeregisterException e) {
      LOGGER.error(e.getMessage());
      // Create standard error message (see 'struts_config.xml').
      throw e;
    } finally { // release lock
      els.releaseLock(enzymeId.toString());
      LOGGER.info("Lock of EC " + enzymeDTO.getEc() + " (ID: " + enzymeId + ") released.");
    }

    // Forward to 'searchEc' Action to show both entries.
    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(enzymeDTO.getEc());
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_BY_EC_ACTION_FWD);
  }

}
