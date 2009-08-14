<%@ page import="uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities,
                 org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeNamesHelper,
                 uk.ac.ebi.xchars.SpecialCharacters"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%
	final EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
	SpecialCharacters encoding = (SpecialCharacters) session.getAttribute("characters");
	String commonName = EnzymeNamesHelper.getIntEnzCommonName(enzymeDTO.getCommonNames());
%>
  <table height="100%" width="100%" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <td align="center">
          <h1 class="index">IntEnz Enzyme Nomenclature</h1>
        </td>
      </tr>
      <tr>
        <td align="center" class="ec">EC <bean:write name="enzymeDTO" property="ec"/></td>
      </tr>
      <tr>
        <td align="center">
          Enzyme ID: <bean:write name="enzymeDTO" property="id" filter="false"/>
	      <logic:equal value="false" name="enzymeDTO" property="active">
	      	(<b style="color: red">INACTIVE</b>)
	      </logic:equal>
        </td>
      </tr>
      <logic:equal value="SU" name="enzymeDTO" property="statusCode"><tr><td class="suggested" align="center"><bean:message bundle="form" key="title.entry.status.suggestion" /></td></tr></logic:equal>
      <logic:equal value="PR" name="enzymeDTO" property="statusCode"><tr><td class="proposed" align="center"><bean:message bundle="form" key="title.entry.status.proposal" /></td></tr></logic:equal>
      <tr>
        <td height="30">&nbsp;</td>
      </tr>
      <tr height="100%">
        <td valign="top" align="center">
          <table width="100%" class="content_table_with_menu" border="0" cellspacing="20" cellpadding="0">

          <!-- Tabs -->
          <div id="menuDiv">
            <ul id="menuList">
              <li class="selected"><a class="selected" name="here" href="#here"><img border="0" width="14" height="12" src="images/blue_bullet.gif"/>&nbsp;IntEnz</a></li>
              <li><a href="searchId.do?id=<bean:write name="enzymeDTO" property="id"/>&view=IUBMB&<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>" title="NC-IUBMB view of this enzyme"><img width="14" height="12" border="0" src="images/green_bullet.gif"/>&nbsp;NC-IUBMB</a></li>
              <li><a href="searchId.do?id=<bean:write name="enzymeDTO" property="id"/>&view=SIB&<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>" title="ENZYME view of this enzyme"><img width="14" height="12" border="0" src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>

            <tr>
              <td>
                <table width="100%" border="0" cellspacing="4" cellpadding="0">

                  <tr>
                    <td colspan="4" class="data_region_header_row">Transferred entry</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>

                  <%
                    if(!commonName.equals("")) {
                  %>
                  <tr>
                    <td width="130px" nowrap="nowrap" class="data_region_name_cell">Common name:</td>
                    <td colspan="3" width="100%" ><%= commonName %></td>
                  </tr>
                  <%
                    }
                  %>
                  <tr>
                    <td width="130px" nowrap="nowrap" class="data_region_name_cell">Transferred to:</td>
                    <td colspan="3" width="100%" ><a href="searchEc.do?ec=<bean:write name="enzymeDTO" property="transferredToEc"/>&view=INTENZ"><bean:write name="enzymeDTO" property="transferredToEc"/></a></td>
                  </tr>
                  <tr>
                    <td width="130px" nowrap="nowrap" class="data_region_name_cell">Note:</td>
                    <td colspan="3" width="100%" >
                  		<%= IntEnzUtilities.linkMarkedEC(encoding.xml2Display(enzymeDTO.getLatestHistoryEventNote()), true) %>
                    </td>
                  </tr>
                  <tr>
                    <td width="130px" nowrap="nowrap" class="data_region_name_cell">History:</td>
                    <td colspan="3" width="100%" ><bean:write name="enzymeDTO" property="historyLine"/></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
