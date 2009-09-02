package uk.ac.ebi.intenz.tools.sib.translator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Vector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;

/**
 * This class is used to log translation activities during the parse process.
 *
 * @author Michael Darsow
 * @version 1.0 - 08-Apr-2003
 */
public class SIBEnzymeLogger {
  public static final int COMMON_NAMES = 0;

  public static final int REACTIONS = 1;

  public static final int OTHER_NAMES = 2;

  public static final int COMMENTS = 3;

  private boolean enzymeLogFormat;

  /**
   * The file name will be the EC number (1_1_1_1.log).
   */
  private File outputFile;

  /**
   * For each entry a log file is generated.
   */
  private FileWriter out;

  /**
   * Initialises the log file of the given EC.
   *
   * @param ec Used for the file name.
   */
  public SIBEnzymeLogger(String dir, String ec) {
    enzymeLogFormat = false;
    String ecOrig = ec;
    Pattern p = Pattern.compile("\\.");
    Matcher m = p.matcher(ec);
    ec = m.replaceAll("_");

    try {
      outputFile = new File(dir + ec + ".log");
      out = new FileWriter(outputFile);
      out.write("EC " + ecOrig + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Enables a logger which stores all results in one file.
   *
   * @param dir             Directory of the log file.
   * @param type            Type of comparison (see static fields).
   * @param enzymeLogFormat
   */
  public SIBEnzymeLogger(String dir, int type, boolean enzymeLogFormat) {
    this.enzymeLogFormat = enzymeLogFormat;
    if (dir.charAt(dir.length() - 1) != '/') dir += "/";
    if (enzymeLogFormat)
      writeEnzymeLog(type, dir);
    else
      writeHtmlLog(type, dir);
  }

  private void writeEnzymeLog(int type, String dir) {
    Date today = new Date(System.currentTimeMillis());
    try {
      switch (type) {
        case 0:
          outputFile = new File(dir + "common_names_clashes.log");
          out = new FileWriter(outputFile);
          out.write("Common name clashes - generated on ");
          out.write(today.toString());
          break;
        case 1:
          outputFile = new File(dir + "reaction_clashes.log");
          out = new FileWriter(outputFile);
          out.write("Reaction clashes - generated on ");
          out.write(today.toString());
          break;
        case 2:
          outputFile = new File(dir + "other_names.log");
          out = new FileWriter(outputFile);
          out.write("Synonyms clashes - generated on ");
          out.write(today.toString());
          break;
        case 3:
          outputFile = new File(dir + "comments.log");
          out = new FileWriter(outputFile);
          out.write("Comments clashes - generated on ");
          out.write(today.toString());
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeHtmlLog(int type, String dir) {
    try {
      switch (type) {
        case 0:
          outputFile = new File(dir + "common_names_log.html");
          out = new FileWriter(outputFile);
          out.write("<html><head><title>Common name clashes</title></title></head><body>\n");
          out.write("<center><h2>Common names clashes</h2></center>\n");
          out.write("<table width=\"100%\" border=\"0\">\n");
          out.write("<tr><td align=\"center\"><b>EC</b></td><td align=\"center\"><b>IUBMB &rarr; ENZYME</b></td></tr>\n");
          break;
        case 1:
          outputFile = new File(dir + "reactions_log.html");
          out = new FileWriter(outputFile);
          out.write("<html><head><title>Reaction clashes</title></title></head><body>\n");
          out.write("<center><h2>Reaction clashes</h2></center>\n");
          out.write("<table width=\"100%\" border=\"0\">\n");
          out.write("<tr><td align=\"center\"><b>EC</b></td><td align=\"center\"><b>IUBMB &rarr; ENZYME</b></td></tr>\n");
          break;
        case 2:
          outputFile = new File(dir + "other_names_log.html");
          out = new FileWriter(outputFile);
          out.write("<html><head><title>Other names clashes</title></title></head><body>\n");
          out.write("<center><h2>Other names clashes</h2></center>\n");
          out.write("<table width=\"100%\" border=\"0\">\n");
          out.write("<tr><td align=\"center\"><b>EC</b></td><td align=\"center\"><b>Smaller set</b><td align=\"left\"><b>Names not matching\nin smaller set</b></td></tr>\n");
          break;
        case 3:
          outputFile = new File(dir + "comments_log.html");
          out = new FileWriter(outputFile);
          out.write("<html><head><title>Comments clashes</title></title></head><body>\n");
          out.write("<center><h2>Comments clashes</h2></center>\n");
          out.write("<table width=\"100%\" border=\"0\">\n");
          out.write("<tr><td align=\"center\"><b>EC</b></td><td align=\"center\"><b>IUBMB &rarr; ENZYME</b></td></tr>\n");
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//  /**
//   * Adds information to the log file.
//   *
//   * @param before Not translated string.
//   * @param after Translated string.
//   */
//  public void add(String before, String after) {
//    try {
//      out.write(before + " --> " + after + "\n");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  /**
   * Adds information to the log file.
   *
   * @param before Not translated string.
   * @param after  Translated string.
   * @param ec     The EC number.
   */
  public void add(String before, String after, String ec) {
    if (!enzymeLogFormat) {
      before = encodeTags(before);
      after = encodeTags(after);
    }
//    else {
//      before = removeTags(before);
//      after = removeTags(after);
//    }
    if (before.length() > 2)
      before = before.substring(0, 1).toUpperCase() + before.substring(1);
    if (after.length() > 2)
      after = after.substring(0, 1).toUpperCase() + after.substring(1);
    int diffStartIndex = getDifferenceStartIndex(before, after);
    try {
      if (diffStartIndex + 1 < before.length() && diffStartIndex + 1 < after.length()) {
//        System.out.println("case1");
        int beforeEnd = 1;
        int afterEnd = 1;
        if (before.charAt(diffStartIndex) == '&') beforeEnd = 4;
        if (after.charAt(diffStartIndex) == '&') afterEnd = 4;
        if (enzymeLogFormat) {
          out.write("\nAC:\n");
          out.write(ec);
          out.write("\nFROM:\n");
          out.write(after.substring(0, diffStartIndex));
          out.write(after.substring(diffStartIndex, diffStartIndex + afterEnd));
          out.write(after.substring(diffStartIndex + afterEnd));
          out.write(".\nTO:\n");
          out.write(before.substring(0, diffStartIndex));
          out.write(before.substring(diffStartIndex, diffStartIndex + beforeEnd));
          out.write(before.substring(diffStartIndex + beforeEnd));
          out.write(".\n//");
        } else {
          out.write("<tr><td valign=\"top\"><font color=\"blue\"><a href=\"http://www.ebi.ac.uk/~mdarsow/cgi-bin/development/test/intenz_curator/handler?cmd=SearchAllEC&ec=" + ec + "\">" + ec + "</a></font></td><td>" + before.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + before.substring(diffStartIndex, diffStartIndex + beforeEnd) + "</font>" + before.substring(diffStartIndex + beforeEnd) +
                  "<br/>" + after.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + after.substring(diffStartIndex, diffStartIndex + afterEnd) + "</font>" + after.substring(diffStartIndex + afterEnd) + "</td></tr>\n");
        }
      }
      if (diffStartIndex + 1 >= before.length() && diffStartIndex + 1 < after.length()) {
//        System.out.println("case2");
        int afterEnd = 1;
        if (after.charAt(diffStartIndex) == '&') afterEnd = 4;
        if (enzymeLogFormat) {
          out.write("\nAC:\n");
          out.write(ec);
          out.write("\nFROM:\n");
          out.write(after.substring(0, diffStartIndex));
          out.write(after.substring(diffStartIndex, diffStartIndex + afterEnd));
          out.write(after.substring(diffStartIndex + afterEnd));
          out.write(".\nTO:\n");
          out.write(before.substring(0, diffStartIndex));
          out.write(before.substring(diffStartIndex));
          out.write(".\n//");
        } else {
          out.write("<tr><td valign=\"top\"><font color=\"blue\"><a href=\"http://www.ebi.ac.uk/~mdarsow/cgi-bin/development/test/intenz_curator/handler?cmd=SearchAllEC&ec=" + ec + "\">" + ec + "</a></font></td><td>" + before.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + before.substring(diffStartIndex) + "</font><br/>" + after.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + after.substring(diffStartIndex, diffStartIndex + afterEnd) + "</font>" + after.substring(diffStartIndex + afterEnd) + "</td></tr>\n");
        }
      }
      if (diffStartIndex + 1 < before.length() && diffStartIndex + 1 >= after.length()) {
//        System.out.println("case3");
        int beforeEnd = 1;
        if (before.charAt(diffStartIndex) == '&') beforeEnd = 4;
        if (enzymeLogFormat) {
          out.write("\nAC:\n");
          out.write(ec);
          out.write("\nFROM:\n");
          out.write(after.substring(0, diffStartIndex));
          out.write(after.substring(diffStartIndex));
          out.write(".\nTO:\n");
          out.write(before.substring(0, diffStartIndex));
          out.write(before.substring(diffStartIndex, diffStartIndex + beforeEnd));
          out.write(before.substring(diffStartIndex + beforeEnd));
          out.write(".\n//");
        } else {
          out.write("<tr><td valign=\"top\"><font color=\"blue\"><a href=\"http://www.ebi.ac.uk/~mdarsow/cgi-bin/development/test/intenz_curator/handler?cmd=SearchAllEC&ec=" + ec + "\">" + ec + "</a></font></td><td>" + before.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + before.substring(diffStartIndex, diffStartIndex + beforeEnd) + "</font>" + before.substring(diffStartIndex + beforeEnd) +
                  "<br/>" + after.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + after.substring(diffStartIndex) + "</font></td></tr>\n");
        }
      }
      if (diffStartIndex + 1 >= before.length() && diffStartIndex + 1 >= after.length()) {
//        System.out.println("case4");
        if (enzymeLogFormat) {
          out.write("\nAC:\n");
          out.write(ec);
          out.write("\nFROM:\n");
          out.write(after.substring(0, diffStartIndex));
          out.write(after.substring(diffStartIndex));
          out.write(".\nTO:\n");
          out.write(before.substring(0, diffStartIndex));
          out.write(before.substring(diffStartIndex));
          out.write(".\n//");
        } else {
          out.write("<tr><td valign=\"top\"><font color=\"blue\"><a href=\"http://www.ebi.ac.uk/~mdarsow/cgi-bin/development/test/intenz_curator/handler?cmd=SearchAllEC&ec=" + ec + "\">" + ec + "</a></font></td><td>" + before.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + before.substring(diffStartIndex) + "</font><br/>" + after.substring(0, diffStartIndex) +
                  "<font color=\"red\">" + after.substring(diffStartIndex) + "</font></td></tr>\n");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String removeTags(String text) {
    return text.replaceAll("\\<.+?\\>", "");
  }

  public void addOtherNameLog(String nameNotMatching, String set, String ec) {
    nameNotMatching = encodeTags(nameNotMatching);
    System.out.println("nameNotMatching = " + nameNotMatching);
    System.out.println("set = " + set);
    System.out.println("ec = " + ec);
    try {
      if (enzymeLogFormat) {

      } else
        out.write("<tr><td valign=\"top\"><font color=\"blue\"><a href=\"http://www.ebi.ac.uk/~mdarsow/cgi-bin/development/test/intenz_curator/handler?cmd=SearchAllEC&ec=" + ec + "\">" + ec + "</a></font></td>" +
                "<td>" + set + "</td><td>" + nameNotMatching + "</td></tr>");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

//  /**
//   * Adds information to the log file.
//   *
//   * @param before Not translated <code>Vector</code> of <code>String</code>s.
//   * @param after Translated <code>Vector</code> of <code>String</code>s.
//   * @param type Line type.
//   */
//  public void add(Vector before, Vector after, String type) {
//    try {
//      out.write(type + ":\n");
//      for (int iii = 0; iii < before.size(); iii++) {
//        String beforeString = (String) before.elementAt(iii);
//        String afterString = (String) after.elementAt(iii);
//        out.write(beforeString + " --> " + afterString + "\n");
//      }
//      out.write("\n");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

//  /**
//   * The disease line is a bit different from the other lines (see above).
//   *
//   * @param before Not translated <code>Vector</code> of <code>String</code>s.
//   * @param after Translated <code>Vector</code> of <code>String</code>s.
//   */
//  public void addDisease(Vector before, Vector after) {
//    try {
//      out.write("DI:\n");
//      for (int iii = 0; iii < before.size(); iii++) {
//        Vector beforeVector = (Vector) before.elementAt(iii);
//        Vector afterVector = (Vector) after.elementAt(iii);
//        String beforeName = (String) beforeVector.get(0);
//        String beforeNumber = (String) beforeVector.get(1);
//        String afterName = (String) afterVector.get(0);
//        String afterNumber = (String) afterVector.get(1);
//        out.write(beforeName + " - " + beforeNumber + " --> " + afterName + " - " + afterNumber + "\n");
//      }
//      out.write("\n");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

//  /**
//   * Comparator output is logged here.
//   *
//   * @param lineType Line type which has been compared with the IUBMB data set.
//   * @param result Result message of comparison.
//   */
//  public void addComparison(String lineType, String result) {
//    try {
//      out.write("Compared line type:\t" + lineType + "\n");
//      out.write("Result of comparison: " + result);
//      out.write("\n");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  /**
   * Must be called to close the log file.
   */
  public void close() {
    try {
      if(!enzymeLogFormat) out.write("</table></body></html>");
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String encodeTags(String text) {
    Pattern p = Pattern.compile("\\<");
    Matcher m = p.matcher(text);
    text = m.replaceAll("&lt;");

    p = Pattern.compile("\\>");
    m = p.matcher(text);
    text = m.replaceAll("&gt;");

    return text;
  }

  private int getDifferenceStartIndex(String before, String after) {
    // Todo: include this rule into rule set in SIBAndIUBMBComparator!
//		if (before.length() > 2)
//			before = before.substring(0, 1).toUpperCase() + before.substring(1);
//		if (after.length() > 2)
//			after = after.substring(0, 1).toUpperCase() + after.substring(1);
    char[] beforeArray = before.toCharArray();
    char[] afterArray = after.toCharArray();
    int iii = 0;
    for (; iii < beforeArray.length; iii++) {
      char characterBefore = ' ', characterAfter = ' ';
      if (iii < beforeArray.length)
        characterBefore = beforeArray[iii];
      else
        return 0;
      if (iii < afterArray.length)
        characterAfter = afterArray[iii];
      else
        return 0;
      if (characterBefore != characterAfter) return iii;
    }

    return iii;
  }
}
