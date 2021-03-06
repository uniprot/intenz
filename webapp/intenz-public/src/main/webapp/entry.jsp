<%@ page import="uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.enzyme.*,
                 uk.ac.ebi.intenz.webapp.IntEnzUtilities,
                 java.util.List,
                 java.util.ArrayList,
                 java.util.Map,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeEntryHelper"%>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags/intenz" %>
<%@ taglib prefix="b" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - EC ${requestScope['result'].ec}"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value=""/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<c:set var="entry" value="${requestScope['result']}"/>

<%
  final String viewParam = request.getParameter("view");
  EnzymeViewConstant view = null;
  if(viewParam != null) view = EnzymeViewConstant.valueOf(viewParam);
  else view = EnzymeViewConstant.INTENZ;
  //todo:uncomment for all views
 //  else view = EnzymeViewConstant.IUBMB;
  EnzymeEntry enzymeEntry = (EnzymeEntry) request.getAttribute("result");
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
%>
<div id="cf">
    <h3 style="margin-left: 0em">
	    <a href="query?cmd=SearchEC&ec=${entry.ec.ec1}">EC ${entry.ec.ec1} -
	    	<x:translate>${entry.className}</x:translate></a>
    </h3>
    <h3 style="margin-left: 1em">
	    <a href="query?cmd=SearchEC&ec=${entry.ec.ec1}.${entry.ec.ec2}">EC
	    	${entry.ec.ec1}.${entry.ec.ec2} -
	    	<x:translate>${entry.subclassName}</x:translate></a>
    </h3>
    <h3 style="margin-left: 2em">
	    <a href="query?cmd=SearchEC&ec=${entry.ec.ec1}.${entry.ec.ec2}.${entry.ec.ec3}">EC
	    	${entry.ec.ec1}.${entry.ec.ec2}.${entry.ec.ec3}
			<c:if test="${not empty entry.subSubclassName}">
				- <x:translate>${entry.subSubclassName}</x:translate>
			</c:if>
		</a>
    </h3>
</div>

<h2 style="margin-left: 3em">
	<c:if test="${not empty applicationScope.spotlightsMap[entry.ec]}">
		<div class="entryNav">
			<a href="spotlight.jsp?ec=${entry.ec}"
				class="print">Read more about EC ${entry.ec} ...</a>
		</div>
 	</c:if>
	EC ${entry.ec}
	<c:if test="${not empty entry.commonName.name}">
		- <x:translate><b:ucfl>${entry.commonName.name}</b:ucfl></x:translate>
	</c:if>
</h2>

<div class="entryNav">
	<a href="javascript:iconToggle('cf', 'cf_switch', 'Show classification', 'Hide classification');"
		 id="cf_switch" class="icon icon-functional" data-icon="h"
		 style="font-size: 200%">
		 </a>
</div>

<%-- = EnzymeEntryHelper.toHTML(enzymeEntry, encoding, null, view) --%>

<div class="viewsTabBar">&nbsp;
<c:forEach var="view" items="${applicationScope.views}">
	<span class="${(empty param.view and view eq 'INTENZ')
		or param.view eq view? '' : 'un'}selected viewTab">
		<c:choose>
			<c:when test="${(empty param.view and view eq 'INTENZ') or param.view eq view}">
				${view.label} view
			</c:when>
			<c:otherwise>
				<a href="query?cmd=SearchID&id=${entry.id}&view=${view}">
					${view.label} view</a>
			</c:otherwise>
		</c:choose>
	</span>
</c:forEach>
</div>

<c:choose>
	<c:when test="${param.view eq 'SIB'}">
		<p style="font-size: larger"><b>ENZYME: </b>${entry.ec}
            <c:if test="${entry.status eq 'PROPOSED'}">(proposal)</c:if>
            <c:if test="${entry.status eq 'PRELIMINARY'}">
                <div>This is a preliminary EC number. The content
                <c:choose>
                    <c:when test="${entry.history.deletedRootNode or entry.history.transferredRootNode}">
                        was
                    </c:when>
                    <c:otherwise>
                        has not yet been
                    </c:otherwise>
                </c:choose>
                validated and included in the official IUBMB Enzyme list.</div>
            </c:if>
        </p>
		<hr/>
		<div><pre><i:createFfLinks><i:sibView entry="${entry}"/></i:createFfLinks></pre></div>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${entry.history.deletedRootNode or entry.history.transferredRootNode}">
				<%@include file="deleted-transferred.jspf" %>
			</c:when>
			<c:otherwise>
				<%@include file="intenz_view.jspf" %>
<%--
 				<c:choose>
					<c:when test="${param.view eq 'IUBMB'}">
						<%@include file="iubmb_view.jspf" %>
					</c:when>
					<c:otherwise>
						<%@include file="intenz_view.jspf" %>
					</c:otherwise>
				</c:choose>
 --%>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>

<br/>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
