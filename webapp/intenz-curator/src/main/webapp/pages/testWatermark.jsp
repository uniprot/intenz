<% if (application.getInitParameter("dbUrl").indexOf("dev") > -1
	|| application.getInitParameter("dbUrl").indexOf("test") > -1){ %>
<style type="text/css">
	body {
		background: url("img/test.png") no-repeat fixed 50% 50%;
	}
</style>
<% } %>
