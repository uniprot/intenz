package uk.ac.ebi.intenz.webapp.helper;

import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/02/22 13:53:19 $
 */
public class EnzymeNamesHelper {

   private static final Logger LOGGER =
	   Logger.getLogger(EnzymeNamesHelper.class.getName());
   private static EnzymeNameComparator ENZYME_NAME_CASING_COMPARATOR =
         new EnzymeNameComparator();

   private static Comparator STRING_REVERSE_CASING_COMPARATOR =
         StringUtil.getStringReverseCasingComparator();

   public static String getIntEnzCommonName (List commonNames) {
      for ( int iii = 0; iii < commonNames.size(); iii++ ) {
         EnzymeNameDTO commonName = (EnzymeNameDTO) commonNames.get(iii);
         if ( EnzymeViewConstant.isInIntEnzView(commonName.getView()) ) return commonName.getName();
      }
      return "";
   }
   
   public static Comparator getEnzymeNameSortingComparator(){
      return ENZYME_NAME_CASING_COMPARATOR;
   }

   public static Comparator getStringCasingComparator(){
      return STRING_REVERSE_CASING_COMPARATOR;
   }

   /**
    * This class is a comparator used to provide comparison of data in
    * a case sensitive ordering of [aA-zA]. It is used primarily in SIB data
    * to export data in the correct ordering.
    *
    * @author P. de Matos
    *         
    */
   private static class EnzymeNameComparator implements Comparator {

      /**
       * This method accepts two EnzymeName objects and compares them. A class
       * cast exception is thrown if the objects are not EnzymeName objects.<br>
       * All sorting is done according to the [aA-zZ] algorithm of sorting</br>
       *
       * @param o
       * @param o1
       * @return the value <code>0</code> if the argument string is equal to
       *         this string; a value less than <code>0</code> if this string
       *         is lexicographically less than the string argument; and a
       *         value greater than <code>0</code> if this string is
       *         lexicographically greater than the string argument. The exception 
       *         occurs when the objects are identical except for casing, in which
       *         case the lower cased one always has a higher int value.
       */
      public int compare (Object o, Object o1) {
         EnzymeName enzymeName0 = (EnzymeName) o;
         EnzymeName enzymeName1 = (EnzymeName) o1;

         return STRING_REVERSE_CASING_COMPARATOR.compare(enzymeName0.getName(), enzymeName1.getName());
      }

   }

}
