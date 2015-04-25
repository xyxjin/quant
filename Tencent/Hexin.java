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
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Hexin {
	private static final String hexinBaseUrl = "http://q.10jqka.com.cn/";

	public Map<String, String> fetchLinksInfo(String url) throws ConnectException{
		Map<String, String> link = new HashMap<String, String>();
		try {
			Elements scripts = fetchHtmlDoc(url).select("script");
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
		return link;
	}

	protected Document fetchHtmlDoc(String url) throws ConnectException, HttpException, IOException {
		String html = httpQuery(url);
		//System.out.println(html);
		BufferedWriter  writer = new BufferedWriter(new FileWriter("input.html"));
		writer.write(html);
		writer.close();
		File input = new File("input.html");
		return Jsoup.parse(input, "gbk", hexinBaseUrl);
	}
	
	protected JSONObject fetchHtmlBody(Map<String, String> link, int page) throws ConnectException, HttpException, IOException, JSONException {
		String nextUrl = link.get("baseurl") + link.get("url") + link.get("field") + '/' + link.get("sort") + '/' + String.valueOf(page) + '/' + link.get("type") + '/' + link.get("py");
		Document doc = fetchHtmlDoc(nextUrl);
		String body = doc.select("body").html();
		body = formatHtmlBodytoJson(body);
		JSONObject jsonObject = new JSONObject(body);
		//System.out.println(jsonObject.toString());
		return jsonObject;
	}
	
	public String formatHtmlBodytoJson(String body){
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
	
	public int subStringCount(String mainStr, String subStr)  
	{  
	     int time = 0;  
	     while(mainStr.indexOf(subStr) != -1)  
	    {  
	         time++;  
	         mainStr = mainStr.substring(mainStr.indexOf(subStr) + subStr.length());  
	    }  
	    return time;  
	}
	
	protected String httpQuery(String url) throws ConnectException, HttpException, IOException {
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
		//String proxyHost = "87.254.212.120";
		//String proxyPort = "8080";
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
