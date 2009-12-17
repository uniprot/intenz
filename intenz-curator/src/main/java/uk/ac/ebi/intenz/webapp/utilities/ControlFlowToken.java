package uk.ac.ebi.intenz.webapp.utilities;

import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.taglib.html.Constants;

/**
 * This class provides a mechanism to guarantee correct control flow, that is using "back" or "reload" buttons of
 * a browser do not lead to unwanted behaviour.
 * <p/>
 * This is achieved by storing a unique token in the session and request when accessing a servlets service() method.
 * The resulting pages of a servlet response contain the token by accessing it via the request object. Pressing
 * the "back" or "reload" button leads to already generated pages with <b>different</b> tokens, therefore a subsequent
 * processing action by a servlet will encounter the difference when comparing the token of the request with token of
 * the current session and permit this transaction.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class ControlFlowToken {

  private static final Logger LOGGER =
	  Logger.getLogger(ControlFlowToken.class.getName());

  /**
   * Sets a control flow token in the session and in the request.
   * <p/>
   * The token is a kind of checksum (MD5 message digest, part of java.security).
   *
   * @param req The request object.
   */
  public static void setToken(HttpServletRequest req, Long enzymeId) {
    HttpSession session = req.getSession(true);
    long sysTime = System.currentTimeMillis();
    byte[] time = new Long(sysTime).toString().getBytes();
    byte[] id = session.getId().getBytes();

    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(id);
      md5.update(time);
      String token = toHex(md5.digest());
      req.setAttribute(Constants.TOKEN_KEY, token);
      session.setAttribute(Constants.TOKEN_KEY, token);

      // Store token and enzymeId for reloading purposes.
      if (session.getAttribute("tokenHashtable") == null) {
        session.setAttribute("tokenHashtable", new Hashtable());
      }

      Map tokenHashtable = (Map) session.getAttribute("tokenHashtable");
      tokenHashtable.put(token, enzymeId);

    } catch (Exception e) {
      System.err.println("MD5 digest calculation error");
    }
  }

  /**
   * Returns <tt>true</tt> if the token is valid (request token == session token).
   *
   * @param req The request object.
   * @return see above.
   */
  public static boolean isValid(HttpServletRequest req) {
    HttpSession session = req.getSession(true);
    String requestToken = req.getParameter(Constants.TOKEN_KEY);
    LOGGER.info("request token = " + requestToken);
    String sessionToken = (String) session.getAttribute(Constants.TOKEN_KEY);
    LOGGER.info("session token = " + sessionToken);
    if (requestToken == null || sessionToken == null)
      return false;
    else
      return requestToken.equals(sessionToken);
  }

  /**
   * Converts MD5 digest to hex.
   */
  private static String toHex(byte[] digest) {
    StringBuffer buffer = new StringBuffer();
    for (int iii = 0; iii < digest.length; iii++) {
      buffer.append(Integer.toHexString((int) digest[iii] & 0x00ff));
    }

    return buffer.toString();
  }
}
