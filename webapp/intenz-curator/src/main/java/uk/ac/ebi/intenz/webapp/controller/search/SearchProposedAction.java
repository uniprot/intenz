package uk.ac.ebi.intenz.webapp.controller.search;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.Globals;
import org.apache.struts.taglib.html.Constants;

import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeListDTO;
import uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * This Action retreives all proposed entries and forwards to a page to display them.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class SearchProposedAction extends Action {

  private final static String PROPOSED_ENTRIES_JSP_FWD = "proposed_entries";

  private static final Logger LOGGER = Logger.getLogger(SearchProposedAction.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    Connection con = (Connection) request.getSession().getAttribute("connection");
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    GhostEnzymeListDTO ghostEnzymeListDTO = (GhostEnzymeListDTO) form;

    // Get special characters instance (set in the servlet handler's process method).
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");

    // Populate form.
    ghostEnzymeListDTO.setGhostEnzymeList(getEnzymeDTOInstances(enzymeEntryMapper.findProposedList(con), encoding, null));

    request.setAttribute("title", "Proposed Entries - IntEnz Curator Application");
    return mapping.findForward(PROPOSED_ENTRIES_JSP_FWD);
  }

  /**
   * Populates {@link uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO} instances and adds the to an
   * {@link java.util.ArrayList}.
   *
   * @param proposedList List of {@link uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry} instances.
   * @param encoding An instance of {@link uk.ac.ebi.xchars.SpecialCharacters}.
   * @param encodingType The {@link uk.ac.ebi.xchars.domain.EncodingType} to be used to create the display string.
   * @return an {@link java.util.ArrayList} of {@link uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO} instances.  
   */
  private List getEnzymeDTOInstances(List proposedList, SpecialCharacters encoding, EncodingType encodingType) {
    assert proposedList != null : "Parameter 'proposedList' must not be null.";
    assert encoding != null : "Parameter 'encoding' must not be null.";

    List ghostEntryList = new ArrayList();
    for (int iii = 0; iii < proposedList.size(); iii++) {
      EnzymeEntry enzymeEntry = (EnzymeEntry) proposedList.get(iii);
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
      ghostEntryList.add(ghostEnzymeDTO);
    }
    return ghostEntryList;
  }
}
