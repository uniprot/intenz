
function fold(elementId, more, moreName, hideName) {
  var expiresInDays = 30;
  var elementToFold = document.getElementById(elementId);
  var moreLink = document.getElementById(more);
  if (navigator.userAgent.indexOf("Netscape6") != -1)	{
    if (elementToFold.style.visibility=="visible") {
      elementToFold.style.visibility="hidden";
      moreLink.firstChild.nodeValue=moreName;
	  createCookie('intenz.public.hide.'+elementId, true, expiresInDays);
    }	else {
      elementToFold.style.visibility="visible";
      moreLink.firstChild.nodeValue=hideName;
	  createCookie('intenz.public.hide.'+elementId, false, expiresInDays);
    }
  } else {
    if (elementToFold.style.display=="") {
      elementToFold.style.display="none";
      moreLink.firstChild.nodeValue=moreName;
	  createCookie('intenz.public.hide.'+elementId, true, expiresInDays);
    } else {
      elementToFold.style.display="";
      moreLink.firstChild.nodeValue=hideName;
	  createCookie('intenz.public.hide.'+elementId, false, expiresInDays);
    }
  }
}

function toggleVisibility(elem){
	var theElem = document.getElementById(elem);
	var vis = null;
	if (window.getComputedStyle){ // W3C compliant
	  	vis = window.getComputedStyle(theElem, '').visibility;
	} else if (theElem.currentStyle){ // IE
		vis = theElem.currentStyle.visibility;
	}
  	if (vis == 'hidden'){
  		theElem.style.visibility = 'visible';
  	} else {
		theElem.style.visibility = 'hidden';
	}
}

/**
 * Hides/shows an element by clicking an icon. The icon's title (tooltip)
 * is updated to reflect its toggled expected behaviour.
 * Parameters:
 * 		elementId - the id of the element to hide/show
 *		iconId - the id of the icon element
 *		showTip - tooltip text
 *		hideTip - tooltip text
 */
function iconToggle(elementId, iconId, showTip, hideTip){
	var theElem = document.getElementById(elementId);
	var theIcon = document.getElementById(iconId);
	var expiresInDays = 30;
	if (theElem.style.display == ""){
		theElem.style.display = "none";
		theIcon.setAttribute("title", showTip);
		createCookie('intenz.public.hide.'+elementId, true, expiresInDays);
	} else {
		theElem.style.display = "";
		theIcon.setAttribute("title", hideTip);
		createCookie('intenz.public.hide.'+elementId, false, expiresInDays);
	}
}

function toTop() {
	self.scrollTo(0, 0);
}

function windowOpenWithSize(theurl, thewd, theht) {
  if(!(theht)) {
    theht = 250;
  }
  if(!(thewd)) {
    thewd = 250;
  }
  var newwin  = window.open(theurl,"labelsize","dependent=yes, resizable=yes,toolbar=no,menubar=no,scrollbars=yes,width="+thewd+",height="+theht);
  newwin.focus();
}

function hideShow(name){
	var hideShowCookie = readCookie('intenz.public.hide.' + name);
    if (hideShowCookie != null && hideShowCookie == 'true'){
		fold('cf', 'cf_switch', 'Show classification', 'Hide classification');
	}
}

// COOKIES:

function createCookie(name,value,days){
	if (days){
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/intenz";
}

function readCookie(name){
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++){
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function eraseCookie(name){
	createCookie(name,"",-1);
}

// FIREFOX SEARCH PLUGIN INSTALLER

function errorMsg(){
  alert("Netscape 6 or Mozilla is needed to install a sherlock plugin");
}

function addEngine(name,ext,cat,type){
  if ((typeof window.sidebar == "object") && (typeof window.sidebar.addSearchEngine == "function")){
    //cat="Web";
    //cat=prompt('In what category should this engine be installed?','Web')
    window.sidebar.addSearchEngine(
      "http://www.ebi.ac.uk/intenz/firefoxSearchPlugin/"+name+".src",
      "http://www.ebi.ac.uk/intenz/firefoxSearchPlugin/"+name+"."+ext,
      name,
      cat );
  } else {
    errorMsg();
  }
}
