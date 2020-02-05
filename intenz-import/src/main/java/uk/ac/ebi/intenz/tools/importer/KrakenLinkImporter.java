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
 *
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class KrakenLinkImporter extends Importer {

    private static final Logger LOG = Logger.getLogger(KrakenLinkImporter.class);

    private Connection impCon;
    private List<EnzymeEntry> enzymeEntries;

    private UniprotService uniprotService;

    protected KrakenLinkImporter() throws IOException {
        super();
 
    }

    @Override
    protected void setup() throws Exception {
        LOG.debug("Opening IntEnz import database connections");
        impCon = OracleDatabaseInstance
                .getInstance(importerProps.getProperty("intenz.database"))
                .getConnection();
        LOG.debug("Database connection obtained " + impCon);
        setupProteinApi();
    }

    protected void setupProteinApi() {
        uniprotService = IntenzUniprotService.getIntenzUniprotService().uniprotService();
    }


    @Override
    protected void importData() throws Exception {
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        enzymeEntries = mapper.findAll(impCon);
        LOG.debug("Obtained enzymes to be updated.");

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
            LOG.error("No Uniprot result for this EC " + ec);
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

    @Override
    protected void loadData() throws Exception {
        LOG.debug("Load data");
        Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
        EnzymeLinkMapper mapper = new EnzymeLinkMapper();
        while (iter.hasNext()) {
            EnzymeEntry entry = (EnzymeEntry) iter.next();
            mapper.deleteByCodeXref(entry.getId(), XrefDatabaseConstant.SWISSPROT.getDatabaseCode(), impCon);
            mapper.insert(new ArrayList<>(entry.getLinks()), entry.getId(), entry.getStatus(), impCon);
        }
    }

    @Override
    protected void destroy() {
        LOG.debug("Closing IntEnz import connection..." + impCon);
        try {
            if (impCon != null) {
                impCon.close();
            }
         } catch (SQLException e) {
            LOG.error(e);
        }
    }

}
