function setPubFields(type, index) {
  var name = "reference[" + index + "]";
  if(type == 'J') setJournalFields(name);
  if(type == 'B') setBookFields(name);
  if(type == 'P') setPatentFields(name, index);
}

function setJournalFields(name) {
  node = document.getElementById(name + ".pubMedIdLabel");
  node.firstChild.nodeValue = "PubMed ID:";
  document.enzymeDTO.elements[name+".edition"].disabled = true;
  document.enzymeDTO.elements[name+".xmlEditor"].disabled = true;
  document.enzymeDTO.elements[name+".xmlEditor.xchars"].disabled = true;
  document.enzymeDTO.elements[name+".xmlPublisher"].disabled = true;
  document.enzymeDTO.elements[name+".publisherPlace"].disabled = true;

  document.enzymeDTO.elements[name+".pubMedId"].disabled = false;
  document.enzymeDTO.elements[name+".xmlAuthors"].disabled = false;
  document.enzymeDTO.elements[name+".xmlTitle"].disabled = false;
  document.enzymeDTO.elements[name+".xmlPubName"].disabled = false;
  document.enzymeDTO.elements[name+".volume"].disabled = false;
  document.enzymeDTO.elements[name+".year"].disabled = false;
  document.enzymeDTO.elements[name+".firstPage"].disabled = false;
  document.enzymeDTO.elements[name+".lastPage"].disabled = false;
}

function setBookFields(name) {
  node = document.getElementById(name + ".pubMedIdLabel");
  node.firstChild.nodeValue = "PubMed ID:";
  document.enzymeDTO.elements[name+".pubMedId"].disabled = true;

  document.enzymeDTO.elements[name+".xmlAuthors"].disabled = false;
  document.enzymeDTO.elements[name+".xmlTitle"].disabled = false;
  document.enzymeDTO.elements[name+".xmlPubName"].disabled = false;
  document.enzymeDTO.elements[name+".volume"].disabled = false;
  document.enzymeDTO.elements[name+".year"].disabled = false;
  document.enzymeDTO.elements[name+".firstPage"].disabled = false;
  document.enzymeDTO.elements[name+".lastPage"].disabled = false;
  document.enzymeDTO.elements[name+".edition"].disabled = false;
  document.enzymeDTO.elements[name+".xmlEditor"].disabled = false;
  document.enzymeDTO.elements[name+".xmlEditor.xchars"].disabled = false;
  document.enzymeDTO.elements[name+".xmlPublisher"].disabled = false;
  document.enzymeDTO.elements[name+".publisherPlace"].disabled = false;
}

function setPatentFields(name, index) {
  node = document.getElementById(name + ".pubMedIdLabel");
  node.firstChild.nodeValue = "Patent number:";

  document.enzymeDTO.elements[name+".volume"].disabled = true;
  document.enzymeDTO.elements[name+".firstPage"].disabled = true;
  document.enzymeDTO.elements[name+".lastPage"].disabled = true;
  document.enzymeDTO.elements[name+".edition"].disabled = true;
  document.enzymeDTO.elements[name+".xmlEditor"].disabled = true;
  document.enzymeDTO.elements[name+".xmlEditor.xchars"].disabled = true;
  document.enzymeDTO.elements[name+".xmlPublisher"].disabled = true;
  document.enzymeDTO.elements[name+".publisherPlace"].disabled = true;
  document.enzymeDTO.elements[name+".xmlPubName"].disabled = true;
  document.enzymeDTO.elements["pubMedFetch["+index+"]"].disabled = true;

  document.enzymeDTO.elements[name+".pubMedId"].disabled = false;
  document.enzymeDTO.elements[name+".xmlAuthors"].disabled = false;
  document.enzymeDTO.elements[name+".xmlTitle"].disabled = false;
  document.enzymeDTO.elements[name+".year"].disabled = false;
}

