package uk.ac.ebi.intenz.rhea.mapper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sql.ARRAY;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.citations.CitexploreWSClient;
import uk.ac.ebi.biobabel.citations.DataSource;
import uk.ac.ebi.biobabel.util.db.SQLLoader;
import uk.ac.ebi.cdb.webservice.Result;
import uk.ac.ebi.rhea.domain.Coefficient;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Direction;
import uk.ac.ebi.rhea.domain.Merging;
import uk.ac.ebi.rhea.domain.Qualifier;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.ReactionException;
import uk.ac.ebi.rhea.domain.ReactionParticipant;
import uk.ac.ebi.rhea.domain.ReactionParticipant.Location;
import uk.ac.ebi.rhea.domain.Side;
import uk.ac.ebi.rhea.domain.Status;
import uk.ac.ebi.rhea.domain.XRef;
import uk.ac.ebi.rhea.domain.XRef.Availability;
import uk.ac.ebi.rhea.mapper.IRheaCompoundReader;
import uk.ac.ebi.rhea.mapper.IRheaReader;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.SearchOptions;
import uk.ac.ebi.rhea.mapper.SearchSwitch;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;


/**
 * created to access to rhea from intenz after db split - see UES-12 for motivations, UES-14 for specific sql queries
 *
 */
public class IntEnzRheaDbReader extends RheaDbReader  {
	
	/**
	 * Compound field used for a search.
	 */
//	private enum Field { NAME, ACCESSION }

	private Logger LOGGER = Logger.getLogger(IntEnzRheaDbReader.class);
	
//	protected Connection con;
//	protected SQLLoader sqlLoader;
//	protected RheaCompoundDbReader compoundReader;
	protected IntEnzRheaCompoundDbReader intEnzRheacompoundReader;
	
	/**
	 * @param compoundReader the object who retrieves information about
	 * 		reaction participants from the database.
	 * @throws IOException
	 */
//	public RheaDbReader(RheaCompoundDbReader compoundReader) throws IOException{
//		this.compoundReader = compoundReader;
//		this.con = compoundReader.getConnection();
//		this.sqlLoader = new SQLLoader(this.getClass(), con);
//	}
	public IntEnzRheaDbReader(IntEnzRheaCompoundDbReader intEnzRheacompoundReader) throws IOException{
		super(null);
		this.intEnzRheacompoundReader = intEnzRheacompoundReader;
		this.con = intEnzRheacompoundReader.getConnection();
		this.sqlLoader = new SQLLoader(this.getClass(), con);
	}

	@Override
	//Added to reuse the compoundReader variable
	public IntEnzRheaCompoundDbReader getCompoundReader() {
		return intEnzRheacompoundReader;
	}

}
