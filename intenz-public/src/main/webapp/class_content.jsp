<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeClass,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass,
                 uk.ac.ebi.xchars.SpecialCharacters,
                 java.util.List,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeClassHelper"%>

<%
  EnzymeClass enzymeClass = (EnzymeClass) request.getAttribute("result");
  List subclasses = enzymeClass.getSubclasses();
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
  response.setContentType("text/html; charset=UTF-8");
%>

<!-- START CLASSIF -->

<div id="cf">
	<h1>EC <%= enzymeClass.getEc().toString() %> - <%= encoding.xml2Display(enzymeClass.getName()) %></h1>
	<h2>Description</h2>
	<div><%= EnzymeClassHelper.descriptionToHTML(enzymeClass, encoding, null) %></div>
</div>

<!-- END CLASSIF -->

<h2>Contents</h2>

<table align="center" border="0">
  <%
    for (int iii = 0; iii < subclasses.size(); iii++) {
      EnzymeSubclass enzymeSubclass = (EnzymeSubclass) subclasses.get(iii);
  %>
  <tr>
    <td valign="top" align="left">
      <a href="query?cmd=SearchEC&ec=<%= enzymeSubclass.getEc().toString() %>">EC
      	<%= enzymeSubclass.getEc().toString() %></a>
    </td>
    <td valign="top" align="left">
      <%= encoding.xml2Display(enzymeSubclass.getName()) %>
    </td>
  </tr>
  <%
    }
  %>
</table>
