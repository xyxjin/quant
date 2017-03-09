package com.pureblue.quant.yahooAPI;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pureblue.quant.dao.IMarketDataProvider;
import com.pureblue.quant.dao.IOHLCPoint;
import com.pureblue.quant.dao.OHLCPoint;
import com.pureblue.quant.model.BarSize;
import com.pureblue.quant.model.DataType;
import com.pureblue.quant.model.IContract;
import com.pureblue.quant.model.ITaskMonitor;
import com.pureblue.quant.model.RequestFailedException;
import com.pureblue.quant.model.SecurityType;
import com.pureblue.quant.model.beans.ContractBean;
import com.pureblue.quant.model.beans.ContractDescBean;
import com.pureblue.quant.util.HttpUtil;

public class YahooFinanceAdapterComponent implements IMarketDataProvider {
    private static final String YAHOO_TICKER_QUERY_URL = "http://d.yimg.com/autoc.finance.yahoo.com/autoc?query=%s&callback=YAHOO.Finance.SymbolSuggest.ssCallback";
    private static final String YAHOO_STOCK_QUERY_URL = "http://finance.yahoo.com/q?s=%s";
    private static final String YAHOO_STOCK_PRICES_QUERY_URL = "http://ichart.finance.yahoo.com/table.csv?s=%s&a=%d&b=%d&c=%d&d=%d&e=%s&f=%d&g=%s&ignore=.csv";
    private static final String YAHOO_STOCK_PRICES_HEADER = "Date,Open,High,Low,Close,Volume,Adj Close";
    private static final String YAHOO_STOCK_PRICES_DATE_FORMAT = "yyyy-MM-dd";
    private static final String YAHOO_SYMBOL_KEY = "symbol";
    private static final String YAHOO_EXCHANGE_DISPLAY_KEY = "exchDisp";
    private static final String YAHOO_EXCHANGE_KEY = "exch";
    private static final String YAHOO_TYPE_KEY = "typeDisp";
    private static final String YAHOO_DESCRIPTION_KEY = "name";
    private static final String YAHOO_BROKER_ID = "Yahoo!";
    private static final String URL_ENCODING_ENCODING = "UTF-8";
    private final Pattern STOCK_CURRENCY_PATTERN = Pattern.compile(".*Currency in (...)\\..*", Pattern.DOTALL);
    private Logger logger;
    private String symbol;
    public static final TimeZone YAHOO_FINANCE_TIMEZONE = TimeZone.getTimeZone("UTC");

    public YahooFinanceAdapterComponent(String stockId) {
        this.symbol = stockId;
        this.logger = Logger.getLogger(getClass());
    }

    @Override
    public DataType[] availableDataTypes() {
        return new DataType[] { DataType.MIDPOINT };
    }

    @Override
    public BarSize[] availableBarSizes() {
        return new BarSize[] { BarSize.ONE_DAY, BarSize.ONE_WEEK, BarSize.ONE_MONTH };
    }

    private String addMarketNameToSymbol(String symbol) {
        if ("600000".compareTo(symbol) <= 0) {
            return symbol + ".SS";
        } else {
            return symbol + ".SZ";
        }
    }

