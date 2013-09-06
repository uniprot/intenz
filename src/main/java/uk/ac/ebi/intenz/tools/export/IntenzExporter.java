package uk.ac.ebi.intenz.tools.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;

/**
 * Interface to export IntEnz objects.
 * @author rafa
 * @since 1.3.18
 */
public interface IntenzExporter {

    /**
     * Exports a collection of enzymes.
     * @param enzymes the enzymes to export.
     * @param os the output stream to write the export to.
     * @throws IOException
     */
    void export(Collection<EnzymeEntry> enzymes, OutputStream os)
    throws IOException;

    /**
     * Exports just one enzyme.
     * @param enzyme the enzyme to export.
     * @param os the output stream to write the entry to.
     * @throws IOException
     */
    void export(EnzymeEntry enzyme, OutputStream os) throws IOException;
}
