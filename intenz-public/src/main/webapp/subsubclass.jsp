<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass"%>

<%
  EnzymeSubSubclass essc = (EnzymeSubSubclass) request.getAttribute("result");
  String title = "EC " + essc.getEc().toString();
%>
<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="subsubclass_content.jsp"/>
</jsp:include>
