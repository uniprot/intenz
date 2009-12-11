package uk.ac.ebi.intenz.webapp.helper;

import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * This class ... is crap
 * @deprecated
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:08 $
 */
public class EnzymeReactionHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeEntryHelper.class);

  private EnzymeReactionHelper() {}

  /**
   * @deprecated MVC sin
   * @param reaction
   * @param encoding
   * @param encodingType
   * @return
   */
  public static String textualRepresentationToHTML(Reaction reaction, SpecialCharacters encoding, EncodingType encodingType) {
    return encoding.xml2Display(IntEnzUtilities.linkMarkedEC(reaction.getTextualRepresentation(), false), encodingType);
  }
}
