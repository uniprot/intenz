<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="uk.ac.ebi.rhea.domain.Qualifier,
    	uk.ac.ebi.rhea.domain.Status"%>
<%
	pageContext.setAttribute("reactionQualifiers", Qualifier.values());
	pageContext.setAttribute("reactionStatus", Status.values());
%>

<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/scriptaculous/scriptaculous.js"></script>
<script type="text/javascript" src="js/scriptaculous/controls.js"></script>
<script type="text/javascript" src="js/ajaxtags.js"></script>
<script type="text/javascript" src="js/overlibmws/overlibmws.js"></script>
<script type="text/javascript" src="js/rhea-view.js"></script>
<script type="text/javascript" src="js/rhea-search.js"></script>
<script type="text/javascript" src="js/rhea-search-options.js"></script>
<script type="text/javascript" src="js/rhea-search-for-intenz.js"></script>

<style>
<!--
@import "css/rhea-view.css";
@import "css/rhea-search.css";
@import "css/rheaSelector.css";
-->
</style>

<%@ include file="anyReaction.jsp" %>
