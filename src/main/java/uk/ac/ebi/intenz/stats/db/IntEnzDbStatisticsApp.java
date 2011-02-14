package uk.ac.ebi.intenz.stats.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.stats.IIntEnzStatistics;

/**
 * Standalone application to dump statistics from a database connection.
 * @author rafalcan
 * @since 1.1.0
 */
public class IntEnzDbStatisticsApp {
	
    private static final Logger LOGGER = Logger.getLogger(IntEnzDbStatisticsApp.class);                                                                                                                             
    
    /**
     * Gathers statistics and dumps them to stdout in CSV format.                                                                                                                                                 
     * @param args <ol>
     *      <li>database connection configuration (see
     *          {@link OracleDatabaseInstance})</li>
     *      <li>output directory for generated file(s)</li>
     * </ol>
     * @throws SQLException In case of problem closing the connection                                                                                                                                             
     */
    public static void main(String args[]) throws SQLException{                                                                                                                                                   
        Connection con = null;    
        try {
            con = OracleDatabaseInstance.getInstance(args[0]).getConnection();                                                                                                                                    
            File outputDir = new File(args[1]);
            IIntEnzStatistics statistics = new IntEnzDbStatistics(con);                                                                                                                                           
            
            Properties velocityProps = new Properties();
            velocityProps.load(IntEnzDbStatisticsApp.class.getClassLoader()
                    .getResourceAsStream("velocity.properties"));
            Velocity.init(velocityProps);
            VelocityContext context = new VelocityContext();
            context.put("statistics", statistics);
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());                                                                                                                                  
            context.put("date", date);
            output(context, "csv", outputDir);
        } catch (Exception e){        
            LOGGER.error("Unable to output statistics", e);                                                                                                                                                       
        } finally {
            if (con != null) con.close();                                                                                                                                                                         
        }
    }

    private static void output(VelocityContext context, String format, File outputDir)                                                                                                                            
    throws IOException {
        Template template = null; 
        try {
            template = Velocity.getTemplate(format + ".vm");
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Template not found", e);                                                                                                                                                                
            return;
        } catch (ParseErrorException e) {
            LOGGER.error("Error parsing the template", e);                                                                                                                                                        
            return;
        } catch (Exception e) {       
            LOGGER.error("Error getting velocity template", e);                                                                                                                                                   
            return;
        }
        File outputFile = new File(outputDir,
                "intenz-statistics_" + context.get("date") + "." + format);                                                                                                                                         
        Writer writer = null;     
        try {
            writer = new FileWriter(outputFile);
            template.merge(context, writer);
        } catch (IOException e) {     
            LOGGER.error("Unable to write file", e);                                                                                                                                                              
        } finally {
            if (writer != null) writer.close();                                                                                                                                                                   
        }
    }

}
