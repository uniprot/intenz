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
                    <td class="data_region_header_row">Classification</td>
                  </tr>
                  <tr>
                    <td>
                      <table width="100%" cellpadding="3">
                        <tr>
                          <td align="right"><b>Class:</b></td>
                          <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="subclassDTO" paramProperty="classEc" paramId="ec">EC <bean:write name="subclassDTO" property="classEc"/>&nbsp;&nbsp;<bean:write name="subclassDTO" property="className" filter="false"/></html:link></td>
                        </tr>
                        <tr>
                          <td align="right"><b>Subclass:</b></td>
                          <td width="100%"><b><html:link styleClass="no_decoration" action="searchEc" paramName="subclassDTO" paramProperty="ec" paramId="ec">EC <bean:write name="subclassDTO" property="ec"/>&nbsp;&nbsp;<bean:write name="subclassDTO" property="name" filter="false"/></html:link></b></td>
                        </tr>
                        <logic:notEqual value="" name="subclassDTO" property="description">
                        <tr>
                          <td align="right">&nbsp;</td>
                          <td width="100%"><bean:write name="subclassDTO" property="description" filter="false"/></td>
                        </tr>
                        </logic:notEqual>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td class="data_region_header_row">Sub-Subclasses</td>
                  </tr>
                  <tr>
                    <td>
                      <table width="100%" border="0">
                        <tr>
                          <td align="center">
                            <table cellspacing="8" border="0">
                              <logic:iterate name="subclassDTO" id="rowAt" property="subSubclasses">
                              <tr>
                                <td align="right">
                                  <b><html:link action="searchEc" paramName="rowAt" paramProperty="ec" paramId="ec"><bean:write name="rowAt" property="ec"/></html:link></b>
                                </td>
                                <td align="left">
                                  <bean:write name="rowAt" property="name" filter="false"/>
                                </td>
                              </tr>
                              </logic:iterate>
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
        </td>
      </tr>
    </table>




