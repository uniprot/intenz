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
import java.sql.Connection;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;

/**
 *
 * @author joseph
 */
public class IntEnzRheaCompoundDbReaderTest {

    private IntEnzRheaCompoundDbReader instance;
    private static final String SQL_FILE = "uk.ac.ebi.intenz.rhea.mapper.db.IntEnzRheaCompoundDbReader.sql";
    private final static String db = "intenz-db-dev";
     private static volatile Connection connection;
    
    
        public static Connection getConnection() throws IOException {
        if (connection == null) {
            synchronized (SQLLoader.class) {
                if (connection == null) {
                    connection = OracleDatabaseInstance.getInstance(db)
                            .getConnection();

                }
            }
        }
        return connection;
    }

    @Before
    public void setUp() throws IOException {

////        only used when IDE cannot get System environment variables
//        String userHome = System.getProperty("user.home");
//        System.setProperty(
//                "oracle.net.tns_admin",
//                userHome + "/tns_admin");

        SQLLoader sQLLoader = SQLLoader.getSQLLoader(SQL_FILE);
        instance = new IntEnzRheaCompoundDbReader(getConnection(), sQLLoader);
    }

    @After
    public void tearDown() {

    }

    /**
     * Test of getSqlLoader method, of class IntEnzRheaCompoundDbReader.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetSqlLoader() throws IOException {
        System.out.println("getSqlLoader");
        SQLLoader result = instance.getSqlLoader();
        assertNotNull(result);

    }

    /**
     * Test of getConnection method, of class IntEnzRheaCompoundDbReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        Connection result = instance.getConnection();
        assertNotNull(result);

    }

}
