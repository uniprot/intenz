package uk.ac.ebi.intenz.webapp.controller.search;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;

public class SearchPreliminaryAction extends Action {

	private static final String PRELIMINARY_ECS_JSP_FWD = "preliminary_ecs";

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	    Connection con = (Connection) request.getSession().getAttribute("connection");
	    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
	    List<GhostEnzymeDTO> preliminaryEcs = new ArrayList<GhostEnzymeDTO>();
	    for (EnzymeEntry enzyme : enzymeEntryMapper.findByStatus(con, Status.PRELIMINARY)){
	    	GhostEnzymeDTO ghost = new GhostEnzymeDTO();
	    	ghost.setEnzymeId(enzyme.getId().toString());
	    	ghost.setEc(enzyme.getEc().toString());
	    	ghost.setName(SpecialCharacters.getInstance(null)
	    			.xml2Display(enzyme.getCommonName().getName()));
	    	ghost.setSource(enzyme.getSource().toString());
	    	ghost.setStatus(enzyme.getStatus().toString());
	    	// History...
	        final HistoryEvent latestHistoryEventOfRoot =
	        	enzyme.getHistory().getLatestHistoryEventOfRoot();
	        ghost.setEventClass(latestHistoryEventOfRoot.getEventClass().toString());
	        if (latestHistoryEventOfRoot.getEventClass() == EventConstant.TRANSFER &&
	        		!enzyme.getHistory().isTransferredRootNode()){
	        	ghost.setEventClass(EventConstant.CREATION.toString());
	        }
	        ghost.setEventNote(IntEnzUtilities.linkMarkedEC(
	        		enzyme.getHistory().getLatestHistoryEventOfRoot().getNote(), true));
	        preliminaryEcs.add(ghost);
	    }
	    request.setAttribute("preliminaryEcs", preliminaryEcs);
	    request.setAttribute("title", "Preliminary EC numbers - IntEnz Curator Application");
	    return mapping.findForward(PRELIMINARY_ECS_JSP_FWD);
	}

}
