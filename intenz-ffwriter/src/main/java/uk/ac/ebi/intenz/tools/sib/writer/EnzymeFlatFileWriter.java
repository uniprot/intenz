package uk.ac.ebi.intenz.tools.sib.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference;
import uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeEntryImpl;
import uk.ac.ebi.interfaces.sptr.SPTRCrossReference;
import uk.ac.ebi.interfaces.sptr.SPTRException;

/**
 * This utility class provides methods for exporting enzyme data into the ENZYME flat file format.
 * <br/><br/>
 * <p/>
 * It adds the standard <code>enzyme.dat</code> header containing the current date
 * (<code>d-MMM-yyyy</code> format). This header looks as follows:
 * <p/>
 * <p/>
 * <code>
 * <b>
 * CC   -----------------------------------------------------------------------<br/>
 * CC<br/>
 * CC   ENZYME nomenclature database<br/>
 * CC<br/>
 * CC   -----------------------------------------------------------------------<br/>
 * CC   Release 21-Mar-2007<br/>
 * CC<br/>
 * CC   Amos Bairoch and Kristian Axelsen<br/>
 * CC   Swiss Institute of Bioinformatics (SIB)<br/>
 * CC   Centre Medical Universitaire (CMU)<br/>
 * CC   1, rue Michel Servet<br/>
 * CC   1211 Geneva 4<br/>
 * CC   Switzerland<br/>
 * CC<br/>
 * CC   Email: enzyme@isb-sib.ch<br/>
 * CC   Telephone: +41-22-379 50 50<br/>
 * CC   Fax: +41-22-379 58 58<br/>
 * CC<br/>
 * CC   WWW server: http://www.expasy.org/enzyme/<br/>
 * CC<br/>
 * CC   -----------------------------------------------------------------------<br/>
 * CC   This database is copyright from the Swiss Institute of Bioinformatics.<br/>
 * CC   There are  no restrictions  on  its use by any institutions as long as<br/>
 * CC   its content is in no way modified.<br/>
 * CC   -----------------------------------------------------------------------<br/>
 * //
 * </b>
 * </code>
 * <br/><br/>
 * <p/>
 * An example of an ENZYME entry is shown below:
 * <p/>
 * <p/>
 * <code>
 * <b>
 * ID   1.1.1.2<br/>
 * DE   Alcohol dehydrogenase (NADP+).<br/>
 * AN   Aldehyde reductase (NADPH).<br/>
 * CA   An alcohol + NADP(+) = an aldehyde + NADPH.<br/>
 * CF   Zinc.<br/>
 * CC   -!- Some members of this group oxidize only primary alcohols; others act<br/>
 * CC       also on secondary alcohols.<br/>
 * CC   -!- May be identical with EC 1.1.1.19, EC 1.1.1.33 and EC 1.1.1.55.<br/>
 * CC   -!- A-specific with respect to NADPH.<br/>
 * PR   PROSITE; PDOC00061;<br/>
 * DR   P35630, ADH1_ENTHI;  Q24857, ADH3_ENTHI;  O57380, ADH4_RANPE;<br/>
 * DR   P25984, ADH_CLOBE ;  P75214, ADH_MYCPN ;  P31975, ADH_MYCTU ;<br/>
 * DR   P14941, ADH_THEBR ;  O70473, AKA1_CRIGR;  P14550, AKA1_HUMAN;<br/>
 * DR   Q9JII6, AKA1_MOUSE;  P50578, AKA1_PIG  ;  P51635, AKA1_RAT  ;<br/>
 * DR   Q9UUN9, ALD2_SPOSA;  P27800, ALDX_SPOSA;<br/>
 * //<br/>
 * </b>
 * </code>
 * <br/><br/>
 * The creation of this file follows the rules defined <a href="http://ca.expasy.org/cgi-bin/lists?enzuser.txt">here</a>.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2008/04/01 14:26:09 $
 */
public class EnzymeFlatFileWriter {

  private static final Logger LOGGER = Logger.getLogger(EnzymeFlatFileWriter.class);
  
