package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

/**
 * This ActionForm only manages the <code>ec</code> parameter.
 * <p/>
 * The Validator framework checks if the EC number is valid.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EcSearchForm extends ValidatorForm {
  private String ec;

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = super.validate(mapping, request); // Use EcValidator.
    if (errors == null) errors = new ActionErrors();
    if (errors.isEmpty()) return null;
    return errors;
  }

  public String getEc() {
    return ec;
  }

  public void setEc(String ec) {
    this.ec = ec;
  }

}