function setLinkFields(databaseSource, url, ac, name) {
  if(databaseSource == "NONE") setEmptyLinkFields(url, ac, name);
  if(databaseSource == "BRENDA") setXrefLinkFields(url, ac, name);
  if(databaseSource == "MetaCyc") setXrefLinkFields(url, ac, name);
  if(databaseSource == "KEGG") setKeggLinkFields(url, ac, name);
  if(databaseSource == "ERGO") setErgoLinkFields(url, ac, name);
  if(databaseSource == "PROSITE") setPrositeLinkFields(url, ac, name);
  if(databaseSource == "OMIM") setMimLinkFields(url, ac, name);
  if(databaseSource == "CAS") setCasLinkFields(url, ac, name);
  if(databaseSource == "DIAGRAM") setDiagramLinkFields(url, ac, name);
  if(databaseSource == "GO") setGoLinkFields(url, ac, name);
  if(databaseSource == "NIST 74") setNIST74LinkFields(url, ac, name);
  if(databaseSource == "MEROPS") setMeropsLinkFields(url, ac, name);
  if(databaseSource == "UM-BBD") setUmbbdLinkFields(url, ac, name);
  if(databaseSource == "PDB") setPredefinedLinkFields(url, ac, name);
}

function setEmptyLinkFields(url, ac, name) {
  document.enzymeDTO.elements[url].value = "";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].disabled = true;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[name].value = "";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

function setBrendaLinkFields(url, ac, name) {
	setXrefLinkFields(url, ac, name);
}

function setKeggLinkFields(url, ac, name) {
	setPredefinedLinkFields(url, ac, name);
}

function setErgoLinkFields(url, ac, name) {
	setPredefinedLinkFields(url, ac, name);
}

function setSpLinkFields(url, ac, name) {

  document.enzymeDTO.elements[url].value = "(predefined)";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[ac].disabled = false;
  document.enzymeDTO.elements[name].value = "";
  document.enzymeDTO.elements[name].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[name].disabled = false;
}

function setPrositeLinkFields(url, ac, name) {
  document.enzymeDTO.elements[url].value = "(predefined)";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].disabled = false;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[name].value = "PROSITE";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

function setMimLinkFields(url, ac, name) {
  document.enzymeDTO.elements[url].value = "n/a";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].disabled = false;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[name].value = "";
  document.enzymeDTO.elements[name].disabled = false;
  document.enzymeDTO.elements[name].style.backgroundColor = "#ffffff";
}

function setCasLinkFields(url, ac, name) {
	setXrefLinkFields(url, ac, name);
}


function setGoLinkFields(url, ac, name) {
	setXrefLinkFields(url, ac, name);
}