  /** EC numbers of entries which have been transferred more than once,
   * or to more than one entry, or deleted but actually transferred,
   * and whose DE line must be changed to match the enzyme.dat entry. */
//  public static final List MULTI_TRANSFER_ENTRIES = new ArrayList();
  public static final Properties DE_MODIFIED_ENTRIES = new Properties();

  static {
	  InputStream stream;
	  stream = EnzymeFlatFileWriter.class.getClassLoader().getResourceAsStream("DE_line_modifications.properties");
	  try {
		  DE_MODIFIED_ENTRIES.load(stream);
	  } catch (IOException e) {
		  e.printStackTrace();
	  } finally {
		  try {
			  if (stream != null) stream.close();
		  } catch (IOException e){}
	  }
  }

  /**
   * Used for lazy initialisation of the flat file header.
   */
  private static String header = "";

  /**
   * This class should never be instantiated nor subclassed.
   */
  private EnzymeFlatFileWriter() {
  }

   /**
    * Gives single enzyme view (used in webapp tools).
    * @param enzyme
    * @return
    * @throws SPTRException
    */
  public static String export(EnzymeEntryImpl enzyme) throws SPTRException {
    if (enzyme == null) throw new NullPointerException("Parameter 'enzyme' must not be null.");
    StringWriter sw = new StringWriter();
    try {
        exportAndWrite(enzyme, sw);
    } catch (IOException e) {
        throw new EnzymeFlatFileWriteException(e);
    }
    return sw.toString();
  }

  /**
   * Exports the given collection of enzymes into the ENZYME flat file format using the given file.
   *
   * @param enzymes    The collection of {@link EnzymeEntry EnzymeEntry} instances.
   * @param version    Release version.
   * @param outputFile The file storing the enzyme information.
   * @return the time elapsed during this process.
   * @throws NullPointerException     if any of the given parameters is <code>null</code>
   * @throws IllegalArgumentException if <code>version</code> does not match the following regular expression pattern:
   *                                  <code>\d+\.\d+</code>.
   * @throws SPTRException            if an error occured during the export process.
   */
  public static long export(Collection enzymes, String version, File outputFile) throws SPTRException {
    if (enzymes == null) throw new NullPointerException("Parameter enzymes == null not allowed here.");
    if (version == null) throw new NullPointerException("Parameter version == null not allowed here.");
//    if (!version.matches("\\d+?\\.\\d+?"))   todo: do something about version number
//     if(!version.matches("\\d+?"))
//      throw new IllegalArgumentException("Parameter version must match the following regular expression: \\d+\\.\\d+");
    if (outputFile == null) throw new NullPointerException("Parameter outputFile == null not allowed here.");

    FileWriter fileWriter = null;
    long start = System.currentTimeMillis();

    try {
      fileWriter = new FileWriter(outputFile);
      fileWriter.write(getHeader(version));

      Iterator enzymeIterator = enzymes.iterator();

      while (enzymeIterator.hasNext()) {
        EnzymeEntryImpl enzymeEntry = (EnzymeEntryImpl) enzymeIterator.next();
        exportAndWrite(enzymeEntry, fileWriter);
      }
    } catch (UnsupportedOperationException e) {
      throw new EnzymeFlatFileWriteException(e);
    } catch (IOException e) {
      throw new EnzymeFlatFileWriteException(e);
    } finally {
      try {
        if (fileWriter != null) fileWriter.close();
      } catch (IOException e) {
        throw new EnzymeFlatFileWriteException(e);
      }
    }

    return System.currentTimeMillis() - start;
  }

