// JavaScript Document
function hxtoTop(){
	var brower = navigator.userAgent.toLowerCase();
  	var strongIE = (HX.$B.IE && (brower.indexOf('se')!=-1 || brower.indexOf('maxthon')!=-1 || brower.indexOf('tencent')!=-1));
	var right = 0;
  	if(HX.$('foot2010')) {
		var obj = HX.$('foot2010');
		right = (HX.$E.left(obj)<=21)?0:(HX.$E.left(obj)-21);
		if(right!=0 && HX.$B.IE && !strongIE) right-=2;
		}	 
	 var fragment = document.createDocumentFragment();
	 var con = HX.$E.create('div',fragment);
	 con.style.right = right+'px';
	 con.id = 'hx_toTop';
	 con.innerHTML = '·µ»Ø¶¥²¿';
	 document.body.appendChild(fragment);
	 HX.$V.bind(window,'resize',function(){
	 	var right = 0;
  		if(HX.$('foot2010')) {
			var obj = HX.$('foot2010');
			right = (HX.$E.left(obj)<=21)?0:(HX.$E.left(obj)-21);
			if(right!=0 && HX.$B.IE && !strongIE) right-=2;
		}
		con.style.right = right+'px';
		});
	 set();
	window.onscroll=set;
	con.onclick=function (){
		con.style.display = 'none';
		window.onscroll=null;
		this.timer=setInterval(function(){
			var top = document.body.scrollTop||document.documentElement.scrollTop;
			if(document.documentElement.scrollTop)
			document.documentElement.scrollTop-=Math.ceil(top*0.1);
			else document.body.scrollTop-=Math.ceil(top*0.1);
			if(top==0) clearInterval(con.timer,window.onscroll=set);
		},10);
	};
	function set(){
		var top = document.body.scrollTop||document.documentElement.scrollTop;
		con.style.display=top>0?'block':"none";
		}
	}