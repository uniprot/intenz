<%@ page import="uk.ac.ebi.intenz.webapp.dtos.EnzymeSubSubclassDTO"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

    <!---------------------------------------------- Content --------------------------------------------------------->

<table class="content_table" width="100%" border="0" cellspacing="20" cellpadding="0">
  <tr>
    <td class="data_region_header_row">Classification</td>
  </tr>
  <tr>
    <td>
      <table width="100%" cellpadding="3">
        <tr>
          <td align="right"><b>Class:</b></td>
          <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="subSubclassDTO" paramProperty="classEc" paramId="ec">EC <bean:write name="subSubclassDTO" property="classEc"/>&nbsp;&nbsp;<bean:write name="subSubclassDTO" property="className" filter="false"/></html:link></td>
        </tr>
        <tr>
          <td align="right"><b>Subclass:</b></td>
          <td width="100%"><html:link styleClass="no_decoration" action="searchEc" paramName="subSubclassDTO" paramProperty="subclassEc" paramId="ec">EC <bean:write name="subSubclassDTO" property="subclassEc"/>&nbsp;&nbsp;<bean:write name="subSubclassDTO" property="subclassName" filter="false"/></html:link></td>
        </tr>
        <tr>
          <td align="right"><b>Sub-Subclass:</b></td>
          <td width="100%"><b>EC <bean:write name="subSubclassDTO" property="ec"/>&nbsp;&nbsp;<bean:write name="subSubclassDTO" property="name" filter="false"/></b></td>
        </tr>
        <logic:notEqual value="" name="subSubclassDTO" property="description">
        <tr>
          <td align="right">&nbsp;</td>
          <td width="100%"><bean:write name="subSubclassDTO" property="description" filter="false"/></td>
        </tr>
        </logic:notEqual>
      </table>
    </td>
  </tr>
  <bean:size id="listSize" name="subSubclassDTO" property="entries" />
  <logic:greaterThan name="listSize" value="0">
  <tr>
    <td class="data_region_header_row">Entries</td>
  </tr>
  <tr>
    <td>
      <table border="0" cellspacing="5">

        <logic:iterate name="subSubclassDTO" id="ghostEntry" property="entries">
          <tr>
            <td align="right">
                <a href="searchId.do?id=${ghostEntry.enzymeId}&view=INTENZ">${ghostEntry.ec}
            </td>
            <logic:equal value="deleted" name="ghostEntry" property="eventClass">
            <td align="left" class="deleted_transferred" align="left">deleted</td>
            </logic:equal>
            <logic:equal value="transferred" name="ghostEntry" property="eventClass">
            <td align="left" class="deleted_transferred" align="left">transferred.
                <bean:write name="ghostEntry" property="eventNote" filter="false"/>.
            </td>
            </logic:equal>
            <logic:notEqual value="deleted" name="ghostEntry" property="eventClass">
              <logic:notEqual value="transferred" name="ghostEntry" property="eventClass">
                <td align="left"><bean:write name="ghostEntry" property="name" filter="false"/></td>
              </logic:notEqual>
            </logic:notEqual>
            <td align="center" class="${ghostEntry.status}">${ghostEntry.status}</td>
          </tr>
        </logic:iterate>

      </table>
    </td>
  </tr>
  </logic:greaterThan>
</table>




