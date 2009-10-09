<%
	Object query = request.getAttribute("query");
	if (query == null) query = "no query";
	String title = "No result for '" + query + "'";
%>

<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="noResult_content.jsp"/>
</jsp:include>
