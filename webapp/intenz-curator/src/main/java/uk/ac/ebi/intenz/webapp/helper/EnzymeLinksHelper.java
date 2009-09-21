package uk.ac.ebi.intenz.webapp.helper;

import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;

import java.util.*;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 * @deprecated this class is an aberration. Please, use HTML somewhere else.
 */
public class EnzymeLinksHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeLinksHelper.class);

  public static String renderLinks(List links) {
    return renderLinks(links, true);
  }

  /**
   * Renders links, adding some automatically generated ones.
   * @param links
   * @param ec
   * @return
   */
  public static String renderLinks(List links, String ec){
	  List completeLinks = new ArrayList(links);
      addAutoLinks(completeLinks, ec);
	  return renderLinks(completeLinks, true);
  }

  public static String renderLinks(List links, boolean displayViewImgs){
      Set sortedLinks = new TreeSet(links);
      StringBuffer html = new StringBuffer();
      Set databaseNames = new HashSet();
      for (Iterator it = sortedLinks.iterator(); it.hasNext();) {
        EnzymeLinkDTO link = (EnzymeLinkDTO) it.next();
        if (link.getDatabaseName().equals(XrefDatabaseConstant.SWISSPROT.getDisplayName())) continue; // Skip UniProt links for now.
        html.append(getLinkHTMLTableRow(link, databaseNames.contains(link.getDatabaseName()), displayViewImgs));
        databaseNames.add(link.getDatabaseName());
      }

      html.append(renderUniprotLinks(links, true));

      return html.toString();
  }

  private static EnzymeLinkDTO getDto(EnzymeLink link, String ec) {
	EnzymeLinkDTO dto = new EnzymeLinkDTO();
	dto.setAccession(link.getAccession());
	dto.setName(link.getName());
	dto.setDatabaseName(link.getXrefDatabaseConstant().getDisplayName());
	dto.setDatabaseCode(link.getXrefDatabaseConstant().getDatabaseCode());
	dto.setUrl(link.getFullUrl(ec));
	return dto;
}

  /**
   * Adds automatic links which should appear in IUBMB view.
   * @param links
   * @param ec
   */
  private static void addIubmbAutoLinks(List links, String ec){
	  links.add(getDto(EnzymeLink.PDB, ec));
	  links.add(getDto(EnzymeLink.EXPASY, ec));
	  links.add(getDto(EnzymeLink.BRENDA, ec));
	  links.add(getDto(EnzymeLink.KEGG, ec));
  }

  /**
   * Adds all the automatic links (IntEnz view)
   * @param links
   * @param ec
   */
  private static void addAutoLinks(List links, String ec){
	  addIubmbAutoLinks(links, ec);
	  links.add(getDto(EnzymeLink.CSA, ec));
	  links.add(getDto(EnzymeLink.NC_IUBMB, ec));
  }

  public static String renderIubmbLinks(List links, String ec){
	  List completeLinks = new ArrayList(links);
	  addIubmbAutoLinks(completeLinks, ec);
	  return renderIubmbLinks(completeLinks);
  }

