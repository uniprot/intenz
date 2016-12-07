package uk.ac.ebi.intenz.webapp.utilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.biobabel.validator.DbIdentifierValidator;
import uk.ac.ebi.biobabel.validator.SyntaxValidator;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.webapp.dtos.CofactorDTO;
import uk.ac.ebi.intenz.webapp.dtos.CommentDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO;
import uk.ac.ebi.intenz.webapp.dtos.EnzymeNameDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReactionDTO;
import uk.ac.ebi.intenz.webapp.dtos.ReferenceDTO;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/03/12 12:29:16 $
 */
public class IntEnzValidations {

  // -------------------------- STATIC METHODS --------------------------

	private static String getStringValue(Object bean, Field field) {
		String value = null;
		if (isString(bean)) {
			value = (String) bean;
		} else {
			value = ValidatorUtils.getValueAsString(bean, field.getProperty());
		}
		return value;
	}

  /**
   * Validate EC numbers
   */
  public static boolean validateEc(Object bean,
          ValidatorAction validatorAction,
          Field field,
          ActionMessages actionMessages,
          HttpServletRequest httpServletRequest) {
      String value = getStringValue(bean, field);
      if (value == null) {
          actionMessages.add(field.getKey(), new ActionMessage("The given EC is invalid."));
          return false;
      }

      return DbIdentifierValidator.getInstance().validate(value,
              DbIdentifierValidator.EC_NUMBER);
  }

  public static boolean validateBrackets(Object bean,
          ValidatorAction validatorAction,
          Field field,
          ActionMessages actionMessages,
          HttpServletRequest httpServletRequest){
	  String value = getStringValue(bean, field);
	  return SyntaxValidator.getInstance().validate(value, SyntaxValidator.BRACKETS);
  }

  /**
   * Validate one CAS registry number
   */
  public static boolean validateCASRegistryNumberFormat(EnzymeLinkDTO CASLink) {
    if (CASLink == null) throw new NullPointerException("Parameter 'CASLink' must not be null.");
    return DbIdentifierValidator.getInstance().validate(CASLink.getAccession(),
            DbIdentifierValidator.CAS_REGISTRY_NUMBER);
  }

  public static boolean validateCASRegistryNumber(Object bean,
          ValidatorAction validatorAction,
          Field field,
          ActionMessages actionMessages,
          HttpServletRequest httpServletRequest) {
      List casLinks = null;
      if (bean instanceof List){
          casLinks = (List) bean;
      } else {
          casLinks = ((EnzymeDTO) bean).getCasLinks();
      }
      for (int i = 0; i < casLinks.size(); i++){
          EnzymeLinkDTO link = (EnzymeLinkDTO) casLinks.get(i);
          if (!DbIdentifierValidator.getInstance().validate(link.getAccession(),
                  DbIdentifierValidator.CAS_REGISTRY_NUMBER)) return false;
      }
      return true;
  }


//  public static boolean validateXChars(Object bean,
//                                       ValidatorAction validatorAction,
//                                       Field field,
//                                       ActionMessages actionMessages,
//                                       HttpServletRequest httpServletRequest) {
//    String value = null;
//    if (isString(bean))
//      value = (String) bean;
//    else
//      value = ValidatorUtils.getValueAsString(bean, field.getProperty());
//
//    // If nothing's there then checking is not necessary.
//    if (value == null) return true;
//
//    return true;
//  }

  /**
   * This method is used by Action classes to check the content of an enzyme form before performing the actual command.
   *
   * @param enzymeDTO DTO storing the form values.
   * @param errors    List for storing error errors.
   */
  public static void validateEnzymeDTOLists(EnzymeDTO enzymeDTO, ActionMessages errors) {
    if (enzymeDTO == null) throw new NullPointerException("Parameter 'enzymeDTO' must not be null.");
    if (errors == null) throw new NullPointerException("Parameter 'errors' must not be null.");
    validateCommonNames(enzymeDTO.getCommonNames(), errors);
    validateSynonyms(enzymeDTO.getSynonyms(), errors);
    validateReactions(enzymeDTO.getReactionDtos(), errors);
//    validateCofactors(enzymeDTO.getCofactors(), errors);
    validateComments(enzymeDTO.getComments(), errors);
    validateLinks(enzymeDTO.getLinks(), enzymeDTO.getEc(), errors);
    //validateReferences(enzymeDTO.getReferences(), null, null, errors, null);
  }

