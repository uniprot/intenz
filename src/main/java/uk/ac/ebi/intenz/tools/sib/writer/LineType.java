package uk.ac.ebi.intenz.tools.sib.writer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an enumeration of all ENZYME line types.
 * <p/>
 * A comprehensive description of all line types can be found
 * <a href="http://enzyme.expasy.org/enzuser.txt">here</a>.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class LineType {
  /**
   * Stores the line type code.
   */
  private final String lineTypeCode;

  public static final LineType ID = new LineType("ID");
  public static final LineType DE = new LineType("DE");
  public static final LineType AN = new LineType("AN");
  public static final LineType CA = new LineType("CA");
  public static final LineType CF = new LineType("CF");
  public static final LineType CC = new LineType("CC");
  public static final LineType DI = new LineType("DI");
  public static final LineType PR = new LineType("PR");
  public static final LineType DR = new LineType("DR");

  /**
   * Array of all line types.
   */
  private static final LineType[] PRIVATE_LINE_TYPES = {ID, DE, AN, CA, CF, CC, DI, PR, DR};

  /**
   * An unmodifiable list of all line types.
   */
  public static final List LINE_TYPES = Collections.unmodifiableList(Arrays.asList(PRIVATE_LINE_TYPES));

  /**
   * Object cannot be created outside this class.
   *
   * @param lineTypeCode The line type code.
   */
  private LineType(String lineTypeCode) {
    this.lineTypeCode = lineTypeCode;
  }

  /**
   * Returns the line type code.
   *
   * @return the line type code.
   */
  public String toString() {
    return lineTypeCode;
  }
}
