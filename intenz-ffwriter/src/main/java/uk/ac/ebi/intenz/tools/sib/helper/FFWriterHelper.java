package uk.ac.ebi.intenz.tools.sib.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class currently contains only one method to create hyperlinks for all cross-references (xrefs) in a flat file.
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2009/04/15 09:09:34 $
 * @deprecated this is a view-tier task, use the IntEnzUtilities class instead (public webapp)
 */
public class FFWriterHelper {

//  private static final Logger LOGGER = Logger.getLogger(FFWriterHelper.class);

  /**
   * Create hyperlinks for all cross-references (xrefs) in a flat file.
   *
   * @param flatFile The flat file string.
   * @return the hyperlinked flat file string.
   * @throws NullPointerException if <code>flatFile</code> is <code>null</code>.
   */
  public static String createXrefHyperlinks(String flatFile) {
    if (flatFile == null) throw new NullPointerException("Parameter 'flatFile' must not be null.");
    String linkedFlatFile = flatFile.replaceAll("PR   PROSITE\\; (.+?)\\;",
                                                "PR   PROSITE; <a class=\"pre_anchor\" href=\"http://www.expasy.org/prosite/$1\" target=\"_blank\">$1</a>;");
    linkedFlatFile = linkedFlatFile.replaceAll("DI   (.+?)MIM\\:(\\d+)\\.",
                                               "DI   $1<a class=\"pre_anchor\" href=\"http://www.ncbi.nlm.nih.gov/entrez/dispomim.cgi?id=$2\" target=\"_blank\">MIM:$2</a>.");

    Pattern DRLinePattern = Pattern.compile("DR   (.+?)\n");
    Matcher DRLinePatternMatcher = DRLinePattern.matcher(linkedFlatFile);
    StringBuffer newLinkedFlatFile = new StringBuffer();
    while (DRLinePatternMatcher.find()) {
      String DRLineContents = DRLinePatternMatcher.group(1);
      DRLineContents = DRLineContents.replaceAll("(\\w+?)\\,(\\s+?)(.+?)\\;",
                                                 "<a class=\"pre_anchor\" href=\"http://www.uniprot.org/uniprot/$1\" target=\"_blank\">$1</a>,$2$3;");
      DRLinePatternMatcher.appendReplacement(newLinkedFlatFile, "DR   " + DRLineContents + "\n");
    }
    newLinkedFlatFile = DRLinePatternMatcher.appendTail(newLinkedFlatFile);

    return newLinkedFlatFile.toString();
  }

}
