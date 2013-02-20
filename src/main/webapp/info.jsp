<%@ page import="java.util.List,
                 java.util.ArrayList,
                 uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant,
                 uk.ac.ebi.xchars.domain.EncodingType"%>
<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Info"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value=""/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

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

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
