<% if (application.getInitParameter("dbConfig").indexOf("dev") > -1
	|| application.getInitParameter("dbConfig").indexOf("test") > -1){ %>
<style type="text/css">
	body {
		background: url("img/test.png") no-repeat fixed 50% 50%;
	}
</style>
<% } %>
