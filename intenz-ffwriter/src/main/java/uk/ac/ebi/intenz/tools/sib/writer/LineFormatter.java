package uk.ac.ebi.intenz.tools.sib.writer;

/**
 * This interface provides a method to format lines according to the rules of the <code>enzyme.dat</code> file.
 * <p/>
 * Some of the rules can be found <a target="_blank" href="http://ca.expasy.org/cgi-bin/lists?enzuser.txt">here</a>.
 * <p/>
 * The formatting of lines is dependent on the {@link LineType line type} and the content of each line type. A
 * <code>LineFormatter</code> implementation will provide a means to format and wrap the lines of a
 * {@link LineType line type} by implementing {@link LineFormatter#formatLines(String, LineType)
 * formatLines(String, LineType)}.
 * <p/>
 * As rules for wrapping lines are often applicable to several {@link LineType line types}, the wrapping is defined by
 * the interface {@link LineWrapper LineWrapper}, which defines the implementation of line wrapping rules. These rules
 * should be used within a <code>LineFormatter</code> concrete class.
 * <p/>
 * Apart from the correct line wrapping the <code>LineFormatter</code> must take care of proper formatting in the
 * beginning of the line, i.e. line headers.<br/>
 * For example, in case of the <code><b>CC</b></code> line type the
 * <code>LineFormatter</code> must format each line beginning with <b><code>&gt;CC   &lt;</code></b> plus an additional
 * <code><b>&gt;-!- &lt;</b></code> to indicate the beginning of a sentence or spaces, if the line is a continuation of
 * a sentence.
 * <p/>
 * <b>Example:</b>
 * <p/>
 * <img src="../../../../../../../images/example_formatter.gif">
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public interface LineFormatter {

  /** Defines the maximum line width within the flat file. **/
  static final int LINEWIDTH = 78;

  /**
   * This method takes care of proper formatting of the line(s) corresponding one {@link LineType line type} as
   * described in the class description.
   *
   * @param text The whole text of one line type.
   * @param lineType The {@link LineType line type}.
   * @return the formatted lines.
   * @throws EnzymeFlatFileWriteException if an error occured during the formatting process.
   */
  String formatLines(String text, LineType lineType) throws EnzymeFlatFileWriteException;
}
