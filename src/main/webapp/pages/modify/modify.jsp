<%@ page contentType="text/html" pageEncoding="UTF-8"
    import="org.apache.struts.taglib.html.Constants,
            uk.ac.ebi.intenz.webapp.dtos.CofactorDTO,
            uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,
            uk.ac.ebi.intenz.webapp.dtos.EnzymeLinkDTO,
            uk.ac.ebi.intenz.webapp.helper.EnzymeLinksHelper"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript" src="js/form.js"></script>
<script type="text/javascript" src="resources/jt/jt_.js"></script>
<script type="text/javascript" src="resources/jt/dom-drag.js"></script>
<script type="text/javascript" src="resources/jt/jt_DialogBox.js"></script>
<script type="text/javascript" src="resources/jt/jt_AppDialogs.js"></script>
<script type="text/javascript" src="resources/jt/jt_AppDialogs.js"></script>
<script type="text/javascript" src="js/rheaSelector.js"></script>

<style>
<!--
@import "resources/jt/jt_DialogBox.css";
@import "resources/jt/veils.css";
@import "css/rhea-view.css";
.hidden {
	display: none;
}
-->
</style>

    <!-------------------------- Content ----------------------------------->
    <table style="height: 100%; width: 100%; border: none;
    	margin: 0%; padding: 0%">
      <tr>
        <td valign="top" align="center">
          <table class="content_table"
          		style="border: none; margin: 20px; padding: 0%">
            <tr>
              <logic:messagesPresent>
                <td>
                  <html:messages id="error" header="errors.header" footer="errors.footer">
                  	<li class="error"><bean:write name="error" filter="false"/></li>
                  </html:messages>
                </td>
              </logic:messagesPresent>
            </tr>
            <tr>
              <td>
                <html:form action="${modifAction}" method="post">
                <input type="hidden" name="<%= Constants.TOKEN_KEY %>" value="<%=request.getAttribute(Constants.TOKEN_KEY)%>"/>                                                                                                        
                <input type="hidden" name="buttonCmd">
                <input type="hidden" name="listType">
                <input type="hidden" name="index">
                <html:hidden property="id" name="enzymeDTO"/>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td class="data_region_header_row" colspan="5">
                    	<%=  modification %><% if (modification.equals("Transfer")){ %>red<% } %> Entry
<% if (modification.equals("Amend")
		|| modification.equals("Transfer")
		|| (modification.equals("Create") && update.equals("Update"))){ %>
                     	EC <bean:write name="enzymeDTO" property="ec" filter="false" scope="session" />
                    	/ Enzyme ID: <bean:write name="enzymeDTO" property="id" filter="false" scope="session" />
<% } %>
                    <logic:equal value="Update" name="update">
                    	<i>(update)</i>
                    </logic:equal>
                    </td>
                  </tr>

                  <tr>
                    <td colspan="5">&nbsp;</td>
                  </tr>

