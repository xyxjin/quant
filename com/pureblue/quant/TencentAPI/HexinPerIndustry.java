package com.pureblue.quant.TencentAPI;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HexinPerIndustry extends Hexin{
	private static final String baseUrl = "http://q.10jqka.com.cn/stock/thshy/";
	private static final int stocksPerPage = 50;
	private String hyCode;
	private int num;
	private Collection<String> stocks;
	
	public HexinPerIndustry(String hyCode, String num) {
		super();
		this.hyCode = hyCode;
		this.num = Integer.parseInt(num);
		this.stocks = new HashSet<String>();
	}
	
	public void fetchStocks() throws JSONException{
		String url = baseUrl + hyCode;
		Map<String, String> link;
		try {
			link = fetchLinksInfo(url);
			fetchStocksFromInd(link);
		} catch (ConnectException e) {
			e.printStackTrace();
		}
	}

	private void fetchStocksFromInd(Map<String, String> link) throws ConnectException {
		System.out.println(link.toString());
		int pageCount = (int) Math.ceil((double)num/stocksPerPage);
		for(int page=Integer.parseInt(link.get("page")); page<=pageCount; page++){
			try {
				JSONObject jsonObject = fetchHtmlBody(link, page);
				Object type = jsonObject.get("data");
				//String type = jsonObject.getJSONArray("data").toString();
				//System.out.println(type.toString());
				if(type.toString() != "null"){
					JSONArray stockList = jsonObject.getJSONArray("data");
					for(int i=0; i<stockList.length(); i++){
						String stockcode = stockList.getJSONObject(i).getString("stockcode");
						stocks.add(stockcode);
						//System.out.println(stockcode);
					}
				}
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(stocks.toString());
	}

	public String getHyCode() {
		return hyCode;
	}

	public void setHyCode(String hyCode) {
		this.hyCode = hyCode;
	}

	public Collection<String> getStocks() {
		return stocks;
	}
}
