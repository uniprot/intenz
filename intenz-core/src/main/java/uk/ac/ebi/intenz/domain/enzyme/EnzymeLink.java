package uk.ac.ebi.intenz.domain.enzyme;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;

/**
 * An enzyme link is used to store cross-reference information.
 * <p/>
 * Most of the links use a static URL which differs only in the EC number used to identify the enzyme.
 * These links can be obtained by accessing the class constants.<br/>
 * Other cross-references contain a static URL but need a different identifier. For example,
 * a SwissProt cross-reference needs a protein accession number. These links are provided
 * via the according factory methods. In case of SwissProt and MIM links it is also possible
 * to provide a name for the protein or disease respectively.<br/>
 * <p/>
 * The terms <i>link</i> and <i>cross-reference</i> are used synonymously.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeLink implements Comparable<EnzymeLink>, Commented, Viewable {
  /**
   * Static link to the BRENDA database.
   */
  public static final EnzymeLink BRENDA =
	  new EnzymeLink(XrefDatabaseConstant.BRENDA, XrefDatabaseConstant.BRENDA.getUrl(),
			  "", "BRENDA", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);
  
  /**
   * Link to MetaCyc.
   */
  public static final EnzymeLink METACYC =
	  new EnzymeLink(XrefDatabaseConstant.METACYC, XrefDatabaseConstant.METACYC.getUrl(),
			  "", "MetaCyc", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  /**
   * Static link to the KEGG database.
   */
  public static final EnzymeLink KEGG =
	  new EnzymeLink(XrefDatabaseConstant.KEGG, XrefDatabaseConstant.KEGG.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  /**
   * Static link to the ENZYME database at expasy.org.
   */
  public static final EnzymeLink EXPASY =
	  new EnzymeLink(XrefDatabaseConstant.ENZYME, XrefDatabaseConstant.ENZYME.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  /**
   * Static link to the GM pages (NC-IUBMB).
   */
  public static final EnzymeLink NC_IUBMB =
	  new EnzymeLink(XrefDatabaseConstant.NC_IUBMB, XrefDatabaseConstant.NC_IUBMB.getUrl(),
			  "", "", EnzymeSourceConstant.IUBMB, EnzymeViewConstant.IUBMB_INTENZ);

  /**
   * Static link to the ERGO database.
   */
  public static final EnzymeLink ERGO =
	  new EnzymeLink(XrefDatabaseConstant.ERGO, XrefDatabaseConstant.ERGO.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  /**
   * Static link to the ERGO (formerly WIT) database.
   */
  public static final EnzymeLink WIT =
	  new EnzymeLink(XrefDatabaseConstant.WIT, XrefDatabaseConstant.WIT.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  /**
   * Static link to the Gene Ontology database.
   */
  public static final EnzymeLink GO =
	  new EnzymeLink(XrefDatabaseConstant.GO, XrefDatabaseConstant.GO.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.IUBMB_INTENZ);

  /**
   * Static link to the NIST 74 database.
   */
  public static final EnzymeLink NIST74 =
	  new EnzymeLink(XrefDatabaseConstant.NIST74, XrefDatabaseConstant.NIST74.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.IUBMB_INTENZ);

  /**
   * Static link to the UM-BBD database.
   */
  public static final EnzymeLink UMBBD =
	  new EnzymeLink(XrefDatabaseConstant.UMBBD, XrefDatabaseConstant.UMBBD.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.IUBMB_INTENZ);

  /**
   * Static link to the PDB database.
   */
  public static final EnzymeLink PDB =
	  new EnzymeLink(XrefDatabaseConstant.PDB, XrefDatabaseConstant.PDB.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.IUBMB_INTENZ);

  /**
   * Static link to the CSA database.
   */
  public static final EnzymeLink CSA =
	  new EnzymeLink(XrefDatabaseConstant.CSA, XrefDatabaseConstant.CSA.getUrl(),
			  "", "", EnzymeSourceConstant.INTENZ, EnzymeViewConstant.INTENZ);

  private static Map<XrefDatabaseConstant, EnzymeLink> STATIC_LINKS;

  static {
    STATIC_LINKS = new TreeMap<XrefDatabaseConstant, EnzymeLink>();
    STATIC_LINKS.put(BRENDA.getXrefDatabaseConstant(), BRENDA);
    STATIC_LINKS.put(METACYC.getXrefDatabaseConstant(), METACYC);
    STATIC_LINKS.put(KEGG.getXrefDatabaseConstant(), KEGG);
    STATIC_LINKS.put(EXPASY.getXrefDatabaseConstant(), EXPASY);
    STATIC_LINKS.put(ERGO.getXrefDatabaseConstant(), ERGO);
//    STATIC_LINKS.put(GO.getXrefDatabaseConstant(), GO);
    STATIC_LINKS.put(NIST74.getXrefDatabaseConstant(), NIST74);
    STATIC_LINKS.put(UMBBD.getXrefDatabaseConstant(), UMBBD);
    STATIC_LINKS.put(PDB.getXrefDatabaseConstant(), PDB);
  }

  /**
   * All sources are controlled vocabulary.
   */
  private XrefDatabaseConstant xrefDatabaseConstant;

  /**
   * Some links have specific URLs.
   */
  private String specificUrl;

  /**
   * Some links need an accession number (e.g. EC number).
   */
  private String accession;

  /**
   * Some links have name associated (e.g. MIM links).
   */
  private String name;

  private EnzymeSourceConstant source;

  /**
   * Defines in which view this link will be displayed.
   */
  private EnzymeViewConstant view;

    private String dataComment;

    /**
   * Object cannot be created outside this class.
   *
   * @param xrefDatabaseConstant The data xrefDatabaseConstant name.
   * @param specificUrl          The URL of the given xrefDatabaseConstant.
   * @param accession            The accession (if needed).
   * @param name                 The name (if available).
   */
  private EnzymeLink(XrefDatabaseConstant xrefDatabaseConstant, String specificUrl, String accession, String name,
                     EnzymeSourceConstant source, EnzymeViewConstant view) {
    this.xrefDatabaseConstant = xrefDatabaseConstant;
    this.specificUrl = specificUrl;
    this.accession = accession;
    this.name = name;
    this.source = source;
    this.view = view;
  }

  /**
   * Returns an <code>EnzymeLink</code> instance.
   *
   * @param xrefDatabaseConstant The link's xrefDatabaseConstant.
   * @param specificUrl          The link's URL.
   * @param accession            The accession of this link.
   * @param name                 The name of this link.
   * @return the <code>EnzymeLink</code> instance.
   * @throws NullPointerException     if <code>xrefDatabaseConstant</code> is <code>null</code>.
   * @throws IllegalArgumentException if the arguments are not sufficient to create a link.
   */
  public static EnzymeLink valueOf(XrefDatabaseConstant xrefDatabaseConstant,
		  String specificUrl, String accession, String name,
          EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (xrefDatabaseConstant == null) throw new NullPointerException("Parameter 'xrefDatabaseConstant' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");

    // Static links.
//      if (view.toString().equals("") && STATIC_LINKS.containsKey(xrefDatabaseConstant))
    // Links based on EC number, except when they are passed as accession (BRENDA):
    if (STATIC_LINKS.containsKey(xrefDatabaseConstant)){
    	if (accession == null){
            return STATIC_LINKS.get(xrefDatabaseConstant);
    	} else {
    		// A static link to an EC number, but we have an accession? Preliminary ECs
    		return new EnzymeLink(xrefDatabaseConstant,
    				xrefDatabaseConstant.getUrl(),
    				accession, accession, source, view);
    	}
    }

    if ((specificUrl == null || specificUrl.equals("")) && (accession == null || accession.equals("")) &&
        (name == null || name.equals("")))
      throw new IllegalArgumentException("Either parameter specificUrl, accession or name must contain a value.");

    return new EnzymeLink(xrefDatabaseConstant, specificUrl, accession, name, source, view);
  }

  /**
   * Returns a commented <code>EnzymeLink</code> instance.
   * @param xrefDatabaseConstant
   * @param specificUrl
   * @param accession
   * @param name
   * @param source
   * @param view
   * @param dataComment
   * @return
   */
  public static EnzymeLink valueOf(XrefDatabaseConstant xrefDatabaseConstant, String specificUrl, String accession, String name,
                                   EnzymeSourceConstant source, EnzymeViewConstant view, String dataComment){
      EnzymeLink el = EnzymeLink.valueOf(xrefDatabaseConstant, specificUrl, accession, name, source, view);
      el.setDataComment(dataComment);
      return el;
  }

  /**
   * Checks if the given link is a static link.
   *
   * @param databaseConstant The database constant to be checked.
   * @return <code>true</code> if the link is a static link.
   * @throws NullPointerException if <code>link</code> is <code>null</code>.
   */
  public static boolean isStaticLink(XrefDatabaseConstant databaseConstant) {
    if (databaseConstant == null) throw new NullPointerException("Parameter 'databaseConstant' must not be null.");
    return STATIC_LINKS.containsKey(databaseConstant);
  }

  public static EnzymeLink getStaticLink(XrefDatabaseConstant databaseConstant) {
    if (databaseConstant == null) throw new NullPointerException("Parameter 'databaseConstant' must not be null.");
    return STATIC_LINKS.get(databaseConstant);
  }

  /**
   * Used for ordering links in a collection.
   * <p/>
   * Static and other links are ordered by their xrefDatabaseConstant name,
   * SwissProt and MIM cross-references by their name
   * and PROSITE links by their accession. Other links are ordered by their specific URL
   * if the xrefDatabaseConstant name is the same.
   * Noted: Changed so that only SwissProt compares on the basis of name and OMIM compares on the
   * basis of accession like PROSITE.
   *
   * @param link The object to be compared to this.
   * @return neg, 0 or pos. value to indicate the order of these two objects.
   * @throws NullPointerException if <code>o</code> is <code>null</code>.
   */
  public int compareTo(EnzymeLink link) {
    if (xrefDatabaseConstant == link.getXrefDatabaseConstant()) {
      if (name.equals(link.getName())) {
        if (accession.equals(link.getAccession())) {
          return specificUrl.compareTo(link.getSpecificUrl());
        }
        return accession.compareTo(link.getAccession());
      }
      return name.compareTo(link.getName());
    }

    return xrefDatabaseConstant.getDatabaseCode().compareTo(link.getXrefDatabaseConstant().getDatabaseCode());
  }

  /**
   * Checks for equality.
   *
   * @param o The object to be compared to this.
   * @return <code>true</code> if <code>o</code> is equal this object.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeLink)) return false;

    final EnzymeLink enzymeLink = (EnzymeLink) o;

    if (accession != null ? !accession.equals(enzymeLink.accession) : enzymeLink.accession != null) return false;
    if (name != null ? !name.equals(enzymeLink.name) : enzymeLink.name != null) return false;
    if (source != null ? !source.equals(enzymeLink.source) : enzymeLink.source != null) return false;
    if (specificUrl != null ? !specificUrl.equals(enzymeLink.specificUrl) : enzymeLink.specificUrl != null) return false;
    if (view != null ? !view.equals(enzymeLink.view) : enzymeLink.view != null) return false;
    if (xrefDatabaseConstant != null ? !xrefDatabaseConstant.equals(enzymeLink.xrefDatabaseConstant) : enzymeLink.xrefDatabaseConstant !=
                                                                                                       null)
      return false;

    return true;
  }

  /**
   * Generates a hash code for this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result;
    result = (xrefDatabaseConstant != null ? xrefDatabaseConstant.hashCode() : 0);
    result = 29 * result + (specificUrl != null ? specificUrl.hashCode() : 0);
    result = 29 * result + (accession != null ? accession.hashCode() : 0);
    result = 29 * result + (name != null ? name.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (view != null ? view.hashCode() : 0);
    return result;
  }

  /**
   * Provides a string representation of an <code>EnzymeLink</code>.
   *
   * @return the string representation.
   */
  public String toString() {
    StringBuffer output = new StringBuffer();
    output.append(this.xrefDatabaseConstant);
    output.append(", ");
    output.append(this.specificUrl);
    output.append(", ");
    output.append(this.accession);
    output.append(", ");
    output.append(this.name);
    output.append(", ");
    output.append(this.view.toString());
    output.append(", ");
    output.append(this.source.toString());

    return output.toString();
  }


  // ----------------  GETTER ------------------

  /**
   * Creates a unique name for this link.
   * <p/>
   * This method will be used to populate lists (esp. combo box content).
   *
   * @return the unique name.
   */
  public String getListOrderCriterion() {
    if (xrefDatabaseConstant == XrefDatabaseConstant.SWISSPROT ||
        xrefDatabaseConstant == XrefDatabaseConstant.PROSITE ||
        xrefDatabaseConstant == XrefDatabaseConstant.CAS) {
      return accession;
    }

    if (xrefDatabaseConstant == XrefDatabaseConstant.MEROPS || xrefDatabaseConstant == XrefDatabaseConstant.UMBBD) return specificUrl;

    if (xrefDatabaseConstant == XrefDatabaseConstant.DIAGRAM) return name;

    return xrefDatabaseConstant.getDatabaseCode();
  }

  /**
   * Links with the EC number as identifier need to call this method to get the correct URL.
   *
   * @param ec The EC number.
   * @return the full URL.
   * @throws NullPointerException if <code>ec</code> is <code>null</code>.
   */
  public String getFullUrl(String ec) {
    if (ec == null) throw new NullPointerException();
    if (STATIC_LINKS.keySet().contains(xrefDatabaseConstant)){
    	boolean withAccession = EnzymeCommissionNumber.isPreliminary(ec)
    		&& accession != null && accession.length() > 0;
        return specificUrl + (withAccession? accession : ec);
    }
    if (xrefDatabaseConstant == XrefDatabaseConstant.CSA){
        StringTokenizer st = new StringTokenizer(ec, ".");
        StringBuffer sb = new StringBuffer(xrefDatabaseConstant.getUrl());
        sb.append("ec1=").append(st.nextToken())
            .append("&ec2=").append(st.nextToken())
            .append("&ec3=").append(st.nextToken())
            .append("&ec4=").append(st.nextToken());
        return sb.toString();
    }
    if (xrefDatabaseConstant == XrefDatabaseConstant.PROSITE ||
        xrefDatabaseConstant == XrefDatabaseConstant.SWISSPROT ||
        xrefDatabaseConstant == XrefDatabaseConstant.MIM ||
        xrefDatabaseConstant == XrefDatabaseConstant.PDB ||
        xrefDatabaseConstant == XrefDatabaseConstant.GO ||
        xrefDatabaseConstant == XrefDatabaseConstant.BRENDA ||
        xrefDatabaseConstant == XrefDatabaseConstant.METACYC){
        StringBuffer sb = new StringBuffer(xrefDatabaseConstant.getUrl());
        sb.append(accession);
        return sb.toString();
    }
    if (xrefDatabaseConstant == XrefDatabaseConstant.NC_IUBMB){
    	StringTokenizer st = new StringTokenizer(ec, ".");
        StringBuffer sb = new StringBuffer(xrefDatabaseConstant.getUrl());
    	sb.append(st.nextToken()).append('/')        // ec1
    		.append(st.nextToken()).append('/')      // ec2
    		.append(st.nextToken()).append('/')      // ec3
    		.append(st.nextToken()).append(".html"); // ec4
    	return sb.toString();
    }
    return specificUrl;
  }

  public XrefDatabaseConstant getXrefDatabaseConstant() {
    return xrefDatabaseConstant;
  }

  public String getSpecificUrl() {
    return specificUrl;
  }

  public String getAccession() {
    return accession;
  }

  public String getName() {
    return name;
  }

  public EnzymeViewConstant getView() {
    return view;
  }

  public EnzymeSourceConstant getSource() {
    return source;
  }

    public String getDataComment() {
        return dataComment;
    }

    public void setDataComment(String comment) {
        dataComment = comment;
    }
}
