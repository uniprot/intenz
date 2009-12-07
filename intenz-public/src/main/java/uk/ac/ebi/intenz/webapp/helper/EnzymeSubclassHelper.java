package uk.ac.ebi.intenz.webapp.helper;

import uk.ac.ebi.intenz.webapp.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import java.util.List;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.1 $ $Date: 2008/02/25 14:03:41 $
 */
public class EnzymeSubclassHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeClassHelper.class);

  private EnzymeSubclassHelper() {
  }

  public static String descriptionToHTML(EnzymeSubclass enzymeSubclass, SpecialCharacters encoding, EncodingType encodingType) {
    if (enzymeSubclass.getDescription().equals(""))
      return "<i>No description available.</i>";
    return encoding.xml2Display(IntEnzUtilities.cleanInternalLinks(enzymeSubclass.getDescription(), true), encodingType);
  }

  public static String toHTML(EnzymeSubclass enzymeSubclass, SpecialCharacters encoding, EncodingType encodingType) {
    StringBuffer html = new StringBuffer();

    // Title
    html.append("<table valign=\"top\" border=\"0\">");
    html.append("<tr><td nowrap><nobr>&nbsp;<span class=\"pt_enzyme\">EC&nbsp;");
    html.append(enzymeSubclass.getEc().getEc1());
    html.append("</span>&nbsp;</nobr></td><td width = \"100%\"><span class=\"pt_enzyme\">");
    html.append(enzymeSubclass.getClassName());
    html.append("</nobr></td></tr><tr><td class = \"tablegreen\" nowrap><nobr>&nbsp;<span class=\"wt_enzyme\">EC&nbsp;");
    html.append(enzymeSubclass.getEc().toString());
    html.append("</span>&nbsp;</nobr></td><td width = \"100%\"><span class=\"pt_enzyme\">");
    html.append(enzymeSubclass.getName());
    html.append("</nobr></td></tr></table>");

    // Dotted line and subtitle
    html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" ");
    html.append("cellpadding=\"0\"><tr><td height=\"3\" background=\"http://www.ebi.ac.uk/services/images/hor.gif\">");
    html.append("<img src=\"http://www.ebi.ac.uk/services/images/trans.gif\" width=\"25\" height=\"3\"></td></tr></table>");
    html.append("<table valign=\"top\" border=\"0\" cellspacing=\"0\" cellpadding=\"4\"><tr><td width=\"100%\" >&nbsp;");
    html.append("</td><td class =\"tablebody\" nowrap><span  class =\" green\"><nobr>Abstract</nobr></span></td>");
    html.append("</tr></table>");

    // Description
    html.append("<table border=\"0\"><tr><td align=\"left\"><span class=\"pt_enzyme_black\">");
    html.append(EnzymeSubclassHelper.descriptionToHTML(enzymeSubclass, encoding, encodingType));
    html.append("</span></td> </tr> </table>");

    // Space
    html.append("<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> <tr> ");
    html.append("<td height=\"30\">&nbsp;</td> </tr></table>");

    // Dotted line and subtitle
    html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
    html.append("<tr> <td width=\"100%\" height=\"3\" background=\"http://www.ebi.ac.uk/Groups/images/hor.gif\">");
    html.append("<img src=\"http://www.ebi.ac.uk/Groups/images/trans.gif\" width=\"25\" height=\"3\"></td>");
    html.append("</tr> </table>");

    // Contents title
    html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"4\"> <tr> <td width=\"100%\" >&nbsp;</td>");
    html.append("<td class =\" tablebody\" nowrap><span  class =\" green\"><nobr>Contents</nobr></span></td> </tr> </table>");

    // Contents
    html.append("<table align=\"left\" border=\"0\">");
    List subSubclasses = enzymeSubclass.getSubSubclasses();
    for (int iii = 0; iii < subSubclasses.size(); iii++) {
      EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) subSubclasses.get(iii);

      html.append("<tr> <td valign=\"top\" align=\"left\"><span class=\"pt_enzyme\">");
      html.append(enzymeSubSubclass.getEc().toString());
      html.append("</span></td> <td valign=\"top\" align=\"left\"><span class=\"pt_enzyme_black\">");
      html.append(encoding.xml2Display(enzymeSubSubclass.getName()));
      html.append("</span></td> </tr>");
    }
    html.append("</table>");

    return html.toString();
  }

}
