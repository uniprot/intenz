package uk.ac.ebi.intenz.tools.sib.sptr_enzyme;

import uk.ac.ebi.interfaces.enzyme.EnzymeEntry;
import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;
import uk.ac.ebi.interfaces.sptr.SPTRException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a simple implementation of the {@link EnzymeEntry EnzymeEntry} interface.
 *
 * @author Michael Darsow
 * @version preliminary - $Revision: 1.2 $ $Date: 2008/01/28 11:43:22 $
 */
public class EnzymeEntryImpl implements EnzymeEntry, Serializable {
  private int id;
  private String ec, commonName, cofactors, comment;
  private ArrayList synonyms, reactions, xrefs;
  private boolean isTransferredOrDeleted;

  public EnzymeEntryImpl() {
    id = 0;
    ec = "";
    commonName = "";
    cofactors = "";
    comment = "";
    synonyms = new ArrayList();
    reactions = new ArrayList();
    xrefs = new ArrayList();
    isTransferredOrDeleted = false;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setEC(String s) throws SPTRException, UnsupportedOperationException {
    ec = s;
  }

  public String getEC() throws SPTRException, UnsupportedOperationException {
    return ec;
  }

  public void setCommonName(String s) throws SPTRException, UnsupportedOperationException {
    commonName = s;
  }

  public String getCommonName() throws SPTRException, UnsupportedOperationException {
    return commonName;
  }

  public void addSynonym(String s) throws SPTRException, UnsupportedOperationException {
    synonyms.add(s);
  }

  public boolean removeSynonym(String s) throws SPTRException, UnsupportedOperationException {
    return synonyms.remove(s);
  }

  public String[] getSynonyms() throws SPTRException, UnsupportedOperationException {
    String[] synonymsArray = new String[synonyms.size()];
    for (int iii = 0; iii < synonyms.size(); iii++) {
      synonymsArray[iii] = (String) synonyms.get(iii);
    }
    return synonymsArray;
  }

  public void setSynonyms(String[] synonyms) throws SPTRException, UnsupportedOperationException {
    for (int iii = 0; iii < synonyms.length; iii++) {
      addSynonym(synonyms[iii]);
    }
  }

  public void addReaction(String s) throws SPTRException, UnsupportedOperationException {
    reactions.add(s);
  }

  public boolean removeReaction(String s) throws SPTRException, UnsupportedOperationException {
    return reactions.remove(s);
  }

  public String[] getReactions() throws SPTRException, UnsupportedOperationException {
    String[] reactionsArray = new String[reactions.size()];
    for (int iii = 0; iii < reactions.size(); iii++) {
      reactionsArray[iii] = (String) reactions.get(iii);
    }
    return reactionsArray;
  }

  public void setReactions(String[] reactions) throws SPTRException, UnsupportedOperationException {
    for (int iii = 0; iii < reactions.length; iii++) {
      addReaction(reactions[iii]);
    }
  }

  public void setCofactors(String s) throws SPTRException, UnsupportedOperationException {
    cofactors = s;
  }

  public String getCofactors() throws SPTRException, UnsupportedOperationException {
    return cofactors;
  }

  public void setComment(String s) throws SPTRException, UnsupportedOperationException {
    comment = s;
  }

  public String getComment() throws SPTRException, UnsupportedOperationException {
    return comment;
  }

  public void addCrossReference(SPTRCrossReference sptrCrossReference) throws SPTRException, UnsupportedOperationException {
    xrefs.add(sptrCrossReference);
  }

  public void addCrossReferences(SPTRCrossReference[] sptrCrossReference) throws SPTRException, UnsupportedOperationException {
    for (int iii = 0; iii < sptrCrossReference.length; iii++) {
      addCrossReference(sptrCrossReference[iii]);
    }
  }

  public boolean removeCrossReference(SPTRCrossReference sptrCrossReference) throws SPTRException, UnsupportedOperationException {
    return xrefs.remove(sptrCrossReference);
  }

  public SPTRCrossReference[] getCrossReferences() throws SPTRException, UnsupportedOperationException {
    SPTRCrossReference[] xrefsArray = new SPTRCrossReference[this.xrefs.size()];
    for (int iii = 0; iii < this.xrefs.size(); iii++) {
      xrefsArray[iii] = (SPTRCrossReference) this.xrefs.get(iii);
    }
    return xrefsArray;
  }

  public void setCrossReferences(SPTRCrossReference[] xrefs) throws SPTRException, UnsupportedOperationException {
    for (int iii = 0; iii < xrefs.length; iii++) {
      addCrossReference(xrefs[iii]);
    }
  }

  public boolean isTransferredOrDeleted() {
    return isTransferredOrDeleted;
  }

  public void setTransferredOrDeleted(boolean transferredOrDeleted) {
    isTransferredOrDeleted = transferredOrDeleted;
  }
}
