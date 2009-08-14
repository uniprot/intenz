package uk.ac.ebi.intenz.webapp.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.EventConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.history.HistoryEvent;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.tools.sib.helper.FFWriterHelper;
import uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;
import uk.ac.ebi.intenz.webapp.IntEnzUtilities;
import uk.ac.ebi.interfaces.sptr.SPTRException;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.XRef;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/04/16 15:02:01 $
 * @deprecated PLEASE PLEASE TAKE ALL THIS ******* HTML OUT OF HERE!
 */
public class EnzymeEntryHelper {

  private static final Logger LOGGER = Logger.getLogger(EnzymeClassHelper.class);

  private EnzymeEntryHelper() { }

  public static String toHTML(EnzymeEntry enzymeEntry, SpecialCharacters encoding, EncodingType encodingType,
                              EnzymeViewConstant view) throws SPTRException {
    if (enzymeEntry.getHistory().isDeletedRootNode() || enzymeEntry.getHistory().isTransferredRootNode()) {
      return deletedEntryToHTML(enzymeEntry, encoding, encodingType, view);
    } else {
      return wholeEntryToHTML(enzymeEntry, encoding, encodingType, view);
    }
  }

  /**
   * Return the HTML representation of a deleted entry.
 * @param encodingType TODO
   *
   * @return the HTML representation.
   * @throws SPTRException
   */
  private static String deletedEntryToHTML(EnzymeEntry enzymeEntry, SpecialCharacters encoding,
                                           EncodingType encodingType, EnzymeViewConstant view)
  throws SPTRException {
    StringBuffer htmlStringBuffer = new StringBuffer();
    HistoryEvent latestHistoryEvent = enzymeEntry.getHistory().getLatestHistoryEventOfRoot();

    if (enzymeEntry.isGhost()) {
      htmlStringBuffer.append("<tr>\n");
      htmlStringBuffer.append("<td align=\"left\">\n");
      htmlStringBuffer.append("<a href=\"query?cmd=SearchEC&ec=")
      	.append(enzymeEntry.getEc().toString()).append("\">EC ")
      	.append(enzymeEntry.getEc().toString()).append("</a>\n");
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("<td align=\"left\">\n");
      htmlStringBuffer.append("<span class=\"inactiveEntry\">");
      if (enzymeEntry.getHistory().isDeletedRootNode()){
        htmlStringBuffer.append("deleted");
      } else if (enzymeEntry.getHistory().isTransferredRootNode()){
    	  if (latestHistoryEvent.getNote() != null && !latestHistoryEvent.getNote().equals("")){
    		  htmlStringBuffer.append(IntEnzUtilities.linkEcNumber(latestHistoryEvent.getNote(), true));
//    		  htmlStringBuffer.append("<br/>");
    	  }
    	  else {
    		  htmlStringBuffer.append("<i>Transferred to EC ")
    		  .append(IntEnzUtilities.linkEcNumber(latestHistoryEvent.getAfterNode().getEnzymeEntry().getEc().toString(), false))
    		  .append("</i>");
//  		  htmlStringBuffer.append("<span class=\"inactiveEntry\">transferred</span>");
    	  }
      }
      htmlStringBuffer.append("</span>").append("</td>\n").append("</tr>\n");
      return htmlStringBuffer.toString();
    }

    if (view == EnzymeViewConstant.INTENZ || view == EnzymeViewConstant.IUBMB)
      htmlStringBuffer.append(getIubmbIntEnzDeletedEntry(enzymeEntry, latestHistoryEvent, encoding, view ==
                                                                                                    EnzymeViewConstant.IUBMB));
    if (view == EnzymeViewConstant.SIB){
//        htmlStringBuffer.append(getSibDeletedEntry(enzymeEntry));
        htmlStringBuffer.append(getSibView(enzymeEntry, encoding, encodingType));
    }

    return getViewsTabs(enzymeEntry, view) + htmlStringBuffer.toString();
  }

