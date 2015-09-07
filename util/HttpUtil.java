package util;

import java.io.IOException;
import java.net.ConnectException;

import model.SymbolFormat;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class HttpUtil {
	private static final String PROXY_HOST_PROPERTY_KEY = "http.proxy.host";
	private static final String PROXY_PORT_PROPERTY_KEY = "http.proxy.port";
	
	public static String httpQuery(String url) throws HttpException, IOException {
		Logger logger = LoggerUtils.getLogger(LoggerUtils.path);
		HttpClient client = new HttpClient();
		//String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
		//String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
		String proxyHost = null;
		String proxyPort = null;
//		proxyHost = "87.254.212.120";
//		proxyPort = "8080";
		HostConfiguration config = new HostConfiguration();
		if (proxyHost != null && proxyPort != null) {
			int port = Integer.parseInt(proxyPort);
			config.setProxy(proxyHost, port);
		}
		HttpMethod method = new GetMethod(url);
		int statusCode = client.executeMethod(config, method);
		logger.info(url);
        if (statusCode != HttpStatus.SC_OK) {
        	logger.warn("Query to " + url + " failed!");
        	throw new ConnectException("Query to " + url + " failed [" + method.getStatusLine() + "]");
        }
		byte[] responseBody = method.getResponseBody();
		return new String(responseBody);
	}
}
