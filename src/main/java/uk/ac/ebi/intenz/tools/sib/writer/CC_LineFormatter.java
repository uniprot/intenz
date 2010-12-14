package uk.ac.ebi.intenz.tools.sib.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the special needs regarding the format of the <code><b>CC</b></code> line.
 * <p/>
 * The <code><b>CC</b></code> line contains of sentences which are introduced by <code><b>-!-</b></code>. In some
 * cases not all sentences are preceeded by this string, especially when it comes to ordered lists. These lists also
 * have to be indented as shown in the example below.
 * <p/>
 * <b>Example:</b>
 * <p/>
 * <img src="../../../../../../../images/example_cc_formatter.gif">
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class CC_LineFormatter extends DefaultLineFormatter {
  /**
   * Formats the <code><b>CC</b></code> lines according to the format described in the class description.
   *
   * @param text The text of the <code><b>CC</b></code> line(s).
   * @param lineType Must always be {@link LineType#CC LineType.CC}.
   * @return the formatted <code><b>CC</b></code> line(s).
   * @throws EnzymeFlatFileWriteException if an error occured during this process.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   * @throws IllegalArgumentException if <code>lineType</code> is not {@link LineType#CC LineType.CC}.
   */
  public String formatLines(String text, LineType lineType) throws EnzymeFlatFileWriteException {
    if (text == null || lineType == null) throw new NullPointerException();
    if (lineType != LineType.CC) throw new IllegalArgumentException();

    // Every sentence within the CC lines will be wrapped individually and starts with '-!-'.
    List commentSentences = getCommentSentences(text);
    StringBuffer CCContent = new StringBuffer();
    boolean containsOrderedListWithIndent = text.matches(".*?\\:\\s+?\\(\\d+\\)\\s+.+?");
    for (int iii = 0; iii < commentSentences.size(); iii++) {
      String sentence = (String) commentSentences.get(iii);
      CCContent.append(wrapCCLine(sentence, containsOrderedListWithIndent));
    }

    // Comments ending with '/' do not have a period in the end (e.g. URLs).
    String ccLines = CCContent.toString();
    if (CCContent.indexOf("/.") > -1)
      return CCContent.replace(CCContent.indexOf("/."), CCContent.indexOf("/.") + 2, "/").toString();

    return ccLines;
  }

  /**
   * Inserts line breaks for a sentence in the CC line.
   * <p/>
   * Every sentence in the CC line is preceeded by <code>-!-</code> .
   *
   * @param text      The sentence to be wrapped.
   * @param addIndent <code>true</code> if the current sentence contains an ordered list and needs additional indent.
   * @return the wrapped sentence.
   * @throws EnzymeFlatFileWriteException if an error occurred during the line breaking process.
   */
  private String wrapCCLine(String text, boolean addIndent) throws EnzymeFlatFileWriteException {
    assert text != null : text;

    String lineStart = "CC   ";
    int netLineWidth = LINEWIDTH - (lineStart.length() + 4); // Minus length '-!- '.
    StringBuffer wrappedText = new StringBuffer();
    if (text.length() <= netLineWidth) {
      wrappedText.append(lineStart);
      wrappedText.append("-!- ");
      wrappedText.append(text);
      wrappedText.append("\n");
      return wrappedText.toString();
    }

    StringBuffer restText = new StringBuffer(text);
    wrappedText.append(lineStart);
    wrappedText.append("-!- ");

    boolean orderedListIndicatorFound = false;
    boolean foundBeginningOfItem = false;
    String indent = "";
    LineWrapper lineWrapPositioner = LineWrapperFactory.create(text, LineType.CC);
    while (restText.toString().trim().length() > netLineWidth) {
      int position = 0;
      if (restText.charAt(0) == ' ') restText.deleteCharAt(0);
      // Calculate correct net line width in case of an ordered list with additional indent.
      if (orderedListIndicatorFound) {
        if (restText.toString().trim().matches("\\(\\d+\\).+?")) {
          position = lineWrapPositioner.findPosition(restText.toString().trim(), netLineWidth);
          foundBeginningOfItem = true;
        } else {
          position = lineWrapPositioner.findPosition(restText.toString().trim(), netLineWidth - indent.length());
          foundBeginningOfItem = false;
        }
      } else {
        position = lineWrapPositioner.findPosition(restText.toString().trim(), netLineWidth);
      }


      String line = restText.substring(0, position).trim(); // Ignore leading space.

      // Check if additional indent has to be added and retrieve indent.
      if (addIndent && line.trim().endsWith(":")) orderedListIndicatorFound = true;
      if (foundBeginningOfItem)
        indent = getIndent(line);

      restText.delete(0, position);
      // Add indent if necessary.
      if (orderedListIndicatorFound && !foundBeginningOfItem) {
        wrappedText.append(indent);
      }

      wrappedText.append(line);
      wrappedText.append("\n");
      wrappedText.append(lineStart);
      wrappedText.append("    ");
    }
//    if (restText.charAt(0) == ' ') restText.deleteCharAt(0); // Ignore leading space.
    String tail = restText.toString().trim();
//    if (orderedListIndicatorFound && !tail.matches("\\(\\d+\\).+?")) {
//      wrappedText.append(indent);
//    }
    wrappedText.append(tail);
    wrappedText.append("\n");

    return wrappedText.toString();
  }

  /**
   * Breaks the comment text into individual sentences.
   *
   * @param commentText The comment text.
   * @return a list of sentences.
   */
  public List getCommentSentences(String commentText) {
    assert commentText != null : commentText;

    List sentences = new ArrayList();
    final String sentenceDelimiterPattern = "(.*?\\.\\s|.*?http\\:\\/\\/\\S+?\\/\\s)";

    // Exceptions which do NOT indicate the end of a sentence.
    // First item is the 'false' end of sentence
    // Second item is the acceptable regexp for the rest of the sentence
    final String[][] nonSentenceDelimiters = {
      {"C. ", "\\p{Lower}.*?"},
      {"E. ", ".*?"},
      {"(cf. ", ".*?"},
      {"Cf. ", ".*?"},
      {"cf. ", ".*?"},
      {"i.e. ", ".*?"},
      {"i.e., ", ".*?"},
      {"i.e.", ".*?"},
      {"i.e.,", ".*?"},
      {"e.g. ", ".*?"},
      {"e.g., ", ".*?"},
      {"e.g.", ".*?"},
      {"e.g.,", ".*?"},
      {"etc. ", ".*?"},
      {"etc., ", ".*?"},
      {"etc.", ".*?"},
      {"etc.,", ".*?"},
      {"sp. ", "(\\p{Lower}|OxB-1|PCC|AK2|M128|AL-1|SBUG 290|A17|No\\.|\\d+|\\().*?"},
      {"sp., ", ".*?"},
      {"sp.", ".*?"},
      {"sp.,", ".*?"},
      {"spp. ", ".*?"},
      {".) ", ".*?"},
      {".)", ".*?"},
      {".( ", ".*?"},
      {".(", ".*?"},
      {" var. ", ".*?"},
      {"No. ", ".*?"}
    };

    Pattern pattern = Pattern.compile(sentenceDelimiterPattern);
    Matcher matcher = pattern.matcher(commentText);

    boolean concat = false;
    StringBuffer sentence = null;
    int end = 0;
    boolean found = matcher.find();
    while (found) {
      String substring = matcher.group(1); // The sentence (probably).
      end = matcher.end(1);                // End of the sentence.

      // The previous iteration extracted a substring of the sentence and needs to be extended by the current substring.
      if (concat) {
        sentence.append(substring);
        concat = false;
      } else {
        sentence = new StringBuffer(substring);
      }

      // Check if the current substring is not a sentence and needs to be extended by the next substring.
      for (int iii = 0; iii < nonSentenceDelimiters.length; iii++) {
        String nonSentenceDelimiter = nonSentenceDelimiters[iii][0];
        if (substring.endsWith(nonSentenceDelimiter)) {
          String restOfSentence = commentText.substring(matcher.end());
          if (Pattern.matches(nonSentenceDelimiters[iii][1], restOfSentence)){
              concat = true;
              break;
          }
        }
      }

      found = matcher.find();                      // Any more sentences/substrings?
      if (concat && found) continue;               // Concatenate the next substring.

      if (!concat)
        sentences.add(sentence.toString().trim()); // Add sentence.
      else
        sentences.add(sentence.toString());        // Keep space for concatenation of the tail.
    }

    if (concat) { // Concatenate the tail.
      StringBuffer temp = new StringBuffer((String) sentences.get(sentences.size() - 1));
      temp.append(commentText.substring(end));
      sentences.remove(sentences.size() - 1);
      sentences.add(temp.toString().trim());
    } else {
      sentences.add(commentText.substring(end).trim());
    }

    if (commentText.matches(".*?\\:\\s\\(\\d+\\)\\s+.*?")) sentences = mergeListSentences(sentences);

    return sentences;
  }

  /**
   * Merges sentences which belong to an ordered list.
   *
   * @param sentences All sentences identified in the given text.
   * @return If the list of sentences contained an ordered list the returned list will have less elements.
   */
  private List mergeListSentences(List sentences) {
    List mergedSentences = new ArrayList();

    int count = 0;
    for (int iii = 0; iii < sentences.size(); iii++) {
      String sentence = (String) sentences.get(iii);
      if (sentence.matches("^\\(\\d+\\).*?")) {
        StringBuffer extendedSentence = new StringBuffer((String) mergedSentences.remove(iii - (1 + count))); // Previous sentence.
        extendedSentence.append(" ");
        extendedSentence.append(sentence); // Current sentence.
        mergedSentences.add(iii - (1 + count), extendedSentence.toString());
        count++;
      } else
        mergedSentences.add(sentence);
    }

    return mergedSentences;
  }

  /**
   * Calculates the indent to be added to subsequent lines.
   *
   * @param line The line containing the beginning of a list item (e.g. '(1)').
   * @return the indent.
   */
  private String getIndent(String line) {
    assert line != null : line;

    StringBuffer indent = new StringBuffer();
    // Ex.: >(1) < -> 4 spaces.
//    for (int counter = 0, spaces = line.substring(line.indexOf("("), line.indexOf(")") + 2).length();
//         counter < spaces;
//         counter++)
//      indent.append(" ");

    return indent.toString();
  }

}
