<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Returns a linked cofactor."
	import="uk.ac.ebi.intenz.domain.enzyme.Cofactor" %>
		
<%@ taglib prefix="x" uri="http://www.ebi.ac.uk/xchars" %>

<%@ attribute name="cofactor" required="true"
	type="uk.ac.ebi.intenz.domain.enzyme.Cofactor" %>

<a href="${cofactor.compound.xref.url}"
	target="_blank"><x:translate>${cofactor.compound}</x:translate></a>
