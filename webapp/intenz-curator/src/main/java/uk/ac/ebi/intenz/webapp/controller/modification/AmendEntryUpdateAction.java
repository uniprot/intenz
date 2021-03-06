package uk.ac.ebi.intenz.webapp.controller.modification;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
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
public class AmendEntryUpdateAction extends CurationAction {
  private static final Logger LOGGER =
	  Logger.getLogger(AmendEntryUpdateAction.class.getName());
  private final static String SEARCH_BY_EC_ACTION_FWD = "searchEc";

    @Override
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    // Do standard checks in the parent's execute method.
    ActionForward forward = super.execute(mapping, form, request, response);
    if (forward != null) return forward;

    ActionMessages errors = new ActionMessages();
    EnzymeDTO enzymeDTO = (EnzymeDTO) form;

    // Validate the entry's lists.
//    IntEnzValidations.validateEnzymeDTOLists(enzymeDTO, errors);
//    if (!errors.isEmpty()) {
//      saveErrors(request, errors);
//      return mapping.getInputForward();
//    }
    
    Connection con = (Connection) request.getSession().getAttribute("connection");
    EntryLockSingleton els = (EntryLockSingleton) request.getSession().getServletContext().getAttribute("entryLock");
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");
    try {
      // Set the standard remark in the audit tables.
      AuditPackageMapper auditPackageMapper = new AuditPackageMapper();
      auditPackageMapper.setRemark(AuditPackageMapper.STANDARD_REMARK, con);

      // Store event.
      if (!enzymeDTO.getStatusCode().equals(Status.PRELIMINARY.getCode())){
          EventPackageMapper eventPackageMapper = new EventPackageMapper();
          eventPackageMapper.updateFutureModificationEvent(Integer.parseInt(enzymeDTO.getLatestHistoryEventGroupId()),
                                                       Integer.parseInt(enzymeDTO.getLatestHistoryEventId()),
                                                       enzymeDTO.getStatusCode(), con);
      }
      // Commit
      LOGGER.info("Committing form data.");
      unitOfWork.commit(enzymeDTO, con);
      LOGGER.info("Data subimtted");

      con.commit();
      
      LOGGER.info("Amend event (update) finished.");
    } catch (SQLException e){
        con.rollback();
        throw e;
    } catch (EcException e) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.ec.invalid", enzymeDTO.getTransferredEc()));
      saveErrors(request, errors);
      keepToken(request);
      return mapping.getInputForward();
    } catch (DeregisterException e) {
      LOGGER.error(e.getMessage());
      // Create standard error message (see 'struts_config.xml').
      throw e;
    } finally { // release lock
      els.releaseLock(enzymeDTO.getId());
      LOGGER.info("Lock of EC " + enzymeDTO.getEc() + " (ID: " + enzymeDTO.getEc() + " released.");
    }

    // Forward to 'searchEc' Action to show both entries.
    LOGGER.info("Searching for EC " + enzymeDTO.getEc());
    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(enzymeDTO.getEc());
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_BY_EC_ACTION_FWD);
  }

}
