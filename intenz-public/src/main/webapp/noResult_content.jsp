<%
	response.setContentType("text/html; charset=UTF-8");
	String message = (String) request.getAttribute("message");
    if (message != null && !message.equals("")) {
  %>
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td><%= message %></td>
    </tr>
  </table>
  <%
    }
  %>

  <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center">
        <form method="get" action="query">
        <input type="hidden" name="cmd" value="Search">
        <table width="100%" border="0">
          <tr>
            <td align="center" width="100%">
              <table cellspacing="5">
                <tr>
                  <td valign="center"><input type="text" size="50" maxsize="100" name="q"></td>
                  <td valign="center">
                    <select name="t" size="1">
                      <option value="exact" selected="selected">Exact phrase</option>
                      <option value="any">Any of these words</option>
                      <option value="all">All of these words</option>
                    </select><br/>
                  </td>
                  <td valign="center">
                    <a href="javascript:windowOpenWithSize('encoding.html', 540, 400)"><img style="vertical-align:middle" src="images/alpha_icon.gif" width="16" height="15" border="1" alt="Special characters"></a>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" align="center"><input type="submit" value="Search" class="submit_button"></td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        </form>
      </td>
    </tr>
  </table>
<br>
