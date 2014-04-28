<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" prefix="ajax"%>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/ajaxtags.js"></script>
<script type="text/javascript" src="js/rhea-view.js"></script>
<script type="text/javascript" src="js/rhea-search.js"></script>
<script type="text/javascript" src="js/assignChebiId.js"></script>

<style type="text/css">
@IMPORT url("${pageContext.request.contextPath}/css/rhea-view.css");
@IMPORT url("${pageContext.request.contextPath}/css/rhea-search.css");
</style>

<h2>Assign a ChEBI ID:</h2>

<p>Re-assign the ChEBI entity associated to a cofactor in IntEnz.</p>
<p class="warning">Please note that this change affects every enzyme using the
    cofactor (if not sure, check the <a href="cofactorList.do">list</a>).</p>

<form action="processChebiIdAssignment.do" method="post">

<div id="intenzCompoundBox" style="float: left">

	<h3>IntEnz compound:</h3>
    <div id="intenzCompound">
        <jsp:include flush="true" page="/getCompound.do">
            <jsp:param name="id" value="${param.id}" />
        </jsp:include>
    </div>
	<div id="getChebiCompoundBox" style="margin-top: 1ex;">	    
	    <div class="fillAndClickBox" style="margin-top: 1em">
	        <div id="chebiCompoundIndicator"
                 style="display: none; float: right"><img
                    src="img/indicator.gif" alt=""/></div>
	        <b>Assign ChEBI ID:</b>
	        <input type="text" id="chebiId"/>
	        <button type="button" id="searchChebiButton">Check ChEBI ID
	            <img src="img/Forward16.gif" alt=""/></button>
	        <ajax:htmlContent baseUrl="getCompound.do"
	            source="searchChebiButton"
	            eventType="click"
	            target="chebiCompound"
	            preFunction="checkChebiAccession"
	            postFunction="checkCompoundId"
	            errorFunction="hideIndicator"
	            var="ajaxResponse"
	            parameters="accession={chebiId}"/>
	    </div>
	</div>
</div>

<div id="chebiCompoundBox" style="float: right">
    <h3>ChEBI compound:</h3>
    <div id="chebiCompound"></div>

    <button type="submit" id="assignChebiIdButton" style="display: none;">
        (No replacement selected)
    </button>
</div>

<input type="hidden" id="compoundId" name="compoundId" value="${param.id}"/>
<input type="hidden" id="oldChebiId" name="oldChebiId" value="${param.oldChebiId}"/>
<input type="hidden" id="oldName" name="oldName" value="${param.oldName}"/>
<input type="hidden" id="newChebiId" name="newChebiId" value=""/>
<input type="hidden" id="newName" name="newName" value=""/>

</form>
