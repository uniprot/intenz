package uk.ac.ebi.intenz.tools.sib.writer;

/**
 * Provides a default implementation which can be applied to all line types.
 * <p/>
 * The only exception is the <code><b>CC</b></code> line, which is an extension of this class.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:23 $
 */
public class DefaultLineFormatter implements LineFormatter {
  /**
   * Formats the text by adding the line headers and wrapping the content using {@link LineWrapper LineWrapper}
   * implementations.
   *
   * @param text The text to be formatted.
   * @param lineType The {@link LineType line type} .
   * @return the formatted text.
   * @throws EnzymeFlatFileWriteException if an error occured during the formatting process.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public String formatLines(String text, LineType lineType) throws EnzymeFlatFileWriteException {
    if (text == null || lineType == null) throw new NullPointerException();

    String lineStart = lineType.toString() + "   ";
    int netLineWidth = LINEWIDTH - lineStart.length();

    if(!text.equals("") &&
            (lineType == LineType.DE || lineType == LineType.AN || lineType == LineType.CA || 
            lineType == LineType.CF || lineType == LineType.DI) &&
            !text.endsWith(".")) text += '.'; // Take care of the period.

    StringBuffer wrappedText = new StringBuffer();

    // Check if the given text fits into one line.
    if (text.length() <= netLineWidth) {
      wrappedText.append(lineStart);
      wrappedText.append(text);
      return wrappedText.toString();
    }

    StringBuffer restText = new StringBuffer(text);
    LineWrapper lineWrapPositioner = LineWrapperFactory.create(text, lineType);
    int additionalIndent = 0;
    while (restText.toString().trim().length() > netLineWidth) {
      int position = lineWrapPositioner.findPosition(restText.toString().trim(), netLineWidth);
      wrappedText.append(lineStart);                                                    // Add the line type string.
      if (restText.length() > 0 && restText.charAt(0) == ' ') restText.deleteCharAt(0); // Ignore leading space.
      String line = restText.substring(0, position);                                    // Get the line.
      restText.delete(0, position);                                                     // Remove the line from the text.
      for (int iii = 0; iii < additionalIndent; iii++)                                  // Insert indent.
        wrappedText.append(" ");
      wrappedText.append(line);                                                         // Append line.
      wrappedText.append("\n");
    }

    // Append last line.
    wrappedText.append(lineStart);
    if (restText.length() > 0 && restText.charAt(0) == ' ') restText.deleteCharAt(0); // Ignore leading space.
    for (int iii = 0; iii < additionalIndent; iii++)
      wrappedText.append(" ");
    wrappedText.append(restText.toString());

    return wrappedText.toString();
  }
}
