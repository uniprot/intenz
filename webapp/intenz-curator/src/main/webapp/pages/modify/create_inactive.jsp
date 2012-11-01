<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2>Creating new <span id="deltraType">
	${empty enzymeDTO.transferredToEc? 'deleted' : 'transferred'}</span>
	EC number.</h2>

<script type="text/javascript">
/**
 * Sets the UI label according to the type of entry being created (DELeted or
 * TRAnsferred).
 * @param field the field containing any target EC number for a transfer.
 */
function setDeltraType(field){
	var deltraType = document.getElementById('deltraType');
	if (field.value.length > 0){
		deltraType.innerHTML = 'transferred';
	} else {
		deltraType.innerHTML = 'deleted';
	}
}
</script>

<logic:messagesPresent>
	<html:messages id="error" header="errors.header" footer="errors.footer">
		<li class="error"><bean:write name="error" filter="false"/></li>
	</html:messages>
</logic:messagesPresent>

<div>
<html:form action="/createInactiveEntry" focus="ec">
<html:hidden name="enzymeDTO" property="statusCode" value="OK"/>
<html:hidden name="enzymeDTO" property="active" value="false"/>

<div style="display: table-row;">
	<span style="display: table-cell; vertical-align: top;
		text-align: right;">EC</span>
	<html:text name="enzymeDTO" property="ec" styleId="ec" size="15"
		style="display: table-cell;"/>
</div>

<div style="display: table-row;">
	<span style="display: table-cell; vertical-align: top;
		text-align: right;">Transferred to EC</span>
	<html:text name="enzymeDTO" property="transferredToEc" size="15"
		onkeyup="setDeltraType(this)" style="display: table-cell;"
		title="This EC number must exist already."/>
</div>

<div style="display: table-row;">
	<span style="display: table-cell; vertical-align: top;
		text-align: right;">History line:</span>
	<html:textarea name="enzymeDTO" property="historyLine" cols="70" rows="2"
		style="vertical-align: top; display: table-cell;"/>
	<input type="button" value="&lt;x&gt;"
		onclick="openSpecialCharacterWindow('enzymeDTO', 'historyLine');"
		title="Open xchars window" class="xchars"/>
</div>

<div style="display: table-row;">
	<span style="display: table-cell; vertical-align: top;
		text-align: right;">Additional history information:</span>
	<html:textarea name="enzymeDTO" property="latestHistoryEventNote"
		cols="70" rows="5" style="vertical-align: top; display: table-cell;"/>
	<input type="button" value="&lt;x&gt;"
		onclick="openSpecialCharacterWindow('enzymeDTO', 'latestHistoryEventNote');"
		title="Open xchars window" class="xchars"/>
</div>

<div style="display: table-row;">
	<div style="display: table-cell"></div>
	<div style="display: table-cell; text-align: right;">
		<html:reset />&nbsp;
		<html:submit title="Create new inactive entry" value="Create entry"
			styleClass="submitButton" />
	</div>
</div>


</html:form>
</div>
