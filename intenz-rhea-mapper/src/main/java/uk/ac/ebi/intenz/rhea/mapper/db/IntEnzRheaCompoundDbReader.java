package uk.ac.ebi.intenz.rhea.mapper.db;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.db.SQLLoader;
import uk.ac.ebi.rhea.domain.ChebiCompound;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Compound.ChebiStatus;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Formula;
import uk.ac.ebi.rhea.domain.GenericCompound;
import uk.ac.ebi.rhea.domain.NotChemical;
import uk.ac.ebi.rhea.domain.Polymer;
import uk.ac.ebi.rhea.domain.PolymerizationIndex;
import uk.ac.ebi.rhea.domain.Residue;
import uk.ac.ebi.rhea.domain.SmallMolecule;
import uk.ac.ebi.rhea.domain.Sru;
import uk.ac.ebi.rhea.domain.XRef;
import uk.ac.ebi.rhea.domain.XRef.Availability;
import uk.ac.ebi.rhea.domain.util.CompoundType;
import uk.ac.ebi.rhea.domain.util.GenericType;
import uk.ac.ebi.rhea.mapper.IRheaCompoundReader;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.util.IChebiHelper.Image;

/**
 * created to access to rhea from intenz after db split - see UES-12 for motivations, UES-14 for specific sql queries
 *
 */
public class IntEnzRheaCompoundDbReader extends RheaCompoundDbReader {
	
    private final Logger LOGGER = Logger.getLogger(IntEnzRheaCompoundDbReader.class);
//    private Connection connection;
//    private SQLLoader sqlLoader;
//    private Properties statementsSql;

    public IntEnzRheaCompoundDbReader(Connection con) throws IOException{
    	super(con);
//    	this.connection = con;
    	this.sqlLoader = new SQLLoader(this.getClass(), con);
    }

}
