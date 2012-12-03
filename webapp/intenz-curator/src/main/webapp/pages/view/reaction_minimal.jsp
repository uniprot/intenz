<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="r" name="reactionForm" property="reaction"
    type="uk.ac.ebi.rhea.domain.Reaction"/>

<%@ include file="reaction_minimal_single.jsp" %>

<logic:equal value="true" name="r" property="complex">
<${r.stepwise? 'o':'u'}l style="margin: 0px">
<logic:iterate id="child" name="r" property="children">
	<bean:define id="r" name="child" property="reaction" type="uk.ac.ebi.rhea.domain.Reaction"/>
	<li>
	<%@ include file="reaction_minimal_single.jsp" %>
	</li>
</logic:iterate>
</${r.stepwise? 'o':'u'}l>
</logic:equal>
