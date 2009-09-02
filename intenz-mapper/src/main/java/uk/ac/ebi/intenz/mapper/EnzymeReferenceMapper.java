package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;

/**
 * Maps reference information to the corresponding database tables.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/26 14:59:09 $
 */
public class EnzymeReferenceMapper {

  private static final String COLUMNS = "p.pub_id, p.medline_id, p.pubmed_id, p.pub_type, p.author, " +
                                        "p.pub_year, p.title, p.journal_book, p.volume, p.first_page, p.last_page, " +
                                        "p.edition, p.editor, p.pub_company, p.pub_place, p.web_view, p.source";

//  private static final Logger LOGGER = Logger.getLogger(EnzymeReferenceMapper.class);

  public EnzymeReferenceMapper() {
  }

  private String findStatement() {
    return "SELECT " + COLUMNS +
           " FROM citations c, publications p" +
           " WHERE c.enzyme_id = ? AND c.pub_id = p.pub_id ORDER BY c.order_in";
  }

  private String findPubIdsStatement() {
    return "SELECT pub_id FROM citations WHERE enzyme_id = ?";
  }

  private String findNextPubIdStatement() {
    return "SELECT s_pub_id.nextval FROM DUAL";
  }

  private String findNextOrderInStatement() {
    return "SELECT MAX(order_in) FROM citations WHERE enzyme_id = ?";
  }

  private String insertJournalStatement() {
    return "INSERT INTO publications " +
           "(pub_id, medline_id, pubmed_id, pub_type, author, pub_year, title, journal_book, volume, first_page, last_page, web_view, source) " +
           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  }

  private String updateJournalStatement() {
    return "UPDATE publications " +
           "SET medline_id = ?, pubmed_id = ?, pub_type = ?, author = ?, pub_year = ?, title = ?, journal_book = ?, " +
           "volume = ?, first_page = ?, last_page = ?, web_view = ?, source = ? " +
           "WHERE pub_id = ?";
  }

  private String insertBookStatement() {
    return "INSERT INTO publications " +
           "(pub_id, pub_type, author, pub_year, title, journal_book, volume, first_page, last_page, edition, editor, pub_company, pub_place, web_view, source) " +
           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  }

  private String updateBookStatement() {
    return "UPDATE publications " +
           "SET pub_type = ?, author = ?, pub_year = ?, title = ?, journal_book = ?, volume = ?, first_page = ?, " +
           "last_page = ?, edition = ?, editor = ?, pub_company = ?, pub_place = ?, web_view = ?, source = ? " +
           "WHERE pub_id = ?";
  }

  private String insertPatentStatement() {
    return "INSERT INTO publications " +
           "(pub_id, pub_type, author, pub_year, title, journal_book, web_view, source) " +
           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
  }

  private String updatePatentStatement() {
    return "UPDATE publications " +
           "SET pub_type = ?, author = ?, pub_year = ?, title = ?, journal_book = ?, web_view = ?, source = ? " +
           "WHERE pub_id = ?";
  }

  private String insertCitationStatement() {
    return "INSERT INTO citations (enzyme_id, pub_id, order_in, status, source) VALUES (?, ?, ?, ?, ?)";
  }

  private String deleteCitationStatement() {
    return "DELETE citations WHERE enzyme_id = ? AND order_in = ?";
  }

  private String deletePublicationStatement() {
    return "DELETE publications WHERE pub_id = ?";
  }

  private String deleteCitationsStatement() {
    return "DELETE citations WHERE enzyme_id = ?";
  }

  private String deleteCitationByPubIdStatement() {
    return "DELETE citations WHERE enzyme_id = ? AND pub_id = ?";
  }

