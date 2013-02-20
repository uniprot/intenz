<%@ page import="java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.xchars.domain.EncodingType"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Error"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value=""/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

  <table width="100%" border="0" cellpadding="0" cellspacing="0">

  <%
    if(request.getAttribute("javax.servlet.error.status_code") != null) {
      String errorCode = request.getAttribute("javax.servlet.error.status_code").toString();
      if(errorCode.equals("404")) {
    %>
      <tr>
        <td><b>The requested page is not available:</b></td>
      </tr>
    <%
      }

      if(errorCode.equals("500")) {
    %>
      <tr>
        <td><b>The server encountered an unexpected condition which prevented it from fulfilling the request.<br></b></td>
      </tr>
    <%
      }
    %>

      <tr>
        <td><%= request.getAttribute("javax.servlet.error.message") %></td>
      </tr>

  <% } else {
       if(request.getAttribute("message") != null) {
         String errorMessage = (String) request.getAttribute("message");
      %>
        <tr>
          <td><%= errorMessage %></td>
        </tr>
      <%
       } else {
      %>
        <tr>
          <td>
            <b>The server encountered an unexpected condition which prevented it from fulfilling the request.<br>
            It would be very kind of you, if you could report this error to
            <a href="http://www.ebi.ac.uk/support/">EBI Support</a><br/>
            Sorry for any inconvenience this error might have caused. 
          </td>
        </tr>
  <%
       }
     }
  %>
  </table>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
