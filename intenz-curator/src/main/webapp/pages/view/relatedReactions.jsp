<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<table class="relatedReactions" align="center">
	<tr>
		<th>Direction</th>
		<th>Reaction ID</th>
		<th>Related EC numbers</th>
		<th>Parent reactions and related EC numbers</th>
	</tr>
<logic:iterate id="rr" name="relatedReactions">
	<tr class="${rr.value == param.id ? 'this' : ''}reaction">
		<td align="center"><bean:write name="rr" property="key.label"/></td>
		<td align="center" style="white-space: nowrap;">
			<logic:equal value="${param.id}" name="rr" property="value">
				<bean:write name="rr" property="value"/>
			</logic:equal>
			<logic:notEqual value="${param.id}" name="rr" property="value">
				<bean:write name="rr" property="value"/>
				<html:link action="searchReaction"
					paramId="q" paramName="rr" paramProperty="value">
				<img src="${pageContext.request.contextPath}/img/Zoom16.gif"
					alt="View ${param.id}" title="View ${param.id}"/>
				</html:link>
			</logic:notEqual>
		</td>
		<td><jsp:include flush="true" page="${contextPath}/getRelatedEnzymes.do">
				<jsp:param name="id" value="${rr.value}"/>
			</jsp:include>
		</td>
		<td><jsp:include flush="true" page="${contextPath}/getParentReactions.do">
				<jsp:param name="id" value="${rr.value}"/>
				<jsp:param name="equation" value="true"/>
				<jsp:param name="ecs" value="true"/>
			</jsp:include>
		</td>
	</tr>
</logic:iterate>
</table>
