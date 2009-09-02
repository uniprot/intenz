<%@ tag language="java" pageEncoding="UTF-8"
	import="uk.ac.ebi.intenz.tools.sib.helper.SibEntryHelper,
		uk.ac.ebi.intenz.tools.sib.writer.EnzymeFlatFileWriter,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry,
		uk.ac.ebi.xchars.SpecialCharacters"
%><%@ attribute name="entry" required="true"
	type="uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry"
%><% EnzymeEntry entryParam = (EnzymeEntry) jspContext.getAttribute("entry");
%><%= EnzymeFlatFileWriter.export(
		SibEntryHelper.getSibEnzymeEntry(entryParam, SpecialCharacters.getInstance(null), null)) %>