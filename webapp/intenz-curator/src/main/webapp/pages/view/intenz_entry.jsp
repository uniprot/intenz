<%@ page import="uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper,
				 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.dtos.ReactionDTO,
                 uk.ac.ebi.intenz.webapp.dtos.CofactorDTO,
				 org.apache.struts.action.ActionMessages,
				 org.apache.struts.Globals,
				 org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities,
                 uk.ac.ebi.xchars.SpecialCharacters"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<%
	SpecialCharacters encoding = (SpecialCharacters) session.getAttribute("characters");
	EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
%>

  <table height="100%" width="100%" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <logic:messagesPresent>
        <tr>
          <td class="error">
            <html:messages id="error">
            	<bean:write name="error" filter="false"/>
            </html:messages>
          </td>
        </tr>
        </logic:messagesPresent>
      </tr>
      <tr>
        <td align="center">
          <h1 class="index">IntEnz Enzyme Nomenclature</h1>
        </td>
      </tr>
      <tr>
        <td align="center" class="ec">EC <bean:write name="enzymeDTO" property="ec" filter="false"/></td>
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
      <logic:equal value="PR" name="enzymeDTO" property="statusCode">
    	  <tr><td class="proposed" align="center"><bean:message bundle="form" key="title.entry.status.proposal" /></td></tr>
      </logic:equal>
      <c:if test="${not preview}">
      <tr>
        <td class="data_region_header_row">Classification</td>
      </tr>
      <tr>
        <td>
          <table width="100%" cellpadding="3">
            <tr>
              <td align="right"><b>Class:</b></td>
              <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="enzymeDTO" paramProperty="classEc" paramId="ec">EC <bean:write name="enzymeDTO" property="classEc"/>&nbsp;&nbsp;<bean:write name="enzymeDTO" property="className" filter="false"/></html:link></td>
            </tr>
            <tr>
              <td align="right"><b>Subclass:</b></td>
              <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="enzymeDTO" paramProperty="subclassEc" paramId="ec">EC <bean:write name="enzymeDTO" property="subclassEc"/>&nbsp;&nbsp;<bean:write name="enzymeDTO" property="subclassName" filter="false"/></html:link></td>
            </tr>
            <tr>
              <td align="right"><b>Sub-Subclass:</b></td>
              <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="enzymeDTO" paramProperty="subSubclassEc" paramId="ec">EC <bean:write name="enzymeDTO" property="subSubclassEc"/>&nbsp;&nbsp;<bean:write name="enzymeDTO" property="subSubclassName" filter="false"/></html:link></td>
            </tr>
          </table>
        </td>
      </tr>
      </c:if>
      <tr>
        <td height="30">&nbsp;</td>
      </tr>
      <tr height="100%">
        <td valign="top" align="center">
          <table width="100%" class="content_table_with_menu" border="0" cellspacing="20" cellpadding="0">

          <!-- Tabs -->
		<c:set var="tabUrl">searchId.do?id=${enzymeDTO.id}&amp;<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %></c:set>
          <div id="menuDiv">
            <ul id="menuList">
              <li class="selected"><a class="selected" name="here" href="#here"><img
              	border="0" width="14" height="12" src="images/blue_bullet.gif"/>&nbsp;IntEnz*</a></li>
              <li><a href="${preview? 'populatePreviewSibEnzymeDTO.do' : tabUrl}${preview? '' : '&view=SIB'}"
              	title="ENZYME view of this enzyme"><img
              		width="14" height="12" border="0" src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>

            <tr>
              <td>
                <table width="100%" border="0" cellspacing="4" cellpadding="0">

                  <%
                    if(enzymeDTO.getCommonNames().size() > 0 || enzymeDTO.getSynonyms().size() > 0 ||
                       (enzymeDTO.getSystematicName() != null
                        && !enzymeDTO.getSystematicName().getXmlName().equals("")
                        && !enzymeDTO.getSystematicName().getXmlName().equals("-"))) {
                  %>
                  <!-- NAMES -->

                  <tr>
                    <td colspan="2" class="data_region_header_row">Names</td>
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">Source</td>
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">View</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>


                  <%-- COMMON NAMES --%>

                  <bean:size id="commonNamesSize" name="enzymeDTO" property="commonNames"/>
                  <logic:greaterThan name="commonNamesSize" value="0">
                    <logic:iterate id="commonName" name="enzymeDTO" property="commonNames" indexId="index">
                      <tr>
                        <td colspan="2">
                          <table>
                            <tr>
                              <logic:equal name="index" value="0">
                                <logic:greaterThan name="commonNamesSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Common names:</td>
                                </logic:greaterThan>
                                <logic:equal name="commonNamesSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Common name:</td>
                                </logic:equal>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px" class="data_region_name_cell">&nbsp;</td>
                              </logic:notEqual>
                              <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                                 <td><bean:write name="commonName" property="name" filter="false" /></td>
                              </logic:equal>
                              <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                                 <td><bean:write name="commonName" property="xmlName" filter="true" /></td>
                              </logic:equal>
                            </tr>
                          </table>
                        </td>
                        <td width="100px" align="center" valign="top"><bean:write name="commonName" property="sourceDisplay" filter="false" /></td>
                        <td width="100px" align="center" valign="top"><bean:write name="commonName" property="viewDisplayImage" filter="false" /></td>
                      </tr>
                    </logic:iterate>
                  </logic:greaterThan>


                  <%-- SYSTEMATIC NAME --%>

                  <logic:notEqual value="" name="enzymeDTO" property="systematicName.name">
                  <logic:notEqual value="-" name="enzymeDTO" property="systematicName.name">
                    <tr>
                      <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <table>
                          <tr>
                            <td width="130px" class="data_region_name_cell">Systematic name:</td>
                            <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                              <td>
                              	<div class="longLine">
                              		<bean:write name="enzymeDTO" property="systematicName.name" filter="false" />
                              	</div>
                              </td>
                            </logic:equal>
                            <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                              <td>
                              	<div class="longLine">
                              		<bean:write name="enzymeDTO" property="systematicName.xmlName" filter="true" />
                              	</div>
                              </td>
                            </logic:equal>
                          </tr>
                        </table>
                      </td>
                      <td width="100px" align="center" valign="top"><bean:write name="enzymeDTO" property="systematicName.sourceDisplay" filter="false" /></td>
                      <td width="100px" align="center" valign="top"><bean:write name="enzymeDTO" property="systematicName.viewDisplayImage" filter="false" /></td>
                    </tr>
                  </logic:notEqual>
                  </logic:notEqual>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>


                  <%-- SYNONYMS --%>

                  <bean:size id="synonymsSize" name="enzymeDTO" property="synonyms" />
                  <logic:greaterThan name="synonymsSize" value="0">
                    <logic:iterate id="synonym" name="enzymeDTO" property="synonyms" indexId="index">
                      <tr>
                        <td colspan="2">
                          <table>
                            <tr>
                              <logic:equal name="index" value="0">
                                <logic:greaterThan name="synonymsSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Other names:</td>
                                </logic:greaterThan>
                                <logic:equal name="synonymsSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Other name:</td>
                                </logic:equal>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px" class="data_region_name_cell">&nbsp;</td>
                              </logic:notEqual>
                              <td>
                              <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                                 <bean:write name="synonym" property="name" filter="false" />
                              </logic:equal>
                              <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                                 <bean:write name="synonym" property="xmlName" filter="true" />
                              </logic:equal>
                              <logic:notEqual name="synonym" property="qualifier" value="">&nbsp;<b><bean:write name="synonym" property="qualifierDisplay" filter="false" /></b></logic:notEqual></td>
                            </tr>
                          </table>
                        </td>
                        <td width="100px" align="center" valign="top"><bean:write name="synonym" property="sourceDisplay" filter="false" /></td>
                        <td width="100px" align="center" valign="top"><bean:write name="synonym" property="viewDisplayImage" filter="false" /></td>
                      </tr>
                    </logic:iterate>
                  </logic:greaterThan>

                  <tr>
                    <td height="40px" colspan="4">&nbsp;</td>
                  </tr>

                  <%
                     }
                  %>

                  <%-- REACTIONS --%>
                  <%@ include file="reactions.jspf" %>


                  <!-- COFACTORS -->
                  <%@ include file="cofactors.jspf" %>

                  <!-- COMMENT -->

                  <bean:size id="listSize" name="enzymeDTO" property="comments" />
                  <logic:greaterThan name="listSize" value="0">
                  <tr>
                    <logic:equal name="listSize" value="1">
                      <td colspan="2" class="data_region_header_row">Comment</td>
                    </logic:equal>
                    <logic:greaterThan name="listSize" value="1">
                      <td colspan="2" class="data_region_header_row">Comments</td>
                    </logic:greaterThan>
                    <td width="100px" class="data_region_header_row_small_blue" align="center">Source</td>
                    <td width="100px" class="data_region_header_row_small_blue" align="center">View</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>

                    <% int iii = 0; %>
                    <logic:iterate id="comment" name="enzymeDTO" property="comments" >
                      <tr>
                           <td colspan="2">
								<logic:greaterThan name="listSize" value="1">
		                             <b><%= (iii+1) %>.</b>&nbsp;
								</logic:greaterThan>
								<logic:equal name="enzymeDTO" property="xcharsView" value="false">
									<bean:define id="c" name="comment" property="comment" type="String"/>
									<%= IntEnzUtilities.linkMarkedEC(c, true) %>
								</logic:equal>
		                        <logic:equal name="enzymeDTO" property="xcharsView" value="true">
									<bean:write name="comment" property="xmlComment" filter="true" />
		                        </logic:equal>
                           </td>
                        <td width="100px" align="center" valign="top"><bean:write name="comment" property="sourceDisplay" filter="false" /></td>
                        <td width="100px" align="center" valign="top"><bean:write name="comment" property="viewDisplayImage" filter="false" /></td>
                      </tr>
                      <% iii++; %>
                    </logic:iterate>


                  <tr>
                    <td height="40px" colspan="4">&nbsp;</td>
                  </tr>
                  </logic:greaterThan>

                  <!-- LINKS -->

                  <bean:size id="listSize" name="enzymeDTO" property="links" />
                  <logic:greaterThan name="listSize" value="0">
                  <tr>
                    <td colspan="2" class="data_region_header_row">Links to other databases</td>
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">Source</td>
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">View</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <c:choose>
	                  <c:when test="${enzymeDTO.xcharsView}">
	                    <%= EnzymeLinksHelper.renderLinks(enzymeDTO.getAllLinks(), enzymeDTO.getEc()) %>
	                  </c:when>
	                  <c:otherwise>
	                  	<xchars:translate>
                    	<%= EnzymeLinksHelper.renderLinks(enzymeDTO.getAllLinks(), enzymeDTO.getEc()) %>
	                  	</xchars:translate>
	                  </c:otherwise>
                  </c:choose>
