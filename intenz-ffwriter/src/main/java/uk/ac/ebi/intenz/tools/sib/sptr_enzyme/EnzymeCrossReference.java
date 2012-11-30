package uk.ac.ebi.intenz.tools.sib.sptr_enzyme;

import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;

import java.io.Serializable;
import java.util.Hashtable;

/** 
 * This class implements the {@link uk.ac.ebi.interfaces.sptr.SPTRCrossReference SPTRCrossReference} interface.
 *
 * @author Michael Darsow
 * @version preliminary - $Revision: 1.2 $ $Date: 2008/01/28 11:43:22 $
 */
public class EnzymeCrossReference implements SPTRCrossReference, Serializable {

  /** Constant for database cross reference to SwissProt. */
  public static final String SWISSPROT = "SwissProt";

  /** Constant for database cross reference to PROSITE. */
  public static final String PROSITE = "PROSITE";

  /** Constant for database cross reference to MIM Disease Database. */
  public static final String MIM = "MIM";

  private String accessionNumber;

  private String databaseName;

  private Hashtable properties;

  /**
   * Creates an <code>EnzymeCrossReference</code> instance to the given database.
   *
   * Possible database name constants are:
   * <ul>
   *   <li>{@link uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference#SWISSPROT SwissProt}</li>
   *   <li>{@link uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference#PROSITE PROSITE}</li>
   *   <li>{@link uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference#MIM MIM Disease Database}</li>
   * </ul>
   *
   * @param databaseName The name of database of this cross reference.
   */
  public EnzymeCrossReference(String databaseName) {
    this.accessionNumber = "";
    this.databaseName = databaseName;
    properties = new Hashtable();
  }

  /**
   * <em class="text">
   * Sets the accession number of this EnzymeCrossReference.
   * </em>
   * <br><br>
   * <em class="text">
   * Cross references are found in the DR/PR/DI lines on the marked positions, where
   * the data base name is marked red (if existing) and the accession number green.
   * DR lines are SwissProt, PR lines are PROSITE and DI lines are MIM cross references.
   * DR lines contain between 1 and 3 cross references.
   *
   * <pre class="example">
   * DR   <em class="green">O66503</em>; RIR1_AQUAE; <em class="green">P42491</em>, RIR1_ASFB7;  <em class="green">P26685</em>, RIR1_ASFM2;
   * PR   <em class="red">PROSITE</em>; <em class="green">PDOC00084</em>;
   * DI   Guanidinoacetate methyltransferase deficiency; <em class="red">MIM</em>:<em class="green">601240</em>.
   * </pre>
   * </em>
   *
   * @param accessionNumber The accession number.
   */
  public void setAccessionNumber(String accessionNumber) {
    this.accessionNumber = accessionNumber;
  }

  /**
   * <em class="text">
   * Returns the accession number of this EnzymeCrossReference.
   * </em>
   * <br><br>
   * <em class="text">
   * Cross references are found in the DR/PR/DI lines on the marked positions, where
   * the data base name is marked red (if existing) and the accession number green.
   * DR lines are SwissProt, PR lines are PROSITE and DI lines are MIM cross references.
   * DR lines contain between 1 and 3 cross references.
   *
   * <pre class="example">
   * DR   <em class="green">O66503</em>; RIR1_AQUAE; <em class="green">P42491</em>, RIR1_ASFB7;  <em class="green">P26685</em>, RIR1_ASFM2;
   * PR   <em class="red">PROSITE</em>; <em class="green">PDOC00084</em>;
   * DI   Guanidinoacetate methyltransferase deficiency; <em class="red">MIM</em>:<em class="green">601240</em>.
   * </pre>
   * </em>
   *
   * @return The accession number of this EnzymeCrossReference.
   */
  public String getAccessionNumber() {
    return accessionNumber;
  }

  /**
   * <em class="text">
   * Sets a property and value of this EnzymeCrossReference.
   * </em>
   *
   * Properties of Enzyme cross references are always
   * {@link uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference#PROPERTY_DESCRIPTION description}s.
   *
   * @param property The property.
   * @param value The value of the property.
   * @throws IllegalArgumentException if the <code>property</code> is not a valid property.
   */
  public void setPropertyValue(int property, String value)  {
    if(property != EnzymeCrossReference.PROPERTY_DESCRIPTION) throw new IllegalArgumentException("The given property is not supported.");
    properties.put(new Integer(property),  value);
  }

  /**
   * <em class="text">
   * Indicates if a cross reference property is set.
   * </em>
   *
   * @param property The property.
   * @return true, if the property is set, false if it is not.
   */
  public boolean hasProperty(int property) {
    return properties.get(new Integer(property)) != null;
  }

  /**
   * <em class="text">
   * Returns the value of the property.
   * </em>
   *
   * @param property The property.
   * @return The value of the property.
   */
  public String getPropertyValue(int property) {
    return (String) properties.get(new Integer(property));
  }

  /**
   * <em class="text">
   * Returns the name of the database this EnzymeCrossReference refers to.
   * </em>
   *
   * @return The name of the database this EnzymeCrossReference refers to.
   */
  public String getDatabaseName() {
    return databaseName;
  }
}
