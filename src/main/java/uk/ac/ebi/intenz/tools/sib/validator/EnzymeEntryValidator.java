package uk.ac.ebi.intenz.tools.sib.validator;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import uk.ac.ebi.intenz.tools.sib.writer.LineType;
import uk.ac.ebi.intenz.tools.sib.exceptions.EnzymeEntryValidationException;

/**
 * This class provides a method to validate ENZYME entries.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class EnzymeEntryValidator {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeEntryValidator.class.getName());

  private static final String ID_LINE_CONTENT_REGEXP = "\\d+?\\.\\d+?\\.\\d+?\\.\\d+?\n";
  private static final String DE_AN_CF_LINE_CONTENT_REGEXP = ".+?\\.\n";
  private static final String CA_LINE_CONTENT_REGEXP = ".+?\n";
  private static final String LAST_CA_LINE_CONTENT_REGEXP = ".+?\\.\n";
  private static final String CC_SINGLE_LINE_CONTENT_REGEXP = "CC   \\-\\!\\- .+?(\\.|http\\:\\/\\/\\S+?\\/)\n";
  private static final String CC_TWO_LINE_CONTENT_REGEXP = "CC   \\-\\!\\- .+?\\\n" +
                                                           "CC       .+?(\\.|http\\:\\/\\/\\S+?\\/)\n";
  private static final String CC_MULTI_LINE_CONTENT_REGEXP = "CC   \\-\\!\\- .+?\n" +
                                                             "(?:CC       .+?\n)+?" +
                                                             "CC       .+?(\\.|http\\:\\/\\/\\S+?\\/)\n";
  private static final String DI_LINE_CONTENT_REGEXP = ".+?\\; MIM\\:\\d+?\\.\n";
  private static final String PR_LINE_CONTENT_REGEXP = "PROSITE\\; PDOC\\d+?\\;\n";
  private static final String DR_LINE_SINGLE_XREF_REGEXP = "(?:.+?\\,\\s\\w{1,5}\\_\\w{1,5}\\s*?\\;){1}\n";
  private static final String DR_LINE_DOUBLE_XREF_REGEXP = "(?:.+?\\,\\s\\w{1,5}\\_\\w{1,5}\\s*?\\;){2}\n";
  private static final String DR_LINE_TRIPLE_XREF_REGEXP = "(?:.+?\\,\\s\\w{1,5}\\_\\w{1,5}\\s*?\\;){3}\n";

  public static boolean validate(String entry) throws EnzymeEntryValidationException {
    if (entry == null) throw new NullPointerException("Parameter 'entry' must not be null.");
    if (entry.equals("")) throw new EnzymeEntryValidationException("The given entry is empty.");

    BufferedReader entryReader = new BufferedReader(new StringReader(entry));
    try {
      StringBuffer CCLineStringBuffer = new StringBuffer();
      boolean CCLinesProcessed = false;
      String line = entryReader.readLine() + "\n";
      while (line != null) {
        if (line.equals("//\n")) break; // end of entry reached
        Pattern lineTypePattern = Pattern.compile("(ID|DE|AN|CA|CF|CC|DI|PR|DR)   (.+?\n)");
        Matcher lineTypePatternMatcher = lineTypePattern.matcher(line);
        if (lineTypePatternMatcher.find()) {
          String lineType = lineTypePatternMatcher.group(1);

          if (lineType.equals(LineType.ID.toString()) &&
              !lineTypePatternMatcher.group(2).matches(ID_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The ID line does not contain a valid EC number.");

          if (lineType.equals(LineType.DE.toString()) &&
              !lineTypePatternMatcher.group(2).matches(DE_AN_CF_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The DE line does not contain valid content.");

          if (lineType.equals(LineType.AN.toString()) &&
              !lineTypePatternMatcher.group(2).matches(DE_AN_CF_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The AN line does not contain valid content.");

          if (isLastCALine(line, entry)) {
            if (lineType.equals(LineType.CA.toString()) &&
                !lineTypePatternMatcher.group(2).matches(LAST_CA_LINE_CONTENT_REGEXP))
              throw new EnzymeEntryValidationException("The CA line does not contain valid content.");
          } else {
            if (lineType.equals(LineType.CA.toString()) &&
                !lineTypePatternMatcher.group(2).matches(CA_LINE_CONTENT_REGEXP))
              throw new EnzymeEntryValidationException("The CA line does not contain valid content.");
          }

          if (lineType.equals(LineType.CF.toString()) &&
              !lineTypePatternMatcher.group(2).matches(DE_AN_CF_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The CF line does not contain valid content.");

          // Checks every sentence (pattern: CC   -!- .+?.\n) for validity.
          if (lineType.equals(LineType.CC.toString()) && !CCLinesProcessed) {
            CCLineStringBuffer.append(line); // store line
            line = entryReader.readLine(); // read next line
            if (line != null) line += "\n";

            if (line != null && line.matches("CC.*?\n")) { // if the next line is a CC line
              if (line.matches("CC   \\-\\!\\-.*?\n")) { // check if this line is the beginning of a new sentence
                if (!CCLineStringBuffer.toString().matches(CC_SINGLE_LINE_CONTENT_REGEXP) &&
                    !CCLineStringBuffer.toString().matches(CC_TWO_LINE_CONTENT_REGEXP) &&
                    !CCLineStringBuffer.toString().matches(CC_MULTI_LINE_CONTENT_REGEXP)) // check the sentence stored in the buffer for validity
                  throw new EnzymeEntryValidationException("The comment does not comply to the ENZYME comment format.");
                CCLineStringBuffer = new StringBuffer(); // empty StringBuffer for next sentence
//                CCLineStringBuffer.append(line); // add beginning of next sentence to the empty StringBuffer
              }
            } else { // check last sentence
              if (!CCLineStringBuffer.toString().matches(CC_SINGLE_LINE_CONTENT_REGEXP) &&
                  !CCLineStringBuffer.toString().matches(CC_TWO_LINE_CONTENT_REGEXP) &&
                  !CCLineStringBuffer.toString().matches(CC_MULTI_LINE_CONTENT_REGEXP))
                throw new EnzymeEntryValidationException("The comment does not comply to the ENZYME comment format.");
              CCLinesProcessed = true;
            }
            continue; // new line has been acquired already
          }

          if (lineType.equals(LineType.DI.toString()) &&
              !lineTypePatternMatcher.group(2).matches(DI_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The DI line does not contain valid content.");

          if (lineType.equals(LineType.PR.toString()) &&
              !lineTypePatternMatcher.group(2).matches(PR_LINE_CONTENT_REGEXP))
            throw new EnzymeEntryValidationException("The PR line does not contain valid content.");

          if (lineType.equals(LineType.DR.toString())) {
            switch (getNumberOfXrefs(lineTypePatternMatcher.group(2))) {
              case 1:
                if (!lineTypePatternMatcher.group(2).matches(DR_LINE_SINGLE_XREF_REGEXP))
                  throw new EnzymeEntryValidationException("The DR line does not contain valid content.");
                break;
              case 2:
                if (!lineTypePatternMatcher.group(2).matches(DR_LINE_DOUBLE_XREF_REGEXP))
                  throw new EnzymeEntryValidationException("The DR line does not contain valid content.");
                break;
              case 3:
                if (!lineTypePatternMatcher.group(2).matches(DR_LINE_TRIPLE_XREF_REGEXP))
                  throw new EnzymeEntryValidationException("The DR line does not contain valid content.");
                break;
              default :
                throw new EnzymeEntryValidationException("The DR line does not contain valid content.");
            }
          }

        } else {
          throw new EnzymeEntryValidationException("No valid line type found.");
        }

        line = entryReader.readLine();
        if (line != null) line += "\n";
      }
    } catch (IOException e) {
      LOGGER.error("Error while reading a line of an entry.", e);
    } finally {
      try {
        entryReader.close();
      } catch (IOException e) {
        LOGGER.error("Error while closing the reader.", e);
      }
    }

    return true;
  }

  /**
   * Checks whether the given CA line is the last CA line of this entry.
   *
   * @param line  The CA line to be checked.
   * @param entry The current entry being validated.
   * @return <code>true</code> if the given CA line is the last CA line of this entry.
   */
  private static boolean isLastCALine(String line, String entry) {
    assert line != null : "Parameter 'line' must not be null.";
    assert entry != null : "Parameter 'entry' must not be null.";
    Pattern CALinePattern = Pattern.compile("(CA.*?\n)");
    Matcher CALinePatternMatcher = CALinePattern.matcher(entry);
    boolean foundLine = false;
    while (CALinePatternMatcher.find()) {
      if (foundLine) return false;
      if (CALinePatternMatcher.group(1).equals(line)) foundLine = true;
    }
    return true;
  }

  /**
   * Checks how many cross-references might be contained in the given DR line.
   *
   * @param DRLineContent The DR line to be checked.
   * @return the number of cross-reference occurrences
   */
  private static int getNumberOfXrefs(String DRLineContent) {
    assert DRLineContent != null : "Parameter 'DRLineContent' must not be null.";
    Pattern DRXrefPattern = Pattern.compile(".+?\\;");
    Matcher DRXrefPatternMatcher = DRXrefPattern.matcher(DRLineContent);
    int count = 0;
    while (DRXrefPatternMatcher.find()) {
      count++;
    }
    return count;
  }

}
