/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.xchars.domain.EncodingType;
import java.util.Iterator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;
import org.apache.log4j.Logger;


/**
 * DELineTest
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 13:19:51
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public class DELineTest extends BaseLineTest {

   Logger LOGGER = Logger.getLogger(DELineTest.class);

   public void populateTests(){
      dataHolders.add( new DataHolder("1.1.1.196", "15-hydroxyprostaglandin-D dehydrogenase (NADP<smallsup>+</smallsup>)",
            "15-hydroxyprostaglandin-D dehydrogenase (NADP(+))"));
      dataHolders.add(new DataHolder("1.1.1.51",
            "3(or 17)<greek>beta</greek>-hydroxysteroid dehydrogenase",
            "3(or 17)-beta-hydroxysteroid dehydrogenase"));
      dataHolders.add( new DataHolder("1.1.1.53",
            "3<greek>alpha</greek>(or 20<greek>beta</greek>)-hydroxysteroid dehydrogenase",
            "3-alpha-(or 20-beta)-hydroxysteroid dehydrogenase"));
      dataHolders.add( new DataHolder("1.1.1.100",
            "3-oxoacyl-<protein>acyl-carrier-protein</protein> reductase",
            "3-oxoacyl-[acyl-carrier-protein] reductase"));
      dataHolders.add(new DataHolder("1.1.1.145",
      		"3<greek>beta</greek>-hydroxy-<greek>Delta</greek><smallsup>5</smallsup>-steroid dehydrogenase",
        		"3-beta-hydroxy-Delta(5)-steroid dehydrogenase") );
      dataHolders.add ( new DataHolder("2.7.1.88",
             "dihydrostreptomycin-6-phosphate 3'<greek>alpha</greek>-kinase",
             "Dihydrostreptomycin-6-phosphate 3'-alpha-kinase"));
      dataHolders.add ( new DataHolder("2.7.4.13",
            "(deoxy)nucleoside-phosphate kinase",
            "(Deoxy)nucleoside-phosphate kinase"));
      dataHolders.add(new DataHolder("3.2.1.52",
            "<greek>beta</greek>-<element>N</element>-acetylhexosaminidase",
            "Beta-N-acetylhexosaminidase"));
      dataHolders.add(new DataHolder("3.4.13.20",
            "<greek>beta</greek>-Ala-His dipeptidase",
            "Beta-Ala-His dipeptidase"));
   }

   public DELineTest(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();
     populateTests();
  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

   /**
    * Tests all the reactions provided in the reaction map.
    */
   public void testData(){
      Iterator iter = dataHolders.iterator();
      while ( iter.hasNext() ) {
         DataHolder nameData = (DataHolder) iter.next();
         String translatedString = sc.xml2Display( translator.toASCII(nameData.getIntenzData(), false, false), EncodingType.SWISSPROT_CODE);
         if( !translatedString.equals(nameData.getEnzymeData()) ){
            LOGGER.error("ASSERT FALSE - EC "+nameData.getEC()+"  ORIGINAL IntEnz XML \n"+nameData.getIntenzData()+" : \n"+translatedString+" SHOULD BE \n"+nameData.getEnzymeData());
            assertFalse(true);
         }
      }
   }
}
