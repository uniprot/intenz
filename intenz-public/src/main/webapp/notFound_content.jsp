<%@ page import="java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.xchars.domain.EncodingType"%>

<%
  response.setContentType("text/html; charset=UTF-8");
  String title = "Data not found - Integrated Enzyme Database (IntEnz)";
%>

  <table width="100%" border="0" cellpadding="0" cellspacing="0">
  <%
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
          <a class="icon_email" href="mailto:support@ebi.ac.uk">support@ebi.ac.uk</a>.<br>
          Sorry for any inconvenience this error might have caused.</b>
        </td>
      </tr>
  <%
       }
  %>
  </table>
