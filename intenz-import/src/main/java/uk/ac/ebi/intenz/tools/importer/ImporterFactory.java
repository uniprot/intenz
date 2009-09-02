package uk.ac.ebi.intenz.tools.importer;

import org.apache.log4j.Logger;

/**
 * This class, is used to create instances of the importers.
 *
 * @author pmatos
 * @version $id 31-May-2005 13:43:45
 *          History:
 *          Developer       Date        Description
 *          pmatos      31-May-2005 Created class
 *          rafalcan    06-Jan-2006 Added GoLinkImporter
 */
public class ImporterFactory {

    static Logger LOGGER = Logger.getLogger(ImporterFactory.class);

    public static final String GO_IMPORTER = GoLinkImporter.class.getName();

    public static final String KRAKEN_IMPORTER = KrakenLinkImporter.class.getName();

    public static Importer createImporter(String importer)
    throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        try {
            Class clazz = Class.forName(importer);
            return (Importer) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
           throw e;
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
           throw e;
        } catch (InstantiationException e) {
            LOGGER.error(e);
           throw e;
        }        
    }

}
