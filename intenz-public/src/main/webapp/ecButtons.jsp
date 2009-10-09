<%@page import="uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
				uk.ac.ebi.intenz.domain.enzyme.*" %>

<%	String view = null;
	if (request.getParameter("view") != null)
		view = EnzymeViewConstant.valueOf(request.getParameter("view")).toString();
	else
		view = EnzymeViewConstant.INTENZ.toString();
	String ac = null;
	Object result = request.getAttribute("result");
	if (result instanceof EnzymeEntry) {
		EnzymeEntry entry = (EnzymeEntry) request.getAttribute("result");
		ac = entry.getId().toString();
	} else if (result instanceof EnzymeSubSubclass) {
		EnzymeSubSubclass essc = (EnzymeSubSubclass) request.getAttribute("result");
		ac = essc.getEc().toString();
	} else if (result instanceof EnzymeSubclass) {
		EnzymeSubclass esc = (EnzymeSubclass) request.getAttribute("result");
		ac = esc.getEc().toString();
	} else if (result instanceof EnzymeClass) {
		EnzymeClass ec = (EnzymeClass) request.getAttribute("result");
		ac = ec.getEc().toString();
	}
	if (result instanceof EnzymeEntry){ %>
<div class="entryNav">
	<a href="javascript:iconToggle('cf', 'cf_switch', 'Show classification', 'Hide classification');"
		 id="cf_switch" class="print"><img src="images/classif.png" border="0" /></a>
</div><% } %>

<div class="entryNav">
	<a href="query?cmd=PrinterFriendlyView&ac=<%= ac %>&enc=unicode&view=<%= view %>"
	 	class="print" title="Printer-friendly view"><img src="images/print.png" border="0" /></a>
</div>
