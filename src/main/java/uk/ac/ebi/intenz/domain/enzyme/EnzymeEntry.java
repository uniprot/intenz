package uk.ac.ebi.intenz.domain.enzyme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeNameException;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeReactionException;
import uk.ac.ebi.intenz.domain.exceptions.EnzymeReferenceException;
import uk.ac.ebi.intenz.domain.history.HistoryGraph;
import uk.ac.ebi.intenz.domain.history.HistoryNode;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Reaction;

/**
 * This class stores the information of an IntEnz enzyme.
 * <p/>
 * Such an enzyme includes information from three different sources:
 * <p/>
 * <ul>
 * <li>NC-IUBMB (the official data)</li>
 * <li>enzyme.dat (Amos' enzyme file)</li>
 * <li>BRENDA (enzyme database from the University of Cologne)</li>
 * </ul>
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2009/04/20 13:44:24 $
 */
public class EnzymeEntry {

  /**
   * The enzyme's unique ID.
   */
  private Long id;

  /**
   * If an enzyme gets approved it must have a valid EC number.
   */
  private EnzymeCommissionNumber ec;

  /**
   * An enzyme belongs to a class of enzymes.
   */
  private String className;

  /**
   * An enzyme belongs to a subclass of a class of enzymes.
   */
  private String subclassName;

  /**
   * The sub-subclass is the lowest hierarchical level to which an enzyme belongs.
   */
  private String subSubclassName;

  /**
   * The common name of an enzyme.
   */
  private List<EnzymeName> commonNames;

  /**
   * The systematic name of this enzyme.
   */
  private EnzymeName systematicName;

  /**
   * Synonyms will be ordered by their order number internally.
   */
  private List<EnzymeName> synonyms;

  /**
   * An enzyme may catalyse more than one reaction.
   * @deprecated use enzymaticReactions istead
   */
  private List<Reaction> reactions;

  /**
   * Map to reactions catalyzed by this enzyme.
   */
  private EnzymaticReactions enzymaticReactions;

  /**
   * List of all cofactors.
   */
  private Set<Object> cofactors;

  /**
   * Stores links of this enzyme.
   */
  private SortedSet<EnzymeLink> links;

  /**
   * Some enzymes contain comments.
   */
  private List<EnzymeComment> comments;

  /**
   * At least one reference must be present to describe the enzyme.
   */
  private List<Reference> references;

  /**
   * The note can be added by a curator but it will not be publicly visible.
   */
  private String note;

  /**
   * Stores the enzyme's history as a graph for all history events stored in the database (since 2002).
   */
  private HistoryGraph historyGraph;

  /**
   * The status of this enzyme (can be suggested, proposed or approved).
   */
  private Status status;

  /**
   * The source of the enzyme (can be IUBMB, SIB, BRENDA or INTENZ).
   */
  private EnzymeSourceConstant source;

  /**
   * A flag to indicate whether this enzyme is isActive (not transferred or deleted) or not.
   */
  private boolean isActive;

  /**
   * Has this enzyme been lazy-loaded or does it contain the whole set of information?
   */
  private boolean isGhost;

  /**
   * The default source is <code>INTENZ</code>.
   * Every new instance is active by default.
   */
  @SuppressWarnings("unchecked")
  public EnzymeEntry() {
    id = new Long(-1);
    ec = EnzymeCommissionNumber.UNDEF;
    className = "";
    subclassName = "";
    subSubclassName = "";
    commonNames = new ArrayList<EnzymeName>();
    systematicName = EnzymeName.UNDEF;
    reactions = new ArrayList<Reaction>();
    cofactors = new OperatorSet();
    synonyms = new ArrayList<EnzymeName>();
    links = new TreeSet<EnzymeLink>();
    comments = new ArrayList<EnzymeComment>();
    references = new ArrayList<Reference>();
    note = "";
    HistoryNode currentNode = new HistoryNode();
    currentNode.setEnzymeEntry(this);
    historyGraph = new HistoryGraph(currentNode);
    status = Status.SUGGESTED;
    source = EnzymeSourceConstant.INTENZ;
    isActive = true;
    isGhost = false;
  }

