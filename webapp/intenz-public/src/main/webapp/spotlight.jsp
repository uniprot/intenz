<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.Map,
		java.util.Properties,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Spotlight"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="docs"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<section class="grid_18 alpha">

<h2>Enzyme spotlights</h2>

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

</section>

<section class="grid_6 omega">
    <%@ include file="docs.html" %>
</section>

<section>

<div style="margin-top: 1em">
<jsp:include flush="true"
	page='<%= "spotlight/" + spotEc + ".html"%>'></jsp:include>
</div>

</section>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
