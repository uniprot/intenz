/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator.helper;


/**
 * DataHolder holds data for the tests.
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 13:20:50
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public class DataHolder {

   private String EC;
   private String intenzData;
   private String enzymeData;

   public DataHolder (String EC, String intenzData, String enzymeData) {
      this.EC = EC;
      this.intenzData = intenzData;
      this.enzymeData = enzymeData;
   }

   public String getEC () {
      return EC;
   }

   public String getIntenzData () {
      return intenzData;
   }

   public String getEnzymeData () {
      return enzymeData;
   }
}
