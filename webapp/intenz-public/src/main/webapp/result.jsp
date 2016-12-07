<%@ page import="uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type,
                 java.util.ArrayList,
                 java.net.URLEncoder,
                 uk.ac.ebi.intenz.webapp.controller.SearchCommand.Result"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Search results"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<%@ include file="headers.jsp" %>

<div class="grid_24 clearfix" id="content" role="main">

<%
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
%>

<script language='javascript'>
  function windowOpenWithSize(theurl, thewd, theht) {
    if(!(theht)) {
      theht = 528;
    }
    if(!(thewd)) {
      thewd = 410;
    }
    var newwin  = window.open(theurl,"labelsize","dependent=yes, resizable=yes,toolbar=no,menubar=no,scrollbars=yes,width="+thewd+",height="+theht);
    newwin.focus();
  }
</script>

<section class="grid_18 alpha">
    <h2>IntEnz results for
           <span class="searchterm">${requestScope.query}</span>
    </h2>
</section>

<aside id="search-extras" class="grid_6 omega shortcuts expander">
    <div id="ebi_search_results">
        <h3 class="slideToggle icon icon-functional" data-icon="u">Show
            more data from EMBL-EBI</h3>
    </div>
</aside>

<section id="search-results" class="grid_24">

    <div style="text-align: center" class="searchBar">
      <%
        int start = Integer.parseInt((String) request.getAttribute("st"));
        int end = Integer.parseInt((String) request.getAttribute("end"));
        int size = Integer.parseInt((String) request.getAttribute("size"));

        if(request.getAttribute("pst") != null) {
	%>
          &lt; <a href="query?cmd=BrowseResult&st=<%= request.getAttribute("pst") %>&q=<%= URLEncoder.encode((String) request.getAttribute("query"), "UTF-8") %>)">previous</a> |
	<%
        }
    %>
    <b><%= start + 1 %>
    <%
        if(start != end) {
    %>
        .. <%= end %>
    <%
        }
    %>
    of <%= size %></b>
      <%
         if(request.getAttribute("nst") != null) {
           %>
             | <a href="query?cmd=BrowseResult&st=<%= request.getAttribute("nst") %>&q=<%= URLEncoder.encode((String) request.getAttribute("query"), "UTF-8") %>">next</a> &gt;
           <%
         }
        %>
    </div>

	<c:forEach var="r" items="${requestScope.group}" varStatus="vs">
		<div class="result">
            <div style="text-align: right">
            	${requestScope.st + vs.index + 1}.
           	</div>
           	<div style="text-align: right">
           		<c:choose>
           			<c:when test="${r.ec.type eq 'ENZYME'
           					or r.ec.type eq 'PRELIMINARY'}">
						<a href="query?cmd=SearchID&id=${r.id}"
							class="${r.ec eq requestScope.query? 'queryEc':''}">
							EC ${r.ec}</a>
           			</c:when>
           			<c:otherwise>
						<a href="query?cmd=SearchEC&ec=${r.ec}">EC ${r.ec}</a>
           			</c:otherwise>
           		</c:choose>
           	</div>
           	<div class="${r.active? '':'in'}activeEntry">
           		<x:translate>${r.commonName}</x:translate>
           		<c:choose>
           			<c:when test="${r.status eq 'PR'}">
           				(proposed)
           			</c:when>
           			<c:when test="${not r.active}">
           				(inactive entry)
           			</c:when>
           		</c:choose>
           	</div>
		</div>
	</c:forEach>

</section>

<script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>
<!-- script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script -->
<script src="js/ebi-global-search-run.js"></script>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
