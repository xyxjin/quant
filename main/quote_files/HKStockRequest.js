/* ----- */

HKStockCodeRequest = new function(){
// private fields 

// private static methods 

// public fields 
	this.scriptid ="HKStockCodeRequest";
	this.url = "http://hkquote.stock.hexun.com/hqzx/restHKStockIndex.aspx";
	this.code = "";
	this.page = 1;//当前页
	this.totalpage = 1;//总页数
// public methods 
	this.CreateLink = function()
	{
		var request = this.url + "?";
		if(!Common.IsHKCode(this.code))
		{
			this.code = "90001|90007|90020|90006";
		}
		request += "indexcode=" + this.code + "&time=" + Common.Time();
		return request;
	}
	this.Request = function()
	{
		Common.AppendDataArray(this.scriptid,this.CreateLink());
	}
// constructor
	{
	}
}


/* ----- */

HKStockListRequest = new function(){
// private fields 

// private static methods 

// public fields 
	this.scriptid ="HKStockListRequest";
	this.url = "http://hkquote.stock.hexun.com/hqzx/restHKStockQuote_new.ashx";
	this.type = "0";
	this.sorttype = "3";
	this.updown = "up";
	this.count = 50;//每页数量
	this.page = 1;//当前页
	this.totalpage = 1;//总页数
	this.input=2;
// public methods 
	this.CreateLink = function()
	{
	
		var request = this.url + "?";
		if(this.type != "")
		{
			request += "type=" + this.type + "&";
		}
		request += "sorttype=" + this.sorttype + "&updown=" + this.updown + "&page=" + this.page + "&count=" + this.count + "&input=" + this.input + "&time=" + Common.Time();
		return request;
	}
	this.Request = function()
	{
		Common.AppendDataArray(this.scriptid,this.CreateLink());
	}
	this.FirstPage = function()
	{
		this.page = 1;
		Common.AppendDataArray(this.scriptid,this.CreateLink());
	}
	this.EndPage = function()
	{
		this.page = this.totalpage;
		Common.AppendDataArray(this.scriptid,this.CreateLink());
	}
	this.NextPage = function()
	{
		if(this.page < this.totalpage)
		{
			this.page += 1;
			Common.AppendDataArray(this.scriptid,this.CreateLink());
			//获取下一页数据
		}
	}
	this.PrevPage = function()
	{
		if(this.page >1)
		{
			this.page -= 1;
			Common.AppendDataArray(this.scriptid,this.CreateLink());
			//获取上一页数据
		}
	}
	this.GoToPage = function(input)
	{
		if(Common.IsNumber(input)){
			if(input > 0 && input <= this.totalpage && input != this.page)
			{
				this.page = Number(input);
				Common.AppendDataArray(this.scriptid,this.CreateLink());
			}
		}
	}
// constructor
	{
	}
}