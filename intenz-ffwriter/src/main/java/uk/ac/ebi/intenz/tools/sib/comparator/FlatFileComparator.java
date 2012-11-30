package uk.ac.ebi.intenz.tools.sib.comparator;

import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.tools.sib.exceptions.EnzymeEntryValidationException;
import uk.ac.ebi.intenz.tools.sib.validator.EnzymeEntryValidator;
import uk.ac.ebi.intenz.tools.sib.writer.LineType;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.exceptions.EcException;

import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

/**
 * This class compares two ENZYME.dat flat file formats.<br/>
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class FlatFileComparator {

   private static final Logger LOGGER =
	   Logger.getLogger(FlatFileComparator.class.getName());
   private static final Logger LOGGER_ID = Logger.getLogger("ID");
   private static final Logger LOGGER_DE = Logger.getLogger("DE");
   private static final Logger LOGGER_AN = Logger.getLogger("AN");
   private static final Logger LOGGER_CA = Logger.getLogger("CA");
   private static final Logger LOGGER_CC = Logger.getLogger("CC");
   private static final Logger LOGGER_CF = Logger.getLogger("CF");
   private static final Logger LOGGER_DI = Logger.getLogger("DI");
   private static final Logger LOGGER_DR = Logger.getLogger("DR");
   private static final Logger LOGGER_PR = Logger.getLogger("PR");

   /**
    * This number indicates the amount of lines which contain the header of ENZYME.dat.
    */
   private static final int START_LINE_NO = 27;
   private static final String NEW_LINE = "\n";

   private static final String END_OF_ENTRY = new String("\\/\\/.*?");

   private static final String MISSING_ENZYME_ENTRY = new String("MISSING ENZYME ENTRY");
   private static final String MISSING_INTENZ_ENTRY = new String("MISSING INTENZ ENTRY");

   private static int idCounter = 0;
   private static int deCounter = 0;
   private static int anCounter = 0;
   private static int caCounter = 0;
   private static int ccCounter = 0;
   private static int cfCounter = 0;
   private static int diCounter = 0;
   private static int prCounter = 0;
   private static int drCounter = 0;

   public static void compare (File flatFile1, File flatFile2) {
      BufferedReader flatFile1Reader = null;
      BufferedReader flatFile2Reader = null;
      try {
         flatFile1Reader = new BufferedReader(new FileReader(flatFile1));
         flatFile2Reader = new BufferedReader(new FileReader(flatFile2));
         // This is the line number used to count where we are in the flat file
         int lineNo = 1;

         String lineOfLoadedFF = flatFile1Reader.readLine();
         lineOfLoadedFF = addNewLine(lineOfLoadedFF);
         String lineOfGeneratedFF = flatFile2Reader.readLine();
         lineOfGeneratedFF = addNewLine(lineOfGeneratedFF);

         StringBuffer entryA = new StringBuffer();
         StringBuffer entryB = new StringBuffer();
         String missingEntry = "";
         // Compare lines and return a message in case of a difference between the line's content.
         while ( lineOfLoadedFF != null && lineOfGeneratedFF != null ) {
            // If lineNo is greater than the start number start appending.
            if ( lineNo > START_LINE_NO ) {
               if (missingEntry != MISSING_INTENZ_ENTRY) entryA.append(lineOfLoadedFF); // FIXME: maintain missingEntry flag
               if (missingEntry != MISSING_ENZYME_ENTRY) entryB.append(lineOfGeneratedFF); // FIXME: maintain missingEntry flag
               try {
                  // There are two scenarios.
                  // Either one of the files can finish first in which case it has to iterate through the second
                  // file to find the end of entry of the second before it can compare entries.
                  if ( !isNotEndOfEntry(lineOfLoadedFF) ) {
                     // If its not the end of entry of the second file - then find it before proceeding.
                     if (missingEntry != MISSING_ENZYME_ENTRY)
                         findEndOfEntry(lineOfGeneratedFF, flatFile2Reader, entryB);

                     String outputResult = compareEntries(entryA.toString(), entryB.toString());
                     if ( outputResult.length() != 0 ){
                         missingEntry = checkForMissingEntries(outputResult);
                         LOGGER.info(outputResult);
                     } else {
                         missingEntry = "";
                     }
                     if (missingEntry != MISSING_ENZYME_ENTRY) entryB.delete(0, entryB.length());
                     if (missingEntry != MISSING_INTENZ_ENTRY) entryA.delete(0, entryA.length());

                     // Else the second file has finished first and it must check the first file for its
                     // end of entry pattern
                  } else if ( !isNotEndOfEntry(lineOfGeneratedFF) ) {
                     if (missingEntry != MISSING_INTENZ_ENTRY)
                         findEndOfEntry(lineOfLoadedFF, flatFile1Reader, entryA);

                     String outputResult = compareEntries(entryA.toString(), entryB.toString());
                     if ( outputResult.length() != 0 ){
                         missingEntry = checkForMissingEntries(outputResult);
                         LOGGER.info(outputResult);
                     } else {
                         missingEntry = "";
                     }
                     if (missingEntry != MISSING_ENZYME_ENTRY) entryB.delete(0, entryB.length());
                     if (missingEntry != MISSING_INTENZ_ENTRY) entryA.delete(0, entryA.length());
                  }
               } catch ( EnzymeEntryValidationException e ) {
                  LOGGER.error(e);
                  clearBuffers(entryA, entryB);
               }
            }

            lineNo++;
            if (missingEntry != MISSING_INTENZ_ENTRY) lineOfLoadedFF = nextLine(flatFile1Reader);
            if (missingEntry != MISSING_ENZYME_ENTRY) lineOfGeneratedFF = nextLine(flatFile2Reader);
            if ( lineOfLoadedFF.equals("null" + NEW_LINE) || lineOfGeneratedFF.equals("null" + NEW_LINE) )
               break;
         }

         logTotals();

      } catch ( IOException e ) {
         LOGGER.error("Error while reading a flat file.", e);
      } finally {
         try {
            if ( flatFile1Reader != null ) flatFile1Reader.close();
            if ( flatFile2Reader != null ) flatFile2Reader.close();
         } catch ( IOException e ) {
            LOGGER.error("Error while closing a reader.", e);
         }
      }
   }
   
   /**
    * Called only for unmatching entries.
    * @param outputResult
    * @return MISSING_ENZYME_ENTRY or MISSING_INTENZ_ENTRY when the entry ID is different,
    *   empty string otherwise.
    */
   private static String checkForMissingEntries(String outputResult) {
       String missingEntry = "";
       if (outputResult.startsWith(MISSING_ENZYME_ENTRY)){
           missingEntry = MISSING_ENZYME_ENTRY;
       } else if (outputResult.startsWith(MISSING_INTENZ_ENTRY)){
           missingEntry = MISSING_INTENZ_ENTRY;
       }
       return missingEntry;
   }

    private static String nextLine(BufferedReader reader) throws IOException {
         String line = reader.readLine();
         return addNewLine(line);
    }


   /**
    * Compares two entries by checking their data line by line.
    *
    * @param entryA The first entry...
    * @param entryB ...and the second one.
    * @return the differences (if any)
    * @throws EnzymeEntryValidationException if an enzyme does not comply to the ENZYME format.
    */
   public static String compareEntries (String entryA, String entryB) throws EnzymeEntryValidationException {
      if ( entryA == null ) throw new NullPointerException("Parameter 'entryA' must not be null.");
      if ( entryB == null ) throw new NullPointerException("Parameter 'entryB' must not be null.");
      LOGGER.debug(entryA);
      LOGGER.debug(entryB);
      entryA = reconstructSentences(entryA);
      entryB = reconstructSentences(entryB);
      // PreParse entries to reconstruct lines
      // Check if both entries contain values in the correct ENZYME entry format.
      try {
         EnzymeEntryValidator.validate(entryA);
      } catch ( EnzymeEntryValidationException e ) {
         throw new EnzymeEntryValidationException("Error in entryA: "
        		 + e.getMessage()
        		 + ((entryA.indexOf('\n') > -1)? entryA.substring(0, entryA.indexOf('\n')) : entryA));
      }
      try {
         EnzymeEntryValidator.validate(entryB);
      } catch ( EnzymeEntryValidationException e ) {
         throw new EnzymeEntryValidationException("Error in entryB: "
        		 + e.getMessage()
        		 + ((entryB.indexOf('\n') > -1)? entryB.substring(0, entryB.indexOf('\n')) : entryB));
      }

      StringBuffer differences = new StringBuffer();

      // Return no difference if both entries are identical.
      if ( entryA.equals(entryB) )
         return differences.toString();

      if ( lineIsDifferent(entryA, entryB, LineType.ID) ) {
          // Check for missing entries:
          String idLine = null;
          String ecString = null;
          try {
              idLine = addNewLine(getLine(entryA, LineType.ID));
              ecString = getEC(idLine);
              EnzymeCommissionNumber ecA = EnzymeCommissionNumber.valueOf(ecString);
              idLine = addNewLine(getLine(entryB, LineType.ID));
              ecString = getEC(idLine);
              EnzymeCommissionNumber ecB = EnzymeCommissionNumber.valueOf(ecString);
              int ecDiff = ecA.compareTo(ecB);
              if (ecDiff != 0){
                  if (ecDiff < 0){
                      differences.append(MISSING_INTENZ_ENTRY);
                      differences.append(": ");
                      differences.append(ecB.toString());
                      differences.append(" does not exist in ENZYME");
                  } else if (ecDiff > 0){
                      differences.append(MISSING_ENZYME_ENTRY);
                      differences.append(": ");
                      differences.append(ecA.toString());
                      differences.append(" does not exist in IntEnz");
                  }
                  LOGGER_ID.warn(differences.toString());
                  idCounter++;
                  return differences.toString();
              }
          } catch (EcException e) {
              throw new EnzymeEntryValidationException("Bad EC number: " + ecString);
          }
//          String diffString = getDifferenceMessage(LineType.ID, entryA, entryB, LOGGER_ID);
//         if ( diffString != null && !diffString.equals("") ) {
//            idCounter++;
//            differences.append(diffString);
//         }
      }
      if ( lineIsDifferent(entryA, entryB, LineType.DE) ) {
         String diffString = getDifferenceMessage(LineType.DE, entryA, entryB, LOGGER_DE);
         if ( diffString != null && !diffString.equals("") ) {
            deCounter++;
            differences.append(diffString);
         }
      }

      int lineIndex = linesAreDifferent(entryA, entryB, LineType.AN);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.AN, entryA, entryB, lineIndex, LOGGER_AN);
         if ( diffString != null && !diffString.equals("") ) {
            anCounter++;
            differences.append(diffString);
         }
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.CA);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.CA, entryA, entryB, lineIndex, LOGGER_CA);
         if ( diffString != null && !diffString.equals("") ) {
            caCounter++;
            differences.append(diffString);
         }
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.CF);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.CF, entryA, entryB, lineIndex, LOGGER_CF);
         if ( diffString != null && !diffString.equals("") ) {
            cfCounter++;
            differences.append(diffString);
         }
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.CC);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.CC, entryA, entryB, lineIndex, LOGGER_CC);
         if ( diffString != null && !diffString.equals("") ) {
            ccCounter++;
            differences.append(diffString);
         }
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.DI);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.DI, entryA, entryB, lineIndex, LOGGER_DI);
         if ( diffString != null && !diffString.equals("") ) {
            diCounter++;
            differences.append(diffString);
         }
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.PR);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.PR, entryA, entryB, lineIndex, LOGGER_PR);
         if ( diffString != null && !diffString.equals("") ) {
            prCounter++;
            differences.append(diffString);
         }
         //differences.append(getDifferenceMessage(LineType.PR, getEC(entryA), lineIndex));
      }

      lineIndex = linesAreDifferent(entryA, entryB, LineType.DR);
      if ( lineIndex != -1 ) {
         String diffString = getDifferenceMessage(LineType.DR, entryA, entryB, lineIndex, LOGGER_DR);
         if ( diffString != null && !diffString.equals("") ) {
            drCounter++;
            differences.append(diffString);
         }
         // differences.append(getDifferenceMessage(LineType.DR, getEC(entryA), lineIndex));
      }

      if ( differences.length() == 0 || differences.length() == 1 ) {
         //    LOGGER.info("PROBLEM HERE for entryA: " + entryA.toString());
         //    LOGGER.info("PROBLEM HERE for entryB: " + entryB.toString());
      }

      return differences.toString();
   }

   //----------------------------- PRIVATE METHODS --------------------------//

   /**
    * This method preparses the entry from the flat file and tries to reconstruct
    * sentences. If first looks for hyphens and then appends them without a space
    * and then looks for lines not ending with full stops and appends them with a
    * space.<br/>
    * @param entry
    * @return
    */
   private static String reconstructSentences (String entry) {
      // Variable definitions
      StringBuffer finalPreParsed = new StringBuffer();
      StringBuffer hypenParsed = new StringBuffer();
      Pattern idPattern = Pattern.compile("(ID   \\d+?\\.\\d+?\\.\\d+?\\.\\d+?\n)");
      Matcher idMatcher = idPattern.matcher(entry);

      // Remove identifier
      if ( idMatcher.find() ) {
         finalPreParsed.append(idMatcher.group());
         entry = entry.substring(finalPreParsed.length(), entry.length());
      }

      // Match hyphens first lines
      Pattern hyphenPattern = Pattern.compile("\\-\n(\\p{Upper}{2}\\s{3,}+)");
      Matcher hyphenMatcher = hyphenPattern.matcher(entry);
      boolean foundHyphen = hyphenMatcher.find();
      if ( foundHyphen ) {
         String whatFollows = entry.substring(hyphenMatcher.end());
         if (whatFollows.startsWith("or ")
                 || whatFollows.startsWith("and ")
                 || whatFollows.startsWith("bonds ")){
             hypenParsed.append(hyphenMatcher.replaceAll("- "));
         } else {
             hypenParsed.append(hyphenMatcher.replaceAll("-"));
         }
      } else {
         hypenParsed.append(entry);
      }

      // Match new lines first lines
//      Pattern newLinePattern = Pattern.compile("(.[^\\.;\\/])(\n(\\p{Upper}{2}\\s{3,}+))");
      removeSpareText(hypenParsed, "(.[^\\.;\\/]|sp\\.|e\\.g\\.|cf\\.)(\n(\\p{Upper}{2}\\s{3,}+))");
      
      removeSpareText(hypenParsed, "(CA   .+?)\nCA   ");
      
      removeSpareText(hypenParsed, "(CC   .+?)\nCC       ");
      
      finalPreParsed.append(hypenParsed.toString());

      return finalPreParsed.toString();
   }
    
    /**
     * Removes not needed text
     * @param text
     * @param regex A regex with at least one group in parenthesis,
     *      which will be kept in the text. The rest of the pattern will be
     *      discarded. A space will be added at the end.
     */
    private static void removeSpareText(StringBuffer text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toString());
        boolean found = matcher.find();
        while (found) {
            String subString = matcher.group(0);
            String replacement = matcher.group(1);
            text.replace(text.indexOf(subString), text.indexOf(subString)
                    + subString.length(), replacement + " ");
            found = matcher.find();
        }
    }

   /**
    * Helper method to find the end of an entry being read from the flat file.
    * It also appends the data to the StringBuffer provided.
    *
    * @param lineOfLoadedFF
    * @param flatFile1Reader
    * @param entryA
    * @throws IOException
    */
   private static void findEndOfEntry (String lineOfLoadedFF, BufferedReader flatFile1Reader,
                                       StringBuffer entryA) throws IOException {
      if ( isNotEndOfEntry(lineOfLoadedFF) ) {
         String line = readLineAndAppend(flatFile1Reader, entryA);
         while ( isNotEndOfEntry(line) ) {
            line = readLineAndAppend(flatFile1Reader, entryA);
         }
      }
   }

   private static boolean isNotEndOfEntry (String line) {
      return (line != null && !line.trim().matches(END_OF_ENTRY));
   }

   /**
    * This helper reads the line from the Reader and appends a new line character as well the line
    * being read.
    *
    * @param flatFile2Reader
    * @param entryB
    * @return True if its the end of entry else false if it isn't
    * @throws IOException
    */
   private static String readLineAndAppend (BufferedReader flatFile2Reader, StringBuffer entryB) throws IOException {
      String lineOfGeneratedFF;
      lineOfGeneratedFF = flatFile2Reader.readLine();
      lineOfGeneratedFF = addNewLine(lineOfGeneratedFF);
      entryB.append(lineOfGeneratedFF);
      return lineOfGeneratedFF;
   }

   /**
    * Convenience method to clear the buffers.
    *
    * @param entryA
    * @param entryB
    */
   private static void clearBuffers (StringBuffer entryA, StringBuffer entryB) {
      entryA.delete(0, entryA.length());
      entryB.delete(0, entryB.length());
   }

   /**
    * Logs the totals after the files have been compared.
    */
   private static void logTotals () {
      int totalCounter = idCounter + deCounter + anCounter + caCounter + ccCounter
            + cfCounter + diCounter + drCounter + prCounter;
      LOGGER.info("-------------------------------------------------");
      LOGGER.info("ID: " + idCounter);
      LOGGER.info("DE: " + deCounter);
      LOGGER.info("AN: " + anCounter);
      LOGGER.info("CA: " + caCounter);
      LOGGER.info("CC: " + ccCounter);
      LOGGER.info("CF: " + cfCounter);
      LOGGER.info("DI: " + diCounter);
      LOGGER.info("DR: " + drCounter);
      LOGGER.info("PR: " + prCounter);
      LOGGER.info("-------------------------------------------------");
      LOGGER.info("Total Errors: " + totalCounter);
      LOGGER.info("-------------------------------------------------");
   }

   /**
    * This method is a convenience method to add a new line character.
    *
    * @param lineToAdd
    */
   private static String addNewLine (String lineToAdd) {
      return lineToAdd += NEW_LINE;
   }

   /**
    * Compares the lines with the given line type of both entries with each other.
    *
    * @param entryA   The first entry to be compared with...
    * @param entryB   ...the second entry.
    * @param lineType The line type of the lines to be compared.
    * @return <code>true</code> if both lines with the same line type of the given entries are identical.
    */
   private static boolean lineIsDifferent (String entryA, String entryB, LineType lineType) {
      assert entryA != null : "Parameter 'entryA' must not be null.";
      assert entryB != null : "Parameter 'entryB' must not be null.";
      assert lineType != null : "Parameter 'lineType' must not be null.";
      String lineEntryA = getLine(entryA, lineType);
      String lineEntryB = getLine(entryB, lineType);
      return !lineEntryA.equals(lineEntryB);
   }

   /**
    * Compares the lines with the given line type of both entries with each other.
    *
    * @param entryA   The first entry to be compared with...
    * @param entryB   ...the second entry.
    * @param lineType The line type of the lines to be compared.
    * @return the line index where the difference has been spotted, or -1 if all lines with the given line type of the
    *         given entries are identical.
    */
   private static int linesAreDifferent (String entryA, String entryB, LineType lineType) {
      assert entryA != null : "Parameter 'entryA' must not be null.";
      assert entryB != null : "Parameter 'entryB' must not be null.";
      assert lineType != null : "Parameter 'lineType' must not be null.";
      List linesEntryA = getLines(entryA, lineType);
      List linesEntryB = getLines(entryB, lineType);
      for ( int lineIndex = 0; lineIndex < linesEntryA.size(); lineIndex++ ) {
         String lineEntryA = (String) linesEntryA.get(lineIndex);
         if ( linesEntryB.size() - 1 < lineIndex ) //something missing
            return lineIndex + 1;
         String lineEntryB = (String) linesEntryB.get(lineIndex);
         if ( !lineEntryA.equals(lineEntryB) )
            return lineIndex + 1;
      }
      return -1;
   }

   /**
    * Creates a message describing the difference that has been found.
    *
    * @param lineType The line type of the line where the difference has been spotted.
    * @param entryA   The first entry.
    * @param entryB   The second entry.
    * @param logger
    * @return the message describing the difference.
    */
   private static String getDifferenceMessage (LineType lineType, String entryA, String entryB, Logger logger) {
      assert lineType != null : "Parameter 'lineType' must not be null.";
      assert entryA != null : "Parameter 'entryA' must not be null.";
      assert entryB != null : "Parameter 'entryB' must not be null.";
      StringBuffer differences = new StringBuffer();
      differences.append(lineType.toString());
      differences.append(" line is different for entry: ");
      differences.append(getEC(entryA));
      differences.append(" (EC of ENZYME.dat)");
      logger.info(differences);
      StringBuffer enzymeDat = new StringBuffer("ENZYME.DAT: ").append(getLine(entryA, lineType));
      differences.append(enzymeDat);
      logger.info(enzymeDat);
      StringBuffer intenzDat = new StringBuffer("INTENZ.DAT: ").append(getLine(entryB, lineType));
      differences.append(intenzDat);
      logger.info(intenzDat);
      return differences.toString();
   }

   /**
    * Creates a message describing the difference that has been found.
    * <p/>
    * This method also gives the line number when line types have been checked which can have multiple lines.
    *
    * @param lineType  The line type of the line where the difference has been spotted.
    * @param entryA    The EC number of the first entry.
    * @param lineIndex The number of the line in the group of lines.
    * @param logger
    * @return the message describing the difference.
    */
   private static String getDifferenceMessage (LineType lineType, String entryA, String entryB, int lineIndex, Logger logger) {
      assert lineType != null : "Parameter 'lineType' must not be null.";
      assert entryA != null : "Parameter 'entryA' must not be null.";
      StringBuffer differences = new StringBuffer();
      differences.append(lineType.toString());
      differences.append(" line ");
      differences.append(lineIndex);
      differences.append(" is different for entry: ");
      differences.append(getEC(entryA));
      differences.append(" (EC of enzyme.dat)");
      logger.info(differences.toString());
      StringBuffer enzymeDat = new StringBuffer("ENZYME.DAT: ").append(getLineOnLineIndex(entryA, lineType, lineIndex));
      differences.append(enzymeDat);
      logger.info(enzymeDat);
      StringBuffer intenzDat = new StringBuffer("INTENZ.DAT: ").append(getLineOnLineIndex(entryB, lineType, lineIndex));
      differences.append(intenzDat);
      logger.info(intenzDat);
      return differences.toString();
   }

   /**
    * Extracts the EC number from the ID line.
    *
    * @param entryA The entry containing the ID line.
    * @return the EC number.
    */
   private static String getEC (String entryA) {
      assert entryA != null : "Parameter 'entryA' must not be null.";
      Pattern IDLinePattern = Pattern.compile("ID   (\\d+?\\.\\d+?\\.\\d+?\\.\\d+?)\n");
      Matcher IDLinePatternMatcher = IDLinePattern.matcher(entryA);
      if ( IDLinePatternMatcher.find() ) return IDLinePatternMatcher.group(1);
      return "";
   }

   /**
    * Extracts the line of the given line type from the given entry.
    *
    * @param entry    The entry from which the line will be exctracted.
    * @param lineType The line type of the line to be extracted.
    * @return the line or an empty string if no ID line could be found.
    */
   private static String getLine (String entry, LineType lineType) {
      assert entry != null : "Parameter 'entry' must not be null.";
      Pattern linePattern = Pattern.compile("(" + lineType.toString() + "   .+?)\n");
      Matcher linePatternMatcher = linePattern.matcher(entry);
      if ( linePatternMatcher.find() ) return linePatternMatcher.group(1);
      return "";
   }

   /**
    * Extracts the all lines of the given line type from the given entry.
    *
    * @param entry    The entry from which the lines will be exctracted.
    * @param lineType The line type of the lines to be extracted.
    * @return the lines stored in an {@link java.util.ArrayList}.
    */
   private static List getLines (String entry, LineType lineType) {
      assert entry != null : "Parameter 'entry' must not be null.";
      assert lineType != null : "Parameter 'lineType' must not be null.";
      Pattern linePattern = Pattern.compile("(" + lineType.toString() + "   .+?)\n");
      Matcher linePatternMatcher = linePattern.matcher(entry);
      List lines = new ArrayList();
      while ( linePatternMatcher.find() ) lines.add(linePatternMatcher.group(1));
      return lines;
   }

   /**
    * Static method which returns the line based on the lineIndex provided.
    *
    * @param entry
    * @param lineType
    * @param lineIndex
    * @return
    * @throws IndexOutOfBoundsException Will be thrown if the index is larger than the size
    *                                   of the list generated.
    */
   private static String getLineOnLineIndex (String entry, LineType lineType, int lineIndex) {
      --lineIndex;
      List lines = getLines(entry, lineType);
      if ( lines.size() == 0 )
         return new String("");
      else if ( lineIndex < lines.size() )
         return (String) lines.get(lineIndex);
      else
         return (String) lines.get(lines.size() - 1);
   }

}
