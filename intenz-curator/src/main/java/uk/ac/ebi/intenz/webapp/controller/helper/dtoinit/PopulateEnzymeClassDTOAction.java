package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.webapp.dtos.*;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/02/22 13:53:19 $
 */
public class PopulateEnzymeClassDTOAction extends Action {
  private final static String CLASS_JSP_FWD = "enzyme_class";

  private static final Logger LOGGER =
	  Logger.getLogger(PopulateEnzymeClassDTOAction.class.getName());

    @Override
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateEnzymeClassDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");

    // TODO: provide means to choose the view and the corresponding encoding type.
    EncodingType encodingType = null;

    // Populate form.
    final EnzymeClass enzymeClass = (EnzymeClass) request.getAttribute("result");
    populateForm(enzymeClass, form, encoding, encodingType);

    // Save page title.
    request.setAttribute("title", "EC " + enzymeClass.getEc());

    return mapping.findForward(CLASS_JSP_FWD);
  }

  private void populateForm(EnzymeClass enzymeClass, ActionForm form, SpecialCharacters encoding, EncodingType encodingType) {
    LOGGER.debug("Populating EnzymeClassDTO ...");
    EnzymeClassDTO enzymeClassDTO = (EnzymeClassDTO) form;

    enzymeClassDTO.setEc(enzymeClass.getEc().toString());
    enzymeClassDTO.setName(encoding.xml2Display(enzymeClass.getName(), encodingType));
    enzymeClassDTO.setDescription((enzymeClass.getDescription().equals(""))?
        "<i>No description available.</i>" :
        encoding.xml2Display(IntEnzUtilities.linkMarkedEC(enzymeClass.getDescription(), false), encodingType));

    List subclasses = enzymeClass.getSubclasses();
    List ghostSubclasses = new ArrayList();
    for (int iii = 0; iii < subclasses.size(); iii++) {
      EnzymeSubclass enzymeSubclass = (EnzymeSubclass) subclasses.get(iii);
      GhostEntityDTO ghostEntityDTO = new GhostEntityDTO();
      ghostEntityDTO.setEc(enzymeSubclass.getEc().toString());
      ghostEntityDTO.setName(encoding.xml2Display(enzymeSubclass.getName(), encodingType));
      ghostSubclasses.add(ghostEntityDTO);
    }
    enzymeClassDTO.setSubclasses(ghostSubclasses);

    LOGGER.debug("... EnzymeClassDTO populated.");
  }

}
