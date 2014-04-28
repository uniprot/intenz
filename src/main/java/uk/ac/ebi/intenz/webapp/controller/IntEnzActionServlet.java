package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;

import uk.ac.ebi.biobabel.util.db.DatabaseInstance;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.biobabel.webapp.listeners.ConnectionBindingListener;
import uk.ac.ebi.intenz.webapp.utilities.UnitOfWork;
import uk.ac.ebi.rhea.domain.Qualifier;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * The IntEnz Action Servlet creates a <code>SpecialCharacters</code> instance and establishes a connection.
 *
 * @author Michael Darsow
 * @version $Revision: 1.7 $ $Date: 2008/11/17 17:14:10 $
 */
public class IntEnzActionServlet extends ActionServlet {

	private static final long serialVersionUID = -7398277166501014952L;

	private static final Logger LOGGER =
    	Logger.getLogger(IntEnzActionServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        getServletContext().setAttribute(
                "reactionQualifiersForVisibleSearchOptions",
                Qualifier.VISIBLE_SEARCH_OPTIONS);
    }

    /**
     * Creates a <code>SpecialCharacters</code> instance and establishes a connection.
     *
     * @param httpServletRequest  The request object.
     * @param httpServletResponse The response object.
     * @throws IOException      ...
     * @throws ServletException ...
     */
    @Override
    protected void process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException, ServletException {
    	if (httpServletRequest.getSession().getAttribute("user") == null){
    		// Create a Unit of Work for this each session.
            httpServletRequest.getSession().setAttribute("uow", new UnitOfWork());

            // Create a unique SpecialCharacters instance for each session.
            SpecialCharacters specialCharacters =
                    (SpecialCharacters) this.getServletContext().getAttribute("specialCharactersInstance");
            SpecialCharacters sc = SpecialCharacters.copy(specialCharacters);
            httpServletRequest.getSession().setAttribute("characters", sc);
            LOGGER.info("SpecialCharacters instance stored in session.");
            LOGGER.info("Creating connections to the databases...");
            try {
                establishConnection(httpServletRequest);
                establishChebiProductionConnection(httpServletRequest);
                LOGGER.info("... connections established.");
            } catch (SQLException e) {
                LOGGER.fatal("Connection could not be established.", e);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
                saveErrors(httpServletRequest, errors);
                httpServletRequest.getRequestDispatcher("/pages/error.jsp")
                        .forward(httpServletRequest, httpServletResponse);
            } catch (ClassNotFoundException e) {
                LOGGER.fatal("Connection could not be established.", e);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
                saveErrors(httpServletRequest, errors);
                httpServletRequest.getRequestDispatcher("/pages/error.jsp")
                        .forward(httpServletRequest, httpServletResponse);
            }
            httpServletRequest.getSession().setAttribute("user", httpServletRequest.getUserPrincipal().getName());
    	}
        super.process(httpServletRequest, httpServletResponse);
    }

    /**
     * Creates a database connection and stores it in the user's session.
     * <p/>
     * If a session is already existing no action will be taken to create a new one.
     *
     * @param request The request object.
     * @return The database connection.
     * @throws SQLException           if a database error occurred.
     * @throws ClassNotFoundException if the driver class could not be found.
     */
    private Connection establishConnection(HttpServletRequest request)
            throws SQLException, IOException, ClassNotFoundException {
        DatabaseInstance odbi = OracleDatabaseInstance.getInstance(
                this.getServletConfig().getServletContext()
                    .getInitParameter("intenz.db.config"));
        Connection con = odbi.getConnection();
        con.setAutoCommit(false);
        Statement stm = con.createStatement();
        stm.execute("{CALL auditpackage.setosuser('"
                + request.getUserPrincipal().getName() + " (webapp)" + "')}");
        request.getSession().setAttribute("connectionBindingListener", new ConnectionBindingListener(con));
        request.getSession().setAttribute("connection", con);
        request.getSession().setAttribute("rhea.connection", con);
        return con;
    }

    private Connection establishChebiProductionConnection(HttpServletRequest req) throws IOException {
        DatabaseInstance dbi = OracleDatabaseInstance
                .getInstance(getServletContext().getInitParameter("chebi.prod.db.config"));
        Connection con = dbi.getConnection();
        if (con != null){
        	req.getSession().setAttribute("chebi.connection.production", con);
            req.getSession().setAttribute("chebiProdConBindingListener", new ConnectionBindingListener(con));
        }
        return con;
    }

    /**
     * Keeps error messages for user feedback.
     *
     * @param request Used to store the error messages.
     * @param errors  The error messages.
     */
    protected void saveErrors(HttpServletRequest request, ActionMessages errors) {
        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        // Save the error messages we need
        request.setAttribute(Globals.ERROR_KEY, errors);
    }
}
