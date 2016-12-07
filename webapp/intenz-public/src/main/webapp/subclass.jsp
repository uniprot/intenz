<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass,
    uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass,
    uk.ac.ebi.xchars.SpecialCharacters,
    java.util.List,
    java.util.ArrayList,
    uk.ac.ebi.intenz.webapp.helper.EnzymeSubclassHelper"%>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - EC ${requestScope['result'].ec}"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="browse"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<%
	EnzymeSubclass enzymeSubclass = (EnzymeSubclass) request
			.getAttribute("result");
	SpecialCharacters encoding = (SpecialCharacters) request
			.getSession().getServletContext()
			.getAttribute("characters");
%>


<!-- START CLASSIF -->

<div id="cf">
    <h3 style="margin-left: 0em">
	    <a href="query?cmd=SearchEC&ec=${requestScope['result'].ec.ec1}">EC
	    	${requestScope['result'].ec.ec1} -
	    	<x:translate>${requestScope['result'].className}</x:translate></a>
    </h3>
</div>

<h2 style="margin-left: 1em">EC ${requestScope['result'].ec} -
	<x:translate>${requestScope['result'].name}</x:translate></h2>

<h3>Description</h3>
<p><%=EnzymeSubclassHelper.descriptionToHTML(enzymeSubclass,
							encoding, null)%></p>

<!-- END OF CLASSIF -->

<h3>Contents</h3>

<c:forEach var="enzymeSubSubclass" items="${requestScope['result'].subSubclasses}">
	<div class="result">
		<div>
			<a href="query?cmd=SearchEC&ec=${enzymeSubSubclass.ec}"
				style="white-space: nowrap;">EC ${enzymeSubSubclass.ec}</a>
		</div>
		<div>
			<x:translate>${enzymeSubSubclass.name}</x:translate>
		</div>
	</div>
</c:forEach>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
