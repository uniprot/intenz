/** Overrides rhea-search function */
function endSearchReaction(ajaxResponse){
	var reactionList = document.getElementById('reactionSearchResults');
	var reactionListSize = reactionList.getElementsByTagName('div').length;
	var data = document.getElementById('reactionData');
	hideIndicator(ajaxResponse);
	if (reactionListSize == 0){
		data.textContent = '(No matching reaction)';
		top.document.getElementById('rheaId').value = '';
	} else {
		data.textContent = 'Select one of the ' + reactionListSize + ' matching reactions';
	}
}

/** Overrides rhea-search function */
function endGetReaction(ajaxResponse){
	top.document.getElementById('rheaId').value = document.getElementById('selectedReactionId').value;
	hideIndicator(ajaxResponse);
}

/** Overrides rhea-search function */
function endSearchCompound(ajaxResponse){
	var compoundList = document.getElementById('compoundSearchResults');
	var compoundListSize = compoundList.getElementsByTagName('div').length;
	var data = document.getElementById('compoundData');
	hideIndicator(ajaxResponse);
	if (compoundListSize == 0){
		data.textContent = '(No matching compound)';
		top.document.getElementById('cofactorId').value = '';
	} else {
		data.textContent = 'Select one of the ' + compoundListSize + ' matching compounds';
	}
}

/** Overrides rhea-search function */
function endGetCompound(ajaxResponse){
	var cofactorId = document.getElementById('compoundId').value;
	if (cofactorId == 0) cofactorId = document.getElementById('compoundXrefAcc').value;
	top.document.getElementById('cofactorId').value = cofactorId;
	hideIndicator(ajaxResponse);
}