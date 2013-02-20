<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="Concact IntEnz"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="contact"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>Contact us</h2>

<% if (request.getAttribute("sent") == null){ %>


	<p style="margin-bottom: 2em">Send your comments straight to the IntEnz team using this form.
		 <br/>
		For support or feature requests you are encouraged to use our
		<a target="_blank" class="externalLink" href="https://sourceforge.net/projects/intenz/">SourceForge</a>
		project pages, where we all can keep track of them.
	</p>

<div id="xround" style="width: 600px"><b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><div class="xboxcontent">
	<form method="POST" action="contact">
	<table class="mailForm" align="center">
		<tr>
			<th>E-mail:</th>
			<td><input type="text" size="30" name="email" /></td>
		</tr>
		<tr>
			<th>Comment:</th>
			<td><textarea name="message" rows="10" cols="50">Insert your comment here.</textarea></td>
		</tr>
		 <tr>
		 <td></td>
		 <th><input type="submit" class="submit" name="intenzMail" value="Send"
                style="margin-left: auto"/>
		 </th>
		 </tr>
	</table>
	</form>
</div><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b></div>

<% } else if (request.getAttribute("sent").equals(Boolean.TRUE)){ %>

	<p>Thanks for your feedback! Your message will be replied to as soon as possible.</p>

<% } else { %>

	<p class="error"><%= request.getAttribute("error") %></p>

<% } %>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
