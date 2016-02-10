package uk.ac.ebi.intenz.tools.release;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.DatabaseInstance;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.exceptions.DomainException;
import uk.ac.ebi.intenz.mapper.EnzymeClassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubSubclassMapper;
import uk.ac.ebi.intenz.mapper.EnzymeSubclassMapper;
import uk.ac.ebi.intenz.tools.release.helper.EnzymeClassHelper;
import uk.ac.ebi.intenz.tools.release.helper.EnzymeEntryHelper;
import uk.ac.ebi.intenz.tools.release.helper.EnzymeSubSubclassHelper;
import uk.ac.ebi.intenz.tools.release.helper.EnzymeSubclassHelper;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * This class creates populates the free text search table (intenz_text).
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/05/27 15:47:24 $
 */
public class IntEnzText {

	private static final Logger LOGGER = Logger.getLogger(IntEnzText.class);
	
  /**
   * SQL statement used for loading the list of all approved enzymes.
   * <p/>
   * Because of modified enzymes which still have the same EC number
   * (and are both approved) only the most up-to-date
   * version should be loaded. This is done by the sub-statement.
   */
  private static String FIND_ALL = new StringBuilder()
		  .append("SELECT enzyme_id, ec1, ec2, ec3, ec4,")
		  .append(" history, note, status, source, active")
		  .append(" FROM enzyme.enzymes")
  		  .append(" WHERE status IN ('OK','PR','PM') AND source = 'INTENZ'")
  		  .append(" AND enzyme_id NOT IN")
  		  .append(" (SELECT before_id FROM enzyme.history_events")
  		  .append(" WHERE event_class = 'MOD')")
  		  .append(" ORDER BY ec1, ec2, ec3, ec4")
  		  .toString();

    private static String DELETE_ALL = "DELETE FROM intenz_text";

    private static String INSERT = new StringBuilder()
    	.append("INSERT INTO intenz_text (enzyme_id, ec, common_name,")
    	.append(" status, source, text_order, text)")
    	.append(" VALUES ( ?, ?, ?, ?, ?, ?, ? )")
    	.toString();
    
