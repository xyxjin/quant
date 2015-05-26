package Tencent;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.FundType;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TencentFundAdaptor {
	private static final String tencentFundUrl1[] = {"http://stockqt.gtimg.cn/cgi-bin/hcenter/q?id=501"};
	private static final String tencentFundUrl2[] = {"http://stock.gtimg.cn/data/index.php?appn=rank&t=rankclosefund/chr", "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranketf/chr", "http://stock.gtimg.cn/data/index.php?appn=rank&t=ranklof/chr"};
	private static final String fundDetailBaseUrl = "http://web.ifzq.gtimg.cn/fund/newfund/fundInvesting/getInvesting?app=web&symbol=";
	public static void main(String[] args){

		TencentFundAdaptor fund = new TencentFundAdaptor();
		Collection<IFund> mapping = fund.fetchAllFunds();
		System.out.println(mapping.toString());
	}
	
	public List<IFund> fetchAllFunds(){
		String body, url;
		int count = 0;
		List<IFund> stocks = new LinkedList<IFund>();
		Collection<String> funds = fetchAllFundIndex();
//		Collection<String> funds = new ArrayList<String>();
//		funds.add("jj003003");
//		funds.add("jj690001");
//		funds.add("jj590002");
		for(String fund : funds){
			url = fundDetailBaseUrl + fund;
			try {
				System.out.println(url);
				body = httpQuery(url);
				stocks.addAll(parsePerFundDetail(body));
			} catch (ConnectException | HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(count++ == 20)
				break;
		}
		return stocks;
	}
	
	@SuppressWarnings("null")
	private Collection<String> fetchAllFundIndex(){
		Collection<String> fundList = null;
		try {
			Collection<String> openFunds = fetchOpenFundIndex(tencentFundUrl1);
			fundList = openFunds;
			Collection<String> closeFunds = fetchCloseFundIndex(tencentFundUrl2);
			fundList.addAll(closeFunds);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
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
					body = httpQuery(url);
					jsonObject = new JSONObject(body.split("=")[1]);
					total = jsonObject.getInt("total");
					currentPage = jsonObject.getInt("p");
					String[] funds = jsonObject.getString("data").split(",");
					for(int j=0; j<funds.length; j++)
						fundList.add(funds[j]);
					url = baseUrl[i] + "&p=" + String.valueOf(currentPage+1);
				}while(currentPage<total);
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException | JSONException e) {
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
			try {
				do{
					//System.out.println(url);
					body = httpQuery(url);
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
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fundList;
	}
	
	private Collection<IFund> parsePerFundDetail(String body){
		Collection<IFund> stocks = new LinkedList<IFund>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(body);
			JSONObject basicInfo = jsonObject.getJSONObject("data");		
			String symbol = basicInfo.getString("symbol");
			String date = basicInfo.getString("date");
			int type = basicInfo.getInt("type");
			if(FundType.STOCK.value()!=type){
				return stocks;
			}
			System.out.println(symbol + ' ' + date + ' ' + type);
			
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
				IFund stock = new FundStocks(symbol, date, code, count, total_assets, ratio);
				stocks.add(stock);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stocks;
	}
	
	private String httpQuery(String url) throws HttpException, IOException, ConnectException {
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
		String proxyHost = null;
		String proxyPort = null;
		proxyHost = "87.254.212.120";
		proxyPort = "8080";
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
