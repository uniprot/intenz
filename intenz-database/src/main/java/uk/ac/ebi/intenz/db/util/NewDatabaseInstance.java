package uk.ac.ebi.intenz.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class NewDatabaseInstance {
	private static final String SERVICE_NAME = "serviceName";
	private static final String INSTANCE = "instance";
	private static final String PORT = "port";
	private static final String HOST = "host";
	private static final String SCHEMA = "schema";
	private static final String PASSWORD = "password";
	private static final String USER = "user";
	private static final String TYPE = "type";
	private static Logger logger = Logger.getLogger(NewDatabaseInstance.class);
	private final Properties properties;
	private static final String URL_ORACLE = "jdbc:oracle:thin:@$host$:$port$:$name$";
	private static final String URL_ORACLE_SN = "jdbc:oracle:thin:@//$host$:$port$/$name$";
	private static final String URL_MYSQL = "jdbc:mysql://$host$:$port$/$name$";
	private static final String URL_POSTGRES = "jdbc:postgresql://$host$:$port$/$name$\\?currentSchema=$schema$";

	private static final String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
	private static final String DRIVER_MYSQL = "com.mysql.cj.jdbc.Driver";
	private static final String DRIVER_POSTGRES = "org.postgresql.Driver";

	private static final List<String> VALID_KEYS = Arrays.asList(USER, PASSWORD, HOST, PORT, TYPE, INSTANCE,
			SERVICE_NAME, SCHEMA);

	private static Map<String, String> jdbcDrivers = new HashMap<>();
	private static Map<String, String> connectionURLs = new HashMap<>();
	static {
		connectionURLs.put("ORACLE", URL_ORACLE);
		connectionURLs.put("MYSQL", URL_MYSQL);
		connectionURLs.put("POSTGRES", URL_POSTGRES);
		jdbcDrivers.put("ORACLE", DRIVER_ORACLE);
		jdbcDrivers.put("MYSQL", DRIVER_MYSQL);
		jdbcDrivers.put("POSTGRES", DRIVER_POSTGRES);
	}

	public Connection getConnection() {
		Connection con = null;
		String url = getUrl();
		String type = getDbType();
		String driver = jdbcDrivers.get(type.toUpperCase());
		String user =getUser();
		String name =getInstance();
		String password = getPassword();
		logger.info("Connecting to " + user + "@" + name);
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			logger.info("Connected to " + user + "@" + name);
		} catch (ClassNotFoundException e) {
			logger.error("Database driver not found: " + driver);
		} catch (SQLException e) {
			logger.error("Unable to establish connection", e);
		}
		if (con != null && !getSchema().equals(getUser())){
			// Use the right schema:
			Statement stm = null;
			try {
				stm = con.createStatement();
				stm.execute("ALTER SESSION SET CURRENT_SCHEMA = \"" + getSchema().toUpperCase() + "\"");
			} catch (SQLException e){
				logger.error("Unable to change database schema", e);
			} finally {
				if (stm != null) try {
					stm.close();
				} catch (SQLException e){}
			}
		}

		return con;
	}

	protected NewDatabaseInstance(Properties properties) {
		this.properties = properties;
	}

	public static NewDatabaseInstance getInstance(String fileName) throws IOException {
		Properties properties = loadProperties(fileName);

		return new NewDatabaseInstance(properties);
	}

	public String getUrl() {
		String type =getDbType();
		String url = connectionURLs.get(type.toUpperCase());
		String name = getInstance();
		String serviceName = getServiceName();

		if (type.equalsIgnoreCase("Oracle") && serviceName != null) {
			url = URL_ORACLE_SN;
			name = serviceName;
		}
		url = url.replace("$host$", getHost());
		url = url.replace("$port$", getPort());

		url = url.replace("$name$", name);
		url = url.replace("$schema$",getSchema());

		return url;
	}
	public String getDbType() {
		return properties.getProperty(TYPE);
	}
	public String getUser() {
		return properties.getProperty(USER);
	}
	public String getPassword() {
		return properties.getProperty(PASSWORD);
	}
	public String getSchema() {
		return properties.getProperty(SCHEMA);
	}
	public String getHost() {
		return properties.getProperty(HOST);
	}
	public String getPort() {
		return properties.getProperty(PORT);
	}
	public String getInstance() {
		return properties.getProperty(INSTANCE);
	}
	
	public String getServiceName() {
		return properties.getProperty(SERVICE_NAME);
	}
	
	static Properties loadProperties(String fileName) throws IOException {
		InputStream is = NewDatabaseInstance.class.getClassLoader().getResourceAsStream(fileName + ".properties");
		if (is == null) {
			String message = "Unable to open configuration file " + fileName;
			logger.error(message);
			throw new IOException(message);
		}
		Properties properties = new Properties();
		properties.load(is);
		Properties newProperties = updateProperties(properties);
		String schema = newProperties.getProperty(SCHEMA);
		if (schema == null) {
			newProperties.setProperty(SCHEMA, newProperties.getProperty(USER));
		}

		String type = newProperties.getProperty(TYPE);
		if (type == null) {
			newProperties.setProperty(TYPE, "Oracle");
		}

		return newProperties;
	}

	private static Properties updateProperties(Properties properties) {
		Properties newProperties = new Properties();
		for (String key : VALID_KEYS) {
			for (Object pkey : properties.keySet()) {
				if (pkey.toString().contains(key)) {
					newProperties.setProperty(key, properties.getProperty(pkey.toString()));
				}
			}
		}
		return newProperties;

	}
}
