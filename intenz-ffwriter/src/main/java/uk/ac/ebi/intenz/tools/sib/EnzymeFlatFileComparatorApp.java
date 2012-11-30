package uk.ac.ebi.intenz.tools.sib;

import uk.ac.ebi.intenz.tools.sib.comparator.FlatFileComparator;
import uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter;

import java.io.File;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

/**
 * This class provides methods for exporting enzyme data into the ENZYME flat file format.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 11:43:22 $
 */
public class EnzymeFlatFileComparatorApp {

  private static final Logger LOGGER =
	  Logger.getLogger(EnzymeFlatFileComparatorApp.class.getName());

  /**
   * Compares the content of two ENZYME flat files.
   *
   * @param args Command line arguments (see {@link EnzymeFlatFileWriterApp#printHelp()} .
   */
  public static void main(String[] args) {
//    if (!argumentsOk(args)) {
//      printHelp();
//      System.exit(1);
//    }
    LOGGER.debug("Running comparator");
    File flatFile1 = new File(ApplicationResources.getInstance().getInputFlatFileName());
    File flatFile2 = new File(ApplicationResources.getInstance().getExportFlatFileName());
    LOGGER.debug("Enzyme.dat input file found at: "+ApplicationResources.getInstance().getInputFlatFileName());

    FlatFileComparator.compare(flatFile1, flatFile2);
  }


  // --------------------------- PRIVATE METHODS ------------------------------------------------

  /**
   * Checks the arguments taken from the command line.
   * <p/>
   * For further info see {@link EnzymeFlatFileComparatorApp#main(String[])}.
   *
   * @param args The arguments to be checked.
   * @return <code>true</code> if the arguments are ok.
   */
  private static boolean argumentsOk(String[] args) {
    if (args.length != 2) {
      System.err.println("Please provide the two file names (incl. abs. path info) of the flat files to be compared.");
      return false;
    }
    return true;
  }

  /**
   * Prints usage information of this class to the console.
   */
  private static void printHelp() {
    StringBuffer help = new StringBuffer();
    help.append("Usage: java EnzymeFlatFileComparatorApp <flat file 1> <flat file 2>\n");
    help.append("Examples: java EnzymeFlatFileComparatorApp -v 33.0\n");
    help.append("          java EnzymeFlatFileComparatorApp -v 33.12\n");
    System.out.println(help.toString());
  }

}
