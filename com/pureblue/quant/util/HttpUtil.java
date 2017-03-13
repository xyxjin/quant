package com.pureblue.quant.util;

import java.lang.Exception;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class HttpUtil {
    public static final String PROXY_HOST_PROPERTY_KEY = "http.proxy.host";
    public static final String PROXY_PORT_PROPERTY_KEY = "http.proxy.port";

    public static String httpQuery(String url) {
        Logger logger = Logger.getLogger(HttpUtil.class);
        logger.debug("HttpUtil::httpQuery: http query the url entry: " + url);
        HttpClient client = new HttpClient();
        String proxyHost="", proxyPort="";
        try {
            proxyHost = ConfigPropValue.getPropValue(HttpUtil.PROXY_HOST_PROPERTY_KEY);
            proxyPort = ConfigPropValue.getPropValue(HttpUtil.PROXY_PORT_PROPERTY_KEY);
        } catch (Exception e) {
            logger.warn("HttpUtil::httpQuery: fail to get proxy configuation for url=" + url + " with error:" + e.toString());
        }
        
        HostConfiguration config = new HostConfiguration();
        if (!proxyHost.isEmpty() && !proxyPort.isEmpty()) {
            int port = Integer.parseInt(proxyPort);
            config.setProxy(proxyHost, port);
        }
        HttpMethod method = new GetMethod(url);
        String httpRsp = null;
        try {
            int statusCode = client.executeMethod(config, method);
            if (statusCode != HttpStatus.SC_OK)
                logger.error("HttpUtil::httpQuery: http Query to " + url + " failed with status code = " + statusCode);
            httpRsp = method.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error("HttpUtil::httpQuery: http exception for " + url + " with error:" + e.toString());
        }
        if(null == httpRsp)
            logger.error("HttpUtil::httpQuery: http response is null for " + url);
        logger.debug("HttpUtil::httpQuery: http query the url:" + url + " exit!");
        
        return httpRsp;
    }
}
