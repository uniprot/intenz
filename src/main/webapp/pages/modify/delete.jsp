<%@ page import="org.apache.struts.taglib.html.Constants"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
	String update = (String) request.getAttribute("update");
	String deleteAction = "deleteEntry" + update + ".do";
%>
    <!---------------------------------------------- Content ------------------------------------------------->
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <table class="content_table" border="0" cellspacing="20" cellpadding="0">
            <tr>
              <logic:messagesPresent>
              <tr>
                <td class="error">
                  <html:messages id="error">
                  	<bean:write name="error"/>
                  </html:messages>
                </td>
              </tr>
              </logic:messagesPresent>
            </tr>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="20" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row">
                    	Delete${enzymeDTO.statusCode eq 'OK'
                    		and not enzymeDTO.active? 'd' : ''} Entry
                    	<bean:write name="enzymeDTO" property="ec" filter="false"/>
                    	<logic:equal name="update" value="Update">
                    	(<i>update</i>)
                    	</logic:equal>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <html:form action="<%= deleteAction %>" focus="latestHistoryEventNote">
                      <input type="hidden" name="<%= Constants.TOKEN_KEY %>" value="<%=request.getAttribute(Constants.TOKEN_KEY)%>"/>
                      <table width="100%" cellpadding="3">
                          <tr>
                              <td colspan="2"><b>History:</b></td>
                          </tr>
                          <tr>
                              <td colspan="2">
                                  <html:textarea readonly="false" property="historyLine" tabindex="2" cols="70" rows="5"/>
                              </td>
                          </tr>
                          <tr>
                              <td colspan="2"><b>Additional History Information:</b></td>
                          </tr>
                          <tr>
                              <td colspan="2">
                              	<html:textarea property="latestHistoryEventNote" tabindex="1" cols="70" rows="5" />
								<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'latestHistoryEventNote');"
									title="Open xchars window" class="xchars"/>
                              </td>
                          </tr>
                          
						<logic:equal name="update" value="Update">
                          
                          <tr>
                              <td align="right" nowrap="nowrap"><b>Current Status:</b></td>
                              <td width="100%"><bean:write name="enzymeDTO" property="statusText" filter="false"/></td>
                          </tr>
                          <tr>
                              <logic:equal name="enzymeDTO" property="statusCode" value="SU">
	                              <td align="right" nowrap="nowrap"><b>New Status:</b></td>
                                  <td width="100%">
                                      <html:select property="statusCode" tabindex="2" size="1" value="SU" >
                                          <html:option value="SU">suggested</html:option>
                                          <html:option value="PR">proposed</html:option>
                                      </html:select>
                                  </td>
                              </logic:equal>
                              <logic:equal name="enzymeDTO" property="statusCode" value="PR">
	                              <td align="right" nowrap="nowrap"><b>New Status:</b></td>
	                              <td width="100%">
	                                  <html:select property="statusCode" tabindex="2" size="1" value="SU" >
	                                      <html:option value="SU">suggested</html:option>
	                                      <html:option value="OK">approved</html:option>
	                                  </html:select>
	                              </td>
                              </logic:equal>
                          </tr>
                          <tr>
                              <td width="100%" align="right" colspan="2">
                              	<html:reset tabindex="3" />&nbsp;
                              	<html:submit title="Store changes in the database" value="Update entry" tabindex="4" styleClass="submitButton" />
                              </td>
                          </tr>

						</logic:equal>

						<logic:notEqual name="update" value="Update">

                          <tr>
                              <td width="100%" align="right" colspan="2">
                              	<html:reset tabindex="3" />&nbsp;
                              	<html:submit title="Delete entry" value="Delete entry" tabindex="3" styleClass="deleteButton" />
                              </td>
                          </tr>

						</logic:notEqual>
                          
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




