package uk.ac.ebi.intenz.webapp.helper;

import java.util.*;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.1 $ $Date: 2008/02/25 14:03:41 $
 */
public class EnzymeLinkHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeClassHelper.class);

  private EnzymeLinkHelper() {
  }

  public static String renderLinks(SortedSet links, String ec) {
    StringBuffer html = new StringBuffer();
    Set databaseNames = new HashSet();
    SortedSet uniprotLinks = new TreeSet();
    SortedSet otherLinks = new TreeSet();
    for (Iterator it = links.iterator(); it.hasNext();) {
      EnzymeLink link = (EnzymeLink) it.next();
      if (link.getXrefDatabaseConstant() == XrefDatabaseConstant.SWISSPROT) {
        uniprotLinks.add(link);
        continue;
      }
      otherLinks.add(link);
      databaseNames.add(link.getXrefDatabaseConstant().getDisplayName());
    }
    html.append("<div style=\"margin-left: 2em\">");
    html.append(renderIubmbLinks(otherLinks, ec));
    html.append("\n<br/><br/>\n");
    html.append(renderUniprotLinks(uniprotLinks, ec));
    html.append("</div>");

    return html.toString();
  }

  public static String renderIubmbLinks(SortedSet links, String ec) {
    StringBuffer html = new StringBuffer();
    int iii = 0;
    List casNumbers = new ArrayList();
    for (Iterator it = links.iterator(); it.hasNext(); iii++) {
      EnzymeLink link = (EnzymeLink) it.next();
      if (link.getXrefDatabaseConstant().getDisplayName().equals(XrefDatabaseConstant.CAS.getDatabaseCode())) {
        if (!link.getAccession().equals("")) casNumbers.add(link);
        continue;
      }
      if (html.length() > 0) html.append(", ");
      html.append("<a href=\"")
      	.append(link.getFullUrl(ec))
      	.append("\" target=\"_blank\"");
      // Hovers:
      if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.DIAGRAM)
    		  || link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.MIM)){
    	  html.append(" title=\"").append(link.getName()).append('"');
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.PROSITE)){
    	  html.append(" title=\"Database of protein families and domains\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.BRENDA)){
    	  html.append(" title=\"A comprehensive enzyme information system (Cologne University Bioinformatics Center)\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.CSA)){
    	  html.append(" title=\"Catalyic Site Atlas\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.ENZYME)){
    	  html.append(" title=\"ENZYME database at Expert Protein Analysis SYstem (Swiss Institute of Bioinformatics)\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.ERGO)){
    	  html.append(" title=\"ERGO Light: curated database of public and proprietary genomic DNA\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.MIM)){
    	  html.append(" title=\"Online Mendelian Inheritance in Man: a catalog of human genes and genetic disorders (NCBI)");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.PDB)){
    	  html.append(" title=\"Known enzyme structures deposited in the Protein Data Bank (PDB)\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.MEROPS)){
    	  html.append(" title=\"Information resource for peptidases and the proteins that inhibit them\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.UMBBD)){
    	  html.append(" title=\"University of Minnesota Biocatalysis/Biodegradation Database\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.NIST74)){
    	  html.append(" title=\"Thermodynamics of Enzyme-Catalyzed Reactions\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.GO)){
    	  html.append(" title=\"Gene Ontology: "+ link.getName() +"\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.KEGG)){
    	  html.append(" title=\"KEGG ENZYME: a part of the KEGG (Kyoto Encyclopedia of Genes and Genomes) LIGAND database\"");
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.NC_IUBMB)){
    	  html.append(" title=\"Nomenclature Comitee of the IUBMB\"");
      }
      html.append(">");
      // Link text:
      if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.GO)){
    	  html.append(link.getAccession());
      } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.PROSITE)){
    	  html.append("PROSITE:").append(link.getAccession());
      } else {
    	  html.append(link.getXrefDatabaseConstant().getDisplayName());
      }
      html.append("</a>");
    }
    if (casNumbers.size() > 0){
      if (html.length() > 0) html.append(", ");
      html.append("CAS Registry Number");
      if (casNumbers.size() > 1) html.append("s");
      html.append(": ");
      for (int i = 0; i < casNumbers.size(); i++){
          EnzymeLink cas = (EnzymeLink) casNumbers.get(i);
          if (i > 0) html.append(", ");
          html.append(cas.getAccession());
          String dc = cas.getDataComment();
          if (dc != null && dc.length() > 0){
        	  html.append(" (").append(dc).append(")");
          }
      }
    }
    return html.toString();
  }

  public static String renderUniprotLinks(SortedSet links, String ec) {
//    Set uniprotLinks = getUniprotLinks(links);
    if (links == null) throw new NullPointerException("Parameter 'links' must not be null.");
    StringBuffer html = new StringBuffer();
    if (links.size() > 0) {
//      html.append("<tr>\n");
//      html.append("<td valign=\"top\" nowrap=\"nowrap\" colspan=\"2\">\n");

      html.append("<table border=\"0\"><tr><td valign=\"top\" nowrap=\"nowrap\" width=\"130\">");
      html.append("<b title=\"UniProt Knowledge Base: curated protein sequence database\">UniProtKB/Swiss-Prot</b>");
      if (links.size() > 9) {
        html.append(" (").append(links.size()).append("):<br/>");
        html.append(" <span class=\"link_url\">[<a class=\"link\" id=\"sp_more\" href=\"javascript:fold('sp', 'sp_more', 'show', 'hide');\">show</a>]</span>\n");
      } else {
        html.append(':');
      }
      html.append("</td>\n");
      html.append("<td valign=\"top\" align=\"left\" nowrap=\"nowrap\" width=\"100%\">\n");
      if (links.size() > 9) {
        html.append("&nbsp;");
        html.append("<div id=\"sp\" style=\"display:none\">\n");
      }
      html.append("<table border=\"0\" celltabbing=\"3\">\n");
      int count = 0;
      for (Iterator it = links.iterator(); it.hasNext();) {
        EnzymeLink uniprotLink = (EnzymeLink) it.next();
        if (count % 2 > 0 || count == 0) {
          if (count == 0) html.append("<tr>\n");
          html.append("<td valign=\"top\"><a target=\"_blank\" href=\"");
          html.append(uniprotLink.getFullUrl(ec));
          html.append("\">");
          html.append(uniprotLink.getAccession());
          html.append("</a></td>\n");
          html.append("<td valign=\"top\">\n");
          html.append(uniprotLink.getName());
          html.append("</td>\n");
          html.append("<td>&nbsp;</td>\n");
          if(!it.hasNext()) html.append("</tr>\n");
          count++;
        } else {
          html.append("<td valign=\"top\"><a target=\"_blank\" href=\"");
          html.append(uniprotLink.getFullUrl(ec));
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
      html.append("</table></td></tr>\n");
      if (links.size() > 9) html.append("</div>\n");
      html.append("</td></tr></table>");
//      html.append("</td></tr>");
      html.append("\n");
    }

    return html.toString();
  }

  private static String getLinkHTMLTableRow(EnzymeLink link, String ec, boolean isNotFirstLink) {
    final String displayName = link.getXrefDatabaseConstant().getDisplayName();
    StringBuffer html = new StringBuffer();
    html.append("<tr>\n");
    html.append("<td nowrap=\"nowrap\" colspan=\"2\">\n");

    html.append("<table border=\"0\"><tr><td nowrap=\"nowrap\" width=\"80px\">");

    if (isNotFirstLink) {
      html.append("&nbsp;");
    } else {
      html.append("<b>");
      html.append(displayName);
      html.append(":</b>\n");
    }
    html.append("</td>\n");
    html.append("<td class=\"link_url\" nowrap=\"nowrap\">(");
    if (displayName.equals("BRENDA") || displayName.equals("KEGG") ||
        displayName.equals("GO") ||
        displayName.equals("EXPASY") || displayName.equals("ERGO") ||
        displayName.equals("NIST 74")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getFullUrl(ec) + "\">");
      html.append(link.getFullUrl(ec));
      html.append("</a>");
    }

    if (displayName.equals("PROSITE") || displayName.equals("CAS")) {
      if (displayName.equals("PROSITE")) {
        html.append("<a class=\"link\" target=\"new\" href=\"" + link.getFullUrl(ec) + "\">");
        html.append(link.getFullUrl(ec));
        html.append("</a>");
      }
      if (displayName.equals("CAS") && !link.getAccession().equals(""))
        html.append(link.getAccession());
    }

    if (displayName.equals("MIM")) {
      html.append(link.getName());
      html.append(" MIM:");
      html.append(link.getAccession());
    }

    if (displayName.equals("DIAGRAM")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getFullUrl(ec) + "\">");
      html.append(link.getName());
    }

    if (displayName.equals("UM-BBD") || displayName.equals("MEROPS")) {
      html.append("<a class=\"link\" target=\"new\" href=\"" + link.getFullUrl(ec) + "\">");
      html.append(link.getFullUrl(ec));
      html.append("</a>");
    }
    html.append(")</td></tr>\n");

    html.append("</table></td></tr>\n");

    return html.toString();
  }

  private static Set getUniprotLinks(List links) {
    Set sortedlinks = new TreeSet();
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLink enzymeLink = (EnzymeLink) links.get(iii);
      if (enzymeLink.getXrefDatabaseConstant() == XrefDatabaseConstant.SWISSPROT)
        sortedlinks.add(enzymeLink);
    }
    return sortedlinks;
  }

}
