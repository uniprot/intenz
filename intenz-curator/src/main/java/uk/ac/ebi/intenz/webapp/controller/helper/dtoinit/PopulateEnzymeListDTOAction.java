package uk.ac.ebi.intenz.webapp.controller.helper.dtoinit;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.TokenProcessor;
import org.apache.struts.Globals;
import org.apache.struts.taglib.html.Constants;

import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.enzyme.*;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.webapp.dtos.*;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class PopulateEnzymeListDTOAction extends Action {
  private final static String ALL_ENTRIES_JSP_FWD = "entries";

  private static final Logger LOGGER =
	  Logger.getLogger(PopulateEnzymeListDTOAction.class.getName());

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    LOGGER.debug("PopulateEnzymeListDTOAction");

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");

    // TODO: provide means to choose the view and the corresponding encoding type.
    EncodingType encodingType = null;

    // Populate form.
    populateForm((List) request.getAttribute("result"), form, encoding, encodingType);

    return mapping.findForward(ALL_ENTRIES_JSP_FWD);
  }

  private void populateForm(List result, ActionForm form, SpecialCharacters encoding, EncodingType encodingType) {
    LOGGER.debug("Populating EnzymeListDTO ...");
    EnzymeListDTO enzymeListDTO = (EnzymeListDTO) form;
    List ghostEntryList = new ArrayList();
    for (int iii = 0; iii < result.size(); iii++) {
      EnzymeEntry enzymeEntry = (EnzymeEntry) result.get(iii);
      GhostEnzymeDTO ghostEnzymeDTO = new GhostEnzymeDTO();
      ghostEnzymeDTO.setEnzymeId(enzymeEntry.getId().toString());
      ghostEnzymeDTO.setEc(enzymeEntry.getEc().toString());
      List commonNames = enzymeEntry.getCommonNames();
      for (int jjj = 0; jjj < commonNames.size(); jjj++) {
        EnzymeName commonName = (EnzymeName) commonNames.get(jjj);
        if(commonName.getView().isInIntEnzView()) {
          ghostEnzymeDTO.setName(encoding.xml2Display(commonName.getName(), encodingType));
          break;
        }
      }
      ghostEnzymeDTO.setSource(enzymeEntry.getSource().toString());
      ghostEnzymeDTO.setStatus(enzymeEntry.getStatus().toString());
      ghostEnzymeDTO.setActive(enzymeEntry.isActive());
      ghostEnzymeDTO.setEventClass(getEventHistory(enzymeEntry));
      ghostEnzymeDTO.setEventNote(IntEnzUtilities.linkMarkedEC(enzymeEntry.getHistory().getLatestRelevantHistoryEventOfRoot().getNote(), true));
      ghostEntryList.add(ghostEnzymeDTO);
    }
    enzymeListDTO.setResult(ghostEntryList);
    LOGGER.debug("... EnzymeListDTO populated.");
  }

   private String getEventHistory(EnzymeEntry enzymeEntry){
      return enzymeEntry.getHistory().getLatestRelevantHistoryEventOfRoot().toString();
//      if( history.isDeletedRootNode()) return history.getLatestHistoryEventOfRoot().getEventClass().toString();
//      if( history.isTransferredRootNode()) return history.getLatestHistoryEventOfRoot().getEventClass().toString();
//      return "";
   }

}
