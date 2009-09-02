<%@page import="java.util.List,
				java.util.ArrayList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="eng">
<!-- InstanceBegin template="/Templates/new_template_no_menus.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta name="description" content="The European Bioinformatics Institute" />
<meta name="author" content="EBI Web Team" />
<meta http-equiv="Content-Language" content="en-GB" />
<meta http-equiv="Window-target" content="_top" />
<meta name="no-email-collection" content="http://www.unspam.com/noemailcollection/" />
<meta name="generator" content="Dreamweaver 8" />
<!-- InstanceBeginEditable name="doctitle" -->
<% String title = request.getParameter("title"); %>
<title><%=title%> - Integrated Enzyme Database (IntEnz)</title>
<!-- InstanceEndEditable -->

<link rel="stylesheet" type="text/css" href="http://www.ebi.ac.uk/inc/css/contents.css"/>
<link rel="stylesheet" type="text/css" href="http://www.ebi.ac.uk/inc/css/userstyles.css" />
<link rel="stylesheet" type="text/css" href="http://www.ebi.ac.uk/inc/css/sidebars.css"/>
<link rel="stylesheet" type="text/css" href="intenz.css"/>

<link rel="search" href="openSearchDescription.xml"
      type="application/opensearchdescription+xml"
      title="IntEnz"/>

<style type="text/css">
@media print {
	body, .contents, .header, .contentsarea, .head {
		position: relative;
	}
}
</style>
<!-- InstanceEndEditable -->

<link rel="SHORTCUT ICON" href="http://www.ebi.ac.uk/bookmark.ico" />

<script type="text/javascript" src="http://www.ebi.ac.uk/inc/js/contents.js"></script>
<script type="text/javascript" src="resources/intenz.js"></script>
<script language='javascript'>
  function windowOpenWithSize(theurl, thewd, theht) {
    if(!(theht)) {
      theht = 528;
    }
    if(!(thewd)) {
      thewd = 410;
    }
    var newwin  = window.open(theurl,"labelsize","dependent=yes, resizable=yes,toolbar=no,menubar=no,scrollbars=yes,width="+thewd+",height="+theht);
    newwin.focus();
  }
</script>

</head>

<body
	onload="if(navigator.userAgent.indexOf('MSIE') != -1) {document.getElementById('head').allowTransparency = true;}">

<div class="headerdiv" id="headerdiv" style="position:absolute; z-index: 1;">
	<iframe
		src="/inc/head.html" name="head" id="head" frameborder="0"
		marginwidth="0px" marginheight="0px" scrolling="no" width="100%"
		style="position:absolute; z-index: 1; height: 57px;">
	</iframe>
</div>

<div class="contents" id="contents">
<table class="contentspane" id="contentspane" summary="The main content pane of the page" style="width: 100%">
	<tr>
		<td class="leftmargin">
			<img src="http://www.ebi.ac.uk/inc/images/spacer.gif" class="spacer" alt="spacer" />
		</td>
		<td class="leftmenucell" id="leftmenucell">

<!-- Navigation -->

		<div class="leftmenu" id="leftmenu"
			style="width: 145px; visibility: visible; display: block;">

			<a href="index.jsp" class="centered"><img
				src="http://www.ebi.ac.uk/Groups/images/intenz_small.gif"
				width="75" height="50" border="0"
				style="margin: 5px"/></a>

			<div id="quicksearch">Quick search:
				<form method="get" action="query">
					<input type="hidden" name="cmd" value="Search" />
					<input type="text" style="width: 140px" maxsize="100" name="q" />
					<input type="submit" value="Search" style="width: 140px" class="submit_button"/>
				</form>
			</div>
