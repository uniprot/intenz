<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Returns a hypertext link to an external database entry,
		based on EC number as an identifier."
	import="uk.ac.ebi.intenz.domain.enzyme.EnzymeLink" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
	
<%@ attribute name="enzymeLink" required="true"
	description="The EnzymeLink object to link to."
	type="uk.ac.ebi.intenz.domain.enzyme.EnzymeLink" %>
<%@ attribute name="ec" required="true"
	description="The EC number." %>

<%
jspContext.setAttribute("fullUrl",
	((EnzymeLink) jspContext.getAttribute("enzymeLink"))
		.getFullUrl((String) jspContext.getAttribute("ec")));
%>
<fmt:setBundle basename="linksTitles" var="linksTitles"/>
<c:set var="linkTitle">
    <c:choose>
        <c:when test="${enzymeLink.xrefDatabaseConstant eq 'DIAGRAM'
            or enzymeLink.xrefDatabaseConstant eq 'OMIM'}">${enzymeLink.name}</c:when>
        <c:when test="${enzymeLink.xrefDatabaseConstant eq 'GO'}">
            <fmt:message bundle="${linksTitles}" key="${enzymeLink.xrefDatabaseConstant.databaseCode}">
                <fmt:param value="${enzymeLink.name}"/>
            </fmt:message>
        </c:when>
        <c:otherwise>
            <fmt:message bundle="${linksTitles}" key="${enzymeLink.xrefDatabaseConstant.databaseCode}"/>
        </c:otherwise>
    </c:choose>
</c:set>
<jsp:element name="a">
	<jsp:attribute name="href">${fullUrl}</jsp:attribute>
	<jsp:attribute name="target">_blank</jsp:attribute>
	<jsp:attribute name="title">${f:trim(linkTitle)}</jsp:attribute>
	<jsp:body>
		<c:choose>
			<c:when test="${enzymeLink.xrefDatabaseConstant eq 'PROSITE'}">PROSITE:${enzymeLink.accession}</c:when>
			<c:when test="${enzymeLink.xrefDatabaseConstant eq 'GO'}">${enzymeLink.accession}</c:when>
			<c:when test="${enzymeLink.xrefDatabaseConstant eq 'Swiss-Prot'}">${enzymeLink.accession}</c:when>
			<c:otherwise>${enzymeLink.xrefDatabaseConstant.displayName}</c:otherwise>
		</c:choose>
	</jsp:body>
</jsp:element>
