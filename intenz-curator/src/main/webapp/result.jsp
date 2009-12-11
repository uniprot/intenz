<%@ page import="java.util.List"%>
<%@ page import="org.apache.struts.taglib.html.Constants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    Object results = session.getAttribute("result");
    Object gs = session.getAttribute("gs");
    Object maxScore = session.getAttribute("max_score");
    Object query = session.getAttribute("query");
    Object group = request.getAttribute("group");
    Object st = request.getAttribute("st");
    Object end = request.getAttribute("end");
    Object size = request.getAttribute("size");
    Object pst = request.getAttribute("pst");
    Object nst = request.getAttribute("nst");
%>
<html>
  <head>
      <title>Search results for '<bean:write name="query" filter="true"/>' - Integrated Enzyme Database (IntEnz)</title>
      <link rel="stylesheet" href="<html:rewrite forward='intenzStyle'/>" type="text/css">
      <link rel="stylesheet" href="<html:rewrite forward='tabbedMenuStyle'/>" type="text/css">
    
    <%-- Make it clear whenever we are using a development database: --%>
	<%@ include file="pages/testWatermark.jsp" %>

      <script type="text/javascript" src="js/intenz.js"></script>
      <script type="text/javascript" src="js/master.js"></script>
      <!-- ULTIMATE DROP DOWN MENU Version 4.2 by Brothercake -->
      <!-- http://www.udm4.com/ -->
      <script type="text/javascript" src="resources/udm-resources/udm-custom.js"></script>
      <script type="text/javascript" src="resources/udm-resources/udm-control.js"></script>
      <script type="text/javascript" src="resources/udm-resources/udm-style.js"></script>
  </head>

  <body>

  <ul id="udm" class="udm">
    <li><html:link forward="index">Home</html:link></li>
    <li><a class="nohref">View</a>
      <ul>
        <li><html:link action="searchProposed">Show Proposed Enzymes</html:link></li>
        <li><html:link action="searchSuggested">Show Suggested Enzymes</html:link></li>
    <%--      <li><a href="handler?cmd=SearchStatistic">Statistics</a></li>--%>
      </ul>
    </li>
    <li><a class="nohref">Tools</a>
    <ul>
      <li><a href="createEntryFWD.do?<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>">Create</a></li>
      <li><html:link action="addSubSubclassFWD" >Create Sub-Subclass</html:link></li>
    </ul>
    </li>
    <li><html:link forward="logout">Log out</html:link></li>
      <li style="float:right; padding-top:5px; padding-right:5px; margin-bottom:-10px">
          <html:form action="searchEc" focus="ec" method="get" >
              <b>EC:</b>&nbsp;<html:text property="ec" size="20" maxlength="100"/>&nbsp;
              <html:submit value="Search"/>
          </html:form>
      </li>
  </ul>

  <table width="100%" height="50" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="30">&nbsp;</td>
    </tr>
  </table>

<%-------------------------------- Content --------------------------------------------%>

<logic:notEmpty name="query">
Search results for <b>'<bean:write name="query" filter="true"/>'</b>:<br/>
</logic:notEmpty>

<br/>

<table cellpadding="10" cellspacing="0" width="60%" align="center">

    <logic:notEmpty name="query">

    <tr class="search">
      <logic:equal value="0" name="size">
        <th colspan="3">No results for <i>'<bean:write name="query" filter="true"/>'</i></th>
      </logic:equal>
      <logic:greaterThan value="0" name="size">
        <td align="left" width="20%">
            <logic:notEmpty name="pst">
                <html:form action="search">
                    <html:submit value="&lt;"/>
                    <input type="hidden" name="startIndex" value="<bean:write name="pst"/>"/>
                </html:form>
            </logic:notEmpty>
        </td>
        <th align="center">
            <bean:write name="st"/>-<bean:write name="end"/> of <bean:write name="size"/>
        </th>
        <td align="right" width="20%">
            <logic:notEmpty name="nst">
                <html:form action="search">
                    <html:submit value="&gt;"/>
                    <input type="hidden" name="startIndex" value="<bean:write name="nst"/>"/>
                </html:form>
            </logic:notEmpty>
        </td>
      </logic:greaterThan>
    </tr>

    </logic:notEmpty>

    <logic:iterate id="record" name="group" indexId="index">
    <tr class="tr<%= index.intValue()%2 %>">
        <td align="left" colspan="2">
            <a href="searchEc.do?ec=<%= ((List)record).get(1) %>" class="no_decoration">
                <div class="result_ec"><%= ((List)record).get(1) %></div>
                <%= ((List)record).get(2) %>
            </a>
        </td>
        <td>
            <% if (((Integer) ((List)record).get(4)).intValue() == 0){ %>
                <span class="inactive">INACTIVE</span>
            <% } %>
        </td>
    </tr>
    </logic:iterate>

    <html:form action="search">
      <tr class="search">
          <td align="right" colspan="2"><html:text disabled="false" property="q" size="50" maxlength="100"/></td>
          <td align="left"><html:submit disabled="false" value="Search"/></td>
      </tr>
    </html:form>

</table>



  <script type="text/javascript" src="resources/udm-resources/udm-dom.js"></script>

  </body>
</html>