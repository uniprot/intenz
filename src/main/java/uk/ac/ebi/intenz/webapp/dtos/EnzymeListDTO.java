package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  Only used for clones when an enzyme consists of "two rows".
 * (Not used as a list in other DTO's)
 *
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeListDTO extends ActionForm {

  private List result;

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();


    return errors;
  }

  
  // ------------------------------- GETTER & SETTER --------------------------------

  public List getResult() {
    return result;
  }

  public void setResult(List result) {
    this.result = result;
  }

  public GhostEnzymeDTO getRowAt(int index) {
    return (GhostEnzymeDTO) result.get(index);
  }
}
