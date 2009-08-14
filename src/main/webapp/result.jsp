<%
	String query = request.getAttribute("query").toString();
	String title = "Search result for '" + query + "'"; %>

<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="result_content.jsp"/>
</jsp:include>
