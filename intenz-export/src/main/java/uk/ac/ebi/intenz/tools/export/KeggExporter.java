package uk.ac.ebi.intenz.tools.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

public class KeggExporter implements IntenzExporter {

	private static final Logger LOGGER = Logger.getLogger(KeggExporter.class);

	public KeggExporter() throws Exception {
		Properties velocityProps = new Properties();
		velocityProps.load(KeggExporter.class.getClassLoader()
				.getResourceAsStream("velocity.properties"));
        Velocity.init(velocityProps);
	}
	
	public void export(Collection<EnzymeEntry> enzymes, OutputStream os)
	throws IOException{
        VelocityContext context = new VelocityContext();
        context.put("nl", "\n");
        context.put("enzymes", enzymes);
        context.put("specialCharacters", SpecialCharacters.getInstance(null));
        context.put("spEncoding", EncodingType.SWISSPROT_CODE);
        context.put("intenzView", View.INTENZ);
        
        Template template = null;
        try {
			template = Velocity.getTemplate("templates/keggEnzyme.vm");
		} catch (ResourceNotFoundException e) {
			LOGGER.error("Template not found", e);
		} catch (ParseErrorException e) {
			LOGGER.error("Error parsing the template", e);
		} catch (Exception e) {
			LOGGER.error("Error getting velocity template", e);
		}
		if (template == null) return;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(os);
            template.merge(context, osw);
            osw.flush();
        } catch (IOException ex) {
            LOGGER.error("Unable to write keggEnzyme", ex);
        } finally {
            if (osw != null) osw.close();
        }
	}

    public void export(EnzymeEntry enzyme, OutputStream os) throws IOException {
        export(Collections.singleton(enzyme), os);
    }
}
