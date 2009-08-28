<%@tag language="java" pageEncoding="UTF-8"
    body-content="empty"
    description="Renderer for the reaction participants in one side of a reaction" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="r" tagdir="/WEB-INF/tags/rhea" %>

<%@attribute name="participants"
    required="true"
    description="The participants to render."
    type="java.util.Collection" %>

<c:forEach var="rp" items="${participants}" varStatus="i">
    <r:reactionParticipant rp="${rp}"/>
    ${not i.last? '+' : ''}
</c:forEach>