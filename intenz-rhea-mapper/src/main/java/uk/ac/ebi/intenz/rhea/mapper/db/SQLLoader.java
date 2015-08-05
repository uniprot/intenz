/*
 * Copyright 2015 EMBL-EBI.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.intenz.rhea.mapper.db;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;

/**
 *
 * @author joseph
 */
public final class SQLLoader {
    
 private final Properties statementsSql;
    
    private final Logger LOGGER = Logger.getLogger(SQLLoader.class);

    /**
     * A mapping from String - SQL double-dash comments - to already open
     * prepared statements.
     */
    protected Map<String,PreparedStatement> statementsMap;
    
    private Connection connection;

    /**
     * Loads the SQL from file. The file must follow these conventions:
     * <ul>
     * 	<li>It must be named <code>&lt;aClass&gt;.sql</code>,
     * 		<code>&lt;aClass&gt;</code> being the fully qualified name
     * 		of the parameter aClass</li>
     * 	<li>It is actually a Java properties file, the keys being SQL
     * 		double-dash comments (these are convenient while editing).</li>
     * 	<li>The keys - including the double dash - are to be used as parameters
     * 		with the {@link #getPreparedStatement(String)} method in order to
     * 		retrieve the corresponding PreparedStatement object.</li>
     * 	<li>Being a properties file, don't forget the \ character in case
     * 		the key/value pair spans more than one line!</li>
     * </ul>
     * Example:
     * <pre>
     * --my.insert.1:\
     * INSERT INTO my_table (col1, col2) VALUES (?, ?)
     * 
     * --my.select.2:\
     * SELECT a, b FROM my_table \
     * 	WHERE c LIKE ?
     * </pre>
     * @param aClass the class which requires the SQL.
     * @param con the connection to prepare statements with.
     * @throws IOException in case of problems reading the SQL file.
     */
    public SQLLoader(Class<?> aClass, Connection con) throws IOException {
        statementsSql = new Properties();
 
        
        statementsSql.load(aClass.getClassLoader()
                .getResourceAsStream(aClass.getName() + ".sql"));
      
        statementsMap = new HashMap<String, PreparedStatement>();
        this.connection = con;
    }
    
    
        /**
     * Loads the SQL from file. The file must follow these conventions:
     * <ul>
     * 	<li>It must be named <code>&lt;aClass&gt;.sql</code>,
     * 		<code>&lt;aClass&gt;</code> being the fully qualified name
     * 		of the parameter aClass</li>
     * 	<li>It is actually a Java properties file, the keys being SQL
     * 		double-dash comments (these are convenient while editing).</li>
     * 	<li>The keys - including the double dash - are to be used as parameters
     * 		with the {@link #getPreparedStatement(String)} method in order to
     * 		retrieve the corresponding PreparedStatement object.</li>
     * 	<li>Being a properties file, don't forget the \ character in case
     * 		the key/value pair spans more than one line!</li>
     * </ul>
     * Example:
     * <pre>
     * --my.insert.1:\
     * INSERT INTO my_table (col1, col2) VALUES (?, ?)
     * 
     * --my.select.2:\
     * SELECT a, b FROM my_table \
     * 	WHERE c LIKE ?
     * </pre>
     * @param sqlFile the .sql file
     * @throws IOException in case of problems reading the SQL file.
     */
    public SQLLoader(String sqlFile) throws IOException {
        statementsSql = new Properties();
 
// this is the default file loaded in the statement in this contenxt       
//             statementsSql.load(SQLLoader.class.getClassLoader()
//                .getResourceAsStream("uk.ac.ebi.intenz.rhea.mapper.db.IntEnzRheaCompoundDbReader.sql"));   
        
             statementsSql.load(SQLLoader.class.getClassLoader()
                .getResourceAsStream(sqlFile));
        
        statementsMap = new HashMap<String, PreparedStatement>();
        this.connection = getConnection();
    }

    public Connection getConnection() throws IOException {
        connection = OracleDatabaseInstance.getInstance("intenz-db-dev")
                .getConnection(); 
        
        return connection;
    }
    
    

