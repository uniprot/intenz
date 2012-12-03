<%@ page import="uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.dtos.IubmbEnzymeDTO,
                 org.apache.struts.Globals,
                 org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper,
                 uk.ac.ebi.intenz.webapp.helper.ReferenceHelper,
                 uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% IubmbEnzymeDTO iubmbEnzymeDTO = ((IubmbEnzymeDTO) request.getAttribute("iubmbEnzymeDTO")); %>

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
          <!-- Tabs -->
		<c:set var="tabUrl">searchId.do?id=${iubmbEnzymeDTO.id}&amp;<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %></c:set>
          <div id="menuDiv">
            <ul id="menuList">
              <li><a href="${preview? 'populatePreviewIntEnzEnzymeDTO.do' : tabUrl}${preview? '' : '&view=INTENZ'}"
              	title="IntEnz view of this enzyme containing all available data"><img
              		border="0" width="14" height="12"
              		src="images/blue_bullet.gif"/>&nbsp;IntEnz*</a></li>
              <li class="selected"><a class="selected" name="here" href="#here"><img
              	width="14" height="12" border="0" src="images/green_bullet.gif"/>&nbsp;NC-IUBMB</a></li>
              <li><a href="${preview? 'populatePreviewSibEnzymeDTO.do' : tabUrl}${preview? '' : '&view=SIB'}"
              	title="ENZYME view of this enzyme"><img
              		width="14" border="0" height="12"
              		src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>

