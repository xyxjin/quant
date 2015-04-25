// JavaScript Document
//----------- 左侧导航 ---------------

   //------------- 一级导航打开折叠 ------------
//var formerObj = document.getElementById("root_hsgs");
//var formerSid = document.getElementById("folder_hsgs");
var formerObj;
var formerSid;
function showFirstNav(obj,sId)
{
	if(obj != formerObj)
	{
       
    var sId=document.getElementById("folder_"+sId);
    
    if(obj!=formerObj) closeTag(formerObj,formerSid);
    
    formerObj=obj;
    formerSid=sId;
    
    if(sId.className=="hexunHidden")
    {
    	  sId.className="hexunShow";
    	  obj.style.background="#778A98 url(img/hq06.gif) no-repeat 11px 6px"//调整一级导航小三角状态_向下
    }else
    {
    	  sId.className="hexunHidden";
    	  obj.style.background="#778A98 url(img/hq05.gif) no-repeat 11px 6px"//调整一级导航小三角状态_向右
    }
  }
}
function closeTag(formerObj,formerSid)
{
    if(formerObj)
        {
			formerSid.className="hexunHidden"
			formerObj.style.background="#778A98 url(img/hq05.gif) no-repeat 11px 6px"//调整一级导航小三角状态_向右
        }
}
     //----------------- 二级导航打开折叠 ------------------
function showSub(ee,subId)
{   
    var	subId=document.getElementById("folder_"+subId);
	  if(subId.className=="hexunHidden")
	    {
	      subId.className="hexunShow";
		    ee.style.background="#DBE2E8 url(img/hq09.gif) no-repeat 21px 6px"//调整二级导航小三角状态_向下
			}
	 else
	   {
	       subId.className="hexunHidden";
		     ee.style.background="#DBE2E8 url(img/hq08.gif) no-repeat 21px 6px"//调整二级导航小三角状态_向右
		 }
} 

var url = window.location.href;

var p = url.substring(url.indexOf("#")+1,url.length);

