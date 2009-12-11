package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

/**
 * This ActionForm only manages the <code>q</code> (query) parameter.
 * <p/>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class SearchForm extends ValidatorForm {
  private String q;

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

}
