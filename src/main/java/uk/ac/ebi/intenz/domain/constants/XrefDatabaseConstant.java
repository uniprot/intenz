package uk.ac.ebi.intenz.domain.constants;

import java.util.*;

import uk.ac.ebi.intenz.domain.exceptions.DomainException;

/**
 * This class represents an enumeration of all supported link source types.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2008/07/09 09:49:05 $
 */
public class XrefDatabaseConstant implements Comparable<XrefDatabaseConstant> {
  private String databaseCode;

  private String displayName;

  private String url;

  private boolean isXref;

  public static final XrefDatabaseConstant UNDEF = new XrefDatabaseConstant("UNDEF", "UNDEFINED",
                                                                            "", false);

  /**
   * <a href="http://www.brenda.uni-koeln.de/">BRENDA</a> collects functional
   * data about enzymes. NOTE: this is now considered as xref, as the identifiers
   * we are linking to are their preliminary EC numbers.
   */
  public static final XrefDatabaseConstant BRENDA = new XrefDatabaseConstant(
		  "BRENDA", "BRENDA",
		  "http://www.brenda-enzymes.info/php/result_flat.php4?ecno=", true);

  public static final XrefDatabaseConstant METACYC = new XrefDatabaseConstant(
		  "MCYC", "MetaCyc",
		  "http://biocyc.org/META/NEW-IMAGE?type=REACTION&object=", true);
  
  /**
   * <a href="http://www.cas.org/">CAS</a> is commercial and cannot be linked to.
   */
  public static final XrefDatabaseConstant CAS = new XrefDatabaseConstant("CAS", "CAS", "", false);

  /**
   * <a href="http://ca.expasy.org/enzyme/">ENZYME</a> is the EXPASY version of the NC-IUBMB Nomenclature containing some
   * additional information (cofactors, improved comments and additional links).
   */
  public static final XrefDatabaseConstant ENZYME =
		  new XrefDatabaseConstant("EXPASY", "ENZYME@ExPASy",
				  "http://enzyme.expasy.org/EC/", false);

  /**
   * Gerry Moss' web pages (Nomenclature Comitee of the IUBMB).
   */
  public static final XrefDatabaseConstant NC_IUBMB = new XrefDatabaseConstant("NCIUBMB", "NC-IUBMB",
          "http://www.chem.qmul.ac.uk/iubmb/enzyme/EC", false);

  /**
   * <a href="http://geneontology.org/">GO</a> is the Gene Ontology database.
   */
//  public static final XrefDatabaseConstant GO = new XrefDatabaseConstant("GO", "GO",
//                                                                         "http://www.ebi.ac.uk/ego/GSearch?query=", false);
  public static final XrefDatabaseConstant GO = new XrefDatabaseConstant("GO", "GO",
          "http://www.ebi.ac.uk/ego/DisplayGoTerm?id=", true);

  /**
   * <a href="http://www.ebi.ac.uk/thornton-srv/databases/CSA/">Catalytic Site Atlas</a>
   * provides catalytic residues details.
   */
  public static final XrefDatabaseConstant CSA = new XrefDatabaseConstant("CSA", "CSA",
          "http://www.ebi.ac.uk/thornton-srv/databases/cgi-bin/CSA/CSA_Show_EC_List.pl?", false);

  /**
   * <a href="http://wwwbiotech.nist.gov:8030/enzyme/ename.html">NIST74</a> provides further information about the
   * catalytic activity.
   */
  public static final XrefDatabaseConstant NIST74 = new XrefDatabaseConstant("NIST74", "NIST 74",
                                                                             "http://xpdb.nist.gov/enzyme_thermodynamics/enzyme_compose_query.pl?EC=", false);

  /**
   * <a href="http://www.genome.ad.jp/kegg/kegg2.html">KEGG</a> comprises several databases storing information
   * especially related to enzymes (e.g. LIGAND, REACTION) and genes and genomes in general.
   */
  public static final XrefDatabaseConstant KEGG = new XrefDatabaseConstant("KEGG", "KEGG",
                                                                           "http://www.genome.ad.jp/dbget-bin/www_bget?ec:", false);

  /**
   * <a href="http://merops.sanger.ac.uk/">MEROPS</a> is a resource for peptidases.
   */
  public static final XrefDatabaseConstant MEROPS = new XrefDatabaseConstant("MEROPS", "MEROPS", "", false);

  /**
   * <a href="http://www.ncbi.nlm.nih.gov/omim/">OMIM</a> stores information about human genetic diseases.
   */
  public static final XrefDatabaseConstant MIM = new XrefDatabaseConstant("MIM", "OMIM", "http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=", true);

  /**
   * <a href="http://ca.expasy.org/prosite/">PROSITE</a> is located at the SIB and contains information about protein
   * families and domains.
   */
  public static final XrefDatabaseConstant PROSITE = new XrefDatabaseConstant("PROSITE", "PROSITE",
                                                                              "http://www.expasy.org/prosite/", true);

