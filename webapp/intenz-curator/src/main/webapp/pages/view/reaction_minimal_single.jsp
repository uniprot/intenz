<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>

<span class="rheaId">${r.id}</span>
<a href="${initParam['rhea.curator.url']}/view.do?id=${r.id}&embed=true" target="_blank">
	<img src="${pageContext.request.contextPath}/img/Zoom16.gif"
		alt="View RHEA:${r.id}" title="View RHEA:${r.id}"/></a>

<a href="${initParam['rhea.curator.url']}/modify.do?id=${r.id}" target="_blank">
	<img src="${pageContext.request.contextPath}/img/Edit16.gif"
		alt="Edit RHEA:${r.id}" title="Edit RHEA:${r.id}"/></a>

<bean:define id="equation" name="r" property="textualRepresentation" type="java.lang.String"/>
<xchars:translate>
	<%= equation.replace("=>","=&gt;").replace("<=","&lt;=").replace("<=>","&lt;=&gt;").replace("<?>","&lt;?&gt;") %>
</xchars:translate>