if(url.indexOf("#") > 0)
{
    p=p.toLowerCase();
    
    
	if(p == "stock")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/default.aspx";
	}
	else if(p == "hkstock"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "hkstock/default.html";
	}
	else if(p == "ggt"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "hkstock/ggtStock.aspx";
	}
	else if(p == "global")
	{
		showFirstNav(document.getElementById("root_quanqiu"),'quanqiu');
		document.getElementById("ifrName").src = "global/default.html";
		document.getElementById("ifrName").style.height = "1650px";
	}else if (p == "russell")
	{
		showFirstNav(document.getElementById("root_quanqiu"),'quanqiu');
		document.getElementById("ifrName").src = "global/Russell.html";
		document.getElementById("ifrName").style.height = "1650px";
	}
	
	else if(p == "warrant")
	{
		showFirstNav(document.getElementById("root_quanzheng"),'quanzheng');
		document.getElementById("ifrName").src = "warrant/default.html";
	}else if(p == "fund"){
		showFirstNav(document.getElementById("root_jijin"),'jijin');
		document.getElementById("ifrName").src = "fund/default.aspx";
	}else if(p == "futures"){
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		document.getElementById("ifrName").src = "futures/default.aspx";
	}else if(p == "forex"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").src = "forex/default.aspx";
	}else if(p == "rmbhl"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/rmbhl.html";
		document.getElementById("ifrName").style.height = "3400px";
	}
	else if(p == "zgbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").style.height = "1650px";
		showSub(document.getElementById("hwhpj"), "whpj");
		
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=1";
		
	}
	else if(p == "gsbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=2";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
	else if(p == "nybank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=3";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
	else if(p == "zsbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=5";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
		else if(p == "jtbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=6";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
		else if(p == "gdbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=7";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
		else if(p == "pfbank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=8";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
		else if(p == "xybank"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		showSub(document.getElementById("hwhpj"), "whpj");
		document.getElementById("ifrName").src = "forex/ForexBank.aspx?type=9";
		document.getElementById("ifrName").style.height = "1200px";
		
	}
	
	else if(p == "jbforex"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").src = "forex/forex.aspx?type=1";
	}

	else if(p == "jcforex"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").src = "forex/forex.aspx?type=2";
	}
	else if(p == "syforex"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").src = "forex/forex.aspx?type=3";
	}
	else if(p == "rmbforex"){
		showFirstNav(document.getElementById("root_huilv"),'huilv');
		document.getElementById("ifrName").src = "forex/rmbhl.htm";
		
				document.getElementById("ifrName").style.height = "2550px";
		
			
	}
	else if(p == "bond"){
		showFirstNav(document.getElementById("root_zhaiquan"),'zhaiquan');
		document.getElementById("ifrName").src = "bond/default.aspx";
	}else if(p == "gold"){
		showFirstNav(document.getElementById("root_hangjin"),'hangjin');
		document.getElementById("ifrName").src = "gold/default.aspx";
	}
	else if(p == "btb")
	{
	    showFirstNav(document.getElementById("root_btb"),'btb');
	    document.getElementById("ifrName").src = "http://data.iof.hexun.com/iframe/btb.html";
	}
	else if(p == "xianhuo")
	{
		showFirstNav(document.getElementById("root_xianhuo"),'xianhuo');
		document.getElementById("ifrName").src = "futures/bohai.aspx?market=15&type=all&name=全部";
	}
	else if(p == "njs")
	{
		showFirstNav(document.getElementById("root_xianhuo"),'xianhuo');
		showSub(document.getElementById("njs"), "njs");
		document.getElementById("folder_bhspjys").className = "hexunHidden";
	
		document.getElementById("ifrName").src = "futures/njs.aspx";
	}
	else if(p == "dalian")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		document.getElementById("ifrName").src = "futures/futures.aspx?market=2&type=all";
	}
	
	else if(p == "zhengzhou")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hzzspjys"), "zzspjys");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=3&type=all";
	}
	
	else if(p == "shanghai")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hshspjys"), "shspjys");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=1&type=all";
	}
	else if(p == "shanghai_ag")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hshspjys"), "shspjys");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=1&type=ag";
	}
	else if(p == "zs_sr")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hshspjys"), "zzspjys");
		document.getElementById("folder_zzspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=3&type=sr";
	}
	else if(p == "zhongjin")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hzgjrqhjys"), "zgjrqhjys");
		document.getElementById("folder_hxqhzs").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=9&type=all";
	}
	else if(p == "guozhai")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hzgjrqhjys"), "zgjrqhjys");
		document.getElementById("folder_hxqhzs").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/GZFutures.aspx";
	}
	else if(p == "phcci")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("bdhf"), "bdhf");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/Phcci.aspx";
	}
	
	
	else if(p == "pgfcci")
	{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("bdghfb"), "bdghfb");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/pgfcci.aspx";
	}
	else if(p == "bdghla100")
	{
		showFirstNav(document.getElementById("root_quanqiu"),'quanqiu');
		showSub(document.getElementById("bd2ghfb"), "bd2ghfb"); 
		document.getElementById("ifrName").src = "global/FuBang.html";
	}
	
	else if(p == "300nengyuan")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1001&name=300能源&c=399908&m=2";
	}
	else if(p == "300cailiao")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1002&name=300材料&c=000909&m=1";
	}
	else if(p == "300gongye")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1003&name=300工业&c=000910&m=1";
	}
	else if(p == "300kexuan")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1004&name=300可选&c=000911&m=1";
	}
	else if(p == "300xiaofei")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1005&name=300消费&c=000912&m=1";
	}
	else if(p == "300yiyao")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1006&name=300医药&c=399913&m=2";
	}
	else if(p == "300jinrong")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1007&name=300金融&c=000914&m=1";
	}
	else if(p == "300xinxi")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1008&name=300信息&c=399915&m=2";
	}
	else if(p == "300dianxin")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1009&name=300电信&c=399916&m=2";
	}
	else if(p == "300gongyong")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y1010&name=300公用&c=399917&m=2";
	}
		else if(p == "50sz")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/index.aspx?code=Y0194&name=%BB%A6%CA%D050%D6%B8%CA%FD&c=000016&m=1";
	}
		else if(p == "50option")
	{
		showFirstNav(document.getElementById("root_option"),'option');
		document.getElementById("ifrName").src = "option/50_option.aspx";
	}
		else if(p == "50option1")
	{
		showFirstNav(document.getElementById("root_option"),'option');
		document.getElementById("ifrName").src = "option/50_option.aspx?type=1";
	}
		else if(p == "50option2")
	{
		showFirstNav(document.getElementById("root_option"),'option');
		document.getElementById("ifrName").src = "option/50_option.aspx?type=2";
	}
	else if(p == "wpfutures"){
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		document.getElementById("ifrName").src = "wpfutures/default.aspx";
	}
	
	
	else if(p == "zjgqhjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=50";
	}
	
	else if(p == "nyspjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hnyspjys"), "nyspjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=51";
	}
	
	else if(p == "rbdjgypjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hrbdjgypjys"), "rbdjgypjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=53";
	}
	
	else if(p == "ldjsjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hldjsjys"), "ldjsjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=54";
	}
	
	else if(p == "mlxyyspjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hmlxyyspjys"), "mlxyyspjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=55";
	}
	
	else if(p == "nyqhjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hnyqhjys"), "nyqhjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=56";
	}
	
	else if(p == "nysyjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hnysyjys"), "nysyjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=57";
	}
		else if(p == "zjjys")
	{
		showFirstNav(document.getElementById("root_wpqihuo"),'wpqihuo');
		showSub(document.getElementById("hzjjys"), "zjjys");
		document.getElementById("folder_zjgspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "wpfutures/wpfutures.aspx?market=58";
	}
	
	else if(p == "xfd")
	{
		
		showFirstNav(document.getElementById("root_xianhuo"),'xianhuo');
		showSub(document.getElementById("xfd"), "xfd");
		document.getElementById("folder_bhspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/xfd.aspx?classid=0";
	}
	else if(p == "hqb")
	{
		
		showFirstNav(document.getElementById("root_xianhuo"),'xianhuo');
		showSub(document.getElementById("jgzs"), "jgzs");
		document.getElementById("folder_bhspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/PriceInde.aspx";
	}
		else if(p == "sys")
	{
		
		showFirstNav(document.getElementById("root_xianhuo"),'xianhuo');
		showSub(document.getElementById("sys"), "sys");
		document.getElementById("folder_bhspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/sys.aspx";
	}
	
	
	else if(p.toUpperCase().indexOf("CBLOCK") >= 0)
	{	
		// 根元素
		var objRoot = document.getElementById("root_hsgs");
		
		// 打开第一级
		showFirstNav(objRoot,'hsgs');
		
		var cblock = document.getElementById("ConceptionBlockLi");
		
		cblock.style.backgroundColor="#999999";
		
		var objLinks = cblock.getElementsByTagName("A");
		
		for(var i = 0; i < objLinks.length; i++)
		{
			var href = objLinks[i].href;

			var anchor = href.substring(href.indexOf("#") + 1, href.length);
			
			if(href.indexOf("#") > 0)
			{
				if(anchor == p)
				{
					var subURL = href.substring(0, href.indexOf("#"));
					
					document.getElementById("ifrName").src = subURL;
					
					break;
				}	
			}
		}
		
		//document.getElementById("ifrName").src = subURL;
	}
	
	
	
	else if (p =="hsgz_bzhy_a")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=A&name=农、林、牧、渔业";
	}
	else if (p=="hsgz_bzhy_b")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=B&name=采掘业";
	}
	else if (p=="hsgz_bzhy_c")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C&name=制造业";
	}
	
	
	else if (p=="hsgz_bzhy_c0")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C0&name=食品、饮料";
	}
	else if (p=="hsgz_bzhy_c1")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C1&name=纺织、服装、皮毛";
	}
	else if (p=="hsgz_bzhy_c2")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C2&name=木材、家具";
	}
	else if (p=="hsgz_bzhy_c3")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C3&name=造纸、印刷";
	}
	else if (p=="hsgz_bzhy_c4")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C4&name=石油、化学、塑胶、塑料";
	}
	else if (p=="hsgz_bzhy_c5")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C5&name=电子";
	}
	else if (p=="hsgz_bzhy_c6")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C6&name=金属、非金属";
	}
	else if (p=="hsgz_bzhy_c7")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C7&name=机械、设备、仪表";
	}
	else if (p=="hsgz_bzhy_c8")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C8&name=医药、生物制品";
	}
	else if (p=="hsgz_bzhy_c9")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=C9&name=其他制造业";
	}
	else if (p=="hsgz_bzhy_d")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=D&name=电力、热力、燃气及水生产和供应业";
	}
	else if (p=="hsgz_bzhy_e")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=E&name=建筑业";
	}
	else if (p=="hsgz_bzhy_f")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=F&name=批发和零售业";
	}
	else if (p=="hsgz_bzhy_g")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=G&name=交通运输、仓储和邮政业";
	}
	else if (p=="hsgz_bzhy_h")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=H&name=住宿和餐饮业";
	}
	else if (p=="hsgz_bzhy_i")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=I&name=信息传输、软件和信息技术服务业";
	}
	else if (p=="hsgz_bzhy_j")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=J&name=金融业";
	}
	else if (p=="hsgz_bzhy_k")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=K&name=房地产业";
	}
	else if (p=="hsgz_bzhy_l")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=L&name=租赁和商务服务业";
	}
	else if (p=="hsgz_bzhy_m")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=M&name=科学研究和技术服务业";
	}
	else if (p=="hsgz_bzhy_n")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=N&name=水利、环境和公共设施管理业";
	}
	else if (p=="hsgz_bzhy_o")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=O&name=居民服务、修理和其他服务业";
	}
	else if (p=="hsgz_bzhy_p")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=P&name=教育";
	}
	else if (p=="hsgz_bzhy_q")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=Q&name=卫生和社会工作";
	}
	else if (p=="hsgz_bzhy_r")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=R&name=文化、体育和娱乐业";
	}
	else if (p=="hsgz_bzhy_s")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/industry.aspx?code=S&name=综合";
	}
	
	
	else if (p=="hsgz_icb_0")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=0&name=石油与天然气";
	}
	else if (p=="hsgz_icb_1")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=1&name=基础材料";
	}
	else if (p=="hsgz_icb_2")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=2&name=工业";
	}
	else if (p=="hsgz_icb_3")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=3&name=消费品";
	}
	else if (p=="hsgz_icb_4")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=4&name=医疗保健";
	}
	else if (p=="hsgz_icb_5")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=5&name=消费服务";
	}
	else if (p=="hsgz_icb_6")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=6&name=通信";
	}
	else if (p=="hsgz_icb_7")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=7&name=公用事业";
	}
	else if (p=="hsgz_icb_8")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=8&name=金融业";
	}
	else if (p=="hsgz_icb_9")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/icb.aspx?code=9&name=科技";
	}
	 
	
	else if (p=="hsgz_dybk_301000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=301000000&name=北京板块";
	}
	else if (p=="hsgz_dybk_302000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=302000000&name=上海板块";
	}
	else if (p=="hsgz_dybk_303000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=303000000&name=天津板块";
	}
	else if (p=="hsgz_dybk_304000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=304000000&name=广东板块";
	}
	else if (p=="hsgz_dybk_305000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=305000000&name=重庆板块";
	}
	else if (p=="hsgz_dybk_306000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=306000000&name=黑龙江板块";
	}
	else if (p=="hsgz_dybk_307000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=307000000&name=吉林板块";
	}
	else if (p=="hsgz_dybk_308000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=308000000&name=辽宁板块";
	}
	else if (p=="hsgz_dybk_309000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=309000000&name=河北板块";
	}
	else if (p=="hsgz_dybk_310000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=310000000&name=河南板块";
	}
	else if (p=="hsgz_dybk_311000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=311000000&name=山东板块";
	}
	else if (p=="hsgz_dybk_312000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=312000000&name=山西板块";
	}
	else if (p=="hsgz_dybk_313000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=313000000&name=陕西板块";
	}
	else if (p=="hsgz_dybk_314000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=314000000&name=宁夏板块";
	}
	else if (p=="hsgz_dybk_315000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=315000000&name=甘肃板块";
	}
	else if (p=="hsgz_dybk_316000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=316000000&name=新疆板块";
	}
	else if (p=="hsgz_dybk_317000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=317000000&name=内蒙古板块";
	}
	else if (p=="hsgz_dybk_318000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=318000000&name=西藏板块";
	}
	else if (p=="hsgz_dybk_319000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=319000000&name=四川板块";
	}
	else if (p=="hsgz_dybk_320000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=320000000&name=云南板块";
	}
	else if (p=="hsgz_dybk_321000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=321000000&name=贵州板块";
	}
	else if (p=="hsgz_dybk_322000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=322000000&name=广西板块";
	}
	else if (p=="hsgz_dybk_323000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=323000000&name=海南板块";
	}
	else if (p=="hsgz_dybk_324000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=324000000&name=湖北板块";
	}
	else if (p=="hsgz_dybk_325000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=325000000&name=湖南板块";
	}
	else if (p=="hsgz_dybk_326000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=326000000&name=江西板块";
	}
	else if (p=="hsgz_dybk_327000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=327000000&name=江苏板块";
	}
	else if (p=="hsgz_dybk_328000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=328000000&name=浙江板块";
	}
	else if (p=="hsgz_dybk_329000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=329000000&name=安徽板块";
	}
	else if (p=="hsgz_dybk_330000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=330000000&name=福建板块";
	}
	else if (p=="hsgz_dybk_331000000")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/zone.aspx?code=331000000&name=青海板块";
	}
	else if(p == "bkgn_up")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/rank.aspx?type=3&updown=up";
	}
	else if(p == "bkgn_down")
	{
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/rank.aspx?type=3&updown=down";
	}
	else if (p=="hgt")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "stock/hgtStock.aspx";
	}
	else if (p=="ustock_1")
	{
	    showFirstNav(document.getElementById("root_usags"),'usags');
		document.getElementById("ifrName").src = "usastock/xqStock.aspx?market=1";
	}
	else if (p=="ustock_2")
	{
	    showFirstNav(document.getElementById("root_usags"),'usags');
		document.getElementById("ifrName").src = "usastock/xqStock.aspx?market=2";
	}
		else if (p=="ustock_3")
	{
	    showFirstNav(document.getElementById("root_usags"),'usags');
		document.getElementById("ifrName").src = "usastock/xqStock.aspx?market=3";
	}
		else if (p=="ustock_0")
	{

	
	    showFirstNav(document.getElementById("root_usags"),'usags');
		document.getElementById("ifrName").src = "usastock/usastock.aspx";
		document.getElementById("ifrName").style.height = "1200px"; 
	}
	else if (p.indexOf("hsgz_gnbk")==0)         //概念板块的定位
	{
	
	    var items=p.split('|');
	    if(items.length>=3)
	    {
	   
	       showFirstNav(document.getElementById("root_hsgs"),'hsgs'); 
	        
		   document.getElementById("ifrName").src = "stock/conseption.aspx?code="+items[1]+"&name="+items[2];
		  
	    }
	} 
	
	else if (p.indexOf("usags")==0)         //概念板块的定位
	{

	    var items=p.split('|');
	    	
	    if(items.length>=3)
	    {
	       showFirstNav(document.getElementById("root_usags"),'usags');
		   document.getElementById("ifrName").src = "usastock/bk_stock.aspx?code="+items[1]+"&name="+decodeURIComponent(items[2]); 
		   
	    }
	} 
	
	
	else if(p == "hkstock_100"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=100";
	}
	else if(p == "hkstock_100_up"){
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=100&updown=up";
		showFirstNav(document.getElementById("root_xggs"),'xggs');}
	else if(p == "hkstock_100_down"){
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=100&updown=down";
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		}
		
	
	else if(p == "hkstock_0"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=0";
	}
	else if(p == "hkstock_1"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=1";
	}
	else if(p == "hkstock_2"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=2";
	}
	else if(p == "hkstock_3"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=3";
	}
	else if(p == "hkstock_5"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=5";
	}
	else if(p == "hkstock_ah"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hk_ah.html";
	}
	else if(p == "hkstock_index"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hk_index.html";
	}
	else if(p == "hkstock_hs_1"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock_zs.aspx?type=1&name=恒生指数成分股";
	}
	else if(p == "hkstock_hs_2"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock_zs.aspx?type=2&name=红筹指数成分股";
	}
	else if(p == "hkstock_hs_3"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock_zs.aspx?type=3&name=国企指数成分股";
	}
		else if(p == "hkstock_hs_4"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/conseption.aspx?type=702&name=%C8%ED%BC%FE%B7%FE%CE%F1";
	}
	else if(p == "hkstock_hs_5"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/conseption.aspx?type=701&name=%D7%CA%D1%B6%BF%C6%BC%BC%C6%F7%B2%C4";
	}
	else if (p.indexOf("hkstock_hy")==0)         //行业分类
	{
	    var items=p.split('|');
	    if(items.length>=3)
	    {
	       showFirstNav(document.getElementById("root_xggs"),'hsgs');
		   document.getElementById("ifrName").src = "hkstock/conseption.aspx?703="+items[1]+"&name="+decodeURIComponent(items[2]); 
	    }
	} 
	else if(p == "hkstock_6"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=6";
	}
	else if(p == "hkstock_7"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=7";
	}
	else if(p == "hkstock_8"){
		showFirstNav(document.getElementById("root_xggs"),'xggs');
		document.getElementById("ifrName").src = "/hkstock/hkstock.aspx?type=8";
	}
	
	
	else if (p=="hsgs_hsbk")
	{
	    showFirstNav(document.getElementById("root_hsgs"),'hsgs');
		document.getElementById("ifrName").src = "/stock/stock.aspx?type=2&market=0";
	}
	else
	{
	 
	
       var arr = new Array();
		arr = p.split( '_');
		if(arr[0]=='1')
		{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hshspjys"), "shspjys");
		document.getElementById("folder_dlspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=1&type="+arr[1];
		}
		else  if(arr[0]=='2')
		{
		showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hdlspjys"), "dlspjys");
		document.getElementById("folder_zzspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=2&type="+arr[1];
		}
        else if(arr[0]=='3')
        {
          showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hshspjys"), "zzspjys");
		document.getElementById("folder_zgjrqhjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=3&type="+arr[1];
        }
         else if(arr[0]=='9')
        {
          showFirstNav(document.getElementById("root_qihuo"),'qihuo');
		showSub(document.getElementById("hzgjrqhjys"), "zgjrqhjys");
		document.getElementById("folder_shspjys").className = "hexunHidden";
		document.getElementById("ifrName").src = "futures/futures.aspx?market=9&type=all";
        }
		
		
		
	}
	
	
	
	
}else{

          
	if(document.getElementById("ifrName") != null){
		showFirstNav(document.getElementById("root_hsgs"),'hsgs');	
		document.getElementById("ifrName").src = "stock/default.aspx";
	}
}

//-------- iframe自适应 -----------
function TuneHeight(s,h){ 
				var frm = document.getElementById("ifrName");
				if(frm != null) {frm.style.height=h+"px";frm.src=s;} 
}
function IframeOpen(s){
				var frm = document.getElementById("ifrName");
				if(frm != null) {frm.src=s;} 
}
			
//香港主板
function SwitchHKTags(id,num,count)
{
	ClearHKTagClass(id,count);
	document.getElementById("tagname_" + id + num).className = "on";
	document.getElementById(id + num).className = "hexunShow";
}
function ClearHKTagClass(id,count)
{
	for(i=1;i<=count;i++)
	{
		document.getElementById("tagname_" + id + i).className = "out";
		document.getElementById(id + i).className = "hexunHidden";
	}
}
//黄金
function SwitchWyTags(id,num,count,thisColor,otherColor)
{
	ClearWyTagClass(id,count,otherColor);
	document.getElementById("tagname_" + id + num).className = thisColor;
	document.getElementById(id + num).className = "hexunShow";
	Common.Resize();
}
function ClearWyTagClass(id,count,otherColor)
{
	for(i=1;i<=count;i++)
	{
		document.getElementById("tagname_" + id + i).className =otherColor;
		document.getElementById(id + i).className = "hexunHidden";
	}
}
