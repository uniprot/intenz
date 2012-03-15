<%@tag language="java" pageEncoding="UTF-8"
    body-content="empty"
    description="Renderer for a reaction participant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>

<%@attribute name="rp"
    required="true"
    description="The reaction participant to render."
    type="uk.ac.ebi.rhea.domain.ReactionParticipant" %>

<div style="text-align: center; display: table-cell; padding: 0.5ex 0.5em; white-space: nowrap">

    <c:if test="${rp.coefficient.type ne 'P'
        and ((rp.coef eq 1 and rp.coefficient.type ne 'F') or rp.coef gt 1)}">
        <b>${rp.coefficient}</b></c:if>

    <a target="chebiFromIntEnz" class="participant-coef-${rp.coefficient.type}"
        style="text-decoration: none !important"
        href="http://www.ebi.ac.uk/chebi/searchId.do?chebiId=${rp.compound.accession}"
        title="${rp.compound.accession}">
        <x:translate>${rp.compound.name}</x:translate>
    </a>

    <c:if test="${rp.coefficient.type eq 'P'}">${rp.coefficient}</c:if>

    <br/>

</div>