  /**
   * Tries to find comment information about an enzyme.
   *
   * @param enzymeId Enzyme ID of entry.
   * @param con      The logical connection.
   * @return a <code>Vector</code> containing <code>Reference</code>instances or <code>null</code> if nothing has been found.
   * @throws SQLException
   */
  public List<Reference> find(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findStatement = null;
    ResultSet rs = null;
    List<Reference> result = new ArrayList<Reference>();
    boolean noResult = true;
    try {
      // Core information.
      findStatement = con.prepareStatement(findStatement());
      findStatement.setLong(1, enzymeId.longValue());
      rs = findStatement.executeQuery();
      while (rs.next()) {
        noResult = false;
        Reference reference = doLoad(rs);
        if (reference == null) continue;
        result.add(reference);
      }
    } finally {
    	if (rs != null) rs.close();
      findStatement.close();
    }

    if (noResult) return null;
    return result;
  }

  public List<Long> findPubIds(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement findPubIdsStatement = null;
    ResultSet rs = null;
    List<Long> result = new ArrayList<Long>();
    try {
      // Core information.
      findPubIdsStatement = con.prepareStatement(findPubIdsStatement());
      findPubIdsStatement.setLong(1, enzymeId.longValue());
      rs = findPubIdsStatement.executeQuery();
      while (rs.next()) {
        result.add(new Long(rs.getLong(1)));
      }
    } finally {
    	if (rs != null) rs.close();
      findPubIdsStatement.close();
    }

    return result;
  }

