package com.pureblue.quant.TencentAPI;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPoint;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.STKktype;
import com.pureblue.quant.model.TimePeriod;
import com.pureblue.quant.util.HttpUtil;

public class TencentHistoryAdaptor {
    private static final String STOCKS_QUERY_URL = "http://data.gtimg.cn/flashdata/hushen/daily/%s/%s.js";
    //http://web.ifzq.gtimg.cn/appstock/app/kline/kline?_var=kline_day2007&param=sh600030,day,2007-01-01,2008-12-31,640,&r=0.9766976774371002
    private static final String NEW_TT_STOCKS_QUERY_URL = "http://web.ifzq.gtimg.cn/appstock/app/kline/kline?_var=kline_day%d&param=%s,day,%d-01-01,%d-12-31,640,&r=0.%s";
    private static final String TT_M30_STOCKS_QUERY_URL = "http://web.ifzq.gtimg.cn/appstock/app/kline/mkline?param=%s,%s,,640&_var=%s_today&r=0.%s";
    private static final String STOCK_CODE_PATTERN = "([0-9]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9\\.]+) ([0-9]+)";
    private static final String TENCENT_STOCK_PRICES_DATE_FORMAT = "yyMMdd";
    private static final String TENCENT_STOCK_M30_PRICES_DATE_FORMAT = "yyyyMMddHHmm";
    private static final String NEW_TENCENT_STOCK_PRICES_DATE_FORMAT = "yyyy-MM-dd";
//    private static final String[] defaultYears = {"2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
    private static final String[] defaultYears = {"2003", "2005", "2007", "2009", "2011", "2013", "2015", "2017"};
    private ArrayList<String> years = null;
    private Logger logger;

    public TencentHistoryAdaptor(){
        years = new ArrayList<String>();
        for(String item:defaultYears)
            years.add(item);
        logger = Logger.getLogger(TencentHistoryAdaptor.class);
    }

    public String _random(int strLength) {
        Random rm = new Random();
        String randomStr = "";
        int[] number = new int[strLength];
        for (int i = 0; i < strLength; i++) {
            number[i] = rm.nextInt(10);
            if(0 == number[strLength-1])
                number[strLength-1] = 1;
            randomStr = randomStr + Integer.toString(number[i]);
        }
        return randomStr;
    }

    public List<IOHLCPoint> historicalBars(String symbol) throws IOException {
        logger.debug("TencentHistoryAdaptor::historicalBars for " + symbol + " entry!");
        List<IOHLCPoint> point = null;
        int preYear, nextYear;
        List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
        for(String item:years){
//            String yearAbbr = TimePeriod.yearToAbbr(item);
//            String url = String.format(STOCKS_QUERY_URL, yearAbbr, symbol);
            int yearInt = Integer.parseInt(item);
            if(yearInt%2 == 0)
                preYear = yearInt-1;
            else
                preYear = yearInt;
            nextYear = preYear + 1;
            String url = String.format(NEW_TT_STOCKS_QUERY_URL, preYear, symbol, preYear, nextYear, _random(17));
            logger.info("TencentHistoryAdaptor::historicalBars http request: " + url.toString());
            try{
                String body = HttpUtil.httpQuery(url);
//                point = parsePriceLines(body);
                points = parseKline(body, symbol);
                points.addAll(point);
            }catch(Exception e){
                logger.debug("TencentHistoryAdaptor::historicalBars http request exception " + e.toString() + " for " + symbol);
            }
        }
        logger.debug("TencentHistoryAdaptor::historicalBars for " + symbol + " exit!");
        return points;
    }

    private String stripOneChar(String source) {
        return source.substring(1, source.length()-1);
    }

    private String trimFirstAndLastChar(String source, char element) {
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do{
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

    private List<IOHLCPoint> parseKline(String body, String symbol) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(NEW_TENCENT_STOCK_PRICES_DATE_FORMAT);
        List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
        String[] array = body.split("=", 2);
        JSONObject jObject = new JSONObject(array[1]);
        JSONArray jArray = jObject.getJSONObject("data").getJSONObject(symbol).getJSONArray("day");
        for(int i=0; i<jArray.length(); i++) {
            String item = jArray.get(i).toString();
            String[] pointStr= stripOneChar(item).split(",");
            Date date;
            date = dateFormat.parse(trimFirstAndLastChar(pointStr[0], '"'));
            OHLCPoint point = new OHLCPoint(BarSize.ONE_DAY,
                                            date,
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[1], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[3], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[4], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[2], '"')),
                                            (long)Double.parseDouble(trimFirstAndLastChar(pointStr[5], '"')),
                                            Double.parseDouble("0"),
                                            Double.parseDouble("0"),
                                            1);
            points.add(point);
        }
        return points;
    }

    private List<IOHLCPoint> parseM30Kline(String body, String symbol) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(TENCENT_STOCK_M30_PRICES_DATE_FORMAT);
        List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();

//        System.out.println(body);

        String[] array = body.split("=", 2);
        JSONObject jObject = new JSONObject(array[1]);
        JSONArray jArray = jObject.getJSONObject("data").getJSONObject(symbol).getJSONArray("m30");
