package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import analytics.HighLow;
import model.BarSize;
import model.TimePeriod;
import dao.CapitalPoint;
import dao.IOHLCPoint;
import dao.OHLCPointDao;
import dao.StockDatebaseFactory;
import Tencent.HexinPerIndustry;
import Tencent.HexunConceptionAdaptor;
import Tencent.HexunConceptionIndustryDao;
import Tencent.TencentRealTimeAdapterComponent;
import Tencent.HushenMarket;

import java.net.URLEncoder;

public class myQuant {
	
	public static void main(String[] args)  throws Exception{
		String url = "http://fund.10jqka.com.cn/550002/allocation.html";
		url = "http://fund.10jqka.com.cn/550002/stock.html";
		url = "http://stockqt.gtimg.cn/cgi-bin/hcenter/q?id=501&pn=18";
		url = "http://stock.gtimg.cn/data/index.php?appn=rank&t=rankclosefund/chr";
		url = "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranketf/chr&p=1";
		url = "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranklof/chr&p=1";
		//url ="http://gu.qq.com/sz150153";
		//url = "http://web.ifzq.gtimg.cn/fund/newfund/fundInvesting/getInvesting?app=web&symbol=sz150153";
//		System.out.println(httpQuery(url));
//		String body = httpQuery(url);
//		System.out.println(body.split("=")[1]);
//		
//		JSONObject jsonObject = new JSONObject(body.split("=")[1]);
//		System.out.println(jsonObject.getInt("total"));
//		System.out.println(jsonObject.getString("data"));
		
		Connection conn = StockDatebaseFactory.getInstance("test");
		OHLCPointDao ohlcPointDao = new OHLCPointDao(conn);
		List<IOHLCPoint> data = ohlcPointDao.findTicker("600030", TimePeriod.formatStringToDate("2014-12-1"), TimePeriod.formatStringToDate("2014-1-1"));
		List<Double> close = new ArrayList<Double>();
		List<Double> volume = new ArrayList<Double>();
		int len = data.size();
		for(int i=0;i<len;i++){
//			System.out.println(data.toString());
			close.add(data.get(i).getClose());
			volume.add((double)data.get(i).getVolume());
		}
		List<Map<Integer, Double>> peaks = HighLow.peak_detection(close, 0.03);
		System.out.println((peaks.get(0).toString()));
		System.out.println(data.get(15).getClose());
		/*
		String fundStr = body.split("=")[4];
		
		String fundsStr = fundStr.replace("\"", "");
		
		String[] funds = fundsStr.split("\\^");
		for(String str:funds){
			System.out.println(str.split("~")[0]);
		}
		*/
		/*
		File input = new File("input.html");
		Document doc = Jsoup.parse(input, "utf-8", "http://fund.10jqka.com.cn");
//		System.out.println(doc.toString());
		Elements listInfo = doc.select("#tb_st_2014-12-31");
		System.out.println(listInfo.toString());
		for(org.jsoup.nodes.Element item : listInfo){
			try {
				Integer.parseInt(item.html());
//				System.out.println(item.attr("href"));
//				System.out.println(item.html());
//				System.out.println("ok");
			} catch (Exception e) {
//				System.out.println(e.getMessage());
			}
		}
		System.out.println("done");
		*/
		/*
		//String body = '{"code":0,"msg":"OK","data":{"symbol":"sz150153","date":"2014-12-31","type":4,"data":{"peizhi":[{"name":"\u80a1\u7968","ratio":"90.38"},{"name":"\u503a\u5238","ratio":"0.00"},{"name":"\u73b0\u91d1","ratio":"5.79"},{"name":"\u5176\u4ed6","ratio":"3.83"}],"hangye":[{"name":"\u5236\u9020\u4e1a","ratio":"44.97"},{"name":"\u4fe1\u606f\u4f20\u8f93\u3001\u8f6f\u4ef6\u548c\u4fe1\u606f","ratio":"24.11"},{"name":"\u6587\u5316\u3001\u4f53\u80b2\u548c\u5a31\u4e50\u4e1a","ratio":"9.13"},{"name":"\u6c34\u5229\u3001\u73af\u5883\u548c\u516c\u5171\u8bbe\u65bd","ratio":"4.04"}],"zhongcang":[{"name":"\u4e1c\u65b9\u8d22\u5bcc","code":"300059","count":"4033305","total_assets":"112771000.00","ratio":"4.20"},{"name":"\u534e\u8c0a\u5144\u5f1f","code":"300027","count":"3983161","total_assets":"105036000.00","ratio":"3.91"},{"name":"\u78a7\u6c34\u6e90","code":"300070","count":"2805903","total_assets":"97645400.00","ratio":"3.63"},{"name":"\u673a\u5668\u4eba","code":"300024","count":"2449322","total_assets":"96478800.00","ratio":"3.59"},{"name":"\u4e07\u8fbe\u4fe1\u606f","code":"300168","count":"1620725","total_assets":"74877504.00","ratio":"2.79"},{"name":"\u4e50\u89c6\u7f51","code":"300104","count":"1825703","total_assets":"59225800.00","ratio":"2.20"},{"name":"\u6c47\u5ddd\u6280\u672f","code":"300124","count":"1925980","total_assets":"56219400.00","ratio":"2.09"},{"name":"\u84dd\u8272\u5149\u6807","code":"300058","count":"2577454","total_assets":"54435800.00","ratio":"2.03"},{"name":"\u795e\u5dde\u6cf0\u5cb3","code":"300002","count":"3275287","total_assets":"54369800.00","ratio":"2.02"},{"name":"\u4e50\u666e\u533b\u7597","code":"300003","count":"2214116","total_assets":"52696000.00","ratio":"1.96"}]}}}';
		//String body = '{"code":0,"msg":"OK","data":{"symbol":"sz150153","date":"2014-12-31","type":4,"data":{"peizhi":[{"name":"80a17968","ratio":"90.38"},{"name":"503a5238","ratio":"0.00"},{"name":"73b091d1","ratio":"5.79"},{"name":"51764ed6","ratio":"3.83"}],"hangye":[{"name":"523690204e1a","ratio":"44.97"},{"name":"4fe1606f4f208f9330018f6f4ef6548c4fe1606f","ratio":"24.11"},{"name":"6587531630014f5380b2548c5a314e504e1a","ratio":"9.13"},{"name":"6c345229300173af5883548c516c51718bbe65bd","ratio":"4.04"}],"zhongcang":[{"name":"4e1c65b98d225bcc","code":"300059","count":"4033305","total_assets":"112771000.00","ratio":"4.20"},{"name":"534e8c0a51445f1f","code":"300027","count":"3983161","total_assets":"105036000.00","ratio":"3.91"},{"name":"78a76c346e90","code":"300070","count":"2805903","total_assets":"97645400.00","ratio":"3.63"},{"name":"673a56684eba","code":"300024","count":"2449322","total_assets":"96478800.00","ratio":"3.59"},{"name":"4e078fbe4fe1606f","code":"300168","count":"1620725","total_assets":"74877504.00","ratio":"2.79"},{"name":"4e5089c67f51","code":"300104","count":"1825703","total_assets":"59225800.00","ratio":"2.20"},{"name":"6c475ddd6280672f","code":"300124","count":"1925980","total_assets":"56219400.00","ratio":"2.09"},{"name":"84dd827251496807","code":"300058","count":"2577454","total_assets":"54435800.00","ratio":"2.03"},{"name":"795e5dde6cf05cb3","code":"300002","count":"3275287","total_assets":"54369800.00","ratio":"2.02"},{"name":"4e50666e533b7597","code":"300003","count":"2214116","total_assets":"52696000.00","ratio":"1.96"}]}}}';
		//String body = "{code:0,msg:OK,data:{symbol:sz150153,date:2014-12-31,type:4,data:{peizhi:[{name:80a17968,ratio:90.38},{name:503a5238,ratio:0.00},{name:73b091d1,ratio:5.79},{name:51764ed6,ratio:3.83}],hangye:[{name:523690204e1a,ratio:44.97},{name:4fe1606f4f208f9330018f6f4ef6548c4fe1606f,ratio:24.11},{name:6587531630014f5380b2548c5a314e504e1a,ratio:9.13},{name:6c345229300173af5883548c516c51718bbe65bd,ratio:4.04}],zhongcang:[{name:4e1c65b98d225bcc,code:300059,count:4033305,total_assets:112771000.00,ratio:4.20},{name:534e8c0a51445f1f,code:300027,count:3983161,total_assets:105036000.00,ratio:3.91},{name:78a76c346e90,code:300070,count:2805903,total_assets:97645400.00,ratio:3.63},{name:673a56684eba,code:300024,count:2449322,total_assets:96478800.00,ratio:3.59},{name:4e078fbe4fe1606f,code:300168,count:1620725,total_assets:74877504.00,ratio:2.79},{name:4e5089c67f51,code:300104,count:1825703,total_assets:59225800.00,ratio:2.20},{name:6c475ddd6280672f,code:300124,count:1925980,total_assets:56219400.00,ratio:2.09},{name:84dd827251496807,code:300058,count:2577454,total_assets:54435800.00,ratio:2.03},{name:795e5dde6cf05cb3,code:300002,count:3275287,total_assets:54369800.00,ratio:2.02},{name:4e50666e533b7597,code:300003,count:2214116,total_assets:52696000.00,ratio:1.96}]}}}";
		String body = "{code:0,msg:OK,data:{symbol:sz150153,date:2014-12-31,type:4,data:{peizhi:[{name:\u80a1\u7968,ratio:90.38},{name:\u503a\u5238,ratio:0.00},{name:\u73b0\u91d1,ratio:5.79},{name:\u5176\u4ed6,ratio:3.83}],hangye:[{name:\u5236\u9020\u4e1a,ratio:44.97},{name:\u4fe1\u606f\u4f20\u8f93\u3001\u8f6f\u4ef6\u548c\u4fe1\u606f,ratio:24.11},{name:\u6587\u5316\u3001\u4f53\u80b2\u548c\u5a31\u4e50\u4e1a,ratio:9.13},{name:\u6c34\u5229\u3001\u73af\u5883\u548c\u516c\u5171\u8bbe\u65bd,ratio:4.04}],zhongcang:[{name:\u4e1c\u65b9\u8d22\u5bcc,code:300059,count:4033305,total_assets:112771000.00,ratio:4.20},{name:\u534e\u8c0a\u5144\u5f1f,code:300027,count:3983161,total_assets:105036000.00,ratio:3.91},{name:\u78a7\u6c34\u6e90,code:300070,count:2805903,total_assets:97645400.00,ratio:3.63},{name:\u673a\u5668\u4eba,code:300024,count:2449322,total_assets:96478800.00,ratio:3.59},{name:\u4e07\u8fbe\u4fe1\u606f,code:300168,count:1620725,total_assets:74877504.00,ratio:2.79},{name:\u4e50\u89c6\u7f51,code:300104,count:1825703,total_assets:59225800.00,ratio:2.20},{name:\u6c47\u5ddd\u6280\u672f,code:300124,count:1925980,total_assets:56219400.00,ratio:2.09},{name:\u84dd\u8272\u5149\u6807,code:300058,count:2577454,total_assets:54435800.00,ratio:2.03},{name:\u795e\u5dde\u6cf0\u5cb3,code:300002,count:3275287,total_assets:54369800.00,ratio:2.02},{name:\u4e50\u666e\u533b\u7597,code:300003,count:2214116,total_assets:52696000.00,ratio:1.96}]}}}";
		JSONObject jsonObject = new JSONObject(body);
		
		JSONArray peizhiList = jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("peizhi");
		for(int i=0; i<peizhiList.length(); i++){
			String name = peizhiList.getJSONObject(i).getString("name");
			Double ratio = peizhiList.getJSONObject(i).getDouble("ratio");
			System.out.println(name + ' ' + String.valueOf(ratio));
		}
		
		System.out.println();
		
		//System.out.println(jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("zhongcang").toString());
		double total = 0;
		peizhiList = jsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("zhongcang");
		for(int i=0; i<peizhiList.length(); i++){
			int code = peizhiList.getJSONObject(i).getInt("code");
			String name = peizhiList.getJSONObject(i).getString("name");
			Double ratio = peizhiList.getJSONObject(i).getDouble("ratio");
			int count = peizhiList.getJSONObject(i).getInt("count");
			int total_assets = peizhiList.getJSONObject(i).getInt("total_assets");
			
			System.out.println(URLEncoder.encode(name, "UTF-8"));
			System.out.println(URLEncoder.encode("华谊兄弟", "UTF-8"));
			
			if(URLEncoder.encode(name, "UTF-8").equalsIgnoreCase(URLEncoder.encode("华谊兄弟", "UTF-8"))){
				System.out.println("ok");
			}
				
			total = total + total_assets;
			//System.out.println(String.valueOf(code) + ' ' + name + ' ' + String.valueOf(ratio) + ' ' + String.valueOf(total_assets) + ' ' + String.valueOf(count));
		}
		
		System.out.println(total);
	    
		File input = new File("input.html");
		Document doc = Jsoup.parse(input, "utf-8", "http://fund.fund123.cn");
//		System.out.println(doc.toString());
		Elements listInfo = doc.select(".listInfo").select("a");
		for(org.jsoup.nodes.Element item : listInfo){
			try {
				Integer.parseInt(item.html());
//				System.out.println(item.attr("href"));
//				System.out.println(item.html());
//				System.out.println("ok");
			} catch (Exception e) {
//				System.out.println(e.getMessage());
			}
			
		}
//		System.out.println(listInfo.toString());
		//System.out.println(jsonObject.toString());
		//System.out.println(jsonObject.get("data"));
		 */
		/*
		TDistribution t = new TDistribution(29);
		double lowerTail = t.cumulativeProbability(-2.656);
		double upperTail = 1.0 - t.cumulativeProbability(2.75);
		
		System.out.println(lowerTail);
		System.out.println(upperTail);
		
		Connection connection = StockDatebaseFactory.getInstance("test");
		OHLCPointDao ohlcPointDao = new OHLCPointDao(connection);
		Date endDateTime = TimePeriod.formatStringToDate("2015-04-10");
		Date startDateTime = TimePeriod.formatStringToDate("2014-07-01");
		
		List<IOHLCPoint> data = ohlcPointDao.findTicker("601875", endDateTime, startDateTime);
		
		int len = data.size();
		
		System.out.println(len);
		*/
		
		/*
		List<Double> close = new ArrayList<Double>();
		List<Double> volume = new ArrayList<Double>();
		
		for(int i=0;i<len;i++){
			System.out.println(data.toString());
			close.add(data.get(i).getClose());
			volume.add((double)data.get(i).getVolume());
		}
		*/
		/*
		int period = 120;
		double[] close = new double[period];
		double[] volume = new double[period];
		for(int round =1; round<len-period; round++){
			int count = 0;
			for(int i=1+round;i<round+period;i++){
				//System.out.println(data.toString());period
				if(data.get(i).getVolume() != 0){
					if(data.get(i).getHigh() - data.get(i).getLow() != 0){
						double moneyFlowMultiplier = ((data.get(i).getClose() - data.get(i).getLow()) - (data.get(i).getHigh() - data.get(i).getClose())) / (data.get(i).getHigh() - data.get(i).getLow());
						
						double chaikinVolume = moneyFlowMultiplier * data.get(i).getVolume();
	
						volume[count] = chaikinVolume;
						close[count] = (data.get(i).getClose() - data.get(i-1).getClose())/data.get(i-1).getClose();
						//volume[count] = (double)data.get(i).getVolume();	
						
						//System.out.println(close[count]);
						//System.out.println(volume[count]);
						count++;
					}
				}
			}
			PearsonsCorrelation cov = new PearsonsCorrelation();
	        System.out.println(cov.correlation(Arrays.copyOfRange(close,0,count), Arrays.copyOfRange(volume,0,count)));    
		}
		*/
		/*
        SpearmansCorrelation cov1 = new SpearmansCorrelation();
        System.out.println(cov1.correlation(Arrays.copyOfRange(close,0,count), Arrays.copyOfRange(volume,0,count)));
        
        KendallsCorrelation cov2 = new KendallsCorrelation();
        System.out.println(cov2.correlation(Arrays.copyOfRange(close,0,count), Arrays.copyOfRange(volume,0,count)));
        */ 
		
        //Covariance cov3 = new Covariance();
        //System.out.println(cov3.covariance(Arrays.copyOfRange(close,0,count), Arrays.copyOfRange(volume,0,count)));
		//"sh000001", "sz399001", 
		/*
		String[] quotes = {"sz399006"};
		System.out.println(quotes.length + quotes.toString());
		ExecutorService pool = Executors.newFixedThreadPool(100);
		for(String symbol : quotes){
			TencentHistoricalThread t = new TencentHistoricalThread("test", symbol);
			//t.setFetchMode(true);
			pool.execute(t);
			System.out.println(symbol);
		}
		pool.shutdown();
		System.out.println("done!!!!!!!!!!!!!!!!!!!");
		*/
		/*
		Connection connection = StockDatebaseFactory.getInstance("test");
		HushenMarket market = new HushenMarket(connection);
		Set<String> quotes = market.findAll();
		//String[] quotes = {"600030","000723","000725","000726","000727","000728","000729","002100","000720","000721","000722","002607","600400","600401","002606","002605","002604","002609","002608","002603"};
		//System.out.println(quotes.length + quotes.toString());
		ExecutorService pool = Executors.newFixedThreadPool(100);
		for(String symbol : quotes){
			YahooHistoricalThread t = new YahooHistoricalThread("test", symbol);
			pool.execute(t);
			System.out.println(symbol);
		}
		pool.shutdown();
		System.out.println("done!!!!!!!!!!!!!!!!!!!");
		*/
		/*
		TencentAdapterComponent adapter = new TencentAdapterComponent();
		CapitalPoint point = adapter.latestQuotation("sz000858");
		System.out.println(point.toString());
		Connection connection = StockDatebaseFactory.getInstance("test");

		HushenMarket market = new HushenMarket(connection);
		market.importMarket();
		Set<String> quotes;
		quotes = market.findAll();
		System.out.println(quotes.toString());
		
		HexunConceptionAdaptor conceptAdaptor = new HexunConceptionAdaptor();
		Map<String, ArrayList<String>> gn = conceptAdaptor.getGnIndustry();
		HexunConceptionIndustryDao conceptdao = new HexunConceptionIndustryDao(connection);
		conceptdao.deleteAll();
		conceptdao.initDb();
		conceptdao.update(gn);
		*/
	}
	
	private static String httpQuery(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
//		String proxyHost = "87.254.212.120";
//		String proxyPort = "8080";
		String proxyHost = null;
		String proxyPort = null;
		HostConfiguration config = new HostConfiguration();
		if (proxyHost != null && proxyPort != null) {
			int port = Integer.parseInt(proxyPort);
			config.setProxy(proxyHost, port);
		}
		HttpMethod method = new GetMethod(url);
		int statusCode = client.executeMethod(config, method);
        if (statusCode != HttpStatus.SC_OK) {
          throw new ConnectException("Query to " + url + " failed [" + method.getStatusLine() + "]");
        }
		byte[] responseBody = method.getResponseBody();
		return new String(responseBody);
	}
}
