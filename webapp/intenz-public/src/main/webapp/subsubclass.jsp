<%@ page import="uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass,
                 uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
                 java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeEntryHelper"%>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i" uri="http://www.ebi.ac.uk/intenz" %>
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

<h2 style="margin-left: 2em">EC ${requestScope['result'].ec}
	<c:if test="${not empty requestScope['result'].name}">
		- <x:translate>${requestScope['result'].name}</x:translate>
	</c:if>
</h2>

<!-- END OF CLASSIF -->

<script type="text/javascript" language='JavaScript'>
  function link(id, ec) {
    document.forms.linkForm.id.value = id;
    document.forms.linkForm.action = "query?cmd=SearchEC&ec=" + ec;
    document.forms.linkForm.submit();
  }
</script>

<h3>Contents</h3>

<c:forEach var="enzymeEntry" items="${requestScope['result'].entries}">
	<div class="result ${enzymeEntry.status}">
		<div>
			<a href="query?cmd=SearchEC&ec=${enzymeEntry.ec}"
				style="white-space: nowrap;">EC ${enzymeEntry.ec}</a>
		</div>
		<div class="${enzymeEntry.active? '':'in'}activeEntry">
			<c:choose>
				<c:when test="${enzymeEntry.active}">
					<x:translate>${enzymeEntry.commonName.name}</x:translate>
				</c:when>
				<c:when test="${enzymeEntry.history.deletedRootNode}">
					(deleted)
				</c:when>
				<c:when test="${enzymeEntry.history.transferredRootNode}">
					<c:set var="latestHistoryEvent"
						value="${enzymeEntry.history.latestHistoryEventOfRoot}"/>
					<i:linkEc>
					<c:choose>
						<c:when test="${not empty latestHistoryEvent.note}">
							${latestHistoryEvent.note}
						</c:when>
						<c:otherwise>
							Transferred to EC
							${latestHistoryEvent.afterNode.enzymeEntry.ec}
						</c:otherwise>
					</c:choose>
					</i:linkEc>
				</c:when>
			</c:choose>
		</div>
	</div>
</c:forEach>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