  public void insert(Reference reference, Long enzymeId, EnzymeStatusConstant status, Connection con) throws SQLException {
    if (reference == null) throw new NullPointerException("Parameter 'reference' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement insertJournalStatement = null, insertBookStatement = null, insertPatentStatement = null,
            insertCitationStatement = null, deleteCitationStatement = null;

    try {
      insertCitationStatement = con.prepareStatement(insertCitationStatement());
      deleteCitationStatement = con.prepareStatement(deleteCitationStatement());
      insertJournalStatement = con.prepareStatement(insertJournalStatement());
      insertBookStatement = con.prepareStatement(insertBookStatement());
      insertPatentStatement = con.prepareStatement(insertPatentStatement());
      int orderIn = findNextOrderIn(enzymeId, con);

      Long oldPubId = reference.getPubId();
      // Give it a new Id only when it has none yet:
      Long newPubId = (oldPubId == null)? findNextPubId(con) : oldPubId;

      doInsertCitation(enzymeId, newPubId, orderIn, status, reference.getSource(), insertCitationStatement);
      insertCitationStatement.execute();
//      con.commit();
      orderIn++;

      if (reference instanceof Journal) {
        Journal journal = (Journal) reference;
        doInsertJournal(newPubId, journal, insertJournalStatement);
        insertJournalStatement.execute();
//        con.commit();
      }
      if (reference instanceof Book) {
        Book book = (Book) reference;
        doInsertBook(newPubId, book, insertBookStatement);
        insertBookStatement.execute();
//        con.commit();
      }
      if (reference instanceof Patent) {
        Patent patent = (Patent) reference;
        doInsertPatent(newPubId, patent, insertPatentStatement);
        insertPatentStatement.execute();
//        con.commit();
      }
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (insertJournalStatement != null) insertJournalStatement.close();
      if (insertBookStatement != null) insertBookStatement.close();
      if (insertPatentStatement != null) insertPatentStatement.close();
      if (insertCitationStatement != null) insertCitationStatement.close();
      if (deleteCitationStatement != null) deleteCitationStatement.close();
    }
  }

  /**
   * Stores the given list of references into the database.
   *
   * @param references The enzyme's references.
   * @param enzymeId   The enzyme ID.
   * @param status     ...
   * @param con        ...
   * @throws SQLException
   */
  public void insert(List<Reference> references, Long enzymeId, EnzymeStatusConstant status, Connection con)
  throws SQLException {
    if (references == null) throw new NullPointerException("Parameter 'references' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    for (int iii = 0; iii < references.size(); iii++) {
      Reference reference = references.get(iii);
      insert(reference, enzymeId, status, con);
    }
  }

  public void reload(List<Reference> references, Long enzymeId, EnzymeStatusConstant status, Connection con)
  throws SQLException {
    deleteAll(enzymeId, con);
    insert(references, enzymeId, status, con);
  }

  public void update(Reference reference, Long enzymeId, EnzymeStatusConstant status, Connection con) throws SQLException {
    if (reference == null) throw new NullPointerException("Parameter 'reference' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement updateJournalStatement = null, updateBookStatement = null, updatePatentStatement = null;
    try {
      updateJournalStatement = con.prepareStatement(updateJournalStatement());
      updateBookStatement = con.prepareStatement(updateBookStatement());
      updatePatentStatement = con.prepareStatement(updatePatentStatement());

      if (reference instanceof Journal) {
        Journal journal = (Journal) reference;
        doUpdateJournal(journal, updateJournalStatement);
        updateJournalStatement.execute();
//        con.commit();
      }
      if (reference instanceof Book) {
        Book book = (Book) reference;
        doUpdateBook(book, updateBookStatement);
        updateBookStatement.execute();
//        con.commit();
      }
      if (reference instanceof Patent) {
        Patent patent = (Patent) reference;
        doUpdatePatent(patent, updatePatentStatement);
        updatePatentStatement.execute();
//        con.commit();
      }
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      if (updateJournalStatement != null) updateJournalStatement.close();
      if (updateBookStatement != null) updateBookStatement.close();
      if (updatePatentStatement != null) updatePatentStatement.close();
    }
  }

  public void update(List<Reference> references, Long enzymeId, EnzymeStatusConstant status, Connection con)
  throws SQLException {
    if (references == null) throw new NullPointerException("Parameter 'references' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (status == null) throw new NullPointerException("Parameter 'status' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    for (int iii = 0; iii < references.size(); iii++) {
      Reference reference = references.get(iii);
      update(reference, enzymeId, status, con);
    }
  }

  public void deleteAll(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    // Only delete publications, which will be inserted afterwards (deletion of references only requires the deletion
    // of the citation).
    List<Long> pubIds = findPubIds(enzymeId, con);
    for (int iii = 0; iii < pubIds.size(); iii++) {
      deletePublication(pubIds.get(iii), con);
    }
    deleteCitations(enzymeId, con);
  }

  public void deleteCitations(Long enzymeId, Connection con) throws SQLException {
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteCitationsStatement = null;
    try {
      deleteCitationsStatement = con.prepareStatement(deleteCitationsStatement());
      deleteCitationsStatement.setLong(1, enzymeId.longValue());
      deleteCitationsStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      deleteCitationsStatement.close();
    }
  }

  /**
   * Deletes enzyme relations to publications (internally called citations).
   *
   * @param pubIds   The pub ID of the relation to be deleted.
   * @param enzymeId The enzyme ID.
   * @param con      Database connection.
   * @throws SQLException             if any database error occurred.
   * @throws NullPointerException     if any of the parameters is <code>null</code>.
   * @throws IllegalArgumentException if <code>enzymeId</code> < 1.
   */
  public void deleteCitations(List<Long> pubIds, Long enzymeId, Connection con) throws SQLException {
    if (pubIds == null) throw new NullPointerException("Parameter 'pubIds' must not be null.");
    if (enzymeId == null) throw new NullPointerException("Parameter 'enzymeId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deleteCitationStatement = null;
    try {
      for (int iii = 0; iii < pubIds.size(); iii++) {
        Long pubId = pubIds.get(iii);
        deleteCitationStatement = con.prepareStatement(deleteCitationByPubIdStatement());
        deleteCitationStatement.setLong(1, enzymeId.longValue());
        deleteCitationStatement.setLong(2, pubId.longValue());
        deleteCitationStatement.execute();
      }
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      deleteCitationStatement.close();
    }
  }

  public void deletePublication(Long pubId, Connection con) throws SQLException {
    if (pubId == null) throw new NullPointerException("Parameter 'pubId' must not be null.");
    if (con == null) throw new NullPointerException("Parameter 'con' must not be null.");

    PreparedStatement deletePublicationStatement = null;
    try {
      deletePublicationStatement = con.prepareStatement(deletePublicationStatement());
      deletePublicationStatement.setLong(1, pubId.longValue());
      deletePublicationStatement.execute();
//      con.commit();
//    } catch (SQLException e) {
//      con.rollback();
//      throw e;
    } finally {
      deletePublicationStatement.close();
    }
  }


  // ------------------- PRIVATE METHODS ------------------------

  /**
   * Creates the <code>Reference</code> object from the given result set.
   *
   * @param rs The result set object.
   * @return an <code>Reference</code> instance.
   * @throws SQLException
   */
  private Reference doLoad(ResultSet rs) throws SQLException {
    assert rs != null : "Parameter 'rs' must not be null.";

    Long pubId = null;
    String medlineId = "";
    String pubMedId = "";
    String pubType = "";
    String authors = "";
    String pubYear = "";
    String title = "";
    String pubName = "";
    String volume = "";
    String firstPage = "";
    String lastPage = "";
    String edition = "";
    String editor = "";
    String publisher = "";
    String pubPlace = "";
    String webView = "";
    String source = "";

    if (rs.getLong("pub_id") > 0) pubId = new Long(rs.getLong("pub_id"));
    if (rs.getString("medline_id") != null) medlineId = rs.getString("medline_id");
    if (rs.getString("pubmed_id") != null) pubMedId = rs.getString("pubmed_id");
    if (rs.getString("pub_type") != null) pubType = rs.getString("pub_type");
    if (rs.getString("author") != null) authors = rs.getString("author");
    if (rs.getString("pub_year") != null) pubYear = rs.getString("pub_year");
    if (rs.getString("title") != null) title = rs.getString("title");
    if (rs.getString("journal_book") != null) pubName = rs.getString("journal_book");
    if (rs.getString("volume") != null) volume = rs.getString("volume");
    if (rs.getString("first_page") != null) firstPage = rs.getString("first_page");
    if (rs.getString("last_page") != null) lastPage = rs.getString("last_page");
    if (rs.getString("edition") != null) edition = rs.getString("edition");
    if (rs.getString("editor") != null) editor = rs.getString("editor");
    if (rs.getString("pub_company") != null) publisher = rs.getString("pub_company");
    if (rs.getString("pub_place") != null) pubPlace = rs.getString("pub_place");
    if (rs.getString("web_view") != null) webView = rs.getString("web_view");
    if (rs.getString("source") != null) source = rs.getString("source");

    if (pubType.equals("B"))
      return new Book(pubId, authors, title, pubYear, firstPage, lastPage, pubName,
                      edition, editor, volume, publisher, pubPlace, EnzymeViewConstant.valueOf(webView),
                      EnzymeSourceConstant.valueOf(source));

    if (pubType.equals("J"))
      return new Journal(pubId, authors, title, pubYear, pubName, firstPage, lastPage, volume, pubMedId, medlineId,
                         EnzymeViewConstant.valueOf(webView), EnzymeSourceConstant.valueOf(source));

    if (pubType.equals("P"))
      return new Patent(pubId, authors, title, pubYear, pubName, EnzymeViewConstant.valueOf(webView),
                        EnzymeSourceConstant.valueOf(source)); // the patent number is currently stored in the journal_book column.

    return null;
  }

  /**
   * Returns the next available pub ID.
   *
   * @param con The connection.
   * @return The next pub ID.
   * @throws SQLException
   */
  private Long findNextPubId(Connection con) throws SQLException {
    assert con != null : "Parameter 'con' must not be null.";

    Long pubId = null;
    PreparedStatement findNextPubId = null;
    ResultSet rs = null;
    try {
      findNextPubId = con.prepareStatement(findNextPubIdStatement());
      rs = findNextPubId.executeQuery();
      if (rs.next()) {
        pubId = new Long(rs.getLong(1));
      }
    } finally {
    	if (rs != null) rs.close();
      findNextPubId.close();
    }

    return pubId;
  }

  private int findNextOrderIn(Long enzymeId, Connection con) throws SQLException {
    assert enzymeId != null : "Parameter 'enzymeId' must not be null.";
    assert con != null : "Parameter 'con' must not be null.";

    int orderIn = 0;
    PreparedStatement findNextOrderIn = null;
    ResultSet rs = null;
    try {
      findNextOrderIn = con.prepareStatement(findNextOrderInStatement());
      findNextOrderIn.setLong(1, enzymeId.longValue());
      rs = findNextOrderIn.executeQuery();
      if (rs.next()) {
        orderIn = rs.getInt(1);
      }
    } finally {
    	if (rs != null) rs.close();
      findNextOrderIn.close();
    }

    return ++orderIn;
  }

  /**
   * Sets the citation's prepared statement parameters.
   *
   * @param enzymeId                ...
   * @param pubId                   ...
   * @param orderIn                 ...
   * @param status                  ...
   * @param source                  ...
   * @param insertCitationStatement ...
   * @throws SQLException
   */
  private void doInsertCitation(Long enzymeId, Long pubId, int orderIn, EnzymeStatusConstant status, EnzymeSourceConstant source,
                                PreparedStatement insertCitationStatement) throws SQLException {
    insertCitationStatement.setLong(1, enzymeId.longValue());
    insertCitationStatement.setLong(2, pubId.longValue());
    insertCitationStatement.setInt(3, orderIn);
    insertCitationStatement.setString(4, status.getCode());
    insertCitationStatement.setString(5, source.toString());
  }

  private void doInsertJournal(Long newPubId, Journal journal, PreparedStatement insertJournalStatement) throws SQLException {
    assert newPubId != null : "Parameter 'newPubId' must not be null.";
    assert journal != null : "Parameter 'journal' must not be null.";
    assert insertJournalStatement != null : "Parameter 'insertJournalStatement' must not be null.";

    insertJournalStatement.setLong(1, newPubId.longValue());
    insertJournalStatement.setString(2, journal.getMedlineId());
    insertJournalStatement.setString(3, journal.getPubMedId());
    insertJournalStatement.setString(4, "J");
    insertJournalStatement.setString(5, journal.getAuthors());
    insertJournalStatement.setString(6, journal.getYear());
    insertJournalStatement.setString(7, journal.getTitle());
    insertJournalStatement.setString(8, journal.getPubName());
    insertJournalStatement.setString(9, journal.getVolume());
    insertJournalStatement.setString(10, journal.getFirstPage());
    insertJournalStatement.setString(11, journal.getLastPage());
    insertJournalStatement.setString(12, journal.getView().toString());
    insertJournalStatement.setString(13, journal.getSource().toString());
  }

  private void doUpdateJournal(Journal journal, PreparedStatement updateJournalStatement) throws SQLException {
    assert journal != null : "Parameter 'journal' must not be null.";
    assert updateJournalStatement != null : "Parameter 'updateJournalStatement' must not be null.";

    updateJournalStatement.setString(1, journal.getMedlineId());
    updateJournalStatement.setString(2, journal.getPubMedId());
    updateJournalStatement.setString(3, "J");
    updateJournalStatement.setString(4, journal.getAuthors());
    updateJournalStatement.setString(5, journal.getYear());
    updateJournalStatement.setString(6, journal.getTitle());
    updateJournalStatement.setString(7, journal.getPubName());
    updateJournalStatement.setString(8, journal.getVolume());
    updateJournalStatement.setString(9, journal.getFirstPage());
    updateJournalStatement.setString(10, journal.getLastPage());
    updateJournalStatement.setLong(11, journal.getPubId().longValue());
    updateJournalStatement.setString(12, journal.getView().toString());
    updateJournalStatement.setString(13, journal.getSource().toString());
  }

  private void doInsertBook(Long newPubId, Book book, PreparedStatement insertBookStatement) throws SQLException {
    assert newPubId != null : "Parameter 'newPubId' must not be null.";
    assert book != null : "Parameter 'book' must not be null.";
    assert insertBookStatement != null : "Parameter 'insertBookStatement' must not be null.";

    insertBookStatement.setLong(1, newPubId.longValue());
    insertBookStatement.setString(2, "B");
    insertBookStatement.setString(3, book.getAuthors());
    insertBookStatement.setString(4, book.getYear());
    insertBookStatement.setString(5, book.getTitle());
    insertBookStatement.setString(6, book.getPubName());
    insertBookStatement.setString(7, book.getVolume());
    insertBookStatement.setString(8, book.getFirstPage());
    insertBookStatement.setString(9, book.getLastPage());
    insertBookStatement.setString(10, book.getEdition(false));
    insertBookStatement.setString(11, book.getEditor(false));
    insertBookStatement.setString(12, book.getPublisher());
    insertBookStatement.setString(13, book.getPublisherPlace());
    insertBookStatement.setString(14, book.getView().toString());
    insertBookStatement.setString(15, book.getSource().toString());
  }

  private void doUpdateBook(Book book, PreparedStatement updateBookStatement) throws SQLException {
    assert book != null : "Parameter 'book' must not be null.";
    assert updateBookStatement != null : "Parameter 'updateBookStatement' must not be null.";

    updateBookStatement.setString(1, "B");
    updateBookStatement.setString(2, book.getAuthors());
    updateBookStatement.setString(3, book.getYear());
    updateBookStatement.setString(4, book.getTitle());
    updateBookStatement.setString(5, book.getPubName());
    updateBookStatement.setString(6, book.getVolume());
    updateBookStatement.setString(7, book.getFirstPage());
    updateBookStatement.setString(8, book.getLastPage());
    updateBookStatement.setString(9, book.getEdition(false));
    updateBookStatement.setString(10, book.getEditor(false));
    updateBookStatement.setString(11, book.getPublisher());
    updateBookStatement.setString(12, book.getPublisherPlace());
    updateBookStatement.setLong(13, book.getPubId().longValue());
    updateBookStatement.setString(14, book.getView().toString());
    updateBookStatement.setString(15, book.getSource().toString());
  }

  private void doInsertPatent(Long newPubId, Patent patent, PreparedStatement insertPatentStatement) throws SQLException {
    assert newPubId != null : "Parameter 'newPubId' must not be null.";
    assert patent != null : "Parameter 'patent' must not be null.";
    assert insertPatentStatement != null : "Parameter 'insertPatentStatement' must not be null.";

    insertPatentStatement.setLong(1, newPubId.longValue());
    insertPatentStatement.setString(2, "P");
    insertPatentStatement.setString(3, patent.getAuthors());
    insertPatentStatement.setString(4, patent.getYear());
    insertPatentStatement.setString(5, patent.getTitle());
    insertPatentStatement.setString(6, patent.getPatentNumber());
    insertPatentStatement.setString(7, patent.getView().toString());
    insertPatentStatement.setString(8, patent.getSource().toString());
  }

  private void doUpdatePatent(Patent patent, PreparedStatement updatePatentStatement) throws SQLException {
    assert patent != null : "Parameter 'patent' must not be null.";
    assert updatePatentStatement != null : "Parameter 'updatePatentStatement' must not be null.";

    updatePatentStatement.setString(1, "P");
    updatePatentStatement.setString(2, patent.getAuthors());
    updatePatentStatement.setString(3, patent.getYear());
    updatePatentStatement.setString(4, patent.getTitle());
    updatePatentStatement.setString(5, patent.getPatentNumber());
    updatePatentStatement.setLong(6, patent.getPubId().longValue());
    updatePatentStatement.setString(7, patent.getView().toString());
    updatePatentStatement.setString(8, patent.getSource().toString());
  }

}
