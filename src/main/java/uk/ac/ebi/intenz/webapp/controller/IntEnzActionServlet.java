package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
import uk.ac.ebi.rhea.mapper.IRheaReader;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbWriter;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;
import uk.ac.ebi.rhea.mapper.util.IChebiHelper;
import uk.ac.ebi.rhea.mapper.util.db.ChebiDbHelper;
import uk.ac.ebi.rhea.updater.ChebiUpdater;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * The IntEnz Action Servlet creates a <code>SpecialCharacters</code> instance and establishes a connection.
 *
 * @author Michael Darsow
 * @version $Revision: 1.7 $ $Date: 2008/11/17 17:14:10 $
 */
public class IntEnzActionServlet extends ActionServlet {

    private static final Logger LOGGER =
    	Logger.getLogger(IntEnzActionServlet.class.getName());

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
        // Create a Unit of Work for this each session.
        if (httpServletRequest.getSession().getAttribute("uow") == null) {
            httpServletRequest.getSession().setAttribute("uow", new UnitOfWork());
        }

        // Create a unique SpecialCharacters instance for each session.
        if (httpServletRequest.getSession().getAttribute("characters") == null) {
            SpecialCharacters specialCharacters = (SpecialCharacters) this.getServletContext().getAttribute("specialCharactersInstance");
            SpecialCharacters sc = SpecialCharacters.copy(specialCharacters);
            httpServletRequest.getSession().setAttribute("characters", sc);
            LOGGER.info("SpecialCharacters instance stored in session.");
        }

        if (httpServletRequest.getSession().getAttribute("user") == null) {
            httpServletRequest.getSession().setAttribute("user", httpServletRequest.getUserPrincipal().getName());
        }

        if (httpServletRequest.getSession().getAttribute("connection") == null) {
            LOGGER.info("Creating connection to the database ...");
            try {
                establishConnection(httpServletRequest);
                LOGGER.info("... connection established.");
            } catch (SQLException e) {
                LOGGER.fatal("Connection could not be established.", e);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
                saveErrors(httpServletRequest, errors);
                httpServletRequest.getRequestDispatcher("/pages/error.jsp").forward(httpServletRequest, httpServletResponse);
            } catch (ClassNotFoundException e) {
                LOGGER.fatal("Connection could not be established.", e);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
                saveErrors(httpServletRequest, errors);
                httpServletRequest.getRequestDispatcher("/pages/error.jsp").forward(httpServletRequest, httpServletResponse);
            }
        }
        RheaCompoundDbReader compoundReader =
        	(RheaCompoundDbReader) httpServletRequest.getSession().getAttribute("rheaCompoundReader");
        if (compoundReader == null) {
            compoundReader =
                    new RheaCompoundDbReader((Connection) httpServletRequest.getSession().getAttribute("connection"));
            httpServletRequest.getSession().setAttribute("rheaCompoundReader", compoundReader);
        }
        IRheaReader rheaReader = (IRheaReader) httpServletRequest.getSession().getAttribute("rheaReader");
        if (rheaReader == null) {
            rheaReader = new RheaDbReader(compoundReader);
            httpServletRequest.getSession().setAttribute("rheaReader", rheaReader);
        }
        
        Connection chebiProdCon = null, chebiPubCon = null;
        IChebiHelper chebiProdHelper = null, chebiPubHelper = null;
        if (httpServletRequest.getSession().getAttribute("chebiHelper") == null){
            try {
                chebiProdCon = establishChebiProductionConnection(httpServletRequest);
                chebiProdHelper = new ChebiDbHelper(chebiProdCon);
                chebiPubCon = establishChebiPublicConnection(httpServletRequest);
                chebiPubHelper = new ChebiDbHelper(chebiPubCon);
                httpServletRequest.getSession().setAttribute("chebiHelper", chebiProdHelper);
            } catch (SQLException e) {
                LOGGER.error("Unable to connect to ChEBI database", e);
                throw new ServletException(e);
            }
        }
        if (httpServletRequest.getSession().getAttribute("chebiUpdater") == null) {
            RheaCompoundDbWriter compoundWriter =
                    new RheaCompoundDbWriter((Connection) httpServletRequest.getSession().getAttribute("connection"));
            ChebiUpdater chebiUpdater = new ChebiUpdater(chebiProdHelper, chebiPubHelper,
                    compoundReader, compoundWriter, rheaReader, null);
            httpServletRequest.getSession().setAttribute("chebiUpdater", chebiUpdater);
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
     * @throws SQLException           if a database error occured.
     * @throws ClassNotFoundException if the driver class could not be found.
     */
    private Connection establishConnection(HttpServletRequest request)
            throws SQLException, IOException, ClassNotFoundException {
        Connection con = null;
        if (request.getSession().getAttribute("connection") != null) {
            con = (Connection) request.getSession().getAttribute("connection");
        } else { // Create new connection and store in the session.
            DatabaseInstance odbi = OracleDatabaseInstance.getInstance(
                this.getServletConfig().getServletContext().getInitParameter("dbConfig"));
            Properties props = new Properties();
            props.put("user", odbi.getUser());
            props.put("password", odbi.getPassword());
            props.put("v$session.osuser",
            		request.getUserPrincipal().getName() + "-webapp");
            Class.forName(odbi.getDriver());
            con = DriverManager.getConnection(odbi.getUrl(), props);
            con.setAutoCommit(false);
            request.getSession().setAttribute("connectionBindingListener", new ConnectionBindingListener(con));
            request.getSession().setAttribute("connection", con);
        }
        return con;
    }

    private Connection establishChebiProductionConnection(HttpServletRequest req) throws IOException {
        DatabaseInstance dbi = OracleDatabaseInstance.getInstance(getServletContext().getInitParameter("chebi.prod.db.config"));
        Connection con = dbi.getConnection();
        if (con != null)
            req.getSession().setAttribute("chebiProdConBindingListener", new ConnectionBindingListener(con));
        return con;
    }

    private Connection establishChebiPublicConnection(HttpServletRequest req) throws IOException {
        DatabaseInstance dbi = OracleDatabaseInstance.getInstance(getServletContext().getInitParameter("chebi.pub.db.config"));
        Connection con = dbi.getConnection();
        if (con != null)
           req.getSession().setAttribute("chebiPubConBindingListener", new ConnectionBindingListener(con));
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