  protected static boolean isString(Object o) {
    return (o == null) ? true : String.class.isInstance(o);
  }

  // ---------------- PRIVATE METHODS ----------------------------------------------

  /**
   * Checks the value of the common name's view.
   * <p/>
   * It is not valid to have two common names with the view value 'INTENZ'.
   *
   * @param commonNames Common names list to be checked.
   * @param errors      List for storing error errors.
   */
  private static void validateCommonNames(List commonNames, ActionMessages errors) {
    assert commonNames != null : "Parameter 'commonNames' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";

    removeEmptyNameItems(commonNames);
    removeDuplicates(commonNames);
    boolean intenzViewUsed = false;
    for (int iii = 0; iii < commonNames.size(); iii++) {
      EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) commonNames.get(iii);
      if (EnzymeViewConstant.isInIntEnzView(enzymeNameDTO.getView()) &&
          ((enzymeNameDTO.getName() != null && !enzymeNameDTO.getName().equals("")) ||
           (enzymeNameDTO.getXmlName() != null && !enzymeNameDTO.getXmlName().equals("")))) {
        if (intenzViewUsed) {
          ActionMessage message = new ActionMessage("errors.form.common_name.invalidView");
          errors.add("commonName", message);
        } else
          intenzViewUsed = true;
      }
    }
  }

  private static void removeEmptyNameItems(List names) {
    assert names != null : "Parameter 'names' must not be null.";
    for (int iii = 0; iii < names.size(); iii++) {
      EnzymeNameDTO enzymeNameDTO = (EnzymeNameDTO) names.get(iii);
      if (enzymeNameDTO.getXmlName() == null || enzymeNameDTO.getXmlName().equals("")) {
        names.remove(iii);
        iii--;
      }
    }
  }

  private static void removeDuplicates(List list) {
    list.retainAll(SetUniqueList.decorate(list));
  }

  /**
   * At the moment this method only removes empty and duplicated items from the list of synonyms.
   *
   * @param synonyms List of synonyms to be checked.
   * @param errors   List for storing error errors.
   */
  private static void validateSynonyms(List synonyms, ActionMessages errors) {
    assert synonyms != null : "Parameter 'synonyms' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    removeEmptyNameItems(synonyms);
    removeDuplicates(synonyms);
  }

  /**
   * Checks if at least one reaction exists.
   * <p/>
   * Otherwise the user will be informed that one or more reactions have to exist for an enzyme entry.
   *
   * @param reactions List of reactions.
   * @param errors    List for storing error errors.
   */
  private static void validateReactions(List reactions, ActionMessages errors) {
    assert reactions != null : "Parameter 'reactions' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    removeEmptyReactionItems(reactions);
    removeDuplicates(reactions);
    if (reactions.size() == 0) {
      ActionMessage message = new ActionMessage("errors.form.reaction.notExisting");
      errors.add("reaction", message);
    }
  }

  private static void removeEmptyReactionItems(List reactions) {
    for (int iii = 0; iii < reactions.size(); iii++) {
      ReactionDTO reactionDTO = (ReactionDTO) reactions.get(iii);
      if (reactionDTO.getXmlTextualRepresentation() == null || reactionDTO.getXmlTextualRepresentation().equals("")) {
        reactions.remove(iii);
        iii--;
      }
    }
  }

  /**
   * At the moment this method only removes empty and duplicated items from the list of cofactors.
   *
   * @param cofactors The list of cofactors.
   * @param errors    List for storing error errors.
   * @deprecated will be using sets of cofactors (no emptiness, no duplicates)
   */
  private static void validateCofactors(List cofactors, ActionMessages errors) {
    assert cofactors != null : "Parameter 'cofactors' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    removeEmptyCofactorItems(cofactors);
    removeDuplicates(cofactors);
  }

  private static void removeEmptyCofactorItems(List cofactors) {
    for (int iii = 0; iii < cofactors.size(); iii++) {
      CofactorDTO cofactorDTO = (CofactorDTO) cofactors.get(iii);
      if (cofactorDTO.getXmlCofactorValue() == null || cofactorDTO.getXmlCofactorValue().equals("")) {
        cofactors.remove(iii);
        iii--;
      }
    }
  }

  /**
   * At the moment this method only removes empty and duplicated items from the list of comments.
   *
   * @param comments The list of comments.
   * @param errors   List for storing error errors.
   */
  private static void validateComments(List comments, ActionMessages errors) {
    assert comments != null : "Parameter 'comments' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    removeEmptyCommentItems(comments);
    removeDuplicates(comments);
    checkForPeriod(comments, errors);
  }

  private static void removeEmptyCommentItems(List comments) {
    for (int iii = 0; iii < comments.size(); iii++) {
      CommentDTO commentDTO = (CommentDTO) comments.get(iii);
      if (commentDTO.getXmlComment() == null || commentDTO.getXmlComment().equals("")) {
        comments.remove(iii);
        iii--;
      }
    }
  }

  private static void checkForPeriod(List list, ActionMessages errors) {
    for (int iii = 0; iii < list.size(); iii++) {
      CommentDTO commentDTO = (CommentDTO) list.get(iii);
      if (!commentDTO.getXmlComment().endsWith(".")) {
        ActionMessage error = new ActionMessage("errors.form.comment.missing_period", "" + (iii + 1));
        errors.add("comments", error);
        return;
      }
    }
  }

  /**
   * Checks the list of links for valid values.
   * <p/>
   * The following links are being checked:
   * <ul>
   * <li>CAS (value must exist)</li>
   * <li>MEROPS (valid hyperlink must exist)</li>
   * <li>OMIM (values for name and MIM accession must exist)</li>
   * <li>PROSITE (PROSITE accession must exist)</li>
   * <li>DIAGRAM (a valid hyperlink and a value for the name must exist)</li>
   * </ul>
   *
   * @param links  List of links.
   * @param errors List for storing error errors.
   */
  private static void validateLinks(List links, String ec, ActionMessages errors) {
    assert links != null : "Parameter 'links' must not be null.";
    assert ec != null : "Parameter 'ec' must not be null.";
    assert errors != null : "Parameter 'errors' must not be null.";
    removeEmptyLinkItems(links);
    removeLinkDuplicates(links);
    try {
      updateStaticLinks(links, ec);
    } catch (DomainException e) {
      ActionMessage message = new ActionMessage(e.getMessageKey(), e.getProperty());
      errors.add("links", message);
    }
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.CAS.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("")
                || !validateCASRegistryNumberFormat(enzymeLinkDTO)) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidCasValue");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.MEROPS.getDisplayName())) {
        try {
          URL url = new URL(enzymeLinkDTO.getUrl());
          URLConnection connection = url.openConnection();
          connection.connect();
        } catch (IOException e) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidMeropsUrl");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.MIM.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("") ||
            enzymeLinkDTO.getName() == null || enzymeLinkDTO.getName().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.MimIncomplete");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.PROSITE.getDisplayName())) {
        if (enzymeLinkDTO.getAccession() == null || enzymeLinkDTO.getAccession().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidPrositeValue");
          errors.add("links", message);
        }
      }
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.DIAGRAM.getDisplayName())) {
        if (enzymeLinkDTO.getName() == null || enzymeLinkDTO.getName().equals("")) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramName");
          errors.add("links", message);
        }
        try {
          URL url = new URL(enzymeLinkDTO.getUrl());
          // Set up proxy TODO: TAKE THIS OUT OF HERE
          Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-proxy.ebi.ac.uk", 8080));
          HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
          if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
              ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramUrl");
              errors.add("links", message);        	  
          }
        } catch (IOException e) {
          ActionMessage message = new ActionMessage("errors.form.link.invalidDiagramUrl");
          errors.add("links", message);
        }
      }
    }
  }

  /**
   * All database links which name value is <code>NONE</code> will be removed.
   * <p/>
   * All the others will be kept and checked in the updating Action.
   *
   * @param links The list of links to be checked.
   */
  private static void removeEmptyLinkItems(List links) {
    assert links != null : "Parameter 'links' must not be null.";
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (enzymeLinkDTO.getDatabaseName().equals(XrefDatabaseConstant.UNDEF.getDisplayName())) {
        links.remove(iii);
        iii--;
      }
    }
  }

  /**
   * Removes duplicates from the list of links.
   *
   * @param links The list of links to be checked.
   */
  private static void removeLinkDuplicates(List links) {
    assert links != null : "Parameter 'links' must not be null.";
    Set databaseNames = new HashSet();
    Set checkedLinks = new HashSet();
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      if (databaseNames.contains(enzymeLinkDTO.getDatabaseName())) {
        // If the given name is a valid xref name and it is a unique xref then remove it from the list.
        if (XrefDatabaseConstant.isUniqueXrefDatabaseName(enzymeLinkDTO.getDatabaseName())) {
          links.remove(enzymeLinkDTO);
        } else { // Remove xref only if all xref values are the same.
          for (Iterator it = checkedLinks.iterator(); it.hasNext();) {
            EnzymeLinkDTO checkedLink = (EnzymeLinkDTO) it.next();
            if (checkedLink.equals(enzymeLinkDTO)) links.remove(enzymeLinkDTO);
          }
        }
      } else {
        databaseNames.add(enzymeLinkDTO.getDatabaseName());
        checkedLinks.add(enzymeLinkDTO);
      }
    }
  }

  private static void updateStaticLinks(List links, String ec) throws DomainException {
    assert links != null : "Parameter 'links' must not be null.";
    for (int iii = 0; iii < links.size(); iii++) {
      EnzymeLinkDTO enzymeLinkDTO = (EnzymeLinkDTO) links.get(iii);
      EnzymeLink staticLink = EnzymeLink.getStaticLink(XrefDatabaseConstant.valueOf(enzymeLinkDTO.getDatabaseCode()));
      if (staticLink != null) {
        enzymeLinkDTO.setDatabaseName(staticLink.getXrefDatabaseConstant().getDisplayName());
        enzymeLinkDTO.setName(staticLink.getName());
        enzymeLinkDTO.setUrl(staticLink.getFullUrl(ec));
      }
    }
  }

  public static boolean validateReferences(Object obj,
          ValidatorAction validatorAction,
          Field field,
          ActionMessages actionMessages,
          HttpServletRequest httpServletRequest) {
    assert obj != null : "Parameter 'references' must not be null.";
    List<ReferenceDTO> references = ((EnzymeDTO) obj).getReferences();
    removeEmptyReferenceItems(references);
    removeDuplicates(references);
      for (ReferenceDTO referenceDTO : references) {
        if (StringUtil.isNullOrEmpty(referenceDTO.getYear())) return false;
      }
    return true;
  }

  private static void removeEmptyReferenceItems(List references) {
    assert references != null : "Parameter 'references' must not be null.";
    for (int iii = 0; iii < references.size(); iii++) {
      ReferenceDTO referenceDTO = (ReferenceDTO) references.get(iii);
      if (referenceDTO.isEmpty()) {
        references.remove(iii);
        iii--;
      }
    }
  }

  public static boolean hasNoUnicode(String s){
	  for (int i = 0; i < s.length(); i++) {
		  int codePoint = s.codePointAt(i);
		  if (codePoint > 127) return false;
	  }
	  return true;
  }

}

