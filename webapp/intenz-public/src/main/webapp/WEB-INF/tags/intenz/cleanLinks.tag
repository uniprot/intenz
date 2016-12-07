<%@ tag language="java" pageEncoding="UTF-8"
	description="Clean links in the body content: removes existing links to EC
		numbers and creates new ones (no need of linkEc tag); creates links to
		carbohydrate nomenclature and Merops where identifiers detected."
	import="uk.ac.ebi.intenz.webapp.IntEnzUtilities" %>
	
<jsp:doBody var="body" scope="page"/>

<%= IntEnzUtilities.cleanLinks((String) jspContext.getAttribute("body")) %>
