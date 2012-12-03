package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class PopulatePreviewIntEnzEnzymeDTOAction extends Action {
  private final static String INTENZ_ENTRY_PREVIEW_JSP_FWD = "intenz_entry_preview";

  private static final Logger LOGGER =
	  Logger.getLogger(PopulatePreviewIntEnzEnzymeDTOAction.class.getName());

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulatePreviewIntEnzEnzymeDTOAction");
    request.setAttribute("title", "Preview entry " + ((EnzymeDTO) request.getSession().getAttribute("enzymeDTO")).getEc() + " - IntEnz Curator Application");
    return mapping.findForward(INTENZ_ENTRY_PREVIEW_JSP_FWD);
  }

}
