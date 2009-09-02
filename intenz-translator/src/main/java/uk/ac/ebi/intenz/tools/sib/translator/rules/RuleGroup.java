package uk.ac.ebi.intenz.tools.sib.translator.rules;

/**
 * This interface defines the mandatory method for all <code>RuleGroup</code> implementations.
 *
 * A rule group is a set of regular expression rules to translate text.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/11/11 12:01:24 $
 */
public interface RuleGroup {
  /**
   * Implementations of this interface must implement this method to translate text based on rules.
   *
   * @param text The text to be translated.
   * @return the translated text.
   */
  public String applyRules(String text);

  /**
   * This method can be used to reverse the rules which have been applied in {@link RuleGroup#applyRules(String)}.
   *
   * @param text The text to be translated.
   * @return the translated text.
   */
  public String reverseRules(String text);
}