  /** 
   * Refreshes the intenz_text table.<br/>
   * The first and only parameter is the database instance to be updated.
   * @param args 
   */
  public static void main(String[] args) {
    final SpecialCharacters encoding = SpecialCharacters.getInstance(null);

      if (args.length == 0){
          LOGGER.error("IntEnzText needs one parameter");
          System.exit(1);
      }

      String instanceName = args[0];
      DatabaseInstance instance = null;
      try {
          instance = OracleDatabaseInstance.getInstance(instanceName);
      } catch (IOException e) {
          LOGGER.error("Missing database configuration for " + instanceName, e);
          System.exit(2);
      }

      if (instance == null){
          LOGGER.error("Missing database parameter(s)");
          System.exit(3);
      }


    Connection con = null;
    try {
      Class.forName(instance.getDriver());
      String url =  instance.getOracleThinUrl();
      con = DriverManager.getConnection(url, instance.getUser(), instance.getPassword());
      con.setAutoCommit(false);
    } catch (Exception e) {
      LOGGER.error("Could not open connection to " + instanceName, e);
      System.exit(4);
    }

      Statement deleteAllStatement = null;
      try {
          deleteAllStatement = con.createStatement();
          LOGGER.info("Deleting from intenz_text table... ");
          deleteAllStatement.execute(DELETE_ALL);
          LOGGER.info("Deleted!");
      } catch (SQLException e) {
          LOGGER.error("Could not clear table intenz_text on " + instanceName, e);
          try {
              if (deleteAllStatement != null) deleteAllStatement.close();
              if (con != null) con.close();
          } catch (SQLException e2) {
              e2.printStackTrace();
          }
          System.exit(5);
      }

    PreparedStatement findAllStatement = null, insertStatement = null,
    	selectEcStatement = null;
    ResultSet rs = null;
    EnzymeEntryMapper enzymeEntryMapper = new EnzymeEntryMapper();
    EnzymeClassMapper enzymeClassMapper = new EnzymeClassMapper();
    EnzymeSubclassMapper enzymeSubclassMapper = new EnzymeSubclassMapper();
    EnzymeSubSubclassMapper enzymeSubSubclassMapper = new EnzymeSubSubclassMapper();

    // switch this to IntEnz to view all
//    final EnzymeViewConstant view = EnzymeViewConstant.IUBMB;
    final EnzymeViewConstant view = EnzymeViewConstant.INTENZ;

    LOGGER.info("Starting INTENZ_TEXT indexing...");
    long timeElapsed = System.currentTimeMillis();
    try {
      int countEnzymes = 0;
      insertStatement = con.prepareStatement(INSERT);
      selectEcStatement = con.prepareStatement("SELECT ec1 FROM classes");

      // Enzymes
      LOGGER.info("   Loading enzymes ... ");
      findAllStatement = con.prepareStatement(FIND_ALL);
      rs = findAllStatement.executeQuery();

      while (rs.next()) {
        countEnzymes++;
        Long id = new Long(rs.getLong(1));
        try {
          EnzymeEntry enzymeEntry = enzymeEntryMapper.findById(id, con);
          List textParts = getXmlParts(new StringBuffer(EnzymeEntryHelper.toXML(enzymeEntry, encoding, view, true)));

          for (int iii = 0; iii < textParts.size(); iii++) {
            String text = (String) textParts.get(iii);
            insertStatement.setLong(1, enzymeEntry.getId().longValue());
            insertStatement.setString(2, enzymeEntry.getEc().toString());
            String commonName = enzymeEntry.getCommonName(EnzymeViewConstant.INTENZ).getName();
            if(commonName == null) commonName = enzymeEntry.getCommonName(view).getName();
            insertStatement.setString(3, commonName);
            insertStatement.setString(4, enzymeEntry.getStatus().getCode());
            insertStatement.setString(5, enzymeEntry.getSource().toString());
            insertStatement.setInt(6, (iii + 1));
            insertStatement.setString(7, text);
            insertStatement.executeUpdate();
            insertStatement.clearParameters();
          }
//          con.commit();

        } catch (DomainException e) {
          e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
      LOGGER.info("   ... loaded and written (XML) " + countEnzymes + " enzymes.");

      // Classes
      LOGGER.info("   Loading classes ...");
      int idFake = 0; // classes do not have an ID but it is needed for the population.
      rs = selectEcStatement.executeQuery();
      while (rs.next()) {
        idFake++;
        String ec1 = rs.getString(1);
        try {
          EnzymeClass enzymeClass = enzymeClassMapper.find(ec1, con);
          List textParts = getXmlParts(new StringBuffer(EnzymeClassHelper.toXML(enzymeClass, encoding)));

          for (int iii = 0; iii < textParts.size(); iii++) {
            String text = (String) textParts.get(iii);
            insertStatement.setInt(1, idFake);
            insertStatement.setString(2, enzymeClass.getEc().toString());
            insertStatement.setString(3, enzymeClass.getName());
            insertStatement.setString(4, "OK");
            insertStatement.setString(5, "IUBMB");
            insertStatement.setInt(6, (iii + 1));
            insertStatement.setString(7, text);
            insertStatement.executeUpdate();
            insertStatement.clearParameters();
          }
//          con.commit();
        } catch (DomainException e) {
          e.printStackTrace();
        }
      }
      LOGGER.info("   ... classes loaded.");

      // Subclasses
      LOGGER.info("   Loading subclasses ...");
      selectEcStatement = con.prepareStatement("SELECT ec1, ec2 FROM subclasses");
      rs = selectEcStatement.executeQuery();
      while (rs.next()) {
        idFake++;
        String ec1 = rs.getString(1);
        String ec2 = rs.getString(2);
        try {
          EnzymeSubclass enzymeSubclass = enzymeSubclassMapper.find(ec1, ec2, con);
          List textParts = getXmlParts(new StringBuffer(EnzymeSubclassHelper.toXML(enzymeSubclass, encoding)));

          for (int iii = 0; iii < textParts.size(); iii++) {
            String text = (String) textParts.get(iii);
            insertStatement.setInt(1, idFake);
            insertStatement.setString(2, enzymeSubclass.getEc().toString());
            insertStatement.setString(3, enzymeSubclass.getName());
            insertStatement.setString(4, "OK");
            insertStatement.setString(5, "IUBMB");
            insertStatement.setInt(6, (iii + 1));
            insertStatement.setString(7, text);
            insertStatement.executeUpdate();
            insertStatement.clearParameters();
          }
//          con.commit();
        } catch (DomainException e) {
          e.printStackTrace();
        }
      }
      LOGGER.info("   ... subclasses loaded.");

      // SubSubclasses
      LOGGER.info("   Loading sub-subclasses ...");
      selectEcStatement = con.prepareStatement("SELECT ec1, ec2, ec3 FROM subsubclasses");
      rs = selectEcStatement.executeQuery();
      while (rs.next()) {
        idFake++;
        int ec1 = rs.getInt(1);
        int ec2 = rs.getInt(2);
        int ec3 = rs.getInt(3);
        try {
            EnzymeSubSubclass enzymeSubSubclass = enzymeSubSubclassMapper.find(ec1, ec2, ec3, con);
          List textParts = getXmlParts(new StringBuffer(EnzymeSubSubclassHelper.toXML(enzymeSubSubclass, encoding)));
          for (int iii = 0; iii < textParts.size(); iii++) {
            String text = (String) textParts.get(iii);
            insertStatement.setInt(1, idFake);
            insertStatement.setString(2, enzymeSubSubclass.getEc().toString());
            insertStatement.setString(3, enzymeSubSubclass.getName());
            insertStatement.setString(4, "OK");
            insertStatement.setString(5, "IUBMB");
            insertStatement.setInt(6, (iii + 1));
            insertStatement.setString(7, text);
            insertStatement.executeUpdate();
            insertStatement.clearParameters();
          }
//          con.commit();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      LOGGER.info("   ... sub-subclasses loaded.");

      con.commit();

    } catch (SQLException e) {
      try {
        con.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      e.printStackTrace();
    } finally {
      try {
        if (findAllStatement != null) findAllStatement.close();
        if (insertStatement != null) insertStatement.close();
        if (selectEcStatement != null) selectEcStatement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    try {
      con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    LOGGER.info("... population successfully ended.");
    if (System.currentTimeMillis() > timeElapsed) {
      timeElapsed = System.currentTimeMillis() - timeElapsed;
      LOGGER.info("Time elapsed: " + getElapsedTime(timeElapsed / 1000));
    }
  }

  private static List getXmlParts(StringBuffer xml) {
    List textParts = new ArrayList();

    while (xml.length() > 4000) {
      textParts.add(xml.substring(0, 4000));
      xml = xml.delete(0, 4000);
    }
    textParts.add(xml.toString());

    return textParts;
  }

  private static String getElapsedTime(long seconds) {
    StringBuffer elapsedTime = new StringBuffer();
    LOGGER.info("seconds = " + seconds);
    if (seconds > 3599) {
      elapsedTime.append(seconds / 3600);
      elapsedTime.append("h ");
      seconds %= 3600;
    } else {
      if (seconds > 59) {
        elapsedTime.append(seconds / 60);
        elapsedTime.append("m ");
        seconds %= 60;
      }
    }
    elapsedTime.append(seconds);
    elapsedTime.append("s");

    return elapsedTime.toString();
  }
}
