package uk.ac.ebi.intenz.webapp.controller.modification;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
import uk.ac.ebi.intenz.mapper.CommonProceduresMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EventPackageMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.exceptions.DeregisterException;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;
import uk.ac.ebi.intenz.webapp.utilities.UnitOfWork;

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
public class TransferEntryAction extends CurationAction {
  private static final Logger LOGGER = Logger.getLogger(TransferEntryAction.class);
  private final static String ERROR_FWD = "error";
  private final static String SEARCH_BY_EC_ACTION_FWD = "searchEc";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    // Do standard checks in the parent's execute method.
    ActionForward forward = super.execute(mapping, form, request, response);
    if (forward != null) return forward;

//    EnzymeDTO sessionEnzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
    String ec = enzymeDTO.getEc();
    Long enzymeId = new Long(enzymeDTO.getId());
    Connection con = (Connection) request.getSession().getAttribute("connection");
    EntryLockSingleton els = (EntryLockSingleton) request.getSession().getServletContext().getAttribute("entryLock");
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    ActionMessages errors = new ActionMessages();
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");
    try {
      // Check if the new EC number is valid.
      EnzymeCommissionNumber transferredEc = EnzymeCommissionNumber.valueOf(enzymeDTO.getTransferredEc());
      if (!isValidEc(transferredEc, errors, con)) {
        saveErrors(request, errors);
        keepToken(request);
        return mapping.getInputForward();
      }
      enzymeDTO.setEc(transferredEc.toString());

      // Check if a clone already exists.
      if (enzymeEntryMapper.cloneExists(enzymeId, con)) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.clone.exists", ec));
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
      Long transferredEnzymeId = commonProceduresMapper.createClone(enzymeId, con);
      LOGGER.info("EC " + ec + " (ID: " + enzymeId + ") cloned. New enzyme ID: " + transferredEnzymeId);
      enzymeDTO.setId(transferredEnzymeId.toString());

      // Always set the status to 'suggested'.
      enzymeDTO.setStatusCode(EnzymeStatusConstant.SUGGESTED.getCode());
      enzymeDTO.setStatusText(EnzymeStatusConstant.SUGGESTED.toString());

      // Commit
      LOGGER.info("Committing form data.");
      unitOfWork.commit(enzymeDTO, con);
      LOGGER.info("Data subimtted");

      // Store event.
      EventPackageMapper eventPackageMapper = new EventPackageMapper();
      eventPackageMapper.insertFutureTransferEvent(enzymeId, transferredEnzymeId,
                                                   enzymeDTO.getLatestHistoryEventNote(), con);
      con.commit();

      LOGGER.info("Transfer event stored.");
    } catch (SQLException e){
        con.rollback();
        throw e;
    } catch (EcException e) {
      errors.add("transferredEc", new ActionMessage("errors.application.ec.invalid", enzymeDTO.getEc()));
      saveErrors(request, errors);
      keepToken(request);
      return mapping.getInputForward();
    } catch (DeregisterException e) {
      LOGGER.error(e.getMessage());
      // Create standard error message (see 'struts_config.xml').
      throw e;
    } finally { // release lock
      els.releaseLock(enzymeId.toString());
      LOGGER.info("Lock of EC " + ec + " (ID: " + enzymeId + " released.");
    }

    // Forward to 'searchEc' Action to show both entries.
    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(enzymeDTO.getEc());
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_BY_EC_ACTION_FWD);
  }

  /**
   * Checks if the new EC number can be used for this transfer process.
   *
   * @param ec       The new EC number.
   * @param errors Stores potential error errors.
   * @param con      The database connection.
   * @return <code>true</code> if the EC number is valid and can be assigned to the transferred entry.
   * @throws SQLException if a database exception occurs.
   */
  private boolean isValidEc(EnzymeCommissionNumber ec, ActionMessages errors, Connection con) throws SQLException {
    assert ec != null : "Parameter 'ec' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    assert con != null : "Parameter 'con' must not be null.";

    try {
      // Check if the EC already exists.
      if (EnzymeEntryMapper.ecExists(ec, con)) {
        errors.add("transferredEc", new ActionMessage("errors.application.ec.exists", ec.toString()));
        return false;
      }

      // Check if the corresponding sub-subclass exists.
      EnzymeCommissionNumber subclassEc = EnzymeCommissionNumber.valueOf(ec.getEc1(), ec.getEc2());
      final boolean subSubclassExists = EnzymeEntryMapper.ecExists(subclassEc, con);
      if (!subSubclassExists) {
        errors.add("transferredEc", new ActionMessage("errors.application.ec.nosubsubclass", ec.toString()));
        return subSubclassExists;
      }
    } catch (EcException e) {
      errors.add("transferredEc", new ActionMessage("errors.application.ec.invalid"));
    }
    return true;
  }

}
