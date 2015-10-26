package com.pureblue.quant.model;

public class SymbolFormat {

	public static String yahooSymbolFormat(String symbol){
		if("600000".compareTo(symbol) <= 0){
            return symbol + ".SS";
        }else{
        	return symbol + ".SZ";
        }
	}
	
	public static String tencentSymbolFormat(String symbol){
		if(symbol.length()>6){
			return symbol;
		}
		if("600000".compareTo(symbol) <= 0){
            return "sh"+symbol;
        }else{
        	return "sz"+symbol;
        }
	}

}
