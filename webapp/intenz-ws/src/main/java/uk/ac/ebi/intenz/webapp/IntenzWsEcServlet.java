package uk.ac.ebi.intenz.webapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

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
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.stats.db.IntEnzDbStatistics;
import uk.ac.ebi.intenz.tools.export.ExporterApp;
import uk.ac.ebi.intenz.tools.export.XmlExporter;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.util.EnzymeEntryMapperPool;
import uk.ac.ebi.intenz.webapp.util.XmlExporterPool;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * Main servlet for IntEnz web services.
 */
public class IntenzWsEcServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4868575459199989086L;
	
	private XmlExporterPool xmlExporterPool;
	private EnzymeEntryMapperPool mapperPool;
	
	private static enum ResponseFormat {
		/** <a href="http://intenz.sf.net/intenz-xml">IntEnz XML</a>. */
		XML("xml", "application/xml"),
		/** <a href="http://biopax.org">BioPAX level 2</a>. */
		BIOPAX_L2("owl", "application/rdf+xml"),
		ENZYME("txt", "text/plain");
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

	private Logger LOGGER = Logger.getLogger(IntenzWsEcServlet.class);
	
	private DataSource ds;

	/**
	 * Object containing release number and date.
	 */
	private IntEnzDbStatistics stats;
	
	private String relNum, relDate;

	private Map<String, Object> descriptions;
	
	/**
	 * Creates the naming context, loads shared data from the database and
	 * creates a pool of XML exporters.
	 */
	@Override
	public void init() throws ServletException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup(
					getServletContext().getInitParameter("intenz.data.source"));
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
			int numOfExporters = Integer.parseInt(getServletContext()
					.getInitParameter("xml.exporters.pool.size"));
			xmlExporterPool = new XmlExporterPool(numOfExporters, descriptions);
			int numOfMappers = Integer.parseInt(getServletContext().
					getInitParameter("mappers.pool.size"));
			mapperPool = new EnzymeEntryMapperPool(numOfMappers);
		} catch (Exception e) {
			LOGGER.error("Unable to create XML exporters pool", e);
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
	 * 		subclasses, subsubclasses or partial EC numbers are not.</li>
	 * 	<li><code>{format}</code> is the extension corresponding to the
	 * 		requested format (see {@link ResponseFormat} enumeration). This can
	 * 		be replaced by a <code>format</code> request parameter or a valid
	 * 		<code>Accept</code> HTTP header.</li>
	 * </ul>
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		String path = req.getPathInfo().substring(1); // remove slash
		String ecString = path.matches("(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\..+")?
				path.substring(0, path.lastIndexOf('.')) : path;
		Connection con = null;
		OutputStream os = null;
		EnzymeEntryMapper mapper = null;
		try {
			res.setCharacterEncoding("UTF-8");
			os = res.getOutputStream();
			EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ecString);
			Status status = null;
			switch (ec.getType()) {
			case PRELIMINARY:
				status = Status.PRELIMINARY;
				break;
			case ENZYME:
				status = Status.APPROVED;
				break;
			default:
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				throw new IllegalArgumentException(
						"EC number not valid/supported: " + path);
			}
			con = ds.getConnection();

			// The enzyme
			mapper = mapperPool.borrowObject();
			EnzymeEntry enzyme = mapper.findByEc(
					ec.getEc1(), ec.getEc2(), ec.getEc3(), ec.getEc4(),
					status, con);

			ResponseFormat rf = getResponseFormat(req);
			res.setContentType(rf.mimeType);
			switch (rf) {
			case XML:
				if (stats == null) loadData(con);
				XmlExporter xmlExporter = null;
				try {
					xmlExporter = xmlExporterPool.borrowObject();
					xmlExporter.export(enzyme, relNum, relDate, os);
				} finally {
					if (xmlExporter != null){
						xmlExporterPool.returnObject(xmlExporter);
					}
				}
				break;
			case BIOPAX_L2:
				Biopax.write(Collections.singleton(enzyme), os);
				break;
			case ENZYME:
				EnzymeFlatFileWriter.export(SibEntryHelper.getSibEnzymeEntry(
						enzyme, SpecialCharacters.getInstance(null),
						EncodingType.SWISSPROT_CODE),
						new OutputStreamWriter(os));
				break;
			}
			res.setStatus(HttpServletResponse.SC_OK);
		} catch (IllegalArgumentException e){
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			processException(res, path, os, e);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			processException(res, path, os, e);
		} finally {
			res.flushBuffer();
			if (mapper != null)
				try {
					mapperPool.returnObject(mapper);
				} catch (Exception e) {
					LOGGER.error("Unable to return mapper to pool", e);
				}
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
		relNum = String.valueOf(stats.getReleaseNumber());
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
			res.setContentType("text/plain");
			String error = "ERROR: " + path;
			LOGGER.error(error, e);
			os.write(error.getBytes());
			os.write('\n');
			os.write(e.getMessage().getBytes());
		}
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
		String[] splitPathInfo = req.getPathInfo().split("\\.");
		if (splitPathInfo.length > 4){
			// we have an extension
			rf = ResponseFormat.forExtension(splitPathInfo[4]);
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
						for (ResponseFormat f : ResponseFormat.values()) {
							if (accept.nextElement().contains(f.mimeType)){
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

}
