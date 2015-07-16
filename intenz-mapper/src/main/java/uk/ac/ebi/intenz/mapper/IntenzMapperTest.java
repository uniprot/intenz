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
package uk.ac.ebi.intenz.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author joseph
 */
public class IntenzMapperTest {
    
    public static void main(String args[]) throws ClassNotFoundException{
        System.out.println("ok");
        
          String userHome = System.getProperty("user.home");
        
        // tell the driver where to look for the TNSNAMES.ORA file
            System.setProperty(
                    "oracle.net.tns_admin",
                    userHome + "/tns_admin");
 
    // ORCL is net service name from the TNSNAMES.ORA file

    //String dbURL = "jdbc:oracle:thin:@ora-dlvm5-037.ebi.ac.uk:1561:VENZDEV";
    
      String dbURL = "jdbc:oracle:thin:@ora-vm-050.ebi.ac.uk:1551:UZPDEV";
 
    // load the driver
    Class.forName("oracle.jdbc.OracleDriver");
 
    Connection conn = null;
    Statement stmt = null;
        System.out.println("URL "+ dbURL);
 
    try {
      conn = DriverManager.getConnection(dbURL,
                                         "enzyme",
                                         "delphi");
      
        System.out.println("CONNECTION "+ conn);
 
      stmt = conn.createStatement();
 
      ResultSet rs = stmt.executeQuery("SELECT dummy FROM dual");
 
      if (rs.next()) {
        System.out.println("Dummy is equal to: " + rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (stmt != null) try { stmt.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
    
}
