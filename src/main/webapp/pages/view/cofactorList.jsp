<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>

<style>
.even {
	background-color: #eee;
}
.odd {
	background-color: #ddd;
}
.cofactorsTableHeader {
	background-color: #c0d;
}
.cofactorsTableHeader a {
	color: #fff;
}
a img.view {
	border: solid 1px blue;
}
a img.edit {
	border: solid 1px red;
}
th.sortable:before {
	content: url("img/sort.png");
}
th.sorted {
	text-decoration: underline;
}
th.sorted.order1:before {
	content: url("img/sortAsc.png");
}
th.sorted.order2:before {
	content: url("img/sortDesc.png");
}
</style>

<logic:messagesPresent message="false">
	<html:messages id="error" header="errors.header" footer="errors.footer">
		<li class="error">
			<xchars:translate><bean:write name="error" filter="false"/></xchars:translate>
		</li>
	</html:messages>
</logic:messagesPresent>

<logic:messagesPresent message="true">
	<html:messages id="info" header="info.header" footer="info.footer" message="true">
		<li class="info">
			<xchars:translate><bean:write name="info" filter="false"/></xchars:translate>
		</li>
	</html:messages>
</logic:messagesPresent>

<h1>IntEnz cofactors</h1>
<div style="font-size: small;">Cofactors in IntEnz are automatically updated
with changes in ChEBI, unless they appear also as reaction participants.<br/>
Any changes of mappings to ChEBI IDs here won't affect reaction participants
in Rhea.
</div>

<display:table id="cofactor" export="false" name="requestScope.results"
	requestURI="cofactorList.do"
	pagesize="100" sort="list" defaultsort="2" defaultorder="descending">
    <display:column title="Cofactor" sortable="true"
        headerClass="cofactorsTableHeader">
        <form action="assignChebiId.do">
	        <b><xchars:translate>${cofactor.key}</xchars:translate></b>
	        <br/>
	        <logic:empty name="cofactor" property="key.accession">
		        (No ChEBI mapping)
	        </logic:empty>
	        <logic:notEmpty name="cofactor" property="key.accession">
		        [${cofactor.key.accession}]
		        <a href="http://www.ebi.ac.uk/chebi/searchId.do?chebiId=${cofactor.key.accession}"
		        	title="View ${cofactor.key.accession}" target="_blank"><img
		        	class="view" src="img/Chebi16.gif"
		        	alt="View in ChEBI public website"/></a>
		        <a href="http://www.ebi.ac.uk/internal-tools/chebi/curation/viewEditRheaCompound.do?chebiId=${cofactor.key.accession}"
		        	title="Edit ${cofactor.key.accession}" target="_blank"><img
		        	class="edit" src="img/Chebi16.gif"
		        	alt="Edit in ChEBI curator tool"/></a>
	        </logic:notEmpty>
	        <br/>
        	<input type="hidden" name="cofactor" value="true"/>
        	<input type="hidden" name="name" value="${cofactor.key.name}">
        	<button name="id" value="${cofactor.key.id}">
        		<img src="img/Properties16.gif" alt=""/>
        		${empty cofactor.key.accession?'Add': 'Change'} ChEBI mapping
        	</button>
       	</form>
    </display:column>
    <display:column title="Number of enzymes" sortable="true" sortProperty="value"
    	comparator="uk.ac.ebi.intenz.webapp.utilities.SizeComparator"
        headerClass="cofactorsTableHeader" style="text-align: left">
        ${fn:length(cofactor.value)}
		<button type="button" id="${cofactor.key.id}toggleLink"
			onclick="fold('ecListCofactor${cofactor.key.id}', '${cofactor.key.id}toggleLink', 'Show list', 'Hide list')"
			style="font-style: italic; font-size: small;">Show list</button>
		<div style="display: none" id="ecListCofactor${cofactor.key.id}">
	        <logic:iterate id="ec" name="cofactor" property="value" indexId="i">
	        	${i gt 0? ', ': ''}
	        	<a target="_blank" href="searchEc.do?ec=${ec}">EC ${ec}</a>
	        </logic:iterate>
		</div>
	</display:column>
</display:table>


