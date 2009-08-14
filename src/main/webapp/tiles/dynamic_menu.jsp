<%@ page import="org.apache.struts.Globals,
                 org.apache.struts.taglib.html.Constants,
                 uk.ac.ebi.intenz.webapp.dtos.EnzymeDTO,uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant,uk.ac.ebi.intenz.domain.constants.EventConstant"%>
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

    <%
      EnzymeDTO enzymeDTO = (EnzymeDTO) request.getSession().getAttribute("enzymeDTO");
      String urlToLink = new StringBuffer(Constants.TOKEN_KEY).append("=").append(request.getAttribute(Constants.TOKEN_KEY)).append("&id=").append(enzymeDTO.getId()).append("&statusCode=").append(enzymeDTO.getStatusCode()).append("&ec=").append(enzymeDTO.getEc()).toString();

      if(enzymeDTO.getStatusCode().equals(EnzymeStatusConstant.APPROVED.getCode())) {
        if(enzymeDTO.isActive()) {
        %>
        <li><a href="amendEntryFWD.do?<%=urlToLink%>">Amend</a></li>
        <li><a href="transferEntryFWD.do?<%=urlToLink%>">Transfer</a></li>
        <li><a href="deleteEntryFWD.do?<%=urlToLink%>">Delete</a></li>
        <%
        }
    %>
      <li><a href="createEntryFWD.do?<%=urlToLink%>">Create</a></li>
    <%
      } else {
        if(enzymeDTO.getLatestHistoryEventClass().equals(EventConstant.MODIFICATION.toString())) {
          %>
            <li><a href="amendEntryUpdateFWD.do?<%=urlToLink%>">Amend (<i>update</i>)</a></li>
          <%
        }
        if(enzymeDTO.getLatestHistoryEventClass().equals(EventConstant.TRANSFER.toString())) {
          %>
            <li><a href="transferEntryUpdateFWD.do?<%=urlToLink%>">Transfer (<i>update</i>)</a></li>
          <%
        }
        if(enzymeDTO.getLatestHistoryEventClass().equals(EventConstant.DELETION.toString())) {
          %>
          <li><a href="deleteEntryUpdateFWD.do?<%=urlToLink%>">Delete (<i>update</i>)</a></li>
          <%
        }
        if(enzymeDTO.getLatestHistoryEventClass().equals(EventConstant.CREATION.toString())) {
          %>
          <li><a href="createEntryUpdateFWD.do?<%=urlToLink%>">Create (<i>update</i>)</a></li>
          <%
        }

      }
      %>
        <li><html:link action="addSubSubclassFWD" >Create Sub-Subclass</html:link></li>
      <%
      if( enzymeDTO.getXcharsView().indexOf("false")!=-1) {
        %>
        <li><a href="searchId.do?<%=urlToLink%>&view=INTENZ&xcharsView=true">IntEnz View (<i>XML</i>)</a></li>
        <%
      }else {
         %>
         <li><a href="searchId.do?<%=urlToLink%>&view=INTENZ&xcharsView=false">IntEnz View (<i>Default</i>)</a></li>
         <%
      }
    %>
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
