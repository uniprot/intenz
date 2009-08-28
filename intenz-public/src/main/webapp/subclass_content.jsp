<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass,uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass,uk.ac.ebi.xchars.SpecialCharacters,java.util.List,java.util.ArrayList,uk.ac.ebi.intenz.webapp.helper.EnzymeSubclassHelper"%>

<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<h1 style="margin-left: 1em">EC ${requestScope['result'].ec} -
	<x:translate>${requestScope['result'].name}</x:translate></h1>

<h2>Description</h2>
<div><%=EnzymeSubclassHelper.descriptionToHTML(enzymeSubclass,
							encoding, null)%></div>

<!-- END OF CLASSIF -->

<h2>Contents</h2>

<table align="center" border="0">
	<c:forEach var="enzymeSubSubclass" items="${requestScope['result'].subSubclasses}">
		<tr>
			<td valign="top" align="left">
				<a href="query?cmd=SearchEC&ec=${enzymeSubSubclass.ec}">EC
					${enzymeSubSubclass.ec}</a>
			</td>
			<td valign="top" align="left">
				<x:translate>${enzymeSubSubclass.name}</x:translate>
			</td>
		</tr>
	</c:forEach>
</table>
