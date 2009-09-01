package uk.ac.ebi.intenz.mapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Provides methods for access to procedures not related to a specific package.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class CommonProceduresMapper {

  public CommonProceduresMapper() {
  }

  private String callCloneStatement() {
    return "{CALL p_clone_enzyme(?, ?, ?, ?)}";
  }

  /**
   * Creates a clone of an enzyme by using its enzyme ID.
   *
   * @param enzymeId The enzyme's ID.
   * @param con      The connection.
   * @throws SQLException
   */
  public Long createClone(Long enzymeId, Connection con) throws SQLException {
    int newEnzymeId = 0;
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callCloneStatement());
      cStmt.setString(1, "" + enzymeId);
      cStmt.registerOutParameter(2, Types.VARCHAR);
      cStmt.setString(3, "SU");
      cStmt.setString(4, "N");
      cStmt.execute();

      newEnzymeId = Integer.parseInt(cStmt.getString(2));
    } finally {
      if (cStmt != null) cStmt.close();
    }

    return new Long(newEnzymeId);
  }

}
