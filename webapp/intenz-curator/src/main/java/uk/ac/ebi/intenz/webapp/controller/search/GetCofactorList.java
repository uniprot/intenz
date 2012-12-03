package uk.ac.ebi.intenz.webapp.controller.search;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.ac.ebi.intenz.mapper.EnzymeCofactorMapper;
import uk.ac.ebi.rhea.domain.Compound;

/**
 * Action to retrieve a list of compounds acting as cofactors in the database.
 * @author rafalcan
 *
 */
public class GetCofactorList extends Action {

	private static final String COFACTOR_LIST = "cofactorList";
	
	protected EnzymeCofactorMapper cofactorMapper = new EnzymeCofactorMapper();
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	    Connection con =
	    	(Connection) request.getSession().getAttribute("connection");
	    Map<Compound, Map<Long, String>> results = cofactorMapper.findAll(con);
	    // Group by EC number:
	    Map<Compound, Set<String>> filteredResults =
	    	new HashMap<Compound, Set<String>>();
	    for (Compound c : results.keySet()){
	    	filteredResults.put(c,
	    			new TreeSet<String>(results.get(c).values()));
	    }
	    request.setAttribute("results", filteredResults.entrySet());
        request.setAttribute("title", "IntEnz cofactors list");
	    return mapping.findForward(COFACTOR_LIST);
	}

}
