package com.pureblue.quant.main;

import com.pureblue.quant.quantAPI.YahooHistoryStock;

public class YahooHistoryExample {

    public static void main(String[] args) {
        YahooHistoryStock yahooHistoryStock = new YahooHistoryStock(100);

        yahooHistoryStock.fetchActions();

        yahooHistoryStock.pending();
    }

}
