package uk.ac.ebi.intenz.tools.sib.writer;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a factory method to retrieve the adequate <code>LineWrapper</code> implementation.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/11/18 17:01:10 $
 */
public class LineWrapperFactory {

  /**
   * These regular expression patterns are used to identify potential line break positions within a string.
   */
  private static final String[] lineBreakPatterns = {
    "\\=\\s",
    "[^\\(\\sEC]\\s",
    "\\-\\d+\\-[^\\>\\|\\)]",
    "\\]\\-[^\\>\\|\\)]",
    "\\-\\w{1,1}\\-[^\\>\\|\\)]",
    "\\-\\d(\\,\\d)+\\-[^\\>\\|\\)]",
    "\\w{3,}\\-[^\\>\\|\\)]",
    "\\,\\d+\\-[^\\>\\|\\)]"
  };

//    "\\-\\d+\\-[^\\>\\|]",
//    "\\]\\-[^\\>\\|]",
//    "\\-\\w{1,1}\\-[^\\>\\|]",
//    "\\-\\d(\\,\\d)+\\-[^\\>\\|]",
//    "\\w{3,}\\-[^\\>\\|]",
//    "\\,\\d+\\-[^\\>\\|]"
  private static final String[] enforcedLineBreakPatterns = {
    "\\:?((?<!and)\\s\\(\\d+\\)\\s+)(?!.*?and\\s\\(\\d+\\).*?).+?\\."
  };

  /**
   * Returns a <code>LineWrapper</code> implementation which will be chosen according to the needs of the text to be
   * wrapped.
   *
   * @param wholeText The text to be wrapped.
   * @param lineType  The {@link LineType LineType}
   * @return a concrete <code>LineWrapper</code> implementation.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public static LineWrapper create(String wholeText, LineType lineType) {
    if (wholeText == null || lineType == null) throw new NullPointerException();

    if (!wholeText.matches(".*?[\\:\\.]\\s+?\\(\\d+\\)\\s+.+?")) {
      if (lineType == LineType.CA && wholeText.matches(".+?\\s\\+\\s.+?\\s\\=\\s.+?\\s\\+\\s.+?")) {
        // If the compounds consist of more than 3 words this reaction is very likely a descriptive reaction and
        // therefore not affected by the rule contained in the ReactionWrapper.
        Pattern pattern = Pattern.compile("(.+?)\\s\\+\\s(.+?)\\s\\=\\s(.+?)\\s\\+\\s(.+)");
        Matcher matcher = pattern.matcher(wholeText);
        if (matcher.find()) {
          String compound1 = matcher.group(1);
          String compound2 = matcher.group(2);
          String compound3 = matcher.group(3);
          String compound4 = matcher.group(4);
          if (countWords(compound1) > 3 || countWords(compound2) > 3 ||
                  countWords(compound3) > 3 || countWords(compound4) > 3)
            return new DefaultLineWrapper();
        }

        return new ReactionWrapper(); // Descriptive reactions are not included.
      }
      return new DefaultLineWrapper();
    } else
      return new OrderedListLineWrapper();
  }

  private static class DefaultLineWrapper implements LineWrapper {
    private static final int MIN_LENGTH_DIVISOR = 4;
    private static final int MIN_LENGTH_NUMBER = 2;

    public int findPosition(String text, int netLineWidth) throws EnzymeFlatFileWriteException {
      SortedSet possibleLineBreakPositions = getPossibleLineBreakPositions(text, netLineWidth);
      if (possibleLineBreakPositions == null) throw new EnzymeFlatFileWriteException("No line break positions found");
      return getNearestLineBreakPosition(possibleLineBreakPositions, netLineWidth, text);
    }

    /**
     * Tries to find as many potential line break positions in the given text as possible.
     * <p/>
     *
     * @param text Text to be examined.
     * @return a <code>TreeSet</code> containing all potential line break positions or <code>null</code> if no positions
     *         could be found.
     */
    protected SortedSet getPossibleLineBreakPositions(String text, int lineWidth) {
      assert text != null : text;
      SortedSet possibleLineBreakPositions = null;

      Pattern pattern;
      Matcher matcher;

      for (int iii = 0; iii < lineBreakPatterns.length; iii++) {
        if (iii == 0) possibleLineBreakPositions = new TreeSet();
        String lineBreakPattern = lineBreakPatterns[iii];

        pattern = Pattern.compile(lineBreakPattern);
        matcher = pattern.matcher(text);
        while (matcher.find()) {
          int position = matcher.end();
          // The space character is ignored for position determination, so is the '>' character.
          if (lineBreakPattern.endsWith("\\s") || lineBreakPattern.endsWith("[^\\>\\|\\)]")) position--;
          possibleLineBreakPositions.add(new Integer(position));
        }
      }

//      Iterator it = possibleLineBreakPositions.iterator();
//      while (it.hasNext()) {
//        System.out.println("it.next() = " + ((Integer) it.next()).intValue());
//      }

      return possibleLineBreakPositions;
    }

