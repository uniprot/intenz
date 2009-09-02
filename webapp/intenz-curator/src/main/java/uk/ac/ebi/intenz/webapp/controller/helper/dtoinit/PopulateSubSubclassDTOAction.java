package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeSubSubclassDTO;
import uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class PopulateSubSubclassDTOAction extends Action {

  private static final Logger LOGGER = Logger.getLogger(PopulateSubSubclassDTOAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateSubSubclassDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");

    // TODO: provide means to choose the view and the corresponding encoding type.
    EncodingType encodingType = null;

    // Populate form.
    final EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) request.getAttribute("result");
    populateForm(enzymeSubSubclass, form, encoding, encodingType);

    // Save page title.
    request.setAttribute("title", "EC " + enzymeSubSubclass.getEc());

    return mapping.findForward("" + request.getAttribute("forward"));
  }

  private void populateForm(EnzymeSubSubclass enzymeSubSubclass, ActionForm form, SpecialCharacters encoding,
                            EncodingType encodingType) {
    LOGGER.debug("Populating EnzymeSubSubclassDTO ...");
    EnzymeSubSubclassDTO enzymeSubSubclassDTO = (EnzymeSubSubclassDTO) form;

    enzymeSubSubclassDTO.setEc(enzymeSubSubclass.getEc().toString());
    enzymeSubSubclassDTO.setName(encoding.xml2Display(enzymeSubSubclass.getName(), encodingType));
    enzymeSubSubclassDTO.setDescription(encoding.xml2Display(enzymeSubSubclass.getDescription(), encodingType));
    enzymeSubSubclassDTO.setClassName(encoding.xml2Display(enzymeSubSubclass.getClassName(), encodingType));
    enzymeSubSubclassDTO.setClassEc("" + enzymeSubSubclass.getEc().getEc1());
    enzymeSubSubclassDTO.setSubclassName(encoding.xml2Display(enzymeSubSubclass.getSubclassName(), encodingType));
    enzymeSubSubclassDTO.setSubclassEc(enzymeSubSubclass.getEc().getEc1() + "." + enzymeSubSubclass.getEc().getEc2());

    List enzymeEntries = enzymeSubSubclass.getEntries();
    List ghostEntries = new ArrayList();
    for (int iii = 0; iii < enzymeEntries.size(); iii++) {
      EnzymeEntry enzymeEntry = (EnzymeEntry) enzymeEntries.get(iii);
      GhostEnzymeDTO ghostEnzymeDTO = new GhostEnzymeDTO();
      ghostEnzymeDTO.setEnzymeId(enzymeEntry.getId().toString());
      ghostEnzymeDTO.setEc(enzymeEntry.getEc().toString());
      List commonNames = enzymeEntry.getCommonNames();
      for (int jjj = 0; jjj < commonNames.size(); jjj++) {
        EnzymeName commonName = (EnzymeName) commonNames.get(jjj);
        if (commonName.getView().isInIntEnzView()) {
          ghostEnzymeDTO.setName(encoding.xml2Display(commonName.getName(), encodingType));
          break;
        }
      }
      ghostEnzymeDTO.setSource(enzymeEntry.getSource().toString());
      ghostEnzymeDTO.setStatus(enzymeEntry.getStatus().toString());
      final HistoryEvent latestHistoryEventOfRoot = enzymeEntry.getHistory().getLatestHistoryEventOfRoot();
      ghostEnzymeDTO.setEventClass(latestHistoryEventOfRoot.getEventClass().toString());
      // Check whether this node has been transferred (set event class to 'TRA') or
      // if it is the new enzyme in a transfer process (set event class NOT to 'TRA' but 'NEW').
      if (latestHistoryEventOfRoot.getEventClass() == EventConstant.TRANSFER &&
          !enzymeEntry.getHistory().isTransferredRootNode())
        ghostEnzymeDTO.setEventClass(EventConstant.CREATION.toString());
      ghostEnzymeDTO.setEventNote(IntEnzUtilities.linkMarkedEC(enzymeEntry.getHistory().getLatestHistoryEventOfRoot().getNote(), true));
      ghostEntries.add(ghostEnzymeDTO);
    }
    enzymeSubSubclassDTO.setEntries(ghostEntries);

    LOGGER.debug("... EnzymeSubSubclassDTO populated.");
  }

}
