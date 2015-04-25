package Tencent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HexinConceptionAdaptor {

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
		String url = "http://q.10jqka.com.cn/stock/thshy/";
		//url = "http://q.10jqka.com.cn/stock/thshy/jchy/";
		url = "http://q.10jqka.com.cn/stock/thshy/zq/";
		Map<String, String> link = new HashMap<String, String>();
		try {
			String html = httpQuery(url);
			//System.out.println(html);
			BufferedWriter  writer = new BufferedWriter(new FileWriter("input.html"));
		    writer.write(html);
		    writer.close();
		    
			File input = new File("input.html");
			Document doc = Jsoup.parse(input, "gbk", "http://q.10jqka.com.cn/");
			Elements scripts = doc.select("script");
			//System.out.println(scripts.toString());
			for(Element script : scripts){
				Pattern p = Pattern.compile("type : '([^']+)',\\s+page : '([0-9]+)',\\s+last : '([0-9]+)',\\s+field : '([^']+)',\\s+sort : '([^']+)',\\s+baseurl : '([^']+)',\\s+url	: '([^']+)',\\s+py	: '([^']+)'\\s", Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(script.html());
				if(m.find()){
					link.put("type", m.group(1));
					link.put("page", m.group(2));
					link.put("last", m.group(3));
					link.put("field", m.group(4));
					link.put("sort", m.group(5));
					link.put("baseurl", m.group(6));
					link.put("url", m.group(7));
					link.put("py", m.group(8));
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(link.toString());
		String nextUrl = null;
		int page = 1;
		//for(page=Integer.parseInt(link.get("page")); page<=Integer.parseInt(link.get("last")); page++){
		for(page=2; page<=2; page++){
			
			nextUrl = link.get("baseurl") + link.get("url") + link.get("field") + '/' + link.get("sort") + '/' + String.valueOf(page) + '/' + link.get("type") + '/' + link.get("py");
			System.out.println(nextUrl);
			String html = httpQuery(nextUrl);
			BufferedWriter  writer = new BufferedWriter(new FileWriter("input1.html"));
		    writer.write(html);
		    writer.close();
			
			File input = new File("input1.html");
			Document doc = Jsoup.parse(input, "gbk", "http://q.10jqka.com.cn/");
			//Document doc = Jsoup.connect(nextUrl).timeout(2000).get();
			String body = doc.select("body").html();
			body = formatHtmlBodytoJson(body);
			JSONObject jsonObject = new JSONObject(body);
			System.out.println(jsonObject.toString());
			Object type = jsonObject.get("data");
			//String type = jsonObject.getJSONArray("data").toString();
			System.out.println(type.toString());
			/*
			if(type.toString() != "null"){
				JSONArray plateList = jsonObject.getJSONArray("data");
				JSONObject item = plateList.getJSONObject(0);
				
				String platecode = item.getString("platecode");
				String hycode = item.getString("hycode");
				System.out.println(platecode);
				System.out.println(hycode);
			}
			*/
		}
	}
	
	public static String formatHtmlBodytoJson(String body){
		String PATTERN = "\\s*<a[^>]*>([^<]|<(?!/a))*</a>\\s*";
		Pattern COMPILED_PATTERN = Pattern.compile(PATTERN,  Pattern.CASE_INSENSITIVE);
		Matcher matcher = COMPILED_PATTERN.matcher(body);
		body = matcher.replaceAll("");
		int quoteCount = subStringCount(body,"\"");
		int leftBraceCount = subStringCount(body,"{");
		int rightBraceCount = subStringCount(body,"}");
		body = body.replace("\\", "");
		body = body.replace("\"[", "[");
		body = body.replace("]\"", "]");
		if(quoteCount%2 == 1)
			body = body + "\"";
		int braces = leftBraceCount - rightBraceCount;
		while(braces>0){
			body = body + "}";
			braces--;
		}		
		return body.replace("\\u", "");
	}

	public static int subStringCount(String mainStr, String subStr)  
	{  
	     int time = 0;  
	     while(mainStr.indexOf(subStr) != -1)  
	    {  
	         time++;  
	         mainStr = mainStr.substring(mainStr.indexOf(subStr) + subStr.length());  
	    }  
	    return time;  
	}
	
	private static String httpQuery(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
		String proxyHost = "87.254.212.120";
		String proxyPort = "8080";
		//String proxyHost = null;
		//String proxyPort = null;
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
