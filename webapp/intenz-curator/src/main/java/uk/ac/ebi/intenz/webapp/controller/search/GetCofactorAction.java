package uk.ac.ebi.intenz.webapp.controller.search;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.biobabel.webapp.ForwardConstants;
import uk.ac.ebi.intenz.mapper.EnzymeCofactorMapper;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.util.IChebiHelper;
import uk.ac.ebi.rhea.webapp.SessionManager;

public class GetCofactorAction extends Action implements ForwardConstants {

    private final Logger LOGGER = Logger.getLogger(GetCofactorAction.class);
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String accession = request.getParameter("accession");
        EnzymeCofactorMapper cofactorMapper = null;
        IChebiHelper chebiHelper = null;
        try {
            Compound compound = null;
            if (id != null && !id.equals("0")){
                cofactorMapper = new EnzymeCofactorMapper();
                Connection con = (Connection)
                        request.getSession().getAttribute("connection");
                compound = cofactorMapper.getCompound(con, Long.parseLong(id));
            } else if (accession != null){
                chebiHelper = SessionManager.getChebiProdHelper(request);
                //      SessionManager.getChebiPubHelper(request);
                compound = chebiHelper.getCompoundById(accession);
            }
            request.setAttribute("compound", compound);
            return mapping.findForward(SUCCESS);
        } catch (Exception e) {
            LOGGER.error("Unable to retrieve compound " + accession, e);
            return mapping.findForward(ERROR);
        } finally {
            if (cofactorMapper != null){
                cofactorMapper.close();
            }
            if (chebiHelper != null){
                try {
                    chebiHelper.close();
                } catch (MapperException e) {
                    LOGGER.error("Unable to close ChEBI helper", e);
                }
            }
        }
    }

}
