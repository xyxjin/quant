(function(globalName,version){if(window[globalName]){return}var doc=document;function getCookie(name){name=encodeURIComponent(name);var cookies=doc.cookie?doc.cookie.split("; "):[];for(var i=0,l=cookies.length;i<l;i++){var parts=cookies[i].split("=");if(parts.shift()===name){return decodeURIComponent(parts.join("="))}}return""}function onClick(e){e=e||window.event;var tar=e.target||e.srcElement,boss;while(tar&&tar.getAttribute){boss=tar.getAttribute("boss");if(boss){if(boss.charAt(0)==="{"){send(window.eval("("+boss+")"),tar)}else{send({sPos:boss},tar)}}tar=tar.parentNode}}var seed=0;var attrMap={ip:"sIp",qq:"iQQ",biz:"sBiz",op:"sOp",sta:"iSta",ty:"BossId",id:"BossId",flow:"iFlow",url:"sUrl",ref:"sRef",pageid:"sPageId",pos:"sPos"};function getAttrs(cfg,tar){var target=true;while(tar&&tar.attributes){if(target||!tar.getAttribute("boss")){for(var n=0;n<tar.attributes.length;n++){var attr=tar.attributes[n];var name=attr.nodeName||attr.name;if(name.substr(0,5)==="boss-"){var cfgName=name.substr(5);cfgName=attrMap[cfgName]||cfgName;!cfg.hasOwnProperty(cfgName)&&(cfg[cfgName]=tar.getAttribute(name))}}}target=false;tar=tar.parentNode}}var send=window[globalName]=function(cfg,tar){var urls=["http://btrace.qq.com/kvcollect?"],n;getAttrs(cfg,tar);for(n in CFG){!cfg.hasOwnProperty(n)&&(cfg[n]=(typeof CFG[n]==="function"?CFG[n](cfg,tar):CFG[n]))}for(n in cfg){urls.push(n+"="+encodeURIComponent(cfg[n])+"&")}urls.push("_dc="+Math.random());var img=send["img"+(seed++)]=new Image(1,1);img.src=urls.join("")};var CFG=send.config={BossId:function(cfg,tar){return cfg.BossId||cfg.iTy||2669},CheckSum:0,iQQ:function(cfg,tar){return(getCookie("uin")||getCookie("luin")).replace(/o0*/,"")||window.pgvGetNewRand&&(window.pgvGetNewRand()||"").replace("&nrnd=","")||getCookie("o_cookie")||""},sBiz:"stock-anon",sOp:"click-anon"};send.version=version;if(doc.attachEvent){doc.attachEvent("onclick",onClick)}else{doc.addEventListener("click",onClick,false)}})("bossClick","0.2.1");(function(a,b){if(typeof define=="function"&&define.amd){define(b)}else{if(typeof exports==="object"){module.exports=b()}}})(this,function(){return bossClick});
/*  |xGv00|f1da7a82ea9b76063ab2678b591508c1 */