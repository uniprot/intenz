package uk.ac.ebi.intenz.mapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Provides methods for audit package access.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:07 $
 */
public class AuditPackageMapper {

  public static final String STANDARD_REMARK = "Update by web application.";

  public AuditPackageMapper() {
  }

  private String callStatement() {
    return "{CALL enzyme.auditpackage.setremark(?)}";
  }

  /**
   * Sets a remark in the audit tables.
   *
   * @param remark Remark to be stored in the audit tables.
   * @param con    The connection.
   * @throws SQLException
   */
  public void setRemark(String remark, Connection con) throws SQLException {
    CallableStatement cStmt = null;

    try {
      cStmt = con.prepareCall(callStatement());
      if (remark == null){
    	  cStmt.setNull(1, Types.VARCHAR);
      } else {
    	  cStmt.setString(1, remark);
      }
      cStmt.executeUpdate();
    } finally {
      cStmt.close();
    }
  }

}
