<%@ page import="uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper,
                 uk.ac.ebi.intenz.webapp.dtos.IubmbEnzymeDTO,
                 uk.ac.ebi.intenz.webapp.helper.ReferenceHelper,
                 org.apache.struts.Globals,
                 org.apache.struts.taglib.html.Constants"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

  <table height="100%" width="100%" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <td align="center"><b>IUBMB Enzyme Nomenclature</b></td>
      </tr>
      <tr>
        <td align="center" class="ec">EC <bean:write name="iubmbEnzymeDTO" property="ec"/></td>
      </tr>
      <logic:equal value="SU" name="iubmbEnzymeDTO" property="statusCode"><tr><td class="suggested" align="center"><bean:message bundle="form" key="title.entry.status.suggestion" /></td></tr></logic:equal>
      <logic:equal value="PR" name="iubmbEnzymeDTO" property="statusCode"><tr><td class="proposed" align="center"><bean:message bundle="form" key="title.entry.status.proposal" /></td></tr></logic:equal>
      <tr>
        <td height="30">&nbsp;</td>
      </tr>
      <tr height="100%">
        <td valign="top" align="center">
          <table width="100%" class="content_table_with_menu" border="0" cellspacing="20" cellpadding="0">

          <!-- Tabs -->
          <div id="menuDiv">
            <ul id="menuList">
              <li><a href="searchId.do?id=<bean:write name="iubmbEnzymeDTO" property="id"/>&view=INTENZ&<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>" title="IntEnz view of this enzyme"><img border="0" width="14" height="12" src="images/blue_bullet.gif"/>&nbsp;IntEnz</a></li>
              <li class="selected"><a class="selected" name="here" href="#here"><img width="14" height="12" border="0" src="images/green_bullet.gif"/>&nbsp;NC-IUBMB</a></li>
              <li><a href="searchId.do?id=<bean:write name="iubmbEnzymeDTO" property="id"/>&view=SIB&<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>" title="ENZYME view of this enzyme"><img width="14" border="0" height="12" src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>

          <tr>
            <logic:equal value="transferred" name="iubmbEnzymeDTO" property="latestHistoryEventClass">
              <td nowrap="nowrap" class="data_region_name_cell">Transferred entry:</td>
            </logic:equal>
            <logic:equal value="deleted" name="iubmbEnzymeDTO" property="latestHistoryEventClass">
              <td nowrap="nowrap" class="data_region_name_cell">Deleted entry:</td>
            </logic:equal>

            <bean:size id="listSize" name="iubmbEnzymeDTO" property="commonNames" />

            <td width="100%" nowrap="nowrap">
              <logic:greaterThan name="listSize" value="0">
                <bean:write name="iubmbEnzymeDTO" property="commonNames[0].name" filter="false" />.
              </logic:greaterThan>
              <logic:equal value="0" name="listSize">&nbsp;</logic:equal>

<%--              <logic:equal value="transferred" name="iubmbEnzymeDTO" property="latestHistoryEventClass">--%>
                <bean:write name="iubmbEnzymeDTO" property="latestHistoryEventNote" filter="false" />
<%--              </logic:equal>--%>
            </td>
          </tr>

          <!-- History line -->
          <tr>
            <td colspan="3" width="100%" class="data_region_name_cell" align="center">
              [<bean:write name="iubmbEnzymeDTO" property="historyLine" filter="false" />]
            </td>
          </tr>
          </table>
        </td>
      </tr>
    </table>
