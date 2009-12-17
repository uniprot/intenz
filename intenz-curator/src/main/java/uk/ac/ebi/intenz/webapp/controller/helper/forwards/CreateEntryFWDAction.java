package uk.ac.ebi.intenz.webapp.controller.helper.forwards;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class CreateEntryFWDAction extends ForwardAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
	  if (request.getParameter("amend") == null){
		    EnzymeDTO enzymeDTO = new EnzymeDTO();
		    request.getSession().setAttribute("enzymeDTO", enzymeDTO);
	  }
    request.setAttribute("title", "Create entry - IntEnz Curator Application");
    return super.execute(mapping, form, request, response);
  }
}
