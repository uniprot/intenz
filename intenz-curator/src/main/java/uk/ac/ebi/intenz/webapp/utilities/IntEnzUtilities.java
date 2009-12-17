package uk.ac.ebi.intenz.webapp.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility classes, which are used throughout the whole project.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class IntEnzUtilities {

  /**
   * Tries to link all EC numbers and checks whether the first letter is a uppercase letter.
   *
   * @param text The history comment to be formatted.
   * @return the formatted history comment.
   * @throws NullPointerException if <code>text</code> is <code>null</code>.
   */
  public static String linkUnmarkedEC(String text) {
    if (text == null) throw new NullPointerException("Parameter 'text' must not be null.");
    if (text.equals("")) return text;    
    text = text.replaceAll("([^>]EC )(\\d+\\.\\d+\\.\\d+\\.\\d+)", "$1<a href=\"searchEc.do?ec=$2&view=INTENZ\">$2</a>");
//    return firstLetterToUppercase(text);
//    return GrammarRules.getInstance().applyRules(text);
    return text;
  }

  /**
   * Transforms old links to EC numbers to the current way of linking to them.
   * <p/>
   * Old links may still contain hyperlinks with the wrong syntax. This method corrects these links according to the new
   * format.
   * <p/>
   * Preferably this method gets redundant, if links to EC numbers are generated on the fly in the future entirely
   * (see {@link IntEnzUtilities#linkUnmarkedEC(String) linkUnmarkedEC(String)}. At the moment it has not been agreed upon this
   * as some EC should not be linked automatically (esp. in comments).
   *
   * @param string2Check String to be checked for outdated links.
   * @param linkUnmarkedECs
   * @return a corrected version of the given string.
   */
  public static String linkMarkedEC(String string2Check, boolean linkUnmarkedECs) {
     if( string2Check == null) throw new NullPointerException("Parameter 'string2Check' must not be null.");

    Pattern p = Pattern.compile("(((EC )||())(\\<a href=\"(.+?)\"\\>((EC )||())(\\d+\\.\\d+\\.\\d+\\.\\d+)\\<\\/a\\>))");
    Matcher m = p.matcher(string2Check);
    StringBuffer cleanedString = new StringBuffer();
    while (m.find()) {
      final String group = m.group(1);
      if (group.indexOf("href=\"searchEc.do?ec=") > -1) continue; // skip correct links
      if (group.indexOf("\\<a href=\"action?cmd=SearchEC&ec") > -1)
        m.appendReplacement(cleanedString, m.group(1));
      else
        m.appendReplacement(cleanedString, m.group(2)+m.group(7)+"<a href=\"searchEc.do?ec=" + m.group(10) + "&view=INTENZ\">" + m.group(10) + "</a>");
    }
    m.appendTail(cleanedString);

    if(linkUnmarkedECs) return linkUnmarkedEC(cleanedString.toString());

    return cleanedString.toString();
  }

  /**
   * Replaces the first letter with the uppercase letter.
   *
   * @param text to be checked.
   * @return formatted string.
   */
  public static String firstLetterToUppercase(String text) {
    StringBuffer s = new StringBuffer(text);
    String firstLetter = s.substring(0, 1);
    s.replace(0, 1, firstLetter.toUpperCase());
    return s.toString();
  }

}
