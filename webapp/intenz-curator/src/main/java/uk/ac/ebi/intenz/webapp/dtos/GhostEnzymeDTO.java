package uk.ac.ebi.intenz.webapp.dtos;



/**
 *
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class GhostEnzymeDTO extends GhostEntityDTO {

  String enzymeId;

  String source;

  String status;

  String eventClass;

  String eventNote;

  boolean active;

  public GhostEnzymeDTO() {
    name = ""; // This is the only attribute which might be null.
  }

  // ------------------------------- GETTER & SETTER --------------------------------

  public String getEnzymeId() {
    return enzymeId;
  }

  public void setEnzymeId(String enzymeId) {
    this.enzymeId = enzymeId;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEventClass() {
    return eventClass;
  }

  public void setEventClass(String eventClass) {
    this.eventClass = eventClass;
  }

  public String getEventNote() {
    return eventNote;
  }

  public void setEventNote(String eventNote) {
    this.eventNote = eventNote;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
