/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.webapp.utilities;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;

import org.apache.log4j.Logger;


/**
 * TestIntEnzUtilities tests the IntEnzUtilities class as there were some errors
 * with pattern matching hyperlink tags and EC numbers.<br>
 *
 * @author pmatos
 * @version $id 19-May-2005 11:41:30
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          pmatos            19-May-2005         Created class<br>
 */
public class TestIntEnzUtilities extends TestCase {

   Logger LOGGER = Logger.getLogger(TestIntEnzUtilities.class.getName());

   /** TESTS **/

   /** METHOD: linkUnmarkedEC (String text) **/
   /**
    * Tests a null parameter on linkUnmarkedEC
    */
   public void testLinkUnmarkedECNull () {
      LOGGER.info("Running testLinkUnmarkedECNull ...");
      try {
         IntEnzUtilities.linkUnmarkedEC(null);
         // should not get here
         assertFalse(true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertTrue(true);
      }

   }

   /**
    * Tests a valid EC number on linkUnmarkedEC
    */
   public void testLinkUnmarkedECValid () {
      LOGGER.info("Running testLinkUnmarkedECValid ...");
      String text = "This is new text EC 1.1.1.1 and another.";
      String textCorrect = "This is new text EC <a href=\"searchEc.do?ec=1.1.1.1&view=INTENZ\">1.1.1.1</a> and another.";
      try {
         text = IntEnzUtilities.linkUnmarkedEC(text);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textCorrect);
   }

   /**
    * Tests a multiple valid EC numbers on linkUnmarkedEC
    */
   public void testLinkUnmarkedECValidMultiple () {
      LOGGER.info("Running testLinkUnmarkedECValidMultiple ...");
      String text = "This is new text EC 1.1.1.1 and EC 1.1.34.2 another.";
      String textCorrect = "This is new text EC <a href=\"searchEc.do?ec=1.1.1.1&view=INTENZ\">1.1.1.1</a> and EC " +
            "<a href=\"searchEc.do?ec=1.1.34.2&view=INTENZ\">1.1.34.2</a> another.";
      try {
         text = IntEnzUtilities.linkUnmarkedEC(text);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textCorrect);
   }

   /**
    * Tests a multiple invalid EC numbers on linkUnmarkedEC
    */
   public void testLinkUnmarkedECInValidMultiple () {
      LOGGER.info("Running testLinkUnmarkedECValidMultiple ...");
      String textOrig = "This is new text EC 1.1.1. and EC 1.1.34. another.";
      String textNew = null;
      try {
         textNew = IntEnzUtilities.linkUnmarkedEC(textOrig);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(textOrig, textNew);
   }

   /** METHOD: linkMarkedEC(String string2Check, boolean linkUnmarkedECs) **/
   /**
    * Tests that a string containing no EC is not changed
    */
   public void testLinkMarkedECwithNoEC(){
      LOGGER.info("Running testLinkMarkedECwithNoEC ...");
      String textOrig = "This is new text <a href=\"testing?parmt=2\">Glycomate</a> and another.";
      String textNew = null;
      try {
         textNew = IntEnzUtilities.linkMarkedEC(textOrig, false);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(textOrig, textNew);
   }

   /**
    * Tests that a null value exception thrown
    */
   public void testLinkMarkedECNullValue(){
      LOGGER.info("Running testLinkMarkedECNullValue ...");
      try {
         IntEnzUtilities.linkMarkedEC(null, false);

         // we should never get here
         assertFalse(true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertTrue(true);
      }
   }

   /**
    * Tests that string is correctly changed when a single EC is present.
    */
   public void testLinkMarkedCorrectECSingleEc(){
      LOGGER.info("Running testLinkMarkedCorrectECSingleEc ...");
      String text = "NAD can be used as cofactors with similar efficiency, unlike " +
            "EC <a href=\"handler?cmd=SearchEC&ec=1.2.1.12\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and EC glyceraldehyde.";
      String textNew = "NAD can be used as cofactors with similar efficiency, unlike " +
            "EC <a href=\"searchEc.do?ec=1.2.1.12&view=INTENZ\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and EC glyceraldehyde.";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, false);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }

   /**
    * Tests that string is correctly changed when a single EC is present but with
    * nothing between the href.
    */
   public void testLinkMarkedCorrectECSingleEcEmptyHref(){
      LOGGER.info("Running testLinkMarkedCorrectECSingleEc ...");
      String text = "NAD can be used as cofactors with similar efficiency, unlike " +
            "EC <a href=\" \">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and EC glyceraldehyde.";
      String textNew = "NAD can be used as cofactors with similar efficiency, unlike " +
            "EC <a href=\"searchEc.do?ec=1.2.1.12&view=INTENZ\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and EC glyceraldehyde.";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, false);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }

   /**
    * Tests that a string is correctly changed when multiple ECs are present.
    */
   public void testLinkMarkedCorrectECMultipleEc(){
      LOGGER.info("Running testLinkMarkedCorrectECMultipleEc ...");
      String text = "NAD can be used as cofactors with similar efficiency, unlike EC " +
            "<a href=\"handler?cmd=SearchEC&ec=1.2.1.12\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and (phosphorylating) " +
            "and EC <a href=\"handler?cmd=SearchEC&ec=1.2.1.13\">1.2.1.13</a> glyceraldehyde.";
      String textNew = "NAD can be used as cofactors with similar efficiency, unlike EC " +
            "<a href=\"searchEc.do?ec=1.2.1.12&view=INTENZ\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and (phosphorylating) " +
            "and EC <a href=\"searchEc.do?ec=1.2.1.13&view=INTENZ\">1.2.1.13</a> glyceraldehyde.";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, false);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }

   /**
    * Tests that a string is correctly changed when multiple ECs are present
    * aswell as when an unmarked EC is present.
    */
   public void testLinkMarkedCorrectECMultipleEcPlusUnmarkedEC(){
      LOGGER.info("Running testLinkMarkedCorrectECMultipleEc ...");
      String text = "NAD can be used as cofactors with similar efficiency, unlike EC " +
            "<a href=\"handler?cmd=SearchEC&ec=1.2.1.12\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and (phosphorylating) " +
            "and EC 1.1.1.1 "+
            "and EC <a href=\"handler?cmd=SearchEC&ec=1.2.1.13\">1.2.1.13</a> glyceraldehyde.";
      String textNew = "NAD can be used as cofactors with similar efficiency, unlike EC " +
            "<a href=\"searchEc.do?ec=1.2.1.12&view=INTENZ\">1.2.1.12</a> " +
            "glyceraldehyde-3-phosphate dehydrogenase (phosphorylating) and (phosphorylating) " +
            "and EC <a href=\"searchEc.do?ec=1.1.1.1&view=INTENZ\">1.1.1.1</a> " +
            "and EC <a href=\"searchEc.do?ec=1.2.1.13&view=INTENZ\">1.2.1.13</a> glyceraldehyde.";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }

   /**
    * Tests that a string is correctly changed when multiple ECs are present
    * aswell as when an unmarked EC is present.
    */
   public void testLinkMarkedCorrectECOldQueryFormat(){
      LOGGER.info("Running testLinkMarkedCorrectECOldQueryFormat ...");
      String text = "Also acts on 3-(3,4-dihydroxyphenyl)lactate. Involved with " +
            "<a href=\"query?search=2.3.1.140&type=ecSearch\">EC 2.3.1.140</a> rosmarinate synthase in the " +
            "biosynthesis of rosmarinic acid.";
      String textNew = "Also acts on 3-(3,4-dihydroxyphenyl)lactate. Involved with EC " +
            "<a href=\"searchEc.do?ec=2.3.1.140&view=INTENZ\">2.3.1.140</a> rosmarinate synthase in the " +
            "biosynthesis of rosmarinic acid.";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }


   /**
    * Tests that a string is correctly changed when multiple ECs are present
    * aswell as when an unmarked EC is present.
    */
   public void testLinkMarkedCorrectECHtmlFormat(){
      LOGGER.info("Running testLinkMarkedCorrectECOldQueryFormat ...");
      String text = "Specific for prostaglandins D [<ital>cf.</ital> " +
            "<a href=\"141.html\">EC 1.1.1.141</a> " +
            "15-hydroxyprostaglandin dehydrogenase (NAD<smallsup>+</smallsup>) and " +
            "<a href=\"197.html\">EC 1.1.1.197</a> 15-hydroxyprostaglandin dehydrogenase " +
            "(NADP<smallsup>+</smallsup>)].";
      String textNew = "Specific for prostaglandins D [<ital>cf.</ital> " +
            "EC <a href=\"searchEc.do?ec=1.1.1.141&view=INTENZ\">1.1.1.141</a> " +
            "15-hydroxyprostaglandin dehydrogenase (NAD<smallsup>+</smallsup>) and " +
            "EC <a href=\"searchEc.do?ec=1.1.1.197&view=INTENZ\">1.1.1.197</a> 15-hydroxyprostaglandin dehydrogenase " +
            "(NADP<smallsup>+</smallsup>)].";
      try {
         text = IntEnzUtilities.linkMarkedEC(text, true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         assertFalse(true);
      }
      assertEquals(text, textNew);
   }

   /** METHOD: firstLetterToUppercase (String text)**/
   /**
    * Tests that a null pointer is thrown if the text is null.
    */
   public void testFirstLetterToUppercaseNull(){
      LOGGER.info("Running testFirstLetterToUppercaseNull ...");
      try {
         IntEnzUtilities.firstLetterToUppercase(null);
         // should not get here
         assertFalse(true);
      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         // Correct because a nullPointer was thrown
         assertTrue(true);
      }
   }

   /**
    * Tests that a valid strings first letter is uppercased.
    */
   public void testFirstLetterToUppercaseValid(){
      LOGGER.info("Running testFirstLetterToUppercaseNull ...");
      String string2test = "my first string.";
      String stringAnswer = "My first string.";
      try {
         string2test = IntEnzUtilities.firstLetterToUppercase(string2test);

      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         // Correct because a nullPointer was thrown
         assertFalse(true);
      }
      assertEquals(string2test, stringAnswer);
   }

   /**
    * Tests that a valid strings with uppercase first letter is not changed.
    */
   public void testFirstLetterToUppercaseNoChanged(){
      LOGGER.info("Running testFirstLetterToUppercaseNoChanged ...");
      String string2test = "My first string.";
      String stringAnswer = "My first string.";
      try {
         string2test = IntEnzUtilities.firstLetterToUppercase(string2test);

      } catch ( NullPointerException t ) {
         LOGGER.error(t);
         // Correct because a nullPointer was thrown
         assertFalse(true);
      }
      assertEquals(string2test, stringAnswer);
   }

   /**
    * Default JUNIT setup *
    */

   public void setUp () throws Exception {
      super.setUp();

   }

   public void tearDown () throws Exception {
      super.tearDown();
   }

   public static Test suite () {

      TestSuite suite = new TestSuite(TestIntEnzUtilities.class);

      return suite;
   }


}
