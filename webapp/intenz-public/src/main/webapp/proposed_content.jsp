<%@ page import="uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
                 java.util.*,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant"%>

<%
  List proposedEnzymes = (List) request.getAttribute("proposed");
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
  response.setContentType("text/html; charset=UTF-8");
%>

<%
  if(proposedEnzymes != null && proposedEnzymes.size() > 0) {
%>

<b>Proposed Changes to the Enzyme List</b>

<p>
The entries below are proposed additions and amendments to the Enzyme Nomenclature list.
Comments and suggestions on these draft entries should be sent to
<a class="icon_email" href="mailto:ktipton@tcd.ie; sboyce@tcd.ie">Professor K.F. Tipton and Dr S. Boyce</a>
(Department of Biochemistry, Trinity College Dublin, Dublin 2, Ireland).
To prevent confusion please do not quote new EC numbers until they are incorporated into the main list.
</p>

  <table width="70%" align="center" border="0">
    <%
      for (Iterator it = proposedEnzymes.iterator(); it.hasNext();) {
        EnzymeEntry proposedEnzyme = (EnzymeEntry) it.next();
        String amended = "";
    %>
    <tr>
      <td valign="top" align="left">
        <a href="query?cmd=SearchProposed&id=<%= proposedEnzyme.getId() %>">EC
           <%= amended + proposedEnzyme.getEc().toString() %></a>
      </td>
      <td valign="top" align="left">
        <%= encoding.xml2Display(proposedEnzyme.getCommonName(EnzymeViewConstant.INTENZ).getName()) %>
      </td>
    </tr>
    <%
      }
    %>
  </table>
<%
  } else {
 %>
  <table width="70%" align="center" border="0">
    <tr>
      <td valign="top" align="left">
        <%= request.getAttribute("message") %>
      </td>
    </tr>
  </table>
  <%
  }
%>
