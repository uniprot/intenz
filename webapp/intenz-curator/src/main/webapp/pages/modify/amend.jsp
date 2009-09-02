<%@ page import="uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>

<script type="text/javascript" src="resources/form.js"></script>

<%
	EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
	String update = (String) request.getAttribute("update");
	String modification = "Amend";
	String modifAction = modification.toLowerCase() + "Entry" + update + ".do";
	String formButtonAction = "formButton" + modification + update + ".do";
	String previewAction = "preview" + modification + "Entry" + update + ".do";

	request.setAttribute("modification", modification);
	request.setAttribute("modifAction", modifAction);
	request.setAttribute("formButtonAction", formButtonAction);
	request.setAttribute("previewAction", previewAction);
%>
<%@ include file="modify.jsp" %>