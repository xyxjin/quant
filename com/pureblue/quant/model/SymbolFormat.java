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
	
	public static boolean isStockSymbos(String symbol){
		if(symbol.length() != 6){
			return false;
		}
		int id = Integer.parseUnsignedInt(symbol);
		
		if(id > 0 && id < 10000)
			return true;
		if(id > 300000 && id < 400000)
			return true;
		if(id > 600000 && id < 700000)
			return true;
		
		return false;
	}

}
