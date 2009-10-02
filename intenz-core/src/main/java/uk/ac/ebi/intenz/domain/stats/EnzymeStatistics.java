package uk.ac.ebi.intenz.domain.stats;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:13 $
 */
public class EnzymeStatistics {
  private int allEnzymes;
  private int approvedEnzymes;
  private int proposedEnzymes;
  private int suggestedEnzymes;
  private int approvedDeletedEnzymes;
  private int proposedDeletedEnzymes;
  private int suggestedDeletedEnzymes;
  private int classes;
  private int subclasses;
  private int subsubclasses;

  public EnzymeStatistics() {
    allEnzymes = 0;
    approvedEnzymes = 0;
    proposedEnzymes = 0;
    suggestedEnzymes = 0;
    approvedDeletedEnzymes = 0;
    proposedDeletedEnzymes = 0;
    suggestedDeletedEnzymes = 0;
    classes = 0;
    subclasses = 0;
    subsubclasses = 0;
  }

  public int getAllEnzymes() {
    return allEnzymes;
  }

  public void setAllEnzymes(int allEnzymes) {
    this.allEnzymes = allEnzymes;
  }

  public int getApprovedEnzymes() {
    return approvedEnzymes;
  }

  public void setApprovedEnzymes(int approvedEnzymes) {
    this.approvedEnzymes = approvedEnzymes;
  }

  public int getProposedEnzymes() {
    return proposedEnzymes;
  }

  public void setProposedEnzymes(int proposedEnzymes) {
    this.proposedEnzymes = proposedEnzymes;
  }

  public int getSuggestedEnzymes() {
    return suggestedEnzymes;
  }

  public void setSuggestedEnzymes(int suggestedEnzymes) {
    this.suggestedEnzymes = suggestedEnzymes;
  }

  public int getApprovedDeletedEnzymes() {
    return approvedDeletedEnzymes;
  }

  public void setApprovedDeletedEnzymes(int approvedDeletedEnzymes) {
    this.approvedDeletedEnzymes = approvedDeletedEnzymes;
  }

  public int getProposedDeletedEnzymes() {
    return proposedDeletedEnzymes;
  }

  public void setProposedDeletedEnzymes(int proposedDeletedEnzymes) {
    this.proposedDeletedEnzymes = proposedDeletedEnzymes;
  }

  public int getSuggestedDeletedEnzymes() {
    return suggestedDeletedEnzymes;
  }

  public void setSuggestedDeletedEnzymes(int suggestedDeletedEnzymes) {
    this.suggestedDeletedEnzymes = suggestedDeletedEnzymes;
  }

  public int getClasses() {
    return classes;
  }

  public void setClasses(int classes) {
    this.classes = classes;
  }

  public int getSubclasses() {
    return subclasses;
  }

  public void setSubclasses(int subclasses) {
    this.subclasses = subclasses;
  }

  public int getSubsubclasses() {
    return subsubclasses;
  }

  public void setSubsubclasses(int subsubclasses) {
    this.subsubclasses = subsubclasses;
  }
}
