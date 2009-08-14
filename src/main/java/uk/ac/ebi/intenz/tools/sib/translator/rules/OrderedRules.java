package uk.ac.ebi.intenz.tools.sib.translator.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * This singleton class stores regular expression rules to be used to transform text.
 * <p/>
 * Each rule consists of a regular expression pattern and a replacement string. The rules will be applied in an
 * arbitrary order.
 * <p/>
 * These rules should only be applied on enzyme reaction data.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/03/27 15:33:26 $
 */
public class OrderedRules implements RuleGroup {

  private static final Logger LOGGER = Logger.getLogger(OrderedRules.class);

  private static OrderedRules INSTANCE = new OrderedRules();

  private final String[][] RULES =
          {
            {"([Dd])elta\\((\\d+)\\((\\d+)\\((\\d+)\\)\\)\\)",
             "<greek>$1elta</greek><smallsup>$2($3<smallsup>$4</smallsup>)</smallsup>"},
            {"([Dd])elta\\((\\d+)\\((\\d+)\\)\\)", "<greek>$1elta</greek><smallsup>$2($3)</smallsup>"},
            {"([Dd])elta\\((\\d+)\\)", "<greek>$1elta</greek><smallsup>$2</smallsup>"},
            {"([^\\>])([dD])elta([^\\<])", "$1<greek>$2elta</greek>$3"},
            {"([^\\>])([aA])lpha([^\\<])", "$1<greek>$2lpha</greek>$3"},
            {"([^\\>])([gG])amma([^\\<])", "$1<greek>$2amma</greek>$3"},
            {"([^\\>])([bB])eta([^\\<])", "$1<greek>$2eta</greek>$3"},
            {"([^\\>])([eE])psilon([^\\<])", "$1<greek>$2psilon</greek>$3"},
            {"\\<greek\\>beta\\<\\/greek\\>ine", "betaine"},
            {"mu\\-", "<greek>mu</greek>-"},
            {"N\\,N([\\'\\-])", "<element>N</element>,<element>N</element>$1"},
            {"(\\W)N\\-([^\\dt])", "$1<element>N</element>-$2"},
            {"^N\\-([^\\dt])", "<element>N</element>-$1"},
            {"O\\(2\\)\\(\\-\\)", "O<smallsub>2</smallsub><smallsup>-</smallsup>"},
            {"O\\(2\\)", "O<smallsub>2</smallsub>"},
            {"\\<em\\_dash\\/\\>([DL])\\-", "<em_dash/><small>$1</small>-"},
            {"\\<em\\_dash\\/\\>([aA])lpha", "<em_dash/><greek>$1lpha</greek>"},
            {"\\<em\\_dash\\/\\>([bB])eta", "<em_dash/><greek>$1eta</greek>"},
            {"\\<em\\_dash\\/\\>([gG])amma", "<em_dash/><greek>$1amma</greek>"},
            {"\\<em\\_dash\\/\\>([dD])elta", "<em_dash/><greek>$1elta</greek>"},
            {"\\<em\\_dash\\/\\>([eE])psilon", "<em_dash/><greek>$1psilon</greek>"},
            {"([SOCN])(\\(\\d+)", "<element>$1</element>$2"},
            {"\\-([SOCN])\\-", "-<element>$1</element>-"},
            {"(\\W+?)([SOCN])\\-", "$1<element>$2</element>-"},
            {"^([SOCN])\\-", "<element>$1</element>-"},
            {"\\-([SOCN])(\\W)+?", "-<element>$1</element>$2"},
            {"(\\-?)(\\d+)([DL])(\\(?)\\-", "$1$2<stereo>$3</stereo>$4-"},
            {"\\-([DL])([\\-\\(])", "-<stereo>$1</stereo>$2"},
            {"(\\W)([DL])([\\-\\(])", "$1<stereo>$2</stereo>$3"},
            {"^([DL])([\\-\\(])", "<stereo>$1</stereo>$2"},
            {"([\\-\\,])all-cis([\\-\\,])", "$1<stereo>all-cis</stereo>$2"},
            {"[aA]ll-cis([\\-\\,])", "<stereo>all-cis</stereo>$1"},
            {"([\\-\\,])all-trans([\\-\\,])", "$1<stereo>all-trans</stereo>$2"},
            {"[aA]ll\\-trans([\\-\\,])", "<stereo>all-trans</stereo>$1"},
            {"([\\-\\,])cis([\\-\\,])", "$1<stereo>cis</stereo>$2"},
            {"[cC]is([\\-\\,])", "<stereo>cis</stereo>$1"},
            {"([\\-\\,])trans([\\-\\,])", "$1<stereo>trans</stereo>$2"},
            {"[tT]rans([\\-\\,])", "<stereo>trans</stereo>$1"},
            {"\\-allo\\-", "-<stereo>allo</stereo>-"},

            {"(\\W)([aA])llo\\-", "$1<stereo>$2llo</stereo>-"},
            {"^([aA])llo\\-", "<stereo>$1llo</stereo>-"},
            {"\\-altro\\-", "-<stereo>altro</stereo>-"},
            {"(\\W)([aA])ltro\\-", "$1<stereo>$2ltro</stereo>-"},
            {"^([aA])ltro\\-", "<stereo>$1ltro</stereo>-"},
            {"\\-arabino\\-", "-<stereo>arabino</stereo>-"},
            {"(\\W)([aA])rabino\\-", "$1<stereo>$2rabino</stereo>-"},
            {"^([aA])rabino\\-", "<stereo>$1rabino</stereo>-"},
            {"\\-erythro\\-", "-<stereo>erythro</stereo>-"},
            {"(\\W)([eE])rythro\\-", "$1<stereo>$2rythro</stereo>-"},
            {"^([eE])rythro\\-", "<stereo>$1rythro</stereo>-"},
            {"\\-galacto\\-", "-<stereo>galacto</stereo>-"},
            {"(\\W)([gG])alacto\\-", "$1<stereo>$2alacto</stereo>-"},
            {"^([gG])alacto\\-", "<stereo>$1alacto</stereo>-"},
            {"\\-gluco\\-", "-<stereo>gluco</stereo>-"},
            {"(\\W)([gG])luco\\-", "$1<stereo>$2luco</stereo>-"},
            {"^([gG])luco\\-", "<stereo>$1luco</stereo>-"},
            {"\\-glycero\\-", "-<stereo>glycero</stereo>-"},
            {"(\\W)([gG])lycero\\-", "$1<stereo>$2lycero</stereo>-"},
            {"^([gG])lycero\\-", "<stereo>$1lycero</stereo>-"},
            {"\\-gulo\\-", "-<stereo>gulo</stereo>-"},
            {"(\\W)([gG])ulo\\-", "$1<stereo>$2ulo</stereo>-"},
            {"^([gG])ulo\\-", "<stereo>$1ulo</stereo>-"},
            {"\\-ido\\-", "-<stereo>ido</stereo>-"},
            {"(\\W)([iI])do\\-", "$1<stereo>$2do</stereo>-"},
            {"^([iI])do\\-", "<stereo>$1do</stereo>-"},
            {"\\-lyxo\\-", "-<stereo>lyxo</stereo>-"},
            {"(\\W)([lL])yxo\\-", "$1<stereo>$2yxo</stereo>-"},
            {"^([lL])yxo\\-", "<stereo>$1yxo</stereo>-"},
            {"\\-manno\\-", "-<stereo>manno</stereo>-"},
            {"(\\W)([mM])anno\\-", "$1<stereo>$2anno</stereo>-"},
            {"^([mM])anno\\-", "<stereo>$1anno</stereo>-"},
            {"\\-ribo\\-", "-<stereo>ribo</stereo>-"},
            {"(\\W)([rR])ibo\\-", "$1<stereo>$2ibo</stereo>-"},
            {"^([rR])ibo\\-", "<stereo>$1ibo</stereo>-"},
            {"\\-talo\\-", "-<stereo>talo</stereo>-"},
            {"(\\W)([tT])alo\\-", "$1<stereo>$2alo</stereo>-"},
            {"^([tT])alo\\-", "<stereo>$1alo</stereo>-"},
            {"\\-threo\\-", "-<stereo>threo</stereo>-"},
            {"(\\W)([tT])hreo\\-", "$1<stereo>$2hreo</stereo>-"},
            {"^([tT])hreo\\-", "<stereo>$1hreo</stereo>-"},
            {"\\-xylo\\-", "-<stereo>xylo</stereo>-"},
            {"(\\W)([xX])ylo\\-", "$1<stereo>$2ylo</stereo>-"},
            {"^([xX])ylo\\-", "<stereo>$1ylo</stereo>-"},
            {"((\\d)\\((\\d))H(\\)-)","$1<element>H</element>$4"}
          };