    /**
     * Returns the nearest line break position to the given maximum position.
     *
     * @param possibleLineBreakPositions All line break positions calculated.
     * @param netLineWidth               The maximum position.
     * @return the nearest line break position which is <= netLineWidth.
     */
    protected int getNearestLineBreakPosition(SortedSet possibleLineBreakPositions, int netLineWidth, String text) {
      assert possibleLineBreakPositions != null : possibleLineBreakPositions;
      assert netLineWidth > -1 : netLineWidth;
//      System.out.println("text = " + text);
      int previousLineBreakPosition = 0;
      int possibleLineBreakPosition = 0;
      int lastSpace = 0;

      Iterator it = possibleLineBreakPositions.iterator();
      while (it.hasNext()) {
        int position = ((Integer) it.next()).intValue();
        if (position <= netLineWidth) {
          previousLineBreakPosition = possibleLineBreakPosition;
          if (text.charAt(previousLineBreakPosition) == ' ') lastSpace = previousLineBreakPosition; // Save index of last space.
          possibleLineBreakPosition = position;
        }
      }

      // Exceptions ...
      if (previousLineBreakPosition > 0) {
        // Enforce line wrapping after '=', '+', 'and' and 'or' if these strings appear in the end of the line.
        if ((possibleLineBreakPosition - previousLineBreakPosition < 5 &&
                (text.charAt(previousLineBreakPosition - 1) == '=' ||
                text.charAt(previousLineBreakPosition - 1) == '+' ||
                text.charAt(previousLineBreakPosition - 1) == ',' //||
               // text.substring(previousLineBreakPosition - 1, possibleLineBreakPosition).indexOf("and") > -1 ||
               // text.substring(previousLineBreakPosition - 1, possibleLineBreakPosition).indexOf("or") > -1
              ))) {
          return previousLineBreakPosition;
        }
      }

      // Dashed words in the end are treated with special care ...
//      if (isDashedWordSpecialCase(possibleLineBreakPosition, lastSpace, text))
//        return lastSpace;
       if(possibleLineBreakPosition==0)
         if(possibleLineBreakPositions.first()!=null)
            possibleLineBreakPosition = ((Integer) possibleLineBreakPositions.first()).intValue();

      return possibleLineBreakPosition;
    }


    private boolean isDashedWordSpecialCase(int index, int lastSpace, String text) {
      String endOfLine = text.substring(lastSpace, index).trim();
      String wrappedWord = "";
      if (text.indexOf(" ", lastSpace + 1) > -1) {
        wrappedWord = text.substring(lastSpace, text.indexOf(" ", lastSpace + 1)).trim();
      } else {
        wrappedWord = text.substring(lastSpace);
      }

      // 1. If numbers (maybe concatenated by a comma) appear in the endOfLine string only, then wrap AFTER the dash
      //    only if the length of endOfLine is > MIN_LENGTH_NUMBER; otherwise wrap BEFORE the dash.
//      if (endOfLine.matches("\\d+(?:\\,\\d+)*?\\-")) {
//        if (endOfLine.substring(0, endOfLine.indexOf("-")).length() > MIN_LENGTH_NUMBER)
//          return false;
//        return true;
//      }

      // 2. If only one dash exists in the wrapped word than always wrap AFTER the dash.
      //    Otherwise if the substring is < a quarter of wrappedWord.length() wrap BEFORE the dash;
      //    WRAP AFTER the dash if none of the above cases apply.
      if (endOfLine.matches("\\w+?\\-")) {
        if (wrappedWord.indexOf("-") == wrappedWord.lastIndexOf("-")) return false;
        if (endOfLine.length() < (wrappedWord.length() / MIN_LENGTH_DIVISOR)) return true; // wrap before endOfLine begins
        return false;
      }

      return false;
    }
  }

  private static class OrderedListLineWrapper extends DefaultLineWrapper {
    protected SortedSet getPossibleLineBreakPositions(String text, int lineWidth) {
      SortedSet possibleLineBreakPositions = null;
      Pattern pattern;
      Matcher matcher;
      for (int iii = 0; iii < enforcedLineBreakPatterns.length; iii++) {
        if (iii == 0) possibleLineBreakPositions = new TreeSet();
        String lineBreakPattern = enforcedLineBreakPatterns[iii];

        pattern = Pattern.compile(lineBreakPattern);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
          int position = text.indexOf(matcher.group(1)) + 1;
          if (position > lineWidth) break;
          possibleLineBreakPositions.add(new Integer(position));
          return possibleLineBreakPositions;
        }
      }

      return super.getPossibleLineBreakPositions(text, lineWidth);
    }
  }

  private static class ReactionWrapper extends DefaultLineWrapper {
    // 1.10.2.1
    protected SortedSet getPossibleLineBreakPositions(String text, int lineWidth) {
      SortedSet possibleLineBreakPositions = super.getPossibleLineBreakPositions(text, lineWidth);
      SortedSet cleanedLineBreakPositions = new TreeSet();
      for (Iterator it = possibleLineBreakPositions.iterator(); it.hasNext();) {
        int position = ((Integer) it.next()).intValue();
        if (text.charAt(position) == ' ') {
          char before = text.charAt(position - 1);
          char after = text.charAt(position + 1);
//          if (before != '+' && before != '=' && after != '+' && after != '=')
          if (before != '+' && before != '=')
            continue;
        }
        cleanedLineBreakPositions.add(new Integer(position));
      }

      return cleanedLineBreakPositions;
    }
  }

  private static int countWords(String text) {
    Pattern pattern = Pattern.compile("\\b");
    Matcher matcher = pattern.matcher(text);
    int count = 0;
    while (matcher.find()) count++;
    return count / 2;
  }

}
