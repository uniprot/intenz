package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * This class provides methods to switch between a pure Unicode based special character
 * representation and a browser dependent non-pure Unicode encoding.
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class ChangeCharacterRepresentationCommand extends NonDatabaseCommand {

  /**
   * Depending on the given parameter this method switches between a Unicode representation for some
   * <i>very</i> special characters (scissile, activated, ...) and a browser-dependent encoding.
   * <p/>
   * This class makes use of the Special Characters API, which allows to define a representation for
   * special characters in different ways. See documentation (TODO: Insert link here).
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    boolean unicodeOnly = request.getParameter("uo").equals("true");

//    SpecialCharacters specialCharacters = (SpecialCharacters) request.getSession().getServletContext().getAttribute(
//            "characters");
//    SpecialCharacters specialCharactersCopy = SpecialCharacters.copy(specialCharacters);

//    if (unicodeOnly) { // Use default setting (which is mainly Unicode).
//      specialCharactersCopy.setMappingIdentifier(null);
//      request.getSession().setAttribute("uo", "true");
//    } else {
//      specialCharactersCopy.setMappingIdentifier("defaultAlt");
//      request.getSession().setAttribute("uo", "false");
//    }

    request.setAttribute("info", getInfo(unicodeOnly));
    forward("/info.jsp");
  }

  /**
   * Returns the content for the info page.
   *
   * @param unicodeOnly Use default encoding if set to <code>true</code>.
   * @return the HTML info.
   */
  private String getInfo(boolean unicodeOnly) {
    StringBuffer info = new StringBuffer();

    info.append("<form action=\"query?cmd=ChangeCharacterRepresentation\" method=\"post\">");
    info.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
    info.append("  <tr>\n");

    if (!unicodeOnly) {
      info.append("    <td>You have switched to individual encoding (browser-dependent and font-independent) of " +
                  "special characters. Some characters may now be displayed as images, depending on the browser version " +
                  "you use. If you want to use the default encoding again, press the button below this text.</td></tr>\n");
      info.append("<tr><td>&nbsp;</td></tr>\n");
      info.append("      <tr><td align=\"center\"><input type=\"submit\" value=\"Switch to default encoding\">\n" +
                  "      <input type=\"hidden\" name=\"uo\" value=\"true\"></td>\n");
    } else {
      info.append("    <td>You have switched to the default encoding for special characters. The default encoding uses " +
                  "Unicode to display special characters and works best with current browser versions. Please check if you " +
                  "have a Unicode font installed and if you can see all " +
                  "<a href=\"javascript:windowOpenWithSize('encoding.html', 280, 400)\">special characters</a> used within " +
                  "IntEnz. If you cannot see all characters or if you are not happy with the representation then press the " +
                  "button below to enable individual encoding (browser-dependent and font-independent).</td></tr>\n");
      info.append("<tr><td>&nbsp;</td></tr>\n");
      info.append("      <tr><td align=\"center\"><input type=\"submit\" value=\"Switch to individual encoding\">\n" +
                  "      <input type=\"hidden\" name=\"uo\" value=\"false\"></td>\n");
    }

    info.append("  </tr>");
    info.append("</table>");
    info.append("</form>");
    return info.toString();
  }

}
