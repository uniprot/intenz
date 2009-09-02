/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.importer;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 * ImportController used to run the importers.
 *
 * @author P.de Matos
 * @version $id 01-Jun-2005 18:38:07
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P.de Matos         01-Jun-2005       Created class<br>
 */
public class ImportController {

   static Logger LOGGER = Logger.getLogger(ImportController.class);
   
   /**
    * Imports links from external databases.
    * @param args A list of databases whose links should be imported.
    *       Allowed values are (letter casing doesn't matter):
    *       <ul>
    *           <li>UNIPROT</li>
    *           <li>GO</li>
    *       </ul>
    *       If no argument is passed, all of the links are imported. 
    * @throws Exception
    * @throws InstantiationException
    * @throws ClassNotFoundException
    */
   public static void main (String[] args) throws Exception, InstantiationException, ClassNotFoundException {
       
       for (int i = 0; i < args.length; i++){
           args[i] = args[i].toLowerCase();
       }
       Arrays.sort(args);
       
       Importer imp = null;

       LOGGER.info("Starting time: "+new Date(System.currentTimeMillis()));

       if (args.length == 0 || Arrays.binarySearch(args, "uniprot") > -1){
           LOGGER.info("Obtaining ProteinImporter...");
//           imp = ImporterFactory.createImporter(ImporterFactory.PROTEIN_IMPORTER);
           imp = ImporterFactory.createImporter(ImporterFactory.KRAKEN_IMPORTER);
           LOGGER.info("Doing import");
           imp.doImport();           
       }
       
       if (args.length == 0 || Arrays.binarySearch(args, "go") > -1){
           LOGGER.info("Obtaining GoImporter...");
           imp = ImporterFactory.createImporter(ImporterFactory.GO_IMPORTER);
           LOGGER.info("Doing import");
           imp.doImport();           
       }
      
      LOGGER.info("Ending time: "+new Date(System.currentTimeMillis()));

   }
}
