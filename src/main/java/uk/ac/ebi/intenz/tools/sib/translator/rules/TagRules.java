package uk.ac.ebi.intenz.tools.sib.translator.rules;

import org.apache.log4j.Logger;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;


/**
 * This singleton class stores regular expression rules to be used to transform text.
 * <p/>
 * Each rule consists of a regular expression pattern and a replacement string. The rules will be applied in an
 * arbitrary order.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/11/11 12:01:24 $
 */
public class TagRules implements RuleGroup {

  private static final Logger LOGGER = Logger.getLogger(TagRules.class);

  private static TagRules INSTANCE;
  SpecialCharacters encoding;

  private final String[][] RULES =
          {
         //   {"\\<[^\\s].*?[^\\s]\\>", ""},
             {"\\<b\\>(.*?)\\<\\/b\\>","$1"},
             {"\\<i\\>(.*?)\\<\\/i\\>","$1"},
             {"\\<a\\s(.+?)\\>(.+?)\\<\\/a\\>","$2"},
             {"\\<ring\\>p\\<\\/ring\\>","\\<ringsugar\\>p\\<\\/ringsugar\\>"},
             {"\\<ring\\>f\\<\\/ring\\>","\\<ringsugar\\>f\\<\\/ringsugar\\>"},
          };

  /**
   * Nothing special is happening here.
   */
  private TagRules(SpecialCharacters encoding) {
   this.encoding = encoding;
  }


  /**
   * Returns the sole instance of this class.
   *
   * If no instance is available yet then it will be created.
   *
   * @return the class sole instance.
   */
  public static TagRules getInstance(SpecialCharacters encoding) {
     if( INSTANCE == null){
        synchronized (TagRules.class){
           if(INSTANCE == null)
            INSTANCE = new TagRules(encoding);
        }
     }
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
    return translate(text, RULES);
  }

  /**
   * Calls {@link TagRules#applyRules(String)} only.
   *
   * @param text The text to be translated.
   * @return the translated text.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public String reverseRules(String text) {
//    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    return applyRules(text);
  }

  /**
   * This method performs the actual application of the rules.
   *
   * @param text The text to be translated.
   * @param rules Map of rules to be applied.
   * @return the translated text.
   */
  private String translate(String text, String[][] rules) {
    // Encode the special characters
    text = this.encoding.xml2Display(text, EncodingType.SWISSPROT_CODE);
    for (int iii = 0; iii < rules.length; iii++) {
      String[] rule = rules[iii];
      text = text.replaceAll(rule[0], rule[1]);
    }    
    return text;
  }

}
