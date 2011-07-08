package uk.ac.ebi.intenz.stats.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.ebi.biobabel.util.db.SQLLoader;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.stats.IIntEnzStatistics;

/**
 * IntEnz statistics gathered from a database connection.
 * @author rafalcan
 * @since 1.1.0
 */
public class IntEnzDbStatistics implements IIntEnzStatistics {

	private int relNo;
	private Date relDate;

	private int classes;
	private int subclasses;
	private int subSubclasses;
	private Map<String, Map<Boolean, Integer>> enzymesByStatus;
	private int synonyms;
	private Map<XrefDatabaseConstant, XrefsStats> xrefs;
	private Map<XrefDatabaseConstant, XrefsStats> links;
	
	private SQLLoader sqlLoader;
    private Statement synonymsStm; // FIXME: HACK!

	public int getReleaseNumber() {
		return relNo;
	}

	public Date getReleaseDate() {
		return relDate;
	}

	public int getClasses() {
		return classes;
	}

	public int getSubclasses() {
		return subclasses;
	}

	public int getSubSubclasses() {
		return subSubclasses;
	}

	public Map<String, Map<Boolean, Integer>> getEnzymesByStatus() {
		return enzymesByStatus;
	}

	public int getSynonyms() {
		return synonyms;
	}

	public Map<XrefDatabaseConstant, XrefsStats> getXrefs() {
		return xrefs;
	}

	public Map<XrefDatabaseConstant, XrefsStats> getLinks() {
		return links;
	}

	public IntEnzDbStatistics(Connection con)
	throws IOException, SQLException, DomainException{
		this.setConnection(con);
		updateStatistics();
	}

	public void setConnection(Connection con) throws IOException, SQLException {
		if (sqlLoader != null) sqlLoader.close();
		sqlLoader = new SQLLoader(this.getClass(), con);
        synonymsStm = con.createStatement();
	}

	public void updateStatistics() throws SQLException, DomainException {
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		stm = sqlLoader.getPreparedStatement("--release");
		rs = stm.executeQuery();
		if (rs.next()){
			relNo = rs.getInt("rel_no");
			relDate = rs.getDate("rel_date");
		}
		
		stm = sqlLoader.getPreparedStatement("--classes");
		rs = stm.executeQuery();
		if (rs.next()) classes = rs.getInt("c");

		stm = sqlLoader.getPreparedStatement("--subclasses");
		rs = stm.executeQuery();
		if (rs.next()) subclasses = rs.getInt("c");
		
		stm = sqlLoader.getPreparedStatement("--subsubclasses");
		rs = stm.executeQuery();
		if (rs.next()) subSubclasses = rs.getInt("c");
		
		stm = sqlLoader.getPreparedStatement("--enzymes.by.status", "--constraint.non.transient");
		rs = stm.executeQuery();
		enzymesByStatus = new LinkedHashMap<String, Map<Boolean, Integer>>();
		while (rs.next()){
			String status = rs.getString("status");
			boolean active = "Y".equals(rs.getString("active"));
			int enzymes = rs.getInt("c");
			if (!enzymesByStatus.containsKey(status)){
				HashMap<Boolean, Integer> activeMap = new LinkedHashMap<Boolean, Integer>();
				enzymesByStatus.put(status, activeMap);
			}
			enzymesByStatus.get(status).put(active, enzymes);
		}
		
		//stm = sqlLoader.getPreparedStatement("--synonyms", "--constraint.non.transient");
		//rs = stm.executeQuery();
        rs = synonymsStm.executeQuery("select count(distinct n.name) c from names n, enzymes e where n.status = 'OK' and n.name_class = 'OTH' and n.enzyme_id = e.enzyme_id and e.enzyme_id not in (select before_id from history_events where event_class = 'MOD')");
		if (rs.next()) synonyms = rs.getInt("c");
        synonymsStm.close();
		
		stm = sqlLoader.getPreparedStatement("--xrefs");
		rs = stm.executeQuery();
		xrefs = new LinkedHashMap<XrefDatabaseConstant, XrefsStats>();
		while (rs.next()){
			XrefDatabaseConstant db = getDb(rs.getString("dbname"));
			XrefsStats xrefsStats = new XrefsStats(rs.getInt("c"), rs.getInt("cd"));
			xrefs.put(db, xrefsStats);
		}
		
		stm = sqlLoader.getPreparedStatement("--links");
		rs = stm.executeQuery();
		links = new LinkedHashMap<XrefDatabaseConstant, XrefsStats>();
		while (rs.next()){
			XrefDatabaseConstant db = getDb(rs.getString("dbname"));
			XrefsStats xrefsStats = new XrefsStats(rs.getInt("c"), rs.getInt("cd"));
			links.put(db, xrefsStats);
		}
		
		sqlLoader.close();
	}
	
	private XrefDatabaseConstant getDb(String dbName) throws DomainException{
		XrefDatabaseConstant db = null;
		try {
			db = XrefDatabaseConstant.valueOf(dbName);
		} catch (DomainException e) {
			db = XrefDatabaseConstant.valueOf(XrefDatabaseConstant.getDatabaseCodeOf(dbName));
		}
		return db;
	}
}
