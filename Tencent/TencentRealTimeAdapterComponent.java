package Tencent;

import java.io.IOException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import model.BarSize;
import model.DataType;
import model.IContract;
import model.ITaskMonitor;
import model.RequestFailedException;
import dao.CapitalPoint;
import dao.IMarketDataProvider;
import dao.IOHLCPoint;

public class TencentRealTimeAdapterComponent implements IMarketDataProvider {
	private static final Logger logger = Logger.getLogger("TencentFinanceAdapterComponent");
	private static final String TENCENT_STOCK_DATE_FORMAT = "yyyyMMdd";
	private static final String TENCENT_STOCK_PRICES_QUERY_URL = "http://qt.gtimg.cn/q=%s";
	private static final String TENCENT_STOCK_CAPITAL_QUERY_URL = "http://qt.gtimg.cn/q=ff_%s"; 

	public TencentRealTimeAdapterComponent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DataType[] availableDataTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BarSize[] availableBarSizes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IContract> searchContracts(IContract criteria,
			ITaskMonitor taskMonitor) throws ConnectException,
			RequestFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IOHLCPoint> historicalBars(IContract contract,
			Date startDateTime, Date endDateTime, BarSize barSize,
			DataType dataType, boolean includeAfterHours,
			ITaskMonitor taskMonitor) throws ConnectException,
			RequestFailedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static CapitalPoint parseCapitalLine(String capitalInfo, String basicInfo, DateFormat dateFormat) throws ParseException{
		String[] tokens = capitalInfo.split("~");
		if (tokens.length < 13) {
			throw new IllegalArgumentException("Received invalid line: " + capitalInfo);
		}
		Date date = dateFormat.parse(tokens[13]);
		Double mainInflow = Double.parseDouble(tokens[1]);
		Double mainOutflow = Double.parseDouble(tokens[2]);
		Double mainNetflow = Double.parseDouble(tokens[3]);
		Double mainRate = Double.parseDouble(tokens[4]);
		Double retailInflow = Double.parseDouble(tokens[5]);
		Double retailOutflow = Double.parseDouble(tokens[6]);
		Double retailNetflow = Double.parseDouble(tokens[7]);
		Double retailRate = Double.parseDouble(tokens[8]);
		Double volumn = Double.parseDouble(tokens[9]);
		tokens = basicInfo.split("~");
		Double price = Double.parseDouble(tokens[3]);
		Double turnoverRate = Double.parseDouble(tokens[38]);
		Double PER = Double.parseDouble(tokens[39]);
		Double marketValue = Double.parseDouble(tokens[44]);
		Double totalValue = Double.parseDouble(tokens[45]);
		Double PBR = Double.parseDouble(tokens[46]);
		CapitalPoint point = new CapitalPoint(date, mainInflow, mainOutflow, mainNetflow, mainRate, retailInflow, retailOutflow, 
				                              retailNetflow, retailRate, volumn, price, turnoverRate, PER, marketValue, totalValue, PBR);
		return point;
	}
	
	public CapitalPoint latestQuotation(String quotedSymbol)throws ConnectException, ParseException{
		String queryCapitalUrl = String.format(TENCENT_STOCK_CAPITAL_QUERY_URL, quotedSymbol);
		String queryPriceUrl = String.format(TENCENT_STOCK_PRICES_QUERY_URL, quotedSymbol);
		DateFormat dateFormat = new SimpleDateFormat(TENCENT_STOCK_DATE_FORMAT);
		logger.log(Level.INFO, "Query Tencent Finance for historical prices: " + queryCapitalUrl);
		String rspCapitalString;
		String rspPriceString;
		try {
			rspCapitalString = httpQuery(queryCapitalUrl);
			rspPriceString = httpQuery(queryPriceUrl);
		} catch (IOException e) {
			throw new ConnectException("Exception while connecting to: " + queryCapitalUrl + "or" + queryPriceUrl + " [" + e.getMessage() + "]");
		}
		return parseCapitalLine(rspCapitalString, rspPriceString, dateFormat);
	}
	
	private String httpQuery(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
		HostConfiguration config = new HostConfiguration();
		HttpMethod method = new GetMethod(url);
		int statusCode = client.executeMethod(config, method);
        if (statusCode != HttpStatus.SC_OK) {
          throw new ConnectException("Query to " + url + " failed [" + method.getStatusLine() + "]");
        }
		byte[] responseBody = method.getResponseBody();
		return new String(responseBody);
	}

}
