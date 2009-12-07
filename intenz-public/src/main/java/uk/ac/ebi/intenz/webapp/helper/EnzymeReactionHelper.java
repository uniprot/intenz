package uk.ac.ebi.intenz.webapp.helper;

import uk.ac.ebi.intenz.webapp.IntEnzUtilities;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;


/**
 * This class ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2009/04/16 15:02:01 $
 * @deprecated to hell with this and EnzymeEntryHelper
 */
public class EnzymeReactionHelper {

//  private static final Logger LOGGER = Logger.getLogger(EnzymeClassHelper.class);

  private EnzymeReactionHelper() {
  }

  public static String textualRepresentationToHTML(Reaction reaction,
          SpecialCharacters encoding, EncodingType encodingType) {
    return encoding.xml2Display(
            IntEnzUtilities.cleanInternalLinks(reaction.getTextualRepresentation(), false),
            encodingType);
  }

}