  /**
   * Adds a reaction to the list of reactions.
   * <p/>
   * Duplicates are not stored.
   *
   * @param reaction The reaction to be added.
   * @throws NullPointerException if <code>reaction</code> is <code>null</code>.
   * @deprecated use addEnzymaticReaction instead
   */
  public void addReaction(Reaction reaction) {
    if (reaction == null) throw new NullPointerException("Parameter 'reaction' must not be null.");
    for (Iterator<Reaction> it = reactions.iterator(); it.hasNext();) {
      Reaction storedReaction = it.next();
      if (storedReaction.equals(reaction)) return;
    }

    reactions.add(reaction);
  }

    /**
     * Adds a reaction to the list of alternative reactions.
     * @param reaction
     * @param view
     * @param iubmb the IUBMB flag.
     */
    public void addEnzymaticReaction(Reaction reaction, String view,
    		boolean iubmb){
        if (enzymaticReactions == null)
            enzymaticReactions = new EnzymaticReactions();
        enzymaticReactions.add(reaction, view, iubmb);
    }

  /**
   * Adds a cofactor to the list of cofactors.
   * <p/>
   * Duplicates are not stored.
   *
   * @param cofactor The cofactor to be added.
   * @throws NullPointerException if <code>cofactor</code> is <code>null</code>.
   * @deprecated use {@link #addCofactor(Object)} instead
   */
  public void addCofactor(Cofactor cofactor) {
    if (cofactor == null)
        throw new NullPointerException("Parameter 'cofactor' must not be null.");
    if (!cofactors.contains(cofactor))
        cofactors.add(cofactor);
  }

  /**
   * Adds a cofactor to the list of cofactors.
   * @param cofactor A <code>Cofactor</code> or <code>OperatorSet</code> object.
   */
  public void addCofactor(Object cofactor){
	  if (cofactor == null)
		  throw new NullPointerException("Parameter 'cofactor' must not be null.");
	  if (!(cofactor instanceof Cofactor) && !(cofactor instanceof OperatorSet))
		  throw new IllegalArgumentException("The cofactor class is " + cofactor.getClass());
	  cofactors.add(cofactor);
  }

  /**
   * Removes a reaction from the list of reactions.
   *
   * @param equation The reaction to be removed.
   * @throws NullPointerException if <code>equation</code> is <code>null</code>.
   * @deprecated use removeEnzymaticReaction instead
   */
  public void removeReaction(String equation) {
    if (equation == null) throw new NullPointerException("Parameter 'equation' must not be null.");
    for (Iterator<Reaction> it = reactions.iterator(); it.hasNext();) {
      Reaction reaction = it.next();
      if (reaction.getTextualRepresentation().equals(equation)) {
        reactions.remove(reaction);
        return;
      }
    }
  }

    public void removeEnzymaticReaction(Reaction reaction){
        enzymaticReactions.removeReaction(reaction);
    }

  /**
   * Removes a cofactor from the list of cofactors.
   *
   * @param cofactorString The cofactor string to be removed.
   * @throws NullPointerException if <code>cofactorString</code> is <code>null</code>.
   * @deprecated use removeCofactor(Object) instead
   */
  public void removeCofactor(String cofactorString) {
      if (cofactorString == null) throw new NullPointerException("Parameter 'cofactorString' must not be null.");
      for (Iterator<Object> it = cofactors.iterator(); it.hasNext();) {
          Cofactor cofactor = (Cofactor) it.next(); // FIXME: what if it is a OperatorSet?
          if (cofactor.getCompound().getName().equals(cofactorString)) {
              cofactors.remove(cofactor);
              return;
          }
      }
  }

  /**
   * Removes a cofactor from the list of cofactors.
   * @param cofactor
   * @deprecated use {@link #removeCofactor(Object)} instead
   */
  public void removeCofactor(Cofactor cofactor){
      if (cofactor == null)
          throw new NullPointerException("Cannot remove a null cofactor");
      cofactors.remove(cofactor);
  }


  /**
   * Removes a cofactor from the list of cofactors.
   * @param cofactor
   */
  public void removeCofactor(Object cofactor){
      if (cofactor == null)
          throw new NullPointerException("Cannot remove a null cofactor");
      cofactors.remove(cofactor);
  }

  /**
   * Adds a synonsm to the list of synonyms.
   *
   * @param synonym The synonym to be added.
   * @throws NullPointerException if <code>synonym</code> is <code>null</code>.
   */
  public void addSynonym(EnzymeName synonym) {
    if (synonym == null) throw new NullPointerException("Parameter 'synonym' must not be null.");
    synonyms.add(synonym);
  }

