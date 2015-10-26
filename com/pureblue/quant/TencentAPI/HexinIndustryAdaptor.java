package com.pureblue.quant.TencentAPI;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HexinIndustryAdaptor extends Hexin{
	private static final String baseUrl = "http://q.10jqka.com.cn/stock/thshy";

	public static void main(String[] args){
		int num = (int) Math.ceil((double)100/50);
		System.out.println(num);
		
		HexinIndustryAdaptor hexin = new HexinIndustryAdaptor();
		Map<String, Collection<String>> mapping = hexin.fetchAll();
		System.out.println(mapping.toString());
		
	}
	
	public Map<String, Collection<String>> fetchAll(){
		Map<String, String> link = null;
		try {
			link = fetchLinksInfo(baseUrl);
		} catch (ConnectException e) {
			e.printStackTrace();
			return null;
		}
		return fetchStocksFromInds(link);
	}

	private Map<String, Collection<String>> fetchStocksFromInds(Map<String, String> link) {
		System.out.println(link.toString());
		Map<String, Collection<String>> marketIndtoStockMapping = new HashMap<String, Collection<String>>();
		for(int page=Integer.parseInt(link.get("page")); page<=Integer.parseInt(link.get("last")); page++){
			try {
				JSONObject jsonObject = fetchHtmlBody(link, page);
				Object type = jsonObject.get("data");
				if(type.toString() != "null"){
					JSONArray plateList = jsonObject.getJSONArray("data");
					for(int i=0; i<plateList.length(); i++){
						String pyCode = plateList.getJSONObject(i).getString("hycode");
						String num = plateList.getJSONObject(i).getString("num");
						HexinPerIndustry indInfo = new HexinPerIndustry(pyCode, num);
						indInfo.fetchStocks();
						marketIndtoStockMapping.put(pyCode, indInfo.getStocks());
					}
				}
			} catch (JSONException | IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return marketIndtoStockMapping;
	}
}
