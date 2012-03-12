<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://www.ebi.ac.uk/xchars" prefix="xchars" %>
<tr>
    <td valign="top" width="100%">
    	<logic:greaterThan value="0" name="reactionDto" property="id">
    		<html:hidden name="reactionDto" property="xmlTextualRepresentation" indexed="true"/>
    		<span class="${reactionDto.status}">
                <jsp:include page="/view.do">
                    <jsp:param name="id" value="${reactionDto.id}"/>
                </jsp:include>
             </span>
     	</logic:greaterThan>
    	<logic:equal value="0" name="reactionDto" property="id">
	        <html:textarea name="reactionDto" property="xmlTextualRepresentation"
	        	cols="80" rows="4" indexed="true" />
			<input type="button" value="&lt;x&gt;"
				onclick="openSpecialCharacterWindow('enzymeDTO', 'reactionDto[${index}].xmlTextualRepresentation');"
				title="Open xchars window" class="xchars"/>
    	</logic:equal>
	    <html:hidden name="reactionDto" property="id" indexed="true"/>
    </td>
    <td width="100px" valign="top" align="center">
	    <html:hidden name="reactionDto" property="source" indexed="true"/>
        <bean:write name="reactionDto" property="sourceDisplay" filter="false"/>
    </td>
    <td width="100px" valign="top" align="center">
        <html:select name="reactionDto" property="view" indexed="true" size="1"
        	styleId="reactionView-${index}" onchange="">
            <html:option value="IUBMB">NC-IUBMB</html:option>
            <html:option value="SIB">ENZYME</html:option>
            <html:option value="IUBMB_SIB">NC-IUBMB & ENZYME</html:option>
            <html:option value="IUBMB_INTENZ">NC-IUBMB & IntEnz</html:option>
            <html:option value="SIB_INTENZ">ENZYME & IntEnz</html:option>
            <html:option value="INTENZ">All views</html:option>
        </html:select>
    </td>
    <td width="100px" valign="top" align="center">
    <%--
    	<html:select name="reactionDto" property="iubmb" indexed="true"
    		onchange="">
    		<html:option value="true">Yes</html:option>
    		<html:option value="false">No</html:option>
   		</html:select>
     --%>
     	<bean:define id="flag" name="reactionDto" value="iubmb"/>
   		<html:hidden name="reactionDto" property="iubmb" indexed="true"
   			styleId="iubmbFlag-${index}"/>
   		<input type="checkbox" onchange="setIubmbFlag(this,${index})"
   			${flag eq 'true'? 'checked' : ''}/>
    </td>
    <%-- 'Minus' button --%>
    <td valign="top">
        <input title="Remove reaction"
               type="button"
               value="-"
               onclick="formButtonAction('delete', 'reactions', '${index}', '${formButtonAction}')"/>
    </td>
</tr>
