package com.pureblue.quant.TencentAPI;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pureblue.quant.model.FundType;
import com.pureblue.quant.util.HttpUtil;
import com.pureblue.quant.util.LoggerUtils;

class scrapySingleFund implements java.lang.Runnable
{
	String url;
	List<IFund> stocks;
	private Logger logger;
	
	public scrapySingleFund(String url, List<IFund> stocks){
		this.url = url;
		this.stocks = stocks;
		this.logger = LoggerUtils.getLogger(LoggerUtils.path);
	}

	public void run() {
		String body;
		body = HttpUtil.httpQuery(url);
        synchronized(stocks){
        	stocks.addAll(parsePerFundDetail(body));
        }
//			System.out.println(url + " done!");
	}
	
	private Collection<IFund> parsePerFundDetail(String body){
		Collection<IFund> stocks = new LinkedList<IFund>();
		JSONObject jsonObject;
		String symbol = null;
		try {
			jsonObject = new JSONObject(body);
			JSONObject basicInfo = jsonObject.getJSONObject("data");		
			symbol = basicInfo.getString("symbol");
			String date = basicInfo.getString("date");
			int type = basicInfo.getInt("type");
/*			if(FundType.STOCK.value()!=type){
				return stocks;
			}*/
			
			/*
			JSONArray peizhiList = jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("peizhi");
			for(int i=0; i<peizhiList.length(); i++){
				String name = peizhiList.getJSONObject(i).getString("name");
				try {
					if(URLEncoder.encode(name, "UTF-8").equalsIgnoreCase(URLEncoder.encode("股票", "UTF-8"))){
						System.out.println("ok");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Double ratio = peizhiList.getJSONObject(i).getDouble("ratio");
				System.out.println(name + ' ' + String.valueOf(ratio));
			}
			*/			
			//System.out.println(jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("zhongcang").toString());
			JSONArray peizhiList = jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("zhongcang");
			for(int i=0; i<peizhiList.length(); i++){
				String code = peizhiList.getJSONObject(i).getString("code");
				Double ratio = peizhiList.getJSONObject(i).getDouble("ratio");
				int count = peizhiList.getJSONObject(i).getInt("count");
				double total_assets = peizhiList.getJSONObject(i).getDouble("total_assets");
				if(noStockCode(code)){
					continue;
				}
				IFund stock = new FundStocks(symbol, date, code, count, total_assets, ratio);
				stocks.add(stock);
			}
		} catch (JSONException e) {
			logger.error("Parse fund " + symbol + " failed with error info " + e);
		}
		return stocks;
	}
	
	private boolean noStockCode(String code){
		int codeInt = Integer.parseInt(code);
		if(codeInt >= 600000 && codeInt < 700000)
			return false;
		if(codeInt >= 0 && codeInt < 3000)
			return false;
		if(codeInt >= 300000 && codeInt < 400000)
			return false;
		return true;
	}
}


public class TencentFundAdaptor {
	private static final String tencentFundUrl1[] = {"http://stockqt.gtimg.cn/cgi-bin/hcenter/q?id=501"};
	private static final String tencentFundUrl2[] = {"http://stock.gtimg.cn/data/index.php?appn=rank&t=rankclosefund/chr", "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranketf/chr", "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranklof/chr"};
	private static final String fundDetailBaseUrl = "http://web.ifzq.gtimg.cn/fund/newfund/fundInvesting/getInvesting?app=web&symbol=";
	private Logger logger;
	
	public static void main(String[] args){

		TencentFundAdaptor fund = new TencentFundAdaptor();
		Collection<IFund> mapping = fund.fetchAllFunds();
	}
	
	public TencentFundAdaptor() {
		this.logger = LoggerUtils.getLogger(LoggerUtils.path);
	}

	public List<IFund> fetchAllFunds(){
		String url;
		int i=0;
		List<IFund> stocks = new LinkedList<IFund>();
		Collection<String> funds = fetchAllFundIndex();
		
		System.out.println(funds.toString());
//		Collection<String> funds = new ArrayList<String>();
//		funds.add("jj003003");
//		funds.add("jj690001");
//		funds.add("jj590002");
/*		ExecutorService threadExecutor = Executors.newFixedThreadPool(100);
		Future<?> future[] = new Future[funds.size()];
		
		for(String fund : funds){
			url = fundDetailBaseUrl + fund;
			logger.warn("scrapy fund stocks: " + url);
			scrapySingleFund t = new scrapySingleFund(url, stocks);
        	future[i++] = threadExecutor.submit(t);
		}
		for(i=0; i<funds.size(); i++){
			try {
				future[i].get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		threadExecutor.shutdown();*/
		return stocks;
	}
	
	private Collection<String> fetchAllFundIndex(){
		Collection<String> fundList = null;
		try {
			Collection<String> openFunds = fetchOpenFundIndex(tencentFundUrl1);
			fundList = openFunds;
			Collection<String> closeFunds = fetchCloseFundIndex(tencentFundUrl2);
			fundList.addAll(closeFunds);
		} catch (ConnectException e) {
			e.printStackTrace();
		}
		
		return fundList;
	}
	
	private Collection<String> fetchCloseFundIndex(String[] baseUrl) throws ConnectException{
		Collection<String> fundList = new ArrayList<String>();
		JSONObject jsonObject;
		String body;
		for(int i=0; i<baseUrl.length; i++){
			try {
				int total = 0;
				int currentPage = 0;
				String url = baseUrl[i];
				do{
					body = HttpUtil.httpQuery(url);
					jsonObject = new JSONObject(body.split("=")[1]);
					total = jsonObject.getInt("total");
					currentPage = jsonObject.getInt("p");
					String[] funds = jsonObject.getString("data").split(",");
					for(int j=0; j<funds.length; j++)
						fundList.add(funds[j]);
					url = baseUrl[i] + "&p=" + String.valueOf(currentPage+1);
				}while(currentPage<total);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return fundList;
	}
	
	private Collection<String> fetchOpenFundIndex(String[] baseUrl){
		Collection<String> fundList = new ArrayList<String>();
		for(int i=0; i<baseUrl.length; i++){
			int total = 0;
			int currentPage = 0;
			String body;
			String url = baseUrl[i];
			do{
            	//System.out.println(url);
            	body = HttpUtil.httpQuery(url);
            	//System.out.println(body.split(";")[0].split("=")[1]);
            	Pattern p = Pattern.compile("\"([^\"\"]+)\"");
            	Matcher m = p.matcher(body.split(";")[0].split("=")[1]);
            	if(m.find()){
            		total = Integer.valueOf(m.group(1));
            	}
            	m = p.matcher(body.split(";")[1].split("=")[1]);
            	if(m.find()){
            		currentPage = Integer.valueOf(m.group(1));
            	}
            	m = p.matcher(body.split(";")[3].split("=")[1]);
            	if(m.find()){
            		String[] funds = m.group(1).split("\\^");
            		for(int j=0; j<funds.length; j++){
            			//System.out.println("jj"+funds[j].split("~")[0]);
            			fundList.add("jj"+funds[j].split("~")[0]);
            		}
            	}
            	url = baseUrl[i] + "&pn=" + String.valueOf(currentPage+1);
            }while(currentPage<total);
		}
		return fundList;
	}

}
