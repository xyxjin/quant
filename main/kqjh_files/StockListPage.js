/* ----- */

StockListPage = new function(){
	this.dataArray;
	this.currentColumn;
	this.divName = "StockListPage";
	this.columnArray = new Array();
	this.reloadTime = 30000;
	this.reload = true;
	this.setTimeObj;
	this.GetData = function(array,total,time)
	{
		StockListRequest.totalpage = total;
		this.currentColumn = StockListRequest.sorttype;
		this.columnArray[this.currentColumn] = StockListRequest.updown;
		this.dataArray = array;
		this.ShowTime(time);
		this.ShowPage(StockListRequest.page,StockListRequest.totalpage);
		this.InitDataSort(this.currentColumn,StockListRequest.updown);
		this.LoadDataFinish();
	}
	this.LoadDataFinish = function()
	{
		var hc = new Array();
		hc.push("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"dou_table\">");
		hc.push("<tr><td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(0)\">代码" + this.Arrow(0)+ "</a></td>");
		hc.push("<td class=\"toptd\">名称</td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(2)\">最新价" + this.Arrow(2)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(3)\">涨跌幅" + this.Arrow(3)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(4)\">昨收" + this.Arrow(4)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(5)\">今开" + this.Arrow(5)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(6)\">最高" + this.Arrow(6)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(7)\">最低" + this.Arrow(7)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(8)\">成交量" + this.Arrow(8)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(9)\">成交额" + this.Arrow(9)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(10)\">换手" + this.Arrow(10)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(11)\">振幅" + this.Arrow(11)+ "</a></td>");
		hc.push("<td class=\"toptd\"><a href=\"javascript:StockListPage.DataSort(12)\">量比" + this.Arrow(12)+ "</a></td>");
		hc.push("<td class=\"toptd\">资金</td>");
		hc.push("<td class=\"toptdend\">股吧</td></tr>");
		for(var i=0;i < this.dataArray.length;i++){
			hc.push("<tr" + this.TrBgColor(i) + ">");
			hc.push("<td" + this.TdBgColor(i,0) + "><a href=\"http://stockdata.stock.hexun.com/" + this.dataArray[i][0] + ".shtml\" target=\"_blank\">" + this.dataArray[i][0] + "</a></td>");//代码
			hc.push("<td><a href=\"http://stockdata.stock.hexun.com/" + this.dataArray[i][0] + ".shtml\" target=\"_blank\">" + this.dataArray[i][1] + "</a></td>");//名称
			if(this.dataArray[i][2] == 0 && this.dataArray[i][8] == 0 && this.dataArray[i][9]==0){
				hc.push("<td" + this.TdBgColor(i,2) + ">--</td>");//最新价
				hc.push("<td" + this.TdBgColor(i,3) + ">--</td>");//涨跌幅
				hc.push("<td" + this.TdBgColor(i,4) + ">" + this.dataArray[i][4].toFixed(2) + "</td>");//昨收
				hc.push("<td" + this.TdBgColor(i,5) + ">--</td>");//今开
				hc.push("<td" + this.TdBgColor(i,6) + ">--</td>");//最高
				hc.push("<td" + this.TdBgColor(i,7) + ">--</td>");//最低
				hc.push("<td" + this.TdBgColor(i,8) + ">--</td>");//成交量
				hc.push("<td" + this.TdBgColor(i,9) + ">--</td>");//成交额
				hc.push("<td" + this.TdBgColor(i,10) + ">--</td>");//换手
				hc.push("<td" + this.TdBgColor(i,11) + ">--</td>");//振幅
				hc.push("<td" + this.TdBgColor(i,12) + ">--</td>");//量比
			}else{
				hc.push("<td" + this.TdBgColor(i,2) + ">" + Common.GetColor2DEC(this.dataArray[i][2],this.dataArray[i][4]) + "</td>");//最新价
				hc.push("<td" + this.TdBgColor(i,3) + ">" + Common.GetColor2DEC(this.dataArray[i][3],0) + "</td>");//涨跌幅
				hc.push("<td" + this.TdBgColor(i,4) + ">" + this.dataArray[i][4].toFixed(2) + "</td>");//昨收
				hc.push("<td" + this.TdBgColor(i,5) + ">" + Common.GetColor2DEC(this.dataArray[i][5],this.dataArray[i][4]) + "</td>");//今开
				hc.push("<td" + this.TdBgColor(i,6) + ">" + Common.GetColor2DEC(this.dataArray[i][6],this.dataArray[i][4]) + "</td>");//最高
				hc.push("<td" + this.TdBgColor(i,7) + ">" + Common.GetColor2DEC(this.dataArray[i][7],this.dataArray[i][4]) + "</td>");//最低
				hc.push("<td" + this.TdBgColor(i,8) + ">" + this.dataArray[i][8].toFixed(2) + "</td>");//成交量
				hc.push("<td" + this.TdBgColor(i,9) + ">" + this.dataArray[i][9] + "</td>");//成交额
				hc.push("<td" + this.TdBgColor(i,10) + ">" + this.dataArray[i][10].toFixed(2) + "</td>");//换手
				hc.push("<td" + this.TdBgColor(i,11) + ">" + this.dataArray[i][11].toFixed(2) + "</td>");//振幅
				hc.push("<td" + this.TdBgColor(i,12) + ">" + this.dataArray[i][12].toFixed(2) + "</td>");//量比
			}
			hc.push("<td><a href=\"http://vol.stock.hexun.com/" + this.dataArray[i][0] + ".shtm\" target=\"_blank\"><img src=\"/img/img004.gif\" align=\"absmiddle\"/></a></td>");
			hc.push("<td class=\"tdend\"><a href=\"http://guba.hexun.com/" + this.dataArray[i][0] + ",guba.html\" target=\"_blank\">股吧</a></td></tr>");
		}
		
		hc.push("</table>");
		Common.$(this.divName).innerHTML = hc.join('');
		
		if(this.reload){
			clearTimeout(this.setTimeObj);
			this.setTimeObj = setTimeout(this.AutoReload,this.reloadTime);//按设置时间重读数据
		}
	}
	this.AutoReload = function(){
		StockListRequest.Request();
	}
	this.ShowTime = function(time)
	{
		Common.$("updatetime").innerHTML = time;
		Common.$("topupdatetime").innerHTML = time;
	}
	this.ShowPage = function(page,total){
		Common.$("pagenum").innerHTML = page + "/" + total;
	}
	this.InitDataSort = function(column,order)
	{
		if(order == "up"){
			this.dataArray.sort(function(a,b){ return b[column]-a[column]; });
		}
		if(order == "down"){
			this.dataArray.sort(function(a,b){ return a[column]-b[column]; });
		}
	}
	this.DataSort = function(column)
	{
		if(this.columnArray[column] == "down" || this.columnArray[column] == undefined)
		{
			this.columnArray[column] = "up";
			this.ArraySort(column,"up");
		}
		else
		{
			this.columnArray[column] = "down";
			this.ArraySort(column,"down");
		}
	}
	this.ArraySort = function(column,order)
	{
		this.currentColumn = column;
		StockListRequest.updown = order;
		StockListRequest.sorttype = column;
		StockListRequest.Request();
	}
	this.TrBgColor = function(i)
	{
		if(i%2 == 0){
			return "";
		}else{
			return " bgcolor=\"#F7F7F7\"";
		}
		return "";
	}
	this.TdBgColor = function(i,column)
	{
		if(column == this.currentColumn){
			if(i%2 == 0){
				return " bgcolor=\"#FFF3EB\"";	
			}else{
				return " bgcolor=\"#F9ECE4\"";	
			}
		}
		return "";
	}
	this.Arrow = function(column)
	{
		if(column == this.currentColumn){
			if(this.columnArray[column] == "up"){
				return "<img src=\"/img/dot3.gif\" align=\"absmiddle\"/><img src=\"/img/dot2_2.gif\" align=\"absmiddle\" />";	
			}else{
				return "<img src=\"/img/dot1_1.gif\" align=\"absmiddle\"/><img src=\"/img/dot1_2.gif\" align=\"absmiddle\"/>";	
			}
		}
		return "";
	}
}