<% if (modification.equals("Create") && !update.equals("Update")){ %>
                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">

                        <%-- EC --%>
                        <logic:messagesPresent>
                          <tr>
                            <td colspan="6" class="error">
                              <html:messages id="error" property="ec">
                              	<bean:write name="error" filter="false"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>
                        <tr>
                          <%-- Label --%>
                          <td nowrap="nowrap" class="data_region_name_cell">EC:</td>

                          <%-- EC number --%>
                          <td colspan="5" nowrap="nowrap" width="100%">
                          	<html:text name="enzymeDTO" styleId="ec" property="ec"
                          		size="15" onblur="checkPreliminary(event)"/>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
<% } else if (modification.equals("Amend") || modification.equals("Transfer")){ %>
		            <logic:messagesPresent>
		  	          <tr>
		                <td colspan="5">
		                  <html:messages id="error" property="names" header="errors.header" footer="errors.footer">
		                  	<li class="error"><bean:write name="error" filter="true"/></li>
		                  </html:messages>
		                </td>
			          </tr>
		            </logic:messagesPresent>
<% } %>

                  <tr>
                    <td colspan="5">&nbsp;</td>
                  </tr>
                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">
                        <tr>
                          <td colspan="2" width="100%" class="data_region_header_row">Names</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">Qualifier</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                          <td width="100px" align="center">&nbsp;</td>
                        </tr>
                        <tr>
                          <td colspan="5">&nbsp;</td>
                        </tr>


                        <!-- COMMON NAMES -->

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="6" class="error">
                              <html:messages id="error" property="commonName" header="errors.header" footer="errors.footer">
                              	<li class="error"><bean:write name="error" filter="true"/></li>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="commonNamesSize" name="enzymeDTO" property="commonNames" scope="session" />
                        <logic:equal name="commonNamesSize" value="0">
                          <tr>
                            <%-- Label --%>
                            <td width="130px" nowrap="nowrap" class="data_region_name_cell">Common name(s):</td>
                          </tr>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="6" width="100%">
                              <input title="Add common name"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'commonNames', '<%= enzymeDTO.getCommonNames().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:equal>
                        <logic:greaterThan name="commonNamesSize" value="0">
                          <logic:iterate id="commonName" name="enzymeDTO" property="commonNames" indexId="index" >
                            <tr>
                              <%-- Label --%>
                              <logic:equal name="index" value="0">
                                <td width="130px" nowrap="nowrap" class="data_region_name_cell">Common name(s):</td>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px">&nbsp;</td>
                              </logic:notEqual>

                              <%-- Common name data --%>
                              <td colspan="2" nowrap="nowrap" width="100%">
                              	<html:text name="commonName" property="xmlName" size="70" maxlength="2000" indexed="true" />
                              	<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'commonName[<%=index%>].xmlName');"
									title="Open xchars window" class="xchars"/>
                              </td>
                              <html:hidden name="commonName" property="source" indexed="true" />
                              <td width="100px" align="center"><bean:write name="commonName" property="sourceDisplay" filter="false"/></td>
                              <td width="100px" align="center">
                                <html:select name="commonName" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>

                              <%-- 'Minus' Button --%>
                              <td>
                                <input title="Remove common name"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'commonNames', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:iterate>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="5" width="100%">
                            <td>
                              <input title="Add common name"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'commonNames', '<%= enzymeDTO.getCommonNames().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:greaterThan>


                        <!-- SYSTEMATIC NAME -->

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="6" class="error">
                              <html:messages id="error" property="systematicName" header="errors.header" footer="errors.footer">
                              	<li class="error"><bean:write name="error" filter="true"/></li>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <logic:notEmpty name="enzymeDTO" property="systematicName">
                          <tr>
                            <%-- Label --%>
                            <td width="130px" nowrap="nowrap" class="data_region_name_cell">Systematic name:</td>

                            <%-- Systematic name data --%>
                            <td colspan="2" nowrap="nowrap" width="100%">
                            	<html:text name="enzymeDTO" property="systematicName.xmlName" size="70" maxlength="2000"/>
								<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'systematicName.xmlName');"
									title="Open xchars window" class="xchars"/>
                           	</td>
                            <html:hidden name="enzymeDTO" property="systematicName.source" />
                            <td width="100px" align="center"><bean:write name="enzymeDTO" property="systematicName.sourceDisplay" filter="false"/></td>
                            <td width="100px" align="center">
                              <html:select name="enzymeDTO" property="systematicName.view" size="1">
                                <html:option value="IUBMB">NC-IUBMB</html:option>
                                <html:option value="SIB">ENZYME</html:option>
                                <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                <html:option value="INTENZ">All views</html:option>
                              </html:select>
                            </td>
                          </tr>
                        </logic:notEmpty>


                        <!-- SYNONYMS -->

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="otherNames" header="errors.header" footer="errors.footer">
                              	<li class="error"><bean:write name="error" filter="true"/></li>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="synonymsSize" name="enzymeDTO" property="synonyms" scope="session" />
                        <logic:equal name="synonymsSize" value="0">
                          <tr>
                            <%-- Label --%>
                            <td width="130px" nowrap="nowrap" class="data_region_name_cell">Other name(s):</td>
                          </tr>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="6" width="100%">
                              <input title="Add synonym"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'synonyms', '<%= enzymeDTO.getSynonyms().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:equal>
                        <logic:greaterThan name="synonymsSize" value="0">
                          <logic:iterate id="synonym" name="enzymeDTO" property="synonyms" indexId="index" >
                            <tr>
                              <%-- Label --%>
                              <logic:equal name="index" value="0">
                                <td width="130px" nowrap="nowrap" class="data_region_name_cell">Other name(s):</td>
                              </logic:equal>
                              <logic:notEqual name="index" value="0">
                                <td width="130px">&nbsp;</td>
                              </logic:notEqual>
                              <td>
                              	<html:text name="synonym" property="xmlName" size="70" maxlength="2000" indexed="true" />
								<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'synonym[<%=index%>].xmlName');"
									title="Open xchars window" class="xchars"/>
                              	</td>
                              <%-- Synonym data --%>
                                    <td>
                                      <html:select name="synonym" property="qualifier" indexed="true" size="1">
                                        <html:option value="NON">(none)</html:option>
                                        <html:option value="MIS">misleading</html:option>
                                        <html:option value="AMB">ambiguous</html:option>
                                        <html:option value="OBS">obsolete</html:option>
                                        <html:option value="MPR">misprint</html:option>
                                        <html:option value="INC">incorrect</html:option>
                                      </html:select>
                                    </td>
                              <html:hidden name="synonym" property="source" indexed="true"/>
                              <td width="100px" align="center"><bean:write name="synonym" property="sourceDisplay" filter="false"/></td>
                              <td width="100px" align="center">
                                <html:select name="synonym" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>

                              <%-- 'Minus' button --%>
                              <td>
                                <input title="Remove synonym"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'synonyms', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:iterate>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="5" width="100%">
                            <td>
                              <input title="Add synonym"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'synonyms', '<%= enzymeDTO.getSynonyms().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:greaterThan>

                      </table>
                    </td>
                  </tr>

                  <tr>
                    <td height="40px" colspan="5">&nbsp;</td>
                  </tr>


                  <!-- REACTIONS -->

                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0" id="reactionsTable">
                        <tr>
                          <td width="100%" class="data_region_header_row">Reaction(s)</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">IUBMB</td>
                          <td width="100px" align="center">&nbsp;</td>
                        </tr>

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="reaction" header="errors.header" footer="errors.footer">
                                <li class="error"><bean:write name="error" filter="true"/></li>
							  </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="reactionsSize" name="enzymeDTO" property="reactionDtos" scope="session" />
                        <logic:greaterThan name="reactionsSize" value="0">
                          <logic:iterate id="reactionDto" name="enzymeDTO" property="reactionDtos" indexId="index" >
                            <%-- Reaction data--%>
                            <%@include file="edit_reaction.jsp"%>
                          </logic:iterate>
                        </logic:greaterThan>
                          <tr>
                            <%-- 'Plus' buttons --%>
                            <td colspan="4" width="100%">
                            <td valign="top">
                              <input title="Add free text reaction"
                              		 id="addFtReaction"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'reactions', '<%= enzymeDTO.getReactionDtos().size() %>', '<%=formButtonAction%>')"/>
                              <button title="Add Rhea reaction"
                                     type="button"
                                     onclick="openRheactionSelector(event, '<%=formButtonAction%>')"/>
                                     Add Rhea-ction
                              </button>
								<input type="hidden" name="rheaId" id="rheaId"/>
                            </td>
                          </tr>
                      </table>
                    </td>
                  </tr>

                  <tr>
                    <td height="40px" colspan="5">&nbsp;</td>
                  </tr>


                  <!-- COFACTORS -->

                  <bean:size id="cofactorsSize" name="enzymeDTO" property="cofactors" scope="session" />
                    <tr>
                      <td>
                        <table width="100%" border="0" cellspacing="10" cellpadding="0">
                          <tr>
                            <td colspan="2" width="100%" class="data_region_header_row">Cofactor(s)</td>
                            <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                            <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                            <td width="100px" align="center">&nbsp;</td>
                          </tr>

                          <logic:messagesPresent>
                            <tr>
                              <td colspan="5" class="error">
                                <html:messages id="error" property="cofactor">
                                	<bean:write name="error" filter="false"/>
                                </html:messages>
                              </td>
                            </tr>
                          </logic:messagesPresent>

                          <logic:greaterThan name="cofactorsSize" value="0">
                          <logic:iterate id="cofactor" name="enzymeDTO" property="cofactors" type="CofactorDTO" indexId="index" >
                            <tr>
                              <%-- Cofactor data --%>
                              <td colspan="2" valign="top" nowrap="nowrap" width="100%">
                              	<html:hidden name="cofactor" property="xmlCofactorValue" indexed="true"/>
                                <html:hidden name="cofactor" property="compoundId" indexed="true"/>
                                 <%@ include file="edit_cofactors.jspf" %>
                              </td>

                              <td width="100px" valign="top" align="center">
                                <html:hidden name="cofactor" property="source" indexed="true"/>
                                <bean:write name="cofactor" property="sourceDisplay" filter="false"/>
                              </td>
                              <td width="100px" valign="top" align="center">
                                <html:select name="cofactor" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>

                              <%-- 'Minus' button --%>
                              <td valign="top">
                                <input title="Remove cofactor"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'cofactors', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:iterate>
                          </logic:greaterThan>

                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="4" width="100%">
                            <td valign="top">
                              <button title="Add cofactor"
                                     type="button"
                                     onclick="openCofactorSelector(event, '<%=formButtonAction%>')"/>
                                     +
                              </button>
								<input type="hidden" name="cofactorId" id="cofactorId"/>
								<input type="hidden" name="complexCofactorDtoIndex" id="complexCofactorDtoIndex"/>
								<input type="hidden" name="complexCofactorDtoInternalIndex" id="complexCofactorDtoInternalIndex"/>
								<input type="hidden" name="complexCofactorOperator" id="complexCofactorOperator"/>
								<input type="hidden" name="complexCofactorBrackets" id="complexCofactorBrackets"/>
                            </td>
                          </tr>


                        </table>
                      </td>
                    </tr>

                    <tr>
                      <td height="40px" colspan="5">&nbsp;</td>
                    </tr>

                  <!-- COMMENTS -->

                  <bean:size id="commentsSize" name="enzymeDTO" property="comments" scope="session" />
                    <tr>
                      <td>
                        <table width="100%" border="0" cellspacing="10" cellpadding="0">
                          <tr>
                            <td colspan="2" width="100%" class="data_region_header_row">Comment(s)</td>
                            <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                            <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                            <td width="100px" align="center">&nbsp;</td>
                          </tr>

                          <logic:messagesPresent>
                            <tr>
                              <td colspan="5" class="error">
                                <html:messages id="error" property="comments" header="errors.header" footer="errors.footer">
                                	<li class="error"><bean:write name="error" filter="true"/></li>
                                </html:messages>
                              </td>
                            </tr>
                          </logic:messagesPresent>

                          <logic:equal name="commentsSize" value="0">
                            <tr>
                              <%-- 'Plus' button --%>
                              <td colspan="5" width="100%">
                                <input title="Add comment"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'comments', '<%= enzymeDTO.getComments().size() %>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:equal>
                          <logic:greaterThan name="commentsSize" value="0">
                          <logic:iterate id="comment" name="enzymeDTO" property="comments" indexId="index" >
                            <tr>
                              <%-- Comment data --%>
                              <td colspan="2" valign="top" nowrap="nowrap" width="100%">
                              	<html:textarea name="comment" property="xmlComment" cols="70" rows="5" indexed="true" />
								<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'comment[<%=index%>].xmlComment');"
									title="Open xchars window" class="xchars"/>
                              </td>
                              <html:hidden name="comment" property="source" indexed="true"/>
                              <td width="100px" valign="top" align="center"><bean:write name="comment" property="sourceDisplay" filter="false"/></td>
                              <td width="100px" valign="top" align="center">
                                <html:select name="comment" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>

                              <%-- 'Minus' button --%>
                              <td valign="top">
                                <input title="Remove comment"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'comments', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:iterate>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="4" width="100%">
                            <td valign="top">
                              <input title="Add comment"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'comments', '<%= enzymeDTO.getComments().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                          </logic:greaterThan>
                        </table>
                      </td>
                    </tr>

                    <tr>
                      <td height="40px" colspan="5">&nbsp;</td>
                    </tr>

                  <!-- LINKS TO OTHER DATABASES -->

                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">
                        <tr>
                          <td colspan="2" width="100%" class="data_region_header_row">Links to other databases:</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                          <td width="100px" align="center">&nbsp;</td>
                        </tr>
                        <tr>
                          <td colspan="5">&nbsp;</td>
                        </tr>

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="links">
                              	<bean:write name="error" filter="false"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="linksSize" name="enzymeDTO" property="links" scope="session" />
                        <logic:equal name="linksSize" value="0">
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="5" width="100%">
                              <input title="Add link"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'links', '<%= enzymeDTO.getLinks().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:equal>
                        <logic:greaterThan name="linksSize" value="0">
                          <logic:iterate id="link" name="enzymeDTO" property="links" indexId="index" >
                            <logic:notEqual name="link" property="databaseCode" value="SWISSPROT">
                            <logic:notEqual name="link" property="databaseCode" value="GO">
                            <tr>
                              <td valign="top" colspan="2" nowrap="nowrap" width="100%">
                                <html:hidden name="link" property="source" indexed="true"/>
                                <table width="80%" style="float: left" >
                                  <%= EnzymeLinksHelper.renderEditableLink((EnzymeLinkDTO)link, index.intValue()) %>
                                </table><div style="height: 20px">&nbsp;</div>
                                <html:hidden name="link" property="dataComment" indexed="true" />
                                <% String commentedObjectName = "link[" + index.toString() + "]"; %>
                                <a href="javascript:editDataComment('<%= commentedObjectName %>')" title="Comment link">
                                    <img border="0" id="<%= commentedObjectName %>.dataComment.edit" />
                                    <script language="JavaScript">
                                    updateDataCommentEditLink('<%= commentedObjectName %>');
                                    </script>
                                </a>
                              </td>
                              <td valign="bottom" width="100px" align="center"><bean:write name="link" property="sourceDisplay" filter="false"/></td>
                              <td valign="bottom" width="100px" align="center">
                                <html:select name="link" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>
                              <%-- 'Minus' button --%>
                              <td valign="bottom">
                                <input title="Remove link"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'links', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                            </logic:notEqual>
                            </logic:notEqual>
                          </logic:iterate>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="4" width="100%">
                            <td valign="top">
                              <input title="Add link"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'links', '<%= enzymeDTO.getLinks().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:greaterThan>

						<%-- GO links --%>
						<logic:notEmpty name="enzymeDTO" property="goLinks">
						<tr>
							<td colspan="2">
								<table>
									<tr>
										<td valign="top" width="130"><b>GO:</b></td>
										<td>
										<logic:iterate id="link" name="enzymeDTO" property="goLinks">
											<a href='<bean:write name="link" property="url"/>'>
											<bean:write name="link" property="accession"/>
											</a><br/>
										</logic:iterate>
										</td>
									</tr>
								</table>
							</td>
							<td align="center" width="100">n/a</td>
							<td align="center" width="100">NC-IUBMB &amp; IntEnz</td>
						</tr>
						</logic:notEmpty>

                        <%-- UniProt links --%>
                        <logic:messagesPresent property="uniprotXrefsUpdate">
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="uniprotXrefsUpdate">
                              	<bean:write name="error" filter="false"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="uniProtLinksSize" name="enzymeDTO" property="uniProtLinks" scope="session" />
                        <%-- Show UniProt links and update button. --%>
                        <%= EnzymeLinksHelper.renderUniprotLinks(enzymeDTO.getUniProtLinks(), false) %>
                        <tr>
                          <td colspan="4" width="100%"/>
                        </tr>
                      </table>
                    </td>
                  </tr>


                  <tr>
                    <td height="40px" colspan="5">&nbsp;</td>
                  </tr>


                  <!-- REFERENCES -->

                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">
                        <tr>
                          <td colspan="2" width="100%" class="data_region_header_row">Reference(s)</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">Source</td>
                          <td width="100px" align="center" class="data_region_header_row_small_blue">View</td>
                          <td width="100px" align="center">&nbsp;</td>
                        </tr>

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="reference" header="errors.header" footer="errors.footer">
                              	<li class="error"><bean:write name="error" filter="true"/></li>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <bean:size id="referencesSize" name="enzymeDTO" property="references" scope="session" />
                        <logic:equal name="referencesSize" value="0">
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="5" width="100%">
                              <input title="Add reference"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'references', '<%= enzymeDTO.getReferences().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:equal>
                        <logic:greaterThan name="referencesSize" value="0">
                          <logic:iterate id="reference" name="enzymeDTO" property="references" indexId="index" >
                            <tr>
                              <%-- Reference data --%>
                              <td colspan="2" valign="top" nowrap="nowrap" width="100%">
                                <table width="100%">
                                  <%-- No. --%>
                                  <tr>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">No.:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <td><%= index.intValue() + 1 %></td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <%-- type --%>
                                  <tr>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Type:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <td>
                                            <html:radio name="reference" property="type" value="J" indexed="true" onclick="setPubFields('J', '${index}')" />&nbsp;Journal&nbsp;|
                                            <html:radio name="reference" property="type" value="B" indexed="true" onclick="setPubFields('B', '${index}')" />&nbsp;Book&nbsp;|
                                            <html:radio name="reference" property="type" value="P" indexed="true" onclick="setPubFields('P', '${index}')" />&nbsp;Patent
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <%-- pub ID, pubmed ID --%>
                                  <tr>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Pub ID:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <td><bean:write name="reference" property="pubId"/></td>
                                          <td width="100%">&nbsp;</td>
                                          <logic:equal name="reference" property="type" value="J">
                                            <td id="reference[<bean:write name="index"/>].pubMedIdLabel" nowrap="nowrap" align="right" class="data_region_name_cell">PubMed ID:</td>
                                            <td><html:text name="reference" property="pubMedId" size="20" maxlength="8" indexed="true"/></td>
                                            <td>&nbsp;</td>
                                            <td><input title="Fetch publication data from PubMed" name="pubMedFetch[<bean:write name="index"/>]" type="button" value="Fetch PubMed Data" onclick="formButtonAction('pubMedFetch', 'pubMed', <bean:write name="index"/>, '<%=formButtonAction%>')"></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="B">
                                            <td  id="reference[<bean:write name="index"/>].pubMedIdLabel" nowrap="nowrap" align="right" class="data_region_name_cell">PubMed ID:</td>
                                            <td><html:text disabled="true" name="reference" property="pubMedId" size="20" maxlength="8" indexed="true"/></td>
                                            <td>&nbsp;</td>
                                            <td><input title="Fetch publication data from PubMed" name="pubMedFetch[<bean:write name="index"/>]" type="button" value="Fetch PubMed Data" onclick="formButtonAction('pubMedFetch', 'pubMed', <bean:write name="index"/>, '<%=formButtonAction%>')"></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="P">
                                            <td  id="reference[<bean:write name="index"/>].pubMedIdLabel" nowrap="nowrap" align="right" class="data_region_name_cell">Patent No.:</td>
                                            <td><html:text name="reference" property="pubMedId" size="20" maxlength="8" indexed="true"/></td>
                                          </logic:equal>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <%-- authors --%>
                                  <tr>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell" valign="top">Author(s):</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <td width="100%">
                                          	<html:textarea name="reference" property="xmlAuthors" cols="70" rows="2" indexed="true"/>
											<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlAuthors');"
												title="Open xchars window" class="xchars"/>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <%-- title --%>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell" valign="top">Title:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <td>
                                          	<html:textarea name="reference" property="xmlTitle" cols="70" rows="5" indexed="true"/>
											<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlTitle');"
												title="Open xchars window" class="xchars"/>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <%-- journal/book name --%>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Pub. name:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <logic:equal name="reference" property="type" value="J">
                                            <td width="100%">
                                            	<html:text name="reference" property="xmlPubName" size="70" maxlength="1000" indexed="true"/>
												<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlPubName');"
													title="Open xchars window" class="xchars"/>
                                           	</td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="B">
                                            <td width="100%">
                                            	<html:text name="reference" property="xmlPubName" size="70" maxlength="1000" indexed="true"/>
												<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlPubName');"
													title="Open xchars window" class="xchars"/>
                                           	</td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="P">
                                            <td width="100%"><html:text disabled="true" name="reference" property="xmlPubName" size="70" maxlength="1000" indexed="true"/></td>
                                          </logic:equal>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <%-- volume, year, first and last page --%>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Volume:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <logic:equal name="reference" property="type" value="J">
                                            <td><html:text name="reference" property="volume" size="15" maxlength="15" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Year:</td>
                                            <td><html:text name="reference" property="year" size="7" maxlength="4" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">First page:</td>
                                            <td><html:text name="reference" property="firstPage" size="7" maxlength="12" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Last page:</td>
                                            <td width="100%"><html:text name="reference" property="lastPage" size="7" maxlength="12" indexed="true"/></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="B">
                                            <td><html:text name="reference" property="volume" size="15" maxlength="15" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Year:</td>
                                            <td><html:text name="reference" property="year" size="7" maxlength="4" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">First page:</td>
                                            <td><html:text name="reference" property="firstPage" size="7" maxlength="12" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Last page:</td>
                                            <td width="100%"><html:text name="reference" property="lastPage" size="7" maxlength="12" indexed="true"/></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="P">
                                            <td><html:text disabled="true" name="reference" property="volume" size="15" maxlength="15" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Year:</td>
                                            <td><html:text name="reference" property="year" size="7" maxlength="4" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">First page:</td>
                                            <td><html:text name="reference" disabled="true" property="firstPage" size="7" maxlength="12" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Last page:</td>
                                            <td width="100%"><html:text disabled="true" name="reference" property="lastPage" size="7" maxlength="12" indexed="true"/></td>
                                          </logic:equal>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <%-- edition and editor--%>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Edition:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <logic:equal name="reference" property="type" value="J">
                                            <td><html:text disabled="true" name="reference" property="edition" size="7" maxlength="3" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Editor:</td>
                                            <td width="100%">
                                            	<html:text disabled="true" name="reference" property="xmlEditor" size="60" maxlength="2000" indexed="true"/>
												<input disabled="disabled" type="button" value="&lt;x&gt;" name="reference[<%=index%>].xmlEditor.xchars"
													onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlEditor');"
													title="Open xchars window" class="xchars"/>
                                           	</td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="B">
                                            <td><html:text name="reference" property="edition" size="7" maxlength="3" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Editor:</td>
                                            <td width="100%">
                                            	<html:text name="reference" property="xmlEditor" size="60" maxlength="2000" indexed="true"/>
												<input type="button" value="&lt;x&gt;" name="reference[<%=index%>].xmlEditor.xchars"
													onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlEditor');"
													title="Open xchars window" class="xchars"/>
                                           	</td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="P">
                                            <td><html:text disabled="true" name="reference" property="edition" size="7" maxlength="3" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Editor:</td>
                                            <td width="100%">
                                            	<html:text disabled="true" name="reference" property="xmlEditor" size="60" maxlength="2000" indexed="true"/>
												<input disabled="disabled" type="button" value="&lt;x&gt;" name="reference[<%=index%>].xmlEditor.xchars"
													onclick="openSpecialCharacterWindow('enzymeDTO', 'reference[<%=index%>].xmlEditor');"
													title="Open xchars window" class="xchars"/>
                                            </td>
                                          </logic:equal>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr>
                                    <%-- publisher and place of publisher --%>
                                    <td align="right" nowrap="nowrap" class="data_region_name_cell">Publisher:</td>
                                    <td>
                                      <table width="100%">
                                        <tr>
                                          <logic:equal name="reference" property="type" value="J">
                                            <td><html:text disabled="true" name="reference" property="xmlPublisher" size="35" maxlength="50" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Pub. place:</td>
                                            <td width="100%"><html:text disabled="true" name="reference" property="publisherPlace" size="35" maxlength="50" indexed="true"/></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="B">
                                            <td><html:text name="reference" property="xmlPublisher" size="35" maxlength="50" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Pub. place:</td>
                                            <td width="100%"><html:text name="reference" property="publisherPlace" size="35" maxlength="50" indexed="true"/></td>
                                          </logic:equal>
                                          <logic:equal name="reference" property="type" value="P">
                                            <td><html:text disabled="true" name="reference" property="xmlPublisher" size="35" maxlength="50" indexed="true"/></td>
                                            <td nowrap="nowrap" class="data_region_name_cell" align="right">Pub. place:</td>
                                            <td width="100%"><html:text disabled="true" name="reference" property="publisherPlace" size="35" maxlength="50" indexed="true"/></td>
                                          </logic:equal>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                </table>
                              </td>

                              <html:hidden name="reference" property="source" indexed="true"/>
                              <td width="100px" valign="top" align="center"><bean:write name="reference" property="sourceDisplay" filter="false"/></td>
                              <td width="100px" valign="top" align="center">
                                <html:select name="reference" property="view" indexed="true" size="1">
                                  <html:option value="IUBMB">NC-IUBMB</html:option>
                                  <html:option value="SIB">ENZYME</html:option>
                                  <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
                                  <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
                                  <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
                                  <html:option value="INTENZ">All views</html:option>
                                </html:select>
                              </td>

                              <%-- 'Minus' button --%>
                              <td valign="top">
                                <input title="Remove reference"
                                       type="button"
                                       value="-"
                                       onclick="formButtonAction('delete', 'references', '<bean:write name="index"/>', '<%=formButtonAction%>')"/>
                              </td>
                            </tr>
                          </logic:iterate>
                          <tr>
                            <%-- 'Plus' button --%>
                            <td colspan="4" width="100%"></td>
                            <td valign="top">
                              <input title="Add reference"
                                     type="button"
                                     value="+"
                                     onclick="formButtonAction('plus', 'references', '<%= enzymeDTO.getReferences().size() %>', '<%=formButtonAction%>')"/>
                            </td>
                          </tr>
                        </logic:greaterThan>
                      </table>
                    </td>
                  </tr>

                  <tr>
                    <td height="40px" colspan="5">&nbsp;</td>
                  </tr>


                  <!-- HOUSEKEEPING DATA -->

                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">
                        <tr>
                          <td colspan="4" width="100%" class="data_region_header_row">Housekeeping data:</td>
                          <td width="100px" align="center">&nbsp;</td>
                        </tr>

                        <logic:messagesPresent>
                          <tr>
                            <td colspan="5" class="error">
                              <html:messages id="error" property="historyLine">
                              	<bean:write name="error" filter="false"/>
                              </html:messages>
                            </td>
                          </tr>
                        </logic:messagesPresent>

                        <tr>
                          <%-- Label --%>
                          <td nowrap="nowrap" class="data_region_name_cell">History line:</td>

                          <%-- History line data --%>
                          <td nowrap="nowrap" width="100%">
                          	<html:text name="enzymeDTO" property="historyLine" size="70" maxlength="400" />
                          </td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
