package Tencent;

import java.io.*;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.BarSize;
import model.TimePeriod;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;



import dao.IOHLCPoint;
import dao.OHLCPoint;

public class TencentHistoryAdaptor {
	private static final String STOCKS_QUERY_URL = "http://data.gtimg.cn/flashdata/hushen/daily/%s/%s.js";
	private static final String STOCK_CODE_PATTERN = "([0-9]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9]+)";
	private static final String TENCENT_STOCK_PRICES_DATE_FORMAT = "yyMMdd";
	private static final String[] defaultYears = {"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015"};
	private ArrayList<String> years = null;
	
	public static void main(String[] args)  throws Exception{
		List<IOHLCPoint> points = null;
		TencentHistoryAdaptor adaptor = new TencentHistoryAdaptor();
		//points = adaptor.historicalBars("sh000001");
		Date startDateTime = TimePeriod.formatStringToDate("2015-3-2");
		Date endDateTime = TimePeriod.formatStringToDate("2015-4-1");
		points = adaptor.updateLatestBar("sh000001", startDateTime, endDateTime, BarSize.ONE_DAY);
		System.out.println(points.toString());
		//Map<String, ArrayList<String>> industry = adaptor.historicalBars();
		//System.out.println(industry.toString());
	}
	
	public TencentHistoryAdaptor(){
		years = new ArrayList<String>();
		for(String item:defaultYears)
			years.add(item);		
	}
	
	public void setYears(String year){
		years.add(year);
	}
	
	public List<IOHLCPoint> updateLatestBar(String symbol, Date startDateTime, Date endDateTime, BarSize barSize) throws IOException {
		List<IOHLCPoint> points = null;
		List<IOHLCPoint> selectPoints = new LinkedList<IOHLCPoint>();
		String year = TimePeriod.currentYearToAbbr();
		String url = String.format(STOCKS_QUERY_URL, year, symbol);
		String body = null;
		try{
			body = httpQuery(url);
			//System.out.println(body);
		    points = parsePriceLines(body);
		    Iterator<IOHLCPoint> itr = points.iterator();
		    while(itr.hasNext()){
		    	OHLCPoint item = (OHLCPoint) itr.next();	    	
		    	if(startDateTime.compareTo(item.getIndex())<=0 && endDateTime.compareTo(item.getIndex())>=0)
		    		selectPoints.add(item);
		    }
		}catch(IOException e){
			
		}
		return selectPoints;
	}
	
	public List<IOHLCPoint> historicalBars(String symbol) throws IOException {
		List<IOHLCPoint> point = null;
		List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
		String url = null;
		String body = null;
		for(String item:years){
			String yearAbbr = TimePeriod.yearToAbbr(item);
			url = String.format(STOCKS_QUERY_URL, yearAbbr, symbol);
			System.out.println(url);
			try{
				body = httpQuery(url);
			    point = parsePriceLines(body);
			    points.addAll(point);
			}catch(IOException e){
				continue;
			}
		}
		return points;
	}
	
	private List<IOHLCPoint> parsePriceLines(String body){
		DateFormat dateFormat = new SimpleDateFormat(TENCENT_STOCK_PRICES_DATE_FORMAT);
		List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
		Pattern p = Pattern.compile(STOCK_CODE_PATTERN);
		Matcher m = p.matcher(body);
		while(m.find()){
			try {
				Date date;
				date = dateFormat.parse(m.group(1));
				OHLCPoint point = new OHLCPoint(BarSize.ONE_DAY, date, Double.parseDouble(m.group(2)), Double.parseDouble(m.group(4)), Double.parseDouble(m.group(5)), Double.parseDouble(m.group(3)), Long.parseLong(m.group(6)), Double.parseDouble("0"), Double.parseDouble("0"), 1);
				points.add(point);
			} catch (ParseException e) {
			}
		}
		return points;
	}
	
	private String httpQuery(String url) throws IOException {
		String body = null;
		HttpClient httpClient = new HttpClient();
		GetMethod method = new GetMethod(url);
		try {
			int statusCode = httpClient.executeMethod(method);
	        if (statusCode != HttpStatus.SC_OK) {
	          throw new ConnectException("Query to " + url + " failed [" + method.getStatusLine() + "]");
	        }
	        body = method.getResponseBodyAsString();
		}catch (HttpException e){
			e.printStackTrace();
		}finally{
			method.releaseConnection();
			method.setRequestHeader("Connection", "close"); 
		}
		return body;
	}
}
