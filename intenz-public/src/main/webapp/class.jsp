<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeClass"%>

<%
  EnzymeClass enzymeClass = (EnzymeClass) request.getAttribute("result");
  String title = "EC " + enzymeClass.getEc().toString();
%>
<jsp:include page="frame.jsp">
  <jsp:param name="title" value="<%= title %>"/>
  <jsp:param name="content" value="class_content.jsp"/>
</jsp:include>
