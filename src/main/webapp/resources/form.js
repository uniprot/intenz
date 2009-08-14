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
  if(databaseSource == "BRENDA") setBrendaLinkFields(url, ac, name);
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
	setPredefinedLinkFields(url, ac, name);
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

  document.enzymeDTO.elements[url].value = "n/a";
  document.enzymeDTO.elements[url].disabled = true;
  document.enzymeDTO.elements[url].style.backgroundColor = "#dbdbdb";
  document.enzymeDTO.elements[ac].value = "";
  document.enzymeDTO.elements[ac].disabled = false;
  document.enzymeDTO.elements[ac].style.backgroundColor = "#ffffff";
  document.enzymeDTO.elements[name].value = "n/a";
  document.enzymeDTO.elements[name].disabled = true;
  document.enzymeDTO.elements[name].style.backgroundColor = "#dbdbdb";
}


function setGoLinkFields(url, ac, name) {

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

/* Opens a prompt dialog to edit the comment associated to a data item.
 * Depending on the resulting comment (being empty or not) the appearance
 * of the referring link changes.
 * @param elemName The name of the commented element
 */
function editDataComment(elemName){
    var comment = document.enzymeDTO.elements[elemName+'.dataComment.xmlComment'].value;
    var dbName = document.enzymeDTO.elements[elemName+'.databaseName'].value;
    var accession = document.enzymeDTO.elements[elemName+'.accession'].value;
    var promptText = "Link to " + dbName;
    if (accession != null && accession != '') promptText += '\nAccession # ' + accession;
    promptText += '\nComment:';
    var newComment = window.prompt(promptText, comment);

    if (newComment != null){
        document.enzymeDTO.elements[elemName+'.dataComment.xmlComment'].value = trim(newComment);
    }
    updateDataCommentEditLink(elemName);
}

/** Updates the appearance of the link to the comment mini-editor */
function updateDataCommentEditLink(elemName){
    var link = document.getElementById(elemName+'.dataComment.xmlComment.edit');
    var comment = document.enzymeDTO.elements[elemName+'.dataComment.xmlComment'].value;
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