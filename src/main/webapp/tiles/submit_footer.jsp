<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<div style="float: right">
    <html:form action='<%= (String) session.getAttribute("previewFrom") %>' method="post">
         <input type="submit" value="Submit >>" title="Update entry in the database" name="update"
         	 class="submitButton"/>
    </html:form>
</div>
<div style="float: right">
    <% String editAction = (String) session.getAttribute("previewFrom") + "FWD.do"; %>
    <form name="edit" action="<%= editAction %>" method="post" style="float: left">
         <input type="submit" value="<< Edit" title="Back to amend mode" name="amend" class="editButton"/>
    </form>
</div>

<script type="text/javascript" src="resources/udm-resources/udm-dom.js"></script>