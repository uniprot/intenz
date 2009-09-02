package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This ActionForm stores an {@link java.util.ArrayList} of {@link GhostEnzymeDTO} instances.
 * <p/>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class GhostEnzymeListDTO extends ValidatorForm {
  private List ghostEnzymeList;

  public List getGhostEnzymeList() {
    return ghostEnzymeList;
  }

  public void setGhostEnzymeList(List ghostEnzymeList) {
    this.ghostEnzymeList = ghostEnzymeList;
  }

}