  /**
   * Removes a synonsm from the list of synonyms.
   *
   * @param synonym The synonym to be removed.
   * @throws NullPointerException if <code>synonym</code> is <code>null</code>.
   */
  public void removeSynonym(String synonym) {
    if (synonym == null) throw new NullPointerException("Parameter 'synonym' must not be null.");
    synonyms.remove(synonym);
  }

  /**
   * Adds a link to the list of additional links.
   * <p/>
   * Duplicates are not stored.
   *
   * @param link The link to be added.
   * @throws NullPointerException if <code>link</code> is <code>null</code>.
   */
  public void addLink(EnzymeLink link) {
    if (link == null) throw new NullPointerException("Parameter 'link' must not be null.");
    // Don't save duplicates.
    if (!links.contains(link)) links.add(link);
  }

  /**
   * Removes a link from the list of additional links.
   *
   * @param link The link to be removed.
   * @throws NullPointerException if <code>link</code> is <code>null</code>.
   */
  public void removeLink(EnzymeLink link) {
    if (link == null) throw new NullPointerException("Parameter 'link' must not be null.");
    links.remove(link);
  }

  /**
   * Adds a reference to the list of references.
   *
   * @param reference The reference to be added.
   * @throws NullPointerException if <code>reference</code> is <code>null</code>.
   */
  public void addReference(Reference reference) {
    if (reference == null) throw new NullPointerException("Parameter 'reference' must not be null.");
    for (Iterator<Reference> it = references.iterator(); it.hasNext();) {
      Reference storedReference = it.next();
      if (storedReference.equals(reference)) return;
    }

    references.add(reference);
  }

  /**
   * Removes a reference from the list of references.
   *
   * @param reference The reference to be removed.
   * @throws NullPointerException if <code>reference</code> is <code>null</code>.
   */
  public void removeReference(Reference reference) {
    if (reference == null) throw new NullPointerException("Parameter 'reference' must not be null.");
    for (Iterator<Reference> it = references.iterator(); it.hasNext();) {
      Reference storedReference = it.next();
      if (reference.equals(storedReference)) {
        references.remove(storedReference);
        return;
      }
    }
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeEntry)) return false;

    final EnzymeEntry enzymeEntry = (EnzymeEntry) o;

    if (isActive != enzymeEntry.isActive) return false;
    if (isGhost != enzymeEntry.isGhost) return false;
    if (className != null ? !className.equals(enzymeEntry.className) : enzymeEntry.className != null) return false;
    if (cofactors != null ? !cofactors.equals(enzymeEntry.cofactors) : enzymeEntry.cofactors != null) return false;
    if (comments != null ? !comments.equals(enzymeEntry.comments) : enzymeEntry.comments != null) return false;
    if (commonNames != null ? !commonNames.equals(enzymeEntry.commonNames) : enzymeEntry.commonNames != null) return false;
    if (ec != null ? !ec.equals(enzymeEntry.ec) : enzymeEntry.ec != null) return false;
    if (historyGraph != null ? !historyGraph.equals(enzymeEntry.historyGraph) : enzymeEntry.historyGraph != null) return false;
    if (id != null ? !id.equals(enzymeEntry.id) : enzymeEntry.id != null) return false;
    if (links != null ? !links.equals(enzymeEntry.links) : enzymeEntry.links != null) return false;
    if (note != null ? !note.equals(enzymeEntry.note) : enzymeEntry.note != null) return false;
