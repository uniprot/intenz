package uk.ac.ebi.intenz.tools.importer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeLinkMapper;

/**
 * Describe class <code>GoLinkImporter</code> here.
 *
 * @author <a href="mailto:rafalcan@ebi.ac.uk">Rafael Alcantara</a>
 * @version 1.0
 */
public class GoLinkImporter extends Importer {

    private Logger LOGGER = Logger.getLogger(GoLinkImporter.class);

    private Connection impCon = null, expCon = null;

    private List<EnzymeEntry> enzymeEntries;

    private static String EC2GO_QUERY = "SELECT x.db_id, t.go_id, t.name"
        + " FROM go.terms t, go.xrefs x"
        + " WHERE x.go_id = t.go_id AND x.db_code = 'EC' AND x.db_id NOT LIKE '%-'";

    public GoLinkImporter() throws IOException{
		super();
    }

    /**
     * Open database connections.
     * @exception Exception if an error occurs
     */
    public final void setup() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        impCon = OracleDatabaseInstance
        	.getInstance(importerProps.getProperty("intenz.database"))
        	.getConnection();
        expCon = OracleDatabaseInstance
        	.getInstance(importerProps.getProperty("go.database"))
        	.getConnection();
    }

    /**
     * Gets the list of enzyme entries and the updated GO links.
     * @exception Exception if an error occurs
     */
    public final void importData() throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        enzymeEntries = mapper.findAll(impCon);
        LOGGER.debug("Obtained enzymes to be updated.");

        Hashtable<String, SortedSet<EnzymeLink>> goLinksTable = getGoLinks();
        LOGGER.debug("Obtained GO links.");

        Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
        while ( iter.hasNext() ) {
           EnzymeEntry entry = (EnzymeEntry) iter.next();
           String ec = entry.getEc().toString();
           if (goLinksTable.containsKey(ec))
               entry.setLinks((SortedSet<EnzymeLink>) goLinksTable.get(ec));
        }
    }

    /**
     * @return A table mapping EC numbers as Strings to Sets of GO links
     * @throws SQLException
     */
    private final Hashtable<String, SortedSet<EnzymeLink>> getGoLinks() throws SQLException{
        Statement stm = expCon.createStatement();
        ResultSet rs = stm.executeQuery(EC2GO_QUERY);
        Hashtable<String, SortedSet<EnzymeLink>> goLinksTable =
        	new Hashtable<String, SortedSet<EnzymeLink>>();
        while (rs.next()){
            String ec = rs.getString(1);
            EnzymeLink goLink = getGoLink(rs.getString(2), rs.getString(3));
            if (!goLinksTable.containsKey(ec)){
                SortedSet<EnzymeLink> goLinksSet = new TreeSet<EnzymeLink>();
                goLinksSet.add(goLink);
                goLinksTable.put(ec, goLinksSet);
            } else {
                ((Set<EnzymeLink>) goLinksTable.get(ec)).add(goLink);
            }
        }
        rs.close();
        stm.close();
        return goLinksTable;
    }

    private EnzymeLink getGoLink(String goId, String goName){
      return EnzymeLink.valueOf(XrefDatabaseConstant.GO,
          XrefDatabaseConstant.GO.getUrl(),
          goId,
          goName,
          EnzymeSourceConstant.INTENZ,
          EnzymeViewConstant.IUBMB_INTENZ);
    }

    /**
     * Write imported GO links to the database.
     * @exception Exception if an error occurs
     */
    public final void loadData() throws Exception {
        LOGGER.debug("Load data");
        try {
            EnzymeLinkMapper mapper = new EnzymeLinkMapper();
            Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
            while (iter.hasNext()) {
               EnzymeEntry entry = (EnzymeEntry) iter.next();
               mapper.deleteByCodeXref(entry.getId(), XrefDatabaseConstant.GO.getDatabaseCode(), impCon);
               mapper.insert(new ArrayList<EnzymeLink>(entry.getLinks()), entry.getId(), entry.getStatus(), impCon);
            }
            impCon.commit();
        } catch (Exception e){
            impCon.rollback();
            throw e;
        }
    }

    /**
     * Close database connections.
     */
    public final void destroy() {
        if (impCon != null)
            try {
                impCon.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close import connection!", e);
            }
        if (expCon != null)
            try {
                expCon.close();
            } catch (SQLException e) {
                LOGGER.error("Unable to close export connection!", e);
            }
    }

}
