<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Suggesting changes of enzyme classification"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="docs"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<b>How to suggest new entries and correct existing entries</b>

<p>
Information about <a target="_blank" href="http://www.expasy.org/enzyme/enz_new_form.html">new enzymes</a> or
<a target="_blank" href="http://www.expasy.org/enzyme/enz_update_form.html">corrections</a> to existing entries
may be reported directly from these web pages.
<br/>
<a href="advice.jsp">Advice</a> is available on how to
suggest new enzymes for listing or propose corrections to existing entries.
Comments and suggestions on enzyme
classification and nomenclature also may be sent to
<a class="icon_email" href="mailto:ktipton@tcd.ie; sboyce@tcd.ie">Professor K.F. Tipton and Dr S. Boyce</a>
(Department of Biochemistry, Trinity College Dublin, Dublin 2, Ireland).
</p>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