<%if (title.equals("Home") || title.equals("Advanced search")
		|| title.startsWith("Search result") || title.startsWith("No result")) {
%><script type="text/javascript">toggleVisibility('quicksearch');</script><% } %>


			<ul id="sidemenuid" class="sidemenu" style="margin-bottom: 1em">
				<li class="large"><%
				if (title.equals("Home")) {
				%><span class="navbarCurrent">IntEnz home</span><%
				} else {
				%><a href="index.jsp" class="navbar"
					onmouseover="window.status=''; return true;">IntEnz home</a><% } %>
				</li>
				<li class="large"><%
				if (title.equals("Advanced search")) {
				%><span class="navbarCurrent">Advanced search</span><%
				} else {
				%><a class="navbar" href="search.jsp">Advanced search</a><% } %>
				</li>
				<li class="large"><%
				if (title.equals("Browse the enzyme classification")) {
				%><span class="navbarCurrent">Browse EC</span><%
				} else {
				%><a class="navbar" href="browse.jsp"
					title="Browse the enzyme classification">Browse EC</a><% } %>
				</li>
				<li class="large"><%
				if (title.equals("Proposed changes")) {
				%><span class="navbarCurrent">Proposed changes</span><%
				} else {
				%><a class="navbar" href="query?cmd=SearchProposed"
					title="Proposed additions and amendments to the enzyme classification">Proposed changes</a><% } %>
				</li>
				<li class="<% if (title.equals("How to Name and Classify Your Enzyme")) { %>clickmeopen<% } %>"><a href="#"></a>Data submission
					<ul>
						<li><a class="navbar externalLink" target="_blank"
							title="Report an enzyme not currently included in enzyme nomenclature"
							href="http://www.expasy.org/enzyme/enz_new_form.html">New enzymes</a>
						</li>
						<li><a class="navbar externalLink" target="_blank"
							title="Report an error or update in an existing enzyme entry"
							href="http://www.expasy.org/enzyme/enz_update_form.html">Corrections</a>
						</li>
						<li><%
						if (title.equals("How to Name and Classify Your Enzyme")) {
						%><span class="navbarCurrent">EC guidelines</span><%
						} else {
						%><a class="navbar" href="advice.jsp"
							title="How to name and classify your enzyme">EC guidelines</a><% } %>
						</li>
					</ul>
				</li>
				<li class="large"><%
				if (title.equals("Downloads")) {
				%><span class="navbarCurrent">Downloads</span><%
				} else {
				%><a class="navbar" href="downloads.jsp">Downloads</a><% } %>
				</li>
<% List docs = new ArrayList();
docs.add("Frequently Asked Questions");
docs.add("How to Name and Classify Your Enzyme");
docs.add("Classification rules");
docs.add("Publications");
docs.add("Enzyme spotlight");
docs.add("Statistics");
%>
				<li class="<% if (docs.contains(title)) { %>clickmeopen<% } %>"><a href="#"></a>Documentation
					<ul>
						<li><%
						if (title.equals("Frequently Asked Questions")) {
						%><span class="navbarCurrent">FAQ</span><%
						} else {
						%><a class="navbar" href="faq.jsp"
							title="Frequently asked questions">FAQ</a><% } %>
						</li>
						<li><%
						if (title.equals("How to Name and Classify Your Enzyme")) {
						%><span class="navbarCurrent">EC guidelines</span><%
						} else {
						%><a class="navbar" href="advice.jsp"
							title="How to name and classify your enzyme">EC guidelines</a><% } %>
						</li>
						<li><%
						if (title.equals("Classification rules")) {
						%><span class="navbarCurrent">Classification rules</span><%
						} else {
						%><a class="navbar" href="rules.jsp">Classification rules</a><% } %>
						</li>
						<li class="large"><%
						if (title.equals("Publications")) {
						%><span class="navbarCurrent">Publications</span><%
						} else {
						%><a class="navbar" href="publications.jsp">Publications</a><% } %>
						</li>
						<li><%
						if (title.equals("Enzyme spotlight")) {
						%><span class="navbarCurrent">Enzyme spotlights</span><%
						} else {
						%><a class="navbar" href="spotlight.jsp"
							title="IntEnz focus on one EC number with every release">Enzyme spotlights</a><% } %>
						</li>
						<li><%
						if (title.equals("Statistics")) {
						%><span class="navbarCurrent">Statistics</span><%
						} else {
						%><a class="navbar" href="statistics.jsp">Statistics</a><% } %>
						</li>
					</ul>
				</li>
				<li class="large"><%
				if (title.equals("Contact IntEnz")) {
				%><span class="navbarCurrent">Contact IntEnz</span><%
				} else {
				%><a class="navbar" href="contact.jsp">Contact IntEnz</a><% } %>
				</li>
			</ul>

