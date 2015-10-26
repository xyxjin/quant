package com.pureblue.quant.TencentAPI;

import java.io.*;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HexunConceptionAdaptor {
	private static final String CONCEPTION_QUERY_URL = "http://quote.hexun.com/js/conception.ashx";
	private static final String GN_INDUSTRY_PATTERN = "type_code:'([a-zA-Z_0-9]+)'";
	private static final String STOCKS_QUERY_URL = "http://quote.tool.hexun.com/hqzx/stocktype.aspx?columnid=5522&&count=500&type_code=";
	private static final String STOCK_CODE_PATTERN = "\\['([0-9]+)[^\\[\\]]+\\]";
	
	public static void main(String[] args)  throws Exception{
		HexunConceptionAdaptor adaptor = new HexunConceptionAdaptor();
		Map<String, ArrayList<String>> industry = adaptor.getGnIndustry();
		System.out.println(industry.toString());
	}
	
	public Map<String, ArrayList<String>> getGnIndustry() throws IOException {
		Map<String, ArrayList<String>> industry = new HashMap<String, ArrayList<String>>();
		Document doc = Jsoup.connect(CONCEPTION_QUERY_URL).timeout(2000).get();
		String body = doc.select("body").text();
		Pattern p = Pattern.compile(GN_INDUSTRY_PATTERN);
		Matcher m = p.matcher(body);
		while(m.find()){
			try{
				String stocksInfo = industryQuery(m.group(1));
				ArrayList<String> stocks = parseStockline(stocksInfo);
				industry.put(m.group(1), stocks);
			}catch (IOException e){
				throw new ConnectException("Exception while connecting to: " + m.group(1) + " [" + e.getMessage() + "]");
			}
		}
		return industry;
	}
	
	private ArrayList<String> parseStockline(String stockHtml){
		ArrayList<String> stocks = new ArrayList<String>();
		Pattern p0 = Pattern.compile(STOCK_CODE_PATTERN);
		Matcher m0 = p0.matcher(stockHtml);
		while(m0.find()){
			stocks.add(m0.group(1));
		}
		return stocks;
	}
	
	private String industryQuery(String industryCode) throws IOException {
		String stocksInfo = null;
		HttpClient httpClient = new HttpClient();
		String url = STOCKS_QUERY_URL + industryCode;
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = httpClient.executeMethod(method);
	        if (statusCode != HttpStatus.SC_OK) {
	          throw new ConnectException("Query to " + url + " failed [" + method.getStatusLine() + "]");
	        }
	        stocksInfo = method.getResponseBodyAsString();
		}catch (HttpException e){
			e.printStackTrace();
		}finally{
			method.releaseConnection();
			method.setRequestHeader("Connection", "close"); 
		}
		return stocksInfo;
	}	
}
