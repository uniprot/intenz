<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

    <!---------------------------------------------- Content --------------------------------------------------------->
    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <table class="content_table" border="0" cellspacing="20" cellpadding="0">
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="20" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row">Add New Sub-Subclass</td>
                  </tr>
                  <tr>
                    <td>
                      <html:form action="addSubSubclass" focus="ec">
                      <table width="100%" cellpadding="3">
                        <logic:messagesPresent>
                          <tr>
                            <td colspan="2" class="error">
                              <html:messages id="error" property="ec">
                              	<bean:write name="error"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>
                        <tr>
                          <td valign="top" align="right"><b>EC:</b></td>
                          <td width="100%"><html:text property="ec" tabindex="1" size="15" /></td>
                        </tr>
                        <logic:messagesPresent>
                          <tr>
                            <td colspan="2" class="error">
                              <html:messages id="error" property="name">
                              	<bean:write name="error"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>
                        <tr>
                          <td valign="top" align="right"><b>Name:</b></td>
                          <td width="100%"><html:textarea property="name" tabindex="2" cols="50" rows="2" /></td>
                        </tr>
                        <tr>
                          <td valign="top" align="right"><b>Description:</b></td>
                          <td width="100%"><html:textarea property="description" tabindex="3" cols="50" rows="5" /></td>
                        </tr>
                        <tr>
                          <td width="100%" align="right" colspan="2"><html:reset tabindex="4" />&nbsp;<html:submit title="Create new sub-subclass in the database" value="Add sub-subclass" tabindex="5" styleClass="main_button" /></td>
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




