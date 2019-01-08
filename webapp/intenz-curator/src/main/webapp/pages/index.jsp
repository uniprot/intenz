<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<table height="100%" width="100%" border="0" cellspacing="0"
	cellpadding="0">
	<tr>
		<td align="center"><img src="images/intenz_small.gif" /><br />
		<h1 class="index">Welcome to the IntEnz Curator Web Application</h1>
		</td>
	</tr>
	<tr>
		<td height="30">&nbsp;</td>
	</tr>
	<tr>
		<td valign="top" align="center">
		<table class="content_table" border="0" cellspacing="20"
			cellpadding="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="20" cellpadding="0">
					<tr>
						<td class="data_region_header_row">EC Search</td>
					</tr>
					<tr>
						<td><html:form action="searchEc" focus="ec" method="get">
							<table width="100%" border="0">
								<logic:messagesPresent>
									<tr>
										<td class="error"><html:messages id="error" property="ec"><%=error%></html:messages>
										</td>
									</tr>
								</logic:messagesPresent>
								<tr>
									<td align="center" width="100%">
									<table cellspacing="5">
										<tr>
											<td valign="right" align="center"><b>EC:</b>&nbsp;<html:text
												property="ec" size="20" maxlength="100" /></td>
											<td align="left"><html:submit value="Search" /></td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
						</html:form></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td valign="top">
				<table width="100%" border="0" cellspacing="20" cellpadding="0">
					<tr>
						<td class="data_region_header_row">Browse</td>
					</tr>
					<tr>
						<td align="center">
						<table border="0">
							<tr>
								<td><html:link page="/searchEc.do?ec=1"
									styleClass="no_decoration">EC 1&nbsp;&nbsp;Oxidoreductases</html:link></td>
							</tr>
							<tr>
								<td><html:link page="/searchEc.do?ec=2"
									styleClass="no_decoration">EC 2&nbsp;&nbsp;Transferases</html:link></td>
							</tr>
							<tr>
								<td><html:link page="/searchEc.do?ec=3"
									styleClass="no_decoration">EC 3&nbsp;&nbsp;Hydrolases</html:link></td>
							</tr>
							<tr>
								<td><html:link page="/searchEc.do?ec=4"
									styleClass="no_decoration">EC 4&nbsp;&nbsp;Lyases</html:link></td>
							</tr>
							<tr>
								<td><html:link page="/searchEc.do?ec=5"
									styleClass="no_decoration">EC 5&nbsp;&nbsp;Isomerases</html:link></td>
							</tr>
							<tr>
								<td><html:link page="/searchEc.do?ec=6"
									styleClass="no_decoration">EC 6&nbsp;&nbsp;Ligases</html:link></td>
							</tr>
                                                        <tr>
								<td><html:link page="/searchEc.do?ec=7"
									styleClass="no_decoration">EC 6&nbsp;&nbsp;Translocases</html:link></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
				<td valign="top">
				<table height="100%" width="100%" border="0" cellspacing="20"
					cellpadding="0">
					<tr>
						<td class="data_region_header_row">Text Search</td>
					</tr>
					<tr class="search">
						<td align="center"><html:form action="search">
							<table width="100%" border="0">
								<tr>
									<td width="100%">
									<table cellspacing="5">
										<tr>
											<td align="right"><html:text disabled="false"
												property="q" size="50" maxlength="100" /></td>
											<td align="left"><html:submit disabled="false"
												value="Search" /></td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
						</html:form></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="100%" class="footer_font" align="center" valign="top">
		<br />
		Version <bean:message key="intenz.curator.webapp.version" />
		</td>
	</tr>
</table>