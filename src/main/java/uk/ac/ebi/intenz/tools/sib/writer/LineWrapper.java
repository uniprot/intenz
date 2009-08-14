package uk.ac.ebi.intenz.tools.sib.writer;

/**
 * Defines a method to retrieve the next line wrapping position.
 * <p/>
 * Unfortunately the line wrapping "rules" applied manually are sometimes following the sole rule:
 * <p/>
 * <center><b><code>if (looks nice) then wrap();</code></b></center>
 * <p/>which makes it impossible to create an identical flat file in an
 * automated way.<br/>
 * Therefore the wrapping classes implementing this interface should try to cover all manual line wrapping rules which
 * do not follow this rule.
 * <p>
 * The line wrapping rules are defined in the following PDF file:<p/>
 * &nbsp;&nbsp;&nbsp;&bull;&nbsp;&nbsp;<a href="../../../../../../../pdf/enzyme_LW_rules.pdf">enzyme_LW_rules.pdf</a> (preliminary version,
 * i.e. not approved by Amos)
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public interface LineWrapper {
  /**
   * Returns the next position in the line where the wrapping should be performed.
   * <p/>
   * This method is supposed to be used by a {@link LineFormatter LineFormatter} implementation within
   * a loop as long as the text does not fit into one line.
   *  
   * @param text The text to be wrapped.
   * @param netLineWidth Net line width (w/o line header in the begining).
   * @return athe line break position.
   * @throws EnzymeFlatFileWriteException if an error occured during this process.
   */
  int findPosition(String text, int netLineWidth) throws EnzymeFlatFileWriteException;
}
