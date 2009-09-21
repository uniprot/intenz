package uk.ac.ebi.intenz.webapp.controller.search;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.html.Constants;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class SearchIdAction extends Action {

  private final static String ERROR_JSP_FWD = "error";

  private final static String POPULATE_INTENZ_ENZYME_DTO_ACTION = "populateIntEnzEnzymeDTO";
  private final static String POPULATE_IUBMB_ENZYME_DTO_ACTION = "populateIubmbEnzymeDTO";
  private final static String POPULATE_SIB_ENZYME_DTO_ACTION = "populateSibEnzymeDTO";

  private static final Logger LOGGER =
	  Logger.getLogger(SearchIdAction.class.getName());

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    Long enzymeId = new Long(request.getParameter("id"));
    String view = request.getParameter("view");
    ActionMessages errors = new ActionMessages();

    // This Action will be used for reloading an expired entry. In this case a token check will be omitted.
    boolean reload = false;
    if (request.getParameter("reload") != null) reload = request.getParameter("reload").equals("") ? false : true;

    // Check if the current entry is still valid.
    // If not, reset the enzyme ID.
    if (!reload && request.getParameter(Constants.TOKEN_KEY) != null && !ControlFlowToken.isValid(request)) {
      Map tokenHashtable = (Map) request.getSession().getAttribute("tokenHashtable");
      LOGGER.debug("Entry with ID " + enzymeId + " is being discarded ...");
      enzymeId = (Long) tokenHashtable.get(request.getParameter(Constants.TOKEN_KEY));
      LOGGER.debug("... entry with ID " + enzymeId + " will be loaded instead.");
    }

    // Keep the errors created by the reload procedure.
    if (reload) {
      errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
    }

    Connection con = (Connection) request.getSession().getAttribute("connection");

    LOGGER.debug("Find enzyme by ID " + enzymeId);
    EnzymeEntry enzymeEntry = findEnzymeEntry(enzymeId, con);
    if (enzymeEntry != null) {
      LOGGER.debug("Found enzyme EC " + enzymeEntry.getEc().toString() + ", " +
                   enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName() + ".");
      request.setAttribute("result", enzymeEntry);

      // Set token to ensure that the curator has a consistent view at the data
      // (even when using multiple browser windows).
      ControlFlowToken.setToken(request, enzymeEntry.getId()); // Set token.

      if (view != null && view.equals(EnzymeViewConstant.SIB.toString())) {
        request.setAttribute("title", "EC " + enzymeEntry.getEc().toString() + " (ENZYME)- IntEnz Curator Application");
        return mapping.findForward(POPULATE_SIB_ENZYME_DTO_ACTION);
      }
      if (view != null && view.equals(EnzymeViewConstant.IUBMB.toString())) {
        request.setAttribute("title", "EC " + enzymeEntry.getEc().toString() +
                                      " (NC-IUBMB) - IntEnz Curator Application");
        return mapping.findForward(POPULATE_IUBMB_ENZYME_DTO_ACTION);
      }
      if (view != null && view.equals(EnzymeViewConstant.INTENZ.toString())) {
        request.setAttribute("title", "EC " + enzymeEntry.getEc().toString() + " - IntEnz Curator Application");
        return mapping.findForward(POPULATE_INTENZ_ENZYME_DTO_ACTION);
      }
      if (reload) {  // In case of a reload the 'view' parameter contains the name of the Action to forward to.
        LOGGER.debug("Forward due to reload.");
        if (view != null) {
          request.setAttribute("forwardPath", view);
          return mapping.findForward(POPULATE_INTENZ_ENZYME_DTO_ACTION);
        }
        // No view defined during the reload process.
        LOGGER.debug("No enzyme with ID " + enzymeId + " found.");
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.id.nonexisting", enzymeId));
        saveErrors(request, errors);
        return mapping.findForward(ERROR_JSP_FWD);
      }
      // Error
      LOGGER.debug("The given URL is missing parameters.");
      errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.url.invalid_view", view));
      saveErrors(request, errors);
      return mapping.findForward(ERROR_JSP_FWD);
    }

    // Error
    LOGGER.debug("No enzyme with ID " + enzymeId + " found.");
    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.application.id.nonexisting", enzymeId));
    saveErrors(request, errors);
    return mapping.findForward(ERROR_JSP_FWD);
  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Gets the enzyme with the given ID.
   *
   * @param id The enzyme's ID
   * @return An enzyme entry or <code>null</code>.
   * @throws java.sql.SQLException if database errors occured.
   * @throws uk.ac.ebi.intenz.domain.exceptions.DomainException
   *                               if any error related to domain information occurs.
   */
  private EnzymeEntry findEnzymeEntry(Long id, Connection con) throws SQLException, DomainException {
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    return enzymeEntryMapper.findById(id, con);
  }

}
