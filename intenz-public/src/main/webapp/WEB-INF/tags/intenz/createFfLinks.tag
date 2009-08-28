<%@ tag language="java" pageEncoding="UTF-8"
	description="Creates hypertext links from a flat-file version of an enzyme
		entry (this tag's body) to its cross-references."
	import="uk.ac.ebi.intenz.webapp.IntEnzUtilities"
%><jsp:doBody var="body" scope="page"
/><%= IntEnzUtilities.getLinkedFlatFile((String) jspContext.getAttribute("body")) %>