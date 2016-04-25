/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import org.apache.log4j.Logger;


/**
 * ApplicationResources provides various property resources required to
 * run the applications.<br/>
 * The application will not run unless their is a file called
 * <em>application.properties</em>. In it the following properties are
 * required.<br/>
 * application.export.fileName<br/>
 * application.input.fileName<br/>
 * application.db.config<br/>
 * application.xml.file<br/>
 *
 * @author P. de Matos
 * @version $id 19-Jul-2005 13:50:34
 *          <p/>
 *          History:<br>
 *          <table>
 *          <tr><th>Developer</th><th>Date</th><th>Description</th></tr>
 *          <tr><td>P.de Matos</td><td>19-Jul-2005</td><td>Created class</td></tr>
 *          </table>
 */
public class ApplicationResources extends PropertyResourceBundle {

   private static Logger LOGGER =
	   Logger.getLogger(ApplicationResources.class.getName());

   static ApplicationResources resources;

   /**
    * Creates a property resource bundle.
    *
    * @param stream property file to read from.
    */
   private ApplicationResources (InputStream stream) throws IOException {
      super(stream);
   }

   /**
    * Singleton method to create a single instance of the application
    * resources.<br/>
    * The system will exit if the application resources is unable to be created.<br/>
    * @return
    */
   public static ApplicationResources getInstance () {
      if ( resources == null ) {
         synchronized ( ApplicationResources.class ) {
            //if ( resources == null ) {
               try {
				   //FileInputStream in = new FileInputStream(
                   //     ApplicationResources.class.getClassLoader().getResource("application.properties").getPath());
				   InputStream in = ApplicationResources.class.getClassLoader()
					   .getResourceAsStream("application.properties");
                  resources = new ApplicationResources(in);
               } catch ( IOException e ) {
                  LOGGER.fatal("Application properties file could not be loaded.", e);
                  System.exit(1);
               }
            //}
         }
      }
      return resources;
   }

   // GETTERS //

   public String getDbUrl(){
       return resources.getString("application.db.url");
   }

   public String getDbUserName(){
      return resources.getString("application.db.login");
   }

   public String getDbPassword(){
      return resources.getString("application.db.password");
   }

    public String getDbConfig(){
        return resources.getString("application.db.config");
    }

   public String getInputFlatFileName(){
      return resources.getString("application.input.fileName");
   }

   public String getExportFlatFileName(){
      return resources.getString("application.export.fileName");
   }

   public String getSpecialCharactersFileName(){
      return resources.getString("application.xml.file");
   }

}
