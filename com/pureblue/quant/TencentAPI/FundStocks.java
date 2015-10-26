package com.pureblue.quant.TencentAPI;

public class FundStocks implements IFund{
	String symbol;
	String date;
	String code;
	int count;
	double assets;
	double ratio;
	
	public FundStocks(String symbol, String date, String code, int count,
			double assets, double ratio) {
		super();
		this.symbol = symbol;
		this.date = date;
		this.code = code;
		this.count = count;
		this.assets = assets;
		this.ratio = ratio;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getAssets() {
		return assets;
	}

	public void setAssets(double assets) {
		this.assets = assets;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	@Override
	public String toString() {
		return "[symbol=" + symbol + ", date=" + date + ", code="
				+ code + ", count=" + count + ", assets=" + assets + ", ratio="
				+ ratio + "]";
	}

}
