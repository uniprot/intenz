<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.Map,
		java.util.Properties,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber"%>
<form>
	<select onchange="document.location = 'spotlight.jsp?ec=' + this.value">
	<%
	final Map<EnzymeCommissionNumber, String> spotlightsMap = (Map<EnzymeCommissionNumber, String>) application.getAttribute("spotlightsMap");
	String ecParam = request.getParameter("ec");
	EnzymeCommissionNumber spotEc = null;
	try {
		spotEc = EnzymeCommissionNumber.valueOf(ecParam);
	} catch (Exception e){}
	if (spotEc != null && !spotlightsMap.containsKey(spotEc)) spotEc = null;
	for (EnzymeCommissionNumber ec: spotlightsMap.keySet()){
		if (spotEc == null) spotEc = ec; %>
		<option value="<%= ec %>" <% if (ec.equals(spotEc)){ %>selected<% } %>>EC <%= ec %>
			- <%= spotlightsMap.get(ec) %></option>
	<% } %>
	</select>
</form>

<div style="margin-top: 1em">
<jsp:include flush="true"
	page='<%= "spotlight/" + spotEc + ".html"%>'></jsp:include>
</div>
