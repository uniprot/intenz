<%@ page import="org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO"); %>

  <table height="100%" width="100%" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <td align="left">
          <h1 class="enzyme">ENZYME: EC <bean:write name="sibEnzymeDTO" property="ec"/></h1>
        </td>
      </tr>
      <tr>
        <logic:equal value="SU" name="sibEnzymeDTO" property="statusCode"><td class="suggested" align="center"><bean:message bundle="form" key="title.entry.status.suggestion" /></td></logic:equal>
        <logic:equal value="PR" name="sibEnzymeDTO" property="statusCode"><td class="proposed" align="center"><bean:message bundle="form" key="title.entry.status.proposal" /></td></logic:equal>
      </tr>
      <tr>
        <td height="30">&nbsp;</td>
      </tr>
      <tr height="100%">
        <td valign="top" align="center">
          <table width="100%" class="content_table_with_menu" border="0" cellspacing="20" cellpadding="0">

          <!-- Tabs -->
		<c:set var="tabUrl">searchId.do?id=${sibEnzymeDTO.id}&amp;<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %></c:set>
          <div id="menuDiv">
            <ul id="menuList">
              <li><a href="${preview? 'populatePreviewIntEnzEnzymeDTO.do' : tabUrl}${preview? '' : '&view=INTENZ'}"
              	title="IntEnz view of this enzyme containing all available data"><img
              		border="0" width="14" height="12" src="images/blue_bullet.gif"/>&nbsp;IntEnz*</a></li>
              <li><a href="${preview? 'populatePreviewIubmbEnzymeDTO.do' : tabUrl}${preview? '' : '&view=IUBMB'}"
              	title="NC-IUBMB view of this enzyme"><img
              		border="0" width="14" height="12" src="images/green_bullet.gif"/>&nbsp;NC-IUBMB</a></li>
              <li class="selected"><a class="selected" name="here" href="#here"><img border="0" width="14" height="12" src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><pre><bean:write name="sibEnzymeDTO" property="entry" filter="false" /></pre></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
