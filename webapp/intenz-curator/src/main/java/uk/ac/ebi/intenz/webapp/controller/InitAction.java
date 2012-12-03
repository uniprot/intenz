package uk.ac.ebi.intenz.webapp.controller;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;
import uk.ac.ebi.intenz.webapp.utilities.EntryLockListener;

/**
 * Created by IntelliJ IDEA.
 * User: rafalcan
 * Date: 06-Sep-2005
 * Time: 14:37:27
 * To change this template use File | Settings | File Templates.
 */
public class InitAction extends Action {
    private static final String INTENZ_INDEX = "index";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        initSessionLock(request);
        request.setAttribute("title", "Curator tool");
        return mapping.findForward(INTENZ_INDEX);

    }

    private void initSessionLock(HttpServletRequest request) {
        if (request.getSession().getAttribute("entryLockListener") == null) {
            request.getSession().setAttribute("entryLockListener",
                    new EntryLockListener(EntryLockSingleton.getInstance()));
        }
    }

}
