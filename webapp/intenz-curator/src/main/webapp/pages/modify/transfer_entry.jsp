<%@ page import="org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%
	EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
%>

    <!------------------------------------------ Content -------------------------------------------------->
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <table class="content_table" border="0" cellspacing="20" cellpadding="0">
            <tr>
              <logic:messagesPresent>
              <tr>
                <td class="error">
                  <html:messages id="error"><bean:write name="error"/></html:messages>
                </td>
              </tr>
              </logic:messagesPresent>
            </tr>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="20" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row">Transfer Entry <bean:write name="enzymeDTO" property="ec" filter="false"/></td>
                  </tr>
                  <tr>
                    <td>
                      <html:form action="transferEntry" focus="transferredEc">
                      <table width="100%" cellpadding="3">
                        <input type="hidden" name="<%= Constants.TOKEN_KEY %>" value="<%=request.getAttribute(Constants.TOKEN_KEY)%>"/>
                        <logic:messagesPresent property="transferredEc" >
                          <tr>
                            <td colspan="2" class="error">
                              <html:messages id="error" property="transferredEc">
                              	<bean:write name="error"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>
                        <tr>
                          <td align="right" nowrap="nowrap"><b>New EC:</b></td>
                          <td width="100%"><html:text property="transferredEc" tabindex="1" size="15" /></td>
                        </tr>
                        <tr>
                          <td colspan="2" nowrap="nowrap"><b>New History Line:</b></td>
                        </tr>
                        <tr>
                          <td colspan="2" width="100%">
                          	<html:textarea property="historyLine" tabindex="2" cols="70" rows="2" value="<%= enzymeDTO.getHistoryLine() %>" />
                          </td>
                        </tr>
                        <tr>
                          <td colspan="2"><b>Additional History Information:</b></td>
                        </tr>
                        <tr>
                          <td colspan="2">
                          	<html:textarea property="latestHistoryEventNote" tabindex="3" cols="70" rows="5" />
                           	<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'latestHistoryEventNote');"
								title="Open xchars window" class="xchars"/>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="right" colspan="2">
                          	<html:reset tabindex="4" />&nbsp;
                          	<html:submit title="Transfer entry" value="Transfer entry" tabindex="5" styleClass="submitButton" />
                          </td>
                        </tr>
                      </table>
                      </html:form>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>




