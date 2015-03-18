package uk.ac.ebi.intenz.webapp.controller.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import uk.ac.ebi.intenz.webapp.dtos.SearchForm;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * This Action ...
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/01/14 11:18:33 $
 */
public class SearchAction extends Action {

  private static final String COLUMNS = "enzyme_id, ec, common_name, contains(text, 'true WITHIN active', 3) as active";

  private static final String FULL_TEXT_QUERY =
    "SELECT CONTAINS(text, ?, 1) AS score, " + COLUMNS + " FROM enzyme.intenz_text WHERE enzyme_id IN " +
    "(SELECT enzyme_id FROM enzyme.intenz_text WHERE CONTAINS(text, ?, 2) > 0)";

  private static final Logger LOGGER =
	  Logger.getLogger(SearchAction.class.getName());

  /**
   * Returns the SQL statement for full text searching using a text index (ORACLE interMedia text).
   *
   * @return the SQL statement.
   */
  private static String fulltextQueryStatement() {
    return "SELECT /*+ FIRST_ROWS */ score(1) score, " + COLUMNS +
            " FROM enzyme.intenz_text" +
            " WHERE CONTAINS (text, ?, 1) > 0" +
            " ORDER BY score(1) DESC";
  }

  private final static String RESULT_JSP = "result";
  private final static String SEARCH_JSP = "search";
  private final static String ERROR_JSP_FWD = "error";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        SearchForm searchForm = (SearchForm) form;
        ActionMessages errors = new ActionErrors();
        HttpSession session = request.getSession();
        SpecialCharacters encoding = (SpecialCharacters) session.getAttribute("characters");

        String query;
        ArrayList results;
        Integer maxScore = null;
        int startIndex = 0;