<%--
                    <tr style="background-color: #ffa">
	                    <c:forEach var="linkDTO" items="${enzymeDTO.allLinks}">
	                    	<c:choose>
	                    		<c:when test="${linkDTO.databaseCode ne 'SWISSPROT'}">
	                    		</c:when>
	                    		<c:otherwise>
	                    		</c:otherwise>
	                    	</c:choose>
	                    </c:forEach>
	                    <%@ include file="autoLinks.jspf" %>
                    </tr>
 --%>
                  <tr>
                    <td height="40px" colspan="4">&nbsp;</td>
                  </tr>
                  </logic:greaterThan>

                  <!-- REFERENCES -->

                  <bean:size id="listSize" name="enzymeDTO" property="references" />
                  <logic:greaterThan name="listSize" value="0">
                      <tr>
                        <logic:equal name="listSize" value="1">
                          <td colspan="2" class="data_region_header_row">Reference</td>
                        </logic:equal>
                        <logic:greaterThan name="listSize" value="1">
                          <td colspan="2" class="data_region_header_row">References</td>
                        </logic:greaterThan>
                          <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">Source</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">View</td>
                      </tr>

                      <tr>
                        <td colspan="4">&nbsp;</td>
                      </tr>
                      <% int refIndex = 0; %>
                      <logic:iterate id="reference" name="enzymeDTO" property="references" >
                      <% refIndex++; %>
                      <tr>
                        <td colspan="2"><%= refIndex %>.

                          <!-- journal -->
                          <logic:equal value="J" name="reference" property="type">
                            <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                              <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="pubName" filter="false" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year">(<bean:write name="reference" property="year" filter="false" />)&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" />.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage">&nbsp;only.&nbsp;</logic:equal></logic:notEqual><logic:notEqual value="" name="reference" property="pubMedId">[PMID: <a target="_blank" href="http://europepmc.org/abstract/MED/${reference.pubMedId}"><bean:write name="reference" property="pubMedId" filter="false" /></a>]</logic:notEqual>
                            </logic:equal>
                            <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                              <bean:write name="reference" property="xmlAuthors" filter="true" />&nbsp;<bean:write name="reference" property="xmlTitle" filter="true" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="xmlPubName" filter="true" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year">(<bean:write name="reference" property="year" filter="false" />)&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" />.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage">&nbsp;only.&nbsp;</logic:equal></logic:notEqual><logic:notEqual value="" name="reference" property="pubMedId">[PMID: <a target="_blank" href="http://europepmc.org/abstract/MED/${reference.pubMedId}"><bean:write name="reference" property="pubMedId" filter="false" /></a>]</logic:notEqual>
                            </logic:equal>
                          </logic:equal>

                          <!-- book -->
                          <logic:equal value="B" name="reference" property="type">
                           <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                              <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="editor">In:&nbsp;<bean:write name="reference" property="editor" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="pubName" filter="false" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="edition"> <bean:write name="reference" property="edition" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisher"><bean:write name="reference" property="publisher" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisherPlace"><bean:write name="reference" property="publisherPlace" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year"><logic:equal value="" name="reference" property="firstPage"><logic:equal value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" /></logic:equal><logic:notEqual value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:equal><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><logic:notEqual value="" name="reference" property="lastPage"> pp.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage"> p.&nbsp;</logic:equal><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" /></logic:notEqual>.</logic:notEqual>
                           </logic:equal>
                           <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                              <bean:write name="reference" property="xmlAuthors" filter="true" />&nbsp;<bean:write name="reference" property="xmlTitle" filter="true" />&nbsp;<logic:notEqual value="" name="reference" property="editor">In:&nbsp;<bean:write name="reference" property="xmlEditor" filter="true" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="xmlPubName" filter="true" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="edition"> <bean:write name="reference" property="edition" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisher"><bean:write name="reference" property="xmlPublisher" filter="true" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisherPlace"><bean:write name="reference" property="publisherPlace" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year"><logic:equal value="" name="reference" property="firstPage"><logic:equal value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" /></logic:equal><logic:notEqual value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:equal><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><logic:notEqual value="" name="reference" property="lastPage"> pp.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage"> p.&nbsp;</logic:equal><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" /></logic:notEqual>.</logic:notEqual>
                           </logic:equal>
                          </logic:equal>

                          <!-- patent -->
                          <logic:equal value="P" name="reference" property="type">
                           <logic:equal name="enzymeDTO" property="xcharsView" value="false">
                              <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"> <i><bean:write name="reference" property="pubName" filter="false" /></i></logic:notEqual><logic:notEqual value="" name="reference" property="year">,&nbsp;<bean:write name="reference" property="year" filter="false" /></logic:notEqual>
                           </logic:equal>
                           <logic:equal name="enzymeDTO" property="xcharsView" value="true">
                              <bean:write name="reference" property="xmlAuthors" filter="true" />&nbsp;<bean:write name="reference" property="xmlTitle" filter="true" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"> <i><bean:write name="reference" property="xmlPubName" filter="false" /></i></logic:notEqual><logic:notEqual value="" name="reference" property="year">,&nbsp;<bean:write name="reference" property="year" filter="false" /></logic:notEqual>
                           </logic:equal>
                          </logic:equal>
                        </td>
                        <td width="100px" align="center" valign="top"><bean:write name="reference" property="sourceDisplay" filter="false" /></td>
                        <td width="100px" align="center" valign="top"><bean:write name="reference" property="viewDisplayImage" filter="false" /></td>
                      </tr>
                    </logic:iterate>

                  <tr>
                    <td height="40px" colspan="4">&nbsp;</td>
                  </tr>
                  </logic:greaterThan>

                  <!-- HOUSEKEEPING DATA -->

                  <tr>
                    <td colspan="4" class="data_region_header_row">Housekeeping data</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>

                  <logic:notEqual value="" name="enzymeDTO" property="note">
                  <tr>
                    <td colspan="4">
                      <table>
                        <tr>
                          <td width="130px" class="data_region_name_cell">Note:</td>
                          <td><bean:write name="enzymeDTO" property="note" filter="false" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  </logic:notEqual>
                  <tr>
                    <td colspan="4">
                      <table>
                        <tr>
                          <td width="130px" class="data_region_name_cell">History line:</td>
                          <td><bean:write name="enzymeDTO" property="historyLine" filter="false" /></td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <logic:equal value="transferred" name="enzymeDTO" property="latestHistoryEventClass">
                  <tr>
                    <td colspan="4">
                      <table>
                        <tr>
                          <td width="130px" class="data_region_name_cell">History note:</td>
                          <td>
		                  <%= IntEnzUtilities.linkMarkedEC(encoding.xml2Display(enzymeDTO.getLatestHistoryEventNote()), true) %>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  </logic:equal>

                  <tr>
                    <td colspan="4">
                      <table>
                        <tr>
                          <td width="130px" class="data_region_name_cell">Status:</td>
                          <td><bean:write name="enzymeDTO" property="statusText" filter="false" /></td>
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
