package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;

/**
 * Maps link information to the corresponding database tables.
 * <p/>
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:23:11 $
 */
public class EnzymeLinkMapper {

  private static final String COLUMNS =
    "enzyme_id, url, display_name, status, source, web_view, data_comment";
  private static final String TABLES = "links";

  private static final String XREF_COLUMNS =
    "enzyme_id, database_code, database_ac, name, status, source, web_view, data_comment";
  private static final String XREF_TABLES = "xrefs";

//  private static final Logger LOGGER = Logger.getLogger(EnzymeLinkMapper.class);

  public EnzymeLinkMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM " + TABLES +
           " WHERE links.enzyme_id = ?" +
           " ORDER BY links.display_name";
  }

  private String exportSibLinksStatement() {
    return "SELECT " + COLUMNS +
           " FROM " + TABLES +
           " WHERE links.enzyme_id = ? AND (links.web_view = ? OR links.web_view = ? OR links.web_view = ? OR links.web_view = ?)" +
           " ORDER BY links.display_name";
  }

  private String findXrefsStatement() {
    return "SELECT " + XREF_COLUMNS +
           " FROM " + XREF_TABLES +
           " WHERE xrefs.enzyme_id = ?" +
           " ORDER BY xrefs.name";
  }

  private String findSibXrefsStatement() {
    return "SELECT " + XREF_COLUMNS +
           " FROM " + XREF_TABLES +
           " WHERE xrefs.enzyme_id = ? AND (xrefs.web_view = ? OR xrefs.web_view = ? OR xrefs.web_view = ? OR xrefs.web_view = ?)" +
           " ORDER BY name";
  }

  private String insertStatement() {
    return "INSERT INTO links (enzyme_id, url, display_name, status, source, web_view, data_comment) VALUES (?, ?, ?, ?, ?, ?, ?)";
  }

  private String insertXrefStatement() {
    return "INSERT INTO xrefs (enzyme_id, database_code, database_ac, name, status, source, web_view, data_comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  }

  private String updateLinkUrlStatement() {
    return "UPDATE links SET url = ?, status = ? WHERE enzyme_id = ? AND display_name = ?";
  }

  private String deleteStatement() {
    return "DELETE links WHERE enzyme_id = ? AND display_name = ? AND url = ?";
  }

  private String deleteAllStatement() {
    return "DELETE links WHERE enzyme_id = ?";
  }

  private String deleteXrefStatement() {
    return "DELETE xrefs WHERE enzyme_id = ? AND database_code = ? AND database_ac = ? AND name = ?";
  }

  private String deleteAllXrefStatement() {
    return "DELETE xrefs WHERE enzyme_id = ?";
  }

   private String deleteByCodeXrefStatement() {
    return "DELETE xrefs WHERE enzyme_id = ? AND database_code = ?";
  }

  private String deleteByNameStatement() {
    return "DELETE links WHERE enzyme_id = ? AND display_name = ?";
  }

  /**
   * Tries to find link information about an enzyme in tables LINKS and XREFS.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>Vector</code> containing <code>EnzymeLink</code>instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeLink> find(Long enzymeId, Connection con) throws SQLException, DomainException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeLink> result = new ArrayList<EnzymeLink>();

    try {
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        EnzymeLink link = doLoad(rs);
        if (link == null) continue;
        result.add(link);
      }
      List<EnzymeLink> xrefs = findXrefs(enzymeId, con);
      if (xrefs != null) result.addAll(xrefs);
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (result.size() == 0) return null;
    return result;
  }

  /**
   * Exports all links which are displayed in the ENZYME view.
   *
   * Affected table rows will be locked.
   *
   * @param enzymeId The enzyme ID used to retreive the related links.
   * @param con The database connection.
   * @return an {@link java.util.ArrayList} of links or <code>null</code> if no link could be found.
   * @throws SQLException if a database error occured.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public List<EnzymeLink> exportSibLinks(Long enzymeId, Connection con) throws SQLException, DomainException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeLink> result = new ArrayList<EnzymeLink>();

    try {
      findStatement = con.prepareStatement(exportSibLinksStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeViewConstant.INTENZ.toString());
      findStatement.setString(3, EnzymeViewConstant.IUBMB_SIB.toString());
      findStatement.setString(4, EnzymeViewConstant.SIB.toString());
      findStatement.setString(5, EnzymeViewConstant.SIB_INTENZ.toString());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        EnzymeLink link = doLoad(rs);
        if (link == null) continue;
        result.add(link);
      }
      List<EnzymeLink> xrefs = findSibXrefs(enzymeId, con);
      if (xrefs != null) result.addAll(xrefs);
    } finally {
    	if (rs != null) rs.close();
      if (findStatement != null) findStatement.close();
    }

    if (result.size() == 0) return null;
    return result;
  }

  /**
   * Tries to find SwissProt xref information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>Vector</code> containing <code>EnzymeLink</code>instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<EnzymeLink> findXrefs(Long enzymeId, Connection con) throws SQLException, DomainException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeLink> result = new ArrayList<EnzymeLink>();
    boolean noResult = true;

    try {
      // Core information.
      findStatement = con.prepareStatement(findXrefsStatement());
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        noResult = false;
        EnzymeLink link = doLoadXref(rs);
        if (link == null) continue;
        result.add(link);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Finds all xrefs which are displayed in the ENZYME view.
   *
   * @param enzymeId The enzyme ID used to retreive the related xrefs.
   * @param con The database connection.
   * @return an {@link java.util.ArrayList} of xrefs or <code>null</code> if no xref could be found.
   * @throws SQLException if a database error occured.
   * @throws NullPointerException if either of the parameters is <code>null</code>.
   */
  public List<EnzymeLink> findSibXrefs(Long enzymeId, Connection con) throws SQLException, DomainException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<EnzymeLink> result = new ArrayList<EnzymeLink>();
    boolean noResult = true;

    try {
      // Core information.
      findStatement = con.prepareStatement(findSibXrefsStatement());
      findStatement.setLong(1, enzymeId.longValue());
      findStatement.setString(2, EnzymeViewConstant.INTENZ.toString());
      findStatement.setString(3, EnzymeViewConstant.IUBMB_SIB.toString());
      findStatement.setString(4, EnzymeViewConstant.SIB.toString());
      findStatement.setString(5, EnzymeViewConstant.SIB_INTENZ.toString());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        noResult = false;
        EnzymeLink link = doLoadXref(rs);
        if (link == null) continue;
        result.add(link);
      }
    } finally {
    	if (rs != null) rs.close();
        if (findStatement != null) findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  /**
   * Stores the given list of links into the database.
   *
   * @param links    The vector of links.
   * @param enzymeId The enzyme ID.
   * @param status   ...
   * @param con      ...
   * @throws SQLException
   */
  public void insert(List<EnzymeLink> links, Long enzymeId, Status status, Connection con)
  throws SQLException {
    if (links == null) throw new NullPointerException("Parameter 'links' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertStatement = null, insertXrefStatement = null;

    try {
      insertStatement = con.prepareStatement(insertStatement());
      insertXrefStatement = con.prepareStatement(insertXrefStatement());
      for (int iii = 0; iii < links.size(); iii++) {
        EnzymeLink link = links.get(iii);
        if (link.getXrefDatabaseConstant().isXref()) {
          doInsertXref(link, enzymeId, status, insertXrefStatement);
          insertXrefStatement.addBatch();;
        } else {
          doInsert(link, enzymeId, status, insertStatement);
          insertStatement.addBatch();
        }
      }
      insertStatement.executeBatch();
      insertXrefStatement.executeBatch();
    } finally {
      if (insertStatement != null) insertStatement.close();
      if (insertXrefStatement != null) insertXrefStatement.close();
    }
  }

  public void insertLink(EnzymeLink link, Long enzymeId, Status status, Connection con) throws SQLException {
    if (link == null) throw new NullPointerException("Parameter 'link' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertStatement = null;
    try {
      insertStatement = con.prepareStatement(insertStatement());
      doInsert(link, enzymeId, status, insertStatement);
      insertStatement.execute();
    } finally {
      if (insertStatement != null) insertStatement.close();
    }
  }

  public void updateLinkUrl(EnzymeLink link, Long enzymeId, Status status, Connection con) throws SQLException {
    if (link == null) throw new NullPointerException("Parameter 'link' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement updateStatement = null;
    try {
      updateStatement = con.prepareStatement(updateLinkUrlStatement());
      updateStatement.setString(1, link.getSpecificUrl());
      updateStatement.setString(2, status.getCode());
      updateStatement.setLong(3, enzymeId.longValue());
      if (link.getXrefDatabaseConstant().equals("NIST74"))
        updateStatement.setString(4, "GTD"); // TODO: Change to NIST 74 completely.
      else
        updateStatement.setString(4, link.getXrefDatabaseConstant().toString());
      updateStatement.execute();
    } finally {
      if (updateStatement != null) updateStatement.close();
    }
  }

  public void reloadLinks(List<EnzymeLink> links, Long enzymeId, Status status, Connection con) throws SQLException {
    if (links == null) throw new NullPointerException("Parameter 'links' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    deleteOtherLinks(links, enzymeId, con);
    insert(links, enzymeId, status, con);
  }

  public void delete(Long enzymeId, EnzymeLink link, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (link == null) throw new NullPointerException("Parameter 'link' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteStatement = null;
    try {
      deleteStatement = con.prepareStatement(deleteStatement());
      deleteStatement.setLong(1, enzymeId.longValue());
      if (link.getXrefDatabaseConstant().equals("NIST74"))
        deleteStatement.setString(2, "GTD");
      else
        deleteStatement.setString(2, link.getXrefDatabaseConstant().toString());
      deleteStatement.setString(3, link.getSpecificUrl());
      deleteStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteStatement != null) deleteStatement.close();
    }
  }

  public void deleteAll(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteStatement = null;
    try {
      deleteStatement = con.prepareStatement(deleteAllStatement());
      deleteStatement.setLong(1, enzymeId.longValue());
      deleteStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteStatement != null) deleteStatement.close();
    }
  }

  public void deleteXref(Long enzymeId, EnzymeLink xref, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (xref == null) throw new NullPointerException("Parameter 'xref' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteXrefStatement = null;
    String dbCode = xref.getXrefDatabaseConstant().toString();

    if (dbCode.equals("Swiss-Prot")) dbCode = "S";
    if (dbCode.equals("PROSITE")) dbCode = "P";

    try {
      deleteXrefStatement = con.prepareStatement(deleteXrefStatement());
      deleteXrefStatement.setLong(1, enzymeId.longValue());
      deleteXrefStatement.setString(2, dbCode);
      deleteXrefStatement.setString(3, xref.getAccession());
      deleteXrefStatement.setString(4, xref.getName());
      deleteXrefStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteXrefStatement != null) deleteXrefStatement.close();
    }
  }

  public void deleteAllXref(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteXrefStatement = null;
    try {
      deleteXrefStatement = con.prepareStatement(deleteAllXrefStatement());
      deleteXrefStatement.setLong(1, enzymeId.longValue());
      deleteXrefStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteXrefStatement != null) deleteXrefStatement.close();
    }
  }

  public void deleteByName(Long enzymeId, String displayName, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (displayName == null) throw new NullPointerException("Parameter 'displayName' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteByNameStatement = null;
    try {
      deleteByNameStatement = con.prepareStatement(deleteByNameStatement());
      deleteByNameStatement.setLong(1, enzymeId.longValue());
      deleteByNameStatement.setString(2, displayName);
      deleteByNameStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (deleteByNameStatement != null) deleteByNameStatement.close();
    }
  }

   public void deleteByCodeXref(Long enzymeId, String xrefCode, Connection con) throws SQLException {
       if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
       if (xrefCode == null) throw new NullPointerException("Parameter 'xrefCode' must not be null.");
       if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

       PreparedStatement deleteByNameStatement = null;
       if (xrefCode.equals(XrefDatabaseConstant.SWISSPROT.getDatabaseCode())) xrefCode = "S";
       if (xrefCode.equals(XrefDatabaseConstant.PROSITE.getDatabaseCode())) xrefCode = "P";

       try {
         deleteByNameStatement = con.prepareStatement(deleteByCodeXrefStatement());
         deleteByNameStatement.setLong(1, enzymeId.longValue());
         deleteByNameStatement.setString(2, xrefCode);
         deleteByNameStatement.execute();
//         con.commit();
//       } catch (SQLException e) {
//         con.rollback();
//         throw e;
       } finally {
         if (deleteByNameStatement != null) deleteByNameStatement.close();
       }
     }


  public void deleteOtherLinks(List<EnzymeLink> links, Long enzymeId, Connection con) throws SQLException {
    if (links == null) throw new NullPointerException("Parameter 'links' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    deleteAll(enzymeId, con);
    deleteAllXref(enzymeId, con);

  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>EnzymeLink</code> object from the given result set.
   * <p/>
   *
   * @param rs The result set object.
   * @return an <code>EnzymeLink</code> instance.
   * @throws SQLException
   */
  private EnzymeLink doLoad(ResultSet rs) throws SQLException, DomainException {
    assert rs != null : "Parameter 'rs' must not be null.";

    EnzymeLink result = null;

    String displayName = "";
    String url = "";
    String source = "";
    String webView = "";
    String dataComment = null;

    if (rs.getString("display_name") != null) displayName = rs.getString("display_name");
    if (rs.getString("url") != null) url = rs.getString("url");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("web_view") != null) webView = rs.getString("web_view");
    if (rs.getString("data_comment") != null) dataComment = rs.getString("data_comment");

    if (displayName.equals("UM-BBD")) displayName = "UMBBD";
    if (displayName.equals("NIST 74")) displayName = "NIST74";

        // Check for static links.
    if (displayName.equals(EnzymeLink.BRENDA.getXrefDatabaseConstant().getDatabaseCode())){
        result = EnzymeLink.BRENDA;
    } else if (displayName.equals(EnzymeLink.KEGG.getXrefDatabaseConstant().getDatabaseCode())){
        result = EnzymeLink.KEGG;
    } else if (displayName.equals(EnzymeLink.EXPASY.getXrefDatabaseConstant().getDatabaseCode())){
        return null;//return EnzymeLink.EXPASY; // These links are not necessary anymore.
    } else if (displayName.equals(EnzymeLink.GO.getXrefDatabaseConstant().getDatabaseCode())){
        result = EnzymeLink.GO;
    } else if (displayName.equals(EnzymeLink.NIST74.getXrefDatabaseConstant().getDisplayName()) ||
            displayName.equals("GTD")){
        result = EnzymeLink.NIST74;
    } else if (displayName.equals(EnzymeLink.ERGO.getXrefDatabaseConstant().getDatabaseCode()) ||
            displayName.equals(EnzymeLink.WIT.getXrefDatabaseConstant().getDatabaseCode())) {
        result = EnzymeLink.ERGO;
    } else if (displayName.equals(XrefDatabaseConstant.CAS.getDatabaseCode())) {
        if(url.trim().equals("")) return null;
        result = EnzymeLink.valueOf(XrefDatabaseConstant.CAS, "", url, "",
        		EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(webView), dataComment);
    }

    if (result == null){ // not a static link
        result = EnzymeLink.valueOf(XrefDatabaseConstant.valueOf(displayName), url, "", "",
                EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(webView), dataComment);    
    } else if (dataComment != null){ // static link with a comment!
        result = EnzymeLink.valueOf(XrefDatabaseConstant.valueOf(displayName), result.getSpecificUrl(),
                result.getAccession(), result.getName(), result.getSource(), result.getView(), dataComment);
    }

    return result;
  }

  /**
   * Creates the <code>EnzymeLink</code> object from the given xref result set.
   * <p/>
   *
   * @param rs The result set object.
   * @return an <code>EnzymeLink</code> instance.
   * @throws SQLException
   */
  private EnzymeLink doLoadXref(ResultSet rs) throws SQLException, DomainException {
    assert rs != null : "Parameter 'rs' must not be null.";

    String databaseCode = "";
    String accession = "";
    String name = "";
    String source = "";
    String webView = "";
    String dataComment = null;

    if (rs.getString("database_code") != null) databaseCode = rs.getString("database_code");
    if (rs.getString("database_ac") != null) accession = rs.getString("database_ac");
    if (rs.getString("name") != null) name = rs.getString("name");
    if (rs.getString("source") != null) source = rs.getString("source");
    if (rs.getString("web_view") != null) webView = rs.getString("web_view");
    if (rs.getString("data_comment") != null) dataComment = rs.getString("data_comment");

    if (databaseCode.equals("S")) databaseCode = "SWISSPROT";
    if (databaseCode.equals("P")) databaseCode = "PROSITE";
    if (databaseCode.equals("DIAGR")) {
      databaseCode = "DIAGRAM";
      return EnzymeLink.valueOf(XrefDatabaseConstant.DIAGRAM, accession, "", name,
    		  EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(webView), dataComment); // Accession is the URL for DIAGRAMS.
    }

    return EnzymeLink.valueOf(XrefDatabaseConstant.valueOf(databaseCode), "", accession, name,
        EnzymeSourceConstant.valueOf(source), EnzymeViewConstant.valueOf(webView), dataComment);
  }

  /**
   * Sets the parameters of the prepared statement.
   *
   * @param link            ...
   * @param enzymeId        ...
   * @param status          ...
   * @param insertStatement ...
   * @throws SQLException
   */
  private void doInsert(EnzymeLink link, Long enzymeId, Status status,
                        PreparedStatement insertStatement) throws SQLException {
    assert link != null : "Parameter 'link' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert insertStatement != null : "Parameter 'insertStatement' must not be null.";

    insertStatement.setLong(1, enzymeId.longValue());
    if (link.getXrefDatabaseConstant() == XrefDatabaseConstant.CAS) {
      insertStatement.setString(2, link.getAccession());
    } else {
      insertStatement.setString(2, link.getSpecificUrl());
    }
    if (link.getXrefDatabaseConstant().equals("NIST74")) // TODO: Change to NIST 74 completely.
      insertStatement.setString(3, "GTD");
    else
      insertStatement.setString(3, link.getXrefDatabaseConstant().toString());
    insertStatement.setString(4, status.getCode());
    insertStatement.setString(5, link.getSource().toString());
    insertStatement.setString(6, link.getView().toString());
    if (link.getDataComment() == null || link.getDataComment().equals("")){
        insertStatement.setNull(7, java.sql.Types.VARCHAR);
    } else {
        insertStatement.setString(7, link.getDataComment());
    }
  }

  private void doInsertXref(EnzymeLink xref, Long enzymeId, Status status,
                            PreparedStatement insertXrefStatement) throws SQLException {
    assert xref != null : "Parameter 'xref' must not be null.";
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert status != null : "Parameter 'status' must not be null.";
    assert insertXrefStatement != null : "Parameter 'insertXrefStatement' must not be null.";
    
    String dbCode = xref.getXrefDatabaseConstant().getDatabaseCode();
    if (dbCode.equals("SWISSPROT")) dbCode = "S";
    if (dbCode.equals("PROSITE")) dbCode = "P";
//    if (dbCode.equals("DIAGRAM")) dbCode = "DIAGR";
    insertXrefStatement.setLong(1, enzymeId.longValue());
    insertXrefStatement.setString(2, dbCode);
    if (dbCode.equals("DIAGR"))
      insertXrefStatement.setString(3, xref.getSpecificUrl());
    else
      insertXrefStatement.setString(3, xref.getAccession());
    insertXrefStatement.setString(4, xref.getName());
    insertXrefStatement.setString(5, status.getCode());
    insertXrefStatement.setString(6, xref.getSource().toString());
    insertXrefStatement.setString(7, xref.getView().toString());
    if (xref.getDataComment() == null || xref.getDataComment().equals("")){
        insertXrefStatement.setNull(8, java.sql.Types.VARCHAR);
    } else {
        insertXrefStatement.setString(8, xref.getDataComment());
    }
  }
}
