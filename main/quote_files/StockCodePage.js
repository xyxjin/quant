/* ----- */

StockCodePage = new function(){
	this.dataArray;
	this.codeArray;
	this.divName = "StockCodePage";
	this.reloadTime = 60000;
	this.reload = true;
	this.setTimeObj;
	this.GetData = function(array)
	{
		this.codeArray = StockCodeRequest.code.split("|");
		this.dataArray = array;
		this.LoadDataFinish();
	}
	this.LoadDataFinish = function()
	{
		var hc = new Array();
		
		for(var i=0;i < this.dataArray.length;i++)
		{
			if(i == 0)
			{
				hc.push("<div class=\"stock_left\">");
			}else{
				hc.push("<div class=\"stock_left mLeft\">");
			}
			hc.push("<div class=\"stock_nav\"><strong>" + this.dataArray[i][1] + "</strong>　" + Common.GetColor2DEC(this.dataArray[i][3],this.dataArray[i][4]) + "　" + Common.GetColor2DEC(this.dataArray[i][7],0) + " <span" + Common.GetColorClass(this.dataArray[i][8],0) + ">(" + this.dataArray[i][8].toFixed(2) + "%)</span></div>");
			hc.push("<div class=\"stock_box\">");
			hc.push("<div class=\"img\"><a href=\"http://stockdata.stock.hexun.com/indexhq_" + this.codeArray[i] + ".shtml\" target=\"_blank\"><img src=\"http://minpic.quote.stock.hexun.com/WebPic/" + Common.MarketString(this.dataArray[i][2]) + "/Min/" + this.dataArray[i][0] + "_www2.gif?" + (new Date()).getTime() + "\" /></a></div>");
			hc.push("<div class=\"text\">最低" + Common.GetColor2DEC(this.dataArray[i][5],this.dataArray[i][4]) + " 最高" + Common.GetColor2DEC(this.dataArray[i][6],this.dataArray[i][4]) + " 成交" + this.dataArray[i][9] + "亿</div>");
			hc.push("</div></div>");
		}
		hc.push("<div class=\"clear\"></div>");
		
		if(Common.$(this.divName)) Common.$(this.divName).innerHTML = hc.join('');
		
		if(this.reload){
			clearTimeout(this.setTimeObj);
			this.setTimeObj = setTimeout(this.AutoReload,this.reloadTime);//按设置时间重读数据
		}
	}
	this.AutoReload = function(){
		StockCodeRequest.Request();
	}
}
