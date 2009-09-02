package uk.ac.ebi.intenz.webapp.controller.helper.forwards;

import org.apache.log4j.Logger;
import org.apache.struts.actions.ForwardAction;
import org.apache.struts.action.*;
import org.apache.struts.taglib.html.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;

import java.util.Map;
import java.sql.Connection;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class ModifyEnzymeFWDAction extends ForwardAction {

  private static final Logger LOGGER = Logger.getLogger(ModifyEnzymeFWDAction.class);
  private final static String SEARCH_BY_ID_ACTION_FWD = "searchId";
  private final static String ERROR_FWD = "error";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
    ActionMessages errors = new ActionMessages();
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
        path.append("&amp;view=INTENZ&reload=true&id=");
      } else {
        path.append("?view=INTENZ&reload=true&id=");
      }
      path.append(enzymeId);
      request.setAttribute("title", "Reload");
      return new ActionForward(path.toString());
    }

    // Check if a clone already exists.
    if (enzymeDTO.getStatusCode().equals(EnzymeStatusConstant.APPROVED.getCode())) {
      EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
      Connection con = (Connection) request.getSession().getAttribute("connection");
      if (enzymeEntryMapper.cloneExists(enzymeId, con)) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.clone.exists", enzymeDTO.getEc()));
        saveErrors(request, errors);
        request.setAttribute("title", "Error - IntEnz Curator Application");
        return mapping.findForward(ERROR_FWD);
      }
    }

    ControlFlowToken.setToken(request, enzymeId); // Set token.

    // Try to lock the entry.
    EntryLockSingleton els = (EntryLockSingleton) request.getSession().getServletContext().getAttribute("entryLock");
    if (!els.setLock("" + enzymeId, request.getUserPrincipal().getName())) {
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.entry.locked", enzymeDTO.getEc()));
      saveErrors(request, errors);
      return mapping.findForward(ERROR_FWD);
    }
    
    return super.execute(mapping, form, request, response);
  }
}
