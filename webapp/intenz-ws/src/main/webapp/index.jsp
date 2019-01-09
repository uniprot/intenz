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
        	to the <a href="mailto:enzyme-portal@ebi.ac.uk">IntEnz
        	developers</a>.
       	</p>
        
        <h3>Usage:</h3>
        <dl>
	        <dt><code>/EC/<i>{ecNumber}</i>.<i>{extension}</i></code></dt>
	        <dd>Retrieve an EC number (<i>{ecNumber}</i>) in the format
	        	corresponding to the given <i>{extension}</i> (see below for
	        	supported extensions and content types).<br/>
	        	Ex: <a href="EC/1.1.1.1.xml">/EC/1.1.1.1.xml</a>
        	</dd>
            <dt><code>/EC/<i>{ecNumber}</i>?format=<i>{format}</i></code></dt>
            <dd>Retrieve an EC number (<i>{ecNumber}</i>) in the format
                corresponding to the given <i>{format}</i> query parameter (see
                below for supported extensions and content types).<br/>
                Ex: <a href="EC/1.1.1.1?format=xml">/EC/1.1.1.1?format=xml</a>
            </dd>
            <dt><code>/EC/<i>{ecNumber}</i></code>
                <span style="font-size: small;">(with <code>Accept</code>
                HTTP header set to one of the supported MIME types)</span>
            </dt>
            <dd>Retrieve an EC number (<i>{ecNumber}</i>) in the format
                corresponding to the given <i>Accept</i> HTTP header (see
                below for supported extensions and content types).<br/>
                Ex: <a href="EC/1.1.1.1">/EC/1.1.1.1</a>
            </dd>
        </dl>
        <p>
	        See below for supported extensions, parameter values and MIME types.
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
        		<td>XML (<a href="xsd/intenz.xsd">IntEnzXML schema</a>). This
        		    is the default, if no other is especified.</td>
        	</tr>
        	<tr>
        		<td>owl</td>
        		<td>biopax_l2</td>
        		<td class="mimeType">application/rdf+xml</td>
        		<td><a href="http://www.biopax.org">BioPAX</a> level 2.</td>
        	</tr>
 	      	<tr>
        		<td>txt</td>
        		<td>enzyme</td>
        		<td class="mimeType">text/plain</td>
        		<td><a href="http://enzyme.expasy.org/enzuser.txt">ENZYME</a>
        			flat file.</td>
        	</tr>
            <tr>
                <td>json</td>
                <td>json</td>
                <td class="mimeType">application/json</td>
                <td><a href="https://json.org/">JSON</a> (JavaScript Object
                    Notation). This is the only case where the requested
                    <code>{ecNumber}</code> can be an EC sub-subclass,
                    subclass, class or even empty (meaning all of the EC
                    classes).
                </td>
            </tr>
         </table>

	</div>

    <%@ include file="footer.jsp" %>

</div>
</body>
</html>
