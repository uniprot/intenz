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
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;

/**
 *
 * @author rafalcan
 */
public class SearchPreliminaryCommand extends DatabaseCommand {

    @Override
    public void process() throws ServletException, IOException {
        List<EnzymeEntry> prelimEcs = findPrelimEcsList();
        if (prelimEcs.size() == 0) {
            request.setAttribute("message",
                    "There are no preliminary entries stored in the database at the moment.");
        }
        request.setAttribute("preliminaryEcs", prelimEcs);
        forward("/preliminaryEcs.jsp");
    }

    private List<EnzymeEntry> findPrelimEcsList() {
        ServletContext application = request.getSession().getServletContext();
        List<EnzymeEntry> prelimEcsList =
                (List<EnzymeEntry>) application.getAttribute("preliminaryEcs");

        if (prelimEcsList != null) {
            if (!closeCon()) return null;
        } else {
            EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
            try {
                prelimEcsList = enzymeEntryMapper.findPreliminaryEcsList(con);
                if (prelimEcsList == null) {
                    if (!closeCon()) return null;
                } else {
                    application.setAttribute("preliminaryEcs", prelimEcsList);
                }
            } catch (SQLException e) {
                IntEnzMessenger.sendError(this.getClass().toString(),
                        e.getMessage(),
                        (String) request.getSession().getAttribute("user"));
                request.setAttribute("message", e.getMessage());
                return null;
            } catch (DomainException e) {
                PropertyResourceBundle applicationProperties =
                        (PropertyResourceBundle) request.getSession()
                        .getServletContext().getAttribute("application_properties");
                IntEnzMessenger.sendError(this.getClass().toString(),
                        applicationProperties.getString(e.getMessageKey()),
                        (String) request.getSession().getAttribute("user"));
                request.setAttribute("message", e.getMessage());
                return null;
            } finally {
                if (!closeCon()) return null;
            }
        }
        return prelimEcsList;
    }

    /**
     * Closes the connection without doing anything else like forwarding
     * (see {@link DatabaseCommand#closeConnection}).
     * @return <code>true</code> if the connection closed successfuly,
     *      <code>false</code> otherwise.
     */
    private boolean closeCon() {
        try {
            con.close(); // The connection is not needed.
        } catch (SQLException e) {
            IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(), (String) request.getSession().getAttribute("user"));
            request.setAttribute("message", "The following database error occured:\n" + e.getMessage() + this.databaseErrorMessage);
            return false;
        }
        return true;
    }

}
