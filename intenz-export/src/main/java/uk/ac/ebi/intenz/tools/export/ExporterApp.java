package uk.ac.ebi.intenz.tools.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.util.StringTokenizer;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.biopax.level2.Biopax;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;

public class ExporterApp {

    private enum Format { INTENZ_XML, SITEMAP, BIOPAX }
    
    public static Logger LOGGER = Logger.getLogger(ExporterApp.class);

    private List<EnzymeEntry> enzymeList;

    private Map<String, Object> DESCRIPTIONS;
    
    private Properties props;
	private Properties spotlights;

    /**
     * Exports IntEnz data in the following formats:
     * <ul>
     * 	   <li>XML (both <i>flavours</i> ASCII and XCHARS), using the
     * 			{@link uk.ac.ebi.intenz.tools.export.XmlExporter XmlExporter}
     *          class</li>
     * 	   <li>Site map XML file (<code>sitemap.xml</code>) to be used in
     * 		   	{@link http://www.google.com/webmasters/sitemaps Google sitemaps}
     * 			to make every IntEnz entry available to Google indexing.
     * 			Other search engines accept this standard too.</li>
     * 	   <li><a href="http://www.biopax.org">BioPAX</a>, using the biopax
     *          module</li>
     * </ul>
     * Some properties files must be in the classpath:
     * <ul>
     *      <li><code>intenz-release.properties</code>: containing the release number
     *          and date</li>
     *      <li><code>intenz-export.properties</code>: containing parameters
     *          for the application (database instance, output directories)</li>
     * </ul>
     * @param args 
     * @throws DomainException
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    throws ClassNotFoundException, SQLException, IOException, DomainException {
        ExporterApp app = new ExporterApp();
        String formats = app.props.getProperty("intenz.export.format");
        if (!StringUtil.isNullOrEmpty(formats)){
            StringTokenizer st = new StringTokenizer(formats, " ,;");
            while (st.hasMoreTokens()){
                String format = st.nextToken();
                switch (Format.valueOf(format)){
                case INTENZ_XML:
                    try {
                        app.exportXML();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    break;
                case SITEMAP:
                    try {
                        app.exportSitemap();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    break;
                case BIOPAX:
                    try {
                        app.exportBiopax();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                    break;
                }
            }
        } else { // export all
            try {
                app.exportXML();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            try {
                app.exportSitemap();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            try {
                app.exportBiopax();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private ExporterApp()
    throws ClassNotFoundException, SQLException, IOException, DomainException {
        Connection con = null;
        props = new Properties();
		spotlights = new Properties();
        try {
			props.load(ExporterApp.class.getClassLoader().getResourceAsStream("intenz-export.properties"));
            props.load(ExporterApp.class.getClassLoader().getResourceAsStream("intenz-release.properties"));
            spotlights.load(ExporterApp.class.getClassLoader().getResourceAsStream("spotlights.properties"));

            String dbName = props.getProperty("intenz.export.db.instance");
            con = OracleDatabaseInstance.getInstance(dbName).getConnection();
            LOGGER.info("Retrieving IntEnz entries from " + dbName);
            getEnzymeList(con);
            LOGGER.info("Retrieved IntEnz entries");
            LOGGER.info("Retrieving IntEnz descriptions from " + dbName);
            getDescriptions(con);
            LOGGER.info("Retrieved IntEnz descriptions");
        } finally {
        	if (con != null) con.close();
        }
    }

    /**
     * Gets the whole list of <i>exportable</i> enzymes.
     * @param con
     * @throws SQLException
     * @throws DomainException
     */
    @SuppressWarnings("unchecked")
	private void getEnzymeList(Connection con) throws SQLException, DomainException{
        EnzymeEntryMapper mapper = new EnzymeEntryMapper();
        enzymeList = mapper.exportAllEntries(con);
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
    private void getDescriptions(Connection con) throws SQLException, DomainException{
        Map<String, Object> descriptions = new HashMap<String, Object>();
        EnzymeClassMapper classMapper = new EnzymeClassMapper();
        EnzymeSubclassMapper subclassMapper = new EnzymeSubclassMapper();
        EnzymeSubSubclassMapper subsubclassMapper = new EnzymeSubSubclassMapper();
        for (Object o : classMapper.findAll(con)) {
            EnzymeClass enzymeClass = (EnzymeClass) o;
            descriptions.put(enzymeClass.getEc().toString(), enzymeClass);
        }
        for (Object o : subclassMapper.findAll(con)) {
            EnzymeSubclass enzymeSubclass = (EnzymeSubclass) o;
            descriptions.put(enzymeSubclass.getEc().toString(), enzymeSubclass);
        }
        for (Object o : subsubclassMapper.findAll(con)) {
            EnzymeSubSubclass enzymeSubsubclass = (EnzymeSubSubclass) o;
            descriptions.put(enzymeSubsubclass.getEc().toString(), enzymeSubsubclass);
        }
        DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
    }

    /**
     * Exports data in XML format.
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void exportXML() throws Exception {
        OutputStream os = null;
        String toDir = props.getProperty("intenz.export.xml.output.dir");
        checkWritable(toDir);
        LOGGER.info("Intenz exporter - Release " + props.getProperty("intenz.release.number"));
        LOGGER.info("Outputting XML to " + toDir);
        try {
            XmlExporter exporter = new XmlExporter();
            exporter.setDescriptions(DESCRIPTIONS);
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
                        exporter.export(entry, props.getProperty("intenz.release.number"),
                                props.getProperty("intenz.release.date"), os);
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
                    exporter.export(validEntriesList, props.getProperty("intenz.release.number"),
                                props.getProperty("intenz.release.date"), os);
                } catch (Exception e) {
                    LOGGER.error("Whole tree dump");
                    LOGGER.debug("", e);
                }
                LOGGER.info("Whole tree XML end");
            }
        } finally {
            if (os != null) os.close();
        }
    }

    private void exportSitemap()
    throws IOException, JAXBException, SAXException{
        String toDir = props.getProperty("intenz.export.sitemap.output.dir");
    	checkWritable(toDir);
    	final String queryUrl = "http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&q=";
    	final String spotlightUrl = "http://www.ebi.ac.uk/intenz/spotlight.jsp?ec=";
    	File sitemap = new File(toDir, "sitemap.xml");
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
		for (Object ec : spotlights.keySet()){
			StringBuffer spotSb = new StringBuffer(spotlightUrl);
			spotSb.append((String) ec);
			urls.add(spotSb.toString());
		}
		// Classes, subclasses and subsubclasses:
		for (String ec : DESCRIPTIONS.keySet()){
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

    private void exportBiopax()
    throws IOException, IllegalAccessException, InvocationTargetException{
        OutputStream os = null;
        String toDir = props.getProperty("intenz.export.biopax.output.dir");
        checkWritable(toDir);
        LOGGER.info("Intenz exporter - Release " + props.getProperty("intenz.release.number"));
        LOGGER.info("Outputting BioPAX to " + toDir);
        try {
            File owlFile = new File(toDir, "intenz-biopax.owl");
            if (!owlFile.exists()) owlFile.createNewFile();
            os = new FileOutputStream(owlFile);
            Biopax.write(enzymeList, os);
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
