package uk.ac.ebi.intenz.tools.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
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

public class KrakenLinkImporter extends Importer {

	private static Logger LOGGER = Logger.getLogger(KrakenLinkImporter.class);
	private static final String URL_PREFIX = "https://www.ebi.ac.uk/proteins/api/proteins?offset=0&size=-1&reviewed=true&format=txt&ec=";

	private Connection impCon;
	private List<EnzymeEntry> enzymeEntries;

	protected KrakenLinkImporter() throws IOException {
		super();
	}

	@Override
	protected void setup() throws Exception {
		LOGGER.debug("Opening IntEnz import database connections");
		impCon = OracleDatabaseInstance.getInstance(importerProps.getProperty("intenz.database")).getConnection();
	}

	@Override
	protected void importData() throws Exception {
		EnzymeEntryMapper mapper = new EnzymeEntryMapper();
		enzymeEntries = mapper.findAll(impCon);
		LOGGER.debug("Obtained enzymes to be updated.");

		Iterator<EnzymeEntry> iter = enzymeEntries.iterator();
		while (iter.hasNext()) {
			EnzymeEntry entry = (EnzymeEntry) iter.next();
			String ec = entry.getEc().toString();
			entry.setLinks(getUniProtLinks(ec));
		}
	}

	static Map<String, String> getAcc2IdMap(String ec) {
		Map<String, String> acc2Id = new TreeMap<>();
		String uri = URL_PREFIX + ec;
		Reader reader = null;
		try {
			URL url = new URL(uri);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			// httpConnection.setRequestProperty("Accept", "application/xml");

			InputStream response = connection.getInputStream();
			int responseCode = httpConnection.getResponseCode();

			if (responseCode != 200) {
				return acc2Id;
			}

			String output;

			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			output = builder.toString();

			String[] entries = output.split("//\n");
			for (String entry : entries) {
				String[] lines = entry.split("\n");
				String id = null;
				String acc = null;
				for (String line : lines) {
					if (line.startsWith("ID   ")) {
						int index = line.indexOf(" ", "ID   ".length());
						id = line.substring("ID   ".length(), index);
					} else if (line.startsWith("AC   ")) {
						int index = line.indexOf(";", "AC   ".length());
						acc = line.substring("AC   ".length(), index);
					}
					if ((id != null) && (acc != null)) {
						acc2Id.put(acc, id);
						break;
					}
				}
			}
			return acc2Id;
		} catch (IOException e) {
			LOGGER.warn("failed fetch data for enzyme: " + ec);
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}
		return acc2Id;

	}

	protected SortedSet<EnzymeLink> getUniProtLinks(String ec) {
		Map<String, String> acc2Id = getAcc2IdMap(ec);
		SortedSet<EnzymeLink> updatedUniProtXrefs = new TreeSet<EnzymeLink>();
		for (Map.Entry<String, String> entry : acc2Id.entrySet()) {
			EnzymeLink enzymeLink = EnzymeLink.valueOf(XrefDatabaseConstant.SWISSPROT,
					XrefDatabaseConstant.SWISSPROT.getUrl(), entry.getKey(), entry.getValue(),
					EnzymeSourceConstant.INTENZ, EnzymeViewConstant.SIB_INTENZ);
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
