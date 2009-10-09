package uk.ac.ebi.intenz.webapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility classes, which are used throughout the whole project.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class IntEnzUtilities {

  /**
   * Tries to link all EC numbers and checks whether the first letter is an uppercase letter.
   *
   * @param ecString The String to be formatted.
   * @param uppercaseFirstLetter whether to uppercase the first letter or not
   * @return the formatted history comment.
   * @deprecated use {@link #linkEcNumbers(String)} instead and - if needed -
   * 	{@link #firstLetterToUppercase(String)}
   */
  public static String linkEcNumber(String ecString, boolean uppercaseFirstLetter){
	  String result = linkEcNumbers(ecString);
	  return uppercaseFirstLetter? firstLetterToUppercase(result) : result;
  }
  
  public static String linkEcNumbers(String ecString){
	  StringBuffer linkedECString = new StringBuffer();
	  Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)|(?<=EC )(\\d+(?:\\.\\d+){1,2})");
	  Matcher m = p.matcher(ecString);
	  while (m.find()) {
		  String groupFound = (m.group(1) != null)? m.group(1) : m.group(2);
		  m.appendReplacement(linkedECString, "<a href=\"query?cmd=SearchEC&ec="
				  + groupFound + "\">" + groupFound + "</a>");
	  }
	  m.appendTail(linkedECString);
	  return linkedECString.toString().equals("")? ecString : linkedECString.toString();
  }

  /**
   * Generates links to IUPAC carbohydrate nomenclature pages
   * wherever it finds a <code>2-Carb-</code> prefixed word.
   * @param s
   * @return
   */
  public static String link2carb(String s){
	  StringBuffer linked2carb = new StringBuffer();
	  Pattern p = Pattern.compile("(2-Carb-)(\\d+)(\\.(\\d))?");
	  Matcher m = p.matcher(s);
	  while (m.find()){
		  String l1 = m.group(2);
		  // Pad with zero for single digits:
		  if (l1.length() == 1) l1 = "0" + l1;
		  String l2 = m.group(4);

		  StringBuffer link = new StringBuffer("<a target=\"_blank\" class=\"externalLink\" href=\"http://www.chem.qmul.ac.uk/iupac/2carb/");
		  link.append(l1);
		  // Bloody IUPAC website exceptions:
		  if (l1.equals("06")) link.append("n07");
		  link.append(".html");
		  if (l2 != null) link.append('#').append(l1).append(l2);
		  link.append("\">").append(m.group(1)).append(m.group(2));
		  if (l2 != null) link.append(m.group(3));
		  link.append("</a>");

		  m.appendReplacement(linked2carb, link.toString());
	  }
	  m.appendTail(linked2carb);
	  return linked2carb.toString();
  }

  /**
   * Generates a hypertext link to MEROPS.
   * It tries to guess that the text is about peptidase families, and then
   * links are generated after the words <code>family</code> or <code>families</code>
   * if the following word looks like a MEROPS identifier.
   * @param s
   * @return
   */
  public static String linkMerops(String s){
	  if (!s.matches(".*peptidase.*") || !s.matches(".*\\sfamil(y|ies)\\s.*")) return s;
	  StringBuffer sb = new StringBuffer();
	  Pattern p = Pattern.compile("(?<=\\sfamily\\s)(\\p{Upper}\\d{1,2}\\p{Upper}?)(?=[\\s\\.])");
	  Matcher m = p.matcher(s);
	  while (m.find()){
		  m.appendReplacement(sb,
				  "<a class=\"externalLink\" target=\"_blank\"" +
				  " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id="
				  + m.group(1) + "\">" + m.group(1) + "</a>");
	  }
	  m.appendTail(sb);
	  String tmp = sb.toString();
	  sb.delete(0, sb.length());
	  p = Pattern.compile("(?<=\\speptidase\\sfamilies\\s)(\\p{Upper}\\d{1,2}\\p{Upper}?)" +
	  		"\\sand\\s(\\p{Upper}\\d{1,2}\\p{Upper}?)");
	  m = p.matcher(tmp);
	  while (m.find()){
		  m.appendReplacement(sb, "<a class=\"externalLink\" target=\"_blank\"" +
				  " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id="
				  + m.group(1) + "\">" + m.group(1) + "</a> and "
				  + "<a class=\"externalLink\" target=\"_blank\""
				  + " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id="
				  + m.group(2) + "\">" + m.group(2) + "</a>");
	  }
	  m.appendTail(sb);
	  tmp = sb.toString();
	  sb.delete(0, sb.length());
	  p = Pattern.compile("(?<=\\speptidase\\sfamilies\\s)(\\p{Upper}\\d{1,2}\\p{Upper}?)," +
	  		"\\s(\\p{Upper}\\d{1,2}\\p{Upper}?)\\sand\\s(\\p{Upper}\\d{1,2}\\p{Upper}?)");
	  m = p.matcher(tmp);
	  while (m.find()){
		  m.appendReplacement(sb, "<a class=\"externalLink\" target=\"_blank\"" +
				  " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=" +
				  m.group(1) + "\">" + m.group(1) + "</a>, " +
				  "<a class=\"externalLink\" target=\"_blank\"" +
				  " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=" +
				  m.group(2) + "\">" + m.group(2) + "</a> and " +
				  "<a class=\"externalLink\" target=\"_blank\"" +
				  " href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=" +
				  m.group(3) + "\">" + m.group(3) + "</a>");
	  }
	  m.appendTail(sb);
	  return sb.toString();
  }

  /**
   * Cleans the string of links around EC numbers (if any), and then creates the proper IntEnz links.
   * @param string2Check
   * @param uppercaseFirstLetter whether to uppercase the first letter or not
   * @return A String with EC numbers wrapped in HTML links to IntEnz pages.
   * @deprecated use {@link #cleanLinks(String)} and {@link #firstLetterToUppercase(String)}
   */
  public static String cleanInternalLinks(String string2check, boolean uppercaseFirstLetter){
	  // First, remove any links around EC numbers (shouldn't be there, BTW):
	  String cleanString = string2check.replaceAll("\\<a href.+?\\>(EC\\s)?(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s?</a>", "$1$2");
	  cleanString = link2carb(cleanString);
	  cleanString = linkEcNumber(cleanString, uppercaseFirstLetter);
	  cleanString = linkMerops(cleanString);
	  return cleanString;
  }
  
  public static String cleanLinks(String s){
	  String cleanString = s.replaceAll("\\<a href.+?\\>(EC\\s)?(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s?</a>", "$1$2");
	  cleanString = link2carb(cleanString);
	  cleanString = linkEcNumbers(cleanString);
	  cleanString = linkMerops(cleanString);
	  return cleanString;
  }

  /**
   * Replaces the first letter with the uppercase letter.
   *
   * @param text to be checked.
   * @return formatted string.
   * @deprecated use JSTL's fn:toUpperCase and fn:substring instead
   */
  public static String firstLetterToUppercase(String text) {
    StringBuffer s = new StringBuffer(text);
    String firstLetter = s.substring(0, 1);
    s.replace(0, 1, firstLetter.toUpperCase());
    return s.toString();
  }
  
  /**
   * Create hyperlinks for all cross-references (xrefs) in a flat file.
   *
   * @param flatFile The flat file string.
   * @return the hyperlinked flat file string.
   * @throws NullPointerException if <code>flatFile</code> is <code>null</code>.
   */
  public static String getLinkedFlatFile(String flatFile) {
    if (flatFile == null)
    	throw new NullPointerException("Parameter 'flatFile' must not be null.");
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
