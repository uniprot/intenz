<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry"%>

<%
  EnzymeEntry entry = (EnzymeEntry) request.getAttribute("result");
  String title = "EC " + entry.getEc().toString();
%>
<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="entry_content.jsp"/>
</jsp:include>
