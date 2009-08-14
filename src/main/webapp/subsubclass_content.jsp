<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass,
                 uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
                 java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeEntryHelper"%>

<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  EnzymeSubSubclass enzymeSubSubclass = (EnzymeSubSubclass) request.getAttribute("result");
  List enzymeEntries = enzymeSubSubclass.getEntries();
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
%>

<!-- CLASSIF -->
<div id="cf">
    <h3 style="margin-left: 0em">
	    <a href="query?cmd=SearchEC&ec=${requestScope['result'].ec.ec1}">EC
	    	${requestScope['result'].ec.ec1} -
	    	<x:translate>${requestScope['result'].className}</x:translate></a>
    </h3>
    <h3 style="margin-left: 1em">
	    <a href="query?cmd=SearchEC&ec=${requestScope['result'].ec.ec1}.${requestScope['result'].ec.ec2}">EC
	    	${requestScope['result'].ec.ec1}.${requestScope['result'].ec.ec2} -
 	    	<x:translate>${requestScope['result'].subclassName}</x:translate></a>
    </h3>
</div>

<h1 style="margin-left: 2em">EC ${requestScope['result'].ec}
	<c:if test="${not empty requestScope['result'].name}">
		- <x:translate>${requestScope['result'].name}</x:translate>
	</c:if>
</h1>

<!-- END OF CLASSIF -->

<script type="text/javascript" language='JavaScript'>
  function link(id, ec) {
    document.forms.linkForm.id.value = id;
    document.forms.linkForm.action = "query?cmd=SearchEC&ec=" + ec;
    document.forms.linkForm.submit();
  }
</script>

<h2>Contents</h2>

<br>

<table align="center" border="0">
	<c:forEach var="enzymeEntry" items="${requestScope['result'].entries}">
		<%-- TO DO --%>
	</c:forEach>
  <%
    for (int iii = 0; iii < enzymeEntries.size(); iii++) {
      EnzymeEntry enzymeEntry = (EnzymeEntry) enzymeEntries.get(iii);
  %>
    <%= EnzymeEntryHelper.toHTML(enzymeEntry, encoding, null, EnzymeViewConstant.INTENZ) %>
  <%
    }
  %>
</table>
