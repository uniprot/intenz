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
          <bean:size id="preliminaryEcsSize" name="preliminaryEcs"/>
          <logic:equal value="0" name="preliminaryEcsSize">
            <table border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td width="100%" align="center">
                	<i>No preliminary EC numbers stored in the database at the moment.</i>
               	</td>
              </tr>
            </table>
          </logic:equal>
          <logic:greaterThan name="preliminaryEcsSize" value="0">
            <table class="content_table" border="0" cellspacing="20" cellpadding="0">
              <tr>
                <td>
                  <table width="100%" border="0" cellspacing="4" cellpadding="0">

                    <tr>
                      <td colspan="2" class="data_region_header_row">Preliminary EC numbers</td>
                    </tr>
                    <tr>
                      <td colspan="3">&nbsp;</td>
                    </tr>

					<c:forEach var="preliminaryEc" items="${preliminaryEcs}">
                      <tr>
                        <td colspan="2">
                          <table cellspacing="5">
                            <tr>
                              <td width="100px" align="right">
                              	<a href="searchId.do?id=${preliminaryEc.enzymeId}&view=INTENZ">${preliminaryEc.ec}</a>
                               	<td align="left" class="${preliminaryEc.eventClass eq 'deleted'
                              		or preliminaryEc.eventClass eq 'transferred'? 'deleted_transferred' : ''}">
                              		<c:out escapeXml="false"
                              			value="${preliminaryEc.eventClass eq 'deleted' or preliminaryEc.eventClass eq 'transferred'?
                              			preliminaryEc.eventClass : preliminaryEc.name}"/>
                            	</td>
                           </tr>
                          </table>
                        </td>
                      </tr>
					</c:forEach>

                  </table>
                </td>
              </tr>
            </table>
          </logic:greaterThan>
        </td>
      </tr>
    </table>
