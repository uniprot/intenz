<% if (request.getAttribute("sent") == null){ %>


	<p style="margin-bottom: 2em">Send your comments straight to the IntEnz team using this form.
		 <br/>
		For support or feature requests you are encouraged to use our
		<a target="_blank" class="externalLink" href="https://sourceforge.net/projects/intenz/">SourceForge</a>
		project pages, where we all can keep track of them.
	</p>

<div id="xround" style="width: 600px"><b class="xtop"><b class="xb1"></b><b class="xb2"></b><b class="xb3"></b><b class="xb4"></b></b><div class="xboxcontent">
	<form method="POST" action="contact">
	<table class="mailForm" align="center">
		<tr>
			<th>E-mail:</th>
			<td><input type="text" size="30" name="email" /></td>
		</tr>
		<tr>
			<th>Comment:</th>
			<td><textarea name="message" rows="10" cols="40">Insert your comment here.</textarea></td>
		</tr>
		 <tr>
		 <td></td>
		 <th><input type="submit" class="submit_button" name="intenzMail" value="Send"/>
		 </th>
		 </tr>
	</table>
	</form>
</div><b class="xbottom"><b class="xb4"></b><b class="xb3"></b><b class="xb2"></b><b class="xb1"></b></b></div>

<% } else if (request.getAttribute("sent").equals(Boolean.TRUE)){ %>

	<p>Thanks for your feedback! Your message will be replied to as soon as possible.</p>

<% } else { %>

	<p class="error"><%= request.getAttribute("error") %></p>

<% } %>
