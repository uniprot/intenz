<%@ tag language="java" pageEncoding="UTF-8"
	description="Links EC numbers contained in the tag body to their IntEnz entry."
	import="uk.ac.ebi.intenz.webapp.IntEnzUtilities" %>
	
<jsp:doBody var="body" scope="page"/>

<%= IntEnzUtilities.linkEcNumbers((String) jspContext.getAttribute("body")) %>
