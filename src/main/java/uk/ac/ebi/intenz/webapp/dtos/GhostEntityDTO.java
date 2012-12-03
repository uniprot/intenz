package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class GhostEntityDTO extends ValidatorForm {

  String ec;
  String name;
  
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = super.validate(mapping, request); // use Validator framework
    if (errors == null) errors = new ActionErrors();
    if (errors.isEmpty()) return null;
    return errors;
  }

  
  // ------------------------------- GETTER & SETTER --------------------------------

  public String getEc() {
    return ec;
  }

  public void setEc(String ec) {
    this.ec = ec;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
