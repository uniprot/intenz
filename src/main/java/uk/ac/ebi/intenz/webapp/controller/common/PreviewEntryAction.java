package uk.ac.ebi.intenz.webapp.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.taglib.html.Constants;

import uk.ac.ebi.intenz.webapp.controller.modification.CurationAction;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:13 $
 */
public class PreviewEntryAction extends CurationAction {

//  private static final Logger LOGGER = Logger.getLogger(PreviewEntryAction.class);
  private static final String INTENZ_ENTRY_PREVIEW_JSP_FWD = "intenz_entry_preview";

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
     request.setAttribute("title", "Preview entry " + ((EnzymeDTO) request.getSession().getAttribute("enzymeDTO")).getEc() + " - IntEnz Curator Application");
     request.setAttribute(Constants.TOKEN_KEY, request.getParameter(Constants.TOKEN_KEY));
     // Get rid of "/preview":
     String previewFrom = mapping.getPath().substring(8);
      // Put on the request the action to be taken afterwards:
      request.getSession().setAttribute("previewFrom", previewFrom.substring(0, 1).toLowerCase() + previewFrom.substring(1));
    return mapping.findForward(INTENZ_ENTRY_PREVIEW_JSP_FWD);
  }
}
