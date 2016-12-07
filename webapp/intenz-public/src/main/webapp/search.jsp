<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Advanced search"/>
    <jsp:param name="loc" value="search"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="search"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>Advanced search</h2>

<div style="display: ${empty requestScope.message? 'none' : 'block'}; color: blue">
	${requestScope.message}
</div>

<form method="get" action="query">
	<input type="hidden" name="cmd" value="Search">
	<div>
		Search
	    <select name="t" size="1">
			<option value="exact" selected="selected">exact phrase</option>
			<option value="any">any of these words</option>
			<option value="all">all of these words</option>
	    </select>:
	    <input type="text" size="50" maxlength="100" name="q"
	    	value="${requestScope.queryTF}">
		<a href="javascript:windowOpenWithSize('encoding.html', 340, 400)">
			<img style="vertical-align:middle" src="images/alpha_icon.gif"
				width="16" height="15" border="1" alt="Special characters"
				title="Use of special characters in queries">
		</a>
	</div>
	<div>
		in
		<select name="fields" size="1">
			<option id="inField_all" value="all">All fields</option>
			<option id="inField_ec" value="ec">EC number</option>
			<option id="inField_name" value="name">Common/Recommended name</option>
			<option id="inField_reaction" value="reaction">Reactions</option>
			<option id="inField_cofactor" value="cofactor">Cofactors</option>
			<option id="inField_synonyms" value="synonyms">Other names</option>
			<option id="inField_systematic" value="systematic">Systematic name</option>
			<option id="inField_comment" value="comment">Comments</option>
			<option id="inField_references" value="references">References</option>
			<option id="inField_authors" value="authors">Author(s)</option>
			<option id="inField_title" value="title">Title</option>
			<option id="inField_patentno" value="patentno">Patent number</option>
			<option id="inField_editors" value="editors">Editor(s)</option>
			<option id="inField_publisher" value="publisher">Publisher</option>
			<option id="inField_pubmed" value="pubmed">PubMed ID</option>
			<option id="inField_deleted" value="deleted">Info about deleted entries</option>
		</select>
		<script>
			if ('${requestScope.field}' == 'null' || '${requestScope.field}' == ''){
				document.getElementById('inField_all').selected = true;
			} else {
				document.getElementById('inField_'+'${requestScope.field}')
					.selected = true;
			}
	</script>
	</div>
	<div>Words to exclude:
		<input type="text" size="35" maxlength="100" name="not"
			value="${requestScope.excludedWords}">
		(NB: Separate words by comma)
	</div>

    <input type="submit" value="Search" class="submit">

</form>

<p>Examples:
<a href="query?cmd=Search&q=1.1.1.1">1.1.1.1</a>,
<a href="query?cmd=Search&q=alcohol%">alcohol%</a>,
<a href="query?cmd=Search&q=alpha-glucosidase">&#945;-glucosidase</a>.
</p>

<h3>How to search</h3>

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

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
