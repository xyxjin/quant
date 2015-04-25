
// 概念板块数据
ConceptionBlock = new function()
{
	this.divID = "ConceptionBlockDiv";
	
	this.ShowData = function (data)
	{
		if(data == null || data.length == 0) return;
		
		var hc = new Array();
		
		hc.push('<table width="100%" border="0" cellspacing="0" cellpadding="0">');
		hc.push(' <tr>');
		
		var cellSize = 16; // 20条数据换一块
		
		var cols = Math.ceil(data.length / cellSize); // 总数据列数
		
		var colsIndex = 0; // 当前列
		
		for(var index in data)
		{
			if(index % cellSize == 0)
			{
				colsIndex++; 
				hc.push('<td style="border-right:1px solid #ccc">');
				hc.push('<ul>');
			}
			
			hc.push('<li>');
			hc.push(' <a href="/stock/conseption.aspx?code=' + data[index].type_code + '&name=' + data[index].type_name + '#CBlock' + data[index].type_code + '" class="black" onclick="IframeOpen(this.href);return false" title="' + data[index].type_name + '">' + data[index].type_name + '</a>');
			hc.push('</li>');
			
			if(Math.round((parseInt(index) + 1) % cellSize) == 0 || index == (data.length - 1))
			{			
				if((parseInt(index) + 1) < (colsIndex * cellSize))
				{		
					for(var i = parseInt(index) + 1; i < colsIndex * cellSize; i++)
					{
						hc.push('<li><a>&nbsp;</a></li>');
					}
				}
				
				hc.push('</ul>');
				hc.push('</td>');
			}
		}
		
		hc.push(' </tr>');
		hc.push('</table>');
		
		document.getElementById(this.divID).innerHTML = hc.join('');
	}
}
