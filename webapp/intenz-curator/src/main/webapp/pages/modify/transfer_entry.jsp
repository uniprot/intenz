<%@ page import="org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                    <td class="data_region_header_row">
                    	Transfer${enzymeDTO.active? '' : 'red'} entry
                    	<bean:write name="enzymeDTO" property="ec" filter="false"/>
                    	${enzymeDTO.active? '' : '(updating notes)'}
                   	</td>
                  </tr>
                  <tr>
                    <td>
                      <html:form
                      	action="${enzymeDTO.active? 'transferEntry' : 'transferredEntryUpdate'}"
                      	focus="transferredEc">
                        <input type="hidden" name="<%= Constants.TOKEN_KEY %>" value="<%=request.getAttribute(Constants.TOKEN_KEY)%>"/>
                      <table width="100%" cellpadding="3">
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
                        	<logic:equal value="true" name="enzymeDTO" property="active">
	                          <td align="right" nowrap="nowrap"><b>New EC:</b></td>
	                          <td><html:text property="transferredEc" tabindex="1" size="15" /></td>
                        	</logic:equal>
                        	<logic:equal value="false" name="enzymeDTO" property="active">
                        		<td>Transferred to EC
                        			<html:text property="transferredToEc" tabindex="2" size="15" />
                       			</td>
                        	</logic:equal>
                        </tr>
                        <tr>
                          <td colspan="2" nowrap="nowrap"><b>New History Line:</b></td>
                        </tr>
                        <tr>
                          <td colspan="2" width="100%">
                          	<html:textarea property="historyLine" tabindex="3"
                          		cols="70" rows="2" />
                          </td>
                        </tr>
                        <tr>
                          <td colspan="2"><b>Additional History Information:</b></td>
                        </tr>
                        <tr>
                          <td colspan="2">
                          	<html:textarea property="latestHistoryEventNote" tabindex="4" cols="70" rows="5" />
                           	<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'latestHistoryEventNote');"
								title="Open xchars window" class="xchars"/>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="right" colspan="2">
                          	<html:reset tabindex="5" />&nbsp;
                          	<html:submit
                          		title="${enzymeDTO.active? 'Transfer entry' : 'Update notes'}"
                          		value="${enzymeDTO.active? 'Transfer entry' : 'Update notes'}"
                          		tabindex="6" styleClass="submitButton" />
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




