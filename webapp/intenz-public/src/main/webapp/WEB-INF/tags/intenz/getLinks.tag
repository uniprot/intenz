<%@ tag language="java" pageEncoding="UTF-8"
	body-content="empty"
	description="Retrieves links from an enzyme entry corresponding to a given
	    view as a variable which maps link groups (the Strings
	    'EC' | 'DOMAINS_FAMILIES' | 'STRUCTURE' | 'ONTOLOGIES') to lists of
	    links. NOTE: Swiss-Prot links and CAS numbers are EXCLUDED."
	import="java.util.ArrayList,
		java.util.HashMap,
		java.util.List,
		java.util.Map,
		uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant,
		uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry" %>
<%@ tag import="uk.ac.ebi.intenz.domain.enzyme.EnzymeLink" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="entry" required="true"
	type="uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry"
	description="The enzyme containing links." %>
<%@ attribute name="view" required="true"
    description="The view for which we want links." %>
<%@ attribute name="var" required="true" rtexprvalue="false"
    type="java.lang.String"
	description="Variable name for the exported map." %>

<%@ variable name-from-attribute="var" alias="m"
	variable-class="java.util.Map" scope="AT_END" %>

<%
final String EC = "EC",
    DOMAINS_FAMILIES = "DOMAINS_FAMILIES",
    STRUCTURE = "STRUCTURE",
    ONTOLOGIES = "ONTOLOGIES";

EnzymeEntry entryParam = (EnzymeEntry) jspContext.getAttribute("entry");
String viewParam = (String) jspContext.getAttribute("view");
Map<String, List<EnzymeLink>> m = new HashMap<String, List<EnzymeLink>>();
for (EnzymeLink link : entryParam.getLinks(viewParam)) {
    XrefDatabaseConstant db = link.getXrefDatabaseConstant();
    if (db.equals(XrefDatabaseConstant.SWISSPROT)){
        continue;
    } else if (db.equals(XrefDatabaseConstant.GO)){
        if (m.get(ONTOLOGIES) == null){
            m.put(ONTOLOGIES, new ArrayList<EnzymeLink>());
        }
        m.get(ONTOLOGIES).add(link);
    } else if (db.equals(XrefDatabaseConstant.PROSITE)){
        if (m.get(DOMAINS_FAMILIES) == null){
            m.put(DOMAINS_FAMILIES, new ArrayList<EnzymeLink>());
        }
        m.get(DOMAINS_FAMILIES).add(link);
    } else if (db.equals(XrefDatabaseConstant.CSA)
            || db.equals(XrefDatabaseConstant.PDB)){
        if (m.get(STRUCTURE) == null){
            m.put(STRUCTURE, new ArrayList<EnzymeLink>());
        }
        m.get(STRUCTURE).add(link);
    } else {
        if (m.get(EC) == null){
            m.put(EC, new ArrayList<EnzymeLink>());
        }
        m.get(EC).add(link);
    }
}
jspContext.setAttribute("m", m);
%>
<c:set var="m" value="${m}"/>
