package uk.ac.ebi.intenz.webapp.utilities;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;
import org.apache.struts.Globals;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class IntEnzExceptionHandler extends ExceptionHandler {

  private static final Logger LOGGER = Logger.getLogger(IntEnzExceptionHandler.class);

  public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
                               HttpServletRequest request, HttpServletResponse response) throws ServletException {

    // Log all excpetions.
    if (ex instanceof SQLException) LOGGER.error("DATABASE ERROR: \n", ex);
    if (ex instanceof NullPointerException) LOGGER.error("FATAL ERROR: \n", ex);
    if (ex instanceof IllegalArgumentException) LOGGER.error("FATAL ERROR: \n", ex);
    if (ex instanceof NumberFormatException) LOGGER.error("DOMAIN ERROR: \n", ex);
    if (ex instanceof DomainException) LOGGER.error("DOMAIN ERROR: \n", ex);
    if (ex instanceof SPTRException) LOGGER.error("DOMAIN ERROR: \n", ex);
    if (ex instanceof EcException) {
      EcException ecException = (EcException) ex;
      LOGGER.error("DOMAIN ERROR (EC): "+ecException.getMessage(), ecException);
    }

    ActionForward forward = null;
    ActionMessage error = null;
    String property = null;

    // Build the forward from the exception mapping if it exists
    // or from the form input
    if (ae.getPath() != null) {
      forward = new ActionForward(ae.getPath());
    } else {
      forward = mapping.getInputForward();
    }

    // Figure out the error
    boolean exceptionIdentified = false;
    if (ex instanceof ModuleException) {
      error = ((ModuleException) ex).getActionMessage();
      property = ((ModuleException) ex).getProperty();
      exceptionIdentified = true;
    }
    if (ex instanceof EcException) {
      EcException ecException = (EcException) ex;
      error = new ActionMessage(ae.getKey(), ecException.getMessage());
      property = "ec";
      exceptionIdentified = true;
    }
    if(!exceptionIdentified) {
      error = new ActionMessage(ae.getKey(), ex.getMessage());
      property = error.getKey();
    }

    // Store the exception
    request.setAttribute(Globals.EXCEPTION_KEY, ex);
    this.storeException(request, property, error, forward, ae.getScope());

    request.setAttribute("title", "Error - IntEnz Curator Application");
    return forward;
  }

}
