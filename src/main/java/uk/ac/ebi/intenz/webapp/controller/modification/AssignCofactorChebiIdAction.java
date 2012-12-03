package uk.ac.ebi.intenz.webapp.controller.modification;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.intenz.mapper.EnzymeCofactorMapper;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.webapp.controller.curation.AssignChebiIdAction;

public class AssignCofactorChebiIdAction extends AssignChebiIdAction {
	
	private EnzymeCofactorMapper cofactorMapper = new EnzymeCofactorMapper();
	
	/**
	 * Substitutes a compound with another one in the cofactors table.
	 */
	protected ActionForward doExecute(ActionMapping map, HttpServletRequest req,
            Compound replacedCompound, Compound replacingCompound) throws Exception {
	    Connection con = (Connection) req.getSession().getAttribute("connection");
		cofactorMapper.update(replacedCompound, replacingCompound, con);
		ActionMessages info = new ActionMessages();
		info.add("info", new ActionMessage("info.cofactor.global.change",
				replacedCompound.getName(), replacedCompound.getAccession(),
				replacingCompound.getName(), replacingCompound.getAccession()));
		addMessages(req, info);
		return map.findForward(SUCCESS);
	}
}
