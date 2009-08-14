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
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtId;
import uk.ac.ebi.kraken.uuw.services.remoting.Attribute;
import uk.ac.ebi.kraken.uuw.services.remoting.AttributeIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;

public class KrakenLinkImporter extends Importer {

	private static Logger LOGGER = Logger.getLogger(KrakenLinkImporter.class);

	private Connection impCon;
	private List<EnzymeEntry> enzymeEntries;
	private UniProtQueryService uniProtQueryService;
	
	protected KrakenLinkImporter() throws IOException {
		super();
	}

	@Override
	protected void setup() throws Exception {
		LOGGER.debug("Opening IntEnz import database connections");
        impCon = OracleDatabaseInstance
	    	.getInstance(importerProps.getProperty("intenz.database"))
	    	.getConnection();
		setupKraken();
	}
	
	protected void setupKraken(){
		uniProtQueryService = UniProtJAPI.factory.getUniProtQueryService();
	}

	@Override
	protected void importData() throws Exception {
		EnzymeEntryMapper mapper = new EnzymeEntryMapper();
		enzymeEntries = mapper.findAll(impCon);
		LOGGER.debug("Obtained enzymes to be updated.");

		Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
		while ( iter.hasNext() ) {
			EnzymeEntry entry = (EnzymeEntry) iter.next();
			String ec = entry.getEc().toString();
			entry.setLinks(getKrakenLinks(ec));
		}
	}

	protected SortedSet<EnzymeLink> getKrakenLinks(String ec) {
		Query query = UniProtQueryBuilder.buildECNumberQuery(ec);
		Query queryReviewed = UniProtQueryBuilder.setReviewedEntries(query);
		AttributeIterator<UniProtEntry> it =
			uniProtQueryService.getAttributes(queryReviewed, "ognl:uniProtId");
		SortedSet<EnzymeLink> updatedUniProtXrefs = new TreeSet<EnzymeLink>();
		for (Attribute attribute : it) {
			String accession = attribute. getAccession();
			final UniProtId uniprotId  = (UniProtId) attribute.getValue();
			EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.SWISSPROT ,
	        	XrefDatabaseConstant.SWISSPROT.getUrl(),
	        	accession,
	        	uniprotId.getValue(),
	        	EnzymeSourceConstant.INTENZ,
	        	EnzymeViewConstant.SIB_INTENZ);
        	updatedUniProtXrefs.add(enzymeLink);
		}
		return updatedUniProtXrefs;
	}

	@Override
	protected void loadData() throws Exception {
		LOGGER.debug("Load data");
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
		LOGGER.debug("Closing IntEnz import connection...");
        try {
			impCon.close();
		} catch (SQLException e) {
			LOGGER.error(e);
		}
	}

}
