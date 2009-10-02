<%@ page import="uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper,
                 uk.ac.ebi.intenz.webapp.dtos.CofactorDTO,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities,
                 uk.ac.ebi.xchars.SpecialCharacters" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="preview" value="true" scope="request"/>
<%@ include file="intenz_entry.jsp" %>