  /**
   * <a href="http://ca.expasy.org/sprot/">Swiss-Prot</a> is THE protein sequence database.
   */
  public static final XrefDatabaseConstant SWISSPROT = new XrefDatabaseConstant("SWISSPROT", "Swiss-Prot",
                                                                                "http://www.uniprot.org/uniprot/", true);

  /**
   * <a href="http://umbbd.ethz.ch">UM-BBD</a> is a comprehensive resource about enzymes also
   * containing enzymes which haven't got an EC number yet.
   * <p/>
   * UM-BBD links can be static (using EC number) or non-static (using UM-BBD identifier). Therefore a UM-BBD link
   * will not be part of the list of static links.
   */
  public static final XrefDatabaseConstant UMBBD = new XrefDatabaseConstant("UMBBD", "UM-BBD",
                                                                            "http://umbbd.ethz.ch/servlets/pageservlet?ptype=e&ECcode=", false);

  public static final XrefDatabaseConstant DIAGRAM = new XrefDatabaseConstant("DIAGR", "DIAGRAM",
                                                                              "", true);

  /**
   * <a href="http://ca.expasy.org/sprot/">ERGO</a> is a not very reliable enzyme source ... (I couldn't get any
   * information about it because the server was down).
   * @deprecated This website does not work any more (if it ever did).
   */
   @Deprecated
  public static final XrefDatabaseConstant ERGO = new XrefDatabaseConstant("ERGO", "ERGO",
                                                                           "http://www.ergo-light.com/ERGO/CGI/fr.cgi?org=&user=&fr=", false);

  /**
   * <a href="http://ca.expasy.org/sprot/">ERGO</a> formerly WIT.
   */
  public static final XrefDatabaseConstant WIT = new XrefDatabaseConstant("WIT", "WIT",
                                                                          "http://www.ergo-light.com/ERGO/CGI/fr.cgi?org=&user=&fr=", false);

  /**
   * Link to the <a href="http://www.rcsb.org/pdb/index.html">Protein Data Bank (PDB)</a>.
   */
  public static final XrefDatabaseConstant PDB = new XrefDatabaseConstant("PDB", "EC2PDB", "http://www.ebi.ac.uk/thornton-srv/databases/cgi-bin/enzymes/GetPage.pl?ec_number=", false);

  private static XrefDatabaseConstant[] PRIVATE_LINK_SOURCES = {
    UNDEF,
    BRENDA,
    CAS,
    ENZYME,
    NC_IUBMB,
    GO,
    NIST74,
    KEGG,
    MEROPS,
    METACYC,
    MIM,
    PROSITE,
    SWISSPROT,
    UMBBD,
    ERGO,
    WIT,
    DIAGRAM,
    PDB,
    CSA
  };

  private static XrefDatabaseConstant[] PRIVATE_UNIQUE_LINK_SOURCES = {
    BRENDA,
    ENZYME,
    NC_IUBMB,
    GO,
    METACYC,
    NIST74,
    KEGG,
    //MIM,
    UMBBD,
    ERGO,
    WIT
  };

  /**
   * All link sources ordered alphabetically. This set is immutable.
   */
  public static final List<XrefDatabaseConstant> LINK_SOURCE_CONSTANTS =
	  Collections.unmodifiableList(Arrays.asList(PRIVATE_LINK_SOURCES));

  /**
   * This set contains all link which can only occur once in an enzyme entry.
   */
  public static final Set<XrefDatabaseConstant> UNIQUE_LINK_SOURCES =
	  Collections.unmodifiableSet(new HashSet<XrefDatabaseConstant>(Arrays.asList(PRIVATE_UNIQUE_LINK_SOURCES)));

  /**
   * Object cannot be created outside this class.
   *
   * @param sourceCode  The line type code.
   * @param displayName The full name of this source.
   * @param url         The URL of this link source (if available).
   */
  private XrefDatabaseConstant(String sourceCode, String displayName, String url, boolean isXref) {
    this.databaseCode = sourceCode;
    this.displayName = displayName;
    this.url = url;
    this.isXref = isXref;
  }

  public static XrefDatabaseConstant valueOf(String sourceCode) throws DomainException {
    if (sourceCode == null) throw new NullPointerException("Parameter 'databaseCode' must not be null.");
    if (sourceCode.equals("")) throw new IllegalArgumentException("Parameter 'databaseCode' must not be empty.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant xrefDatabaseConstant = it.next();
      if (xrefDatabaseConstant.getDatabaseCode().equals(sourceCode)) return xrefDatabaseConstant;
    }
    throw new DomainException("org.apache.struts.action.GLOBAL_MESSAGE", "errors.application.xrefs.source_code");
  }

  public int compareTo(XrefDatabaseConstant linkSourceConstant) {
    return this.getDatabaseCode().compareTo(linkSourceConstant.getDatabaseCode());
  }

