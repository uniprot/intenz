<%@ page import="java.util.HashMap,
                 java.util.Map,
                 uk.ac.ebi.intenz.webapp.dtos.GhostEnzymeDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

    <!---------------------------------------------- Content --------------------------------------------------------->

    <table height="100%" width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td valign="top" align="center">
          <bean:size id="proposedEntriesSize" name="ghostEnzymeListDTO" property="ghostEnzymeList"/>
          <logic:equal value="0" name="proposedEntriesSize">
            <table border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td width="100%" align="center"><i>No proposed entries stored in the database at the moment.</i></td>
              </tr>
            </table>
          </logic:equal>
          <logic:greaterThan name="proposedEntriesSize" value="0">
            <table class="content_table" border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td>
                  <table width="100%" border="0" cellspacing="4" cellpadding="0">

                    <tr>
                      <td colspan="2" class="data_region_header_row">Proposed Entries</td>
                    </tr>
                    <tr>
                      <td colspan="3">&nbsp;</td>
                    </tr>

                    <logic:iterate id="proposedEntry" name="ghostEnzymeListDTO" property="ghostEnzymeList">
                      <tr>
                        <td colspan="2">
                          <table cellspacing="5">
                            <tr>
                              <td width="100px" align="right">
                                  <a href="searchId.do?id=${proposedEntry.enzymeId}&view=INTENZ">${proposedEntry.ec}</a>
                                  (<span class="${proposedEntry.eventClass}">${proposedEntry.eventClass}</span>)
                                  <c:if test="${proposedEntry.eventClass != 'deleted'
                                    and proposedEntry.eventClass != 'transferred'}">
                                          ${proposedEntry.name}
                                  </c:if>
                              </td>
                            </tr>
                          </table>
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




