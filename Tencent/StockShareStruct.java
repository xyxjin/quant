package Tencent;

public class StockShareStruct {
	double totalShares;
	double aShares;
	double aFlowShares;
	double aNonFlowShares;
	double hShares;
	double hFlowShares;
	double hNonFlowShares;
	
	public StockShareStruct(double totalShares, double aShares,
			double aFlowShares, double aNonFlowShares, double hShares,
			double hFlowShares, double hNonFlowShares) {
		this.totalShares = totalShares;
		this.aShares = aShares;
		this.aFlowShares = aFlowShares;
		this.aNonFlowShares = aNonFlowShares;
		this.hShares = hShares;
		this.hFlowShares = hFlowShares;
		this.hNonFlowShares = hNonFlowShares;
	}
	
}
