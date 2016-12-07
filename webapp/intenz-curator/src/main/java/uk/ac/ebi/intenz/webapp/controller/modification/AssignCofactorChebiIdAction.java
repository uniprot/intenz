package uk.ac.ebi.intenz.webapp.controller.modification;

import java.sql.Connection;

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
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.mapper.EnzymeCofactorMapper;
import uk.ac.ebi.rhea.domain.Compound;

public class AssignCofactorChebiIdAction extends Action
implements ForwardConstants {
	
    private final Logger LOGGER =
            Logger.getLogger(AssignCofactorChebiIdAction.class);
    
	private EnzymeCofactorMapper cofactorMapper = new EnzymeCofactorMapper();
	
	/**
	 * Substitutes a compound with another one in the cofactors table.
	 */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse res) throws Exception {
        String compoundId = req.getParameter("compoundId");
        String oldChebiId = req.getParameter("oldChebiId");
        String newChebiId = req.getParameter("newChebiId");
        String oldName = req.getParameter("oldName");
        String newName = req.getParameter("newName");
        Connection con = (Connection)
                req.getSession().getAttribute("connection");
        
        try {
            Long oldId = Long.valueOf(compoundId);
            Compound replaced = new Compound(oldId, oldName,
                    EnzymeSourceConstant.INTENZ.toString(), oldChebiId);
            Compound replacing = cofactorMapper.findByChebiId(newChebiId, con);
            if (replacing == null){
                // We need to insert it:
                replacing = new Compound(Compound.NO_ID_ASSIGNED, newName,
                        EnzymeSourceConstant.INTENZ.toString(), newChebiId);
                cofactorMapper.insertCompound(replacing, con);
            }
            LOGGER.info("Replacing cofactor " + oldName + "["
                    + oldChebiId + "] with " + newName + " [" + newChebiId + "]");
            cofactorMapper.update(replaced, replacing, con);
            LOGGER.info("Replacement successful");
            LOGGER.info("Deleting now unused cofactor " + oldName + "["
                    + oldChebiId + "]");
            cofactorMapper.deleteCompound(oldId, con);
            
            ActionMessages info = new ActionMessages();
            info.add("info", new ActionMessage("info.cofactor.global.change",
                    replaced.getName(), replaced.getAccession(),
                    replacing.getName(), replacing.getAccession()));
            addMessages(req, info);
            con.commit();
            return mapping.findForward(SUCCESS);
        } catch (Exception e){
            con.rollback();
            throw e;
        }
    }
}
