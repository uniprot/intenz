<%@ page import="uk.ac.ebi.xchars.SpecialCharacters,
                 uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
                 uk.ac.ebi.intenz.webapp.helper.EnzymeEntryHelper,
                 java.util.*,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant"%>

<%
  List preliminaryEcs = (List) request.getAttribute("preliminaryEcs");
  SpecialCharacters encoding = (SpecialCharacters) request.getSession().getServletContext().getAttribute("characters");
  response.setContentType("text/html; charset=UTF-8");
%>

<%
  if(preliminaryEcs != null && preliminaryEcs.size() > 0) {
%>

<b>Preliminary EC numbers</b>

<p>
The entries below are preliminary EC numbers issued by UniProt:
</p>

  <table width="70%" align="center" border="0">
    <%
      for (Iterator it = preliminaryEcs.iterator(); it.hasNext();) {
        EnzymeEntry enzymeEntry = (EnzymeEntry) it.next();
        String amended = "";
    %>
    <%= EnzymeEntryHelper.toHTML(enzymeEntry, encoding, null, EnzymeViewConstant.INTENZ) %>
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
