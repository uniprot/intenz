<%@ page import="java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.xchars.domain.EncodingType"%>

<%
  response.setContentType("text/html; charset=UTF-8");
  String title = "Info - Integrated Enzyme Database (IntEnz)";
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

<br>

  <%= request.getAttribute("info") %>

<br>
