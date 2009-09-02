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
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/05/14 15:02:28 $
 */
public class UnorderedRules implements RuleGroup {

  private static final Logger LOGGER = Logger.getLogger(UnorderedRules.class);

  private static final UnorderedRules INSTANCE = new UnorderedRules();

  private Map reverseRules;

  private Map rules;

  private Map spellingRules;

  /**
   * Initialises the class's sole instance.
   */
  private UnorderedRules() {
    InputStream stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("unorderedRules.txt");
//    FileInputStream stream = null;
    rules = new HashMap();
    reverseRules = new HashMap();
    spellingRules = new HashMap();
    try {
//      File file = new File("rules/unorderedRules.txt");
//      stream = new FileInputStream(file);
      PropertyResourceBundle prb = new PropertyResourceBundle(stream);
      Enumeration en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        rules.put(key, value);
      }
      stream.close();

//      file = new File("rules/unorderedRules_rev.txt");
//      stream = new FileInputStream(file);
      stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("unorderedRules_rev.txt");
      prb = new PropertyResourceBundle(stream);
      en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        reverseRules.put(key, value);
      }

      stream = XCharsASCIITranslator.class.getClassLoader().getResourceAsStream("spellingRules.txt");
      prb = new PropertyResourceBundle(stream);
      en = prb.getKeys();
      while (en.hasMoreElements()) {
        String key = (String) en.nextElement();
        String value = prb.getString(key);
        spellingRules.put(key, value);
      }

    } catch (IOException e) {
      LOGGER.error("Error while initialising 'UnorderedRules' class.", e);
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
   * <p/>
   * If no instance is available yet then it will be created.
   *
   * @return the class's sole instance.
   */
  public static UnorderedRules getInstance() {
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
   * This rule also capitalises the first letter of the given text since all affected line
   * types start with a capital letter.
   *
   * @param text The text to be translated.
   * @return the translated text.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public String reverseRules(String text) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    text = translate(text, reverseRules);
    text = translate(text, spellingRules);

    return text;
  }

  /**
   * This method performs the actual application of the rules.
   *
   * @param text  The text to be translated.
   * @param rules Map of rules to be applied.
   * @return the translated text.
   */
  private String translate(String text, Map rules) {
    //LOGGER.debug("> " + text);
    Set rulePatterns = rules.keySet();
    for (Iterator it = rulePatterns.iterator(); it.hasNext();) {
      String pattern = (String) it.next();
      String replacement = (String) rules.get(pattern);
      text = text.replaceAll(pattern, replacement);
    }
    //LOGGER.debug("< " + text);
    return text;
  }

}
