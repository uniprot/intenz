<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
  <head>
    <title><%= request.getAttribute("title") %> - Integrated Enzyme Database (IntEnz)</title>
    <link rel="stylesheet" href="<html:rewrite forward='intenzStyle'/>" type="text/css">
    <link rel="stylesheet" href="<html:rewrite forward='tabbedMenuStyle'/>" type="text/css">
    
	<link rel="search" href="<html:rewrite forward='searchEc'/>"
	      type="application/opensearchdescription+xml"
	      title="IntEnz curator - EC"/>
	<link rel="search" href="<html:rewrite forward='searchText'/>"
	      type="application/opensearchdescription+xml"
	      title="IntEnz curator - Text"/>

	<!-- Test application will have a watermark: -->
	<%@ include file="../pages/testWatermark.jsp" %>
	
    <script type="text/javascript" src="js/intenz.js"></script>
    <script type="text/javascript" src="js/master.js"></script>

	<script type="text/javascript" src="xcharsPalette/xcharsPopup.js"></script>

    <!-- ULTIMATE DROP DOWN MENU Version 4.2 by Brothercake -->
    <!-- http://www.udm4.com/ -->
    <script type="text/javascript" src="resources/udm-resources/udm-custom.js"></script>
    <script type="text/javascript" src="resources/udm-resources/udm-control.js"></script>
    <script type="text/javascript" src="resources/udm-resources/udm-style.js"></script>
  </head>
