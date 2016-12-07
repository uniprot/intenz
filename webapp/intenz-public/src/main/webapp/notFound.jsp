<%@ page import="java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.xchars.domain.EncodingType"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Not found"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value=""/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

  <%
    if(request.getAttribute("message") != null) {
      String errorMessage = (String) request.getAttribute("message");
   %>
	<%= errorMessage %>
   <%
    } else {
   %>
          <b>The server encountered an unexpected condition which prevented it from fulfilling the request.<br>
          It would be very kind of you, if you could report this error to
          <a class="icon_email" href="mailto:support@ebi.ac.uk">support@ebi.ac.uk</a>.<br>
          Sorry for any inconvenience this error might have caused.</b>
  <%
       }
  %>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
