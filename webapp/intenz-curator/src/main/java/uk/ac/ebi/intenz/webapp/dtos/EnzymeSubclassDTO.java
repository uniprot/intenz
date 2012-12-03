package uk.ac.ebi.intenz.webapp.dtos;

import java.util.List;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeSubclassDTO extends GhostEntityDTO {

  private String description;
  private List subSubclasses;
  private String classEc;
  private String className;


  // ------------------------------- GETTER & SETTER --------------------------------

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List getSubSubclasses() {
    return subSubclasses;
  }

  public void setSubSubclasses(List subSubclasses) {
    this.subSubclasses = subSubclasses;
  }

  public GhostEntityDTO getRowAt(int index) {
    return (GhostEntityDTO) subSubclasses.get(index);
  }

  public String getClassEc() {
    return classEc;
  }

  public void setClassEc(String classEc) {
    this.classEc = classEc;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
