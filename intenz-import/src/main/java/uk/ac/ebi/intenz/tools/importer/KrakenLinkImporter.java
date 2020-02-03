package uk.ac.ebi.intenz.tools.importer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import uk.ac.ebi.intenz.tools.importer.dto.UniprotApi;

/**
 * code partly adapted from Rafa
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class KrakenLinkImporter extends Importer {

    private static final Logger logger = Logger.getLogger(KrakenLinkImporter.class);

    private Connection impCon;
    private List<EnzymeEntry> enzymeEntries;

    // private UniProtService uniProtService;
    //@Autowired
    private UniprotService uniprotService;

    protected KrakenLinkImporter() throws IOException {
        super();
        //uniprotService = new UniprotService();
    }

    @Override
    protected void setup() throws Exception {
        logger.debug("Opening IntEnz import database connections");
        impCon = OracleDatabaseInstance
                .getInstance(importerProps.getProperty("intenz.database"))
                .getConnection();
        logger.debug("Database connection obtained " + impCon);

        //setupKraken();
        setupProteinApi();
    }

    protected void setupProteinApi() {
        uniprotService = IntenzUniprotService.getIntenzUniprotService().uniprotService();
    }

//    protected void setupKraken() {
//        uniProtService = IntenzUniprotService.getIntenzUniprotService().uniProtService();
//    }
    @Override
    protected void importData() throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        enzymeEntries = mapper.findAll(impCon);
        logger.debug("Obtained enzymes to be updated.");

        Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
        while (iter.hasNext()) {
            EnzymeEntry entry = (EnzymeEntry) iter.next();
            String ec = entry.getEc().toString();
            entry.setLinks(getKrakenLinks(ec));
        }
    }

    protected SortedSet<EnzymeLink> getKrakenLinks(String ec) {
        SortedSet<EnzymeLink> updatedUniProtXrefs = new TreeSet<>();
        List<UniprotApi> uniprotResult = uniprotService.uniprotApiByEc(ec);

        if (uniprotResult != null && !uniprotResult.isEmpty()) {

            uniprotResult.forEach(api -> processUniprotApiResult(api, updatedUniProtXrefs));
        } else {
            logger.error("No Uniprot result for this EC " + ec);
        }
        return updatedUniProtXrefs;
    }

    private void processUniprotApiResult(UniprotApi api, SortedSet<EnzymeLink> updatedUniProtXrefs) {
        String accession = api.getAccession();
        String uniprotId = api.getId();

        EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.SWISSPROT,
                XrefDatabaseConstant.SWISSPROT.getUrl(),
                accession,
                uniprotId,
                EnzymeSourceConstant.INTENZ,
                EnzymeViewConstant.SIB_INTENZ);
        updatedUniProtXrefs.add(enzymeLink);
    }

//    protected SortedSet<EnzymeLink> getKrakenLinksX(String ec) {
//
//        Query query = UniProtQueryBuilder.ec(ec).and(UniProtQueryBuilder.swissprot());
//        Optional< QueryResult<UniProtEntry>> resultEntries = Optional.empty();
//        try {
//            resultEntries = Optional.ofNullable(uniProtService.getEntries(query));
//        } catch (ServiceException ex) {
//
//            logger.error(ex.getMessage(), ex);
//        }
//
//        SortedSet<EnzymeLink> updatedUniProtXrefs = new TreeSet<EnzymeLink>();
//
//        if (resultEntries.isPresent()) {
//            QueryResult<UniProtEntry> queryResult = resultEntries.get();
//            while (queryResult.hasNext()) {
//                UniProtEntry entry = queryResult.next();
//                if (entry != null) {
//                    String accession = entry.getPrimaryUniProtAccession().getValue();
//                    final UniProtId uniprotId = entry.getUniProtId();
//                    System.out.println("DATA FOUND " + uniprotId);
//                    EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.SWISSPROT,
//                            XrefDatabaseConstant.SWISSPROT.getUrl(),
//                            accession,
//                            uniprotId.getValue(),
//                            EnzymeSourceConstant.INTENZ,
//                            EnzymeViewConstant.SIB_INTENZ);
//                    updatedUniProtXrefs.add(enzymeLink);
//                } else {
//                    logger.error("UniProtEntry (QueryResult<>) was Null for this EC " + ec);
//                }
//            }
//        }
//
//        return updatedUniProtXrefs;
//    }
    @Override
    protected void loadData() throws Exception {
        logger.debug("Load data");
        Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
        EnzymeLinkMapper mapper = new EnzymeLinkMapper();
        while (iter.hasNext()) {
            EnzymeEntry entry = (EnzymeEntry) iter.next();
            mapper.deleteByCodeXref(entry.getId(), XrefDatabaseConstant.SWISSPROT.getDatabaseCode(), impCon);
            mapper.insert(new ArrayList<EnzymeLink>(entry.getLinks()), entry.getId(), entry.getStatus(), impCon);
        }
    }

    @Override
    protected void destroy() {
        logger.debug("Closing IntEnz import connection..." + impCon);
        try {
            if (impCon != null) {
                impCon.close();
            }
            //uniProtService.stop();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

//    @Override
//    protected void destroy() {
//             logger.debug("Closing IntEnz import connection..." + impCon);
//        try {
//            if (impCon != null) {
//                impCon.close();
//            }
//            uniProtService.stop();
//        } catch (SQLException e) {
//            logger.error(e);
//        }
//    }
}
