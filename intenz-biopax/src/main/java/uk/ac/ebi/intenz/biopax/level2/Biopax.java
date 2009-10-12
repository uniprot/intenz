package uk.ac.ebi.intenz.biopax.level2;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.biopax.paxtools.model.Model;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;

/**
 * Utility class to dump IntEnz entries as BioPAX model.
 * @author rafalcan
 */
public class Biopax extends uk.ac.ebi.rhea.biopax.level2.Biopax {

    private static final Logger LOGGER = Logger.getLogger(Biopax.class);

    public static final String INTENZ_PREFIX = "intenz";
    public static final String INTENZ_NS = "http://www.ebi.ac.uk/intenz/#";

	/**
	 * Creates an empty ontology model with the needed namespaces.
	 * @return an OWL model.
     *
	 */
    public static Model createModel(){
		Model biopaxModel = uk.ac.ebi.rhea.biopax.level2.Biopax.createModel();
		biopaxModel.getNameSpacePrefixMap().put("", INTENZ_NS);
		biopaxModel.getNameSpacePrefixMap().put(INTENZ_PREFIX, INTENZ_NS);
		return biopaxModel;
	}

    /**
     * Writes a collection of enzyme entries to an output stream.
     * @param enzymes
     * @param os
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public static void write(Collection<EnzymeEntry> enzymes, OutputStream os)
    throws IOException, IllegalAccessException, InvocationTargetException {
        Model model = Biopax.createModel();
    	for (EnzymeEntry entry : enzymes) {
            try {
                new BiopaxCatalysis(entry, model);
                LOGGER.info("Added to BioPAX model - EC " + entry.getEc());
            } catch(Exception e){
                LOGGER.error("Unable to convert to BioPAX - EC " + entry.getEc(), e);
            }
        }
        write(model, os);
    }
}