<script type="text/javascript" src="http://www.ebi.ac.uk/inc/js/sidebars.js"></script>

<% if (title.equals("Home")) { %>
	<div class="iconboxheading">News</div>
	<div class="iconboxcontents" style="margin-bottom: 1em">
		<%@include file="news_content.html" %>
	</div>

	<div class="iconboxheading">Plugins</div>
	<div class="iconboxcontents" style="margin-bottom: 1em">
		<img src="images/firefoxIcon.png" alt="Firefox" class="centered"/>
		<br/>
		<div class="psmall">
			<a href="javascript:addEngine('intenz','png','General',0)">Install</a>
			the IntEnz search plugin
			for <a href="http://www.mozilla.com/firefox/" target="_blank">Firefox</a> 1.5
			(not needed with Firefox 2+ or IE 7+).
		</div>
	</div>

	<div class="iconboxheading">Partners</div>
	<div class="iconboxcontents" style="margin-bottom: 1em">
     	<div align="center">
     		<a href="http://www.isb-sib.ch" title="Go to SIB website" target="sib"><img
     			src="images/sib_logo_emblem.png" alt="SIB"
     			class="centered" align="middle" border="0"/></a>
        </div>
		<div class="psmall">
			<a href="http://www.isb-sib.ch" title="Go to SIB website."
				target="_blank" class="centered externalLink">Swiss Institute of Bioinformatics</a>
			<div class="psmall">
				The ENZYME data are an integral part of IntEnz, and the ENZYME
				flat file generated from IntEnz is available from our
				<a target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/enzyme/">ftp site</a>.
			</div>
		</div>
	</div>
<% } %>

<!-- Navigation end -->

		<img src="http://www.ebi.ac.uk/inc/images/spacer.gif" class="spacer" alt="spacer" />

		</div>
		</td>
		<td class="contentsarea" id="contentsarea">

			<div class="breadcrumbs">
				<a href="http://www.ebi.ac.uk/" class="firstbreadcrumb">EBI</a>
				<a href="http://www.ebi.ac.uk/Databases/">Databases</a>
				<a href="http://www.ebi.ac.uk/Databases/enzymes.html">Enzymes</a>
				<a href="index.jsp">IntEnz</a>
			</div>

		<!-- InstanceBeginEditable name="contents" -->
<% if (!title.startsWith("EC ")) {
%><h1>IntEnz - <%=title%></h1>
<% } %>

			<div style="width: 100%; margin-top: 0em">
				<jsp:include page='<%= request.getParameter("content") %>' />
			</div>

			<!-- InstanceEndEditable -->
			<img src="http://www.ebi.ac.uk/inc/images/spacer.gif" class="spacer" alt="spacer" />
		</td>
		<td class="rightmenucell" id="rightmenucell">
			<div class="rightmenu" id="rightmenu">
				<img src="http://www.ebi.ac.uk/inc/images/spacer.gif" class="spacer" alt="spacer" />
			</div>
		</td>
	</tr>
</table>

<table class="footerpane" id="footerpane" summary="The main footer pane of the page">
	<tr>
		<td colspan="4" class="footerrow">
		<div class="footerdiv" id="footerdiv" style="z-index:2;">
			<iframe
				src="/inc/foot.html" name="foot" frameborder="0" marginwidth="0px"
				marginheight="0px" scrolling="no" height="22px" width="100%"
				style="z-index:2;">
			</iframe>
		</div>
		</td>
	</tr>
</table>

<script src="http://www.ebi.ac.uk/inc/js/footer.js" type="text/javascript"></script>
</div>
</body>
<!-- InstanceEnd -->
</html>
