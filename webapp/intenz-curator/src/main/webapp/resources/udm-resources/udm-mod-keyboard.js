// UDMv4.2 //
/***************************************************************\

  ULTIMATE DROP DOWN MENU Version 4.2 by Brothercake
  http://www.udm4.com/
  
  This script may not be used or distributed without license

\***************************************************************/
function umKM(){um.kbm=this;um.ha=0;um.mt=[um.e[10],um.e[11]];um.tf=null;if(um.m.cookie){um.rf=[um.gd('hotkeySelector'),um.gd('modifierSelector')];um.oe=um.m.cookie.split(';');um.ol=um.oe.length;i=0;do{if(/udmKeyPrefs/.test(um.oe[i])){um.tk=um.oe[i].split('=');um.tk=um.tk[1].split(',');j=0;do{um.keys[j+4]=um.tk[j];if(um.rf[j]){um.zo=um.rf[j].options;um.zl=um.zo.length;var k=0;do{if(um.zo[k].value==um.tk[j]){um.zo[k].selected=1;break;}k++;}while(k<um.zl);}j++;}while(j<2);break;}i++;}while(i<um.ol);}};um.keyPrefs=function(){if(!(um.kb&&um.d)){alert('Sorry, this feature is not supported in your browser.');return false;}um.dd=new Date();um.dd.setTime(um.dd.getTime()+(365*24*60*60*1000));um.m.cookie='udmKeyPrefs=test; expires='+um.dd.toGMTString()+'; path=/';if(!um.m.cookie){alert('Sorry, your browser didn\'t accept the cookie.\nWe cannot save your settings.');}else{um.rf=[um.gd('hotkeySelector'),um.gd('modifierSelector')];i=0;do{um.keys[i+4]=um.rf[i].options[um.rf[i].options.selectedIndex].value;i++;}while(i<2);um.m.cookie='udmKeyPrefs='+um.keys[4]+','+um.keys[5]+'; expires='+um.dd.toGMTString()+'; path=/';alert('Save successful!');}return true;};umKM.prototype.bdh=function(){if(typeof document.addEventListener!=um.un){document.addEventListener('keypress',this.kha,0);}else{document.attachEvent('onkeydown',this.kha);}};umKM.prototype.bth=function(umI){umI.onfocus=function(e){if(!um.lf){this.over(1,e.target);}};};umKM.prototype.cu=function(umM,umI,umTG){um.av=[null,null,null];if((umM!=null&&umM.style.visibility!='visible')||umM==null){if(umI.previousSibling!=null){um.av[0]=umI.previousSibling;}if(umI.nextSibling!=null){um.av[1]=umI.nextSibling;}}umM=(um.gu(um.gp(umTG)).length>0)?um.gu(um.gp(umTG))[0]:null;if(umM!=null&&typeof umM.style!=um.un&&umM.style.visibility=='visible'){um.ct=umM.getElementsByTagName('li');um.ctl=um.ct.length;um.c=2;j=0;do{um.av[um.c++]=um.ct[j++];}while(j<um.ctl);}if(um.tf!=null){um.lc=um.gp(um.tf).parentNode.lastChild;if(um.gp(um.tf)==um.lc){um.n.lr(um.gc(um.lc),0);}}um.avl=um.av.length;i=0;do{if(um.av[i]!=null){if(um.gu(um.av[i]).length>0){um.n.cp(um.gu(um.av[i])[0],um.av[i]);}else{um.n.cp(null,um.av[i]);}}i++;}while(i<um.avl);};umKM.prototype.cdl=function(udmLink){udmLink.href='#';udmLink.style.cursor='default';udmLink.onmousedown=function(){return false;};udmLink.onclick=function(){return false;};};umKM.prototype.mkc=function(udmKey){for(i=1;i<4;i+=2){if(udmKey==um.keys[i]){udmKey=um.keys[4-i];break;}}return udmKey;};umKM.prototype.kha=function(e){if(!e){e=window.event;}um.key=e.keyCode;if(um.key==um.keys[6]){um.ha=1;}if((um.key==um.keys[4] &&((um.keys[5]=='none'&&!e.shiftKey&&!e.ctrlKey&&!e.altKey)||eval('e.'+um.keys[5])))||(um.key==um.keys[6])){um.e[10]=1;um.e[11]=1;if(!um.ha){um.cm(e);um.t=umTree.getElementsByTagName('li')[0];um.t=um.gc(um.t);while(um.t.href==''){um.kbm.cdl(um.t);}um.t.focus();um.ha=1;um.ap('080',um.tr);if(um.ie){return false;}else if(e){e.preventDefault();}}else{if(um.sp){um.sapi.voice.Speak(um.vocab[8],2);}um.cm(e);if(um.wie50&&um.e[13]=='yes'){um.n.ts('visible');}eval(um.keys[7]).focus();um.e[10]=um.mt[0];um.e[11]=um.mt[1];um.ha=0;um.ap('090',um.tr);if(um.ie){return false;}else if(e){e.preventDefault();}}}um.tg=(e.target)?e.target:e.srcElement;if(um.tr.contains(um.tg)){um.e[10]=1;um.e[11]=1;if(um.h&&um.gp(um.tg).parentNode.className=='udm'){if(um.nm&&(um.key==um.keys[0]||um.key==um.keys[2])){return false;}i=0;do{if(um.key==um.keys[i]){um.key=um.keys[3-i];break;}i++;}while(i<4);}else{if(um.nm&&(um.key==um.keys[1]||um.key==um.keys[3])){return false;}um.t=um.gp(um.tg).parentNode;if(um.a||um.e[12]=='yes'){if(um.gu(um.gp(um.tg))[0]){um.xm=um.gu(um.gp(um.tg))[0];if(um.getRealPosition(um.xm,'x')<um.getRealPosition(um.t,'x')){um.key=um.kbm.mkc(um.key);}}else if(um.t.className!='udm'){um.pm=um.gp(um.t).parentNode;if(um.getRealPosition(um.pm,'x')>um.getRealPosition(um.t,'x')){um.key=um.kbm.mkc(um.key);}}}}um.tf=null;switch(um.key){case 9 :i=0;do{if(um.li[i]==um.gp(um.tg)){um.tf=um.tg;if(e.shiftKey){um.j=(i==0)?-1:i-1;}else{um.j=((i+1)==um.ll)?-1:i+1;}if(um.j>-1){um.t=um.gc(um.li[um.j]);if(um.t.href==null||um.t.href==''){um.kbm.cdl(um.t);}}else{um.cm(e);}break;}i++;}while(i<um.ll);break;case um.keys[0] :if(um.gp(um.tg).previousSibling){um.fo=um.gp(um.tg).previousSibling;if(um.fo!=null){um.t=um.gc(um.fo);if(um.ie&&um.t.href==''){um.kbm.cdl(um.t);}um.nf=(typeof um.t!=um.un)?um.t:null;if(um.nf!=null){um.nf.focus();}}}else if(um.gp(um.tg).parentNode.childNodes.length>1){um.n.cp(um.gu(um.gp(um.tg))[0],um.gp(um.tg));um.t=um.gc(um.gp(um.tg).parentNode.lastChild);if(um.ie&&um.t.href==''){um.kbm.cdl(um.t);}um.nf=(um.gp(um.tg).parentNode.className!='udm');if(um.nf&&um.h&&um.gp(um.gp(um.tg).parentNode).parentNode.className=='udm'){um.t=um.gc(um.gp(um.gp(um.tg).parentNode));}um.t.focus();}um.ap('100',um.tg);if(um.ie){return false;}else if(e){e.preventDefault();}break;case um.keys[1] :if(um.gu(um.gp(um.tg))[0]){um.t=um.gu(um.gp(um.tg))[0];um.nf=(um.t)?um.gc(um.t):null;if(um.nf!=null){um.nf.focus();}}um.ap('101',um.tg);if(um.ie){return false;}else if(e){e.preventDefault();}break;case um.keys[2] :if(um.gp(um.tg).nextSibling){um.fo=um.gp(um.tg).nextSibling;if(um.fo!=null){um.t=um.gc(um.fo);if(um.ie&&um.t.href==''){um.kbm.cdl(um.t);}um.nf=(typeof um.t!=um.un)?um.t:null;if(um.nf!=null){um.nf.focus();}}}else if(um.gp(um.tg).parentNode.childNodes.length>1){um.n.cp(um.gu(um.gp(um.tg))[0],um.gp(um.tg));um.t=um.gp(um.tg).parentNode.firstChild;if(um.gc(um.t).href==''){um.kbm.cdl(um.t);}um.gc(um.t).focus();}um.ap('102',um.tg);if(um.ie){return false;}else if(e){e.preventDefault();}break;case um.keys[3] :if(um.gp(um.tg).parentNode.parentNode){um.t=um.gp(um.tg).parentNode;um.nf=(um.t.className=='udm')?null:um.gc(um.gp(um.t));if(um.nf!=null&&(typeof um.nf.focus=='function'||typeof um.nf.focus=='object')){um.nf.focus();}}um.ap('103',um.tg);if(um.ie){return false;}else if(e){e.preventDefault();}break;}}return true;};