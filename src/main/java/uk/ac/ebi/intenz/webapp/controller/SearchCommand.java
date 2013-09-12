package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.WebUtil;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.exceptions.EcException;
import uk.ac.ebi.intenz.webapp.exceptions.QueryException;
import uk.ac.ebi.intenz.webapp.utilities.IntEnzMessenger;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;
import uk.ac.ebi.xchars.exceptions.InvalidUTF8OctetSequenceException;

/**
 * This class processes all full text queries.
 * <p/>
 * All queries are processed using the UTF-8 encoded (octet sequences) representation sent by browsers.
 * Rather than using <code>request.getParameter(...)</code> this class decodes the query string manually, since
 * <code>request.getParameter(...)</code> does not decode the octet sequences correctly, if special characters encoded
 * as two or more octets are transmitted.<br/>
 * See the <code>XChars</code> library documentation for more information regarding UTF-8 octet sequence decoding.
 * <p/>
 * <p/>
 * TODO: Implement a query parser.
 *
 * @author Michael Darsow
 * @version 2.0 - 13-July-2004
 */
public class SearchCommand extends DatabaseCommand {

    public static final Logger LOGGER = Logger.getLogger(SearchCommand.class);

  private static final String COLUMNS =
      "enzyme_id, ec, common_name, status, text, text_order";

  /**
   * Returns the SQL statement for full text searching using a text index (ORACLE interMedia Text).
   *
   * @return the SQL statement.
   */
  private String fulltextQueryStatement() {
    return "SELECT /*+ FIRST_ROWS */ score(1) score, " + COLUMNS +
            " FROM enzyme.intenz_text" +
            " WHERE CONTAINS (text, ?, 1) > 0" +
            " ORDER BY score(1) DESC";
  }