    /**
     * @param enzymeEntry
     * @param writer
     * @throws SPTRException
     * @throws IOException
     */
    private static void exportAndWrite(EnzymeEntryImpl enzymeEntry, Writer writer)
        throws SPTRException, IOException {
        Map enzymeCrossReferences = getGroupedEnzymeCrossReferences(enzymeEntry.getCrossReferences());        
        // 0 - EC (ID)
        writer.write(createIDLine(enzymeEntry.getEC()));
        if (!enzymeEntry.isTransferredOrDeleted()) {
            // 1 - common name (DE)
          writer.write(createDELine(enzymeEntry.getCommonName(), true));
          // 2 - other names (AN)
          writer.write(createANLines(enzymeEntry.getSynonyms(), true));
          // 3 - reaction(s) (CA)
          writer.write(createCALines(enzymeEntry.getReactions(), true));
          // 4 - cofactors (CF)
          writer.write(createCFLines(enzymeEntry.getCofactors(), true));
          // 5 - comments (CC)
          writer.write(createCCLines(enzymeEntry.getComment(), true));
          // 6 - MIM links (DI)
          writer.write(createDILines((ArrayList) enzymeCrossReferences.get(EnzymeCrossReference.MIM)));
          // 7 - PROSITE links (PR)
          writer.write(createPRLines((ArrayList) enzymeCrossReferences.get(EnzymeCrossReference.PROSITE)));
          // 8 - SWISSPROT links (DR)
          writer.write(createDRLines((ArrayList) enzymeCrossReferences.get(EnzymeCrossReference.SWISSPROT)));
        } else {
            // Fix for transferred entries:
//            String deLine = MULTI_TRANSFER_ENTRIES.contains(enzymeEntry.getEC()) ?
//                    XCharsASCIITranslator.getInstance().toASCII(enzymeEntry.getCommonName(), false, true) :
//                    enzymeEntry.getCommonName();
            String deLine = DE_MODIFIED_ENTRIES.containsKey(enzymeEntry.getEC())?
        		DE_MODIFIED_ENTRIES.getProperty(enzymeEntry.getEC()):
        		enzymeEntry.getCommonName();
            writer.write(createDELine(deLine, true));
        }
        writer.write("//\n");
        writer.flush();
    }

  /**
   * Exports the given collection of enzymes into the ENZYME flat file format using the given directory and file name
   * to create the file.
   *
   * @param enzymes  The collection of {@link EnzymeEntry EnzymeEntry} instances.
   * @param version  Release version.
   * @param dir      A valid directory where the file should be stored. If this value is either <code>null</code> or empty the file
   *                 will be stored in the current working directory.
   * @param fileName Name of the file which stores the enzyme information. If this value is either <code>null</code> or
   *                 empty then the file will be named <code>enzyme.dat</code>.
   * @return the time elapsed during this process.
   * @throws NullPointerException     if <code>enzymes</code> or <code>version</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>version</code> does not match the following regular expression pattern:
   *                                  <code>\d+\.\d+</code> and if <code>dir</code> is not an existing directory.
   * @throws SPTRException            if an error occured during the export process.
   */
  public static long export(Collection enzymes, String version, String dir, String fileName) throws SPTRException {
    if (enzymes == null) throw new NullPointerException("Parameter enzymes == null not allowed here.");
    if (version == null) throw new NullPointerException("Parameter version == null not allowed here.");
//    if (!version.matches("\\d+\\.\\d+"))    todo: fix this
//      throw new IllegalArgumentException("Parameter version must match the following regular expression: \\d+\\.\\d+");

    // Check file parameters and create file instance.
    StringBuffer pathname = new StringBuffer();
    if (dir != null && !dir.equals("")) {
      File dirTest = new File(dir);
      if (!dirTest.isDirectory()) throw new IllegalArgumentException("Parameter dir is not a valid pathname");
      pathname.append(dir);
      if (!dir.endsWith("/") && !dir.endsWith("\\")) pathname.append(File.separator);
    }
    if (fileName == null || fileName.equals(""))
      pathname.append("enzyme.dat");
    else
      pathname.append(fileName);
    File outputFile = new File(pathname.toString());

    return export(enzymes, version, outputFile);
  }


  // ------------- PRIVATE METHODS ----------------------------

   /**
    * Sort lines in alphabetical order where necessary
    * @param synonyms
    * @return
    * @throws SPTRException
    */
   private static String[] sortLines (String[] synonyms) throws SPTRException {
      List synonymListToSort = Arrays.asList(synonyms);
      Collections.sort(synonymListToSort);
      return (String[]) synonymListToSort.toArray();
   }