function setGtdLinkFields(url, ac, name) {

  document.enzymeDTO.elements[url].value = "";
  document.enzymeDTO.elements[url].disabled = false;
  document.enzymeDTO.elements[url].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[ac].value = "n/a";
  document.enzymeDTO.elements[ac].disabled = true;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[name].value = "n/a";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

function setMeropsLinkFields(url, ac, name) {

  document.enzymeDTO.elements[url].value = "";
  document.enzymeDTO.elements[url].disabled = false;
  document.enzymeDTO.elements[url].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[ac].value = "n/a";
  document.enzymeDTO.elements[ac].disabled = true;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[name].value = "n/a";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

function setUmbbdLinkFields(url, ac, name) {
	setPredefinedLinkFields(url, ac, name);
}

function setDiagramLinkFields(url, ac, name) {

  document.enzymeDTO.elements[url].value = "";
  document.enzymeDTO.elements[url].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[url].disabled = false;
  document.enzymeDTO.elements[ac].value = "n/a";
  document.enzymeDTO.elements[ac].disabled = true;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[name].value = "";
  document.enzymeDTO.elements[name].disabled = false;
  document.enzymeDTO.elements[name].style.backgroundColor = "#ffffff";
}

function setNIST74LinkFields(url, ac, name) {
	setPredefinedLinkFields(url, ac, name);
}

function setPdbLinkFields(url, ac, name) {
	setXrefLinkFields(url, ac, name);
}

/**
 * Does not allow any edition of the link (based on EC number of the current
 * entry).
 * @param url
 * @param ac
 * @param name
 * @return
 */
function setPredefinedLinkFields(url, ac, name){
	  document.enzymeDTO.elements[url].value = "(predefined)";
	  document.enzymeDTO.elements[url].disabled = true;
	  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
	  document.enzymeDTO.elements[ac].value = "n/a";
	  document.enzymeDTO.elements[ac].disabled = true;
	  document.enzymeDTO.elements[ac].style.backgroundColor = "#dbdbdb";
	  document.enzymeDTO.elements[name].value = "n/a";
	  document.enzymeDTO.elements[name].disabled = true;
	  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

/**
 * Allows edition of the accession number (predefined URL).
 * @param url
 * @param ac
 * @param name
 * @return
 */
function setXrefLinkFields(url, ac, name) {
  document.enzymeDTO.elements[url].value = "(predefined)";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].disabled = false;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[name].value = "n/a";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}

/**
 * Opens a prompt dialog to edit the comment associated to a data item.
 * Depending on the resulting comment (being empty or not) the appearance
 * of the referring link changes.
 * @param elemName The name of the commented element
 */
function editDataComment(elemName){
    var comment = document.enzymeDTO.elements[elemName+'.dataComment'].value;
    var dbName = document.enzymeDTO.elements[elemName+'.databaseName'].value;
    var accession = document.enzymeDTO.elements[elemName+'.accession'].value;
    var promptText = "Link to " + dbName;
    if (accession != null && accession != '') promptText += '\nAccession # ' + accession;
    promptText += '\nComment:';
    var newComment = window.prompt(promptText, comment);

    if (newComment != null){
        document.enzymeDTO.elements[elemName+'.dataComment'].value = trim(newComment);
    }
    updateDataCommentEditLink(elemName);
}

/** Updates the appearance of the link to the comment mini-editor */
function updateDataCommentEditLink(elemName){
    var link = document.getElementById(elemName+'.dataComment.edit');
    var comment = document.enzymeDTO.elements[elemName+'.dataComment'].value;
    //link.setAttribute('style', getStyle(comment));
    link.setAttribute('src', getIconUrl(comment));
}

/** Returns different styles depending on the comment emptiness */
function getStyle(comment){
    if (comment == null || comment == ''){
        return 'background-color: #fff';
    } else {
        return 'background-color: #ff0';
    }
}

/** Returns different URLs depending on the comment emptiness */
function getIconUrl(comment){
    if (comment == null || comment == ''){
        return 'images/uncommented.gif';
    } else {
        return 'images/commented.gif';
    }
}

function trim(str){
    str = str.replace(/^\s+/g, '');
    str = str.replace(/\s+$/g, '');
    return str;
}

/**
 * Checks that the EC number and the status is consistent for preliminary
 * entries, fixing any of them if needed.
 * @param ev the event triggering the check
 */
function checkPreliminary(ev){
	var ec = document.getElementById('ec');
	if (ec == null) return;
	var statusCode = document.getElementById('statusCode');
	var isPreliminary = false;
	if (ev.target == ec){
		isPreliminary = ec.value.indexOf('n') != -1;
		if (isPreliminary){
			statusCode.value = 'PM';
		} else {
			statusCode.value = 'SU'; // FIXME
		}
	} else if (ev.target == statusCode){
		isPreliminary = statusCode.value == 'PM';
		if (isPreliminary && ec.value.indexOf('n') == -1){
			
		} else if (!isPreliminary && ec.value.indexOf('n') != -1){
			ec.value = ec.value.replace('n', '', 'gi');
		}
	}
}

/**
 * Checks that the value of the view selector and the IUBMB checkbox are
 * compatible.
 * @param event the event triggering the check
 */
function checkIubmbView(target){
	if (target.id.indexOf('view') > -1){
		// Modified view, we have to check the IUBMB flag:
		var iubmbElemId = target.id.replace('view', 'iubmb');
		if (target.value == 'INTENZ' || target.value.indexOf('IUBMB') > -1){
			// This is IUBMB's:
			document.getElementById(iubmbElemId).checked = true;
        } else {
        	document.getElementById(iubmbElemId).checked = false;
		}
	} else if (target.id.indexOf('iubmb') > -1){
		// Modified IUBMB flag, we have to check the view:
		var viewElemId = target.id.replace('iubmb', 'view');
		var viewElem = document.getElementById(viewElemId);
		if (target.checked){
			// All views:
			viewElem.value = 'INTENZ';
		} else {
			if (viewElem.value == 'INTENZ' || viewElem.value.indexOf('IUBMB') > -1){
				// Conflict:
				alert('According to the view, this is an IUBMB reaction');
				target.checked = true;
			}
		}
	}
}

function setIubmbFlag(target, index){
	document.getElementById('iubmbFlag-'+index).value = target.checked;
}
