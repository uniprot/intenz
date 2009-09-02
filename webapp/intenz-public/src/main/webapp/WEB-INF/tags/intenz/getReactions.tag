<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Retrieves a list of reactions from an enzyme entry
		(parameter 'entry'), corresponding to a given view (parameter 'view'),
		as a variable whose name is the parameter 'var'."
	import="java.util.List,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry" %>
		
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="entry" required="true"
	type="uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry" %>
<%@ attribute name="view" required="true" %>
<%@ attribute name="var" required="true" rtexprvalue="false"
	description="Variable name for individual reactions in the loop." %>

<%@ variable name-from-attribute="var" alias="r"
	variable-class="java.util.List" scope="AT_END" %>

<%
EnzymeEntry entryParam = (EnzymeEntry) jspContext.getAttribute("entry");
String viewParam = (String) jspContext.getAttribute("view");
jspContext.setAttribute("r", entryParam.getReactions(viewParam));
%>
<c:set var="r" value="${r}"/>