  /**
   * Creates the ID line.
   * <p/>
   * If <code>ec == null</code> or <code>ec.equals("")</code> an Exception will be thrown because the ID line is a
   * mandatory line tytpe.
   *
   * @param ec The ID line only contains the EC number.
   * @return the ID line.
   * @throws SPTRException if the <code>ec</code> does not contain a valid EC.
   */
  private static String createIDLine(String ec) throws SPTRException {
    assert ec != null : ec;
    if (!isValidEc(ec)) throw new EnzymeFlatFileWriteException("Parameter ec does not contain a valid ec.");

    LOGGER.debug("EC: " + ec);

    StringBuffer IDContent = new StringBuffer();
    IDContent.append("ID   ");
    IDContent.append(ec);
    IDContent.append("\n");

    return IDContent.toString();
  }

  /**
   * Creates the DE line.
   * <p/>
   * If <code>commonName == null</code> or <code>commonName.equals("")</code> an Exception will be thrown because
   * the DE line is a mandatory line type.
   *
   * @param commonName The common name usually fills the DE line but could also show history information in terms or
   *                   transferred or deleted entries.
   * @param wrapLines
   * @return the DE line.
   * @throws SPTRException if no output could be created for this DE line.
   */
  private static String createDELine(String commonName, boolean wrapLines) throws SPTRException {
    assert commonName != null : commonName;

//    if (commonName.equals("")) throw new EnzymeFlatFileWriteException("The DE line must not contain an empty string.");

    StringBuffer DEContent = new StringBuffer();
    if (wrapLines) commonName = insertLineBreaks(commonName, LineType.DE);
    DEContent.append(commonName);
    DEContent.append("\n");

    return DEContent.toString();
  }

  /**
   * Creates the AN line by using one line per alternative name.
   * <p/>
   * At the moment the AN lines do not follow a particular order.
   *
   * @param synonyms  All synonyms of this enzyme arranged in an array.
   * @param wrapLines
   * @return the AN line(s) or an empty string if <code>synonyms</code> was either <code>null</code> or empty.
   * @throws SPTRException if no output could be created for an AN line.
   */
  private static String createANLines(String[] synonyms, boolean wrapLines) throws SPTRException {
    assert synonyms != null : synonyms;

    StringBuffer ANContent = new StringBuffer();
    for (int iii = 0; iii < synonyms.length; iii++) {
      String synonym = synonyms[iii];
      if (synonym.equals("") || synonym.equals("-")) continue;
      if (wrapLines) synonym = insertLineBreaks(synonyms[iii], LineType.AN);
      ANContent.append(synonym);
      ANContent.append("\n");
    }

    return ANContent.toString();
  }

  /**
   * Creates the CA line by using one line per reaction.
   *
   * @param reactions All reactions of this enzyme arranged in an array.
   * @param wrapLines
   * @return the CA line(s) or an empty string if <code>reactions</code> was either <code>null</code> or empty.
   * @throws SPTRException if no output could be created for an CA line.
   */
  private static String createCALines(String[] reactions, boolean wrapLines) throws SPTRException {
    assert reactions != null : reactions;

    StringBuffer CAContent = new StringBuffer();
    for (int iii = 0; iii < reactions.length; iii++) {
      if (reactions[iii].equals("")) continue;
      // Removing period at the end of a descriptive reaction.
      StringBuffer reaction = null;
      if (reactions[iii].charAt(reactions[iii].length() - 1) == '.')
        reaction = new StringBuffer(reactions[iii].substring(0, reactions[iii].length() - 1));
      else
        reaction = new StringBuffer(reactions[iii]);
      if (reactions.length > 1) {
        StringBuffer number = new StringBuffer("(");
        number.append(iii + 1);
        number.append(") ");
        reaction.insert(0, number.toString());
      }
      if (wrapLines)
        CAContent.append(insertLineBreaks(reaction.toString(), LineType.CA));
      else
        CAContent.append(reaction.toString());
      CAContent.append("\n");
    }

    return CAContent.toString();
  }

