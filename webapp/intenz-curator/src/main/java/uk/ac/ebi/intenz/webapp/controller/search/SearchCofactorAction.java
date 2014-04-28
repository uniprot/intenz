package uk.ac.ebi.intenz.webapp.controller.search;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.biobabel.webapp.ForwardConstants;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.util.IChebiHelper;
import uk.ac.ebi.rhea.webapp.SessionManager;

public class SearchCofactorAction extends Action implements ForwardConstants {

    private final Logger LOGGER = Logger.getLogger(SearchCofactorAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String q = request.getParameter("q");
        Collection<Compound> compounds = null;
        ActionForward fwd = mapping.findForward(SUCCESS);
        ActionMessages errors = null;
        if (q == null){
            errors = new ActionMessages();
            errors.add("compoundSearchError",
                    new ActionMessage("errors.application.query.empty"));
        } else {
            IChebiHelper chebiHelper = null;
            try {
                chebiHelper = SessionManager.getChebiProdHelper(request);
                //      SessionManager.getChebiPubHelper(request);
                if (q.toUpperCase().startsWith("CHEBI:")){
                    Compound compound = chebiHelper.getCompoundById(q);
                    if (compound != null){
                        compounds = Collections.singleton(compound);
                    }
                } else {
                    compounds = chebiHelper.getCompoundsByName(q);
                }
                request.setAttribute("compoundSearchResults", compounds);
            } catch (Exception e){
                errors = new ActionMessages();
                errors.add("compoundSearchError",
                        new ActionMessage("errors.detail", e.getMessage()));
                LOGGER.error("Unable to search cofactors", e);
            } finally {
                if (chebiHelper != null){
                    try {
                        chebiHelper.close();
                    } catch (MapperException e) {
                        LOGGER.error("Unable to close ChEBI helper", e);
                    }
                }
            }
        }
        if (errors != null){
            addErrors(request, errors);
            fwd = mapping.findForward(ERROR);
        }
        return fwd;
    }

}
