<%@ page import="org.apache.struts.taglib.html.Constants"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<ul id="udm" class="udm">
  <li><a href=".">Home</a></li>
  <li><a class="nohref">View</a>
    <ul>
      <li><html:link action="searchProposed">Show Proposed Enzymes</html:link></li>
      <li><html:link action="searchSuggested">Show Suggested Enzymes</html:link></li>
      <li><html:link action="searchPreliminary">Show Preliminary ECs</html:link></li>
      <li><html:link action="cofactorList">List cofactors</html:link></li>
<%--      <li><a href="handler?cmd=SearchStatistic">Statistics</a></li>--%>
    </ul>
  </li>
  <li><a class="nohref">Tools</a>
  <ul>
    <li><a href="createEntryFWD.do?<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>">Create</a></li>
	<li><a href="createInactiveEntryFWD.do"
			title="Create an already deleted or transferred entry">Create
			inactive</a></li>
     <li><html:link action="addSubSubclassFWD" >Create Sub-Subclass</html:link></li>
  </ul>
  </li>
  <li><a class="nohref">Help</a>
  	<ul>
  		<li><a href="http://intranet.isb-sib.ch/display/cv/IntEnz+Curator+Tool+manual"
  			target="_blank">Curator manual</a></li>
  	</ul>
  </li>
  <li><html:link forward="logout">Log out</html:link></li>
</ul>