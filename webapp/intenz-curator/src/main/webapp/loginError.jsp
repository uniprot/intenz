<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<html>

  <head>
    <title>Log on denied - IntEnz Curator Web Application</title>
    <link rel="stylesheet" href="<html:rewrite forward='intenzStyle'/>" type="text/css">
    
    <%-- Make it clear whenever we are using a development database: --%>
	<%@ include file="pages/testWatermark.jsp" %>
  </head>

  <body>

    <script type="text/javascript" language="JavaScript">
      <!--
      var focusControl = document.forms["logonForm"].elements["Submit"];

      if (focusControl.type != "hidden" && !focusControl.disabled) {
         focusControl.focus();
      }
      // -->

    </script>
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align="center">
          <form name="logonForm" action="j_security_check" method="post">
            <table class="logon_table" border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td align="center">
                  <img src="images/intenz_small.gif">
                </td>
                <td>
                  <table cellpadding="4" cellspacing="0" border="0">
                    <tr>
                      <td class="logon_denied" colspan="2" align="right">Logon denied - please try again.</td>
                    </tr>
                    <tr>
                      <td class="logon_font" align="right">Name:</td>
                      <td align="left"><input type="text" name="j_username"/></td>
                    </tr>
                    <tr>
                      <td class="logon_font" align="right">Password:</td>
                      <td align="left"><input type="password" name="j_password"/></td>
                    </tr>
                    <tr>
                      <td colspan="2" align="right"><input type="submit" name="Submit" value="Log on"/></td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </form>
        </td>
      </tr>
    </table>

  </body>

</html>