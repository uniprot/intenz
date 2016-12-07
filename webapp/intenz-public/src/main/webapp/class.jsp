<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeClass,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass,
                 uk.ac.ebi.xchars.SpecialCharacters,
                 java.util.List,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeClassHelper"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - EC ${requestScope['result'].ec}"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="browse"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<%
  EnzymeClass enzymeClass = (EnzymeClass) request.getAttribute("result");
  List subclasses = enzymeClass.getSubclasses();
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
  response.setContentType("text/html; charset=UTF-8");
%>

<!-- START CLASSIF -->

<div id="cf">
	<h2>EC <%= enzymeClass.getEc().toString() %> - <%= encoding.xml2Display(enzymeClass.getName()) %></h2>
	<h3>Description</h3>
	<p><%= EnzymeClassHelper.descriptionToHTML(enzymeClass, encoding, null) %></p>
</div>

<!-- END CLASSIF -->

<h3>Contents</h3>

  <%
    for (int iii = 0; iii < subclasses.size(); iii++) {
      EnzymeSubclass enzymeSubclass = (EnzymeSubclass) subclasses.get(iii);
  %>
  <div class="result">
  	<div>
      <a href="query?cmd=SearchEC&ec=<%= enzymeSubclass.getEc().toString() %>"
      	style="white-space: nowrap;">EC
      	<%= enzymeSubclass.getEc().toString() %></a>
  	</div>
    <div>
      <%= encoding.xml2Display(enzymeSubclass.getName()) %>
    </div>
  </div>
  <%
    }
  %>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
