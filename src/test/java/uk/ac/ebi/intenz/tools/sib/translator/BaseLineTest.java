/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator;

import junit.framework.TestCase;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import uk.ac.ebi.intenz.tools.sib.translator.helper.DataHolder;
import org.apache.log4j.Logger;


/**
 * BaseLineTest
 *
 * @author P. de Matos
 * @version $id 24-Jun-2005 13:29:18
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          P. de Matos        24-Jun-2005           Created class<br>
 */
public abstract class BaseLineTest extends TestCase {

   Logger LOGGER = Logger.getLogger(BaseLineTest.class);
   XCharsASCIITranslator translator;
   SpecialCharacters sc;
   List dataHolders = new ArrayList();

   public BaseLineTest (String s) {
      super(s);
   }

   public void setUp() throws Exception {
    super.setUp();
     populateTests();
    translator = XCharsASCIITranslator.getInstance();
     String path = CALineTest.class.getClassLoader().getResource("specialCharacters.xml").getPath();
     sc = SpecialCharacters.getInstance(path.replaceFirst("specialCharacters.xml", ""), "specialCharacters.xml");

  }

  public void tearDown() throws Exception {
    super.tearDown();
  }

   public void testData(){
      Iterator iter = dataHolders.iterator();
      while ( iter.hasNext() ) {
         DataHolder reactionData = (DataHolder) iter.next();
         boolean ca = this instanceof CALineTest;
         boolean de = this instanceof DELineTest;
         String translatedString = sc.xml2Display( translator.toASCII(reactionData.getIntenzData(), ca, de), EncodingType.SWISSPROT_CODE);
         if( !translatedString.equals(reactionData.getEnzymeData()) ){
            LOGGER.error("ASSERT FALSE - EC "+reactionData.getEC()+"  ORIGINAL IntEnz XML \n"+reactionData.getIntenzData()+"\n"+translatedString+" SHOULD BE \n"+reactionData.getEnzymeData());
            assertFalse(true);
         }
      }

   }

   public abstract void populateTests();
}
