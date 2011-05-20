package uk.ac.ebi.intenz.tools.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.biopax.level2.Biopax;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;
import uk.ac.ebi.intenz.stats.IIntEnzStatistics;
import uk.ac.ebi.intenz.stats.db.IntEnzDbStatistics;
import uk.ac.ebi.rhea.mapper.MapperException;

public class ExporterApp {

    private enum Format {
    	INTENZ_XML("intenzXml"),
    	SITEMAP("sitemap"),
    	BIOPAX("biopax"),
    	KEGG_ENZYME("keggEnzyme");
    	private String cliOption;
    	private Format(String cliOption){
    		this.cliOption = cliOption;
    	}
	}
    
    public static Logger LOGGER = Logger.getLogger(ExporterApp.class);
    
	private Properties spotlights;

	private Connection intenzConnection;
    // Object to retrieve release number and date from:
    private IIntEnzStatistics stats;

    /**
     * Exports IntEnz data in the following formats:
     * <ul>
     * 	   <li>XML (both <i>flavours</i> ASCII and XCHARS), using the
     * 			{@link uk.ac.ebi.intenz.tools.export.XmlExporter XmlExporter}
     *          class.</li>
     * 	   <li>Site map XML file (<code>sitemap.xml</code>) to be used in
     * 		   	{@link http://www.google.com/webmasters/sitemaps Google sitemaps}
     * 			to make every IntEnz entry available to Google indexing.
     * 			Other search engines accept this standard too.</li>
     * 	   <li><a href="http://www.biopax.org">BioPAX</a>, using the biopax
     *          module.</li>
     * 	   <li><a href="ftp://ftp.genome.jp/pub/kegg/ligand/ligand.txt">KEGG
     * 			enzyme</a>.</li>
     * </ul>
     * @param args
     * <ul>
     * 	<li>-intenzDb &lt;config&gt;: database configuration file for IntEnz.</li>
     *	<li>[-intenzXml &lt;output dir&gt;]: export as <a
     * 		href="http://intenz.sourceforge.net/intenz-xml/index.html">IntEnzXML</a>
     * 		.</li>
     * 	<li>[-biopax &lt;file name&gt;]: export as one <a
     * 		href="http://www.biopax.org">BioPAX</a> OWL file.</li>
     * 	<li>[-sitemap &lt;file name&gt;]: export as a
	 * 		<a href="http://www.sitemaps.org">Sitemap</a> XML file.</li>
     * 	<li>[-keggEnzyme &lt;file name&gt;]: export as a KEGG enzyme file.</li>
     * 	<li>[-ec &lt;EC number&gt;]: export only the passed EC number.
     * 				if not set, all of the public entries are exported.</li>
     * </ul>
     * @throws DomainException
     * @throws IOException
     * @throws SQLException
     * @throws MapperException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings({ "static-access" })
    public static void main(String[] args)
    throws ClassNotFoundException, SQLException, MapperException, IOException, DomainException {
		Options options = new Options();
		options.addOption(OptionBuilder.isRequired()
				.hasArg().withArgName("config")
				.withDescription("IntEnz database configuration")
				.create("intenzDb"));
		options.addOption(OptionBuilder
				.hasArg().withArgName("file name")
				.withDescription("[optional] Export IntEnz as BioPAX")
				.create(Format.BIOPAX.cliOption));
		options.addOption(OptionBuilder
				.hasArg().withArgName("dir name")
				.withDescription("[optional] Export IntEnz as IntEnzXML")
				.create(Format.INTENZ_XML.cliOption));
		options.addOption(OptionBuilder
				.hasArg().withArgName("file name")
				.withDescription("[optional] Export IntEnz as KEGG enzyme")
				.create(Format.KEGG_ENZYME.cliOption));
		options.addOption(OptionBuilder
				.hasArg().withArgName("file name")
				.withDescription("[optional] Export IntEnz as sitemap")
				.create(Format.SITEMAP.cliOption));
		options.addOption(OptionBuilder
				.hasArg().withArgName("EC number")
				.withDescription("[optional] Export only one entry")
				.create("ec"));
		CommandLine cl = null;
		try {
			cl = new GnuParser().parse(options, args);
		} catch (ParseException e){
			new HelpFormatter().printHelp(ExporterApp.class.getName(), options);
			return;
		}
		
        ExporterApp app = new ExporterApp(cl.getOptionValue("intenzDb"));
        Collection<EnzymeEntry> enzymes = app.getEnzymeList(cl.getOptionValue("ec"));
        Map<String, Object> descriptions = app.getDescriptions();
        for (EnzymeEntry enzyme : enzymes) {
        	String classEc = String.valueOf(enzyme.getEc().getEc1());
        	String subclassEc = classEc + "." + String.valueOf(enzyme.getEc().getEc2());
        	String subSubclassEc = subclassEc + "." + String.valueOf(enzyme.getEc().getEc3());
			enzyme.setClassName(((EnzymeClass) descriptions.get(classEc)).getName());
			enzyme.setSubclassName(((EnzymeSubclass) descriptions.get(subclassEc)).getName());
			enzyme.setSubSubclassName(((EnzymeSubSubclass) descriptions.get(subSubclassEc)).getName());
		}
        LOGGER.info("Intenz exporter - Release " + app.stats.getReleaseNumber);
        if (cl.hasOption(Format.INTENZ_XML.cliOption)){
            try {
            	String xmlDir = cl.getOptionValue(Format.INTENZ_XML.cliOption);
                app.exportXML(enzymes, descriptions, xmlDir);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (cl.hasOption(Format.SITEMAP.cliOption)){
            try {
            	String sitemapFile = cl.getOptionValue(Format.SITEMAP.cliOption);
                app.exportSitemap(enzymes, descriptions, sitemapFile);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (cl.hasOption(Format.BIOPAX.cliOption)){
            try {
            	String biopaxFile = cl.getOptionValue(Format.BIOPAX.cliOption);
                app.exportBiopax(enzymes, biopaxFile);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        if (cl.hasOption(Format.KEGG_ENZYME.cliOption)){
        	try {
        		String keggFile = cl.getOptionValue(Format.KEGG_ENZYME.cliOption);
        		app.exportKegg(enzymes, keggFile);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
        	}
        }
    }

    private ExporterApp(String dbConfig)
    throws ClassNotFoundException, SQLException, IOException, DomainException {
        intenzConnection = OracleDatabaseInstance.getInstance(dbConfig)
        		.getConnection();
        stats = new IntEnzDbStatistics(intenzConnection);
    }

    @Override
	protected void finalize() throws Throwable {
    	if (intenzConnection != null) intenzConnection.close();
	}

	/**
     * Gets the list of enzymes to be exported.
     * @param con
     * @param ec An EC number. If <code>null</code>, every exportable enzyme is
     * 		included.
     * @throws SQLException
     * @throws DomainException
     */
    @SuppressWarnings("unchecked")
	private Collection<EnzymeEntry> getEnzymeList(String ecString)
    throws SQLException, MapperException, DomainException{
        Collection<EnzymeEntry> enzymeList = null;
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
		if (ecString != null){
        	EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ecString);
        	Status status = ec.getType().equals(Type.PRELIMINARY)?
        			Status.PRELIMINARY : Status.APPROVED;
    		enzymeList  = Collections.singletonList(
    				mapper.findByEc(ec.getEc1(), ec.getEc2(), ec.getEc3(),
    						ec.getEc4(), status, intenzConnection));
        } else {
            LOGGER.info("Retrieving IntEnz entries");
        	enzymeList = mapper.exportAllEntries(intenzConnection);
            LOGGER.info("Retrieved IntEnz entries");
        }
		return enzymeList;
    }

    /**
     * Builds a map of EC numbers (as String) to <code>EnzymeClass</code>,
     * <code>EnzymeSubClass</code> or <code>EnzymeSubSubClass</code> objects
     * from which to retrieve names and descriptions.
     * @param con
     * @return 
     * @throws SQLException
     * @throws DomainException
     */
    private Map<String, Object> getDescriptions()
    throws SQLException, DomainException{
        LOGGER.info("Retrieving IntEnz descriptions");
        Map<String, Object> descriptions = new HashMap<String, Object>();
        EnzymeClassMapper classMapper = new EnzymeClassMapper();
        EnzymeSubclassMapper subclassMapper = new EnzymeSubclassMapper();
        EnzymeSubSubclassMapper subsubclassMapper = new EnzymeSubSubclassMapper();
        for (Object o : classMapper.findAll(intenzConnection)) {
            EnzymeClass enzymeClass = (EnzymeClass) o;
            descriptions.put(enzymeClass.getEc().toString(), enzymeClass);
        }
        for (Object o : subclassMapper.findAll(intenzConnection)) {
            EnzymeSubclass enzymeSubclass = (EnzymeSubclass) o;
            descriptions.put(enzymeSubclass.getEc().toString(), enzymeSubclass);
        }
        for (Object o : subsubclassMapper.findAll(intenzConnection)) {
            EnzymeSubSubclass enzymeSubsubclass = (EnzymeSubSubclass) o;
            descriptions.put(enzymeSubsubclass.getEc().toString(), enzymeSubsubclass);
        }
        LOGGER.info("Retrieved IntEnz descriptions");
        return Collections.unmodifiableMap(descriptions);
    }

    /**
     * Exports data in XML format.
     * @param enzymeList 
     * @param descriptions 
     * @param toDir destination directory for XML files.
     * @throws Exception
     */
    private void exportXML(Collection<EnzymeEntry> enzymeList,
    		Map<String, Object> descriptions, String toDir) throws Exception {
        OutputStream os = null;
        checkWritable(toDir);
        String releaseDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(stats.getReleaseDate())
        LOGGER.info("Intenz exporter - Release " + stats.getReleaseNumber());
        LOGGER.info("Outputting XML to " + toDir);
        try {
            XmlExporter exporter = new XmlExporter();
            exporter.setDescriptions(descriptions);
            for (XmlExporter.Flavour flavour : XmlExporter.Flavour.values()){
                exporter.setFlavour(flavour);
                File flavourDir = new File(toDir, flavour.toString());
                flavourDir.mkdir();
                LOGGER.info("Single-entry XML start");
                List<EnzymeEntry> validEntriesList = new ArrayList<EnzymeEntry>();
                // Export single-entry files:
                for (EnzymeEntry entry : enzymeList) {
                    String classEc = "EC_" + String.valueOf(entry.getEc().getEc1());
                    String subclassEc = classEc + "." + String.valueOf(entry.getEc().getEc2());
                    String subsubclassEc = subclassEc + "." + String.valueOf(entry.getEc().getEc3());
                    String dirTree = classEc + "/" + subclassEc + "/" + subsubclassEc;
                    File subsubclassDir =  new File(flavourDir, dirTree);
                    subsubclassDir.mkdirs();
                    File outputFile = new File(subsubclassDir, "EC_" + entry.getEc().toString() + ".xml");
                    os = new FileOutputStream(outputFile);
                    try {
                        exporter.export(entry, stats.getReleaseNumber(),
                                releaseDate, os);
                        validEntriesList.add(entry);
                    } catch (MarshalException e) {
                        LOGGER.warn(entry.getEc().toString(), e);
                    }
                }
                LOGGER.info("Single-entry XML end");
                // Export whole tree (only valid entries):
                File treeFile = new File(flavourDir, "intenz.xml");
                os = new FileOutputStream(treeFile);
                LOGGER.info("Whole tree XML start");
                try {
                    exporter.export(validEntriesList, stats.getReleaseNumber(),
                                releaseDate, os);
                } catch (Exception e) {
                    LOGGER.error("Whole tree dump", e);
                }
                LOGGER.info("Whole tree XML end");
            }
        } finally {
            if (os != null) os.close();
        }
    }

    private void exportSitemap(Collection<EnzymeEntry> enzymeList,
    		Map<String, Object> descriptions, String sitemapFile)
    throws IOException, JAXBException, SAXException{
    	final String queryUrl = "http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&q=";
    	final String spotlightUrl = "http://www.ebi.ac.uk/intenz/spotlight.jsp?ec=";
    	File sitemap = new File(sitemapFile);
        checkWritable(sitemap.getParent());
    	if (!sitemap.exists()) sitemap.createNewFile();
    	OutputStream os = null;
    	// Build the list of URLs:
    	Collection<String> urls = new ArrayList<String>();
		// Enzymes:
    	for (EnzymeEntry entry : enzymeList) {
    		StringBuffer sb = new StringBuffer(queryUrl);
			String ec = entry.getEc().toString();
    		sb.append(ec);
    		urls.add(sb.toString());
		}
		// Spotlights:
		spotlights = new Properties();
        spotlights.load(ExporterApp.class.getClassLoader()
        		.getResourceAsStream("spotlights.properties"));
		for (Object ec : spotlights.keySet()){
			StringBuffer spotSb = new StringBuffer(spotlightUrl);
			spotSb.append((String) ec);
			urls.add(spotSb.toString());
		}
		// Classes, subclasses and subsubclasses:
		for (String ec : descriptions.keySet()){
			StringBuffer sb = new StringBuffer(queryUrl);
			sb.append(ec);
    		urls.add(sb.toString());
		}
		// Build the sitemap:
    	try {
        	os = new FileOutputStream(sitemap);
			SitemapExporter exporter = new SitemapExporter();
			exporter.export(urls, os);
    	} finally {
            if (os != null) os.close();
    	}
    }

    private void exportBiopax(Collection<EnzymeEntry> enzymeList, String biopaxFile)
    throws IOException, IllegalAccessException, InvocationTargetException{
        OutputStream os = null;
        LOGGER.info("Outputting BioPAX to " + biopaxFile);
        try {
            File owlFile = new File(biopaxFile);
            checkWritable(owlFile.getParent());
            if (!owlFile.exists()) owlFile.createNewFile();
            os = new FileOutputStream(owlFile);
            Biopax.write(enzymeList, os);
        } finally {
            if (os != null) os.close();
        }
    }

    private void exportKegg(Collection<EnzymeEntry> enzymes, String keggFile)
    throws Exception {
		OutputStream os = null;
    	try {
			File keggEnzymeFile = new File(keggFile);
            checkWritable(keggEnzymeFile.getParent());
			if (!keggEnzymeFile.exists()) keggEnzymeFile.createNewFile();
			os = new FileOutputStream(keggEnzymeFile);
			KeggExporter exporter = new KeggExporter();
			exporter.export(enzymes, os);
        } finally {
            if (os != null) os.close();
		}
		
	}

	private void checkWritable(String toDir) throws IOException{
        File outputDir = new File(toDir);
        if (outputDir.exists()){
            if (!outputDir.canWrite()){
            	String msg = "Cannot write to " + toDir;
                LOGGER.error(msg);
                throw new IOException();
            }
        } else if (!outputDir.mkdirs()){
        	String msg = "Cannot create output directory " + toDir;
            LOGGER.error(msg);
            throw new IOException(msg);
        }
    }

}
