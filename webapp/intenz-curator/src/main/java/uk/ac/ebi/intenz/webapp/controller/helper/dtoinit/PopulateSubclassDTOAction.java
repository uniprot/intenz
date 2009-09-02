package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeSubclassDTO;
import uk.ac.ebi.intenz.webapp.dtos.GhostEntityDTO;
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
public class PopulateSubclassDTOAction extends Action {
  private final static String SUBCLASS_JSP_FWD = "subclass";

  private static final Logger LOGGER = Logger.getLogger(PopulateSubclassDTOAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateSubclassDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");

    // TODO: provide means to choose the view and the corresponding encoding type.
    EncodingType encodingType = null;

    // Populate form.
    final EnzymeSubclass enzymeSubclass = (EnzymeSubclass) request.getAttribute("result");
    populateForm(enzymeSubclass, form, encoding, encodingType);

    // Save page title.
    request.setAttribute("title", "EC " + enzymeSubclass.getEc());

    return mapping.findForward(SUBCLASS_JSP_FWD);
  }

  private void populateForm(EnzymeSubclass enzymeSubclass, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType) {
    LOGGER.debug("Populating EnzymeSubclassDTO ...");
    EnzymeSubclassDTO enzymeSubclassDTO = (EnzymeSubclassDTO) form;

    enzymeSubclassDTO.setEc(enzymeSubclass.getEc().toString());
    enzymeSubclassDTO.setName(encoding.xml2Display(enzymeSubclass.getName(), encodingType));
    enzymeSubclassDTO.setDescription((enzymeSubclass.getDescription().equals(""))?
        "<i>No description available.</i>" :
        encoding.xml2Display(IntEnzUtilities.linkMarkedEC(enzymeSubclass.getDescription(), false), encodingType));
    enzymeSubclassDTO.setClassEc("" + enzymeSubclass.getEc().getEc1());
    enzymeSubclassDTO.setClassName(enzymeSubclass.getClassName());

    List subSubclasses = enzymeSubclass.getSubSubclasses();
    List ghostSubSubclasses = new ArrayList();
    for (int iii = 0; iii < subSubclasses.size(); iii++) {
      EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) subSubclasses.get(iii);
      GhostEntityDTO ghostEntityDTO = new GhostEntityDTO();
      ghostEntityDTO.setEc(enzymeSubSubclass.getEc().toString());
      ghostEntityDTO.setName(encoding.xml2Display(enzymeSubSubclass.getName(), encodingType));
      ghostSubSubclasses.add(ghostEntityDTO);
    }
    enzymeSubclassDTO.setSubSubclasses(ghostSubSubclasses);

    LOGGER.debug("... EnzymeSubclassDTO populated.");
  }

}
