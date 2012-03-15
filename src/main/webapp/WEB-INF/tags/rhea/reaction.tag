<%@tag language="java" pageEncoding="UTF-8"
    body-content="empty"
    description="Renderer for a Rhea reaction" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>
<%@ taglib prefix="r" tagdir="/WEB-INF/tags/rhea" %>

<%@attribute name="reaction"
	rtexprvalue="true"
    description="The reaction to render."
    required="true"
    type="uk.ac.ebi.rhea.domain.Reaction" %>
<%@attribute name="iubmb"
	rtexprvalue="true"
	description="Flag as IUBMB reaction?"
	type="java.lang.Boolean" %>

<div>
    <span style="margin-right: 1em"><a target="rheaFromIntEnz"
        href="http://www.ebi.ac.uk/rhea/reaction.xhtml?id=${reaction.id}">RHEA:${reaction.id}</a>
    </span>
    <c:if test="${iubmb}"><b>[IUBMB]</b></c:if>
</div>
<div style="display: table-row">
    <r:reactionSide
        participants="${reaction.direction.reactantsSide.code eq 'R'?
            reaction.rightSide : reaction.leftSide}" />
    <c:choose>
        <c:when test="${not empty reaction.direction.reactantsSide}">=&gt;</c:when>
        <c:otherwise>${reaction.direction.label}</c:otherwise>
    </c:choose>
    <r:reactionSide
        participants="${reaction.direction.productsSide.code eq 'L'?
            reaction.leftSide : reaction.rightSide}" />
</div>
<c:if test="${reaction.complex}">
    <ol>
    <c:forEach var="child" items="${reaction.children}">
        <li style="list-style-type: ${reaction.stepwise? 'decimal' : 'disc'}}">
            ${child.coef} × [<a target="rheaFromIntEnz"
            href="http://www.ebi.ac.uk/rhea/reaction.xhtml?id=${child.reaction.id}">RHEA:${child.reaction.id}</a>]
            <x:translate>${fn:replace(fn:replace(fn:replace(
                fn:replace(child.reaction.textualRepresentation, '<?>', '&lt;?&gt;'),
                '<=>', '&lt;=&gt;'), '<=', '&lt;='), '=>', '=&gt;')}</x:translate>
        </li>
    </c:forEach>
    </ol>
</c:if>
