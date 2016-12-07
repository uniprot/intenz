<html><head><title>Printer-friendly view</title><link rel="stylesheet" href="intenz.css" type="text/css"></head><body>

<%
  String html = (String) request.getAttribute("result");
  response.setContentType("text/html; charset=UTF-8");
  if(html == null) {
%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <b>The server encountered an unexpected condition which prevented it from fulfilling the request.<br>
      It would be very kind of you, if you could report this error to
      <a href="http://www.ebi.ac.uk/support/">EBI Support</a>.<br>
      Sorry for any inconvenience this error might have caused.</b>
    </td>
  </tr>
</table>

<% } else { %>

<table align="left" valign="top" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      &nbsp;
    </td>
  </tr>
  <tr>
    <td align="left">
      <%= request.getAttribute("result") %>
    </td>
  </tr>
</table>

<% } %>

</body>
</html>
