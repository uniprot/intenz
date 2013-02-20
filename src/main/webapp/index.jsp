<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz web service"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

    <jsp:include page="headers.jsp"/>

	<div class="grid_24 clearfix" id="content" role="main">
        <h2>IntEnz web services</h2>
        <p>For any comments or suggestions, please send an e-mail
        	to the <a href="mailto:intenz-devel@lists.sourceforge.net">IntEnz
        	developers</a>.
       	</p>
        
        <h3>Usage:</h3>
        <dl>
	        <dt><code>/EC/<i>{ecNumber}.{extension}</i></code></dt>
	        <dd>Retrieve an EC number (<i>{ecNumber}</i>) in the format
	        	corresponding to the given <i>{extension}</i> (see below for
	        	supported extensions and content types).<br/>
	        	Ex: <a href="EC/1.1.1.1.xml">/EC/1.1.1.1.xml</a>
        	</dd>
        </dl>
        <p>
	        Note that though this is the preferred way of specifying the
	        response format, you can alternatively do it using a request
	        parameter <code>format</code> or a supported MIME type in the
	        <code>Accept</code> HTTP header (see below for supported parameter
	        values and MIME types).
        </p>
        <table class="contenttable">
        	<caption align="bottom">Supported content types</caption>
        	<tr>
        		<th><code>{extension}</code></th>
        		<th><code>{format}</code> request parameter</th>
        		<th><code>Accept</code> HTTP header</th>
        		<th>Notes</th>
        	</tr>
        	<tr>
        		<td>xml</td>
        		<td>xml</td>
        		<td class="mimeType">application/xml</td>
        		<td>XML (schema <a
        			href="ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml/intenz.xsd">here</a>)</td>
        	</tr>
        	<tr>
        		<td>owl</td>
        		<td>biopax_l2</td>
        		<td class="mimeType">application/rdf+xml</td>
        		<td><a href="http://www.biopax.org">BioPAX</a> level 2</td>
        	</tr>
 	      	<tr>
        		<td>txt</td>
        		<td>enzyme</td>
        		<td class="mimeType">text/plain</td>
        		<td><a href="http://enzyme.expasy.org/enzuser.txt">ENZYME</a>
        			flat file</td>
        	</tr>
         </table>

	</div>

    <%@ include file="footer.jsp" %>

</div>
</body>
</html>
