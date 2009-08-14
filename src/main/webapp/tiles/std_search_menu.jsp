<%@ page import="org.apache.struts.taglib.html.Constants"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<ul id="udm" class="udm">
  <li><a href=".">Home</a></li>
  <li><a class="nohref">View</a>
      <ul>
          <li><html:link action="searchProposed">Show Proposed Enzymes</html:link></li>
          <li><html:link action="searchSuggested">Show Suggested Enzymes</html:link></li>
          <li><html:link action="cofactorList">List cofactors</html:link></li>
          <%--      <li><a href="handler?cmd=SearchStatistic">Statistics</a></li>--%>
      </ul>
  </li>
  <li><a class="nohref">Tools</a>
      <ul>
          <li><a href="createEntryFWD.do?<%=Constants.TOKEN_KEY%>=<%= request.getAttribute(Constants.TOKEN_KEY) %>">Create</a></li>
          <li><html:link action="addSubSubclassFWD" >Create Sub-Subclass</html:link></li>
          <li><a href="search.do">Last free text search</a></li>
      </ul>
  </li>
  <li><a class="nohref">Help</a>
  	<ul>
  		<li><a href="http://intranet.isb-sib.ch/display/cv/IntEnz+Curator+Tool+manual"
  			target="_blank">Curator manual</a></li>
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