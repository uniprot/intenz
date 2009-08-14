<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Retrieves a list of links from an enzyme entry
		(parameter 'entry'), corresponding to a given view (parameter 'view'),
		as a variable whose name is the parameter 'var'. The returned list
		EXCLUDES CAS numbers."
	import="java.util.List,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeLink" %>
		
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="entry" required="true"
	type="uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry" %>
<%@ attribute name="view" required="true" %>
<%@ attribute name="var" required="true" rtexprvalue="false"
	description="Variable name for individual links in the loop. " %>

<%@ variable name-from-attribute="var" alias="l"
	variable-class="java.util.List" scope="AT_END" %>

<%
EnzymeEntry entryParam = (EnzymeEntry) jspContext.getAttribute("entry");
String viewParam = (String) jspContext.getAttribute("view");
jspContext.setAttribute("l", entryParam.getLinks(viewParam));
%>
<c:set var="l" value="${l}"/>
