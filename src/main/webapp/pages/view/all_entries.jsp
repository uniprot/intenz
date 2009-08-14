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
                <table width="100%" border="0" cellspacing="4" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row">Result</td>
                    <td width="100px" class="data_region_header_row_small_blue" align="center">Status</td>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="3">&nbsp;</td>
                  </tr>
                  <logic:iterate name="enzymeListDTO" id="rowAt" property="result">
                  <logic:notEqual value="SIB" name="rowAt" property="source">
                  <tr>
                    <td>
                      <table cellspacing="5">
                        <tr>
                          <td align="right"><a href="searchId.do?id=<bean:write name="rowAt" property="enzymeId"/>&view=INTENZ"><bean:write name="rowAt" property="ec"/></a></td>
                          <logic:equal value="deleted" name="rowAt" property="eventClass"><td align="left" class="deleted_transferred">deleted</td></logic:equal>
                          <logic:equal value="transferred" name="rowAt" property="eventClass"><td align="left" class="deleted_transferred" align="left">transferred. <bean:write name="rowAt" property="eventNote" filter="false"/>.</td></logic:equal>
                          <logic:notEqual value="deleted" name="rowAt" property="eventClass">
                            <logic:notEqual value="transferred" name="rowAt" property="eventClass">
                              <td align="left"><bean:write name="rowAt" property="name" filter="false"/></td>
                            </logic:notEqual>
                          </logic:notEqual>
                        </tr>
                      </table>
                    </td>
                    <td align="center">
                      <bean:write name="rowAt" property="status"/>
                    </td>
                    <logic:equal value="true" name="rowAt" property="active">
                      <td>&nbsp;</td>
                    </logic:equal>
                    <logic:notEqual value="true" name="rowAt" property="active">
                      <td class="inactive">INACTIVE</td>
                    </logic:notEqual>
                  </tr>
                  </logic:notEqual>
                  </logic:iterate>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>




