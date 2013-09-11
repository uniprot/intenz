package uk.ac.ebi.intenz.webapp;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.biopax.level2.Biopax;
import uk.ac.ebi.intenz.domain.constants.Status;
import uk.ac.ebi.intenz.domain.enzyme.*;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;
import uk.ac.ebi.intenz.stats.db.IntEnzDbStatistics;
import uk.ac.ebi.intenz.tools.export.ExporterApp;
import uk.ac.ebi.intenz.tools.export.JsonExporter;
import uk.ac.ebi.intenz.tools.export.XmlExporter;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.util.XmlExporterPool;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * Main servlet for IntEnz web services.
 */
public class IntenzWsEcServlet extends HttpServlet
implements PropertyChangeListener {
	
	private static final long serialVersionUID = 4868575459199989086L;

    private static enum ResponseFormat {
		/** <a href="http://intenz.sf.net/intenz-xml">IntEnz XML</a>. */
		XML("xml", "application/xml"),
		/** <a href="http://biopax.org">BioPAX level 2</a>. */
		BIOPAX_L2("owl", "application/rdf+xml"),
		ENZYME("txt", "text/plain"),
		JSON("json", "application/json");
		private String extension;
		private String mimeType;
		private ResponseFormat(String extension, String mimeType){
			this.extension = extension;
			this.mimeType = mimeType;
		}
		private static ResponseFormat forExtension(String ext){
			for (ResponseFormat rf : ResponseFormat.values()) {
				if (rf.extension.equals(ext)) return rf;
			}
			throw new IllegalArgumentException(ext);
		}
	}

    private IntEnzConfig config;

    private XmlExporterPool xmlExporterPool;
    //	private EnzymeEntryMapperPool mapperPool;

	private Logger LOGGER = Logger.getLogger(IntenzWsEcServlet.class);
	
	private DataSource ds;

	/**
	 * Object containing release number and date.
	 */
	private IntEnzDbStatistics stats;
	
	private int relNum;
	private String relDate;

	private Map<String, Object> descriptions;
	
	/**
	 * Creates the naming context, loads shared data from the database and
	 * creates a pool of XML exporters.
	 */
	@Override
	public void init() throws ServletException {
        config = IntEnzConfig.getInstance();
		try {
            // Publish as MBean:
            ObjectName name = new ObjectName(config.getIntEnzConfigMbeanName());
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            if (!mbs.isRegistered(name)){
                mbs.registerMBean(config, name);
            }
        } catch (Exception e){
            LOGGER.error("Problem with the configuration MBean", e);
        }
        getServletContext().setAttribute("config", config);
        try {
            Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup(config.getIntEnzDataSource());
		} catch (NamingException e) {
			LOGGER.error("Unable to get data source", e);
		}
		
		Connection con = null;
		try {
			con = ds.getConnection();
			loadData(con);
		} catch (Exception e) {
			LOGGER.error("Unable to load shared data", e);
		} finally {
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("Unable to close connection");
				}
			}
		}
		
		try {
            xmlExporterPool = new XmlExporterPool(
                    config.getXmlExportersPoolSize(), descriptions,
                    relNum, relDate);
            config.addPropertyChangeListener(
                    IntEnzConfig.Property.XML_EXPORTER_POOL_SIZE.toString(),
                    this);
//			int numOfMappers = Integer.parseInt(getServletContext().
//					getInitParameter("mappers.pool.size"));
//			mapperPool = new EnzymeEntryMapperPool(numOfMappers);
		} catch (Exception e) {
			LOGGER.error("Unable to create XML exporters pool", e);
		}
	}

    @Override
    public void destroy() {
        if (config != null){
            config.removePropertyChangeListener(
                    IntEnzConfig.Property.XML_EXPORTER_POOL_SIZE.toString(),
                    this);
        }
    }

    /**
	 * Serves an IntEnz resource (enzyme).
	 * The requested URL must be of the form
	 * <blockquote>
	 * <code>{intenz-ws}/EC/{ecNumber}[.{format}]</code>
	 * </blockquote>
	 * where
	 * <ul>
	 * 	<li><code>{intenz-ws}</code> is the context path to the webapp.</li>
	 * 	<li><code>EC</code> is the path to this servlet (defined in web.xml).
	 * 		</li>
	 * 	<li><code>{ecNumber}</code> is the complete EC number of the enzyme.
	 * 		Note that preliminary EC numbers are supported, but classes,
	 * 		subclasses, subsubclasses or partial EC numbers are not.
     * 	    FIXME
     * 		</li>
	 * 	<li><code>{format}</code> is the extension corresponding to the
	 * 		requested format (see {@link ResponseFormat} enumeration). This can
	 * 		be replaced by a <code>format</code> request parameter or a valid
	 * 		<code>Accept</code> HTTP header.</li>
	 * </ul>
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		String path = req.getPathInfo().substring(1); // remove slash
		String ecString = getEcString(req);
        ResponseFormat rf = null;
		Connection con = null;
		OutputStream os = null;
        try {
            os = res.getOutputStream();
            EnzymeCommissionNumber ec = ecString.length() == 0?
                    null : EnzymeCommissionNumber.valueOf(ecString);
            rf = getResponseFormat(req);
            con = ds.getConnection();
            Object output = getOutput(con, ec);
            if (output == null){
                throw new EcException("EC " + ecString + " not found");
            }
            checkEcTypeFormat(rf, ec);

            res.setCharacterEncoding("UTF-8");
			res.setContentType(rf.mimeType);
			switch (rf) {
			case XML:
                exportXml((EnzymeEntry) output, os);
				break;
			case BIOPAX_L2:
				Biopax.write(Collections.singleton((EnzymeEntry) output), os);
				break;
			case ENZYME:
				EnzymeFlatFileWriter.export(SibEntryHelper.getSibEnzymeEntry(
                        (EnzymeEntry) output,
                        SpecialCharacters.getInstance(null),
						EncodingType.SWISSPROT_CODE),
						new OutputStreamWriter(os));
				break;
            case JSON:
                exportJson(output, os);
				break;
			}
			res.setStatus(HttpServletResponse.SC_OK);
        } catch (EcException e){
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.warn("EC not found: " + ecString);
            processException(res, path, os, e);
		} catch (IllegalArgumentException e){
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOGGER.warn("Bad request: " + path);
            processException(res, path, os, e);
        } catch (UnsupportedOperationException e){
            res.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            LOGGER.warn("Unsupported media type: " + ecString
                    + " (" + rf.mimeType + ")");
            processException(res, path, os, e);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOGGER.error(path, e);
			processException(res, path, os, e);
		} finally {
			res.flushBuffer();
			if (con != null){
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("Unable to close connection", e);
				}
			}
			if (os != null){
				try {
					os.close();
				} catch (IOException e){
					LOGGER.error("Unable to close output stream", e);
				}
			}
		}
	}

    private void exportXml(EnzymeEntry output, OutputStream os) throws Exception {
        XmlExporter xmlExporter = null;
        try {
            xmlExporter = xmlExporterPool.borrowObject();
            xmlExporter.export((EnzymeEntry) output, os);
        } finally {
            if (xmlExporter != null){
                xmlExporterPool.returnObject(xmlExporter);
            }
        }
    }

    private void exportJson(Object output, OutputStream os) throws IOException {
        JsonExporter exporter = new JsonExporter();
        if (output instanceof EnzymeEntry){
            exporter.export((EnzymeEntry) output, os);
        } else if (output instanceof EnzymeSubSubclass){
            exporter.export((EnzymeSubSubclass) output, os);
        } else if (output instanceof EnzymeSubclass){
            exporter.export((EnzymeSubclass) output, os);
        } else if (output instanceof EnzymeClass){
            exporter.export((EnzymeClass) output, os);
        } else if (output instanceof List){
            exporter.export((List<EnzymeClass>) output, os);
        }
    }

    /**
     * Retrieves the requested object from the database.
     * @param con a database connection.
     * @param ec the requested EC number
     * @return an EnzymeEntry, EnzymeSubSubclass, EnzymeSubclass or EnzymeClass,
     *      depending on the requested EC number. If the requested EC number is
     *      <code>null</code>, then a List of all the EC classes (without
     *      subclass information) is returned.
     * @throws SQLException
     * @throws DomainException
     * @throws MapperException
     */
    private Object getOutput(Connection con, EnzymeCommissionNumber ec)
    throws SQLException, DomainException, MapperException {
        Object output = null;
        if (ec == null){
            // Requesting all of the EC classification
            EnzymeClassMapper classMapper = new EnzymeClassMapper();
            output = classMapper.findAll(con);
        } else switch (ec.getType()) {
            case PRELIMINARY:
            case ENZYME:
                Status status = ec.getType().equals(Type.PRELIMINARY) ?
                        Status.PRELIMINARY : Status.APPROVED;
                EnzymeEntryMapper entryMapper = null;
//                    entryMapper = mapperPool.borrowObject();
                try {
                    entryMapper = new EnzymeEntryMapper();
                    output = entryMapper.findByEc(
                            ec.getEc1(), ec.getEc2(), ec.getEc3(), ec.getEc4(),
                            status, con);
                } finally {
                    if (entryMapper != null){
                        entryMapper.close();
                    }
//                    try {
//                        mapperPool.returnObject(mapper);
//                    } catch (Exception e) {
//                        LOGGER.error("Unable to return mapper to pool", e);
//                    }
                }
                break;
            case SUBSUBCLASS:
                EnzymeSubSubclassMapper subSubclassMapper =
                        new EnzymeSubSubclassMapper();
                output = subSubclassMapper.find(
                        ec.getEc1(), ec.getEc2(), ec.getEc3(), con);
                break;
            case SUBCLASS:
                EnzymeSubclassMapper subclassMapper =
                        new EnzymeSubclassMapper();
                output = subclassMapper.find(String.valueOf(ec.getEc1()),
                        String.valueOf(ec.getEc2()), con);
                break;
            case CLASS:
                EnzymeClassMapper classMapper = new EnzymeClassMapper();
                output = classMapper.find(String.valueOf(ec.getEc1()), con);
                break;
        }
        return output;
    }

    /**
     * Checks that the requested format is compatible with the requested EC
     * number, i.e. that classes, subclasses and sub-subclasses are only
     * requested as json.
     * @param rf the requested response format.
     * @param ec the EC number. If <code>null</code>, it is understood as all
     *      EC classes being requested.
     * @throws UnsupportedOperationException if the requested format is not
     *      currently implemented for the EC number.
     */
    private void checkEcTypeFormat(ResponseFormat rf,
            EnzymeCommissionNumber ec){
        final EnumSet<Type> highLvls = EnumSet.of(
                Type.CLASS, Type.SUBCLASS, Type.SUBSUBCLASS);
        if (!rf.equals(ResponseFormat.JSON) &&
                (ec == null || highLvls.contains(ec.getType()))){
            throw new UnsupportedOperationException(
                    rf.mimeType + " not supported for EC " + ec);
        }
    }

    /**
	 * Loads shared data from the database, including release date ant number.
	 * @param con a database connection.
	 * @throws IOException
	 * @throws SQLException
	 * @throws DomainException
	 */
	private void loadData(Connection con)
	throws IOException, SQLException, DomainException {
		stats = new IntEnzDbStatistics(con);
		relDate = new SimpleDateFormat("yyyy-MM-dd")
				.format(stats.getReleaseDate());
		relNum = stats.getReleaseNumber();
		descriptions = ExporterApp.getDescriptions(con);
	}

	/**
	 * Processes exceptions thrown while serving a request.
	 * @param res the servlet response.
	 * @param path the request path (EC number and extension).
	 * @param os the response output stream.
	 * @param e the exception thrown.
	 * @throws IOException
	 */
	private void processException(HttpServletResponse res, String path,
			OutputStream os, Exception e) throws IOException {
		if (os != null){
			res.setContentType("text/plain;charset=UTF-8");
			String error = "ERROR: " + path;
			os.write(error.getBytes());
			os.write('\n');
			os.write(e.getMessage().getBytes());
		}
	}

    /**
     * Gets the EC number from the requested path by removing any extension.
     * @param req the servlet request
     * @return the requested EC number.
     */
    private String getEcString(HttpServletRequest req){
        String ecString = null;
        String path = req.getPathInfo().substring(1); // remove slash
        if (path.matches(".*\\.\\D+")){ // extension
            ecString = path.substring(0, path.lastIndexOf('.'));
        } else {
            ecString = path;
        }
        return ecString;
    }

    /**
     * Deduces the response format from the request, taking into account (in
     * the following order of priorities):
     * <ol>
     * 	<li>the extension appended to the EC number (see {@link ResponseFormat}
     * 		extensions).</li>
     * 	<li><code>format</code> parameter: any of the values of
     * 		{@link ResponseFormat} (case ignored).</li>
     * 	<li><code>Accept</code> HTTP header: it must match the MIME type of any
     * 		of the {@link ResponseFormat}s. In case of several matches, the
     * 		first one takes precedence.</li>
     * 	<li><code>text/xml</code> (IntEnzXML) by default.
     * </ol>
     * @param req a HTTP servlet request.
     * @return a response format.
     */
	private ResponseFormat getResponseFormat(HttpServletRequest req) {
		ResponseFormat rf = ResponseFormat.XML;
		String[] splitPathInfo = req.getPathInfo().substring(1).split("\\.");
        final String last = splitPathInfo[splitPathInfo.length - 1];
        if (last.matches("\\D+")){
			// we have an extension
			rf = ResponseFormat.forExtension(last);
		} else {
			String format = req.getParameter("format");
			if (format != null){
				rf = ResponseFormat.valueOf(format.toUpperCase());
			} else {
				// Try the HTTP header:
				@SuppressWarnings("unchecked")
				Enumeration<String> accept = req.getHeaders("accept");
				if (accept != null){
					headersLoop: while (accept.hasMoreElements()){
                        final String acceptHeader = accept.nextElement();
						for (ResponseFormat f : ResponseFormat.values()) {
                            if (acceptHeader.contains(f.mimeType)){
								rf = f;
								break headersLoop;
							}
						}
					}
				}
			}
		}
		return rf;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request,
	 * 		HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		doGet(req, res);
	}

    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals(
                IntEnzConfig.Property.XML_EXPORTER_POOL_SIZE.toString())){
            while (xmlExporterPool.getNumActive() > 0){ /* wait */ }
            synchronized (xmlExporterPool){
                try {
                    int newValue = (Integer) propertyChangeEvent.getNewValue();
                    /* DOES NOT WORK:
                    // unable to find uk.ac.ebi.intenz.xml.jaxb.ObjectFactory!
                    xmlExporterPool.close();
                    xmlExporterPool = new XmlExporterPool(newValue,
                            descriptions, relNum, relDate);
                    */
                    /* DOES NOT WORK, SAME REASON !!
                    xmlExporterPool.clear();
                    for (int i = 0; i < newValue; i++){
                        xmlExporterPool.addObject();
                    }
                    */
                } catch (Exception e){
                    LOGGER.error("Unable to set XML exporters pool size", e);
                }
            }
        }
    }

}
