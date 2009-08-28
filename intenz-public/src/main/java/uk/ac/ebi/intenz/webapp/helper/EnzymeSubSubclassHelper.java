package uk.ac.ebi.intenz.webapp.helper;

import uk.ac.ebi.intenz.webapp.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import java.util.List;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.1 $ $Date: 2008/02/25 14:03:41 $
 */
public class EnzymeSubSubclassHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeClassHelper.class);

  private EnzymeSubSubclassHelper() {
  }

  public static String descriptionToHTML(EnzymeSubSubclass enzymeSubSubclass, SpecialCharacters encoding, EncodingType encodingType) {
    return encoding.xml2Display(IntEnzUtilities.cleanInternalLinks(enzymeSubSubclass.getDescription(), true), encodingType);
  }

  public static String toHTML(EnzymeSubSubclass enzymeSubSubclass, SpecialCharacters encoding, EncodingType encodingType) {
    StringBuffer html = new StringBuffer();

    // Title
    html.append("<table width=\"100%\" valign=\"top\" border=\"0\">");
    html.append("<tr><td nowrap><nobr>&nbsp;<span class=\"pt_enzyme\">EC&nbsp;");
    html.append(enzymeSubSubclass.getEc().getEc1());
    html.append("</span>&nbsp;</nobr></td><td width = \"100%\"><span class=\"pt_enzyme\">");
    html.append(enzymeSubSubclass.getClassName());
    html.append("</nobr></td></tr><tr><td nowrap><nobr>&nbsp;<span class=\"pt_enzyme\">EC&nbsp;");
    html.append(enzymeSubSubclass.getEc().getEc1());
    html.append(".");
    html.append(enzymeSubSubclass.getEc().getEc2());
    html.append("</span>&nbsp;</nobr></td><td width = \"100%\"><span class=\"pt_enzyme\">");
    html.append(enzymeSubSubclass.getSubclassName());
    html.append("</nobr></td></tr><tr><td class = \"tablegreen\" nowrap><nobr>&nbsp;<span class=\"wt_enzyme\">EC&nbsp;");
    html.append(enzymeSubSubclass.getEc().toString());
    html.append("</span>&nbsp;</nobr></td><td width = \"100%\"><span class=\"pt_enzyme\">");
    html.append(enzymeSubSubclass.getName());
    html.append("</nobr></td></tr></table>");

    if (!enzymeSubSubclass.getDescription().equals("")) {
      // Dotted line and subtitle
      html.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" ");
      html.append("cellpadding=\"0\"><tr><td height=\"3\" background=\"http://www.ebi.ac.uk/services/images/hor.gif\">");
      html.append("<img src=\"http://www.ebi.ac.uk/services/images/trans.gif\" width=\"25\" height=\"3\"></td></tr></table>");
      html.append("<table valign=\"top\" border=\"0\" cellspacing=\"0\" cellpadding=\"4\"><tr><td width=\"100%\" >&nbsp;");
      html.append("</td><td class =\"tablebody\" nowrap><span  class =\" green\"><nobr>Abstract</nobr></span></td>");
      html.append("</tr></table>");

      // Description
      html.append("<table border=\"0\"><tr><td align=\"left\"><span class=\"pt_enzyme_black\">");
      html.append(EnzymeSubSubclassHelper.descriptionToHTML(enzymeSubSubclass, encoding, encodingType));
      html.append("</span></td> </tr> </table>");

      // Space
      html.append("<table align=\"left\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"> <tr> ");
      html.append("<td height=\"30\">&nbsp;</td> </tr></table>");
    }

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
    List entries = enzymeSubSubclass.getEntries();
    for (int iii = 0; iii < entries.size(); iii++) {
      EnzymeEntry enzymeEntry = (EnzymeEntry) entries.get(iii);

      html.append("<tr> <td valign=\"top\" align=\"left\"><span class=\"pt_enzyme\">");
      html.append(enzymeEntry.getEc().toString());
      html.append("</span></td> <td valign=\"top\" align=\"left\"><span class=\"pt_enzyme_black\">");

      HistoryEvent latestHistoryEvent = enzymeEntry.getHistory().getLatestHistoryEventOfRoot();
      if (latestHistoryEvent.getEventClass() == EventConstant.DELETION || latestHistoryEvent.getEventClass() == EventConstant.TRANSFER) {
        if (latestHistoryEvent.getEventClass() == EventConstant.DELETION)
          html.append("deleted");
        else {
          if (latestHistoryEvent.getNote() != null && !latestHistoryEvent.getNote().equals(""))
            html.append("<i>" + latestHistoryEvent.getNote() + "</i>");
        }
      } else {
        html.append(encoding.xml2Display(enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName()));
      }

      html.append("</span></td> </tr>");
    }
    html.append("</table>");

    return html.toString();
  }

}