  /**
   * Creates the CF line.
   *
   * @param cofactors The cofactor string of this enzyme.
   * @param wrapLines
   * @return the CF line or an empty string if <code>cofactors</code> was either <code>null</code> or empty.
   * @throws SPTRException if no output could be created for an CF line.
   */
  private static String createCFLines(String cofactors, boolean wrapLines) throws SPTRException {
    assert cofactors != null : cofactors;

    if (cofactors.equals("")) return cofactors;

    StringBuffer CFContent = new StringBuffer();
    if (wrapLines) cofactors = insertLineBreaks(cofactors, LineType.CF);
    CFContent.append(cofactors);
    CFContent.append("\n");

    return CFContent.toString();
  }

  /**
   * Creates the CC line(s).
   * <p/>
   * Each sentence starts with <code>CC   -!-</code> and ends with a period.
   *
   * @param comment   The comment of this enzyme.
   * @param wrapLines
   * @return The CC line(s) or an empty string if <code>comment</code> was either <code>null</code> or empty.
   * @throws SPTRException if no output could be created for an CC line.
   */
  private static String createCCLines(String comment, boolean wrapLines) throws SPTRException {
    assert comment != null : comment;
    if (comment.equals("")) return comment;
    if (wrapLines) comment = insertLineBreaks(comment, LineType.CC);
    return comment;
  }

  /**
   * Creates the DI line by simply creating one line per MIM cross reference.
   *
   * @param DICrossReferences All ENZYME cross references of this enzyme to the MIM database.
   * @return the DI line(s) or an empty string if <code>DICrossReferences</code> was either <code>null</code> or empty.
   */
  private static String createDILines(ArrayList DICrossReferences) {
    assert DICrossReferences != null : DICrossReferences;

    StringBuffer DIContent = new StringBuffer();
    for (int iii = 0; iii < DICrossReferences.size(); iii++) {
      EnzymeCrossReference enzymeCrossReference = (EnzymeCrossReference) DICrossReferences.get(iii);
      DIContent.append("DI   ");
      DIContent.append(enzymeCrossReference.getPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION));
      DIContent.append("; MIM:");
      DIContent.append(enzymeCrossReference.getAccessionNumber());
      DIContent.append(".\n");
    }

