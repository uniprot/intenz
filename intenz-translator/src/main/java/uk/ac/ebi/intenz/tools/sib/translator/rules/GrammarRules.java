/*
Copyright (c) 2005 The European Bioinformatics Institute, and others.
All rights reserved. Please see the file LICENSE
in the root directory of this distribution.
*/
package uk.ac.ebi.intenz.tools.sib.translator.rules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * GrammarRules
 *
 * @author P. de Matos
 * @version $id 15-Jul-2005 14:41:31
 *          <p/>
 *          History:<br>
 *          <table>
 *          <tr><th>Developer</th><th>Date</th><th>Description</th></tr>
 *          <tr><td>P.de Matos</td><td>15-Jul-2005</td><td>Created class</td></tr>
 *          </table>
 */
public class GrammarRules implements RuleGroup {

   private static final Logger LOGGER = Logger.getLogger(GrammarRules.class);

  private static GrammarRules INSTANCE = new GrammarRules();

   private final String[][] CAPITILISATION_RULES_REVERSED = {
     {"^TRNA", "tRNA"},
     {"^MRNA", "mRNA"},
     {"^RRNA", "rRNA"},
     {"^CDNA", "cDNA"},
     {"^DAMP", "dAMP"},
     {"^DADP", "dADP"},
     {"^DATP", "dATP"},
     {"^DUMP", "dUMP"},
     {"^DUDP", "dUDP"},
     {"^DUTP", "dUTP"},
     {"^DCMP", "dCMP"},
     {"^DCDP", "dCDP"},
     {"^DCTP", "dCTP"},
     {"^DGMP", "dGMP"},
     {"^DGDP", "dGDP"},
     {"^DGTP", "dGTP"},
     {"^DTMP", "dTMP"},
     {"^DTDP", "dTDP"},
     {"^DTTP", "dTTP"},
     {"^CAMP", "cAMP"},
     {"^CGMP", "cGMP"}

  };

   // These are done elsewhere so we exclude them
   private final String[] CAPITILISATION_EXCLUSIONS = {
      "^p\\-",
      "^m\\-",
      "^o\\-",
      "^n\\-",
      "^tRNA",
      "^tRNase",
      "^mRNA",
      "^rRNA",
      "^cDNA",
      "^dAMP",
      "^dADP",
      "^dATP",
      "^dUMP",
      "^dUDP",
      "^dUTP",
      "^dCMP",
      "^dCDP",
      "^dCTP",
      "^dGMP",
      "^dGDP",
      "^dGTP",
      "^dTMP",
      "^dTDP",
      "^dTTP",
      "^cAMP",
      "^cGMP",
      "^ppGpp",
      "^pppGpp",
      "^\\(ppGpp\\)",
      "^\\(fMet\\)",
      "^fMet",
      "^n\\s",
      "^m\\s",
      "^tPA",
      "^(\\[|\\(|\\s)eIF",
      "^sn-",
      "^\\d*?\\([\\+\\-\\d]+?\\)\\-",
      "^cd-"
   };

   private final Pattern[] SNEAKY_CAPIT_PATTERNS = {
       Pattern.compile("^(\\[|\\()(\\p{Lower})(\\p{Lower})")
   };


   private final Pattern SNEAKY_CAPIT_PATTERN = Pattern.compile("^(\\[|\\()(\\w)");

   private final ArrayList DECAPITALIZATION_PATTERNS = new ArrayList();

   private GrammarRules () {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("decapitalization_patterns.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String pattern;
        try {
            pattern = br.readLine();
            while (pattern != null){
                DECAPITALIZATION_PATTERNS.add(pattern);
                pattern = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                stream.close();
            } catch (IOException e){}
        }
    }

   /**
     * Returns the sole instance of this class.
     * <p/>
     * If no instance is available yet then it will be created.
     *
     * @return the class's sole instance.
     */
    public static GrammarRules getInstance() {
      return INSTANCE;
    }


   public String applyRules (String text) {
    return reverseRules(text);
   }

   public String reverseRules(String text) {
        if (text == null)
            throw new NullPointerException("Parameter 'text' must not be null.");
        if (text.length() == 0)
            return text;

        // Capitalise the first letter of a line.
        boolean excludeFromCapitilisation = false;
        for (int iii = 0; iii < CAPITILISATION_EXCLUSIONS.length; iii++) {
            String exclusion = CAPITILISATION_EXCLUSIONS[iii];
            if (Pattern.compile(exclusion).matcher(text).find()) {
                excludeFromCapitilisation = true;
                break;
            }
        }
        if (!excludeFromCapitilisation)
            text = doCapitilisation(text);
        text = doDecapitalisation(text);

        return text;
    }

   private String doCapitilisation (String text) {
      for (int i = 0; i < SNEAKY_CAPIT_PATTERNS.length; i++){
          Matcher matcher = SNEAKY_CAPIT_PATTERNS[i].matcher(text);
          if (matcher.find()){
              text = matcher.replaceAll(matcher.group(1) + matcher.group(2).toUpperCase() + matcher.group(3));
          }
      }
      text = text.substring(0,1).toUpperCase() + text.substring(1);
      text = translate(text, CAPITILISATION_RULES_REVERSED);
      return text;
   }

    private String doDecapitalisation(String text) {
        for (int i = 0; i < DECAPITALIZATION_PATTERNS.size(); i++){
            String s = (String) DECAPITALIZATION_PATTERNS.get(i);
            Pattern pattern = Pattern.compile("(^|\\.\\s)(" + s + ")(\\p{Upper})");
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()){
                text = matcher.replaceFirst(matcher.group(1) + matcher.group(2) + matcher.group(3).toLowerCase());
                matcher = pattern.matcher(text);
            }
        }
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
}
