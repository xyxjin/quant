package com.pureblue.quant.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pureblue.quant.util.HttpUtil;
import com.pureblue.quant.model.SymbolFormat;

public class HushenMarketQuotes {

    public static Set<String> stockSymbolFromEastMoney() {
        Logger log = Logger.getLogger(HushenMarketQuotes.class);
        log.debug("HushenMarketQuotes::stockSymbolFromEastMoney enter.");
        Set<String> quotes = new HashSet<String>();
        String output = null;
        try {
            output = HttpUtil.httpQuery("http://quote.eastmoney.com/stocklist.html");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(output);
        Elements body = doc.select("div.quotebody").select("ul").select("li");
        for(Element element : body)
        {
            String symbol = element.select("a").first().attr("href").split("/|\\.")[5].substring(2, 8);
            if(SymbolFormat.isStockSymbos(symbol))
                quotes.add(symbol);
        }
        log.debug("HushenMarketQuotes::stockSymbolFromEastMoney exit.");
        return quotes;
    }
}
