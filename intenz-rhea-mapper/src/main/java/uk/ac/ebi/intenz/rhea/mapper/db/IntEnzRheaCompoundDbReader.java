package uk.ac.ebi.intenz.rhea.mapper.db;

import java.io.IOException;
import java.sql.Connection;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;

/**
 * created to access to rhea from intenz after db split - see UES-12 for
 * motivations, UES-14 for specific sql queries
 *
 */
public class IntEnzRheaCompoundDbReader extends RheaCompoundDbReader {

    private final SQLLoader sqlLoader;

    public IntEnzRheaCompoundDbReader(Connection con, SQLLoader loader) throws IOException {
        super(con);

        this.sqlLoader = loader;

    }

    public SQLLoader getSqlLoader() {
        return sqlLoader;
    }

    public Connection getConnection() throws IOException {
        return SQLLoader.getConnection();
    }

}