<c:choose>
<c:when test="${iubmbEnzymeDTO.statusCode eq 'PM'}">
<br/>This is a preliminary entry, neither proposed nor accepted by NC-IUBMB.
</c:when>
<c:otherwise>

          <table width="100%" class="content_table_with_menu" border="0" cellspacing="20" cellpadding="0">
          <!-- Common name(s) -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="commonNames" />
          <logic:greaterThan name="listSize" value="0">
            <logic:equal name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell">Common name:</td>
              <td width="100%"><bean:write name="iubmbEnzymeDTO" property="commonNames[0].name" filter="false" /></td>
            </tr>
            </logic:equal>
            <logic:greaterThan name="listSize" value="1">
              <% int iii = 0; %>
              <logic:iterate id="commonName" name="iubmbEnzymeDTO" property="commonNames" >
                <tr>
                  <%
                    if(iii == 0) {
                  %>
                  <td width="130px" class="data_region_name_cell">Common names:</td>
                  <%
                    } else {
                  %>
                  <td width="130px">&nbsp;</td>
                  <%
                    }
                    iii++;
                  %>
                  <td><bean:write name="commonName" property="name" filter="false" /></td>
                </tr>
              </logic:iterate>
            </logic:greaterThan>
          </logic:greaterThan>

          <!-- Reaction(s) -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="reactions" />
          <logic:greaterThan name="listSize" value="0">
            <logic:equal name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell">Reaction:</td>
              <td width="100%">
        		<div class="longLine">
	              	<bean:define id="r" name="iubmbEnzymeDTO" property="reactions[0].xmlTextualRepresentation" type="String" />
					<xchars:translate>
			    	<logic:greaterThan value="0" name="iubmbEnzymeDTO" property="reactions[0].id">
							<%= r.replace("<?>","=").replace("=>","=").replace("<=","=").replace("<=>","=") %>
			     	</logic:greaterThan>
					<logic:equal value="0" name="iubmbEnzymeDTO" property="reactions[0].id">
			                    <%= IntEnzUtilities.linkMarkedEC(r, true) %>
			        </logic:equal>
					</xchars:translate>
				</div>
              </td>
            </tr>
            </logic:equal>
            <logic:greaterThan name="listSize" value="1">
              <% int steps = 0; %>
              <logic:iterate id="reaction" name="iubmbEnzymeDTO" property="reactions" indexId="index">
                <bean:define id="r" name="reaction" property="xmlTextualRepresentation" type="String"/>
                <tr>
                	<logic:equal value="0" name="index">
	                  <td width="130px" class="data_region_name_cell">Reactions:</td>
                	</logic:equal>
                	<logic:notEqual value="0" name="index">
	                  <td width="130px">&nbsp;</td>
                	</logic:notEqual>
                  <td>
					<div class="longLine">
				<% if (!r.matches("\\(?\\d\\p{Lower}\\)?\\s.*")){ %>
					(<%= (index.intValue() + 1 - steps) %>)&nbsp;
				<% } else {
				    steps++; %>
					<span style="padding-left: 1em"></span>
				<% } %>
						<xchars:translate>
				    	<logic:greaterThan value="0" name="reaction" property="id">
							<%= r.replace("<?>","=").replace("=>","=").replace("<=","=").replace("<=>","=") %>
				     	</logic:greaterThan>
						<logic:equal value="0" name="reaction" property="id">
		                    <%= IntEnzUtilities.linkMarkedEC(r, true) %>
				        </logic:equal>
						</xchars:translate>
					</div>
                  </td>
                </tr>
              </logic:iterate>
            </logic:greaterThan>
          </logic:greaterThan>

          <!-- Other name(s) -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="synonyms" />
          <logic:greaterThan name="listSize" value="0">
            <logic:equal name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell">Other name:</td>
              <td width="100%"><bean:write name="iubmbEnzymeDTO" property="synonyms[0].name" filter="false" /><logic:notEqual name="iubmbEnzymeDTO" property="synonyms[0].qualifier" value="">&nbsp;<b><bean:write name="iubmbEnzymeDTO" property="synonyms[0].qualifierDisplay" filter="false" /></b></logic:notEqual></td>
            </tr>
            </logic:equal>
            <logic:greaterThan name="listSize" value="1">
              <% int iii = 0; %>
              <logic:iterate id="synonym" name="iubmbEnzymeDTO" property="synonyms" >
                <tr>
                  <%
                    if(iii == 0) {
                  %>
                  <td width="130px" class="data_region_name_cell">Other names:</td>
                  <%
                    } else {
                  %>
                  <td width="130px">&nbsp;</td>
                  <%
                    }
                    iii++;
                  %>
                  <td><bean:write name="synonym" property="name" filter="false" /><logic:notEqual name="synonym" property="qualifier" value="">&nbsp;<b><bean:write name="synonym" property="qualifierDisplay" filter="false" /></b></logic:notEqual></td>
                </tr>
              </logic:iterate>
            </logic:greaterThan>
          </logic:greaterThan>

          <!-- Systematic name -->
          <logic:notEqual value="" name="iubmbEnzymeDTO" property="systematicName.name">
          <logic:notEqual value="-" name="iubmbEnzymeDTO" property="systematicName.name">
            <tr>
              <td width="130px" class="data_region_name_cell">Systematic name:</td>
              <td width="100%">
				<div class="longLine">
              		<bean:write name="iubmbEnzymeDTO" property="systematicName.name" filter="false" />
				</div>
              </td>
            </tr>
          </logic:notEqual>
          </logic:notEqual>

          <!-- Comments -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="comments" />
          <logic:greaterThan name="listSize" value="0">
            <% int iii = 0; %>
            <logic:iterate id="comment" name="iubmbEnzymeDTO" property="comments" >
              <tr>
                <%
                  if(iii == 0) {
                %>
                <td width="130px" class="data_region_name_cell">Comments:</td>
                <%
                  } else {
                %>
                <td width="130px">&nbsp;</td>
                <%
                  }
                  iii++;
                %>
                <td><bean:write name="comment" property="comment" filter="false" /></td>
              </tr>
            </logic:iterate>
          </logic:greaterThan>

          <!-- Link(s) to other databases -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="links" />
          <logic:greaterThan name="listSize" value="0">
            <logic:equal name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell" valign="top">Link to other database:</td>
              <td width="100%"><bean:write name="iubmbEnzymeDTO" property="links[0].databaseName" filter="false" /></td>
            </tr>
            </logic:equal>
            <logic:greaterThan name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell" valign="top">Links to other databases:</td>
              <td>
                <%= EnzymeLinksHelper.renderIubmbLinks(iubmbEnzymeDTO.getLinks(), iubmbEnzymeDTO.getEc()) %>
              </td>
            </tr>
            </logic:greaterThan>
          </logic:greaterThan>

          <!-- References -->
          <bean:size id="listSize" name="iubmbEnzymeDTO" property="references" />

          <logic:greaterThan name="listSize" value="0">
            <logic:equal name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell" valign="top">Reference:</td>
              <td width="100%"><%= ReferenceHelper.renderReferences(iubmbEnzymeDTO.getReferences())%></td>
            </tr>
            </logic:equal>
            <logic:greaterThan name="listSize" value="1">
            <tr>
              <td width="130px" class="data_region_name_cell" valign="top">References:</td>
              <td width="100%"><%= ReferenceHelper.renderReferences(iubmbEnzymeDTO.getReferences())%></td>
            </tr>
            </logic:greaterThan>
          </logic:greaterThan>

          <!-- History line -->
          <tr>
            <td colspan="2" width="100%" class="data_region_name_cell" align="center">
              [<bean:write name="iubmbEnzymeDTO" property="historyLine" filter="false" />]
            </td>
          </tr>
          </table>
</c:otherwise>
</c:choose>
        </td>
      </tr>
    </table>
