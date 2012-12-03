package uk.ac.ebi.intenz.webapp.controller.helper.forwards;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class AddSubSubclassFWDAction extends ForwardAction {
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    request.setAttribute("title", "Add Sub-Subclass - IntEnz Curator Application");
    return super.execute(mapping, form, request, response);
  }
}