  private static String getIubmbIntEnzDeletedEntry(EnzymeEntry enzymeEntry, HistoryEvent latestHistoryEvent,
                                                   SpecialCharacters encoding, boolean isIubmbView) {
    StringBuffer htmlStringBuffer = new StringBuffer();

		if (!isIubmbView && !enzymeEntry.getStatus().equals(EnzymeStatusConstant.PROPOSED)){
			htmlStringBuffer.append(getXmlLink(enzymeEntry));
		}

    // Head
    htmlStringBuffer.append("<table align=\"center\" style=\"margin-bottom: 0px\">\n")
		.append("<tr><td class=\"centered\"><b>");
    if (isIubmbView)
		htmlStringBuffer.append("NC-IUBMB Enzyme Nomenclature");
    else
    	htmlStringBuffer.append("IntEnz Enzyme Nomenclature");
    htmlStringBuffer.append("</b></td></tr>\n");
    htmlStringBuffer.append("<tr><td class=\"centered\"><b style=\"font-size: 150%\">EC ")
    		.append(enzymeEntry.getEc().toString()).append("</b></td></tr>\n");
    if (enzymeEntry.getStatus() == EnzymeStatusConstant.PROPOSED) {
        htmlStringBuffer.append("<tr><td class=\"centered\">(proposal)</td></tr>\n");
    }
	htmlStringBuffer.append("</table>\n");

    htmlStringBuffer.append("<table width=\"100%\" border=\"0\">\n");
    htmlStringBuffer.append("<tr>\n");

    if (latestHistoryEvent.getEventClass() == EventConstant.DELETION)
      htmlStringBuffer.append("<td><b>Deleted entry</b>:\n");
    if (latestHistoryEvent.getEventClass() == EventConstant.TRANSFER)
      htmlStringBuffer.append("<td><b>Transferred entry</b>:\n");

    if (!enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName().equals("")) {
      htmlStringBuffer.append(encoding.xml2Display(enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName()));
      htmlStringBuffer.append(". ");
    }

    if (!latestHistoryEvent.getNote().equals("")) {
//      htmlStringBuffer.append(IntEnzUtilities.linkECnumber(latestHistoryEvent.getNote()));
      htmlStringBuffer.append(IntEnzUtilities.cleanInternalLinks(latestHistoryEvent.getNote(), true));
      if (!latestHistoryEvent.getNote().endsWith(".")) htmlStringBuffer.append(".");
      htmlStringBuffer.append("<br/>");
    }
    if (latestHistoryEvent.getEventClass() == EventConstant.TRANSFER){
		htmlStringBuffer.append("<i>Transferred to EC ")
			.append(IntEnzUtilities.linkEcNumber(latestHistoryEvent.getAfterNode().getEnzymeEntry().getEc().toString(), false))
			.append("</i>");
    }

    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("</tr>\n");
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td align=\"center\">\n");
    htmlStringBuffer.append("<b class=\"centered\">[" + enzymeEntry.getHistory().getRootNode().getHistoryLine() + "]</b>\n");
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("</tr>\n");
    htmlStringBuffer.append("</table>\n");
    return htmlStringBuffer.toString();
  }

  private static String getSibContentTitle(String ecString) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<div style=\"font-size: larger\"><b>ENZYME: </b>");
    htmlStringBuffer.append(ecString);
    htmlStringBuffer.append("</div>\n");
    return htmlStringBuffer.toString();
  }

  /**
   * Returns the HTML representation of the whole entry.
   *
   * @return the HTML representation.
   */
  private static String wholeEntryToHTML(EnzymeEntry enzymeEntry, SpecialCharacters encoding,
                                         EncodingType encodingType, EnzymeViewConstant view)
          throws SPTRException {
    // Ghost entries only provide a minimum of information.
    if (enzymeEntry.isGhost()) {
      StringBuffer htmlStringBuffer = new StringBuffer();
      htmlStringBuffer.append("<tr>\n");
      htmlStringBuffer.append("<td valign=\"top\" align=\"left\">\n");
      htmlStringBuffer.append("<a href=\"query?cmd=SearchEC&ec=");
      htmlStringBuffer.append(enzymeEntry.getEc().toString());
      htmlStringBuffer.append("&status=");
      htmlStringBuffer.append(enzymeEntry.getStatus().getCode());
      htmlStringBuffer.append("\">");
      htmlStringBuffer.append("EC ").append(enzymeEntry.getEc().toString());
      htmlStringBuffer.append("</a>\n");
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("<td valign=\"top\" align=\"left\">\n");
      htmlStringBuffer.append(encoding.xml2Display(enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName()));
      if (enzymeEntry.getStatus() == EnzymeStatusConstant.PROPOSED) {
        htmlStringBuffer.append("&nbsp;<small><b>(proposal)</b></small>");
      }
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("</tr>\n");
      return htmlStringBuffer.toString();
    }

	String html = null;
    if (view == EnzymeViewConstant.INTENZ)
		html = getIntEnzView(enzymeEntry, encoding, encodingType);
    else if (view == EnzymeViewConstant.SIB)
		html = getSibView(enzymeEntry, encoding, encodingType);
    else html = getIubmbView(enzymeEntry, encoding, encodingType);

	return getViewsTabs(enzymeEntry, view) + html;
  }

	private static String getViewsTabs(EnzymeEntry entry, EnzymeViewConstant view){
		StringBuffer tabs = new StringBuffer();
		tabs.append("\n<div class=\"viewsTabBar\">&nbsp;");
		tabs.append(getTab(entry, view, EnzymeViewConstant.INTENZ));
		tabs.append(getTab(entry, view, EnzymeViewConstant.IUBMB));
		tabs.append(getTab(entry, view, EnzymeViewConstant.SIB));
		tabs.append("</div>\n");

		return tabs.toString();
	}

    private static String getTab(EnzymeEntry entry, EnzymeViewConstant view, EnzymeViewConstant tabView){
		StringBuffer tab = new StringBuffer();
		String tabLinkOpen = view.equals(tabView)?
			"" : "<a href=\"query?cmd=SearchID&id="+entry.getId()+"&view="+tabView.toString()+"\">";
		String tabClass = view.equals(tabView)?
			"selected viewTab" : "unselected viewTab";
		String tabName = tabView.equals(EnzymeViewConstant.INTENZ)?
			"IntEnz view" : tabView.toDisplayString()+" view";
		String tabTitle = tabView.equals(EnzymeViewConstant.INTENZ)?
			"" : "EC "+entry.getEc().toString()+" as seen on the "+tabView.toDisplayString()+" website";
		String tabLinkClose = view.equals(tabView)?
			"" : "</a>";
		tab.append("<span class=\"").append(tabClass).append("\" title=\"")
			.append(tabTitle).append("\">");
		tab.append(tabLinkOpen).append(tabName).append(tabLinkClose);
		tab.append("</span>");
    	return tab.toString();
    }


  private static String getIubmbView(EnzymeEntry enzymeEntry, SpecialCharacters encoding, EncodingType encodingType) {
    StringBuffer htmlStringBuffer = new StringBuffer();

    htmlStringBuffer.append("<table width=\"100%\" border=\"0\">\n");
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td width=\"25%\"></td>\n");
    htmlStringBuffer.append("<td align=\"center\" width=\"50%\">\n");
    htmlStringBuffer.append("<b>IUBMB Enzyme Nomenclature</b>\n");
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("<td width=\"25%\"></td>\n");
    htmlStringBuffer.append("</tr>\n");
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td width=\"25%\"></td>\n");
    htmlStringBuffer.append("<td align=\"center\" width=\"50%\">\n");
    htmlStringBuffer.append("<b style=\"font-size: 150%\">EC " + enzymeEntry.getEc().toString() + "</b>\n");
    if (enzymeEntry.getStatus() == EnzymeStatusConstant.PROPOSED) {
      htmlStringBuffer.append("<br>(proposal)\n");
    }

    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("<td width=\"25%\">\n");
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("</tr>\n");
    htmlStringBuffer.append("</table>\n");

	htmlStringBuffer.append("<div class=\"iubmb\"><b>Accepted name:</b> ")
		.append(encoding.xml2Display(enzymeEntry.getCommonName(EnzymeViewConstant.IUBMB).getName()))
		.append("</div>");

	htmlStringBuffer.append("<div class=\"iubmb\"><b>Reaction:</b> ");
	List reactions = getIubmbReactions(enzymeEntry.getEnzymaticReactions());
	for (int i = 0, steps = 0; i < reactions.size(); i++){
		Reaction reaction = (Reaction) reactions.get(i);
		if (reactions.size() > 1 && !reaction.getTextualRepresentation().matches("\\(?\\d\\p{Lower}\\)?\\s.*"))
			htmlStringBuffer.append("(").append(i+1-steps).append(") ");
		else steps++;
		htmlStringBuffer.append(EnzymeReactionHelper.textualRepresentationToHTML(reaction, encoding, encodingType));
		if (i < reactions.size()-1)
			htmlStringBuffer.append("<br/>");
	}
	htmlStringBuffer.append("</div>");

	List synonyms = getIubmbSynonyms(enzymeEntry.getSynonyms());
	if (!synonyms.isEmpty()){
		htmlStringBuffer.append("<div class=\"iubmb\"><b>Other name(s):</b> ");
		for (Iterator it = synonyms.iterator(); it.hasNext();) {
			EnzymeName synonym = (EnzymeName) it.next();
			htmlStringBuffer.append(encoding.xml2Display(synonym.getName()));
			if (synonym.getQualifier() == EnzymeNameQualifierConstant.MISLEADING)
				htmlStringBuffer.append(" [misleading]");
			if (synonym.getQualifier() == EnzymeNameQualifierConstant.OBSOLETE)
				htmlStringBuffer.append(" [obsolete]");
			if (synonym.getQualifier() == EnzymeNameQualifierConstant.AMBIGUOUS)
				htmlStringBuffer.append(" [ambiguous]");
			if (it.hasNext()) htmlStringBuffer.append("; ");
		}
		htmlStringBuffer.append("</div>");
	}

	if (!enzymeEntry.getSystematicName().getName().equals("") &&
		!enzymeEntry.getSystematicName().getName().equals("-")) { // Dash hack for empty systematic names
		htmlStringBuffer.append("<div class=\"iubmb\"><b>Systematic name:</b> ")
			.append(encoding.xml2Display(enzymeEntry.getSystematicName().getName()))
			.append("</div>");
	}

	List comments = getIubmbComments(enzymeEntry.getComments());
	if (comments.size() > 0){
		htmlStringBuffer.append("<div class=\"iubmb\"><b>Comments:</b> ");
		for (Iterator it = comments.iterator(); it.hasNext();){
			EnzymeComment comment = (EnzymeComment) it.next();
			htmlStringBuffer.append(
			    IntEnzUtilities.cleanInternalLinks(encoding.xml2Display(comment.getCommentText()), true));
			if (it.hasNext()) htmlStringBuffer.append("<br/>");
		}
		htmlStringBuffer.append("</div>");
	}

	SortedSet links = enzymeEntry.getLinks();