  /**
   * Processes the full text query.
   * <p/>
   * The query string is parsed, decoded, checked and integrated into a SQL statement.
   * After execution the result is being sent to the browser (if any).
   * Exceptions regarding this process are caught and the user is informed accordingly.
   *
   * @throws ServletException ...
   * @throws IOException      ...
   */
  public void process() throws ServletException, IOException {
      int groupSize = 10;
      try {
          groupSize = Integer.parseInt(config.getPageSize());
      } catch (NumberFormatException e) {
          LOGGER.error("Bad page size: " + config.getPageSize(), e);
      }
      String query = null;
    StringBuffer userFriendlyQuery = null;
    String userFriendlyQueryTF = null;

    // Decode the UTF-8 octets sent by the client.
    try {
      query = decodeQuery(request.getQueryString(), false);
      userFriendlyQuery = new StringBuffer(typifyQuery(query, QueryType.valueOf(request.getParameter("t"))).trim());
      userFriendlyQueryTF = new String(query);
    } catch (InvalidUTF8OctetSequenceException e) {
      request.setAttribute("query", "");
      request.setAttribute("message", e.getMessage());
      forward("/noResult.jsp"); // No result found.
      return;
    }

    // Catch empty queries.
    if (query == null || query.equals("")) {
      request.setAttribute("query", "");
      request.setAttribute("message", "The query string was empty!");
      forward("/noResult.jsp"); // No result found.
      return;
    }

    // Check query if it is valid and prepare it for the actual search process.
    StringBuffer checkedQuery = null;
    try {
      // Parameter 't' stores the type of the search. This can be one of the following:
      // ALL words (logical AND), ANY words (logical OR) or EXACT match (phrase search).
      checkedQuery = new StringBuffer(checkQuery(query, QueryType.valueOf(request.getParameter("t"))).trim());

      // Check if the user entered words to be excluded.
      String excludedWords = decodeQuery(request.getQueryString(), true);
      if (excludedWords != null && !excludedWords.equals("")) {
        checkedQuery.append(" ");
        String extendedQuery = extendQuery(excludedWords, true);
        checkedQuery.append(extendedQuery);
        userFriendlyQuery.append(" ");
        userFriendlyQuery.append(new String(extendQuery(excludedWords, false)));
        request.setAttribute("excludedWords", excludedWords);
      }

      // Check if the user chose a field to limit his/her search.
      String field = request.getParameter("fields");
      if (field != null && !field.equals("all")) {
        checkedQuery.append(" ");
        String withinQuery = addWithinClause(field);
        checkedQuery.append(withinQuery);
        userFriendlyQuery.append(" ");
        userFriendlyQuery.append(new String(withinQuery));
        request.setAttribute("field", field);
      }
    } catch (QueryException e) {
      request.setAttribute("message", e.getMessage());
      // Store (HTML-friendly) original query for feedback.
      request.setAttribute("query", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQuery.toString())));
      request.setAttribute("queryTF", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQueryTF)));
      forward("/search.jsp");
      return;
    } catch (InvalidUTF8OctetSequenceException e) {
      request.setAttribute("query", "");
      request.setAttribute("message", e.getMessage());
      forward("/noResult.jsp"); // No result found.
      return;
    }

    // Store (HTML-friendly) original query for feedback.
    request.setAttribute("query", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQuery.toString())));
    request.setAttribute("queryTF", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQueryTF)));

    PreparedStatement ps = null;
    List<Result> results = new ArrayList<Result>();
    Integer maxScore = new Integer(0);

    try {
      ps = con.prepareStatement(fulltextQueryStatement());
      ps.setString(1, checkedQuery.toString());
//      ps.setString(2, checkedQuery.toString());
      ResultSet rs = ps.executeQuery();

      boolean maxScoreCounted = false;
      queryResultsLoop: while (rs.next()) {
        String id = rs.getString("enzyme_id");
        // Check if we already have the entry within the results:
        for (int i = 0; i < results.size(); i++){
            Result previous = results.get(i);
            if (id.equals(previous.id)){
                previous.addText(rs.getString("text"), rs.getInt("text_order"));
                previous.addScore(rs.getInt("score"));
                continue queryResultsLoop;
            }
        }

        Result res = new Result();
        res.id = id;
        res.ec = EnzymeCommissionNumber.valueOf(rs.getString("ec"));
        String commonName = rs.getString("common_name");
        if (commonName == null) commonName = "";
        res.commonName = commonName;
        res.status = rs.getString("status");
        if (!maxScoreCounted) {
            maxScore = new Integer(rs.getString("score"));
            maxScoreCounted = true;
        }
        res.score = rs.getInt("score");
        res.addText(rs.getString("text"), rs.getInt("text_order"));
        if (res.ec.toString().equals(query)){
        	results.add(0, res);
        } else {
        	results.add(res);
        }

      }
    } catch (IllegalArgumentException e) {
      doErrorExceptionHandling(e);
      return;
    } catch (EcException e){
        doErrorExceptionHandling(e);
        return;
    } catch (SQLException e) {
       LOGGER.error("While searching", e);
      IntEnzMessenger.sendError(this.getClass().toString(),
              e.getMessage() + " query (checked query): " + query + "("
                    + checkedQuery + ")",
              (String) request.getSession().getAttribute("user"));
      if (e.getMessage().indexOf("DRG-51030") > -1) {
        request.setAttribute("message", "Your query resulted in too many terms.\nPlease refine your query.");
        forward("/search.jsp");
        return;
      }
      if (e.getMessage().indexOf("DRG-50901") > -1) {
        request.setAttribute("message",
                "The given query could not be processed.\nPlease check the usage of operators and special characters.");
        forward("/search.jsp");
        return;
      }
      if (e.getMessage().indexOf("DRG-10837") > -1) {
        request.setAttribute("message",
                "The given section does not exist.\nPlease choose a section from the drop down list below.");
        forward("/search.jsp");
        return;
      }
      request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
              this.databaseErrorMessage);
      forward("/error.jsp");
      return;
	} finally {
      try {
        ps.close();
      } catch (SQLException e) {
         doErrorExceptionHandling(e);
         return;
      }
    }

    if (results.size() == 0) {
      // Store (HTML-friendly) original query for feedback.
      request.setAttribute("query", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQuery.toString())));
      request.setAttribute("queryTF", WebUtil.escapeHTMLTag(escapeUTF8(userFriendlyQueryTF)));
      request.setAttribute("message", "No results found for '" + query + "'");
      forward("/noResult.jsp"); // No result found.
      return;
    }

    if (results.size() == 1){
        // Go straight to the only one result:
        String ec = results.get(0).ec.toString();
        try {
            switch (EnzymeCommissionNumber.valueOf(ec).getType()) {
                case ENZYME:
                case PRELIMINARY:
                    String id = results.get(0).id;
                    forward("/query?cmd=SearchID&id=" + id);
                    return;
                default:
                    forward("/query?cmd=SearchEC&ec=" + ec);
                    return;
            }
        } catch (Exception e) {
            // we shouldn't get here, that would mean there's something wrong in the DB!
            request.setAttribute("message", ec + " is not a valid EC number.\nPlease try again.");
            forward("/search.jsp");
            return;
        }
    }

    List<Result> group = getGroup(results, 0, groupSize);
    request.setAttribute("group", group);

    // Set maximum score.
    request.getSession().setAttribute("max_score", maxScore);

    // Set current values (start and end index and result size).
    request.setAttribute("st", "" + 0);
    if (results.size() > groupSize)
      request.setAttribute("end", "" + groupSize);
    else
      request.setAttribute("end", "" + results.size());
    request.setAttribute("size", "" + results.size());

    // Set start index of the following group.
    if (groupSize < results.size()) {
      request.setAttribute("nst", "" + groupSize);
    }

    // Set the group size.
    request.getSession().setAttribute("gs", new Integer(groupSize));
    request.getSession().setAttribute("qResult", escapeUTF8(userFriendlyQuery.toString()));
    request.getSession().setAttribute("qResultTF", escapeUTF8(userFriendlyQueryTF));
    request.getSession().setAttribute("result", results);
    forward("/result.jsp");
  }

   private void doErrorExceptionHandling (Exception e) throws ServletException, IOException {
       LOGGER.error("Other error while searching", e);
      IntEnzMessenger.sendError(this.getClass().toString(), e.getMessage(),
              (String) request.getSession().getAttribute("user"));
      request.setAttribute("message", "The following database error occured:\n" + e.getMessage() +
              this.databaseErrorMessage);
      forward("/error.jsp");
   }

   /**
   * Replaces '+' by a space.
   * <p/>
   * The plus character in UTF-8 URL encoding encodes the space character.
   *
   * @param queryString The query string to be checked. (The parameter cannot be <code>null</code> or empty, because it has been checked already.)
   * @return query string with spaces.
   */
  private String replacePlus(String queryString) {
    return queryString.replaceAll("\\+", " ");
  }

  /**
   * Decodes the UTF-8 query, i.e. the value of the <code>q</code> parameter.
   * <p/>
   * Does the same as <code>URLDecoder</code> except that the octets are decoded differently
   * (see <code>XChars</code> library for more info).
   *
   * @param queryString    The query string to be decoded.
   * @param isNotParameter
   * @return the decoded value of the <code>q</code> parameter (i.e. the full text query).
   * @throws InvalidUTF8OctetSequenceException
   *          if the given octets are invalid (see <code>XChars</code> library for more info).
   */
  private String decodeQuery(String queryString, boolean isNotParameter) throws InvalidUTF8OctetSequenceException {
    if (queryString == null || queryString.equals("")) return queryString;

    StringBuffer searchQuery = new StringBuffer(replacePlus(getSearchQuery(queryString, isNotParameter)));
    Pattern utf8HexPattern = Pattern.compile("((%([a-fA-F0-9]){2}?)+)"); // Pattern for octet sequences.
    Matcher utf8Matcher = utf8HexPattern.matcher(searchQuery);
    int index = 0;
    while (utf8Matcher.find(index)) {
      String decodedString = SpecialCharacters.decodeUTF8(utf8Matcher.group(1));
      index = utf8Matcher.start() + decodedString.length();
      searchQuery.replace(utf8Matcher.start(), utf8Matcher.end(), decodedString);
      if (index > searchQuery.length() - 1) break;
      utf8Matcher.reset(searchQuery);
    }

    return searchQuery.toString();
  }

  private String getSearchQuery(String queryString, boolean isNotParameter) {
    if (queryString == null || queryString.equals("")) return "";
    int searchQueryStart = 0;
    if (isNotParameter)
      searchQueryStart = queryString.indexOf("not=");
    else
      searchQueryStart = queryString.indexOf("q=");

    if (searchQueryStart == -1) return "";
    int searchQueryEnd;
    String temp = null;
    if (isNotParameter) {
      temp = queryString.substring(searchQueryStart + 4);
    } else {
      temp = queryString.substring(searchQueryStart + 2);
    }
    StringBuffer searchQuery = null;
    if (temp.indexOf('&') > -1) {
      searchQueryEnd = temp.indexOf('&');
      searchQuery = new StringBuffer(temp.substring(0, searchQueryEnd));
    } else {
      searchQuery = new StringBuffer(temp.substring(0));
      searchQueryEnd = searchQueryStart + searchQuery.length();
    }
    return searchQuery.toString();
  }

  private String addWithinClause(String field) {
    StringBuffer withinClause = new StringBuffer();
    withinClause.append("WITHIN ");
    withinClause.append(field);
    return withinClause.toString();
  }

  private String extendQuery(String excludedWords, boolean transformQuery) throws QueryException {
    if (transformQuery) excludedWords = transformQuery(excludedWords, null, true);

    StringBuffer extendedQuery = new StringBuffer();
    for (StringTokenizer stringTokenizer = new StringTokenizer(excludedWords, ","); stringTokenizer.hasMoreTokens();) {
      String token = stringTokenizer.nextToken().trim();
      extendedQuery.append("NOT ");
      extendedQuery.append(token);
      extendedQuery.append(" ");
    }

    return extendedQuery.toString().trim();
  }




  // ------------------- PRIVATE METHODS ------------------------

  private String escapeUTF8(String query) {
    // Existing XChars elements will be transformed into escaped UTF-8 strings.
    SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
    if (query.indexOf("<small>") > -1 || query.indexOf("</small>") > -1 || query.indexOf("<smallsup>") > -1 ||
            query.indexOf("</smallsup>") > -1 || query.indexOf("</smallsub>") > -1 || query.indexOf("<smallsub>") > -1)
      return encoding.xml2Display(query, EncodingType.SWISSPROT_CODE);
    return encoding.xml2Display(query);
  }

  /**
   * Checks the query for various things.
   * <p/>
   *
   * @param query
   * @param type  The type of query. Can be <code>null</code>.
   * @return
   * @throws QueryException
   */
  private String checkQuery(String query, QueryType type) throws QueryException {
    assert query != null && !query.equals("");
    return transformQuery(query, type, false);
  }

  public String transformQuery(String query, QueryType queryType, boolean exclusion) throws QueryException {
    assert query != null && !query.equals("");

    // AND, OR and NOT operators will be removed, because manually added operators are not supported.
    query = escapeBooleanOperators(operators2Uppercase(query));

    // XChars formmattings will be removed as they do not appear as tokens within the search engine.
    query = removeFormattings(query);

    // Queries which are not phrase queries will be handled differently.
    if (queryType == null || queryType != QueryType.EXACT) {
      // The within operator will be removed, if the user entered it manually.
      query = escapeWithinOperator(query);

      // Check for 'silly' queries.
      String longQueryWord = getLongQueryWord(query);
      if (!longQueryWord.equals(""))
        throw new QueryException("\"" + longQueryWord.substring(0, 80) +
                "\"... is too long a word. Try using a shorter word.");

      if (countQueryWords(query) > 10)
        throw new QueryException("The search query must not exceed 10 words.");

      if (!exclusion) {
        // Use the type parameter to alter the query accordingly.
        query = typifyQuery(query, queryType);
      }
    }

    // Existing XChars elements will be transformed into escaped UTF-8 strings.
    query = escapeUTF8(query);

     query = removeFormattings(query);

    // Escape remaining tags.
    if (Pattern.matches(".*?\\<.+?\\>.*?", query))
      query = query.replaceAll("\\<", "\\\\<").replaceAll("\\>", "\\\\>").replaceAll("\\/", "\\\\/");

    // Escape/Remove unsupported operators.
    query = escapeUnsupportedOperators(query);

    return query;
  }

  /**
   * Removes the within operator if it has been manually entered by the user.
   *
   * @param query
   * @return
   */
  private String escapeWithinOperator(String query) {
    assert query != null;
    query = " " + query + " ";
    return query.replaceAll("(\\sWITHIN\\s)", " ").trim();
  }

  /**
   * Handles the special case when the user entered <code>XChars</code> formattings.
   * <p/>
   * Currently only <code>&lt;smallsup&gt;</code> and <code>&lt;smallsub&gt;</code> elements represent
   * <code>XChars</code> formattings.
   *
   * @param query The query to be checked.
   * @return the checked query.
   */
  private String removeFormattings(String query) {
    assert query != null;
    query = query.replaceAll("\\<smallsu[pb]\\>", "").replaceAll("\\<\\/smallsu[pb]\\>", "");
    query = query.replaceAll("\\<\\/?small\\>", "");
    query = query.replaceAll("\\<\\/?sup\\>", "");
    query = query.replaceAll("\\<\\/?sub\\>", "");
    query = query.replaceAll("\\<\\/?b\\>", "");
    query = query.replaceAll("\\<\\/?i\\>", "");
    query = query.replaceAll("\\<\\/?p\\/?\\>", "");
    return query.replaceAll("\\<activated\\>", "").replaceAll("\\<\\/activated\\>", "");
  }

  /**
   * Escapes unsupported characters or characters which are reserved characters of the search engine.
   *
   * @param query The query to be checked.
   * @return The checked query.
   */
  private String escapeUnsupportedOperators(String query) {
    assert query != null;
    StringBuffer checkedQuery = new StringBuffer();
    char[] chars = query.toCharArray();
    char previous = '-';
    char current = '-';
    char next = '-';
    for (int iii = 0; iii < chars.length; iii++) {
      if (iii > 0) previous = current;
      current = chars[iii];
      if (iii < chars.length - 1)
        next = chars[(iii + 1)];
      else
        next = '-';
      switch (checkCharacter(previous, current, next)) {
        case 0:
          checkedQuery.append(current);
          break;
        case 1: // Escape character.
          checkedQuery.append("\\");
          checkedQuery.append(current);
          break;
        case 2: // Remove character and previous space.
          checkedQuery = checkedQuery.deleteCharAt(checkedQuery.length() - 1);
          break;
      }
    }

    return checkedQuery.toString();
  }

  /**
   * Checks whether the current character is an unsupported operator or reserved character of the search engine.
   *
   * @param preceeding The preceeding character.
   * @param current    The current character.
   * @param next       The nect character.
   * @return a code fot the calling method (0 = do not escape, 1 = escape, 2 = remove character).
   */
  private int checkCharacter(char preceeding, char current, char next) {
    char[] unsupportedOperators = {'[', ']', ',', '-', '_', '$', '!', '=', '*', ':', '?', '|', '>', '&', '#', ';', '(', ')', '.'};
    Arrays.sort(unsupportedOperators);
    int index = Arrays.binarySearch(unsupportedOperators, current);
    if (index > -1) {
      if (preceeding == ' ' && next == ' ') return 2; // remove character (should never happen, becuase it is checked earlier)
      if (preceeding != '\\') return 1; // escape character
    }
    return 0; // do not escape
  }

  /**
   * Inserts logical operators in the query string according to the given type of search.
   * <p/>
   * Currently only <code>AND</code> and <code>OR</code> are supported.
   *
   * @param query The query to be extended.
   * @param type  The type of the query.
   * @return The extended query.
   */
  private String typifyQuery(String query, QueryType type) {
    assert query != null;
    StringBuffer adjustedQuery = new StringBuffer();
    int iii = 0;
    for (StringTokenizer stringTokenizer = new StringTokenizer(query); stringTokenizer.hasMoreTokens();) {
      String token = stringTokenizer.nextToken();
      if (iii > 0) {
        if (type == QueryType.ANY)
          adjustedQuery.append("OR ");
        if (type == QueryType.ALL)
          adjustedQuery.append("AND ");
      }
      adjustedQuery.append(token);
      adjustedQuery.append(" ");
      iii++;
    }
    return adjustedQuery.toString().trim();
  }

  /**
   * Removes supported boolean operators which have been entered manually.
   * <p/>
   * All supported boolean operators can only be used by filling the search form correctly (i.e. without entering
   * them manually).
   *
   * @param query The query to be checked.
   * @return The checked query.
   */
  private String escapeBooleanOperators(String query) {
    assert query != null;
    query = " " + query + " ";
    query = query.replaceAll("(\\sAND\\s)", " {AND} ");
    query = query.replaceAll("(\\sOR\\s)", " {OR} ");
    query = query.replaceAll("(\\sNOT\\s)", " {NOT} ");
    query = query.replaceAll("\\s\\&\\s", " {&} ");
    query = query.replaceAll("\\s\\|\\s", " {|} ");
    query = query.replaceAll("\\s\\~\\s", " {~} ");
     if (query.indexOf(" BT ") != - 1)
         query = query.replaceAll(" BT ", " {BT} ");
      if (query.indexOf(" ABOUT ") != - 1)
         query = query.replaceAll(" ABOUT ", " {ABOUT} ");
      if (query.indexOf(" ACCUM ") != - 1)
         query = query.replaceAll(" ACCUM ", " {ACCUM} ");
      if (query.indexOf(" BTG ") != - 1)
         query = query.replaceAll(" BTG ", " {BTG} ");
      if (query.indexOf(" BTI ") != - 1)
         query = query.replaceAll(" BTI ", " {BTI} ");
      if (query.indexOf(" BTP ") != - 1)
         query = query.replaceAll(" BTP ", " {BTP} ");
      if (query.indexOf(" FUZZY ") != - 1)
         query = query.replaceAll(" FUZZY ", " {FUZZY} ");
      if (query.indexOf(" HASPATH ") != - 1)
         query = query.replaceAll(" HASPATH ", " {HASPATH} ");
      if (query.indexOf(" INPATH ") != - 1)
         query = query.replaceAll(" INPATH ", " {INPATH} ");
      if (query.indexOf(" MINUS ") != - 1)
         query = query.replaceAll(" MINUS ", " {MINUS} ");
      if (query.indexOf(" NEAR ") != - 1)
         query = query.replaceAll(" NEAR ", " {NEAR} ");
      if (query.indexOf(" NT ") != - 1)
         query = query.replaceAll(" NT ", " {NT} ");
      if (query.indexOf(" NTG ") != - 1)
         query = query.replaceAll(" NTG ", " {NTG} ");
      if (query.indexOf(" NTI ") != - 1)
         query = query.replaceAll(" NTI ", " {NTI} ");
      if (query.indexOf(" NTP ") != - 1)
         query = query.replaceAll(" NTP ", " {NTP} ");
      if (query.indexOf(" PT ") != - 1)
         query = query.replaceAll(" PT ", " {PT} ");
      if (query.indexOf(" RT ") != - 1)
         query = query.replaceAll(" RT ", " {RT} ");
      if (query.indexOf(" SQE ") != - 1)
         query = query.replaceAll(" SQE ", " {SQE} ");
      if (query.indexOf(" SYN ") != - 1)
         query = query.replaceAll(" SYN ", " {SYN} ");
      if (query.indexOf(" TR ") != - 1)
         query = query.replaceAll(" TR ", " {TR} ");
      if (query.indexOf(" TRSYN ") != - 1)
         query = query.replaceAll(" TRSYN ", " {TRSYN} ");
      if (query.indexOf(" TT ") != - 1)
         query = query.replaceAll(" TT ", " {TT} ");
    return query.trim();
  }

  /**
   * Checks if a query word is longer than 200 characters.
   *
   * @param query The query string.
   * @return The word which length is greater than 200 characters.
   */
  private String getLongQueryWord(String query) {
    assert query != null;
    for (StringTokenizer stringTokenizer = new StringTokenizer(query); stringTokenizer.hasMoreTokens();) {
      String word = stringTokenizer.nextToken();
      if (word.length() > 200) return word;
    }
    return "";
  }

  /**
   * Counts the number of query words.
   *
   * @param detaggedQuery The query string.
   * @return The number of query words (w/o operators).
   */
  private int countQueryWords(String detaggedQuery) {
    int count = 0;
    for (StringTokenizer stringTokenizer = new StringTokenizer(detaggedQuery); stringTokenizer.hasMoreTokens(); stringTokenizer.nextToken()) {
      count++;
    }
    return count;
  }

  /**
   * Checks if the given string is a supported operator.
   *
   * @param word The search to be checked.
   * @return <code>true</code>, if the given string is a supported operator.
   */
  private boolean isOperator(String word) {
    assert word != null;
    if (word.trim().toUpperCase().equals("AND") ||
            word.toUpperCase().equals("OR") ||
            word.toUpperCase().equals("NOT") ||
            word.toUpperCase().equals("WITHIN"))
      return true;

    return false;
  }

  /**
   * Writes all supported operators using uppercase letters.
   *
   * @param query The query to be checked.
   * @return The formatted query.
   */
  private String operators2Uppercase(String query) {
    assert query != null && !query.equals("");
    StringBuffer sb = new StringBuffer();
    for (StringTokenizer stringTokenizer = new StringTokenizer(query); stringTokenizer.hasMoreTokens();) {
      String word = stringTokenizer.nextToken();
      if (isOperator(word)) {
        sb.append(word.toUpperCase() + " ");
      } else {
        sb.append(word + " ");
      }
    }

    return sb.toString().trim();
  }

  /**
   * Returns a result group to be shown in the browser.
   *
   * @param result The result.
   * @param start  The start index of the group.
   * @param end    The end index og the group.
   * @return the selected group.
   */
  private List<Result> getGroup(List<Result> result, int start, int end) {
    assert result != null;
    List<Result> group = new ArrayList<Result>();

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

  private static class QueryType {
    private String type;
    public static final QueryType ANY = new QueryType("ANY");
    public static final QueryType ALL = new QueryType("ALL");
    public static final QueryType EXACT = new QueryType("EXACT");

    /**
     * Returns the corresponding instance of the given query type.
     * <p/>
     * If the query type does not match any type an exception is thrown.
     *
     * @param type The query type.
     * @return the class constant corresponding to the given type.
     * @throws IllegalArgumentException if the type is invalid.
     */
    public static QueryType valueOf(String type) {
      if (type == null || type.equals("") || type.toUpperCase().equals("UNDEF")) return EXACT;
      type = type.toUpperCase();
      if (type.equals(ANY.toString())) return ANY;
      if (type.equals(ALL.toString())) return ALL;
      if (type.equals(EXACT.toString())) return EXACT;
      throw new IllegalArgumentException();
    }

    /**
     * Object cannot be created outside this class.
     *
     * @param type The query type.
     */
    private QueryType(String type) {
      this.type = type;
    }

    /**
     * Standard equals method.
     *
     * @param o Object to be compared to this one.
     * @return <code>true</code> if the objects are equal.
     */
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof QueryType)) return false;

      final QueryType queryType = (QueryType) o;

      if (type != null ? !type.equals(queryType.type) : queryType.type != null) return false;

      return true;
    }

    /**
     * Returns the hash code of this object.
     *
     * @return the hash code of this object.
     */
    public int hashCode() {
      return (type != null ? type.hashCode() : 0);
    }

    /**
     * Returns the query type's query.
     *
     * @return the query type's query.
     */
    public String toString() {
      return type;
    }
  }

  public class Result {
      private String id;
      private EnzymeCommissionNumber ec;
      private String commonName;
      private String status;
      private int score;
      private SortedMap<Integer, String> xmlFragments;
      private void addText(String text, int order){
          if (xmlFragments == null) xmlFragments = new TreeMap<Integer, String>();
          xmlFragments.put(new Integer(order), text);
      }
      private void addScore(int i) {
          score += i;
      }
      private String getText(){
          StringBuffer wholeXml = new StringBuffer();
          for (Iterator<String> it = xmlFragments.values().iterator(); it.hasNext();){
              wholeXml.append(it.next());
          }
          return wholeXml.toString();
      }
      public EnzymeCommissionNumber getEc() {
          return ec;
      }
      public String getId() {
          return id;
      }
      public String getCommonName() {
          return commonName;
      }
      public String getStatus() {
          return status;
      }
      public boolean isActive(){
          return getText().indexOf("<active></active>") == -1;
      }

  }
}
