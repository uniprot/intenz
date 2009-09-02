/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator.rules;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.InputStream;
import java.io.IOException;

import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;


/**
 * DescriptionLineRules are specific to the description line.
 * They should be applied before other rules.
 * Only reverse rules are applicable to this DE line currently.
 *
 * @author pmatos
 * @version $id 09-May-2005 17:33:27
 *          <p/>
 *          History:
 *          Developer          Date              Description<br>
 *          pmatos            09-May-2005         Created class<br>
 */
public class DescriptionLineRules implements RuleGroup {

   private static final Logger LOGGER = Logger.getLogger(ReactionRules.class);

  private static final DescriptionLineRules INSTANCE = new DescriptionLineRules();

  private Map reverseRules;

  //private Map rules;

  /**
   * Initialises the class's sole instance.
   */
  private DescriptionLineRules() {
     InputStream stream = null;
     reverseRules = new HashMap();
     try{
      stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("deRules_rev.txt");
      PropertyResourceBundle prb = new PropertyResourceBundle(stream);
      Enumeration en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        reverseRules.put(key, value);
      }
    } catch (IOException e) {
      LOGGER.error("Error while initialising 'DescriptionLineRules' class.", e);
    } finally {
      try {
         if (stream!=null)
        stream.close();
      } catch (IOException e) {
        LOGGER.error("Error while closing input stream.", e);
      }
    }
  }

  /**
   * Returns the sole instance of this class.
   *
   * If no instance is available yet then it will be created.
   *
   * @return the class's sole instance.
   */
  public static DescriptionLineRules getInstance() {
    return INSTANCE;
  }

  /**
   * Not implemented
   *
   * @param text The text to be translated.
   * @return the translated text.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public String applyRules(String text) {
     if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    return text;
  }

  /**
   * Applies the reverse rules.
   *
   * @param text The text to be translated.
   * @return the translated text.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public String reverseRules(String text) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    text = translate(text, reverseRules);
    return text;
  }

  /**
   * This method performs the actual application of the rules.
   *
   * @param text The text to be translated.
   * @param rules Map of rules to be applied.
   * @return the translated text.
   */
  private String translate(String text, Map rules) {
    Set rulePatterns = rules.keySet();
    for (Iterator it = rulePatterns.iterator(); it.hasNext();) {
      String pattern = (String) it.next();
      String replacement = (String) rules.get(pattern);
      text = text.replaceAll(pattern, replacement);
    }
    return text;
  }
}