  private final String[][] RULES_REVERSED =
          {
            {"\\<greek\\>([Dd])elta\\<\\/greek\\>\\<smallsup\\>(\\d+)\\((\\d+)\\<smallsup\\>(\\d+)\\<\\/smallsup\\>\\)\\<\\/smallsup\\>",
             "$1elta($2($3($4)))"},
            {"\\<greek\\>([Dd])elta\\<\\/greek\\>\\<smallsup\\>(\\d+)\\((\\d+)\\)" +
             "\\<\\/smallsup\\>",
             "$1elta($2($3))"},
            {"\\<greek\\>([Dd])elta\\<\\/greek\\>\\<smallsup\\>(\\d+)\\<\\/smallsup\\>",
             "$1elta($2)"},
            {"(\\d+)\\<greek\\>", "$1-<greek>"},
            {"\\<greek\\>([dD])elta\\<\\/greek\\>", "$1elta"},
            {"\\<greek\\>([aA])lpha\\<\\/greek\\>", "$1lpha"},
            {"\\<greek\\>([gG])amma\\<\\/greek\\>", "$1amma"},
            {"\\<greek\\>([bB])eta\\<\\/greek\\>", "$1eta"},
            {"\\<greek\\>([eE])psilon\\<\\/greek\\>", "$1psilon"},
            {"\\<greek\\>mu\\<\\/greek\\>\\-", "mu-"},
            {"\\<element\\>N\\<\\/element\\>\\,\\<element\\>N\\<\\/element\\>([\\'\\-])", "N,N$1"},
            {"(\\W)\\<element\\>N\\<\\/element\\>\\-([^\\dt])", "$1N-$2"},
            {"^\\<element\\>N\\<\\/element\\>\\-([^\\dt])", "N-$1"},
            {"O\\<smallsub\\>2\\<\\/smallsub\\>\\<smallsup\\>\\-\\<\\/smallsup\\>",
             "O(2)(-)"},
            {"O\\<smallsub\\>2\\<\\/smallsub\\>", "O(2)"},
            {"\\-\\<element\\>S\\<\\/element\\>\\-", "-S-"},
            {"\\<element\\>S\\<\\/element\\>\\-", "S-"},
            {"\\-\\<element\\>S\\<\\/element\\>", "-S"},
            {"((\\d)\\((\\d))\\<element\\>H\\<\\/element\\>(\\)-)", "=$1H$4"},
            {"\\<smallsub\\>(.+?)\\</smallsub\\>","($1)"},
            {"\\<smallsup\\>(.+?)\\</smallsup\\>","($1)"},
// TODO: TAKE THE EXCEPTIONS OUT OF HERE TO A TEXT FILE!:
            {"\\[(?!Fe\\([23]\\+\\)|Co\\(II\\)|NiFe|[234]Fe-[24]S|Glu\\]|heparan sulfate|lipopolysaccharide glucose|blood group substance|[Mm]yelin[ -]proteolipid|3\\.2\\.2|4-vinyl|1,4\\]|cd|ambiguous|misleading|misprint|obsolete|incorrect|\\d,\\d-[af]\\]|\\d,\\d-<ital>[af]</ital>\\]|<ital>a</ital>\\]|tRNA\\]|ligated tRNA|-\\d)([^\\[\\]]*?)\\]","($1)"},        // square brackets [ ]
            {"\\{([^\\}]*?\\([^\\}]*?\\)[^\\}]*?)\\}","($1)"},        // curly brackets { }
            {"\\{","("},
            {"\\}", ")"}
          };

