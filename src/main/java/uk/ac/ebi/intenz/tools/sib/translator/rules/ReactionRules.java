package uk.ac.ebi.intenz.tools.sib.translator.rules;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This singleton class stores regular expression rules to be used to transform text.
 * <p/>
 * Each rule consists of a regular expression pattern and a replacement string. The rules will be applied in an
 * arbitrary order.
 *
 * These rules should only be applied on enzyme reaction data.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/14 15:02:29 $
 */
public class ReactionRules implements RuleGroup {

  private static final Logger LOGGER = Logger.getLogger(ReactionRules.class);

  private static final ReactionRules INSTANCE = new ReactionRules();

  private Map reverseRules;

  private Map rules;

  /**
   * Initialises the class sole instance.
   */ 
  private ReactionRules() {
    InputStream stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("reactionRules.txt");
//    FileInputStream stream = null;
    rules = new HashMap();
    reverseRules = new HashMap();
    try {
//      File file = new File("rules/reactionRules.txt");
//      stream = new FileInputStream(file);
      PropertyResourceBundle prb = new PropertyResourceBundle(stream);
      Enumeration en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        rules.put(key, value);
      }
      stream.close();

//      file = new File("rules/reactionRules_rev.txt");
//      stream = new FileInputStream(file);
      stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("reactionRules_rev.txt");
      prb = new PropertyResourceBundle(stream);
      en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        reverseRules.put(key, value);
      }
    } catch (IOException e) {
      LOGGER.error("Error while initialising 'ReactionRules' class.", e);
    } finally {
      try {
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
  public static ReactionRules getInstance() {
    return INSTANCE;
  }

  /**
   * Applies the rules.
   * 
   * @param text The text to be translated.
   * @return the translated text.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */ 
  public String applyRules(String text) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    text = translate(text, rules);
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
      //LOGGER.debug("IN: " + text);
      String pattern = (String) it.next();
      //LOGGER.debug("REACTION RULE: " + pattern);
      String replacement = (String) rules.get(pattern);
      text = text.replaceAll(pattern, replacement);
      //LOGGER.debug("OUT: " + text);
    }
    return text;
  }

}
