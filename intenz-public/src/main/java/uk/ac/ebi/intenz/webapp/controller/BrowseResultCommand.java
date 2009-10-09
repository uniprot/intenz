package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version 0.9 - 21-July-2003
 */
public class BrowseResultCommand extends NonDatabaseCommand {

  /**
   * Provides functionality to browse through search results.
   * <p/>
   * This includes setting the boundaries for the group of results being displayed and updating
   * other attributes used in <code>result.jsp</code>.
   *
   * @throws ServletException
   * @throws IOException
   */
  public void process() throws ServletException, IOException {
    ArrayList result = (ArrayList) request.getSession().getAttribute("result");

    // No result.
    if (result == null || result.size() == 0) {
    	// TODO: if null result (bookmark from expired session)
    	//		build it from q parameter.
      forward("/noResult.jsp"); // No result found.
      return;
    }
    int size = result.size();
    int start = Integer.parseInt(request.getParameter("st"));
    int groupSize = ((Integer) request.getSession().getAttribute("gs")).intValue();
    if (result.size() > groupSize)
      request.setAttribute("end", "" + groupSize);
    else
      request.setAttribute("end", "" + result.size());
    int end = start + groupSize;

    ArrayList group = getGroup(result, start, end);
    end = start + group.size();
    request.setAttribute("group", group);

    // Set current values (start and end index and result size).
    request.setAttribute("st", "" + start);
    request.setAttribute("end", "" + end);
    request.setAttribute("size", "" + result.size());

    // Set start index of preceeding group.
    if (start != 0) {
      request.setAttribute("pst", "" + (start - groupSize));
    }

    // Set start index of the following group.
    if (end < size) {
      request.setAttribute("nst", "" + end);
    }

    request.setAttribute("query", request.getSession().getAttribute("qResult"));
    request.setAttribute("queryTF", request.getSession().getAttribute("qResultTF"));

    forward("/result.jsp");
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
