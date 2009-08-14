function shade(){
    var modalMainPanel = document.getElementById("ModalMainPanel");
    if (modalMainPanel.className == 'hidden') modalMainPanel.className = '';
    else modalMainPanel.className = 'hidden';
}

/**
 * jt_DialogBox.js - DHTML modal dialog box
 *
 * @version 23 Aug 2007
 * @author	Joseph Oster, wingo.com, Copyright (c) 2005-2007
 * @license http://www.wingo.com/dialogbox/license.html
 */
/************ REQUIRES: 'jt_BodyZ' and 'jt_Veil' from 'jt_.js' ************/

jt_DialogBox = function(isModal) {
	// CONSTRUCTOR for 'jt_DialogBox' object
	if (arguments.length == 0) return;
	this.isModal = isModal;

	this.container = document.createElement('div');
	this.container.className = jt_DialogBox.className;
	this.container.dialogBox = this;

	var mainTable = document.createElement('table');
	mainTable.setAttribute('cellSpacing', '0');
	mainTable.setAttribute('cellPadding', '0');
	mainTable.setAttribute('border', '0');

	var tBodyM = document.createElement('tbody');
	var rowM = document.createElement('tr');
	var cellM = document.createElement('td');

	//*********** BEGIN Title TABLE ***********
	var titleTable = document.createElement('table');
	titleTable.setAttribute('cellSpacing', '0');
	titleTable.setAttribute('cellPadding', '0');
	titleTable.setAttribute('border', '0');
	titleTable.setAttribute('width', '100%');

	var tBodyT = document.createElement('tbody');
	var rowT = document.createElement('tr');
/*
	var cellT = document.createElement('td');
	cellT.className = "tbLeft";
	cellT.innerHTML = "&nbsp;";
	rowT.appendChild(cellT);
*/
	this.titleCell = document.createElement('td');
	this.titleCell.className = "Title";
	this.titleCell.ondblclick = shade;
	this.titleCell.setAttribute('title', 'Double-click to shade and easy move');
    rowT.appendChild(this.titleCell);
/*
	cellT = document.createElement('td');
	cellT.className = "tbRight";

	jt_DialogBox.initCloseIcon();
	var closeIcon = document.createElement('img');
	closeIcon.src = jt_DialogBox.closeIcon.src;
	closeIcon.setAttribute('border','0');
	closeIcon.dialogBox = this;

	var aLink = document.createElement('A');
	aLink.setAttribute('href','#');
	aLink.appendChild(closeIcon);
	aLink.onclick = jt_DialogBox.closeBox;

	cellT.appendChild(aLink);
	rowT.appendChild(cellT);
*/

	tBodyT.appendChild(rowT);
	titleTable.appendChild(tBodyT);
	//*********** END Title TABLE ***********

	cellM.appendChild(titleTable);
	rowM.appendChild(cellM);
	tBodyM.appendChild(rowM);

	rowM = document.createElement('tr');
    rowM.id = "ModalMainPanel";
	cellM = document.createElement('td');
	cellM.className = "MainPanel";

	this.contentArea = document.createElement('div');
	this.contentArea.className = "ContentArea";
	cellM.appendChild(this.contentArea);

	rowM.appendChild(cellM);
	tBodyM.appendChild(rowM);
	mainTable.appendChild(tBodyM);
	this.container.appendChild(mainTable);
	jt_BodyZ.toTop(this.container);

	Drag.init(this.titleCell, this.container, 0, null, 0);
	}


/************ BEGIN: Public Methods ************/
jt_DialogBox.imagePath = "../images/"; // set by application; directory path to 'window_close.gif' in titleBar

jt_DialogBox.prototype.show = function() {
	this.container.style.display = "block";
	jt_BodyZ.toTop(this.container);
	//jt_TraceObj.show(this.container);
	if (this.isModal) jt_Veil.show(true);
	jt_divOnScrn(this.container);
	}

jt_DialogBox.prototype.hide = function(ok) {
	this.container.style.display = "none";
	if (this.isModal) jt_Veil.show(false);
	if (ok) {
		if (this.callOK)
			if (this.returnData) this.callOK(this.returnData);
			else this.callOK();
		}
	else if (this.callCancel) this.callCancel();
	}

jt_DialogBox.prototype.moveTo = function(x, y) {
	if (x == -1) x = Math.round((document.body.clientWidth - this.container.offsetWidth) / 2);
	if (y == -1) y = Math.round((document.body.clientHeight - this.container.offsetHeight) / 2) + document.body.scrollTop;
	this.container.style.left = x + "px";
	this.container.style.top = y + "px";
	}

jt_DialogBox.prototype.setTitle = function(title) {
	this.titleCell.innerHTML = title;
	}

jt_DialogBox.prototype.setUrl = function(url, height) {
	// creates one IFRAME above 'setContent()' area, updates 'url'
	if (!this._jtDialogBIF) {
		this._jtDialogBIF = document.createElement('IFRAME');
		this._jtDialogBIF.setAttribute('frameBorder', 'no');
		this._jtDialogBIF.style.width = "100%";
		if (height) this._jtDialogBIF.style.height = height;
		this.contentArea.parentNode.insertBefore(this._jtDialogBIF, this.contentArea);
		}
	this._jtDialogBIF.src = url;
	}

jt_DialogBox.prototype.getUrl = function() {
	if (this._jtDialogBIF) {
		jt_TraceObj.show(this._jtDialogBIF);
		var url = this._jtDialogBIF.src;
		if (this._jtDialogBIF.contentWindow) {
			try {url = this._jtDialogBIF.contentWindow.location.href;}
			catch(e) {}
			}
		return url;
		}
	}

jt_DialogBox.prototype.setContent = function(htmlContent) {
	this.contentArea.innerHTML = htmlContent;
	}

jt_DialogBox.prototype.setWidth = function(width) {
	this.contentArea.style.width = width + "px";
	}

jt_DialogBox.prototype.setCallOK = function(callOK) {
	// set by application as needed
	this.callOK = callOK;
	}

jt_DialogBox.prototype.setCallCancel = function(callCancel) {
	// set by application as needed
	this.callCancel = callCancel;
	}

jt_DialogBox.prototype.getContentNode = function() {
	// expose 'contentArea' DOM node for direct manipulation
	return this.contentArea;
	}
/************ END: Public Methods ************/


/************ BEGIN: Private Methods ************/
jt_DialogBox.className = "jtDialogBox"; // CSS className
jt_DialogBox.closeIcon = null;
jt_DialogBox.maxDepth = 5; // optimize search of parent nodes

jt_DialogBox.initCloseIcon = function() {
	// pre-fetch this icon so it doesn't distort dialog box size
	if (jt_DialogBox.closeIcon == null) {
		jt_DialogBox.closeIcon = new Image();
		jt_DialogBox.closeIcon.src = jt_DialogBox.imagePath + "window_close.gif";
		}
	}

jt_DialogBox.closeBox = function(e) {
	if (!e) e = window.event;
	var node = e.target ? e.target : e.srcElement;
	var count = 0;
	while ((node != null) && (count < jt_DialogBox.maxDepth)) {
		if (node.dialogBox) {
			node.dialogBox.hide();
			return false;
			}
		node = node.parentNode;
		count++;
		}
	return false;
	}
