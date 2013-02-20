<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Publications"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="docs"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<style>
ol li { margin: 2em; }
</style>

<section class="grid_18 alpha">

<h2>Publications</h2>

<ol>
<li>
	<div class="pubauthors">
		Fleischmann, A., Darsow, M., Degtyarenko, K., Fleischmann, W.,
		Boyce, S., Axelsen, K.B., Bairoch, A., Schomburg, D., Tipton, K.F.
		and Apweiler, R. (2004)
	</div>
	<div class="pubtitle">IntEnz, the integrated relational enzyme database.</div>
	<div class="pubjournal">
		<a class="externalLink" target="_blank"
			href="http://nar.oxfordjournals.org/cgi/content/abstract/32/suppl_1/D434">
		<i>Nucleic Acids Res.</i> <b>32</b>, D434-D437</a>.
	</div>
</li>

<li>
	<div class="pubauthors">
		Alc&aacute;ntara R., Ast V., Axelsen K.B., Darsow M., de Matos P.,
		Ennis M., Morgat A. and Degtyarenko K. (2007)
	</div>
	<div class="pubtitle">IntEnz</div>
	<div class="pubjournal">
		<a class="externalLink" target="_blank"
			href="http://www3.oup.co.uk/nar/database/summary/508">
		<i>Nucleic Acids Res.</i> Molecular Biology Database Collection entry number 508</a>.
	</div>
</li>

</ol>
</section>

<section class="grid_6 omega">
    <%@ include file="docs.html" %>
</section>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
