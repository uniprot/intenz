/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.PropertyResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

/**
 *
 * @author rafalcan
 */
public class SearchPreliminaryCommand extends DatabaseCommand {

    public static final Logger LOGGER = Logger.getLogger(SearchPreliminaryCommand.class);

    private final EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();

	@Override
    public void process() throws ServletException, IOException {
        List<EnzymeEntry> prelimEcs = findPrelimEcsList();
        if (prelimEcs == null || prelimEcs.isEmpty()) {
            request.setAttribute("message",
                    "There are no preliminary entries stored in the database at the moment.");
        }
        request.setAttribute("preliminaryEcs", prelimEcs);
        forward("/preliminaryEcs.jsp");
    }

    private List<EnzymeEntry> findPrelimEcsList() {
        ServletContext application = request.getSession().getServletContext();
        @SuppressWarnings("unchecked")
		List<EnzymeEntry> prelimEcsList =
                (List<EnzymeEntry>) application.getAttribute("preliminaryEcs");
        if (prelimEcsList == null) {
            try {
                prelimEcsList = enzymeEntryMapper.findPreliminaryEcsList(con);
                if (prelimEcsList != null) {
                    application.setAttribute("preliminaryEcs", prelimEcsList);
                }
            } catch (SQLException e) {
                LOGGER.error("Finding preliminary list", e);
                IntEnzMessenger.sendError(this.getClass().toString(),
                        e.getMessage(),
                        (String) request.getSession().getAttribute("user"));
                request.setAttribute("message", e.getMessage());
                return null;
            } catch (DomainException e) {
                LOGGER.error("Finding preliminary list", e);
                PropertyResourceBundle intenzMessages =
                        (PropertyResourceBundle) request.getSession()
                        .getServletContext().getAttribute("intenz.messages");
                IntEnzMessenger.sendError(this.getClass().toString(),
                        intenzMessages.getString(e.getMessageKey()),
                        (String) request.getSession().getAttribute("user"));
                request.setAttribute("message", e.getMessage());
                return null;
            }
        }
        return prelimEcsList;
    }

}