  private final String[] STEREO_TERMS = {"D", "L", "all-cis", "all-trans", "cis", "trans", "allo", "altro", "arabino", "erythro", "galacto",
                            "gluco", "glycero", "gulo", "ido", "lyxo", "manno", "ribo", "talo", "threo", "xylo"};


  /**
   * Initialises the class's sole instance.
   */
  private OrderedRules() {
  }

  /**
   * Returns the sole instance of this class.
   * <p/>
   * If no instance is available yet then it will be created.
   *
   * @return the class's sole instance.
   */
  public static OrderedRules getInstance() {
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
    text = applyStereoRSZERule(text);
    text = translate(text, RULES);
    text = applyDecapitalisationRule(text);
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
    text = applyStereoRSZERuleReversed(text);
    text = translate(text, RULES_REVERSED);
    text = applyReverseStereoRules(text);
    return text;
  }

    /**
   * This method performs the actual application of the rules.
   *
   * @param text  The text to be translated.
   * @param rules Map of rules to be applied.
   * @return the translated text.
   */
  private String translate(String text, String[][] rules) {
    for (int iii = 0; iii < rules.length; iii++) {
      String[] rule = rules[iii];
      text = text.replaceAll(rule[0], rule[1]);
    }
    return text;
  }

  /**
   * Transforms R, S, Z, E letters into <code>&lt;stereo&gt;<i>R, S, Z or E</i>&lt;/stereo&gt;</code> if these letters
   * occur within parenthesis and are only preceded by numbers and/or 'A's.
   *
   * @param text Text to be translated.
   * @return the translated text.
   */
  private String applyStereoRSZERule(String text) {
    Pattern coarseRSZEPattern = Pattern.compile("(\\((?:\\d*?A*?[RSZE]\\,?)+?\\))");
    Matcher coarseRSZEMatcher = coarseRSZEPattern.matcher(text);
    StringBuffer textStringBuffer = new StringBuffer();
    boolean found = false;
    int oldStart = 0;
    int start = 0;
    while (coarseRSZEMatcher.find()) {
      found = true;
      start = coarseRSZEMatcher.start(1);
      String group = coarseRSZEMatcher.group(1);
      Pattern RSZEPattern = Pattern.compile("([RSZE])");
      Matcher RSZEMatcher = RSZEPattern.matcher(group);
      group = RSZEMatcher.replaceAll("<stereo>$1</stereo>");
      textStringBuffer.append(text.substring(oldStart, start) + group);
      oldStart = coarseRSZEMatcher.end(1);
    }
    textStringBuffer.append(text.substring(oldStart));
    if (found) text = textStringBuffer.toString();
    return text;
  }

