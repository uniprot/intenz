package uk.ac.ebi.intenz.tools.sib.translator;

import uk.ac.ebi.intenz.tools.sib.translator.rules.*;
import uk.ac.ebi.xchars.SpecialCharacters;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * This class translates <code>ENZYME</code> data into the <code>IntEnz</code> format and vice versa.
 * <p/>
 * The rules applied can be found in the <code>rules</code> package (see also: {@link uk.ac.ebi.intenz.tools.sib.translator.rules.RuleGroup}).
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/11/11 12:01:24 $
 */
public class XCharsASCIITranslator {

   private static final Logger LOGGER = Logger.getLogger(XCharsASCIITranslator.class);


  /**
   * List of rules used to translate strings.
   */
  private List ruleGroups;

  /**
   * The sole instance of this class.
   */
  private static final XCharsASCIITranslator INSTANCE = new XCharsASCIITranslator();


  /**
   * Initialises all rules.
   */
  private XCharsASCIITranslator() {
     SpecialCharacters encoding = initEncoding();

    ruleGroups = new ArrayList();
    ruleGroups.add(ReactionRules.getInstance());
    ruleGroups.add(DescriptionLineRules.getInstance());
    ruleGroups.add(UnorderedRules.getInstance());
    ruleGroups.add(OrderedRules.getInstance());
    ruleGroups.add(TagRules.getInstance(encoding));
    ruleGroups.add(GrammarRules.getInstance());
  }



   /**
   * Returns the sole instance of this class.
   *
   * @return the sole instance of this class.
   */
  public static XCharsASCIITranslator getInstance() {
    return INSTANCE;
  }

  /**
   * Translates a string into the <code>IntEnz</code> format.
   *
   * @param text             String to translate.
   * @param useReactionRules Set to <code>true</code> if a reaction should be translated
   * @return the translated string.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public String toXCharsFormat(String text, boolean useReactionRules) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    int iii = 0;
    if (!useReactionRules) iii++; // skip reaction rules if desired
    for (; iii < ruleGroups.size(); iii++) {
      RuleGroup ruleGroup = (RuleGroup) ruleGroups.get(iii);
       if( ruleGroup instanceof TagRules ) continue;  // skip tag rules
      text = ruleGroup.applyRules(text);
    }
    return text;
  }

  /**
   * Just calls {@link XCharsASCIITranslator#toXCharsFormat(String, boolean)}  method for each item.
   *
   * @param texts            List of strings.
   * @param useReactionRules Set to <code>true</code> if a reaction should be translated
   * @return a list of translated strings.
   * @throws NullPointerException if <code>texts</code> is <code>null</code>.
   */
  public List toXCharsFormat(List texts, boolean useReactionRules) {
    if (texts == null) throw new NullPointerException("Parameter 'texts' must not be null.");
    List translatedTexts = new ArrayList();
    for (int iii = 0; iii < texts.size(); iii++) {
      translatedTexts.add(this.toXCharsFormat((String) texts.get(iii), useReactionRules));
    }
    return translatedTexts;
  }

  /**
   * Translates a XChars encoded string into its ASCII representation.
   *
   * @deprecated Use toAscii(String text, boolean useReactionRules, boolean useDeLineRules)
   * @param text             The string to be translated.
   * @param useReactionRules <code>true</code>, if specific reaction rules should be applied to the translation process.
   * @return the ASCII representation.
   * @throws NullPointerException if <code>texts</code> is <code>null</code>.
   */
  public String toASCII(String text, boolean useReactionRules) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");

    int iii = 0;
    //if (!useReactionRules) iii++; // skip reaction rules if desired
    for (; iii < ruleGroups.size(); iii++) {
       RuleGroup ruleGroup = (RuleGroup) ruleGroups.get(iii);

       // skip reaction rules if desired
       if (ruleGroup instanceof ReactionRules && !useReactionRules)
         continue;
       if (ruleGroup instanceof DescriptionLineRules)
         continue;
      text = ruleGroup.reverseRules(text);

    }
    return text;
  }

   /**
   * Translates a XChars encoded string into its ASCII representation.
   *
   * @param text             The string to be translated.
   * @param useReactionRules <code>true</code>, if specific reaction rules should be applied to the translation process.
   * @return the ASCII representation.
   * @throws NullPointerException if <code>texts</code> is <code>null</code>.
   */
  public String toASCII(String text, boolean useReactionRules,
		  boolean useDeLineRules) {
    return toASCII(text, useReactionRules, useDeLineRules, true);
  }

	/**
	 * Translates a XChars encoded string into its ASCII representation.
	 * @param text The string to be translated.
	 * @param useReactionRules use the reaction rules?
	 * @param useDeLineRules use the DE line rules?
	 * @param useGrammarRules use the grammar rules?
	 * @return
	 */
	public String toASCII(String text, boolean useReactionRules,
			boolean useDeLineRules, boolean useGrammarRules) {
		for (int iii = 0; iii < ruleGroups.size(); iii++) {
			RuleGroup ruleGroup = (RuleGroup) ruleGroups.get(iii);
			if (ruleGroup instanceof ReactionRules && !useReactionRules)
				continue;
			if (ruleGroup instanceof DescriptionLineRules && !useDeLineRules)
				continue;
			if (ruleGroup instanceof GrammarRules && !useGrammarRules)
				continue;
			text = ruleGroup.reverseRules(text);
		}
		return text;
	}

   /**
    * Initialise the SpecialCharacters package as we can't encode without it.
    * @return
    */
   private SpecialCharacters initEncoding () {
      SpecialCharacters encoding = SpecialCharacters.getInstance(null);
      return encoding;
   }
}
