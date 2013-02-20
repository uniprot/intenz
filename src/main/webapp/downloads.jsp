<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="Download IntEnz"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="download"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>Download IntEnz data</h2>

<p>IntEnz data can be downloaded from our <a target="_blank" class="externalLink"
	href="ftp://ftp.ebi.ac.uk/pub/databases/intenz">FTP site</a> in the
    following formats:</p>
<ul>
	<li style="margin-bottom: 2ex"><a
        href="ftp://ftp.ebi.ac.uk/pub/databases/intenz/enzyme"
		target="_blank" class="externalLink">enzyme.dat</a>
		<br/>
		The flat file format used by the <a href="http://www.expasy.org"
        title="Go to ExPASy website." target="_blank" class="externalLink">Swiss
        Institute of Bioinformatics</a>.</li>

	<li style="margin-bottom: 2ex"><a
        href="ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml"
	 	target="_blank" class="externalLink">IntEnzXML</a>
	 	<br/>
		Every single entry in IntEnz and the whole database can be downloaded in XML format,
		whose structure is defined in two XML schemas.</li>

    <li style="margin-bottom: 2ex"><a
        href="ftp://ftp.ebi.ac.uk/pub/databases/intenz/biopax"
        target="_blank" class="externalLink">BioPAX level 2</a>
        <br/>
        A standard data exchange <a href="http://www.biopax.org"
        target="_blank">format</a> for biological pathway data.
        </li>
</ul>
<p>Please have a look at the README files for further information.</p>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
