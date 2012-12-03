<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<% session.invalidate(); %>
<html>

  <head>
    <title>Log out - IntEnz Curator Web Application</title>
    <link rel="stylesheet" href="<html:rewrite forward='intenzStyle'/>" type="text/css">
    
    <%-- Make it clear whenever we are using a development database: --%>
	<%@ include file="pages/testWatermark.jsp" %>
  </head>

  <body>

    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center">
          <table class="logon_table" border="0" cellspacing="20" cellpadding="0">
            <tr>
              <td align="center">
                <img src="images/intenz_small.gif">
              </td>
              <td>
                <table cellpadding="4" cellspacing="0" border="0">
                  <tr>
                    <td class="logon_font" align="left">Thank you for using the IntEnz Curator Web Application</td>
                  </tr>
                  <tr>
                    <td class="logon_font" align="right"><html:link page="/init.jsp" styleClass="white">Log on</html:link> again ...</td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>

  </body>

</html>
