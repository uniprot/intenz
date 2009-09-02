<%@ page import="uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper,
                 uk.ac.ebi.intenz.webapp.dtos.CofactorDTO,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
                 uk.ac.ebi.intenz.webapp.utilities.IntEnzUtilities,
                 uk.ac.ebi.xchars.SpecialCharacters"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%
	EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
	SpecialCharacters encoding = (SpecialCharacters) session.getAttribute("characters");
%>

  <table height="100%" width="100%" border="0" cellspacing="8" cellpadding="0">
      <tr>
        <logic:messagesPresent>
        <tr>
          <td class="error">
            <html:messages id="error"><bean:write name="error" filter="false"/></html:messages>
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
              <li class="selected"><a class="selected" name="here" href="#here"><img border="0" width="14" height="12" src="images/blue_bullet.gif"/>&nbsp;IntEnz*</a></li>
              <li><a href="populatePreviewIubmbEnzymeDTO.do" title="NC-IUBMB preview of this enzyme"><img width="14" height="12" border="0" src="images/green_bullet.gif"/>&nbsp;NC-IUBMB</a></li>
              <li><a href="populatePreviewSibEnzymeDTO.do" title="ENZYME preview of this enzyme"><img width="14" height="12" border="0" src="images/red_bullet.gif"/>&nbsp;ENZYME</a></li>
            </ul>
          </div>

            <tr>
              <td>
                <table width="100%" border="0" cellspacing="4" cellpadding="0">

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

                  <bean:size id="listSize" name="enzymeDTO" property="commonNames"/>
                  <logic:greaterThan name="listSize" value="0">
                    <logic:iterate id="commonName" name="enzymeDTO" property="commonNames" indexId="index">
                      <tr>
                        <td colspan="2">
                          <table>
                            <tr>
                              <logic:equal name="index" value="0">
                                <logic:greaterThan name="listSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Common names:</td>
                                </logic:greaterThan>
                                <logic:equal name="listSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Common name:</td>
                                </logic:equal>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px" class="data_region_name_cell">&nbsp;</td>
                              </logic:notEqual>
                              <td nowrap="nowrap"><bean:write name="commonName" property="name" filter="false" /></td>
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
                            <td>
			             		<div class="longLine">
            	                	<bean:write name="enzymeDTO" property="systematicName.name" filter="false" />
								</div>
                            </td>
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

                  <bean:size id="listSize" name="enzymeDTO" property="synonyms" />
                  <logic:greaterThan name="listSize" value="0">
                    <logic:iterate id="synonym" name="enzymeDTO" property="synonyms" indexId="index">
                      <tr>
                        <td colspan="2">
                          <table>
                            <tr>
                              <logic:equal name="index" value="0">
                                <logic:greaterThan name="listSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Other names:</td>
                                </logic:greaterThan>
                                <logic:equal name="listSize" value="1">
                                  <td width="130px" class="data_region_name_cell">Other name:</td>
                                </logic:equal>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px" class="data_region_name_cell">&nbsp;</td>
                              </logic:notEqual>
                              <td nowrap="nowrap"><bean:write name="synonym" property="name" filter="false" /><logic:notEqual name="synonym" property="qualifier" value="">&nbsp;<b><bean:write name="synonym" property="qualifierDisplay" filter="false" /></b></logic:notEqual></td>
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
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">Source</td>
                    <td width="100px" align="center" class="data_region_header_row_small_blue" align="center">View</td>
                  </tr>

                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>

                    <logic:iterate id="comment" name="enzymeDTO" property="comments" indexId="index">
                      <tr>
                        <td colspan="2">
                          <logic:greaterThan value="1" name="listSize">
                              <b><%= index.intValue()+1 %>.</b>&nbsp;
                          </logic:greaterThan>
                          <bean:define id="c" name="comment" property="comment" type="String"/>
                          <logic:equal value="SIB" name="comment" property="view">
                            <%= c %>
                          </logic:equal>
                          <logic:notEqual value="SIB" name="comment" property="view">
                            <%= IntEnzUtilities.linkMarkedEC(c, true) %>
                          </logic:notEqual>
                        </td>
                        <td width="100px" align="center" valign="top"><bean:write name="comment" property="sourceDisplay" filter="false" /></td>
                        <td width="100px" align="center" valign="top"><bean:write name="comment" property="viewDisplayImage" filter="false" /></td>
                      </tr>
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
                    <%= EnzymeLinksHelper.renderLinks(enzymeDTO.getAllLinks(), enzymeDTO.getEc()) %>
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
                      <% int iii=0; %>
                      <logic:iterate id="reference" name="enzymeDTO" property="references" >
                      <% iii++; %>
                      <tr>
                        <td colspan="2"><%= iii %>.

                          <!-- journal -->
                          <logic:equal value="J" name="reference" property="type">
                            <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="pubName" filter="false" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year">(<bean:write name="reference" property="year" filter="false" />)&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" />.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage">&nbsp;only.&nbsp;</logic:equal></logic:notEqual><logic:notEqual value="" name="reference" property="pubMedId">[PMID: <a target="_blank" href="http://srs.ebi.ac.uk/srsbin/cgi-bin/wgetz?-id+IntEnz+[medline-PMID:<bean:write name="reference" property="pubMedId" filter="false" />]+-e"><bean:write name="reference" property="pubMedId" filter="false" /></a>]</logic:notEqual>
                          </logic:equal>

                          <!-- book -->
                          <logic:equal value="B" name="reference" property="type">
                            <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="editor">In:&nbsp;<bean:write name="reference" property="editor" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="pubName"><i><bean:write name="reference" property="pubName" filter="false" /></i>,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="edition"> <bean:write name="reference" property="edition" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="volume"><bean:write name="reference" property="volume" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisher"><bean:write name="reference" property="publisher" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="publisherPlace"><bean:write name="reference" property="publisherPlace" filter="false" />,&nbsp;</logic:notEqual><logic:notEqual value="" name="reference" property="year"><logic:equal value="" name="reference" property="firstPage"><logic:equal value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" /></logic:equal><logic:notEqual value="" name="reference" property="lastPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:equal><logic:notEqual value="" name="reference" property="firstPage"><bean:write name="reference" property="year" filter="false" />,&nbsp;</logic:notEqual></logic:notEqual><logic:notEqual value="" name="reference" property="firstPage"><logic:notEqual value="" name="reference" property="lastPage"> pp.&nbsp;</logic:notEqual><logic:equal value="" name="reference" property="lastPage"> p.&nbsp;</logic:equal><bean:write name="reference" property="firstPage" filter="false" /><logic:notEqual value="" name="reference" property="lastPage">-<bean:write name="reference" property="lastPage" filter="false" /></logic:notEqual>.</logic:notEqual>
                          </logic:equal>

                          <!-- patent -->
                          <logic:equal value="P" name="reference" property="type">
                            <bean:write name="reference" property="authors" filter="false" />&nbsp;<bean:write name="reference" property="title" filter="false" />&nbsp;<logic:notEqual value="" name="reference" property="pubName"> <i><bean:write name="reference" property="pubName" filter="false" /></i></logic:notEqual><logic:notEqual value="" name="reference" property="year">,&nbsp;<bean:write name="reference" property="year" filter="false" /></logic:notEqual>
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