public static String renderIubmbLinks(List links) {
    Set sortedLinks = new TreeSet(links);
    StringBuffer html = new StringBuffer();
    int iii = 0;
    List casNumbers = new ArrayList();
    for (Iterator it = sortedLinks.iterator(); it.hasNext(); iii++) {
      EnzymeLinkDTO link = (EnzymeLinkDTO) it.next();
      if (link.getDatabaseName().equals(XrefDatabaseConstant.CAS.getDatabaseCode())) {
        if (!link.getAccession().equals("")) casNumbers.add(link);
        continue;
      }
      if (html.length() > 0) html.append(", ");
      html.append("<a href=\"");
      html.append(link.getUrl());
      html.append("\" target=\"_blank\"");
      // Hovers:
      if (link.getDatabaseCode().equals(XrefDatabaseConstant.DIAGRAM.getDatabaseCode())
    		  || link.getDatabaseCode().equals(XrefDatabaseConstant.MIM.getDatabaseCode())){
    	  html.append(" title=\"").append(link.getName()).append('"');
      } else if (link.getDatabaseCode().equals(XrefDatabaseConstant.PROSITE.getDatabaseCode())){
    	  html.append(" title=\"PROSITE documentation ").append(link.getAccession()).append('"');
      }
      html.append(">");
      // Link text:
      if (link.getDatabaseCode().equals(XrefDatabaseConstant.GO.getDatabaseCode())){
    	  html.append(link.getAccession());
      } else if (link.getDatabaseCode().equals(XrefDatabaseConstant.PROSITE.getDatabaseCode())){
    	  html.append("PROSITE:").append(link.getAccession());
      } else {
    	  html.append(link.getDatabaseName());
      }
      html.append("</a>");
    }
    if (casNumbers.size() > 0){
      html.append("<br/><br/>\nCAS Registry Number");
      if (casNumbers.size() > 1) html.append("s");
      html.append(": ");
      for (int i = 0; i < casNumbers.size(); i++){
          EnzymeLinkDTO cas = (EnzymeLinkDTO) casNumbers.get(i);
          if (i > 0) html.append(", ");
          html.append(cas.getAccession());
          String comment = cas.getDataComment().getDisplayComment();
          if (!comment.trim().equals("")){
              html.append(" (").append(comment).append(")");
          }
      }
    }
    return html.toString();
  }

  public static String renderUniprotLinks(List links, boolean displayViewImages) {
    Set uniprotLinks = getUniprotLinks(links);
    if (uniprotLinks == null) throw new NullPointerException("Parameter 'uniprotLinks' must not be null.");
    StringBuffer html = new StringBuffer();
    if (uniprotLinks.size() > 0) {
      html.append("<tr>\n");
      html.append("<td valign=\"top\" nowrap=\"nowrap\" colspan=\"2\">\n");

      html.append("<table border=\"0\"><tr><td valign=\"top\" nowrap=\"nowrap\" width=\"130\">");
      if (uniprotLinks.size() > 9) {
        html.append("<b>UniProtKB/Swiss-Prot</b> (").append(uniprotLinks.size()).append("):<br/>");
        html.append(" <span class=\"link_url\">[<a class=\"link\" id=\"sp_more\" href=\"javascript:fold('sp', 'sp_more', 'show', 'hide');\">show</a>]</span>\n");
      } else {
        html.append("<b>UniProtKB/Swiss-Prot</b>:");
      }
      html.append("</td>\n");
      html.append("<td valign=\"top\" align=\"left\" nowrap=\"nowrap\" width=\"100%\">\n");
      if (uniprotLinks.size() > 9) {
        html.append("&nbsp;");
        html.append("<div id=\"sp\" style=\"display:none\">\n");
      }
      html.append("<table border=\"0\" celltabbing=\"3\">\n");
      int count = 0;
      for (Iterator it = uniprotLinks.iterator(); it.hasNext();) {
        EnzymeLinkDTO uniprotLink = (EnzymeLinkDTO) it.next();
        if (count % 2 > 0 || count == 0) {
          if (count == 0) html.append("<tr>\n");
          html.append("<td valign=\"top\"><a href=\"");
          html.append(uniprotLink.getUrl());
          html.append("\">");
          html.append(uniprotLink.getAccession());
          html.append("</a></td>\n");
          html.append("<td valign=\"top\">\n");
          html.append(uniprotLink.getName());
          html.append("</td>\n");
          html.append("<td>&nbsp;</td>\n");
          count++;
        } else {
          html.append("<td valign=\"top\"><a href=\"");
          html.append(uniprotLink.getUrl());
          html.append("\">");
          html.append(uniprotLink.getAccession());
          html.append("</a></td>\n");
          html.append("<td valign=\"top\">\n");
          html.append(uniprotLink.getName());
          html.append("</td>\n");
          html.append("</tr>\n");
          count = 0;
        }
      }
      html.append("</table>\n");
      if (uniprotLinks.size() > 9) html.append("</div>\n");

      html.append("</td></tr></table></td>\n");

      html.append("</td>\n");
      html.append("<td width=\"100px\" align=\"center\" valign=\"top\">n/a</td>\n");
      if (displayViewImages)
        html.append("<td width=\"100px\" align=\"center\" valign=\"top\"><img src=\"images/blue_bullet.gif\"/><img src=\"images/red_bullet.gif\"/></td>\n");
      else
        html.append("<td width=\"100px\" align=\"center\" valign=\"top\">ENZYME & IntEnz</td>\n");
      html.append("</tr>\n");
    }

    return html.toString();
  }

  public static String renderEditableLink(EnzymeLinkDTO link, int index) {
    StringBuffer html = new StringBuffer();
    html.append("<tr>");

    // Predefined links are disabled.
    if (link.getDatabaseName().equals(XrefDatabaseConstant.KEGG.getDisplayName()) ||
//        link.getDatabaseName().equals(XrefDatabaseConstant.GO.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.ERGO.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.NIST74.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.UMBBD.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.PDB.getDisplayName())) {
      // The URL text field is disabled for predefined links.
      html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
      html.append(renderURLTextField(index, link.getUrl(), true));
      html.append("</tr>\n<tr>");
      html.append(renderAccessionTextField(index, link.getAccession(), true));
      html.append(renderNameTextField(index, link.getName(), true));
    } else {
      if (link.getDatabaseName().equals(XrefDatabaseConstant.BRENDA.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.METACYC.getDisplayName()) ||
        link.getDatabaseName().equals(XrefDatabaseConstant.CAS.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
        html.append(renderURLTextField(index, link.getUrl(), true));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), false));
        html.append(renderNameTextField(index, link.getName(), true));
      }
      if (link.getDatabaseName().equals(XrefDatabaseConstant.MEROPS.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
        html.append(renderURLTextField(index, link.getUrl(), false));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), true));
        html.append(renderNameTextField(index, link.getName(), true));
      }
      if (link.getDatabaseName().equals(XrefDatabaseConstant.DIAGRAM.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
        html.append(renderURLTextField(index, link.getUrl(), false));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), true));
        html.append(renderNameTextField(index, link.getName(), false));
      }
      if (link.getDatabaseName().equals(XrefDatabaseConstant.MIM.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
        html.append(renderURLTextField(index, link.getUrl(), true));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), false));
        html.append(renderNameTextField(index, link.getName(), false));
      }
      if (link.getDatabaseName().equals(XrefDatabaseConstant.PROSITE.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), true));
        html.append(renderURLTextField(index, link.getUrl(), true));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), false));
        html.append(renderNameTextField(index, link.getName(), true));
      }
      if (link.getDatabaseName().equals(XrefDatabaseConstant.UNDEF.getDisplayName())) {
        html.append(renderDatabaseNameComboBox(index, link.getDatabaseName(), false));
        html.append(renderURLTextField(index, link.getUrl(), true));
        html.append("</tr>\n<tr>");
        html.append(renderAccessionTextField(index, link.getAccession(), true));
        html.append(renderNameTextField(index, link.getName(), true));
      }
    }
    html.append("</tr>");

    return html.toString();
  }

  private static String renderDatabaseNameComboBox(int iii, String databaseName, boolean disabled) {
    StringBuffer html = new StringBuffer();
    if (disabled)
      html.append("<td><span class=\"link_label\">Database name:</span><br/>\n<select disabled=\"disabled\" name=\"link[");
    else
      html.append("<td><span class=\"link_label\">Database name:</span><br/>\n<select name=\"link[");
    html.append(iii);
    html.append("].databaseName\" size=\"1\" ");
    if (!disabled) {
      html.append("onchange=\"setLinkFields(");
      html.append("document.enzymeDTO.elements['link[");
      html.append(iii);
      html.append("].databaseName'].options[document.enzymeDTO.elements['link[");
      html.append(iii);
      html.append("].databaseName'].selectedIndex].value");
      html.append(", 'link[");
      html.append(iii);
      html.append("].url'");
      html.append(", 'link[");
      html.append(iii);
      html.append("].accession'");
      html.append(", 'link[");
      html.append(iii);
      html.append("].name'");
      html.append(")\">\n");
    }
    html.append(">\n");

    // UNDEF
    if (databaseName.equals(XrefDatabaseConstant.UNDEF.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.UNDEF.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.UNDEF.getDisplayName());
    html.append("</option>\n");

    // BRENDA
    if (databaseName.equals(XrefDatabaseConstant.BRENDA.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.BRENDA.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.BRENDA.getDisplayName());
    html.append("</option>\n");
    
    html.append("<option")
    	.append(databaseName.equals(XrefDatabaseConstant.METACYC.getDisplayName())?
    			"\" selected=\"selected\"" : "")
    	.append(" value=\"")
		.append(XrefDatabaseConstant.METACYC.getDisplayName())
		.append("\">")
		.append(XrefDatabaseConstant.METACYC.getDisplayName())
		.append("</option>\n");

    // GO
//    if (databaseName.equals(XrefDatabaseConstant.GO.getDisplayName()))
//      html.append("  <option selected=\"selected\" value=\"");
//    else
//      html.append("  <option value=\"");
//    html.append(XrefDatabaseConstant.GO.getDisplayName());
//    html.append("\">");
//    html.append(XrefDatabaseConstant.GO.getDisplayName());
//    html.append("</option>\n");

    // NIST74
    if (databaseName.equals(XrefDatabaseConstant.NIST74.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.NIST74.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.NIST74.getDisplayName());
    html.append("</option>\n");

    // KEGG
    if (databaseName.equals(XrefDatabaseConstant.KEGG.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.KEGG.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.KEGG.getDisplayName());
    html.append("</option>\n");

    // ERGO
    if (databaseName.equals(XrefDatabaseConstant.ERGO.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.ERGO.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.ERGO.getDisplayName());
    html.append("</option>\n");

    // CAS
    if (databaseName.equals(XrefDatabaseConstant.CAS.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.CAS.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.CAS.getDisplayName());
    html.append("</option>\n");

    // MEROPS
    if (databaseName.equals(XrefDatabaseConstant.MEROPS.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.MEROPS.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.MEROPS.getDisplayName());
    html.append("</option>\n");

    // UMBBD
    if (databaseName.equals(XrefDatabaseConstant.UMBBD.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.UMBBD.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.UMBBD.getDisplayName());
    html.append("</option>\n");

    // DIAGRAM
    if (databaseName.equals(XrefDatabaseConstant.DIAGRAM.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.DIAGRAM.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.DIAGRAM.getDisplayName());
    html.append("</option>\n");

    // PROSITE
    if (databaseName.equals(XrefDatabaseConstant.PROSITE.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.PROSITE.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.PROSITE.getDisplayName());
    html.append("</option>\n");

    // OMIM
    if (databaseName.equals(XrefDatabaseConstant.MIM.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.MIM.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.MIM.getDisplayName());
    html.append("</option>\n");

    // PDB
    if (databaseName.equals(XrefDatabaseConstant.PDB.getDisplayName()))
      html.append("  <option selected=\"selected\" value=\"");
    else
      html.append("  <option value=\"");
    html.append(XrefDatabaseConstant.PDB.getDisplayName());
    html.append("\">");
    html.append(XrefDatabaseConstant.PDB.getDisplayName());
    html.append("</option>\n");

    html.append("</select>\n</td>\n");

    return html.toString();
  }

  private static String renderURLTextField(int iii, String url, boolean disabled) {
    StringBuffer html = new StringBuffer();

    if (disabled)
      html.append("<td><span class=\"link_label\">URL:</span><br/><input disabled=\"disabled\" type=\"text\" name=\"link[");
    else
      html.append("<td><span class=\"link_label\">URL:</span><br/><input type=\"text\" name=\"link[");
    html.append(iii);
    html.append("].url\" size=\"70\" maxlength=\"100\" value=\"");
    html.append(url);
    html.append("\"></td>");

    return html.toString();
  }

  private static String renderAccessionTextField(int iii, String accession, boolean disabled) {
    StringBuffer html = new StringBuffer();

    if (disabled)
      html.append("<td><span class=\"link_label\">Accession:</span><br/><input disabled=\"disabled\" type=\"text\" name=\"link[");
    else
      html.append("<td><span class=\"link_label\">Accession:</span><br/><input type=\"text\" name=\"link[");
    html.append(iii);
    html.append("].accession\" size=\"15\" maxlength=\"500\" value=\"");
    html.append(accession);
    html.append("\"></td>");

    return html.toString();
  }

  private static String renderNameTextField(int iii, String name, boolean disabled) {
    StringBuffer html = new StringBuffer();

    if (disabled)
      html.append("<td><span class=\"link_label\">Name:</span><br/><input disabled=\"disabled\" type=\"text\" name=\"link[");
    else
      html.append("<td><span class=\"link_label\">Name:</span><br/><input type=\"text\" name=\"link[");
    html.append(iii);
    html.append("].name\" size=\"70\" maxlength=\"100\" value=\"");
    html.append(name);
    html.append("\"></td>");

    return html.toString();
  }

  private static String getLinkHTMLTableRow(EnzymeLinkDTO link, boolean isNotFirstLink){
      return getLinkHTMLTableRow(link, isNotFirstLink, true);
  }

  private static String getLinkHTMLTableRow(EnzymeLinkDTO link, boolean isNotFirstLink, boolean showViewImgs) {
    StringBuffer html = new StringBuffer();
    html.append("<tr>\n");
    html.append("<td nowrap=\"nowrap\" colspan=\"2\">\n");

    html.append("<table border=\"0\"><tr><td nowrap=\"nowrap\" width=\"80px\">");

    if (isNotFirstLink) {
      html.append("&nbsp;");
    } else {
      html.append("<b>");
      html.append(link.getDatabaseName());
      html.append(":</b>\n");
    }
    html.append("</td>\n");
    html.append("<td class=\"link_url\" nowrap=\"nowrap\">(");
    if (link.getDatabaseName().equals("BRENDA") || link.getDatabaseName().equals("KEGG") ||
        link.getDatabaseName().equals("GO") || link.getDatabaseCode().equals("EXPASY") ||
        link.getDatabaseName().equals("ERGO") || link.getDatabaseName().equals("NIST 74") ||
        link.getDatabaseName().equals("UM-BBD") || link.getDatabaseCode().equals("PDB") ||
        link.getDatabaseName().equals("PROSITE") || link.getDatabaseCode().equals("CSA") ||
        link.getDatabaseCode().equals("NCIUBMB")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getUrl() + "\">");
      html.append(link.getUrl());
      html.append("</a>");
    }

      if (link.getDatabaseName().equals("CAS") && !link.getAccession().equals("")){
        html.append(link.getAccession());
      }

    if (link.getDatabaseName().equals("OMIM")) {
      html.append(link.getName());
      html.append(" MIM:");
      html.append(link.getAccession());
    }

    if (link.getDatabaseName().equals("DIAGRAM")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getUrl() + "\">");
      html.append(link.getName());
      html.append("</a>");
    }

    if (link.getDatabaseName().equals("MEROPS")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getUrl() + "\">");
      html.append(link.getUrl());
      html.append("</a>");
    }
    html.append(")");
    if (link.getDataComment() != null &&
        !link.getDataComment().getDisplayComment().equals("")){
        html.append(" [").append(link.getDataComment().getDisplayComment()).append("]");
    }
    html.append("</td>\n");

    html.append("</td></tr></table></td>\n");

    html.append("<td width=\"100px\" align=\"center\">");
    html.append(link.getSourceDisplay());
    html.append("</td>\n");
    html.append("<td width=\"100px\" align=\"center\">");
    if (showViewImgs){
        html.append(link.getViewDisplayImage());
    } else {
        html.append(link.getViewDisplayString());
    }
    html.append("</td>\n");
    html.append("</tr>\n");

    return html.toString();
  }

  private static Set getUniprotLinks(List links) {
    Set sortedUniprotLinks = new TreeSet();
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (enzymeLinkDTO.getDatabaseCode().equals(XrefDatabaseConstant.SWISSPROT.getDatabaseCode()))
        sortedUniprotLinks.add(enzymeLinkDTO);
    }
    return sortedUniprotLinks;
  }

}
