package uk.ac.ebi.intenz.webapp.dtos;

import java.util.List;

/**
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class EnzymeClassDTO extends GhostEntityDTO {

  private String description;
  private List subclasses;


  // ------------------------------- GETTER & SETTER --------------------------------

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List getSubclasses() {
    return subclasses;
  }

  public void setSubclasses(List subclasses) {
    this.subclasses = subclasses;
  }

  public GhostEntityDTO getRowAt(int index) {
    return (GhostEntityDTO) subclasses.get(index);
  }
}
