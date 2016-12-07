package uk.ac.ebi.intenz.webapp.controller.helper.forwards;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class TransferEntryFWDAction extends ModifyEnzymeFWDAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
    request.setAttribute("title", "Transfer entry " + enzymeDTO.getEc() + " - IntEnz Curator Application");
    return super.execute(mapping, form, request, response);
  }
}
