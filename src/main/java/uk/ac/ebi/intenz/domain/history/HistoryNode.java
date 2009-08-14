package uk.ac.ebi.intenz.domain.history;

import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * This node represents an enzyme within the history graph of an enzyme.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class HistoryNode {
  private EnzymeEntry enzymeEntry;

  private List<HistoryEvent> edges;

  private String historyLine;

  private boolean isRoot;

  /**
   * Nothing special.
   */
  public HistoryNode() {
    this.enzymeEntry = null;
    this.edges = new ArrayList<HistoryEvent>();
    this.historyLine = "";
    this.isRoot = true;
  }

  /**
   * Checks all events of this node for <code>DELETION</code> events and returns the source node of this event.
   *
   * @return the source node of the deletion.
   */
  public HistoryNode checkForDeletion() {
    if (edges.size() > 0) {
      HistoryEvent event = edges.get(0);
      if (event.getEventClass().equals(EventConstant.DELETION)) return event.getBeforeNode();
    }
    return this;
  }

  public String toString() {
    StringBuffer historyNodeString = new StringBuffer();
    historyNodeString.append("ENZYME ID: ");
    historyNodeString.append(enzymeEntry.getId());
    historyNodeString.append("\nEDGES SIZE: ");
    historyNodeString.append(edges.size());
    historyNodeString.append("\nHISTORY LINE: ");
    historyNodeString.append(historyLine);
    historyNodeString.append(isRoot ? "\nIS ROOT NODE" : "\nIS NOT ROOT NODE");
    return historyNodeString.toString();
  }


  // --------------------  GETTER/SETTER -----------------------

  public EnzymeEntry getEnzymeEntry() {
    return enzymeEntry;
  }

  public void setEnzymeEntry(EnzymeEntry enzymeEntry) {
    this.enzymeEntry = enzymeEntry;
  }

  public List<HistoryEvent> getEdges() {
    return edges;
  }

  public void setEdges(List<HistoryEvent> edges) {
    this.edges = edges;
  }

  public String getHistoryLine() {
    return historyLine;
  }

  public void setHistoryLine(String historyLine) {
    this.historyLine = historyLine;
  }

  public boolean isRoot() {
    return isRoot;
  }

  public void setRoot(boolean root) {
    isRoot = root;
  }
}