  /**
   * Checks if the given link source code exists in this enumeration.
   *
   * @param sourceCode The source code of the link.
   * @return <code>true</code> if the source code exists.
   * @throws NullPointerException if <code>databaseCode</code> is <code>null</code>.
   */
  public static boolean contains(String sourceCode) {
    if (sourceCode == null) throw new NullPointerException("Parameter 'databaseCode' must not be null.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant sourceConstant = it.next();
      if (sourceCode.equals(sourceConstant.getDatabaseCode())) return true;
    }
    return false;
  }

  public static boolean isUniqueXrefDatabaseName(String databaseName) {
    if (databaseName == null) throw new NullPointerException("Parameter 'databaseName' must not be null.");
    String databaseCode = getDatabaseCodeOf(databaseName);
    return isUniqueXrefDatabaseCode(databaseCode);
  }

  public static boolean isUniqueXrefDatabaseCode(String databaseCode) {
    if (databaseCode == null) throw new NullPointerException("Parameter 'databaseCode' must not be null.");
    if (!XrefDatabaseConstant.contains(databaseCode)) throw new IllegalArgumentException("The databaseCode paramter is invalid.");
    for (Iterator<XrefDatabaseConstant> it = UNIQUE_LINK_SOURCES.iterator(); it.hasNext();) {
      XrefDatabaseConstant xrefDatabaseConstant = it.next();
      if (xrefDatabaseConstant.databaseCode.equals(databaseCode)) return true;
    }
    return false;
  }

  /**
   * Returns <code>true</code> if this link belongs to the XREFS table.
   * <p/>
   * If the source code is not a valid source code an exception is thrown (see below).
   *
   * @param sourceCode The source code of this link constant.
   * @return <code>true</code> if this link belongs to the XREFS table.
   * @throws NullPointerException     if <code>databaseCode</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>databaseCode</code> is invalid.
   */
  public static boolean isXref(String sourceCode) {
    if (sourceCode == null) throw new NullPointerException();
    if (!XrefDatabaseConstant.contains(sourceCode)) throw new IllegalArgumentException("The databaseCode paramter is invalid.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant sourceConstant = it.next();
      if (sourceCode.equals(sourceConstant.getDatabaseCode())) return sourceConstant.isXref();
    }
    return false;
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof XrefDatabaseConstant)) return false;

    final XrefDatabaseConstant xrefDatabaseConstant = (XrefDatabaseConstant) o;

    if (databaseCode != null ? !databaseCode.equals(xrefDatabaseConstant.databaseCode) : xrefDatabaseConstant.databaseCode !=
                                                                                         null) return false;
    if (url != null ? !url.equals(xrefDatabaseConstant.url) : xrefDatabaseConstant.url != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (databaseCode != null ? databaseCode.hashCode() : 0);
    result = 29 * result + (url != null ? url.hashCode() : 0);
    return result;
  }

  /**
   * Returns the display name.
   *
   * @return the display name.
   */
  public String toString() {
    return displayName;
  }


  // ----------------  GETTER ------------------

  /**
   * Returns the URL of the given source code.
   * <p/>
   * If the source code is not a valid source code an exception is thrown (see below).
   *
   * @param sourceCode The source code of this link constant.
   * @return the URL.
   * @throws NullPointerException     if <code>databaseCode</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>databaseCode</code> is invalid.
   */
  public static String getUrlOf(String sourceCode) {
    if (sourceCode == null) throw new NullPointerException("Parameter 'databaseCode' must not be null.");
    if (!XrefDatabaseConstant.contains(sourceCode)) throw new IllegalArgumentException("The 'databaseCode' paramter is invalid.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant sourceConstant = it.next();
      if (sourceCode.equals(sourceConstant.getDatabaseCode())) return sourceConstant.getUrl();
    }
    return "";
  }

  /**
   * Returns the display name of the source defined by <code>databaseCode</code>.
   *
   * @param sourceCode The source's code.
   * @return the display name.
   * @throws NullPointerException if <code>databaseCode</code> is <code>null</code>.
   */
  public static String getDisplayNameOf(String sourceCode) {
    if (sourceCode == null) throw new NullPointerException("Parameter 'databaseCode' must not be null.");
    if (!XrefDatabaseConstant.contains(sourceCode)) throw new IllegalArgumentException("The 'databaseCode' paramter is invalid.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant sourceConstant = it.next();
      if (sourceCode.equals(sourceConstant.getDatabaseCode())) return sourceConstant.getDisplayName();
    }
    return "";
  }

  public static String getDatabaseCodeOf(String databaseName) {
    if (databaseName == null) throw new NullPointerException("Parameter 'databaseName' must not be null.");
    for (Iterator<XrefDatabaseConstant> it = LINK_SOURCE_CONSTANTS.iterator(); it.hasNext();) {
      XrefDatabaseConstant sourceConstant = it.next();
      if (databaseName.equals(sourceConstant.getDisplayName())) return sourceConstant.getDatabaseCode();
    }
    throw new IllegalArgumentException("The 'databaseName' parameter is invalid:" + databaseName);
  }

  public String getDatabaseCode() {
    return databaseCode;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getUrl() {
    return url;
  }

  public boolean isXref() {
    return isXref;
  }

}