  /**
   * Reverses the rule applied in {@link OrderedRules#applyStereoRSZERule(String)}.
   *
   * @param text Text to be translated.
   * @return the translated text.
   */
  private String applyStereoRSZERuleReversed(String text) {
    return text.replaceAll("\"<stereo>([RSZE])</stereo>\"", "$1");
  }

  /**
   * Decapitalises words which follow a square bracket.
   *
   * @param text The text to be translated.
   * @return the translated text.
   */
  private String applyDecapitalisationRule(String text) {
    Pattern pattern = Pattern.compile("^\\[(\\p{Upper})(\\p{Lower})");
    Matcher matcher = pattern.matcher(text);
    if (matcher.find()) {
      text = matcher.replaceAll("[" + matcher.group(1).toLowerCase() + matcher.group(2));
    }
    return text;
  }

  /**
   * Applies reverse <code>STEREO</code> rules.
   *
   * @param text The text to be translated.
   * @return the translated text.
   */
  private String applyReverseStereoRules(String text) {
    for (int iii = 0; iii < STEREO_TERMS.length; iii++) {
      String stereoTerm = STEREO_TERMS[iii];
      text = text.replaceAll("([\\-\\,])<stereo>" + stereoTerm + "</stereo>([\\-\\,\\(])", "$1" + stereoTerm + "$2");
      text = text.replaceAll("<stereo>" + stereoTerm + "</stereo>([\\-\\,\\(])", stereoTerm + "$1");
    }
    return text;
  }
}