//    if (reactions != null ? !reactions.equals(enzymeEntry.reactions) : enzymeEntry.reactions != null) return false;
    if (enzymaticReactions != null ?
        !enzymaticReactions.equals(enzymeEntry.enzymaticReactions) :
		enzymeEntry.enzymaticReactions != null) return false;
    if (references != null ? !references.equals(enzymeEntry.references) : enzymeEntry.references != null) return false;
    if (source != null ? !source.equals(enzymeEntry.source) : enzymeEntry.source != null) return false;
    if (status != null ? !status.equals(enzymeEntry.status) : enzymeEntry.status != null) return false;
    if (subSubclassName != null ? !subSubclassName.equals(enzymeEntry.subSubclassName) : enzymeEntry.subSubclassName !=
                                                                                         null)
      return false;
    if (subclassName != null ? !subclassName.equals(enzymeEntry.subclassName) : enzymeEntry.subclassName != null) return false;
    if (synonyms != null ? !synonyms.equals(enzymeEntry.synonyms) : enzymeEntry.synonyms != null) return false;
    if (systematicName != null ? !systematicName.equals(enzymeEntry.systematicName) : enzymeEntry.systematicName !=
                                                                                      null)
      return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
    @Override
  public int hashCode() {
    int result;
    result = (id != null ? id.hashCode() : 0);
    result = 29 * result + (ec != null ? ec.hashCode() : 0);
    result = 29 * result + (className != null ? className.hashCode() : 0);
    result = 29 * result + (subclassName != null ? subclassName.hashCode() : 0);
    result = 29 * result + (subSubclassName != null ? subSubclassName.hashCode() : 0);
    result = 29 * result + (commonNames != null ? commonNames.hashCode() : 0);
    result = 29 * result + (systematicName != null ? systematicName.hashCode() : 0);
    result = 29 * result + (synonyms != null ? synonyms.hashCode() : 0);
    result = 29 * result + (reactions != null ? reactions.hashCode() : 0);
    result = 29 * result + (cofactors != null ? cofactors.hashCode() : 0);
    result = 29 * result + (links != null ? links.hashCode() : 0);
    result = 29 * result + (comments != null ? comments.hashCode() : 0);
    result = 29 * result + (references != null ? references.hashCode() : 0);
    result = 29 * result + (note != null ? note.hashCode() : 0);
    result = 29 * result + (historyGraph != null ? historyGraph.hashCode() : 0);
    result = 29 * result + (status != null ? status.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (isActive ? 1 : 0);
    result = 29 * result + (isGhost ? 1 : 0);
    return result;
  }

  // ----------------  GETTER & SETTER ------------------

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public EnzymeCommissionNumber getEc() {
    return ec;
  }

  /**
   * Sets the EC number.
   *
   * @param ec The new EC number.
   * @throws NullPointerException if <code>ec</code> is <code>null</code>.
   */
  public void setEc(EnzymeCommissionNumber ec) {
    if (ec == null) throw new NullPointerException("Parameter 'ec' must not be null.");
    this.ec = ec;
  }

  public String getClassName() {
    return className;
  }

  /**
   * Sets the class name.
   *
   * @param className The class name.
   * @throws NullPointerException     if <code>className</code> is <code>null</code>
   * @throws IllegalArgumentException if <code>className</code> is empty.
   */
  public void setClassName(String className) {
    if (className == null) throw new NullPointerException("Parameter 'className' must not be null.");
    if (className.equals("")) throw new IllegalArgumentException("Parameter 'className' must not be empty.");
    this.className = className;
  }

  public String getSubclassName() {
    return subclassName;
  }

  /**
   * Sets the subclass name.
   *
   * @param subclassName The subclass name.
   * @throws NullPointerException     if <code>subclassName</code> is <code>null</code>
   * @throws IllegalArgumentException if <code>subclassName</code> is empty.
   */
  public void setSubclassName(String subclassName) {
    if (subclassName == null) throw new NullPointerException("Parameter 'subclassName' must not be null.");
    if (subclassName.equals("")) throw new IllegalArgumentException("Parameter 'subclassName' must not be empty.");
    this.subclassName = subclassName;
  }

  public String getSubSubclassName() {
    return subSubclassName;
  }

  /**
   * Sets the sub-subclass name.
   *
   * @param subSubclassName The sub-subclass name.
   * @throws NullPointerException if <code>subSubclassName</code> is <code>null</code>.
   */
  public void setSubSubclassName(String subSubclassName) {
    if (subSubclassName == null) throw new NullPointerException("Parameter 'subSubclassName' must not be null.");
    this.subSubclassName = subSubclassName;
  }

  public List<EnzymeName> getCommonNames() {
    return commonNames;
  }
  
  /**
   * @return the IntEnz common name.
   */
  public EnzymeName getCommonName(){
	  return getCommonName(null);
  }

  /**
   * Returns the first common name which <code>view</code> value matches the
   * parameter's view value.
   * @param view web view (defaults to INTENZ)
   * @return the common name.
   * @throws IllegalArgumentException if <code>view</code> is not either
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.INTENZ},
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.IUBMB} or
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.SIB} .
   */
  public EnzymeName getCommonName(EnzymeViewConstant view) {
    if (view == null) view = EnzymeViewConstant.INTENZ;
    if (view != EnzymeViewConstant.INTENZ && view != EnzymeViewConstant.IUBMB && view != EnzymeViewConstant.SIB)
      throw new IllegalArgumentException("Parameter 'view' must be either 'EnzymeViewConstant.INTENZ', 'EnzymeViewConstant.IUBMB' or 'EnzymeViewConstant.SIB'");
    if (commonNames == null || commonNames.size() == 0) return EnzymeName.UNDEF;
    for (int iii = 0; iii < commonNames.size(); iii++) {
      EnzymeName enzymeName = commonNames.get(iii);
      if (view.toString().equals(EnzymeViewConstant.INTENZ.toString()) &&
          EnzymeViewConstant.isInIntEnzView(enzymeName.getView().toString()))
        return enzymeName;
      if (view.toString().equals(EnzymeViewConstant.IUBMB.toString()) &&
          EnzymeViewConstant.isInIUBMBView(enzymeName.getView().toString()))
        return enzymeName;
      if (view.toString().equals(EnzymeViewConstant.SIB.toString()) &&
          EnzymeViewConstant.isInSIBView(enzymeName.getView().toString()))
        return enzymeName;
    }
    return commonNames.get(0);
  }

  /**
   * Sets the list of common names.
   * <p/>
   * An enzyme must have at least one or at most two common names.
   * Empty strings will be omitted.
   *
   * @param commonNames List of common names.
   * @throws NullPointerException if <code>commonNames</code> is <code>null</code>.
   * @throws EnzymeNameException  if the list of common names does contain less than 1 or more than 2 common names or
   *                              if the type of a name in the list is not defined as {@link EnzymeNameTypeConstant.COMMON_NAME}.
   */
  public void setCommonNames(List<EnzymeName> commonNames) throws EnzymeNameException {
    if (commonNames == null) throw new NullPointerException("Parameter 'commonNames' must not be null.");
    if (commonNames.size() > 2)
      throw new EnzymeNameException("At most two common names can be part of this list.");
    for (int iii = 0; iii < commonNames.size(); iii++) {
      EnzymeName enzymeName = commonNames.get(iii);
      if (enzymeName.getType() != EnzymeNameTypeConstant.COMMON_NAME)
        throw new EnzymeNameException("The name's type " +
                                      "must be of type 'uk.ac.ebi.intenz.domain.constants.EnzymeNameTypeConstant.COMMON_NAME'.");
      if (enzymeName.getName().equals("")) commonNames.remove(iii--);
    }
    if (commonNames.size() < 1)
      throw new EnzymeNameException("At least one common name must be part of this list.");

    this.commonNames = commonNames;
  }

  /**
   * Adds a common name to the list of common names.
   * <p/>
   * If the list already contains two elements an {@link uk.ac.ebi.intenz.webapp.exceptions.EnzymeNameException} is thrown.
   * Empty strings will be omitted.
   *
   * @param commonName The common name to be added.
   * @throws EnzymeNameException if the list of common names already contains two elements.
   */
  public void addCommonName(EnzymeName commonName) throws EnzymeNameException {
    if (commonName == null) throw new NullPointerException("Parameter 'commonName' must not be null.");
    if (commonNames.size() == 2) throw new EnzymeNameException("The list of common names already contains two common names.");
    if (commonName.getName().equals("")) return;
    commonNames.add(commonName);
  }

  public EnzymeName getSystematicName() {
    return systematicName;
  }

  /**
   * Sets the systematic name of the enzyme.
   *
   * @param systematicName The systematic name.
   * @throws NullPointerException if <code>systematicName</code> is <code>null</code>.
   */
  public void setSystematicName(EnzymeName systematicName) {
    if (systematicName == null) throw new NullPointerException("Parameter 'systematicName' must not be null.");
    this.systematicName = systematicName;
  }

  /** @deprecated use getEnzymaticReactions instead */
  public List<Reaction> getReactions() {
    return reactions;
  }

  /**
   * Returns a list of reactions which <code>view</code> values match the parameter's view value.
   *
   * @return the {@link java.util.ArrayList} of reactions.
   * @throws NullPointerException     if <code>view</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>view</code> is not either
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.INTENZ},
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.IUBMB} or
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.SIB} .
   * @deprecated use {@link #getReactions(View)} or {@link #getReactions(String)} instead.
   */
  public List<Reaction> getReactions(EnzymeViewConstant view) {
	  return getReactions(view.toString());
  }
  
  public List<Reaction> getReactions(String view){
    View theView = View.valueOf(view);
    return getReactions(theView);
  }

  public List<Reaction> getReactions(View view){
	return enzymaticReactions.getReactions(view);
  }
  
  /**
   * Gets a collection of enzymatic reactions for the given view.
   * @param view a web view.
   * @return an {@link EnzymaticReactions} object containing only reactions in
   * 		the given view.
   * @since 4.2.7
   */
  public EnzymaticReactions getEnzymaticReactions(View view){
	  return enzymaticReactions.forView(view);
  }

  /**
   * Sets the list of reactions.
   * <p/>
   * Empty reaction strings will be omitted.
   *
   * @param reactions The list of reactions.
   * @throws EnzymeReactionException if the list of reactions is empty.
   */
  public void setReactions(List<Reaction> reactions) throws EnzymeReactionException {
    if (reactions == null) throw new NullPointerException("Parameter 'reactions' must not be null.");
    if (reactions.isEmpty())
        throw new EnzymeReactionException("At least one reaction must be part of this list.");
    for (int iii = 0; iii < reactions.size(); iii++) {
      Reaction reaction = reactions.get(iii);
      if (reaction.getTextualRepresentation().equals("")) reactions.remove(iii--);
    }
    this.reactions = reactions;
  }

  public EnzymaticReactions getEnzymaticReactions(){
	  return enzymaticReactions;
  }

  public void setEnzymaticReactions(EnzymaticReactions enzymaticReactions) {
	this.enzymaticReactions = enzymaticReactions;
  }

  public Set<Object> getCofactors() {
    return cofactors;
  }

  public void setCofactors(Set<Object> cofactors) {
    this.cofactors = cofactors;
  }

  public List<EnzymeName> getSynonyms() {
    return synonyms;
  }
  
  public List<EnzymeName> getSynonyms(String view){
	  View theView = View.valueOf(view);
	  return getSynonyms(theView);
  }
  
  public List<EnzymeName> getSynonyms(View theView){
	  if (synonyms == null) return new ArrayList<EnzymeName>();
	  if (synonyms.size() == 0) return synonyms;
	  boolean isInView = false;
	  List<EnzymeName> names = new ArrayList<EnzymeName>();
	  for (EnzymeName name : synonyms){
		  switch (theView) {
		  case INTENZ:
			  isInView = name.getView().isInIntEnzView();
			  break;
		  case IUBMB:
			  isInView = name.getView().isInIUBMBView();
			  break;
		  case SIB:
			  isInView = name.getView().isInSIBView();
			  break;
		  }
		  if (isInView) names.add(name);
	  }
	  return names;
  }

  /**
   * Returns a list of synonyms which <code>view</code> values match the parameter's view value.
   *
   * @return the {@link java.util.ArrayList} of synonyms.
   * @throws NullPointerException     if <code>view</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>view</code> is not either
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.INTENZ},
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.IUBMB} or
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.SIB} .
   * @deprecated use {@link #getSynonyms(String)} or {@link #getSynonyms(View)} instead.
   */
  public List<EnzymeName> getSynonyms(EnzymeViewConstant view) {
    return getSynonyms(view.toString());
  }

  /**
   * Sets the list of synonyms.
   *
   * @param synonyms The list of synonyms.
   * @throws NullPointerException if <code>synonyms</code> is <code>null</code>.
   */
  public void setSynonyms(List<EnzymeName> synonyms) {
    if (synonyms == null) throw new NullPointerException("Parameter 'synonyms' must not be null.");
    this.synonyms = synonyms;
  }

  public List<EnzymeComment> getComments() {
    return comments;
  }
  
  public List<EnzymeComment> getComments(String view){
	  View theView = View.valueOf(view);
	  return getComments(theView);
  }

  public List<EnzymeComment> getComments(View theView){
	  if (comments == null) return new ArrayList<EnzymeComment>();
	  if (comments.size() == 0) return comments;
	  boolean isInView = false;
	  List<EnzymeComment> commentsInView = new ArrayList<EnzymeComment>();
	  for (EnzymeComment comment : comments){
		  switch (theView) {
		  case INTENZ:
			  isInView = comment.getView().isInIntEnzView();
			  break;
		  case IUBMB:
			  isInView = comment.getView().isInIUBMBView();
			  break;
		  case SIB:
			  isInView = comment.getView().isInSIBView();
			  break;
		  }
		  if (isInView) commentsInView.add(comment);
	  }
	  return commentsInView;
  }

  /**
   * Returns a list of comments which <code>view</code> values match the parameter's view value.
   *
   * @return the {@link java.util.ArrayList} of comments.
   * @throws NullPointerException     if <code>view</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>view</code> is not either
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.INTENZ},
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.IUBMB} or
   *                                  {@link uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant.SIB} .
   * @deprecated use {@link #getComments(String)} instead
   * */
  public List<EnzymeComment> getComments(EnzymeViewConstant view) {
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (view != EnzymeViewConstant.INTENZ && view != EnzymeViewConstant.IUBMB && view != EnzymeViewConstant.SIB)
      throw new IllegalArgumentException("Parameter 'view' must be either 'EnzymeViewConstant.INTENZ', 'EnzymeViewConstant.IUBMB' or 'EnzymeViewConstant.SIB'");
    if (comments == null) return new ArrayList<EnzymeComment>();
    if (comments.size() == 0) return comments;
    List<EnzymeComment> groupedComments = new ArrayList<EnzymeComment>();
    for (int iii = 0; iii < comments.size(); iii++) {
      EnzymeComment comment = comments.get(iii);
      if (view.toString().equals(EnzymeViewConstant.INTENZ.toString()) &&
          EnzymeViewConstant.isInIntEnzView(comment.getView().toString())) {
        groupedComments.add(comment);
        continue;
      }
      if (view.toString().equals(EnzymeViewConstant.IUBMB.toString()) &&
          EnzymeViewConstant.isInIUBMBView(comment.getView().toString())) {
        groupedComments.add(comment);
        continue;
      }
      if (view.toString().equals(EnzymeViewConstant.SIB.toString()) &&
          EnzymeViewConstant.isInSIBView(comment.getView().toString())) {
        groupedComments.add(comment);
        continue;
      }
    }
    return groupedComments;
  }

  /**
   * Sets the list of comments.
   *
   * @param comments The list of comments.
   * @throws NullPointerException if <code>comments</code> is <code>null</code>.
   */
  public void setComments(List<EnzymeComment> comments) {
    if (comments == null) throw new NullPointerException("Parameter 'comments' must not be null.");
    this.comments = comments;
  }

  public SortedSet<EnzymeLink> getLinks() {
    return links;
  }
  
  public SortedSet<EnzymeLink> getLinks(String view){
	  View theView = View.valueOf(view);
	  return getLinks(theView);
  }
  
  /**
   * CAS numbers for IntEnz and IUBMB views.
   * @return
   */
  public SortedSet<EnzymeLink> getCasNumbers(){
	  SortedSet<EnzymeLink> cas = new TreeSet<EnzymeLink>();
	  for (EnzymeLink link : links){
		  if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.CAS)){
			  cas.add(link);
		  }
	  }
	  return cas;
  }

  public SortedSet<EnzymeLink> getUniProtLinks(){
      SortedSet<EnzymeLink> uniProtLinks = new TreeSet<EnzymeLink>();
 	  for (EnzymeLink link : links){
		  if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.SWISSPROT))
            uniProtLinks.add(link);
      }
      return uniProtLinks;
  }
  
  /**
   * Return the links to entries in external databases.
   * These exclude:
   * <ul>
   * <li>CAS numbers (they are just identifiers, without
   * any URL to go to). Use getCasNumbers.</li>
   * <li>UniProt links. Use getUniProtLinks.</li>
   * </ul>
   * @param theView
   * @return
   */
  public SortedSet<EnzymeLink> getLinks(View theView){
	  boolean isInView = false;
	  SortedSet<EnzymeLink> linksInView = new TreeSet<EnzymeLink>();
	  for (EnzymeLink link : links){
		  if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.CAS) ||
              link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.SWISSPROT))
			  continue;
		  switch (theView) {
		  case INTENZ:
			  isInView = link.getView().isInIntEnzView();
			  break;
		  case IUBMB:
			  isInView = link.getView().isInIUBMBView();
			  break;
		  case SIB:
			  isInView = link.getView().isInSIBView();
			  break;
		  }
		  if (isInView) linksInView.add(link);
	  }
	  // Automatic links (not in the database):
      if (status.equals(Status.PRELIMINARY)){
          if (theView == View.INTENZ)
              linksInView.add(EnzymeLink.EXPASY);
      } else {
          switch (theView) {
          case INTENZ:
              linksInView.add(EnzymeLink.CSA);
              linksInView.add(EnzymeLink.NC_IUBMB);
          case IUBMB:
              linksInView.add(EnzymeLink.PDB);
              linksInView.add(EnzymeLink.BRENDA);
              linksInView.add(EnzymeLink.EXPASY);
              linksInView.add(EnzymeLink.KEGG);
              linksInView.add(EnzymeLink.ERGO);
              break;
          }
      }
	  return linksInView;
  }

  /**
   * 
   * @param view
   * @return
   * @deprecated use {@link #getLinks(String)} instead
   */
  public SortedSet<EnzymeLink> getLinks(EnzymeViewConstant view) {
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (view != EnzymeViewConstant.INTENZ && view != EnzymeViewConstant.IUBMB && view != EnzymeViewConstant.SIB)
      throw new IllegalArgumentException("Parameter 'view' must be either 'EnzymeViewConstant.INTENZ', 'EnzymeViewConstant.IUBMB' or 'EnzymeViewConstant.SIB'");
    if (links == null) return new TreeSet<EnzymeLink>();
    if (links.size() == 0) return links;
    SortedSet<EnzymeLink> groupedLinks = new TreeSet<EnzymeLink>();
    for (Iterator<EnzymeLink> it = links.iterator(); it.hasNext();) {
      EnzymeLink link = it.next();
      if (view.toString().equals(EnzymeViewConstant.INTENZ.toString()) &&
          EnzymeViewConstant.isInIntEnzView(link.getView().toString())) {
        groupedLinks.add(link);
        continue;
      }
      if (view.toString().equals(EnzymeViewConstant.IUBMB.toString()) &&
          EnzymeViewConstant.isInIUBMBView(link.getView().toString())) {
        groupedLinks.add(link);
        continue;
      }
      if (view.toString().equals(EnzymeViewConstant.SIB.toString()) &&
          EnzymeViewConstant.isInSIBView(link.getView().toString())) {
        groupedLinks.add(link);
        continue;
      }
    }
    return groupedLinks;
  }

  /**
   * Sets the list of links.
   *
   * @param links The list of links.
   * @throws NullPointerException if <code>links</code> is <code>null</code>.
   */
  public void setLinks(SortedSet<EnzymeLink> links) {
    if (links == null) throw new NullPointerException("Parameter 'links' must not be null.");
    this.links = links;
  }

  public List<Reference> getReferences() {
    return references;
  }

  /**
   * Sets the list of refernces.
   *
   * @param references The list of references.
   * @throws EnzymeReferenceException if <code>references</code> is <code>null</code>.
   */
  public void setReferences(List<Reference> references) throws EnzymeReferenceException {
    if (references == null) throw new NullPointerException("Parameter 'references' must not be null.");
    if (references.size() < 1)
      throw new EnzymeReferenceException("At least one reference must be part of this list.");
    this.references = references;
  }

  public String getNote() {
    return note;
  }

  /**
   * Sets the note.
   *
   * @param note the note.
   * @throws NullPointerException if <code>note</code> is <code>null</code>.
   */
  public void setNote(String note) {
    if (note == null) throw new NullPointerException("Parameter 'note' must not be null.");
    this.note = note;
  }

  public HistoryGraph getHistory() {
    return historyGraph;
  }

  /**
   * Sets the history graph.
   *
   * @param historyGraph the history graph.
   * @throws NullPointerException if <code>historyGraph</code> is <code>null</code>.
   */
  public void setHistory(HistoryGraph historyGraph) {
    if (historyGraph == null) throw new NullPointerException("Parameter 'historyGraph' must not be null.");
    this.historyGraph = historyGraph;
  }

  public Status getStatus() {
    return status;
  }

  /**
   * Sets the enzyme's status.
   *
   * @param status The enzyme's status.
   * @throws NullPointerException if <code>status</code> is <code>null</code>.
   */
  public void setStatus(Status status) {
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    this.status = status;
  }

  public EnzymeSourceConstant getSource() {
    return source;
  }

  /**
   * Sets the enzyme's source.
   *
   * @param source The enzyme's source.
   * @throws NullPointerException if <code>source</code> is <code>null</code>.
   */
  public void setSource(EnzymeSourceConstant source) {
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    this.source = source;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  public boolean isGhost() {
    return isGhost;
  }

  public void setGhost(boolean ghost) {
    isGhost = ghost;
  }

  public boolean hasNames() {
    if (commonNames != null && commonNames.size() > 0) return true;
    if (synonyms != null && synonyms.size() > 0) return true;
    if (systematicName != null && !systematicName.getName().equals("")) return true;
    return false;
  }

  // ------------------- PRIVATE METHODS ------------------------

}
