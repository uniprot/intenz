package uk.ac.ebi.intenz.tools.importer;

import java.io.IOException;
import java.util.Properties;

/**
 * This interface defines the imported for all data.
 *
 * @author local_admin
 * @version $id 31-May-2005 13:46:10
 *          History:
 *          Developer    Date    Description
 *          local_admin      31-May-2005 Created class
 */
public abstract class Importer {

	protected Properties importerProps;

	protected Importer() throws IOException{
		importerProps = new Properties();
		importerProps.load(this.getClass().getClassLoader()
				.getResourceAsStream("Importer.properties"));
	}

	/**
	 * Using the template pattern to do the import.
	 *
	 * @throws Exception
	 */
	public final void doImport () throws Exception {
		try {
			setup();
			importData();
			loadData();
		} finally {
			destroy();
		}
	}

	/**
	 * Used to setup all network connections.
	 */
	protected abstract void setup () throws Exception;


	/**
	 * Used to import the data from the source
	 */
	protected abstract void importData () throws Exception;

	/**
	 * Method called after the data has been imported succesfully.
	 */
	protected abstract void loadData () throws Exception;

	/**
	 * Method used to close all loose ends.
	 */
	protected abstract void destroy ();

}
