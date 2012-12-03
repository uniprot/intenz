package uk.ac.ebi.intenz.webapp.controller.modification;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.taglib.html.Constants;
import uk.ac.ebi.intenz.webapp.dtos.*;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class checks to see if the entry is still valid.
 * If its not it reloads it. 
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class CurationAction extends Action {

  private static final Logger LOGGER =
	  Logger.getLogger(CurationAction.class.getName());
  private final static String SEARCH_BY_ID_ACTION_FWD = "searchId";

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.info("CurationAction");
    ActionMessages errors = new ActionMessages();
//    EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
    EnzymeDTO enzymeDTO = (EnzymeDTO) form;
    if(enzymeDTO.getId() == null || enzymeDTO.getId().equals("")) return null; // new enzymes do not need token checking
    Long enzymeId = new Long(enzymeDTO.getId());

    // Check if the current entry is still valid.
    if (request.getParameter(Constants.TOKEN_KEY) != null && !ControlFlowToken.isValid(request)) {
      Map tokenHashtable = (Map) request.getSession().getAttribute("tokenHashtable");
      LOGGER.debug("Entry with ID " + enzymeId + " is being discarded ...");
      enzymeId = (Long) tokenHashtable.get(request.getParameter(Constants.TOKEN_KEY));
      LOGGER.debug("... entry with ID " + enzymeId + " will be loaded instead.");

      // Store a message to inform the user about the reload.
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.entry.reloaded.noec"));
      saveErrors(request, errors);

      // Set view where to forward to after the entry has been reloaded.
      ActionForward forward = mapping.findForward(SEARCH_BY_ID_ACTION_FWD);
      StringBuffer path = new StringBuffer(forward.getPath());
      boolean isQuery = (path.indexOf("?") > -1);
      if (isQuery) {
        path.append("&amp;view=" + mapping.getInputForward().getPath() + "&reload=true&id=");
      } else {
        path.append("?view=" + mapping.getInputForward().getPath() + "&reload=true&id=");
      }
      path.append(enzymeId);

      return new ActionForward(path.toString());
    }

    return null;
  }

  /**
   * Keeps the token as it is to ensure that the entry can be reloaded if necessary.
   *
   * @param request Used to transfer the token parameter to the token attribute.
   * @throws NullPointerException if <code>request</code> is <code>null</code>
   */
  protected void keepToken(HttpServletRequest request) {
    if (request == null) throw new NullPointerException("Parameter 'request' must not be null.");
    if (request.getParameter(Constants.TOKEN_KEY) != null) request.setAttribute(Constants.TOKEN_KEY, request.getParameter(Constants.TOKEN_KEY));
  }
}
