<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Returns hyperlinked cofactors (may be only one, maybe a
		complex combination)."
	import="java.util.Set,
		uk.ac.ebi.intenz.domain.enzyme.Cofactor" %>
		
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="i" tagdir="/WEB-INF/tags/intenz" %>

<%@ attribute name="cofactors" required="true"
	type="java.lang.Object" %>
<%@ attribute name="useBrakets" required="false"
	type="java.lang.Boolean"
	description="Use brakets to group cofactors? Defaults to false." %>

<%
Object cofactorObj = jspContext.getAttribute("cofactors");
if (cofactorObj instanceof Cofactor){
%>
	<i:linkCofactor cofactor="${cofactors}"/>
<%
} else if (cofactorObj instanceof Set<?>){
%>
	${useBrakets? '(' : ''}
	<c:forEach var="cf" items="${cofactors}" varStatus="vs">
		<i:linkCofactors cofactors="${cf}" useBrakets="${fn:length(cofactors) gt 1}"/>
		<c:if test="${cofactors.class.simpleName eq 'OperatorSet' and not vs.last}">
			<c:choose>
				<c:when test="${fn:startsWith(cofactors.operator, 'OR')}">or</c:when>
				<c:when test="${fn:startsWith(cofactors.operator, 'AND')}">and</c:when>
			</c:choose>
		</c:if>
	</c:forEach>
	${useBrakets? ')' : ''}
<%
}
%>
