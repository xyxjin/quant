package model;

public class SymbolFormat {

	@SuppressWarnings("unused")
	public static String yahooSymbolFormat(String symbol){
		if("600000".compareTo(symbol) <= 0){
            return symbol + ".SS";
        }else{
        	return symbol + ".SZ";
        }
	}
	
	@SuppressWarnings("unused")
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