<% if (modification.equals("Transfer") && update.equals("Update")){ %>
                        <tr>
                          <%-- Label --%>
                          <td nowrap="nowrap" class="data_region_name_cell" valign="top">History note:</td>

                          <%-- History note --%>
                          <td nowrap="nowrap" width="100%">
                          	<html:textarea name="enzymeDTO" property="latestHistoryEventNote" rows="3" cols="70" />
							<input type="button" value="&lt;x&gt;" onclick="openSpecialCharacterWindow('enzymeDTO', 'latestHistoryEventNote');"
								title="Open xchars window" class="xchars"/>
                          </td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
<% } %>
                        <tr>
                          <%-- Label --%>
                          <td nowrap="nowrap" class="data_region_name_cell">Status:</td>

                          <%-- Status info --%>
                          <td width="100%">
                            <html:select name="enzymeDTO" property="statusCode" size="1"
                            	styleId="statusCode" onchange="checkPreliminary(event)">
                             	<html:option value="PM"
                             		disabled="${modification ne 'Create' and enzymeDTO.statusCode ne 'PM'}">preliminary</html:option>
                               	<html:option value="SU"
                               		disabled="${update eq 'Update' and enzymeDTO.statusCode eq 'PM'}">suggested</html:option>
                                <html:option value="PR"
                                	disabled="${(update eq 'Update' and enzymeDTO.statusCode eq 'PM') or update ne 'Update'}">proposed</html:option>
                                <html:option value="OK"
                                	disabled="${(update eq 'Update' and enzymeDTO.statusCode eq 'PM')
                                	or (update eq 'Update' and enzymeDTO.statusCode eq 'SU')
                                	or (update ne 'Update' and modification ne 'Amend')}">approved</html:option>
                            </html:select>
                          </td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>

                      </table>
                    </td>
                  </tr>

                  <!-- PREVIEW / SUBMIT BUTTONS -->

                  <tr>
                    <td>
                      <table width="100%" border="0" cellspacing="10" cellpadding="0">
                        <tr>
                          <td colspan="5" width="100%" class="data_region_header_row">&nbsp;</td>
                        </tr>

                        <tr>
                          <td align="right">
                            <input title="Preview entry" name="preview" type="submit" value="Preview Entry"
                            	class="previewButton"
                            	onclick="javascript:submitt('<%=previewAction%>')"/>
                          </td>
                        </tr>
                      </table>
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
