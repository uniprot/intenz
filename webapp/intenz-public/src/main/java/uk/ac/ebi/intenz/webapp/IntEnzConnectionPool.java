package uk.ac.ebi.intenz.webapp;

import oracle.jdbc.pool.OracleConnectionCacheImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Tis connection pool uses the Oracle connection pooling implementation.
 *
 * At the moment two physical connections are used for this application. The algorithm for managing the connection
 * is a <code>Fixed Wait</code> algorithm, that is the client will wait as long as needed if all connections
 * are in use.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class IntEnzConnectionPool {

   Logger LOGGER = Logger.getLogger(IntEnzConnectionPool.class);
  /** The one and only INSTANCE. */
  private static IntEnzConnectionPool INSTANCE;

  /** Oracle's implementation of Connection Pooling/Caching will be used. */
  private OracleConnectionCacheImpl ods;

  private IntEnzConnectionPool(String[] dbInitValues) {
     LOGGER.debug("Intialising connection pool.");
    try {
      ods = new OracleConnectionCacheImpl();
      ods.setURL(dbInitValues[0]);
      ods.setDriverType(dbInitValues[1]);
      ods.setUser(dbInitValues[2]);
      ods.setPassword(dbInitValues[3]);
      // Set the limit of phys. connections.
      ods.setMaxLimit(4);
      ods.setCacheScheme(OracleConnectionCacheImpl.FIXED_RETURN_NULL_SCHEME);
    } catch (SQLException e) {
      LOGGER.error(e);
    }
  }
  
  /* Code changed for Oracle 11g (I know, it should have been a branch)
   * LOAD TEST BEFORE DEPLOYING!
    private OracleDataSource ods;

    private IntEnzConnectionPool(String[] dbInitValues) {
       LOGGER.debug("Intialising connection pool.");
      try {
        ods = new OracleDataSource();
        ods.setURL(dbInitValues[0]);
        ods.setDriverType(dbInitValues[1]);
        ods.setUser(dbInitValues[2]);
        ods.setPassword(dbInitValues[3]);
        ods.setConnectionCachingEnabled(true);
        // Set the limit of phys. connections.
        Properties cacheProperties = new Properties();
        cacheProperties.setProperty("MaxLimit", "4"); // Set Max Limit for the Cache  
        OracleConnectionCacheManager connMgr =
      	  OracleConnectionCacheManager.getConnectionCacheManagerInstance();
        connMgr.createCache(ods, cacheProperties);
      } catch (SQLException e) {
        LOGGER.error(e);
      }
    }

  */

  /**
   * This method creates the singleton INSTANCE.
   *
   * @return the INSTANCE.
   */
  public static IntEnzConnectionPool getInstance(String[] dbInitValues) {
    if (INSTANCE == null) {
      synchronized (IntEnzConnectionPool.class) {
        if (INSTANCE == null) {
          INSTANCE = new IntEnzConnectionPool(dbInitValues);
        }
      }
    }

    return INSTANCE;
  }

  /**
   * Releases all connections.
   */
  public void cleanUp() {
    LOGGER.debug("Cleaning up connections.");
    try {
      if (ods != null) ods.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Connection getConnection() throws SQLException {
    Connection con = null;
    LOGGER.debug("Getting connection.");
    try {
      con = ods.getConnection();
			// Use the right schema:
			Statement stm = null;
			try {
				stm = con.createStatement();
				stm.execute("ALTER SESSION SET CURRENT_SCHEMA = \"ENZYME\"");
			} catch (SQLException e){
				LOGGER.error("Unable to change database schema", e);
			} finally {
				if (stm != null) stm.close();
			}
    } catch (SQLException e) {
      LOGGER.error(e);
    }

    return con;
  }
}