//        System.out.println(jArray.toString());

        for(int i=0; i<jArray.length(); i++) {
            String item = jArray.get(i).toString();
            String[] pointStr= stripOneChar(item).split(",");
            Date date;
            date = dateFormat.parse(trimFirstAndLastChar(pointStr[0], '"'));
            OHLCPoint point = new OHLCPoint(BarSize.THIRTY_MINS,
                                            date,
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[1], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[3], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[4], '"')),
                                            Double.parseDouble(trimFirstAndLastChar(pointStr[2], '"')),
                                            (long)Double.parseDouble(trimFirstAndLastChar(pointStr[5], '"')),
                                            Double.parseDouble("0"),
                                            Double.parseDouble("0"),
                                            1);
            points.add(point);
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

    public void setYears(String year){
        years.add(year);
    }

    public List<IOHLCPoint> updateLatestBar(String symbol, STKktype ktype, Date startDateTime, Date endDateTime) throws HttpException, IOException, ParseException{
        List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
        List<IOHLCPoint> selectPoints = new LinkedList<IOHLCPoint>();
        String url = null;
        switch(ktype) {
            case DAY:
                {
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    endCalendar.setTime(endDateTime);
//                    System.out.println(startCalendar.get(Calendar.YEAR));
                    startCalendar.setTime(startDateTime);
//                    System.out.println(endCalendar.get(Calendar.YEAR));

                    int year = startCalendar.get(Calendar.YEAR);
//                    System.out.println("out: " + year);
                    while(year <= endCalendar.get(Calendar.YEAR))
                    {
//                        System.out.println("in");
                        int preYear = 0;
                        int nextYear = 0;
//                        String year = TimePeriod.currentYearToAbbr();
//                        int yearInt = Integer.parseInt(year);

                        if(year%2 == 0)
                            preYear = year-1;
                        else
                            preYear = year;
                        nextYear = preYear + 1;
                        url = String.format(NEW_TT_STOCKS_QUERY_URL, preYear, symbol, preYear, nextYear, _random(17));
//                        System.out.println(url);
                        String body = HttpUtil.httpQuery(url);
//                        points = parseKline(body, symbol);
                        points.addAll(parseKline(body, symbol));
                        year = year + 2;
                    }

                }
                break;
            case M30:
                url = String.format(TT_M30_STOCKS_QUERY_URL, symbol, STKktype.M30.toString(), STKktype.M30.toString(), _random(17));
                logger.info("TencentHistoryAdaptor::historicalBars http request: " + url.toString());
                String body = HttpUtil.httpQuery(url);
                points = parseM30Kline(body, symbol);
                break;
            default:
                break;
        }
//        System.out.println(points.size());
        logger.info("TencentHistoryAdaptor::historicalBars http request: " + url.toString());
        try{
//            String body = HttpUtil.httpQuery(url);
//            points = parseM30Kline(body, symbol);
            Iterator<IOHLCPoint> itr = points.iterator();
            while(itr.hasNext()){
                OHLCPoint item = (OHLCPoint) itr.next();
//                System.out.println(item.getIndex().toLocaleString());
//                System.out.println(item.getIndex().toLocaleString());
                //&& endDateTime.compareTo(item.getIndex())>=0
                if(startDateTime.compareTo(item.getIndex())<0 && endDateTime.compareTo(item.getIndex())>=0)
                    selectPoints.add(item);
            }
        }catch(Exception e){
            logger.debug("TencentHistoryAdaptor::updateLatestBar http request exception " + e.toString() + " for " + symbol);
        }
        logger.info("TencentHistoryAdaptor::updateLatestBar " + selectPoints.size() + " points will be updated");
        return selectPoints;
    }

    public List<IOHLCPoint> updateLatestBar(String symbol, Date startDateTime, Date endDateTime, BarSize barSize) throws IOException {
        logger.debug("TencentHistoryAdaptor::updateLatestBar for " + symbol + " entry!");
        List<IOHLCPoint> points = null;
        List<IOHLCPoint> selectPoints = new LinkedList<IOHLCPoint>();
        int preYear = 0;
        int nextYear = 0;
        String year = TimePeriod.currentYearToAbbr();
        int yearInt = Integer.parseInt(year);

        if(yearInt%2 == 0)
            preYear = yearInt-1;
        else
            preYear = yearInt;
        nextYear = preYear + 1;
        String url = String.format(NEW_TT_STOCKS_QUERY_URL, preYear, symbol, preYear, nextYear, _random(17));
        logger.info("TencentHistoryAdaptor::historicalBars http request: " + url.toString());
        try{
            String body = HttpUtil.httpQuery(url);
            points = parseKline(body, symbol);
            Iterator<IOHLCPoint> itr = points.iterator();
            while(itr.hasNext()){
                OHLCPoint item = (OHLCPoint) itr.next();
                if(startDateTime.compareTo(item.getIndex())<=0 && endDateTime.compareTo(item.getIndex())>=0)
                    selectPoints.add(item);
            }
        }catch(Exception e){
            logger.debug("TencentHistoryAdaptor::updateLatestBar http request exception " + e.toString() + " for " + symbol);
        }
        logger.debug("TencentHistoryAdaptor::updateLatestBar for " + symbol + " exit!");
        return selectPoints;
    }
}
