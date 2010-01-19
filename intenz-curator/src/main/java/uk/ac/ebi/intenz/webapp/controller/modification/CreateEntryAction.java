package uk.ac.ebi.intenz.webapp.controller.modification;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EventPackageMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.exceptions.DeregisterException;
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
public class CreateEntryAction extends CurationAction {
  private static final Logger LOGGER =
	  Logger.getLogger(CreateEntryAction.class.getName());
  private final static String SEARCH_BY_EC_ACTION_FWD = "searchEc";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    // Do standard checks in the parent's execute method.
    ActionForward forward = super.execute(mapping, form, request, response);
    if (forward != null) return forward;

    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
    Connection con = (Connection) request.getSession().getAttribute("connection");
    ActionMessages errors = new ActionMessages();
    UnitOfWork unitOfWork = (UnitOfWork) request.getSession().getAttribute("uow");
    try {
      EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(enzymeDTO.getEc());
      if (!isValidEc(ec, errors, con)) {
        saveErrors(request, errors);
        return mapping.getInputForward();
      }
      enzymeDTO.setEc(ec.toString());

      // Set the standard remark in the audit tables.
      AuditPackageMapper auditPackageMapper = new AuditPackageMapper();
      auditPackageMapper.setRemark(AuditPackageMapper.STANDARD_REMARK, con);

      // Commit
      LOGGER.info("Committing form data.");
      unitOfWork.commit(enzymeDTO, con);
      LOGGER.info("Data subimtted");

      // Store event.
      if (!ec.getType().equals(Type.PRELIMINARY)){
        EventPackageMapper eventPackageMapper = new EventPackageMapper();
        eventPackageMapper.insertFutureCreationEvent(new Long(enzymeDTO.getId()), con);
      }

      con.commit();

      LOGGER.info("Create event finished.");
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
    }

    LOGGER.info("Searching for EC " + enzymeDTO.getEc());
    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(enzymeDTO.getEc());
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_BY_EC_ACTION_FWD);
//    return mapping.findForward(POPULATE_INTENZ_ENZYME_DTO_ACTION);
  }

  /**
   * Checks if the new EC number can be used for the new enzyme entry.
   *
   * @param ec       The new EC number.
   * @param errors Stores potential error errors.
   * @param con      The database connection.
   * @return <code>true</code> if the EC number is valid and can be assigned to the new entry.
   * @throws SQLException if a database exception occurs.
   */
  private boolean isValidEc(EnzymeCommissionNumber ec, ActionMessages errors, Connection con) throws SQLException {
    assert ec != null : "Parameter 'ec' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    assert con != null : "Parameter 'con' must not be null.";

    try {
      // Check if the EC already exists.
      if (EnzymeEntryMapper.ecExists(ec, con)) {
        errors.add("ec", new ActionMessage("errors.application.ec.exists", ec.toString()));
        return false;
      }

      // Check if the corresponding sub-subclass exists.
      EnzymeCommissionNumber subclassEc = EnzymeCommissionNumber.valueOf(ec.getEc1(), ec.getEc2());
      final boolean subSubclassExists = EnzymeEntryMapper.ecExists(subclassEc, con);
      if (!subSubclassExists) {
        errors.add("ec", new ActionMessage("errors.application.ec.nosubsubclass", ec.toString()));
        return subSubclassExists;
      }
    } catch (EcException e) {
      errors.add("ec", new ActionMessage("errors.application.ec.invalid"));
    }
    return true;
  }
}