//	if (links.size() > 0) {
		htmlStringBuffer.append("<div class=\"iubmb\"><b>Links to other databases:</b> ")
			.append(EnzymeLinkHelper.renderIubmbLinks(getIubmbLinksOnly(links), enzymeEntry.getEc().toString()))
			.append("</div>");
//	}

	htmlStringBuffer.append("<div class=\"iubmb\"><b>References:</b><br/>");
	List references = enzymeEntry.getReferences();
	for (int i = 0; i < references.size(); i++){
		Reference reference = (Reference) references.get(i);
		htmlStringBuffer.append(i+1).append(". ")
			.append(encoding.xml2Display(reference.toString()));
		if (i < references.size() - 1) htmlStringBuffer.append("<br/>");
	}
	htmlStringBuffer.append("</div>");

	htmlStringBuffer.append("<div class=\"iubmbHistory\">[")
		.append(enzymeEntry.getHistory().getRootNode().getHistoryLine())
		.append("]</div>");

    return htmlStringBuffer.toString();
  }

  private static List getIubmbComments(List comments) {
    List iubmbComments = new ArrayList();
    for (Iterator it = comments.iterator(); it.hasNext();) {
      EnzymeComment comment = (EnzymeComment) it.next();
      if(comment.getView().isInIUBMBView()) iubmbComments.add(comment);
    }
    return iubmbComments;
  }

  private static List getIubmbSynonyms(List synonyms) {
    List iubmbSynonyms = new ArrayList();
    for (Iterator it = synonyms.iterator(); it.hasNext();) {
      EnzymeName synonym = (EnzymeName) it.next();
      if(synonym.getView().isInIUBMBView()) iubmbSynonyms.add(synonym);
    }
    return iubmbSynonyms;
  }

  private static List<Reaction> getIubmbReactions(EnzymaticReactions reactions) {
    List<Reaction> iubmbReactions = new ArrayList<Reaction>();
    for (int i = 0; i < reactions.size(); i++){
        Reaction reaction = reactions.getReaction(i);
		// For now, Rhea-ctions won't appear:
		if (reaction.getId() > Reaction.NO_ID_ASSIGNED /*&& !reaction.getStatus().equals(Status.OK)*/)
			// Not accepted Rhea-ction
			continue;
        if (reactions.getReactionView(i).isInIUBMBView()) iubmbReactions.add(reaction);
    }
    return iubmbReactions;
  }

  private static SortedSet getIubmbLinksOnly(SortedSet links) {
    SortedSet iubmbLinks = new TreeSet();
    for (Iterator it = links.iterator(); it.hasNext();) {
      EnzymeLink link = (EnzymeLink) it.next();
      if(link.getView().isInIUBMBView()) iubmbLinks.add(link);
    }
    iubmbLinks.add(EnzymeLink.PDB);
    iubmbLinks.add(EnzymeLink.EXPASY);
    iubmbLinks.add(EnzymeLink.BRENDA);
    iubmbLinks.add(EnzymeLink.KEGG);
    return iubmbLinks;
  }

  public static String getSibView(EnzymeEntry enzymeEntry, SpecialCharacters encoding, EncodingType encodingType)
          throws SPTRException {
    EnzymeEntryImpl sibEnzymeEntry = SibEntryHelper.getSibEnzymeEntry(enzymeEntry, encoding, encodingType);
    StringBuffer htmlStringBuffer = new StringBuffer();

    // Head
    htmlStringBuffer.append(getSibContentTitle(enzymeEntry.getEc().toString()));

    if (enzymeEntry.getStatus() == EnzymeStatusConstant.PROPOSED) {
      htmlStringBuffer.append("<div class=\"centered\">\n");
      htmlStringBuffer.append("(proposal)\n");
      htmlStringBuffer.append("</div>\n");
    }

    htmlStringBuffer.append("<hr/>\n");
    htmlStringBuffer.append("<div><pre>");
    String xrefHyperlinks = FFWriterHelper.createXrefHyperlinks(EnzymeFlatFileWriter.export(sibEnzymeEntry));
    htmlStringBuffer.append(xrefHyperlinks);
    htmlStringBuffer.append("</pre></div>\n");

    return htmlStringBuffer.toString();
  }

	private static String getXmlLink(EnzymeEntry enzymeEntry){
		StringBuffer xmlLink = new StringBuffer();
		String xmlBase = "ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml/ASCII/";
		String classEc = new StringBuffer("EC_")
			.append(enzymeEntry.getEc().getEc1())
			.toString();
		String subclassEc = new StringBuffer(classEc).append('.')
			.append(enzymeEntry.getEc().getEc2())
			.toString();
		String subsubclassEc = new StringBuffer(subclassEc).append('.')
			.append(enzymeEntry.getEc().getEc3())
			.toString();
		String xmlTree = new StringBuffer(classEc).append('/')
			.append(subclassEc).append('/')
			.append(subsubclassEc).append('/')
			.append("EC_").append(enzymeEntry.getEc().toString()).append(".xml")
			.toString();
		xmlLink.append("<div class=\"xml\" title=\"Download EC ")
			.append(enzymeEntry.getEc().toString()).append(" in XML format\">")
			.append("<a href=\"").append(xmlBase).append(xmlTree)
			.append("\" class=\"xml\">")
			.append("<span class=\"xml\">XML</span></a></div>");
		return xmlLink.toString();
	}

  private static String getIntEnzView(EnzymeEntry enzymeEntry, SpecialCharacters encoding, EncodingType encodingType) {
    StringBuffer htmlStringBuffer = new StringBuffer();

		if (!enzymeEntry.getStatus().equals(EnzymeStatusConstant.PROPOSED)){
			htmlStringBuffer.append(getXmlLink(enzymeEntry));
		}

    // Head
    htmlStringBuffer.append("<table align=\"center\" style=\"margin-bottom: 0px\">\n")
		.append("<tr><td class=\"centered\"><b>IntEnz Enzyme Nomenclature</b></td></tr>\n")
    	.append("<tr><td class=\"centered\"><b style=\"font-size: 150%\">EC ")
    		.append(enzymeEntry.getEc().toString()).append("</b></td></tr>\n");
    if (enzymeEntry.getStatus() == EnzymeStatusConstant.PROPOSED) {
        htmlStringBuffer.append("<tr><td class=\"centered\">(proposal)</td></tr>\n");
    }
	htmlStringBuffer.append("</table>\n");

    // Names
    if (enzymeEntry.hasNames()) {
      htmlStringBuffer.append(getBlockHeader("Names"));
      htmlStringBuffer.append("<table border=\"0\" cellspacing=\"5\" style=\"margin-left: 2em\">\n");
      EnzymeName commonName = enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ);
      EnzymeName systematicName = enzymeEntry.getSystematicName();
      List synonyms = enzymeEntry.getSynonyms(EnzymeViewConstant.INTENZ);
      if (commonName != null && !commonName.getName().equals(""))
        htmlStringBuffer.append(getIntEnzCommonName(commonName, encoding));
      if (synonyms != null && synonyms.size() > 0)
        htmlStringBuffer.append(getIntEnzSynonyms(synonyms, encoding));
      if (systematicName != null && !systematicName.getName().equals("") &&
          !systematicName.getName().equals("-")) // Dash hack for empty systematic names
        htmlStringBuffer.append(getIntEnzSystematicName(systematicName, encoding));
      htmlStringBuffer.append("</table>\n");
    }

    // Reaction(s)
	EnzymaticReactions er = enzymeEntry.getEnzymaticReactions();
    if (er != null && er.size() > 0) {
      htmlStringBuffer.append(getBlockHeader("Reaction"));
      htmlStringBuffer.append(getIntEnzReaction(er, encoding, encodingType));
    }

    // Cofactors
    final Set<Object> cofactors = enzymeEntry.getCofactors();
    if (cofactors != null && cofactors.size() > 0) {
      htmlStringBuffer.append(getBlockHeader("Cofactors"));
      htmlStringBuffer.append(getIntEnzCofactor(cofactors, encoding, encodingType));
    }

    // Comments
    final List comments = enzymeEntry.getComments(EnzymeViewConstant.INTENZ);
    if (comments != null && comments.size() > 0) {
      htmlStringBuffer.append(getBlockHeader("Comments"));
      htmlStringBuffer.append(getIntEnzComments(comments, encoding));
    }

    // Links
    final SortedSet links = enzymeEntry.getLinks();