    /**
     * Retrieves the prepared statement from the internal map. If not already
     * there, it uses the connection to prepare it and stores it for later
     * use.
     * @param key the key used in the SQL file as a comment
     * 	(see {@link #SQLLoader(Class, Connection)}).
     * @return a prepared statement (can be a {@link CallableStatement} if
     * 		the SQL is recognised as such).
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String key)
    throws SQLException {
        return getPreparedStatement(key, null, (Object[]) null);
    }

    /**
     * Retrieves the prepared statement from the internal map. If not already
     * there, this method substitutes any passed parameters, uses the
     * connection to prepare it and stores it for later use.<br/>
     * Note that calling this method more than once with the same key but
     * different parameters will create (if needed) and retrieve a new
     * PreparedStatement. The internal map will use the key <i>and</i> the
     * parameters to store it.
     * @param key the key used in the SQL file as a comment
     *      (see {@link #SQLLoader(Class, Connection)}).
     * @param params Parameters to substitute in the SQL string (see
     *      MessageFormat documentation). These are useful to define
     *      for example the database schema we are working with:
     *      <pre><code>
     *      --my.select.3:\
     *      SELECT * FROM {0}.table1
     *      
     *      // result: "SELECT * FROM myDbSchema.table1"
     *      mySqlLoader.getPreparedStatement("--my.select.3", "myDbSchema");
     *      </code></pre>
     *      
     *      Other keys in the SQL file can be used as parameters here, for
     *      example to introduce some constraint to a query:<br/>
     *      <pre><code>
     *      --my.select.4:\
     *      SELECT * FROM {0}.table1 {1}
     *
     *      --my.constraint.1:\
     *      WHERE col1 IS NOT NULL
     *
     *      // result: "SELECT * FROM myDbSchema.table1 WHERE col1 IS NOT NULL"
     *      mySqlLoader.getPreparedStatement("--my.select.4", "myDbSchema",
     *          "--my.constraint.1");
     *      </code></pre>
     *      <b>Warning:</b> single quotes in this kind of parameterised
     *      queries should be escaped (using another quote) so that
     *      MessageFormat won't break the final SQL syntax. Example:
     *      <pre><code>
     *      --my.select.5:\
     *      SELECT * FROM {0}.table1 WHERE col1 = ''blah''
     *      </code></pre>
     *
     * @return a prepared statement (can be a {@link CallableStatement} if
     * 		the SQL is recognised as such).
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String key, Object... params)
    throws SQLException {
        return getPreparedStatement(key, null, params);
    }
    
    /**
     * Retrieves a prepared statement which can return auto-generated keys and
     * can be parameterised (see {@link #getPreparedStatement(String, Object[])}).
     * @param key the key used in the SQL file as a comment
     *      (see {@link #SQLLoader(Class, Connection)}).
     * @param returnedKeys the column names of the auto-generated keys. They can
     * 		be retrieved later from the ResultSet Statement.getGeneratedKeys()
     * 		[NOTE: it seems to work only using column indexes, not names, i.e.
     * 		even if <code>returnedKeys = { "foo" }</code>,
     * 		use <code>stm.getGeneratedKeys().getString(1)</code> instead of
     * 		<code>stm.getGeneratedKeys().getString("foo")</code>]
     * @param params Parameters to substitute in the SQL string (see
     *      {@link #getPreparedStatement(String, Object...)}).
     * @return a prepared statement (can be a {@link CallableStatement} if
     * 		the SQL is recognised as such).
     * @throws SQLException
     */
    public PreparedStatement getPreparedStatement(String key,
            String[] returnedKeys, Object... params)
    throws SQLException {
        String completeKey = new StringBuilder(key)
        	.append(returnedKeys == null? "" : Arrays.asList(returnedKeys))
        	.append(params == null? "" : Arrays.asList(params))
        	.toString();
        Object[] processedParams = null;
        int i = 0;
        if (params != null){
        	// Process parameters which can be substituted with SQL code:
            processedParams = new Object[params.length];
            for (Object o : params){
                String s = null;
                if (o instanceof String && o.toString().startsWith("--")){
                    s = statementsSql.getProperty(o.toString());
                }
                processedParams[i++] = s != null? s : o;
            }
        }
        if (!statementsMap.containsKey(completeKey)){
            String sql = (params == null)?
                statementsSql.getProperty(key) :
                MessageFormat.format(statementsSql.getProperty(key), processedParams);
            //if (sql.endsWith(";")) sql = sql.substring(0, sql.length()-1);
            statementsMap.put(completeKey, sql.startsWith("{CALL ")?
                connection.prepareCall(sql) :
                returnedKeys == null?
            		connection.prepareStatement(sql) :
        			connection.prepareStatement(sql, returnedKeys));
        }
        return statementsMap.get(completeKey);
    }

    /**
     * Closes every PreparedStatement already open, and removes every reference
     * to them. Note that the connection is not closed by this method!
     * @throws SQLException
     */
    public void close() throws SQLException {
        for (PreparedStatement stm : statementsMap.values()){
            try {
                stm.close();
            } catch (SQLException e){
                // Hack to avoid errors when closing a statement already closed
                // (allowed according to the Statement interface).
                LOGGER.error("Unable to close statement", e);
            }
        }
        statementsMap.clear();
    }

    /**
     * Changes the connection used to prepare statements, after having
     * closed any open ones.
     * @param con a database connection
     * @throws SQLException
     */
    public void setConnection(Connection con) throws SQLException {
        close();
        this.connection = con;
    }
}
