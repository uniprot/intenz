<%@ page import="java.util.Map,
                 java.util.HashMap"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

    <!---------------------------------------------- Content --------------------------------------------------------->

    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <table width="50%" class="content_table" border="0" cellspacing="20" cellpadding="0">
            <tr>
              <td align="center">
                <table border="0" cellspacing="20" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row">Error</td>
                  </tr>
                  <tr>
                    <td>
                      <table width="100%" cellpadding="3">
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

                        <% } else { %>
                            <logic:messagesPresent>
                            <tr>
                              <td class="error_large">
                                <html:messages id="error">
                                	<bean:write name="error"/>
                                </html:messages>
                              </td>
                            </tr>
                            </logic:messagesPresent>
                            <logic:messagesNotPresent>
                            <tr>
                              <td><b>The server encountered an unexpected condition which prevented it from fulfilling the request.<br></b></td>
                            </tr>
                            </logic:messagesNotPresent>
                        <%
                           }
                        %>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>