//    if (links != null && links.size() > 0) {
      htmlStringBuffer.append(getBlockHeader("Links to other databases"));
      htmlStringBuffer.append(getIntEnzLinks(links, enzymeEntry.getEc().toString()));
//    }

    // References
    final List references = enzymeEntry.getReferences();
    if (references != null && references.size() > 0) {
      htmlStringBuffer.append(getBlockHeader("References"));
      htmlStringBuffer.append(getIntEnzReferences(references, encoding));
    }

    htmlStringBuffer.append("<div class=\"centered\"><b>[")
    		.append(enzymeEntry.getHistory().getRootNode().getHistoryLine())
    		.append("]</b></div>");

    return htmlStringBuffer.toString();
  }

  private static String getIntEnzCommonName(EnzymeName commonName, SpecialCharacters encoding) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td nowrap=\"nowrap\" valign=\"top\">\n");
    htmlStringBuffer.append("<b>Accepted name:</b>\n");
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("<td width=\"100%\" valign=\"top\">\n");
    htmlStringBuffer.append(encoding.xml2Display(commonName.getName()));
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("</tr>\n");
    return htmlStringBuffer.toString();
  }

  private static String getIntEnzSynonyms(List synonyms, SpecialCharacters encoding) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td nowrap=\"nowrap\" valign=\"top\">\n");
    htmlStringBuffer.append("<b>Other name(s):</b>\n");
    htmlStringBuffer.append("</td>\n");
    for (int iii = 0; iii < synonyms.size(); iii++) {
      EnzymeName synonym = (EnzymeName) synonyms.get(iii);
      if (iii > 0) {
        htmlStringBuffer.append("<tr>\n");
        htmlStringBuffer.append("<td>&nbsp;</td>\n");
      }
      htmlStringBuffer.append("<td bgcolor=\"#FFFFFF\" width=\"100%\" valign=\"top\">\n");
      htmlStringBuffer.append(encoding.xml2Display(synonym.getName()));
      if (synonym.getQualifier() == EnzymeNameQualifierConstant.MISLEADING)
        htmlStringBuffer.append(" [misleading]");
      if (synonym.getQualifier() == EnzymeNameQualifierConstant.OBSOLETE)
        htmlStringBuffer.append(" [obsolete]");
      if (synonym.getQualifier() == EnzymeNameQualifierConstant.AMBIGUOUS)
        htmlStringBuffer.append(" [ambiguous]");
      if (synonym.getQualifier() == EnzymeNameQualifierConstant.INCORRECT)
          htmlStringBuffer.append(" [incorrect]");
      if (synonym.getQualifier() == EnzymeNameQualifierConstant.MISPRINT)
          htmlStringBuffer.append(" [misprint]");
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("</tr>\n");
    }
    return htmlStringBuffer.toString();
  }

  private static String getIntEnzSystematicName(EnzymeName systematicName, SpecialCharacters encoding) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<tr>\n");
    htmlStringBuffer.append("<td nowrap=\"nowrap\" valign=\"top\">\n");
    htmlStringBuffer.append("<b>Systematic name:</b>\n");
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("<td width=\"100%\" valign=\"top\">\n");
    htmlStringBuffer.append(encoding.xml2Display(systematicName.getName()));
    htmlStringBuffer.append("</td>\n");
    htmlStringBuffer.append("</tr>\n");
    return htmlStringBuffer.toString();
  }

  private static String getIntEnzReaction(EnzymaticReactions reactions,
          SpecialCharacters encoding, EncodingType encodingType) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<table border=\"0\" cellspacing=\"5\" style=\"margin-left: 2em\">\n");
    if (reactions != null) {
      for (int iii = 0, counter= 1/*, steps = 0, rheactions = 0*/; iii < reactions.size(); iii++) {
        Reaction reaction = reactions.getReaction(iii);
		// For now, Rhea-ctions won't appear:
		if (reaction.getId() > Reaction.NO_ID_ASSIGNED /*&& !reaction.getStatus().equals(Status.OK)*/){
			// Not accepted Rhea-ction
//			rheactions++;
			continue;
		}
        if (!reactions.getReactionView(iii).isInIntEnzView()) continue;
        htmlStringBuffer.append("<tr>\n");
        if (reactions.size() > 1) {
          htmlStringBuffer.append("<td valign=\"top\">");
		  if (!reaction.getTextualRepresentation().matches("\\(?\\d\\p{Lower}\\)?\\s.*")){
			  //htmlStringBuffer.append('(').append(iii + 1 - rheactions - steps).append(')');
			  htmlStringBuffer.append('(').append(counter++).append(')');
//          } else {
//			  steps++;
		  }
		  htmlStringBuffer.append("</td>\n");
        }
        htmlStringBuffer.append("<td>");
        htmlStringBuffer.append(EnzymeReactionHelper.textualRepresentationToHTML(reaction, encoding, encodingType));
        htmlStringBuffer.append("</td>\n");
        htmlStringBuffer.append("</tr>\n");
      }
    }
    htmlStringBuffer.append("</table>\n");
    return htmlStringBuffer.toString();
  }

    protected static String getIntEnzCofactor(Set<Object> cofactors,
            SpecialCharacters xchars, EncodingType encodingType) {
        StringBuffer htmlStringBuffer = new StringBuffer();
        htmlStringBuffer.append("<table border=\"0\" cellspacing=\"5\" style=\"margin-left: 2em\">\n");
        int i = 1;
        for (Object cofactor : cofactors) {
            htmlStringBuffer.append("<tr>\n");
            if (cofactors.size() > 1) {
                htmlStringBuffer.append("<td valign=\"top\">(");
                htmlStringBuffer.append(i++);
                htmlStringBuffer.append(")</td>\n");
            }
            htmlStringBuffer.append("<td>");
            htmlStringBuffer.append(getCofactorObjectLinks(cofactor, xchars, encodingType, false));
            htmlStringBuffer.append("</td>\n");
            htmlStringBuffer.append("</tr>\n");
        }

        htmlStringBuffer.append("</table>\n");
        return htmlStringBuffer.toString();
    }
    
    protected static String getCofactorObjectLinks(Object o,
            SpecialCharacters xchars, EncodingType encodingType, boolean useBrakets){
        StringBuilder sb = new StringBuilder();
        if (o instanceof Cofactor){
            sb.append(getCompoundLink(((Cofactor) o).getCompound(), xchars, encodingType));
        } else if (o instanceof OperatorSet){
            sb.append(getCofactorSetLinks((OperatorSet) o, xchars, encodingType, useBrakets));
        } else {
            LOGGER.error(o + "should be a Cofactor or an OperatorSet");
            sb.append(o.toString());
        }
        return sb.toString();
    }
    
    protected static String getCompoundLink(Compound compound,
            SpecialCharacters xchars, EncodingType encodingType){
        StringBuilder sb =
                new StringBuilder(xchars.xml2Display(compound.toString(), encodingType));
        XRef compoundXref = compound.getXref();
        if (compoundXref != null){
            sb.insert(0, "\" target=\"_blank\">")
                .insert(0, compoundXref.getUrl())
                .insert(0, "<a href=\"");
            sb.append("</a>");
        }
        return sb.toString();
    }
    
    protected static String getCofactorSetLinks(OperatorSet os,
            SpecialCharacters xchars, EncodingType encodingType, boolean useBrakets){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object o : os){
            sb.append(getCofactorObjectLinks(o, xchars, encodingType, true));
            if (i++ < os.size() - 1){
                sb.append(' ')
                    .append(os.getOperator().replaceAll("OR\\d", "or").toLowerCase())
                    .append(' ');
            } 
        }
        if (useBrakets){
            sb.insert(0, '(').append(')');
        }
        return sb.toString();
    }

  private static String getIntEnzComments(List comments, SpecialCharacters encoding) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<table border=\"0\" cellspacing=\"5\" style=\"margin-left: 2em\">\n");
    for (int iii = 0; iii < comments.size(); iii++) {
      EnzymeComment comment = (EnzymeComment) comments.get(iii);
      htmlStringBuffer.append("<tr>\n");
      htmlStringBuffer.append("<td>\n");
      htmlStringBuffer.append(IntEnzUtilities.cleanInternalLinks(encoding.xml2Display(comment.getCommentText()), true));
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("</tr>\n");
    }
    htmlStringBuffer.append("</table>\n");
    return htmlStringBuffer.toString();
  }

  private static String getIntEnzLinks(SortedSet links, String ec) {
     SortedSet completeLinks = new TreeSet(links);
     completeLinks.add(EnzymeLink.PDB);
     completeLinks.add(EnzymeLink.CSA);
     completeLinks.add(EnzymeLink.EXPASY);
     completeLinks.add(EnzymeLink.BRENDA);
     completeLinks.add(EnzymeLink.KEGG);
     completeLinks.add(EnzymeLink.NC_IUBMB);
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append(EnzymeLinkHelper.renderLinks(completeLinks, ec));
    return htmlStringBuffer.toString();
  }

  private static String getIntEnzReferences(List references, SpecialCharacters encoding) {
    StringBuffer htmlStringBuffer = new StringBuffer();
    htmlStringBuffer.append("<table border=\"0\" cellspacing=\"5\" style=\"margin-left: 2em\">\n");

    for (int iii = 0; iii < references.size(); iii++) {
      Reference reference = (Reference) references.get(iii);
      htmlStringBuffer.append("<tr>\n");
      htmlStringBuffer.append("<td valign=\"top\">\n");
      htmlStringBuffer.append(iii + 1);
      htmlStringBuffer.append(".");
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("<td valign=\"top\">\n");
//      htmlStringBuffer.append(encoding.xml2Display(reference.toString()));
      htmlStringBuffer.append(encoding.xml2Display(getReferenceHtml(reference)));
      htmlStringBuffer.append("</td>\n");
      htmlStringBuffer.append("</tr>\n");
    }
    htmlStringBuffer.append("</table>\n");
    return htmlStringBuffer.toString();
  }

  private static String getReferenceHtml(Reference ref){
	  StringBuffer html = new StringBuffer();
	  html.append("<div class=\"pubauthors\">").append(ref.getAuthors()).append("</div>");
	  html.append("<div class=\"pubtitle\">").append(ref.getTitle()).append("</div>");
	  html.append("<div class=\"pubjournal\">");
	  if (ref instanceof Journal){
		  Journal journal = (Journal) ref;
		  html.append("<span class=\"pubName\">").append(journal.getPubName())
		  	.append("</span>");
		  boolean hasVolume = !StringUtil.isNullOrEmpty(journal.getVolume());
		  if (hasVolume){
			  html.append(" <b>").append(journal.getVolume()).append("</b>");
		  }
		  if (!StringUtil.isNullOrEmpty(journal.getFirstPage())){
			  if (hasVolume) html.append(": ");
			  html.append(journal.getFirstPage());
			  if (!StringUtil.isNullOrEmpty(journal.getLastPage())){
				  html.append('-').append(journal.getLastPage());
			  }
		  }
		  html.append(" (").append(ref.getYear()).append(").");
		  if (!journal.getPubMedId().equals("")) {
			  String url = "http://www.ebi.ac.uk/citexplore/citationDetails.do?dataSource=MED&externalId=";
			  html.append(" [PMID: <a target=\"_blank\" href=\"").append(url)
	        	.append(journal.getPubMedId())
	        	.append("\">")
	        	.append(journal.getPubMedId())
	        	.append("</a>]");
	      }
	  } else if (ref instanceof Book){
		  Book book = (Book) ref;
		  if (!StringUtil.isNullOrEmpty(book.getEditor(false))){
			  html.append("<span style=\"font-style: normal\">In: ")
			  	.append(book.getEditor(true)).append("</span>");
		  }
		  if (!StringUtil.isNullOrEmpty(book.getPubName())){
			  html.append(' ').append(book.getPubName());
		  }
		  if (!StringUtil.isNullOrEmpty(book.getEdition(false))){
			  html.append(", ").append(book.getEdition(true));
		  }
		  if (!StringUtil.isNullOrEmpty(book.getVolume())){
			  html.append(" vol. ").append(book.getVolume());
		  }
		  if (!StringUtil.isNullOrEmpty(book.getPublisher())){
			  html.append(", ").append(book.getPublisher());
		  }
		  if (!StringUtil.isNullOrEmpty(book.getPublisherPlace())){
			  html.append(", ").append(book.getPublisherPlace());
		  }
		  if (!StringUtil.isNullOrEmpty(book.getYear())){
			  html.append(", ").append(book.getYear());
		  }
		  if (!StringUtil.isNullOrEmpty(book.getFirstPage())){
			  html.append(", ").append(book.getFirstPage());
			  if (!StringUtil.isNullOrEmpty(book.getLastPage())){
				  html.append('-').append(book.getLastPage());
			  }
		  }
		  html.append('.');
	  } else if (ref instanceof Patent){
		  Patent patent = (Patent) ref;
		  html.append(patent.getPatentNumber());
	  }
	  html.append("</div>");
	  return html.toString();
  }

  private static String getBlockHeader(String name) {
    StringBuffer blockHeader = new StringBuffer();
    blockHeader.append("<h2>").append(name).append("</h2>");
    return blockHeader.toString();
  }
}
