jt_DialogBox.imagePath = "images/";
var rheactionSelectorPopup = null;
var cofactorSelectorPopup = null;
var fba = null; // formButtonAction

function selectRheaction(){
	if (document.getElementById('rheaId').value == ''){
		alert("No reaction selected!");
	} else {
		formButtonAction('plus', 'rheaction', null, fba);
	}
}

function cancelRheaction(){}

function openRheactionSelector(ev, action){
    var e = jt_fixE(ev);
    fba = action;
    if (rheactionSelectorPopup == null) {
        rheactionSelectorPopup = new jt_AppConfirm('rhea_icon.png', selectRheaction, cancelRheaction);
        rheactionSelectorPopup.setWidth(700);
        rheactionSelectorPopup.setTitle("Rhea - select a reaction");
        rheactionSelectorPopup.setUrl('searchRhea.do', 400);
    }
    rheactionSelectorPopup.moveTo(e.clientX, e.clientY + document.body.scrollTop);
    rheactionSelectorPopup.askUser("Select a reaction and click OK...");
    if (rheactionSelectorPopup.focus) rheactionSelectorPopup.focus();
}

function selectCofactor(){
	if (document.getElementById('cofactorId').value == ''){
		alert("No cofactor selected!");
	} else {
		formButtonAction('plus', 'cofactors', null, fba);
	}
}

function cancelCofactor(){
	document.getElementById('cofactorId').value = null;
	document.getElementById('complexCofactorDtoIndex').value = null;
	document.getElementById('complexCofactorDtoInternalIndex').value = null;
	document.getElementById('complexCofactorOperator').value = null;
	document.getElementById('complexCofactorBrackets').value = null;
}

function openCofactorSelector(ev, action) {
    var e = jt_fixE(ev);
    var popupWidth = 700;
    fba = action;
    if (cofactorSelectorPopup == null) {
        cofactorSelectorPopup =
        	new jt_AppConfirm('chebi_icon.png', selectCofactor, cancelCofactor);
        cofactorSelectorPopup.setWidth(popupWidth);
        cofactorSelectorPopup.setTitle("ChEBI - select a compound as cofactor");
        cofactorSelectorPopup.setUrl('searchCofactors.do', '46ex');
    }
    cofactorSelectorPopup.moveTo(e.clientX - popupWidth,
    		e.clientY + document.body.scrollTop - 100);
    cofactorSelectorPopup.askUser("Select a compound and click OK...");
    if (cofactorSelectorPopup.focus) cofactorSelectorPopup.focus();
}

function openOredCofactorSelector(cofactorDtoIndex, ev, action){
	document.getElementById('complexCofactorDtoIndex').value = cofactorDtoIndex;
	openCofactorSelector(ev, action);
}

/**
 * Function to manage complex cofactors (using operators AND/OR)
 * by adding a new cofactor to an existing cofactorDTO.
 * @param cofactorDtoIndex index of the modified cofactorDto
 * @param dtoInternalIndex index of the affected existing cofactor within the cofactorDto
 * @param operator operator to be used with the newly added cofactor
 *		and the existing one at dtoInternalIndex
 * @param brackets create a new operator group? (boolean)
 * @param ev the javascript event
 * @param action action to be executed after the new cofactor has been chosen
 */
function openComplexCofactorSelector(cofactorDtoIndex, dtoInternalIndex, operator, brackets, ev, action){
	document.getElementById('complexCofactorDtoIndex').value = cofactorDtoIndex;
	document.getElementById('complexCofactorDtoInternalIndex').value = dtoInternalIndex;
	document.getElementById('complexCofactorOperator').value = operator;
	document.getElementById('complexCofactorBrackets').value = brackets;
	openCofactorSelector(ev, action);
}