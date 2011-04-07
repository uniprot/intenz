package uk.ac.ebi.intenz.webapp.controller.search;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;

import uk.ac.ebi.intenz.domain.enzyme.*;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;
import uk.ac.ebi.intenz.webapp.dtos.EcSearchForm;
import uk.ac.ebi.intenz.webapp.utilities.ControlFlowToken;
import uk.ac.ebi.rhea.mapper.MapperException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/17 17:14:10 $
 */
public class SearchEcAction extends Action {

  private final static String INDEX_JSP_FWD = "index";
  private final static String ERROR_JSP_FWD = "error";
  private static final String SUB_SUBCLASS_FWD = "sub_subclass";

  private final static String POPULATE_INTENZ_ENZYME_DTO_ACTION = "populateIntEnzEnzymeDTO";
  private final static String POPULATE_ENZYME_LIST_DTO_ACTION = "populateEnzymeListDTO";
  private final static String POPULATE_CLASS_DTO_ACTION = "populateEnzymeClassDTO";
  private final static String POPULATE_SUB_SUBCLASS_DTO_ACTION = "populateSubSubclassDTO";
  private final static String POPULATE_SUBCLASS_DTO_ACTION = "populateSubclassDTO";

  private static final Logger LOGGER =
	  Logger.getLogger(SearchEcAction.class.getName());

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    EcSearchForm ecSearchForm = (EcSearchForm) form;
    EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ecSearchForm.getEc().trim());
    ActionMessages errors = new ActionMessages();
    String forward = findForward(ec, errors, request, (Connection) request.getSession().getAttribute("connection"));
    saveErrors(request, errors);
    return mapping.findForward(forward);
  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Tries to retrieve the demanded domain object(s) and returns the key whose
   * value contains the JSP to forward to.
   * <p/>
   * If the result is empty a message will be displayed otherwise the
   * corresponding data page of the requested object.
   * TODO: Split up this Action and use proper forms for the results.
   *
   * @param ec      The enzyme's EC to search for.
   * @param errors  To store errors if the result is empty.
   * @param request To store the retrieved object (either in request or session scope).
   * @return The forward constant (see class constants).
   * @throws SQLException    if database errors occurred.
   * @throws DomainException if any error related to domain information occurs.
   * @throws MapperException in case of problems retrieving reaction/cofactor info
   */
  private String findForward(EnzymeCommissionNumber ec, ActionMessages errors,
                             HttpServletRequest request, Connection con)
  throws SQLException, DomainException, MapperException {
    assert ec != null : "Parameter 'ec' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    assert request != null : "Parameter 'request' must not be null.";
    assert con != null : "Parameter 'con' must not be null.";

    switch (ec.getType()) {
      case CLASS:
        LOGGER.debug("Find class information for EC " + ec.toString());
        EnzymeClass enzymeClass = findEnzymeClass(ec, con);
        if (enzymeClass != null) {
          LOGGER.debug("Found enzyme class EC " + enzymeClass.getEc().toString() + ", '" + enzymeClass.getName() +
                       "'.");
          request.setAttribute("result", enzymeClass);
          request.setAttribute("title", "EC " + ec.toString() + " - IntEnz Curator Application");
          return POPULATE_CLASS_DTO_ACTION;
        }
        LOGGER.debug("No enzyme class found.");
        break;
      case SUBCLASS:
        LOGGER.debug("Find subclass information for EC " + ec.toString());
        EnzymeSubclass enzymeSubclass = findEnzymeSubclass(ec, con);
        if (enzymeSubclass != null) {
          LOGGER.debug("Found enzyme subclass EC " + enzymeSubclass.getEc().toString() + ", '" +
                       enzymeSubclass.getName() +
                       "'.");
          request.setAttribute("result", enzymeSubclass);
          request.setAttribute("title", "EC " + ec.toString() + " - IntEnz Curator Application");
          return POPULATE_SUBCLASS_DTO_ACTION;
        }
        LOGGER.debug("No enzyme subclass found.");
        break;
      case SUBSUBCLASS:
        LOGGER.debug("Find sub-subclass information for EC " + ec.toString());
        EnzymeSubSubclass enzymeSubSubclass = findEnzymeSubSubclass(ec, con);
        if (enzymeSubSubclass != null) {
          LOGGER.debug("Found enzyme sub-subclass EC " + enzymeSubSubclass.getEc().toString() + ", '" +
                       enzymeSubSubclass.getName() +
                       "'.");
          String forward = SUB_SUBCLASS_FWD;
          if (request.getParameter("fw") != null && !request.getParameter("fw").equals("")) forward = request.getParameter("fw");
          request.setAttribute("forward", forward);
          request.setAttribute("result", enzymeSubSubclass);
          request.setAttribute("title", "EC " + ec.toString() + " - IntEnz Curator Application");
          return POPULATE_SUB_SUBCLASS_DTO_ACTION;
        }
        LOGGER.debug("No enzyme sub-subclass found.");
        break;
      case ENZYME:
      case PRELIMINARY:
        LOGGER.debug("Find enzyme entries for EC " + ec.toString());
        List enzymeEntries = findEnzymeEntries(ec, con); // Loads ghost entry/ies only!
        if (enzymeEntries != null) {
          LOGGER.debug("Found " + enzymeEntries.size() + " entry/ies.");
          if (enzymeEntries.size() > 1) {
            request.setAttribute("result", enzymeEntries);
            request.setAttribute("title", "Result for EC " + ec.toString() + " - IntEnz Curator Application");
            return POPULATE_ENZYME_LIST_DTO_ACTION;
          } else {
            if (enzymeEntries.size() == 1) {
              Long id = ((EnzymeEntry) enzymeEntries.get(0)).getId();
              EnzymeEntry enzymeEntry = findEnzymeEntry(id, con); // Load full enzyme entry information.
              request.setAttribute("result", enzymeEntry);

              // Set token to ensure that the curator has a consistent view at the data
              // (even when using multiple browser windows).
              ControlFlowToken.setToken(request, enzymeEntry.getId()); // Set token.

              request.setAttribute("title", "EC " + ec.toString() + " - IntEnz Curator Application");
              return POPULATE_INTENZ_ENZYME_DTO_ACTION;
            } else {
              errors.add("ec", new ActionMessage("errors.application.id.nonexisting", ec.toString()));
              return ERROR_JSP_FWD;
            }
          }
        }
        LOGGER.debug("No entries found.");
    }

    errors.add("ec", new ActionMessage("errors.application.ec.nonexisting", ec.toString()));
    return INDEX_JSP_FWD;
  }

  /**
   * Calls the according <code>find()</code> method.
   *
   * @param ec Number of enzyme class to search for.
   * @return An <code>EnzymeClass</code> instance or <code>null</code>.
   * @throws SQLException    if a database error occurs.
   * @throws DomainException if any error related to domain information occurs.
   */
  private EnzymeClass findEnzymeClass(EnzymeCommissionNumber ec, Connection con) throws SQLException, DomainException {
    EnzymeClassMapper classMapper = new EnzymeClassMapper();
    return classMapper.find(Integer.toString(ec.getEc1()), con);
  }

  /**
   * Calls the according <code>find()</code> method.
   *
   * @param ec Number of enzyme class to search for.
   * @return An <code>EnzymeClass</code> instance or <code>null</code>.
   * @throws SQLException    if database errors occured.
   * @throws DomainException if any error related to domain information occurs.
   */
  private EnzymeSubclass findEnzymeSubclass(EnzymeCommissionNumber ec, Connection con) throws SQLException,
          DomainException {
    EnzymeSubclassMapper subclassMapper = new EnzymeSubclassMapper();
    return subclassMapper.find(Integer.toString(ec.getEc1()), Integer.toString(ec.getEc2()), con);
  }

  /**
   * Calls the according <code>find()</code> method.
   *
   * @param ec Number of enzyme class to search for.
   * @return An <code>EnzymeClass</code> instance or <code>null</code>.
   * @throws SQLException    if database errors occured.
   * @throws DomainException if any error related to domain information occurs.
   */
  private EnzymeSubSubclass findEnzymeSubSubclass(EnzymeCommissionNumber ec, Connection con) throws SQLException,
          DomainException {
    EnzymeSubSubclassMapper subSubclassMapper = new EnzymeSubSubclassMapper();
    return subSubclassMapper.find(ec.getEc1(), ec.getEc2(), ec.getEc3(), con);
  }

  /**
   * Gets the enzyme with the given ID.
   *
   * @param id The enzyme's ID
   * @return An enzyme entry or <code>null</code>.
   * @throws SQLException    if database errors occured.
   * @throws DomainException if any error related to domain information occurs.
   * @throws MapperException in case of problems retrieving reaction/cofactor info
   */
  private EnzymeEntry findEnzymeEntry(Long id, Connection con)
  throws SQLException, DomainException, MapperException {
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    return enzymeEntryMapper.findById(id, con);
  }


  /**
   * Calls the according <code>find()</code> method.
   * <p/>
   * This method will return all enzymes specified by the given EC number. This includes suggested and proposed entries.
   *
   * @param ec EC number of enzymes to search for.
   * @return A Vector of enzyme entries or <code>null</code>.
   * @throws SQLException    if database errors occured.
   * @throws DomainException if any error related to domain information occurs.
   */
  private List findEnzymeEntries(EnzymeCommissionNumber ec, Connection con) throws SQLException, DomainException {
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    return enzymeEntryMapper.findAllByEc(ec.getEc1(), ec.getEc2(), ec.getEc3(),
    		ec.getEc4(), ec.getType().equals(Type.PRELIMINARY), con);
  }

}
