<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass"%>

<%
  EnzymeSubclass esc = (EnzymeSubclass) request.getAttribute("result");
  String title = "EC " + esc.getEc().toString();
%>
<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="subclass_content.jsp"/>
</jsp:include>