        String siParam = request.getParameter("startIndex");
        if (siParam != null){
            // We are browsing an existing result set:
            startIndex = Integer.parseInt(siParam);
            Object queryAttribute = session.getAttribute("query");
            query = (String) queryAttribute;
            Object resultAttribute = session.getAttribute("result");
            results = (ArrayList) resultAttribute;
            Object msAttribute = session.getAttribute("maxScore");
            maxScore = (Integer) msAttribute;
        } else if (searchForm.getQ() == null){
            // No query yet, so no results:
            query = null;
            results = new ArrayList(0);
        } else {
            // Search the database:
            query = preParseQuery(searchForm.getQ().trim());
            Map resultsMap = new Hashtable();
            Connection con = (Connection) session.getAttribute("connection");
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(FULL_TEXT_QUERY);
                LOGGER.info("Searching for '" + query + "'");
                String containsQuery =
                	((query.charAt(0) == '{' && query.charAt(query.length()-1) == '}')
        			|| query.indexOf('%') > -1 || query.indexOf('_') > -1)?
        					query : "{" + query + "}";
                ps.setString(1, containsQuery);
                ps.setString(2, containsQuery);
                ResultSet rs = ps.executeQuery();
                boolean maxScoreCounted = false;
                while (rs.next()) {
                    String id = rs.getString("enzyme_id");
                    String ec = rs.getString("ec");
                    if (!resultsMap.containsKey(id)){
                        // Each result is a list with the following elements:
                        // 0 - enzyme_id
                        // 1 - ec number
                        // 2 - common name
                        // 3 - score
                        // 4 - active
                        ArrayList result = new ArrayList();
                        result.add(id);
                        result.add(ec);
                        String xmlName = retagString(rs.getString("common_name"));
                        result.add(encoding.xml2Display(xmlName));
                        if (!maxScoreCounted) {
                            maxScore = new Integer(rs.getInt("score"));
                            maxScoreCounted = true;
                        }
                        result.add(new Integer(rs.getInt("score")));
                        int active = isEnzymeEntry(ec)? rs.getInt("active") : 1;
                        result.add(new Integer(active));
                        resultsMap.put(id, result);
                    } else {
                        List result = (List) resultsMap.get(id);
                        // Add scores:
                        int score = ((Integer) result.get(3)).intValue();
                        int moreScore = rs.getInt("score");
                        result.set(3, new Integer(score + moreScore));
                        // Add actives:
                        int active = ((Integer) result.get(4)).intValue();
                        int moreActive = rs.getInt("active");
                        result.set(4, new Integer(active + moreActive));
                    }
                }
            } catch (SQLException e) {
                LOGGER.error("Database error.", e);
//                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.standard"));
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.other", e.getMessage()));
                saveErrors(request, errors);
                searchForm.setQ(null);
                return mapping.findForward(ERROR_JSP_FWD);
            } finally {
                try {
                    if (ps != null) ps.close();
                } catch (SQLException e) {
                    LOGGER.error("Database error.", e);
//                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.standard"));
                    errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.application.sql.other", e.getMessage()));
                    saveErrors(request, errors);
                    return mapping.findForward(ERROR_JSP_FWD);
                }
            }
            results = new ArrayList(resultsMap.values());
        }

        Object gsAttribute = session.getAttribute("gs");
        int groupSize = (gsAttribute == null)?
            Integer.parseInt(this.getResources(request).getMessage("application.search.group_size")) :
            ((Integer) gsAttribute).intValue();
        int endIndex = startIndex + groupSize;

        ArrayList group = getGroup(results, startIndex, endIndex);
        request.setAttribute("group", group);

        // Set current values (start and end index and result size).
        request.setAttribute("st", new Integer(startIndex+1));
        if (results.size() > endIndex){
            request.setAttribute("end", new Integer(endIndex));
        } else {
            request.setAttribute("end", new Integer(results.size()));
        }
        request.setAttribute("size", new Integer(results.size()));
        // Set start index of the following group.
        if (endIndex < results.size()) {
            request.setAttribute("nst", new Integer(endIndex));
        } else {
            request.removeAttribute("nst");
        }
        // Set start index of previous group:
        if (startIndex > 0){
            request.setAttribute("pst", new Integer(startIndex-groupSize));
        } else {
            request.removeAttribute("pst");
        }
        // Store whole results, query, etc. in session for later use:
        session.setAttribute("max_score", maxScore);
        session.setAttribute("query", query);
        session.setAttribute("gs", new Integer(groupSize));
        session.setAttribute("result", results);

        return mapping.findForward(RESULT_JSP);
    }

  // ------------------- PRIVATE METHODS ------------------------

    /**
     * @param ec EC number as a String
     * @return true if it is not a class, subclass or subsubclass
     */
  private boolean isEnzymeEntry(String ec){
      Pattern ecPattern = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
      return ecPattern.matcher(ec).find();
  }

  private String preParseQuery(String query) {
    query = detagString(query, true);
    if(!query.isEmpty() && query.length() >0){
       
    // Dots at the end of a searchForm.getQ() string combined with a wildcard operator
    // are not supported by interMedia (i.e. this is an interMedia error).
    if ( query.charAt(query.length() - 1) == '%' && query.charAt(query.length() - 2) == '.') {
      query = query.substring(0, query.length() - 2) + "%";
    }
  }
    

    return query;
  }

  /**
   * Restores XML tags.
   * <p/>
   * All information which has been stored for searching has been formatted with XML. To avoid conflicts with the
   * special character XML encoding, these tags have been reformatted so that they are not anymore recognisable
   * as XML tags ('<' -> '&lt;' and '>' -> '&gt;').
   *
   * @param text Text to be reformatted.
   * @return reformatted text (restored XML)
   */
  private String retagString(String text) {
    if (text != null) {
      Pattern p = Pattern.compile("\\&lt\\;");
      Matcher m = p.matcher(text);
      String temp = m.replaceAll("<");

      p = Pattern.compile("\\&gt\\;");
      m = p.matcher(temp);
      return m.replaceAll("<");
    }
    return "";
  }

  /**
   * Encodes XML tags.
   * <p/>
   * Does the opposite to link{retagString(String) retagString(String)} and escapes '&' and ';' characters,
   * otherwise Oracle cannot process the string properly.
   *
   * @param text             Text to be reformatted.
   * @param escapeCharacters Set to <code>true</code> to escape '&' and ';'.
   * @return reformatted text (encoded XML)
   */
  private String detagString(String text, boolean escapeCharacters) {
    if (text != null) {
      Pattern p = Pattern.compile("<");
      Matcher m = p.matcher(text);
      String temp = "";
      if (escapeCharacters)
        temp = m.replaceAll("\\\\&lt\\\\;");
      else
        temp = m.replaceAll("\\&lt\\;");

      p = Pattern.compile(">");
      m = p.matcher(temp);
      if (escapeCharacters)
        return m.replaceAll("\\\\&gt\\\\;");
      else
        return m.replaceAll("\\&gt\\;");
    }
    return "";
  }

  private ArrayList getGroup(ArrayList result, int start, int end) {
    ArrayList group = new ArrayList();

    if (end > result.size()) {
      for (int iii = start; iii < result.size(); iii++) {
        group.add(result.get(iii));
      }
    } else {
      for (int iii = start; iii < end; iii++) {
        group.add(result.get(iii));
      }
    }

    return group;
  }
}
