<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - No results"/>
</jsp:include>

<body class="level2 noresults">
<div id="wrapper" class="container_24">

<%@ include file="headers.jsp" %>

<div class="grid_24 clearfix" id="content" role="main">

	<section class="grid_18 alpha">
	    <h2>No IntEnz results found</h2>
	</section>
	
	<aside id="search-extras" class="grid_6 omega shortcuts expander">
	    <div id="ebi_search_results">
	        <h3 class="slideToggle icon icon-functional" data-icon="u">Show
	            more data from EMBL-EBI</h3>
	    </div>
	</aside>

	<section class="grid_24">
		<p class="alert">${requestScope.message}</p>
		<h3>Try the advanced search</h3>
		<p>You can also create a more complex query using our
		    <a href="search.jsp">advanced search</a>.</p>
		<ul>
		    <li>Search by field</li>
		    <li>Exclude results by keyword</li>
		</ul>
	</section>

	<script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>
	<!-- script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script -->
	<script src="js/ebi-global-search-run.js"></script>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