    @Override
    public List<IContract> searchContracts(IContract criteria, ITaskMonitor taskMonitor) throws ConnectException, RequestFailedException {
        String symbol = criteria.getSymbol();
        if (symbol == null || symbol.trim().length() == 0) {
            throw new RequestFailedException("Symbol must be speficied for Yahoo! Finance ticker query");
        }
        String quotedSymbol;
        try {
            quotedSymbol = URLEncoder.encode(symbol, URL_ENCODING_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RequestFailedException("Exception encoding symbol: " + symbol, e);
        }
        String stockId = addMarketNameToSymbol(quotedSymbol);
        String queryUrl = String.format(YAHOO_TICKER_QUERY_URL, stockId);
        logger.warn("Query Yahoo!Finance for tickers: " + queryUrl);
        JSON response = null;
        try {
            String responseString = HttpUtil.httpQuery(queryUrl);
            String jsonResponse = responseString.replace("YAHOO.Finance.SymbolSuggest.ssCallback(", "").replace(")", "");
            Reader responseReader = new StringReader(jsonResponse);
            logger.info("Response from Yahoo!Finance: " + jsonResponse);
            response = JSON.parse(responseReader);
        } catch (IOException e) {
            throw new ConnectException("Exception while connecting to: " + queryUrl + " [" + e.getMessage() + "]");
        } catch (JSONException e) {
            throw new RequestFailedException("Exception parsing response data from: " + queryUrl, e);
        }
        @SuppressWarnings("unchecked")
        List<JSON> securityList = (List<JSON>) response.get("ResultSet").get("Result").getValue();
        List<IContract> contractList = new LinkedList<IContract>();
        for (JSON security : securityList) {
            String ticker = (String) security.get(YAHOO_SYMBOL_KEY).getValue();
            logger.info("Query information about stock: " + ticker);
            Currency stockCurrency = null;
            try {
                StockInfo stockInfo = getStockInfo(ticker);
                stockCurrency = stockInfo.currency;
            } catch (Exception e) {
                logger.info("Exception while querying currency for: " + ticker, e);
                continue;
            }
            if (criteria.getCurrency() != null && !criteria.getCurrency().equals(stockCurrency)) {
                continue;
            }
            ContractBean contract = new ContractBean();
            contract.setSymbol(ticker);
            contract.setExchange(extractExchange(security));
            contract.setSecurityType(decodeSecurityType(extractSecurityType(security)));
            contract.setCurrency(stockCurrency);
            ContractDescBean description = new ContractDescBean();
            description.setLongName(extractDescription(security));
            description.setTimeZone(YAHOO_FINANCE_TIMEZONE);
            contract.setContractDescription(description);
            contract.setBrokerID(YAHOO_BROKER_ID);
            contractList.add(contract);
        }
        return contractList;
    }

    private String extractSecurityType(JSON security) {
        String type = null;
        JSON typeNode = security.get(YAHOO_TYPE_KEY);
        if (typeNode != null) {
            type = (String) typeNode.getValue();
        }
        return type;
    }

    private String extractExchange(JSON security) {
        String exchange = "UNKNOWN";
        JSON exchangeNode = security.get(YAHOO_EXCHANGE_DISPLAY_KEY);
        if (exchangeNode == null) {
            exchangeNode = security.get(YAHOO_EXCHANGE_KEY);
        }
        if (exchangeNode != null) {
            exchange = (String) exchangeNode.getValue();
        }
        return exchange;
    }

    private String extractDescription(JSON security) {
        String description = "";
        JSON descNode = security.get(YAHOO_DESCRIPTION_KEY);
        if (descNode != null) {
            description = (String) descNode.getValue();
        }
        return description;
    }

    @Override
    public List<IOHLCPoint> historicalBars(IContract contract, Date startDateTime, Date endDateTime, BarSize barSize, DataType dataType, boolean includeAfterHours,
            ITaskMonitor taskMonitor) {
        logger.info("YahooFinanceAdapterComponent::historicalBars: Query Yahoo!Finance for " + symbol + "historical prices entry.");
        Calendar cal = Calendar.getInstance(YAHOO_FINANCE_TIMEZONE);
        cal.setTime(startDateTime);
        int startDay = cal.get(Calendar.DATE);
        int startMonth = cal.get(Calendar.MONTH);
        int startYear = cal.get(Calendar.YEAR);
        cal.setTime(endDateTime);
        int endDay = cal.get(Calendar.DATE);
        int endMonth = cal.get(Calendar.MONTH);
        int endYear = cal.get(Calendar.YEAR);
        String quotedSymbol;
        try {
            quotedSymbol = URLEncoder.encode(contract.getSymbol(), URL_ENCODING_ENCODING);
        } catch (UnsupportedEncodingException e) {
            logger.warn("YahooFinanceAdapterComponent::historicalBars: UnsupportedEncodingException encoding symbol: " + contract.getSymbol() + " with error info " + e.toString());
            return null;
        }
        String stockId = addMarketNameToSymbol(quotedSymbol);
        String queryUrl = String.format(YAHOO_STOCK_PRICES_QUERY_URL, stockId, startMonth, startDay, startYear, endMonth, endDay, endYear, encodeBarSize(barSize));
        logger.info("YahooFinanceAdapterComponent::historicalBars: Query Yahoo!Finance for historical prices: " + queryUrl);
        String responseString = HttpUtil.httpQuery(queryUrl);
        if(null == responseString){
            logger.warn("YahooFinanceAdapterComponent::historicalBars: Received null http RSP for symbol=" + symbol);
            return null;
        }
        String[] lines = responseString.split("\\n");
        if (!lines[0].equals(YAHOO_STOCK_PRICES_HEADER)) {
            logger.warn("YahooFinanceAdapterComponent::historicalBars: Response format not recognized: " + responseString.substring(0, 200) + " for symbol=" + contract.getSymbol());
            return null;
        }
        logger.info("YahooFinanceAdapterComponent::historicalBars: Received " + (lines.length - 1) + " lines");
        List<IOHLCPoint> points = new LinkedList<IOHLCPoint>();
        DateFormat dateFormat = new SimpleDateFormat(YAHOO_STOCK_PRICES_DATE_FORMAT);
        for (int lineNo = lines.length - 1; lineNo > 0; lineNo--) {
            IOHLCPoint point = parsePriceLine(lines[lineNo], barSize, dateFormat);
            points.add(point);
        }
        logger.info("YahooFinanceAdapterComponent::historicalBars: Query Yahoo!Finance for " + symbol + "historical prices exit!");
        return points;
    }

    private SecurityType decodeSecurityType(String code) {
        if ("Future".equals(code)) {
            return SecurityType.FUT;
        } else if ("Index".equals(code)) {
            return SecurityType.IND;
        } else {
            return SecurityType.STK;
        }
    }

    private String encodeBarSize(BarSize barSize){
        String code = null;
        switch (barSize) {
        case ONE_DAY:
            code = "d";
            break;
        case ONE_WEEK:
            code = "w";
            break;
        case ONE_MONTH:
            code = "m";
            break;
        default:
            logger.warn("YahooFinanceAdapterComponent::encodeBarSize: Price from Yahoo!Finance are only in daily, weekly, monthly periods for symbol=" + symbol + ".");
        }
        return code;
    }

    private OHLCPoint parsePriceLine(String line, BarSize barSize, DateFormat dateFormat){
        OHLCPoint point = null;
        String[] tokens = line.split(",");
        if (tokens.length != 7) {
            logger.warn("YahooFinanceAdapterComponent::parsePriceLine: Yahoo! stock=" + symbol + "history received invalid line: " + line);
            return null;
        }
        try {
            Date date;
            date = dateFormat.parse(tokens[0]);
            Double open = Double.parseDouble(tokens[1]);
            Double high = Double.parseDouble(tokens[2]);
            Double low = Double.parseDouble(tokens[3]);
            Double close = Double.parseDouble(tokens[4]);
            Long volume = Long.parseLong(tokens[5]);
            Double adjClose = Double.parseDouble(tokens[6]);
            point = new OHLCPoint(barSize, date, open, high, low, close, volume, adjClose, (open + close) / 2, 1);
        } catch (ParseException e) {
            logger.warn("YahooFinanceAdapterComponent::parsePriceLine: Error while parsing line: " + line + " for symbol=" + symbol + " with error info " + e.toString());
        }
        return point;
    }

    private class StockInfo {
        StockInfo(Currency currency) {
            this.currency = currency;
        }

        Currency currency;
    }

    private StockInfo getStockInfo(String symbol){
        String quotedSymbol;
        try {
            quotedSymbol = URLEncoder.encode(symbol, URL_ENCODING_ENCODING);
        } catch (UnsupportedEncodingException e) {
            logger.warn("YahooFinanceAdapterComponent::getStockInfo: Exception encoding symbol: " + symbol + " with error info " + e.toString());
            return null;
        }
        String stockId = addMarketNameToSymbol(quotedSymbol);
        String queryUrl = String.format(YAHOO_STOCK_QUERY_URL, stockId);
        String responseString = HttpUtil.httpQuery(queryUrl);
        Matcher m = STOCK_CURRENCY_PATTERN.matcher(responseString);
        if (!m.matches()) {
            logger.warn("YahooFinanceAdapterComponent::getStockInfo: HTTP response doesn't match currency pattern: " + responseString + " for stock: " + symbol);
            return null;
        }
        String currencyCode = m.group(1);
        return new StockInfo(Currency.getInstance(currencyCode));
    }
}
