<%@ page import="uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.constants.EnzymeStatusConstant,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber.Type,
                 java.util.ArrayList,
                 java.net.URLEncoder,
                 uk.ac.ebi.intenz.webapp.controller.SearchCommand.Result"%>

<%
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
  response.setContentType("text/html; charset=UTF-8");
  String title = "Search Result - Integrated Enzyme Database (IntEnz)";
%>

<script language='javascript'>
  function windowOpenWithSize(theurl, thewd, theht) {
    if(!(theht)) {
      theht = 528;
    }
    if(!(thewd)) {
      thewd = 410;
    }
    var newwin  = window.open(theurl,"labelsize","dependent=yes, resizable=yes,toolbar=no,menubar=no,scrollbars=yes,width="+thewd+",height="+theht);
    newwin.focus();
  }
</script>

<br/>

  <table width="90%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center" class="searchBar">
      <%
        int start = Integer.parseInt((String) request.getAttribute("st"));
        int end = Integer.parseInt((String) request.getAttribute("end"));
        int size = Integer.parseInt((String) request.getAttribute("size"));

        if(request.getAttribute("pst") != null) {
	%>
          &lt; <a href="query?cmd=BrowseResult&st=<%= request.getAttribute("pst") %>&q=<%= URLEncoder.encode((String) request.getAttribute("query"), "UTF-8") %>)">previous</a> |
	<%
        }
    %>
    <b><%= start + 1 %>
    <%
        if(start != end) {
    %>
        .. <%= end %>
    <%
        }
    %>
    of <%= size %></b>
      <%
         if(request.getAttribute("nst") != null) {
           %>
             | <a href="query?cmd=BrowseResult&st=<%= request.getAttribute("nst") %>&q=<%= URLEncoder.encode((String) request.getAttribute("query"), "UTF-8") %>">next</a> &gt;
           <%
         }
        %>
        </td>
    </tr>
  </table>
  <p>
  <table width="100%" border="0" cellpadding="0" cellspacing="10">

    <%
      ArrayList group = (ArrayList)request.getAttribute("group");
      for (int iii = 0; iii < group.size(); iii++) {
        Result result = (Result) group.get(iii);
        String enzymeId = result.getId();
        String ecString = result.getEc();
        EnzymeCommissionNumber ec = EnzymeCommissionNumber.valueOf(ecString);
        String name = result.getCommonName();
        String status = result.getStatus();
        %>
          <tr>
            <td align="right" valign="top"><%= start+iii+1 %>.</td>
            <td>
<%--
            
            <a href="query?cmd=Search${ec.type eq 'ENZYME' or ec.type eq 'PRELIMINARY'?
            	'ID&id=' : 'EC&ec='}${ec.type eq 'ENZYME' or ec.type eq 'PRELIMINARY'?
            	enzymeId : ecString}"
            	class="${ecString eq query? 'queryEc' : ''}">${ecString}</a>
            
            &nbsp;<small><b class="${status eq 'PR' or result.active? '' : 'inactiveEntry'}">
            	${status eq 'PR'? '(proposal)' : result.active? '' : 'inactive entry'}
            	</b></small>
 --%>            
            <%
              if(ec.getType() == Type.ENZYME || ec.getType() == Type.PRELIMINARY) {
            %>
           <a href="query?cmd=SearchID&id=<%= enzymeId %>"
            	class="<%= ecString.equals(request.getAttribute("query"))? "queryEc" : "" %>"><%= ecString %></a>
            <%
              } else {
            %>
            <a href="query?cmd=SearchEC&ec=<%= ecString %>"><%= ecString %></a>
            <%
              }
              if(status.equals(EnzymeStatusConstant.PROPOSED.getCode())) {
            %>
              &nbsp;<small><b>(proposal)</b></small>
            <%
              } else if (!result.isActive()){
            %>
              &nbsp;<small><b class="inactiveEntry">inactive entry</b></small>
            <%
              }
            %>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td><%= encoding.xml2Display(name) %></td>
          </tr>
        <%
      }
    %>

  </table>

<hr/>

  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td height="57">
        <form method="get" action="query">
          <input type="hidden" name="cmd" value="Search">
          <table width="100%" border="0">
            <tr>
              <td align="center" width="100%">
                <table cellspacing="5">
                  <tr>
                    <td valign="center"><input type="text" size="50" maxsize="100" name="q" value="<%= (String) request.getAttribute("queryTF") %>"></td>
                    <td valign="center">
                      <select name="t" size="1">
                        <option value="exact" selected="selected">Exact phrase</option>
                        <option value="any">Any of these words</option>
                        <option value="all">All of these words</option>
                      </select><br/>
                    </td>
                    <td valign="center">
                      <a href="javascript:windowOpenWithSize('encoding.html', 280, 400)"><img style="vertical-align:middle" src="images/alpha_icon.gif" width="16" height="15" border="1" alt="Special characters"></a>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2" align="center"><input type="submit" value="Search" class="submit_button"></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          </form>
      </td>
    </tr>
  </table>
