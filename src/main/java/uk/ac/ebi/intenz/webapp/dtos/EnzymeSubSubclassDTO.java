package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeSubSubclassDTO extends GhostEntityDTO {

  private String description;
  private List entries;
  private String className;
  private String classEc;
  private String subclassName;
  private String subclassEc;

  /**
   * The Validator framework is used to check form parameters.
   *
   * @param mapping the Action mapping
   * @param request
   * @return
   */
  public ActionErrors validate(ActionMapping mapping, ServletRequest request) {
    ActionErrors errors = super.validate(mapping, request); // use Validator framework
    if (errors == null) errors = new ActionErrors();
    if (errors.isEmpty()) return null;
    return errors;
  }


  // ------------------------------- GETTER & SETTER --------------------------------

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List getEntries() {
    return entries;
  }

  public void setEntries(List entries) {
    this.entries = entries;
  }

  public GhostEnzymeDTO getRowAt(int index) {
    return (GhostEnzymeDTO) entries.get(index);
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getSubclassName() {
    return subclassName;
  }

  public void setSubclassName(String subclassName) {
    this.subclassName = subclassName;
  }

  public String getClassEc() {
    return classEc;
  }

  public void setClassEc(String classEc) {
    this.classEc = classEc;
  }

  public String getSubclassEc() {
    return subclassEc;
  }

  public void setSubclassEc(String subclassEc) {
    this.subclassEc = subclassEc;
  }
}
