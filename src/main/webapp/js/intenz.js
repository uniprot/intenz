function fold(elementId, more, moreName, hideName) {
  var elementToFold = document.getElementById(elementId);
  var moreLink = document.getElementById(more);
  if (navigator.userAgent.indexOf("Netscape6") != -1)	{
    if (elementToFold.style.visibility=="visible") {
      elementToFold.style.visibility="hidden";
      moreLink.firstChild.nodeValue=moreName;
    }	else {
      elementToFold.style.visibility="visible";
      moreLink.firstChild.nodeValue=hideName;
    }
  } else {
    if (elementToFold.style.display=="") {
      elementToFold.style.display="none";
      moreLink.firstChild.nodeValue=moreName;
    } else {
      elementToFold.style.display="";
      moreLink.firstChild.nodeValue=hideName;
    }
  }
}

function update(buttonCmdValue, listType, index, action) {
  document.enzymeDTO.buttonCmd.value = buttonCmdValue;
  document.enzymeDTO.listType.value = listType;
  document.enzymeDTO.index.value = index;
  document.enzymeDTO.action = action;
  document.enzymeDTO.submit();
}

function formButtonAction(buttonCmdValue, listTypeValue, indexValue, actionValue) {
  document.enzymeDTO.buttonCmd.value = buttonCmdValue;
  document.enzymeDTO.listType.value = listTypeValue;
  document.enzymeDTO.index.value = indexValue;
  document.enzymeDTO.action = actionValue;
  document.forms[0].submit();
}

function fetch(form) {
  formButtonAction("fetch", "uniProtLinks", "-1", "formButton"+form+".do");
}

function pubmed(index, form) {
  formButtonAction("pubMedFetch", "pubMed", index, "formButton"+form+".do");
}

function submitt(action) {
  document.enzymeDTO.action = action;
}

function preloadImages() {
  var doc = document;
  if (doc.images) {
    if (!doc.imageArray) doc.imageArray = new Array();
    var index;
    var length = doc.imageArray.length;
    var args = preloadImages.arguments;
    for(index=0; index<args.length; index++) {
      if (args[index].indexOf("#") != 0) {
        doc.imageArray[length] = new Image;
        doc.imageArray[length++].src = args[index];
      }
    }
  }
}