<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Submissions"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="submissions"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<h2>Submit enzyme data</h2>

<ul>
    <li><a href="http://www.expasy.org/enzyme/enz_new_form.html"
        class="externalLink">Report an enzyme
        not currently included in the enzyme nomenclature.</a></li>

    <li><a href="http://www.expasy.org/enzyme/enz_update_form.html"
        class="externalLink">Report an error
        or update in an existing enzyme entry.</a></li>

    <li><a href="advice.jsp">Help: how to name and classify your enzyme.</a>
        </li>
</ul>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
