<%@ tag language="java" pageEncoding="UTF-8"
	description="Upper case the first letter of the body content."
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><jsp:doBody var="body" scope="page"
/>${fn:toUpperCase(fn:substring(body, 0, 1))}${fn:substring(body, 1, -1)}