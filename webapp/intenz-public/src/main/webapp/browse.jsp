<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="Browse IntEnz"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="browse"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>Browse IntEnz</h2>

<p>Browse the database by selecting the EC classification of interest:</p>
<div style="text-align: center; min-width: 170px">
	<div style="text-align: left; margin-left: auto; margin-right: auto; width: 170px">
	   <ul style="list-style-type: none; padding-left: 5px; margin-left: 0px">
		    <li><a href="query?cmd=SearchEC&ec=1">EC 1</a>&nbsp;&nbsp;Oxidoreductases</li>
			<li><a href="query?cmd=SearchEC&ec=2">EC 2</a>&nbsp;&nbsp;Transferases</li>
			<li><a href="query?cmd=SearchEC&ec=3">EC 3</a>&nbsp;&nbsp;Hydrolases</li>
			<li><a href="query?cmd=SearchEC&ec=4">EC 4</a>&nbsp;&nbsp;Lyases</li>
			<li><a href="query?cmd=SearchEC&ec=5">EC 5</a>&nbsp;&nbsp;Isomerases</li>
			<li><a href="query?cmd=SearchEC&ec=6">EC 6</a>&nbsp;&nbsp;Ligases</li>
		</ul>
	</div>
</div>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
