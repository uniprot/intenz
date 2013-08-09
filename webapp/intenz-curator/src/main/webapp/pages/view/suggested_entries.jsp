<%@ page import="java.util.HashMap,
                 java.util.Map,
                 uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

    <!---------------------------------------------- Content --------------------------------------------------------->

    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <bean:size id="suggestedEntriesSize" name="ghostEnzymeListDTO" property="ghostEnzymeList"/>
          <logic:equal value="0" name="suggestedEntriesSize">
            <table border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td width="100%" align="center"><i>No suggested entries stored in the database at the moment.</i></td>
              </tr>
            </table>
          </logic:equal>
          <logic:greaterThan name="suggestedEntriesSize" value="0">
            <table class="content_table" border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td>
                  <table width="100%" border="0" cellspacing="4" cellpadding="0">

                    <tr>
                      <td colspan="2" class="data_region_header_row">Suggested Entries</td>
                    </tr>
                    <tr>
                      <td colspan="3">&nbsp;</td>
                    </tr>

                    <logic:iterate id="suggestedEntry"
                        name="ghostEnzymeListDTO" property="ghostEnzymeList">
                      <tr>
                        <td colspan="2">
                            <a href="searchId.do?id=${suggestedEntry.enzymeId}&view=INTENZ">${suggestedEntry.ec}</a>
                            (<span class="${suggestedEntry.eventClass}">${suggestedEntry.eventClass}</span>)
                            <c:if test="${suggestedEntry.eventClass != 'deleted'
                                and suggestedEntry.eventClass != 'transferred'}">
                                ${suggestedEntry.name}
                            </c:if>
                        </td>
                      </tr>
                    </logic:iterate>

                  </table>
                </td>
              </tr>
            </table>
          </logic:greaterThan>
        </td>
      </tr>
    </table>




