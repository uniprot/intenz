<%
  response.setContentType("text/html; charset=UTF-8");
  String message = (String) request.getAttribute("message");
  String query = (String) request.getAttribute("queryTF");
  if(query == null) query = "";
  String excludedWords = (String) request.getAttribute("excludedWords");
  if(excludedWords == null) excludedWords = "";
  String field = (String) request.getAttribute("field");
%>

  <%
    if(message != null && !message.equals("")) {
  %>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><font color="blue"><%= message %></font></td>
    </tr>
  </table>
  <%
    }
  %>

<div id="xround" style="width: 60em"><b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><div class="xboxcontent">
<form method="get" action="query">
	<input type="hidden" name="cmd" value="Search">
    <table cellspacing="5" align="center">
      <tr>
        <td valign="center"><input type="text" size="50" maxsize="100" name="q" value="<%= query %>"></td>
        <td valign="center">
          <select name="t" size="1">
            <option value="exact" selected="selected">Exact phrase</option>
            <option value="any">Any of these words</option>
            <option value="all">All of these words</option>
          </select><br/>
        </td>
        <td valign="center">
          <a href="javascript:windowOpenWithSize('encoding.html', 340, 400)"><img style="vertical-align:middle" src="images/alpha_icon.gif" width="16" height="15" border="1" alt="Special characters"></a>
        </td>
      </tr>
      <tr>
        <td colspan="3">
          <hr/>
        </td>
      </tr>
      <tr>
        <td colspan="3">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td valign="top" align="left">
                  <div style="font-weight:bold;">Words to exclude</div>
                  <input type="text" size="35" maxsize="100" name="not" value="<%= excludedWords %>">
                  <div style="font-size: smaller;">(NB: Separate words by comma)</div>
              </td>
              <td valign="top" align="right">
                  <div style="font-weight:bold;">Search in fields</div>
                  <select name="fields" size="1">
                  <% if (field == null || field.equals("all")) {
                  %><option value="all" selected="selected">All fields</option>
                  <% } else {
                  %><option value="all">All fields</option>
                  <% }
                    if(field != null && field.equals("ec")) {
                  %><option value="ec" selected="selected">EC number</option>
                  <% } else {
                  %><option value="ec">EC number</option>
                  <% }
                    if(field != null && field.equals("name")) {
                  %><option value="name" selected="selected">Common/Recommended name</option>
                  <% } else {
                  %><option value="name">Common/Recommended name</option>
                  <% }
                    if(field != null && field.equals("reaction")) {
                  %><option value="reaction" selected="selected">Reactions</option>
                  <% } else {
                  %><option value="reaction">Reactions</option>
                  <% }
                    if(field != null && field.equals("cofactor")) {
                  %><option value="cofactor" selected="selected">Cofactors</option>
                  <% } else {
                  %><option value="cofactor">Cofactors</option>
                  <% }
                    if(field != null && field.equals("synonyms")) {
                  %><option value="synonyms" selected="selected">Other names</option>
                  <% } else {
                  %><option value="synonyms">Other names</option>
                  <% }
                    if(field != null && field.equals("systematic")) {
                  %><option value="systematic" selected="selected">Systematic name</option>
                  <% } else {
                  %><option value="systematic">Systematic name</option>
                  <% }
                    if(field != null && field.equals("comment")) {
                  %><option value="comment" selected="selected">Comments</option>
                  <% } else {
                  %><option value="comment">Comments</option>
                  <% }
                    if(field != null && field.equals("references")) {
                  %><option value="references" selected="selected">References</option>
                  <% } else {
                  %><option value="references">References</option>
                  <% }
                    if(field != null && field.equals("authors")) {
                  %><option value="authors" selected="selected">Author(s)</option>
                  <% } else {
                  %><option value="authors">Author(s)</option>
                  <% }
                    if(field != null && field.equals("title")) {
                  %><option value="title" selected="selected">Title</option>
                  <% } else {
                  %><option value="title">Title</option>
                  <% }
                    if(field != null && field.equals("patentno")) {
                  %><option value="patentno" selected="selected">Patent number</option>
                  <% } else {
                  %><option value="patentno">Patent number</option>
                  <% }
                    if(field != null && field.equals("editors")) {
                  %><option value="editors" selected="selected">Editor(s)</option>
                  <% } else {
                  %><option value="editors">Editor(s)</option>
                  <% }
                    if(field != null && field.equals("publisher")) {
                  %><option value="publisher" selected="selected">Publisher</option>
                  <% } else {
                  %><option value="publisher">Publisher</option>
                  <% }
                    if(field != null && field.equals("pubmed")) {
                  %><option value="pubmed" selected="selected">PubMed ID</option>
                  <% } else {
                  %><option value="pubmed">PubMed ID</option>
                  <% } if(field != null && field.equals("deleted")) {
                  %><option value="deleted" selected="selected">Info about deleted entries</option>
                  <% } else {
                  %><option value="deleted">Info about deleted entries</option>
                  <% } %>
                  </select>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td colspan="2" align="center"><input type="submit" value="Search" class="submit_button"></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-left: 1em;">Examples:
           <ul>
              <li>1.1.1.1</li>
              <li>alcohol%</li>
              <li>&#945;-glucosidase</li>
           </ul>
        </td>
      </tr>
    </table>
</form>
</div><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b></div>

<h2>How to search</h2>

<p>The search will be executed on all <b>approved</b> and <b>proposed enzymes</b>, <b>class</b>, <b>subclass</b>
and <b>sub-subclass</b> information stored in the database. To search for a particular term, several terms
or phrases just fill in the text field(s) above and adjust the drop-down list(s) according to your needs.
<br/>
You can use the <b>wildcard operator %</b> to find words matching a pattern like <tt>'enzyme%'</tt>,
although searches like <tt>'%enzyme%'</tt> should be avoided as such queries tend to be rather slow.
<br/>
If you would like to search for a term containing a <b>special character</b> you can use one of the
following options:</p>

<ul>
	<li>Use special characters on your keyboard (e.g. umlauts)</li>
	<li>Copy a special character from the list you will get when clicking on this icon
		<a href="javascript:windowOpenWithSize('encoding.html', 340, 400)"><img
			style="vertical-align:middle" src="images/alpha_icon.gif" width="12" height="11"
			border="1" alt="Special characters"/></a> and paste it into the search text field.</li>
	<li>Use a textual representation (e.g. 'alpha')</li>
</ul>

<p>The default setting is to look for the exact phrase, but you can also choose to search for <b>all</b> or
<b>any</b> words entered in the text field to the left:</p>

<ul>
  <li><b>All of these words</b> is the equivalent to the boolean
  	operator AND meaning that all of the given words have to be found in an entry.</li>
  <li><b>Any of these words</b> is the equivalent to the boolean
  	operator OR which is less restrictive than AND, since at least one of the given words
  	has to be found in an entry.</li>
</ul>

<p>Adding a word in the text field for <b>exclusion</b> will reduce the result.
Entries containing one of the entered words (boolean NOT operator) will not be part of the result.</p>

<p>The last drop-down list lets you define to which fields you want to <b>limit your search</b>.
Currently it is possible to search in all fields (default) or in an individual one.</p>

