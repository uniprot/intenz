package uk.ac.ebi.intenz.tools.release.helper;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.1 $ $Date: 2008/02/21 12:44:56 $
 */
public class EnzymeClassHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeEntryHelper.class);

  private EnzymeClassHelper() {}

  public static String toXML(EnzymeClass enzymeClass, SpecialCharacters encoding) {
    StringBuffer xmlStringBuffer = new StringBuffer();

    xmlStringBuffer.append("<enzyme>");

    xmlStringBuffer.append("<ec>");
    xmlStringBuffer.append("EC " + enzymeClass.getEc().toString());
    xmlStringBuffer.append("</ec>");

    xmlStringBuffer.append("<deleted_text>");
    xmlStringBuffer.append("</deleted_text>");

    xmlStringBuffer.append("<common_name>");
    if (!enzymeClass.getName().equals("")) {
      xmlStringBuffer.append(encoding.xml2Display(removeFormatting(enzymeClass.getName())));
    }
    xmlStringBuffer.append("</common_name>");

    if (!enzymeClass.getName().equals("")) {
      xmlStringBuffer.append("<common_name>");
      xmlStringBuffer.append(encoding.xml2Display(removeFormatting(enzymeClass.getName()), EncodingType.SWISSPROT_CODE));
      xmlStringBuffer.append("</common_name>");
    }
    xmlStringBuffer.append("<description>");
    if (!enzymeClass.getDescription().equals("")) {
      xmlStringBuffer.append(encoding.xml2Display(removeFormatting(enzymeClass.getDescription())));
    }
    xmlStringBuffer.append("</description>");

    if (!enzymeClass.getDescription().equals("")) {
      xmlStringBuffer.append("<description>");
      xmlStringBuffer.append(encoding.xml2Display(removeFormatting(enzymeClass.getDescription()), EncodingType.SWISSPROT_CODE));
      xmlStringBuffer.append("</description>");
    }

    xmlStringBuffer.append("<reactions>");
    xmlStringBuffer.append("</reactions>");

    xmlStringBuffer.append("<syst_name>");
    xmlStringBuffer.append("</syst_name>");

    xmlStringBuffer.append("<synonyms>");
    xmlStringBuffer.append("</synonyms>");

    xmlStringBuffer.append("<comments>");
    xmlStringBuffer.append("</comments>");

    xmlStringBuffer.append("<links>");
    xmlStringBuffer.append("</links>");

    xmlStringBuffer.append("<references>");
    xmlStringBuffer.append("<reference>");
    xmlStringBuffer.append("<reference>");
    xmlStringBuffer.append("<number>");
    xmlStringBuffer.append("</number>");
    xmlStringBuffer.append("<authors>");
    xmlStringBuffer.append("</auhors>");
    xmlStringBuffer.append("<title>");
    xmlStringBuffer.append("</title>");
    xmlStringBuffer.append("<year>");
    xmlStringBuffer.append("</year>");
    xmlStringBuffer.append("<issue>");
    xmlStringBuffer.append("</issue>");
    xmlStringBuffer.append("<patent_no>");
    xmlStringBuffer.append("</patent_no>");
    xmlStringBuffer.append("<first_page>");
    xmlStringBuffer.append("</first_page>");
    xmlStringBuffer.append("<last_page>");
    xmlStringBuffer.append("</last_page>");
    xmlStringBuffer.append("<edition>");
    xmlStringBuffer.append("</edition>");
    xmlStringBuffer.append("<editor>");
    xmlStringBuffer.append("</editor>");
    xmlStringBuffer.append("<volume>");
    xmlStringBuffer.append("</volume>");
    xmlStringBuffer.append("<pub_place>");
    xmlStringBuffer.append("</pub_place>");
    xmlStringBuffer.append("<pub_company>");
    xmlStringBuffer.append("</pub_company>");
    xmlStringBuffer.append("<pub_med>");
    xmlStringBuffer.append("</pub_med>");
    xmlStringBuffer.append("<medline>");
    xmlStringBuffer.append("</medline>");
    xmlStringBuffer.append("</reference>");
    xmlStringBuffer.append("</reference>");
    xmlStringBuffer.append("</references>");

    xmlStringBuffer.append("<history>");
    xmlStringBuffer.append("</history>");

    xmlStringBuffer.append("</enzyme>");

    return xmlStringBuffer.toString();
  }

  private static String removeFormatting(String text) {
    text = text.replaceAll("\\<small\\>", "");
    text = text.replaceAll("\\<\\/small\\>", "");
    text = text.replaceAll("\\<b\\>", "");
    text = text.replaceAll("\\<\\/b\\>", "");
    text = text.replaceAll("\\<i\\>", "");
    text = text.replaceAll("\\<\\/i\\>", "");
    return text;
  }
}
