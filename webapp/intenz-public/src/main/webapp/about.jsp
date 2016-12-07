<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="About IntEnz"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="about"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>About IntEnz</h2>

<p>
IntEnz is the name for the <b>Int</b>egrated relational <b>Enz</b>yme database.
IntEnz is supported by the Nomenclature Committee of the <a href="http://www.iubmb.org/">
International Union of Biochemistry and Molecular Biology</a> (NC-IUBMB) and contains enzyme data
that is curated and approved by the Nomenclature Committee. The relational database (IntEnz)
implemented and supported by the EBI is the <b>master copy</b> of the Enzyme Nomenclature data
containing enzyme data from two different sources:
</p>
<ul>
  <li>The Enzyme Classification list maintained by
	<a href="http://www.tcd.ie/">Trinity College Dublin</a> on behalf of
     the Nomenclature Committee of the International Union of
     Biochemistry and Molecular Biology (NC-IUBMB)</li>
  <li>The ENZYME database originally developed by the
	<a href="http://www.isb-sib.ch/">Swiss Institute of Bioinformatics</a>
	(SIB)</li>
</ul>
<p>
The ENZYME data are an integral part of IntEnz, and the ENZYME flat file generated from IntEnz
is available from our <a href="ftp://ftp.ebi.ac.uk/pub/databases/enzyme/">ftp site</a>.
</p>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