    return DIContent.toString();
  }

  /**
   * Creates the PR line by simply creating one line per PROSITE cross reference.
   *
   * @param PRCrossReferences All ENZYME cross references of this enzyme to the PROSITE database.
   * @return the PR line(s) or an empty string if <code>PRCrossReferences</code> was either <code>null</code> or empty.
   */
  private static String createPRLines(ArrayList PRCrossReferences) {
    assert PRCrossReferences != null : PRCrossReferences;

    StringBuffer PRContent = new StringBuffer();
    for (int iii = 0; iii < PRCrossReferences.size(); iii++) {
      EnzymeCrossReference enzymeCrossReference = (EnzymeCrossReference) PRCrossReferences.get(iii);
      PRContent.append("PR   ");
      PRContent.append(enzymeCrossReference.getDatabaseName());
      PRContent.append("; ");
      PRContent.append(enzymeCrossReference.getAccessionNumber());
      PRContent.append(";\n");
    }

    return PRContent.toString();
  }

  /**
   * Loads the information needed for the DR line(s).
   * <p/>
   * This method assumes that the DR elements are sorted by name.
   *
   * @param DRCrossReferences All ENZYME cross references of this enzyme to the SwissProt database.
   * @return the DR line(s) or an empty string if <code>DRCrossReferences</code> was either <code>null</code> or empty.
   */
  private static String createDRLines(ArrayList DRCrossReferences) {
    assert DRCrossReferences != null : DRCrossReferences;

    StringBuffer DRContent = new StringBuffer();
    int count = 0;
    for (int iii = 0; iii < DRCrossReferences.size(); iii++) {
      EnzymeCrossReference enzymeCrossReference = (EnzymeCrossReference) DRCrossReferences.get(iii);
      if (count == 0)
        DRContent.append("DR   ");
      DRContent.append(enzymeCrossReference.getAccessionNumber());
      DRContent.append(", ");
      DRContent.append(getNameWithLength11(enzymeCrossReference.getPropertyValue(EnzymeCrossReference.PROPERTY_DESCRIPTION)));
      DRContent.append(";  ");
      if (count == 2) {
        count = 0;
        DRContent.delete(DRContent.length() - 2, DRContent.length()); // Remove spaces after last semicolon.
        DRContent.append("\n");
        continue;
      }

      count++;
    }

    if (DRContent.length() > 1) {
      if (DRContent.charAt(DRContent.length() - 1) == ' ')
        DRContent.delete(DRContent.length() - 2, DRContent.length()); // Remove spaces after last semicolon.
      if (count > 0 && count < 3)
        DRContent.append("\n");
    }

    return DRContent.toString();
  }

  /**
   * Groups all <code>SPTRCrossReferences</code> into the three groups which occur in the <code>enzyme.dat</code> file.
   * <p/>
   * These groups are MIM, PROSITE and SwissProt xrefs.
   *
   * @param allCrossReferences
   * @return a <code>Hashtable</code> containing the thre groups. The keys are the
   *         {@link EnzymeCrossReference EnzymeCrossReference} constants.
   */
  private static Hashtable getGroupedEnzymeCrossReferences(SPTRCrossReference[] allCrossReferences) {
    Hashtable groupedCrossReferences = new Hashtable();

    ArrayList DICrossReferences = new ArrayList();
    ArrayList PRCrossReferences = new ArrayList();
    ArrayList DRCrossReferences = new ArrayList();

    for (int iii = 0; iii < allCrossReferences.length; iii++) {
      EnzymeCrossReference crossReference = (EnzymeCrossReference) allCrossReferences[iii];
      if (crossReference.getDatabaseName().equals(EnzymeCrossReference.MIM)) {
        DICrossReferences.add(crossReference);
      }
      if (crossReference.getDatabaseName().equals(EnzymeCrossReference.PROSITE)) {
        PRCrossReferences.add(crossReference);
      }
      if (crossReference.getDatabaseName().equals(EnzymeCrossReference.SWISSPROT)) {
        DRCrossReferences.add(crossReference);
      }
    }

    groupedCrossReferences.put(EnzymeCrossReference.MIM, DICrossReferences);
    groupedCrossReferences.put(EnzymeCrossReference.PROSITE, PRCrossReferences);
    groupedCrossReferences.put(EnzymeCrossReference.SWISSPROT, DRCrossReferences);

    return groupedCrossReferences;
  }

  /**
   * Returns the normalised (name length max. is 11 characters at the moment) SwissProt cross reference name.
   *
   * @param name The cross reference name.
   * @return the normalised cross reference name.
   */
  private static String getNameWithLength11(String name) {
    assert name != null : name;

    if (name.length() == 11) return name;
    StringBuffer name11 = new StringBuffer(name);
    for (int iii = name.length(); iii < 11; iii++) {
      name11.append(" ");
    }

    return name11.toString();
  }

  /**
   * Checks if the given EC is valid (syntax is correct).
   *
   * @param ecString The EC to be checked.
   * @return <code>true</code> if the EC is valid.
   */
  private static boolean isValidEc(String ecString) {
    assert ecString != null : ecString;

    int ec1 = 0, ec2 = 0, ec3 = 0, ec4 = 0;
    if (!Pattern.matches("(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)){0,1}){0,1}){0,1}", ecString)) return false;
    Pattern ecPattern = Pattern.compile("(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)){0,1}){0,1}){0,1}");
    Matcher ecMatcher = ecPattern.matcher(ecString);
    try {
      if (ecMatcher.find()) {
        ec1 = Integer.parseInt(ecMatcher.group(1));
        if (ec1 < 0) return false;
        if (ecMatcher.group(2) != null) {
          ec2 = Integer.parseInt(ecMatcher.group(2));
          if (ec2 < 0) return false;
          if (ecMatcher.group(3) != null) {
            ec3 = Integer.parseInt(ecMatcher.group(3));
            if (ec3 < 0) return false;
            if (ecMatcher.group(4) != null) {
              ec4 = Integer.parseInt(ecMatcher.group(4));
              if (ec4 < 0) return false;
            }
          }
        }
      }
    } catch (NumberFormatException e) {
      return false;
    }

    if (ec1 == 0) {
      if (ec2 > 0 || ec3 > 0 || ec4 > 0) return false;
    }
    if (ec2 == 0) {
      if (ec3 > 0 || ec4 > 0) return false;
    }
    if (ec3 == 0) {
      if (ec4 > 0) return false;
    }

    return true;
  }

  /**
   * Lazy initialisation of the class member <code>header</code> which represents the flat file header.
   * <p/>
   * It uses the given version number and adds the current data (month and year) automatically.
   *
   * @return the flat file header.
   */
  private static String getHeader(String version) {
    assert version != null : version;

    if (header.equals("")) {
      StringBuffer enzymeHeader = new StringBuffer();
      Date today = new Date();
//      SimpleDateFormat formatter = new SimpleDateFormat("MMMMM yyyy");
      SimpleDateFormat formatter = new SimpleDateFormat("d-MMM-yyyy");

      enzymeHeader.append("CC   -----------------------------------------------------------------------\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   ENZYME nomenclature database\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   -----------------------------------------------------------------------\n");
      enzymeHeader.append("CC   Release ");
//      enzymeHeader.append(version);
//      enzymeHeader.append(" of ");
      enzymeHeader.append(formatter.format(today));
      enzymeHeader.append("\n");
      enzymeHeader.append("CC   -----------------------------------------------------------------------\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   Amos Bairoch and Kristian Axelsen\n");
      enzymeHeader.append("CC   Swiss Institute of Bioinformatics (SIB)\n");
      enzymeHeader.append("CC   Centre Medical Universitaire (CMU)\n");
      enzymeHeader.append("CC   1, rue Michel Servet\n");
      enzymeHeader.append("CC   1211 Geneva 4\n");
      enzymeHeader.append("CC   Switzerland\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   Email: enzyme@isb-sib.ch\n");
      enzymeHeader.append("CC   Telephone: +41-22-379 50 50\n");
      enzymeHeader.append("CC   Fax: +41-22-379 58 58\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   WWW server: http://www.expasy.org/enzyme/\n");
      enzymeHeader.append("CC\n");
      enzymeHeader.append("CC   -----------------------------------------------------------------------\n");
      enzymeHeader.append("CC   This database is copyright from the Swiss Institute of Bioinformatics.\n");
      enzymeHeader.append("CC   There are  no restrictions  on  its use by any institutions as long as\n");
      enzymeHeader.append("CC   its content is in no way modified.\n");
      enzymeHeader.append("CC   -----------------------------------------------------------------------\n");
      enzymeHeader.append("//\n");
      header = enzymeHeader.toString();
    }

    return header;
  }

  /**
   * Inserts line breaks for the given line type if the given text is longer than
   * {@link LineFormatter#LINEWIDTH 78} characters.
   *
   * @param nonWrappedText The text following the line type.
   * @param lineType       The line type of the line to be wrapped.
   * @return the wrapped line including the line type information.
   * @throws SPTRException        if an error occurred during the line breaking process.
   * @throws NullPointerException if any of the parameters is <code>null</code>.
   */
  public static String insertLineBreaks(String nonWrappedText, LineType lineType) throws SPTRException {
    if (nonWrappedText == null) throw new NullPointerException("Parameter nonWrappedText == null not allowed here.");
    if (lineType == null) throw new NullPointerException("Parameter lineType == null not allowed here.");

    LineFormatter lineWrapper;

    // CC lines are treated differently.
    if (lineType == LineType.CC) {
      lineWrapper = new CC_LineFormatter();
    } else {
      lineWrapper = new DefaultLineFormatter();
    }

    return lineWrapper.formatLines(nonWrappedText, lineType);
  }
}
