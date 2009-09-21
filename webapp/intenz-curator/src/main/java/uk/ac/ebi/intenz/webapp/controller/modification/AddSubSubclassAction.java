package uk.ac.ebi.intenz.webapp.controller.modification;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.AuditPackageMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeSubSubclassDTO;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class AddSubSubclassAction extends Action {

  private final static String SEARCH_EC_ACTION_FWD = "searchEc";

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    ActionMessages errors = new ActionMessages();
    EnzymeSubSubclassDTO subSubclassDTO = (EnzymeSubSubclassDTO) form;
    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(subSubclassDTO.getEc());

    // Check whether this EC is a sub-subclass EC.
    if (!ec.getType().equals(Type.SUBSUBCLASS)) {
      errors.add("ec", new ActionMessage("errors.form.subsubclass.invalidEc"));
      saveErrors(request, errors);
      return mapping.getInputForward();
    }

    Connection con = (Connection) request.getSession().getAttribute("connection");

    // Check whether this EC exists already.
    if (EnzymeEntryMapper.ecExists(ec, con)) {
      errors.add("ec", new ActionMessage("errors.application.ec.exists", ec.getEc1() + "." + ec.getEc2() + "." + ec.getEc3()));
      saveErrors(request, errors);
      return mapping.getInputForward();
    }

    // Check if the given EC is valid, that is the corresponding class and subclass exist.
    if (!validClassEc(ec, con)) {
      errors.add("ec", new ActionMessage("errors.form.subsubclass.noClass", ec.toString()));
      saveErrors(request, errors);
      return mapping.getInputForward();
    }
    if (!validSubclassEc(ec, con)) {
      errors.add("ec", new ActionMessage("errors.form.subsubclass.noSubclass", ec.toString()));
      saveErrors(request, errors);
      return mapping.getInputForward();
    }

    try {
        // Set the standard remark in the audit tables.
        AuditPackageMapper auditPackageMapper = new AuditPackageMapper();
        auditPackageMapper.setRemark(AuditPackageMapper.STANDARD_REMARK, con);

        // Insert into database.
        EnzymeSubSubclassMapper subSubclassMapper = new EnzymeSubSubclassMapper();
        subSubclassMapper.insertSubSubclass(ec, subSubclassDTO.getName(), subSubclassDTO.getDescription(), con);

        con.commit();
    } catch (SQLException e){
        con.rollback();
        throw e;
    }

    EcSearchForm ecSearchForm = new EcSearchForm();
    ecSearchForm.setEc(ec.toString());

    // Forward to 'searchEc' Action.
    request.setAttribute("ecSearchForm", ecSearchForm);
    return mapping.findForward(SEARCH_EC_ACTION_FWD);
  }

  /**
   * Checks whether the corresponding class already exists.
   *
   * @param ec  Sub-subclass EC number.
   * @param con The database connection.
   * @return <code>true</code> if the class exists.
   */
  private boolean validClassEc(EnzymeCommissionNumber ec, Connection con) throws EcException, SQLException {
    EnzymeCommissionNumber classEc = EnzymeCommissionNumber.valueOf(ec.getEc1());
    return EnzymeEntryMapper.ecExists(classEc, con);
  }

  /**
   * Checks whether the corresponding subclass already exists.
   *
   * @param ec  Sub-subclass EC number.
   * @param con The database connection.
   * @return <code>true</code> if the subclass exists.
   */
  private boolean validSubclassEc(EnzymeCommissionNumber ec, Connection con) throws EcException, SQLException {
    EnzymeCommissionNumber subclassEc = EnzymeCommissionNumber.valueOf(ec.getEc1(), ec.getEc2());
    return EnzymeEntryMapper.ecExists(subclassEc, con);
  }

}
