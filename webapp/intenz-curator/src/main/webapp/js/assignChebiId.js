/**
 * Only callable from the ajax tag, which has a target property.
 */
function checkChebiAccession(){
	var userInput = document.getElementById('chebiId');
	userInput.value = userInput.value.toUpperCase();
	if (userInput.value.indexOf('CHEBI:') != 0){
		userInput.value = 'CHEBI:' + userInput.value;
	}
	showIndicator(this.target);
}

function checkCompoundId(ajaxResponse){
    var oldChebiId = document.getElementById('oldChebiId').value;
    var oldName = document.getElementById('oldName').value;
    var newChebiId = document.querySelector(
    	"#chebiCompound input[name='xref.accessionNumber']").value;
    var newName = document.querySelector(
		"#chebiCompound input[name='name']").value;
    if (newChebiId){
    	document.getElementById('newChebiId').value = newChebiId;
    	document.getElementById('newName').value = newName;
    	if (oldChebiId == newChebiId){
	        alert('It\'s the same compound!');
	        Element.hide('assignChebiIdButton');
	    } else {
	    	document.getElementById('assignChebiIdButton').innerHTML =
	    		'Replace cofactor<br/>'
	    		+ '<b>' + oldName + ' [' + oldChebiId + ']</b><br/>'
	    		+ 'with<br/>'
	    		+ '<b>' + newName + ' [' + newChebiId + ']</b>';
	        Element.show('assignChebiIdButton');
	    }
    } else {
        Element.hide('assignChebiIdButton');
    }
    hideIndicator(ajaxResponse);
}

// See rhea-search.js
enter2clickFields.set('chebiId', 'searchChebiButton');