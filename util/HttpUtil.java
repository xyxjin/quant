package util;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class HttpUtil {
    public static final String PROXY_HOST_PROPERTY_KEY = "http.proxy.host";
    public static final String PROXY_PORT_PROPERTY_KEY = "http.proxy.port";

    public static String httpQuery(String url) {
        Logger logger = Logger.getLogger(HttpUtil.class);
        logger.info("HttpUtil::httpQuery: http query the url entry: " + url);
        HttpClient client = new HttpClient();
        String proxyHost = System.getProperty(PROXY_HOST_PROPERTY_KEY);
        String proxyPort = System.getProperty(PROXY_PORT_PROPERTY_KEY);
        HostConfiguration config = new HostConfiguration();
        if (!proxyHost.isEmpty() && !proxyPort.isEmpty()) {
            int port = Integer.parseInt(proxyPort);
            config.setProxy(proxyHost, port);
        }
        HttpMethod method = new GetMethod(url);
        try {
            int statusCode = client.executeMethod(config, method);
            if (statusCode != HttpStatus.SC_OK)
                logger.error("HttpUtil::httpQuery: http Query to " + url
                        + " failed with status code = " + statusCode);
        } catch (HttpException e) {
            logger.error("HttpUtil::httpQuery: http exception for " + url);
        } catch (IOException e) {
            logger.error("HttpUtil::httpQuery: http IO exception for " + url);
        }

        byte[] responseBody = null;
        try {
            responseBody = method.getResponseBody();
        } catch (IOException e) {
            logger.error("HttpUtil::httpQuery: http response handle IO exception for " + url + " with error info " + e.toString());
        }
        String httpRsp = null;
        if(null != responseBody)
            httpRsp = new String(responseBody);
        else
            logger.error("HttpUtil::httpQuery: http response is null for " + url);
        logger.info("HttpUtil::httpQuery: http query the url:" + url + " exit!");
        return httpRsp;
    }
}
